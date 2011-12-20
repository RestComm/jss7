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

package org.mobicents.protocols.ss7.map.smstpdu;


import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class AddressFieldTest {

	public byte[] getData() {
		return new byte[] { 11, -111, 39, 34, -125, 72, 35, -15 };
	}

	@Test(groups = { "functional.decode","smstpdu"})
	public void testDecode() throws Exception {

		InputStream stm = new ByteArrayInputStream(this.getData());
		AddressFieldImpl impl = AddressFieldImpl.createMessage(stm);
		assertEquals(impl.getTypeOfNumber(), TypeOfNumber.InternationalNumber);
		assertEquals(impl.getNumberingPlanIdentification(), NumberingPlanIdentification.ISDNTelephoneNumberingPlan);
		assertEquals(impl.getAddressValue(), "72223884321");
	}

	@Test(groups = { "functional.encode","smstpdu"})
	public void testEncode() throws Exception {

		AddressFieldImpl impl = new AddressFieldImpl(TypeOfNumber.InternationalNumber, NumberingPlanIdentification.ISDNTelephoneNumberingPlan, "72223884321");
		ByteArrayOutputStream stm = new ByteArrayOutputStream();
		impl.encodedData(stm);
		assertTrue(Arrays.equals(stm.toByteArray(), this.getData()));
	}
}
