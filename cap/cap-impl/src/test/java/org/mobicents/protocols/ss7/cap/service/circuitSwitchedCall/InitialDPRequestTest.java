/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.isup.CalledPartyNumberCap;
import org.mobicents.protocols.ss7.cap.api.isup.CauseCap;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CGEncountered;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Carrier;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.HoldTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwo;
import org.mobicents.protocols.ss7.cap.isup.BearerCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CallingPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;
import org.mobicents.protocols.ss7.cap.isup.LocationNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.OriginalCalledNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.RedirectingPartyIDCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.primitives.CalledPartyBCDNumberImpl;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.BearerCapabilityImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CarrierImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.IPSSPCapabilitiesImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InitialDPArgExtensionImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.mobicents.protocols.ss7.inap.isup.CallingPartysCategoryInapImpl;
import org.mobicents.protocols.ss7.inap.isup.HighLayerCompatibilityInapImpl;
import org.mobicents.protocols.ss7.inap.isup.RedirectionInformationInapImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.UserServiceInformationImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.UserTeleserviceInformationImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGIndex;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.service.callhandling.CallReferenceNumberImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.SubscriberStateImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CUGIndexImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CUGInterlockImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class InitialDPRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 107, (byte) 128, 1, 110, (byte) 130, 8, (byte) 131, (byte) 144, 33, 114, 16, (byte) 144, 0, 0,
                (byte) 131, 3, 3, (byte) 151, 87, (byte) 133, 1, 10, (byte) 140, 6, (byte) 131, 20, 7, 1, 9, 0, (byte) 187, 5,
                (byte) 128, 3, (byte) 128, (byte) 144, (byte) 163, (byte) 156, 1, 2, (byte) 157, 6, (byte) 131, 20, 7, 1, 9, 0,
                (byte) 158, 2, 3, 97, (byte) 159, 50, 8, 6, 7, (byte) 146, 9, 16, 4, (byte) 145, (byte) 249, (byte) 191, 53, 3,
                (byte) 131, 1, 17, (byte) 159, 54, 5, 19, (byte) 250, 61, 61, (byte) 234, (byte) 159, 55, 6, (byte) 145, 34,
                112, 87, 0, 112, (byte) 159, 57, 8, 2, 80, 17, 66, 49, 1, 101, 0, (byte) 191, 59, 8, (byte) 129, 6, (byte) 145,
                34, 112, 87, 0, 112 };
    }

    public byte[] getData2() {
        return new byte[] { 48, -127, -90, -128, 1, 110, -126, 8, -125, -112, 33, 114, 16, -112, 0, 0, -125, 3, 3, -105, 87,
                -123, 1, 10, -120, 1, 19, -118, 2, 22, 11, -116, 6, -125, 20, 7, 1, 9, 0, -81, 18, 48, 5, 2, 1, 2, -127, 0, 48,
                9, 2, 1, 3, 10, 1, 1, -127, 1, -1, -105, 2, 8, 9, -103, 2, 20, 2, -69, 5, -128, 3, -128, -112, -93, -100, 1, 2,
                -99, 6, -125, 20, 7, 1, 9, 0, -98, 2, 3, 97, -97, 50, 8, 6, 7, -110, 9, 16, 4, -111, -7, -65, 51, 2, -126, 0,
                -65, 52, 3, 2, 1, 111, -65, 53, 3, -125, 1, 17, -97, 54, 5, 19, -6, 61, 61, -22, -97, 55, 6, -111, 34, 112, 87,
                0, 112, -97, 56, 7, -111, 20, -121, 8, 80, 64, -9, -97, 57, 8, 2, 80, 17, 66, 49, 1, 101, 0, -97, 58, 0, -65,
                59, 8, -127, 6, -111, 34, 112, 87, 0, 112 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 48, (byte) 128, 1, 110, (byte) 130, 7, 4, 16, 17, 17, 34, 34, 102, (byte) 135, 1, 2, (byte) 145, 2, (byte) 196, (byte) 186,
                (byte) 191, 32, 4, (byte) 159, 50, 1, 2, (byte) 159, 37, 4, 1, 2, 3, 4, (byte) 159, 45, 2, 0, (byte) 211, (byte) 159, 46, 4, 11, 12, 13, 14,
                (byte) 159, 47, 0 };
    }

    public byte[] getDataCalledPartyNumber() {
        return new byte[] { -125, -112, 33, 114, 16, -112, 0, 0 };
    }

    public byte[] getCallingPartyNumber() {
        return new byte[] { 3, -105, 87 };
    }

    public byte[] getCallingPartysCategory() {
        return new byte[] { 10 };
    }

    public byte[] getOriginalCalledPartyID() {
        return new byte[] { -125, 20, 7, 1, 9, 0 };
    }

    public byte[] getBearerCapability() {
        return new byte[] { -128, -112, -93 };
    }

    public byte[] getRedirectingPartyID() {
        return new byte[] { -125, 20, 7, 1, 9, 0 };
    }

    public byte[] getRedirectionInformation() {
        return new byte[] { 3, 97 };
    }

    public byte[] getExtBasicServiceCode() {
        return new byte[] { 17 };
    }

    public byte[] getCallReferenceNumber() {
        return new byte[] { 19, -6, 61, 61, -22 };
    }

    public byte[] getLocationNumber() {
        return new byte[] { 22, 11 };
    }

    public byte[] getHighLayerCompatibility() {
        return new byte[] { 8, 9 };
    }

    public byte[] getAdditionalCallingPartyNumberCap() {
        return new byte[] { 20, 2 };
    }

    public byte[] getCalledPartyBCDNumber() {
        return new byte[] { (byte) 145, 20, (byte) 135, 8, 80, 64, (byte) 247 };
    }

    public byte[] getCarrier() {
        return new byte[] { 1, 2, 3, 4 };
    }

    public byte[] getCUGInterlock() {
        return new byte[] { 11, 12, 13, 14 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        InitialDPRequestImpl elem = new InitialDPRequestImpl(false);
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getServiceKey(), 110);
        assertTrue(Arrays.equals(elem.getCalledPartyNumber().getData(), getDataCalledPartyNumber()));
        assertTrue(Arrays.equals(elem.getCallingPartyNumber().getData(), getCallingPartyNumber()));
        assertTrue(Arrays.equals(elem.getCallingPartysCategory().getData(), getCallingPartysCategory()));
        assertTrue(Arrays.equals(elem.getOriginalCalledPartyID().getData(), getOriginalCalledPartyID()));
        assertTrue(Arrays.equals(elem.getBearerCapability().getBearerCap().getData(), getBearerCapability()));
        assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.collectedInfo);
        assertTrue(Arrays.equals(elem.getRedirectingPartyID().getData(), getRedirectingPartyID()));
        assertTrue(Arrays.equals(elem.getRedirectionInformation().getData(), getRedirectionInformation()));
        assertTrue(elem.getIMSI().getData().equals("607029900140199"));
        assertTrue(Arrays.equals(elem.getExtBasicServiceCode().getExtTeleservice().getData(), getExtBasicServiceCode()));
        assertTrue(Arrays.equals(elem.getCallReferenceNumber().getData(), getCallReferenceNumber()));
        assertEquals(elem.getMscAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getMscAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(elem.getMscAddress().getAddress().equals("2207750007"));
        assertEquals(elem.getTimeAndTimezone().getYear(), 2005);
        assertEquals(elem.getTimeAndTimezone().getMonth(), 11);
        assertEquals(elem.getTimeAndTimezone().getDay(), 24);
        assertEquals(elem.getTimeAndTimezone().getHour(), 13);
        assertEquals(elem.getTimeAndTimezone().getMinute(), 10);
        assertEquals(elem.getTimeAndTimezone().getSecond(), 56);
        assertEquals(elem.getTimeAndTimezone().getTimeZone(), 0);
        assertEquals(elem.getInitialDPArgExtension().getGmscAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getInitialDPArgExtension().getGmscAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(elem.getInitialDPArgExtension().getGmscAddress().getAddress().equals("2207750007"));
        assertFalse(elem.getCallForwardingSSPending());
        assertNull(elem.getCGEncountered());
        assertNull(elem.getCause());
        assertNull(elem.getServiceInteractionIndicatorsTwo());
        assertNull(elem.getCarrier());
        assertNull(elem.getCugIndex());
        assertNull(elem.getCugInterlock());
        assertFalse(elem.getCugOutgoingAccess());


        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new InitialDPRequestImpl(false);
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getServiceKey(), 110);
        assertTrue(Arrays.equals(elem.getCalledPartyNumber().getData(), getDataCalledPartyNumber()));
        assertTrue(Arrays.equals(elem.getCallingPartyNumber().getData(), getCallingPartyNumber()));
        assertTrue(Arrays.equals(elem.getCallingPartysCategory().getData(), getCallingPartysCategory()));
        assertTrue(Arrays.equals(elem.getOriginalCalledPartyID().getData(), getOriginalCalledPartyID()));
        assertTrue(Arrays.equals(elem.getBearerCapability().getBearerCap().getData(), getBearerCapability()));
        assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.collectedInfo);
        assertTrue(Arrays.equals(elem.getRedirectingPartyID().getData(), getRedirectingPartyID()));
        assertTrue(Arrays.equals(elem.getRedirectionInformation().getData(), getRedirectionInformation()));
        assertTrue(elem.getIMSI().getData().equals("607029900140199"));
        assertTrue(Arrays.equals(elem.getExtBasicServiceCode().getExtTeleservice().getData(), getExtBasicServiceCode()));
        assertTrue(Arrays.equals(elem.getCallReferenceNumber().getData(), getCallReferenceNumber()));
        assertEquals(elem.getMscAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getMscAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(elem.getMscAddress().getAddress().equals("2207750007"));
        assertEquals(elem.getTimeAndTimezone().getYear(), 2005);
        assertEquals(elem.getTimeAndTimezone().getMonth(), 11);
        assertEquals(elem.getTimeAndTimezone().getDay(), 24);
        assertEquals(elem.getTimeAndTimezone().getHour(), 13);
        assertEquals(elem.getTimeAndTimezone().getMinute(), 10);
        assertEquals(elem.getTimeAndTimezone().getSecond(), 56);
        assertEquals(elem.getTimeAndTimezone().getTimeZone(), 0);
        assertEquals(elem.getInitialDPArgExtension().getGmscAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getInitialDPArgExtension().getGmscAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(elem.getInitialDPArgExtension().getGmscAddress().getAddress().equals("2207750007"));

        assertTrue(elem.getIPSSPCapabilities().getIPRoutingAddressSupported());
        assertTrue(elem.getIPSSPCapabilities().getVoiceBackSupported());
        assertFalse(elem.getIPSSPCapabilities().getVoiceInformationSupportedViaSpeechRecognition());
        assertFalse(elem.getIPSSPCapabilities().getVoiceInformationSupportedViaVoiceRecognition());
        assertTrue(elem.getIPSSPCapabilities().getGenerationOfVoiceAnnouncementsFromTextSupported());
        assertNull(elem.getIPSSPCapabilities().getExtraData());
        assertTrue(Arrays.equals(elem.getLocationNumber().getData(), getLocationNumber()));
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertTrue(Arrays.equals(elem.getHighLayerCompatibility().getData(), getHighLayerCompatibility()));
        assertTrue(Arrays.equals(elem.getAdditionalCallingPartyNumber().getData(), getAdditionalCallingPartyNumberCap()));
        assertEquals(elem.getSubscriberState().getSubscriberStateChoice(), SubscriberStateChoice.notProvidedFromVLR);
        assertNull(elem.getSubscriberState().getNotReachableReason());
        assertEquals((int) elem.getLocationInformation().getAgeOfLocationInformation(), 111);
        assertTrue(Arrays.equals(elem.getCalledPartyBCDNumber().getData(), getCalledPartyBCDNumber()));
        assertTrue(elem.getCallForwardingSSPending());

        assertNull(elem.getCGEncountered());
        assertNull(elem.getCause());
        assertNull(elem.getServiceInteractionIndicatorsTwo());
        assertNull(elem.getCarrier());
        assertNull(elem.getCugIndex());
        assertNull(elem.getCugInterlock());
        assertFalse(elem.getCugOutgoingAccess());


        data = this.getData3();
        ais = new AsnInputStream(data);
        elem = new InitialDPRequestImpl(false);
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getServiceKey(), 110);

        assertEquals(elem.getCalledPartyNumber().getCalledPartyNumber().getAddress(), "1111222266");
        assertEquals(elem.getCalledPartyNumber().getCalledPartyNumber().getInternalNetworkNumberIndicator(), 0);
        assertEquals(elem.getCalledPartyNumber().getCalledPartyNumber().getNatureOfAddressIndicator(), CalledPartyNumber._NAI_INTERNATIONAL_NUMBER);
        assertEquals(elem.getCalledPartyNumber().getCalledPartyNumber().getNumberingPlanIndicator(), CalledPartyNumber._NPI_ISDN);

        assertNull(elem.getCallingPartyNumber());
        assertNull(elem.getCallingPartysCategory());
        assertNull(elem.getOriginalCalledPartyID());
        assertNull(elem.getBearerCapability());
        assertNull(elem.getEventTypeBCSM());
        assertNull(elem.getRedirectingPartyID());
        assertNull(elem.getRedirectionInformation());
        assertNull(elem.getIMSI());
        assertNull(elem.getExtBasicServiceCode());
        assertNull(elem.getCallReferenceNumber());
        assertNull(elem.getMscAddress());
        assertNull(elem.getTimeAndTimezone());
        assertNull(elem.getInitialDPArgExtension());
        assertFalse(elem.getCallForwardingSSPending());

        assertEquals(elem.getCGEncountered(), CGEncountered.scpOverload);
        assertEquals(elem.getCause().getCauseIndicators().getCauseValue(), CauseIndicators._CV_BEARER_CAPABILITY_NOT_AVAILABLE);
        assertEquals(elem.getCause().getCauseIndicators().getCodingStandard(), CauseIndicators._CODING_STANDARD_NATIONAL);
        assertEquals(elem.getCause().getCauseIndicators().getLocation(), CauseIndicators._LOCATION_PUBLIC_NSRU);
        assertEquals(elem.getCause().getCauseIndicators().getRecommendation(), 0);
        assertEquals(elem.getServiceInteractionIndicatorsTwo().getHoldTreatmentIndicator(), HoldTreatmentIndicator.rejectHoldRequest);
        assertEquals(elem.getCarrier().getData(), getCarrier());
        assertEquals(elem.getCugIndex().getData(), 211);
        assertEquals(elem.getCugInterlock().getData(), getCUGInterlock());
        assertTrue(elem.getCugOutgoingAccess());
     // CGEncountered
     // Cause
     // serviceInteractionIndicatorsTwo
     // carrier
     // cugIndex
     // cugInterlock
     // cugOutgoingAccess
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        CalledPartyNumberCapImpl calledPartyNumber = new CalledPartyNumberCapImpl(getDataCalledPartyNumber());
        CallingPartyNumberCapImpl callingPartyNumber = new CallingPartyNumberCapImpl(getCallingPartyNumber());
        CallingPartysCategoryInapImpl callingPartysCategory = new CallingPartysCategoryInapImpl(getCallingPartysCategory());
        OriginalCalledNumberCapImpl originalCalledPartyID = new OriginalCalledNumberCapImpl(getOriginalCalledPartyID());
        BearerCapImpl bearerCap = new BearerCapImpl(getBearerCapability());
        BearerCapabilityImpl bearerCapability = new BearerCapabilityImpl(bearerCap);
        RedirectingPartyIDCapImpl redirectingPartyID = new RedirectingPartyIDCapImpl(getRedirectingPartyID());
        RedirectionInformationInapImpl redirectionInformation = new RedirectionInformationInapImpl(getRedirectionInformation());
        IMSIImpl imsi = new IMSIImpl("607029900140199");
        ExtTeleserviceCodeImpl extTeleservice = new ExtTeleserviceCodeImpl(getExtBasicServiceCode());
        ExtBasicServiceCodeImpl extBasicServiceCode = new ExtBasicServiceCodeImpl(extTeleservice);
        CallReferenceNumberImpl callReferenceNumber = new CallReferenceNumberImpl(getCallReferenceNumber());
        ISDNAddressStringImpl mscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "2207750007");
        TimeAndTimezoneImpl timeAndTimezone = new TimeAndTimezoneImpl(2005, 11, 24, 13, 10, 56, 0);
        ISDNAddressStringImpl gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "2207750007");
        InitialDPArgExtensionImpl initialDPArgExtension = new InitialDPArgExtensionImpl(gmscAddress, null, null, null, null,
                null, null, null, null, null, null, false, null, false, false, false);

        InitialDPRequestImpl elem = new InitialDPRequestImpl(110, calledPartyNumber, callingPartyNumber, callingPartysCategory,
                null, null, null, originalCalledPartyID, null, null, null, bearerCapability, EventTypeBCSM.collectedInfo,
                redirectingPartyID, redirectionInformation, null, null, null, null, null, false, imsi, null, null,
                extBasicServiceCode, callReferenceNumber, mscAddress, null, timeAndTimezone, false, initialDPArgExtension,
                false);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData1());


        IPSSPCapabilitiesImpl IPSSPCapabilities = new IPSSPCapabilitiesImpl(true, true, false, false, true, null);
        LocationNumberCapImpl locationNumber = new LocationNumberCapImpl(getLocationNumber());
        HighLayerCompatibilityInapImpl highLayerCompatibility = new HighLayerCompatibilityInapImpl(getHighLayerCompatibility());
        DigitsImpl additionalCallingPartyNumber = new DigitsImpl(getAdditionalCallingPartyNumberCap());
        SubscriberStateImpl subscriberState = new SubscriberStateImpl(SubscriberStateChoice.notProvidedFromVLR, null);
        LocationInformationImpl locationInformation = new LocationInformationImpl(111, null, null, null, null, null, null,
                null, null, false, false, null, null);
        CalledPartyBCDNumberImpl calledPartyBCDNumber = new CalledPartyBCDNumberImpl(getCalledPartyBCDNumber());

        elem = new InitialDPRequestImpl(110, calledPartyNumber, callingPartyNumber, callingPartysCategory, null,
                IPSSPCapabilities, locationNumber, originalCalledPartyID, CAPExtensionsTest.createTestCAPExtensions(),
                highLayerCompatibility, additionalCallingPartyNumber, bearerCapability, EventTypeBCSM.collectedInfo,
                redirectingPartyID, redirectionInformation, null, null, null, null, null, false, imsi, subscriberState,
                locationInformation, extBasicServiceCode, callReferenceNumber, mscAddress, calledPartyBCDNumber,
                timeAndTimezone, true, initialDPArgExtension, false);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        byte[] a1 = this.getData2();
        byte[] a2 = aos.toByteArray();
        assertEquals(a1, a2);


        CalledPartyNumber calledPartyNumber2 = new CalledPartyNumberImpl();
        calledPartyNumber2.setAddress("1111222266");
        calledPartyNumber2.setInternalNetworkNumberIndicator(0);
        calledPartyNumber2.setNatureOfAddresIndicator(CalledPartyNumber._NAI_INTERNATIONAL_NUMBER);
        calledPartyNumber2.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
        CalledPartyNumberCap calledPartyNumberCap2 = new CalledPartyNumberCapImpl(calledPartyNumber2);
        CauseIndicators causeIndicators = new CauseIndicatorsImpl();
        causeIndicators.setCauseValue(CauseIndicators._CV_BEARER_CAPABILITY_NOT_AVAILABLE);
        causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_NATIONAL);
        causeIndicators.setLocation(CauseIndicators._LOCATION_PUBLIC_NSRU);
        causeIndicators.setRecommendation(0);
        CauseCap cause = new CauseCapImpl(causeIndicators);
        ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl(null, null, null, null, false,
                HoldTreatmentIndicator.rejectHoldRequest, null, null);
        Carrier carrier = new CarrierImpl(getCarrier());
        CUGIndex cugIndex = new CUGIndexImpl(211);
        CUGInterlock cugInterlock = new CUGInterlockImpl(getCUGInterlock());
        elem = new InitialDPRequestImpl(110, calledPartyNumberCap2, null, null, CGEncountered.scpOverload, null, null, null, null, null, null, null, null,
                null, null, cause, serviceInteractionIndicatorsTwo, carrier, cugIndex, cugInterlock, true, null, null, null, null, null, null, null, null,
                false, null, false);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        a1 = this.getData3();
        a2 = aos.toByteArray();
        assertEquals(a1, a2);
        // CGEncountered
        // Cause
        // serviceInteractionIndicatorsTwo
        // carrier
        // cugIndex
        // cugInterlock
        // cugOutgoingAccess

        // int serviceKey, CalledPartyNumberCap calledPartyNumber, CallingPartyNumberCap callingPartyNumber,
        // CallingPartysCategoryInap callingPartysCategory, CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities,
        // LocationNumberCap locationNumber, OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
        // HighLayerCompatibilityInap highLayerCompatibility, AdditionalCallingPartyNumberCap additionalCallingPartyNumber,
        // BearerCapability bearerCapability,
        // EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap
        // redirectionInformation, CauseCap cause,
        // ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex, CUGInterlock
        // cugInterlock,
        // boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState, LocationInformation locationInformation,
        // ExtBasicServiceCode extBasicServiceCode, CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress,
        // CalledPartyBCDNumber calledPartyBCDNumber, TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending,
        // InitialDPArgExtension initialDPArgExtension, boolean isCAPVersion3orLater

    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        CalledPartyNumberCapImpl calledPartyNumber = new CalledPartyNumberCapImpl(getDataCalledPartyNumber());
        CallingPartyNumberCapImpl callingPartyNumber = new CallingPartyNumberCapImpl(getCallingPartyNumber());
        CallingPartysCategoryInapImpl callingPartysCategory = new CallingPartysCategoryInapImpl(getCallingPartysCategory());
        OriginalCalledNumberCapImpl originalCalledPartyID = new OriginalCalledNumberCapImpl(getOriginalCalledPartyID());
        UserServiceInformationImpl original0 = new UserServiceInformationImpl();
        original0.setCodingStandart(UserServiceInformation._CS_INTERNATIONAL);
        original0.setInformationTransferCapability(UserServiceInformation._ITS_VIDEO);
        original0.setTransferMode(UserServiceInformation._TM_PACKET);
        original0.setInformationTransferRate(UserServiceInformation._ITR_64x2);
        BearerCapImpl bc = new BearerCapImpl(original0);
        BearerCapabilityImpl bearerCapability = new BearerCapabilityImpl(bc);
        RedirectingPartyIDCapImpl redirectingPartyID = new RedirectingPartyIDCapImpl(getRedirectingPartyID());
        RedirectionInformationInapImpl redirectionInformation = new RedirectionInformationInapImpl(getRedirectionInformation());
        IMSIImpl imsi = new IMSIImpl("607029900140199");
        ExtTeleserviceCodeImpl extTeleservice = new ExtTeleserviceCodeImpl(getExtBasicServiceCode());
        ExtBasicServiceCodeImpl extBasicServiceCode = new ExtBasicServiceCodeImpl(extTeleservice);
        CallReferenceNumberImpl callReferenceNumber = new CallReferenceNumberImpl(getCallReferenceNumber());
        ISDNAddressStringImpl mscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "2207750007");
        TimeAndTimezoneImpl timeAndTimezone = new TimeAndTimezoneImpl(2005, 11, 24, 13, 10, 56, 0);
        ISDNAddressStringImpl gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "2207750007");
        InitialDPArgExtensionImpl initialDPArgExtension = new InitialDPArgExtensionImpl(gmscAddress, null, null, null, null,
                null, null, null, null, null, null, false, null, false, false, false);
        IPSSPCapabilitiesImpl IPSSPCapabilities = new IPSSPCapabilitiesImpl(true, true, false, false, true, null);
        LocationNumberImpl ln = new LocationNumberImpl(LocationNumber._NAI_NATIONAL_SN, "12345333111",
                LocationNumber._NPI_TELEX, LocationNumber._INN_ROUTING_NOT_ALLOWED, LocationNumber._APRI_ALLOWED,
                LocationNumber._SI_USER_PROVIDED_VERIFIED_PASSED);
        LocationNumberCapImpl locationNumber = new LocationNumberCapImpl(ln);
        UserTeleserviceInformationImpl uti = new UserTeleserviceInformationImpl(
                UserTeleserviceInformation._CODING_STANDARD_NATIONAL, UserTeleserviceInformation._INTERPRETATION_FHGCI,
                UserTeleserviceInformation._PRESENTATION_METHOD_HLPP, UserTeleserviceInformation._HLCI_IVTI);
        HighLayerCompatibilityInapImpl highLayerCompatibility = new HighLayerCompatibilityInapImpl(uti);
        GenericNumberImpl gn = new GenericNumberImpl(GenericNumber._NAI_NATIONAL_SN, "12345",
                GenericNumber._NQIA_CONNECTED_NUMBER, GenericNumber._NPI_TELEX, GenericNumber._APRI_ALLOWED,
                GenericNumber._NI_INCOMPLETE, GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        DigitsImpl additionalCallingPartyNumber = new DigitsImpl(gn);
        SubscriberStateImpl subscriberState = new SubscriberStateImpl(SubscriberStateChoice.notProvidedFromVLR, null);
        LocationInformationImpl locationInformation = new LocationInformationImpl(111, null, null, null, null, null, null,
                null, null, false, false, null, null);
        CalledPartyBCDNumberImpl calledPartyBCDNumber = new CalledPartyBCDNumberImpl(getCalledPartyBCDNumber());

        InitialDPRequestImpl original = new InitialDPRequestImpl(110, calledPartyNumber, callingPartyNumber,
                callingPartysCategory, CGEncountered.manualCGencountered, IPSSPCapabilities, locationNumber,
                originalCalledPartyID, CAPExtensionsTest.createTestCAPExtensions(), highLayerCompatibility,
                additionalCallingPartyNumber, bearerCapability, EventTypeBCSM.collectedInfo, redirectingPartyID,
                redirectionInformation, null, null, null, null, null, false, imsi, subscriberState, locationInformation,
                extBasicServiceCode, callReferenceNumber, mscAddress, calledPartyBCDNumber, timeAndTimezone, true,
                initialDPArgExtension, false);
        original.setInvokeId(24);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "initialDP", InitialDPRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        InitialDPRequestImpl copy = reader.read("initialDP", InitialDPRequestImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
        assertEquals(copy.getServiceKey(), original.getServiceKey());
        assertEquals(copy.getCalledPartyNumber().getData(), original.getCalledPartyNumber().getData());
        assertEquals(copy.getCallingPartyNumber().getData(), original.getCallingPartyNumber().getData());
        assertEquals(copy.getCallingPartysCategory().getData(), original.getCallingPartysCategory().getData());
        assertEquals(copy.getCGEncountered(), original.getCGEncountered());
        assertEquals(copy.getIPSSPCapabilities().getIPRoutingAddressSupported(), original.getIPSSPCapabilities()
                .getIPRoutingAddressSupported());
        assertEquals(copy.getIPSSPCapabilities().getVoiceInformationSupportedViaSpeechRecognition(), original
                .getIPSSPCapabilities().getVoiceInformationSupportedViaSpeechRecognition());
        assertEquals(copy.getLocationNumber().getData(), original.getLocationNumber().getData());
        assertEquals(copy.getOriginalCalledPartyID().getData(), original.getOriginalCalledPartyID().getData());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(copy.getExtensions()));
        assertEquals(copy.getHighLayerCompatibility().getData(), original.getHighLayerCompatibility().getData());
        assertEquals(copy.getAdditionalCallingPartyNumber().getData(), original.getAdditionalCallingPartyNumber().getData());
        assertEquals(copy.getBearerCapability().getBearerCap().getData(), original.getBearerCapability().getBearerCap()
                .getData());
        assertEquals(copy.getEventTypeBCSM(), original.getEventTypeBCSM());
        assertEquals(copy.getRedirectingPartyID().getData(), original.getRedirectingPartyID().getData());
        assertEquals(copy.getRedirectionInformation().getData(), original.getRedirectionInformation().getData());
        assertEquals(copy.getIMSI().getData(), original.getIMSI().getData());
        assertEquals(copy.getSubscriberState().getSubscriberStateChoice(), original.getSubscriberState()
                .getSubscriberStateChoice());
        assertEquals((int) copy.getLocationInformation().getAgeOfLocationInformation(), (int) original.getLocationInformation()
                .getAgeOfLocationInformation());
        assertEquals(copy.getExtBasicServiceCode().getExtTeleservice().getData(), original.getExtBasicServiceCode()
                .getExtTeleservice().getData());
        assertEquals(copy.getCallReferenceNumber().getData(), original.getCallReferenceNumber().getData());
        assertEquals(copy.getMscAddress().getAddress(), original.getMscAddress().getAddress());
        assertEquals(copy.getCalledPartyBCDNumber().getData(), original.getCalledPartyBCDNumber().getData());
        assertEquals(copy.getTimeAndTimezone().getYear(), original.getTimeAndTimezone().getYear());
        assertEquals(copy.getTimeAndTimezone().getMonth(), original.getTimeAndTimezone().getMonth());
        assertEquals(copy.getTimeAndTimezone().getDay(), original.getTimeAndTimezone().getDay());
        assertEquals(copy.getCallForwardingSSPending(), original.getCallForwardingSSPending());
        assertEquals(copy.getInitialDPArgExtension().getGmscAddress().getAddress(), original.getInitialDPArgExtension()
                .getGmscAddress().getAddress());


        original = new InitialDPRequestImpl(110, calledPartyNumber, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, false,
                null, false);
        original.setInvokeId(24);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "initialDP", InitialDPRequestImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("initialDP", InitialDPRequestImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
        assertEquals(copy.getServiceKey(), original.getServiceKey());
        assertEquals(copy.getCalledPartyNumber().getData(), original.getCalledPartyNumber().getData());
        assertNull(copy.getCallingPartyNumber());
        assertNull(copy.getCallingPartysCategory());
        assertNull(copy.getAdditionalCallingPartyNumber());
        assertFalse(copy.getCallForwardingSSPending());


        CalledPartyNumber calledPartyNumber2 = new CalledPartyNumberImpl();
        calledPartyNumber2.setAddress("1111222266");
        calledPartyNumber2.setInternalNetworkNumberIndicator(0);
        calledPartyNumber2.setNatureOfAddresIndicator(CalledPartyNumber._NAI_INTERNATIONAL_NUMBER);
        calledPartyNumber2.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
        CalledPartyNumberCap calledPartyNumberCap2 = new CalledPartyNumberCapImpl(calledPartyNumber2);
        CauseIndicators causeIndicators = new CauseIndicatorsImpl();
        causeIndicators.setCauseValue(CauseIndicators._CV_BEARER_CAPABILITY_NOT_AVAILABLE);
        causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_NATIONAL);
        causeIndicators.setLocation(CauseIndicators._LOCATION_PUBLIC_NSRU);
        causeIndicators.setRecommendation(0);
        CauseCap cause = new CauseCapImpl(causeIndicators);
        ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl(null, null, null, null, false,
                HoldTreatmentIndicator.rejectHoldRequest, null, null);
        Carrier carrier = new CarrierImpl(getCarrier());
        CUGIndex cugIndex = new CUGIndexImpl(211);
        CUGInterlock cugInterlock = new CUGInterlockImpl(getCUGInterlock());
        original = new InitialDPRequestImpl(110, calledPartyNumberCap2, null, null, CGEncountered.scpOverload, null, null, null, null, null, null, null, null,
                null, null, cause, serviceInteractionIndicatorsTwo, carrier, cugIndex, cugInterlock, true, null, null, null, null, null, null, null, null,
                false, null, false);
        original.setInvokeId(12);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "initialDP", InitialDPRequestImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("initialDP", InitialDPRequestImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
        assertEquals(copy.getServiceKey(), original.getServiceKey());

        assertEquals(copy.getCalledPartyNumber().getData(), original.getCalledPartyNumber().getData());
        assertEquals(copy.getCGEncountered(), original.getCGEncountered());
        assertEquals(copy.getCause().getCauseIndicators().getCauseValue(), original.getCause().getCauseIndicators().getCauseValue());
        assertEquals(copy.getCause().getCauseIndicators().getCodingStandard(), original.getCause().getCauseIndicators().getCodingStandard());
        assertEquals(copy.getCause().getCauseIndicators().getLocation(), original.getCause().getCauseIndicators().getLocation());
        assertEquals(copy.getServiceInteractionIndicatorsTwo().getHoldTreatmentIndicator(), original.getServiceInteractionIndicatorsTwo().getHoldTreatmentIndicator());
        assertEquals(copy.getCarrier().getData(), original.getCarrier().getData());
        assertEquals(copy.getCugIndex().getData(), original.getCugIndex().getData());
        assertEquals(copy.getCugInterlock().getData(), original.getCugInterlock().getData());
        assertEquals(copy.getCugOutgoingAccess(), original.getCugOutgoingAccess());
    }
}
