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

package org.mobicents.protocols.ss7.cap.isup;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class LocationNumberCapTest {

	public byte[] getData() {
		return new byte[] { (byte) 138, 8, (byte) 132, (byte) 151, 8, 2, (byte) 151, 1, 32, 0 };
	}

	public byte[] getIntData() {
		return new byte[] { (byte) 132, (byte) 151, 8, 2, (byte) 151, 1, 32, 0 };
	}

	@Test(groups = { "functional.decode","isup"})
	public void testDecode() throws Exception {

		byte[] data = this.getData();
		AsnInputStream ais = new AsnInputStream(data);
		LocationNumberCapImpl elem = new LocationNumberCapImpl();
		int tag = ais.readTag();
		elem.decodeAll(ais);
		LocationNumber ln = elem.getLocationNumber();
		assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
		assertEquals(ln.getNatureOfAddressIndicator(), 4);
		assertTrue(ln.getAddress().equals("80207910020"));
		assertEquals(ln.getNumberingPlanIndicator(), 1);
		assertEquals(ln.getInternalNetworkNumberIndicator(), 1);
		assertEquals(ln.getAddressRepresentationRestrictedIndicator(), 1);
		assertEquals(ln.getScreeningIndicator(), 3);
	}

	@Test(groups = { "functional.encode","isup"})
	public void testEncode() throws Exception {

		LocationNumberCapImpl elem = new LocationNumberCapImpl(this.getIntData());
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 10);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

		LocationNumber cpn = new LocationNumberImpl(4, "80207910020", 1, 1, 1, 3);
		elem = new LocationNumberCapImpl(cpn);
		aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 10);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));
		
//		int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator, int addressRepresentationREstrictedIndicator,
//		int screeningIndicator
	}
}

