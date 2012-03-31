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

package org.mobicents.protocols.ss7.sccp.impl.message;

import static org.testng.Assert.assertEquals;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.Mtp3UserPartImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.LongMessageRule;
import org.mobicents.protocols.ss7.sccp.impl.router.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.impl.router.Mtp3Destination;
import org.mobicents.protocols.ss7.sccp.impl.router.Mtp3ServiceAccessPoint;
import org.mobicents.protocols.ss7.sccp.impl.router.Rule;
import org.mobicents.protocols.ss7.sccp.impl.router.RuleType;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class GetMaxUserDataLengthTest {

	private SccpStackImpl stack = new SccpStackImpl("TestStack");

	@BeforeMethod
	public void setUp() {
		stack.start();
		stack.removeAllResourses();
	}

	@AfterMethod
	public void tearDown() {
	}

	@Test(groups = { "SccpMessage", "MessageLength",})
	public void testMessageLength() throws Exception {

		SccpAddress a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 2, null, 8);
		SccpAddress a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(0, "1122334455"), 18);

		Mtp3UserPartImpl_2 mtp3UserPart = new Mtp3UserPartImpl_2();
		stack.setMtp3UserPart(1, mtp3UserPart);
		Mtp3ServiceAccessPoint sap = new Mtp3ServiceAccessPoint(1, 1, 2);
		stack.getRouter().addMtp3ServiceAccessPoint(1, sap);
		Mtp3Destination dest = new Mtp3Destination(2, 2, 0, 255, 255);
		sap.addMtp3Destination(1, dest);
		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 2, GlobalTitle.getInstance(0, "1122334455"), 18);
		stack.getRouter().addPrimaryAddress(1, primaryAddress);
		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 2, GlobalTitle.getInstance(0, "1122334455"), 18);
		Rule rule = new Rule(RuleType.Solitary, pattern, "K");
		rule.setPrimaryAddressId(1);
		stack.getRouter().addRule(1, rule);

		int len = stack.getSccpProvider().getMaxUserDataLength(a1, a2);
		assertEquals(len, 248);

		len = stack.getSccpProvider().getMaxUserDataLength(a2, a1);
		assertEquals(len, 248);

		LongMessageRule lmr = new LongMessageRule(2, 2, LongMessageRuleType.XudtEnabled);
		stack.getRouter().addLongMessageRule(1, lmr);

		len = stack.getSccpProvider().getMaxUserDataLength(a1, a2);
		assertEquals(len, 2560);

		lmr = new LongMessageRule(2, 2, LongMessageRuleType.LudtEnabled);
		stack.getRouter().addLongMessageRule(1, lmr);

		len = stack.getSccpProvider().getMaxUserDataLength(a1, a2);
		assertEquals(len, 231);

		mtp3UserPart.setMtpMsgLen(4000);
		len = stack.getSccpProvider().getMaxUserDataLength(a1, a2);
		assertEquals(len, 2560);

	}

	private class Mtp3UserPartImpl_2 extends Mtp3UserPartImpl {

		private int mtpMsgLen = 268;

		public void setMtpMsgLen(int mtpMsgLen) {
			this.mtpMsgLen = mtpMsgLen;
		}

		@Override
		public int getMaxUserDataLength(int dpc) {
			return mtpMsgLen;
		}
	
	}
}

