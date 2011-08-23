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
		SupportedLCSCapabilitySetsImpl supportedLCSCapabilityTest = new SupportedLCSCapabilitySetsImpl();
		supportedLCSCapabilityTest.decodeAll(asn);

		assertEquals(Tag.STRING_BIT, tag);
		assertEquals(Tag.CLASS_UNIVERSAL, asn.getTagClass());

		assertEquals(false, (boolean)supportedLCSCapabilityTest.getLcsCapabilitySet1());
		assertEquals(true, (boolean)supportedLCSCapabilityTest.getLcsCapabilitySet2());
		assertEquals(false, (boolean)supportedLCSCapabilityTest.getLcsCapabilitySet3());
		assertEquals(false, (boolean)supportedLCSCapabilityTest.getLcsCapabilitySet4());
	}
	
	@org.junit.Test
	public void testEncode() throws Exception {
		
		SupportedLCSCapabilitySetsImpl supportedLCSCapabilityTest = new SupportedLCSCapabilitySetsImpl(false, true, false, false);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		supportedLCSCapabilityTest.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();		
		assertTrue(Arrays.equals(rawData, encodedData));
		
	}

}
