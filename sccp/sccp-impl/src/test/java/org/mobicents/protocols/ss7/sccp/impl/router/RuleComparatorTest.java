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
package org.mobicents.protocols.ss7.sccp.impl.router;

import java.util.Arrays;
import static org.testng.Assert.*;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * 
 */
public class RuleComparatorTest {

	RuleComparator ruleComparator = new RuleComparator();

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

	@Test(groups = { "comparator", "functional.sort" })
	public void testSorting() throws Exception {

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

		// This is unsorted
		Rule[] rules = new Rule[] { rule1, rule2, rule3, rule4, rule5, rule6, rule7, rule8 };

		// Sort
		Arrays.sort(rules, ruleComparator);

		assertEquals(rule6, rules[0]);
		assertEquals(rule7, rules[1]);
		assertEquals(rule1, rules[2]);
		assertEquals(rule5, rules[3]);
		assertEquals(rule4, rules[4]);
		assertEquals(rule8, rules[5]);
		assertEquals(rule3, rules[6]);
		assertEquals(rule2, rules[7]);
	}
}
