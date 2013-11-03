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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallSegmentToCancel;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class CallSegmentToCancelImpl implements CallSegmentToCancel, CAPAsnPrimitive {

    private static final String INVOKE_ID = "invokeID";
    private static final String CALL_SEGMENT_ID = "callSegmentID";

    public static final int _ID_invokeID = 0;
    public static final int _ID_callSegmentID = 1;

    public static final String _PrimitiveName = "CallSegmentToCancel";

    private Integer invokeID;
    private Integer callSegmentID;

    public CallSegmentToCancelImpl() {
    }

    public CallSegmentToCancelImpl(Integer invokeID, Integer callSegmentID) {
        this.invokeID = invokeID;
        this.callSegmentID = callSegmentID;
    }

    @Override
    public Integer getInvokeID() {
        return invokeID;
    }

    @Override
    public Integer getCallSegmentID() {
        return callSegmentID;
    }

    @Override
    public int getTag() throws CAPException {
        return Tag.SEQUENCE;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.invokeID = null;
        this.callSegmentID = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_invokeID:
                        this.invokeID = (int) ais.readInteger();
                        break;
                    case _ID_callSegmentID:
                        this.callSegmentID = (int) ais.readInteger();
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());

    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream aos) throws CAPException {

        try {
            if (this.invokeID != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_invokeID, this.invokeID);
            if (this.callSegmentID != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_callSegmentID, this.callSegmentID);

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.invokeID != null) {
            sb.append("invokeID=");
            sb.append(this.invokeID);
        }
        if (this.callSegmentID != null) {
            sb.append(", callSegmentID=");
            sb.append(this.callSegmentID);
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CallSegmentToCancelImpl> CALL_SEGMENT_TO_CANCEL_XML = new XMLFormat<CallSegmentToCancelImpl>(
            CallSegmentToCancelImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CallSegmentToCancelImpl callSegmentToCancel)
                throws XMLStreamException {

            callSegmentToCancel.invokeID = xml.get(INVOKE_ID, Integer.class);
            callSegmentToCancel.callSegmentID = xml.get(CALL_SEGMENT_ID, Integer.class);
        }

        @Override
        public void write(CallSegmentToCancelImpl callSegmentToCancel, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            if (callSegmentToCancel.invokeID != null) {
                xml.add(((Integer) callSegmentToCancel.invokeID), INVOKE_ID, Integer.class);
            }

            if (callSegmentToCancel.callSegmentID != null) {
                xml.add(((Integer) callSegmentToCancel.callSegmentID), CALL_SEGMENT_ID, Integer.class);
            }

        }
    };
}
