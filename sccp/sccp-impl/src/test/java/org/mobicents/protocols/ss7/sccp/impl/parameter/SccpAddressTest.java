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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import org.testng.annotations.*;
import static org.testng.Assert.*;

import java.util.Arrays;


import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author kulikov
 */
public class SccpAddressTest {

	private SccpAddressCodec codec = new SccpAddressCodec();
	private byte[] data = new byte[] { 0x12, (byte) 0x92, 0x00, 0x11, 0x04, (byte) 0x97, 0x20, (byte) 0x73, 0x00,
			(byte) 0x92, 0x09 };

	public SccpAddressTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@BeforeMethod
	public void setUp() {
	}

	@AfterMethod
	public void tearDown() {
	}

	/**
	 * Test of decode method, of class SccpAddressCodec.
	 */
	@Test(groups = { "parameter","functional.decode"})
	public void testDecode1() throws Exception {
		SccpAddress address = codec.decode(data);

		assertEquals( address.getSignalingPointCode(),0);
		assertEquals( address.getSubsystemNumber(),146);
		assertEquals( address.getGlobalTitle().getDigits(),"79023700299");
	}

	@Test(groups = { "parameter","functional.decode"})
	public void testDecode2() throws Exception {
		SccpAddress address = codec.decode(new byte[] { 0x42, 0x08 });

		assertEquals( address.getSignalingPointCode(),0);
		assertEquals( address.getSubsystemNumber(),8);
		assertNull(address.getGlobalTitle());
	}

	/**
	 * Test of encode method, of class SccpAddressCodec.
	 */
	@Test(groups = { "parameter","functional.encode"})
	public void testEncode() throws Exception {
		GlobalTitle gt = GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL,
				"79023700299");
		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt, 146);
		byte[] bin = codec.encode(address);
		assertTrue(Arrays.equals(data, bin),"Wrong encoding");
	}

	@Test(groups = { "parameter","functional.encode"})
	public void testEncode2() throws Exception {
		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 0, null, 8);
		byte[] bin = codec.encode(address);
		assertTrue(Arrays.equals(new byte[] { 0x42, 0x08 }, bin),"Wrong encoding");
	}

}
