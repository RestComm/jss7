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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class IMSIWithLMSITest {

    public byte[] getData1() {
        return new byte[] { 48, 12, 4, 4, 17, 17, 119, 119, 4, 4, 1, 2, 3, 4 };
    };

    public byte[] getDataLmsi() {
        return new byte[] { 1, 2, 3, 4 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        IMSIWithLMSIImpl prim = new IMSIWithLMSIImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(prim.getImsi().getData().equals("11117777"));
        assertTrue(Arrays.equals(prim.getLmsi().getData(), getDataLmsi()));
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testEncode() throws Exception {

        IMSIImpl imsi = new IMSIImpl("11117777");
        LMSIImpl lmsi = new LMSIImpl(getDataLmsi());
        IMSIWithLMSIImpl prim = new IMSIWithLMSIImpl(imsi, lmsi);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));
    }

}
