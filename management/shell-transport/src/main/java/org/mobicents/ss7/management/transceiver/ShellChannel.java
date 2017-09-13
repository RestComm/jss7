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

package org.mobicents.ss7.management.transceiver;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A selectable channel for message connecting sockets.
 *
 * @author amit bhayani
 *
 */
public class ShellChannel extends ShellSelectableChannel {

    // Queue for incoming messages
    protected ConcurrentLinkedQueue<Message> rxQueue = new ConcurrentLinkedQueue<Message>();
    // Queue for outgoing messages
    protected ConcurrentLinkedQueue<Message> txQueue = new ConcurrentLinkedQueue<Message>();

    public static final int BYTE_BUFFER_SIZE = 8192;

    // receiver buffer
    private ByteBuffer rxBuffer = ByteBuffer.allocateDirect(BYTE_BUFFER_SIZE);
    // transmittor buffer
    private ByteBuffer txBuffer = ByteBuffer.allocateDirect(BYTE_BUFFER_SIZE);

    // provider instance
    private ChannelProvider provider;
    private MessageFactory messageFactory;

    protected ShellChannel(ChannelProvider provider, AbstractSelectableChannel channel) throws IOException {
        this.channel = channel;
        this.channel.configureBlocking(false);
        this.provider = provider;
        this.messageFactory = this.provider.createMessageFactory();

        // clean transmission buffer
        txBuffer.clear();
        txBuffer.rewind();
        txBuffer.flip();

        // clean receiver buffer
        rxBuffer.clear();
        rxBuffer.rewind();
        rxBuffer.flip();
    }

    /**
     * Opens a socket channel.
     *
     * <p>
     * The new channel is created by invoking the {@link ChannelProvider#openChannel openChannel} method of the system-wide
     * default {@link ChannelProvider} object.
     * </p>
     *
     * @return A new channel
     * @throws IOException If an I/O error occurs
     */
    public static ShellChannel open() throws IOException {
        return ChannelProvider.provider().openChannel();
    }

    /**
     * Read the {@link Message} if available, null otherwise
     *
     * @return
     * @throws IOException
     */
    public Message receive() throws IOException {
        if (!this.isConnected()) {
            throw new ClosedChannelException();
        }

        return rxQueue.poll();
    }

    /**
     * Send the {@link Message} to underlying socket
     *
     * @param message
     * @throws IOException
     */
    public void send(Message message) throws IOException {
        if (!this.isConnected()) {
            throw new ClosedChannelException();
        }
        txQueue.offer(message);
        // Below code was introduced for JSSSEVEN-163. But its not that simple and needs more detailed work
        // int msgLength = message.getLength();
        // byte[] orgData = message.getData();
        //
        // // reduce header size
        // int maximumByteBufferSize = BYTE_BUFFER_SIZE - MessageFactory.MESSAGE_HEADER_SIZE;
        // int numberOfBuckets = msgLength / maximumByteBufferSize + (msgLength % maximumByteBufferSize == 0 ? 0 : 1);
        //
        // for (int count = 0; count < numberOfBuckets; count++) {
        // byte[] tempData;
        // if (count == (numberOfBuckets - 1)) {
        // tempData = new byte[msgLength - (maximumByteBufferSize * count)];
        // } else {
        // tempData = new byte[maximumByteBufferSize];
        // }
        //
        // System.arraycopy(orgData, (maximumByteBufferSize * count), tempData, 0, tempData.length);
        // Message tempMessage = new Message(tempData);
        // txQueue.offer(tempMessage);
        // }
    }

    public void sendImmediate(Message message) throws IOException {
        txBuffer.clear();
        txBuffer.rewind();

        message.encode(txBuffer);
        txBuffer.flip();
        ((SocketChannel) channel).write(txBuffer);
    }

    /**
     * Tells whether or not {@link Message} is available for read
     *
     * @return
     */
    protected boolean isReadable() {
        return !rxQueue.isEmpty();
    }

    /**
     * Tells whether or not {@link Message} is available for write
     *
     * @return
     */
    protected boolean isWritable() {
        return txQueue.isEmpty();
    }

    public void doRead() throws IOException {
        // clean rx buffer
        rxBuffer.clear();

        // reading data from socketChannel
        int len = ((SocketChannel) channel).read(rxBuffer);
        if (len == -1) {
            // socketChannel closed by remote peer
            ((SocketChannel) channel).close();
            rxQueue.clear();

            return;
        }

        rxBuffer.flip();

        // split stream on to the messages
        while (rxBuffer.position() < rxBuffer.limit()) {
            // try to read message

            Message message = messageFactory.createMessage(rxBuffer);
            if (message != null) {
                rxQueue.offer(message);
            }
        }
    }

    public void doWrite() throws IOException {
        if (txBuffer.hasRemaining()) {
            ((SocketChannel) channel).write(txBuffer);
        } else if (!txQueue.isEmpty()) {
            Message msg = txQueue.poll();

            txBuffer.clear();
            txBuffer.rewind();

            msg.encode(txBuffer);
            txBuffer.flip();
            ((SocketChannel) channel).write(txBuffer);
        }
    }

    /**
     * Bind the channel to given address
     *
     * @param address
     * @throws IOException
     */
    public void bind(SocketAddress address) throws IOException {
        ((SocketChannel) channel).socket().bind(address);
    }

    /**
     * Connect the channel to given address
     *
     * @param remote
     * @return
     * @throws IOException
     */
    public boolean connect(SocketAddress remote) throws IOException {
        return ((SocketChannel) channel).connect(remote);
    }

    /**
     * Finishes the process of connecting a channel.
     *
     * A non-blocking connection operation is initiated by invoking its connect method. Once the connection is established, or
     * the attempt has failed, the channel will become conectable and this method may be invoked to complete the connection
     * sequence. If the connection operation failed then invoking this method will cause an appropriate IOException to be
     * thrown.
     *
     *
     * @return true if, and only if, this channel is now connected
     * @throws java.io.IOException
     */
    public boolean finishConnect() throws IOException {
        return ((SocketChannel) channel).finishConnect();
    }

    /**
     * Tells whether or not this channel is connected.
     *
     * @return
     */
    public boolean isConnected() {
        return ((SocketChannel) channel).isConnected();
    }

    /**
     * See {@link java.net.Socket#getRemoteSocketAddress()}
     *
     * @return
     * @throws IOException
     */
    public SocketAddress getRemoteAddress() {
        return ((SocketChannel) channel).socket().getRemoteSocketAddress();
    }

    /**
     * Tells whether or not a connection operation is in progress on this channel.
     *
     * @return true if, and only if, a connection operation has been initiated on this channel but not yet completed by invoking
     *         the finishConnect method
     */
    public boolean isConnectionPending() {
        return ((SocketChannel) channel).isConnectionPending();
    }

    /**
     * Closes this channel.
     *
     * If the channel has already been closed then this method returns immediately.
     *
     * @throws java.io.IOException
     * @throws IOException
     */
    public void close() throws IOException {
        ((SocketChannel) channel).close();
        ((SocketChannel) channel).socket().close();
    }

}
