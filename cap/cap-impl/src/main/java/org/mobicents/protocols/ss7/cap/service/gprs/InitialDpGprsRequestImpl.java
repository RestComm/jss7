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
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.gprs.InitialDpGprsRequest;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.SGSNCapabilities;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.AccessPointNameImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.EndUserAddressImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.QualityOfServiceImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.SGSNCapabilitiesImpl;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GPRSChargingIDImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GPRSMSClassImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.RAIdentityImpl;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class InitialDpGprsRequestImpl extends GprsMessageImpl implements InitialDpGprsRequest {

    public static final String _PrimitiveName = "InitialDpGprsRequest";

    public static final int _ID_serviceKey = 0;
    public static final int _ID_gprsEventType = 1;
    public static final int _ID_msisdn = 2;
    public static final int _ID_imsi = 3;
    public static final int _ID_timeAndTimezone = 4;
    public static final int _ID_gprsMSClass = 5;
    public static final int _ID_endUserAddress = 6;
    public static final int _ID_qualityOfService = 7;
    public static final int _ID_accessPointName = 8;
    public static final int _ID_routeingAreaIdentity = 9;
    public static final int _ID_chargingID = 10;
    public static final int _ID_sgsnCapabilities = 11;
    public static final int _ID_locationInformationGPRS = 12;
    public static final int _ID_pdpInitiationType = 13;
    public static final int _ID_extensions = 14;
    public static final int _ID_gsnAddress = 15;
    public static final int _ID_secondaryPDPContext = 16;
    public static final int _ID_imei = 17;

    private int serviceKey;
    private GPRSEventType gprsEventType;
    private ISDNAddressString msisdn;
    private IMSI imsi;
    private TimeAndTimezone timeAndTimezone;
    private GPRSMSClass gprsMSClass;
    private EndUserAddress endUserAddress;
    private QualityOfService qualityOfService;
    private AccessPointName accessPointName;
    private RAIdentity routeingAreaIdentity;
    private GPRSChargingID chargingID;
    private SGSNCapabilities sgsnCapabilities;
    private LocationInformationGPRS locationInformationGPRS;
    private PDPInitiationType pdpInitiationType;
    private CAPExtensions extensions;
    private GSNAddress gsnAddress;
    private boolean secondaryPDPContext;
    private IMEI imei;

    public InitialDpGprsRequestImpl() {
        super();
    }

    public InitialDpGprsRequestImpl(int serviceKey, GPRSEventType gprsEventType, ISDNAddressString msisdn, IMSI imsi,
            TimeAndTimezone timeAndTimezone, GPRSMSClass gprsMSClass, EndUserAddress endUserAddress,
            QualityOfService qualityOfService, AccessPointName accessPointName, RAIdentity routeingAreaIdentity,
            GPRSChargingID chargingID, SGSNCapabilities sgsnCapabilities, LocationInformationGPRS locationInformationGPRS,
            PDPInitiationType pdpInitiationType, CAPExtensions extensions, GSNAddress gsnAddress, boolean secondaryPDPContext,
            IMEI imei) {
        super();
        this.serviceKey = serviceKey;
        this.gprsEventType = gprsEventType;
        this.msisdn = msisdn;
        this.imsi = imsi;
        this.timeAndTimezone = timeAndTimezone;
        this.gprsMSClass = gprsMSClass;
        this.endUserAddress = endUserAddress;
        this.qualityOfService = qualityOfService;
        this.accessPointName = accessPointName;
        this.routeingAreaIdentity = routeingAreaIdentity;
        this.chargingID = chargingID;
        this.sgsnCapabilities = sgsnCapabilities;
        this.locationInformationGPRS = locationInformationGPRS;
        this.pdpInitiationType = pdpInitiationType;
        this.extensions = extensions;
        this.gsnAddress = gsnAddress;
        this.secondaryPDPContext = secondaryPDPContext;
        this.imei = imei;
    }

    @Override
    public int getServiceKey() {
        return this.serviceKey;
    }

    @Override
    public GPRSEventType getGPRSEventType() {
        return this.gprsEventType;
    }

    @Override
    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    @Override
    public IMSI getImsi() {
        return this.imsi;
    }

    @Override
    public TimeAndTimezone getTimeAndTimezone() {
        return this.timeAndTimezone;
    }

    @Override
    public GPRSMSClass getGPRSMSClass() {
        return this.gprsMSClass;
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
    public AccessPointName getAccessPointName() {
        return this.accessPointName;
    }

    @Override
    public RAIdentity getRouteingAreaIdentity() {
        return this.routeingAreaIdentity;
    }

    @Override
    public GPRSChargingID getChargingID() {
        return this.chargingID;
    }

    @Override
    public SGSNCapabilities getSGSNCapabilities() {
        return this.sgsnCapabilities;
    }

    @Override
    public LocationInformationGPRS getLocationInformationGPRS() {
        return this.locationInformationGPRS;
    }

    @Override
    public PDPInitiationType getPDPInitiationType() {
        return this.pdpInitiationType;
    }

    @Override
    public CAPExtensions getExtensions() {
        return this.extensions;
    }

    @Override
    public GSNAddress getGSNAddress() {
        return this.gsnAddress;
    }

    @Override
    public boolean getSecondaryPDPContext() {
        return this.secondaryPDPContext;
    }

    @Override
    public IMEI getImei() {
        return this.imei;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.initialDPGPRS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.initialDPGPRS;
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

        this.serviceKey = -1;
        this.gprsEventType = null;
        this.msisdn = null;
        this.imsi = null;
        this.timeAndTimezone = null;
        this.gprsMSClass = null;
        this.endUserAddress = null;
        this.qualityOfService = null;
        this.accessPointName = null;
        this.routeingAreaIdentity = null;
        this.chargingID = null;
        this.sgsnCapabilities = null;
        this.locationInformationGPRS = null;
        this.pdpInitiationType = null;
        this.extensions = null;
        this.gsnAddress = null;
        this.secondaryPDPContext = false;
        this.imei = null;
        boolean isServiceKeyFound = false;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();

            if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                switch (tag) {
                    case _ID_serviceKey:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".serviceKey: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.serviceKey = (int) ais.readInteger();
                        isServiceKeyFound = true;
                        break;
                    case _ID_gprsEventType:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".gprsEventType: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        int i1 = (int) ais.readInteger();
                        this.gprsEventType = GPRSEventType.getInstance(i1);
                        break;
                    case _ID_msisdn:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".msisdn: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.msisdn = new ISDNAddressStringImpl();
                        ((ISDNAddressStringImpl) this.msisdn).decodeAll(ais);
                        break;
                    case _ID_imsi:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".imsi: Parameter is not primitive", CAPParsingComponentExceptionReason.MistypedParameter);
                        this.imsi = new IMSIImpl();
                        ((IMSIImpl) this.imsi).decodeAll(ais);
                        break;
                    case _ID_timeAndTimezone:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".timeAndTimezone: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.timeAndTimezone = new TimeAndTimezoneImpl();
                        ((TimeAndTimezoneImpl) this.timeAndTimezone).decodeAll(ais);
                        break;
                    case _ID_gprsMSClass:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".gprsMSClass: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.gprsMSClass = new GPRSMSClassImpl();
                        ((GPRSMSClassImpl) this.gprsMSClass).decodeAll(ais);
                        break;
                    case _ID_endUserAddress:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".endUserAddress: Parameter is primitive",
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
                    case _ID_accessPointName:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".accessPointName: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.accessPointName = new AccessPointNameImpl();
                        ((AccessPointNameImpl) this.accessPointName).decodeAll(ais);
                        break;
                    case _ID_routeingAreaIdentity:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".routeingAreaIdentity: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.routeingAreaIdentity = new RAIdentityImpl();
                        ((RAIdentityImpl) this.routeingAreaIdentity).decodeAll(ais);
                        break;
                    case _ID_chargingID:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".chargingID: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.chargingID = new GPRSChargingIDImpl();
                        ((GPRSChargingIDImpl) this.chargingID).decodeAll(ais);
                        break;
                    case _ID_sgsnCapabilities:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".sgsnCapabilities: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.sgsnCapabilities = new SGSNCapabilitiesImpl();
                        ((SGSNCapabilitiesImpl) this.sgsnCapabilities).decodeAll(ais);
                        break;

                    case _ID_locationInformationGPRS:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".locationInformationGPRS: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.locationInformationGPRS = new LocationInformationGPRSImpl();
                        ((LocationInformationGPRSImpl) this.locationInformationGPRS).decodeAll(ais);
                        break;
                    case _ID_pdpInitiationType:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".pdpInitiationType: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        int i2 = (int) ais.readInteger();
                        this.pdpInitiationType = PDPInitiationType.getInstance(i2);
                        break;
                    case _ID_extensions:
                        if (ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".extensions: Parameter is primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.extensions = new CAPExtensionsImpl();
                        ((CAPExtensionsImpl) this.extensions).decodeAll(ais);
                        break;
                    case _ID_gsnAddress:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".gsnAddress: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        this.gsnAddress = new GSNAddressImpl();
                        ((GSNAddressImpl) this.gsnAddress).decodeAll(ais);
                        break;
                    case _ID_secondaryPDPContext:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".secondaryPDPContext: Parameter is not primitive",
                                    CAPParsingComponentExceptionReason.MistypedParameter);
                        ais.readNull();
                        secondaryPDPContext = true;
                        break;
                    case _ID_imei:
                        if (!ais.isTagPrimitive())
                            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                                    + ".imei: Parameter is not primitive", CAPParsingComponentExceptionReason.MistypedParameter);
                        this.imei = new IMEIImpl();
                        ((IMEIImpl) this.imei).decodeAll(ais);
                        break;
                    default:
                        ais.advanceElement();
                        break;
                }
            } else {
                ais.advanceElement();
            }
        }

        if (this.gprsEventType == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": parameter gprsEventType is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (this.msisdn == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": parameter msisdn is mandatory but not found", CAPParsingComponentExceptionReason.MistypedParameter);

        if (this.imsi == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": parameter imsi is mandatory but not found", CAPParsingComponentExceptionReason.MistypedParameter);

        if (this.timeAndTimezone == null)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": parameter timeAndTimezone is mandatory but not found",
                    CAPParsingComponentExceptionReason.MistypedParameter);

        if (!isServiceKeyFound)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": parameter ServiceKey is mandatory but not found", CAPParsingComponentExceptionReason.MistypedParameter);
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

        if (this.gprsEventType == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": gprsEventType must not be null");

        if (this.msisdn == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": msisdn must not be null");

        if (this.imsi == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": imsi must not be null");

        if (this.timeAndTimezone == null)
            throw new CAPException("Error while encoding " + _PrimitiveName + ": timeAndTimezone must not be null");

        try {

            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_serviceKey, this.serviceKey);

            asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_gprsEventType, this.gprsEventType.getCode());

            ((ISDNAddressStringImpl) this.msisdn).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_msisdn);

            ((IMSIImpl) this.imsi).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_imsi);

            ((TimeAndTimezoneImpl) this.timeAndTimezone).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_timeAndTimezone);

            if (this.gprsMSClass != null)
                ((GPRSMSClassImpl) this.gprsMSClass).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_gprsMSClass);

            if (this.endUserAddress != null)
                ((EndUserAddressImpl) this.endUserAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_endUserAddress);

            if (this.qualityOfService != null)
                ((QualityOfServiceImpl) this.qualityOfService).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_qualityOfService);

            if (this.accessPointName != null)
                ((AccessPointNameImpl) this.accessPointName).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_accessPointName);

            if (this.routeingAreaIdentity != null)
                ((RAIdentityImpl) this.routeingAreaIdentity).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_routeingAreaIdentity);

            if (this.chargingID != null)
                ((GPRSChargingIDImpl) this.chargingID).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_chargingID);

            if (this.sgsnCapabilities != null)
                ((SGSNCapabilitiesImpl) this.sgsnCapabilities).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_sgsnCapabilities);

            if (this.locationInformationGPRS != null)
                ((LocationInformationGPRSImpl) this.locationInformationGPRS).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_locationInformationGPRS);

            if (this.pdpInitiationType != null)
                asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_pdpInitiationType, this.pdpInitiationType.getCode());

            if (this.extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);

            if (this.gsnAddress != null)
                ((GSNAddressImpl) this.gsnAddress).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_gsnAddress);

            if (this.secondaryPDPContext)
                asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_secondaryPDPContext);

            if (this.imei != null)
                ((IMEIImpl) this.imei).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_imei);

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (MAPException e) {
            throw new CAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName + " [");

        sb.append("serviceKey=");
        sb.append(this.serviceKey);
        sb.append(", ");

        if (this.gprsEventType != null) {
            sb.append("gprsEventType=");
            sb.append(this.gprsEventType.toString());
            sb.append(", ");
        }

        if (this.msisdn != null) {
            sb.append("msisdn=");
            sb.append(this.msisdn.toString());
            sb.append(", ");
        }

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(this.imsi.toString());
            sb.append(", ");
        }

        if (this.timeAndTimezone != null) {
            sb.append("timeAndTimezone=");
            sb.append(this.timeAndTimezone.toString());
            sb.append(", ");
        }

        if (this.gprsMSClass != null) {
            sb.append("gprsMSClass=");
            sb.append(this.gprsMSClass.toString());
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

        if (this.accessPointName != null) {
            sb.append("accessPointName=");
            sb.append(this.accessPointName.toString());
            sb.append(", ");
        }

        if (this.routeingAreaIdentity != null) {
            sb.append("routeingAreaIdentity=");
            sb.append(this.routeingAreaIdentity.toString());
            sb.append(", ");
        }

        if (this.chargingID != null) {
            sb.append("chargingID=");
            sb.append(this.chargingID.toString());
            sb.append(", ");
        }

        if (this.sgsnCapabilities != null) {
            sb.append("sgsnCapabilities=");
            sb.append(this.sgsnCapabilities.toString());
            sb.append(", ");
        }

        if (this.locationInformationGPRS != null) {
            sb.append("locationInformationGPRS=");
            sb.append(this.locationInformationGPRS.toString());
            sb.append(", ");
        }

        if (this.pdpInitiationType != null) {
            sb.append("pdpInitiationType=");
            sb.append(this.pdpInitiationType.toString());
            sb.append(", ");
        }

        if (this.extensions != null) {
            sb.append("extensions=");
            sb.append(this.extensions.toString());
            sb.append(", ");
        }

        if (this.gsnAddress != null) {
            sb.append("gsnAddress=");
            sb.append(this.gsnAddress.toString());
            sb.append(", ");
        }

        if (this.secondaryPDPContext) {
            sb.append("secondaryPDPContext ");
            sb.append(", ");
        }

        if (this.imei != null) {
            sb.append("imei=");
            sb.append(this.imei.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
