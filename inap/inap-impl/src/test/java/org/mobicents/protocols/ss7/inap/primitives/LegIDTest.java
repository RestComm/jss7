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

package org.mobicents.protocols.ss7.inap.primitives;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.testng.*;import org.testng.annotations.*;

/**
* 
* @author sergey vetyutnev
* 
*/
public class LegIDTest {
	
	private byte[] getData1() {
		return new byte[] { (byte) 128, 1, 2 };
	}
	
	private byte[] getData2() {
		return new byte[] { (byte) 129, 1, 1 };
	}

	@Test(groups = { "functional.decode","primitives"})
	public void testDecode() throws Exception {

		byte[] data = this.getData1();
		AsnInputStream ais = new AsnInputStream(data);
		LegIDImpl legId = new LegIDImpl();
		int tag = ais.readTag();
		legId.decodeAll(ais);
		assertNotNull(legId.getSendingSideID());
		assertNull(legId.getReceivingSideID());
		byte[] val = legId.getSendingSideID();
		assertEquals(val.length, 1);
		assertEquals(val[0], 2);

		data = this.getData2();
		ais = new AsnInputStream(data);
		legId = new LegIDImpl();
		tag = ais.readTag();
		legId.decodeAll(ais);
		assertNull(legId.getSendingSideID());
		assertNotNull(legId.getReceivingSideID());
		val = legId.getReceivingSideID();
		assertEquals(val.length, 1);
		assertEquals(val[0], 1);
		
	}

	@Test(groups = { "functional.encode","primitives"})
	public void testEncode() throws Exception {

		LegIDImpl legId = new LegIDImpl(true, new byte[] { 2 });
		AsnOutputStream aos = new AsnOutputStream();
		legId.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

		legId = new LegIDImpl(false, new byte[] { 1 });
		aos.reset();
		legId.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
		
	}
}
