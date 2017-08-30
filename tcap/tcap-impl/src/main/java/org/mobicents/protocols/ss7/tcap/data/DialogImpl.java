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

package org.mobicents.protocols.ss7.tcap.data;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.api.tc.component.OperationState;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.mobicents.protocols.ss7.tcap.asn.AbortSource;
import org.mobicents.protocols.ss7.tcap.asn.AbortSourceType;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextNameImpl;
import org.mobicents.protocols.ss7.tcap.asn.DialogAPDU;
import org.mobicents.protocols.ss7.tcap.asn.DialogAPDUType;
import org.mobicents.protocols.ss7.tcap.asn.DialogAbortAPDU;
import org.mobicents.protocols.ss7.tcap.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU;
import org.mobicents.protocols.ss7.tcap.asn.DialogResponseAPDU;
import org.mobicents.protocols.ss7.tcap.asn.DialogServiceProviderType;
import org.mobicents.protocols.ss7.tcap.asn.DialogServiceUserType;
import org.mobicents.protocols.ss7.tcap.asn.DialogUniAPDU;
import org.mobicents.protocols.ss7.tcap.asn.EncodeException;
import org.mobicents.protocols.ss7.tcap.asn.ErrorCodeImpl;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcap.asn.OperationCodeImpl;
import org.mobicents.protocols.ss7.tcap.asn.ProblemImpl;
import org.mobicents.protocols.ss7.tcap.asn.Result;
import org.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.mobicents.protocols.ss7.tcap.asn.ResultType;
import org.mobicents.protocols.ss7.tcap.asn.ReturnResultImpl;
import org.mobicents.protocols.ss7.tcap.asn.ReturnResultLastImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCAbortMessageImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCBeginMessageImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCContinueMessageImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCEndMessageImpl;
import org.mobicents.protocols.ss7.tcap.asn.TCUniMessageImpl;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.Utils;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.DialogPrimitiveFactoryImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCBeginIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCContinueIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCEndIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCPAbortIndicationImpl;
import org.mobicents.protocols.ss7.tcap.tc.dialog.events.TCUserAbortIndicationImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author <a href="mailto:info@pro-ids.com">ProIDS sp. z o.o.</a>
 *
 */
public class DialogImpl extends DialogBaseImpl {
    // timeout of remove task after TC_END
    private final IDialogData data;

    @Override
    public IDialogDataBase getBaseData() { return data; }


    @Override
    public int getLocalSsn() {
        return data.getLocalSsn();
    }

    public void setLocalSsn(int newSsn) {
        data.setLocalSsn(newSsn);
    }

    private static final Logger logger = Logger.getLogger(DialogImpl.class);

    /**
     * Creating a Dialog for normal mode
     *
     */
    public DialogImpl(TCAPProviderImpl provider, IDialogData data ) {
        super(provider);
        this.data=data;
    }

