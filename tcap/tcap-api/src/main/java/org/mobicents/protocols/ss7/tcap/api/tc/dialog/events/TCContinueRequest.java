package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

import org.mobicents.ss7.sccp.parameter.SccpAddress;

public interface TCContinueRequest extends DialogRequest{

	/**
	 * Sets QOS optional parameter. 
	 * @param b
	 */
	public void setQOS(byte b) throws IllegalArgumentException;
	public byte getQOS();
	public boolean isQOS();
	
	public ApplicationContextName getApplicationContextName();
	public void setApplicationContextName(ApplicationContextName acn);
	
	public UserInformation getUserInformation();
	public void setUserInformation(UserInformation acn);
	
	public void setOriginatingAddress(SccpAddress a);
	public SccpAddress getOriginatingAddress();
	
}
