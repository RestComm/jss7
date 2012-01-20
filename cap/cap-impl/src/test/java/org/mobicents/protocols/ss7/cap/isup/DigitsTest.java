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
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class DigitsTest {

	public byte[] getData1() {
		return new byte[] { (byte) 157, 5, 65, 5, 6, 7, 8 };
	}

	public byte[] getData2() {
		return new byte[] { (byte) 157, 7, 3, (byte) 132, 33, 7, 1, 9, 0 };
	}

	public int[] getGenericDigitsInt() {
		return new int[] { 5, 6, 7, 8 };
	}

	@Test(groups = { "functional.decode","isup"})
	public void testDecode() throws Exception {

		byte[] data = this.getData1();
		AsnInputStream ais = new AsnInputStream(data);
		DigitsImpl elem = new DigitsImpl();
		int tag = ais.readTag();
		assertEquals(tag, 29);
		elem.decodeAll(ais);
		GenericDigits gd = elem.getGenericDigits();
		assertEquals(gd.getEncodingScheme(), 2);
		assertEquals(gd.getTypeOfDigits(), 1);
		assertTrue(Arrays.equals(gd.getDigits(), getGenericDigitsInt()));
		
		data = this.getData2();
		ais = new AsnInputStream(data);
		elem = new DigitsImpl();
		tag = ais.readTag();
		assertEquals(tag, 29);
		elem.decodeAll(ais);
		GenericNumber gn = elem.getGenericNumber();
		assertEquals(gn.getNatureOfAddressIndicator(), 4);
		assertTrue(gn.getAddress().equals("7010900"));
		assertEquals(gn.getNumberQualifierIndicator(), 3);
		assertEquals(gn.getNumberingPlanIndicator(), 2);
		assertEquals(gn.getAddressRepresentationRestrictedIndicator(), 0);
		assertEquals(gn.getScreeningIndicator(), 1);
	}

	@Test(groups = { "functional.encode","isup"})
	public void testEncode() throws Exception {

		GenericDigitsImpl genericDigits = new GenericDigitsImpl(2, 1, getGenericDigitsInt());
		DigitsImpl elem = new DigitsImpl(genericDigits);
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 29);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
		// int encodingScheme, int typeOfDigits, int[] digits

		GenericNumber rn = new GenericNumberImpl(4, "7010900", 3, 2, 0, false, 1);
		elem = new DigitsImpl(rn);
		aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 29);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
		// int natureOfAddresIndicator, String address, int numberQualifierIndicator, int numberingPlanIndicator, int addressRepresentationREstrictedIndicator,
		// boolean numberIncomplete, int screeningIndicator
	}
}

