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
import org.mobicents.protocols.ss7.cap.api.service.gprs.ApplyChargingGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ApplyChargingGPRSRequestImpl extends GprsMessageImpl implements ApplyChargingGPRSRequest {

    public static final String _PrimitiveName = "ApplyChargingGPRSRequest";

    public static final int _ID_chargingCharacteristics = 0;
    public static final int _ID_tariffSwitchInterval = 1;
    public static final int _ID_pdpID = 2;

    private ChargingCharacteristics chargingCharacteristics;
    private Integer tariffSwitchInterval;
    private PDPID pdpID;

    public ApplyChargingGPRSRequestImpl() {
    }

    public ApplyChargingGPRSRequestImpl(ChargingCharacteristics chargingCharacteristics, Integer tariffSwitchInterval,
            PDPID pdpID) {
        super();
        this.chargingCharacteristics = chargingCharacteristics;
        this.tariffSwitchInterval = tariffSwitchInterval;
        this.pdpID = pdpID;
    }

    @Override
    public ChargingCharacteristics getChargingCharacteristics() {
        return this.chargingCharacteristics;
    }

    @Override
    public Integer getTariffSwitchInterval() {
        return this.tariffSwitchInterval;
    }

    @Override
    public PDPID getPDPID() {
        return this.pdpID;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.applyChargingGPRS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.applyChargingGPRS;
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

        this.chargingCharacteristics = null;
        this.tariffSwitchInterval = null;
        this.pdpID = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_chargingCharacteristics:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".chargingCharacteristics: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.chargingCharacteristics = new ChargingCharacteristicsImpl();
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        ((ChargingCharacteristicsImpl) this.chargingCharacteristics).decodeAll(ais2);
                        break;
                    case _ID_tariffSwitchInterval:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".tariffSwitchInterval: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.tariffSwitchInterval = (int) ais.readInteger();
                        break;
                    case _ID_pdpID:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".pdpID: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.pdpID = new PDPIDImpl();
                        ((PDPIDImpl) this.pdpID).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.chargingCharacteristics == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": parameter chargingCharacteristics is mandatory but not found",
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

        if (this.chargingCharacteristics == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": chargingCharacteristics must not be null");

        try {

            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_chargingCharacteristics);
            int pos = asnOs.StartContentDefiniteLength();
            ((ChargingCharacteristicsImpl) this.chargingCharacteristics).encodeAll(asnOs);
            asnOs.FinalizeContent(pos);

            if (this.tariffSwitchInterval != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_tariffSwitchInterval, tariffSwitchInterval.intValue());

            if (this.pdpID != null)
                ((PDPIDImpl) this.pdpID).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_pdpID);

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.chargingCharacteristics != null) {
            sb.append("chargingCharacteristics=");
            sb.append(this.chargingCharacteristics.toString());
            sb.append(", ");
        }

        if (this.tariffSwitchInterval != null) {
            sb.append("tariffSwitchInterval=");
            sb.append(this.tariffSwitchInterval.toString());
            sb.append(", ");
        }

        if (this.pdpID != null) {
            sb.append("pdpID=");
            sb.append(this.pdpID.toString());
        }
        sb.append("]");

        return sb.toString();
    }

}
