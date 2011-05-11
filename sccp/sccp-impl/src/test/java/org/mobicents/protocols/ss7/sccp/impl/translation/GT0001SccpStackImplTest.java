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

package org.mobicents.protocols.ss7.sccp.impl.translation;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.User;
import org.mobicents.protocols.ss7.sccp.impl.router.Rule;
import org.mobicents.protocols.ss7.sccp.message.UnitData;
import org.mobicents.protocols.ss7.sccp.parameter.GT0001;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author amit bhayani
 * @author kulikov
 * @author baranowb
 */
public class GT0001SccpStackImplTest extends SccpHarness {

	private SccpAddress a1, a2;

	public GT0001SccpStackImplTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws IllegalStateException {
		super.setUp();

	}

	@After
	public void tearDown() {
		super.tearDown();
	}

	
	protected static final String GT1_digits = "1234567890";
	protected static final String GT2_digits = "0987654321";
	
	protected static final String GT1_pattern_digits = "1/???????/90";
	protected static final String GT2_pattern_digits = "0/???????/21";
	
	@Test
	public void testRemoteRoutingBasedOnGT001_DPC_SSN() throws Exception {
		
		GT0001 gt1 = new GT0001(NatureOfAddress.NATIONAL,GT1_digits);
		GT0001 gt2 = new GT0001(NatureOfAddress.NATIONAL,GT2_digits);
		
		a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt1, getSSN());
		a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt2, getSSN());
		
		SccpAddress rule1SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, new GT0001(NatureOfAddress.NATIONAL,GT2_pattern_digits), getSSN());
		SccpAddress rule2SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, new GT0001(NatureOfAddress.NATIONAL,GT1_pattern_digits), getSSN());
		Rule rule1 = new Rule(rule1SccpAddress, "K/R/K");
		rule1.setPrimaryAddressId(22);
		Rule rule2 = new Rule(rule2SccpAddress, "R/R/R");
		rule2.setPrimaryAddressId(33);
		super.router1.getRules().put(1, rule1);
		super.router2.getRules().put(1, rule2);
		
		//add addresses to translate
		SccpAddress primary1SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), GlobalTitle.getInstance("-/-/-"), getSSN());
		SccpAddress primary2SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), GlobalTitle.getInstance("-/-/-"), getSSN());
		
		
		super.router1.getPrimaryAddresses().put(22, primary1SccpAddress);
		super.router2.getPrimaryAddresses().put(33, primary2SccpAddress);
		
		
		//now create users, we need to override matchX methods, since our rules do kinky stuff with digits, plus 
		User u1 = new User(sccpStack1.getSccpProvider(), a1, a2,getSSN()){

	
			protected boolean matchCalledPartyAddress() {
				UnitData udt = (UnitData) msg;
				SccpAddress addressToMatch = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), null, getSSN());
				if (!addressToMatch.equals(udt.getCalledPartyAddress())) {
					return false;
				}
				return true;
			}
			
		};
		User u2 = new User(sccpStack2.getSccpProvider(), a2, a1,getSSN()){

	
			protected boolean matchCalledPartyAddress() {
				UnitData udt = (UnitData) msg;
				SccpAddress addressToMatch = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), new GT0001(NatureOfAddress.NATIONAL,"021"), getSSN());
				if (!addressToMatch.equals(udt.getCalledPartyAddress())) {
					return false;
				}
				return true;
			}
			
		};

		u1.send();
		u2.send();

		Thread.currentThread().sleep(1000);

		assertTrue("Message not received", u1.check());
		assertTrue("Message not received", u2.check());
	}
	
	
	@Test
	public void testRemoteRoutingBasedOnGT001() throws Exception {
		
		//here we do as above, however receiving stack needs also rule, to match it localy.
		GT0001 gt1 = new GT0001(NatureOfAddress.NATIONAL,GT1_digits);
		GT0001 gt2 = new GT0001(NatureOfAddress.NATIONAL,GT2_digits);
		
		a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt1, getSSN());
		a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt2, getSSN());
		
		SccpAddress rule1SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, new GT0001(NatureOfAddress.NATIONAL,GT2_pattern_digits), getSSN());
		SccpAddress rule2SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, new GT0001(NatureOfAddress.NATIONAL,GT1_pattern_digits), getSSN());
		Rule rule1 = new Rule(rule1SccpAddress, "K/R/K");
		rule1.setPrimaryAddressId(22);
		Rule rule2 = new Rule(rule2SccpAddress, "R/K/R");
		rule2.setPrimaryAddressId(33);
		super.router1.getRules().put(1, rule1);
		super.router2.getRules().put(1, rule2);
		
		//add addresses to translate
		SccpAddress primary1SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, getStack2PC(), GlobalTitle.getInstance("-/-/-"), getSSN());
		SccpAddress primary2SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, getStack1PC(), GlobalTitle.getInstance("-/-/-"), getSSN());
		super.router1.getPrimaryAddresses().put(22, primary1SccpAddress);
		super.router2.getPrimaryAddresses().put(33, primary2SccpAddress);
		
		
		//add rules for incoming messages, 
		
		//1. add primary addresses
		//NOTE PC passed in address match local PC for stack
		primary1SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), GlobalTitle.getInstance("-/-/-"), getSSN());
		primary2SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), GlobalTitle.getInstance("-/-/-"), getSSN());
		super.router1.getPrimaryAddresses().put(44, primary1SccpAddress);
		super.router2.getPrimaryAddresses().put(66, primary2SccpAddress);
		//2. add rules to make translation to above
		rule1SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, new GT0001(NatureOfAddress.NATIONAL,"23456/?/8"), getSSN());
		rule2SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, new GT0001(NatureOfAddress.NATIONAL,"02/?"), getSSN());
		rule1 = new Rule(rule1SccpAddress, "K/K/K");
		rule1.setPrimaryAddressId(44);
		rule2 = new Rule(rule2SccpAddress, "K/K");
		rule2.setPrimaryAddressId(66);
		super.router1.getRules().put(2, rule1);
		super.router2.getRules().put(2, rule2);
		
		
		//now create users, we need to override matchX methods, since our rules do kinky stuff with digits, plus 
		User u1 = new User(sccpStack1.getSccpProvider(), a1, a2,getSSN()){

	
			protected boolean matchCalledPartyAddress() {
				UnitData udt = (UnitData) msg;
				//pc=1,ssn=8,gt=GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY 2345678
				SccpAddress addressToMatch = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), new GT0001(NatureOfAddress.NATIONAL,"2345678"), getSSN());
				if (!addressToMatch.equals(udt.getCalledPartyAddress())) {
					return false;
				}
				return true;
			}
			
		};
		
		
		User u2 = new User(sccpStack2.getSccpProvider(), a2, a1,getSSN()){

	
			protected boolean matchCalledPartyAddress() {
				UnitData udt = (UnitData) msg;
				SccpAddress addressToMatch = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), new GT0001(NatureOfAddress.NATIONAL,"021"), getSSN());
				if (!addressToMatch.equals(udt.getCalledPartyAddress())) {
					return false;
				}
				return true;
			}
			
		};

		u1.send();
		u2.send();

		Thread.currentThread().sleep(1000);

		assertTrue("Message not received", u1.check());
		assertTrue("Message not received", u2.check());
	}
	
	

}