/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
    private static final String RETURN_RESULT_NOT_LAST = "returnResultNotLast";

    private long invokeId;
    private MAPDialog mapDialog;
    private boolean returnResultNotLast = false;

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

    public boolean isReturnResultNotLast() {
        return returnResultNotLast;
    }

    public void setReturnResultNotLast(boolean returnResultNotLast) {
        this.returnResultNotLast = returnResultNotLast;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MessageImpl> MAP_MESSAGE_XML = new XMLFormat<MessageImpl>(MessageImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MessageImpl message) throws XMLStreamException {
            message.invokeId = xml.getAttribute(INVOKE_ID, -1L);
            message.returnResultNotLast = xml.getAttribute(RETURN_RESULT_NOT_LAST, false);
        }

        @Override
        public void write(MessageImpl message, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(INVOKE_ID, message.invokeId);
            if (message.returnResultNotLast)
                xml.setAttribute(RETURN_RESULT_NOT_LAST, message.returnResultNotLast);
        }
    };

}
