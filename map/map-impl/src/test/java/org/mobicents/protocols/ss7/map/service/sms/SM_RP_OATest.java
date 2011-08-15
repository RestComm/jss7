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
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;

import junit.framework.TestCase;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class SM_RP_OATest extends TestCase {
	
	private byte[] getEncodedData_Msisdn() {
		return new byte[] { (byte)130, 7, (byte)145, (byte)147, 51, 88, 38, 101, 89 };
	}
	
	private byte[] getEncodedData_ServiceCentreAddressOA() {
		return new byte[] { -124, 7, -111, -127, 16, 7, 17, 17, -15 };
	}
	
	private byte[] getEncodedData_No() {
		return new byte[] { (byte)133, 0 };
	}
	
	@org.junit.Test
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedData_ServiceCentreAddressOA();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		SM_RP_OAImpl oa = new SM_RP_OAImpl();
		oa.decodeAll(asn);

		assertEquals(4, tag);
		assertEquals(Tag.CLASS_CONTEXT_SPECIFIC, asn.getTagClass());
		
		AddressString nnm = oa.getServiceCentreAddressOA();
		assertEquals(AddressNature.international_number, nnm.getAddressNature());
		assertEquals(NumberingPlan.ISDN, nnm.getNumberingPlan());
		assertEquals("18017011111", nnm.getAddress());

		
		rawData = getEncodedData_Msisdn();
		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		oa = new SM_RP_OAImpl();
		oa.decodeAll(asn);

		assertEquals(2, tag);
		assertEquals(Tag.CLASS_CONTEXT_SPECIFIC, asn.getTagClass());
		
		ISDNAddressString msisdn = oa.getMsisdn();
		assertEquals(AddressNature.international_number, msisdn.getAddressNature());
		assertEquals(NumberingPlan.ISDN, msisdn.getNumberingPlan());
		assertEquals("393385625695", msisdn.getAddress());

		
		rawData = getEncodedData_No();
		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		oa = new SM_RP_OAImpl();
		oa.decodeAll(asn);

		assertEquals(5, tag);
		assertEquals(Tag.CLASS_CONTEXT_SPECIFIC, asn.getTagClass());
		
		assertEquals(null, oa.getServiceCentreAddressOA());
		assertEquals(null, oa.getMsisdn());
	}

	@org.junit.Test
	public void testEncode() throws Exception {
		
		AddressStringImpl astr = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "18017011111");
		SM_RP_OAImpl oa = new SM_RP_OAImpl();
		oa.setServiceCentreAddressOA(astr);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		oa.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData_ServiceCentreAddressOA();		
		assertTrue(Arrays.equals(rawData, encodedData));
		
		
		ISDNAddressStringImpl isdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "393385625695");
		oa = new SM_RP_OAImpl();
		oa.setMsisdn(isdn);
		
		asnOS = new AsnOutputStream();
		oa.encodeAll(asnOS);
		
		encodedData = asnOS.toByteArray();
		rawData = getEncodedData_Msisdn();		
		assertTrue(Arrays.equals(rawData, encodedData));
		
		
		oa = new SM_RP_OAImpl();
		
		asnOS = new AsnOutputStream();
		oa.encodeAll(asnOS);
		
		encodedData = asnOS.toByteArray();
		rawData = getEncodedData_No();		
		assertTrue(Arrays.equals(rawData, encodedData));
	}
}
