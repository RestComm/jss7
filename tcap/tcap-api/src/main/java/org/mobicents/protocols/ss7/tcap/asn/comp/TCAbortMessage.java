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
public interface TCAbortMessage extends Encodable{

	public static final int _TAG = 0x07;
	public static final boolean _TAG_PC_PRIMITIVE = false;
	public static final int _TAG_CLASS = Tag.CLASS_APPLICATION;
	
	public static final int _TAG_P = 0x0A;
	public static final boolean _TAG_P_PC_PRIMITIVE = true;
	public static final int _TAG_CLASS_P = Tag.CLASS_APPLICATION;
	
	public static final int _TAG_DTX = 0x09;
	public static final boolean _TAG_DTX_PC_PRIMITIVE = true;
	public static final int _TAG_CLASS_DTX = Tag.CLASS_APPLICATION;
	
	//mandatory
	public Long getDestinationTransactionId();
	public void setDestinationTransactionId(Long t);
	
	//optionals
	public PAbortCauseType getPAbortCause();
	public void setPAbortCause(PAbortCauseType t);
	
	public DialogPortion getDialogPortion();
	public void setDialogPortion(DialogPortion dp);
	
}
