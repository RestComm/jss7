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
 * operations identified in the key's interest set during a selection operation. This set is returned by the
 * {@link #selectNow() selectNow} method.
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

    private FastSet<ChannelSelectionKey> selectedKey = new FastSet<ChannelSelectionKey>();

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
        selectedKey.clear();
        selector.selectNow();
        Set<SelectionKey> selection = selector.selectedKeys();
        for (SelectionKey key : selection) {
            if (key.isAcceptable()) {
                ChannelSelectionKey k = (ChannelSelectionKey) key.attachment();
                selectedKey.add(k);
            } else {

                if (key.isReadable()) {
                    ChannelSelectionKey k = (ChannelSelectionKey) key.attachment();
                    ((ShellChannel) k.channel()).doRead();
                    if (k.isReadable()) {
                        selectedKey.add(k);
                    }
                }

                if (key.isWritable()) {
                    ChannelSelectionKey k = (ChannelSelectionKey) key.attachment();
                    ((ShellChannel) k.channel()).doWrite();
                    if (k.isWritable()) {
                        selectedKey.add(k);
                    }
                }
            }
        }// for
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
