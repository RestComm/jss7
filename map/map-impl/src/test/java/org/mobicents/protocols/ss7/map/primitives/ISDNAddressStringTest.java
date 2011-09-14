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

import java.util.Arrays;

import static org.testng.Assert.*;import org.testng.*;import org.testng.annotations.*;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class ISDNAddressStringTest  {
	
	private byte[] getEncodedData() {
		return new byte[] { -126, 7, -111, -105, 114, 99, 80, 24, -7 };
	}

	@Test(groups = { "functional.decode","primitives"})
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedData();
		
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		ISDNAddressStringImpl addStr = new ISDNAddressStringImpl();
		addStr.decodeAll(asn);

		assertEquals( tag,2);
		assertEquals( asn.getTagClass(),Tag.CLASS_CONTEXT_SPECIFIC);
		assertFalse(addStr.isExtension());
		assertEquals( addStr.getAddressNature(),AddressNature.international_number);
		assertEquals( addStr.getNumberingPlan(),NumberingPlan.ISDN);
		assertEquals( addStr.getAddress(),"79273605819");
	}
	
	@Test(groups = { "functional.encode","primitives"})
	public void testEncode() throws Exception {
		
		ISDNAddressStringImpl addStr = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79273605819");
		AsnOutputStream asnOS = new AsnOutputStream();
		
		addStr.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 2);
		
		byte[] encodedData = asnOS.toByteArray();
		
		byte[] rawData = getEncodedData();		
		
		assertTrue( Arrays.equals(rawData,encodedData));
		
	}

}
