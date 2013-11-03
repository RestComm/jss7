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

package org.mobicents.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaType;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AreaIdentificationTest {

    public byte[] getData1() {
        return new byte[] { 4, 2, 82, (byte) 240 };
    };

    public byte[] getData1Val() {
        return new byte[] { 82, (byte) 240 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 3, 82, (byte) 240, 112 };
    };

    public byte[] getData3() {
        return new byte[] { 4, 5, 82, (byte) 128, 118, 17, 92 };
    };

    public byte[] getData4() {
        return new byte[] { 4, 6, 82, (byte) 128, 118, 17, 92, (byte) 200 };
    };

    public byte[] getData5() {
        return new byte[] { 4, 7, 82, (byte) 128, 118, 17, 92, (byte) 214, (byte) 216 };
    };

    public byte[] getData6() {
        return new byte[] { 4, 7, 82, (byte) 128, 118, (byte) 248, 0, 0, 1 };
    };

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.STRING_OCTET);

        AreaIdentificationImpl prim = new AreaIdentificationImpl();
        prim.decodeAll(asn);

        assertNotNull(prim.getData());
        assertTrue(Arrays.equals(getData1Val(), prim.getData()));

        assertEquals(prim.getMCC(), 250);
        try {
            prim.getMNC();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getLac();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getRac();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getUtranCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        // assertEquals(prim.getMNC(), 1);
        // assertEquals(prim.getLac(), 4444);
        // assertEquals(prim.getCellId(), 3333);
        // prim.getRac();
        // prim.getUtranCellId();

        data = this.getData2();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.STRING_OCTET);

        prim = new AreaIdentificationImpl();
        prim.decodeAll(asn);

        assertNotNull(prim.getData());

        assertEquals(prim.getMCC(), 250);
        assertEquals(prim.getMNC(), 7);
        try {
            prim.getLac();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getRac();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getUtranCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        // assertEquals(prim.getLac(), 4444);
        // assertEquals(prim.getCellId(), 3333);
        // prim.getRac();
        // prim.getUtranCellId();

        data = this.getData3();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.STRING_OCTET);

        prim = new AreaIdentificationImpl();
        prim.decodeAll(asn);

        assertNotNull(prim.getData());

        assertEquals(prim.getMCC(), 250);
        assertEquals(prim.getMNC(), 678);
        assertEquals(prim.getLac(), 4444);
        try {
            prim.getCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getRac();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getUtranCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        // assertEquals(prim.getCellId(), 3333);
        // prim.getRac();
        // prim.getUtranCellId();

        data = this.getData4();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.STRING_OCTET);

        prim = new AreaIdentificationImpl();
        prim.decodeAll(asn);

        assertNotNull(prim.getData());

        assertEquals(prim.getMCC(), 250);
        assertEquals(prim.getMNC(), 678);
        assertEquals(prim.getLac(), 4444);
        assertEquals(prim.getRac(), 200);
        try {
            prim.getCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        try {
            prim.getUtranCellId();
            fail("Must be exeption");
        } catch (MAPException ee) {
        }
        // assertEquals(prim.getCellId(), 3333);
        // prim.getUtranCellId();

        data = this.getData5();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.STRING_OCTET);

        prim = new AreaIdentificationImpl();
        prim.decodeAll(asn);

        assertNotNull(prim.getData());

        assertEquals(prim.getMCC(), 250);
        assertEquals(prim.getMNC(), 678);
        assertEquals(prim.getLac(), 4444);
        assertEquals(prim.getCellId(), 55000);

        data = this.getData6();

        asn = new AsnInputStream(data);
        tag = asn.readTag();
        assertEquals(tag, Tag.STRING_OCTET);

        prim = new AreaIdentificationImpl();
        prim.decodeAll(asn);

        assertNotNull(prim.getData());

        assertEquals(prim.getMCC(), 250);
        assertEquals(prim.getMNC(), 678);
        assertEquals(prim.getUtranCellId(), (int) (4160749569L));
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testEncode() throws Exception {

        AreaIdentificationImpl prim = new AreaIdentificationImpl(AreaType.countryCode, 250, 0, 0, 0);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

        prim = new AreaIdentificationImpl(getData1Val());

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

        prim = new AreaIdentificationImpl(AreaType.plmnId, 250, 7, 0, 0);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));

        prim = new AreaIdentificationImpl(AreaType.locationAreaId, 250, 678, 4444, 0);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData3()));

        prim = new AreaIdentificationImpl(AreaType.routingAreaId, 250, 678, 4444, 200);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData4()));

        prim = new AreaIdentificationImpl(AreaType.cellGlobalId, 250, 678, 4444, 55000);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData5()));

        prim = new AreaIdentificationImpl(AreaType.utranCellId, 250, 678, 0, (int) (4160749569L));

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData6()));
    }
}
