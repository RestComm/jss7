package org.mobicents.protocols.ss7.tcap.asn;

public interface DialogPortion extends Encodable{

	//Dialgo portion is actaully type of EXTERNAL, this sucks....
	public static final int _TAG_E = 0x6B;
	public static final int _TAG_EXTERNAL_E = 0x28;
	public static final int _TAG_EXTERNAL_OBJ_E = 0x06;
	public static final int _TAG_EXTERNAL_SINGLE_E = 0xA0;
	
	public static final int _TAG = _TAG_E & 0x1F;
	public static final int _TAG_EXTERNAL = _TAG_EXTERNAL_E & 0x1F;
	public static final int _TAG_EXTERNAL_OBJ = _TAG_EXTERNAL_OBJ_E & 0x1F;
	public static final int _TAG_EXTERNAL_SINGLE = _TAG_EXTERNAL_SINGLE_E & 0x1F;
	
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
