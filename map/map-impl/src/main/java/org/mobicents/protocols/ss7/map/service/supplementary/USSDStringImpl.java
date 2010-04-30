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
