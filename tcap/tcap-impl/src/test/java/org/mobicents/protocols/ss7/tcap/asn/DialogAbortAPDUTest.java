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

package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
@Test(groups = { "asn" })
public class DialogAbortAPDUTest {

	private byte[] getData() {
		return new byte[] { 100, 3, (byte) 128, 1, 0 };
	}

	private byte[] getData2() {
		return new byte[] { 100, 21, (byte) 128, 1, 1, (byte) 190, 16, 40, 14, 6, 7, 4, 0, 0, 1, 1, 1, 1, (byte) 160, 3, 1, 2, 3 };
	}

	@Test(groups = { "functional.decode" })
	public void testDecode() throws IOException, ParseException, AsnException {

		byte[] b = getData();
		AsnInputStream asnIs = new AsnInputStream(b);
		int tag = asnIs.readTag();
		assertEquals(4, tag);
		DialogAbortAPDU d = TcapFactory.createDialogAPDUAbort();
		d.decode(asnIs);
		AbortSource as = d.getAbortSource();
		assertEquals(AbortSourceType.User, as.getAbortSourceType());
		UserInformation ui = d.getUserInformation();
		assertNull(ui);

		AsnOutputStream aos = new AsnOutputStream();
		d.encode(aos);
		assertTrue(Arrays.equals(b, aos.toByteArray()));

		
		b = getData2();
		asnIs = new AsnInputStream(b);
		tag = asnIs.readTag();
		assertEquals(4, tag);
		d = TcapFactory.createDialogAPDUAbort();
		d.decode(asnIs);
		as = d.getAbortSource();
		assertEquals(AbortSourceType.Provider, as.getAbortSourceType());
		ui = d.getUserInformation();
		assertNotNull(ui);
		assertTrue(Arrays.equals(new byte[] { 1, 2, 3 }, ui.getEncodeType()));

		aos = new AsnOutputStream();
		d.encode(aos);
		assertTrue(Arrays.equals(b, aos.toByteArray()));
	}
}
