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
package org.mobicents.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.primitives.USSDStringImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 *
 */
public class UnstructuredSSRequestTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    @Test(groups = { "functional.decode", "service.ussd" })
    public void testDecode() throws Exception {
        byte[] data = new byte[] { 0x30, 0x3e, 0x04, 0x01, 0x0f, 0x04, 0x39, (byte) 0xd5, (byte) 0xe9, (byte) 0x94, 0x08,
                (byte) 0x9a, (byte) 0xd2, (byte) 0xe5, 0x69, (byte) 0xf7, 0x19, (byte) 0xa4, 0x03, 0x21, (byte) 0xcb, 0x6c,
                (byte) 0xf6, 0x1b, 0x74, 0x7d, (byte) 0xcb, (byte) 0xd9, 0x64, 0x10, 0x6f, 0x28, (byte) 0xf5, (byte) 0x81,
                0x62, 0x2e, (byte) 0x90, 0x30, (byte) 0xcc, 0x0e, (byte) 0xbb, (byte) 0xc7, 0x65, 0x10, 0x6f, 0x28,
                (byte) 0xf5, (byte) 0x81, 0x64, 0x2e, 0x10, (byte) 0xb5, (byte) 0x8c, (byte) 0xa7, (byte) 0xcf, 0x41,
                (byte) 0xd2, 0x72, 0x3b, (byte) 0x9c, 0x76, (byte) 0xa7, (byte) 0xdd, 0x67 };

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        UnstructuredSSRequestImpl addNum = new UnstructuredSSRequestImpl();
        addNum.decodeAll(asn);
        CBSDataCodingScheme dataCodingScheme = addNum.getDataCodingScheme();
        assertEquals(dataCodingScheme.getCode(), 0x0f);

        USSDString ussdString = addNum.getUSSDString();
        assertNotNull(ussdString);

        assertEquals(ussdString.getString(null), "USSD String : Hello World <CR> 1. Balance <CR> 2. Texts Remaining");

    }

    @Test(groups = { "functional.encode", "service.ussd" })
    public void testEncode() throws Exception {
        byte[] data = new byte[] { 0x30, 0x3e, 0x04, 0x01, 0x0f, 0x04, 0x39, (byte) 0xd5, (byte) 0xe9, (byte) 0x94, 0x08,
                (byte) 0x9a, (byte) 0xd2, (byte) 0xe5, 0x69, (byte) 0xf7, 0x19, (byte) 0xa4, 0x03, 0x21, (byte) 0xcb, 0x6c,
                (byte) 0xf6, 0x1b, 0x74, 0x7d, (byte) 0xcb, (byte) 0xd9, 0x64, 0x10, 0x6f, 0x28, (byte) 0xf5, (byte) 0x81,
                0x62, 0x2e, (byte) 0x90, 0x30, (byte) 0xcc, 0x0e, (byte) 0xbb, (byte) 0xc7, 0x65, 0x10, 0x6f, 0x28,
                (byte) 0xf5, (byte) 0x81, 0x64, 0x2e, 0x10, (byte) 0xb5, (byte) 0x8c, (byte) 0xa7, (byte) 0xcf, 0x41,
                (byte) 0xd2, 0x72, 0x3b, (byte) 0x9c, 0x76, (byte) 0xa7, (byte) 0xdd, 0x67 };

        USSDString ussdStr = new USSDStringImpl("USSD String : Hello World <CR> 1. Balance <CR> 2. Texts Remaining", null, null);
        UnstructuredSSRequestImpl addNum = new UnstructuredSSRequestImpl(new CBSDataCodingSchemeImpl(0x0f), ussdStr, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        addNum.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));
    }
}
