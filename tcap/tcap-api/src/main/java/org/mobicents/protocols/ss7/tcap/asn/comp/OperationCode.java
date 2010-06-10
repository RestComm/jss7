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
public interface OperationCode extends Encodable{

	public static final int _TAG_LOCAL = 0x02;
	public static final int _TAG_GLOBAL = 0x06;
	public static final int _TAG_CLASS = Tag.CLASS_UNIVERSAL;
	public static final boolean _TAG_PRIMITIVE = true;
	
	
	//it integer, but two different tags denotate this.

	public void setOperationType(OperationCodeType t);
	public OperationCodeType getOperationType();
	
	public void setCode(Long i);
	public Long getCode();

}
