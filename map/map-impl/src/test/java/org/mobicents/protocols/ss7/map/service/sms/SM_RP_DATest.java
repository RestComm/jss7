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
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class SM_RP_DATest extends TestCase {
	
	private byte[] getEncodedData_ServiceCentreAddressDA() {
		return new byte[] { -124, 7, -111, 33, 49, -107, 6, 105, 0 };
	}
	
	private byte[] getEncodedData_LMSI() {
		return new byte[] { (byte)129, 4, 0, 7, (byte)144, (byte)178 };
	}
	
	private byte[] getEncodedData_IMSI() {
		return new byte[] { -128, 8, 64, 1, 4, 34, 18, 22, 69, -9 };
	}
	
	private byte[] getEncodedData_No() {
		return new byte[] { (byte)133, 0 };
	}
	
	@org.junit.Test
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedData_ServiceCentreAddressDA();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		SM_RP_DAImpl da = new SM_RP_DAImpl();
		da.decodeAll(asn);

		assertEquals(4, tag);
		assertEquals(Tag.CLASS_CONTEXT_SPECIFIC, asn.getTagClass());
		
		AddressString nnm = da.getServiceCentreAddressDA();
		assertEquals(AddressNature.international_number, nnm.getAddressNature());
		assertEquals(NumberingPlan.ISDN, nnm.getNumberingPlan());
		assertEquals("121359609600", nnm.getAddress());
		
		
		rawData = getEncodedData_LMSI();
		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		da = new SM_RP_DAImpl();
		da.decodeAll(asn);

		assertEquals(1, tag);
		assertEquals(Tag.CLASS_CONTEXT_SPECIFIC, asn.getTagClass());
		
		assertTrue(Arrays.equals(new byte[] { 0, 7, -112, -78 }, da.getLMSI().getData()));
		
		
		rawData = getEncodedData_IMSI();
		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		da = new SM_RP_DAImpl();
		da.decodeAll(asn);

		assertEquals(0, tag);
		assertEquals(Tag.CLASS_CONTEXT_SPECIFIC, asn.getTagClass());

		IMSI imsi = da.getIMSI();
		assertEquals(41, (long)imsi.getMCC());
		assertEquals(4, (long)imsi.getMNC());
		assertEquals("0222161547", imsi.getMSIN());
		
		
		rawData = getEncodedData_No();
		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		da = new SM_RP_DAImpl();
		da.decodeAll(asn);

		assertEquals(5, tag);
		assertEquals(Tag.CLASS_CONTEXT_SPECIFIC, asn.getTagClass());

		assertTrue(da.getServiceCentreAddressDA() == null);		
		assertTrue(da.getIMSI() == null);		
		assertTrue(da.getLMSI() == null);		
	}

	@org.junit.Test
	public void testEncode() throws Exception {

		AddressStringImpl astr = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "121359609600");
		SM_RP_DAImpl da = new SM_RP_DAImpl(astr);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		da.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData_ServiceCentreAddressDA();		
		assertTrue(Arrays.equals(rawData, encodedData));


		LMSIImpl lmsi = new LMSIImpl(new byte[] { 0, 7, -112, -78 });
		da = new SM_RP_DAImpl(lmsi);
		
		asnOS = new AsnOutputStream();
		da.encodeAll(asnOS);
		
		encodedData = asnOS.toByteArray();
		rawData = getEncodedData_LMSI();		
		assertTrue(Arrays.equals(rawData, encodedData));

		
		IMSIImpl imsi = new IMSIImpl(41L, 4L, "0222161547");		
		da = new SM_RP_DAImpl(imsi);
		
		asnOS = new AsnOutputStream();
		da.encodeAll(asnOS);
		
		encodedData = asnOS.toByteArray();
		rawData = getEncodedData_IMSI();		
		assertTrue(Arrays.equals(rawData, encodedData));
		
		
		da = new SM_RP_DAImpl();
		
		asnOS = new AsnOutputStream();
		da.encodeAll(asnOS);
		
		encodedData = asnOS.toByteArray();
		rawData = getEncodedData_No();		
		assertTrue(Arrays.equals(rawData, encodedData));
	}
}
