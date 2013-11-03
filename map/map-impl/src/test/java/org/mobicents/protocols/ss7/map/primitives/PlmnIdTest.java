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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class PlmnIdTest {

    private byte[] getEncodedData() {
        return new byte[] { 4, 3, -71, -2, -59 };
    }

    private byte[] getEncodedData3Dig() {
        return new byte[] { 4, 3, 0x04, 0x15, (byte) 0x93 };
    }

    private byte[] getEncodedData2Dig() {
        return new byte[] { 4, 3, 0x04, (byte) 0xF5, (byte) 0x93 };
    }

    private byte[] getData() {
        return new byte[] { -71, -2, -59 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        PlmnIdImpl pi = new PlmnIdImpl();
        pi.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(Arrays.equals(getData(), pi.getData()));

        rawData = getEncodedData3Dig();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        pi = new PlmnIdImpl();
        pi.decodeAll(asn);
        assertEquals(pi.getMcc(), 405);
        assertEquals(pi.getMnc(), 391);

        rawData = getEncodedData2Dig();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        pi = new PlmnIdImpl();
        pi.decodeAll(asn);
        assertEquals(pi.getMcc(), 405);
        assertEquals(pi.getMnc(), 39);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        PlmnIdImpl pi = new PlmnIdImpl(getData());
        AsnOutputStream asnOS = new AsnOutputStream();

        pi.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        pi = new PlmnIdImpl(405, 391);
        asnOS = new AsnOutputStream();
        pi.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData3Dig();
        assertTrue(Arrays.equals(rawData, encodedData));

        pi = new PlmnIdImpl(405, 39);
        asnOS = new AsnOutputStream();
        pi.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2Dig();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
