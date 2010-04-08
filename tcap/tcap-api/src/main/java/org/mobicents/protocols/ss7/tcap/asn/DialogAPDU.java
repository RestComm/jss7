package org.mobicents.protocols.ss7.tcap.asn;

import org.mobicents.protocols.asn.Tag;

public interface DialogAPDU extends Encodable {

	public static final int _TAG_REQUEST = 0x00;
	public static final int _TAG_UNIDIRECTIONAL = 0x00;
	public static final int _TAG_RESPONSE = 0x01;
	public static final int _TAG_ABORT = 0x04;
	public static final int _TAG_CLASS = Tag.CLASS_APPLICATION;
	public static final boolean _TAG_PRIMITIVE = false;
	public boolean isUniDirectional();
	public DialogAPDUType getType();
	
}
