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
package org.mobicents.protocols.ss7.cap.service.gprs;

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
import org.mobicents.protocols.ss7.cap.api.service.gprs.SendChargingInformationGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SendChargingInformationGPRSRequestImpl extends GprsMessageImpl implements SendChargingInformationGPRSRequest {

    public static final int _ID_sciGPRSBillingChargingCharacteristics = 0;

    public static final String _PrimitiveName = "SendChargingInformationGPRSRequest";

    private CAMELSCIGPRSBillingChargingCharacteristics sciGPRSBillingChargingCharacteristics;

    public SendChargingInformationGPRSRequestImpl() {
    }

    public SendChargingInformationGPRSRequestImpl(
            CAMELSCIGPRSBillingChargingCharacteristics sciGPRSBillingChargingCharacteristics) {
        super();
        this.sciGPRSBillingChargingCharacteristics = sciGPRSBillingChargingCharacteristics;
    }

    @Override
    public CAMELSCIGPRSBillingChargingCharacteristics getSCIGPRSBillingChargingCharacteristics() {
        return this.sciGPRSBillingChargingCharacteristics;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.sendChargingInformationGPRS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.sendChargingInformationGPRS;
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
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
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
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException,
            MAPParsingComponentException {
        this.sciGPRSBillingChargingCharacteristics = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_sciGPRSBillingChargingCharacteristics:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".sciGPRSBillingChargingCharacteristics: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        length = ais.readLength();
                        byte[] buf = ais.readOctetStringData(length);
                        AsnInputStream ais2 = new AsnInputStream(buf);
                        tag = ais2.readTag();
                        this.sciGPRSBillingChargingCharacteristics = new CAMELSCIGPRSBillingChargingCharacteristicsImpl();
                        ((CAMELSCIGPRSBillingChargingCharacteristicsImpl) this.sciGPRSBillingChargingCharacteristics)
                                .decodeAll(ais2);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.sciGPRSBillingChargingCharacteristics == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": sciGPRSBillingChargingCharacteristics is mandatory but not found",
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
    public void encodeData(AsnOutputStream asnOs) throws CAPException {
        if (this.sciGPRSBillingChargingCharacteristics == null)
            throw new CAPException("Error while encoding " + _PrimitiveName
                    + ": sciGPRSBillingChargingCharacteristics must not be null");

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, _ID_sciGPRSBillingChargingCharacteristics);
            int pos = asnOs.StartContentDefiniteLength();
            ((CAMELSCIGPRSBillingChargingCharacteristicsImpl) this.sciGPRSBillingChargingCharacteristics).encodeAll(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.sciGPRSBillingChargingCharacteristics != null) {
            sb.append("sciGPRSBillingChargingCharacteristics=");
            sb.append(this.sciGPRSBillingChargingCharacteristics.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }

}
