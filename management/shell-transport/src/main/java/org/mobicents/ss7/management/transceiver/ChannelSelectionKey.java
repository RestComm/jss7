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

import java.nio.channels.SelectionKey;

/**
 * <p>
 * A token representing the registration of a {@link ShellSelectableChannel} with a {@link ChannelSelector}.
 * </p>
 * <p>
 * A selection key is created each time a channel is registered with a selector. A key remains valid until it is
 * <i>cancelled</i> by invoking its {@link #cancel cancel} method, by closing its channel, or by closing its selector.
 * </p>
 *
 * @author amit bhayani
 *
 */
public class ChannelSelectionKey {
    private ShellSelectableChannel shellSelectableChannel = null;
    private SelectionKey key = null;

    protected ChannelSelectionKey(ShellSelectableChannel shellSelectableChannel, SelectionKey k) {
        this.shellSelectableChannel = shellSelectableChannel;
        this.key = k;
    }

    /**
     * <p>
     * Returns the channel for which this key was created. This method will continue to return the channel even after the key is
     * canceled.
     * </p>
     *
     * @return This key's channel
     */
    public ShellSelectableChannel channel() {
        return shellSelectableChannel;
    }

    /**
     * Tests whether this key's channel is ready to accept a new socket connection.
     *
     * @return
     */
    public boolean isAcceptable() {
        return key.isAcceptable();
    }

    /**
     * Tests whether this key's channel is ready for reading.
     *
     * @return
     */
    public boolean isReadable() {
        return ((ShellChannel) shellSelectableChannel).isReadable();
    }

    /**
     * Tests whether this key's channel is ready for writing.
     *
     * @return
     */
    public boolean isWritable() {
        return ((ShellChannel) shellSelectableChannel).isWritable();
    }

    /**
     * <p>
     * Requests that the registration of this key's channel with its selector be canceled. Upon return the key will be invalid
     * </p>
     * <p>
     * If this key has already been canceled then invoking this method has no effect. Once canceled, a key remains forever
     * invalid.
     * </p>
     */
    public void cancel() {
        key.cancel();
    }
}
