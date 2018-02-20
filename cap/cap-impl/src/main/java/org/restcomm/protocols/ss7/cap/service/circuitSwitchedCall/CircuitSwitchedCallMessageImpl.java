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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.cap.MessageImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CircuitSwitchedCallMessage;
import org.restcomm.protocols.ss7.cap.primitives.CAPAsnPrimitive;

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
