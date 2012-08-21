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

package org.mobicents.protocols.ss7.map.service.mobility.imei;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 * 
 * @author normandes
 *
 */
public class CheckImeiRequestTest {

	// Real Trace
	private byte[] getEncodedDataV2() {
		return new byte[] { 0x04, 0x08, 0x53, 0x08, 0x19, 0x10, (byte)0x86, 0x35, 0x55, (byte)0xf0 };
	}
	
	private byte[] getEncodedDataV3() {
		// TODO this is self generated trace. We need trace from operator
		return new byte[] { 48, 14, 4, 8, 83, 8, 25, 16, -122, 53, 85, -16, 3, 2, 6, -128 };
	}
	
	private byte[] getEncodedDataV3Full() {
		// TODO this is self generated trace. We need trace from operator
		return new byte[] { 48, 55, 4, 8, 83, 8, 25, 16, -122, 53, 85, -16, 3, 2, 6, -128, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5,
				6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
	}
	
	@Test(groups = { "functional.decode", "imei" })
	public void testDecode() throws Exception {
		// Testing version 3
		byte[] rawData = getEncodedDataV3();
		AsnInputStream asnIS = new AsnInputStream(rawData);
		
		int tag = asnIS.readTag();
		assertEquals(tag, Tag.SEQUENCE);
		
		CheckImeiRequestImpl checkImeiImpl = new CheckImeiRequestImpl(3);
		checkImeiImpl.decodeAll(asnIS);
		
		assertTrue(checkImeiImpl.getIMEI().getIMEI().equals("358091016853550"));
		assertTrue(checkImeiImpl.getRequestedEquipmentInfo().getEquipmentStatus());
		assertFalse(checkImeiImpl.getRequestedEquipmentInfo().getBmuef());
		
		// Testing version 3 Full
		rawData = getEncodedDataV3Full();
		asnIS = new AsnInputStream(rawData);
		
		tag = asnIS.readTag();
		assertEquals(tag, Tag.SEQUENCE);
		
		checkImeiImpl = new CheckImeiRequestImpl(3);
		checkImeiImpl.decodeAll(asnIS);
		
		assertTrue(checkImeiImpl.getIMEI().getIMEI().equals("358091016853550"));
		assertTrue(checkImeiImpl.getRequestedEquipmentInfo().getEquipmentStatus());
		assertFalse(checkImeiImpl.getRequestedEquipmentInfo().getBmuef());
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(checkImeiImpl.getExtensionContainer()));
		
		// Testing version 1 and 2
		rawData = getEncodedDataV2();
		asnIS = new AsnInputStream(rawData);
		
		tag = asnIS.readTag();
		assertEquals(tag, Tag.STRING_OCTET);
		checkImeiImpl = new CheckImeiRequestImpl(2);
		checkImeiImpl.decodeAll(asnIS);
		
		assertTrue(checkImeiImpl.getIMEI().getIMEI().equals("358091016853550"));
	}
	
	@Test(groups = { "functional.encode", "imei" })
	public void testEncode() throws Exception {
		// Testing version 3
		IMEIImpl imei = new IMEIImpl("358091016853550");
		RequestedEquipmentInfoImpl requestedEquipmentInfo = new RequestedEquipmentInfoImpl(true, false);
		
		CheckImeiRequestImpl checkImei = new CheckImeiRequestImpl(3, imei, requestedEquipmentInfo, null);
		AsnOutputStream asnOS = new AsnOutputStream();
		checkImei.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedDataV3();
		assertTrue(Arrays.equals(rawData, encodedData));
		
		// Testing version 3 Full
		imei = new IMEIImpl("358091016853550");
		requestedEquipmentInfo = new RequestedEquipmentInfoImpl(true, false);
		MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
		
		checkImei = new CheckImeiRequestImpl(3, imei, requestedEquipmentInfo, extensionContainer);
		asnOS = new AsnOutputStream();
		checkImei.encodeAll(asnOS);
		
		encodedData = asnOS.toByteArray();
		rawData = getEncodedDataV3Full();
		assertTrue(Arrays.equals(rawData, encodedData));
		
		// Testing version 1 and 2
		imei = new IMEIImpl("358091016853550");
		checkImei = new CheckImeiRequestImpl(2, imei, null, null);
		
		asnOS = new AsnOutputStream();
		checkImei.encodeAll(asnOS);
		
		encodedData = asnOS.toByteArray();
		rawData = getEncodedDataV2();
		assertTrue(Arrays.equals(rawData, encodedData));
	}
	
}