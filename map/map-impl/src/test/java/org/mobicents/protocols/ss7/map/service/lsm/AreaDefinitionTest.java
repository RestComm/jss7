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
import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.service.lsm.Area;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaIdentification;
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
public class AreaDefinitionTest {
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
        // TODO this is self generated trace. We need trace from operator
        return new byte[] { 48, 29, -96, 27, 48, 12, -128, 1, 5, -127, 7, 2, -15, 113, 13, 62, -41, -114, 48, 11, -128, 1, 3,
                -127, 6, 18, 112, 113, 3, -24, 100 };
    }

    @Test(groups = { "functional.decode", "service.lsm" })
    public void testDecode() throws Exception {
        byte[] data = getEncodedData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        AreaDefinition areaDef = new AreaDefinitionImpl();
        ((AreaDefinitionImpl) areaDef).decodeAll(asn);

        ArrayList<Area> areaList = areaDef.getAreaList();

        assertNotNull(areaList);
        assertEquals(areaList.size(), 2);

        assertEquals(areaList.get(0).getAreaType(), AreaType.utranCellId);
        assertEquals(areaList.get(1).getAreaType(), AreaType.routingAreaId);
        assertEquals(areaList.get(0).getAreaIdentification().getMCC(), 201);
        assertEquals(areaList.get(0).getAreaIdentification().getMNC(), 17);
        assertEquals(areaList.get(0).getAreaIdentification().getUtranCellId(), 222222222);
        assertEquals(areaList.get(1).getAreaIdentification().getMCC(), 210);
        assertEquals(areaList.get(1).getAreaIdentification().getMNC(), 177);
        assertEquals(areaList.get(1).getAreaIdentification().getLac(), 1000);
        assertEquals(areaList.get(1).getAreaIdentification().getRac(), 100);

    }

    @Test(groups = { "functional.encode", "service.lsm" })
    public void testEncode() throws Exception {
        byte[] data = getEncodedData();

        AreaIdentification ai1 = new AreaIdentificationImpl(AreaType.utranCellId, 201, 17, 0, 222222222);
        AreaIdentification ai2 = new AreaIdentificationImpl(AreaType.routingAreaId, 210, 177, 1000, 100);
        Area area1 = new AreaImpl(AreaType.utranCellId, ai1);
        Area area2 = new AreaImpl(AreaType.routingAreaId, ai2);

        ArrayList<Area> areaList = new ArrayList<Area>();
        areaList.add(area1);
        areaList.add(area2);
        AreaDefinition areaDef = new AreaDefinitionImpl(areaList);

        AsnOutputStream asnOS = new AsnOutputStream();
        ((AreaDefinitionImpl) areaDef).encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));

    }

    @Test(groups = { "functional.serialize", "service.lsm" })
    public void testSerialization() throws Exception {
        AreaIdentification ai1 = new AreaIdentificationImpl(AreaType.utranCellId, 201, 17, 0, 222222222);
        AreaIdentification ai2 = new AreaIdentificationImpl(AreaType.routingAreaId, 210, 177, 1000, 100);
        Area area1 = new AreaImpl(AreaType.utranCellId, ai1);
        Area area2 = new AreaImpl(AreaType.routingAreaId, ai2);

        ArrayList<Area> areaList = new ArrayList<Area>();
        areaList.add(area1);
        areaList.add(area2);

        AreaDefinition original = new AreaDefinitionImpl(areaList);
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
        AreaDefinitionImpl copy = (AreaDefinitionImpl) o;

        // test result
        for (int i = 0; i < areaList.size(); i++) {
            Area a1 = copy.getAreaList().get(i);
            Area a2 = original.getAreaList().get(i);
            assertTrue(a1.equals(a2));
        }

    }
}
