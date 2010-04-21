package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

/**
 * 
 * @author amit bhayani
 *
 */
public interface TCUserAbortIndication extends DialogIndication {

	public Byte getQOS();

	public AbortReason getAbortReason();

	public ApplicationContextName getApplicationContextName();

	public UserInformation getUserInformation();

}
