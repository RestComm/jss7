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

package org.mobicents.protocols.ss7.sccp.parameter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;

/**
 * 
 * @author kulikov
 */
public class SccpAddressTest {

	public SccpAddressTest() {
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

	/**
	 * Test of getAddressIndicator method, of class SccpAddress.
	 */
	@Test
	public void testEquals() {
		GlobalTitle gt = GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "123");
		SccpAddress a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt, 0);
		SccpAddress a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt, 0);
		assertEquals(a1, a2);
		assertEquals(a1.hashCode(), a2.hashCode());
	}

	@Test
	public void testEquals1() {
		GlobalTitle gt = GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL,
				"79023700271");
		SccpAddress a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 146, gt, 0);

		HashMap<SccpAddress, Integer> map = new HashMap();
		map.put(a1, 1);

		SccpAddress a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 146, gt, 0);
		Integer i = map.get(a2);

		if (i == null) {
			fail("Address did not match");
		}

		assertEquals(1, i);
	}

	@Test
	public void testSerialization() throws Exception {
		GlobalTitle gt = GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL,
				"79023700271");
		SccpAddress a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 146, gt, 0);

		// Writes
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
		writer.setIndentation("\t"); // Optional (use tabulation for
		// indentation).
		writer.write(a1, "SccpAddress", SccpAddress.class);
		writer.close();

		System.out.println(output.toString());

		ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		XMLObjectReader reader = XMLObjectReader.newInstance(input);
		SccpAddress aiOut = reader.read("SccpAddress", SccpAddress.class);

		assertEquals(
				GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS,
				aiOut.getAddressIndicator().getGlobalTitleIndicator());
		assertEquals(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, aiOut.getAddressIndicator().getRoutingIndicator());
		assertTrue(aiOut.getAddressIndicator().pcPresent());
		assertFalse(aiOut.getAddressIndicator().ssnPresent());

		assertEquals(146, aiOut.getSignalingPointCode());
		assertEquals(0, aiOut.getSubsystemNumber());

		assertEquals("79023700271", aiOut.getGlobalTitle().getDigits());
	}

	@Test
	public void testSerialization1() throws Exception {

		SccpAddress a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 146, null, 8);

		// Writes
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
		writer.setIndentation("\t"); // Optional (use tabulation for
		// indentation).
		writer.write(a1, "SccpAddress", SccpAddress.class);
		writer.close();

		System.out.println(output.toString());

		ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		XMLObjectReader reader = XMLObjectReader.newInstance(input);
		SccpAddress aiOut = reader.read("SccpAddress", SccpAddress.class);

		assertEquals(GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED, aiOut.getAddressIndicator()
				.getGlobalTitleIndicator());
		assertEquals(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, aiOut.getAddressIndicator().getRoutingIndicator());
		assertTrue(aiOut.getAddressIndicator().pcPresent());
		assertTrue(aiOut.getAddressIndicator().ssnPresent());

		assertEquals(146, aiOut.getSignalingPointCode());
		assertEquals(8, aiOut.getSubsystemNumber());

		assertNull(aiOut.getGlobalTitle());
	}

}