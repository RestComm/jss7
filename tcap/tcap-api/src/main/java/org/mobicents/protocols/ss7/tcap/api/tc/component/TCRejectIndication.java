package org.mobicents.protocols.ss7.tcap.api.tc.component;

public interface TCRejectIndication extends ComponentIndication {

	public int getProblemCode();

	public boolean isLastComponent();

}
