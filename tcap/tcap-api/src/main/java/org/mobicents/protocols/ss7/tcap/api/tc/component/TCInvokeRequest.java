package org.mobicents.protocols.ss7.tcap.api.tc.component;

public interface TCInvokeRequest extends ComponentRequest {

	/**
	 * Returns invoked id.
	 * 
	 * @return
	 */
	public int getInvokeId();

	/**
	 * 
	 * @return oepartion element.
	 */
	public Operation getOperation();

	/**
	 * Returns invoked id.
	 * 
	 * @return
	 */
	public void setInvokeId(int id);

	/**
	 * 
	 * @return oepartion element.
	 */
	public void setOperation(Operation op);

}
