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

import static org.testng.Assert.*;import org.testng.*;import org.testng.annotations.*;

import java.util.Arrays;


import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * TODO : Self generated trace. Get real ones
 * 
 * @author amit bhayani
 *
 */
public class SupportedLCSCapabilitySetsTest {
	
	private byte[] getEncodedData() {
		return new byte[] { 3, 2, 4, 64 };
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
		SupportedLCSCapabilitySetsImpl supportedLCSCapabilityTest = new SupportedLCSCapabilitySetsImpl();
		supportedLCSCapabilityTest.decodeAll(asn);

		assertEquals( tag,Tag.STRING_BIT);
		assertEquals( asn.getTagClass(),Tag.CLASS_UNIVERSAL);

		assertEquals( (boolean)supportedLCSCapabilityTest.getLcsCapabilitySet1(),false);
		assertEquals( (boolean)supportedLCSCapabilityTest.getLcsCapabilitySet2(),true);
		assertEquals( (boolean)supportedLCSCapabilityTest.getLcsCapabilitySet3(),false);
		assertEquals( (boolean)supportedLCSCapabilityTest.getLcsCapabilitySet4(),false);
	}
	
	@Test(groups = { "functional.encode","service.lsm"})
	public void testEncode() throws Exception {
		
		SupportedLCSCapabilitySetsImpl supportedLCSCapabilityTest = new SupportedLCSCapabilitySetsImpl(false, true, false, false);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		supportedLCSCapabilityTest.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();		
		assertTrue( Arrays.equals(rawData,encodedData));
		
	}

}
