package org.mobicents.protocols.ss7.tcap.tc.dialog.events;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.AbortReason;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.EventType;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

public class TCUserAbortIndicationImpl extends DialogIndicationImpl implements
		TCUserAbortIndication {

	
	
	TCUserAbortIndicationImpl() {
		super(EventType.Abort);
		// TODO Auto-generated constructor stub
	}

	public AbortReason getAbortReason() {
		// TODO Auto-generated method stub
		return null;
	}

	public ApplicationContextName getApplicationContextName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Byte getQOS() {
		// TODO Auto-generated method stub
		return null;
	}

	public UserInformation getUserInformation() {
		// TODO Auto-generated method stub
		return null;
	}

}
