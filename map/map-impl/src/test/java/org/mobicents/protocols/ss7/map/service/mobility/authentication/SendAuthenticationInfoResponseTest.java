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

import static org.testng.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationSetList;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.AuthenticationTriplet;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.AuthenticationSetListImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.AuthenticationTripletImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoResponseImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.TripletListImpl;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class SendAuthenticationInfoResponseTest {

	private byte[] getEncodedData_V3_tripl() {
		return new byte[] { (byte) 163, 38, -96, 36, 48, 34, 4, 16, 15, -2, 18, -92, -49, 43, -35, -71, -78, -98, 109, 83, -76, -87, 77, -128, 4, 4, -32, 82,
				-17, -14, 4, 8, 31, 72, -93, 97, 78, -17, -52, 0 };
	}

	private byte[] getEncodedData_V2_tripl() {
		return new byte[] { 48, 36, 48, 34, 4, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 4, 4, 4, 4, 4, 4, 4, 8, 8, 8, 8, 8, 8, 8, 8,
				8 };
	}
	
	

	@Test
	public void testDecode() throws Exception {

		byte[] rawData = getEncodedData_V3_tripl();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		SendAuthenticationInfoResponseImpl asc = new SendAuthenticationInfoResponseImpl(3);
		asc.decodeAll(asn);

		assertEquals(tag, SendAuthenticationInfoResponseImpl._TAG_General);
		assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

		AuthenticationSetList asl = asc.getAuthenticationSetList();
		assertEquals(asl.getMapProtocolVersion(), 3);
		assertEquals(asl.getTripletList().getAuthenticationTriplets().size(), 1);
		assertNull(asl.getQuintupletList());
		
		assertNull(asc.getEpsAuthenticationSetList());
		assertNull(asc.getExtensionContainer());


		rawData = getEncodedData_V2_tripl();
		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		asc = new SendAuthenticationInfoResponseImpl(2);
		asc.decodeAll(asn);

		assertEquals(tag, Tag.SEQUENCE);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

		asl = asc.getAuthenticationSetList();
		assertEquals(asl.getMapProtocolVersion(), 2);
		assertEquals(asl.getTripletList().getAuthenticationTriplets().size(), 1);
		assertTrue(Arrays.equals(asl.getTripletList().getAuthenticationTriplets().get(0).getRand(), TripletListTest.getRandData()));
		assertNull(asl.getQuintupletList());

		assertNull(asc.getEpsAuthenticationSetList());
		assertNull(asc.getExtensionContainer());
	}

	@Test(groups = { "functional.encode"})
	public void testEncode() throws Exception {

		ArrayList<AuthenticationTriplet> ats = new ArrayList<AuthenticationTriplet>();
		AuthenticationTripletImpl at = new AuthenticationTripletImpl(AuthenticationTripletTest.getRandData(), AuthenticationTripletTest.getSresData(),
				AuthenticationTripletTest.getKcData());
		ats.add(at);
		TripletListImpl tl = new TripletListImpl(ats);
		AuthenticationSetListImpl asl = new AuthenticationSetListImpl(tl, 3);
		SendAuthenticationInfoResponseImpl asc = new SendAuthenticationInfoResponseImpl(3, asl, null, null);
		// long mapProtocolVersion, AuthenticationSetList authenticationSetList, MAPExtensionContainer extensionContainer,
		// EpsAuthenticationSetList epsAuthenticationSetList

		AsnOutputStream asnOS = new AsnOutputStream();
		asc.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData_V3_tripl();		
		assertTrue( Arrays.equals(rawData,encodedData));


		ats = new ArrayList<AuthenticationTriplet>();
		at = new AuthenticationTripletImpl(TripletListTest.getRandData(), TripletListTest.getSresData(),
				TripletListTest.getKcData());
		ats.add(at);
		tl = new TripletListImpl(ats);
		asl = new AuthenticationSetListImpl(tl, 2);
		asc = new SendAuthenticationInfoResponseImpl(2, asl, null, null);

		asnOS = new AsnOutputStream();
		asc.encodeAll(asnOS);
		
		encodedData = asnOS.toByteArray();
		rawData = getEncodedData_V2_tripl();		
		assertTrue( Arrays.equals(rawData,encodedData));
	}
}

