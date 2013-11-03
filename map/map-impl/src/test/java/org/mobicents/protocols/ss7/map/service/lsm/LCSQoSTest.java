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

package org.mobicents.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 *
 */
public class LCSQoSTest {
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

    public byte[] getData() {
        return new byte[] { 0x30, 0x05, (byte) 0xa3, 0x03, 0x0a, 0x01, 0x00 };
    }

    public byte[] getDataFull() {
        return new byte[] { 48, 54, -128, 1, 10, -127, 0, -126, 1, 20, -93, 3, 10, 1, 0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3,
                4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32,
                33 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
        byte[] data = getData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        LCSQoSImpl lcsQos = new LCSQoSImpl();
        lcsQos.decodeAll(asn);

        assertNotNull(lcsQos.getResponseTime());
        ResponseTime resTime = lcsQos.getResponseTime();
        assertNotNull(resTime.getResponseTimeCategory());
        assertEquals(resTime.getResponseTimeCategory(), ResponseTimeCategory.lowdelay);

        assertNull(lcsQos.getHorizontalAccuracy());
        assertFalse(lcsQos.getVerticalCoordinateRequest());
        assertNull(lcsQos.getVerticalAccuracy());
        assertNull(lcsQos.getExtensionContainer());

        data = getDataFull();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        lcsQos = new LCSQoSImpl();
        lcsQos.decodeAll(asn);

        assertNotNull(lcsQos.getResponseTime());
        resTime = lcsQos.getResponseTime();
        assertNotNull(resTime.getResponseTimeCategory());
        assertEquals(resTime.getResponseTimeCategory(), ResponseTimeCategory.lowdelay);

        assertEquals((int) lcsQos.getHorizontalAccuracy(), 10);
        assertTrue(lcsQos.getVerticalCoordinateRequest());
        assertEquals((int) lcsQos.getVerticalAccuracy(), 20);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lcsQos.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
        byte[] data = getData();

        LCSQoSImpl lcsQos = new LCSQoSImpl(null, null, false, new ResponseTimeImpl(ResponseTimeCategory.lowdelay), null);
        AsnOutputStream asnOS = new AsnOutputStream();
        lcsQos.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

        data = getDataFull();

        lcsQos = new LCSQoSImpl(10, 20, true, new ResponseTimeImpl(ResponseTimeCategory.lowdelay),
                MAPExtensionContainerTest.GetTestExtensionContainer());
        // Integer horizontalAccuracy, Integer verticalAccuracy, boolean verticalCoordinateRequest, ResponseTime responseTime,
        // MAPExtensionContainer extensionContainer
        asnOS = new AsnOutputStream();
        lcsQos.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.serialize", "service.lsm" })
    public void testSerialization() throws Exception {
        LCSQoSImpl original = new LCSQoSImpl(null, null, false, new ResponseTimeImpl(ResponseTimeCategory.lowdelay), null);

        // serialize
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        // deserialize
        byte[] pickled = out.toByteArray();
        InputStream in = new ByteArrayInputStream(pickled);
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        LCSQoSImpl copy = (LCSQoSImpl) o;

        // test result
        assertEquals(copy, original);
    }
}
