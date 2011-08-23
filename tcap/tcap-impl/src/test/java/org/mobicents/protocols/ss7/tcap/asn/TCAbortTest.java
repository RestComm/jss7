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

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.TCAPTestUtils;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class TCAbortTest extends TestCase {

	private byte[] getDataDialogPort() {
		return new byte[] { 0x67, 0x2E, 0x49, 0x04, 0x7B, (byte) 0xA5, 0x34, 0x13, 0x6B, 0x26, 0x28, 0x24, 0x06, 0x07, 0x00, 0x11, (byte) 0x86, 0x05, 0x01,
				0x01, 0x01, (byte) 0xA0, 0x19, 0x64, 0x17, (byte) 0x80, 0x01, 0x00, (byte) 0xBE, 0x12, 0x28, 0x10, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x01,
				0x01, 0x01, (byte) 0xA0, 0x05, (byte) 0xA3, 0x03, 0x0A, 0x01, 0x00 };
	}

	private byte[] getDataAbortCause() {
		return new byte[] { 103, 9, 73, 4, 123, -91, 52, 19, 74, 1, 125 };
	}
	
	@org.junit.Test
	public void testBasicTCAbortTestEncode() throws IOException, ParseException {

		//This Raw data is taken from ussd-abort- from msc2.txt
		byte[] expected = getDataDialogPort();

		TCAbortMessageImpl tcAbortMessage = new TCAbortMessageImpl();
		tcAbortMessage.setDestinationTransactionId(2074424339l);

		DialogPortion dp = TcapFactory.createDialogPortion();
		dp.setUnidirectional(false);
		DialogAbortAPDU dapdu = TcapFactory.createDialogAPDUAbort();
		AbortSource as = TcapFactory.createAbortSource();
		as.setAbortSourceType(AbortSourceType.User);
		dapdu.setAbortSource(as);

		UserInformationImpl userInformation = new UserInformationImpl();
		userInformation.setOid(true);
		userInformation.setOidValue(new long[] { 0, 4, 0, 0, 1, 1, 1, 1 });

		userInformation.setAsn(true);
		userInformation.setEncodeType(new byte[] { (byte) 0xA3, 0x03,
				(byte) 0x0A, 0x01, 0x00 });

		dapdu.setUserInformation(userInformation);

		dp.setDialogAPDU(dapdu);

		tcAbortMessage.setDialogPortion(dp);

		AsnOutputStream aos = new AsnOutputStream();

		tcAbortMessage.encode(aos);

		byte[] data = aos.toByteArray();

		System.out.println(dump(data, data.length, false));
		
		TCAPTestUtils.compareArrays(expected, data);

		
		expected = getDataAbortCause();

		tcAbortMessage = new TCAbortMessageImpl();
		tcAbortMessage.setDestinationTransactionId(2074424339l);
		tcAbortMessage.setPAbortCause(PAbortCauseType.DialogueIdleTimeout);

		aos = new AsnOutputStream();
		tcAbortMessage.encode(aos);
		data = aos.toByteArray();
		
		TCAPTestUtils.compareArrays(expected, data);
		
	}

	@org.junit.Test
	public void testBasicTCAbortTestDecode() throws IOException, ParseException {

		//This Raw data is taken from ussd-abort- from msc2.txt
		byte[] data = getDataDialogPort();

		AsnInputStream ais = new AsnInputStream(data);
		int tag = ais.readTag();
		assertEquals("Expected TCAbort", TCAbortMessage._TAG, tag);

		TCAbortMessageImpl impl = new TCAbortMessageImpl();
		impl.decode(ais);
		
		assertTrue(2074424339 == impl.getDestinationTransactionId());
		
		DialogPortion dp = impl.getDialogPortion();
		
		assertNotNull(dp);
		assertFalse(dp.isUnidirectional());
		
		DialogAPDU dialogApdu = dp.getDialogAPDU();
		
		assertNotNull(dialogApdu);


		data = getDataAbortCause();
		ais = new AsnInputStream(data);
		tag = ais.readTag();
		assertEquals("Expected TCAbort", TCAbortMessage._TAG, tag);

		impl = new TCAbortMessageImpl();
		impl.decode(ais);
		
		assertTrue(2074424339 == impl.getDestinationTransactionId());
		
		dp = impl.getDialogPortion();
		assertNull(dp);
		assertEquals(PAbortCauseType.DialogueIdleTimeout, impl.getPAbortCause());
	}

	public final static String dump(byte[] buff, int size, boolean asBits) {
		String s = "";
		for (int i = 0; i < size; i++) {
			String ss = null;
			if (!asBits) {
				ss = Integer.toHexString(buff[i] & 0xff);
			} else {
				ss = Integer.toBinaryString(buff[i] & 0xff);
			}
			ss = fillInZeroPrefix(ss, asBits);
			s += " " + ss;
		}
		return s;
	}

	public final static String fillInZeroPrefix(String ss, boolean asBits) {
		if (asBits) {
			if (ss.length() < 8) {
				for (int j = ss.length(); j < 8; j++) {
					ss = "0" + ss;
				}
			}
		} else {
			// hex
			if (ss.length() < 2) {

				ss = "0" + ss;
			}
		}

		return ss;
	}

}
