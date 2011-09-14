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

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;

import org.testng.*;import org.testng.annotations.*;
import static org.testng.Assert.*;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class ForwardShortMessageRequestIndicationTest {
	
	private byte[] getEncodedDataSimple() {
		return new byte[] { 48, 38, -124, 7, -111, 34, 51, 67, -103, 32, 50, -126, 8, -111, 50, 17, 50, 33, 67, 51, -12, 4, 17, 11, 22, 33, 44, 55, 66, 77, 0,
				1, 2, 3, 4, 5, 6, 7, 9, 8 };
	}

	private byte[] getEncodedDataComplex() {
		return new byte[] { 48, 63, -124, 8, -111, 50, 17, 50, 33, 67, 51, -12, -126, 7, -111, 34, 51, 67, -103, 32, 50, 4, 40, 11, 22, 33, 44, 55, 66, 77, 0,
				1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 5, 0 };
	}
	
	@Test(groups = { "functional.decode","service.sms"})
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedDataSimple();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		ForwardShortMessageRequestIndicationImpl ind = new ForwardShortMessageRequestIndicationImpl();
		ind.decodeAll(asn);

		assertEquals(Tag.SEQUENCE, tag);
		assertEquals(Tag.CLASS_UNIVERSAL, asn.getTagClass());
		
		SM_RP_DA da = ind.getSM_RP_DA();
		SM_RP_OA oa = ind.getSM_RP_OA();
		byte[] ui = ind.getSM_RP_UI();
		assertEquals(AddressNature.international_number, da.getServiceCentreAddressDA().getAddressNature());
		assertEquals(NumberingPlan.ISDN, da.getServiceCentreAddressDA().getNumberingPlan());
		assertEquals("223334990223", da.getServiceCentreAddressDA().getAddress());
		assertEquals(AddressNature.international_number, oa.getMsisdn().getAddressNature());
		assertEquals(NumberingPlan.ISDN, oa.getMsisdn().getNumberingPlan());
		assertEquals("2311231234334", oa.getMsisdn().getAddress());
		assertTrue(Arrays.equals(ui, new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8 }));
		assertFalse(ind.getMoreMessagesToSend());
		
		rawData = getEncodedDataComplex();
		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		ind = new ForwardShortMessageRequestIndicationImpl();
		ind.decodeAll(asn);

		assertEquals(Tag.SEQUENCE, tag);
		assertEquals(Tag.CLASS_UNIVERSAL, asn.getTagClass());
		
		da = ind.getSM_RP_DA();
		oa = ind.getSM_RP_OA();
		ui = ind.getSM_RP_UI();
		assertEquals(AddressNature.international_number, da.getServiceCentreAddressDA().getAddressNature());
		assertEquals(NumberingPlan.ISDN, da.getServiceCentreAddressDA().getNumberingPlan());
		assertEquals("2311231234334", da.getServiceCentreAddressDA().getAddress());
		assertEquals(AddressNature.international_number, oa.getMsisdn().getAddressNature());
		assertEquals(NumberingPlan.ISDN, oa.getMsisdn().getNumberingPlan());
		assertEquals("223334990223", oa.getMsisdn().getAddress());
		assertTrue(Arrays.equals(ui, new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4 }));
		assertTrue(ind.getMoreMessagesToSend());
	}

	@Test(groups = { "functional.encode","service.sms"})
	public void testEncode() throws Exception {

		AddressString sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "223334990223");
		SM_RP_DA sm_RP_DA = new SM_RP_DAImpl(sca);
		ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "2311231234334");
		SM_RP_OAImpl sm_RP_OA = new SM_RP_OAImpl();
		sm_RP_OA.setMsisdn(msisdn);
		byte[] sm_RP_UI = new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8 };
		ForwardShortMessageRequestIndicationImpl ind = new ForwardShortMessageRequestIndicationImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, false);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		ind.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedDataSimple();		
		assertTrue(Arrays.equals(rawData, encodedData));

		
		sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "2311231234334");
		sm_RP_DA = new SM_RP_DAImpl(sca);
		msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "223334990223");
		sm_RP_OA = new SM_RP_OAImpl();
		sm_RP_OA.setMsisdn(msisdn);
		sm_RP_UI = new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4 };
		ind = new ForwardShortMessageRequestIndicationImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, true);
		
		asnOS = new AsnOutputStream();
		ind.encodeAll(asnOS);
		
		encodedData = asnOS.toByteArray();
		rawData = getEncodedDataComplex();		
		assertTrue(Arrays.equals(rawData, encodedData));
	}

}
