package org.mobicents.ss7.management.transceiver;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ShellChannel extends ShellSelectableChannel {

    // Queue for incoming messages
    protected ConcurrentLinkedQueue<Message> rxQueue = new ConcurrentLinkedQueue<Message>();
    // Queue for outgoing messages
    protected ConcurrentLinkedQueue<Message> txQueue = new ConcurrentLinkedQueue<Message>();

    // receiver buffer
    private ByteBuffer rxBuffer = ByteBuffer.allocateDirect(8192);
    // transmittor buffer
    private ByteBuffer txBuffer = ByteBuffer.allocateDirect(8192);

    // provider instance
    private ChannelProvider provider;

    protected ShellChannel(ChannelProvider provider,
            AbstractSelectableChannel channel) throws IOException {
        this.channel = channel;
        this.channel.configureBlocking(false);
        this.provider = provider;

        // clean transmission buffer
        txBuffer.clear();
        txBuffer.rewind();
        txBuffer.flip();

        // clean receiver buffer
        rxBuffer.clear();
        rxBuffer.rewind();
        rxBuffer.flip();
    }

    public static ShellChannel open(ChannelProvider provider) throws IOException {
        return new ShellChannel(provider, SocketChannel.open());
    }

    public Message receive() throws IOException {
        return rxQueue.poll();
    }

    public void send(Message message) throws IOException {
        txQueue.offer(message);
    }

    protected boolean isReadable() {
        return !rxQueue.isEmpty();
    }

    protected boolean isWritable() {
        return txQueue.isEmpty();
    }

    protected void doRead() throws IOException {
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

            Message message = this.provider.getMessageFactory().createMessage(
                    rxBuffer);
            if (message != null) {
                rxQueue.offer(message);
            }
        }
    }

    protected void doWrite() throws IOException {
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

    public void bind(SocketAddress address) throws IOException {
        ((SocketChannel) channel).socket().bind(address);
    }

    public boolean connect(SocketAddress remote) throws IOException {
        return ((SocketChannel) channel).connect(remote);
    }

    public boolean finishConnect() throws IOException {
        return ((SocketChannel) channel).finishConnect();
    }

    public boolean isConnected() {
        return ((SocketChannel) channel).isConnected();
    }

    public boolean isConnectionPending() {
        return ((SocketChannel) channel).isConnectionPending();
    }

    public void close() throws IOException {
        ((SocketChannel) channel).close();
        ((SocketChannel) channel).socket().close();
    }

}
