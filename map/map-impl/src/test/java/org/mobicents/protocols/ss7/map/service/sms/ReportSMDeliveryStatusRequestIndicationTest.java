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
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class ReportSMDeliveryStatusRequestIndicationTest extends TestCase {
	
	private byte[] getEncodedData() {
		return new byte[] { 48, 19, 4, 6, -111, 39, 34, 51, 19, 17, 4, 6, -111, 1, -112, 115, 84, -13, 10, 1, 1 };
	}
	
	private byte[] getEncodedDataFull() {
		return new byte[] { 48, 73, 4, 6, -72, 17, 33, 34, 51, -13, 4, 6, -111, 51, 35, 34, 17, -15, 10, 1, 2, -128, 2, 1, -68, -95, 39, -96, 32, 48, 10, 6, 3,
				42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -126, 0, -124, 1, 0,
				-123, 2, 2, 43 };
	}
	
	@org.junit.Test
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedData();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		ReportSMDeliveryStatusRequestIndicationImpl ind = new ReportSMDeliveryStatusRequestIndicationImpl();
		ind.decodeAll(asn);

		assertEquals(Tag.SEQUENCE, tag);
		assertEquals(Tag.CLASS_UNIVERSAL, asn.getTagClass());

		ISDNAddressString msisdn = ind.getMsisdn();
		assertEquals(AddressNature.international_number, msisdn.getAddressNature());
		assertEquals(NumberingPlan.ISDN, msisdn.getNumberingPlan());
		assertEquals("7222333111", msisdn.getAddress());
		AddressString sca = ind.getServiceCentreAddress();
		assertEquals(AddressNature.international_number, sca.getAddressNature());
		assertEquals(NumberingPlan.ISDN, sca.getNumberingPlan());
		assertEquals("100937453", sca.getAddress());
		assertEquals(SMDeliveryOutcome.absentSubscriber, ind.getSMDeliveryOutcome());

		
		rawData = getEncodedDataFull();
		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		ind = new ReportSMDeliveryStatusRequestIndicationImpl();
		ind.decodeAll(asn);

		assertEquals(Tag.SEQUENCE, tag);
		assertEquals(Tag.CLASS_UNIVERSAL, asn.getTagClass());

		msisdn = ind.getMsisdn();
		assertEquals(AddressNature.network_specific_number, msisdn.getAddressNature());
		assertEquals(NumberingPlan.national, msisdn.getNumberingPlan());
		assertEquals("111222333", msisdn.getAddress());
		sca = ind.getServiceCentreAddress();
		assertEquals(AddressNature.international_number, sca.getAddressNature());
		assertEquals(NumberingPlan.ISDN, sca.getNumberingPlan());
		assertEquals("333222111", sca.getAddress());
		assertEquals(SMDeliveryOutcome.successfulTransfer, ind.getSMDeliveryOutcome());
		assertEquals(444, (int) ind.getAbsentSubscriberDiagnosticSM());		
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
		assertEquals(true, (boolean)ind.getGprsSupportIndicator());		
		assertEquals(false, (boolean)ind.getDeliveryOutcomeIndicator());		
		assertEquals(SMDeliveryOutcome.memoryCapacityExceeded, ind.getAdditionalSMDeliveryOutcome());
		assertEquals(555, (int) ind.getAdditionalAbsentSubscriberDiagnosticSM());		
	}

	@org.junit.Test
	public void testEncode() throws Exception {

		ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "7222333111");
		AddressString sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "100937453");
		ReportSMDeliveryStatusRequestIndicationImpl ind = new ReportSMDeliveryStatusRequestIndicationImpl(msisdn, sca, SMDeliveryOutcome.absentSubscriber,
				null, null, null, null, null, null);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		ind.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();		
		assertTrue(Arrays.equals(rawData, encodedData));

		
		msisdn = new ISDNAddressStringImpl(AddressNature.network_specific_number, NumberingPlan.national, "111222333");
		sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "333222111");
		ind = new ReportSMDeliveryStatusRequestIndicationImpl(msisdn, sca, SMDeliveryOutcome.successfulTransfer, 444,
				MAPExtensionContainerTest.GetTestExtensionContainer(), true, false, SMDeliveryOutcome.memoryCapacityExceeded, 555);
		
		asnOS = new AsnOutputStream();
		ind.encodeAll(asnOS);
		
		encodedData = asnOS.toByteArray();
		rawData = getEncodedDataFull();		
		assertTrue(Arrays.equals(rawData, encodedData));
		
		
	}

}
