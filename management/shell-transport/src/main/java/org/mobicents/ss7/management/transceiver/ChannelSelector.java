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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

import javolution.util.FastSet;

/**
 * <p>
 * A multiplex of {@link ShellSelectableChannel} objects.
 * </p>
 *
 * <p>
 * A selector may be created by invoking the {@link #open open} method of this class, which will use the system's default
 * {@link ChannelProvider </code>selector provider<code>} to create a new selector. A selector remains open until it is closed
 * via its {@link #close close} method.
 * </p>
 *
 * <p>
 * A selectable channel's registration with a selector is represented by a {@link ChannelSelectionKey} object. A selector
 * maintains one set of selection keys:
 *
 * <ul>
 * <li>
 * <p>
 * The <i>selected-key set</i> is the set of keys such that each key's channel was detected to be ready for at least one of the
 * operations identified in the key's interest set during a selection operation. This set is returned by the {@link #selectNow()
 * selectNow} method.
 * </p>
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * Every time {@link #selectNow() selectNow} method is called the <i>selected-key set</i> is cleared before adding keys that are
 * ready for operation
 * </p>
 *
 *
 * @author amit bhayani
 *
 */
public class ChannelSelector {

    protected Selector selector;

    protected ChannelSelector(Selector selector) {
        this.selector = selector;
    }

    /**
     * <p>
     * Opens a ChannelSelector
     * </p>
     * <p>
     * The new selector is created by invoking the {@link ChannelProvider#openSelector openSelector} method of the system-wide
     * default {@link ChannelProvider} object.
     * </p>
     *
     * @return A new selector
     * @throws IOException If an I/O error occurs
     */
    public static ChannelSelector open() throws IOException {
        return ChannelProvider.provider().openSelector();
    }

    /**
     * * Selects a set of keys whose corresponding channels are ready for I/O operations.
     *
     * <p>
     * This method performs a non-blocking selection operation. If no channels have become selectable since the previous
     * selection operation then this method immediately returns empty selected-key set.
     * </p>
     *
     * @return
     * @throws IOException If an I/O error occurs
     */
    public FastSet<ChannelSelectionKey> selectNow() throws IOException {
        FastSet<ChannelSelectionKey> selectedKey = new FastSet<ChannelSelectionKey>();
        selector.selectNow();
        Set<SelectionKey> selection = selector.selectedKeys();
        for (SelectionKey key : selection) {
            ChannelSelectionKey k = (ChannelSelectionKey) key.attachment();
            try {
                if (key.isValid()) {
                    if (key.isValid() && key.isAcceptable()) {
                        selectedKey.add(k);
                    } else {

                        if (key.isValid() && key.isReadable()) {
                            ((ShellChannel) k.channel()).doRead();
                            if (k.isValid() && k.isReadable()) {
                                selectedKey.add(k);
                            }
                        }

                        if (key.isValid() && key.isWritable()) {
                            ((ShellChannel) k.channel()).doWrite();
                            if (k.isValid() && k.isWritable()) {
                                selectedKey.add(k);
                            }
                        }
                    }
                } else {
                    // adding invalid channel to allow its removal
                    selectedKey.add(k);
                }
            } catch (IOException ioe) {
                throw new ChannelException(k, ioe.getMessage());
            }
        } // for
        selection.clear();
        return selectedKey;
    }

    /**
     * Closes this selector.
     *
     * @throws IOException If an I/O error occurs
     */
    public void close() throws IOException {
        selector.close();
    }
}
