package org.mobicents.ss7.management.transceiver;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;

import org.apache.log4j.Logger;

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

    public static ShellServerChannel open(ChannelProvider chanProvider)
            throws IOException {
        return new ShellServerChannel(chanProvider, ServerSocketChannel.open());
    }

    public ShellChannel accept() throws IOException {
        return new ShellChannel(chanProvider, ((ServerSocketChannel) channel)
                .accept());
    }

    public void bind(SocketAddress address) throws IOException {
        ((ServerSocketChannel) channel).socket().bind(address);
    }

    public void close() throws IOException {
        ((ServerSocketChannel) channel).close();
        ((ServerSocketChannel) channel).socket().close();
    }
}
