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

package org.mobicents.protocols.ss7.sccp.impl.router;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.parameter.GT0100;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

	@BeforeMethod
	public void setUp() {
		binding.setClassAttribute("type");
	}

	@AfterMethod
	public void tearDown() {
	}

	@Test(groups = { "router", "functional.translate" })
	public void testTranslate1() throws Exception {

		// Match digits 123456789 and replace with PC and SSN. It removes the GT
		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "123456789"), 0);
		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 123, GlobalTitle.getInstance("-"), 8);

		RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern, "R");
		rule.setPrimaryAddressId(1);

		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "123456789"), 0);

		assertTrue(rule.matches(address));

		SccpAddress translatedAddress = rule.translate(address, primaryAddress);

		assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN);
		assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(), GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED);
		assertEquals(translatedAddress.getSignalingPointCode(), 123);
		assertEquals(translatedAddress.getSubsystemNumber(), 8);
		assertNull(translatedAddress.getGlobalTitle());
	}

	@Test(groups = { "router", "functional.translate" })
	public void testTranslate2() throws Exception {
		// Match a seven digit number starting "123", followed by any three
		// digits, then "7".

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "123/???/7"), 0);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123, GlobalTitle.getInstance(1, "333/---/4"), 0);

		RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern, "R/K/R");
		rule.setPrimaryAddressId(1);

		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "1234567"), 0);

		assertTrue(rule.matches(address));

		SccpAddress translatedAddress = rule.translate(address, primaryAddress);

		assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
		assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY);
		assertEquals(translatedAddress.getSignalingPointCode(), 123);
		assertEquals(translatedAddress.getSubsystemNumber(), 0);
		assertEquals(translatedAddress.getGlobalTitle().getDigits(), "3334564");
	}

	@Test(groups = { "router", "functional.translate" })
	public void testTranslate3() throws Exception {
		// Match "441425", followed by any digits Remove the first six digits.
		// Keep any following digits in the Input. Add a PC(123) & SSN (8).

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "441425/*"), 0);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123, GlobalTitle.getInstance("-/-"), 8);

		RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern, "R/K");
		rule.setPrimaryAddressId(1);

		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "4414257897897"), 0);

		assertTrue(rule.matches(address));

		SccpAddress translatedAddress = rule.translate(address, primaryAddress);

		assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
		assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY);
		assertEquals(translatedAddress.getSignalingPointCode(), 123);
		assertEquals(translatedAddress.getSubsystemNumber(), 8);
		assertEquals(translatedAddress.getGlobalTitle().getDigits(), "7897897");
	}

	@Test(groups = { "router", "functional.translate" })
	public void testTranslate4() throws Exception {
		// Match any digits keep the digits in the and add a PC(123) & SSN (8).

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "*"), 0);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 123, GlobalTitle.getInstance("-"), 8);

		RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern, "K");
		rule.setPrimaryAddressId(1);

		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "4414257897897"), 0);

		assertTrue(rule.matches(address));

		SccpAddress translatedAddress = rule.translate(address, primaryAddress);

		assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN);
		assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY);
		assertEquals(translatedAddress.getSignalingPointCode(), 123);
		assertEquals(translatedAddress.getSubsystemNumber(), 8);
		assertEquals(translatedAddress.getGlobalTitle().getDigits(), "4414257897897");
	}

	@Test(groups = { "router", "functional.translate" })
	public void testTranslate5() throws Exception {
		// Match any digits keep the digits in the and add a PC(123) & SSN (8).

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(0, NumberingPlan.valueOf(1),
				NatureOfAddress.valueOf(4), "*"), 6);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 6045, GlobalTitle.getInstance("-"), 6);

		RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern, "K");
		rule.setPrimaryAddressId(1);

		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(0, NumberingPlan.valueOf(1),
				NatureOfAddress.valueOf(4), "4414257897897"), 6);

		assertTrue(rule.matches(address));

		SccpAddress translatedAddress = rule.translate(address, primaryAddress);

		assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
		assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
				GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS);
		assertEquals(translatedAddress.getSignalingPointCode(), 6045);
		assertEquals(translatedAddress.getSubsystemNumber(), 6);
		assertEquals(translatedAddress.getGlobalTitle().getDigits(), "4414257897897");

		GT0100 gt = (GT0100) translatedAddress.getGlobalTitle();
		assertEquals(gt.getTranslationType(), 0);
		assertEquals(gt.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
		assertEquals(gt.getNatureOfAddress(), NatureOfAddress.INTERNATIONAL);
	}

	@Test(groups = { "router", "functional.translate" })
	public void testTranslate6() throws Exception {
		// Match any GT Digits, keep the SSN from original address and add PC 123

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "*"), 0);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123, GlobalTitle.getInstance(1, "-"), 0);

		RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern, "K");
		rule.setPrimaryAddressId(1);

		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "1234567"), 8);

		assertTrue(rule.matches(address));

		SccpAddress translatedAddress = rule.translate(address, primaryAddress);

		assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
		assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY);
		assertEquals(translatedAddress.getSignalingPointCode(), 123);
		assertEquals(translatedAddress.getSubsystemNumber(), 8);
		assertEquals(translatedAddress.getGlobalTitle().getDigits(), "1234567");
	}

	@Test(groups = { "router", "functional.translate" })
	public void testTranslate7() throws Exception {
		// The case when address length is less then size

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "555"), 0);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123, GlobalTitle.getInstance(1, "-"), 0);

		RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern, "K");
		rule.setPrimaryAddressId(1);

		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "55"), 8);

		// TODO: the exception is here
		assertFalse(rule.matches(address));
	}

	@Test(groups = { "router", "functional.translate" })
	public void testTranslate8() throws Exception {
		// Some bad pattern

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "*/5555"), 0);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123, GlobalTitle.getInstance(1, "-/-"), 0);

		RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern, "K/K");
		rule.setPrimaryAddressId(1);

		SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "222"), 8);

		assertTrue(rule.matches(address));

		// TODO: the exception is here
		SccpAddress translatedAddress = rule.translate(address, primaryAddress);

		assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
		assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY);
		assertEquals(translatedAddress.getSignalingPointCode(), 123);
		assertEquals(translatedAddress.getSubsystemNumber(), 8);
		assertEquals(translatedAddress.getGlobalTitle().getDigits(), "222");
	}

	@Test(groups = { "router", "functional.encode" })
	public void testSerialization() throws Exception {
		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "441425/*"), 0);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123, GlobalTitle.getInstance("-/-"), 8);

		RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern, "R/K");
		rule.setPrimaryAddressId(1);

		// Writes
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
		writer.setIndentation("\t"); // Optional (use tabulation for
		// indentation).
		writer.write(rule, "Rule", RuleImpl.class);
		writer.close();

		System.out.println(output.toString());

		ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		XMLObjectReader reader = XMLObjectReader.newInstance(input);
		RuleImpl aiOut = reader.read("Rule", RuleImpl.class);

		assertNotNull(aiOut);

	}

	/**
	 * Test of toString method, of class Rule.
	 */
	@Test(groups = { "router", "functional.encode" })
	public void testToString() {
		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "123/???/7"), 0);

		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123, GlobalTitle.getInstance(1, "333/---/4"), 0);

		RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern, "R/K/R");
		rule.setPrimaryAddressId(1);
		rule.setSecondaryAddressId(2);

		System.out.println(rule.toString());

		// assertEquals( rule.toString(),RULE);
	}
}