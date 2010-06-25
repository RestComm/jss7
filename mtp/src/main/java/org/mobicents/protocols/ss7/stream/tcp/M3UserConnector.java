/**
 * 
 */
package org.mobicents.protocols.ss7.stream.tcp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.Mtp3Listener;
import org.mobicents.protocols.ss7.stream.HDLCHandler;
import org.mobicents.protocols.ss7.stream.InterceptorHook;
import org.mobicents.protocols.ss7.stream.LinkStateProtocol;
import org.mobicents.protocols.ss7.stream.StreamForwarder;
import org.mobicents.protocols.ss7.stream.tlv.LinkStatus;
import org.mobicents.protocols.ss7.stream.tlv.TLVInputStream;
import org.mobicents.protocols.ss7.stream.tlv.TLVOutputStream;
import org.mobicents.protocols.ss7.stream.tlv.Tag;

/**
 * @author baranowb
 * 
 */
public class M3UserConnector extends MTPProviderImpl implements Runnable , StreamForwarder{
	public static final String _PROPERTY_IP = "server.ip";
	public static final String _PROPERTY_PORT = "server.port";

	private static final Logger logger = Logger.getLogger(M3UserConnector.class);

	private Properties properties = new Properties();

	private boolean runnable;
	private LinkStateProtocol linkStateProtocol = new LinkStateProtocol();
	// ////////////////////
	// Server variables //
	// ////////////////////

	private String serverAddress = "127.0.0.1";
	private int serverPort = 1345;
	// we need this since lower layer is very time sensitivie, lets deliver on
	// different thread.
	// private ExecutorService executor = Executors.newFixedThreadPool(5);
	// client part for streaming
	private Selector connectSelector;
	private Selector writeSelector;
	private Selector readSelector;
	private ByteBuffer readBuff = ByteBuffer.allocate(8192);
	//private ByteBuffer txBuff = ByteBuffer.allocate(8192);
	private ArrayList<ByteBuffer> dataToSend = new ArrayList<ByteBuffer>();
	private ExecutorService streamExecutor = Executors.newSingleThreadExecutor();
	private Future streamFuture;
	private boolean connected = false;
	private SocketChannel socketChannel;


	public M3UserConnector(Properties properties) {
		this();
		if(properties!=null)
			this.properties.putAll(properties);
		
		
	}
	public M3UserConnector() {
		super();

		linkStateProtocol.setStreamForwarder(this);

	}
	@Override
	public void start() throws StartFailedException, IllegalStateException {

		if (this.streamFuture != null) {
			throw new IllegalStateException("Provider is already started!");
		}
		// wont send empty buffer
		//this.txBuff.limit(0);
		linkStateProtocol.reset();
		readProperties();
		// initiateConnection();
		this.runnable = true;
		this.streamFuture = streamExecutor.submit(this);
		
	}

