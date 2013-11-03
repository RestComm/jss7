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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

public class UpdateLocationResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 6, 4, 4, -111, -112, 120, -10 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 51, 4, 4, -111, -112, 120, -10, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 5, 0, -128, 0 };
    }

    private byte[] getEncodedData_V1() {
        return new byte[] { 4, 4, -111, -112, 120, -10 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        UpdateLocationResponseImpl asc = new UpdateLocationResponseImpl(3);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals(asc.getMapProtocolVersion(), 3);

        ISDNAddressString mscNumber = asc.getHlrNumber();
        assertTrue(mscNumber.getAddress().equals("09876"));
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertNull(asc.getExtensionContainer());
        assertFalse(asc.getAddCapability());
        assertFalse(asc.getPagingAreaCapability());

        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new UpdateLocationResponseImpl(3);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals(asc.getMapProtocolVersion(), 3);

        mscNumber = asc.getHlrNumber();
        assertTrue(mscNumber.getAddress().equals("09876"));
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));
        assertTrue(asc.getAddCapability());
        assertTrue(asc.getPagingAreaCapability());

        rawData = getEncodedData_V1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new UpdateLocationResponseImpl(1);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals(asc.getMapProtocolVersion(), 1);

        mscNumber = asc.getHlrNumber();
        assertTrue(mscNumber.getAddress().equals("09876"));
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertNull(asc.getExtensionContainer());
        assertFalse(asc.getAddCapability());
        assertFalse(asc.getPagingAreaCapability());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        ISDNAddressStringImpl hlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "09876");
        UpdateLocationResponseImpl asc = new UpdateLocationResponseImpl(3, hlrNumber, null, false, false);
        // long mapProtocolVersion, ISDNAddressString hlrNumber, MAPExtensionContainer extensionContainer, boolean
        // addCapability,
        // boolean pagingAreaCapability

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        asc = new UpdateLocationResponseImpl(3, hlrNumber, MAPExtensionContainerTest.GetTestExtensionContainer(), true, true);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        asc = new UpdateLocationResponseImpl(1, hlrNumber, null, false, false);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_V1();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
