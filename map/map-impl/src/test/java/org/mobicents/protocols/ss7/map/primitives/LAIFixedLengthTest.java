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

package org.mobicents.protocols.ss7.map.primitives;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.testng.*;import org.testng.annotations.*;


/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class LAIFixedLengthTest {

	public byte[] getData() {
		return new byte[] { 4, 5, 82, (byte) 240, 16, 17, 92 };
	};

	public byte[] getDataVal() {
		return new byte[] { 82, (byte) 240, 16, 17, 92 };
	};

	public byte[] getData2() {
		return new byte[] { 4, 5, 16, 97, 66, 1, 77 };
	};
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {

		byte[] data = this.getData();

		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		LAIFixedLengthImpl prim = new LAIFixedLengthImpl();
		prim.decodeAll(asn);

		assertNotNull(prim.getData());
		assertTrue(Arrays.equals(getDataVal(), prim.getData()));		
		
		assertEquals(prim.getMCC(), 250);
		assertEquals(prim.getMNC(), 1);
		assertEquals(prim.getLac(), 4444);

		
		data = this.getData2();

		asn = new AsnInputStream(data);
		tag = asn.readTag();

		prim = new LAIFixedLengthImpl();
		prim.decodeAll(asn);

		assertNotNull(prim.getData());
		
		assertEquals(prim.getMCC(), 11);
		assertEquals(prim.getMNC(), 246);
		assertEquals(prim.getLac(), 333);
	}
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testEncode() throws Exception {

		LAIFixedLengthImpl prim = new LAIFixedLengthImpl(250, 1, 4444);

		AsnOutputStream asn = new AsnOutputStream();
		prim.encodeAll(asn);

		assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

		
		prim = new LAIFixedLengthImpl(getDataVal());

		asn = new AsnOutputStream();
		prim.encodeAll(asn);

		assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

		
		prim = new LAIFixedLengthImpl(11, 246, 333);

		asn = new AsnOutputStream();
		prim.encodeAll(asn);

		assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));
	}
}
