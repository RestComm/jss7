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

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPMessageType;
import org.mobicents.protocols.ss7.cap.api.CAPOperationCode;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CancelRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallSegmentToCancel;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CallSegmentToCancelImpl;

/**
 *
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class CancelRequestImpl extends CircuitSwitchedCallMessageImpl implements CancelRequest {

    private static final String INVOKE_ID = "invokeID";
    private static final String ALL_REQUESTS = "allRequests";
    private static final String CALL_SEGMENT_TO_CANCEL = "callSegmentToCancel";

    public static final int _ID_invokeID = 0;
    public static final int _ID_allRequests = 1;
    public static final int _ID_callSegmentToCancel = 2;

    public static final String _PrimitiveName = "CancelRequest";

    private Integer invokeID;
    private boolean allRequests;
    private CallSegmentToCancel callSegmentToCancel;

    public CancelRequestImpl() {
    }

    public CancelRequestImpl(Integer invokeID) {
        this.invokeID = invokeID;
    }

    public CancelRequestImpl(boolean allRequests) {
        this.allRequests = allRequests;
    }

    public CancelRequestImpl(CallSegmentToCancel callSegmentToCancel) {
        this.callSegmentToCancel = callSegmentToCancel;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.cancel_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.cancelCode;
    }

    @Override
    public Integer getInvokeID() {
        return invokeID;
    }

    @Override
    public boolean getAllRequests() {
        return allRequests;
    }

    @Override
    public CallSegmentToCancel getCallSegmentToCancel() {
        return callSegmentToCancel;
    }

    @Override
    public int getTag() throws CAPException {

        if (this.invokeID != null) {
            return _ID_invokeID;
        } else if (this.allRequests) {
            return _ID_allRequests;
        } else if (this.callSegmentToCancel != null) {
            return _ID_callSegmentToCancel;
        } else {
            throw new CAPException("Error while encoding " + _PrimitiveName + ": no of choices has been definite");
        }
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        if (this.callSegmentToCancel != null)
            return false;
        else
            return true;
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

    private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.invokeID = null;
        this.allRequests = false;
        this.callSegmentToCancel = null;

        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad tagClass",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        switch (ais.getTag()) {
            case _ID_invokeID:
                this.invokeID = (int) ais.readIntegerData(length);
                break;
            case _ID_allRequests:
                ais.readNullData(length);
                this.allRequests = true;
                break;
            case _ID_callSegmentToCancel:
                this.callSegmentToCancel = new CallSegmentToCancelImpl();
                ((CallSegmentToCancelImpl) this.callSegmentToCancel).decodeData(ais, length);
                break;
            default:
                throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad tag: " + ais.getTag(),
                        CAPParsingComponentExceptionReason.MistypedParameter);
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

        int choiceCnt = 0;
        if (this.invokeID != null)
            choiceCnt++;
        if (this.allRequests)
            choiceCnt++;
        if (this.callSegmentToCancel != null)
            choiceCnt++;

        if (choiceCnt != 1)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": only one choice must be definite, found: "
                    + choiceCnt);

        try {
            if (this.invokeID != null)
                aos.writeIntegerData(this.invokeID);
            if (this.allRequests)
                aos.writeNullData();
            if (this.callSegmentToCancel != null)
                ((CallSegmentToCancelImpl) this.callSegmentToCancel).encodeData(aos);
        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.invokeID != null) {
            sb.append("invokeID=");
            sb.append(invokeID);
        }
        if (this.allRequests) {
            sb.append(" allRequests");
        }
        if (this.callSegmentToCancel != null) {
            sb.append(" callSegmentToCancel=");
            sb.append(callSegmentToCancel.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<CancelRequestImpl> CANCEL_REQUEST_XML = new XMLFormat<CancelRequestImpl>(
            CancelRequestImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, CancelRequestImpl cancelRequest) throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, cancelRequest);
            cancelRequest.invokeID = xml.get(INVOKE_ID, Integer.class);
            Boolean bval = xml.get(ALL_REQUESTS, Boolean.class);
            if (bval != null)
                cancelRequest.allRequests = bval;
            cancelRequest.callSegmentToCancel = xml.get(CALL_SEGMENT_TO_CANCEL, CallSegmentToCancelImpl.class);
        }

        @Override
        public void write(CancelRequestImpl cancelRequest, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(cancelRequest, xml);

            xml.add(cancelRequest.invokeID, INVOKE_ID, Integer.class);
            if (cancelRequest.allRequests)
                xml.add(cancelRequest.allRequests, ALL_REQUESTS, Boolean.class);
            xml.add((CallSegmentToCancelImpl) cancelRequest.callSegmentToCancel, CALL_SEGMENT_TO_CANCEL,
                    CallSegmentToCancelImpl.class);
        }
    };
}
