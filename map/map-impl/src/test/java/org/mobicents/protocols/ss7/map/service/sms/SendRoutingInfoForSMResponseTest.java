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

package org.mobicents.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AdditionalNumberType;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

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
        return new byte[] { 48, 115, 4, 7, 17, 1, 35, 34, 51, 19, 17, -96, 63, -127, 5, -58, 0, 0, 17, 17, 4, 4, 0, 2, 1, 0,
                48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21,
                22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -123, 0, -122, 5, -90, -103, -103, -103, -103, -92, 39, -96, 32, 48,
                10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26,
                -95, 3, 31, 32, 33 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { 48, 22, 4, 7, 82, 0, 17, 17, 17, 17, 17, -96, 8, -127, 6, -111, -105, -103, 25, 17, 17, -126, 1, 0 };
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
        assertEquals(li.getAdditionalNumberType(), AdditionalNumberType.sgsn);
        ISDNAddressString an = li.getAdditionalNumber();
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
        assertNull(li.getAdditionalNumberType());
        assertNull(li.getAdditionalNumber());
        assertNull(ind.getExtensionContainer());
        assertFalse(ind.getMwdSet());
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        IMSI imsi = new IMSIImpl("200990200111227");
        ISDNAddressString nnn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "12032100295");
        LMSI lmsi = new LMSIImpl(new byte[] { 0, 3, 98, 49 });

        LocationInfoWithLMSI li = new LocationInfoWithLMSIImpl(nnn, lmsi, null, null, null);
        SendRoutingInfoForSMResponseImpl ind = new SendRoutingInfoForSMResponseImpl(imsi, li, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        imsi = new IMSIImpl("11103222333111");
        nnn = new ISDNAddressStringImpl(AddressNature.subscriber_number, NumberingPlan.land_mobile, "00001111");
        lmsi = new LMSIImpl(new byte[] { 0, 2, 1, 0 });
        ISDNAddressString additionalNumber = new ISDNAddressStringImpl(AddressNature.national_significant_number,
                NumberingPlan.land_mobile, "99999999");
        li = new LocationInfoWithLMSIImpl(nnn, lmsi, MAPExtensionContainerTest.GetTestExtensionContainer(),
                AdditionalNumberType.sgsn, additionalNumber);
        ind = new SendRoutingInfoForSMResponseImpl(imsi, li, MAPExtensionContainerTest.GetTestExtensionContainer(), null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));

        imsi = new IMSIImpl("25001111111111");
        nnn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "7999911111");
        li = new LocationInfoWithLMSIImpl(nnn, null, null, null, null);
        ind = new SendRoutingInfoForSMResponseImpl(imsi, li, null, false);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
