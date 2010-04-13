/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.Encodable;

/**
 * @author baranowb
 *
 */
public interface ErrorCode extends Encodable{

	//it contains valid params for error.... 
	
	public static final int _TAG_LOCAL = 0x02;
	public static final int _TAG_GLOBAL = 0x06;
	public static final int _TAG_CLASS = Tag.CLASS_UNIVERSAL;
	public static final boolean _TAG_PRIMITIVE = true;

	
	public void setErrorType(ErrorCodeType t);
	public ErrorCodeType getErrorType();
	
	
	public void setData(byte[] d);
	public byte[] getData();
	
}
