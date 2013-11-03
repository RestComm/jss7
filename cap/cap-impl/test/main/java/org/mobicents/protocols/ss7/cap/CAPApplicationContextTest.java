/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
