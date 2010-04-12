/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.ss7.tcap.asn.ParseException;

/**
 * @author baranowb
 *
 */
public enum InvokeProblemType {

	DuplicateInvokeID(0),UnrecognizedOperation(1),MistypedParameter(2),
	ResourceLimitation(3),InitiatingRelease(4),UnrechognizedLinkedID(5),
	LinkedResponseUnexpected(6), UnexpectedLinkedOperation(7);
	
	
	
	private long type;
	
	InvokeProblemType(long l)
	{
		this.type = l;
	}

	/**
	 * @return the type
	 */
	public long getType() {
		return type;
	}
	
	public static InvokeProblemType getFromInt(long t) throws ParseException {
		if (t == 0) {
			return DuplicateInvokeID;
		} else if (t == 1) {
			return UnrecognizedOperation;
		}  else if (t == 2) {
			return MistypedParameter;
		}   else if (t == 3) {
			return ResourceLimitation;
		}  else if (t == 4) {
			return InitiatingRelease;
		}   else if (t == 5) {
			return UnrechognizedLinkedID;
		}  else if (t == 6) {
			return LinkedResponseUnexpected;
		}   else if (t == 7) {
			return UnexpectedLinkedOperation;
		} 

		throw new ParseException("Wrong value of type: " + t);
	}
	
}
