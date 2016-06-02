/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.TimerID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ResetTimerRequest;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;

/**
 *
 * @author sergey vetyutnev
 * @author alerant appngin
 */
public class ResetTimerRequestImpl extends CircuitSwitchedCallMessageImpl implements ResetTimerRequest {

    private static final String TIMER_ID = "timerID";
    private static final String TIMER_VALUE = "timervalue";
    private static final String EXTENSIONS = "extensions";
    private static final String CALL_SEGMENT_ID = "callSegmentID";

    public static final int _ID_timerID = 0;
    public static final int _ID_timervalue = 1;
    public static final int _ID_extensions = 2;
    public static final int _ID_callSegmentID = 3;

    public static final String _PrimitiveName = "ResetTimerRequest";

    private TimerID timerID;
    private int timerValue;
    private CAPExtensions extensions;
    private Integer callSegmentID;

    public ResetTimerRequestImpl() {
    }

    public ResetTimerRequestImpl(TimerID timerID, int timerValue, CAPExtensions extensions, Integer callSegmentID) {
        this.timerID = timerID;
        this.timerValue = timerValue;
        this.extensions = extensions;
        this.callSegmentID = callSegmentID;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.resetTimer_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.resetTimer;
    }

    @Override
    public TimerID getTimerID() {
        return timerID;
    }

    @Override
    public int getTimerValue() {
        return timerValue;
    }

    @Override
    public CAPExtensions getExtensions() {
        return extensions;
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

        this.timerID = TimerID.tssf;
        this.timerValue = -1;
        this.extensions = null;
        this.callSegmentID = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_timerID:
                        int i1 = (int) ais.readInteger();
                        this.timerID = TimerID.getInstance(i1);
                        break;
                    case _ID_timervalue:
                        this.timerValue = (int) ais.readInteger();
                        break;
                    case _ID_extensions:
                        this.extensions = new CAPExtensionsImpl();
                        ((CAPExtensionsImpl) this.extensions).decodeAll(ais);
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

        if (this.timerValue == -1)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": timerValue is mandatory but not found", CAPParsingComponentExceptionReason.MistypedParameter);
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

        if (this.timerID == null)
            this.timerID = TimerID.tssf;

        try {

            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_timerID, this.timerID.getCode());
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_timervalue, this.timerValue);
            if (this.extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);
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
        this.addInvokeIdInfo(sb);

        if (this.timerID != null) {
            sb.append(", timerID=");
            sb.append(timerID.toString());
        }
        sb.append(", timerValue=");
        sb.append(timerValue);
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.callSegmentID != null) {
            sb.append(", callSegmentID=");
            sb.append(callSegmentID.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    /** XML serialization */
    protected static final XMLFormat<ResetTimerRequestImpl> RESET_TIMER_XML = new XMLFormat<ResetTimerRequestImpl>(
            ResetTimerRequestImpl.class) {

        @Override
        public void write(ResetTimerRequestImpl resetTimerRequest, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(resetTimerRequest, xml);

            // mandatory elements
            xml.setAttribute(TIMER_ID, resetTimerRequest.timerID.name());
            xml.add(resetTimerRequest.timerValue, TIMER_VALUE, Integer.class);
            // optional elements. add method ignores null values
            xml.add((CAPExtensionsImpl) resetTimerRequest.extensions, EXTENSIONS, CAPExtensionsImpl.class);
            xml.add(resetTimerRequest.callSegmentID, CALL_SEGMENT_ID, Integer.class);
        }

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ResetTimerRequestImpl resetTimerRequest) throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, resetTimerRequest);

            // mandatory elements
            resetTimerRequest.timerID = TimerID.valueOf(xml.getAttribute(TIMER_ID, TimerID.tssf.name()));
            resetTimerRequest.timerValue = xml.get(TIMER_VALUE, Integer.class);
            // optional elements
            resetTimerRequest.extensions = xml.get(EXTENSIONS, CAPExtensionsImpl.class);
            resetTimerRequest.callSegmentID = xml.get(CALL_SEGMENT_ID, Integer.class);
        }

    };
}
