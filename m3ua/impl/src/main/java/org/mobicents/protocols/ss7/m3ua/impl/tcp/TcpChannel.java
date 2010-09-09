/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, encode to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.protocols.ss7.m3ua.impl.tcp;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAChannelImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;

/**
 * Implements M3UA channel over TCP/IP
 * @author kulikov
 */
public class TcpChannel extends M3UAChannelImpl {
    //receiver buffer
    private ByteBuffer rxBuffer = ByteBuffer.allocateDirect(8192);
    //transmittor buffer
    private ByteBuffer txBuffer = ByteBuffer.allocateDirect(8192);

    //provider instance
    private TcpProvider provider;
    
    /**
     * Creates new channel.
     * 
     * @param provider the provider instance.
     * @param channel the underlying socket channel.
     * @throws java.io.IOException
     */
    protected TcpChannel(TcpProvider provider, AbstractSelectableChannel channel) throws IOException {
        super(channel);
        this.provider = provider;
        
        //clean transmission buffer
        txBuffer.clear();
        txBuffer.rewind();
        txBuffer.flip();
        
        //clean receiver buffer
        rxBuffer.clear();
        rxBuffer.rewind();
        rxBuffer.flip();
    }
    
    /**
     * Opens this channel
     * 
     * @return the new channel
     * @throws java.io.IOException
     */
    public static TcpChannel open(TcpProvider provider) throws IOException {
        return new TcpChannel(provider, SocketChannel.open());
    }
    
    @Override
    protected void doRead() throws IOException {
        //clean rx buffer
        rxBuffer.clear();
        
        //reading data from socketChannel 
        int len = ((SocketChannel)channel).read(rxBuffer);
        if (len == -1) {
            //socketChannel closed by remote peer
            ((SocketChannel)channel).close();            
            rxQueue.clear();      
            
            return;
        }
        
        rxBuffer.flip();

        //split stream on to the messages
        while (rxBuffer.position() < rxBuffer.limit()) {
            //try to read message
            M3UAMessageImpl message = ((MessageFactoryImpl)provider.getMessageFactory()).createMessage(rxBuffer);
            if (message != null) {
                rxQueue.offer(message);
            }
        }
    }

    @Override
    protected void doWrite() throws IOException {
        if (txBuffer.hasRemaining()) {
            ((SocketChannel)channel).write(txBuffer);
        } else if (!txQueue.isEmpty()) {
            M3UAMessageImpl msg = txQueue.poll();
            
            txBuffer.clear();
            txBuffer.rewind();
            
            msg.encode(txBuffer);
            txBuffer.flip();
            ((SocketChannel)channel).write(txBuffer);
        }
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAChannel#bind(java.net.SocketAddress);
     */
    public void bind(SocketAddress address) throws IOException {
        ((SocketChannel)channel).socket().bind(address);
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAChannel#connect(java.net.SocketAddress) 
     */
    public boolean connect(SocketAddress remote) throws IOException {
        return ((SocketChannel)channel).connect(remote);
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAChannel#finishConnect() 
     */
    public boolean finishConnect() throws IOException {
        return ((SocketChannel)channel).finishConnect();
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAChannel#isConnected() 
     */
    public boolean isConnected() {
        return ((SocketChannel)channel).isConnected();
    }

    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAChannel#isConnectionPending() 
     */
    public boolean isConnectionPending() {
        return ((SocketChannel)channel).isConnectionPending();
    }
    
    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UAChannel#close() 
     */
    public void close() throws IOException {
        ((SocketChannel)channel).close();        
        ((SocketChannel)channel).socket().close();
    }

}
