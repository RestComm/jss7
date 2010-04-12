/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn.comp;

import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcap.asn.Encodable;

/**
 * @author baranowb
 *
 */
public interface TCUniMessage extends Encodable {
	
	public static final int _TAG = 0x01;
	public static final boolean _TAG_PC_PRIMITIVE = false;
	public static final int _TAG_CLASS = Tag.CLASS_APPLICATION;
	
	//opt
	public DialogPortion getDialogPortion();
	public void setDialogPortion(DialogPortion dp);
	//mandatory
	public Component[] getComponent();
	public void setComponent(Component[] c);
}
