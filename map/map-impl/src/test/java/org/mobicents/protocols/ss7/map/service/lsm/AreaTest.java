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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.service.lsm.Area;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class AreaTest {

    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    public byte[] getEncodedData() {
        return new byte[] { 48, 8, -128, 1, 1, -127, 3, 9, 112, 113 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
        byte[] data = getEncodedData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        Area area = new AreaImpl();
        ((AreaImpl) area).decodeAll(asn);

        assertNotNull(area.getAreaType());
        assertEquals(area.getAreaType(), AreaType.plmnId);

        assertNotNull(area.getAreaIdentification());
        assertEquals(area.getAreaIdentification().getMCC(), 900);
        assertEquals(area.getAreaIdentification().getMNC(), 177);

    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {

        byte[] data = getEncodedData();

        AreaIdentificationImpl ai = new AreaIdentificationImpl(AreaType.plmnId, 900, 177, 0, 0);
        Area area = new AreaImpl(AreaType.plmnId, ai);

        AsnOutputStream asnOS = new AsnOutputStream();
        ((AreaImpl) area).encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

    }

    @Test(groups = { "functional.serialize", "service.lsm" })
    public void testSerialization() throws Exception {
        AreaIdentificationImpl ai = new AreaIdentificationImpl(AreaType.plmnId, 900, 177, 0, 0);
        Area original = new AreaImpl(AreaType.utranCellId, ai);

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
        AreaImpl copy = (AreaImpl) o;

        // test result
        assertEquals(copy.getAreaType(), original.getAreaType());
        assertEquals(copy.getAreaIdentification().getMCC(), original.getAreaIdentification().getMCC());
        assertEquals(copy.getAreaIdentification().getMNC(), original.getAreaIdentification().getMNC());

    }

}
