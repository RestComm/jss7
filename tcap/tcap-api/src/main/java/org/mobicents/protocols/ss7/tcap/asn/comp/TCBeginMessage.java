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
public interface TCBeginMessage extends Encodable {

	
	public static final int _TAG = 0x02;
	public static final boolean _TAG_PC_PRIMITIVE = false;
	public static final int _TAG_CLASS = Tag.CLASS_APPLICATION;
	
	public static final int _TAG_OTX = 0x08;
	public static final boolean _TAG_OTX_PC_PRIMITIVE = true;
	public static final int _TAG_CLASS_OTX = Tag.CLASS_APPLICATION;
	//mandatory
	public Long getOriginatingTransactionId();
	public void setOriginatingTransactionId(Long t);
	
	//opt FIXME: make this External?
	public DialogPortion getDialogPortion();
	public void setDialogPortion(DialogPortion dp);
	//opt
	public Component[] getComponent();
	public void setComponent(Component[] c);
	
}
