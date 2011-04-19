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

package org.mobicents.protocols.ss7.m3ua.impl;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import org.mobicents.protocols.ss7.m3ua.M3UASelectableChannel;
import org.mobicents.protocols.ss7.m3ua.M3UASelectionKey;
import org.mobicents.protocols.ss7.m3ua.M3UASelector;

/**
 * Imlements multiplexable channel.
 * 
 * @author kulikov
 */
public abstract class M3UASelectableChannelImpl implements M3UASelectableChannel {
    
    //underlying network channel
    protected SelectableChannel channel;

    /**
     * (Non Java-doc.)
     * 
     * @see  org.mobicents.protocols.ss7.m3ua.M3UASelectableChannel#register(org.mobicents.protocols.ss7.m3ua.M3UASelector, int);
     */
    public M3UASelectionKey register(M3UASelector selector, int ops) throws ClosedChannelException {
        SelectionKey k = channel.register(((M3UASelectorImpl)selector).selector, ops);
        M3UASelectionKeyImpl key = new M3UASelectionKeyImpl(this, k);
        k.attach(key);
        return key;
    }
    
}
