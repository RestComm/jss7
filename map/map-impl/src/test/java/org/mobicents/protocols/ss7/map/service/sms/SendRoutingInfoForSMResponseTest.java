/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.service.sms;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.lsm.AdditionalNumber;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.lsm.AdditionalNumberImpl;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SendRoutingInfoForSMResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 27, 4, 8, 2, -112, 9, 2, 16, 17, 34, -9, -96, 15, -127, 7, -111, 33, 48, 18, 0, -110, -11, 4,
                4, 0, 3, 98, 49 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 115, 4, 7, 17, 1, 35, 34, 51, 19, 17, (byte) 160, 63, (byte) 129, 5, (byte) 198, 0, 0, 17, 17, 4, 4, 0, 2, 1, 0, 48, 39,
                (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161,
                3, 31, 32, 33, (byte) 166, 7, (byte) 129, 5, (byte) 166, (byte) 153, (byte) 153, (byte) 153, (byte) 153, (byte) 164, 39, (byte) 160, 32, 48,
                10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { 48, 22, 4, 7, 82, 0, 17, 17, 17, 17, 17, -96, 8, -127, 6, -111, -105, -103, 25, 17, 17, -126, 1, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] {48,30,4,7,82,0,17,17,17,17,17,-96,8,-127,6,-111,-105,-103,25,17,17,-126,1,0,-91,6,2,1,30,2,1,40};
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        SendRoutingInfoForSMResponseImpl ind = new SendRoutingInfoForSMResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        IMSI imsi = ind.getIMSI();
        // assertEquals( (long)imsi.getMCC(),200L);
        // assertEquals( (long)imsi.getMNC(),99L);
        assertEquals(imsi.getData(), "200990200111227");
        LocationInfoWithLMSI li = ind.getLocationInfoWithLMSI();
        ISDNAddressString nnn = li.getNetworkNodeNumber();
        assertEquals(nnn.getAddressNature(), AddressNature.international_number);
        assertEquals(nnn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(nnn.getAddress(), "12032100295");
        LMSI lmsi = li.getLMSI();
        assertTrue(Arrays.equals(new byte[] { 0, 3, 98, 49 }, lmsi.getData()));
        assertNull(ind.getMwdSet());

        rawData = getEncodedDataFull();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = ind.getIMSI();
        // assertEquals( (long)imsi.getMCC(),111L);
        // assertEquals( (long)imsi.getMNC(),3L);
        assertEquals(imsi.getData(), "11103222333111");
        li = ind.getLocationInfoWithLMSI();
        nnn = li.getNetworkNodeNumber();
        assertEquals(nnn.getAddressNature(), AddressNature.subscriber_number);
        assertEquals(nnn.getNumberingPlan(), NumberingPlan.land_mobile);
        assertEquals(nnn.getAddress(), "00001111");
        lmsi = li.getLMSI();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(li.getExtensionContainer()));
        ISDNAddressString an = li.getAdditionalNumber().getSGSNNumber();
        assertEquals(an.getAddressNature(), AddressNature.national_significant_number);
        assertEquals(an.getNumberingPlan(), NumberingPlan.land_mobile);
        assertEquals(an.getAddress(), "99999999");
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
        assertNull(ind.getMwdSet());

        rawData = getEncodedData1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = ind.getIMSI();
        assertEquals(imsi.getData(), "25001111111111");
        li = ind.getLocationInfoWithLMSI();
        nnn = li.getNetworkNodeNumber();
        assertEquals(nnn.getAddressNature(), AddressNature.international_number);
        assertEquals(nnn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(nnn.getAddress(), "7999911111");
        assertNull(li.getLMSI());
        assertNull(li.getAdditionalNumber());
        assertNull(ind.getExtensionContainer());
        assertFalse(ind.getMwdSet());

        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMResponseImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = ind.getIMSI();
        assertEquals(imsi.getData(), "25001111111111");
        li = ind.getLocationInfoWithLMSI();
        nnn = li.getNetworkNodeNumber();
        assertEquals(nnn.getAddressNature(), AddressNature.international_number);
        assertEquals(nnn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(nnn.getAddress(), "7999911111");
        assertNull(li.getLMSI());
        assertNull(li.getAdditionalNumber());
        assertNull(ind.getExtensionContainer());
        assertNotNull(ind.getIpSmGwGuidance());
        assertEquals(ind.getIpSmGwGuidance().getMinimumDeliveryTimeValue(), 30);
        assertEquals(ind.getIpSmGwGuidance().getRecommendedDeliveryTimeValue(), 40);
        assertFalse(ind.getMwdSet());
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        IMSI imsi = new IMSIImpl("200990200111227");
        ISDNAddressString nnn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "12032100295");
        LMSI lmsi = new LMSIImpl(new byte[] { 0, 3, 98, 49 });

        LocationInfoWithLMSI li = new LocationInfoWithLMSIImpl(nnn, lmsi, null, false, null);
        SendRoutingInfoForSMResponseImpl ind = new SendRoutingInfoForSMResponseImpl(imsi, li, null, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        imsi = new IMSIImpl("11103222333111");
        nnn = new ISDNAddressStringImpl(AddressNature.subscriber_number, NumberingPlan.land_mobile, "00001111");
        lmsi = new LMSIImpl(new byte[] { 0, 2, 1, 0 });
        ISDNAddressString sgsnAdditionalNumber = new ISDNAddressStringImpl(AddressNature.national_significant_number,
                NumberingPlan.land_mobile, "99999999");
        AdditionalNumber additionalNumber = new AdditionalNumberImpl(null, sgsnAdditionalNumber);
        li = new LocationInfoWithLMSIImpl(nnn, lmsi, MAPExtensionContainerTest.GetTestExtensionContainer(), false, additionalNumber);
        ind = new SendRoutingInfoForSMResponseImpl(imsi, li, MAPExtensionContainerTest.GetTestExtensionContainer(), null, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));

        imsi = new IMSIImpl("25001111111111");
        nnn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "7999911111");
        li = new LocationInfoWithLMSIImpl(nnn, null, null, false, null);
        ind = new SendRoutingInfoForSMResponseImpl(imsi, li, null, false, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        imsi = new IMSIImpl("25001111111111");
        nnn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "7999911111");
        li = new LocationInfoWithLMSIImpl(nnn, null, null, false, null);
        IpSmGwGuidanceImpl ipSmGwGuidance = new IpSmGwGuidanceImpl(30, 40, null);
        ind = new SendRoutingInfoForSMResponseImpl(imsi, li, null, false, ipSmGwGuidance);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
