/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.service.mobility.authentication;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class AuthenticationSetListTest {

	private byte[] getEncodedData() {
		return new byte[] { (byte) 160, 36, 48, 34, 4, 16, 15, -2, 18, -92, -49, 43, -35, -71, -78, -98, 109, 83, -76, -87, 77, -128, 4, 4, -32, 82, -17, -14,
				4, 8, 31, 72, -93, 97, 78, -17, -52, 0 };
	}

	@Test
	public void testDecode() throws Exception {

		byte[] rawData = getEncodedData();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		AuthenticationSetListImpl asc = new AuthenticationSetListImpl();
		asc.decodeAll(asn);

		assertEquals(tag, AuthenticationSetListImpl._TAG_tripletList);
		assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

//		AuthenticationSetList asl = asc.getAuthenticationSetList();
//		assertEquals(asl.getTripletList().getAuthenticationTriplets().size(), 1);
//		assertNull(asl.getQuintupletList());
//		
//		assertNull(asc.getEpsAuthenticationSetList());
//		assertNull(asc.getExtensionContainer());

	}

	@Test(groups = { "functional.encode"})
	public void testEncode() throws Exception {

//		ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "29113123311");
//		AddressString sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "49883700292");
//		AlertServiceCentreRequestImpl asc = new AlertServiceCentreRequestImpl(msisdn, sca);
//		
//		AsnOutputStream asnOS = new AsnOutputStream();
//		asc.encodeAll(asnOS);
//		
//		byte[] encodedData = asnOS.toByteArray();
//		byte[] rawData = getEncodedData();		
//		assertTrue( Arrays.equals(rawData,encodedData));
	}

}

