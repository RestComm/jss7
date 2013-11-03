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
import org.mobicents.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.AccessPointNameImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.EndUserAddressImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.QualityOfServiceImpl;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationGPRSImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class PDPContextEstablishmentSpecificInformationImpl extends SequenceBase implements
        PDPContextEstablishmentSpecificInformation {

    public static final int _ID_accessPointName = 0;
    public static final int _ID_endUserAddress = 1;
    public static final int _ID_qualityOfService = 2;
    public static final int _ID_locationInformationGPRS = 3;
    public static final int _ID_timeAndTimezone = 4;
    public static final int _ID_pdpInitiationType = 5;
    public static final int _ID_secondaryPDPContext = 6;

    public static final int _ID_PDPContextEstablishmentSpecificInformation = 4;

    private AccessPointName accessPointName;
    private EndUserAddress endUserAddress;
    private QualityOfService qualityOfService;
    private LocationInformationGPRS locationInformationGPRS;
    private TimeAndTimezone timeAndTimezone;
    private PDPInitiationType pdpInitiationType;
    private boolean secondaryPDPContext;

    public PDPContextEstablishmentSpecificInformationImpl() {
        super("PdpContextchangeOfPositionSpecificInformation");
    }

    public PDPContextEstablishmentSpecificInformationImpl(AccessPointName accessPointName, EndUserAddress endUserAddress,
            QualityOfService qualityOfService, LocationInformationGPRS locationInformationGPRS,
            TimeAndTimezone timeAndTimezone, PDPInitiationType pdpInitiationType, boolean secondaryPDPContext) {
        super("PdpContextchangeOfPositionSpecificInformation");
        this.accessPointName = accessPointName;
        this.endUserAddress = endUserAddress;
        this.qualityOfService = qualityOfService;
        this.locationInformationGPRS = locationInformationGPRS;
        this.timeAndTimezone = timeAndTimezone;
        this.pdpInitiationType = pdpInitiationType;
        this.secondaryPDPContext = secondaryPDPContext;
    }

    @Override
    public AccessPointName getAccessPointName() {
        return this.accessPointName;
    }

    @Override
    public PDPInitiationType getPDPInitiationType() {
        return this.pdpInitiationType;
    }

    @Override
    public boolean getSecondaryPDPContext() {
        return this.secondaryPDPContext;
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
        this.endUserAddress = null;
        this.qualityOfService = null;
        this.locationInformationGPRS = null;
        this.timeAndTimezone = null;
        this.pdpInitiationType = null;
        this.secondaryPDPContext = false;

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
                    case _ID_locationInformationGPRS:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".locationInformationGPRS: Parameter is  primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.locationInformationGPRS = new LocationInformationGPRSImpl();
                        ((LocationInformationGPRSImpl) this.locationInformationGPRS).decodeAll(ais);
                        break;
                    case _ID_timeAndTimezone:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".timeAndTimezone: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.timeAndTimezone = new TimeAndTimezoneImpl();
                        ((TimeAndTimezoneImpl) this.timeAndTimezone).decodeAll(ais);
                        break;
                    case _ID_pdpInitiationType:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".pdpInitiationType: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        int i1 = (int) ais.readInteger();
                        this.pdpInitiationType = PDPInitiationType.getInstance(i1);
                        break;
                    case _ID_secondaryPDPContext:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".secondaryPDPContext: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        this.secondaryPDPContext = true;
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

            if (this.endUserAddress != null)
                ((EndUserAddressImpl) this.endUserAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_endUserAddress);

            if (this.qualityOfService != null)
                ((QualityOfServiceImpl) this.qualityOfService).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_qualityOfService);

            if (this.locationInformationGPRS != null)
                ((LocationInformationGPRSImpl) this.locationInformationGPRS).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_locationInformationGPRS);

            if (this.timeAndTimezone != null)
                ((TimeAndTimezoneImpl) this.timeAndTimezone).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_timeAndTimezone);

            if (this.pdpInitiationType != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_pdpInitiationType, this.pdpInitiationType.getCode());

            if (this.secondaryPDPContext)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_secondaryPDPContext);

        } catch (MAPException e) {
            throw new CAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
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

        if (this.accessPointName != null) {
            sb.append("accessPointName=");
            sb.append(this.accessPointName.toString());
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

        if (this.pdpInitiationType != null) {
            sb.append("pdpInitiationType=");
            sb.append(this.pdpInitiationType.toString());
            sb.append(", ");
        }

        if (this.secondaryPDPContext) {
            sb.append("secondaryPDPContext=");
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }

}
