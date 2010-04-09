/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * @author baranowb
 * 
 */
public class ResultImpl implements Result {

	private ResultType resultType;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.Result#getResultType()
	 */
	public ResultType getResultType() {

		return resultType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Result#setResultType(org.mobicents
	 * .protocols.ss7.tcap.asn.ResultType)
	 */
	public void setResultType(ResultType t) {
		this.resultType = t;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols
	 * .asn.AsnInputStream)
	 */
	public void decode(AsnInputStream ais) throws ParseException {

		int len;
		try {
			len = ais.readLength();

			if (len > ais.available()) {
				throw new ParseException("Not enough data: " + ais.available());
			}

			int tag = ais.readTag();
			if (tag != Tag.INTEGER) {
				throw new ParseException("Expected INTEGER tag, found: " + tag);

			}
			// y, its a bit of enum, should be ok to cast :)
			long t = ais.readInteger();
			this.resultType = ResultType.RejectedPermanent.getFromInt(t);
		} catch (IOException e) {
			throw new ParseException(e);
		} catch (AsnException e) {
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
		if (resultType == null) {
			throw new ParseException("No type set!");
		}
		try {
			AsnOutputStream localAos = new AsnOutputStream();
			localAos.writeInteger(this.resultType.getType());
			byte[] b = localAos.toByteArray();
			aos.writeTag(_TAG_CLASS, _TAG_PC_PRIMITIVE, _TAG);
			aos.writeLength(b.length);
			aos.write(b);
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

}
