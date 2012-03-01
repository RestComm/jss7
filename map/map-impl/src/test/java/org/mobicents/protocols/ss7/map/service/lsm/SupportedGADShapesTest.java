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
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * 
 */
public class SupportedGADShapesTest {

	private byte[] getEncodedData() {
		return new byte[] { (byte)0x03, 0x02, 0x01, (byte) 0xfe };
	}

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
		byte[] rawData = getEncodedData();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		SupportedGADShapesImpl supportedLCSCapabilityTest = new SupportedGADShapesImpl();
		supportedLCSCapabilityTest.decodeAll(asn);

		assertEquals( tag,Tag.STRING_BIT);
		assertEquals( asn.getTagClass(),Tag.CLASS_UNIVERSAL);

		assertEquals( (boolean) supportedLCSCapabilityTest.getEllipsoidArc(),true);
		assertEquals( (boolean) supportedLCSCapabilityTest.getEllipsoidPoint(),true);
		assertEquals( (boolean) supportedLCSCapabilityTest.getEllipsoidPointWithAltitude(),true);
		assertEquals( (boolean) supportedLCSCapabilityTest.getEllipsoidPointWithAltitudeAndUncertaintyElipsoid(),true);
		assertEquals( (boolean) supportedLCSCapabilityTest.getEllipsoidPointWithUncertaintyCircle(),true);
		assertEquals( (boolean) supportedLCSCapabilityTest.getEllipsoidPointWithUncertaintyEllipse(),true);
		assertEquals( (boolean) supportedLCSCapabilityTest.getPolygon(),true);
	}

	@Test(groups = { "functional.encode","service.lsm"})
	public void testEncode() throws Exception {

		SupportedGADShapesImpl supportedLCSCapabilityTest = new SupportedGADShapesImpl(true, true, true, true, true, true, true);

		AsnOutputStream asnOS = new AsnOutputStream();
		supportedLCSCapabilityTest.encodeAll(asnOS);

		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();
		assertTrue( Arrays.equals(rawData,encodedData));

	}
	
	@Test(groups = { "functional.serialize", "service.lsm" })
	public void testSerialization() throws Exception {
		SupportedGADShapesImpl original = new SupportedGADShapesImpl(true, true, true, true, true, true, true);

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
		SupportedGADShapesImpl copy = (SupportedGADShapesImpl) o;
		
		//test result
		assertEquals(copy, original);
	}
}