    @Override
    public void release() {
        for (ITCAPOperation op : data.listTCAPOpeartions()) {
            op.setState(OperationState.Idle);
        }

        if (data.isStructured() && this.provider.getStack().getStatisticsEnabled()) {
            long lg = System.currentTimeMillis() - data.getStartDialogTime();
            this.provider.getStack().getCounterProviderImpl().updateAllDialogsDuration(lg);
        }


        setState(TRPseudoState.Expunged);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#getNewInvokeId()
     */
    @Override
    public Long getNewInvokeId() throws TCAPException {
        return data.allocateInvokeId();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#cancelInvocation (java.lang.Long)
     */
    @Override
    public boolean cancelInvocation(final Long invokeId) throws TCAPException {
        try {
            getDialogLock().lock();
            boolean success=false;
            ITCAPOperation to=data.getTCAPOperation(invokeId);
            if(to!=null) {
                to.setState(OperationState.Idle);
                to.free();
                success=true;
            }

            // lookup through send buffer.
            success|=data.removeScheduledComponent(new IDialogData.ScheduledComponentRemoveFilter() {
                    @Override
                    public boolean filter(Component c) {
                        return c instanceof InvokeImpl && invokeId.equals(((InvokeImpl)c).getInvokeId());
                    }
                }
            );


            return success;
        } finally {
            getDialogLock().unlock();
        }
    }

    @Override
    public void keepAlive() {
        try {
            getDialogLock().lock();
            if (data.isIdleTimerInvoked()) {
                data.setIdleTimerActionTaken(true);
            }
        } finally {
            getDialogLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents
     * .protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest)
     */
    @Override
    public void send(TCBeginRequest event) throws TCAPSendException {
        if (!data.isStructured()) {
            throw new TCAPSendException("Unstructured dialogs do not use Begin");
        }
        try {
            getDialogLock().lock();
            if (data.getState() != TRPseudoState.Idle) {
                throw new TCAPSendException("Can not send Begin in this state: " + data.getState());
            }

            data.setIdleTimerActionTaken(true);
            restartIdleTimer();
            TCBeginMessageImpl tcbm = (TCBeginMessageImpl) TcapFactory.createTCBeginMessage();

            // build DP

            if (event.getApplicationContextName() != null) {
                data.setDpSentInBegin(true);
                DialogPortion dp = TcapFactory.createDialogPortion();
                dp.setUnidirectional(false);
                DialogRequestAPDU apdu = TcapFactory.createDialogAPDURequest();
                apdu.setDoNotSendProtocolVersion(this.provider.getStack().getDoNotSendProtocolVersion());
                dp.setDialogAPDU(apdu);
                apdu.setApplicationContextName(event.getApplicationContextName());
                data.setLastACN(event.getApplicationContextName());
                if (event.getUserInformation() != null) {
                    apdu.setUserInformation(event.getUserInformation());
                    data.setLastUI(event.getUserInformation());
                }
                tcbm.setDialogPortion(dp);

                if (this.provider.getStack().getStatisticsEnabled()) {
                    String acn = ((ApplicationContextNameImpl) event.getApplicationContextName()).getStringValue();
                    this.provider.getStack().getCounterProviderImpl().updateOutgoingDialogsPerApplicatioContextName(acn);
                }
            } else {
                if (this.provider.getStack().getStatisticsEnabled()) {
                    this.provider.getStack().getCounterProviderImpl().updateOutgoingDialogsPerApplicatioContextName("");
                }
            }

            // now comps
            if (data.getLocalRelayedTransactionIdObject()!=null) {
                tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(data.getLocalRelayedTransactionIdObject()));
            } else {
                tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(data.getLocalTransactionId()));
            }
            Component[] componentsToSend = prepareComponents(true);
            if(componentsToSend!=null) {
                tcbm.setComponent(componentsToSend);
            }

            AsnOutputStream aos = new AsnOutputStream();
            try {
                tcbm.encode(aos);
                setState(TRPseudoState.InitialSent);
                if (this.provider.getStack().getStatisticsEnabled()) {
                    this.provider.getStack().getCounterProviderImpl().updateTcBeginSentCount();
                }
                this.provider.send(aos.toByteArray(), event.getReturnMessageOnError(), data.getRemoteAddress(), data.getLocalAddress(),
                        data.getSeqControl(), data.getNetworkId(),this.getLocalSsn());
                data.clearScheduledComponentList();
            } catch (Throwable e) {
                // FIXME: remove freshly added invokes to free invoke ID??
                // TODO: should we release this dialog because TC-BEGIN sending has been failed
                if (logger.isEnabledFor(Level.ERROR)) {
                    logger.error("Failed to send message: ", e);
                }
                throw new TCAPSendException("Failed to send TC-Begin message: " + e.getMessage(), e);
            }

        } finally {
            getDialogLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents
     * .protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest)
     */
    @Override
    public void send(TCContinueRequest event) throws TCAPSendException {
        if (!data.isStructured()) {
            throw new TCAPSendException("Unstructured dialogs do not use Continue");
        }
        try {
            getDialogLock().lock();
            if (data.getState() == TRPseudoState.InitialReceived) {
                data.setIdleTimerActionTaken(true);
                restartIdleTimer();
                TCContinueMessageImpl tcbm = (TCContinueMessageImpl) TcapFactory.createTCContinueMessage();

                if (event.getApplicationContextName() != null) {

                    // set dialog portion
                    DialogPortion dp = TcapFactory.createDialogPortion();
                    dp.setUnidirectional(false);
                    DialogResponseAPDU apdu = TcapFactory.createDialogAPDUResponse();
                    apdu.setDoNotSendProtocolVersion(this.provider.getStack().getDoNotSendProtocolVersion());
                    dp.setDialogAPDU(apdu);
                    apdu.setApplicationContextName(event.getApplicationContextName());
                    if (event.getUserInformation() != null) {
                        apdu.setUserInformation(event.getUserInformation());
                    }
                    // WHERE THE HELL THIS COMES FROM!!!!
                    // WHEN REJECTED IS USED !!!!!
                    Result res = TcapFactory.createResult();
                    res.setResultType(ResultType.Accepted);
                    ResultSourceDiagnostic rsd = TcapFactory.createResultSourceDiagnostic();
                    rsd.setDialogServiceUserType(DialogServiceUserType.Null);
                    apdu.setResultSourceDiagnostic(rsd);
                    apdu.setResult(res);
                    tcbm.setDialogPortion(dp);

                }

                tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(data.getLocalTransactionId()));
                tcbm.setDestinationTransactionId(data.getRemoteTransactionId());
                Component[] componentsToSend = prepareComponents(true);
                if(componentsToSend!=null) {
                    tcbm.setComponent(componentsToSend);
                }
                // local address may change, lets check it;
                if (event.getOriginatingAddress() != null && !event.getOriginatingAddress().equals(data.getLocalAddress())) {
                    data.setLocalAddress(event.getOriginatingAddress());
                }
                AsnOutputStream aos = new AsnOutputStream();
                try {
                    tcbm.encode(aos);
                    if (this.provider.getStack().getStatisticsEnabled()) {
                        this.provider.getStack().getCounterProviderImpl().updateTcContinueSentCount();
                    }
                    this.provider.send(aos.toByteArray(), event.getReturnMessageOnError(), data.getRemoteAddress(),
                            data.getLocalAddress(), data.getSeqControl(), data.getNetworkId(),data.getLocalSsn());
                    setState(TRPseudoState.Active);
                    data.clearScheduledComponentList();
                } catch (Exception e) {
                    // FIXME: remove freshly added invokes to free invoke ID??
                    if (logger.isEnabledFor(Level.ERROR)) {
                        logger.error("Failed to send message: ", e);
                    }
                    throw new TCAPSendException("Failed to send TC-Continue message: " + e.getMessage(), e);
                }

            } else if (data.getState() == TRPseudoState.Active) {
                data.setIdleTimerActionTaken(true);
                restartIdleTimer();
                // in this we ignore acn and passed args(except qos)
                TCContinueMessageImpl tcbm = (TCContinueMessageImpl) TcapFactory.createTCContinueMessage();

                tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(data.getLocalTransactionId()));
                tcbm.setDestinationTransactionId(data.getRemoteTransactionId());
                Component[] componentsToSend = prepareComponents(true);
                if(componentsToSend!=null) {
                    tcbm.setComponent(componentsToSend);
                }

                AsnOutputStream aos = new AsnOutputStream();
                try {
                    tcbm.encode(aos);
                    this.provider.getStack().getCounterProviderImpl().updateTcContinueSentCount();
                    this.provider.send(aos.toByteArray(), event.getReturnMessageOnError(), data.getRemoteAddress(),
                            data.getLocalAddress(), data.getSeqControl(), data.getNetworkId(),data.getLocalSsn());
                    data.clearScheduledComponentList();
                } catch (Exception e) {
                    // FIXME: remove freshly added invokes to free invoke ID??
                    if (logger.isEnabledFor(Level.ERROR)) {
                        logger.error("Failed to send message: ", e);
                    }
                    throw new TCAPSendException("Failed to send TC-Continue message: " + e.getMessage(), e);
                }
            } else {
                throw new TCAPSendException("Wrong state: " + data.getState());
            }

        } finally {
            getDialogLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents
     * .protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest)
     */
    @Override
    public void send(TCEndRequest event) throws TCAPSendException {

        if (!data.isStructured()) {
            throw new TCAPSendException("Unstructured dialogs do not use End");
        }

        try {
            getDialogLock().lock();
            TCEndMessageImpl tcbm = null;

            if (data.getState() == TRPseudoState.InitialReceived) {
                // TC-END request primitive issued in response to a TC-BEGIN
                // indication primitive
                data.setIdleTimerActionTaken(true);
                stopIdleTimer();
                tcbm = (TCEndMessageImpl) TcapFactory.createTCEndMessage();
                tcbm.setDestinationTransactionId(data.getRemoteTransactionId());

                if (event.getTerminationType() == TerminationType.Basic) {
                    Component[] componentsToSend = prepareComponents(true);
                    if(componentsToSend!=null) {
                        tcbm.setComponent(componentsToSend);
                    }
                } else if (event.getTerminationType() == TerminationType.PreArranged) {
                    data.clearScheduledComponentList();
                } else {
                    throw new TCAPSendException("Termination TYPE must be present");
                }

                ApplicationContextName acn = event.getApplicationContextName();
                if (acn != null) { // acn & DialogPortion is absent in TCAP V1

                    // set dialog portion
                    DialogPortion dp = TcapFactory.createDialogPortion();
                    dp.setUnidirectional(false);
                    DialogResponseAPDU apdu = TcapFactory.createDialogAPDUResponse();
                    apdu.setDoNotSendProtocolVersion(this.provider.getStack().getDoNotSendProtocolVersion());
                    dp.setDialogAPDU(apdu);

                    apdu.setApplicationContextName(event.getApplicationContextName());
                    if (event.getUserInformation() != null) {
                        apdu.setUserInformation(event.getUserInformation());
                    }

                    // WHERE THE HELL THIS COMES FROM!!!!
                    // WHEN REJECTED IS USED !!!!!
                    Result res = TcapFactory.createResult();
                    res.setResultType(ResultType.Accepted);
                    ResultSourceDiagnostic rsd = TcapFactory.createResultSourceDiagnostic();
                    rsd.setDialogServiceUserType(DialogServiceUserType.Null);
                    apdu.setResultSourceDiagnostic(rsd);
                    apdu.setResult(res);
                    tcbm.setDialogPortion(dp);
                }
                // local address may change, lets check it
                if (event.getOriginatingAddress() != null && !event.getOriginatingAddress().equals(data.getLocalAddress())) {
                    data.setLocalAddress(event.getOriginatingAddress());
                }

            } else if (data.getState() == TRPseudoState.Active) {
                restartIdleTimer();
                tcbm = (TCEndMessageImpl) TcapFactory.createTCEndMessage();

                tcbm.setDestinationTransactionId(data.getRemoteTransactionId());
                if (event.getTerminationType() == TerminationType.Basic) {
                    Component[] componentsToSend = prepareComponents(true);
                    if(componentsToSend!=null) {
                        tcbm.setComponent(componentsToSend);
                    }
                } else if (event.getTerminationType() == TerminationType.PreArranged) {
                    data.clearScheduledComponentList();
                } else {
                    throw new TCAPSendException("Termination TYPE must be present");
                }

                // ITU - T Q774 Section 3.2.2.1 Dialogue Control

                // when a dialogue portion is received inopportunely (e.g. a
                // dialogue APDU is received during the active state of a
                // transaction).

                // Don't set the Application Context or Dialogue Portion in
                // Active state

            } else {
                throw new TCAPSendException(String.format("State is not %s or %s: it is %s", TRPseudoState.Active,
                        TRPseudoState.InitialReceived, data.getState()));
            }

            AsnOutputStream aos = new AsnOutputStream();
            try {
                tcbm.encode(aos);
                if (this.provider.getStack().getStatisticsEnabled()) {
                    this.provider.getStack().getCounterProviderImpl().updateTcEndSentCount();
                }
                this.provider.send(aos.toByteArray(), event.getReturnMessageOnError(), data.getRemoteAddress(), data.getLocalAddress(),
                        data.getSeqControl(), data.getNetworkId(),data.getLocalSsn());

                data.clearScheduledComponentList();
            } catch (Exception e) {
                // FIXME: remove freshly added invokes to free invoke ID??
                if (logger.isEnabledFor(Level.ERROR)) {
                    logger.error("Failed to send message: ", e);
                }
                throw new TCAPSendException("Failed to send TC-End message: " + e.getMessage(), e);
            } finally {
                // FIXME: is this proper place - should we not release in case
                // of error ?
                release();
            }
        } finally {
            getDialogLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#sendUni()
     */
    @Override
    public void send(TCUniRequest event) throws TCAPSendException {
        if (data.isStructured()) {
            throw new TCAPSendException("Structured dialogs do not use Uni");
        }

        try {
            getDialogLock().lock();
            TCUniMessageImpl msg = (TCUniMessageImpl) TcapFactory.createTCUniMessage();

            if (event.getApplicationContextName() != null) {
                DialogPortion dp = TcapFactory.createDialogPortion();
                DialogUniAPDU apdu = TcapFactory.createDialogAPDUUni();
                apdu.setDoNotSendProtocolVersion(this.provider.getStack().getDoNotSendProtocolVersion());
                apdu.setApplicationContextName(event.getApplicationContextName());
                if (event.getUserInformation() != null) {
                    apdu.setUserInformation(event.getUserInformation());
                }
                dp.setUnidirectional(true);
                dp.setDialogAPDU(apdu);
                msg.setDialogPortion(dp);

            }

            Component[] componentsToSend = prepareComponents(true);
            if(componentsToSend!=null) {
                msg.setComponent(componentsToSend);
            }

            AsnOutputStream aos = new AsnOutputStream();
            try {
                msg.encode(aos);
                if (this.provider.getStack().getStatisticsEnabled()) {
                    this.provider.getStack().getCounterProviderImpl().updateTcUniSentCount();
                }
                this.provider.send(aos.toByteArray(), event.getReturnMessageOnError(), data.getRemoteAddress(), data.getLocalAddress(),
                        data.getSeqControl(), data.getNetworkId(),data.getLocalSsn());
                data.clearScheduledComponentList();
            } catch (Exception e) {
                if (logger.isEnabledFor(Level.ERROR)) {
                    logger.error("Failed to send message: ", e);
                }
                throw new TCAPSendException("Failed to send TC-Uni message: " + e.getMessage(), e);
            } finally {
                release();
            }
        } finally {
            getDialogLock().unlock();
        }
    }

    @Override
    public void send(TCUserAbortRequest event) throws TCAPSendException {

        // is abort allowed in "Active" state ?
        if (!isStructured()) {
            throw new TCAPSendException("Unstructured dialog can not be aborted!");
        }

        try {
            getDialogLock().lock();

            if (data.getState() == TRPseudoState.InitialReceived || data.getState() == TRPseudoState.Active) {
                // allowed
                DialogPortion dp = null;
                if (event.getUserInformation() != null || event.getDialogServiceUserType() != null) {
                    // User information can be absent in TCAP V1

                    dp = TcapFactory.createDialogPortion();
                    dp.setUnidirectional(false);

                    if (event.getDialogServiceUserType() != null) {
                        // ITU T Q.774 Read Dialogue end on page 12 and 3.2.2
                        // Abnormal
                        // procedures on page 13 and 14
                        DialogResponseAPDU apdu = TcapFactory.createDialogAPDUResponse();
                        apdu.setDoNotSendProtocolVersion(this.provider.getStack().getDoNotSendProtocolVersion());
                        apdu.setApplicationContextName(event.getApplicationContextName());
                        apdu.setUserInformation(event.getUserInformation());

                        Result res = TcapFactory.createResult();
                        res.setResultType(ResultType.RejectedPermanent);
                        ResultSourceDiagnostic rsd = TcapFactory.createResultSourceDiagnostic();
                        rsd.setDialogServiceUserType(event.getDialogServiceUserType());
                        apdu.setResultSourceDiagnostic(rsd);
                        apdu.setResult(res);
                        dp.setDialogAPDU(apdu);
                    } else {
                        // When a BEGIN message has been received (i.e. the
                        // dialogue
                        // is
                        // in the "Initiation Received" state) containing a
                        // Dialogue
                        // Request (AARQ) APDU, the TC-User can abort for any
                        // user
                        // defined reason. In such a situation, the TC-User
                        // issues a
                        // TC-U-ABORT request primitive with the Abort Reason
                        // parameter
                        // absent or with set to any value other than either
                        // "application-context-name-not-supported" or
                        // dialogue-refused". In such a case, a Dialogue Abort (ABRT) APDU is generated with abort-source coded as "dialogue-service-user",
                        // and supplied as the User Data parameter of the
                        // TR-U-ABORT
                        // request primitive. User information (if any) provided
                        // in
                        // the
                        // TC-U-ABORT request primitive is coded in the
                        // user-information
                        // field of the ABRT APDU.
                        DialogAbortAPDU dapdu = TcapFactory.createDialogAPDUAbort();

                        AbortSource as = TcapFactory.createAbortSource();
                        as.setAbortSourceType(AbortSourceType.User);
                        dapdu.setAbortSource(as);
                        dapdu.setUserInformation(event.getUserInformation());
                        dp.setDialogAPDU(dapdu);
                    }
                }

                TCAbortMessageImpl msg = (TCAbortMessageImpl) TcapFactory.createTCAbortMessage();
                msg.setDestinationTransactionId(data.getRemoteTransactionId());
                msg.setDialogPortion(dp);

                if (data.getState() == TRPseudoState.InitialReceived) {
                    // local address may change, lets check it
                    if (event.getOriginatingAddress() != null && !event.getOriginatingAddress().equals(data.getLocalAddress())) {
                        data.setLocalAddress(event.getOriginatingAddress());
                    }
                }

                // no components
                AsnOutputStream aos = new AsnOutputStream();
                try {
                    msg.encode(aos);
                    if (this.provider.getStack().getStatisticsEnabled()) {
                        this.provider.getStack().getCounterProviderImpl().updateTcUserAbortSentCount();
                    }
                    this.provider.send(aos.toByteArray(), event.getReturnMessageOnError(), data.getRemoteAddress(),
                            data.getLocalAddress(), data.getSeqControl(), data.getNetworkId(),data.getLocalSsn());

                    data.clearScheduledComponentList();
                } catch (Exception e) {
                    // FIXME: remove freshly added invokes to free invoke ID??
                    if (logger.isEnabledFor(Level.ERROR)) {
                        e.printStackTrace();
                        logger.error("Failed to send message: ", e);
                    }
                    throw new TCAPSendException("Failed to send TC-U-Abort message: " + e.getMessage(), e);
                } finally {
                    release();
                }
            } else if (data.getState() == TRPseudoState.InitialSent) {
                release();
            }
        } finally {
            getDialogLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#sendComponent(org
     * .mobicents.protocols.ss7.tcap.api.tc.component.ComponentRequest)
     */
    @Override
    public void sendComponent(Component componentRequest) throws TCAPSendException {
        if (this.provider.getStack().getStatisticsEnabled()) {
            switch (componentRequest.getType()) {
            case Invoke:
                this.provider.getStack().getCounterProviderImpl().updateInvokeSentCount();

                Invoke inv = (Invoke) componentRequest;
                OperationCodeImpl oc = (OperationCodeImpl) inv.getOperationCode();
                if (oc != null) {
                    this.provider.getStack().getCounterProviderImpl().updateOutgoingInvokesPerOperationCode(oc.getStringValue());
                }
                break;
            case ReturnResult:
                this.provider.getStack().getCounterProviderImpl().updateReturnResultSentCount();
                break;
            case ReturnResultLast:
                this.provider.getStack().getCounterProviderImpl().updateReturnResultLastSentCount();
                break;
            case ReturnError:
                this.provider.getStack().getCounterProviderImpl().updateReturnErrorSentCount();

                ReturnError re = (ReturnError) componentRequest;
                ErrorCodeImpl ec = (ErrorCodeImpl) re.getErrorCode();
                if (ec != null) {
                    this.provider.getStack().getCounterProviderImpl().updateOutgoingErrorsPerErrorCode(ec.getStringValue());
                }
                break;
            case Reject:
                this.provider.getStack().getCounterProviderImpl().updateRejectSentCount();

                Reject rej = (Reject) componentRequest;
                ProblemImpl prob = (ProblemImpl) rej.getProblem();
                if (prob != null) {
                    this.provider.getStack().getCounterProviderImpl().updateOutgoingRejectPerProblem(prob.getStringValue());
                }
                break;
            }
        }

        try {
            getDialogLock().lock();
            if (componentRequest.getType() == ComponentType.Invoke) {
                InvokeImpl invoke = (InvokeImpl) componentRequest;
                invoke.setDialog(this);
                ITCAPOperation to = data.newTCAPOperation(invoke);
                // check if its taken!
                to.setState(OperationState.Pending);

                // if the Invoke timeout value has not be reset by TCAP-User
                // for this invocation we are setting it to the the TCAP stack
                // default value
                if (invoke.getTimeout() == TCAPStackImpl._EMPTY_INVOKE_TIMEOUT)
                    invoke.setTimeout(this.provider.getStack().getInvokeTimeout());
            } else {
                if (componentRequest.getType() != ComponentType.ReturnResult) {
                    // we are sending a response and removing invokeId from
                    // incomingInvokeList
                    data.removeIncomingInvoke(componentRequest.getInvokeId());
                }
            }
            data.addScheduledComponent(componentRequest);
        } catch(TCAPException e) {
            throw new TCAPSendException(e.getMessage(),e);
        } finally {
            getDialogLock().unlock();
        }
    }

    @Override
    public void processInvokeWithoutAnswer(Long invokeId) {
        data.removeIncomingInvoke(invokeId);
    }

    private Component[] prepareComponents(boolean updateState) throws TCAPSendException {
        List<Component> list=data.getScheduledComponentList();
        if(list==null || list.size()==0)
            return null;
        Component[] array=new Component[list.size()];
        list.toArray(array);
        if(updateState) {
            for (Component c : array) {
                if (c.getType() == ComponentType.Invoke) {
                    ITCAPOperation to = data.getTCAPOperation(c.getInvokeId());
                    // it must have been created during sendComponent
                    to.setState(OperationState.Sent);
                }
            }
        }
        return array;
    }

    @Override
    public int getMaxUserDataLength() {
        return this.provider.getMaxUserDataLength(data.getRemoteAddress(), data.getLocalAddress(), data.getNetworkId());
    }

    @Override
    public int getDataLength(TCBeginRequest event) throws TCAPSendException {

        TCBeginMessageImpl tcbm = (TCBeginMessageImpl) TcapFactory.createTCBeginMessage();

        if (event.getApplicationContextName() != null) {
            DialogPortion dp = TcapFactory.createDialogPortion();
            dp.setUnidirectional(false);
            DialogRequestAPDU apdu = TcapFactory.createDialogAPDURequest();
            apdu.setDoNotSendProtocolVersion(this.provider.getStack().getDoNotSendProtocolVersion());
            dp.setDialogAPDU(apdu);
            apdu.setApplicationContextName(event.getApplicationContextName());
            if (event.getUserInformation() != null) {
                apdu.setUserInformation(event.getUserInformation());
            }
            tcbm.setDialogPortion(dp);
        }

        // now comps
        tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(data.getLocalTransactionId()));
        Component[] componentsToSend = prepareComponents(false);
        if(componentsToSend!=null) {
            tcbm.setComponent(componentsToSend);
        }

        AsnOutputStream aos = new AsnOutputStream();
        try {
            tcbm.encode(aos);
        } catch (EncodeException e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to encode message while length testing: ", e);
            }
            throw new TCAPSendException("Error encoding TCBeginRequest", e);
        }
        return aos.size();
    }

    @Override
    public int getDataLength(TCContinueRequest event) throws TCAPSendException {

        TCContinueMessageImpl tcbm = (TCContinueMessageImpl) TcapFactory.createTCContinueMessage();

        if (event.getApplicationContextName() != null) {

            // set dialog portion
            DialogPortion dp = TcapFactory.createDialogPortion();
            dp.setUnidirectional(false);
            DialogResponseAPDU apdu = TcapFactory.createDialogAPDUResponse();
            apdu.setDoNotSendProtocolVersion(this.provider.getStack().getDoNotSendProtocolVersion());
            dp.setDialogAPDU(apdu);
            apdu.setApplicationContextName(event.getApplicationContextName());
            if (event.getUserInformation() != null) {
                apdu.setUserInformation(event.getUserInformation());
            }
            // WHERE THE HELL THIS COMES FROM!!!!
            // WHEN REJECTED IS USED !!!!!
            Result res = TcapFactory.createResult();
            res.setResultType(ResultType.Accepted);
            ResultSourceDiagnostic rsd = TcapFactory.createResultSourceDiagnostic();
            rsd.setDialogServiceUserType(DialogServiceUserType.Null);
            apdu.setResultSourceDiagnostic(rsd);
            apdu.setResult(res);
            tcbm.setDialogPortion(dp);

        }

        tcbm.setOriginatingTransactionId(Utils.encodeTransactionId(data.getLocalTransactionId()));
        tcbm.setDestinationTransactionId(data.getRemoteTransactionId());
        Component[] componentsToSend = prepareComponents(false);
        if(componentsToSend!=null) {
            tcbm.setComponent(componentsToSend);
        }

        AsnOutputStream aos = new AsnOutputStream();
        try {
            tcbm.encode(aos);
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to encode message while length testing: ", e);
            }
            throw new TCAPSendException("Error encoding TCContinueRequest", e);
        }

        return aos.size();
    }

    @Override
    public int getDataLength(TCEndRequest event) throws TCAPSendException {

        // TC-END request primitive issued in response to a TC-BEGIN
        // indication primitive
        TCEndMessageImpl tcbm = (TCEndMessageImpl) TcapFactory.createTCEndMessage();
        tcbm.setDestinationTransactionId(data.getRemoteTransactionId());

        if (event.getTerminationType() == TerminationType.Basic) {
            Component[] componentsToSend = prepareComponents(false);
            if(componentsToSend!=null) {
                tcbm.setComponent(componentsToSend);
            }
        }

        if (data.getState() == TRPseudoState.InitialReceived) {
            ApplicationContextName acn = event.getApplicationContextName();
            if (acn != null) { // acn & DialogPortion is absent in TCAP V1

                // set dialog portion
                DialogPortion dp = TcapFactory.createDialogPortion();
                dp.setUnidirectional(false);
                DialogResponseAPDU apdu = TcapFactory.createDialogAPDUResponse();
                apdu.setDoNotSendProtocolVersion(this.provider.getStack().getDoNotSendProtocolVersion());
                dp.setDialogAPDU(apdu);

                apdu.setApplicationContextName(event.getApplicationContextName());
                if (event.getUserInformation() != null) {
                    apdu.setUserInformation(event.getUserInformation());
                }

                // WHERE THE HELL THIS COMES FROM!!!!
                // WHEN REJECTED IS USED !!!!!
                Result res = TcapFactory.createResult();
                res.setResultType(ResultType.Accepted);
                ResultSourceDiagnostic rsd = TcapFactory.createResultSourceDiagnostic();
                rsd.setDialogServiceUserType(DialogServiceUserType.Null);
                apdu.setResultSourceDiagnostic(rsd);
                apdu.setResult(res);
                tcbm.setDialogPortion(dp);
            }
        }

        AsnOutputStream aos = new AsnOutputStream();
        try {
            tcbm.encode(aos);
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to encode message while length testing: ", e);
            }
            throw new TCAPSendException("Error encoding TCEndRequest", e);
        }

        return aos.size();
    }

