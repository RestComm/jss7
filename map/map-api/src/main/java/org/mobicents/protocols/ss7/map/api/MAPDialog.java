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

package org.mobicents.protocols.ss7.map.api;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.dialog.MAPDialogState;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.MessageType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 *
 * @author amit bhayani
 *
 */
public interface MAPDialog extends Serializable {

    int _Timer_s = 10000;
    int _Timer_m = 30000;
    int _Timer_ml = 600000;
    int _Timer_l = 136800000;

    int _Timer_Default = -1;

    /**
     * Returns the current {@link MAPDialogState} of this Dialog
     *
     * @return
     */
    MAPDialogState getState();

    /**
     *
     * @return - local sccp address.
     */
    SccpAddress getLocalAddress();

    /**
     * Sets local Sccp Address.
     *
     * @param localAddress
     */
    void setLocalAddress(SccpAddress localAddress);

    /**
     *
     * @return - remote sccp address which represents remote peer
     */
    SccpAddress getRemoteAddress();

    /**
     * Sets remote Sccp Address
     *
     * @param remoteAddress
     */
    void setRemoteAddress(SccpAddress remoteAddress);

    /**
     * Setting this property to true lead that all sent to TCAP messages of this Dialog will be marked as "ReturnMessageOnError"
     * (SCCP will return the notification is the message has non been delivered to the peer)
     *
     * @param val
     */
    void setReturnMessageOnError(boolean val);

    /**
     * If returnMessageOnError is set to true
     *
     * @return
     */
    boolean getReturnMessageOnError();

    /**
     * Returns the type of the last incoming TCAP primitive (TC-BEGIN, TC-CONTINUE, TC-END or TC-ABORT) It will be equal null if
     * we have just created a Dialog and no messages has income
     *
     * @return
     */
    MessageType getTCAPMessageType();

    /**
     * Return received OrigReference from MAPOpenInfo or null if no OrigReference has been received
     *
     * @return
     */
    AddressString getReceivedOrigReference();

    /**
     * Return received DestReference from MAPOpenInfo or null if no OrigReference has been received
     *
     * @return
     */
    AddressString getReceivedDestReference();

    /**
     * Return received ExtensionContainer from MAPOpenInfo or null if no OrigReference has been received
     *
     * @return
     */
    MAPExtensionContainer getReceivedExtensionContainer();

    /**
     * @return NetworkId to which virtual network Dialog belongs to
     */
    int getNetworkId();

    /**
     * @param networkId
     *            NetworkId to which virtual network Dialog belongs to
     */
    void setNetworkId(int networkId);

    /**
     * Remove MAPDialog without sending any messages and invoking events
     */
    void release();

    /**
     * This method can be called on timeout of dialog, inside {@link MAPDialogListener#onDialogTimeout(Dialog)} callback. If its
     * called, dialog wont be removed in case application does not perform 'send'.
     */
    void keepAlive();

    /**
     * Returns this Dialog's ID. This ID is actually TCAP's Dialog ID.
     * {@link org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog}
     *
     * @return
     */
    Long getLocalDialogId();

    /**
     * Returns this Dialog's remote ID. This ID is actually TCAP's remote Dialog ID.
     * {@link org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog}
     *
     * @return
     */
    Long getRemoteDialogId();

    /**
     * Returns the MAP service that serve this dialog
     *
     * @return
     */
    MAPServiceBase getService();

    /**
     * Set ExtentionContainer that will be send in 1) T-BEGIN 2) T-CONTINUE or T-END if it is response to the T-BEGIN 3) T-ABORT
     * If no Dialogue control APDU is sending - ExtentionContainer will also not be sent
     */
    void setExtentionContainer(MAPExtensionContainer extContainer);

    /**
     * This is equivalent of MAP User issuing the MAP_DELIMITER Service Request. send() is called to explicitly request the
     * transfer of the MAP protocol data units to the peer entities.
     */
    void send() throws MAPException;

    /**
     * This is equivalent of MAP User issuing the MAP_CLOSE Service Request. This service is used for releasing a previously
     * established MAP dialogue. The service may be invoked by either MAP service-user depending on rules defined within the
     * service-user.
     *
     * <br/>
     *
     * If prearrangedEnd is false, all the Service Primitive added to MAPDialog and not sent yet, will be sent to peer.
     *
     * <br/>
     *
     * If prearrangedEnd is true, all the Service Primitive added to MAPDialog and not sent yet, will not be sent to peer.
     *
     * @param prearrangedEnd
     */
    void close(boolean prearrangedEnd) throws MAPException;

    /**
     * This method makes the same as send() method. But when invoking it from events of parsing incoming components real sending
     * will occur only when all incoming components events and onDialogDelimiter() or onDialogClose() would be processed
     *
     * If you are receiving several primitives you can invoke sendDelayed() in several processing components events - the result
     * will be sent after onDialogDelimiter() in a single TC-CONTINUE message
     */
    void sendDelayed() throws MAPException;

