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

package org.mobicents.protocols.ss7.cap.api;

import org.mobicents.protocols.ss7.cap.api.dialog.CAPDialogState;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.MessageType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public interface CAPDialog {
	
	// Invoke timers
	public static int _Timer_CircuitSwitchedCallControl_Short = 6000; // 1 - 10 sec
	public static int _Timer_CircuitSwitchedCallControl_Medium = 30000; // 1 - 60 sec
	public static int _Timer_CircuitSwitchedCallControl_Long = 300000; // 1 s - 30 minutes
	public static int _Timer_Sms_Short = 10000; // 1 - 20 sec
	public static int _Timer_Gprs_Short = 10000; // 1 - 20 sec
	
	public static int _Timer_Default = -1;


	/* 
	 * Setting this property to true lead that all sent to TCAP messages of this Dialog will be marked as "ReturnMessageOnError"
	 * (SCCP will return the notification is the message has non been delivered to the peer)
	 */
	public void setReturnMessageOnError(boolean val);

	public boolean getReturnMessageOnError();

	public SccpAddress getLocalAddress();

    public SccpAddress getRemoteAddress();
	
	/**
	 * This method can be called on timeout of dialog, inside
	 * {@link CAPDialogListener#onDialogTimeout(Dialog)} callback. If its called,
	 * dialog wont be removed in case application does not perform 'send'.
	 */
	public void keepAlive();
	
	/**
	 * Returns this Dialog's ID. This ID is actually TCAP's Dialog ID.
	 * {@link org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog}
	 * 
	 * @return
	 */
	public Long getLocalDialogId();
	
	/**
	 * Returns this Dialog's remote ID. This ID is actually TCAP's remote Dialog ID.
	 * {@link org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog}
	 * 
	 * @return
	 */
	public Long getRemoteDialogId();

	/**
	 * Returns the CAP service that serve this dialog
	 * 
	 * @return
	 */
	public CAPServiceBase getService();

	public CAPDialogState getState();

	/**
	 * Set CAPGprsReferenceNumber that will be send in 1) T-BEGIN 2) first T-CONTINUE 
	 * messages. This parameter is applied only to gprsSSF-gsmSCF interface
	 */
	public void setGprsReferenceNumber(CAPGprsReferenceNumber capGprsReferenceNumber);

	public CAPGprsReferenceNumber getGprsReferenceNumber();

	/**
	 * Return received GprsReferenceNumber or null if no GprsReferenceNumber has been received
	 * @return
	 */
	public CAPGprsReferenceNumber getReceivedGprsReferenceNumber();

	/**
	 * Returns the type of the last incoming TCAP primitive (TC-BEGIN, TC-CONTINUE, TC-END or TC-ABORT)
	 * It will be equal null if we have just created a Dialog and no messages has income
	 * 
	 * @return
	 */
	public MessageType getTCAPMessageType();

	public void release();

	/**
	 * Sends TB-BEGIN, TC-CONTINUE depends on dialogue state
	 * including primitives  
	 */
	public void send() throws CAPException;

	/**
	 * This service is used for releasing a previously established CAP dialogue.
	 * Sends TC-CLOSE
	 * 
	 * @param prearrangedEnd
	 *            If prearrangedEnd is false, all the Service Primitive added to
	 *            CAPDialog and not sent yet, will be sent to peer. If
	 *            prearrangedEnd is true, all the Service Primitive added to
	 *            CAPDialog and not sent yet, will not be sent to peer.
	 */
	public void close(boolean prearrangedEnd) throws CAPException;

	/**
	 * This method makes the same as send() method.
	 * But when invoking it from events of parsing incoming components
	 * real sending will occur only when all incoming components events 
	 * and onDialogDelimiter() or onDialogClose() would be processed 
	 * 
	 * If you are receiving several primitives you can invoke sendDelayed()
	 * in several processing components events - the result will be sent after
	 * onDialogDelimiter() in a single TC-CONTINUE message
	 */
	public void sendDelayed() throws CAPException;

	/**
	 * This method makes the same as close() method.
	 * But when invoking it from events of parsing incoming components
	 * real sending and dialog closing will occur only when all incoming components events 
	 * and onDialogDelimiter() or onDialogClose() would be processed 
	 * 
	 * If you are receiving several primitives you can invoke closeDelayed()
	 * in several processing components events - the result will be sent 
	 * and the dialog will be closed after onDialogDelimiter() in a single TC-END message
	 * 
	 * If both of sendDelayed() and closeDelayed() have been invoked
	 * TC-END will be issued and the dialog will be closed
	 * If sendDelayed() or closeDelayed() were invoked, TC-CONTINUE/TC-END were not sent
	 * and abort() or release() are invoked - no TC-CONTINUE/TC-END messages will be sent
	 */
	public void closeDelayed(boolean prearrangedEnd) throws CAPException;

	/**
	 * Sends TC_U_ABORT Service Request with an abort reason.
	 * 
	 * @param abortReason
	 *            optional - may be null
	 */
	public void abort(CAPUserAbortReason abortReason) throws CAPException;

	/**
	 * Sends the TC-INVOKE component
	 * 
	 * @param invoke
	 * @throws CAPException
	 */
	public void sendInvokeComponent(Invoke invoke) throws CAPException;

	/**
	 * Sends the TC-RESULT-L component
	 * 
	 * @param returnResultLast
	 * @throws CAPException
	 */
	public void sendReturnResultLastComponent(ReturnResultLast returnResultLast) throws CAPException;

	/**
	 * Sends the TC-U-ERROR component
	 * 
	 * @param invokeId
	 * @param capErrorMessage
	 * @throws CAPException
	 */
	public void sendErrorComponent(Long invokeId, CAPErrorMessage capErrorMessage) throws CAPException;
	
	/**
	 * Sends the TC-U-REJECT component
	 * 
	 * @param invokeId
	 *            This parameter is optional and may be the null
	 * @param problem
	 * @throws CAPException
	 */
	public void sendRejectComponent(Long invokeId, Problem problem) throws CAPException;

	/**
	 * Reset the Invoke Timeout timer for the Invoke. (TC-TIMER-RESET)  
	 * 
	 * @param invokeId
	 * @throws CAPException
	 */
	public void resetInvokeTimer(Long invokeId) throws CAPException;

	/**
	 * Causes local termination of an operation invocation (TC-U-CANCEL)
	 * 
	 * @param invokeId
	 * @return true:OK, false: Invoke not found 
	 * @throws CAPException
	 */
	public boolean cancelInvocation(Long invokeId) throws CAPException;
	
	/**
	 * Getting from the CAPDialog a user-defined object to save relating to the
	 * Dialog information
	 * 
	 * @return
	 */
	public Object getUserObject();

	/**
	 * Store in the CAPDialog a user-defined object to save relating to the
	 * Dialog information
	 * 
	 * @param userObject
	 */
	public void setUserObject(Object userObject);
	
	public CAPApplicationContext getApplicationContext();
	
	/**
	 * Return the maximum CAP message length (in bytes) that are allowed for this dialog
	 * @return
	 */
	public int getMaxUserDataLength();
	
	/**
	 * Return the CAP message length (in bytes) that will be after encoding
	 * if TC-BEGIN or TC-CONTINUE cases
	 * This value must not exceed getMaxUserDataLength() value
	 * @return
	 */
	public int getMessageUserDataLengthOnSend() throws CAPException;
	
	/**
	 * Return the CAP message length (in bytes) that will be after encoding
	 * if TC-END case
	 * This value must not exceed getMaxUserDataLength() value
	 * @param prearrangedEnd
	 * @return
	 */
	public int getMessageUserDataLengthOnClose(boolean prearrangedEnd) throws CAPException;
}
