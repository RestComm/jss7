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
public interface Component extends Encodable{

	public static final int _COMPONENT_TAG = 0x0C;
	public static final boolean _COMPONENT_TAG_PC_PRIMITIVE = false;
	public static final int _COMPONENT_TAG_CLASS = Tag.CLASS_APPLICATION;
	
	
	//this is doubled by each interface, 
	public void setInvokeId(Long i);
	public Long getInvokeId();
	
	public ComponentType getType();
	
}
