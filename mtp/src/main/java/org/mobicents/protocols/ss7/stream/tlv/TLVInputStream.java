/**
 * 
 */
package org.mobicents.protocols.ss7.stream.tlv;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;



/**
 * Simple stream class to decode mtp as TLV like(optional part, always tagged)
 * @author baranowb
 *
 */
public class TLVInputStream extends FilterInputStream{
	private int tagClass;
	private int pCBit;
	private int tag;
	private int len;
	public TLVInputStream(InputStream in) {
		super(in);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public int read() throws IOException {
		int i = super.read();
		if (i == -1) {
			throw new EOFException("AsnInputStream has reached the end");
		}

		return i;
	}
	
	
	//Access to bits
	/**
	 * @return the tagClass
	 */
	public int getTagClass() {
		return tagClass;
	}

	/**
	 * @return the pCBit
	 */
	public boolean isTagPrimitive() {
		return pCBit == Tag.PC_PRIMITIVITE;
	}
	
	public int readTag() throws IOException {
		// Tag tag = null;
		byte b = (byte) this.read();

		tagClass = (b & Tag.CLASS_MASK) >> 6;
		pCBit = (b & Tag.PC_MASK) >> 5;

		tag = b & Tag.TAG_MASK;
		

		// For larger tag values, the first octet has all ones in bits 5 to 1, and the tag value is then encoded in
		// as many following octets as are needed, using only the least significant seven bits of each octet,
		// and using the minimum number of octets for the encoding. The most significant bit (the "more"
		// bit) is set to 1 in the first following octet, and to zero in the last.
		if (tag == Tag.TAG_MASK) {
			byte temp;
			tag = 0;
			do {
				temp = (byte) this.read();
				tag = (tag << 7) | (0x7F & temp);
			} while (0 != (0x80 & temp));
		}

		// tag = new Tag(tagClass, (pCBit == 0 ? true : false), value);
		return tag;
	}
	
	
	
	public int readLength() throws IOException {
		len = 0;

		byte b = (byte) this.read();

		// This is short form. The short form can be used if the number of octets in the Value part is less than or
		// equal to 127, and can be used whether the Value part is primitive or constructed. This form is identified by
		// encoding bit 8 as zero, with the length count in bits 7 to 1 (as usual, with bit 7 the most significant bit
		// of the length).
		if ((b & 0x80) == 0) {
			return b;
		}

		// This is indefinite form. The indefinite form of length can only be used (but does not have to be) if the V
		// part is constructed, that
		// is to say, consists of a series of TLVs. In the indefinite form of length the first bit of the first octet is
		// set to 1, as for the long form, but the value N is set to zero.
		b = (byte) (b & 0x7F);
		if (b == 0) {
			return 0x80;
		}

		// If bit 8 of the first length octet is set to 1, then we have the long form of length. In long form, the first
		// octet encodes in its remaining seven bits a value N which is the length of a series of octets that themselves
		// encode the length of the Value part.
		byte temp;
		for (int i = 0; i < b; i++) {
			temp = (byte) this.read();
			len = (len << 8) | (0xFF & temp);
		}

		return len;
	}
	//support only those two.
	
	public LinkStatus readLinkStatus() throws IOException
	{
		//len == 1;
		len=this.readLength();
		if(len != 1)
		{
			throw new IOException("Wrong link status length: "+len);
		}
		if(this.available()<1)
		{
			throw new IOException("Not enough data");
		}
		byte b = (byte) read();
		LinkStatus ls = LinkStatus.LinkDown.getFromByte(b);
		return ls;
	}
	public byte[] readLinkData() throws IOException 
	{
		len = this.readLength();
		if(len == 0x80)
		{
			//its undefined LEN...
			throw new UnsupportedOperationException("Undefined len is not supported!");
		}

		byte[] data = new byte[len];
		int read = super.read(data);
		if(read != data.length)
		{
			throw new IOException("Not enough data: "+read+", expected: "+data.length);
		}
		return data;
	}
	
}
