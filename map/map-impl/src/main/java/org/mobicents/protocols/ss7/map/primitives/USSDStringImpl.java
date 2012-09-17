/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012. 
 * and individual contributors
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
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingGroup;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharset;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetDecoder;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetDecodingData;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetEncoder;
import org.mobicents.protocols.ss7.map.datacoding.GSMCharsetEncodingData;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class USSDStringImpl implements USSDString, MAPAsnPrimitive {

	private String ussdString;
	private byte[] encodedString;
	private CBSDataCodingScheme dataCodingScheme;
	private Charset gsm8Charset;

	public static final String _PrimitiveName = "USSDString";

	private static GSMCharset gsm7Charset = new GSMCharset("GSM", new String[] {});
	private static Charset ucs2Charset = Charset.forName("UTF-16BE");

	/**
	 * 
	 */
	public USSDStringImpl(CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset) {
		super();

		this.dataCodingScheme = dataCodingScheme;
		this.gsm8Charset = gsm8Charset;
	}

	public USSDStringImpl(String ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset) {
		this.ussdString = ussdString;
		this.dataCodingScheme = dataCodingScheme;
		this.gsm8Charset = gsm8Charset;

		// set to default if not set by user
		if (this.dataCodingScheme == null) {
			this.dataCodingScheme = new CBSDataCodingSchemeImpl(15);
		}
	}

	public USSDStringImpl(byte[] encodedString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset) {
		this.encodedString = encodedString;
		this.dataCodingScheme = dataCodingScheme;
		this.gsm8Charset = gsm8Charset;

		// set to default if not set by user
		if (this.dataCodingScheme == null) {
			this.dataCodingScheme = new CBSDataCodingSchemeImpl(15);
		}
	}

	public byte[] getEncodedString() {
		return this.encodedString;
	}

	public String getString() {
		return this.ussdString;
	}

	public CBSDataCodingScheme getDataCodingScheme() {
		return this.dataCodingScheme;
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

		this.ussdString = "";

		if (dataCodingScheme == null) {
			return;
		}		

		if (dataCodingScheme.getIsCompressed()) {
			// TODO: implement the case with compressed sms message
		} else {

			switch (this.dataCodingScheme.getCharacterSet()) {
			case GSM7:
				GSMCharset cSet = gsm7Charset;
				GSMCharsetDecoder decoder = (GSMCharsetDecoder) cSet.newDecoder();
				decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData());		
				ByteBuffer bb = ByteBuffer.wrap(this.encodedString);
				CharBuffer bf = null;
				try {
					bf = decoder.decode(bb);
				} catch (CharacterCodingException e) {
					// This can not occur
				}
				if (bf != null)
					this.ussdString = bf.toString();
				break;

			case GSM8:
				if (gsm8Charset != null) {
					byte[] buf = this.encodedString;
					bb = ByteBuffer.wrap(buf);
					bf = gsm8Charset.decode(bb);
					this.ussdString = bf.toString();
				}
				break;

			case UCS2:
				String pref = "";
				byte[] buf = this.encodedString;
				if (this.dataCodingScheme.getDataCodingGroup() == CBSDataCodingGroup.GeneralWithLanguageIndication) {
					cSet = gsm7Charset;
					decoder = (GSMCharsetDecoder) cSet.newDecoder();
					decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData());
					byte[] buf2 = new byte[3];
					if (this.encodedString.length < 3)
						buf2 = new byte[this.encodedString.length];
					System.arraycopy(this.encodedString, 0, buf2, 0, buf2.length);
					bb = ByteBuffer.wrap(this.encodedString);
					bf = null;
					try {
						bf = decoder.decode(bb);
					} catch (CharacterCodingException e) {
						// This can not occur
					}
					if (bf != null)
						pref = bf.toString();

					if (this.encodedString.length <= 3) {
						buf = new byte[0];
					} else {
						buf = new byte[this.encodedString.length - 3];
						System.arraycopy(this.encodedString, 3, buf, 0, buf.length);
					}
				}

				bb = ByteBuffer.wrap(buf);
				bf = ucs2Charset.decode(bb);
				this.ussdString = bf.toString();
				break;
			}
		}
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
			this.ussdString = "";
		}

		if (this.dataCodingScheme.getIsCompressed()) {
			// TODO: implement the case with compressed message
			throw new MAPException("Error encoding a text in USSDStringImpl: compressed message is not supported yet");
		} else {

			switch (this.dataCodingScheme.getCharacterSet()) {
			case GSM7:
				Charset cSet = gsm7Charset;
				GSMCharsetEncoder encoder = (GSMCharsetEncoder) cSet.newEncoder();
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
				} else
					this.encodedString = new byte[0];
				break;

			case GSM8:
				if (gsm8Charset != null) {
					bb = gsm8Charset.encode(this.ussdString);
					this.encodedString = new byte[bb.limit()];
					bb.get(this.encodedString);
				} else {
					throw new MAPException("Error encoding a text in USSDStringImpl: gsm8Charset is not defined for GSM8 dataCodingScheme");
				}
				break;

			case UCS2:
				if (this.dataCodingScheme.getDataCodingGroup() == CBSDataCodingGroup.GeneralWithLanguageIndication) {
					if (this.ussdString.length() < 1)
						this.ussdString = this.ussdString + " ";
					if (this.ussdString.length() < 2)
						this.ussdString = this.ussdString + " ";
					if (this.ussdString.length() < 3)
						this.ussdString = this.ussdString + "\n";
					cSet = gsm7Charset;
					encoder = (GSMCharsetEncoder) cSet.newEncoder();
					encoder.setGSMCharsetEncodingData(new GSMCharsetEncodingData());
					bb = null;
					try {
						bb = encoder.encode(CharBuffer.wrap(this.ussdString.substring(0, 3)));
					} catch (Exception e) {
						// This can not occur
					}
					byte[] buf1;
					if (bb != null) {
						buf1 = new byte[bb.limit()];
						bb.get(this.encodedString);
						asnOs.writeOctetStringData(this.encodedString);
					} else
						buf1 = new byte[0];

					bb = ucs2Charset.encode(this.ussdString.substring(3));
					this.encodedString = new byte[buf1.length + bb.limit()];
					System.arraycopy(buf1, 0, this.encodedString, 0, buf1.length);
					bb.get(this.encodedString, buf1.length, this.encodedString.length - buf1.length);
				} else {
					bb = ucs2Charset.encode(this.ussdString);
					this.encodedString = new byte[bb.limit()];
					bb.get(this.encodedString);
				}
				break;
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ussdString == null) ? 0 : ussdString.hashCode());
		result = prime * result + ((dataCodingScheme == null) ? 0 : dataCodingScheme.getCode());
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
		if (dataCodingScheme == null) {
			if (other.dataCodingScheme != null)
				return false;
		} else if (dataCodingScheme.getCode() != other.dataCodingScheme.getCode())
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
		if (this.dataCodingScheme != null) {
			sb.append(", dcs=");
			sb.append(dataCodingScheme);
		}

		sb.append("]");

		return sb.toString();
	}
}

