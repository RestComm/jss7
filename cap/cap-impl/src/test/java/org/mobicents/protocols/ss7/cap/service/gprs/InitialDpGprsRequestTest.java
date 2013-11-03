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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoS;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoSExtension;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumberValue;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganizationValue;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.SGSNCapabilities;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.AccessPointNameImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.EndUserAddressImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.GPRSQoSExtensionImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.GPRSQoSImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPAddressImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPTypeNumberImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPTypeOrganizationImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.QualityOfServiceImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.SGSNCapabilitiesImpl;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClass;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GPRSChargingIDImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GPRSMSClassImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GeodeticInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.MSNetworkCapabilityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.MSRadioAccessCapabilityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.RAIdentityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.QoSSubscribedImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class InitialDpGprsRequestTest {

    public byte[] getData() {
        return new byte[] { 48, -127, -63, -128, 1, 2, -127, 1, 2, -126, 4, -111, 34, 50, -12, -125, 5, 17, 17, 33, 34, 34,
                -124, 8, 2, 80, 17, 66, 49, 1, 101, 0, -91, 11, -128, 3, 1, 2, 3, -127, 4, 11, 22, 33, 44, -90, 11, -128, 1,
                -15, -127, 1, 1, -126, 3, 4, 7, 7, -89, 12, -96, 5, -128, 3, 4, 7, 7, -93, 3, -128, 1, 52, -120, 3, 52, 20, 30,
                -119, 6, 12, 52, 23, 20, 30, 45, -118, 4, 52, 34, 20, 30, -117, 1, 1, -84, 57, -96, 7, -127, 5, 82, -16, 16,
                17, 92, -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 31, 32, 33, 34, 35, 36, 37, 38, -125, 4, -111, 86, 52, 18,
                -124, 3, 91, 92, 93, -122, 0, -121, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -120, 0, -119, 1, 13, -115, 1, 1, -82,
                18, 48, 5, 2, 1, 2, -127, 0, 48, 9, 2, 1, 3, 10, 1, 1, -127, 1, -1, -113, 6, 4, 5, 6, 8, 9, 10, -112, 0, -111,
                8, 17, 34, 51, 68, 85, 102, 119, -120 };
    };

    private byte[] getEncodedDataNetworkCapability() {
        return new byte[] { 1, 2, 3 };
    }

    private byte[] getEncodedDataRadioAccessCapability() {
        return new byte[] { 11, 22, 33, 44 };
    }

    public byte[] getPDPAddressData() {
        return new byte[] { 4, 7, 7 };
    };

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
    };

    private byte[] getEncodedqos2Subscribed1() {
        return new byte[] { 52 };
    }

    private byte[] getAccessPointNameData() {
        return new byte[] { 52, 20, 30 };
    }

    private byte[] getRAIdentityData() {
        return new byte[] { 12, 52, 23, 20, 30, 45 };
    }

    private byte[] getGPRSChargingIDData() {
        return new byte[] { 52, 34, 20, 30 };
    }

    private byte[] getEncodedDataRAIdentity() {
        return new byte[] { 11, 12, 13, 14, 15, 16 };
    }

    private byte[] getGeographicalInformation() {
        return new byte[] { 31, 32, 33, 34, 35, 36, 37, 38 };
    }

    private byte[] getEncodedDataLSAIdentity() {
        return new byte[] { 91, 92, 93 };
    }

    private byte[] getGeodeticInformation() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    }

    private byte[] getGSNAddressData() {
        return new byte[] { 4, 5, 6, 8, 9, 10 };
    }

    public byte[] getDataLiveTrace() {
        return new byte[] { 0x30, (byte) 0x81, /* (byte) 0xd2 */-44 /* end */, (byte) 0x80, 0x01, 0x17, (byte) 0x81, 0x01,
                0x0c, (byte) 0x82, 0x07, (byte) 0x91, 0x55, 0x43, (byte) 0x99, 0x37, 0x09, 0x52, (byte) 0x83, 0x08, 0x27, 0x34,
                0x04, 0x01, 0x10, (byte) 0x83, 0x06, (byte) 0xf0, (byte) 0x84, 0x08, 0x02, 0x31, 0x10, 0x40, 0x60, 0x13, 0x43,
                (byte) 0x88, (byte) 0xa5, 0x22, (byte) 0x80, 0x02, (byte) 0xe5, (byte) 0xe0, (byte) 0x81, 0x1c, 0x17,
                (byte) 0xb3, 0x42, 0x2b, 0x25, (byte) 0x96, 0x62, 0x40, 0x18, (byte) 0x9a, 0x42, (byte) 0x86, 0x62, 0x40, 0x18,
                (byte) 0xa2, 0x42, (byte) 0x86, 0x62, 0x40, 0x18, (byte) 0xba, 0x48, (byte) 0x86, 0x62, 0x40, 0x18, 0x00,
                (byte) 0xa6, 0x0c, (byte) 0x80, 0x01, (byte) 0xf1, (byte) 0x81, 0x01, 0x21, (byte) 0x82, 0x04, (byte) 0xb1,
                (byte) 0xbf, (byte) 0x95, (byte) 0xb3, (byte) 0xa7, 0x27, (byte) 0xa0, 0x0b, (byte) 0x81, 0x09, 0x00, 0x10,
                (byte) 0x96, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xa1, 0x0b, (byte) 0x81, 0x09, 0x02, 0x6b, (byte) 0x96,
                0x40, 0x40, 0x74, 0x06, (byte) 0xff, (byte) 0xff, (byte) 0xa2, 0x0b, (byte) 0x81, 0x09, 0x02, 0x71,
                (byte) 0x96, 0x40, 0x40, 0x74, 0x02, (byte) 0xff, (byte) 0xff, (byte) 0x88, 0x1b, 0x04, 0x63, 0x74, 0x62, 0x63,
                0x02, 0x62, 0x72, 0x06, 0x4d, 0x4e, 0x43, 0x30, 0x33, 0x34, 0x06, 0x4d, 0x43, 0x43, 0x37, 0x32, 0x34, 0x04,
                0x47, 0x50, 0x52, 0x53, (byte) 0x89, 0x06, 0x27, (byte) 0xf4, 0x43, (byte) 0x81, 0x6e, 0x04, (byte) 0x8a, 0x04,
                0x14, (byte) 0xf8, 0x55, (byte) 0xd1, (byte) 0x8b, 0x01, 0x00, (byte) 0xac, /* 0x1a, */28, -96, 9,/* end */
                (byte) 0x80, 0x07, 0x27, (byte) 0xf4, 0x43, (byte) 0x81, 0x6e, 0x15, (byte) 0xa0, (byte) 0x81, 0x06, 0x27,
                (byte) 0xf4, 0x43, (byte) 0x81, 0x6e, 0x04, (byte) 0x83, 0x07, (byte) 0x91, 0x55, 0x43, 0x69, 0x26,
                (byte) 0x99, 0x59, (byte) 0x8d, 0x01, 0x00, (byte) 0x8f, 0x05, 0x04, (byte) 0xc9, 0x30, (byte) 0xe2, 0x1b };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        InitialDpGprsRequestImpl prim = new InitialDpGprsRequestImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(prim.getServiceKey(), 2);
        assertEquals(prim.getGPRSEventType(), GPRSEventType.attachChangeOfPosition);

        // msisdn
        ISDNAddressString msisdn = prim.getMsisdn();
        assertTrue(msisdn.getAddress().equals("22234"));
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);

        // getImsi
        assertTrue(prim.getImsi().getData().equals("1111122222"));

        // getTimeAndTimezone
        assertEquals(prim.getTimeAndTimezone().getYear(), 2005);
        assertEquals(prim.getTimeAndTimezone().getMonth(), 11);
        assertEquals(prim.getTimeAndTimezone().getDay(), 24);
        assertEquals(prim.getTimeAndTimezone().getHour(), 13);
        assertEquals(prim.getTimeAndTimezone().getMinute(), 10);
        assertEquals(prim.getTimeAndTimezone().getSecond(), 56);
        assertEquals(prim.getTimeAndTimezone().getTimeZone(), 0);

        // gprsMSClass
        GPRSMSClass gprsMSClass = prim.getGPRSMSClass();
        assertTrue(Arrays.equals(gprsMSClass.getMSNetworkCapability().getData(), this.getEncodedDataNetworkCapability()));
        assertTrue(Arrays
                .equals(gprsMSClass.getMSRadioAccessCapability().getData(), this.getEncodedDataRadioAccessCapability()));

        // endUserAddress
        assertEquals(prim.getEndUserAddress().getPDPTypeNumber().getPDPTypeNumberValue(), PDPTypeNumberValue.PPP);
        assertEquals(prim.getEndUserAddress().getPDPTypeOrganization().getPDPTypeOrganizationValue(),
                PDPTypeOrganizationValue.ETSI);
        assertTrue(Arrays.equals(prim.getEndUserAddress().getPDPAddress().getData(), this.getPDPAddressData()));

        // qualityOfService
        assertTrue(Arrays.equals(prim.getQualityOfService().getRequestedQoS().getShortQoSFormat().getData(),
                this.getQoSSubscribedData()));
        assertNull(prim.getQualityOfService().getRequestedQoS().getLongQoSFormat());
        assertTrue(Arrays.equals(
                prim.getQualityOfService().getRequestedQoSExtension().getSupplementToLongQoSFormat().getData(),
                this.getEncodedqos2Subscribed1()));

        // getAccessPointName
        assertTrue(Arrays.equals(prim.getAccessPointName().getData(), this.getAccessPointNameData()));

        // routeingAreaIdentity
        assertTrue(Arrays.equals(prim.getRouteingAreaIdentity().getData(), this.getRAIdentityData()));

        // chargingID
        assertTrue(Arrays.equals(prim.getChargingID().getData(), this.getGPRSChargingIDData()));

        // sgsnCapabilities
        assertEquals(prim.getSGSNCapabilities().getData(), 1);

        // locationInformationGPRS
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC(), 250);
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC(), 1);
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac(), 4444);
        assertTrue(Arrays.equals(prim.getLocationInformationGPRS().getRouteingAreaIdentity().getData(),
                this.getEncodedDataRAIdentity()));
        assertTrue(Arrays.equals(prim.getLocationInformationGPRS().getGeographicalInformation().getData(),
                this.getGeographicalInformation()));
        assertTrue(prim.getLocationInformationGPRS().getSGSNNumber().getAddress().equals("654321"));
        assertTrue(Arrays
                .equals(prim.getLocationInformationGPRS().getLSAIdentity().getData(), this.getEncodedDataLSAIdentity()));
        assertTrue(prim.getLocationInformationGPRS().isSaiPresent());
        assertTrue(Arrays.equals(prim.getLocationInformationGPRS().getGeodeticInformation().getData(),
                this.getGeodeticInformation()));
        assertTrue(prim.getLocationInformationGPRS().isCurrentLocationRetrieved());
        assertEquals((int) prim.getLocationInformationGPRS().getAgeOfLocationInformation(), 13);

        // PDPInitiationType
        assertEquals(prim.getPDPInitiationType(), PDPInitiationType.networkInitiated);

        // extensions
        CAPExtensionsTest.checkTestCAPExtensions(prim.getExtensions());

        // gsnAddress
        assertTrue(Arrays.equals(prim.getGSNAddress().getData(), this.getGSNAddressData()));

        // getSecondaryPDPContext
        assertTrue(prim.getSecondaryPDPContext());

        // getImei
        assertTrue(prim.getImei().getIMEI().equals("1122334455667788"));
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecodeLiveTrace() throws Exception {
        byte[] data = this.getDataLiveTrace();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        InitialDpGprsRequestImpl prim = new InitialDpGprsRequestImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(prim.getServiceKey(), 23);
        assertEquals(prim.getGPRSEventType(), GPRSEventType.pdpContextEstablishmentAcknowledgement);

        // msisdn
        ISDNAddressString msisdn = prim.getMsisdn();
        assertTrue(msisdn.getAddress().equals("553499739025"));
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);

        // getImsi
        assertTrue(prim.getImsi().getData().equals("724340100138600"));

        // sgsnCapabilities
        assertEquals(prim.getSGSNCapabilities().getData(), 0);

        // locationInformationGPRS
        assertNull(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength());
        assertNotNull(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI()
                .getCellGlobalIdOrServiceAreaIdFixedLength());

        // PDPInitiationType
        assertEquals(prim.getPDPInitiationType(), PDPInitiationType.mSInitiated);

        // getSecondaryPDPContext
        assertTrue(!prim.getSecondaryPDPContext());

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        int serviceKey = 2;
        GPRSEventType gprsEventType = GPRSEventType.attachChangeOfPosition;
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");
        IMSI imsi = new IMSIImpl("1111122222");
        TimeAndTimezone timeAndTimezone = new TimeAndTimezoneImpl(2005, 11, 24, 13, 10, 56, 0);

        // gprsMSClass
        MSNetworkCapabilityImpl nc = new MSNetworkCapabilityImpl(this.getEncodedDataNetworkCapability());
        MSRadioAccessCapabilityImpl rac = new MSRadioAccessCapabilityImpl(this.getEncodedDataRadioAccessCapability());
        GPRSMSClass gprsMSClass = new GPRSMSClassImpl(nc, rac);

        // endUserAddress
        PDPAddressImpl pdpAddress = new PDPAddressImpl(getPDPAddressData());
        PDPTypeNumberImpl pdpTypeNumber = new PDPTypeNumberImpl(PDPTypeNumberValue.PPP);
        PDPTypeOrganizationImpl pdpTypeOrganization = new PDPTypeOrganizationImpl(PDPTypeOrganizationValue.ETSI);
        EndUserAddress endUserAddress = new EndUserAddressImpl(pdpTypeOrganization, pdpTypeNumber, pdpAddress);

        // qualityOfService
        QoSSubscribed qosSubscribed = new QoSSubscribedImpl(this.getQoSSubscribedData());
        GPRSQoS requestedQoS = new GPRSQoSImpl(qosSubscribed);
        Ext2QoSSubscribedImpl qos2Subscribed1 = new Ext2QoSSubscribedImpl(this.getEncodedqos2Subscribed1());
        GPRSQoSExtension requestedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed1);
        QualityOfService qualityOfService = new QualityOfServiceImpl(requestedQoS, null, null, requestedQoSExtension, null,
                null);

        // accessPointName
        AccessPointName accessPointName = new AccessPointNameImpl(this.getAccessPointNameData());

        // routeingAreaIdentity
        RAIdentity routeingAreaIdentity = new RAIdentityImpl(this.getRAIdentityData());

        GPRSChargingID chargingID = new GPRSChargingIDImpl(this.getGPRSChargingIDData());

        // sgsnCapabilities
        SGSNCapabilities sgsnCapabilities = new SGSNCapabilitiesImpl(1);

        // locationInformationGPRS
        LAIFixedLengthImpl lai = new LAIFixedLengthImpl(250, 1, 4444);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
        RAIdentityImpl ra = new RAIdentityImpl(this.getEncodedDataRAIdentity());
        GeographicalInformationImpl ggi = new GeographicalInformationImpl(this.getGeographicalInformation());
        ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "654321");
        LSAIdentityImpl lsa = new LSAIdentityImpl(this.getEncodedDataLSAIdentity());
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(this.getGeodeticInformation());
        LocationInformationGPRS locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra, ggi, sgsn, lsa, null, true,
                gdi, true, 13);

        // pdpInitiationType
        PDPInitiationType pdpInitiationType = PDPInitiationType.networkInitiated;

        // extensions
        CAPExtensions extensions = CAPExtensionsTest.createTestCAPExtensions();

        // GSNAddress
        GSNAddress gsnAddress = new GSNAddressImpl(this.getGSNAddressData());

        // secondaryPDPContext
        boolean secondaryPDPContext = true;

        // imei
        IMEI imei = new IMEIImpl("1122334455667788");

        InitialDpGprsRequestImpl prim = new InitialDpGprsRequestImpl(serviceKey, gprsEventType, msisdn, imsi, timeAndTimezone,
                gprsMSClass, endUserAddress, qualityOfService, accessPointName, routeingAreaIdentity, chargingID,
                sgsnCapabilities, locationInformationGPRS, pdpInitiationType, extensions, gsnAddress, secondaryPDPContext, imei);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncodeLiveTrace() throws Exception {

        int serviceKey = 23;
        GPRSEventType gprsEventType = GPRSEventType.pdpContextEstablishmentAcknowledgement;
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553499739025");
        IMSI imsi = new IMSIImpl("724340100138600");
        TimeAndTimezone timeAndTimezone = new TimeAndTimezoneImpl(new byte[] { 0x02, 0x31, 0x10, 0x40, 0x60, 0x13, 0x43,
                (byte) 0x88 });

        // gprsMSClass
        MSNetworkCapabilityImpl nc = new MSNetworkCapabilityImpl(new byte[] { (byte) 0xe5, (byte) 0xe0 });
        MSRadioAccessCapabilityImpl rac = new MSRadioAccessCapabilityImpl(new byte[] { 0x17, (byte) 0xb3, 0x42, 0x2b, 0x25,
                (byte) 0x96, 0x62, 0x40, 0x18, (byte) 0x9a, 0x42, (byte) 0x86, 0x62, 0x40, 0x18, (byte) 0xa2, 0x42,
                (byte) 0x86, 0x62, 0x40, 0x18, (byte) 0xba, 0x48, (byte) 0x86, 0x62, 0x40, 0x18, 0x00 });

        GPRSMSClass gprsMSClass = new GPRSMSClassImpl(nc, rac);

        // endUserAddress
        PDPAddressImpl pdpAddress = new PDPAddressImpl(new byte[] { (byte) 0xb1, (byte) 0xbf, (byte) 0x95, (byte) 0xb3 });

        PDPTypeNumberImpl pdpTypeNumber = new PDPTypeNumberImpl(PDPTypeNumberValue.IPV4);

        PDPTypeOrganizationImpl pdpTypeOrganization = new PDPTypeOrganizationImpl((byte) 0xf1);
        EndUserAddress endUserAddress = new EndUserAddressImpl(pdpTypeOrganization, pdpTypeNumber, pdpAddress);

        // qualityOfService
        ExtQoSSubscribed longQos1 = new ExtQoSSubscribedImpl(new byte[] { 0x00, 0x10, (byte) 0x96, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00 });
        GPRSQoS requestedQoS = new GPRSQoSImpl(longQos1);

        ExtQoSSubscribed longQos2 = new ExtQoSSubscribedImpl(new byte[] { 0x02, 0x6b, (byte) 0x96, 0x40, 0x40, 0x74, 0x06,
                (byte) 0xff, (byte) 0xff });
        GPRSQoS subscribedQoS = new GPRSQoSImpl(longQos2);

        ExtQoSSubscribed longQos3 = new ExtQoSSubscribedImpl(new byte[] { 0x02, 0x71, (byte) 0x96, 0x40, 0x40, 0x74, 0x02,
                (byte) 0xff, (byte) 0xff });
        GPRSQoS negotiatedQoS = new GPRSQoSImpl(longQos3);

        QualityOfService qualityOfService = new QualityOfServiceImpl(requestedQoS, subscribedQoS, negotiatedQoS, null, null,
                null);

        // accessPointName
        AccessPointName accessPointName = new AccessPointNameImpl(new byte[] { 0x04, 0x63, 0x74, 0x62, 0x63, 0x02, 0x62, 0x72,
                0x06, 0x4d, 0x4e, 0x43, 0x30, 0x33, 0x34, 0x06, 0x4d, 0x43, 0x43, 0x37, 0x32, 0x34, 0x04, 0x47, 0x50, 0x52,
                0x53 });

        // routeingAreaIdentity
        RAIdentity routeingAreaIdentity = new RAIdentityImpl(new byte[] { 0x27, (byte) 0xf4, 0x43, (byte) 0x81, 0x6e, 0x04 });

        GPRSChargingID chargingID = new GPRSChargingIDImpl(new byte[] { 0x14, (byte) 0xf8, 0x55, (byte) 0xd1 });

        // sgsnCapabilities
        SGSNCapabilities sgsnCapabilities = new SGSNCapabilitiesImpl(0x00);

        CellGlobalIdOrServiceAreaIdFixedLength cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(
                new byte[] { 0x27, (byte) 0xf4, 0x43, (byte) 0x81, 0x6e, 0x15, (byte) 0xa0 });

        CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(
                cellGlobalIdOrServiceAreaIdFixedLength);

        RAIdentityImpl ra = new RAIdentityImpl(new byte[] { 0x27, (byte) 0xf4, 0x43, (byte) 0x81, 0x6e, 0x04 });

        ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553496629995");

        LocationInformationGPRS locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra, null, sgsn, null, null,
                false, null, false, null);

        // pdpInitiationType
        PDPInitiationType pdpInitiationType = PDPInitiationType.mSInitiated;

        // GSNAddress
        GSNAddress gsnAddress = new GSNAddressImpl(new byte[] { 0x04, (byte) 0xc9, 0x30, (byte) 0xe2, 0x1b });

        // secondaryPDPContext
        boolean secondaryPDPContext = false;

        InitialDpGprsRequestImpl prim = new InitialDpGprsRequestImpl(serviceKey, gprsEventType, msisdn, imsi, timeAndTimezone,
                gprsMSClass, endUserAddress, qualityOfService, accessPointName, routeingAreaIdentity, chargingID,
                sgsnCapabilities, locationInformationGPRS, pdpInitiationType, null, gsnAddress, secondaryPDPContext, null);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getDataLiveTrace()));

    }

}
