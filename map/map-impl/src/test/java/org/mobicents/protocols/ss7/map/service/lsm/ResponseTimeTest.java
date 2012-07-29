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
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * 
 */
public class ResponseTimeTest {
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

	@Test(groups = { "functional.decode","service.lsm"})
	public void testDecode() throws Exception {
		byte[] data = new byte[] { 0x30, 0x03, 0x0a, 0x01, 0x00 };

		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		assertEquals(tag, Tag.SEQUENCE);

		ResponseTimeImpl responseTime = new ResponseTimeImpl();
		responseTime.decodeAll(asn);

		assertEquals( responseTime.getResponseTimeCategory(),ResponseTimeCategory.lowdelay);

	}

	@Test(groups = { "functional.encode","service.lsm"})
	public void testEncode() throws Exception {
		byte[] data = new byte[] { 0x30, 0x03, 0x0a, 0x01, 0x00 };

		ResponseTimeImpl responseTime = new ResponseTimeImpl(ResponseTimeCategory.lowdelay);

		AsnOutputStream asnOS = new AsnOutputStream();
		responseTime.encodeAll(asnOS);

		byte[] encodedData = asnOS.toByteArray();

		assertTrue( Arrays.equals(data,encodedData));
	}
	
	@Test(groups = { "functional.serialize", "service.lsm" })
	public void testSerialization() throws Exception {
		ResponseTimeImpl original = new ResponseTimeImpl(ResponseTimeCategory.lowdelay);

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
		ResponseTimeImpl copy = (ResponseTimeImpl) o;
		
		//test result
		assertEquals(copy, original);
	}
}
