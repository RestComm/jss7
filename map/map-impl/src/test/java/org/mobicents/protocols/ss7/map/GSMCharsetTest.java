package org.mobicents.protocols.ss7.map;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;

import junit.framework.TestCase;

/**
 * 
 * @author amit bhayani
 * 
 */
public class GSMCharsetTest extends TestCase {

	@org.junit.Test
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

		assertEquals(ussdString, s1);

	}

	@org.junit.Test
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

	@org.junit.Test
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

		assertEquals(ussdString, s1);

	}
	
	@org.junit.Test
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
}
