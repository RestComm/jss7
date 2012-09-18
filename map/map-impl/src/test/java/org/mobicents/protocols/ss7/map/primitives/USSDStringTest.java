/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingGroup;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.smstpdu.CharacterSet;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
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
		byte[] data = new byte[] { 0x04, 0x04, 0x2a, 0x1c, 0x6e, (byte)0x04 };
		
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		USSDStringImpl ussdStr = new USSDStringImpl(new CBSDataCodingSchemeImpl(0x0f));
		ussdStr.decodeAll(asn);
		
		assertEquals( ussdStr.getString(null),"*88#");

	}
	
	//TODO Fix the GSMEncoder. This is failing 
	@Test(groups = { "functional.encode","primitives"})
	public void testEncode() throws Exception {
		byte[] data = new byte[] { 0x04, 0x04, 0x2a, 0x1c, 0x6e, (byte)0x04 };
		
		CBSDataCodingScheme dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7, CharacterSet.GSM7, null, null, false);
		USSDStringImpl ussdStr = new USSDStringImpl("*88#", dcs, null);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		ussdStr.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();

		assertTrue( Arrays.equals(data,encodedData));
	}
	
	@Test(groups = { "functional.serialize", "primitives" })
	public void testSerialization() throws Exception {
		CBSDataCodingScheme dcs = new CBSDataCodingSchemeImpl(CBSDataCodingGroup.GeneralGsm7, CharacterSet.GSM7, null, null, false);
		USSDStringImpl original = new USSDStringImpl("*88#", dcs, null);
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
		assertTrue(copy.getString(null).equals(original.getString(null)));
		
		//TODO Charset is not Serializable now
		//assertEquals(copy.getCharset(), original.getCharset());
	}
}
