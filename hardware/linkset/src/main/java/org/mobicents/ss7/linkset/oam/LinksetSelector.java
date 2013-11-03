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

package org.mobicents.ss7.linkset.oam;

import java.io.IOException;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.Stream;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 * the stream of Linkset
 *
 * @author amit bhayani
 *
 */
public class LinksetSelector implements StreamSelector {

    private static final Logger logger = Logger.getLogger(LinksetSelector.class);

    private FastList<Stream> registered = new FastList<Stream>();

    /** array of selected channels */
    private FastList<SelectorKey> selected = new FastList<SelectorKey>();

    public LinksetSelector() {
        // TODO Auto-generated constructor stub
    }

    public SelectorKey register(LinksetStream linkSet) {
        // add LinkSet instance to the collection
        registered.add(linkSet);

        LinksetSelectorKey key = new LinksetSelectorKey(linkSet, this);
        linkSet.selectorKey = key;
        return key;
    }

    public void unregister(LinksetStream linkSet) {
        registered.remove(linkSet);
        linkSet.selectorKey = null;

        // TODO : Should some method be called in LinkSet indicating that its
        // Unregistered for cleanup?
    }

    public void close() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public FastList<Stream> getRegisteredStreams() {
        return registered;
    }

    public boolean isClosed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public FastList<SelectorKey> selectNow(int operation, int timeout) throws IOException {

        selected.clear();
        for (Stream s : registered) {
            if (((LinksetStream) s).poll(operation, timeout)) {
                selected.add(((LinksetStream) s).selectorKey);
            }
        }
        return selected;
    }

}
