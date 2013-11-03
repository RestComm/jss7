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
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CallingPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.api.isup.LocationNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.OriginalCalledNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.RedirectingPartyIDCap;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapability;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CGEncountered;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilities;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CallingPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;
import org.mobicents.protocols.ss7.cap.isup.LocationNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.OriginalCalledNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.RedirectingPartyIDCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.mobicents.protocols.ss7.cap.primitives.CalledPartyBCDNumberImpl;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.BearerCapabilityImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.IPSSPCapabilitiesImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InitialDPArgExtensionImpl;
import org.mobicents.protocols.ss7.inap.api.INAPException;
import org.mobicents.protocols.ss7.inap.api.INAPParsingComponentException;
import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.mobicents.protocols.ss7.inap.isup.CallingPartysCategoryInapImpl;
import org.mobicents.protocols.ss7.inap.isup.HighLayerCompatibilityInapImpl;
import org.mobicents.protocols.ss7.inap.isup.RedirectionInformationInapImpl;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGIndex;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.service.callhandling.CallReferenceNumberImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.SubscriberStateImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class InitialDPRequestImpl extends CircuitSwitchedCallMessageImpl implements InitialDPRequest {

    public static final int _ID_serviceKey = 0;
    public static final int _ID_calledPartyNumber = 2;
    public static final int _ID_callingPartyNumber = 3;
    public static final int _ID_callingPartysCategory = 5;
    public static final int _ID_cGEncountered = 7;
    public static final int _ID_iPSSPCapabilities = 8;
    public static final int _ID_locationNumber = 10;
    public static final int _ID_originalCalledPartyID = 12;
    public static final int _ID_extensions = 15;
    public static final int _ID_highLayerCompatibility = 23;
    public static final int _ID_additionalCallingPartyNumber = 25;
    public static final int _ID_bearerCapability = 27;
    public static final int _ID_eventTypeBCSM = 28;
    public static final int _ID_redirectingPartyID = 29;
    public static final int _ID_redirectionInformation = 30;
    public static final int _ID_cause = 17;
    public static final int _ID_serviceInteractionIndicatorsTwo = 32;
    public static final int _ID_carrier = 37;
    public static final int _ID_cug_Index = 45;
    public static final int _ID_cug_Interlock = 46;
    public static final int _ID_cug_OutgoingAccess = 47;
    public static final int _ID_iMSI = 50;
    public static final int _ID_subscriberState = 51;
    public static final int _ID_locationInformation = 52;
    public static final int _ID_ext_basicServiceCode = 53;
    public static final int _ID_callReferenceNumber = 54;
    public static final int _ID_mscAddress = 55;
    public static final int _ID_calledPartyBCDNumber = 56;
    public static final int _ID_timeAndTimezone = 57;
    public static final int _ID_callForwardingSS_Pending = 58;
    public static final int _ID_initialDPArgExtension = 59;

    private static final String IS_CAP_VERSION_3_OR_LATER = "isCAPVersion3orLater";
    private static final String SERVICE_KEY = "serviceKey";
    private static final String CALLED_PARTY_NUMBER = "calledPartyNumber";
    private static final String CALLING_PARTY_NUMBER = "callingPartyNumber";
    private static final String CALLING_PARTYS_CATEGORY = "callingPartysCategory";
    private static final String CG_ENCOUNTERED = "cgEncountered";
    private static final String IPSSP_CAPABILITIES = "ipsspCapabilities";
    private static final String LOCATION_NUMBER = "locationNumber";
    private static final String ORIGINAL_CALLED_PARTY_ID = "originalCalledPartyID";
    private static final String EXTENSIONS = "extensions";
    private static final String HIGH_LAYER_COMPATIBILITY = "highLayerCompatibility";
    private static final String ADDITIONAL_CALLING_PARTY_NUMBER = "additionalCallingPartyNumber";
    private static final String BEARER_CAPABILITY = "bearerCapability";
    private static final String EVENT_TYPE_BCSM = "eventTypeBCSM";
    private static final String REDIRECTING_PARTY_ID = "redirectingPartyID";
    private static final String REDIRECTION_INFORMATION = "redirectionInformation";
    private static final String IMSI = "imsi";
    private static final String SUBSCRIBER_STATE = "subscriberState";
    private static final String LOCATION_INFORMATION = "locationInformation";
    private static final String EXT_BASIC_SERVICE_CODE = "extBasicServiceCode";
    private static final String CALL_REFERENCE_NUMBER = "callReferenceNumber";
    private static final String MSC_ADDRESS = "mscAddress";
    private static final String CALLED_PARTY_BCD_NUMBER = "calledPartyBCDNumber";
    private static final String TIME_AND_TIMEZONE = "timeAndTimezone";
    private static final String CALL_FORWARDING_SS_PENDING = "callForwardingSSPending";
    private static final String INITIAL_DP_ARG_EXTENSION = "initialDPArgExtension";

    public static final String _PrimitiveName = "InitialDPRequestIndication";

    private int serviceKey;
    private CalledPartyNumberCap calledPartyNumber;
    private CallingPartyNumberCap callingPartyNumber;
    private CallingPartysCategoryInap callingPartysCategory;
    private CGEncountered CGEncountered;
    private IPSSPCapabilities IPSSPCapabilities;
    private LocationNumberCap locationNumber;
    private OriginalCalledNumberCap originalCalledPartyID;
    private CAPExtensions extensions;
    private HighLayerCompatibilityInap highLayerCompatibility;
    private Digits additionalCallingPartyNumber;
    private BearerCapability bearerCapability;
    private EventTypeBCSM eventTypeBCSM;
    private RedirectingPartyIDCap redirectingPartyID;
    private RedirectionInformationInap redirectionInformation;
    private CauseCap cause;
    private ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo;
    private Carrier carrier;
    private CUGIndex cugIndex;
    private CUGInterlock cugInterlock;
    private boolean cugOutgoingAccess;
    private IMSI imsi;
    private SubscriberState subscriberState;
    private LocationInformation locationInformation;
    private ExtBasicServiceCode extBasicServiceCode;
    private CallReferenceNumber callReferenceNumber;
    private ISDNAddressString mscAddress;
    private CalledPartyBCDNumber calledPartyBCDNumber;
    private TimeAndTimezone timeAndTimezone;
    private boolean callForwardingSSPending;
    private InitialDPArgExtension initialDPArgExtension;

    private boolean isCAPVersion3orLater;

    /**
     * This constructor is only for deserialisation purpose
     */
    public InitialDPRequestImpl() {
    }

    public InitialDPRequestImpl(boolean isCAPVersion3orLater) {
        this.isCAPVersion3orLater = isCAPVersion3orLater;
    }

    public InitialDPRequestImpl(int serviceKey, CalledPartyNumberCap calledPartyNumber,
            CallingPartyNumberCap callingPartyNumber, CallingPartysCategoryInap callingPartysCategory,
            CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities, LocationNumberCap locationNumber,
            OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
            HighLayerCompatibilityInap highLayerCompatibility, Digits additionalCallingPartyNumber,
            BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID,
            RedirectionInformationInap redirectionInformation, CauseCap cause,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState,
            LocationInformation locationInformation, ExtBasicServiceCode extBasicServiceCode,
            CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress, CalledPartyBCDNumber calledPartyBCDNumber,
            TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending, InitialDPArgExtension initialDPArgExtension,
            boolean isCAPVersion3orLater) {
        this.serviceKey = serviceKey;
        this.calledPartyNumber = calledPartyNumber;
        this.callingPartyNumber = callingPartyNumber;
        this.callingPartysCategory = callingPartysCategory;
        this.CGEncountered = CGEncountered;
        this.IPSSPCapabilities = IPSSPCapabilities;
        this.locationNumber = locationNumber;
        this.originalCalledPartyID = originalCalledPartyID;
        this.extensions = extensions;
        this.highLayerCompatibility = highLayerCompatibility;
        this.additionalCallingPartyNumber = additionalCallingPartyNumber;
        this.bearerCapability = bearerCapability;
        this.eventTypeBCSM = eventTypeBCSM;
        this.redirectingPartyID = redirectingPartyID;
        this.redirectionInformation = redirectionInformation;
        this.cause = cause;
        this.serviceInteractionIndicatorsTwo = serviceInteractionIndicatorsTwo;
        this.carrier = carrier;
        this.cugIndex = cugIndex;
        this.cugInterlock = cugInterlock;
        this.cugOutgoingAccess = cugOutgoingAccess;
        this.imsi = imsi;
        this.subscriberState = subscriberState;
        this.locationInformation = locationInformation;
        this.extBasicServiceCode = extBasicServiceCode;
        this.callReferenceNumber = callReferenceNumber;
        this.mscAddress = mscAddress;
        this.calledPartyBCDNumber = calledPartyBCDNumber;
        this.timeAndTimezone = timeAndTimezone;
        this.callForwardingSSPending = callForwardingSSPending;
        this.initialDPArgExtension = initialDPArgExtension;
        this.isCAPVersion3orLater = isCAPVersion3orLater;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.initialDP_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.initialDP;
    }

    @Override
    public int getServiceKey() {
        return this.serviceKey;
    }

    @Override
    public CalledPartyNumberCap getCalledPartyNumber() {
        return this.calledPartyNumber;
    }

    @Override
    public CallingPartyNumberCap getCallingPartyNumber() {
        return callingPartyNumber;
    }

    @Override
    public CallingPartysCategoryInap getCallingPartysCategory() {
        return callingPartysCategory;
    }

    @Override
    public CGEncountered getCGEncountered() {
        return CGEncountered;
    }

    @Override
    public IPSSPCapabilities getIPSSPCapabilities() {
        return IPSSPCapabilities;
    }

    @Override
    public LocationNumberCap getLocationNumber() {
        return locationNumber;
    }

    @Override
    public OriginalCalledNumberCap getOriginalCalledPartyID() {
        return originalCalledPartyID;
    }

    @Override
    public CAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public HighLayerCompatibilityInap getHighLayerCompatibility() {
        return highLayerCompatibility;
    }

    @Override
    public Digits getAdditionalCallingPartyNumber() {
        return additionalCallingPartyNumber;
    }

    @Override
    public BearerCapability getBearerCapability() {
        return bearerCapability;
    }

    @Override
    public EventTypeBCSM getEventTypeBCSM() {
        return eventTypeBCSM;
    }

    @Override
    public RedirectingPartyIDCap getRedirectingPartyID() {
        return redirectingPartyID;
    }

    @Override
    public RedirectionInformationInap getRedirectionInformation() {
        return redirectionInformation;
    }

    @Override
    public CauseCap getCause() {
        return cause;
    }

    @Override
    public ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo() {
        return serviceInteractionIndicatorsTwo;
    }

    @Override
    public Carrier getCarrier() {
        return carrier;
    }

    @Override
    public CUGIndex getCugIndex() {
        return cugIndex;
    }

    @Override
    public CUGInterlock getCugInterlock() {
        return cugInterlock;
    }

    @Override
    public boolean getCugOutgoingAccess() {
        return cugOutgoingAccess;
    }

    @Override
    public IMSI getIMSI() {
        return imsi;
    }

    @Override
    public SubscriberState getSubscriberState() {
        return subscriberState;
    }

    @Override
    public LocationInformation getLocationInformation() {
        return locationInformation;
    }

    @Override
    public ExtBasicServiceCode getExtBasicServiceCode() {
        return extBasicServiceCode;
    }

    @Override
    public CallReferenceNumber getCallReferenceNumber() {
        return callReferenceNumber;
    }

    @Override
    public ISDNAddressString getMscAddress() {
        return mscAddress;
    }

    @Override
    public CalledPartyBCDNumber getCalledPartyBCDNumber() {
        return calledPartyBCDNumber;
    }

    @Override
    public TimeAndTimezone getTimeAndTimezone() {
        return timeAndTimezone;
    }

    @Override
    public boolean getCallForwardingSSPending() {
        return callForwardingSSPending;
    }

    @Override
    public InitialDPArgExtension getInitialDPArgExtension() {
        return initialDPArgExtension;
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
        } catch (INAPParsingComponentException e) {
            throw new CAPParsingComponentException("INAPParsingComponentException when decoding " + _PrimitiveName + ": "
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
        } catch (INAPParsingComponentException e) {
            throw new CAPParsingComponentException("INAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ansIS, int length) throws INAPParsingComponentException, CAPParsingComponentException,
            MAPParsingComponentException, IOException, AsnException {

        this.serviceKey = 0;
        this.calledPartyNumber = null;
        this.callingPartyNumber = null;
        this.callingPartysCategory = null;
        this.CGEncountered = null;
        this.IPSSPCapabilities = null;
        this.locationNumber = null;
        this.originalCalledPartyID = null;
        this.extensions = null;
        this.highLayerCompatibility = null;
        this.additionalCallingPartyNumber = null;
        this.bearerCapability = null;
        this.eventTypeBCSM = null;
        this.redirectingPartyID = null;
        this.redirectionInformation = null;
        this.cause = null;
        this.serviceInteractionIndicatorsTwo = null;
        this.carrier = null;
        this.cugIndex = null;
        this.cugInterlock = null;
        this.cugOutgoingAccess = false;
        this.imsi = null;
        this.subscriberState = null;
        this.locationInformation = null;
        this.extBasicServiceCode = null;
        this.callReferenceNumber = null;
        this.mscAddress = null;
        this.calledPartyBCDNumber = null;
        this.timeAndTimezone = null;
        this.callForwardingSSPending = false;
        this.initialDPArgExtension = null;

        AsnInputStream ais = ansIS.readSequenceStreamData(length);
        int num = 0;
        while (true) {
            if (ais.available() == 0)
                break;

            int tag = ais.readTag();
            int i1;

            switch (num) {
                case 0:
                    // serviceKey
                    if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || tag != _ID_serviceKey || !ais.isTagPrimitive())
                        throw new CAPParsingComponentException(
                                "Error while decoding InitialDPRequest: Parameter 0 bad tag or tag class or not primitive",
                                CAPParsingComponentExceptionReason.MistypedParameter);
                    this.serviceKey = (int) ais.readInteger();
                    break;

                default:
                if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    switch (tag) {
                    case _ID_calledPartyNumber:
                        this.calledPartyNumber = new CalledPartyNumberCapImpl();
                        ((CalledPartyNumberCapImpl) this.calledPartyNumber).decodeAll(ais);
                        break;
                    case _ID_callingPartyNumber:
                        this.callingPartyNumber = new CallingPartyNumberCapImpl();
                        ((CallingPartyNumberCapImpl) this.callingPartyNumber).decodeAll(ais);
                        break;
                    case _ID_callingPartysCategory:
                        this.callingPartysCategory = new CallingPartysCategoryInapImpl();
                        ((CallingPartysCategoryInapImpl) this.callingPartysCategory).decodeAll(ais);
                        break;
                    case _ID_cGEncountered:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_iPSSPCapabilities:
                        this.IPSSPCapabilities = new IPSSPCapabilitiesImpl();
                        ((IPSSPCapabilitiesImpl) this.IPSSPCapabilities).decodeAll(ais);
                        break;
                    case _ID_locationNumber:
                        this.locationNumber = new LocationNumberCapImpl();
                        ((LocationNumberCapImpl) this.locationNumber).decodeAll(ais);
                        break;
                    case _ID_originalCalledPartyID:
                        this.originalCalledPartyID = new OriginalCalledNumberCapImpl();
                        ((OriginalCalledNumberCapImpl) this.originalCalledPartyID).decodeAll(ais);
                        break;
                    case _ID_extensions:
                        this.extensions = new CAPExtensionsImpl();
                        ((CAPExtensionsImpl) this.extensions).decodeAll(ais);
                        break;
                    case _ID_highLayerCompatibility:
                        this.highLayerCompatibility = new HighLayerCompatibilityInapImpl();
                        ((HighLayerCompatibilityInapImpl) this.highLayerCompatibility).decodeAll(ais);
                        break;
                    case _ID_additionalCallingPartyNumber:
                        this.additionalCallingPartyNumber = new DigitsImpl();
                        ((DigitsImpl) this.additionalCallingPartyNumber).decodeAll(ais);
                        this.additionalCallingPartyNumber.setIsGenericNumber();
                        break;
                    case _ID_bearerCapability:
                        AsnInputStream ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        this.bearerCapability = new BearerCapabilityImpl();
                        ((BearerCapabilityImpl) this.bearerCapability).decodeAll(ais2);
                        break;
                    case _ID_eventTypeBCSM:
                        i1 = (int) ais.readInteger();
                        this.eventTypeBCSM = EventTypeBCSM.getInstance(i1);
                        break;
                    case _ID_redirectingPartyID:
                        this.redirectingPartyID = new RedirectingPartyIDCapImpl();
                        ((RedirectingPartyIDCapImpl) this.redirectingPartyID).decodeAll(ais);
                        break;
                    case _ID_redirectionInformation:
                        this.redirectionInformation = new RedirectionInformationInapImpl();
                        ((RedirectionInformationInapImpl) this.redirectionInformation).decodeAll(ais);
                        break;
                    case _ID_cause:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_serviceInteractionIndicatorsTwo:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_carrier:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_cug_Index:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_cug_Interlock:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_cug_OutgoingAccess:
                        ais.advanceElement(); // TODO: implement it
                        break;
                    case _ID_iMSI:
                        int len = ais.readLength();
                        if (len == 0) {
                            ais.advanceElementData(len);
                        } else {
                            this.imsi = new IMSIImpl();
                            ((IMSIImpl) this.imsi).decodeData(ais, len);
                        }
                        break;
                    case _ID_subscriberState:
                        ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        this.subscriberState = new SubscriberStateImpl();
                        ((SubscriberStateImpl) this.subscriberState).decodeAll(ais2);
                        break;
                    case _ID_locationInformation:
                        this.locationInformation = new LocationInformationImpl();
                        ((LocationInformationImpl) this.locationInformation).decodeAll(ais);
                        break;
                    case _ID_ext_basicServiceCode:
                        ais2 = ais.readSequenceStream();
                        ais2.readTag();
                        this.extBasicServiceCode = new ExtBasicServiceCodeImpl();
                        ((ExtBasicServiceCodeImpl) this.extBasicServiceCode).decodeAll(ais2);
                        break;
                    case _ID_callReferenceNumber:
                        this.callReferenceNumber = new CallReferenceNumberImpl();
                        ((CallReferenceNumberImpl) this.callReferenceNumber).decodeAll(ais);
                        break;
                    case _ID_mscAddress:
                        this.mscAddress = new ISDNAddressStringImpl();
                        ((ISDNAddressStringImpl) this.mscAddress).decodeAll(ais);
                        break;
                    case _ID_calledPartyBCDNumber:
                        this.calledPartyBCDNumber = new CalledPartyBCDNumberImpl();
                        ((CalledPartyBCDNumberImpl) this.calledPartyBCDNumber).decodeAll(ais);
                        break;
                    case _ID_timeAndTimezone:
                        this.timeAndTimezone = new TimeAndTimezoneImpl();
                        ((TimeAndTimezoneImpl) this.timeAndTimezone).decodeAll(ais);
                        break;
                    case _ID_callForwardingSS_Pending:
                        ais.readNull();
                        this.callForwardingSSPending = true;
                        break;
                    case _ID_initialDPArgExtension:
                        this.initialDPArgExtension = new InitialDPArgExtensionImpl(this.isCAPVersion3orLater);
                        ((InitialDPArgExtensionImpl) this.initialDPArgExtension).decodeAll(ais);
                        break;

                    default:
                        ais.advanceElement();
                        break;
                    }
                } else {
                    ais.advanceElement();
                }
                break;
            }

            num++;
        }

        if (num < 1)
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
                    + ": Needs at least 1 mandatory parameters, found " + num,
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

        try {
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_serviceKey, this.serviceKey);

            if (this.calledPartyNumber != null)
                ((CalledPartyNumberCapImpl) this.calledPartyNumber).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_calledPartyNumber);
            if (this.callingPartyNumber != null)
                ((CallingPartyNumberCapImpl) this.callingPartyNumber).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_callingPartyNumber);
            if (this.callingPartysCategory != null)
                ((CallingPartysCategoryInapImpl) this.callingPartysCategory).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_callingPartysCategory);
            if (this.CGEncountered != null) {
                // TODO: implement it - _ID_cGEncountered
            }
            if (this.IPSSPCapabilities != null)
                ((IPSSPCapabilitiesImpl) this.IPSSPCapabilities).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_iPSSPCapabilities);
            if (this.locationNumber != null)
                ((LocationNumberCapImpl) this.locationNumber).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_locationNumber);
            if (this.originalCalledPartyID != null)
                ((OriginalCalledNumberCapImpl) this.originalCalledPartyID).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_originalCalledPartyID);
            if (this.extensions != null)
                ((CAPExtensionsImpl) this.extensions).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensions);
            if (this.highLayerCompatibility != null)
                ((HighLayerCompatibilityInapImpl) this.highLayerCompatibility).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_highLayerCompatibility);
            if (this.additionalCallingPartyNumber != null)
                ((DigitsImpl) this.additionalCallingPartyNumber).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_additionalCallingPartyNumber);
            if (this.bearerCapability != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_bearerCapability);
                int pos = aos.StartContentDefiniteLength();
                ((BearerCapabilityImpl) this.bearerCapability).encodeAll(aos);
                aos.FinalizeContent(pos);
            }
            if (this.eventTypeBCSM != null)
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_eventTypeBCSM, this.eventTypeBCSM.getCode());
            if (this.redirectingPartyID != null)
                ((RedirectingPartyIDCapImpl) this.redirectingPartyID).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_redirectingPartyID);
            if (this.redirectionInformation != null)
                ((RedirectionInformationInapImpl) this.redirectionInformation).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_redirectionInformation);
            if (this.cause != null) {
                // TODO: implement it - _ID_cause
            }
            if (this.serviceInteractionIndicatorsTwo != null) {
                // TODO: implement it - _ID_serviceInteractionIndicatorsTwo
            }
            if (this.carrier != null) {
                // TODO: implement it - _ID_carrier
            }
            if (this.cugIndex != null) {
                // TODO: implement it - _ID_cug_Index
            }
            if (this.cugInterlock != null) {
                // TODO: implement it - _ID_cug_Interlock
            }
            if (this.cugOutgoingAccess) {
                // TODO: implement it - _ID_cug_OutgoingAccess
            }
            if (this.imsi != null)
                ((IMSIImpl) this.imsi).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_iMSI);
            if (this.subscriberState != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_subscriberState);
                int pos = aos.StartContentDefiniteLength();
                ((SubscriberStateImpl) this.subscriberState).encodeAll(aos);
                aos.FinalizeContent(pos);
            }
            if (this.locationInformation != null)
                ((LocationInformationImpl) this.locationInformation).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_locationInformation);
            if (this.extBasicServiceCode != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _ID_ext_basicServiceCode);
                int pos = aos.StartContentDefiniteLength();
                ((ExtBasicServiceCodeImpl) this.extBasicServiceCode).encodeAll(aos);
                aos.FinalizeContent(pos);
            }
            if (this.callReferenceNumber != null)
                ((CallReferenceNumberImpl) this.callReferenceNumber).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_callReferenceNumber);
            if (this.mscAddress != null)
                ((ISDNAddressStringImpl) this.mscAddress).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_mscAddress);
            if (this.calledPartyBCDNumber != null)
                ((CalledPartyBCDNumberImpl) this.calledPartyBCDNumber).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_calledPartyBCDNumber);
            if (this.timeAndTimezone != null)
                ((TimeAndTimezoneImpl) this.timeAndTimezone).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, _ID_timeAndTimezone);
            if (this.callForwardingSSPending)
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_callForwardingSS_Pending);
            if (this.initialDPArgExtension != null)
                ((InitialDPArgExtensionImpl) this.initialDPArgExtension).encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC,
                        _ID_initialDPArgExtension);

        } catch (IOException e) {
            throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (INAPException e) {
            throw new CAPException("INAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        } catch (MAPException e) {
            throw new CAPException("MAPException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        sb.append("serviceKey=");
        sb.append(serviceKey);
        if (this.calledPartyNumber != null) {
            sb.append(", calledPartyNumber=");
            sb.append(calledPartyNumber.toString());
        }
        if (this.callingPartyNumber != null) {
            sb.append(", callingPartyNumber=");
            sb.append(callingPartyNumber.toString());
        }
        if (this.callingPartysCategory != null) {
            sb.append(", callingPartysCategory=");
            sb.append(callingPartysCategory.toString());
        }
        if (this.CGEncountered != null) {
            sb.append(", CGEncountered=");
            sb.append(CGEncountered.toString());
        }
        if (this.IPSSPCapabilities != null) {
            sb.append(", IPSSPCapabilities=");
            sb.append(IPSSPCapabilities.toString());
        }
        if (this.locationNumber != null) {
            sb.append(", locationNumber=");
            sb.append(locationNumber.toString());
        }
        if (this.originalCalledPartyID != null) {
            sb.append(", originalCalledPartyID=");
            sb.append(originalCalledPartyID.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.highLayerCompatibility != null) {
            sb.append(", highLayerCompatibility=");
            sb.append(highLayerCompatibility.toString());
        }
        if (this.additionalCallingPartyNumber != null) {
            sb.append(", additionalCallingPartyNumber=");
            sb.append(additionalCallingPartyNumber.toString());
        }
        if (this.bearerCapability != null) {
            sb.append(", bearerCapability=");
            sb.append(bearerCapability.toString());
        }
        if (this.eventTypeBCSM != null) {
            sb.append(", eventTypeBCSM=");
            sb.append(eventTypeBCSM.toString());
        }
        if (this.redirectingPartyID != null) {
            sb.append(", redirectingPartyID=");
            sb.append(redirectingPartyID.toString());
        }
        if (this.redirectionInformation != null) {
            sb.append(", redirectionInformation=");
            sb.append(redirectionInformation.toString());
        }
        if (this.cause != null) {
            sb.append(", cause=");
            sb.append(cause.toString());
        }
        if (this.serviceInteractionIndicatorsTwo != null) {
            sb.append(", serviceInteractionIndicatorsTwo=");
            sb.append(serviceInteractionIndicatorsTwo.toString());
        }
        if (this.carrier != null) {
            sb.append(", carrier=");
            sb.append(carrier.toString());
        }
        if (this.cugIndex != null) {
            sb.append(", cugIndex=");
            sb.append(cugIndex.toString());
        }
        if (this.cugInterlock != null) {
            sb.append(", cugInterlock=");
            sb.append(cugInterlock.toString());
        }
        if (this.cugOutgoingAccess) {
            sb.append(", cugOutgoingAccess");
        }
        if (this.imsi != null) {
            sb.append(", imsi=");
            sb.append(imsi.toString());
        }
        if (this.subscriberState != null) {
            sb.append(", subscriberState=");
            sb.append(subscriberState.toString());
        }
        if (this.locationInformation != null) {
            sb.append(", locationInformation=");
            sb.append(locationInformation.toString());
        }
        if (this.extBasicServiceCode != null) {
            sb.append(", extBasicServiceCode=");
            sb.append(extBasicServiceCode.toString());
        }
        if (this.callReferenceNumber != null) {
            sb.append(", callReferenceNumber=");
            sb.append(callReferenceNumber.toString());
        }
        if (this.mscAddress != null) {
            sb.append(", mscAddress=");
            sb.append(mscAddress.toString());
        }
        if (this.calledPartyBCDNumber != null) {
            sb.append(", calledPartyBCDNumber=");
            sb.append(calledPartyBCDNumber.toString());
        }
        if (this.timeAndTimezone != null) {
            sb.append(", timeAndTimezone=");
            sb.append(timeAndTimezone.toString());
        }
        if (this.callForwardingSSPending) {
            sb.append(", callForwardingSSPending");
        }
        if (this.initialDPArgExtension != null) {
            sb.append(", initialDPArgExtension=");
            sb.append(initialDPArgExtension.toString());
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<InitialDPRequestImpl> INITIALDP_REQUEST_XML = new XMLFormat<InitialDPRequestImpl>(
            InitialDPRequestImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, InitialDPRequestImpl initialDP) throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.read(xml, initialDP);

            initialDP.isCAPVersion3orLater = xml.getAttribute(IS_CAP_VERSION_3_OR_LATER, false);

            initialDP.serviceKey = xml.get(SERVICE_KEY, Integer.class);
            initialDP.calledPartyNumber = xml.get(CALLED_PARTY_NUMBER, CalledPartyNumberCapImpl.class);
            initialDP.callingPartyNumber = xml.get(CALLING_PARTY_NUMBER, CallingPartyNumberCapImpl.class);
            initialDP.callingPartysCategory = xml.get(CALLING_PARTYS_CATEGORY, CallingPartysCategoryInapImpl.class);

            String str = xml.get(CG_ENCOUNTERED, String.class);
            if (str != null)
                initialDP.CGEncountered = Enum.valueOf(CGEncountered.class, str);
            initialDP.IPSSPCapabilities = xml.get(IPSSP_CAPABILITIES, IPSSPCapabilitiesImpl.class);
            initialDP.locationNumber = xml.get(LOCATION_NUMBER, LocationNumberCapImpl.class);
            initialDP.originalCalledPartyID = xml.get(ORIGINAL_CALLED_PARTY_ID, OriginalCalledNumberCapImpl.class);
            initialDP.extensions = xml.get(EXTENSIONS, CAPExtensionsImpl.class);

            initialDP.highLayerCompatibility = xml.get(HIGH_LAYER_COMPATIBILITY, HighLayerCompatibilityInapImpl.class);
            initialDP.additionalCallingPartyNumber = xml.get(ADDITIONAL_CALLING_PARTY_NUMBER, DigitsImpl.class);
            initialDP.bearerCapability = xml.get(BEARER_CAPABILITY, BearerCapabilityImpl.class);
            str = xml.get(EVENT_TYPE_BCSM, String.class);
            if (str != null)
                initialDP.eventTypeBCSM = Enum.valueOf(EventTypeBCSM.class, str);
            initialDP.redirectingPartyID = xml.get(REDIRECTING_PARTY_ID, RedirectingPartyIDCapImpl.class);

            initialDP.redirectionInformation = xml.get(REDIRECTION_INFORMATION, RedirectionInformationInapImpl.class);
            initialDP.imsi = xml.get(IMSI, IMSIImpl.class);
            initialDP.subscriberState = xml.get(SUBSCRIBER_STATE, SubscriberStateImpl.class);
            initialDP.locationInformation = xml.get(LOCATION_INFORMATION, LocationInformationImpl.class);
            initialDP.extBasicServiceCode = xml.get(EXT_BASIC_SERVICE_CODE, ExtBasicServiceCodeImpl.class);

            initialDP.callReferenceNumber = xml.get(CALL_REFERENCE_NUMBER, CallReferenceNumberImpl.class);
            initialDP.mscAddress = xml.get(MSC_ADDRESS, ISDNAddressStringImpl.class);
            initialDP.calledPartyBCDNumber = xml.get(CALLED_PARTY_BCD_NUMBER, CalledPartyBCDNumberImpl.class);
            initialDP.timeAndTimezone = xml.get(TIME_AND_TIMEZONE, TimeAndTimezoneImpl.class);
            Boolean bval = xml.get(CALL_FORWARDING_SS_PENDING, Boolean.class);
            if (bval != null)
                initialDP.callForwardingSSPending = bval;
            initialDP.initialDPArgExtension = xml.get(INITIAL_DP_ARG_EXTENSION, InitialDPArgExtensionImpl.class);
        }

        @Override
        public void write(InitialDPRequestImpl initialDP, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            CIRCUIT_SWITCHED_CALL_MESSAGE_XML.write(initialDP, xml);

            xml.setAttribute(IS_CAP_VERSION_3_OR_LATER, initialDP.isCAPVersion3orLater);

            xml.add((Integer) initialDP.getServiceKey(), SERVICE_KEY, Integer.class);
            if (initialDP.getCalledPartyNumber() != null)
                xml.add((CalledPartyNumberCapImpl) initialDP.getCalledPartyNumber(), CALLED_PARTY_NUMBER,
                        CalledPartyNumberCapImpl.class);
            if (initialDP.getCallingPartyNumber() != null)
                xml.add((CallingPartyNumberCapImpl) initialDP.getCallingPartyNumber(), CALLING_PARTY_NUMBER,
                        CallingPartyNumberCapImpl.class);
            if (initialDP.getCallingPartysCategory() != null)
                xml.add((CallingPartysCategoryInapImpl) initialDP.getCallingPartysCategory(), CALLING_PARTYS_CATEGORY,
                        CallingPartysCategoryInapImpl.class);

            if (initialDP.getCGEncountered() != null)
                xml.add((String) initialDP.getCGEncountered().toString(), CG_ENCOUNTERED, String.class);
            if (initialDP.getIPSSPCapabilities() != null)
                xml.add((IPSSPCapabilitiesImpl) initialDP.getIPSSPCapabilities(), IPSSP_CAPABILITIES,
                        IPSSPCapabilitiesImpl.class);
            if (initialDP.getLocationNumber() != null)
                xml.add((LocationNumberCapImpl) initialDP.getLocationNumber(), LOCATION_NUMBER, LocationNumberCapImpl.class);
            if (initialDP.getOriginalCalledPartyID() != null)
                xml.add((OriginalCalledNumberCapImpl) initialDP.getOriginalCalledPartyID(), ORIGINAL_CALLED_PARTY_ID,
                        OriginalCalledNumberCapImpl.class);
            if (initialDP.getExtensions() != null)
                xml.add((CAPExtensionsImpl) initialDP.getExtensions(), EXTENSIONS, CAPExtensionsImpl.class);

            if (initialDP.getHighLayerCompatibility() != null)
                xml.add((HighLayerCompatibilityInapImpl) initialDP.getHighLayerCompatibility(), HIGH_LAYER_COMPATIBILITY,
                        HighLayerCompatibilityInapImpl.class);
            if (initialDP.getAdditionalCallingPartyNumber() != null)
                xml.add((DigitsImpl) initialDP.getAdditionalCallingPartyNumber(), ADDITIONAL_CALLING_PARTY_NUMBER,
                        DigitsImpl.class);
            if (initialDP.getBearerCapability() != null)
                xml.add((BearerCapabilityImpl) initialDP.getBearerCapability(), BEARER_CAPABILITY, BearerCapabilityImpl.class);
            if (initialDP.getEventTypeBCSM() != null)
                xml.add((String) initialDP.getEventTypeBCSM().toString(), EVENT_TYPE_BCSM, String.class);
            if (initialDP.getRedirectingPartyID() != null)
                xml.add((RedirectingPartyIDCapImpl) initialDP.getRedirectingPartyID(), REDIRECTING_PARTY_ID,
                        RedirectingPartyIDCapImpl.class);

            if (initialDP.getRedirectionInformation() != null)
                xml.add((RedirectionInformationInapImpl) initialDP.getRedirectionInformation(), REDIRECTION_INFORMATION,
                        RedirectionInformationInapImpl.class);
            if (initialDP.getIMSI() != null)
                xml.add((IMSIImpl) initialDP.getIMSI(), IMSI, IMSIImpl.class);
            if (initialDP.getSubscriberState() != null)
                xml.add((SubscriberStateImpl) initialDP.getSubscriberState(), SUBSCRIBER_STATE, SubscriberStateImpl.class);
            if (initialDP.getLocationInformation() != null)
                xml.add((LocationInformationImpl) initialDP.getLocationInformation(), LOCATION_INFORMATION,
                        LocationInformationImpl.class);
            if (initialDP.getExtBasicServiceCode() != null)
                xml.add((ExtBasicServiceCodeImpl) initialDP.getExtBasicServiceCode(), EXT_BASIC_SERVICE_CODE,
                        ExtBasicServiceCodeImpl.class);

            if (initialDP.getCallReferenceNumber() != null)
                xml.add((CallReferenceNumberImpl) initialDP.getCallReferenceNumber(), CALL_REFERENCE_NUMBER,
                        CallReferenceNumberImpl.class);
            if (initialDP.getMscAddress() != null)
                xml.add((ISDNAddressStringImpl) initialDP.getMscAddress(), MSC_ADDRESS, ISDNAddressStringImpl.class);
            if (initialDP.getCalledPartyBCDNumber() != null)
                xml.add((CalledPartyBCDNumberImpl) initialDP.getCalledPartyBCDNumber(), CALLED_PARTY_BCD_NUMBER,
                        CalledPartyBCDNumberImpl.class);
            if (initialDP.getTimeAndTimezone() != null)
                xml.add((TimeAndTimezoneImpl) initialDP.getTimeAndTimezone(), TIME_AND_TIMEZONE, TimeAndTimezoneImpl.class);
            if (initialDP.getCallForwardingSSPending())
                xml.add((Boolean) initialDP.getCallForwardingSSPending(), CALL_FORWARDING_SS_PENDING, Boolean.class);
            if (initialDP.getInitialDPArgExtension() != null)
                xml.add((InitialDPArgExtensionImpl) initialDP.getInitialDPArgExtension(), INITIAL_DP_ARG_EXTENSION,
                        InitialDPArgExtensionImpl.class);
        }
    };
}
