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
 * @author abhayani
 *
 */
public class ProcessUnstructuredSSResponseTest {
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
        byte[] data = new byte[] { 0x30, 0x15, 0x04, 0x01, 0x0f, 0x04, 0x10, (byte) 0xd9, 0x77, 0x5d, 0x0e, 0x12, (byte) 0x87,
                (byte) 0xd9, 0x61, (byte) 0xf7, (byte) 0xb8, 0x0c, (byte) 0xea, (byte) 0x81, 0x66, 0x35, 0x18 };

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        ProcessUnstructuredSSResponseImpl addNum = new ProcessUnstructuredSSResponseImpl();
        addNum.decodeAll(asn);
        CBSDataCodingScheme dataCodingScheme = addNum.getDataCodingScheme();
        assertEquals(dataCodingScheme.getCode(), (byte) 0x0f);

        USSDString ussdString = addNum.getUSSDString();
        assertNotNull(ussdString);

        assertEquals(ussdString.getString(null), "Your balance = 350");

    }

    @Test(groups = { "functional.encode", "service.ussd" })
    public void testEncode() throws Exception {
        byte[] data = new byte[] { 0x30, 0x15, 0x04, 0x01, 0x0f, 0x04, 0x10, (byte) 0xd9, 0x77, 0x5d, 0x0e, 0x12, (byte) 0x87,
                (byte) 0xd9, 0x61, (byte) 0xf7, (byte) 0xb8, 0x0c, (byte) 0xea, (byte) 0x81, 0x66, 0x35, 0x18 };

        USSDString ussdStr = new USSDStringImpl("Your balance = 350", null, null);
        ProcessUnstructuredSSResponseImpl addNum = new ProcessUnstructuredSSResponseImpl(new CBSDataCodingSchemeImpl(0x0f),
                ussdStr);

        AsnOutputStream asnOS = new AsnOutputStream();
        addNum.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));
    }
}
