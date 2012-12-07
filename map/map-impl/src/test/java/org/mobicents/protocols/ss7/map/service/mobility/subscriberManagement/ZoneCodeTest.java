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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class ZoneCodeTest {
	
	public byte[] getData() {
		return new byte[] {4, 2, 0, 2 };
	};
	
	public byte[] getData1() {
		return new byte[] {4, 2, 2, 5};
	};
	
	public byte[] getZoneCodeData() {
		return new byte[] { 2, 5 };
	};

	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		byte[] data = this.getData();
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		ZoneCodeImpl prim = new ZoneCodeImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, Tag.STRING_OCTET);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
		assertEquals(prim.getValue(), 2);
		
		data = this.getData1();
		asn = new AsnInputStream(data);
		tag = asn.readTag();
		prim = new ZoneCodeImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, Tag.STRING_OCTET);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
		assertEquals(prim.getData(), this.getZoneCodeData());
	}

	@Test(groups = { "functional.encode","primitives"})
	public void testEncode() throws Exception {
		
		ZoneCodeImpl prim = new ZoneCodeImpl(2);
		AsnOutputStream asn = new AsnOutputStream();
		prim.encodeAll(asn);
		assertEquals(asn.toByteArray(), this.getData());
		
		prim = new ZoneCodeImpl(this.getZoneCodeData());
		asn = new AsnOutputStream();
		prim.encodeAll(asn);
		assertEquals(asn.toByteArray(), this.getData1());
		
	}
}
