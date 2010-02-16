/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

/**
 * @author baranowb
 * 
 */
public interface TCUserAbortIndication extends DialogIndication {

	public byte getQOS();

	public boolean isQOS();

	public ApplicationContextName getApplicationContextName();

	public UserInformation getUserInformation();

	public AbortReason getAbortReason();
}
