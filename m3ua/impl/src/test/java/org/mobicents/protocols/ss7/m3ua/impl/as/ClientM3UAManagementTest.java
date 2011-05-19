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

package org.mobicents.protocols.ss7.m3ua.impl.as;

import static org.junit.Assert.assertEquals;

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
public class ClientM3UAManagementTest {

	private ClientM3UAManagement clientM3UAMgmt = new ClientM3UAManagement();

	/**
	 * 
	 */
	public ClientM3UAManagementTest() {
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
		clientM3UAMgmt.start();
	}

	@After
	public void tearDown() throws IOException {
		clientM3UAMgmt.getAppServers().clear();
		clientM3UAMgmt.getAspfactories().clear();
		clientM3UAMgmt.getDpcVsAsName().clear();
		clientM3UAMgmt.stop();
	}

	@Test
	public void testSerialization() throws Exception {
		As as = clientM3UAMgmt.createAppServer("m3ua as create rc 100 AS1".split(" "));
		AspFactory aspFactory = clientM3UAMgmt
				.createAspFactory("m3ua asp create ip 127.0.0.1 port 1111 remip 127.0.0.1 remport 1112 ASP1".split(" "));
		clientM3UAMgmt.assignAspToAs("AS1", "ASP1");

		clientM3UAMgmt.addRouteAsForDpc(123, "AS1");

		clientM3UAMgmt.stop();

		ClientM3UAManagement clientM3UAMgmt1 = new ClientM3UAManagement();
		clientM3UAMgmt1.start();

		assertEquals(1, clientM3UAMgmt1.getAppServers().size());
		assertEquals(1, clientM3UAMgmt1.getAspfactories().size());
		assertEquals(1, clientM3UAMgmt1.getDpcVsAsName().size());

	}
}
