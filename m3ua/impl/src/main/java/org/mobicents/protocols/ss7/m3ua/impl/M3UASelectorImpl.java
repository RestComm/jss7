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

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

import javolution.util.FastList;

import org.mobicents.protocols.ss7.m3ua.M3UASelectionKey;
import org.mobicents.protocols.ss7.m3ua.M3UASelector;

/**
 * Implements channel multiplexer.
 * 
 * @author amit bhayani
 * @author kulikov
 */
public class M3UASelectorImpl implements M3UASelector {
    //NIO multiplexer
    protected Selector selector;
    //list used for collecting selected keys.
    private FastList<M3UASelectionKey> list = new FastList<M3UASelectionKey>();
    
    /**
     * Constructs new multiplexer.
     * 
     * @param selector the NIO multiplexer.
     */
    protected M3UASelectorImpl(Selector selector) {
        this.selector = selector;
    }
    
    /**
     * Static method used for constructing the multiplexer.
     * 
     * @return new multiplexer.
     * @throws java.io.IOException
     */
    public static M3UASelectorImpl open() throws IOException {
        return new M3UASelectorImpl(SelectorProvider.provider().openSelector());
    }
    
    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UASelector#selectNow() 
     * @throws java.io.IOException
     */
    public FastList<M3UASelectionKey> selectNow() throws IOException {
        list.clear();
        selector.selectNow();
        Set<SelectionKey> selection = selector.selectedKeys();
        for (SelectionKey key : selection) {
            if (key.isAcceptable()) {
                M3UASelectionKeyImpl k = (M3UASelectionKeyImpl) key.attachment();
                list.add(k);
            } else if (key.isReadable()) {
                M3UASelectionKeyImpl k = (M3UASelectionKeyImpl) key.attachment();
                ((M3UAChannelImpl)k.channel()).doRead();
                if (k.isReadable()) {
                    list.add(k);
                }
            } else { //FIXME: Oleg this assumes by default that its write....
                M3UASelectionKeyImpl k = (M3UASelectionKeyImpl) key.attachment();
                ((M3UAChannelImpl)k.channel()).doWrite();
                if (k.isWritable()) {
                    list.add(k);
                }
            }
        }
        selection.clear();
        return list;
    }
    
    /**
     * (Non Java-doc.)
     * 
     * @see org.mobicents.protocols.ss7.m3ua.M3UASelector#close() 
     * @throws java.io.IOException
     */
    public void close() throws IOException {
        selector.close();
    }
}
