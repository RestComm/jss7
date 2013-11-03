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
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.testng.annotations.Test;

public class ADDInfoTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 12, -128, 8, 33, 67, 101, -121, 9, 33, 67, -11, -127, 0 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        ADDInfoImpl asc = new ADDInfoImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(asc.getImeisv().getIMEI().equals("123456789012345"));
        assertTrue(asc.getSkipSubscriberDataUpdate());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        IMEIImpl imeisv = new IMEIImpl("123456789012345");
        ADDInfoImpl asc = new ADDInfoImpl(imeisv, true);
        // IMEI imeisv, boolean skipSubscriberDataUpdate

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
