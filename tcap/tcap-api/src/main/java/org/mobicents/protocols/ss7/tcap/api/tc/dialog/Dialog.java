/*
 * TeleStax, Open Source Cloud Communications  
 * Copyright 2012, Telestax Inc and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.tcap.api.tc.dialog;

import java.util.concurrent.locks.ReentrantLock;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;

/**
 * Interface for class representing Dialog/Transaction.
 * 
 * @author baranowb
 * @author sergey vetyutnev
 * 
 */
public interface Dialog {

	/**
	 * returns this dialog ID. It MUST be unique at any given time in local
	 * stack.
	 * 
	 * @return
	 */
	public Long getLocalDialogId();

	/**
	 * return the remote Dialog Id. This will be null if Dialog is locally
	 * originated and not confirmed yet
	 * 
	 * @return
	 */
	public Long getRemoteDialogId();

	/**
	 * Gets local sccp address
	 * 
	 * @return
	 */
	public SccpAddress getLocalAddress();
	
	/**
	 * Sets local Sccp Address.
	 * 
	 * @param localAddress
	 */
	public void setLocalAddress(SccpAddress localAddress);

	/**
	 * Gets remote sccp address
	 * 
	 * @return
	 */
	public SccpAddress getRemoteAddress();
	
	/**
	 * Sets remote Sccp Address
	 * 
	 * @param remoteAddress
	 */
	public void setRemoteAddress(SccpAddress remoteAddress);

	/**
	 * Last sent/received ACN
	 * 
	 * @return the acn
	 */
	public ApplicationContextName getApplicationContextName();

	/**
	 * Last sent/received UI
	 * 
	 * @return the ui
	 */
	public UserInformation getUserInformation();

	/**
	 * returns new, unique for this dialog, invocation id to be used in
	 * TC_INVOKE. If there is no free invoke id, it returns null. Invoke ID is
	 * freed once operation using it is canceled, timeouts or simply returns
	 * final value.
	 * 
	 * @return
	 */
	public Long getNewInvokeId() throws TCAPException;

	/**
	 * Cancels INVOKE pending to be sent. It is equivalent to TC-U-CANCEL.
	 * 
	 * @return <ul>
	 *         <li><b>true</b> - if operation has been success and invoke id has
	 *         been return to pool of available ids.</li>
	 *         <li><b>false</b> -</li>
	 *         </ul>
	 * @throws TCAPException
	 *             - thrown if passed invoke id is wrong
	 */
	public boolean cancelInvocation(Long invokeId) throws TCAPException;

	/**
	 * 
	 * @return <ul>
	 *         <li><b>true </b></li> - if dialog is established(at least one
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
	 * Schedules component for sending. All components on list are queued.
	 * Components are sent once message primitive is issued.
	 * 
	 * @param componentRequest
	 * @throws TCAPSendException
	 */
	public void sendComponent(Component componentRequest) throws TCAPSendException;

	/**
	 * If a TCAP user will not answer to an incoming Invoke
	 * with Response, Error or Reject components
	 * it should invoke this method to remove the incoming Invoke from a pending incoming Invokes list 
	 * 
	 * @param invokeId
	 */
	public void processInvokeWithoutAnswer(Long invokeId);

	/**
	 * Send initial primitive for Structured dialog.
	 * 
	 * @param event
	 * @throws TCAPSendException
	 *             - thrown if dialog is in bad state, ie. Being has already
	 *             been sent or dialog has been removed.
	 */
	public void send(TCBeginRequest event) throws TCAPSendException;

	/**
	 * Sends intermediate primitive for Structured dialog.
	 * 
	 * @param event
	 * @throws TCAPSendException
	 *             - thrown if dialog is in bad state, ie. Begin has not been
	 *             sent or dialog has been removed.
	 */
	public void send(TCContinueRequest event) throws TCAPSendException;

	/**
	 * Sends dialog end request.
	 * 
	 * @param event
	 * @throws TCAPSendException
	 *             - thrown if dialog is in bad state, ie. Begin has not been
	 *             sent or dialog has been removed.
	 */
	public void send(TCEndRequest event) throws TCAPSendException;

	/**
	 * Sends Abort primitive with indication to user as source of termination.
	 * 
	 * @param event
	 * @throws TCAPSendException
	 */
	public void send(TCUserAbortRequest event) throws TCAPSendException;

	/**
	 * Sends unstructured dialog primitive. After this method returns dialog is
	 * expunged from stack as its life cycle reaches end.
	 * 
	 * @param event
	 * @throws TCAPSendException
	 */
	public void send(TCUniRequest event) throws TCAPSendException;

	/**
	 * Programmer hook to release.
	 */
	public void release();

	/**
	 * Resets timeout timer for particular operation.
	 * 
	 * @param invokeId
	 * @throws TCAPException
	 */
	public void resetTimer(Long invokeId) throws TCAPException;

	/**
	 * This method can be called on timeout of dialog, inside
	 * {@link TCListener#onDialogTimeout(Dialog)} callback. If its called,
	 * dialog wont be removed in case application does not perform 'send'.
	 */
	public void keepAlive();

	/**
	 * Returns the state of this Dialog
	 * 
	 * @return
	 */
	public TRPseudoState getState();

	/**
	 * Return the maximum TCAP message length (in bytes) that are allowed for
	 * this dialog
	 * 
	 * @return
	 */
	public int getMaxUserDataLength();

	/**
	 * Return the TCAP message length (in bytes) that will be after encoding
	 * This value must not exceed getMaxUserDataLength() value
	 * 
	 * @param event
	 * @return
	 */
	public int getDataLength(TCBeginRequest event) throws TCAPSendException;

	/**
	 * Return the TCAP message length (in bytes) that will be after encoding
	 * This value must not exceed getMaxUserDataLength() value
	 * 
	 * @param event
	 * @return
	 */
	public int getDataLength(TCContinueRequest event) throws TCAPSendException;

	/**
	 * Return the TCAP message length (in bytes) that will be after encoding
	 * This value must not exceed getMaxUserDataLength() value
	 * 
	 * @param event
	 * @return
	 */
	public int getDataLength(TCEndRequest event) throws TCAPSendException;

	/**
	 * Return the TCAP message length (in bytes) that will be after encoding
	 * This value must not exceed getMaxUserDataLength() value
	 * 
	 * @param event
	 * @return
	 */
	public int getDataLength(TCUniRequest event) throws TCAPSendException;

	/**
	 * Getting from the Dialog a user-defined object to save relating to the
	 * Dialog information
	 * 
	 * @return
	 */
	public Object getUserObject();

	/**
	 * Store in the Dialog a user-defined object to save relating to the Dialog
	 * information
	 * 
	 * @param userObject
	 */
	public void setUserObject(Object userObject);

	/**
	 * 
	 * @return Returns if a dialog works in preview mode
	 */
	public boolean getPreviewMode();

	/**
	 * @return This ReentrantLock object should for synchronizing of Dialog using in multithread environment 
	 */
	public ReentrantLock getDialogLock();

}
