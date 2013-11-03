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

package org.mobicents.protocols.ss7.map.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class OctetStringBaseTest {

    private byte[] getEncodedData() {
        return new byte[] { 4, 5, 1, 2, 3, 4, 5 };
    }

    private byte[] getEncodedDataTooShort() {
        return new byte[] { 4, 1, 1 };
    }

    private byte[] getEncodedDataTooLong() {
        return new byte[] { 5, 8, 1, 2, 3, 4, 5, 6, 7, 8 };
    }

    private byte[] getData() {
        return new byte[] { 1, 2, 3, 4, 5 };
    }

    private byte[] getDataTooShort() {
        return new byte[] { 1 };
    }

    private byte[] getDataTooLong() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        // correct data
        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        TestOctetStringImpl pi = new TestOctetStringImpl();
        pi.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(Arrays.equals(getData(), pi.getData()));

        // bad data
        rawData = getEncodedDataTooShort();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        pi = new TestOctetStringImpl();
        try {
            pi.decodeAll(asn);
            assertFalse(true);
        } catch (MAPParsingComponentException e) {
            assertNotNull(e);
        }

        rawData = getEncodedDataTooLong();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        pi = new TestOctetStringImpl();
        try {
            pi.decodeAll(asn);
            assertFalse(true);
        } catch (MAPParsingComponentException e) {
            assertNotNull(e);
        }
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        // correct data
        TestOctetStringImpl pi = new TestOctetStringImpl(getData());
        AsnOutputStream asnOS = new AsnOutputStream();

        pi.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // bad data
        pi = new TestOctetStringImpl(null);
        asnOS = new AsnOutputStream();
        try {
            pi.encodeAll(asnOS);
            assertFalse(true);
        } catch (MAPException e) {
            assertNotNull(e);
        }

        pi = new TestOctetStringImpl(getDataTooShort());
        asnOS = new AsnOutputStream();
        try {
            pi.encodeAll(asnOS);
        } catch (MAPException e) {
            assertNotNull(e);
        }

        pi = new TestOctetStringImpl(getDataTooLong());
        asnOS = new AsnOutputStream();
        try {
            pi.encodeAll(asnOS);
            assertFalse(true);
        } catch (MAPException e) {
            assertNotNull(e);
        }

    }

    @Test(groups = { "functional.encode", "equality" })
    public void testEqality() throws Exception {

        byte[] testD1 = new byte[2];
        byte[] testD2 = new byte[2];
        byte[] testD3 = new byte[2];
        testD1[0] = 11;
        testD1[1] = 12;
        testD2[0] = 11;
        testD2[1] = 12;
        testD3[0] = 21;
        testD3[1] = 22;

        ExtPDPTypeImpl imp1 = new ExtPDPTypeImpl(testD1);
        ExtPDPTypeImpl imp2 = new ExtPDPTypeImpl(testD2);
        ExtPDPTypeImpl imp3 = new ExtPDPTypeImpl(testD3);

        assertTrue(imp1.equals(imp1));
        assertTrue(imp1.equals(imp2));
        assertFalse(imp1.equals(imp3));
        assertFalse(imp2.equals(imp3));

        int i1 = imp1.hashCode();
    }

    private class TestOctetStringImpl extends OctetStringBase {

        public TestOctetStringImpl(byte[] data) {
            super(2, 7, "Test OctetString primitive", data);
        }

        public TestOctetStringImpl() {
            super(2, 7, "Test OctetString primitive");
        }

        public byte[] getData() {
            return this.data;
        }
    }
}
