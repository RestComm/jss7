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

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.m3ua.M3UASelectionKey#isValid()
	 */
	@Override
	public boolean isValid() {
		return this.key.isValid();
	}
}
