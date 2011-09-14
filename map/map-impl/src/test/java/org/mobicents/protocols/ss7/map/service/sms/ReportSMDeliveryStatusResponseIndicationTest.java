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
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;

import static org.testng.Assert.*;import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class ReportSMDeliveryStatusResponseIndicationTest  {
	
	private byte[] getEncodedData() {
		return new byte[] { 48, 49, 4, 6, -111, -120, 120, 119, 102, -10, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6,
				48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
	}
	
	@Test(groups = { "functional.decode","service.sms"})
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedData();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		ReportSMDeliveryStatusResponseIndicationImpl ind = new ReportSMDeliveryStatusResponseIndicationImpl();
		ind.decodeAll(asn);

		assertEquals( tag,Tag.SEQUENCE);
		assertEquals( asn.getTagClass(),Tag.CLASS_UNIVERSAL);

		assertEquals( ind.getStoredMSISDN().getAddressNature(),AddressNature.international_number);
		assertEquals( ind.getStoredMSISDN().getNumberingPlan(),NumberingPlan.ISDN);
		assertEquals( ind.getStoredMSISDN().getAddress(),"888777666");
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
	}

	@Test(groups = { "functional.encode","service.sms"})
	public void testEncode() throws Exception {

		ISDNAddressString storedMSISDN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "888777666");
		MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
		ReportSMDeliveryStatusResponseIndicationImpl ind = new ReportSMDeliveryStatusResponseIndicationImpl(storedMSISDN, extensionContainer);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		ind.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();		
		assertTrue( Arrays.equals(rawData,encodedData));
	}
}


