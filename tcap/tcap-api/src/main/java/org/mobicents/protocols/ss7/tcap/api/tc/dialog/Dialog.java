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

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
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
public interface Dialog extends Serializable {

    /**
     * returns this dialog ID. It MUST be unique at any given time in local stack.
     *
     * @return
     */
    Long getLocalDialogId();

    /**
     * return the remote Dialog Id. This will be null if Dialog is locally originated and not confirmed yet
     *
     * @return
     */
    Long getRemoteDialogId();

    /**
     * Gets local sccp address
     *
     * @return
     */
    SccpAddress getLocalAddress();

    /**
     * Sets local Sccp Address.
     *
     * @param localAddress
     */
    void setLocalAddress(SccpAddress localAddress);

    /**
     * Gets remote sccp address
     *
     * @return
     */
    SccpAddress getRemoteAddress();

    /**
     * Sets remote Sccp Address
     *
     * @param remoteAddress
     */
    void setRemoteAddress(SccpAddress remoteAddress);

    /**
     * Gets ssn dialog value
     *
     * @return
     */
    int getLocalSsn();

    /**
     * Last sent/received ACN
     *
     * @return the acn
     */
    ApplicationContextName getApplicationContextName();

    /**
     * Last sent/received UI
     *
     * @return the ui
     */
    UserInformation getUserInformation();

    /**
     * returns new, unique for this dialog, invocation id to be used in TC_INVOKE. If there is no free invoke id, it returns
     * null. Invoke ID is freed once operation using it is canceled, timeouts or simply returns final value.
     *
     * @return
     */
    Long getNewInvokeId() throws TCAPException;

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
     * Option responsible for presence of the protocol version in
     * this dialogue portion.
     *
     * @return boolean true if protocol version must be omitted,
     * false when it should be included and null if not defined at the
     * dialog level and global option should be used.
     */
    Boolean isDoNotSendProtcolVersion();

    /**
     * Modifies option responsible for presence of the protocol version in
     * this dialogue portion.
     *
     * @param doNotSendProtocolVersion
     * boolean true if protocol version must be omitted,
     * false when it should be included and null if not defined at the
     * dialog level and global option should be used.
     */
    void setDoNotSendProtocolVersion(Boolean doNotSendProtocolVersion);

    /**
     * Cancels INVOKE pending to be sent. It is equivalent to TC-U-CANCEL.
     *
     * @return <ul>
     *         <li><b>true</b> - if operation has been success and invoke id has been return to pool of available ids.</li>
     *         <li><b>false</b> -</li>
     *         </ul>
     * @throws TCAPException - thrown if passed invoke id is wrong
     */
    boolean cancelInvocation(Long invokeId) throws TCAPException;

    /**
     *
     * @return <ul>
     *         <li><b>true </b></li> - if dialog is established(at least one TC_CONTINUE has been sent/received.)
     *         <li><b>false</b></li> - no TC_CONTINUE sent/received
     *         </ul>
     */
    boolean isEstabilished();

    /**
     *
     * @return <ul>
     *         <li><b>true </b></li> - if dialog is structured - its created with TC_BEGIN not TC_UNI
     *         <li><b>false</b></li> - otherwise
     *         </ul>
     */
    boolean isStructured();

    // //////////////////
    // Sender methods //
    // //////////////////
    /**
     * Schedules component for sending. All components on list are queued. Components are sent once message primitive is issued.
     *
     * @param componentRequest
     * @throws TCAPSendException
     */
    void sendComponent(Component componentRequest) throws TCAPSendException;

    /**
     * If a TCAP user will not answer to an incoming Invoke with Response, Error or Reject components it should invoke this
     * method to remove the incoming Invoke from a pending incoming Invokes list
     *
     * @param invokeId
     */
    void processInvokeWithoutAnswer(Long invokeId);

    /**
     * Send initial primitive for Structured dialog.
     *
     * @param event
     * @throws TCAPSendException - thrown if dialog is in bad state, ie. Being has already been sent or dialog has been removed.
     */
    void send(TCBeginRequest event) throws TCAPSendException;

    /**
     * Sends intermediate primitive for Structured dialog.
     *
     * @param event
     * @throws TCAPSendException - thrown if dialog is in bad state, ie. Begin has not been sent or dialog has been removed.
     */
    void send(TCContinueRequest event) throws TCAPSendException;

    /**
     * Sends dialog end request.
     *
     * @param event
     * @throws TCAPSendException - thrown if dialog is in bad state, ie. Begin has not been sent or dialog has been removed.
     */
    void send(TCEndRequest event) throws TCAPSendException;

    /**
     * Sends Abort primitive with indication to user as source of termination.
     *
     * @param event
     * @throws TCAPSendException
     */
    void send(TCUserAbortRequest event) throws TCAPSendException;

    /**
     * Sends unstructured dialog primitive. After this method returns dialog is expunged from stack as its life cycle reaches
     * end.
     *
     * @param event
     * @throws TCAPSendException
     */
    void send(TCUniRequest event) throws TCAPSendException;

    /**
     * Programmer hook to release.
     */
    void release();

    /**
     * Resets timeout timer for particular operation.
     *
     * @param invokeId
     * @throws TCAPException
     */
    void resetTimer(Long invokeId) throws TCAPException;

    /**
     * This method can be called on timeout of dialog, inside {@link TCListener#onDialogTimeout(Dialog)} callback. If its
     * called, dialog wont be removed in case application does not perform 'send'.
     */
    void keepAlive();

    /**
     * Returns the state of this Dialog
     *
     * @return
     */
    TRPseudoState getState();

    /**
     * Return the maximum TCAP message length (in bytes) that are allowed for this dialog
     *
     * @return
     */
    int getMaxUserDataLength();

    /**
     * Return the TCAP message length (in bytes) that will be after encoding This value must not exceed getMaxUserDataLength()
     * value
     *
     * @param event
     * @return
     */
    int getDataLength(TCBeginRequest event) throws TCAPSendException;

    /**
     * Return the TCAP message length (in bytes) that will be after encoding This value must not exceed getMaxUserDataLength()
     * value
     *
     * @param event
     * @return
     */
    int getDataLength(TCContinueRequest event) throws TCAPSendException;

    /**
     * Return the TCAP message length (in bytes) that will be after encoding This value must not exceed getMaxUserDataLength()
     * value
     *
     * @param event
     * @return
     */
    int getDataLength(TCEndRequest event) throws TCAPSendException;

    /**
     * Return the TCAP message length (in bytes) that will be after encoding This value must not exceed getMaxUserDataLength()
     * value
     *
     * @param event
     * @return
     */
    int getDataLength(TCUniRequest event) throws TCAPSendException;

    /**
     * Getting from the Dialog a user-defined object to save relating to the Dialog information
     *
     * @return
     */
    Object getUserObject();

    /**
     * Store in the Dialog a user-defined object to save relating to the Dialog information
     *
     * @param userObject
     */
    void setUserObject(Object userObject);

    /**
     *
     * @return Returns if a dialog works in preview mode
     */
    boolean getPreviewMode();

    /**
     * @return This ReentrantLock object should for synchronizing of Dialog using in multithread environment
     */
    ReentrantLock getDialogLock();

    /**
    *
    * @return IdleTaskTimeout value in milliseconds
    */
   long getIdleTaskTimeout();

    /**
     * Set IdleTaskTimeout in milliseconds.
     */
    void setIdleTaskTimeout(long idleTaskTimeoutMs);

}
