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

import static org.junit.Assert.assertEquals;
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

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testDecode() throws Exception {
		byte[] rawData = getEncodedData();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		SupportedGADShapesImpl supportedLCSCapabilityTest = new SupportedGADShapesImpl();
		supportedLCSCapabilityTest.decodeAll(asn);

		assertEquals(Tag.STRING_BIT, tag);
		assertEquals(Tag.CLASS_UNIVERSAL, asn.getTagClass());

		assertEquals(true, (boolean) supportedLCSCapabilityTest.getEllipsoidArc());
		assertEquals(true, (boolean) supportedLCSCapabilityTest.getEllipsoidPoint());
		assertEquals(true, (boolean) supportedLCSCapabilityTest.getEllipsoidPointWithAltitude());
		assertEquals(true, (boolean) supportedLCSCapabilityTest.getEllipsoidPointWithAltitudeAndUncertaintyElipsoid());
		assertEquals(true, (boolean) supportedLCSCapabilityTest.getEllipsoidPointWithUncertaintyCircle());
		assertEquals(true, (boolean) supportedLCSCapabilityTest.getEllipsoidPointWithUncertaintyEllipse());
		assertEquals(true, (boolean) supportedLCSCapabilityTest.getPolygon());
	}

	@org.junit.Test
	public void testEncode() throws Exception {

		SupportedGADShapesImpl supportedLCSCapabilityTest = new SupportedGADShapesImpl(true, true, true, true, true, true, true);

		AsnOutputStream asnOS = new AsnOutputStream();
		supportedLCSCapabilityTest.encodeAll(asnOS);

		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();
		assertTrue(Arrays.equals(rawData, encodedData));

	}
}
