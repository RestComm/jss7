package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public interface TCContinueIndication extends DialogIndication {

	public byte getQOS();
	public boolean isQOS();
	
	public ApplicationContextName getApplicationContextName();

	
	public UserInformation getUserInformation();


	public SccpAddress getOriginatingAddress();
}
