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

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MAPSimpleDialogTest extends TestCase  {

	private byte[] getDataAcceptInfo() {
		return new byte[] { -95, 41, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
				24, 25, 26, -95, 3, 31, 32, 33 };
	}

	private byte[] getDataCloseInfo() {
		return new byte[] { -94, 41, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
				24, 25, 26, -95, 3, 31, 32, 33 };
	}
	
	@org.junit.Test
	public void testDecode() throws Exception {

		AsnInputStream asnIs = new AsnInputStream(this.getDataAcceptInfo());

		int tag = asnIs.readTag();
		assertEquals(1, tag);

		MAPAcceptInfoImpl accInfo = new MAPAcceptInfoImpl();
		accInfo.decodeAll(asnIs);
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(accInfo.getExtensionContainer()));

		
		asnIs = new AsnInputStream(this.getDataCloseInfo());

		tag = asnIs.readTag();
		assertEquals(2, tag);

		MAPCloseInfoImpl closeInfo = new MAPCloseInfoImpl();
		closeInfo.decodeAll(asnIs);
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(closeInfo.getExtensionContainer()));
		
	}

	@org.junit.Test
	public void testEncode() throws Exception {

		byte[] b = this.getDataAcceptInfo();
		MAPAcceptInfoImpl accInfo = new MAPAcceptInfoImpl();
		accInfo.setExtensionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		AsnOutputStream asnOS = new AsnOutputStream();
		accInfo.encodeAll(asnOS);
		byte[] data = asnOS.toByteArray();
		assertTrue(Arrays.equals(b, data));

		
		b = this.getDataCloseInfo();
		MAPCloseInfoImpl closeInfo = new MAPCloseInfoImpl();
		closeInfo.setExtensionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		asnOS = new AsnOutputStream();
		closeInfo.encodeAll(asnOS);
		data = asnOS.toByteArray();
		assertTrue(Arrays.equals(b, data));
	}
	
}
