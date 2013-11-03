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

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.CK;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.Cksn;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.GSMSecurityContextData;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.IK;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.KSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.Kc;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.UMTSSecurityContextData;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class CurrentSecurityContextTest {

    public byte[] getData1() {
        return new byte[] { -96, 13, 4, 8, 4, 4, 1, 2, 3, 4, 4, 4, 4, 1, 4 };
    };

    public byte[] getData2() {
        return new byte[] { -95, 39, 4, 16, 4, 4, 1, 2, 3, 4, 4, 4, 1, 2, 3, 4, 4, 4, 1, 2, 4, 16, 2, 5, 27, 6, 23, 23, 34, 56,
                34, 76, 34, 4, 4, 1, 2, 3, 4, 1, 2 };
    };

    public byte[] getDataKc() {
        return new byte[] { 4, 4, 1, 2, 3, 4, 4, 4 };
    };

    public byte[] getDataCk() {
        return new byte[] { 4, 4, 1, 2, 3, 4, 4, 4, 1, 2, 3, 4, 4, 4, 1, 2 };
    };

    public byte[] getDataIk() {
        return new byte[] { 2, 5, 27, 6, 23, 23, 34, 56, 34, 76, 34, 4, 4, 1, 2, 3 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        // option 1
        byte[] data = this.getData1();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        CurrentSecurityContextImpl prim = new CurrentSecurityContextImpl();
        prim.decodeAll(asn);

        assertEquals(tag, CurrentSecurityContextImpl._TAG_gsmSecurityContextData);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        GSMSecurityContextData gsm = prim.getGSMSecurityContextData();
        UMTSSecurityContextData umts = prim.getUMTSSecurityContextData();
        assertNull(umts);
        assertTrue(Arrays.equals(gsm.getKc().getData(), getDataKc()));
        assertEquals(gsm.getCksn().getData(), 4);

        // option 2
        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();

        prim = new CurrentSecurityContextImpl();
        prim.decodeAll(asn);

        assertEquals(tag, CurrentSecurityContextImpl._TAG_umtsSecurityContextData);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        gsm = prim.getGSMSecurityContextData();
        umts = prim.getUMTSSecurityContextData();
        assertNull(gsm);

        assertTrue(Arrays.equals(umts.getCK().getData(), getDataCk()));
        assertTrue(Arrays.equals(umts.getIK().getData(), getDataIk()));
        assertEquals(umts.getKSI().getData(), 2);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        // option 1
        Kc kc = new KcImpl(getDataKc());
        Cksn cksn = new CksnImpl(4);

        GSMSecurityContextDataImpl gsm = new GSMSecurityContextDataImpl(kc, cksn);
        CurrentSecurityContextImpl prim = new CurrentSecurityContextImpl(gsm);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

        // option 2
        CK ck = new CKImpl(getDataCk());
        IK ik = new IKImpl(getDataIk());
        KSI ksi = new KSIImpl(2);
        UMTSSecurityContextDataImpl umts = new UMTSSecurityContextDataImpl(ck, ik, ksi);
        prim = new CurrentSecurityContextImpl(umts);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));
    }
}
