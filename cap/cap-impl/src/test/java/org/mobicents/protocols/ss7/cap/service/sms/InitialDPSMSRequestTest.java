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
package org.mobicents.protocols.ss7.cap.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.CalledPartyBCDNumber;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPDataCodingScheme;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPProtocolIdentifier;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPShortMessageSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriod;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.primitives.CalledPartyBCDNumberImpl;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.SMSAddressStringImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.TPDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.TPProtocolIdentifierImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.TPShortMessageSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.service.sms.primitive.TPValidityPeriodImpl;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.mobicents.protocols.ss7.map.service.callhandling.CallReferenceNumberImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GPRSMSClassImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GeodeticInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.MSClassmark2Impl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.MSNetworkCapabilityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.MSRadioAccessCapabilityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.RAIdentityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;
import org.testng.annotations.Test;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class InitialDPSMSRequestTest {

    public byte[] getData() {
        return new byte[] { 48, -126, 1, 15, -128, 1, 2, -127, 7, -111, 20, -121, 8, 80, 64, -9, -126, 9, -111, 33, 67,
                101, -121, 25, 50, 84, 118, -125, 1, 3, -124, 5, 17, 17, 33, 34, 34, -91, 3, 2, 1, 111, -90, 57, -96,
                7, -127, 5, 82, -16, 16, 17, 92, -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 31, 32, 33, 34, 35, 36, 37,
                38, -125, 4, -111, 86, 52, 18, -124, 3, 91, 92, 93, -122, 0, -121, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                -120, 0, -119, 1, 13, -121, 6, -111, 34, 112, 87, 0, 112, -120, 8, 2, 80, 17, 66, 49, 1, 101, 0, -119,
                1, 5, -118, 1, 5, -117, 1, 5, -116, 6, 1, 2, 3, 4, 5, 6, -83, 18, 48, 5, 2, 1, 2, -127, 0, 48, 9, 2, 1,
                3, 10, 1, 1, -127, 1, -1, -114, 6, 1, 2, 3, 4, 5, 6, -113, 6, -111, 34, 112, 87, 0, -128, -112, 6,
                -111, 34, 112, 87, 0, -112, -111, 3, 1, 2, 3, -90, 57, -96, 7, -127, 5, 82, -16, 16, 17, 92, -127, 6,
                11, 12, 13, 14, 15, 16, -126, 8, 31, 32, 33, 34, 35, 36, 37, 38, -125, 4, -111, 86, 52, 18, -124, 3,
                91, 92, 93, -122, 0, -121, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -120, 0, -119, 1, 13, -78, 11, -128, 3,
                1, 2, 3, -127, 4, 11, 22, 33, 44, -109, 8, 17, 34, 51, 68, 85, 102, 119, -120, -108, 6, -111, 34, 112,
                87, 0, 1 };
    };

    private byte[] getGeographicalInformation() {
        return new byte[] { 31, 32, 33, 34, 35, 36, 37, 38 };
    }

    private byte[] getEncodedDataRAIdentity() {
        return new byte[] { 11, 12, 13, 14, 15, 16 };
    }

    private byte[] getEncodedDataLSAIdentity() {
        return new byte[] { 91, 92, 93 };
    }

    private byte[] getGeodeticInformation() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    }

    private byte[] getTPValidityPeriod() {
        return new byte[] { 1, 2, 3, 4, 5, 6 };
    }

    private byte[] getCallReferenceNumber() {
        return new byte[] { 1, 2, 3, 4, 5, 6 };
    }

    private byte[] getMSClassmark2() {
        return new byte[] { 1, 2, 3 };
    }

    private byte[] getEncodedDataNetworkCapability() {
        return new byte[] { 1, 2, 3 };
    }

    private byte[] getEncodedDataRadioAccessCapability() {
        return new byte[] { 11, 22, 33, 44 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        InitialDPSMSRequestImpl prim = new InitialDPSMSRequestImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        int serviceKey = prim.getServiceKey();
        CalledPartyBCDNumber destinationSubscriberNumber = prim.getDestinationSubscriberNumber();
        SMSAddressString callingPartyNumber = prim.getCallingPartyNumber();
        EventTypeSMS eventTypeSMS = prim.getEventTypeSMS();
        IMSI imsi = prim.getImsi();
        LocationInformation locationInformationMSC = prim.getLocationInformationMSC();
        // LocationInformationGPRS locationInformationGPRS =
        // prim.getLocationInformationGPRS();
        ISDNAddressString smscCAddress = prim.getSMSCAddress();
        // TimeAndTimezone timeAndTimezone = prim.getTimeAndTimezone();
        TPShortMessageSpecificInfo tPShortMessageSpecificInfo = prim.getTPShortMessageSpecificInfo();
        TPProtocolIdentifier tPProtocolIdentifier = prim.getTPProtocolIdentifier();
        TPDataCodingScheme tPDataCodingScheme = prim.getTPDataCodingScheme();
        TPValidityPeriod tPValidityPeriod = prim.getTPValidityPeriod();
        // CAPExtensions extensions = prim.getExtensions();
        CallReferenceNumber smsReferenceNumber = prim.getSmsReferenceNumber();
        ISDNAddressString mscAddress = prim.getMscAddress();
        ISDNAddressString sgsnNumber = prim.getSgsnNumber();
        MSClassmark2 mSClassmark2 = prim.getMSClassmark2();
        GPRSMSClass gprsMSClass = prim.getGPRSMSClass();
        IMEI imei = prim.getImei();
        ISDNAddressString calledPartyNumber = prim.getCalledPartyNumber();

        assertEquals(serviceKey, 2);
        assertNotNull(destinationSubscriberNumber);
        assertTrue(destinationSubscriberNumber.getAddress().equals("41788005047"));

        assertNotNull(callingPartyNumber);
        assertTrue(callingPartyNumber.getAddress().equals("1234567891234567"));

        assertEquals(eventTypeSMS, EventTypeSMS.oSmsSubmission);

        // getImsi
        assertTrue(imsi.getData().equals("1111122222"));

        assertNotNull(locationInformationMSC);

        // locationInformationGPRS
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength()
                .getMCC(), 250);
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength()
                .getMNC(), 1);
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength()
                .getLac(), 4444);
        assertTrue(Arrays.equals(prim.getLocationInformationGPRS().getRouteingAreaIdentity().getData(),
                this.getEncodedDataRAIdentity()));
        assertTrue(Arrays.equals(prim.getLocationInformationGPRS().getGeographicalInformation().getData(),
                this.getGeographicalInformation()));
        assertTrue(prim.getLocationInformationGPRS().getSGSNNumber().getAddress().equals("654321"));
        assertTrue(Arrays.equals(prim.getLocationInformationGPRS().getLSAIdentity().getData(),
                this.getEncodedDataLSAIdentity()));
        assertTrue(prim.getLocationInformationGPRS().isSaiPresent());
        assertTrue(Arrays.equals(prim.getLocationInformationGPRS().getGeodeticInformation().getData(),
                this.getGeodeticInformation()));
        assertTrue(prim.getLocationInformationGPRS().isCurrentLocationRetrieved());
        assertEquals((int) prim.getLocationInformationGPRS().getAgeOfLocationInformation(), 13);

        assertNotNull(smscCAddress);
        assertTrue(smscCAddress.getAddress().equals("2207750007"));

        // gprsMSClass
        assertTrue(Arrays
                .equals(gprsMSClass.getMSNetworkCapability().getData(), this.getEncodedDataNetworkCapability()));
        assertTrue(Arrays.equals(gprsMSClass.getMSRadioAccessCapability().getData(),
                this.getEncodedDataRadioAccessCapability()));

        // getTimeAndTimezone
        assertEquals(prim.getTimeAndTimezone().getYear(), 2005);
        assertEquals(prim.getTimeAndTimezone().getMonth(), 11);
        assertEquals(prim.getTimeAndTimezone().getDay(), 24);
        assertEquals(prim.getTimeAndTimezone().getHour(), 13);
        assertEquals(prim.getTimeAndTimezone().getMinute(), 10);
        assertEquals(prim.getTimeAndTimezone().getSecond(), 56);
        assertEquals(prim.getTimeAndTimezone().getTimeZone(), 0);

        assertEquals(tPShortMessageSpecificInfo.getData(), 5);

        assertEquals(tPProtocolIdentifier.getData(), 5);

        assertEquals(tPDataCodingScheme.getData(), 5);

        assertTrue(Arrays.equals(tPValidityPeriod.getData(), this.getTPValidityPeriod()));

        // extensions
        CAPExtensionsTest.checkTestCAPExtensions(prim.getExtensions());

        assertTrue(Arrays.equals(smsReferenceNumber.getData(), this.getCallReferenceNumber()));

        assertNotNull(mscAddress);
        assertTrue(mscAddress.getAddress().equals("2207750008"));

        assertNotNull(sgsnNumber);
        assertTrue(sgsnNumber.getAddress().equals("2207750009"));

        // getImei
        assertTrue(prim.getImei().getIMEI().equals("1122334455667788"));

        assertNotNull(calledPartyNumber);
        assertTrue(calledPartyNumber.getAddress().equals("2207750010"));

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        int serviceKey = 2;
        CalledPartyBCDNumber destinationSubscriberNumber = new CalledPartyBCDNumberImpl(
                AddressNature.international_number, NumberingPlan.ISDN, "41788005047");
        SMSAddressString callingPartyNumber = new SMSAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "1234567891234567");
        EventTypeSMS eventTypeSMS = EventTypeSMS.oSmsSubmission;
        IMSI imsi = new IMSIImpl("1111122222");
        LocationInformation locationInformationMSC = new LocationInformationImpl(111, null, null, null, null, null,
                null, null, null, false, false, null, null);

        // locationInformationGPRS
        LAIFixedLengthImpl lai = new LAIFixedLengthImpl(250, 1, 4444);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
        RAIdentityImpl ra = new RAIdentityImpl(this.getEncodedDataRAIdentity());
        GeographicalInformationImpl ggi = new GeographicalInformationImpl(this.getGeographicalInformation());
        ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "654321");
        LSAIdentityImpl lsa = new LSAIdentityImpl(this.getEncodedDataLSAIdentity());
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(this.getGeodeticInformation());
        LocationInformationGPRS locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra, ggi, sgsn, lsa,
                null, true, gdi, true, 13);

        ISDNAddressString smscCAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "2207750007");
        TimeAndTimezone timeAndTimezone = new TimeAndTimezoneImpl(2005, 11, 24, 13, 10, 56, 0);
        TPShortMessageSpecificInfo tPShortMessageSpecificInfo = new TPShortMessageSpecificInfoImpl(5);
        TPProtocolIdentifier tPProtocolIdentifier = new TPProtocolIdentifierImpl(5);
        TPDataCodingScheme tPDataCodingScheme = new TPDataCodingSchemeImpl(5);
        TPValidityPeriod tPValidityPeriod = new TPValidityPeriodImpl(getTPValidityPeriod());
        CAPExtensions extensions = CAPExtensionsTest.createTestCAPExtensions();
        CallReferenceNumber smsReferenceNumber = new CallReferenceNumberImpl(getCallReferenceNumber());
        ISDNAddressString mscAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "2207750008");
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "2207750009");
        MSClassmark2 mSClassmark2 = new MSClassmark2Impl(getMSClassmark2());

        // gprsMSClass
        MSNetworkCapabilityImpl nc = new MSNetworkCapabilityImpl(this.getEncodedDataNetworkCapability());
        MSRadioAccessCapabilityImpl rac = new MSRadioAccessCapabilityImpl(this.getEncodedDataRadioAccessCapability());
        GPRSMSClass gprsMSClass = new GPRSMSClassImpl(nc, rac);

        IMEI imei = new IMEIImpl("1122334455667788");
        ISDNAddressString calledPartyNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "2207750010");

        InitialDPSMSRequestImpl prim = new InitialDPSMSRequestImpl(serviceKey, destinationSubscriberNumber,
                callingPartyNumber, eventTypeSMS, imsi, locationInformationMSC, locationInformationGPRS, smscCAddress,
                timeAndTimezone, tPShortMessageSpecificInfo, tPProtocolIdentifier, tPDataCodingScheme,
                tPValidityPeriod, extensions, smsReferenceNumber, mscAddress, sgsnNumber, mSClassmark2, gprsMSClass,
                imei, calledPartyNumber);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }

}
