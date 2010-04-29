/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author baranowb
 * 
 */
public class ParameterImpl implements Parameter {

	private byte[] data;
	private Parameter[] parameters;
	private boolean primitive = true;
	private int tag;
	private int tagClass;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Parameter#getData()
	 */
	public byte[] getData() {

		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Parameter#isPrimitive()
	 */
	public boolean isPrimitive() {

		return primitive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.Parameter#setData(byte[])
	 */
	public void setData(byte[] b) {
		this.data = b;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.Parameter#setPrimitive(boolean)
	 */
	public void setPrimitive(boolean b) {

		this.primitive = b;
	}

	/**
	 * @return the tag
	 */
	public int getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(int tag) {
		this.tag = tag;
	}

	/**
	 * @return the tagClass
	 */
	public int getTagClass() {
		return tagClass;
	}

	/**
	 * @param tagClass
	 *            the tagClass to set
	 */
	public void setTagClass(int tagClass) {
		this.tagClass = tagClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols
	 * .asn.AsnInputStream)
	 */
	public void decode(AsnInputStream ais) throws ParseException {
		try {
			primitive = ais.isTagPrimitive();
			tagClass = ais.getTagClass();
			int len;

			len = ais.readLength();

			if (len == 0x80) {
				throw new ParseException("Undefined length is not supported.");
			}
			data = new byte[len];
			int tlen = ais.read(data);
			if (tlen != len) {
				throw new ParseException("Not enough data read, expected: "+len+", actaul: "+tlen);
			}

		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols
	 * .asn.AsnOutputStream)
	 */
	public void encode(AsnOutputStream aos) throws ParseException {
		if (data == null) {
			throw new ParseException("Parameter data not set.");
		}

		aos.writeTag(tagClass, primitive, tag);
		aos.writeLength(data.length);
		try {
			aos.write(data);
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	public Parameter[] getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setParameters(Parameter[] paramss) {
		// TODO Auto-generated method stub
		
	}

}
