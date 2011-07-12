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

package org.mobicents.protocols.ss7.map.dialog;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;

/**
 * 
 * @author amit bhayani
 *
 */
public class AddressStringTest extends TestCase {

	@org.junit.Test
	public void testDecode() throws Exception {
		
		byte[] rawData = new byte[] { (byte) 0x96, 0x02, 0x24, (byte) 0x80,
				0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2 };
		
		AsnInputStream asn = new AsnInputStream(new ByteArrayInputStream(rawData));
		

		AddressStringImpl addStr = new AddressStringImpl();
		addStr.decode(asn, Tag.CLASS_UNIVERSAL, true, Tag.STRING_OCTET, rawData.length);

		assertFalse(addStr.isExtension());

		assertEquals(AddressNature.international_number, addStr
				.getAddressNature());
		
		assertEquals(NumberingPlan.land_mobile, addStr.getNumberingPlan());
		
		assertEquals("204208300008002", addStr.getAddress());
	}
	
	@org.junit.Test
	public void testEncode() throws Exception {
		AddressStringImpl addStr = new AddressStringImpl(AddressNature.international_number, NumberingPlan.land_mobile, "204208300008002");
		AsnOutputStream asnOS = new AsnOutputStream();
		
		addStr.encode(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		
		//System.out.println(Utils.dump(rawData, rawData.length, false));
		
		byte[] rawData = new byte[] { (byte) 0x96, 0x02, 0x24, (byte) 0x80,
				0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2 };		
		
		assertTrue(Arrays.equals(rawData, encodedData));
		
	}

}
