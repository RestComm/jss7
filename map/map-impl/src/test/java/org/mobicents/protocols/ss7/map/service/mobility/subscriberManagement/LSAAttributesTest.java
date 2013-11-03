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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentificationPriorityValue;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class LSAAttributesTest {

    public byte[] getData() {
        return new byte[] { 4, 1, 57 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 1, 15 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        // option 1
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        LSAAttributesImpl prim = new LSAAttributesImpl();
        prim.decodeAll(asn);
        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(prim.isActiveModeSupportAvailable());
        assertTrue(prim.isPreferentialAccessAvailable());
        assertEquals(prim.getLSAIdentificationPriority(), LSAIdentificationPriorityValue.Priority_10);

        // option 2
        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new LSAAttributesImpl();
        prim.decodeAll(asn);
        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        assertFalse(prim.isActiveModeSupportAvailable());
        assertFalse(prim.isPreferentialAccessAvailable());
        assertEquals(prim.getLSAIdentificationPriority(), LSAIdentificationPriorityValue.Priority_16);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        // option 1
        LSAAttributesImpl prim = new LSAAttributesImpl(LSAIdentificationPriorityValue.Priority_10, true, true);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        // option 2
        prim = new LSAAttributesImpl(LSAIdentificationPriorityValue.Priority_16, false, false);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));

    }

}
