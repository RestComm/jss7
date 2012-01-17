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
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class DigitsTest {

	public byte[] getData() {
		return new byte[] {  };
	}

	public byte[] getIntData() {
		return new byte[] {  };
	}

	@Test(groups = { "functional.decode","isup"})
	public void testDecode() throws Exception {

//		byte[] data = this.getData();
//		AsnInputStream ais = new AsnInputStream(data);
//		AdditionalCallingPartyNumberCapImpl elem = new AdditionalCallingPartyNumberCapImpl();
//		int tag = ais.readTag();
//		elem.decodeAll(ais);
//		GenericNumber gn = elem.getGenericNumber();
//		assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
//		assertEquals(gn.getNatureOfAddressIndicator(), 3);
//		assertTrue(gn.getAddress().equals("7010900"));
//		assertEquals(gn.getNumberingPlanIndicator(), 1);
//		assertEquals(gn.getAddressRepresentationRestrictedIndicator(), 1);
//		assertEquals(gn.getNumberQualifierIndicator(), 1);
//		assertEquals(gn.getScreeningIndicator(), 0);
		
		// TODO: implement decoding test after getting encoded data
	}

	@Test(groups = { "functional.encode","isup"})
	public void testEncode() throws Exception {

//		AdditionalCallingPartyNumberCapImpl elem = new AdditionalCallingPartyNumberCapImpl(this.getIntData());
//		AsnOutputStream aos = new AsnOutputStream();
//		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 29);
//		assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

//		GenericNumber rn = new GenericNumberImpl(3, "7010900", 1, 1, 1, false, 0);
//		elem = new AdditionalCallingPartyNumberCapImpl(rn);
//		aos = new AsnOutputStream();
//		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 29);
//		assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));
		
		// int natureOfAddresIndicator, String address, int numberQualifierIndicator, int numberingPlanIndicator, int addressRepresentationREstrictedIndicator,
		// boolean numberIncomplete, int screeningIndicator
		
		// TODO: resolve ISUP GenericNumber encoding failure 
	}
}

