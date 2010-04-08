package org.mobicents.protocols.ss7.tcap.api.tc.component;

public interface TCResultIndication extends ComponentIndication {

	public Operation getOperation();

	public boolean isLastComponent();
}
