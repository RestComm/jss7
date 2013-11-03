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
import org.mobicents.protocols.ss7.cap.api.service.gprs.FurnishChargingInformationGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class FurnishChargingInformationGPRSRequestImpl extends GprsMessageImpl implements FurnishChargingInformationGPRSRequest {

    public static final String _PrimitiveName = "FurnishChargingInformationGPRSRequest";

    public static final int _ID_CAMELFCIGPRSBillingChargingCharacteristics = 0;

    private CAMELFCIGPRSBillingChargingCharacteristics fciGPRSBillingChargingCharacteristics;

    public FurnishChargingInformationGPRSRequestImpl() {
    }

    public FurnishChargingInformationGPRSRequestImpl(
            CAMELFCIGPRSBillingChargingCharacteristics fciGPRSBillingChargingCharacteristics) {
        super();
        this.fciGPRSBillingChargingCharacteristics = fciGPRSBillingChargingCharacteristics;
    }

    public CAMELFCIGPRSBillingChargingCharacteristics getFCIGPRSBillingChargingCharacteristics() {
        return this.fciGPRSBillingChargingCharacteristics;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.furnishChargingInformationGPRS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.furnishChargingInformationGPRS;
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

        this.fciGPRSBillingChargingCharacteristics = null;

        byte[] buf = ansIS.readOctetStringData(length);
        AsnInputStream aiss = new AsnInputStream(buf);

        int tag = aiss.readTag();

        if (tag != Tag.SEQUENCE || aiss.getTagClass() != Tag.CLASS_UNIVERSAL || aiss.isTagPrimitive())
            throw new CAPParsingComponentException("Error when decoding " + _PrimitiveName
                    + ": bad tag or tagClass or is primitive of the choice fciGPRSBillingChargingCharacteristics",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        this.fciGPRSBillingChargingCharacteristics = new CAMELFCIGPRSBillingChargingCharacteristicsImpl();
        ((CAMELFCIGPRSBillingChargingCharacteristicsImpl) this.fciGPRSBillingChargingCharacteristics).decodeAll(aiss);

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

        if (this.fciGPRSBillingChargingCharacteristics == null)
            throw new CAPException("Error while encoding " + _PrimitiveName
                    + ": fciGPRSBillingChargingCharacteristics must not be null");

        try {
            asnOs.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int pos = asnOs.StartContentDefiniteLength();
            ((CAMELFCIGPRSBillingChargingCharacteristicsImpl) this.fciGPRSBillingChargingCharacteristics).encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.fciGPRSBillingChargingCharacteristics != null) {
            sb.append("fciGPRSBillingChargingCharacteristics=");
            sb.append(this.fciGPRSBillingChargingCharacteristics.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}