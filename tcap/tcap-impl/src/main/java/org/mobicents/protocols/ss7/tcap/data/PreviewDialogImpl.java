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
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
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
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcap.asn.ProblemImpl;
import org.mobicents.protocols.ss7.tcap.asn.Result;
import org.mobicents.protocols.ss7.tcap.asn.ResultSourceDiagnostic;
import org.mobicents.protocols.ss7.tcap.asn.ReturnResultImpl;
import org.mobicents.protocols.ss7.tcap.asn.ReturnResultLastImpl;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
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

public class PreviewDialogImpl extends DialogBaseImpl {

    private static final Logger logger = Logger.getLogger(PreviewDialogImpl.class);

    final IPreviewDialogData data;
    private final boolean sideB;


    PreviewDialogImpl(TCAPProviderImpl provider,IPreviewDialogData data,boolean sideB) {
        super(provider);
        this.data=data;
        this.sideB=sideB;
    }

    public void release() {
        if (data.isStructured() && provider.getStack().getStatisticsEnabled()) {
            long lg = System.currentTimeMillis() - data.getStartDialogTime();
            provider.getStack().getCounterProviderImpl().updateAllDialogsDuration(lg);
        }

        data.setState(TRPseudoState.Expunged);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#cancelInvocation (java.lang.Long)
     */
    @Override
    public boolean cancelInvocation(Long invokeId) throws TCAPException {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents
     * .protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest)
     */
    public void send(TCBeginRequest event) throws TCAPSendException {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents
     * .protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest)
     */
    public void send(TCContinueRequest event) throws TCAPSendException {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#send(org.mobicents
     * .protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest)
     */
    public void send(TCEndRequest event) throws TCAPSendException {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#sendUni()
     */
    public void send(TCUniRequest event) throws TCAPSendException {
    }

    public void send(TCUserAbortRequest event) throws TCAPSendException {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#sendComponent(org
     * .mobicents.protocols.ss7.tcap.api.tc.component.ComponentRequest)
     */
    public void sendComponent(Component componentRequest) throws TCAPSendException {
    }

    public void processInvokeWithoutAnswer(Long invokeId) {
    }

    public int getMaxUserDataLength() {

        return provider.getMaxUserDataLength(data.getRemoteAddress(), data.getLocalAddress(), data.getNetworkId());
    }

    public int getDataLength(TCBeginRequest event) throws TCAPSendException {
        throw new TCAPSendException("Operation not supported in preview mode");
    }

    public int getDataLength(TCContinueRequest event) throws TCAPSendException {
        throw new TCAPSendException("Operation not supported in preview mode");
    }

    public int getDataLength(TCEndRequest event) throws TCAPSendException {
        throw new TCAPSendException("Operation not supported in preview mode");
    }

    public int getDataLength(TCUniRequest event) throws TCAPSendException {
        throw new TCAPSendException("Operation not supported in preview mode");
    }


    @Override
    public void processBegin(TCBeginMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {
        IDialogDataBase data=getBaseData();
        TCBeginIndicationImpl tcBeginIndication = null;
        try {
            data.getDialogLock().lock();
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
            // lets deliver to provider
            this.provider.deliver(this, tcBeginIndication);

        } finally {
            data.getDialogLock().unlock();
        }
    }

    @Override
    public void processContinue(TCContinueMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {

        TCContinueIndicationImpl tcContinueIndication = null;
        try {
            getDialogLock().lock();


            tcContinueIndication = (TCContinueIndicationImpl) ((DialogPrimitiveFactoryImpl) provider
                    .getDialogPrimitiveFactory()).createContinueIndication(this);
            data.setRemoteTransactionId(msg.getOriginatingTransactionId());

            // here we will receive DialogResponse APDU - if request was
            // present!
            DialogPortion dialogPortion = msg.getDialogPortion();
            if (dialogPortion != null) {
                // this should not be null....
                DialogAPDU apdu = dialogPortion.getDialogAPDU();
                if (apdu.getType() == DialogAPDUType.Response) {
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
                }
            }
            tcContinueIndication.setOriginatingAddress(remoteAddress);
            // now comps
            tcContinueIndication.setComponents(processOperationsState(msg.getComponent()));

            // lets deliver to provider
            provider.deliver(this, tcContinueIndication);


        } finally {
            getDialogLock().unlock();
        }
    }

    @Override
    public void processEnd(TCEndMessage msg, SccpAddress localAddress, SccpAddress remoteAddress) {
        TCEndIndicationImpl tcEndIndication = null;
        try {
            getDialogLock().lock();

            try {

                tcEndIndication = (TCEndIndicationImpl) ((DialogPrimitiveFactoryImpl) provider
                        .getDialogPrimitiveFactory()).createEndIndication(this);

                DialogPortion dialogPortion = msg.getDialogPortion();
                if (dialogPortion != null) {
                    DialogAPDU apdu = dialogPortion.getDialogAPDU();
                    if (apdu.getType() == DialogAPDUType.Response) {
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
                }
                // now comps
                tcEndIndication.setComponents(processOperationsState(msg.getComponent()));
                provider.deliver(this, tcEndIndication);

            } finally {
                release();
            }
        } finally {
            getDialogLock().unlock();
        }
    }

    @Override
    public  void processAbort(TCAbortMessage msg, SccpAddress localAddress2, SccpAddress remoteAddress2) {

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
                    TCPAbortIndicationImpl tcAbortIndication = (TCPAbortIndicationImpl) ((DialogPrimitiveFactoryImpl) provider
                            .getDialogPrimitiveFactory()).createPAbortIndication(this);
                    tcAbortIndication.setPAbortCause(type);

                    provider.deliver(this, tcAbortIndication);

                } else {
                    // its TC-U-Abort
                    TCUserAbortIndicationImpl tcUAbortIndication = (TCUserAbortIndicationImpl) ((DialogPrimitiveFactoryImpl) provider
                            .getDialogPrimitiveFactory()).createUAbortIndication(this);
                    if (IsAareApdu)
                        tcUAbortIndication.SetAareApdu();
                    if (IsAbrtApdu)
                        tcUAbortIndication.SetAbrtApdu();
                    tcUAbortIndication.setUserInformation(userInfo);
                    tcUAbortIndication.setAbortSource(abrtSrc);
                    tcUAbortIndication.setApplicationContextName(acn);
                    tcUAbortIndication.setResultSourceDiagnostic(resultSourceDiagnostic);

                    if (getState() == TRPseudoState.InitialSent) {
                        // in userAbort remote address MAY change be changed, so lets
                        // update!
                        data.setRemoteAddress(remoteAddress2);
                        tcUAbortIndication.setOriginatingAddress(remoteAddress2);
                    }

                    provider.deliver(this, tcUAbortIndication);
                }
            } finally {
                release();
            }

        } finally {
            getDialogLock().unlock();
        }
    }

    @Override
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

    @Override
    public void operationTimedOut(ITCAPOperation invoke, Invoke i) {

    }

    @Override
    public void handleOperationTimeout(Long invokeId) {
        ITCAPOperation tc = data.getTCAPOperation(sideB, invokeId);
        if(tc!=null)
            tc.handleOperationTimeout();
    }

    @Override
    public TCAPProviderImpl getProvider() {
        return provider;
    }

    @Override
    public void setLocalSsn(int ssn) {
        getBaseData().setLocalSsn(ssn);
    }

    @Override
    public void sendAbnormalDialog() {
    }

    protected Component[] processOperationsState(Component[] components) {
        if (components == null) {
            return null;
        }

        List<Component> resultingIndications = new ArrayList<Component>();
        for (Component ci : components) {
            Long invokeId;
            if (ci.getType() == ComponentType.Invoke)
                invokeId = ((InvokeImpl) ci).getLinkedId();
            else
                invokeId = ci.getInvokeId();
            ITCAPOperation invoke = null;
            int index = 0;
            if (invokeId != null) {
                invoke = data.getTCAPOperation(sideB,invokeId);
            }

            switch (ci.getType()) {

                case Invoke:
                    if (invokeId != null && invoke == null) {
                        logger.error(String.format("Rx : %s but no sent Invoke for linkedId exists", ci));

                        Problem p = new ProblemImpl();
                        p.setInvokeProblemType(InvokeProblemType.UnrechognizedLinkedID);
                        addReject(resultingIndications, ci.getInvokeId(), p);
                    } else {
                        if (invoke != null) {
                            ((InvokeImpl) ci).setLinkedInvoke(invoke.getInvoke());
                        }
                        resultingIndications.add(ci);
                        ITCAPOperation to=data.newTCAPOperation(sideB,this,(Invoke)ci);
                        to.setState(OperationState.Sent);
                    }
                    break;

                case ReturnResult:

                    if (invoke == null) {
                        logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));

                        Problem p = new ProblemImpl();
                        p.setReturnResultProblemType(ReturnResultProblemType.UnrecognizedInvokeID);
                        addReject(resultingIndications, ci.getInvokeId(), p);
                    } else if (invoke.getInvoke().getInvokeClass() != InvokeClass.Class1 && invoke.getInvoke().getInvokeClass() != InvokeClass.Class3) {
                        logger.error(String.format("Rx : %s but Invoke class is not 1 or 3", ci));

                        Problem p = new ProblemImpl();
                        p.setReturnResultProblemType(ReturnResultProblemType.ReturnResultUnexpected);
                        addReject(resultingIndications, ci.getInvokeId(), p);
                    } else {
                        resultingIndications.add(ci);
                        ReturnResultImpl rri = (ReturnResultImpl) ci;
                        if (rri.getOperationCode() == null)
                            rri.setOperationCode(invoke.getInvoke().getOperationCode());
                    }
                    break;

                case ReturnResultLast:

                    if (invoke == null) {
                        logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));

                        Problem p = new ProblemImpl();
                        p.setType(ProblemType.ReturnResult);
                        p.setReturnResultProblemType(ReturnResultProblemType.UnrecognizedInvokeID);
                        addReject(resultingIndications, ci.getInvokeId(), p);
                    } else if (invoke.getInvoke().getInvokeClass() != InvokeClass.Class1 && invoke.getInvoke().getInvokeClass() != InvokeClass.Class3) {
                        logger.error(String.format("Rx : %s but Invoke class is not 1 or 3", ci));

                        Problem p = new ProblemImpl();
                        p.setReturnResultProblemType(ReturnResultProblemType.ReturnResultUnexpected);
                        addReject(resultingIndications, ci.getInvokeId(), p);
                    } else {
                        invoke.onReturnResultLast();
                        if (invoke.isSuccessReported()) {
                            resultingIndications.add(ci);
                        }
                        ReturnResultLastImpl rri = (ReturnResultLastImpl) ci;
                        if (rri.getOperationCode() == null)
                            rri.setOperationCode(invoke.getInvoke().getOperationCode());
                    }
                    break;

                case ReturnError:
                    if (invoke == null) {
                        logger.error(String.format("Rx : %s but there is no corresponding Invoke", ci));

                        Problem p = new ProblemImpl();
                        p.setReturnErrorProblemType(ReturnErrorProblemType.UnrecognizedInvokeID);
                        addReject(resultingIndications, ci.getInvokeId(), p);
                    } else if (invoke.getInvoke().getInvokeClass() != InvokeClass.Class1 && invoke.getInvoke().getInvokeClass() != InvokeClass.Class2) {
                        logger.error(String.format("Rx : %s but Invoke class is not 1 or 2", ci));

                        Problem p = new ProblemImpl();
                        p.setReturnErrorProblemType(ReturnErrorProblemType.ReturnErrorUnexpected);
                        addReject(resultingIndications, ci.getInvokeId(), p);
                    } else {
                        invoke.onError();
                        if (invoke.isErrorReported()) {
                            resultingIndications.add(ci);
                        }
                    }
                    break;

                case Reject:
                    Reject rej = (Reject) ci;
                    if (invoke != null) {
                        // If the Reject Problem is the InvokeProblemType we
                        // should move the invoke to the idle state
                        Problem problem = rej.getProblem();
                        if (!rej.isLocalOriginated() && problem.getInvokeProblemType() != null)
                            invoke.onReject();
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

    // Rejects for Preview are added just to update counters
    private void addReject(List<Component> resultingIndications, Long invokeId, Problem p) {
        Reject rej = TcapFactory.createComponentReject();
        rej.setLocalOriginated(true);
        rej.setInvokeId(invokeId);
        rej.setProblem(p);

        resultingIndications.add(rej);
    }

    protected void setState(TRPseudoState newState) {
        try {
            getDialogLock().lock();
            // add checks?
            if (getState() == TRPseudoState.Expunged) {
                return;
            }
            setState(newState);
            if (newState == TRPseudoState.Expunged) {
                provider.release(this);
            }
        } finally {
            getDialogLock().unlock();
        }
    }

    // ////////////////////
    // IND like methods //
    // ///////////////////
    public void operationEnded(InvokeImpl tcInvokeRequestImpl) {

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog#operationEnded(
     * org.mobicents.protocols.ss7.tcap.tc.component.TCInvokeRequestImpl)
     */
    public void operationTimedOut(InvokeImpl invoke) {
        // preview operations never time out
        provider.operationTimedOut(invoke);
    }

    // TC-TIMER-RESET
    public void resetTimer(Long invokeId) throws TCAPException {
        // preview operations never time out
    }

    @Override
    public void keepAlive() {
    }


    public boolean getPreviewMode() {
        return true;
    }

    @Override
    IDialogDataBase getBaseData() {
        return data;
    }

    // @Override
    public int getLocalSsn() {
        return getBaseData().getLocalSsn();
    }

    @Override
    public Long getNewInvokeId() throws TCAPException {
        return null;
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



    public void startIdleTimer() {
        try {
            getDialogLock().lock();
            if (data.getIdleTimerHandle() != null) {
                throw new IllegalStateException();
            }

            IdleTimerTask t = new IdleTimerTask(data.getPreviewDialogDataKey1());
            data.setIdleTimerHandle(provider.getTimerFacility().schedule(t, data.getIdleTaskTimeout(), TimeUnit.MILLISECONDS));

        } finally {
            getDialogLock().unlock();
        }
    }

    public PreviewDialogDataKey getPreviewDialogDataKey1() {
        return data.getPreviewDialogDataKey1();
    }

    public PreviewDialogDataKey getPreviewDialogDataKey2() {
        return data.getPreviewDialogDataKey2();
    }

    public IPreviewDialogData getPrevewDialogData() {
        return data;
    }

    private static class IdleTimerTask implements ITimerTask {
        final PreviewDialogDataKey key;
        IdleTimerTask(PreviewDialogDataKey key) {
            this.key=key;
        }

        @Override
        public String getId() {
            return "PreviewDialog/IdleTimer/"+key;
        }

        @Override
        public void handleTimeEvent(TCAPProviderImpl tpi) {
            IDialog dlg=tpi.getPreviewDialogDataStorage().getPreviewDialog(key, null, null, null, 0);

            if(dlg!=null) {
                dlg.handleIdleTimeout();
            } else {
                logger.warn("Timeout occurred for preview dialog, which no longer exists:"+key);
            }
        }
    }

    public void handleIdleTimeout() {
        getDialogLock().lock();
        try {
            provider.timeout(this);
            provider.removePreviewDialog(this);
        } finally{
            getDialogLock().unlock();
        }
    }
}