    /**
     * This method makes the same as close() method. But when invoking it from events of parsing incoming components real
     * sending and dialog closing will occur only when all incoming components events and onDialogDelimiter() or onDialogClose()
     * would be processed
     *
     * If you are receiving several primitives you can invoke closeDelayed() in several processing components events - the
     * result will be sent and the dialog will be closed after onDialogDelimiter() in a single TC-END message
     *
     * If both of sendDelayed() and closeDelayed() have been invoked TC-END will be issued and the dialog will be closed If
     * sendDelayed() or closeDelayed() were invoked, TC-CONTINUE/TC-END were not sent and abort() or release() are invoked - no
     * TC-CONTINUE/TC-END messages will be sent
     */
    void closeDelayed(boolean prearrangedEnd) throws MAPException;

    /**
     * This is equivalent to MAP User issuing the MAP_U_ABORT Service Request.
     *
     * @param userReason
     */
    void abort(MAPUserAbortChoice mapUserAbortChoice) throws MAPException;

    /**
     * Send T_U_ABORT with MAP-RefuseInfo
     */
    void refuse(Reason reason) throws MAPException;

    /**
     * If a MAP user will not answer to an incoming Invoke with Response, Error or Reject components it should invoke this
     * method to remove the incoming Invoke from a pending incoming Invokes list
     *
     * This do not affect class 1 messages hence most of MAP operations do not need it
     *
     * @param invokeId
     */
    void processInvokeWithoutAnswer(Long invokeId);

    /**
     * Sends the TC-INVOKE component
     *
     * @param invoke
     * @throws MAPException
     */
    void sendInvokeComponent(Invoke invoke) throws MAPException;

    /**
     * Sends the TC-RESULT-NL component
     *
     * @param returnResult
     * @throws MAPException
     */
    void sendReturnResultComponent(ReturnResult returnResult) throws MAPException;

    /**
     * Sends the TC-RESULT-L component
     *
     * @param returnResultLast
     * @throws MAPException
     */
    void sendReturnResultLastComponent(ReturnResultLast returnResultLast) throws MAPException;

    /**
     * Sends the TC-U-ERROR component
     *
     * @param invokeId
     * @param mapErrorMessage
     * @throws MAPException
     */
    void sendErrorComponent(Long invokeId, MAPErrorMessage mapErrorMessage) throws MAPException;

    /**
     * Sends the TC-U-REJECT component
     *
     * @param invokeId This parameter is optional and may be the null
     * @param problem
     * @throws MAPException
     */
    void sendRejectComponent(Long invokeId, Problem problem) throws MAPException;

    /**
     * Reset the Invoke Timeout timer for the Invoke. (TC-TIMER-RESET)
     *
     * @param invokeId
     * @throws MAPException
     */
    void resetInvokeTimer(Long invokeId) throws MAPException;

    /**
     * Causes local termination of an operation invocation (TC-U-CANCEL)
     *
     * @param invokeId
     * @return true:OK, false: Invoke not found
     * @throws MAPException
     */
    boolean cancelInvocation(Long invokeId) throws MAPException;

    /**
     * Getting from the MAPDialog a user-defined object to save relating to the Dialog information
     *
     * @return
     */
    Object getUserObject();

    /**
     * Store in the MAPDialog a user-defined object to save relating to the Dialog information
     *
     * @param userObject
     */
    void setUserObject(Object userObject);

    MAPApplicationContext getApplicationContext();

    /**
     * Return the maximum MAP message length (in bytes) that are allowed for this dialog
     *
     * @return
     */
    int getMaxUserDataLength();

    /**
     * Return the MAP message length (in bytes) that will be after encoding if TC-BEGIN or TC-CONTINUE cases This value must not
     * exceed getMaxUserDataLength() value
     *
     * @return
     */
    int getMessageUserDataLengthOnSend() throws MAPException;

    /**
     * Return the MAP message length (in bytes) that will be after encoding if TC-END case This value must not exceed
     * getMaxUserDataLength() value
     *
     * @param prearrangedEnd
     * @return
     */
    int getMessageUserDataLengthOnClose(boolean prearrangedEnd) throws MAPException;

    /**
     * This method should be invoked after MAPDialog creation if Ericsson-style ASN.1 syntax is used
     *
     * @param imsi
     * @param vlrNo
     */
    void addEricssonData(IMSI imsi, AddressString vlrNo);

    /**
    * Return the value of the IdleTaskTimeout of the TCAP Dialog in milliseconds.
    *
    * @return TCAP IdleTaskTimeout value in milliseconds
    */
    long getIdleTaskTimeout();

    /**
     * Set TCAP IdleTaskTimeout in milliseconds.
     *
     * @param idleTaskTimeoutMs
     */
    void setIdleTaskTimeout(long idleTaskTimeoutMs);
}
