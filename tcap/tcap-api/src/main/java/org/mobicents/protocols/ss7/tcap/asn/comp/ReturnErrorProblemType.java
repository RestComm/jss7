/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.ss7.tcap.asn.ParseException;

/**
 * @author baranowb
 *
 */
public enum ReturnErrorProblemType {
	UnrecognizedInvokeID(0), ReturnErrorUnexpected(1), UnrecognizedError(2)
	,UnexpectedError(3), MistypedParameter(4);

	ReturnErrorProblemType(long l) {
		this.type = l;
	}

	private long type;

	/**
	 * @return the type
	 */
	public long getType() {
		return type;
	}

	public static ReturnErrorProblemType getFromInt(long t) throws ParseException {
		if (t == 0) {
			return UnrecognizedInvokeID;
		} else if (t == 1) {
			return ReturnErrorUnexpected;
		} else if (t == 2) {
			return UnrecognizedError;
		} else if (t == 3) {
			return UnexpectedError;
		} else if (t == 4) {
			return MistypedParameter;
		}
		throw new ParseException("Wrong value of type: " + t);
	}
}
