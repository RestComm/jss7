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

package org.mobicents.protocols.ss7.cap.isup;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CallingPartyNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class CallingPartyNumberCapTest {

	public byte[] getData() {
		return new byte[] { (byte) 131, 8, (byte) 132, 17, 20, (byte) 135, 9, 80, 64, (byte) 7 }; // 247
	}

	public byte[] getIntData() {
		return new byte[] { (byte) 132, 17, 20, (byte) 135, 9, 80, 64, (byte) 7 }; // 247
	}

	@Test(groups = { "functional.decode","isup"})
	public void testDecode() throws Exception {

		byte[] data = this.getData();
		AsnInputStream ais = new AsnInputStream(data);
		CallingPartyNumberCapImpl elem = new CallingPartyNumberCapImpl();
		int tag = ais.readTag();
		elem.decodeAll(ais);
		CallingPartyNumber cpn = elem.getCallingPartyNumber();
		assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
		assertTrue(cpn.isOddFlag());
		assertEquals(cpn.getNumberingPlanIndicator(), 1);
		assertEquals(cpn.getScreeningIndicator(), 1);
		assertEquals(cpn.getAddressRepresentationREstrictedIndicator(), 0);
		assertEquals(cpn.getNumberIncompleteIndicator(), 0);
		assertEquals(cpn.getNatureOfAddressIndicator(), 4);
		assertTrue(cpn.getAddress().equals("41789005047"));
	}

	@Test(groups = { "functional.encode","isup"})
	public void testEncode() throws Exception {

		CallingPartyNumberCapImpl elem = new CallingPartyNumberCapImpl(this.getIntData());
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 3);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

		CallingPartyNumber cpn = new CallingPartyNumberImpl(4, "41789005047", 1, 0, 0, 1);
		elem = new CallingPartyNumberCapImpl(cpn);
		aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 3);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));
		
//		int natureOfAddresIndicator, String address, int numberingPlanIndicator, int numberIncompleteIndicator, int addressRepresentationREstrictedIndicator,
//		int screeningIndicator
	}
}


