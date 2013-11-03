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
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.Cksn;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.Kc;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class GSMSecurityContextDataTest {

    public byte[] getData() {
        return new byte[] { 48, 13, 4, 8, 4, 4, 1, 2, 3, 4, 4, 4, 4, 1, 4 };
    };

    public byte[] getDataKc() {
        return new byte[] { 4, 4, 1, 2, 3, 4, 4, 4 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        GSMSecurityContextDataImpl prim = new GSMSecurityContextDataImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(Arrays.equals(prim.getKc().getData(), getDataKc()));
        assertEquals(prim.getCksn().getData(), 4);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        Kc kc = new KcImpl(getDataKc());
        Cksn cksn = new CksnImpl(4);

        GSMSecurityContextDataImpl prim = new GSMSecurityContextDataImpl(kc, cksn);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

    }
}
