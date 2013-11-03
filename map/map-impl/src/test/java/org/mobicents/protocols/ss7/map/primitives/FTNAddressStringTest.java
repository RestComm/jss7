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

package org.mobicents.protocols.ss7.map.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class FTNAddressStringTest {

    private byte[] getEncodedData() {
        return new byte[] { 4, 6, -72, 33, 67, 101, -121, 9 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        FTNAddressStringImpl addrStr = new FTNAddressStringImpl();
        addrStr.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(addrStr.getAddressNature(), AddressNature.network_specific_number);
        assertEquals(addrStr.getNumberingPlan(), NumberingPlan.national);
        assertEquals(addrStr.getAddress(), "1234567890");
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        FTNAddressStringImpl addrStr = new FTNAddressStringImpl(AddressNature.network_specific_number, NumberingPlan.national,
                "1234567890");
        AsnOutputStream asnOS = new AsnOutputStream();

        addrStr.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        byte[] rawData = getEncodedData();

        assertTrue(Arrays.equals(rawData, encodedData));

    }

    @Test(groups = { "functional.serialize", "primitives" })
    public void testSerialization() throws Exception {
        FTNAddressStringImpl original = new FTNAddressStringImpl(AddressNature.network_specific_number, NumberingPlan.national,
                "1234567890");
        // serialize
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        // deserialize
        byte[] pickled = out.toByteArray();
        InputStream in = new ByteArrayInputStream(pickled);
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        FTNAddressStringImpl copy = (FTNAddressStringImpl) o;

        // test result
        assertEquals(copy.getAddressNature(), original.getAddressNature());
        assertEquals(copy.getNumberingPlan(), original.getNumberingPlan());
        assertEquals(copy.getAddress(), original.getAddress());
    }

}
