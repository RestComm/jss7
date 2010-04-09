/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

/**
 * @author baranowb
 *
 */
public interface DialogAbortAPDU extends DialogAPDU {

	//mandatory
	public void setAbortSource(AbortSource as);
	public AbortSource getAbortSource();
	
	
	//opt
	public UserInformation[] getUserInformation();
	public void setUserInformation(UserInformation[] ui);
}


