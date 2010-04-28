/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.Encodable;

/**
 * Super interface for Returns.
 * @author baranowb
 * @author amit bhayani
 *
 */
public interface Return extends Encodable,Component {

	public static final int _TAG_IID = 0x02;
	public static final boolean _TAG_IID_PC_PRIMITIVE = true;
	public static final int _TAG_IID_CLASS = Tag.CLASS_UNIVERSAL;
	
	//mandatory
	//public void setInvokeId(Long i);
	//public Long getInvokeId();
	
	
	//opt all
	public void setOperationCode(OperationCode oc);
	public OperationCode getOperationCode();
	
	public void setParameter(Parameter p);
	public Parameter getParameter();
	
}
