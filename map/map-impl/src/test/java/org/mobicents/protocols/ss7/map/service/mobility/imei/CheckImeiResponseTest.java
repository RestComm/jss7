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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.testng.annotations.Test;

/**
 * 
 * @author normandes
 *
 */
public class CheckImeiResponseTest {

	// Real Trace
	private byte[] getEncodedDataV2() {
		return new byte[] { 0x0a, 0x01, 0x00 };
	}
	
	@Test(groups = { "functional.decode", "imei" })
	public void testDecode() throws Exception {
		byte[] rawData = getEncodedDataV2();
		AsnInputStream asnIS = new AsnInputStream(rawData);

		int tag = asnIS.readTag();
		assertEquals(tag, Tag.ENUMERATED);
		CheckImeiResponseImpl checkImeiImpl = new CheckImeiResponseImpl(2);
		checkImeiImpl.decodeAll(asnIS);
		
		assertEquals(checkImeiImpl.getEquipmentStatus(), EquipmentStatus.whiteListed);
	}
	
	@Test(groups = { "functional.encode", "imei" })
	public void testEncode() throws Exception {
		CheckImeiResponseImpl checkImei = new CheckImeiResponseImpl(2, EquipmentStatus.whiteListed);
		
		AsnOutputStream asnOS = new AsnOutputStream();
		checkImei.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedDataV2();
		assertTrue(Arrays.equals(rawData, encodedData));
	}
	
}