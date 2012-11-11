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

package org.mobicents.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.*;
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
* 
* @author sergey vetyutnev
* 
*/
public class SSStatusTest {

	private byte[] getEncodedData1() {
		return new byte[] { 4, 1, 10 };
	}

	private byte[] getEncodedData2() {
		return new byte[] { 4, 1, 5 };
	}

	@Test(groups = { "functional.decode", "service.supplementary" })
	public void testDecode() throws Exception {

		byte[] rawData = getEncodedData1();

		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		assertEquals(tag, Tag.STRING_OCTET);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

		SSStatusImpl impl = new SSStatusImpl();
		impl.decodeAll(asn);

		assertTrue(impl.getQBit());
		assertFalse(impl.getPBit());
		assertTrue(impl.getRBit());
		assertFalse(impl.getABit());


		rawData = getEncodedData2();

		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		assertEquals(tag, Tag.STRING_OCTET);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

		impl = new SSStatusImpl();
		impl.decodeAll(asn);

		assertFalse(impl.getQBit());
		assertTrue(impl.getPBit());
		assertFalse(impl.getRBit());
		assertTrue(impl.getABit());
	} 
	
	@Test(groups = { "functional.encode", "service.supplementary" })
	public void testEncode() throws Exception {

		SSStatusImpl impl = new SSStatusImpl(true, false, true, false);
		AsnOutputStream asnOS = new AsnOutputStream();

		impl.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData1();		
		assertTrue( Arrays.equals(rawData,encodedData));


		impl = new SSStatusImpl(false, true, false, true);
		asnOS = new AsnOutputStream();

		impl.encodeAll(asnOS);
		
		encodedData = asnOS.toByteArray();
		rawData = getEncodedData2();		
		assertTrue( Arrays.equals(rawData,encodedData));
	}
}
