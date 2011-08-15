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

package org.mobicents.protocols.ss7.map.primitives;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

import junit.framework.TestCase;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class IMEITest extends TestCase {
	
	private byte[] getEncodedData() {
		return new byte[] { 4, 8, 33, 67, 101, (byte)135, 9, 33, 67, 101 };
	}

	@org.junit.Test
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedData();
		
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		IMEIImpl imei = new IMEIImpl();
		imei.decodeAll(asn);

		assertEquals(Tag.STRING_OCTET, tag);
		assertEquals(Tag.CLASS_UNIVERSAL, asn.getTagClass());
		
		assertEquals("1234567890123456", imei.getIMEI());
	}
	
	@org.junit.Test
	public void testEncode() throws Exception {
		
		IMEIImpl imei = new IMEIImpl("1234567890123456");
		AsnOutputStream asnOS = new AsnOutputStream();
		
		imei.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		
		byte[] rawData = getEncodedData();		
		
		assertTrue(Arrays.equals(rawData, encodedData));
		
	}

}

