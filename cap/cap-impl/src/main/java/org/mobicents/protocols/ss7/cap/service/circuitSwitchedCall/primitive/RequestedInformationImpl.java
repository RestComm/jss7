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

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.primitives.DateAndTime;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.cap.primitives.DateAndTimeImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class RequestedInformationImpl implements RequestedInformation, CAPAsnPrimitive {

    public static final int _ID_requestedInformationType = 0;
    public static final int _ID_requestedInformationValue = 1;

    public static final int _ID_callAttemptElapsedTimeValue = 0;
    public static final int _ID_callStopTimeValue = 1;
    public static final int _ID_callConnectedElapsedTimeValue = 2;
    public static final int _ID_releaseCauseValue = 30;

    public static final String _PrimitiveName = "RequestedInformation";

    private RequestedInformationType requestedInformationType;
    private Integer callAttemptElapsedTimeValue;
    private DateAndTime callStopTimeValue;
    private Integer callConnectedElapsedTimeValue;
    private CauseCap releaseCauseValue;

    public RequestedInformationImpl() {
    }

    public RequestedInformationImpl(RequestedInformationType requestedInformationType, int intValue) {
        if (requestedInformationType == RequestedInformationType.callAttemptElapsedTime) {
            this.requestedInformationType = RequestedInformationType.callAttemptElapsedTime;
            this.callAttemptElapsedTimeValue = intValue;
        } else {
            this.requestedInformationType = RequestedInformationType.callConnectedElapsedTime;
            this.callConnectedElapsedTimeValue = intValue;
        }
    }

    public RequestedInformationImpl(DateAndTime callStopTimeValue) {
        this.requestedInformationType = RequestedInformationType.callStopTime;
        this.callStopTimeValue = callStopTimeValue;
    }

    public RequestedInformationImpl(CauseCap releaseCauseValue) {
        this.requestedInformationType = RequestedInformationType.releaseCause;
        this.releaseCauseValue = releaseCauseValue;
    }

    @Override
    public RequestedInformationType getRequestedInformationType() {
        return requestedInformationType;
    }

    @Override
    public Integer getCallAttemptElapsedTimeValue() {
        return callAttemptElapsedTimeValue;
    }

    @Override
    public DateAndTime getCallStopTimeValue() {
        return callStopTimeValue;
    }

    @Override
    public Integer getCallConnectedElapsedTimeValue() {
        return callConnectedElapsedTimeValue;
    }

    @Override
    public CauseCap getReleaseCauseValue() {
        return releaseCauseValue;
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

        this.requestedInformationType = null;
        this.callAttemptElapsedTimeValue = null;
        this.callStopTimeValue = null;
        this.callConnectedElapsedTimeValue = null;
        this.releaseCauseValue = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        boolean valueReceived = false;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_requestedInformationType:
                        int i1 = (int) ais.readInteger();
                        this.requestedInformationType = RequestedInformationType.getInstance(i1);
                        break;
                    case _ID_requestedInformationValue:
                        valueReceived = true;
                        AsnInputStream ais2 = ais.readSequenceStream();
                        int tag2 = ais2.readTag();
                        if (ais2.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ": bad RequestedInformationValue tagClass",
                                    CAPParsingComponentExceptionReason.MistypedParameter);

                        switch (tag2) {
                            case _ID_callAttemptElapsedTimeValue:
                                this.callAttemptElapsedTimeValue = (int) ais2.readInteger();
                                break;
                            case _ID_callStopTimeValue:
                                this.callStopTimeValue = new DateAndTimeImpl();
                                ((DateAndTimeImpl) this.callStopTimeValue).decodeAll(ais2);
                                break;
                            case _ID_callConnectedElapsedTimeValue:
                                this.callConnectedElapsedTimeValue = (int) ais2.readInteger();
                                break;
                            case _ID_releaseCauseValue:
                                this.releaseCauseValue = new CauseCapImpl();
                                ((CauseCapImpl) this.releaseCauseValue).decodeAll(ais2);
                                break;
                            default:
                                if (ais2.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
                                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                            + ": bad RequestedInformationValue tag",
                                            CAPParsingComponentExceptionReason.MistypedParameter);
                        }
                        break;

                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.requestedInformationType == null || !valueReceived)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": requestedInformationType and requestedInformationValue are mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);
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

        if (this.requestedInformationType == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": requestedInformationType must not be null");

        try {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_requestedInformationType, this.requestedInformationType.getCode());

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_requestedInformationValue);
            int pos = aos.StartContentDefiniteLength();

            switch (this.requestedInformationType) {
                case callAttemptElapsedTime:
                    if (this.callAttemptElapsedTimeValue == null)
                        throw new CAPException("Error while encoding " + _PrimitiveName
                                + ": callAttemptElapsedTimeValue must not be null for " + requestedInformationType.toString()
                                + " requestedInformationType");
                    aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_callAttemptElapsedTimeValue,
                            this.callAttemptElapsedTimeValue);
                    break;
                case callStopTime:
                    if (this.callStopTimeValue == null)
                        throw new CAPException("Error while encoding " + _PrimitiveName
                                + ": callStopTimeValue must not be null for " + requestedInformationType.toString()
                                + " requestedInformationType");
                    ((DateAndTimeImpl) this.callStopTimeValue)
                            .encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_callStopTimeValue);
                    break;
                case callConnectedElapsedTime:
                    if (this.callConnectedElapsedTimeValue == null)
                        throw new CAPException("Error while encoding " + _PrimitiveName
                                + ": callConnectedElapsedTimeValue must not be null for " + requestedInformationType.toString()
                                + " requestedInformationType");
                    aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_callConnectedElapsedTimeValue,
                            this.callConnectedElapsedTimeValue);
                    break;
                case releaseCause:
                    if (this.releaseCauseValue == null)
                        throw new CAPException("Error while encoding " + _PrimitiveName
                                + ": releaseCauseValue must not be null for " + requestedInformationType.toString()
                                + " requestedInformationType");
                    ((CauseCapImpl) this.releaseCauseValue).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_releaseCauseValue);
                    break;
                default:
                    throw new CAPException("Error while encoding " + _PrimitiveName + ": bad requestedInformationType value");
            }

            aos.FinalizeContent(pos);
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
        if (this.requestedInformationType != null) {
            sb.append("requestedInformationType=");
            sb.append(requestedInformationType);
        }
        if (this.callAttemptElapsedTimeValue != null) {
            sb.append(", callAttemptElapsedTimeValue=");
            sb.append(callAttemptElapsedTimeValue);
        }
        if (this.callStopTimeValue != null) {
            sb.append(", callStopTimeValue=");
            sb.append(callStopTimeValue.toString());
        }
        if (this.callConnectedElapsedTimeValue != null) {
            sb.append(", callConnectedElapsedTimeValue=");
            sb.append(callConnectedElapsedTimeValue);
        }
        if (this.releaseCauseValue != null) {
            sb.append(", releaseCauseValue=");
            sb.append(releaseCauseValue.toString());
        }
        sb.append("]");

        return sb.toString();
    }
}
