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

package org.mobicents.protocols.ss7.map.primitives;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.GSMCharset;
import org.mobicents.protocols.ss7.map.GSMCharsetDecoder;
import org.mobicents.protocols.ss7.map.GSMCharsetDecodingData;
import org.mobicents.protocols.ss7.map.GSMCharsetEncoder;
import org.mobicents.protocols.ss7.map.GSMCharsetEncodingData;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;

/**
 * 
 * @author amit bhayani
 * 
 */
public class USSDStringImpl implements USSDString, MAPAsnPrimitive {

	private String ussdString;
	private byte[] encodedString;

	public static final String _PrimitiveName = "USSDString";

	//TODO : Should Charset be serializable?
	private transient Charset charset;

	/**
	 * 
	 */
	public USSDStringImpl() {
		super();
	}

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

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
	 */
	public int getTag() throws MAPException {
		return Tag.STRING_OCTET;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass()
	 */
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive()
	 */
	public boolean getIsPrimitive() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll(org.mobicents.protocols.asn.AsnInputStream)
	 */
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding ReportSMDeliveryStatusRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding ReportSMDeliveryStatusRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData(org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding ReportSMDeliveryStatusRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding ReportSMDeliveryStatusRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}		
	}
	
	private void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		this.encodedString = asnIS.readOctetStringData(length);
		
		ByteBuffer bb = ByteBuffer.wrap(this.encodedString);

		// set to default if not set by user
		if (charset == null) {
			charset = new GSMCharset("GSM", new String[] {});
		}
		
//		CharBuffer bf = this.charset.decode(bb);
//		this.ussdString = bf.toString();
		GSMCharsetDecoder decoder = (GSMCharsetDecoder) this.charset.newDecoder();
		decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData());		
		CharBuffer bf = null;
		try {
			bf = decoder.decode(bb);
		} catch (CharacterCodingException e) {
			// This can not occur
		}
		if (bf != null)
			this.ussdString = bf.toString();
		else
			this.ussdString = "";
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET);		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll(org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding reportSMDeliveryStatusRequest: " + e.getMessage(), e);
		}		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData(org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		if (this.ussdString == null) {
			throw new MAPException("Error while encoding USSDString the mandatory USSDString is not defined");
		}
		
//		ByteBuffer bb = this.charset.encode(ussdString);
//		// Not using bb.array() as it also includes the bytes beyond limit till
//		// capacity
//		encodedString = new byte[bb.limit()];
//		int count = 0;
//		while (bb.hasRemaining()) {
//			encodedString[count++] = bb.get();
//		}

		GSMCharsetEncoder encoder = (GSMCharsetEncoder) charset.newEncoder();
		encoder.setGSMCharsetEncodingData(new GSMCharsetEncodingData());
		ByteBuffer bb = null;
		try {
			bb = encoder.encode(CharBuffer.wrap(this.ussdString));
		} catch (Exception e) {
			// This can not occur
		}
		if (bb != null) {
			this.encodedString = new byte[bb.limit()];
			bb.get(this.encodedString);
			asnOs.writeOctetStringData(this.encodedString);
		}		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ussdString == null) ? 0 : ussdString.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		USSDStringImpl other = (USSDStringImpl) obj;
		if (ussdString == null) {
			if (other.ussdString != null)
				return false;
		} else if (!ussdString.equals(other.ussdString))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName);
		sb.append(" [");

		if (this.ussdString != null) {
			sb.append(ussdString);
		}

		sb.append("]");

		return sb.toString();
	}
}

