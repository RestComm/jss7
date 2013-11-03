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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.cap.MessageImpl;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CircuitSwitchedCallMessage;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public abstract class CircuitSwitchedCallMessageImpl extends MessageImpl implements CircuitSwitchedCallMessage, CAPAsnPrimitive {

    public CAPDialogCircuitSwitchedCall getCAPDialog() {
        return (CAPDialogCircuitSwitchedCall) super.getCAPDialog();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CircuitSwitchedCallMessageImpl> CIRCUIT_SWITCHED_CALL_MESSAGE_XML = new XMLFormat<CircuitSwitchedCallMessageImpl>(
            CircuitSwitchedCallMessageImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CircuitSwitchedCallMessageImpl message)
                throws XMLStreamException {
            CAP_MESSAGE_XML.read(xml, message);
        }

        @Override
        public void write(CircuitSwitchedCallMessageImpl message, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            CAP_MESSAGE_XML.write(message, xml);
        }
    };

}
