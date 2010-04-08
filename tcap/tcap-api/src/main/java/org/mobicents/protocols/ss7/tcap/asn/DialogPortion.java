package org.mobicents.protocols.ss7.tcap.asn;

import org.mobicents.protocols.asn.Tag;

public interface DialogPortion extends Encodable{

	//Dialog portion is actually type of EXTERNAL, this sucks....
	public static final int _TAG_CLASS = Tag.CLASS_APPLICATION;
	public static final boolean _TAG_PC_PRIMITIVE = false;
	public static final int _TAG = 0x0B;
	
	/**
	 * @return the dialogAPDU
	 */
	public DialogAPDU getDialogAPDU();

	/**
	 * @param dialogAPDU the dialogAPDU to set
	 */
	public void setDialogAPDU(DialogAPDU dialogAPDU);
	
	public void setUnidirectional(boolean flag);
	public boolean isUnidirectional();
	
}
