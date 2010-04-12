/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;


/**
 * @author baranowb
 *
 */
public interface DialogResponseAPDU extends DialogAPDU {

	//opt, default is 1(no other defined)
	public int getProtocolVersion();
	
	
	//mandatory
	public ApplicationContextName getApplicationContextName();
	public void setApplicationContextName(ApplicationContextName acn);
	public Result getResult();
	public void setResult(Result acn);
	public ResultSourceDiagnostic getResultSourceDiagnostic();
	public void setResultSourceDiagnostic(ResultSourceDiagnostic acn);
	
	
	
	//opt
	public UserInformation[] getUserInformation();
	public void setUserInformation(UserInformation[] ui);
	
}
