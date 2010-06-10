/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

/**
 * @author baranowb
 * 
 */
public interface TCEndRequest extends DialogRequest {
	/**
	 * Sets QOS optional parameter. Its passed to SCCP layer?
	 * 
	 * @param b
	 */
	public void setQOS(Byte b) throws IllegalArgumentException;

	public Byte getQOS();

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

	/**
	 * Type of termination. See values of
	 * {@link org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType
	 * TerminationType} enum.
	 * 
	 * @param t
	 */
	public void setTermination(TerminationType t);

	public TerminationType getTerminationType();
}
