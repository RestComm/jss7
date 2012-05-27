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

import java.util.Arrays;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.PlmnId;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.ReSynchronisationInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoRequestImpl;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class SendAuthenticationInfoRequestTest {

	private byte[] getEncodedData() {
		return new byte[] { 48, 21, (byte) 128, 8, 82, 0, 7, 2, 16, (byte) 145, 34, (byte) 240, 2, 1, 4, (byte) 131, 1, 0, (byte) 132, 3, (byte) 185,
				(byte) 254, (byte) 197 };
	}

	private byte[] getEncodedData2() {
		return new byte[] { 48, 20, (byte) 128, 8, 82, 0, 7, 2, 48, 38, 7, (byte) 244, 2, 1, 5, 5, 0, (byte) 129, 0, (byte) 131, 1, 0 };
	}

	private byte[] getRequestingPlmnId() {
		return new byte[] { (byte) 185, (byte) 254, (byte) 197 };
	}

	@Test
	public void testDecode() throws Exception {

		byte[] rawData = getEncodedData();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		SendAuthenticationInfoRequestImpl asc = new SendAuthenticationInfoRequestImpl();
		asc.decodeAll(asn);

		assertEquals( tag,Tag.SEQUENCE);
		assertEquals( asn.getTagClass(),Tag.CLASS_UNIVERSAL);

		IMSI imsi = asc.getImsi();
		assertTrue(imsi.getData().equals("250070200119220"));
		assertEquals(asc.getRequestingNodeType(), RequestingNodeType.vlr);
		assertEquals(asc.getNumberOfRequestedVectors(), 4);

		assertNotNull(asc.getRequestingPlmnId());
		assertTrue(Arrays.equals(asc.getRequestingPlmnId().getData(), getRequestingPlmnId()));
		
		assertNull(asc.getReSynchronisationInfo());
		assertNull(asc.getExtensionContainer());
		assertNull(asc.getNumberOfRequestedAdditionalVectors());

		assertFalse(asc.getSegmentationProhibited());
		assertFalse(asc.getImmediateResponsePreferred());
		assertFalse(asc.getAdditionalVectorsAreForEPS());


		rawData = getEncodedData2();
		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		asc = new SendAuthenticationInfoRequestImpl();
		asc.decodeAll(asn);

		assertEquals( tag,Tag.SEQUENCE);
		assertEquals( asn.getTagClass(),Tag.CLASS_UNIVERSAL);

		imsi = asc.getImsi();
		assertTrue(imsi.getData().equals("250070200362704"));
		assertEquals(asc.getRequestingNodeType(), RequestingNodeType.vlr);
		assertEquals(asc.getNumberOfRequestedVectors(), 5);

		assertNull(asc.getRequestingPlmnId());
		
		assertNull(asc.getReSynchronisationInfo());
		assertNull(asc.getExtensionContainer());
		assertNull(asc.getNumberOfRequestedAdditionalVectors());

		assertTrue(asc.getSegmentationProhibited());
		assertTrue(asc.getImmediateResponsePreferred());
		assertFalse(asc.getAdditionalVectorsAreForEPS());

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

