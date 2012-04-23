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

package org.mobicents.protocols.ss7.inap.isup;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.UserTeleserviceInformationImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class HighLayerCompatibilityInapTest {

	public byte[] getData() {
		return new byte[] { (byte) 151, 2, (byte) 145, (byte) 129 };
	}

	public byte[] getIntData() {
		return new byte[] { (byte) 145, (byte) 129 };
	}

	@Test(groups = { "functional.decode","isup"})
	public void testDecode() throws Exception {

		byte[] data = this.getData();
		AsnInputStream ais = new AsnInputStream(data);
		HighLayerCompatibilityInapImpl elem = new HighLayerCompatibilityInapImpl();
		int tag = ais.readTag();
		elem.decodeAll(ais);
		UserTeleserviceInformation hlc = elem.getHighLayerCompatibility();
		assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
		assertEquals(hlc.getCodingStandard(), 0);
		assertEquals(hlc.getEHighLayerCharIdentification(), 0);
		assertEquals(hlc.getEVidedoTelephonyCharIdentification(), 0);
		assertEquals(hlc.getInterpretation(), 4);
		assertEquals(hlc.getPresentationMethod(), 1);
		assertEquals(hlc.getHighLayerCharIdentification(), 1);
	}

	@Test(groups = { "functional.encode","isup"})
	public void testEncode() throws Exception {

		HighLayerCompatibilityInapImpl elem = new HighLayerCompatibilityInapImpl(this.getIntData());
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 23);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

		UserTeleserviceInformation hlc = new UserTeleserviceInformationImpl(0, 4, 1, 1);
		elem = new HighLayerCompatibilityInapImpl(hlc);
		aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 23);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));
		
//		int codingStandard, int interpretation, int presentationMethod, int highLayerCharIdentification
	}
}
