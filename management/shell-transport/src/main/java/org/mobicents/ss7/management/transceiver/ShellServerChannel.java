/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
    private static final Logger logger = Logger.getLogger(ShellServerChannel.class);

    private ChannelProvider chanProvider = null;

    protected ShellServerChannel(ChannelProvider chanProvider, AbstractSelectableChannel channel) throws IOException {
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
     * The channel returned by this method, if any, will be in non-blocking mode.
     *
     * @return The {@link ShellChannel} for the new connection, or null if no connection is available to be accepted
     * @throws java.io.IOException
     */
    public ShellChannel accept() throws IOException {
        return new ShellChannel(chanProvider, ((ServerSocketChannel) channel).accept());
    }

    /**
     * Binds the channel to a local address.
     *
     * @param address the SocketAddress to bind to
     * @throws java.io.IOException
     */
    public void bind(SocketAddress address) throws IOException {
        ((ServerSocketChannel) channel).socket().bind(address);
    }

    /**
     * Closes this channel.
     *
     * If the channel has already been closed then this method returns immediately.
     *
     * @throws java.io.IOException
     */
    public void close() throws IOException {
        ((ServerSocketChannel) channel).close();
        ((ServerSocketChannel) channel).socket().close();
    }
}
