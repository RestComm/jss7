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
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.service.lsm.DeferredLocationEventTypeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CSGIdImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class BitStringBaseTest {

    private byte[] getEncodedData() {
        return new byte[] { 3, 3, 4, (byte) 0xF0, (byte) 0xF0 };
    }

    private byte[] getEncodedDataTooShort() {
        return new byte[] { 3, 2, 4, (byte) 0xF0 };
    }

    private byte[] getEncodedDataTooLong() {
        return new byte[] { 3, 5, 4, (byte) 0xF0, (byte) 0xF0, 0, 0 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        // correct data
        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        TestBitStringImpl pi = new TestBitStringImpl();
        pi.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(pi.getData().get(0));
        assertTrue(pi.getData().get(1));
        assertTrue(pi.getData().get(2));
        assertTrue(pi.getData().get(3));
        assertFalse(pi.getData().get(4));
        assertFalse(pi.getData().get(5));
        assertFalse(pi.getData().get(6));
        assertFalse(pi.getData().get(7));
        assertTrue(pi.getData().get(8));
        assertTrue(pi.getData().get(9));
        assertTrue(pi.getData().get(10));
        assertTrue(pi.getData().get(11));
        assertEquals(pi.getData().getStrictLength(), 12);

        // bad data
        rawData = getEncodedDataTooShort();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        pi = new TestBitStringImpl();
        try {
            pi.decodeAll(asn);
            assertFalse(true);
        } catch (MAPParsingComponentException e) {
            assertNotNull(e);
        }

        rawData = getEncodedDataTooLong();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        pi = new TestBitStringImpl();
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
        BitSetStrictLength bs = new BitSetStrictLength(12);
        bs.set(0);
        bs.set(1);
        bs.set(2);
        bs.set(3);
        bs.set(8);
        bs.set(9);
        bs.set(10);
        bs.set(11);

        TestBitStringImpl pi = new TestBitStringImpl(bs);
        AsnOutputStream asnOS = new AsnOutputStream();

        pi.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // bad data
        pi = new TestBitStringImpl(null);
        asnOS = new AsnOutputStream();
        try {
            pi.encodeAll(asnOS);
            assertFalse(true);
        } catch (MAPException e) {
            assertNotNull(e);
        }

    }

    @Test(groups = { "functional.encode", "equality" })
    public void testEquality() throws Exception {
        DeferredLocationEventTypeImpl imp1 = new DeferredLocationEventTypeImpl(true, false, true, false);
        DeferredLocationEventTypeImpl imp2 = new DeferredLocationEventTypeImpl(true, false, true, false);
        DeferredLocationEventTypeImpl imp3 = new DeferredLocationEventTypeImpl(false, true, true, false);
        CSGIdImpl implx = new CSGIdImpl();

        assertTrue(imp1.equals(imp2));
        assertFalse(imp1.equals(imp3));
        assertFalse(imp2.equals(imp3));
        assertFalse(implx.equals(imp3));

        int i1 = imp1.hashCode();
    }

    private class TestBitStringImpl extends BitStringBase {

        public TestBitStringImpl(BitSetStrictLength data) {
            super(12, 20, 12, "Test BitString primitive", data);
        }

        public TestBitStringImpl() {
            super(12, 20, 12, "Test BitString primitive");
        }

        public BitSetStrictLength getData() {
            return this.bitString;
        }
    }
}
