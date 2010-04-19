package org.mobicents.protocols.ss7.tcap.api.tc.dialog;

import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.component.ComponentRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.*;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public interface Dialog {

	/**
	 * returns this dialog ID. It MUST be unique at any given time in local
	 * stack.
	 * 
	 * @return
	 */
	public Long getDialogId();

	/**
	 * Gets local sccp address
	 * 
	 * @return
	 */
	public SccpAddress getLocalAddress();

	/**
	 * Gets remote sccp address
	 * 
	 * @return
	 */
	public SccpAddress getRemoteAddress();

	/**
	 * returns new, unique for this dialog, invocation id to be used in
	 * TC_INVOKE
	 * 
	 * @return
	 * @throws TCAPException
	 */
	public Long getNewInvokeId() throws TCAPException;

	/**
	 * 
	 * @return <ul>
	 *         <li><b>true </b></li> - if dialog is established(at least on
	 *         TC_CONTINUE has been sent/received.)
	 *         <li><b>false</b></li> - no TC_CONTINUE sent/received
	 *         </ul>
	 */
	public boolean isEstabilished();

	/**
	 * 
	 * @return <ul>
	 *         <li><b>true </b></li> - if dialog is structured - its created
	 *         with TC_BEGIN not TC_UNI
	 *         <li><b>false</b></li> - otherwise
	 *         </ul>
	 */
	public boolean isStructured();

	// //////////////////
	// Sender methods //
	// //////////////////
	/**
	 * Schedules component for sending. All components on list are queued. If
	 * used passes cancel component, canceled component is removed and
	 * indication passed to TC User
	 * 
	 * @param componentRequest
	 */
	public void sendComponent(ComponentRequest componentRequest);

	// sender methods, propalby those will change!
	public void sendBegin() throws TCAPSendException;

	public void sendContinue() throws TCAPSendException;

	public void sendEnd() throws TCAPSendException;

	public void sendUni() throws TCAPSendException;

	public void send(TCBeginRequest event) throws TCAPSendException;

	public void send(TCContinueRequest event) throws TCAPSendException;

	public void send(TCEndRequest event) throws TCAPSendException;

	/**
	 * Programmer hook to release.
	 */
	public void release();
}
