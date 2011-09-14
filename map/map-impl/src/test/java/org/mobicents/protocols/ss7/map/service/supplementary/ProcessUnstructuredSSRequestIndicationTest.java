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

import static org.testng.Assert.*;
import org.testng.*;
import org.testng.annotations.*;

import java.util.Arrays;


import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.primitives.USSDStringImpl;

/**
 * Real trace.
 * 
 * TODO get trace with optional parameters and test
 * 
 * @author amit bhayani
 * 
 */
public class ProcessUnstructuredSSRequestIndicationTest {
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

	@Test(groups = { "functional.decode","service.ussd"})
	public void testDecode() throws Exception {
		byte[] data = new byte[] { 0x30, 0x0a, 0x04, 0x01, 0x0f, 0x04, 0x05, 0x2a, (byte) 0xd9, (byte) 0x8c, 0x36, 0x02 };

		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		ProcessUnstructuredSSRequestIndicationImpl addNum = new ProcessUnstructuredSSRequestIndicationImpl();
		addNum.decodeAll(asn);
		byte dataCodingScheme = addNum.getUSSDDataCodingScheme();
		assertEquals( dataCodingScheme,(byte) 0x0f);

		USSDString ussdString = addNum.getUSSDString();
		assertNotNull(ussdString);

		assertEquals( ussdString.getString(),"*234#");

	}

	@Test(groups = { "functional.encode","service.ussd"})
	public void testEncode() throws Exception {
		byte[] data = new byte[] { 0x30, 0x0a, 0x04, 0x01, 0x0f, 0x04, 0x05, 0x2a, (byte) 0xd9, (byte) 0x8c, 0x36, 0x02 };

		USSDString ussdStr = new USSDStringImpl("*234#", null);
		ProcessUnstructuredSSRequestIndicationImpl addNum = new ProcessUnstructuredSSRequestIndicationImpl((byte) 0x0f, ussdStr, null, null);

		AsnOutputStream asnOS = new AsnOutputStream();
		addNum.encodeAll(asnOS);

		byte[] encodedData = asnOS.toByteArray();

		assertTrue( Arrays.equals(data,encodedData));
	}
}
