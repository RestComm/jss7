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

package org.mobicents.protocols.ss7.sccp.impl.messageflow;

import static org.testng.Assert.*;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImplProxy;
import org.mobicents.protocols.ss7.sccp.impl.User;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class CallingPartyAddressTest extends SccpHarness {

	private SccpAddress a1, a2;

	public CallingPartyAddressTest() {
	}

	@BeforeClass
	public void setUpClass() throws Exception {
		this.sccpStack1Name = "MessageTransferTestSccpStack1";
		this.sccpStack2Name = "MessageTransferTestSccpStack2";
	}

	@AfterClass
	public void tearDownClass() throws Exception {
	}

	protected void createStack1() {
		sccpStack1 = new SccpStackImplProxy("sspTestSccpStack1");
		sccpProvider1 = sccpStack1.getSccpProvider();
	}

	protected void createStack2() {
		sccpStack2 = new SccpStackImplProxy("sspTestSccpStack2");
		sccpProvider2 = sccpStack2.getSccpProvider();
	}

	@BeforeMethod
	public void setUp() throws Exception {
		super.setUp();
	}

	@AfterMethod
	public void tearDown() {
		super.tearDown();
	}

	public byte[] getDataSrc() {
		return new byte[] { 11, 12, 13, 14, 15 };
	}

	@Test(groups = { "SccpMessage", "functional.transfer" })
	public void testTransfer() throws Exception {

		a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), null, 8);
		a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), null, 8);

		User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
		User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

		u1.register();
		u2.register();

		Thread.sleep(100);

		// no newCallingPartyAddress
		SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), GlobalTitle.getInstance(1, "111111"), 8);
		sccpStack1.getRouter().addRoutingAddress(1, primaryAddress);
		SccpAddress newCallingPartyAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "222222"), 8);
		sccpStack1.getRouter().addRoutingAddress(2, newCallingPartyAddress);
		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "111111"), 0);
		sccpStack1.getRouter().addRule(1, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "K", 1, -1, null);

		SccpAddress a3 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1, "111111"), 0);
		SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(a3, a1, getDataSrc(), 0, 8, true, null, null);
		sccpProvider1.send(message);
		Thread.sleep(100);

		assertEquals(u1.getMessages().size(), 0);
		assertEquals(u2.getMessages().size(), 1);
		SccpDataMessage dMsg = (SccpDataMessage) u2.getMessages().get(0);
		assertNull(dMsg.getCallingPartyAddress().getGlobalTitle());

		// present newCallingPartyAddress
		sccpStack1.getRouter().removeRule(1);
		sccpStack1.getRouter().addRule(1, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "K", 1, -1, 2);

		message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(a3, a1, getDataSrc(), 0, 8, true, null, null);
		sccpProvider1.send(message);
		Thread.sleep(100);

		assertEquals(u1.getMessages().size(), 0);
		assertEquals(u2.getMessages().size(), 2);
		dMsg = (SccpDataMessage) u2.getMessages().get(1);
		assertTrue(dMsg.getCallingPartyAddress().getGlobalTitle().getDigits().equals("222222"));
	}
}

