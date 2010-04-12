/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

/**
 * @author baranowb
 *
 */
public enum AbortSourceType {

	
	
	User(0),Provider(1);
	
	private long type = -1;
	
	
	AbortSourceType(long t)
	{
		this.type = t;
	}


	/**
	 * @return the type
	 */
	public long getType() {
		return type;
	}
	
	public static AbortSourceType getFromInt(long t) throws ParseException {
		if (t == 0) {
			return User;
		} else if (t == 1) {
			return Provider;
		} 

		throw new ParseException("Wrong value of type: " + t);
	}
	
}
