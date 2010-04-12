package org.mobicents.protocols.ss7.tcap.asn;

public enum DialogServiceProviderType {

	Null(0), NoReasonGiven(1), NoCommonDialogPortion(2);

	private long type = -1;

	DialogServiceProviderType(long t) {
		this.type = t;
	}

	/**
	 * @return the type
	 */
	public long getType() {
		return type;
	}

	public static DialogServiceProviderType getFromInt(long t) throws ParseException {
		if (t == 0) {
			return Null;
		} else if (t == 1) {
			return NoReasonGiven;
		} else if( t == 2)
		{
			return NoCommonDialogPortion;
		}

		throw new ParseException("Wrong value of type: " + t);
	}
	
}
