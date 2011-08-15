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
import org.mobicents.protocols.ss7.map.api.service.sms.MWStatus;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class InformServiceCentreRequestIndicationTest extends TestCase {
	
	private byte[] getEncodedData() {
		return new byte[] { 48, 4, 3, 2, 2, 64 };
	}
	
	private byte[] getEncodedDataFull() {
		return new byte[] { 48, 61, 4, 6, -111, 17, 33, 34, 51, -13, 3, 2, 2, 80, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
				3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 2, 2, 2, 43, -128, 2, 1, -68 };
	}
	
	@org.junit.Test
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedData();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		InformServiceCentreRequestIndicationImpl isc = new InformServiceCentreRequestIndicationImpl();
		isc.decodeAll(asn);

		assertEquals(Tag.SEQUENCE, tag);
		assertEquals(Tag.CLASS_UNIVERSAL, asn.getTagClass());
		
		MWStatus mwStatus = isc.getMwStatus();
		assertNotNull(mwStatus);
		assertFalse(mwStatus.getScAddressNotIncluded());
		assertTrue(mwStatus.getMnrfSet());
		assertFalse(mwStatus.getMcefSet());
		assertFalse(mwStatus.getMnrgSet());
		
		rawData = getEncodedDataFull();
		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		isc = new InformServiceCentreRequestIndicationImpl();
		isc.decodeAll(asn);

		assertEquals(Tag.SEQUENCE, tag);
		assertEquals(Tag.CLASS_UNIVERSAL, asn.getTagClass());
		
		MAPExtensionContainer extensionContainer = isc.getExtensionContainer();
		ISDNAddressString storedMSISDN = isc.getStoredMSISDN();
		mwStatus = isc.getMwStatus();
		int absentSubscriberDiagnosticSM = isc.getAbsentSubscriberDiagnosticSM();
		int additionalAbsentSubscriberDiagnosticSM = isc.getAdditionalAbsentSubscriberDiagnosticSM();

		Assert.assertNotNull(storedMSISDN);
		Assert.assertEquals(storedMSISDN.getAddressNature(), AddressNature.international_number);
		Assert.assertEquals(storedMSISDN.getNumberingPlan(), NumberingPlan.ISDN);
		Assert.assertEquals(storedMSISDN.getAddress(), "111222333");
		Assert.assertNotNull(mwStatus);
		Assert.assertFalse(mwStatus.getScAddressNotIncluded());
		Assert.assertTrue(mwStatus.getMnrfSet());
		Assert.assertFalse(mwStatus.getMcefSet());
		Assert.assertTrue(mwStatus.getMnrgSet());
		Assert.assertNotNull(absentSubscriberDiagnosticSM);
		Assert.assertEquals((int) absentSubscriberDiagnosticSM, 555);
		Assert.assertNotNull(additionalAbsentSubscriberDiagnosticSM);
		Assert.assertEquals((int) additionalAbsentSubscriberDiagnosticSM, 444);
		Assert.assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
	}

	@org.junit.Test
	public void testEncode() throws Exception {
		
		MWStatus mwStatus = new MWStatusImpl(false, true, false, false);
		InformServiceCentreRequestIndicationImpl isc = new InformServiceCentreRequestIndicationImpl(null, mwStatus, null, null, null);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		isc.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();		
		assertTrue(Arrays.equals(rawData, encodedData));

		
		ISDNAddressString storedMSISDN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
		mwStatus = new MWStatusImpl(false, true, false, true);
		Integer absentSubscriberDiagnosticSM = 555;
		Integer additionalAbsentSubscriberDiagnosticSM = 444;
		isc = new InformServiceCentreRequestIndicationImpl(storedMSISDN, mwStatus, MAPExtensionContainerTest.GetTestExtensionContainer(),
				absentSubscriberDiagnosticSM, additionalAbsentSubscriberDiagnosticSM);
		
		asnOS.reset();
		isc.encodeAll(asnOS);
		
		encodedData = asnOS.toByteArray();
		rawData = getEncodedDataFull();		
		assertTrue(Arrays.equals(rawData, encodedData));
	}

}
