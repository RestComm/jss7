package org.mobicents.protocols.ss7.tcap.api.tc.component;

public interface TCResultRequest extends ComponentRequest {

	
	public Operation getOperation();
	
	public boolean isLastInvokeEvent(); 
	
	
	public void setOperation(Operation op);
	
	public void setLastInvokeEvent(boolean isLastInvokeEvent);
}
