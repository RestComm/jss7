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

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

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
	
	@Test(groups = { "functional.decode", "imei" })
	public void testDecode() throws Exception {
		byte[] rawData = getEncodedDataV2();
		AsnInputStream asnIS = new AsnInputStream(rawData);
		
		int tag = asnIS.readTag();
		assertEquals(tag, Tag.STRING_OCTET);
		CheckImeiRequestImpl checkImeiImpl = new CheckImeiRequestImpl(2);
		checkImeiImpl.decodeAll(asnIS);
		
		assertTrue(checkImeiImpl.getIMEI().getIMEI().equals("358091016853550"));
	}
	
	@Test(groups = { "functional.encode", "imei" })
	public void testEncode() throws Exception {
		IMEIImpl imei = new IMEIImpl("358091016853550");
		CheckImeiRequestImpl checkImei = new CheckImeiRequestImpl(2, imei, null, null);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		checkImei.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedDataV2();
		assertTrue(Arrays.equals(rawData, encodedData));
		
	}
	
}