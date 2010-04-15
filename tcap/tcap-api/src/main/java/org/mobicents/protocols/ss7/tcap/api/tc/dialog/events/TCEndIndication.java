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
public interface TCEndIndication extends DialogIndication {

	public Byte getQOS();

	// parts from DialogPortion, if present
	public ApplicationContextName getApplicationContextName();

	public UserInformation getUserInformation();


}
