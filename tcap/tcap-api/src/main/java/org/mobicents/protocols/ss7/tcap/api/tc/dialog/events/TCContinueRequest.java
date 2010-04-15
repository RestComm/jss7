package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;


import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

public interface TCContinueRequest extends DialogRequest{

	/**
	 * Sets QOS optional parameter. Its passed to SCCP layer?
	 * @param b
	 */
	public void setQOS(Byte b) throws IllegalArgumentException;
	public Byte getQOS();

	
	//those are actaully passed to ASN encoders, if those are present, DialogAPDU and portion is coded into messag
	public ApplicationContextName getApplicationContextName();
	public void setApplicationContextName(ApplicationContextName acn);
	
	public UserInformation getUserInformation();	
	public void setUserInformation(UserInformation acn);
	
}
