/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.asn.Tag;

/**
 * @author baranowb
 *
 */
public interface ReturnError extends Component {
	public static final int _TAG = 0x03;
	public static final boolean _TAG_PC_PRIMITIVE = false;
	public static final int _TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	
	
	public static final int _TAG_IID = 0x02;
	public static final boolean _TAG_IID_PC_PRIMITIVE = true;
	public static final int _TAG_IID_CLASS = Tag.CLASS_UNIVERSAL;
	
	//mandatory
	public void setInvokeId(Long i);
	public Long getInvokeId();
	
	
	//mandatory
	//that damn error code
	public void setErrorCode(ErrorCode ec);
	public ErrorCode getErrorCode();
	
	//paramter, optional
	public void setParameter(Parameter p);
	public Parameter getParameter();
}
