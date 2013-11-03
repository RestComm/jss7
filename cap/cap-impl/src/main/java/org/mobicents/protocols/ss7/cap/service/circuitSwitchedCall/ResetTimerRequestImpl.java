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
 *
 */
public class ResetTimerRequestImpl extends CircuitSwitchedCallMessageImpl implements ResetTimerRequest {

    public static final int _ID_timerID = 0;
    public static final int _ID_timervalue = 1;
    public static final int _ID_extensions = 2;
    public static final int _ID_callSegmentID = 3;

    public static final String _PrimitiveName = "ConnectToResourceIndication";

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

        if (this.timerID != null) {
            sb.append("timerID=");
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
}
