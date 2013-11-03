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
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class OctetStringLength1BaseTest {
    private byte[] getEncodedData() {
        return new byte[] { 4, 1, 1 };
    }

    private byte[] getEncodedDataTooLong() {
        return new byte[] { 5, 8, 1, 2, 3, 4, 5, 6, 7, 8 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        // correct data
        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        TestOctetStringLength1Impl pi = new TestOctetStringLength1Impl();
        pi.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertEquals(pi.getData(), 1);

        // bad data
        rawData = getEncodedDataTooLong();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        pi = new TestOctetStringLength1Impl();
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
        TestOctetStringLength1Impl pi = new TestOctetStringLength1Impl(1);
        AsnOutputStream asnOS = new AsnOutputStream();

        pi.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

    }

    @Test(groups = { "functional.encode", "equality" })
    public void testEqality() throws Exception {

        Ext4QoSSubscribedImpl imp1 = new Ext4QoSSubscribedImpl(10);
        Ext4QoSSubscribedImpl imp2 = new Ext4QoSSubscribedImpl(10);
        Ext4QoSSubscribedImpl imp3 = new Ext4QoSSubscribedImpl(12);

        assertTrue(imp1.equals(imp1));
        assertTrue(imp1.equals(imp2));
        assertFalse(imp1.equals(imp3));
        assertFalse(imp2.equals(imp3));

        int i1 = imp1.hashCode();
    }

    private class TestOctetStringLength1Impl extends OctetStringLength1Base {

        public TestOctetStringLength1Impl(int data) {
            super("Test OctetStringLength1 primitive", data);
        }

        public TestOctetStringLength1Impl() {
            super("Test OctetStringLength1 primitive");
        }

        public int getData() {
            return this.data;
        }
    }

}
