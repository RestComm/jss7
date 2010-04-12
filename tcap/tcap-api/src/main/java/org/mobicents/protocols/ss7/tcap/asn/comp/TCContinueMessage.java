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
public interface TCContinueMessage extends Encodable {

	
	public static final int _TAG = 0x05;
	public static final boolean _TAG_PC_PRIMITIVE = false;
	public static final int _TAG_CLASS = Tag.CLASS_APPLICATION;
	
	public static final int _TAG_OTX = 0x08;
	public static final boolean _TAG_OTX_PC_PRIMITIVE = true;
	public static final int _TAG_CLASS_OTX = Tag.CLASS_APPLICATION;
	
	public static final int _TAG_DTX = 0x09;
	public static final boolean _TAG_DTX_PC_PRIMITIVE = true;
	public static final int _TAG_CLASS_DTX = Tag.CLASS_APPLICATION;
	
	//mandatory
	public String getOriginatingTransactionId();
	public void setOriginatingTransactionId(String t);
	public String getDestinationTransactionId();
	public void setDestinationTransactionId(String t);
	//opt
	public DialogPortion getDialogPortion();
	public void setDialogPortion(DialogPortion dp);
	//opt
	public Component[] getComponent();
	public void setComponent(Component[] c);
	
}