	public void stop() throws IllegalStateException {
		
		if (this.streamFuture == null) {
			// throw new IllegalStateException("Provider already stoped!");
			return;
		} else {
			this.runnable = false;
			if (streamFuture != null) {
				streamFuture.cancel(false);
				streamFuture = null;
			}
			
			if(this.connectSelector!=null)
			{
				try{
					this.connectSelector.wakeup();
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
				
			disconnect();
			
		}
		//FIXME: add remove listeners?

	}

	/**
	 * @return the serverAddress
	 */
	public String getRemoteAddress() {
		return serverAddress;
	}
	/**
	 * @param serverAddress the serverAddress to set
	 */
	public void setRemoteAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	/**
	 * @return the serverPort
	 */
	public int getRemotePort() {
		return serverPort;
	}
	/**
	 * @param serverPort the serverPort to set
	 */
	public void setRemotePort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.StreamForwarder#setInterceptorHook(org.mobicents.protocols.ss7.stream.InterceptorHook)
	 */
	public void setInterceptorHook(InterceptorHook ih) {
		this.linkStateProtocol.setInterceptorHook(ih);
		
	}
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.StreamForwarder#streamData(java.nio.ByteBuffer)
	 */
	public void streamData(ByteBuffer data) {
		//FIXME: Amit/Oleg this has to be changed to something else!
		//this.txBuff.put(data);
		ByteBuffer toSendData = LinkStateProtocol.copyToPosition(data);
		this.dataToSend.add(toSendData);
		
	}
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.StreamForwarder#getLocalAddress()
	 */
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.StreamForwarder#setMtp3(org.mobicents.protocols.ss7.mtp.Mtp3)
	 */
	public void setMtp3(Mtp3 mtp) {
		throw new UnsupportedOperationException();
		
	}
	public String getLocalAddress() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.StreamForwarder#getLocalPort()
	 */
	public int getLocalPort() {
		throw new UnsupportedOperationException();
	}
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.StreamForwarder#setLocalAddress(java.lang.String)
	 */
	public void setLocalAddress(String address) throws UnknownHostException {
		throw new UnsupportedOperationException();
		
	}
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.stream.StreamForwarder#setLocalPort(int)
	 */
	public void setLocalPort(int port) {
		throw new UnsupportedOperationException();
		
	}
	
	public LinkStateProtocol getLinkStateProtocol()
	{
			return this.linkStateProtocol;
	}
	
	private void readProperties() {
		serverPort = Integer.parseInt(properties.getProperty(_PROPERTY_PORT, "" + serverPort));
		serverAddress = properties.getProperty(_PROPERTY_IP, "" + serverAddress);

	}

	public void run() {


			while (runnable) {

				try {
					if (!connected) {
						initiateConnection();
						continue;

					}
					Iterator selectedKeys = null;

					// else we try I/O ops.

					if (this.readSelector.selectNow() > 0) {
						selectedKeys = this.readSelector.selectedKeys().iterator();
						// operate on keys set

						performKeyOperations(selectedKeys);

					}

					if (this.writeSelector.selectNow() > 0) {

						selectedKeys = this.writeSelector.selectedKeys().iterator();
						// operate on keys set

						performKeyOperations(selectedKeys);

					}
					// synchronized (this.writeSelector) {
					// this.writeSelector.wait(5);
					// }
					// connected = socketChannel.isConnected();

				} catch(java.nio.channels.ClosedSelectorException e)
				{
					e.printStackTrace();
					disconnect();
				}catch (Exception ee) {
					ee.printStackTrace();
					logger.error("Something failed: ",ee);
				}
			}
			disconnect();
			

	}

	private void initiateConnection() {
		
			try {

				
				
				while(!connected && runnable)
				{
					this.socketChannel = SocketChannel.open();
					this.socketChannel.configureBlocking(false);
					this.connectSelector = SelectorProvider.provider().openSelector();

					this.socketChannel.register(this.connectSelector, SelectionKey.OP_CONNECT);
					if(logger.isInfoEnabled())
					{
						logger.info("Trying connection to: "+this.serverAddress+":"+this.serverPort);
					}
					// Kick off connection establishment: must be done after each connector.select(); !
					this.socketChannel.connect(new InetSocketAddress(this.serverAddress, this.serverPort));

					if (this.connectSelector.select() > 0) {
						Set<SelectionKey> selectedKeys=this.connectSelector.selectedKeys();
						try{
							performKeyOperations(selectedKeys.iterator());
						}catch(java.net.ConnectException ce)
						{
							//this is ok
							if(logger.isDebugEnabled())
							{
								logger.debug("Connection failure:",ce);
							}
							//propably fail to connect, lets wait 5s
							waitReconnect();
						}
						catch(IOException ioe)
						{
							ioe.printStackTrace();
							//propably fail to connect, lets wait 5s
							waitReconnect();
						}
					}else
					{
						//sleep
						//waitReconnect();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				logger.error("Failed to connect: ",e);
				disconnect();
			}
		
	}
	
	private void waitReconnect()
	{
		try {
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void performKeyOperations(Iterator<SelectionKey> selectedKeys) throws IOException {
		while (selectedKeys.hasNext()) {
			SelectionKey key =  selectedKeys.next();
			//THIS MUST BE PRESENT!
			selectedKeys.remove();

			if (!key.isValid()) {
				// handle disconnect here?
				logger.error("Key has become invalid: " + key);
				continue;
			}

			// Check what event is available and deal with it
			if (key.isReadable()) {
				this.read(key);
			} else if (key.isWritable()) {
				this.write(key);
			} else if(key.isConnectable())
			{
				this.connect(key);
			}
		}

	}
	
	private void connect(SelectionKey key) throws IOException 
	{
		//here socket wants to connect
		SocketChannel socketChannel = (SocketChannel) key.channel();
		//this will throw exception if fail happens
		if(!socketChannel.finishConnect())
		{
			throw new IOException("Not in correct time, will retry connection shortly");
		}
		connected = true;
		this.writeSelector = SelectorProvider.provider().openSelector();
		this.readSelector = SelectorProvider.provider().openSelector();
		this.socketChannel.register(this.readSelector, SelectionKey.OP_READ);
		this.socketChannel.register(this.writeSelector, SelectionKey.OP_WRITE);
		if(logger.isDebugEnabled())
        {
        	logger.debug("Connected to server,  "+this.socketChannel.socket().getRemoteSocketAddress()+", local connection "+this.socketChannel.socket().getLocalAddress()+":"+this.socketChannel.socket().getLocalPort());
		}
		this.linkStateProtocol.transportUp();
		
	}
	
	private void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		// FIXME: we must ensure that we have whole frame here?
		// Clear out our read buffer so it's ready for new data
		this.readBuff.clear();

		// Attempt to read off the channel
		int numRead = -1;
		try {
			numRead = socketChannel.read(this.readBuff);

		} catch (IOException e) {
			// The remote forcibly closed the connection, cancel
			// the selection key and close the channel.
			// if(logger.isDebugEnabled())
			// {
			e.printStackTrace();
			// }
			handleClose(key);
			return;
		}

		if (numRead == -1) {
			// Remote entity shut the socket down cleanly. Do the
			// same from our end and cancel the channel.
			handleClose(key);
			return;
		}else if(numRead == this.readBuff.capacity())
		{
			return;
		}

		this.readBuff.flip();
		if(logger.isDebugEnabled())
        {
        	logger.debug("Received data: " + this.readBuff);
		 }
		try{
			this.linkStateProtocol.streamDataReceived(readBuff);
		}catch(Exception b)
		{
			b.printStackTrace();
			
		}


	}
	private void write(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		// Write until there's not more data ?
		while (this.dataToSend.size() > 0) {
			ByteBuffer txBuff = this.dataToSend.get(0);
			while (txBuff.remaining() > 0) {
				int sent = socketChannel.write(txBuff);
				if (sent == 0) {
					// buffer is filled?
					// lets move content thats left
					return;
				}

			}
			this.dataToSend.remove(0);
		}

	}

	private void handleClose(SelectionKey key) {
		if(logger.isDebugEnabled())
        {
        	logger.debug("Handling key close operations: " + key);
		 }
		
		try {
			
			disconnect();
		} finally {
			// linkDown();
			// connected = false;
			// synchronized (this.hdlcHandler) {
			synchronized (this.writeSelector) {
				// this is to ensure buffer does not have any bad data.
				// this.txBuffer.clear();
				

			}
		}
		return;
	}
	
	private void disconnect() {
		this.linkStateProtocol.transportDown();
		this.linkStateProtocol.reset();
		//this.txBuff.clear();
		//this.txBuff.limit(0);
		this.dataToSend.clear();
		if (this.socketChannel != null) {
			try {
				this.socketChannel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.socketChannel = null;

		if (this.connectSelector != null) {
			try {
				this.connectSelector.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (this.readSelector != null ) {
			try {
				this.readSelector.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (this.writeSelector != null ) {
			try {
				this.writeSelector.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.connected = false;

	}
	
	
	
	


	//////////////////////
	// Provider methods //
	//////////////////////
	public void addMtp3Listener(Mtp3Listener lst) {

		if (lst == null) {
			throw new NullPointerException("Listener must not be null.");
		}
		this.linkStateProtocol.addMtp3Listener(lst);
	}

	public void removeMtp3Listener(Mtp3Listener lst) {
		if (lst == null) {
			throw new NullPointerException("Listener must not be null.");
		}
		this.linkStateProtocol.removeMtp3Listener(lst);
	}

	public void send(byte[] msg) throws IOException {
		this.linkStateProtocol.streamDataToSend(msg);

	}

	public boolean isConnected() {
		return this.connected;
	}
	
}