    @Override
    public void processBegin(TCBeginMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {
        IDialogDataBase data=getBaseData();
        TCBeginIndicationImpl tcBeginIndication = null;
        try {
            getDialogLock().lock();


            // this is invoked ONLY for server.
            if (data.getState() != TRPseudoState.Idle) {
                // should we terminate dialog here?
                if (logger.isEnabledFor(Level.ERROR)) {
                    logger.error("Received Begin primitive, but state is not: " + TRPseudoState.Idle + ". Dialog: " + this);
                }
                sendAbnormalDialog();
                return;
            }
            restartIdleTimer();


            // lets setup
            data.setRemoteAddress(remoteAddress);
            data.setLocalAddress(localAddress);
            data.setRemoteTransactionId(msg.getOriginatingTransactionId());
            // convert to indications
            tcBeginIndication = (TCBeginIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider.getDialogPrimitiveFactory())
                    .createBeginIndication(this);

            tcBeginIndication.setDestinationAddress(localAddress);
            tcBeginIndication.setOriginatingAddress(remoteAddress);

            // if APDU and context data present, lets store it
            DialogPortion dialogPortion = msg.getDialogPortion();

            if (dialogPortion != null) {
                // this should not be null....
                DialogAPDU apdu = dialogPortion.getDialogAPDU();
                if (apdu.getType() != DialogAPDUType.Request) {
                    if (logger.isEnabledFor(Level.ERROR)) {
                        logger.error("Received non-Request APDU: " + apdu.getType() + ". Dialog: " + this);
                    }
                    sendAbnormalDialog();
                    return;
                }
                DialogRequestAPDU requestAPDU = (DialogRequestAPDU) apdu;
                data.setLastACN(requestAPDU.getApplicationContextName());
                data.setLastUI(requestAPDU.getUserInformation());
                tcBeginIndication.setApplicationContextName(data.getLastACN());
                tcBeginIndication.setUserInformation(data.getLastUI());
            }
            if (this.provider.getStack().getStatisticsEnabled()) {
                if (tcBeginIndication.getApplicationContextName() != null) {
                    String acn = ((ApplicationContextNameImpl) tcBeginIndication.getApplicationContextName()).getStringValue();
                    this.provider.getStack().getCounterProviderImpl().updateIncomingDialogsPerApplicatioContextName(acn);
                } else {
                    this.provider.getStack().getCounterProviderImpl().updateIncomingDialogsPerApplicatioContextName("");
                }
            }

            tcBeginIndication.setComponents(processOperationsState(msg.getComponent()));
            setState(TRPseudoState.InitialReceived);

            // lets deliver to provider
            this.provider.deliver(this, tcBeginIndication);

        } finally {
            getDialogLock().unlock();
        }
    }

    @Override
    public void processContinue(TCContinueMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {
        IDialogDataBase data=getBaseData();
        TCContinueIndicationImpl tcContinueIndication = null;
        try {
            getDialogLock().lock();
            if (data.getState() == TRPseudoState.InitialSent) {
                restartIdleTimer();
                tcContinueIndication = (TCContinueIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
                        .getDialogPrimitiveFactory()).createContinueIndication(this);

                // in continue remote address MAY change be changed, so lets
                // update!
                data.setRemoteAddress(remoteAddress);
                data.setRemoteTransactionId(msg.getOriginatingTransactionId());
                tcContinueIndication.setOriginatingAddress(remoteAddress);

                // here we will receive DialogResponse APDU - if request was
                // present!
                DialogPortion dialogPortion = msg.getDialogPortion();
                if (dialogPortion != null) {
                    // this should not be null....
                    DialogAPDU apdu = dialogPortion.getDialogAPDU();
                    if (apdu.getType() != DialogAPDUType.Response) {
                        if (logger.isEnabledFor(Level.ERROR)) {
                            logger.error("Received non-Response APDU: " + apdu.getType() + ". Dialog: " + this);
                        }
                        sendAbnormalDialog();
                        return;
                    }
                    DialogResponseAPDU responseAPDU = (DialogResponseAPDU) apdu;
                    // this will be present if APDU is present.
                    if (!responseAPDU.getApplicationContextName().equals(data.getLastACN())) {
                        data.setLastACN(responseAPDU.getApplicationContextName());
                    }
                    if (responseAPDU.getUserInformation() != null) {
                        data.setLastUI(responseAPDU.getUserInformation());
                    }
                    tcContinueIndication.setApplicationContextName(responseAPDU.getApplicationContextName());
                    tcContinueIndication.setUserInformation(responseAPDU.getUserInformation());
                } else if (data.isDpSentInBegin()) {
                    // ITU - T Q.774 3.2.2 : Abnormal procedure page 13

                    // when a dialogue portion is missing when its presence
                    // is
                    // mandatory (e.g. an AARQ APDU was sent in a Begin
                    // message,
                    // but
                    // no AARE APDU was received in the first backward
                    // Continue
                    // message) or when a dialogue portion is received
                    // inopportunely
                    // (e.g. a dialogue APDU is received during the active
                    // state
                    // of
                    // a transaction). At the side where the abnormality is
                    // detected, a TC-P-ABORT indication primitive is issued
                    // to
                    // the
                    // local TC-user with the "P-Abort" parameter in the
                    // primitive
                    // set to "abnormal dialogue". At the same time, a
                    // TR-U-ABORT
                    // request primitive is issued to the transaction
                    // sub-layer
                    // with
                    // an ABRT APDU as user data. The abort-source field of
                    // the
                    // ABRT
                    // APDU is set to "dialogue-service-provider" and the
                    // user
                    // information field is absent.

                    sendAbnormalDialog();
                    return;

                }
                tcContinueIndication.setOriginatingAddress(remoteAddress);
                // now comps
                tcContinueIndication.setComponents(processOperationsState(msg.getComponent()));
                // change state
                setState(TRPseudoState.Active);

                // lets deliver to provider
                this.provider.deliver(this, tcContinueIndication);

            } else if (data.getState() == TRPseudoState.Active) {
                restartIdleTimer();
                // XXX: here NO APDU will be present, hence, no ACN/UI change
                tcContinueIndication = (TCContinueIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
                        .getDialogPrimitiveFactory()).createContinueIndication(this);

                tcContinueIndication.setOriginatingAddress(remoteAddress);

                // now comps
                tcContinueIndication.setComponents(processOperationsState(msg.getComponent()));

                // lets deliver to provider
                this.provider.deliver(this, tcContinueIndication);

            } else {
                if (logger.isEnabledFor(Level.ERROR)) {
                    logger.error("Received Continue primitive, but state is not proper: " + data.getState() + ", Dialog: "
                            + this);
                }
                sendAbnormalDialog();
                return;
            }


        } finally {
            getDialogLock().unlock();
        }
    }

    @Override
    public void processEnd(TCEndMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {
        IDialogDataBase data=getBaseData();
        TCEndIndicationImpl tcEndIndication = null;
        try {
            getDialogLock().lock();

            try {

                restartIdleTimer();
                tcEndIndication = (TCEndIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
                        .getDialogPrimitiveFactory()).createEndIndication(this);

                if (data.getState() == TRPseudoState.InitialSent) {
                    // in end remote address MAY change be changed, so lets
                    // update!
                    data.setRemoteAddress(remoteAddress);
                    tcEndIndication.setOriginatingAddress(remoteAddress);
                }

                DialogPortion dialogPortion = msg.getDialogPortion();
                if (dialogPortion != null) {
                    DialogAPDU apdu = dialogPortion.getDialogAPDU();
                    if (apdu.getType() != DialogAPDUType.Response) {
                        if (logger.isEnabledFor(Level.ERROR)) {
                            logger.error("Received non-Response APDU: " + apdu.getType() + ". Dialog: " + this);
                        }
                        // we do not send "data.sendAbnormalDialog()"
                        // because no sense to send an answer to TC-END
                        return;
                    }
                    DialogResponseAPDU responseAPDU = (DialogResponseAPDU) apdu;
                    // this will be present if APDU is present.
                    if (!responseAPDU.getApplicationContextName().equals(data.getLastACN())) {
                        data.setLastACN(responseAPDU.getApplicationContextName());
                    }
                    if (responseAPDU.getUserInformation() != null) {
                        data.setLastUI(responseAPDU.getUserInformation());
                    }
                    tcEndIndication.setApplicationContextName(responseAPDU.getApplicationContextName());
                    tcEndIndication.setUserInformation(responseAPDU.getUserInformation());

                }
                // now comps
                tcEndIndication.setComponents(processOperationsState(msg.getComponent()));

                this.provider.deliver(this, tcEndIndication);

            } finally {
                release();
            }
        } finally {
            getDialogLock().unlock();
        }
    }

    @Override
    public void processAbort(TCAbortMessage msg, SccpAddress localAddress2, SccpAddress remoteAddress2) {
        IDialogDataBase data=getBaseData();

        try {
            getDialogLock().lock();

            try {
                Boolean IsAareApdu = false;
                Boolean IsAbrtApdu = false;
                ApplicationContextName acn = null;
                Result result = null;
                ResultSourceDiagnostic resultSourceDiagnostic = null;
                AbortSource abrtSrc = null;
                UserInformation userInfo = null;
                DialogPortion dp = msg.getDialogPortion();
                if (dp != null) {
                    DialogAPDU apdu = dp.getDialogAPDU();
                    if (apdu != null && apdu.getType() == DialogAPDUType.Abort) {
                        IsAbrtApdu = true;
                        DialogAbortAPDU abortApdu = (DialogAbortAPDU) apdu;
                        abrtSrc = abortApdu.getAbortSource();
                        userInfo = abortApdu.getUserInformation();
                    }
                    if (apdu != null && apdu.getType() == DialogAPDUType.Response) {
                        IsAareApdu = true;
                        DialogResponseAPDU resptApdu = (DialogResponseAPDU) apdu;
                        acn = resptApdu.getApplicationContextName();
                        result = resptApdu.getResult();
                        resultSourceDiagnostic = resptApdu.getResultSourceDiagnostic();
                        userInfo = resptApdu.getUserInformation();
                    }
                }

                PAbortCauseType type = msg.getPAbortCause();
                if (type == null) {
                    if ((abrtSrc != null && abrtSrc.getAbortSourceType() == AbortSourceType.Provider)) {
                        type = PAbortCauseType.AbnormalDialogue;
                    }
                    if ((resultSourceDiagnostic != null && resultSourceDiagnostic.getDialogServiceProviderType() != null)) {
                        if (resultSourceDiagnostic.getDialogServiceProviderType() == DialogServiceProviderType.NoCommonDialogPortion)
                            type = PAbortCauseType.NoCommonDialoguePortion;
                        else
                            type = PAbortCauseType.NoReasonGiven;
                    }
                }

                if (type != null) {

                    // its TC-P-Abort
                    TCPAbortIndicationImpl tcAbortIndication = (TCPAbortIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
                            .getDialogPrimitiveFactory()).createPAbortIndication(this);
                    tcAbortIndication.setPAbortCause(type);

                    this.provider.deliver(this, tcAbortIndication);

                } else {
                    // its TC-U-Abort
                    TCUserAbortIndicationImpl tcUAbortIndication = (TCUserAbortIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
                            .getDialogPrimitiveFactory()).createUAbortIndication(this);
                    if (IsAareApdu)
                        tcUAbortIndication.SetAareApdu();
                    if (IsAbrtApdu)
                        tcUAbortIndication.SetAbrtApdu();
                    tcUAbortIndication.setUserInformation(userInfo);
                    tcUAbortIndication.setAbortSource(abrtSrc);
                    tcUAbortIndication.setApplicationContextName(acn);
                    tcUAbortIndication.setResultSourceDiagnostic(resultSourceDiagnostic);

                    if (data.getState() == TRPseudoState.InitialSent) {
                        // in userAbort remote address MAY change be changed, so lets
                        // update!
                        data.setRemoteAddress(remoteAddress2);
                        tcUAbortIndication.setOriginatingAddress(remoteAddress2);
                    }

                    this.provider.deliver(this, tcUAbortIndication);
                }
            } finally {
                release();
            }

        } finally {
            getDialogLock().unlock();
        }
    }

    public void sendAbnormalDialog() {
        IDialogDataBase data=getBaseData();

        TCPAbortIndicationImpl tcAbortIndication = null;
        try {
            getDialogLock().lock();

            try {
                // sending to the remote side
                DialogPortion dp = TcapFactory.createDialogPortion();
                dp.setUnidirectional(false);

                DialogAbortAPDU dapdu = TcapFactory.createDialogAPDUAbort();

                AbortSource as = TcapFactory.createAbortSource();
                as.setAbortSourceType(AbortSourceType.Provider);

                dapdu.setAbortSource(as);
                dp.setDialogAPDU(dapdu);

                TCAbortMessageImpl msg = (TCAbortMessageImpl) TcapFactory.createTCAbortMessage();
                msg.setDestinationTransactionId(data.getRemoteTransactionId());
                msg.setDialogPortion(dp);

                AsnOutputStream aos = new AsnOutputStream();
                try {
                    msg.encode(aos);
                    if (this.provider.getStack().getStatisticsEnabled()) {
                        this.provider.getStack().getCounterProviderImpl().updateTcPAbortSentCount();
                    }
                    this.provider.send(aos.toByteArray(), false, data.getRemoteAddress(), data.getLocalAddress(), data.getSeqControl(), data.getNetworkId(),data.getLocalSsn());
                } catch (Exception e) {
                    if (logger.isEnabledFor(Level.ERROR)) {
                        logger.error("Failed to send message: ", e);
                    }
                }

                // sending to the local side
                tcAbortIndication = (TCPAbortIndicationImpl) ((DialogPrimitiveFactoryImpl) this.provider
                        .getDialogPrimitiveFactory()).createPAbortIndication(this);
                tcAbortIndication.setPAbortCause(PAbortCauseType.AbnormalDialogue);
                // tcAbortIndication.setLocalProviderOriginated(true);

                this.provider.deliver(this, tcAbortIndication);
            } finally {
                release();
                // data.clearScheduledComponentList();
            }
        } finally {
            getDialogLock().unlock();
        }
    }

    public int getDataLength(TCUniRequest event) throws TCAPSendException {

        TCUniMessageImpl msg = (TCUniMessageImpl) TcapFactory.createTCUniMessage();

        if (event.getApplicationContextName() != null) {
            DialogPortion dp = TcapFactory.createDialogPortion();
            DialogUniAPDU apdu = TcapFactory.createDialogAPDUUni();
            apdu.setDoNotSendProtocolVersion(this.provider.getStack().getDoNotSendProtocolVersion());
            apdu.setApplicationContextName(event.getApplicationContextName());
            if (event.getUserInformation() != null) {
                apdu.setUserInformation(event.getUserInformation());
            }
            dp.setUnidirectional(true);
            dp.setDialogAPDU(apdu);
            msg.setDialogPortion(dp);

        }

        Component[] componentsToSend = prepareComponents(false);
        if(componentsToSend!=null) {
            msg.setComponent(componentsToSend);
        }

        AsnOutputStream aos = new AsnOutputStream();
        try {
            msg.encode(aos);
        } catch (Exception e) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error("Failed to encode message while length testing: ", e);
            }
            throw new TCAPSendException("Error encoding TCUniRequest", e);
        }

        return aos.size();
    }

    // /////////////////
    // LOCAL METHODS //
    // /////////////////






    protected Component[] processOperationsState(Component[] components) {
        if (components == null) {
            return null;
        }
// TODO: remove me
        data.listTCAPOpeartions();

        List<Component> resultingIndications = new ArrayList<Component>();
        for (Component ci : components) {
            Long invokeId;
            if (ci.getType() == ComponentType.Invoke)
                invokeId = ((InvokeImpl) ci).getLinkedId();
            else
                invokeId = ci.getInvokeId();
            ITCAPOperation operation= null;
            if (invokeId != null) {
                operation = data.getTCAPOperation(invokeId);
            }

            switch (ci.getType()) {
                case Invoke:
                    if (invokeId != null && operation == null) {
                        logger.error(String.format("Rx : %s but no sent Invoke for linkedId exists", ci));

                        Problem p = new ProblemImpl();
                        p.setInvokeProblemType(InvokeProblemType.UnrechognizedLinkedID);
                        addReject(resultingIndications, ci.getInvokeId(), p);
                    } else {
                        if (operation != null) {
                            ((InvokeImpl) ci).setLinkedInvoke(operation.getInvoke());
                        }
                        if (!data.addIncomingInvoke(ci.getInvokeId())) {
                            logger.error(String.format("Rx : %s but there is already Invoke with this invokeId", ci));

                            Problem p = new ProblemImpl();
                            p.setInvokeProblemType(InvokeProblemType.DuplicateInvokeID);
                            addReject(resultingIndications, ci.getInvokeId(), p);
                        } else {
                            resultingIndications.add(ci);
                        }
                    }
                    break;

                case ReturnResult:

                    if (operation == null) {
                        logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));

                        Problem p = new ProblemImpl();
                        p.setReturnResultProblemType(ReturnResultProblemType.UnrecognizedInvokeID);
                        addReject(resultingIndications, ci.getInvokeId(), p);
                    } else if (operation.getInvoke().getInvokeClass() != InvokeClass.Class1 && operation.getInvoke().getInvokeClass() != InvokeClass.Class3) {
                        logger.error(String.format("Rx : %s but Invoke class is not 1 or 3", ci));

                        Problem p = new ProblemImpl();
                        p.setReturnResultProblemType(ReturnResultProblemType.ReturnResultUnexpected);
                        addReject(resultingIndications, ci.getInvokeId(), p);
                    } else {
                        resultingIndications.add(ci);
                        ReturnResultImpl rri = (ReturnResultImpl) ci;
                        if (rri.getOperationCode() == null)
                            rri.setOperationCode(operation.getInvoke().getOperationCode());
                    }
                    break;

                case ReturnResultLast:

                    if (operation == null) {
                        logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));

                        Problem p = new ProblemImpl();
                        p.setType(ProblemType.ReturnResult);
                        p.setReturnResultProblemType(ReturnResultProblemType.UnrecognizedInvokeID);
                        addReject(resultingIndications, ci.getInvokeId(), p);
                    } else if (operation.getInvoke().getInvokeClass() != InvokeClass.Class1 && operation.getInvoke().getInvokeClass() != InvokeClass.Class3) {
                        logger.error(String.format("Rx : %s but Invoke class is not 1 or 3", ci));

                        Problem p = new ProblemImpl();
                        p.setReturnResultProblemType(ReturnResultProblemType.ReturnResultUnexpected);
                        addReject(resultingIndications, ci.getInvokeId(), p);
                    } else {
                        ReturnResultLastImpl rri = (ReturnResultLastImpl) ci;
                        if (rri.getOperationCode() == null)
                            rri.setOperationCode(operation.getInvoke().getOperationCode());
                        operation.onReturnResultLast();
                        if (operation.isSuccessReported()) {
                            resultingIndications.add(ci);
                        }
                    }
                    break;

