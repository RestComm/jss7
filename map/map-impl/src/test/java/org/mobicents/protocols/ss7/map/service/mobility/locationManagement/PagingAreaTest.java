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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.LAC;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.LocationArea;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class PagingAreaTest {

    public byte[] getData1() {
        return new byte[] { 48, 11, (byte) 128, 5, 66, (byte) 249, 16, 54, (byte) 186, (byte) 129, 2, 54, (byte) 186 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        PagingAreaImpl prim = new PagingAreaImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ArrayList<LocationArea> lst = prim.getLocationAreas();

        LAIFixedLength lai = lst.get(0).getLAIFixedLength();
        assertEquals(lai.getMCC(), 249);
        assertEquals(lai.getMNC(), 1);
        assertEquals(lai.getLac(), 14010);
        assertNull(lst.get(0).getLAC());

        LAC lac = lst.get(1).getLAC();
        assertEquals(lac.getLac(), 14010);
        assertNull(lst.get(1).getLAIFixedLength());
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testEncode() throws Exception {

        ArrayList<LocationArea> lst = new ArrayList<LocationArea>();
        LAIFixedLengthImpl lai = new LAIFixedLengthImpl(249, 1, 14010);
        LAC lac = new LACImpl(14010);
        LocationAreaImpl l1 = new LocationAreaImpl(lai);
        LocationAreaImpl l2 = new LocationAreaImpl(lac);
        lst.add(l1);
        lst.add(l2);
        PagingAreaImpl prim = new PagingAreaImpl(lst);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));
    }
}
