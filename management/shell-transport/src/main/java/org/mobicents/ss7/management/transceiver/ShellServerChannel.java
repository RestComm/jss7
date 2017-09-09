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
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
        SocketChannel newChannel = ((ServerSocketChannel) channel).accept();
        if (newChannel == null)
            return null;

        return new ShellChannelExt(chanProvider, newChannel);
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
