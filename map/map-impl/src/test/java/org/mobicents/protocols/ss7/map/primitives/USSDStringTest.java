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
package org.mobicents.protocols.ss7.map.primitives;

import static org.testng.Assert.*;import org.testng.*;import org.testng.annotations.*;

import java.util.Arrays;


import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.primitives.USSDStringImpl;

/**
 * @author amit bhayani
 *
 */
public class USSDStringTest {
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

	@Test(groups = { "functional.decode","primitives"})
	public void testDecode() throws Exception {
		byte[] data = new byte[] { 0x04, 0x04, 0x2a, 0x1c, 0x6e, (byte)0xd4 };
		
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		USSDStringImpl ussdStr = new USSDStringImpl();
		ussdStr.decodeAll(asn);
		
		assertEquals( ussdStr.getString(),"*88#");

	}
	
	//TODO Fix the GSMEncoder. This is failing 
	@Test(groups = { "functional.encode","primitives"})
	public void testEncode() throws Exception {
//		byte[] data = new byte[] { 0x04, 0x04, 0x2a, 0x1c, 0x6e, (byte)0xd4 };
		byte[] data = new byte[] { 0x04, 0x04, 0x2a, 0x1c, 0x6e, (byte)0x4 };
		
		USSDStringImpl ussdStr = new USSDStringImpl("*88#", null);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		ussdStr.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();

		assertTrue( Arrays.equals(data,encodedData));
	}
}
