package org.mobicents.protocols.ss7.tcap.api;

import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.*;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;

public interface TCListener {

	// dialog handlers
	/**
	 * Invoked for TC_UNI. See Q.771 3.1.2.2.2.1
	 */
	public void onTCUni(TCUniIndication ind);

	/**
	 * Invoked for TC_BEGIN. See Q.771 3.1.2.2.2.1
	 */
	public void onTCBegin(TCBeginIndication ind);

	/**
	 * Invoked for TC_CONTINUE dialog primitive. See Q.771
	 * 3.1.2.2.2.2/3.1.2.2.2.3
	 * 
	 * @param ind
	 */
	public void onTCContinue(TCContinueIndication ind);

	/**
	 * Invoked for TC_END dialog primitive. See Q.771 3.1.2.2.2.4
	 * 
	 * @param ind
	 */
	public void onTCEnd(TCEndIndication ind);

	/**
	 * Invoked for TC-U-Abort primitive(P-Abort-Cause is present.). See Q.771
	 * 3.1.2.2.2.4
	 * 
	 * @param ind
	 */
	public void onTCUserAbort(TCUserAbortIndication ind);

	/**
	 * Invoked when dialog has been terminated by some unpredicatable
	 * environment cause. See Q.771 3.1.4.2
	 * 
	 * @param ind
	 */
	public void onTCPAbort(TCPAbortIndication ind);

	/**
	 * Called once dialog is released. It is invoked once primitives are
	 * delivered. Indicates that stack has no reference, and dialog object is
	 * considered invalid.
	 * 
	 * @param d
	 */
	public void dialogReleased(Dialog d);

	/**
	 * 
	 * @param tcInvokeRequest
	 */
	public void onInvokeTimeout(Invoke tcInvokeRequest);

}
