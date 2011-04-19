/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.mtp.provider.m3ua;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.m3ua.M3UAChannel;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.M3UASelectionKey;
import org.mobicents.protocols.ss7.m3ua.M3UASelector;
import org.mobicents.protocols.ss7.m3ua.impl.tcp.TcpProvider;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;
import org.mobicents.protocols.ss7.mtp.provider.MtpListener;
import org.mobicents.protocols.ss7.mtp.provider.MtpProvider;

/**
 * base implementation over stream interface
 * 
 * @author baranowb
 * 
 */
public class Provider implements MtpProvider, Runnable {
    private String name;
	//Oleg name SUCKS!
	
	public static final String PROPERTY_LADDRESS = "mtp.address.local";
	public static final String PROPERTY_RADDRESS = "mtp.address.remote";
	
	public static final String PROPERTY_APC = "mtp.apc";
	public static final String PROPERTY_OPC = "mtp.opc"; 
	public static final String PROPERTY_NI = "mtp.ni"; 
	
    protected SocketAddress localAddress = new InetSocketAddress("127.0.0.1", 8998);;
    protected SocketAddress remoteAddress = new InetSocketAddress("127.0.0.1", 1345);
    
    private M3UAProvider provider;
    private M3UAChannel channel;
    private M3UASelector selector;
    
    private volatile boolean started = false;

    private MtpListener listener;
    private static final Logger logger = Logger.getLogger(Provider.class);
    
    //outgoing message
    private PayloadData tm;
    private volatile boolean txFailed = false;
    private volatile boolean sendReady = false;
    
    private ReentrantLock lock = new ReentrantLock();
    private Condition sendCompleted = lock.newCondition();
    
