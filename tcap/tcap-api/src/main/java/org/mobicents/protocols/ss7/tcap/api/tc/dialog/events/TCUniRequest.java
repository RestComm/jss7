/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

/**
 * See Q.771 3.1.2.2.1
 * 
 * @author baranowb
 * 
 */
public interface TCUniRequest extends DialogRequest {

	/**
	 * Sets QOS optional parameter. Its passed to SCCP layer?
	 * 
	 * @param b
	 */
	public void setQOS(Byte b) throws IllegalArgumentException;

	public Byte getQOS();

	/**
	 * Destination address. If this address is different than one in dialog,
	 * this value will overwrite dialog value.
	 */
	public SccpAddress getDestinationAddress();

	public void setDestinationAddress(SccpAddress dest);

	/**
	 * Origin address. If this address is different than one in dialog, this
	 * value will overwrite dialog value.
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
