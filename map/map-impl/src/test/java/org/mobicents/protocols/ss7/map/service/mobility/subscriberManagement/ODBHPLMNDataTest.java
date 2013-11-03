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
public class ODBHPLMNDataTest {

    private byte[] getEncodedData() {
        return new byte[] { 3, 2, 4, 80 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { 3, 2, 4, -96 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        ODBHPLMNDataImpl imp = new ODBHPLMNDataImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(!imp.getPlmnSpecificBarringType1());
        assertTrue(imp.getPlmnSpecificBarringType2());
        assertTrue(!imp.getPlmnSpecificBarringType3());
        assertTrue(imp.getPlmnSpecificBarringType4());

        rawData = getEncodedData1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        imp = new ODBHPLMNDataImpl();
        imp.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(imp.getPlmnSpecificBarringType1());
        assertTrue(!imp.getPlmnSpecificBarringType2());
        assertTrue(imp.getPlmnSpecificBarringType3());
        assertTrue(!imp.getPlmnSpecificBarringType4());
    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        ODBHPLMNDataImpl imp = new ODBHPLMNDataImpl(false, true, false, true);
        AsnOutputStream asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);
        assertTrue(Arrays.equals(getEncodedData(), asnOS.toByteArray()));

        imp = new ODBHPLMNDataImpl(true, false, true, false);
        asnOS = new AsnOutputStream();
        imp.encodeAll(asnOS);
        assertTrue(Arrays.equals(getEncodedData1(), asnOS.toByteArray()));
    }
}
