/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.map.api;

import org.mobicents.protocols.ss7.map.api.dialog.MAPDialogState;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface MAPDialog {

	public static int _Timer_s = 10000;
	public static int _Timer_m = 30000;
	public static int _Timer_ml = 600000;
	public static int _Timer_l = 136800000;

	public static int _Timer_Default = -1;

	/**
	 * Returns the current {@link MAPDialogState} of this Dialog
	 * 
	 * @return
	 */
	public MAPDialogState getState();

	/**
	 * Remove MAPDialog without sending any messages and invoking events
	 */
	public void release();

	/**
	 * This method can be called on timeout of dialog, inside
	 * {@link MAPDialogListener#onDialogTimeout(Dialog)} callback. If its
	 * called, dialog wont be removed in case application does not perform
	 * 'send'.
	 */
	public void keepAlive();

	/**
	 * Returns this Dialog's ID. This ID is actually TCAP's Dialog ID.
	 * {@link org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog}
	 * 
	 * @return
	 */
	public Long getDialogId();

	/**
	 * Returns the MAP service that serve this dialog
	 * 
	 * @return
	 */
	public MAPServiceBase getService();
	
    /**
     * 
     * @return - sccp address which represents local peer
     */
    public SccpAddress getLocalAddress();

    /**
	 * 
	 * @return - sccp address which represents remote peer
	 */
    public SccpAddress getRemoteAddress();	

	/**
	 * Set ExtentionContainer that will be send in 1) T-BEGIN 2) T-CONTINUE or
	 * T-END if it is response to the T-BEGIN 3) T-ABORT If no Dialogue control
	 * APDU is sending - ExtentionContainer will also not be sent
	 */
	public void setExtentionContainer(MAPExtensionContainer extContainer);

	/**
	 * This is equivalent of MAP User issuing the MAP_DELIMITER Service Request.
	 * send() is called to explicitly request the transfer of the MAP protocol
	 * data units to the peer entities.
	 */
	public void send() throws MAPException;

	/**
	 * This is equivalent of MAP User issuing the MAP_CLOSE Service Request.
	 * This service is used for releasing a previously established MAP dialogue.
	 * The service may be invoked by either MAP service-user depending on rules
	 * defined within the service-user.
	 * 
	 * <br/>
	 * 
	 * If prearrangedEnd is false, all the Service Primitive added to MAPDialog
	 * and not sent yet, will be sent to peer.
	 * 
	 * <br/>
	 * 
	 * If prearrangedEnd is true, all the Service Primitive added to MAPDialog
	 * and not sent yet, will not be sent to peer.
	 * 
	 * @param prearrangedEnd
	 */
	public void close(boolean prearrangedEnd) throws MAPException;

	/**
	 * This is equivalent to MAP User issuing the MAP_U_ABORT Service Request.
	 * 
	 * @param userReason
	 */
	public void abort(MAPUserAbortChoice mapUserAbortChoice) throws MAPException;

	/**
	 * Send T_U_ABORT with MAP-RefuseInfo
	 */
	public void refuse(Reason reason) throws MAPException;

	/**
	 * Sends the TC-INVOKE component
	 * 
	 * @param invoke
	 * @throws MAPException
	 */
	public void sendInvokeComponent(Invoke invoke) throws MAPException;

	/**
	 * Sends the TC-RESULT-NL component
	 * 
	 * @param returnResult
	 * @throws MAPException
	 */
	public void sendReturnResultComponent(ReturnResult returnResult) throws MAPException;

	/**
	 * Sends the TC-RESULT-L component
	 * 
	 * @param returnResultLast
	 * @throws MAPException
	 */
	public void sendReturnResultLastComponent(ReturnResultLast returnResultLast) throws MAPException;

	/**
	 * Sends the TC-U-ERROR component
	 * 
	 * @param invokeId
	 * @param mapErrorMessage
	 * @throws MAPException
	 */
	public void sendErrorComponent(Long invokeId, MAPErrorMessage mapErrorMessage) throws MAPException;

	/**
	 * Sends the TC-U-REJECT component
	 * 
	 * @param invokeId
	 *            This parameter is optional and may be the null
	 * @param problem
	 * @throws MAPException
	 */
	public void sendRejectComponent(Long invokeId, Problem problem) throws MAPException;

	/**
	 * Reset the Invoke Timeout timer for the Invoke. (TC-TIMER-RESET)
	 * 
	 * @param invokeId
	 * @throws MAPException
	 */
	public void resetInvokeTimer(Long invokeId) throws MAPException;

	/**
	 * Causes local termination of an operation invocation (TC-U-CANCEL)
	 * 
	 * @param invokeId
	 * @return true:OK, false: Invoke not found
	 * @throws MAPException
	 */
	public boolean cancelInvocation(Long invokeId) throws MAPException;

	/**
	 * Getting from the MAPDialog a user-defined object to save relating to the
	 * Dialog information
	 * 
	 * @return
	 */
	public Object getUserObject();

	/**
	 * Store in the MAPDialog a user-defined object to save relating to the
	 * Dialog information
	 * 
	 * @param userObject
	 */
	public void setUserObject(Object userObject);

	public MAPApplicationContext getApplicationContext();

	/**
	 * Return the maximum MAP message length (in bytes) that are allowed for
	 * this dialog
	 * 
	 * @return
	 */
	public int getMaxUserDataLength();

	/**
	 * Return the MAP message length (in bytes) that will be after encoding if
	 * TC-BEGIN or TC-CONTINUE cases This value must not exceed
	 * getMaxUserDataLength() value
	 * 
	 * @return
	 */
	public int getMessageUserDataLengthOnSend() throws MAPException;

	/**
	 * Return the MAP message length (in bytes) that will be after encoding if
	 * TC-END case This value must not exceed getMaxUserDataLength() value
	 * 
	 * @param prearrangedEnd
	 * @return
	 */

	public int getMessageUserDataLengthOnClose(boolean prearrangedEnd) throws MAPException;

	/**
	 * This method should be invoked after MAPDialog creation if Ericsson-style
	 * ASN.1 syntax is used
	 * 
	 * @param imsi
	 * @param vlrNo
	 */
	public void addEricssonData(IMSI imsi, AddressString vlrNo);

}
