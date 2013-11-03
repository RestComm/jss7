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
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
 *
 * @author normandes
 *
 */
public class RequestedEquipmentInfoTest {

    private byte[] getEncodedData() {
        // TODO this is self generated trace. We need trace from operator
        return new byte[] { 3, 2, 6, -128 };
    }

    @Test(groups = { "functional.decode", "imei" })
    public void testDecode() throws Exception {
        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        RequestedEquipmentInfoImpl imp = new RequestedEquipmentInfoImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(imp.getEquipmentStatus());
        assertFalse(imp.getBmuef());
    }

    @Test(groups = { "functional.encode", "imei" })
    public void testEncode() throws Exception {
        RequestedEquipmentInfoImpl imp = new RequestedEquipmentInfoImpl(true, false);

        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
