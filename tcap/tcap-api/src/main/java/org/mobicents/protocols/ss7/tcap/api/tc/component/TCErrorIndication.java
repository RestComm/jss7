package org.mobicents.protocols.ss7.tcap.api.tc.component;

public interface TCErrorIndication extends ComponentIndication {

	public byte[] getErrorCode();
	
	public boolean isLastComponent();
	
}
