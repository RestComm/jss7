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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class GroupIdTest {

    public byte[] getData() {
        return new byte[] { 4, 3, 33, 67, 101 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 3, 33, 67, -11 };
    };

    public byte[] getData3() {
        return new byte[] { 4, 3, 33, 67, -1 };
    };

    public byte[] getData4() {
        return new byte[] { 4, 3, 33, -13, -1 };
    };

    public byte[] getData5() {
        return new byte[] { 4, 3, 33, -1, -1 };
    };

    public byte[] getData6() {
        return new byte[] { 4, 3, -15, -1, -1 };
    };

    public byte[] getData7() {
        return new byte[] { 4, 3, -1, -1, -1 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        // option 1
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        GroupIdImpl prim = new GroupIdImpl();
        prim.decodeAll(asn);
        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertTrue(prim.getGroupId().equals("123456"));

        // option 2
        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new GroupIdImpl();
        prim.decodeAll(asn);
        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertTrue(prim.getGroupId().equals("12345"));

        // option 3
        data = this.getData3();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new GroupIdImpl();
        prim.decodeAll(asn);
        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertTrue(prim.getGroupId().equals("1234"));

        // option 4
        data = this.getData4();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new GroupIdImpl();
        prim.decodeAll(asn);
        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertTrue(prim.getGroupId().equals("123"));

        // option 5
        data = this.getData5();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new GroupIdImpl();
        prim.decodeAll(asn);
        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertTrue(prim.getGroupId().equals("12"));

        // option 6
        data = this.getData6();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new GroupIdImpl();
        prim.decodeAll(asn);
        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertTrue(prim.getGroupId().equals("1"));

        // option 7
        data = this.getData7();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new GroupIdImpl();
        prim.decodeAll(asn);
        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertTrue(prim.getGroupId().equals(""));

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        // option 1
        GroupIdImpl prim = new GroupIdImpl("123456");
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        // option 2
        prim = new GroupIdImpl("12345");
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));

        // option 3
        prim = new GroupIdImpl("1234");
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData3()));

        // option 4
        prim = new GroupIdImpl("123");
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData4()));

        // option 5
        prim = new GroupIdImpl("12");
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData5()));

        // option 6
        prim = new GroupIdImpl("1");
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData6()));

        // option 7
        prim = new GroupIdImpl("");
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData7()));
    }

}
