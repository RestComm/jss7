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

package org.mobicents.protocols.ss7.m3ua.impl.message;

import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;

import static org.junit.Assert.*;

/**
 * @author amit bhayani
 * 
 */
public class MessageFactoryTest {

	private MessageFactoryImpl messageFactory = new MessageFactoryImpl();

	/**
	 * 
	 */
	public MessageFactoryTest() {
		// TODO Auto-generated constructor stub
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * This test is from Cisco ITP for SST with padding at last
	 */
	@Test
	public void testSst() {
		byte[] data = new byte[] { 0x01, 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x3c, 0x02, 0x00, 0x00, 0x08, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x06, 0x00, 0x08, 0x00, 0x00, 0x00, 0x19, 0x02, 0x10, 0x00, 0x21, 0x00, 0x00, 0x17,
				(byte) 0x9d, 0x00, 0x00, 0x18, 0x1c, 0x03, 0x03, 0x00, 0x02, 0x09, 0x00, 0x03, 0x05, 0x07, 0x02, 0x42,
				0x01, 0x02, 0x42, 0x01, 0x05, 0x03, (byte) 0xd5, 0x1c, 0x18, 0x00, 0x00, 0x00, 0x00 };

		ByteBuffer byteBuffer = ByteBuffer.allocate(256);
		byteBuffer.put(data);
		byteBuffer.flip();
		M3UAMessageImpl messageImpl = messageFactory.createMessage(byteBuffer);

		assertEquals(MessageType.PAYLOAD, messageImpl.getMessageType());
		PayloadData payloadData = (PayloadData) messageImpl;
		assertEquals(0l, payloadData.getNetworkAppearance().getNetApp());
		assertEquals(1, payloadData.getRoutingContext().getRoutingContexts().length);
		assertEquals(25l, payloadData.getRoutingContext().getRoutingContexts()[0]);
		ProtocolData protocolData = payloadData.getData();
		assertNotNull(protocolData);
		assertEquals(6045, protocolData.getOpc());
		assertEquals(6172, protocolData.getDpc());
		assertEquals(3, protocolData.getSI());
		assertEquals(2, protocolData.getSLS());
		assertEquals(3, protocolData.getNI());
		assertEquals(0, protocolData.getMP());

	}

}
