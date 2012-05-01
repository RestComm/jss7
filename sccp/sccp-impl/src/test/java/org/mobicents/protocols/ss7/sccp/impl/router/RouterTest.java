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

package org.mobicents.protocols.ss7.sccp.impl.router;

import java.io.IOException;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class RouterTest {

	private Rule rule1, rule2;
	private SccpAddress primaryAddr1, primaryAddr2;
	private LongMessageRule longMessageRule1, longMessageRule2;
	private Mtp3ServiceAccessPoint sap1, sap2;
	private Mtp3Destination dest1, dest2;

	private Router router = null;

	public RouterTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@BeforeMethod
	public void setUp() throws IOException {

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "123456789"), 0);

		rule1 = new Rule(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern, "R");
		rule1.setPrimaryAddressId(211);
		rule1.setSecondaryAddressId(212);

		rule2 = new Rule(RuleType.Loadshared, LoadSharingAlgorithm.Bit4, pattern, "K");

		primaryAddr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123, GlobalTitle.getInstance(1, "333/---/4"), 0);
		primaryAddr2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 321, GlobalTitle.getInstance(1, "333/---/4"), 0);

		longMessageRule1 = new LongMessageRule(1, 2, LongMessageRuleType.XudtEnabled);
		longMessageRule2 = new LongMessageRule(3, 4, LongMessageRuleType.LudtEnabled);

		sap1 = new Mtp3ServiceAccessPoint(1, 11, 2);
		sap2 = new Mtp3ServiceAccessPoint(2, 12, 2);

		dest1 = new Mtp3Destination(101, 110, 0, 255, 255);
		dest2 = new Mtp3Destination(111, 120, 0, 255, 255);

		// cleans config file
		router = new Router("RouterTest");
		router.start();
		router.removeAllResourses();

	}

	@AfterMethod
	public void tearDown() {
		router.removeAllResourses();

		router.stop();
	}

	/**
	 * Test of add method, of class RouterImpl.
	 */
	@Test(groups = { "router", "functional" })
	public void testRouter() throws Exception {
		router.addRule(1, rule1);
		assertEquals(router.getRules().size(), 1);
		router.addRule(2, rule2);
		assertEquals(router.getRules().size(), 2);
		router.removeRule(2);
		Rule rule = router.getRules().values().iterator().next();
		assertNotNull(rule);
		assertEquals(rule.getRuleType(), RuleType.Solitary);
		assertEquals(router.getRules().size(), 1);

		router.addPrimaryAddress(1, primaryAddr1);
		assertEquals(router.getPrimaryAddresses().size(), 1);
		router.addPrimaryAddress(2, primaryAddr2);
		assertEquals(router.getPrimaryAddresses().size(), 2);
		router.removePrimaryAddress(1);
		SccpAddress pa = router.getPrimaryAddresses().values().iterator().next();
		assertNotNull(pa);
		assertEquals(pa.getSignalingPointCode(), 321);
		assertEquals(router.getPrimaryAddresses().size(), 1);

		assertEquals(router.getBackupAddresses().size(), 0);
		router.addBackupAddress(1, primaryAddr1);
		assertEquals(router.getBackupAddresses().size(), 1);
		router.addBackupAddress(2, primaryAddr2);
		assertEquals(router.getBackupAddresses().size(), 2);
		router.removeBackupAddress(1);
		pa = router.getBackupAddresses().values().iterator().next();
		assertNotNull(pa);
		assertEquals(pa.getSignalingPointCode(), 321);
		assertEquals(router.getBackupAddresses().size(), 1);

		router.addLongMessageRule(1, longMessageRule1);
		assertEquals(router.getLongMessageRules().size(), 1);
		router.addLongMessageRule(2, longMessageRule2);
		assertEquals(router.getLongMessageRules().size(), 2);
		router.removeLongMessageRule(2);
		LongMessageRule lmr = router.getLongMessageRules().values().iterator().next();
		assertNotNull(lmr);
		assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.XudtEnabled);
		assertEquals(router.getLongMessageRules().size(), 1);

		router.addMtp3ServiceAccessPoint(1, sap1);
		assertEquals(router.getMtp3ServiceAccessPoints().size(), 1);
		router.addMtp3ServiceAccessPoint(2, sap2);
		assertEquals(router.getMtp3ServiceAccessPoints().size(), 2);
		router.removeMtp3ServiceAccessPoint(2);
		Mtp3ServiceAccessPoint sap = router.getMtp3ServiceAccessPoints().values().iterator().next();
		assertNotNull(sap);
		assertEquals(sap.getOpc(), 11);
		assertEquals(router.getLongMessageRules().size(), 1);

		router.addMtp3Destination(1, 1, dest1);
		assertEquals(sap.getMtp3Destinations().size(), 1);
		router.addMtp3Destination(1, 2, dest2);
		assertEquals(sap.getMtp3Destinations().size(), 2);
		router.removeMtp3Destination(1, 2);
		Mtp3Destination dest = sap.getMtp3Destinations().values().iterator().next();
		assertNotNull(dest);
		assertEquals(dest.getFirstDpc(), 101);
		assertEquals(sap.getMtp3Destinations().size(), 1);
	}

	@Test(groups = { "router", "functional.encode" })
	public void testSerialization() throws Exception {
		router.addRule(1, rule2);
		router.addPrimaryAddress(1, primaryAddr1);
		router.addPrimaryAddress(2, primaryAddr2);
		router.addBackupAddress(1, primaryAddr1);
		router.addLongMessageRule(1, longMessageRule1);
		router.addMtp3ServiceAccessPoint(3, sap1);
		router.addMtp3Destination(3, 1, dest1);
		router.stop();

		Router router1 = new Router(router.getName());
		router1.start();

		Rule rl = router1.getRule(1);
		SccpAddress adp = router1.getPrimaryAddress(2);
		SccpAddress adb = router1.getBackupAddress(1);
		LongMessageRule lmr = router1.getLongMessageRule(1);
		Mtp3ServiceAccessPoint sap = router1.getMtp3ServiceAccessPoint(3);
		Mtp3Destination dst = sap.getMtp3Destination(1);

		assertEquals(rl.getPrimaryAddressId(), rule2.getPrimaryAddressId());
		assertEquals(rl.getLoadSharingAlgorithm(), rule2.getLoadSharingAlgorithm());
		assertEquals(adp.getSignalingPointCode(), primaryAddr2.getSignalingPointCode());
		assertEquals(adb.getSignalingPointCode(), primaryAddr1.getSignalingPointCode());
		assertEquals(lmr.getFirstSpc(), longMessageRule1.getFirstSpc());
		assertEquals(sap.getMtp3Destinations().size(), 1);
		assertEquals(dst.getLastDpc(), 110);

		router1.stop();
	}

	/**
	 * Test of Ordering.
	 */
	@Test(groups = { "router", "functional.order" })
	public void testOrdering() throws Exception {
		SccpAddress pattern1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "800/????/9"), 0);
		Rule rule1 = new Rule(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern1, "R/K/R");

		SccpAddress pattern2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "*"), 0);
		Rule rule2 = new Rule(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern2, "K");

		SccpAddress pattern3 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "9/?/9/*"), 0);
		Rule rule3 = new Rule(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern3, "K/K/K/K");

		SccpAddress pattern4 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "80/??/0/???/9"), 0);
		Rule rule4 = new Rule(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern4, "R/K/R/K/R");

		SccpAddress pattern5 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "800/?????/9"), 0);
		Rule rule5 = new Rule(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern5, "R/K/R");

		SccpAddress pattern6 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "123456"), 0);
		Rule rule6 = new Rule(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern6, "K");

		SccpAddress pattern7 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "1234567890"), 0);
		Rule rule7 = new Rule(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern7, "R/K/R");

		SccpAddress pattern8 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "999/*"), 0);
		Rule rule8 = new Rule(RuleType.Solitary, LoadSharingAlgorithm.Undefined, pattern8, "R/K");

		router.addRule(1, rule1);
		router.addRule(2, rule2);
		router.addRule(3, rule3);
		router.addRule(4, rule4);
		router.addRule(5, rule5);
		router.addRule(6, rule6);
		router.addRule(7, rule7);
		router.addRule(8, rule8);

		SccpAddress calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "123456"), 0);
		Rule rule = router.findRule(calledParty);
		assertEquals(rule6, rule);

		calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "1234567890"), 0);
		rule = router.findRule(calledParty);
		assertEquals(rule7, rule);
		
		calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "80012039"), 0);
		rule = router.findRule(calledParty);
		assertEquals(rule1, rule);
		
		calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "80012039"), 0);
		rule = router.findRule(calledParty);
		assertEquals(rule1, rule);
		
		calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "800120349"), 0);
		rule = router.findRule(calledParty);
		assertEquals(rule5, rule);
		
		calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "801203459"), 0);
		rule = router.findRule(calledParty);
		assertEquals(rule4, rule);		
		
		calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "999123456"), 0);
		rule = router.findRule(calledParty);
		assertEquals(rule8, rule);
		
		calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "919123456"), 0);
		rule = router.findRule(calledParty);
		assertEquals(rule3, rule);		
		
	}
}
