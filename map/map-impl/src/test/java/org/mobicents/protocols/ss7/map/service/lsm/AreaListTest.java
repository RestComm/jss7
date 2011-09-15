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

import static org.testng.Assert.*;
import org.testng.*;import org.testng.annotations.*;

import java.util.Arrays;


import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MapParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MapParameterFactory;
import org.mobicents.protocols.ss7.map.api.service.lsm.Area;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaList;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaType;

/**
 * @author amit bhayani
 * 
 */
public class AreaListTest {
	MapParameterFactory MapParameterFactory = new MapParameterFactoryImpl();

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

	@Test(groups = { "functional.decode","service.lsm"})
	public void testDecode() throws Exception {
		// TODO this is self generated trace. We need trace from operator
		byte[] data = new byte[] { 0x30, 0x14, 0x30, 0x08, (byte) 0x80, 0x01, 0x05, (byte) 0x81, 0x03, 0x09, 0x70, 0x71, 0x30, 0x08, (byte) 0x80, 0x01, 0x03,
				(byte) 0x81, 0x03, 0x04, 0x30, 0x31 };

		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		AreaList areaList = new AreaListImpl();
		((AreaListImpl)areaList).decodeAll(asn);

		Area areas[] = areaList.getAreas();

		assertNotNull(areas);
		assertEquals( areas.length,2);

		assertNotNull(areas[0].getAreaIdentification());
		assertTrue(Arrays.equals(new byte[] { 0x09, 0x70, 0x71 }, areas[0].getAreaIdentification()));

	}

	@Test(groups = { "functional.encode","service.lsm"})
	public void testEncode() throws Exception {
		// TODO this is self generated trace. We need trace from operator
		byte[] data = new byte[] { 0x30, 0x14, 0x30, 0x08, (byte) 0x80, 0x01, 0x05, (byte) 0x81, 0x03, 0x09, 0x70, 0x71, 0x30, 0x08, (byte) 0x80, 0x01, 0x03,
				(byte) 0x81, 0x03, 0x04, 0x30, 0x31 };

		Area area1 = new AreaImpl(AreaType.utranCellId, new byte[] { 0x09, 0x70, 0x71 });
		Area area2 = new AreaImpl(AreaType.routingAreaId, new byte[] { 0x04, 0x30, 0x31 });

		AreaList areaList = new AreaListImpl(new Area[] { area1, area2 });
		
		AsnOutputStream asnOS = new AsnOutputStream();
		((AreaListImpl)areaList).encodeAll(asnOS, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
		
		byte[] encodedData = asnOS.toByteArray();

		assertTrue( Arrays.equals(data,encodedData));

	}
}