                case ReturnError:
                    if (operation == null) {
                        logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));

                        Problem p = new ProblemImpl();
                        p.setReturnErrorProblemType(ReturnErrorProblemType.UnrecognizedInvokeID);
                        addReject(resultingIndications, ci.getInvokeId(), p);
                    } else if (operation.getInvoke().getInvokeClass() != InvokeClass.Class1 && operation.getInvoke().getInvokeClass() != InvokeClass.Class2) {
                        logger.error(String.format("Rx : %s but Invoke class is not 1 or 2", ci));

                        Problem p = new ProblemImpl();
                        p.setReturnErrorProblemType(ReturnErrorProblemType.ReturnErrorUnexpected);
                        addReject(resultingIndications, ci.getInvokeId(), p);
                    } else {
                        operation.onError();
                        if (operation.isErrorReported()) {
                            resultingIndications.add(ci);
                        }
                    }
                    break;

                case Reject:
                    Reject rej = (Reject) ci;
                    if (operation != null) {
                        // If the Reject Problem is the InvokeProblemType we
                        // should move the invoke to the idle state
                        Problem problem = rej.getProblem();
                        if (!rej.isLocalOriginated() && problem.getInvokeProblemType() != null)
                            operation.onReject();
                    }
                    if (rej.isLocalOriginated() && data.isStructured()) {
                        try {
                            // this is a local originated Reject - we are rejecting an incoming component
                            // we need to send a Reject also to a peer
                            sendComponent(rej);
                        } catch (TCAPSendException e) {
                            logger.error("TCAPSendException when sending Reject component : Dialog: " + this, e);
                        }
                    }
                    resultingIndications.add(ci);
                    break;

                default:
                    resultingIndications.add(ci);
                    break;
            }
        }

        components = new Component[resultingIndications.size()];
        components = resultingIndications.toArray(components);

        processRcvdComp(components);

        return components;
    }

    private void addReject(List<Component> resultingIndications, Long invokeId, Problem p) {
        try {
            Reject rej = TcapFactory.createComponentReject();
            rej.setLocalOriginated(true);
            rej.setInvokeId(invokeId);
            rej.setProblem(p);

            resultingIndications.add(rej);

            if (data.isStructured())
                sendComponent(rej);
        } catch (TCAPSendException e) {
            logger.error(String.format("Error sending Reject component", e));
        }
    }

    public void setState(TRPseudoState newState) {
        try {
            getDialogLock().lock();
            // add checks?
            if (data.getState() == TRPseudoState.Expunged) {
                return;
            }
            data.setState(newState);
            if (newState == TRPseudoState.Expunged) {
                stopIdleTimer();

                provider.release(this);
            }
        } finally {
            getDialogLock().unlock();
        }

    }

    public void startIdleTimer() {
        if (!data.isStructured())
            return;

        try {
            getDialogLock().lock();
            if (data.getIdleTimerHandle() != null) {
                throw new IllegalStateException();
            }

            IdleTimerTask t = new IdleTimerTask(getLocalDialogId());
            data.setIdleTimerHandle(provider.getTimerFacility().schedule(t, data.getIdleTaskTimeout(), TimeUnit.MILLISECONDS));

        } finally {
            getDialogLock().unlock();
        }
    }

    private static class IdleTimerTask implements ITimerTask {
        final Long dialogId;
        IdleTimerTask(Long dialogId) {
            this.dialogId=dialogId;
        }

        @Override
        public String getId() {
            return "Dialog/IdleTimer/"+dialogId;
        }

        @Override
        public void handleTimeEvent(TCAPProviderImpl tpi) {
            IDialog dlg = tpi.getDialogDataStorage().getDialog(dialogId);
            dlg.getDialogLock().lock();
            try {
                if (dlg == null) {
                    logger.warn("Handle Idle Timer - dialog not found :" + dialogId);
                    return;
                }
                dlg.handleIdleTimeout();
            } finally {
                dlg.getDialogLock().unlock();
            }
        }
    }

    // ////////////////////
    // IND like methods //
    // ///////////////////
