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

package org.mobicents.protocols.ss7.map.service.subscriberManagement;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class ExtBearerServiceCodeTest {

	private byte[] getEncodedData1() {
		return new byte[] { (byte) 130, 1, 22 };
	}

	private byte[] getData1() {
		return new byte[] { 22 };
	}

	@Test(groups = { "functional.decode","primitives"})
	public void testDecode() throws Exception {

		byte[] rawData = getEncodedData1();
		AsnInputStream asn = new AsnInputStream(rawData);
		int tag = asn.readTag();
		ExtBearerServiceCodeImpl impl = new ExtBearerServiceCodeImpl();
		impl.decodeAll(asn);
		assertTrue(Arrays.equals(impl.getData(), this.getData1()));
	}
	
	@Test(groups = { "functional.encode","primitives"})
	public void testEncode() throws Exception {

		ExtBearerServiceCodeImpl impl = new ExtBearerServiceCodeImpl(this.getData1());
		AsnOutputStream asnOS = new AsnOutputStream();
		impl.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 2);
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData1();
		assertTrue(Arrays.equals(rawData, encodedData));		
	}
}
