package org.mobicents.protocols.ss7.tcap.tc.dialog.events;

import org.mobicents.protocols.asn.External;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.AbortReason;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.EventType;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.asn.AbortSource;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

public class TCUserAbortIndicationImpl extends DialogIndicationImpl implements
		TCUserAbortIndication {
	//fields_1
	//private External abortReason;
	// fields_2 
	//private ApplicationContextName applicationContextName;
	private UserInformation userInformation;
	private AbortSource abortSource;
	
	//XXX: fields_1 and fields_2 are mutually exclusive!
	TCUserAbortIndicationImpl() {
		super(EventType.UAbort);
		// TODO Auto-generated constructor stub
	}

	//public External getAbortReason() {
	//	
	//	return abortReason;
	//}

	


	public UserInformation getUserInformation() {
		
		return userInformation;
	}

	/**
	 * @param userInformation the userInformation to set
	 */
	public void setUserInformation(UserInformation userInformation) {
		this.userInformation = userInformation;
	}

	public ApplicationContextName getApplicationContextName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAbortSource(AbortSource abortSource) {
		this.abortSource = abortSource;
		
	}

	/**
	 * @return the abortSource
	 */
	public AbortSource getAbortSource() {
		return abortSource;
	}
	
	

}
