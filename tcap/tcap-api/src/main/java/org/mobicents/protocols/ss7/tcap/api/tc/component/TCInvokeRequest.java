package org.mobicents.protocols.ss7.tcap.api.tc.component;

public interface TCInvokeRequest extends ComponentRequest {

	
	public int getLinkedId();

	/**
	 * 
	 * @return oepartion element.
	 */
	public Operation getOperation();
	
	public void setLinkedId(int id);

	/**
	 * 
	 * @return oepartion element.
	 */
	public void setOperation(Operation op);
	
	public void setTimeOut(long timeOutInMs);

}
