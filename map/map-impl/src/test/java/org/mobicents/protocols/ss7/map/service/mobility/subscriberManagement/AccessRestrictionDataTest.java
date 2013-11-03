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
