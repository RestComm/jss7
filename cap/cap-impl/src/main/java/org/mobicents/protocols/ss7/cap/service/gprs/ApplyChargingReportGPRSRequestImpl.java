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
import org.mobicents.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingResult;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingRollOver;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ChargingResultImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ChargingRollOverImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.QualityOfServiceImpl;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ApplyChargingReportGPRSRequestImpl extends GprsMessageImpl implements ApplyChargingReportGPRSRequest {

    public static final String _PrimitiveName = "ApplyChargingReportGPRSRequest";

    public static final int _ID_chargingResult = 0;
    public static final int _ID_qualityOfService = 1;
    public static final int _ID_active = 2;
    public static final int _ID_pdpID = 3;
    public static final int _ID_chargingRollOver = 4;

    private ChargingResult chargingResult;
    private QualityOfService qualityOfService;
    private boolean active;
    private PDPID pdpID;
    private ChargingRollOver chargingRollOver;

    public ApplyChargingReportGPRSRequestImpl() {
    }

    public ApplyChargingReportGPRSRequestImpl(ChargingResult chargingResult, QualityOfService qualityOfService, boolean active,
            PDPID pdpID, ChargingRollOver chargingRollOver) {
        super();
        this.chargingResult = chargingResult;
        this.qualityOfService = qualityOfService;
        this.active = active;
        this.pdpID = pdpID;
        this.chargingRollOver = chargingRollOver;
    }

    @Override
    public ChargingResult getChargingResult() {
        return this.chargingResult;
    }

    @Override
    public QualityOfService getQualityOfService() {
        return this.qualityOfService;
    }

    @Override
    public boolean getActive() {
        return this.active;
    }

    @Override
    public PDPID getPDPID() {
        return this.pdpID;
    }

    @Override
    public ChargingRollOver getChargingRollOver() {
        return this.chargingRollOver;
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

        this.chargingResult = null;
        this.qualityOfService = null;
        this.active = true;
        this.pdpID = null;
        this.chargingRollOver = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_chargingResult:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".chargingResult: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.chargingResult = new ChargingResultImpl();
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        ((ChargingResultImpl) this.chargingResult).decodeAll(ais2);
                        break;
                    case _ID_qualityOfService:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".qualityOfService: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.qualityOfService = new QualityOfServiceImpl();
                        ((QualityOfServiceImpl) this.qualityOfService).decodeAll(ais);
                        break;
                    case _ID_active:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".active: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.active = ais.readBoolean();
                        break;
                    case _ID_pdpID:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".pdpID: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.pdpID = new PDPIDImpl();
                        ((PDPIDImpl) this.pdpID).decodeAll(ais);
                        break;
                    case _ID_chargingRollOver:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".chargingRollOver: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.chargingRollOver = new ChargingRollOverImpl();
                        AsnInputStream ais3 = ais.readSequenceStream();
                        ais3.readTag();
                        ((ChargingRollOverImpl) this.chargingRollOver).decodeAll(ais3);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.chargingResult == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": parameter chargingResult is mandatory but not found",
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

        if (this.chargingResult == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": chargingResult must not be null");

        try {
            asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_chargingResult);
            int pos = asnOs.StartContentDefiniteLength();
            ((ChargingResultImpl) this.chargingResult).encodeAll(asnOs);
            asnOs.FinalizeContent(pos);

            if (this.qualityOfService != null)
                ((QualityOfServiceImpl) this.qualityOfService).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_qualityOfService);

            asnOs.writeBoolean(Tag.CLASS_CONTEXT_SPECIFIC, _ID_active, active);

            if (this.pdpID != null)
                ((PDPIDImpl) this.pdpID).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_pdpID);

            if (this.chargingRollOver != null) {
                asnOs.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_chargingRollOver);
                int pos1 = asnOs.StartContentDefiniteLength();
                ((ChargingRollOverImpl) this.chargingRollOver).encodeAll(asnOs);
                asnOs.FinalizeContent(pos1);
            }

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

        if (this.chargingResult != null) {
            sb.append("chargingResult=");
            sb.append(this.chargingResult.toString());
            sb.append(", ");
        }

        if (this.qualityOfService != null) {
            sb.append("qualityOfService=");
            sb.append(this.qualityOfService.toString());
            sb.append(", ");
        }

        if (this.active) {
            sb.append("active ");
            sb.append(", ");
        }

        if (this.pdpID != null) {
            sb.append("pdpID=");
            sb.append(this.pdpID.toString());
            sb.append(", ");
        }

        if (this.chargingRollOver != null) {
            sb.append("chargingRollOver=");
            sb.append(this.chargingRollOver.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
