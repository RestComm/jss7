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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.EpcAv;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.EpsAuthenticationSetList;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SendAuthenticationInfoResponseTest {

    private byte[] getEncodedData_V3_tripl() {
        return new byte[] { (byte) 163, 38, -96, 36, 48, 34, 4, 16, 15, -2, 18, -92, -49, 43, -35, -71, -78, -98, 109, 83, -76,
                -87, 77, -128, 4, 4, -32, 82, -17, -14, 4, 8, 31, 72, -93, 97, 78, -17, -52, 0 };
    }

    private byte[] getEncodedData_V3_Eps() {
        return new byte[] { (byte) 163, 80, -94, 78, 48, 76, 4, 16, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 2, 2,
                2, 2, 4, 16, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 32, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4 };
    }

    private byte[] getEncodedData_V2_tripl() {
        return new byte[] { 48, 36, 48, 34, 4, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 4, 4, 4, 4,
                4, 4, 4, 8, 8, 8, 8, 8, 8, 8, 8, 8 };
    }

    @Test
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData_V3_tripl();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        SendAuthenticationInfoResponseImpl asc = new SendAuthenticationInfoResponseImpl(3);
        asc.decodeAll(asn);

        assertEquals(tag, SendAuthenticationInfoResponseImpl._TAG_General);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        AuthenticationSetList asl = asc.getAuthenticationSetList();
        assertEquals(asl.getMapProtocolVersion(), 3);
        assertEquals(asl.getTripletList().getAuthenticationTriplets().size(), 1);
        assertNull(asl.getQuintupletList());

        assertNull(asc.getEpsAuthenticationSetList());
        assertNull(asc.getExtensionContainer());

        rawData = getEncodedData_V3_Eps();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new SendAuthenticationInfoResponseImpl(3);
        asc.decodeAll(asn);

        assertEquals(tag, SendAuthenticationInfoResponseImpl._TAG_General);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        asl = asc.getAuthenticationSetList();
        assertNull(asl);

        EpsAuthenticationSetList easl = asc.getEpsAuthenticationSetList();
        assertEquals(easl.getEpcAv().size(), 1);
        assertTrue(Arrays.equals(easl.getEpcAv().get(0).getRand(), EpcAvTest.getRandData()));
        assertTrue(Arrays.equals(easl.getEpcAv().get(0).getXres(), EpcAvTest.getXresData()));
        assertTrue(Arrays.equals(easl.getEpcAv().get(0).getAutn(), EpcAvTest.getAutnData()));
        assertTrue(Arrays.equals(easl.getEpcAv().get(0).getKasme(), EpcAvTest.getKasmeData()));

        assertNull(asc.getExtensionContainer());

        rawData = getEncodedData_V2_tripl();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new SendAuthenticationInfoResponseImpl(2);
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        asl = asc.getAuthenticationSetList();
        assertEquals(asl.getMapProtocolVersion(), 2);
        assertEquals(asl.getTripletList().getAuthenticationTriplets().size(), 1);
        assertTrue(Arrays.equals(asl.getTripletList().getAuthenticationTriplets().get(0).getRand(),
                TripletListTest.getRandData()));
        assertNull(asl.getQuintupletList());

        assertNull(asc.getEpsAuthenticationSetList());
        assertNull(asc.getExtensionContainer());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        ArrayList<AuthenticationTriplet> ats = new ArrayList<AuthenticationTriplet>();
        AuthenticationTripletImpl at = new AuthenticationTripletImpl(AuthenticationTripletTest.getRandData(),
                AuthenticationTripletTest.getSresData(), AuthenticationTripletTest.getKcData());
        ats.add(at);
        TripletListImpl tl = new TripletListImpl(ats);
        AuthenticationSetListImpl asl = new AuthenticationSetListImpl(tl);
        asl.setMapProtocolVersion(3);
        SendAuthenticationInfoResponseImpl asc = new SendAuthenticationInfoResponseImpl(3, asl, null, null);
        // long mapProtocolVersion, AuthenticationSetList authenticationSetList, MAPExtensionContainer extensionContainer,
        // EpsAuthenticationSetList epsAuthenticationSetList

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData_V3_tripl();
        assertTrue(Arrays.equals(rawData, encodedData));

        EpcAvImpl d1 = new EpcAvImpl(EpcAvTest.getRandData(), EpcAvTest.getXresData(), EpcAvTest.getAutnData(),
                EpcAvTest.getKasmeData(), null);
        ArrayList<EpcAv> epcAvs = new ArrayList<EpcAv>();
        epcAvs.add(d1);
        EpsAuthenticationSetListImpl easl = new EpsAuthenticationSetListImpl(epcAvs);
        asc = new SendAuthenticationInfoResponseImpl(3, null, null, easl);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_V3_Eps();
        assertTrue(Arrays.equals(rawData, encodedData));

        ats = new ArrayList<AuthenticationTriplet>();
        at = new AuthenticationTripletImpl(TripletListTest.getRandData(), TripletListTest.getSresData(),
                TripletListTest.getKcData());
        ats.add(at);
        tl = new TripletListImpl(ats);
        asl = new AuthenticationSetListImpl(tl);
        asl.setMapProtocolVersion(2);
        asc = new SendAuthenticationInfoResponseImpl(2, asl, null, null);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_V2_tripl();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