    private int opc;
    private int apc;
    private int ni;
    /**
     * 
     */
    public Provider() {
        provider = TcpProvider.provider();
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setLocalAddress(SocketAddress localAddress) {
        this.localAddress = localAddress;
    }

    public void setRemoteAddress(SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.mobicents.protocols.ss7.mtp.provider.MtpProvider#configure(java.util
     * .Properties)
     */
	public void configure(Properties p) throws ConfigurationException {
		try {
			//check for non default address
			String s = p.getProperty(PROPERTY_LADDRESS);
			if (s != null) {
				this.localAddress = parseStringAddress(s);
			}

			s = p.getProperty(PROPERTY_RADDRESS);
			if (s != null) {
				this.remoteAddress = parseStringAddress(s);
			}

			//opc/dpc
			s = p.getProperty(PROPERTY_APC);
			if(s == null)
			{
				throw new ConfigurationException("APC must be specified with: "+PROPERTY_APC+" property.");
			}
			this.apc = Integer.parseInt(s);
			
			s = p.getProperty(PROPERTY_OPC);
			if(s == null)
			{
				throw new ConfigurationException("OPC must be specified with: "+PROPERTY_OPC+" property.");
			}
			this.opc = Integer.parseInt(s);
			
			s = p.getProperty(PROPERTY_NI);
			if(s == null)
			{
				throw new ConfigurationException("NI must be specified with: "+PROPERTY_NI+" property.");
			}
			this.ni = Integer.parseInt(s);
			
			
			
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#send(byte[])
     */
    public void send(byte[] msu) throws IOException {
        lock.lock();
        try {
            PayloadData msg = (PayloadData) provider.getMessageFactory().createMessage(
                    MessageClass.TRANSFER_MESSAGES, 
                    MessageType.PAYLOAD);
            ProtocolData data = provider.getParameterFactory().createProtocolData(0, msu);
            msg.setData(data);
            try {
            	tm = msg;
                sendReady = true;
                txFailed = false;
                sendCompleted.await(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new IOException("Interrupted");
            }
            
            //timed out?
            if (sendReady) {                
                throw new IOException("Wait timeout");
            }
            
            if (this.txFailed) {
                throw new IOException("Can not send message");
            }
        } finally {
            lock.unlock();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.protocols.ss7.mtp.provider.MtpProvider#start()
     */
    public void start() throws StartFailedException {
        if (this.started) {
            return;
        }
        logger.info("Starting M3UA provider");
        this.started = true;
        try {
            logger.info("Starting M3UA connector");
            new Thread(new Connector(this), "M3UAConnector").start();
        } catch (Exception e) {
            throw new StartFailedException(e);
        } 
    }

    public void setOriginalPointCode(int opc) {
        this.opc = opc;
    }

    public void setAdjacentPointCode(int apc) {
        this.apc = apc;
    }

    public int getAdjacentPointCode() {
        return apc;
    }

    public int getOriginalPointCode() {
        return opc;
    }

	public void setNetworkIndicator(int ni) {
		this.ni = ni;
	}

	public int getNetworkIndicator() {
		return this.ni;
	}
    public void setMtpListener(MtpListener listener) {
        this.listener = listener;
    }

    public void stop() {
        started = false;
        try {
            channel.close();
        } catch (IOException e) {
        }
    }

    private void receive(M3UAChannel channel) {
        //reading message from M3UA channel
        M3UAMessage msg = null;
        try {
            msg = channel.receive();
            logger.info("Receive " + msg);
        } catch (IOException e) {
            logger.error("Unable to read message, caused by ", e);
            //TODO disconnect channel?
            return;
        }
        
        //determine type of the message
        switch (msg.getMessageClass()) {
            case MessageClass.TRANSFER_MESSAGES :
                //deliver transfer message to the upper layer
                PayloadData message = (PayloadData) msg;
                if (listener != null) {
                    listener.receive(message.getData().getMsu());
                }
                break;
            default :
                logger.info("Unable to handle message :" + msg);
        }
    }

    private void send(M3UAChannel channel) {
        if (!sendReady) {
            return;
        }
        
        try {
            channel.send(tm);
            sendReady = false;
            
            lock.lock();
            try {
                this.sendCompleted.signal();
            } finally {
                lock.unlock();
            }
        } catch (IOException e) {
            this.txFailed = true;
        }
    }

    public void run() {
        while (started) {
            try {
                //selecting channels (only one now)
                Collection<M3UASelectionKey> selection = selector.selectNow();
                for (M3UASelectionKey key : selection) {
                    //obtain channel ready for IO
                    M3UAChannel chann = (M3UAChannel) key.channel();
                    
                    //do receiving part
                    if (key.isReadable()) {
                        receive(chann);
                    }
                    
                    //do sending part
                    if (key.isWritable()) {
                        send(chann);
                    }
                }
            } catch (IOException e) {
                //TODO terminate and reconnect?
            }
        }
    }

    public boolean isLinkUp() {
        return true; //...... Oleg fix this....
    }
    
    /**
     * Starts connection creation in the background.
     */
    private class Connector implements Runnable {
        private Runnable worker;
        
        public Connector(Runnable worker) {
            this.worker = worker;
        }
        
        public void run() {
            boolean connected = false;
            while (!connected && started) {
                try {
                    selector = provider.openSelector();            
                    //opening channel, bind it local address and connect 
                    //to the remote address
                    channel = provider.openChannel();
                    channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            
                    logger.info("Binding M3UA channel to " + localAddress);
                    channel.bind(localAddress);
            
                    logger.info("Connecting M3UA channel to " + remoteAddress);
                    channel.connect(remoteAddress);
            
                    //wait while connection will be established
                    while (!channel.finishConnect()) {
                        synchronized (this) {
                            wait(10);
                        }
                    }
            
                    //run main thread;
                    logger.info("Connected M3UA channel to " + remoteAddress);
                    started = true;
                    new Thread(worker).start();
                    connected = true;
                } catch (Exception e) {
                    logger.warn("Can not connect to " + remoteAddress + ":" + e.getMessage());
                    try {
                        channel.close();
                    } catch (IOException ie) {
                    }
                    //wait 5 second before reconnect
                    try {
                        synchronized(this) {wait(5000);}
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        }
    }
    
    
    private static InetSocketAddress parseStringAddress(String s) {
		String[] d = s.split(":");

		return new InetSocketAddress(d[0], Integer.parseInt(d[1]));
	}

}
