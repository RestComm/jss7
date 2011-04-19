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

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author amit bhayani
 * 
 */
public class RuleExecutorTest {

	RuleExecutor ruleExecutor = null;

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {

		RouterImpl router = new RouterImpl();
		try {
			router.start();
			router.deleteRule("Rule1");
			router.deleteRule("Rule2");
			router.deleteRule("Rule3");
			router.deleteRule("Rule4");
			router.stop();
		} catch (Exception e) {
			// ignore
		}
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testCreateRule() throws Exception {
		ruleExecutor = new RuleExecutor();
		RouterImpl router = new RouterImpl();
		router.start();
		ruleExecutor.setRouter(router);

		String message = ruleExecutor
				.execute("sccprule create Rule1 pattern tt -1 np -1 noa 3 digits 99604 ssn -1 translation tt -1 np 3 noa 4 digits 77865 ssn -1"
						.split(" "));
		assertEquals(1, router.list().size());

		message = ruleExecutor
				.execute("sccprule create Rule2 pattern tt -1 np 3 noa 4 digits 99604 ssn -1 mtpinfo name name1 opc 12 apc 56 sls 1"
						.split(" "));
		assertEquals(2, router.list().size());

		message = ruleExecutor
				.execute("sccprule create Rule3 pattern tt -1 np 3 noa 4 digits 99604 ssn -1 translation tt -1 np 3 noa 4 digits 77865 ssn -1 mtpinfo name name1 opc 12 apc 56 sls 1"
						.split(" "));
		assertEquals(3, router.list().size());

		message = ruleExecutor.execute("sccprule create Rule4 dpc 2 ssn 8 mtpinfo name name1 opc 12 apc 56 sls 1"
				.split(" "));
		assertEquals(4, router.list().size());

		router.stop();
	}

}
