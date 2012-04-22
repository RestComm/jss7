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

package org.mobicents.protocols.ss7.map;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

import static org.testng.Assert.*;

import org.testng.annotations.*;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class GSMCharsetTest  {

	@Test(groups = { "functional.encode","gsm"})
	public void testEncode() throws Exception {

		this.doTestEncode("123456\r", new byte[] { 49, -39, -116, 86, -77, 53, 26 });
		this.doTestEncode("1\r", new byte[] { -79, 6 });
		this.doTestEncode("1", new byte[] { 49 });
		
		// case: adding '\r' instead of '@' - USSD style
		this.doTestEncode("1234567", new byte[] { 49, -39, -116, 86, -77, -35, 26 }); 
		// case: adding double '\r' when '\r' will be removed at the decoder side - USSD style
		this.doTestEncode("1234567\r", new byte[] { 49, -39, -116, 86, -77, -35, 26, 13 });
		
		// This raw data is from nad1053.pcap, 2nd packet.
		this.doTestEncode("*125*+31628839999#", new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e,
				0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 });
		// This raw data is from nad1053.pcap, last packet.
		this.doTestEncode("You don t have enough credit to call this number. ", new byte[] { (byte) 0xd9, (byte) 0x77, 0x1d, 0x44, (byte) 0x7e, (byte) 0xbb,
				0x41, 0x74, 0x10, 0x3a, 0x6c, 0x2f, (byte) 0x83, (byte) 0xca, (byte) 0xee, 0x77, (byte) 0xfd, (byte) 0x8c, 0x06, (byte) 0x8d, (byte) 0xe5,
				0x65, 0x72, (byte) 0x9a, 0x0e, (byte) 0xa2, (byte) 0xbf, 0x41, (byte) 0xe3, 0x30, (byte) 0x9b, 0x0d, (byte) 0xa2, (byte) 0xa3, (byte) 0xd3,
				0x73, (byte) 0x90, (byte) 0xbb, (byte) 0xde, 0x16, (byte) 0x97, (byte) 0xe5, 0x2e, 0x10 });
		// The USSD String represented by above raw data
		this.doTestEncode("ndmgapp2ndmgapp2", new byte[] { 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, 0x6e, 0x72, (byte) 0xfb, 0x1c,
				(byte) 0x86, (byte) 0xc3, 0x65 });
//		this.doTestEncode("*88#", new byte[] { 0x2a, 0x1c, 0x6e, (byte)0xd4 });
		this.doTestEncode("*88#", new byte[] { 0x2a, 0x1c, 0x6e, (byte)0x4 });
	}

	@Test(groups = { "functional.decode","gsm"})
	public void testDecode() throws Exception {

		this.doTestDecode("123456\r", new byte[] { 49, -39, -116, 86, -77, 53, 26 });
		this.doTestDecode("1\r", new byte[] { -79, 6 });
		this.doTestDecode("1", new byte[] { 49 });
		
		this.doTestDecode("1234567", new byte[] { 49, -39, -116, 86, -77, -35, 26 });
		this.doTestDecode("1234567\r\r", new byte[] { 49, -39, -116, 86, -77, -35, 26, 13 });

		// This raw data is from nad1053.pcap, 2nd packet.
		this.doTestDecode("*125*+31628839999#", new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e,
				0x37, (byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 });
		// This raw data is from nad1053.pcap, last packet.
		this.doTestDecode("You don t have enough credit to call this number. ", new byte[] { (byte) 0xd9, (byte) 0x77, 0x1d, 0x44, (byte) 0x7e, (byte) 0xbb,
				0x41, 0x74, 0x10, 0x3a, 0x6c, 0x2f, (byte) 0x83, (byte) 0xca, (byte) 0xee, 0x77, (byte) 0xfd, (byte) 0x8c, 0x06, (byte) 0x8d, (byte) 0xe5,
				0x65, 0x72, (byte) 0x9a, 0x0e, (byte) 0xa2, (byte) 0xbf, 0x41, (byte) 0xe3, 0x30, (byte) 0x9b, 0x0d, (byte) 0xa2, (byte) 0xa3, (byte) 0xd3,
				0x73, (byte) 0x90, (byte) 0xbb, (byte) 0xde, 0x16, (byte) 0x97, (byte) 0xe5, 0x2e, 0x10 });
		// The USSD String represented by above raw data
		this.doTestDecode("ndmgapp2ndmgapp2", new byte[] { 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, 0x6e, 0x72, (byte) 0xfb, 0x1c,
				(byte) 0x86, (byte) 0xc3, 0x65 });
		this.doTestDecode("*88#", new byte[] { 0x2a, 0x1c, 0x6e, (byte)0xd4 });
	}

	private void doTestEncode(String decodedString, byte[] encodedData) throws Exception {

		GSMCharset cs = new GSMCharset("GSM", new String[] {});
		GSMCharsetEncoder encoder = (GSMCharsetEncoder) cs.newEncoder();
		ByteBuffer bb = encoder.encode(CharBuffer.wrap(decodedString));
		byte[] data = new byte[bb.limit()];
		bb.get(data);

		assertTrue(Arrays.equals(encodedData, data));
	}

	private void doTestDecode(String decodedString, byte[] encodedData)
			throws Exception {

		ByteBuffer bb = ByteBuffer.wrap(encodedData);
		GSMCharset cs = new GSMCharset("GSM", new String[] {});
		GSMCharsetDecoder decoder = (GSMCharsetDecoder) cs.newDecoder();
		CharBuffer bf = null;
		bf = decoder.decode(bb);
		String s1 = bf.toString();

		assertTrue(s1.equals(decodedString));
	}
}

