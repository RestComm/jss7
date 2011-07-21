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

package org.mobicents.protocols.ss7.map.service.lsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.map.MapServiceFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.service.lsm.Area;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaDefinition;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaList;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaType;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class AreaDefinitionTest {
	MapServiceFactory mapServiceFactory = new MapServiceFactoryImpl();

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testDecode() throws Exception {
		// TODO this is self generated trace. We need trace from operator
		byte[] data = new byte[] { (byte) 0xa0, 0x14, 0x30, 0x08, (byte) 0x80, 0x01, 0x05, (byte) 0x81, 0x03, 0x09, 0x70, 0x71, 0x30, 0x08, (byte) 0x80, 0x01,
				0x03, (byte) 0x81, 0x03, 0x04, 0x30, 0x31 };

		Parameter p = TcapFactory.createParameter();
		p.setPrimitive(false);
		p.setData(data);

		AreaDefinition areaDef = new AreaDefinitionImpl();
		areaDef.decode(p);

		AreaList areaList = areaDef.getAreaList();

		assertNotNull(areaList);
		assertEquals(2, areaList.getAreas().length);
		Area[] areas = areaList.getAreas();
		assertNotNull(areas[0].getAreaIdentification());
		assertTrue(Arrays.equals(new byte[] { 0x09, 0x70, 0x71 }, areas[0].getAreaIdentification()));

	}

	@Test
	public void testEncode() throws Exception {
		// TODO this is self generated trace. We need trace from operator
		byte[] data = new byte[] { (byte) 0xa0, 0x14, 0x30, 0x08, (byte) 0x80, 0x01, 0x05, (byte) 0x81, 0x03, 0x09, 0x70, 0x71, 0x30, 0x08, (byte) 0x80, 0x01,
				0x03, (byte) 0x81, 0x03, 0x04, 0x30, 0x31 };

		Area area1 = new AreaImpl(AreaType.utranCellId, new byte[] { 0x09, 0x70, 0x71 });
		Area area2 = new AreaImpl(AreaType.routingAreaId, new byte[] { 0x04, 0x30, 0x31 });

		AreaList areaList = new AreaListImpl(new Area[] { area1, area2 });

		AreaDefinition areaDef = new AreaDefinitionImpl(areaList);

		Parameter param = areaDef.encode();
		assertNotNull(param);
		assertTrue(param.isPrimitive());

		assertTrue(Arrays.equals(data, param.getData()));

	}
}
