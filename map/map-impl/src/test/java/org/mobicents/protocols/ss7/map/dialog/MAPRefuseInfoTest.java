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

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPRefuseInfoTest extends TestCase {

	@org.junit.Test
	public void testDecode() throws Exception {
		// The raw data is from last packet of long ussd-abort from msc2.txt
		byte[] data = new byte[] { (byte) 0xA3, 0x03, (byte) 0x0A, 0x01, 0x00 };

		ByteArrayInputStream baIs = new ByteArrayInputStream(data);
		AsnInputStream asnIs = new AsnInputStream(baIs);

		int tag = asnIs.readTag();

		MAPRefuseInfoImpl mapRefuseInfoImpl = new MAPRefuseInfoImpl();
		mapRefuseInfoImpl.decode(asnIs);

		Reason reason = mapRefuseInfoImpl.getReason();

		assertNotNull(reason);

		assertEquals(Reason.noReasonGiven, reason);
	}

	@org.junit.Test
	public void testEncode() throws Exception {

		MAPRefuseInfoImpl mapRefuseInfoImpl = new MAPRefuseInfoImpl();
		mapRefuseInfoImpl.setReason(Reason.noReasonGiven);

		AsnOutputStream asnOS = new AsnOutputStream();

		mapRefuseInfoImpl.encode(asnOS);

		byte[] data = asnOS.toByteArray();

		// System.out.println(dump(data, data.length, false));

		assertTrue(Arrays.equals(new byte[] { (byte) 0xA3, 0x03, (byte) 0x0A,
				0x01, 0x00 }, data));

	}

}
