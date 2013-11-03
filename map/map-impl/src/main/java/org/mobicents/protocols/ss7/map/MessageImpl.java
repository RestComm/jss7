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

package org.mobicents.protocols.ss7.map;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPMessage;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public abstract class MessageImpl implements MAPMessage {

    private static final String INVOKE_ID = "invokeId";

    private long invokeId;
    private MAPDialog mapDialog;

    public long getInvokeId() {
        return this.invokeId;
    }

    public MAPDialog getMAPDialog() {
        return this.mapDialog;
    }

    public void setInvokeId(long invokeId) {
        this.invokeId = invokeId;
    }

    public void setMAPDialog(MAPDialog mapDialog) {
        this.mapDialog = mapDialog;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MessageImpl> MAP_MESSAGE_XML = new XMLFormat<MessageImpl>(MessageImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MessageImpl message) throws XMLStreamException {
            message.invokeId = xml.getAttribute(INVOKE_ID, -1L);
        }

        @Override
        public void write(MessageImpl message, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(INVOKE_ID, message.invokeId);
        }
    };

}
