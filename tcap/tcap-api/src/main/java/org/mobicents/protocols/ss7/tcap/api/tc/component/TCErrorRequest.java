package org.mobicents.protocols.ss7.tcap.api.tc.component;

public interface TCErrorRequest extends ComponentRequest {

	public byte[] getErrorCode();
	
	public void setErrorCode(byte[] errorCode);
}
