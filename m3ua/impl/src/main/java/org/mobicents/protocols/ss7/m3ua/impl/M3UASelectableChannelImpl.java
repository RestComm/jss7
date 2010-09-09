/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
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
