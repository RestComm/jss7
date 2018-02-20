/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPMessage;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public abstract class MessageImpl implements CAPMessage {

    private static final String INVOKE_ID = "invokeId";

    private long invokeId;
    private CAPDialog capDialog;

    public long getInvokeId() {
        return this.invokeId;
    }

    public CAPDialog getCAPDialog() {
        return this.capDialog;
    }

    public void setInvokeId(long invokeId) {
        this.invokeId = invokeId;
    }

    public void setCAPDialog(CAPDialog capDialog) {
        this.capDialog = capDialog;
    }

    protected void addInvokeIdInfo(StringBuilder sb) {
        sb.append("InvokeId=");
        sb.append(this.invokeId);
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MessageImpl> CAP_MESSAGE_XML = new XMLFormat<MessageImpl>(MessageImpl.class) {

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
