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

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

/**
 * @author amit bhayani
 *
 */
public abstract class ShellSelectableChannel {

    // underlying network channel
    protected SelectableChannel channel;

    public ChannelSelectionKey register(ChannelSelector selector, int ops) throws ClosedChannelException {
        SelectionKey k = channel.register(selector.selector, ops);
        ChannelSelectionKey key = new ChannelSelectionKey(this, k);
        k.attach(key);
        return key;
    }
}
