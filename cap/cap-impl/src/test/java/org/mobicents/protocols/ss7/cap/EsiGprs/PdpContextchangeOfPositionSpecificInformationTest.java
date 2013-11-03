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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.primitives.TimeAndTimezone;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoS;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoSExtension;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumberValue;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganizationValue;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.mobicents.protocols.ss7.cap.primitives.TimeAndTimezoneImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.AccessPointNameImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.EndUserAddressImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.GPRSQoSExtensionImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.GPRSQoSImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPAddressImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPTypeNumberImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPTypeOrganizationImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.QualityOfServiceImpl;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GPRSChargingIDImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GeodeticInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
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
public class PdpContextchangeOfPositionSpecificInformationTest {

    public byte[] getData() {
        return new byte[] { -80, -127, -119, -128, 3, 52, 20, 30, -127, 4, 41, 42, 43, 44, -94, 57, -96, 7, -127, 5, 82, -16,
                16, 17, 92, -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 31, 32, 33, 34, 35, 36, 37, 38, -125, 4, -111, 86, 52,
                18, -124, 3, 91, 92, 93, -122, 0, -121, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -120, 0, -119, 1, 13, -93, 11, -128,
                1, -15, -127, 1, 1, -126, 3, 4, 7, 7, -92, 35, -96, 5, -128, 3, 4, 7, 7, -95, 4, -127, 2, 1, 7, -94, 5, -128,
                3, 4, 7, 7, -93, 3, -128, 1, 52, -92, 3, -128, 1, 53, -91, 3, -128, 1, 54, -123, 8, 2, 17, 33, 3, 1, 112, -127,
                35, -122, 5, 1, 1, 1, 1, 1 };
    };

    private byte[] getAccessPointNameData() {
        return new byte[] { 52, 20, 30 };
    }

    private byte[] getEncodedchargingId() {
        return new byte[] { 41, 42, 43, 44 };
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

    public byte[] getPDPAddressData() {
        return new byte[] { 4, 7, 7 };
    };

    private byte[] getEncodedqos2Subscribed1() {
        return new byte[] { 52 };
    }

    private byte[] getEncodedqos2Subscribed2() {
        return new byte[] { 53 };
    }

    private byte[] getEncodedqos2Subscribed3() {
        return new byte[] { 54 };
    }

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
    };

    public byte[] getExtQoSSubscribedData() {
        return new byte[] { 1, 7 };
    };

    private byte[] getGSNAddressData() {
        return new byte[] { 1, 1, 1, 1, 1 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        PdpContextchangeOfPositionSpecificInformationImpl prim = new PdpContextchangeOfPositionSpecificInformationImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        assertTrue(Arrays.equals(prim.getAccessPointName().getData(), this.getAccessPointNameData()));
        assertTrue(Arrays.equals(prim.getChargingID().getData(), this.getEncodedchargingId()));

        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC(), 250);
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC(), 1);
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac(), 4444);

        assertEquals(prim.getEndUserAddress().getPDPTypeNumber().getPDPTypeNumberValue(), PDPTypeNumberValue.PPP);
        assertEquals(prim.getEndUserAddress().getPDPTypeOrganization().getPDPTypeOrganizationValue(),
                PDPTypeOrganizationValue.ETSI);
        assertTrue(Arrays.equals(prim.getEndUserAddress().getPDPAddress().getData(), this.getPDPAddressData()));

        assertTrue(Arrays.equals(
                prim.getQualityOfService().getRequestedQoSExtension().getSupplementToLongQoSFormat().getData(),
                this.getEncodedqos2Subscribed1()));
        assertTrue(Arrays.equals(prim.getQualityOfService().getSubscribedQoSExtension().getSupplementToLongQoSFormat()
                .getData(), this.getEncodedqos2Subscribed2()));
        assertTrue(Arrays.equals(prim.getQualityOfService().getNegotiatedQoSExtension().getSupplementToLongQoSFormat()
                .getData(), this.getEncodedqos2Subscribed3()));

        assertEquals(prim.getTimeAndTimezone().getYear(), 2011);
        assertEquals(prim.getTimeAndTimezone().getSecond(), 18);
        assertEquals(prim.getTimeAndTimezone().getTimeZone(), 32);

        assertTrue(Arrays.equals(getGSNAddressData(), prim.getGSNAddress().getData()));

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        AccessPointName accessPointName = new AccessPointNameImpl(this.getAccessPointNameData());
        GPRSChargingID chargingID = new GPRSChargingIDImpl(getEncodedchargingId());

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

        // endUserAddress
        PDPAddressImpl pdpAddress = new PDPAddressImpl(getPDPAddressData());
        PDPTypeNumberImpl pdpTypeNumber = new PDPTypeNumberImpl(PDPTypeNumberValue.PPP);
        PDPTypeOrganizationImpl pdpTypeOrganization = new PDPTypeOrganizationImpl(PDPTypeOrganizationValue.ETSI);
        EndUserAddress endUserAddress = new EndUserAddressImpl(pdpTypeOrganization, pdpTypeNumber, pdpAddress);

        // qualityOfService
        QoSSubscribed qosSubscribed = new QoSSubscribedImpl(this.getQoSSubscribedData());
        GPRSQoS requestedQoS = new GPRSQoSImpl(qosSubscribed);
        ExtQoSSubscribed extQoSSubscribed = new ExtQoSSubscribedImpl(this.getExtQoSSubscribedData());
        GPRSQoS subscribedQoS = new GPRSQoSImpl(extQoSSubscribed);
        GPRSQoS negotiatedQoS = new GPRSQoSImpl(qosSubscribed);
        Ext2QoSSubscribedImpl qos2Subscribed1 = new Ext2QoSSubscribedImpl(this.getEncodedqos2Subscribed1());
        GPRSQoSExtension requestedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed1);
        Ext2QoSSubscribedImpl qos2Subscribed2 = new Ext2QoSSubscribedImpl(this.getEncodedqos2Subscribed2());
        GPRSQoSExtension subscribedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed2);
        Ext2QoSSubscribedImpl qos2Subscribed3 = new Ext2QoSSubscribedImpl(this.getEncodedqos2Subscribed3());
        GPRSQoSExtension negotiatedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed3);
        QualityOfService qualityOfService = new QualityOfServiceImpl(requestedQoS, subscribedQoS, negotiatedQoS,
                requestedQoSExtension, subscribedQoSExtension, negotiatedQoSExtension);

        // timeAndTimezone
        TimeAndTimezone timeAndTimezone = new TimeAndTimezoneImpl(2011, 12, 30, 10, 7, 18, 32);
        GSNAddress gsnAddress = new GSNAddressImpl(getGSNAddressData());

        PdpContextchangeOfPositionSpecificInformationImpl prim = new PdpContextchangeOfPositionSpecificInformationImpl(
                accessPointName, chargingID, locationInformationGPRS, endUserAddress, qualityOfService, timeAndTimezone,
                gsnAddress);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }

}
