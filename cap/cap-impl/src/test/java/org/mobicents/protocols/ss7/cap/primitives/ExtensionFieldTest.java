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

package org.mobicents.protocols.ss7.cap.primitives;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.primitives.CriticalityType;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class ExtensionFieldTest {

	public byte[] getData1() {
		return new byte[] { 48, 5, 2, 1, 2, (byte) 129, 0 };
	}

	public byte[] getData2() {
		return new byte[] { 48, 7, 6, 2, 40, 22, (byte) 129, 1, (byte) 255 };
	}

	public byte[] getData3() {
		return new byte[] { 48, 11, 2, 2, 8, (byte) 174, 10, 1, 1, (byte) 129, 2, (byte) 253, (byte) 213 };
	}

	public long[] getDataOid() {
		return new long[] { 1, 0, 22 };
	}

	@Test(groups = { "functional.decode","primitives"})
	public void testDecode() throws Exception {

		byte[] data = this.getData1();
		AsnInputStream ais = new AsnInputStream(data);
		ExtensionFieldImpl elem = new ExtensionFieldImpl();
		int tag = ais.readTag();
		elem.decodeAll(ais);
		assertEquals((int) elem.getLocalCode(), 2);
		assertEquals(elem.getCriticalityType(), CriticalityType.typeIgnore);
		ais = new AsnInputStream(elem.getData());
		ais.readNullData(elem.getData().length);

		data = this.getData2();
		ais = new AsnInputStream(data);
		elem = new ExtensionFieldImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		assertTrue(Arrays.equals(elem.getGlobalCode(), this.getDataOid()));
		assertEquals(elem.getCriticalityType(), CriticalityType.typeIgnore);
		ais = new AsnInputStream(elem.getData());
		boolean bool = ais.readBooleanData(elem.getData().length);
		assertTrue(bool);

		data = this.getData3();
		ais = new AsnInputStream(data);
		elem = new ExtensionFieldImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		assertEquals((int) elem.getLocalCode(), 2222);
		assertEquals(elem.getCriticalityType(), CriticalityType.typeAbort);
		ais = new AsnInputStream(elem.getData());
		int i1 = (int)ais.readIntegerData(elem.getData().length);
		assertEquals(i1, -555);
	}

	@Test(groups = { "functional.encode","primitives"})
	public void testEncode() throws Exception {

		AsnOutputStream aos = new AsnOutputStream();
		aos.writeNullData();
		ExtensionFieldImpl elem = new ExtensionFieldImpl(2, CriticalityType.typeIgnore, aos.toByteArray());
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

		aos = new AsnOutputStream();
		aos.writeBooleanData(true);
		elem = new ExtensionFieldImpl(this.getDataOid(), null, aos.toByteArray());
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

		aos = new AsnOutputStream();
		aos.writeIntegerData(-555);
		elem = new ExtensionFieldImpl(2222, CriticalityType.typeAbort, aos.toByteArray());
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));
	}
}

