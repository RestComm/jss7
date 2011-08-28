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
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MapServiceFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword;

/**
 * @author amit bhayani
 *
 */
public class LCSCodewordTest {
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
		byte[] data = new byte[] { 0x30, 0x13, (byte) 0x80, 0x01, 0x0f, (byte) 0x81, 0x0e, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, 0x6e, 0x72,
				(byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65 };

		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		LCSCodewordImpl lcsCodeword = new LCSCodewordImpl();
		lcsCodeword.decodeAll(asn);

		assertEquals((byte) 0x0f, lcsCodeword.getDataCodingScheme());
		assertNotNull(lcsCodeword.getLCSCodewordString());
		assertEquals("ndmgapp2ndmgapp2", lcsCodeword.getLCSCodewordString().getString());

	}

	@Test
	public void testEncode() throws Exception {
		byte[] data = new byte[] { 0x30, 0x13, (byte) 0x80, 0x01, 0x0f, (byte) 0x81, 0x0e, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, 0x6e, 0x72,
				(byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65 };

		USSDString nameString = mapServiceFactory.createUSSDString("ndmgapp2ndmgapp2");
		LCSCodewordImpl lcsCodeword = new LCSCodewordImpl((byte) 0x0f, nameString);
		AsnOutputStream asnOS = new AsnOutputStream();
		lcsCodeword.encodeAll(asnOS, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
		
		byte[] encodedData = asnOS.toByteArray();

		assertTrue(Arrays.equals(data, encodedData));
	}
}
