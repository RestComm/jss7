package org.mobicents.protocols.ss7.tcap.api;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;

public interface DialogPrimitiveFactory {

	public TCBeginRequest createBegin(Dialog d);

	public TCContinueRequest createContinue(Dialog d);

	public TCEndRequest createEnd(Dialog d);

	public TCUniRequest createUni(Dialog d);
	
	public TCUserAbortRequest createUserAbort(Dialog d);
	
	public TCBeginIndication createBeginIndication(Dialog d);

	public TCContinueIndication createContinueIndication(Dialog d);

	public TCEndIndication createEndIndication(Dialog d);

	public TCUniIndication createUniIndication(Dialog d);
	
	public TCUserAbortIndication createUserAbortIndication(Dialog d);
	
	
	public ApplicationContextName createApplicationContextName();
	
	public UserInformation createUserInformation();
}
