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

import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author amit bhayani
 * 
 */
public class GSMCharsetTest  {

	@Test(groups = { "functional.decode","gsm"}) //anything better than "gsm"
	public void testDecode1() throws Exception {

		// This raw data is from nad1053.pcap, 2nd packet.
		byte[] data = new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac,
				(byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e, 0x37,
				(byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 };

		// The USSD String represented by above raw data
		String ussdString = "*125*+31628839999#";

		GSMCharset cs = new GSMCharset("GSM", new String[] {});

		ByteBuffer bb = ByteBuffer.wrap(data);

		CharBuffer bf = cs.decode(bb);

		String s1 = bf.toString();

		assertEquals( s1,ussdString);

	}

	@Test(groups = { "functional.encode","gsm"})
	public void testEncode1() throws Exception {

		// This raw data is from nad1053.pcap, 2nd packet.
		byte[] rawData = new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac,
				(byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e, 0x37,
				(byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 };

		String ussdString = "*125*+31628839999#";

		GSMCharset cs = new GSMCharset("GSM", new String[] {});
		ByteBuffer bb = cs.encode(ussdString);

		// Not using bb.array() as it also includes the abytes beyond limit till
		// capacity
		byte[] data = new byte[bb.limit()];
		int count = 0;
		while (bb.hasRemaining()) {
			data[count++] = bb.get();
		}

		assertTrue(Arrays.equals(rawData, data));

	}

	@Test(groups = { "functional.decode","gsm"})
	public void testDecode2() throws Exception {

		// This raw data is from nad1053.pcap, last packet.
		byte[] data = new byte[] { (byte) 0xd9, (byte) 0x77, 0x1d, 0x44,
				(byte) 0x7e, (byte) 0xbb, 0x41, 0x74, 0x10, 0x3a, 0x6c, 0x2f,
				(byte) 0x83, (byte) 0xca, (byte) 0xee, 0x77, (byte) 0xfd,
				(byte) 0x8c, 0x06, (byte) 0x8d, (byte) 0xe5, 0x65, 0x72,
				(byte) 0x9a, 0x0e, (byte) 0xa2, (byte) 0xbf, 0x41, (byte) 0xe3,
				0x30, (byte) 0x9b, 0x0d, (byte) 0xa2, (byte) 0xa3, (byte) 0xd3,
				0x73, (byte) 0x90, (byte) 0xbb, (byte) 0xde, 0x16, (byte) 0x97,
				(byte) 0xe5, 0x2e, 0x10 };

		// The USSD String represented by above raw data
		String ussdString = "You don t have enough credit to call this number. ";

		GSMCharset cs = new GSMCharset("GSM", new String[] {});

		ByteBuffer bb = ByteBuffer.wrap(data);

		CharBuffer bf = cs.decode(bb);

		String s1 = bf.toString();

		assertEquals( s1,ussdString);

	}
	
	@Test(groups = { "functional.encode","gsm"})
	public void testEncode2() throws Exception {

		// This raw data is from nad1053.pcap, last packet.
		byte[] rawData = new byte[] { (byte) 0xd9, (byte) 0x77, 0x1d, 0x44,
				(byte) 0x7e, (byte) 0xbb, 0x41, 0x74, 0x10, 0x3a, 0x6c, 0x2f,
				(byte) 0x83, (byte) 0xca, (byte) 0xee, 0x77, (byte) 0xfd,
				(byte) 0x8c, 0x06, (byte) 0x8d, (byte) 0xe5, 0x65, 0x72,
				(byte) 0x9a, 0x0e, (byte) 0xa2, (byte) 0xbf, 0x41, (byte) 0xe3,
				0x30, (byte) 0x9b, 0x0d, (byte) 0xa2, (byte) 0xa3, (byte) 0xd3,
				0x73, (byte) 0x90, (byte) 0xbb, (byte) 0xde, 0x16, (byte) 0x97,
				(byte) 0xe5, 0x2e, 0x10 };

		String ussdString = "You don t have enough credit to call this number. ";

		GSMCharset cs = new GSMCharset("GSM", new String[] {});
		ByteBuffer bb = cs.encode(ussdString);

		// Not using bb.array() as it also includes the bytes beyond limit till
		// capacity
		byte[] data = new byte[bb.limit()];
		int count = 0;
		while (bb.hasRemaining()) {
			data[count++] = bb.get();
		}

		assertTrue(Arrays.equals(rawData, data));

	}
	
	@Test(groups = { "functional.decode","gsm"})
	public void testDecode3() throws Exception {

		byte[] data = new byte[] { 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, 0x6e, 0x72,
				(byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65 };

		// The USSD String represented by above raw data
		String ussdString = "ndmgapp2ndmgapp2";

		GSMCharset cs = new GSMCharset("GSM", new String[] {});

		ByteBuffer bb = ByteBuffer.wrap(data);

		CharBuffer bf = cs.decode(bb);

		String s1 = bf.toString();

		assertEquals( s1,ussdString);

	}
	
	@Test(groups = { "functional.encode","gsm"})
	public void testEncode3() throws Exception {

		// This raw data is from nad1053.pcap, last packet.
		byte[] rawData = new byte[] { 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65, 0x6e, 0x72,
				(byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65 };

		String ussdString = "ndmgapp2ndmgapp2";

		GSMCharset cs = new GSMCharset("GSM", new String[] {});
		ByteBuffer bb = cs.encode(ussdString);

		// Not using bb.array() as it also includes the bytes beyond limit till
		// capacity
		byte[] data = new byte[bb.limit()];
		int count = 0;
		while (bb.hasRemaining()) {
			data[count++] = bb.get();
		}

		assertTrue(Arrays.equals(rawData, data));
		
	}
	
	@Test(groups = { "functional.decode","gsm"})
	public void testDecode4() throws Exception {

		byte[] data = new byte[] { 0x2a, 0x1c, 0x6e, (byte)0xd4 };

		// The USSD String represented by above raw data
		String ussdString = "*88#";

		GSMCharset cs = new GSMCharset("GSM", new String[] {});

		ByteBuffer bb = ByteBuffer.wrap(data);

		CharBuffer bf = cs.decode(bb);

		String s1 = bf.toString();

		assertEquals( s1,ussdString);

	}
	
	//@Test(groups = { "functional.encode","gsm"})
	//TODO : This fails
//	public void testEncode4() throws Exception {
//
//		// This raw data is from nad1053.pcap, last packet.
//		byte[] rawData = new byte[] { 0x2a, 0x1c, 0x6e, (byte)0xd4 };
//
//		String ussdString = "*88#";
//
//		GSMCharset cs = new GSMCharset("GSM", new String[] {});
//		ByteBuffer bb = cs.encode(ussdString);
//
//		// Not using bb.array() as it also includes the bytes beyond limit till
//		// capacity
//		byte[] data = new byte[bb.limit()];
//		int count = 0;
//		while (bb.hasRemaining()) {
//			data[count++] = bb.get();
//		}
//
//		assertTrue( data),Arrays.equals(rawData);
//
//	}
}
