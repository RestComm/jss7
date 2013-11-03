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

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TripletListTest {

    private byte[] getEncodedData() {
        return new byte[] { (byte) 160, 36, 48, 34, 4, 16, 15, (byte) 254, 18, (byte) 164, (byte) 207, 43, (byte) 221,
                (byte) 185, (byte) 178, (byte) 158, 109, 83, (byte) 180, (byte) 169, 77, (byte) 128, 4, 4, (byte) 224, 82,
                (byte) 239, (byte) 242, 4, 8, 31, 72, (byte) 163, 97, 78, (byte) 239, (byte) 204, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { -96, 72, 48, 34, 4, 16, 15, -2, 18, -92, -49, 43, -35, -71, -78, -98, 109, 83, -76, -87, 77, -128,
                4, 4, -32, 82, -17, -14, 4, 8, 31, 72, -93, 97, 78, -17, -52, 0, 48, 34, 4, 16, 16, 16, 16, 16, 16, 16, 16, 16,
                16, 16, 16, 16, 16, 16, 16, 16, 4, 4, 4, 4, 4, 4, 4, 8, 8, 8, 8, 8, 8, 8, 8, 8 };
    }

    public static byte[] getRandData() {
        return new byte[] { 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16 };
    }

    public static byte[] getSresData() {
        return new byte[] { 4, 4, 4, 4 };
    }

    public static byte[] getKcData() {
        return new byte[] { 8, 8, 8, 8, 8, 8, 8, 8 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        TripletListImpl asc = new TripletListImpl();
        asc.decodeAll(asn);

        assertEquals(tag, AuthenticationSetListImpl._TAG_tripletList);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        assertEquals(asc.getAuthenticationTriplets().size(), 1);

        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(0).getRand(), AuthenticationTripletTest.getRandData()));
        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(0).getSres(), AuthenticationTripletTest.getSresData()));
        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(0).getKc(), AuthenticationTripletTest.getKcData()));

        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new TripletListImpl();
        asc.decodeAll(asn);

        assertEquals(tag, AuthenticationSetListImpl._TAG_tripletList);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        assertEquals(asc.getAuthenticationTriplets().size(), 2);

        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(0).getRand(), AuthenticationTripletTest.getRandData()));
        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(0).getSres(), AuthenticationTripletTest.getSresData()));
        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(0).getKc(), AuthenticationTripletTest.getKcData()));

        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(1).getRand(), getRandData()));
        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(1).getSres(), getSresData()));
        assertTrue(Arrays.equals(asc.getAuthenticationTriplets().get(1).getKc(), getKcData()));

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        AuthenticationTripletImpl d1 = new AuthenticationTripletImpl(AuthenticationTripletTest.getRandData(),
                AuthenticationTripletTest.getSresData(), AuthenticationTripletTest.getKcData());
        ArrayList<AuthenticationTriplet> arr = new ArrayList<AuthenticationTriplet>();
        arr.add(d1);
        TripletListImpl asc = new TripletListImpl(arr);

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, AuthenticationSetListImpl._TAG_tripletList);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        d1 = new AuthenticationTripletImpl(AuthenticationTripletTest.getRandData(), AuthenticationTripletTest.getSresData(),
                AuthenticationTripletTest.getKcData());
        AuthenticationTripletImpl d2 = new AuthenticationTripletImpl(getRandData(), getSresData(), getKcData());
        arr = new ArrayList<AuthenticationTriplet>();
        arr.add(d1);
        arr.add(d2);
        asc = new TripletListImpl(arr);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, AuthenticationSetListImpl._TAG_tripletList);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
