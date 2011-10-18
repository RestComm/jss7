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

package org.mobicents.protocols.ss7.cap;

import static org.testng.Assert.assertEquals;

import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class CAPApplicationContextTest {

	@Test(groups = { "api","CAPApplicationContext"})
	public void testOidValues() throws Exception {
		CAPApplicationContext[] vall = CAPApplicationContext.values();
		for (CAPApplicationContext val : vall) {
			String x1 = val.toString();

			long[] oid = val.getOID();
			CAPApplicationContext val2 = CAPApplicationContext.getInstance(oid);
			assertEquals(val, val2);
		}
		
		int i1=0;
		i1++;
	}
}
