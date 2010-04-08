package org.mobicents.protocols.ss7.tcap.api.tc.component;

public interface TCInvokeIndication extends ComponentIndication {

	public int getLinkedId();

	/**
	 * 
	 * @return oepartion element.
	 */
	public Operation getOperation();

	public boolean isLastComponent();
}
