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

import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.Stream;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 *
 * @author amit bhayani
 *
 */
public class LinksetSelectorKey implements SelectorKey {

    private boolean isValid;
    private boolean isReadable;
    private boolean isWritable;

    private LinksetStream linkSet = null;
    private LinksetSelector linkSetSelector = null;

    private Object attachment;

    public LinksetSelectorKey(LinksetStream linkSet, LinksetSelector linkSetSelector) {
        this.linkSet = linkSet;
        this.linkSetSelector = linkSetSelector;
    }

    public void attach(Object paramObject) {
        this.attachment = paramObject;
    }

    public Object attachment() {
        return this.attachment;
    }

    public void cancel() {
        linkSetSelector.unregister(linkSet);
    }

    public Stream getStream() {
        return this.linkSet;
    }

    public StreamSelector getStreamSelector() {
        return this.linkSetSelector;
    }

    public boolean isReadable() {
        return isReadable;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isWriteable() {
        return isWritable;
    }

}
