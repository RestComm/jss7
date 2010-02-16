package org.mobicents.protocols.ss7.tcap.api.tc.component;

public interface TCInvokeIndication extends ComponentIndication{

	
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
}
