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
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class MtForwardShortMessageRequestIndicationTest extends TestCase {
	
	private byte[] getEncodedData() {
		return new byte[] { 48, 73, -128, 8, 16, 33, 34, 34, 17, -126, 21, -12, -124, 7, -111, -127, 33, 105, 0, -112, -10, 4, 52, 11, 22, 33, 44, 55, 66, 77,
				0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11,
				0 };
	}
	
	private byte[] getEncodedDataFull() {
		return new byte[] { 48, 70, -128, 8, 1, -128, 56, 67, 84, 101, 118, -9, -124, 6, -111, 17, 17, 33, 34, 34, 4, 7, 11, 22, 33, 44, 55, 66, 77, 5, 0, 48,
				39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32,
				33 };
	}
	
	@org.junit.Test
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedData();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		MtForwardShortMessageRequestIndicationImpl ind = new MtForwardShortMessageRequestIndicationImpl();
		ind.decodeAll(asn);

		assertEquals(Tag.SEQUENCE, tag);
		assertEquals(Tag.CLASS_UNIVERSAL, asn.getTagClass());
		
		SM_RP_DA da = ind.getSM_RP_DA();
		SM_RP_OA oa = ind.getSM_RP_OA();
		byte[] ui = ind.getSM_RP_UI();
		assertEquals(11, (long) da.getIMSI().getMCC());
		assertEquals(22, (long) da.getIMSI().getMNC());
		assertEquals("2221128514", da.getIMSI().getMSIN());
		assertEquals(AddressNature.international_number, oa.getServiceCentreAddressOA().getAddressNature());
		assertEquals(NumberingPlan.ISDN, oa.getServiceCentreAddressOA().getNumberingPlan());
		assertEquals("18129600096", oa.getServiceCentreAddressOA().getAddress());
		assertTrue(Arrays.equals(ui, new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
				4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 }));
		assertFalse(ind.getMoreMessagesToSend());
		
		rawData = getEncodedDataFull();
		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		ind = new MtForwardShortMessageRequestIndicationImpl();
		ind.decodeAll(asn);

		assertEquals(Tag.SEQUENCE, tag);
		assertEquals(Tag.CLASS_UNIVERSAL, asn.getTagClass());
		
		da = ind.getSM_RP_DA();
		oa = ind.getSM_RP_OA();
		ui = ind.getSM_RP_UI();
		Boolean moreMesToSend = ind.getMoreMessagesToSend();
		assertEquals(100, (long) da.getIMSI().getMCC());
		assertEquals(88, (long) da.getIMSI().getMNC());
		assertEquals("3344556677", da.getIMSI().getMSIN());
		assertEquals(AddressNature.international_number, oa.getServiceCentreAddressOA().getAddressNature());
		assertEquals(NumberingPlan.ISDN, oa.getServiceCentreAddressOA().getNumberingPlan());
		assertEquals("1111122222", oa.getServiceCentreAddressOA().getAddress());
		assertTrue(Arrays.equals(ui, new byte[] { 11, 22, 33, 44, 55, 66, 77 }));
		assertTrue(moreMesToSend);
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
	}

	@org.junit.Test
	public void testEncode() throws Exception {

		IMSI imsi = new IMSIImpl(11L, 22L, "2221128514");
		SM_RP_DA sm_RP_DA = new SM_RP_DAImpl(imsi);
		AddressString sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "18129600096");
		SM_RP_OAImpl sm_RP_OA = new SM_RP_OAImpl();
		sm_RP_OA.setServiceCentreAddressOA(sca);
		byte[] sm_RP_UI = new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4,
				4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 };
		MtForwardShortMessageRequestIndicationImpl ind = new MtForwardShortMessageRequestIndicationImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, false, null);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		ind.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();		
		assertTrue(Arrays.equals(rawData, encodedData));

		
		imsi = new IMSIImpl(100L, 88L, "3344556677");
		sm_RP_DA = new SM_RP_DAImpl(imsi);
		sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1111122222");
		sm_RP_OA = new SM_RP_OAImpl();
		sm_RP_OA.setServiceCentreAddressOA(sca);
		sm_RP_UI = new byte[] { 11, 22, 33, 44, 55, 66, 77 };
		ind = new MtForwardShortMessageRequestIndicationImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, true, MAPExtensionContainerTest.GetTestExtensionContainer());
		
		asnOS = new AsnOutputStream();
		ind.encodeAll(asnOS);

		encodedData = asnOS.toByteArray();
		rawData = getEncodedDataFull();		
		assertTrue(Arrays.equals(rawData, encodedData));
	}

}
