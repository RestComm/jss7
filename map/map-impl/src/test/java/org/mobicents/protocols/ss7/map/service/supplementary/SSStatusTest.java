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
import static org.testng.Assert.assertFalse;
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
public class SSStatusTest {

    private byte[] getEncodedData1() {
        return new byte[] { 4, 1, 10 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 4, 1, 5 };
    }

    @Test(groups = { "functional.decode", "service.supplementary" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData1();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        SSStatusImpl impl = new SSStatusImpl();
        impl.decodeAll(asn);

        assertTrue(impl.getQBit());
        assertFalse(impl.getPBit());
        assertTrue(impl.getRBit());
        assertFalse(impl.getABit());

        rawData = getEncodedData2();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        impl = new SSStatusImpl();
        impl.decodeAll(asn);

        assertFalse(impl.getQBit());
        assertTrue(impl.getPBit());
        assertFalse(impl.getRBit());
        assertTrue(impl.getABit());
    }

    @Test(groups = { "functional.encode", "service.supplementary" })
    public void testEncode() throws Exception {

        SSStatusImpl impl = new SSStatusImpl(true, false, true, false);
        AsnOutputStream asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new SSStatusImpl(false, true, false, true);
        asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
