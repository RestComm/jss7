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

package org.mobicents.protocols.ss7.map.dialog;

import java.util.Arrays;

import static org.testng.Assert.*;

import org.testng.*;import org.testng.annotations.*;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextNameImpl;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPRefuseInfoTest  {

	private byte[] getData() {
		return new byte[] { (byte) 0xA3, 0x03, (byte) 0x0A, 0x01, 0x00 };
	}

	private byte[] getDataFull() {
		return new byte[] { -93, 51, 10, 1, 2, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21,
				22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 6, 5, 42, 3, 4, 5, 6 };
	}
	
	@Test(groups = { "functional.decode","dialog"})  
	public void testDecode() throws Exception {
		// The raw data is from last packet of long ussd-abort from msc2.txt
		byte[] data = this.getData();

		AsnInputStream asnIs = new AsnInputStream(data);

		int tag = asnIs.readTag();
		assertEquals( tag,3);

		MAPRefuseInfoImpl mapRefuseInfoImpl = new MAPRefuseInfoImpl();
		mapRefuseInfoImpl.decodeAll(asnIs);

		Reason reason = mapRefuseInfoImpl.getReason();

		assertNotNull(reason);

		assertEquals( reason,Reason.noReasonGiven);

		
		data = this.getDataFull();
		asnIs = new AsnInputStream(data);

		tag = asnIs.readTag();
		assertEquals( tag,3);

		mapRefuseInfoImpl = new MAPRefuseInfoImpl();
		mapRefuseInfoImpl.decodeAll(asnIs);

		reason = mapRefuseInfoImpl.getReason();
		assertNotNull(reason);
		assertEquals( reason,Reason.invalidOriginatingReference);
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mapRefuseInfoImpl.getExtensionContainer()));
		assertNotNull(mapRefuseInfoImpl.getAlternativeAcn());
		assertTrue(Arrays.equals(new long[] { 1, 2, 3, 4, 5, 6 }, mapRefuseInfoImpl.getAlternativeAcn().getOid()));
	}

	@Test(groups = { "functional.encode","dialog"})
	public void testEncode() throws Exception {

		MAPRefuseInfoImpl mapRefuseInfoImpl = new MAPRefuseInfoImpl();
		mapRefuseInfoImpl.setReason(Reason.noReasonGiven);
		AsnOutputStream asnOS = new AsnOutputStream();
		mapRefuseInfoImpl.encodeAll(asnOS);
		byte[] data = asnOS.toByteArray();
		assertTrue(Arrays.equals(this.getData(), data));

		
		mapRefuseInfoImpl = new MAPRefuseInfoImpl();
		mapRefuseInfoImpl.setReason(Reason.invalidOriginatingReference);
		ApplicationContextName acn = new ApplicationContextNameImpl();
		acn.setOid(new long[] { 1, 2, 3, 4, 5, 6 });
		mapRefuseInfoImpl.setAlternativeAcn(acn);
		mapRefuseInfoImpl.setExtensionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());
		asnOS = new AsnOutputStream();
		mapRefuseInfoImpl.encodeAll(asnOS);
		data = asnOS.toByteArray();
		assertTrue(Arrays.equals(this.getDataFull(), data));

	}

}
