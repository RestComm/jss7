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

package org.mobicents.protocols.ss7.map.service.mobility.imei;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
 *
 * @author normandes
 *
 */
public class UESBIIuTest {

    public byte[] getEncodedData() {
        // TODO this is self generated trace. We need trace from operator
        return new byte[] { 48, 8, -128, 2, 7, -128, -127, 2, 7, 0 };
    }

    @Test(groups = { "functional.decode", "imei" })
    public void testDecode() throws Exception {
        byte[] data = getEncodedData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        UESBIIuImpl imp = new UESBIIuImpl();
        imp.decodeAll(asn);

        BitSetStrictLength bsUESBIIuA = imp.getUESBI_IuA().getData();
        BitSetStrictLength bsUESBIIuB = imp.getUESBI_IuB().getData();

        assertTrue(bsUESBIIuA.get(0));
        assertFalse(bsUESBIIuB.get(0));
    }

    @Test(groups = { "functional.encode", "imei" })
    public void testEncode() throws Exception {
        BitSetStrictLength bsUESBIIuA = new BitSetStrictLength(1);
        bsUESBIIuA.set(0);
        UESBIIuAImpl impUESBIIuA = new UESBIIuAImpl(bsUESBIIuA);

        BitSetStrictLength bsUESBIIuB = new BitSetStrictLength(1);
        UESBIIuBImpl impUESBIIuB = new UESBIIuBImpl(bsUESBIIuB);

        UESBIIuImpl imp = new UESBIIuImpl(impUESBIIuA, impUESBIIuB);

        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
