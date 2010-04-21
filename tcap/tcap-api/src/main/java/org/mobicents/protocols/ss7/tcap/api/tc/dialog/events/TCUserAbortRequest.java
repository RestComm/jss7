package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface TCUserAbortRequest extends DialogRequest {

	public void setQOS(Byte b) throws IllegalArgumentException;

	public Byte getQOS();

	public ApplicationContextName getApplicationContextName();

	public void setApplicationContextName(ApplicationContextName acn);

	public UserInformation getUserInformation();

	public void setUserInformation(UserInformation acn);

	public AbortReason getAbortReason();

	public void setAbortReason(AbortReason abortReason);

}
