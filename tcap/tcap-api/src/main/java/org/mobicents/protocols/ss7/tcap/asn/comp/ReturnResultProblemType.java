/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.ss7.tcap.asn.ParseException;

/**
 * @author baranowb
 * 
 */
public enum ReturnResultProblemType {

	UnrecognizedInvokeID(0), ReturnResultUnexpected(1), MistypedParameter(2);

	ReturnResultProblemType(long l) {
		this.type = l;
	}

	private long type;

	/**
	 * @return the type
	 */
	public long getType() {
		return type;
	}

	public static ReturnResultProblemType getFromInt(long t) throws ParseException {
		if (t == 0) {
			return UnrecognizedInvokeID;
		} else if (t == 1) {
			return ReturnResultUnexpected;
		} else if (t == 2) {
			return MistypedParameter;
		}
		throw new ParseException("Wrong value of type: " + t);
	}
}
