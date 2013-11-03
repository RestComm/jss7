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

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPMessageType;
import org.mobicents.protocols.ss7.cap.api.CAPOperationCode;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueRequest;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class ContinueRequestImpl extends CircuitSwitchedCallMessageImpl implements ContinueRequest {

    public static final String _PrimitiveName = "ContinueRequest";

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.continue_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.continueCode;
    }

    @Override
    public int getTag() throws CAPException {
        throw new CAPException("Parameter " + _PrimitiveName + ": does not support encoding");
    }

    @Override
    public int getTagClass() {
        return 0;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {
        throw new CAPParsingComponentException("Parameter " + _PrimitiveName + ": does not support encoding",
                CAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {
        throw new CAPParsingComponentException("Parameter " + _PrimitiveName + ": does not support encoding",
                CAPParsingComponentExceptionReason.MistypedParameter);
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        throw new CAPException("Parameter " + _PrimitiveName + ": does not support encoding");
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {
        throw new CAPException("Parameter " + _PrimitiveName + ": does not support encoding");
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {
        throw new CAPException("Parameter " + _PrimitiveName + ": does not support encoding");
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ContinueRequestImpl> CONTINUE_REQUEST_XML = new XMLFormat<ContinueRequestImpl>(
            ContinueRequestImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ContinueRequestImpl continueRequest)
                throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, continueRequest);
        }

        @Override
        public void write(ContinueRequestImpl continueRequest, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(continueRequest, xml);
        }
    };
}
