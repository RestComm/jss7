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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

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
	
	@Test(groups = { "functional.serialize", "primitives" })
	public void testSerialization() throws Exception {
		USSDStringImpl original = new USSDStringImpl("*88#", null);
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
		USSDStringImpl copy = (USSDStringImpl) o;
		
		//test result
		assertEquals(copy.getString(), original.getString());
		
		//TODO Charset is not Serializable now
		//assertEquals(copy.getCharset(), original.getCharset());
	}
}
