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
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.BitSet;

/**
 * 
 * @author amit bhayani
 * 
 */
public class GSMCharsetEncoder extends CharsetEncoder {

	int bitpos = 0;
	byte carryOver;

	// The mask to check if corresponding bit in read byte is 1 or 0 and hence
	// store it i BitSet accordingly
	byte[] mask = new byte[] { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40 };

	// BitSet to hold the bits of passed char to be encoded
	BitSet bitSet = new BitSet();

	static final byte ESCAPE = 0x1B;

	protected GSMCharsetEncoder(Charset cs, float averageBytesPerChar,
			float maxBytesPerChar) {
		super(cs, averageBytesPerChar, maxBytesPerChar);
		implReset();
	}

	@Override
	protected void implReset() {
		bitpos = 0;
		carryOver = 0;
		bitSet.clear();
	}

	/**
	 * TODO :
	 */
	@Override
	protected CoderResult implFlush(ByteBuffer out) {

		if (!out.hasRemaining()) {
			return CoderResult.OVERFLOW;
		}
		return CoderResult.UNDERFLOW;
	}

	byte rawData = 0;

	@Override
	protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
		while (in.hasRemaining()) {

			// Read the first char
			char c = in.get();

			for (int i = 0; i < GSMCharset.BYTE_TO_CHAR.length; i++) {

				// Get the index of BYTE_TO_CHAR where this char is present.
				if (GSMCharset.BYTE_TO_CHAR[i] == c) {

					// The index represents the byte for us, from which least
					// significant 7 bits are to be consumed
					rawData = (byte) i;

					for (int j = 0; j < mask.length; j++) {
						if ((rawData & mask[j]) == mask[j]) {
							bitSet.set(bitpos);
						}
						bitpos++;
					}

					break;
				}// end of if(GSMCharset.BYTE_TO_CHAR[i] == c)

				// TODO : What if we get char that doesn't match? Throw error?
				// Or ignore like we are doing now?
			}
		}

		// All the char's are read and corresponding BitSet also filled. Now
		// each 7 Bits forms one byte and to be added to ByteBuffer out
		int b = 0x00;
		for (int count = 0; count < bitpos; count++) {

			// If 7 bits are read, add it to ByteBuffer.
			if (count > 0 && (count % 8) == 0) {
				out.put((byte) (b & 0xFF));

				// reset previous byte for next byte formation
				b = 0x00;
			}

			// Formation of byte. Keep moving each bit to left and append
			// current bit
			if (bitSet.get(count)) {
				b = (b | 1 << (count % 8));
			}
		}// end of For loop

		// The final one if total bits are not LCM of 8
		//if (bitpos % 8 != 0) {
			out.put((byte) (b & 0xFF));
		//}

		return CoderResult.UNDERFLOW;
	}

}
