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
import org.mobicents.protocols.ss7.isup.impl.message.parameter.RedirectionInformationImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class RedirectionInformationInapTest {

	public byte[] getData() {
		return new byte[] { (byte) 158, 2, 3, 97 };
	}

	public byte[] getIntData() {
		return new byte[] { 3, 97 };
	}

	@Test(groups = { "functional.decode","isup"})
	public void testDecode() throws Exception {

		byte[] data = this.getData();
		AsnInputStream ais = new AsnInputStream(data);
		RedirectionInformationInapImpl elem = new RedirectionInformationInapImpl();
		int tag = ais.readTag();
		elem.decodeAll(ais);
		RedirectionInformation ri = elem.getRedirectionInformation();
		assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
		assertEquals(ri.getOriginalRedirectionReason(), 0);
		assertEquals(ri.getRedirectingIndicator(), 3);
		assertEquals(ri.getRedirectionCounter(), 1);
		assertEquals(ri.getRedirectionReason(), 6);
	}

	@Test(groups = { "functional.encode","isup"})
	public void testEncode() throws Exception {

		RedirectionInformationInapImpl elem = new RedirectionInformationInapImpl(this.getIntData());
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 30);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

		RedirectionInformation ri = new RedirectionInformationImpl(3, 0, 1, 6);
		elem = new RedirectionInformationInapImpl(ri);
		aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 30);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));
		
//		int redirectingIndicator, int originalRedirectionReason, int redirectionCounter, int redirectionReason
	}
}
