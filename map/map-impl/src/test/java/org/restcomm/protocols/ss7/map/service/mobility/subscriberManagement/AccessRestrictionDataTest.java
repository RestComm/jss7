/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.AccessRestrictionDataImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class AccessRestrictionDataTest {

    public byte[] getData() {
        return new byte[] { 3, 2, 2, 84 };
    };

    public byte[] getData1() {
        return new byte[] { 3, 2, 2, -88 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] rawData = getData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        AccessRestrictionDataImpl imp = new AccessRestrictionDataImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(!imp.getUtranNotAllowed());
        assertTrue(imp.getGeranNotAllowed());
        assertTrue(!imp.getGanNotAllowed());
        assertTrue(imp.getIHspaEvolutionNotAllowed());
        assertTrue(!imp.getEUtranNotAllowed());
        assertTrue(imp.getHoToNon3GPPAccessNotAllowed());

        rawData = getData1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        imp = new AccessRestrictionDataImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(imp.getUtranNotAllowed());
        assertTrue(!imp.getGeranNotAllowed());
        assertTrue(imp.getGanNotAllowed());
        assertTrue(!imp.getIHspaEvolutionNotAllowed());
        assertTrue(imp.getEUtranNotAllowed());
        assertTrue(!imp.getHoToNon3GPPAccessNotAllowed());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        AccessRestrictionDataImpl imp = new AccessRestrictionDataImpl(false, true, false, true, false, true);
        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);
        assertTrue(Arrays.equals(getData(), asnOS.toByteArray()));

        imp = new AccessRestrictionDataImpl(true, false, true, false, true, false);
        asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);
        assertTrue(Arrays.equals(getData1(), asnOS.toByteArray()));
    }
}
