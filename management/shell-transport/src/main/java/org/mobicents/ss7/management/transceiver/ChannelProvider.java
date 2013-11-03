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
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 * <p>
 * Service-provider class for {@link ChannelSelector} and selectable {@link ShellSelectableChannel}
 * </p>
 * <p>
 * A given invocation of the Java virtual machine maintains a single system-wide default provider instance, which is returned by
 * the {@link #provider() provider} method. The first invocation of that method will locate the default provider as specified
 * below.
 * </p>
 * <p>
 * The system-wide default provider is used by the static <tt>open</tt> methods of the {@link ShellChannel#open ShellChannel},
 * {@link ShellServerChannel#open ShellServerChannel}, and {@link ChannelSelector#open ChannelSelector} classes.
 * </p>
 *
 * <p>
 * A program may make use of system-wide default provider to get instance of {@link MessageFactory} by calling
 * <tt>getMessageFactory</tt>
 * </p>
 *
 * @author amit bhayani
 *
 */
public class ChannelProvider {

    private MessageFactory messageFactory = new MessageFactory();
    private static ChannelProvider channelProvider = new ChannelProvider();

    protected ChannelProvider() {

    }

    /**
     * Returns the system-wide default selector provider for this invocation of the Java virtual machine.
     *
     * @return Returns the system-wide default selector provider.
     */
    public static ChannelProvider provider() {
        return channelProvider;
    }

    /**
     * Opens a shell channel
     *
     * @return The new channel
     * @throws IOException
     */
    public ShellChannel openChannel() throws IOException {
        return new ShellChannel(this, SocketChannel.open());
    }

    /**
     * Opens a server channel
     *
     * @return The new channel
     * @throws IOException
     */
    public ShellServerChannel openServerChannel() throws IOException {
        return new ShellServerChannel(this, ServerSocketChannel.open());
    }

    /**
     * Opens a channel selector
     *
     * @return New selector
     * @throws IOException
     */
    public ChannelSelector openSelector() throws IOException {
        return new ChannelSelector(SelectorProvider.provider().openSelector());
    }

    /**
     * Returns the system-wide default message factory for this invocation of the Java virtual machine.
     *
     * @return Returns the system-wide default message factory
     */
    public MessageFactory getMessageFactory() {
        return messageFactory;
    }
}
