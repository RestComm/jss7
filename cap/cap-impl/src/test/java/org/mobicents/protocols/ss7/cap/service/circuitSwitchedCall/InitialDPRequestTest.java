/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.isup.BearerCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.CallingPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;
import org.mobicents.protocols.ss7.cap.isup.LocationNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.OriginalCalledNumberCapImpl;
import org.mobicents.protocols.ss7.cap.isup.RedirectingPartyIDCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.primitives.CalledPartyBCDNumberImpl;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.BearerCapabilityImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.IPSSPCapabilitiesImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InitialDPArgExtensionImpl;
import org.mobicents.protocols.ss7.inap.isup.CallingPartysCategoryInapImpl;
import org.mobicents.protocols.ss7.inap.isup.HighLayerCompatibilityInapImpl;
import org.mobicents.protocols.ss7.inap.isup.RedirectionInformationInapImpl;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.service.callhandling.CallReferenceNumberImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.SubscriberStateImpl;
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
		return new byte[] { 48, 107, (byte) 128, 1, 110, (byte) 130, 8, (byte) 131, (byte) 144, 33, 114, 16, (byte) 144, 0, 15, (byte) 131, 3, 3, (byte) 151, 87,
				(byte) 133, 1, 10, (byte) 140, 6, (byte) 131, 20, 7, 1, 9, 0, (byte) 187, 5, (byte) 128, 3, (byte) 128, (byte) 144, (byte) 163, (byte) 156, 1,
				2, (byte) 157, 6, (byte) 131, 20, 7, 1, 9, 0, (byte) 158, 2, 3, 97, (byte) 159, 50, 8, 6, 7, (byte) 146, 9, 16, 4, (byte) 145, (byte) 249,
				(byte) 191, 53, 3, (byte) 131, 1, 17, (byte) 159, 54, 5, 19, (byte) 250, 61, 61, (byte) 234, (byte) 159, 55, 6, (byte) 145, 34, 112, 87, 0,
				112, (byte) 159, 57, 8, 2, 80, 17, 66, 49, 1, 101, 0, (byte) 191, 59, 8, (byte) 129, 6, (byte) 145, 34, 112, 87, 0, 112 };
	}

	public byte[] getData2() {
		return new byte[] { 48, (byte) 129, (byte) 163, (byte) 128, 1, 110, (byte) 130, 8, (byte) 131, (byte) 144, 33, 114, 16, (byte) 144, 0, 15, (byte) 131,
				3, 3, (byte) 151, 87, (byte) 133, 1, 10, (byte) 136, 1, 19, (byte) 138, 2, 22, 11, (byte) 140, 6, (byte) 131, 20, 7, 1, 9, 0, (byte) 175, 18,
				48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255, (byte) 151, 2, 8, 9, (byte) 153, 2, 20, 2, (byte) 187, 5,
				(byte) 128, 3, (byte) 128, (byte) 144, (byte) 163, (byte) 156, 1, 2, (byte) 157, 6, (byte) 131, 20, 7, 1, 9, 0, (byte) 158, 2, 3, 97,
				(byte) 159, 50, 8, 6, 7, (byte) 146, 9, 16, 4, (byte) 145, (byte) 249, (byte) 191, 51, 2, (byte) 130, 0, (byte) 191, 52, 3, 2, 1, 111,
				(byte) 191, 53, 3, (byte) 131, 1, 17, (byte) 159, 54, 5, 19, (byte) 250, 61, 61, (byte) 234, (byte) 159, 55, 6, (byte) 145, 34, 112, 87, 0,
				112, (byte) 159, 56, 4, 1, 2, 3, 4, (byte) 159, 57, 8, 2, 80, 17, 66, 49, 1, 101, 0, (byte) 159, 58, 0, (byte) 191, 59, 8, (byte) 129, 6,
				(byte) 145, 34, 112, 87, 0, 112 };
	}

	public byte[] getDataCalledPartyNumber() {
		return new byte[] { -125, -112, 33, 114, 16, -112, 0, 15 };
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
		return new byte[] { 1, 2, 3, 4 };
	}

	@Test(groups = { "functional.decode","circuitSwitchedCall"})
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

		assertTrue(elem.getIPSSPCapabilities().IPRoutingAddressSupported());
		assertTrue(elem.getIPSSPCapabilities().VoiceBackSupported());
		assertFalse(elem.getIPSSPCapabilities().VoiceInformationSupportedViaSpeechRecognition());
		assertFalse(elem.getIPSSPCapabilities().VoiceInformationSupportedViaVoiceRecognition());
		assertTrue(elem.getIPSSPCapabilities().GenerationOfVoiceAnnouncementsFromTextSupported());
		assertNull(elem.getIPSSPCapabilities().getExtraData());
		assertTrue(Arrays.equals(elem.getLocationNumber().getData(), getLocationNumber()));
		assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
		assertTrue(Arrays.equals(elem.getHighLayerCompatibility().getData(), getHighLayerCompatibility()));
		assertTrue(Arrays.equals(elem.getAdditionalCallingPartyNumber().getData(), getAdditionalCallingPartyNumberCap()));
		assertEquals(elem.getSubscriberState().getSubscriberStateChoice(), SubscriberStateChoice.notProvidedFromVLR);
		assertNull(elem.getSubscriberState().getNotReachableReason());
		assertEquals((int)elem.getLocationInformation().getAgeOfLocationInformation(), 111);
		assertTrue(Arrays.equals(elem.getCalledPartyBCDNumber().getData(), getCalledPartyBCDNumber()));
		assertTrue(elem.getCallForwardingSSPending());
	}

	@Test(groups = { "functional.encode","circuitSwitchedCall"})
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
		ISDNAddressStringImpl mscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "2207750007");
		TimeAndTimezoneImpl timeAndTimezone = new TimeAndTimezoneImpl(2005,11,24,13,10,56,0);
		ISDNAddressStringImpl gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "2207750007");
		InitialDPArgExtensionImpl initialDPArgExtension = new InitialDPArgExtensionImpl(gmscAddress, null, null, null, null, null, null, null, null, null,
				null, false, null, false);
		
		InitialDPRequestImpl elem = new InitialDPRequestImpl(110, calledPartyNumber, callingPartyNumber, callingPartysCategory, null, null,
				null, originalCalledPartyID, null, null, null, bearerCapability, EventTypeBCSM.collectedInfo, redirectingPartyID, redirectionInformation, null,
				null, null, null, null, false, imsi, null, null, extBasicServiceCode, callReferenceNumber, mscAddress, null, timeAndTimezone, false,
				initialDPArgExtension, false);
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

		IPSSPCapabilitiesImpl IPSSPCapabilities = new IPSSPCapabilitiesImpl(true, true, false, false, true, null);
		LocationNumberCapImpl locationNumber = new LocationNumberCapImpl(getLocationNumber());
		HighLayerCompatibilityInapImpl highLayerCompatibility = new HighLayerCompatibilityInapImpl(getHighLayerCompatibility());
		DigitsImpl additionalCallingPartyNumber = new DigitsImpl(getAdditionalCallingPartyNumberCap());
		SubscriberStateImpl subscriberState = new SubscriberStateImpl(SubscriberStateChoice.notProvidedFromVLR, null);
		LocationInformationImpl locationInformation = new LocationInformationImpl(111, null, null, null, null, null, null, null, null, false, false, null, null);
		CalledPartyBCDNumberImpl calledPartyBCDNumber = new CalledPartyBCDNumberImpl(getCalledPartyBCDNumber());

		elem = new InitialDPRequestImpl(110, calledPartyNumber, callingPartyNumber, callingPartysCategory, null, IPSSPCapabilities, locationNumber,
				originalCalledPartyID, CAPExtensionsTest.createTestCAPExtensions(), highLayerCompatibility, additionalCallingPartyNumber, bearerCapability,
				EventTypeBCSM.collectedInfo, redirectingPartyID, redirectionInformation, null, null, null, null, null, false, imsi, subscriberState, locationInformation,
				extBasicServiceCode, callReferenceNumber, mscAddress, calledPartyBCDNumber, timeAndTimezone, true, initialDPArgExtension, false);
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		byte[] a1 = this.getData2();
		byte[] a2 = aos.toByteArray();
		assertTrue(Arrays.equals(a1, a2));
//		assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

//		int serviceKey, CalledPartyNumberCap calledPartyNumber, CallingPartyNumberCap callingPartyNumber,
//		CallingPartysCategoryInap callingPartysCategory, CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities,
//		LocationNumberCap locationNumber, OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
//		HighLayerCompatibilityInap highLayerCompatibility, AdditionalCallingPartyNumberCap additionalCallingPartyNumber, BearerCapability bearerCapability,
//		EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap redirectionInformation, CauseCap cause,
//		ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex, CUGInterlock cugInterlock,
//		boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState, LocationInformation locationInformation,
//		ExtBasicServiceCode extBasicServiceCode, CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress,
//		CalledPartyBCDNumber calledPartyBCDNumber, TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending,
//		InitialDPArgExtension initialDPArgExtension, boolean isCAPVersion3orLater
		
	}
}

