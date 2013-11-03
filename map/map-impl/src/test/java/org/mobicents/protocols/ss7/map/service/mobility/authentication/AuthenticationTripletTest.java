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

package org.mobicents.protocols.ss7.map.service.mobility.authentication;

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
public class AuthenticationTripletTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 34, 4, 16, 15, (byte) 254, 18, (byte) 164, (byte) 207, 43, (byte) 221, (byte) 185, (byte) 178,
                (byte) 158, 109, 83, (byte) 180, (byte) 169, 77, (byte) 128, 4, 4, (byte) 224, 82, (byte) 239, (byte) 242, 4,
                8, 31, 72, (byte) 163, 97, 78, (byte) 239, (byte) 204, 0 };
    }

    static protected byte[] getRandData() {
        return new byte[] { 15, -2, 18, -92, -49, 43, -35, -71, -78, -98, 109, 83, -76, -87, 77, -128 };
    }

    static protected byte[] getSresData() {
        return new byte[] { -32, 82, -17, -14 };
    }

    static protected byte[] getKcData() {
        return new byte[] { 31, 72, -93, 97, 78, -17, -52, 0 };
    }

    @Test
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        AuthenticationTripletImpl asc = new AuthenticationTripletImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(Arrays.equals(asc.getRand(), getRandData()));
        assertTrue(Arrays.equals(asc.getSres(), getSresData()));
        assertTrue(Arrays.equals(asc.getKc(), getKcData()));

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        AuthenticationTripletImpl asc = new AuthenticationTripletImpl(getRandData(), getSresData(), getKcData());

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
