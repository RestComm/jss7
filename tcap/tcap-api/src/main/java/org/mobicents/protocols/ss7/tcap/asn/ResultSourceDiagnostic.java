package org.mobicents.protocols.ss7.tcap.asn;

import org.mobicents.protocols.asn.Tag;

public interface ResultSourceDiagnostic extends Encodable {

	//Annoying... TL[CHOICE[TL[TLV]]
	
	public static final int _TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	public static final boolean _TAG_PC_PRIMITIVE = false; //constructed....
	public static final int _TAG = 0x03;
	
	
	//membersL CHOICE
	public static final int _TAG_U_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	public static final boolean _TAG_U_PC_PRIMITIVE = false; //constructed....
	public static final int _TAG_U = 0x01;
	
	public static final int _TAG_P_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	public static final boolean _TAG_P_PC_PRIMITIVE = false; //constructed....
	public static final int _TAG_P = 0x02;
	
	public void setDialogServiceProviderType(DialogServiceProviderType t);
	public DialogServiceProviderType getDialogServiceProviderType();
	
	public void setDialogServiceUserType(DialogServiceUserType t);
	public DialogServiceUserType getDialogServiceUserType();
	
}
