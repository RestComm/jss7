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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.*;
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.LAC;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.LAIFixedLength;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class LocationAreaTest {

	public byte[] getData1() {
		return new byte[] { (byte) 128, 5, 66, (byte) 249, 16, 54, (byte) 186 };
	};

	public byte[] getData2() {
		return new byte[] { (byte) 129, 2, 54, (byte) 186 };
	};

	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {

		byte[] data = this.getData1();

		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		LocationAreaImpl prim = new LocationAreaImpl();
		prim.decodeAll(asn);

		assertEquals(tag, LocationAreaImpl._TAG_laiFixedLength);
		assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

		LAIFixedLength lai = prim.getLAIFixedLength();
		assertEquals(lai.getMCC(), 249);
		assertEquals(lai.getMNC(), 1);
		assertEquals(lai.getLac(), 14010);
		assertNull(prim.getLAC());


		data = this.getData2();

		asn = new AsnInputStream(data);
		tag = asn.readTag();

		prim = new LocationAreaImpl();
		prim.decodeAll(asn);

		assertEquals(tag, LocationAreaImpl._TAG_lac);
		assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

		assertNull(prim.getLAIFixedLength());
		LAC lac = prim.getLAC();
		assertEquals(lac.getLac(), 14010);
	}

	@Test(groups = { "functional.decode", "primitives" })
	public void testEncode() throws Exception {

		LAIFixedLengthImpl lai = new LAIFixedLengthImpl(249, 1, 14010);
		LocationAreaImpl prim = new LocationAreaImpl(lai);

		AsnOutputStream asn = new AsnOutputStream();
		prim.encodeAll(asn);

		assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

		
		LAC lac = new LACImpl(14010);
		prim = new LocationAreaImpl(lac);

		asn = new AsnOutputStream();
		prim.encodeAll(asn);

		assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));
	}
}

