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

import static org.testng.Assert.*;import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class AlertServiceCentreRequestIndicationTest  {
	
	private byte[] getEncodedData() {
		return new byte[] { 48, 18, 4, 7, -111, -110, 17, 19, 50, 19, -15, 4, 7, -111, -108, -120, 115, 0, -110, -14 };
	}
	
	@Test
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedData();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		AlertServiceCentreRequestIndicationImpl asc = new AlertServiceCentreRequestIndicationImpl();
		asc.decodeAll(asn);

		assertEquals( tag,Tag.SEQUENCE);
		assertEquals( asn.getTagClass(),Tag.CLASS_UNIVERSAL);
		
		ISDNAddressString msisdn = asc.getMsisdn();
		assertEquals( msisdn.getAddressNature(),AddressNature.international_number);
		assertEquals( msisdn.getNumberingPlan(),NumberingPlan.ISDN);
		assertEquals( msisdn.getAddress(),"29113123311");
		
		AddressString sca = asc.getServiceCentreAddress();
		assertEquals( sca.getAddressNature(),AddressNature.international_number);
		assertEquals( sca.getNumberingPlan(),NumberingPlan.ISDN);
		assertEquals( sca.getAddress(),"49883700292");
	}

	@Test(groups = { "functional.encode"})
	public void testEncode() throws Exception {

		ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "29113123311");
		AddressString sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "49883700292");
		AlertServiceCentreRequestIndicationImpl asc = new AlertServiceCentreRequestIndicationImpl(msisdn, sca);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		asc.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();		
		assertTrue( Arrays.equals(rawData,encodedData));
	}
}
