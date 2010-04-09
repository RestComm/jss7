package org.mobicents.protocols.ss7.tcap.asn;

import org.mobicents.protocols.asn.Tag;

public interface Result extends Encodable{

	
	public static final int _TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	public static final boolean _TAG_PC_PRIMITIVE = false; //constructed....
	public static final int _TAG = 0x02;
	
	public void setResultType(ResultType t);
	public ResultType getResultType();
	
}
