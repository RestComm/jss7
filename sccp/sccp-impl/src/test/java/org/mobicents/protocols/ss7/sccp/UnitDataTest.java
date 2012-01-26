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

package org.mobicents.protocols.ss7.sccp;


import java.io.ByteArrayInputStream;

import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.UnitDataImpl;
import org.mobicents.protocols.ss7.sccp.message.UnitData;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author amit bhayani
 * 
 */
public class UnitDataTest {

	private MessageFactoryImpl messageFactory = new MessageFactoryImpl(false);

	@BeforeMethod
	public void setUp() {

	}

	@AfterMethod
	public void tearDown() {
	}

	@Test(groups = { "udts", "functional.decode",})
	public void testDecode() throws Exception {
		// This is data comes from Dialogic MTU test sending the SMS message
		byte[] b = new byte[] { 0x01, 0x03, 0x05, 0x09, 0x02, 0x42, 0x08, 0x04, 0x43, 0x01, 0x00, 0x08, 0x5D, 0x62,
				0x5B, 0x48, 0x04, 0x00, 0x02, 0x00, 0x30, 0x6B, 0x1A, 0x28, 0x18, 0x06, 0x07, 0x00, 0x11, (byte) 0x86,
				0x05, 0x01, 0x01, 0x01, (byte) 0xA0, 0x0D, 0x60, 0x0B, (byte) 0xA1, 0x09, 0x06, 0x07, 0x04, 0x00, 0x00,
				0x01, 0x00, 0x19, 0x02, 0x6C, 0x37, (byte) 0xA1, 0x35, 0x02, 0x01, 0x01, 0x02, 0x01, 0x2E, 0x30, 0x2D,
				(byte) 0x80, 0x05, (byte) 0x89, 0x67, 0x45, 0x23, (byte) 0xF1, (byte) 0x84, 0x06, (byte) 0xA1, 0x21,
				0x43, 0x65, (byte) 0x87, (byte) 0xF9, 0x04, 0x1C, 0x2C, 0x09, 0x04, 0x21, 0x43, 0x65, (byte) 0x87,
				(byte) 0xF9, 0x04, 0x00, 0x11, 0x30, (byte) 0x92, 0x60, 0x60, 0x62, 0x00, 0x0B, (byte) 0xC8, 0x32,
				(byte) 0x9B, (byte) 0xFD, 0x06, 0x5D, (byte) 0xDF, 0x72, 0x36, 0x19 };

		UnitDataImpl testObjectDecoded = (UnitDataImpl) messageFactory.createMessage(UnitData.MESSAGE_TYPE,
				new ByteArrayInputStream(b));
		System.out.println(testObjectDecoded);
		assertNotNull(testObjectDecoded);

		SccpAddress calledAdd = testObjectDecoded.getCalledPartyAddress();
		assertNotNull(calledAdd);
		assertEquals( calledAdd.getSubsystemNumber(),8);
		assertNull(calledAdd.getGlobalTitle());

		SccpAddress callingAdd = testObjectDecoded.getCallingPartyAddress();
		assertNotNull(callingAdd);
		assertEquals( callingAdd.getSignalingPointCode(),1);
		assertEquals( callingAdd.getSubsystemNumber(),8);
		assertNull(callingAdd.getGlobalTitle());

	}

	@Test(groups = { "udts", "functional.decode",})
	public void testDecode1() throws Exception {
		// This is data comes from Dialogic MTU test
		byte[] b = new byte[] { 0x00, 0x03, 0x05, 0x09, 0x02, 0x42, 0x01, 0x04, 0x43, 0x01, 0x00, 0x01, 0x05, 0x03,
				0x08, 0x02, 0x00, 0x00 };

		UnitDataImpl testObjectDecoded = (UnitDataImpl) messageFactory.createMessage(UnitData.MESSAGE_TYPE,
				new ByteArrayInputStream(b));
		System.out.println(testObjectDecoded);
		assertNotNull(testObjectDecoded);

		SccpAddress calledAdd = testObjectDecoded.getCalledPartyAddress();
		assertNotNull(calledAdd);
		assertEquals( calledAdd.getSubsystemNumber(),1);
		assertNull(calledAdd.getGlobalTitle());

		SccpAddress callingAdd = testObjectDecoded.getCallingPartyAddress();
		assertNotNull(callingAdd);
		assertEquals( callingAdd.getSignalingPointCode(),1);
		assertEquals( callingAdd.getSubsystemNumber(),1);
		assertNull(callingAdd.getGlobalTitle());

	}
}
