/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.service.supplementary;

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
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.primitives.USSDStringImpl;

/**
 * @author abhayani
 * 
 */
public class ProcessUnstructuredSSResponseIndicationTest {
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
		byte[] data = new byte[] { 0x30, 0x15, 0x04, 0x01, 0x0f, 0x04, 0x10, (byte) 0xd9, 0x77, 0x5d, 0x0e, 0x12, (byte) 0x87, (byte) 0xd9, 0x61, (byte) 0xf7,
				(byte) 0xb8, 0x0c, (byte) 0xea, (byte) 0x81, 0x66, 0x35, 0x18 };

		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		ProcessUnstructuredSSResponseIndicationImpl addNum = new ProcessUnstructuredSSResponseIndicationImpl();
		addNum.decodeAll(asn);
		byte dataCodingScheme = addNum.getUSSDDataCodingScheme();
		assertEquals((byte) 0x0f, dataCodingScheme);

		USSDString ussdString = addNum.getUSSDString();
		assertNotNull(ussdString);

		assertEquals("Your balance = 350", ussdString.getString());

	}

	@Test
	public void testEncode() throws Exception {
		byte[] data = new byte[] { 0x30, 0x15, 0x04, 0x01, 0x0f, 0x04, 0x10, (byte) 0xd9, 0x77, 0x5d, 0x0e, 0x12, (byte) 0x87, (byte) 0xd9, 0x61, (byte) 0xf7,
				(byte) 0xb8, 0x0c, (byte) 0xea, (byte) 0x81, 0x66, 0x35, 0x18 };

		USSDString ussdStr = new USSDStringImpl("Your balance = 350", null);
		ProcessUnstructuredSSResponseIndicationImpl addNum = new ProcessUnstructuredSSResponseIndicationImpl((byte) 0x0f, ussdStr);

		AsnOutputStream asnOS = new AsnOutputStream();
		addNum.encodeAll(asnOS);

		byte[] encodedData = asnOS.toByteArray();

		assertTrue(Arrays.equals(data, encodedData));
	}
}
