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

import org.testng.annotations.*;

import static org.testng.Assert.*;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
@Test(groups = { "asn" })
public class DialogResponseAPDUTest {

	private byte[] getData() {
		return new byte[] { 97, 44, (byte) 128, 2, 7, (byte) 128, (byte) 161, 9, 6, 7, 4, 0, 0, 1, 0, 21, 2, (byte) 162, 3, 2, 1, 0, (byte) 163, 5, (byte) 161,
				3, 2, 1, 0, (byte) 190, 15, 40, 13, 6, 7, 4, 0, 0, 1, 1, 1, 1, (byte) 160, 2, (byte) 161, 0 };
	}

	private byte[] getData2() {
		return new byte[] { 97, 27, (byte) 128, 2, 7, (byte) 128, (byte) 161, 9, 6, 7, 4, 0, 0, 1, 0, 25, 2, (byte) 162, 3, 2, 1, 1, (byte) 163, 5, (byte) 161,
				3, 2, 1, 2 };
	}

	private byte[] getData3() {
		return new byte[] { 97, 27, (byte) 128, 2, 7, (byte) 128, (byte) 161, 9, 6, 7, 4, 0, 0, 1, 0, 25, 2, (byte) 162, 3, 2, 1, 1, (byte) 163, 5, (byte) 162,
				3, 2, 1, 2 };
	}
	
	@Test(groups = { "functional.encode","functional.decode" })
	public void testResponseAPDU() throws IOException, ParseException, AsnException {

		byte[] b = getData();
		AsnInputStream asnIs = new AsnInputStream(b);
		int tag = asnIs.readTag();
		assertEquals(1, tag);
		DialogResponseAPDU d = TcapFactory.createDialogAPDUResponse();
		d.decode(asnIs);
		assertTrue(Arrays.equals(new long[]{0, 4, 0, 0, 1, 0, 21, 2}, d.getApplicationContextName().getOid()));
		Result r = d.getResult();
		assertEquals(ResultType.Accepted, r.getResultType());		
		ResultSourceDiagnostic diag = d.getResultSourceDiagnostic();
		assertNotNull(diag.getDialogServiceUserType());
		assertEquals(DialogServiceUserType.Null, diag.getDialogServiceUserType());	
		UserInformation ui = d.getUserInformation();
		assertNotNull(ui);
		assertNotNull(ui.getEncodeType());
		ui.getEncodeType();
		assertTrue(Arrays.equals(new byte[] { -95, 0 }, ui.getEncodeType()));

		AsnOutputStream aos = new AsnOutputStream();
		d.encode(aos);
		assertTrue(Arrays.equals(b, aos.toByteArray()));
		
		
		b = getData2();
		asnIs = new AsnInputStream(b);
		tag = asnIs.readTag();
		assertEquals(1, tag);
		d = TcapFactory.createDialogAPDUResponse();
		d.decode(asnIs);
		assertTrue(Arrays.equals(new long[]{0, 4, 0, 0, 1, 0, 25, 2}, d.getApplicationContextName().getOid()));
		r = d.getResult();
		assertEquals(ResultType.RejectedPermanent, r.getResultType());		
		diag = d.getResultSourceDiagnostic();
		assertNotNull(diag.getDialogServiceUserType());
		assertEquals(DialogServiceUserType.AcnNotSupported, diag.getDialogServiceUserType());	
		ui = d.getUserInformation();
		assertNull(ui);
		
		aos = new AsnOutputStream();
		d.encode(aos);
		assertTrue(Arrays.equals(b, aos.toByteArray()));
		
		
		b = getData3();
		asnIs = new AsnInputStream(b);
		tag = asnIs.readTag();
		assertEquals(1, tag);
		d = TcapFactory.createDialogAPDUResponse();
		d.decode(asnIs);
		assertTrue(Arrays.equals(new long[]{0, 4, 0, 0, 1, 0, 25, 2}, d.getApplicationContextName().getOid()));
		r = d.getResult();
		assertEquals(ResultType.RejectedPermanent, r.getResultType());		
		diag = d.getResultSourceDiagnostic();
		assertNotNull(diag.getDialogServiceProviderType());
		assertEquals(DialogServiceProviderType.NoCommonDialogPortion, diag.getDialogServiceProviderType());	
		ui = d.getUserInformation();
		assertNull(ui);
		
		aos = new AsnOutputStream();
		d.encode(aos);
		assertTrue(Arrays.equals(b, aos.toByteArray()));
	}

}
