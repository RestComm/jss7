/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCodeType;

/**
 * @author baranowb
 * 
 */
public class OperationCodeImpl implements OperationCode {

	private Long operationCode;
	private OperationCodeType type;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.OperationCode#getCode()
	 */
	public Long getCode() {

		return operationCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.OperationCode#getOperationType()
	 */
	public OperationCodeType getOperationType() {

		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.OperationCode#setCode(java.lang.
	 * Integer)
	 */
	public void setCode(Long i) {
		operationCode = i;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.OperationCode#setOperationType(org
	 * .mobicents.protocols.ss7.tcap.asn.OperationCodeType)
	 */
	public void setOperationType(OperationCodeType t) {
		this.type = t;

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
			this.operationCode = ais.readInteger();
		} catch (AsnException e) {
			throw new ParseException(e);
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
		if (this.type == null) {
			throw new ParseException("No indication Global|Local.");
		}
		if (this.operationCode == null) {
			throw new ParseException("Operation code not set.");
		}
		try {
			if (type == OperationCodeType.Global) {

				aos.writeInteger(_TAG_CLASS, _TAG_GLOBAL, this.operationCode);

			} else {
				aos.writeInteger(_TAG_CLASS, _TAG_LOCAL, this.operationCode);
			}
		} catch (IOException e) {
			throw new ParseException(e);
		}

	}

}
