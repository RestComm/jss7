package org.mobicents.protocols.ss7.tcap.api;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniRequest;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

public interface DialogPrimitiveFactory {

	public TCBeginRequest createBegin(Dialog d);

	public TCContinueRequest createContinue(Dialog d);

	public TCEndRequest createEnd(Dialog d);

	public TCUniRequest createUni(Dialog d);

	//public TCUserAbortRequest createUserAbort(Dialog d);
	
	public ApplicationContextName createApplicationContextName();
	
	public UserInformation createUserInformation();
	
	
	
}