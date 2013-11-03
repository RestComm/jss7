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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.ss7.hardware.dahdi;

import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.Stream;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 *
 * @author kulikov
 */
public class SelectorKeyImpl implements SelectorKey {

    private boolean isValid;
    private boolean isReadable;
    private boolean isWritable;

    private Channel channel;
    private Selector selector;

    private Object attachment;

    protected SelectorKeyImpl(Channel channel, Selector selector) {
        this.channel = channel;
        this.selector = selector;
    }

    public boolean isValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isReadable() {
        return isReadable;
    }

    public boolean isWriteable() {
        return isWritable;
    }

    public Stream getStream() {
        return channel;
    }

    public StreamSelector getStreamSelector() {
        return selector;
    }

    public void cancel() {
        selector.unregister(channel);
    }

    public void attach(Object obj) {
        this.attachment = obj;
    }

    public Object attachment() {
        return this.attachment;
    }

}
