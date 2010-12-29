package org.mobicents.ss7.management.transceiver;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

import org.apache.log4j.Logger;

/**
 * A selectable channel for Message listening sockets.
 * 
 * @author amit bhayani
 * 
 */
public class ShellServerChannel extends ShellSelectableChannel {
    private static final Logger logger = Logger
            .getLogger(ShellServerChannel.class);

    private ChannelProvider chanProvider = null;

    protected ShellServerChannel(ChannelProvider chanProvider,
            AbstractSelectableChannel channel) throws IOException {
        this.channel = channel;
        this.channel.configureBlocking(false);
        this.chanProvider = chanProvider;
    }

    public static ShellServerChannel open() throws IOException {
        return ChannelProvider.provider().openServerChannel();
    }

    /**
     * Accepts a connection made to this channel's socket.
     * 
     * The channel returned by this method, if any, will be in non-blocking
     * mode.
     * 
     * @return The {@link ShellChannel} for the new connection, or null if no
     *         connection is available to be accepted
     * @throws java.io.IOException
     */
    public ShellChannel accept() throws IOException {
        return new ShellChannel(chanProvider, ((ServerSocketChannel) channel)
                .accept());
    }

    /**
     * Binds the channel to a local address.
     * 
     * @param address
     *            the SocketAddress to bind to
     * @throws java.io.IOException
     */
    public void bind(SocketAddress address) throws IOException {
        ((ServerSocketChannel) channel).socket().bind(address);
    }

    /**
     * Closes this channel.
     * 
     * If the channel has already been closed then this method returns
     * immediately.
     * 
     * @throws java.io.IOException
     */
    public void close() throws IOException {
        ((ServerSocketChannel) channel).close();
        ((ServerSocketChannel) channel).socket().close();
    }
}
