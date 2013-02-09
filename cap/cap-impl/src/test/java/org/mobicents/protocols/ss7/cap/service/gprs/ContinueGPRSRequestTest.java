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
package org.mobicents.protocols.ss7.cap.service.gprs;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertNull;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.testng.annotations.Test;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class ContinueGPRSRequestTest {
	
	public byte[] getData() {
		return new byte[] {48, 3, -128, 1, 2};
	};
	
	public byte[] getDataLiveTrace() {
		return new byte[] {0x30,0x00};
	}
	

	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		byte[] data = this.getData();
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		ContinueGPRSRequestImpl prim = new ContinueGPRSRequestImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, Tag.SEQUENCE);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
		assertEquals(prim.getPDPID().getId(),2);
	}
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testDecodeLiveTrace() throws Exception {
		byte[] data = this.getDataLiveTrace();
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		ContinueGPRSRequestImpl prim = new ContinueGPRSRequestImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, Tag.SEQUENCE);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
		assertNull(prim.getPDPID());
	}
	
	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {
		
		PDPID pdpID = new PDPIDImpl(2);
		ContinueGPRSRequestImpl prim = new ContinueGPRSRequestImpl(pdpID);
		AsnOutputStream asn = new AsnOutputStream();
		prim.encodeAll(asn);
		assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
	}
	
	@Test(groups = { "functional.encode", "primitives" })
	public void testEncodeLiveTrace() throws Exception {
		ContinueGPRSRequestImpl prim = new ContinueGPRSRequestImpl(null);
		AsnOutputStream asn = new AsnOutputStream();
		prim.encodeAll(asn);
		assertTrue(Arrays.equals(asn.toByteArray(), this.getDataLiveTrace()));
	}
	
}
