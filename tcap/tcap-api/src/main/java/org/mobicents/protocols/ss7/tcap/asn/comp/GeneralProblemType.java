/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;

/**
 * @author baranowb
 *
 */
public enum GeneralProblemType {


	
	UnrecognizedComponent(0),MistypedComponent(1),BadlyStructuredComponent(2);
	
	
	private long type = -1;
	
	public static final int _TAG_CLASS = Tag.CLASS_APPLICATION;
	public static final boolean _TAG_PC_PRITIMITIVE = true;
	
	
	
	GeneralProblemType(long l)
	{
		this.type = l;
	}



	/**
	 * @return the type
	 */
	public long getType() {
		return type;
	}
	
	public static GeneralProblemType getFromInt(long t) throws ParseException {
		if (t == 0) {
			return UnrecognizedComponent;
		} else if (t == 1) {
			return MistypedComponent;
		}  else if (t == 2) {
			return BadlyStructuredComponent;
		}  

		throw new ParseException("Wrong value of type: " + t);
	}
}
