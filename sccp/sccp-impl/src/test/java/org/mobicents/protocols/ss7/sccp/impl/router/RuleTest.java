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

package org.mobicents.protocols.ss7.sccp.impl.router;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLBinding;
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
import org.mobicents.protocols.ss7.sccp.parameter.GT0100;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class RuleTest {
	private final static String RULE = "1;pattern(ROUTING_BASED_ON_GLOBAL_TITLE#tt= #np= #noa=NATIONAL#digits=9023629581#ssn= #dpc=0#dpcProhibited=false);translation(ROUTING_BASED_ON_GLOBAL_TITLE#tt= #np= #noa=INTERNATIONAL#digits=79023629581#ssn= #dpc=345#dpcProhibited=false);\n";

	XMLBinding binding = new XMLBinding();

	public RuleTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		binding.setClassAttribute("type");
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testTranslate1() throws Exception {

		// Match digits 123456789 and replace with PC and SSN
		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "123456789"), 0);
		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 123,
				GlobalTitle.getInstance("-"), 8);

		Rule rule = new Rule(pattern, "R");
		rule.setPrimaryAddressId(1);

		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "123456789"), 0);

		assertTrue(rule.matches(address));

		SccpAddress translatedAddress = rule.translate(address, primaryAddress);

		assertEquals(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, translatedAddress.getAddressIndicator()
				.getRoutingIndicator());
		assertEquals(GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED, translatedAddress.getAddressIndicator()
				.getGlobalTitleIndicator());
		assertEquals(123, translatedAddress.getSignalingPointCode());
		assertEquals(8, translatedAddress.getSubsystemNumber());
		assertNull(translatedAddress.getGlobalTitle());
	}

	@Test
	public void testTranslate2() throws Exception {
		// Match a seven digit number starting "123", followed by any three
		// digits, then "7".

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "123/???/7"), 0);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
				GlobalTitle.getInstance(1, "333/---/4"), 0);

		Rule rule = new Rule(pattern, "R/K/R");
		rule.setPrimaryAddressId(1);

		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "1234567"), 0);

		assertTrue(rule.matches(address));

		SccpAddress translatedAddress = rule.translate(address, primaryAddress);

		assertEquals(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, translatedAddress.getAddressIndicator()
				.getRoutingIndicator());
		assertEquals(GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY, translatedAddress
				.getAddressIndicator().getGlobalTitleIndicator());
		assertEquals(123, translatedAddress.getSignalingPointCode());
		assertEquals(0, translatedAddress.getSubsystemNumber());
		assertEquals("3334564", translatedAddress.getGlobalTitle().getDigits());
	}

	@Test
	public void testTranslate3() throws Exception {
		// Match "441425", followed by any digits Remove the first six digits.
		// Keep any following digits in the Input. Add a PC(123) & SSN (8).

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "441425/*"), 0);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
				GlobalTitle.getInstance("-/-"), 8);

		Rule rule = new Rule(pattern, "R/K");
		rule.setPrimaryAddressId(1);

		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "4414257897897"), 0);

		assertTrue(rule.matches(address));

		SccpAddress translatedAddress = rule.translate(address, primaryAddress);

		assertEquals(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, translatedAddress.getAddressIndicator()
				.getRoutingIndicator());
		assertEquals(GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY, translatedAddress
				.getAddressIndicator().getGlobalTitleIndicator());
		assertEquals(123, translatedAddress.getSignalingPointCode());
		assertEquals(8, translatedAddress.getSubsystemNumber());
		assertEquals("7897897", translatedAddress.getGlobalTitle().getDigits());
	}

	@Test
	public void testTranslate4() throws Exception {
		// Match any digits keep the digits in the and add a PC(123) & SSN (8).

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "*"), 0);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 123,
				GlobalTitle.getInstance("-"), 8);

		Rule rule = new Rule(pattern, "K");
		rule.setPrimaryAddressId(1);

		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "4414257897897"), 0);

		assertTrue(rule.matches(address));

		SccpAddress translatedAddress = rule.translate(address, primaryAddress);

		assertEquals(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, translatedAddress.getAddressIndicator()
				.getRoutingIndicator());
		assertEquals(GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY, translatedAddress
				.getAddressIndicator().getGlobalTitleIndicator());
		assertEquals(123, translatedAddress.getSignalingPointCode());
		assertEquals(8, translatedAddress.getSubsystemNumber());
		assertEquals("4414257897897", translatedAddress.getGlobalTitle().getDigits());
	}

	public void testTranslate5() throws Exception {
		// Match any digits keep the digits in the and add a PC(123) & SSN (8).

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(0, NumberingPlan.valueOf(1), NatureOfAddress.valueOf(4), "*"), 180);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 6045,
				GlobalTitle.getInstance("-"), 180);

		Rule rule = new Rule(pattern, "K");
		rule.setPrimaryAddressId(1);

		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(0, NumberingPlan.valueOf(1), NatureOfAddress.valueOf(4), "4414257897897"), 180);

		assertTrue(rule.matches(address));

		SccpAddress translatedAddress = rule.translate(address, primaryAddress);

		assertEquals(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, translatedAddress.getAddressIndicator()
				.getRoutingIndicator());
		assertEquals(
				GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS,
				translatedAddress.getAddressIndicator().getGlobalTitleIndicator());
		assertEquals(6045, translatedAddress.getSignalingPointCode());
		assertEquals(180, translatedAddress.getSubsystemNumber());
		assertEquals("4414257897897", translatedAddress.getGlobalTitle().getDigits());
		
		GT0100 gt = (GT0100)translatedAddress.getGlobalTitle();
		assertEquals(0, gt.getTranslationType());
		assertEquals(NumberingPlan.ISDN_TELEPHONY, gt.getNumberingPlan());
		assertEquals(NatureOfAddress.INTERNATIONAL, gt.getNatureOfAddress());
	}

	@Test
	public void testSerialization() throws Exception {
		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "441425/*"), 0);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
				GlobalTitle.getInstance("-/-"), 8);

		Rule rule = new Rule(pattern, "R/K");
		rule.setPrimaryAddressId(1);

		// Writes
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
		writer.setIndentation("\t"); // Optional (use tabulation for
		// indentation).
		writer.write(rule, "Rule", Rule.class);
		writer.close();

		System.out.println(output.toString());

		ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		XMLObjectReader reader = XMLObjectReader.newInstance(input);
		Rule aiOut = reader.read("Rule", Rule.class);

		assertNotNull(aiOut);

	}

	/**
	 * Test of toString method, of class Rule.
	 */
	@Test
	public void testToString() {
		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "123/???/7"), 0);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
				GlobalTitle.getInstance(1, "333/---/4"), 0);

		Rule rule = new Rule(pattern, "R/K/R");
		rule.setPrimaryAddressId(1);
		rule.setSecondaryAddressId(2);

		System.out.println(rule.toString());

		// assertEquals(RULE, rule.toString());
	}
}