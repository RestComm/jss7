/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.map.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.testng.annotations.Test;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class AddressStringTest {

    byte[] rawData = new byte[] { 4, 9, (byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2 };
    byte[] rawData2 = new byte[] { 4, 5, -106, 33, -29, 78, -11 };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        AddressStringImpl addStr = new AddressStringImpl();
        addStr.decodeAll(asn);

        assertEquals(tag, 4);
        assertFalse(addStr.isExtension());
        assertEquals(addStr.getAddressNature(), AddressNature.international_number);
        assertEquals(addStr.getNumberingPlan(), NumberingPlan.land_mobile);
        assertEquals(addStr.getAddress(), "204208300008002");


        asn = new AsnInputStream(rawData2);

        tag = asn.readTag();
        addStr = new AddressStringImpl();
        addStr.decodeAll(asn);

        assertEquals(tag, 4);
        assertFalse(addStr.isExtension());
        assertEquals(addStr.getAddressNature(), AddressNature.international_number);
        assertEquals(addStr.getNumberingPlan(), NumberingPlan.land_mobile);
        assertEquals(addStr.getAddress(), "123cc45");
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        AddressStringImpl addStr = new AddressStringImpl(AddressNature.international_number, NumberingPlan.land_mobile,
                "204208300008002");
        AsnOutputStream asnOS = new AsnOutputStream();
        addStr.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData, encodedData));

        addStr = new AddressStringImpl(AddressNature.international_number, NumberingPlan.land_mobile,
                "123cc45");
        asnOS = new AsnOutputStream();
        addStr.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData2, encodedData));

        addStr = new AddressStringImpl(AddressNature.international_number, NumberingPlan.land_mobile,
                "123CC45");
        asnOS = new AsnOutputStream();
        addStr.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(rawData2, encodedData));
    }

    @Test(groups = { "functional.serialize", "primitives" })
    public void testSerialization() throws Exception {
        AddressStringImpl original = new AddressStringImpl(AddressNature.international_number, NumberingPlan.land_mobile,
                "204208300008002");
        // serialize
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(original);
        oos.close();

        // deserialize
        byte[] pickled = out.toByteArray();
        String xml = new String(pickled);
        System.out.println(xml);

        InputStream in = new ByteArrayInputStream(pickled);
        ObjectInputStream ois = new ObjectInputStream(in);
        Object o = ois.readObject();
        AddressStringImpl copy = (AddressStringImpl) o;

        // test result
        assertEquals(copy.getAddressNature(), original.getAddressNature());
        assertEquals(copy.getNumberingPlan(), original.getNumberingPlan());
        assertEquals(copy.getAddress(), original.getAddress());
    }

}
