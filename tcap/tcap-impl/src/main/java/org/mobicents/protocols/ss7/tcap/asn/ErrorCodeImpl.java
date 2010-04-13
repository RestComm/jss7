/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCodeType;

/**
 * @author baranowb
 * 
 */
public class ErrorCodeImpl implements ErrorCode {

	private ErrorCodeType errorType;
	private byte[] data;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode#getData()
	 */
	public byte[] getData() {

		return this.data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode#getErrorType()
	 */
	public ErrorCodeType getErrorType() {

		return this.errorType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode#setData(byte[])
	 */
	public void setData(byte[] d) {
		this.data = d;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode#setErrorType(org.
	 * mobicents.protocols.ss7.tcap.asn.comp.ErrorCodeType)
	 */
	public void setErrorType(ErrorCodeType t) {

		this.errorType = t;
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
			int len = ais.readLength();
			this.data = new byte[len];
			if (len != ais.read(data)) {
				throw new ParseException("Not enough data read.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		if (errorType == null) {
			throw new ParseException("No error type set!");
		}
		if (data == null) {
			throw new ParseException("No data set!");
		}
		try {
			if (errorType == ErrorCodeType.Local) {
				aos.writeTag(_TAG_CLASS, _TAG_PRIMITIVE, _TAG_LOCAL);
			} else {
				aos.writeTag(_TAG_CLASS, _TAG_PRIMITIVE, _TAG_GLOBAL);
			}

			aos.writeLength(data.length);

			aos.write(data);
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

}
