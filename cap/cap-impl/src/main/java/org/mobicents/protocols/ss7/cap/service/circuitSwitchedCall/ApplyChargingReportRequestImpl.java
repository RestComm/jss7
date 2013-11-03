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
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResult;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.TimeDurationChargingResultImpl;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class ApplyChargingReportRequestImpl extends CircuitSwitchedCallMessageImpl implements ApplyChargingReportRequest {

    private static final String TIME_DURATION_CHARGING_RESULT = "timeDurationChargingResult";

    public static final int _ID_timeDurationChargingResult = 0;

    public static final int _ID_partyToCharge = 0;

    public static final String _PrimitiveName = "ApplyChargingReportRequestIndication";

    private TimeDurationChargingResult timeDurationChargingResult;

    public ApplyChargingReportRequestImpl() {
    }

    public ApplyChargingReportRequestImpl(TimeDurationChargingResult timeDurationChargingResult) {
        this.timeDurationChargingResult = timeDurationChargingResult;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.applyChargingReport_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.applyChargingReport;
    }

    @Override
    public TimeDurationChargingResult getTimeDurationChargingResult() {
        return timeDurationChargingResult;
    }

    @Override
    public int getTag() throws CAPException {
        return Tag.STRING_OCTET;
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_UNIVERSAL;
    }

    @Override
    public boolean getIsPrimitive() {
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

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {

        this.timeDurationChargingResult = null;

        byte[] buf = ansIS.readOctetStringData(length);
        AsnInputStream aiss = new AsnInputStream(buf);

        int tag = aiss.readTag();

        if (tag != _ID_timeDurationChargingResult || aiss.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || aiss.isTagPrimitive())
            throw new CAPParsingComponentException("Error when decoding " + _PrimitiveName
                    + ": bad tag or tagClass or is primitive of the choice timeDurationChargingResult",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        this.timeDurationChargingResult = new TimeDurationChargingResultImpl();
        ((TimeDurationChargingResultImpl) this.timeDurationChargingResult).decodeAll(aiss);
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
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (this.timeDurationChargingResult == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": timeDurationChargingResult must not be null");

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_timeDurationChargingResult);
            int pos = asnOs.StartContentDefiniteLength();
            ((TimeDurationChargingResultImpl) this.timeDurationChargingResult).encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.timeDurationChargingResult != null) {
            sb.append("timeDurationChargingResult=");
            sb.append(timeDurationChargingResult.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<ApplyChargingReportRequestImpl> APPLY_CHARGING_REPORT_REQUEST_XML = new XMLFormat<ApplyChargingReportRequestImpl>(
            ApplyChargingReportRequestImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, ApplyChargingReportRequestImpl applyChargingReportRequest)
                throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, applyChargingReportRequest);
            applyChargingReportRequest.timeDurationChargingResult = xml.get(TIME_DURATION_CHARGING_RESULT,
                    TimeDurationChargingResultImpl.class);
        }

        @Override
        public void write(ApplyChargingReportRequestImpl applyChargingReportRequest, javolution.xml.XMLFormat.OutputElement xml)
                throws XMLStreamException {

            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(applyChargingReportRequest, xml);

            xml.add((TimeDurationChargingResultImpl) applyChargingReportRequest.timeDurationChargingResult,
                    TIME_DURATION_CHARGING_RESULT, TimeDurationChargingResultImpl.class);
        }
    };

}
