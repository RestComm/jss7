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
public class LMSITest extends TestCase {
	
	private byte[] getEncodedData() {
		return new byte[] { 4, 4, 0, 3, 98, 49 };
	}

	@org.junit.Test
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedData();
		
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		LMSIImpl lmsi = new LMSIImpl();
		lmsi.decodeAll(asn);

		assertEquals(Tag.STRING_OCTET, tag);
		assertEquals(Tag.CLASS_UNIVERSAL, asn.getTagClass());
		
		assertTrue(Arrays.equals(new byte[] { 0, 3, 98, 49 }, lmsi.getData()));
	}
	
	@org.junit.Test
	public void testEncode() throws Exception {
		
		LMSIImpl lmsi = new LMSIImpl(new byte[] { 0, 3, 98, 49 });
		AsnOutputStream asnOS = new AsnOutputStream();
		
		lmsi.encodeAll(asnOS, Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET);
		
		byte[] encodedData = asnOS.toByteArray();
		
		byte[] rawData = getEncodedData();		
		
		assertTrue(Arrays.equals(rawData, encodedData));
		
	}
}
