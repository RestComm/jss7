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
	 * @param b
	 */
	public void setQOS(Byte b) throws IllegalArgumentException;
	public Byte getQOS();

	//only getter, since we send via Dialog object, ID is ensured to be present.

	
	//those are actaully passed to ASN encoders, if those are present, DialogAPDU and portion is coded into messag
	public ApplicationContextName getApplicationContextName();
	public void setApplicationContextName(ApplicationContextName acn);
	
	public UserInformation getUserInformation();	
	public void setUserInformation(UserInformation acn);
	
	
	//local
	public void setTermination(TerminationType t);
	public TerminationType getTerminationType();
}
