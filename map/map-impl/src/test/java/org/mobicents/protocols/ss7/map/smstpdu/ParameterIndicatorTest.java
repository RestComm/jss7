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

package org.mobicents.protocols.ss7.map.smstpdu;

import static org.testng.Assert.*;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class ParameterIndicatorTest {
	@Test(groups = { "functional.encode","smstpdu"})
	public void testEncode() throws Exception {

		//ParameterIndicatorImpl ind = new ParameterIndicatorImpl(boolean TP_UDLPresence, boolean getTP_DCSPresence, boolean getTP_PIDPresence); 
		ParameterIndicatorImpl ind = new ParameterIndicatorImpl(true, false, false);
		assertTrue(ind.getTP_UDLPresence());
		assertFalse(ind.getTP_DCSPresence());
		assertFalse(ind.getTP_PIDPresence());

		ind = new ParameterIndicatorImpl(false, true, false);
		assertFalse(ind.getTP_UDLPresence());
		assertTrue(ind.getTP_DCSPresence());
		assertFalse(ind.getTP_PIDPresence());

		ind = new ParameterIndicatorImpl(false, false, true);
		assertFalse(ind.getTP_UDLPresence());
		assertFalse(ind.getTP_DCSPresence());
		assertTrue(ind.getTP_PIDPresence());
	}
}
