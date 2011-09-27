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

package org.mobicents.protocols.ss7.m3ua.impl.tcp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

import org.mobicents.protocols.ss7.m3ua.impl.M3UAChannelImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;

/**
 * Implements M3UA channel over TCP/IP
 * @author amit bhayani
 * @author kulikov
 */
public class TcpChannel extends M3UAChannelImpl {
    //receiver buffer
    private ByteBuffer rxBuffer = ByteBuffer.allocateDirect(8192);
    //transmittor buffer
    private ByteBuffer txBuffer = ByteBuffer.allocateDirect(8192);

    //provider instance
    private TcpProvider provider;
    
    
    //the address to which the socket is connected.
    private InetAddress inetAddress = null;
    
    //Returns the remote port to which this socket is connected.
    private int port;
    
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
        
        this.inetAddress = ((SocketChannel)channel).socket().getInetAddress();
        this.port = ((SocketChannel)channel).socket().getPort();
    }
    
    public InetAddress getInetAddress(){
        return this.inetAddress;
    }
    
    public int getPort(){
        return this.port;
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
            txBuffer.clear();
            while(!txQueue.isEmpty()){
                //Lets read all the messages in txQueue and add to txBuffer
                M3UAMessageImpl msg = txQueue.poll();
                msg.encode(txBuffer);
            }
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