//    @Override
    public void operationEnded(ITCAPOperation tcInvokeRequestImpl) {
        try {
            getDialogLock().lock();
            // this op died cause of timeout, TC-L-CANCEL!
            tcInvokeRequestImpl.free();
            // lets call listener
            // This is done actually with COmponentIndication ....
        } finally {
            getDialogLock().unlock();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#operationEnded(
     * org.mobicents.protocols.ss7.tcap.tc.component.TCInvokeRequestImpl)
     */
    public void operationTimedOut(ITCAPOperation tcapOp,Invoke ii) {
        // this op died cause of timeout, TC-L-CANCEL!

        try {
            getDialogLock().lock();
            tcapOp.free();
        } finally {
            getDialogLock().unlock();
        }

        ((InvokeImpl)ii).setDialog(this);
        this.provider.operationTimedOut(ii);
    }

    @Override
    public void handleOperationTimeout(Long invokeId) {
        ITCAPOperation op = data.getTCAPOperation(invokeId);
        if(op!=null)
            op.handleOperationTimeout();
    }

    @Override
    public TCAPProviderImpl getProvider() {
        return provider;
    }

    // TC-TIMER-RESET
    public void resetTimer(Long invokeId) throws TCAPException {
        try {
            getDialogLock().lock();
            ITCAPOperation op=data.getTCAPOperation(invokeId);
            if (op== null) {
                throw new TCAPException("No operation with this ID");
            }
            op.startTimer();
        } finally {
            getDialogLock().unlock();
        }
    }

    @Override
    public boolean getPreviewMode() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return super.toString() + ": Local[" + getLocalDialogId() + "] Remote[" + getRemoteDialogId()
                + "], LocalAddress[" + data.getLocalAddress() + "] RemoteAddress[" + data.getRemoteAddress() + "]";
    }

}
