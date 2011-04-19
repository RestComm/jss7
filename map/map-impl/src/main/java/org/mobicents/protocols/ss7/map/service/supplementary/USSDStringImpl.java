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

package org.mobicents.protocols.ss7.map.service.supplementary;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.mobicents.protocols.ss7.map.GSMCharset;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;

/**
 * 
 * @author amit bhayani
 * 
 */
public class USSDStringImpl implements USSDString {

	private String ussdString;
	private byte[] encodedString;

	private Charset charset;

	public USSDStringImpl(String ussdString, Charset charset) {
		this.ussdString = ussdString;
		this.charset = charset;

		// set to default if not set by user
		if (this.charset == null) {
			this.charset = new GSMCharset("GSM", new String[] {});
		}
	}

	public USSDStringImpl(byte[] encodedString, Charset charset) {
		this.encodedString = encodedString;
		this.charset = charset;

		// set to default if not set by user
		if (this.charset == null) {
			this.charset = new GSMCharset("GSM", new String[] {});
		}
	}

	public byte[] getEncodedString() {
		return this.encodedString;
	}

	public String getString() {
		return this.ussdString;
	}
	
	public Charset getCharset() {
		return this.charset;
	}


	public void decode() throws MAPException {

		if (this.encodedString == null) {
			throw new MAPException("encodedString byte[] is null");
		}

		ByteBuffer bb = ByteBuffer.wrap(this.encodedString);

		CharBuffer bf = this.charset.decode(bb);

		this.ussdString = bf.toString();

	}

	public void encode() throws MAPException {
		if (this.ussdString == null) {
			throw new MAPException("USSD String is null");
		}

		// set to default if not set by user
		if (charset == null) {
			charset = new GSMCharset("GSM", new String[] {});
		}

		ByteBuffer bb = this.charset.encode(ussdString);

		// Not using bb.array() as it also includes the bytes beyond limit till
		// capacity
		encodedString = new byte[bb.limit()];
		int count = 0;
		while (bb.hasRemaining()) {
			encodedString[count++] = bb.get();
		}
	}

}
