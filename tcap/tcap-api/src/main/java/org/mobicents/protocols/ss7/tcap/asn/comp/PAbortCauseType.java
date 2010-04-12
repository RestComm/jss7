/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.ss7.tcap.asn.ParseException;


/**
 * @author baranowb
 *
 */
public enum PAbortCauseType {
	// its encoded as INT

	UnrecogniedMessageType(0), UnrecognizedTxID(1),
	BadlyFormattedTxPortion(2), IncorrectTxPortion(3), 
	ResourceLimitation(4);

	private long type = -1;

	PAbortCauseType(long t) {
		this.type = t;
	}

	/**
	 * @return the type
	 */
	public long getType() {
		return type;
	}

	public PAbortCauseType getFromInt(long t) throws ParseException {
		if (t == 0) {
			return UnrecogniedMessageType;
		} else if (t == 1) {
			return UnrecognizedTxID;
		} else if( t == 2)
		{
			return BadlyFormattedTxPortion;
		} else if ( t == 3)
		{
			return IncorrectTxPortion;
		}else if( t == 4 )
		{
			return ResourceLimitation;
		}

		throw new ParseException("Wrong value of response: " + t);
	}
}
