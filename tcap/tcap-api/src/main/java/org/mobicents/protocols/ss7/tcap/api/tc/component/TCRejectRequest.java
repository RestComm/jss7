package org.mobicents.protocols.ss7.tcap.api.tc.component;

public interface TCRejectRequest extends ComponentRequest {

	
	public int getProblemCode();
	
	public void setProblemCode(int probCode);
}
