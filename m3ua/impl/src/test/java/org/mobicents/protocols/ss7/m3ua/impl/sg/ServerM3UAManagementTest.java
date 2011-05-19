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

package org.mobicents.protocols.ss7.m3ua.impl.sg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;

/**
 * Test the serialization/de-serialization
 * 
 * @author amit bhayani
 * 
 */
public class ServerM3UAManagementTest {

	private ServerM3UAManagement serverM3uaMgmt = new ServerM3UAManagement();

	/**
	 * 
	 */
	public ServerM3UAManagementTest() {
		// TODO Auto-generated constructor stub
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		serverM3uaMgmt.start();
	}

	@After
	public void tearDown() throws IOException {
		serverM3uaMgmt.getAppServers().clear();
		serverM3uaMgmt.getAspfactories().clear();
		serverM3uaMgmt.stop();
	}

	@Test
	public void testSerialization() throws Exception {
		As as = serverM3uaMgmt
				.createAppServer("m3ua ras create rc 100 rk dpc 123 opc 456 si 3 traffic-mode loadshare RAS1"
						.split(" "));
		AspFactory aspFactory = serverM3uaMgmt.createAspFactory("m3ua rasp create ip 127.0.0.1 port 2345 RASP1"
				.split(" "));
		serverM3uaMgmt.assignAspToAs("RAS1", "RASP1");

		serverM3uaMgmt.stop();
		
		ServerM3UAManagement serverM3uaMgmt1 = new ServerM3UAManagement(); 
		serverM3uaMgmt1.start();

		assertEquals(1, serverM3uaMgmt1.getAppServers().size());
		assertEquals(1, serverM3uaMgmt1.getAspfactories().size());
		assertNotNull(serverM3uaMgmt1.m3uaRouter.getAs(123, 456, (short) 3));

	}

}
