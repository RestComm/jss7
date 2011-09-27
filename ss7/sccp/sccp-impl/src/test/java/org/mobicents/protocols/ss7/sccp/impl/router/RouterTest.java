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

		SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
				GlobalTitle.getInstance(1, "123456789"), 0);

		rule1 = new Rule(pattern, "R");

		rule2 = new Rule(pattern, "K");

		primaryAddr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123, GlobalTitle.getInstance(1,
				"333/---/4"), 0);
		primaryAddr2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 321, GlobalTitle.getInstance(1,
				"333/---/4"), 0);

		// cleans config file
		Router router = new Router();
		try {
			router.start();
			router.getRules().clear();
			router.getPrimaryAddresses().clear();
			router.getBackupAddresses().clear();
			router.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@AfterMethod
	public void tearDown() {
	}

	/**
	 * Test of add method, of class RouterImpl.
	 */
	@Test(groups = { "router","functional"})
	public void testRouter() throws Exception {
		Router router = new Router();
		router.start();
		router.getRules().put(1, rule1);
		assertEquals( router.getRules().size(),1);
		router.getRules().put(2, rule2);
		assertEquals( router.getRules().size(),2);

		router.getRules().remove(2);
		Rule rule = router.getRules().values().iterator().next();
		assertNotNull(rule);
		assertEquals( router.getRules().size(),1);

		router.getPrimaryAddresses().put(1, primaryAddr1);
		router.getPrimaryAddresses().put(2, primaryAddr2);

		assertEquals( router.getPrimaryAddresses().size(),2);
		router.getPrimaryAddresses().remove(1);
		assertEquals( router.getPrimaryAddresses().size(),1);

		assertEquals( router.getBackupAddresses().size(),0);
		router.stop();
	}
	
	@Test(groups = { "router","functional.encode"})
	public void testSerialization() throws Exception {
		Router router = new Router();
		router.start();
		router.getRules().put(1, rule1);	
		router.getPrimaryAddresses().put(1, primaryAddr1);
		router.getPrimaryAddresses().put(2, primaryAddr2);
		router.stop();
		
		router = null;//just
		
		Router router1 = new Router();
		router1.start();
		assertEquals( router1.getRules().size(),1);
		Rule rule = router1.getRules().values().iterator().next();
		assertNotNull(rule);
		
		assertEquals( router1.getPrimaryAddresses().size(),2);
		router1.stop();
	}

}
