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

package org.mobicents.protocols.ss7.map.service.sms;

import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AdditionalNumberType;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class LocationInfoWithLMSITest extends TestCase {
	
	private byte[] getEncodedData() {
		return new byte[] { -96, 15, -127, 7, -111, -105, 48, 115, 0, 34, -14, 4, 4, 0, 3, 98, 49 };
	}

	private byte[] getEncodedDataFull() {
		return new byte[] { -96, 65, -127, 6, -88, 33, 67, 101, -121, 9, 4, 4, 4, 3, 2, 1, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5,
				6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -123, 0, -122, 6, -71, -119, 103, 69, 35, -15 };
	}

	@org.junit.Test
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedData();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		LocationInfoWithLMSIImpl liw = new LocationInfoWithLMSIImpl();
		liw.decodeAll(asn);

		assertEquals(0, tag);
		assertEquals(Tag.CLASS_CONTEXT_SPECIFIC, asn.getTagClass());
		
		ISDNAddressString nnm = liw.getNetworkNodeNumber();
		assertEquals(AddressNature.international_number, nnm.getAddressNature());
		assertEquals(NumberingPlan.ISDN, nnm.getNumberingPlan());
		assertEquals("79033700222", nnm.getAddress());
		assertTrue(Arrays.equals(new byte[] { 0, 3, 98, 49 }, liw.getLMSI().getData()));
		
		
		rawData = getEncodedDataFull();
		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		liw = new LocationInfoWithLMSIImpl();
		liw.decodeAll(asn);

		assertEquals(0, tag);
		assertEquals(Tag.CLASS_CONTEXT_SPECIFIC, asn.getTagClass());
		
		nnm = liw.getNetworkNodeNumber();
		assertEquals(AddressNature.national_significant_number, nnm.getAddressNature());
		assertEquals(NumberingPlan.national, nnm.getNumberingPlan());
		assertEquals("1234567890", nnm.getAddress());
		assertTrue(Arrays.equals(new byte[] { 4, 3, 2, 1 }, liw.getLMSI().getData()));
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(liw.getExtensionContainer()));
		assertEquals(AdditionalNumberType.sgsn, liw.getAdditionalNumberType());
		nnm = liw.getAdditionalNumber();
		assertEquals(AddressNature.network_specific_number, nnm.getAddressNature());
		assertEquals(NumberingPlan.private_plan, nnm.getNumberingPlan());
		assertEquals("987654321", nnm.getAddress());
	}

	@org.junit.Test
	public void testEncode() throws Exception {
		
		ISDNAddressString nnm = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79033700222");
		LMSIImpl lmsi = new LMSIImpl(new byte[] { 0, 3, 98, 49 });
		LocationInfoWithLMSIImpl liw = new LocationInfoWithLMSIImpl(nnm, lmsi, null, null, null);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		liw.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();		
		assertTrue(Arrays.equals(rawData, encodedData));
		
		nnm = new ISDNAddressStringImpl(AddressNature.national_significant_number, NumberingPlan.national, "1234567890");
		ISDNAddressStringImpl an = new ISDNAddressStringImpl(AddressNature.network_specific_number, NumberingPlan.private_plan, "987654321");
		lmsi = new LMSIImpl(new byte[] { 4, 3, 2, 1 });
		liw = new LocationInfoWithLMSIImpl(nnm, lmsi, MAPExtensionContainerTest.GetTestExtensionContainer(), AdditionalNumberType.sgsn, an);
		
		asnOS.reset();
		liw.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);
		
		encodedData = asnOS.toByteArray();
		rawData = getEncodedDataFull();		
		assertTrue(Arrays.equals(rawData, encodedData));
		
	}
}
