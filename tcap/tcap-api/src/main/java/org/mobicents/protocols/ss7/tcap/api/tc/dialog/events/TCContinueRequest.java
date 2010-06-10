package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

public interface TCContinueRequest extends DialogRequest {

	/**
	 * Sets QOS optional parameter. Its passed to SCCP layer?
	 * 
	 * @param b
	 */
	public void setQOS(Byte b) throws IllegalArgumentException;

	public Byte getQOS();

	/**
	 * Sets origin address. This parameter is used only in first TCContinue,
	 * sent as response to TCBegin. This parameter, if set, changes local peer
	 * address(remote end will send request to value set by this method).
	 * 
	 * @return
	 */
	public SccpAddress getOriginatingAddress();

	public void setOriginatingAddress(SccpAddress dest);

	/**
	 * Application context name for this dialog.
	 * 
	 * @return
	 */
	public ApplicationContextName getApplicationContextName();

	public void setApplicationContextName(ApplicationContextName acn);

	/**
	 * User information for this dialog.
	 * 
	 * @return
	 */
	public UserInformation getUserInformation();

	public void setUserInformation(UserInformation acn);

}
