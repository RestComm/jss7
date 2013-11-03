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
package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.Cksn;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.CurrentSecurityContext;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.GSMSecurityContextData;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.Kc;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.UMTSSecurityContextData;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.AuthenticationSetListImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.AuthenticationTripletImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.CksnImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.CurrentSecurityContextImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.GSMSecurityContextDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.KcImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.TripletListImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SendIdentificationResponseTest {

    public byte[] getData1() {
        return new byte[] { 48, 48, 4, 8, 16, 33, 2, 2, 16, -119, 34, -9, 48, 36, 48, 34, 4, 16, 15, -2, 18, -92, -49, 43, -35,
                -71, -78, -98, 109, 83, -76, -87, 77, -128, 4, 4, -32, 82, -17, -14, 4, 8, 31, 72, -93, 97, 78, -17, -52, 0 };
    };

    public byte[] getData2() {
        return new byte[] { -93, 106, 4, 8, 16, 33, 2, 2, 16, -119, 34, -9, -96, 36, 48, 34, 4, 16, 15, -2, 18, -92, -49, 43,
                -35, -71, -78, -98, 109, 83, -76, -87, 77, -128, 4, 4, -32, 82, -17, -14, 4, 8, 31, 72, -93, 97, 78, -17, -52,
                0, -94, 15, -96, 13, 4, 8, 31, 72, -93, 97, 78, -17, -52, 0, 4, 1, 4, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4,
                11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    static protected byte[] getRandData() {
        return new byte[] { 15, -2, 18, -92, -49, 43, -35, -71, -78, -98, 109, 83, -76, -87, 77, -128 };
    }

    static protected byte[] getSresData() {
        return new byte[] { -32, 82, -17, -14 };
    }

    static protected byte[] getKcData() {
        return new byte[] { 31, 72, -93, 97, 78, -17, -52, 0 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
        // version 2
        byte[] data = this.getData1();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        SendIdentificationResponseImpl prim = new SendIdentificationResponseImpl(2);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(prim.getImsi().getData(), "011220200198227");
        assertEquals(prim.getAuthenticationSetList().getMapProtocolVersion(), 2);
        assertEquals(prim.getAuthenticationSetList().getTripletList().getAuthenticationTriplets().size(), 1);
        assertNull(prim.getAuthenticationSetList().getQuintupletList());

        assertNull(prim.getCurrentSecurityContext());
        assertNull(prim.getExtensionContainer());

        // version 3
        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();

        prim = new SendIdentificationResponseImpl(3);
        prim.decodeAll(asn);

        assertEquals(tag, SendIdentificationResponseImpl._TAG_SendIdentificationResponse);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        assertEquals(prim.getImsi().getData(), "011220200198227");

        assertEquals(prim.getAuthenticationSetList().getMapProtocolVersion(), 3);
        assertEquals(prim.getAuthenticationSetList().getTripletList().getAuthenticationTriplets().size(), 1);
        assertNull(prim.getAuthenticationSetList().getQuintupletList());

        GSMSecurityContextData gsm = prim.getCurrentSecurityContext().getGSMSecurityContextData();
        UMTSSecurityContextData umts = prim.getCurrentSecurityContext().getUMTSSecurityContextData();
        assertNull(umts);
        assertTrue(Arrays.equals(gsm.getKc().getData(), SendIdentificationResponseTest.getKcData()));
        assertEquals(gsm.getCksn().getData(), 4);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
        // version 2
        IMSI imsi = new IMSIImpl("011220200198227");

        ArrayList<AuthenticationTriplet> ats = new ArrayList<AuthenticationTriplet>();
        AuthenticationTripletImpl at = new AuthenticationTripletImpl(SendIdentificationResponseTest.getRandData(),
                SendIdentificationResponseTest.getSresData(), SendIdentificationResponseTest.getKcData());
        ats.add(at);
        TripletListImpl tl = new TripletListImpl(ats);
        AuthenticationSetListImpl authenticationSetList = new AuthenticationSetListImpl(tl);
        authenticationSetList.setMapProtocolVersion(2);

        CurrentSecurityContext currentSecurityContext = null;
        MAPExtensionContainer extensionContainer = null;
        SendIdentificationResponseImpl prim = new SendIdentificationResponseImpl(imsi, authenticationSetList,
                currentSecurityContext, extensionContainer, 2);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

        // version 3
        imsi = new IMSIImpl("011220200198227");

        ats = new ArrayList<AuthenticationTriplet>();
        at = new AuthenticationTripletImpl(SendIdentificationResponseTest.getRandData(),
                SendIdentificationResponseTest.getSresData(), SendIdentificationResponseTest.getKcData());
        ats.add(at);
        tl = new TripletListImpl(ats);
        authenticationSetList = new AuthenticationSetListImpl(tl);
        authenticationSetList.setMapProtocolVersion(3);

        Kc kc = new KcImpl(SendIdentificationResponseTest.getKcData());
        Cksn cksn = new CksnImpl(4);
        GSMSecurityContextDataImpl gsm = new GSMSecurityContextDataImpl(kc, cksn);
        currentSecurityContext = new CurrentSecurityContextImpl(gsm);

        extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        prim = new SendIdentificationResponseImpl(imsi, authenticationSetList, currentSecurityContext, extensionContainer, 3);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));
    }
}
