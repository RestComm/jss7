package org.mobicents.protocols.ss7.tcap.asn;

public interface DialogAPDU extends Encodable {

	public boolean isUniDirectional();
	public DialogAPDUType getType();
	
}
