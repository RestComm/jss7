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

package org.mobicents.protocols.ss7.mtp;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author abhayani
 * 
 */
public class MtpPausePrimitiveTest {

	/**
	 * 
	 */
	public MtpPausePrimitiveTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of schedule method, of class MTPScheduler.
	 */
	@Test
	public void testEncodeDecode() throws InterruptedException {
		Mtp3PausePrimitive pausePrimi = new Mtp3PausePrimitive(123);
		byte[] data = pausePrimi.getValue();

		Mtp3PausePrimitive pausePrimi1 = new Mtp3PausePrimitive(data);
		assertEquals(Mtp3Primitive.PAUSE, pausePrimi1.getType());
		assertEquals(123, pausePrimi1.getAffectedDpc());

	}

}
