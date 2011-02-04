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

import org.mobicents.protocols.ss7.m3ua.*;
import java.nio.channels.SelectionKey;

/**
 * Implements registration key.
 * 
 * @author amit bhayani
 * @author kulikov
 */
public class M3UASelectionKeyImpl implements M3UASelectionKey {
    //registered channel
    private M3UASelectableChannelImpl channel;
    //NIO selection key which represent selection of actual network channel
    private SelectionKey key;
    
    private Object object;
    
    /**
     * Constructs new selection key.
     * 
     * @param channel the registered M3UA channel.
     * @param k the NIO selection key representing network selection.
     */
    protected M3UASelectionKeyImpl(M3UASelectableChannelImpl channel, SelectionKey k) {
        this.channel = channel;
        this.key = k;
    }
    
    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UASelectionKey#channel() 
     */
    public M3UASelectableChannelImpl channel() {
        return channel;
    }
    
    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UASelectionKey#isAcceptable() 
     */
    public boolean isAcceptable() {
        return key.isAcceptable();
    }
    
    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UASelectionKey#isReadable() 
     */
    public boolean isReadable() {
        return ((M3UAChannelImpl)channel).isReadable();
    }
    
    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UASelectionKey#isWritable() 
     */
    public boolean isWritable() {
        return ((M3UAChannelImpl)channel).isWritable();
    }
    
    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UASelectionKey#cancel() 
     */
    public void cancel() {
        key.cancel();
    }

    public Object attach(Object ob) {
        Object prev = this.object;
        this.object = ob;
        return prev;
    }

    public Object attachment() {
        return this.object;
    }
}
