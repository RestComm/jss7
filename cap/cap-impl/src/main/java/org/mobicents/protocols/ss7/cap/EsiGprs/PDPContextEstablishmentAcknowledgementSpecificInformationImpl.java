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
package org.mobicents.protocols.ss7.cap.EsiGprs;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentAcknowledgementSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.AccessPointNameImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.EndUserAddressImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.QualityOfServiceImpl;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GPRSChargingIDImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationGPRSImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class PDPContextEstablishmentAcknowledgementSpecificInformationImpl extends SequenceBase implements
        PDPContextEstablishmentAcknowledgementSpecificInformation {

    public static final int _ID_accessPointName = 0;
    public static final int _ID_chargingID = 1;
    public static final int _ID_locationInformationGPRS = 2;
    public static final int _ID_endUserAddress = 3;
    public static final int _ID_qualityOfService = 4;
    public static final int _ID_timeAndTimezone = 5;
    public static final int _ID_gsnAddress = 6;

    public static final int _ID_PDPContextEstablishmentAcknowledgementSpecificInformation = 5;

    private AccessPointName accessPointName;
    private GPRSChargingID chargingID;
    private LocationInformationGPRS locationInformationGPRS;
    private EndUserAddress endUserAddress;
    private QualityOfService qualityOfService;
    private TimeAndTimezone timeAndTimezone;
    private GSNAddress gsnAddress;

    public PDPContextEstablishmentAcknowledgementSpecificInformationImpl() {
        super("PDPContextEstablishmentAcknowledgementSpecificInformation");
    }

    public PDPContextEstablishmentAcknowledgementSpecificInformationImpl(AccessPointName accessPointName,
            GPRSChargingID chargingID, LocationInformationGPRS locationInformationGPRS, EndUserAddress endUserAddress,
            QualityOfService qualityOfService, TimeAndTimezone timeAndTimezone, GSNAddress gsnAddress) {
        super("PDPContextEstablishmentAcknowledgementSpecificInformation");
        this.accessPointName = accessPointName;
        this.chargingID = chargingID;
        this.locationInformationGPRS = locationInformationGPRS;
        this.endUserAddress = endUserAddress;
        this.qualityOfService = qualityOfService;
        this.timeAndTimezone = timeAndTimezone;
        this.gsnAddress = gsnAddress;
    }

    @Override
    public AccessPointName getAccessPointName() {
        return this.accessPointName;
    }

    @Override
    public GPRSChargingID getChargingID() {
        return this.chargingID;
    }

    @Override
    public LocationInformationGPRS getLocationInformationGPRS() {
        return this.locationInformationGPRS;
    }

    @Override
    public EndUserAddress getEndUserAddress() {
        return this.endUserAddress;
    }

    @Override
    public QualityOfService getQualityOfService() {
        return this.qualityOfService;
    }

    @Override
    public TimeAndTimezone getTimeAndTimezone() {
        return this.timeAndTimezone;
    }

    @Override
    public GSNAddress getGSNAddress() {
        return this.gsnAddress;
    }

    public int getTag() throws CAPException {
        return Tag.SEQUENCE;
    }

    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    protected void _decode(AsnInputStream asnIS, int length) throws CAPParsingComponentException, IOException, AsnException,
            MAPParsingComponentException {
        this.accessPointName = null;
        this.chargingID = null;
        this.locationInformationGPRS = null;
        this.endUserAddress = null;
        this.qualityOfService = null;
        this.timeAndTimezone = null;
        this.gsnAddress = null;

        AsnInputStream ais = asnIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {

                    case _ID_accessPointName:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".accessPointName: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.accessPointName = new AccessPointNameImpl();
                        ((AccessPointNameImpl) this.accessPointName).decodeAll(ais);
                        break;
                    case _ID_chargingID:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".chargingID: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.chargingID = new GPRSChargingIDImpl();
                        ((GPRSChargingIDImpl) this.chargingID).decodeAll(ais);
                        break;
                    case _ID_locationInformationGPRS:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".locationInformationGPRS: Parameter is  primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.locationInformationGPRS = new LocationInformationGPRSImpl();
                        ((LocationInformationGPRSImpl) this.locationInformationGPRS).decodeAll(ais);
                        break;
                    case _ID_endUserAddress:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".endUserAddress: Parameter is  primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.endUserAddress = new EndUserAddressImpl();
                        ((EndUserAddressImpl) this.endUserAddress).decodeAll(ais);
                        break;
                    case _ID_qualityOfService:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".qualityOfService: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.qualityOfService = new QualityOfServiceImpl();
                        ((QualityOfServiceImpl) this.qualityOfService).decodeAll(ais);
                        break;
                    case _ID_timeAndTimezone:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".timeAndTimezone: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.timeAndTimezone = new TimeAndTimezoneImpl();
                        ((TimeAndTimezoneImpl) this.timeAndTimezone).decodeAll(ais);
                        break;
                    case _ID_gsnAddress:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".gsnAddress: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.gsnAddress = new GSNAddressImpl();
                        ((GSNAddressImpl) this.gsnAddress).decodeAll(ais);
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
    public void encodeData(AsnOutputStream asnOs) throws CAPException {
        try {
            if (this.accessPointName != null)
                ((AccessPointNameImpl) this.accessPointName).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_accessPointName);

            if (this.chargingID != null)
                ((GPRSChargingIDImpl) this.chargingID).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_chargingID);

            if (this.locationInformationGPRS != null)
                ((LocationInformationGPRSImpl) this.locationInformationGPRS).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_locationInformationGPRS);

            if (this.endUserAddress != null)
                ((EndUserAddressImpl) this.endUserAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_endUserAddress);

            if (this.qualityOfService != null)
                ((QualityOfServiceImpl) this.qualityOfService).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_qualityOfService);

            if (this.timeAndTimezone != null)
                ((TimeAndTimezoneImpl) this.timeAndTimezone).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_timeAndTimezone);

            if (this.gsnAddress != null)
                ((GSNAddressImpl) this.gsnAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_gsnAddress);

        } catch (MAPException e) {
            throw new CAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        if (this.accessPointName != null) {
            sb.append("accessPointName=");
            sb.append(this.accessPointName.toString());
            sb.append(", ");
        }

        if (this.chargingID != null) {
            sb.append("chargingID=");
            sb.append(this.chargingID.toString());
            sb.append(", ");
        }

        if (this.locationInformationGPRS != null) {
            sb.append("locationInformationGPRS=");
            sb.append(this.locationInformationGPRS.toString());
            sb.append(", ");
        }

        if (this.endUserAddress != null) {
            sb.append("endUserAddress=");
            sb.append(this.endUserAddress.toString());
            sb.append(", ");
        }

        if (this.qualityOfService != null) {
            sb.append("qualityOfService=");
            sb.append(this.qualityOfService.toString());
            sb.append(", ");
        }

        if (this.timeAndTimezone != null) {
            sb.append("timeAndTimezone=");
            sb.append(this.timeAndTimezone.toString());
            sb.append(", ");
        }

        if (this.gsnAddress != null) {
            sb.append("gsnAddress=");
            sb.append(this.gsnAddress.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }

}
