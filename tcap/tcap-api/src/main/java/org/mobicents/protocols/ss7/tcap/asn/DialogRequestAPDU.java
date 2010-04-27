/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

/**
 * @author baranowb
 *
 */
public interface DialogRequestAPDU extends DialogAPDU {



	//opt, default is 1(no other defined)
	public int getProtocolVersion();
	//mandatory
	public ApplicationContextName getApplicationContextName();
	public void setApplicationContextName(ApplicationContextName acn);
	//opt
	public UserInformation getUserInformation();
	public void setUserInformation(UserInformation ui);
	
}
