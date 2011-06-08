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

package org.mobicents.protocols.ss7.sccp.impl.oam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpResource;
import org.mobicents.protocols.ss7.sccp.impl.router.Router;
import org.mobicents.protocols.ss7.sccp.impl.router.Rule;
import org.mobicents.protocols.ss7.sccp.parameter.GT0100;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author amit bhayani
 * 
 */
public class SccpExecutorTest {

	private Router router = null;
	private SccpResource sccpResource = null;

	private SccpExecutor sccpExecutor = null;

	/**
	 * 
	 */
	public SccpExecutorTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws IllegalStateException {
		this.router = new Router();
		this.router.start();
		this.router.getRules().clear();
		this.router.getPrimaryAddresses().clear();
		this.router.getBackupAddresses().clear();

		this.sccpResource = new SccpResource();
		this.sccpResource.start();
		this.sccpResource.getRemoteSpcs().clear();
		this.sccpResource.getRemoteSsns().clear();

		sccpExecutor = new SccpExecutor();
		sccpExecutor.setRouter(this.router);
		sccpExecutor.setSccpResource(this.sccpResource);
	}

	@After
	public void tearDown() {
		this.router.stop();
		this.sccpResource.stop();
	}

	@Test
	public void testManageRule() {
		String prim_addressCmd = "sccp primary_add create 1 71 2 8 0 0 3 123456789";
		String result = this.sccpExecutor.execute(prim_addressCmd.split(" "));
		assertEquals(SccpOAMMessage.ADDRESS_SUCCESSFULLY_ADDED, result);
		assertEquals(1, this.router.getPrimaryAddresses().size());

		String createRuleCmd = "sccp rule create 1 R 71 2 8 0 0 3 123456789 1";
		result = this.sccpExecutor.execute(createRuleCmd.split(" "));
		assertEquals(SccpOAMMessage.RULE_SUCCESSFULLY_ADDED, result);
		assertEquals(1, this.router.getRules().size());
		assertEquals(1, this.router.getRules().get(1).getPrimaryAddressId());

		createRuleCmd = "sccp rule create 2 K 18 0 180 0 1 4 * 1";
		result = this.sccpExecutor.execute(createRuleCmd.split(" "));
		assertEquals(SccpOAMMessage.RULE_SUCCESSFULLY_ADDED, result);
		assertEquals(2, this.router.getRules().size());
		Rule rule = this.router.getRules().get(2);
		assertNotNull(rule);
		SccpAddress pattern = rule.getPattern();
		assertNotNull(pattern);
		assertEquals(18, (int) pattern.getAddressIndicator().getValue());
		assertEquals(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, pattern.getAddressIndicator()
				.getRoutingIndicator());
		assertEquals(
				GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS,
				pattern.getGlobalTitle().getIndicator());
		GT0100 gt = (GT0100) pattern.getGlobalTitle();
		assertEquals(0, gt.getTranslationType());
		assertEquals(NumberingPlan.ISDN_TELEPHONY, gt.getNumberingPlan());
		assertEquals(NatureOfAddress.INTERNATIONAL, gt.getNatureOfAddress());

		String sec_addressCmd = "sccp backup_add create 1 71 3 8 0 0 3 123456789";
		this.sccpExecutor.execute(sec_addressCmd.split(" "));
		assertEquals(SccpOAMMessage.RULE_SUCCESSFULLY_ADDED, result);
		assertEquals(1, this.router.getBackupAddresses().size());

		String createRuleCmd2 = "sccp rule create 2 R 71 2 8 0 0 3 123456789 1 1";
		this.sccpExecutor.execute(createRuleCmd2.split(" "));
		assertEquals(SccpOAMMessage.RULE_SUCCESSFULLY_ADDED, result);
		assertEquals(2, this.router.getRules().size());

		String createRuleCmd3 = "sccp rule create 3 K 18 0 180 0 1 4 * 1";
		this.sccpExecutor.execute(createRuleCmd3.split(" "));
		assertEquals(SccpOAMMessage.RULE_SUCCESSFULLY_ADDED, result);
		assertEquals(3, this.router.getRules().size());

	}

	@Test
	public void testManageResource() {
		String rspCmd = "sccp rsp create 1 1 0 0";
		this.sccpExecutor.execute(rspCmd.split(" "));
		assertEquals(1, this.sccpResource.getRemoteSpcs().size());

		String rssCmd = "sccp rss create 1 1 8 0";
		this.sccpExecutor.execute(rssCmd.split(" "));
		assertEquals(1, this.sccpResource.getRemoteSsns().size());

		rssCmd = "sccp rss show 1";
		this.sccpExecutor.execute(rssCmd.split(" "));

	}

}
