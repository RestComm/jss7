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

package org.mobicents.protocols.ss7.map;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPServiceBase;
import org.mobicents.protocols.ss7.map.api.dialog.MAPDialogState;
import org.mobicents.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.mobicents.protocols.ss7.map.api.dialog.Reason;
import org.mobicents.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.errors.MAPErrorMessageImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.MessageType;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 *
 * MAP-DialoguePDU ::= CHOICE { map-open [0] MAP-OpenInfo, map-accept [1] MAP-AcceptInfo, map-close [2] MAP-CloseInfo,
 * map-refuse [3] MAP-RefuseInfo, map-userAbort [4] MAP-UserAbortInfo, map-providerAbort [5] MAP-ProviderAbortInfo}
 *
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 */
public abstract class MAPDialogImpl implements MAPDialog {
    private static final Logger logger = Logger.getLogger(MAPDialogImpl.class);

    protected Dialog tcapDialog = null;
    protected MAPProviderImpl mapProviderImpl = null;
    protected MAPServiceBase mapService = null;

    // Application Context of this Dialog
    protected MAPApplicationContext appCntx;

    protected AddressString destReference;
    protected AddressString origReference;
    protected MAPExtensionContainer extContainer = null;
    protected AddressString receivedOrigReference;
    protected AddressString receivedDestReference;
    protected MAPExtensionContainer receivedExtensionContainer;

    protected MAPDialogState state = MAPDialogState.IDLE;

    // private Set<Long> incomingInvokeList = new HashSet<Long>();

    protected boolean eriStyle;
    protected IMSI eriImsi;
    protected AddressString eriVlrNo;

    private boolean returnMessageOnError = false;
    protected MessageType tcapMessageType;
    protected DelayedAreaState delayedAreaState;

    protected MAPDialogImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
            MAPServiceBase mapService, AddressString origReference, AddressString destReference) {
        this.appCntx = appCntx;
        this.tcapDialog = tcapDialog;
        this.mapProviderImpl = mapProviderImpl;
        this.mapService = mapService;
        this.destReference = destReference;
        this.origReference = origReference;
    }

    public void setReturnMessageOnError(boolean val) {
        returnMessageOnError = val;
    }

    public boolean getReturnMessageOnError() {
        return returnMessageOnError;
    }

    /**
     *
     * @return - local sccp address.
     */
    public SccpAddress getLocalAddress() {
        return this.tcapDialog.getLocalAddress();
    }

    public void setLocalAddress(SccpAddress localAddress) {
        this.tcapDialog.setLocalAddress(localAddress);
    }

    /**
     *
     * @return - sccp address of calling party
     */
    public SccpAddress getRemoteAddress() {
        return this.tcapDialog.getRemoteAddress();
    }

    public void setRemoteAddress(SccpAddress remoteAddress) {
        this.tcapDialog.setRemoteAddress(remoteAddress);
    }

    public MessageType getTCAPMessageType() {
        return tcapMessageType;
    }

    public int getNetworkId() {
        return this.tcapDialog.getNetworkId();
    }

    public void setNetworkId(int networkId) {
        this.tcapDialog.setNetworkId(networkId);
    }

    public void keepAlive() {
        this.tcapDialog.keepAlive();
    }

    public Long getLocalDialogId() {
        return tcapDialog.getLocalDialogId();
    }

    public Long getRemoteDialogId() {
        return tcapDialog.getRemoteDialogId();
    }

    public MAPServiceBase getService() {
        return this.mapService;
    }

    public Dialog getTcapDialog() {
        return tcapDialog;
    }

    public void release() {
        this.setState(MAPDialogState.EXPUNGED);

        if (this.tcapDialog != null)
            this.tcapDialog.release();
    }

    public void setExtentionContainer(MAPExtensionContainer extContainer) {
        this.extContainer = extContainer;
    }

    /**
     * Adding the new incoming invokeId into incomingInvokeList list
     *
     * @param invokeId
     * @return false: failure - this invokeId already present in the list
     */
    // public boolean addIncomingInvokeId(Long invokeId) {
    // synchronized (this.incomingInvokeList) {
    // if (this.incomingInvokeList.contains(invokeId))
    // return false;
    // else {
    // this.incomingInvokeList.add(invokeId);
    // return true;
    // }
    // }
    // }
    //
    // public void removeIncomingInvokeId(Long invokeId) {
    // synchronized (this.incomingInvokeList) {
    // this.incomingInvokeList.remove(invokeId);
    // }
    // }
    //
    // public Boolean checkIncomingInvokeIdExists(Long invokeId) {
    // synchronized (this.incomingInvokeList) {
    // return this.incomingInvokeList.contains(invokeId);
    // }
    // }

    public AddressString getReceivedOrigReference() {
        return receivedOrigReference;
    }

    public MAPExtensionContainer getReceivedExtensionContainer() {
        return receivedExtensionContainer;
    }

    public AddressString getReceivedDestReference() {
        return receivedDestReference;
    }

    public void abort(MAPUserAbortChoice mapUserAbortChoice) throws MAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        try {
            this.getTcapDialog().getDialogLock().lock();

            // Dialog is not started or has expunged - we need not send
            // TC-U-ABORT,
            // only Dialog removing
            if (this.getState() == MAPDialogState.EXPUNGED || this.getState() == MAPDialogState.IDLE) {
                this.setState(MAPDialogState.EXPUNGED);
                return;
            }

            this.mapProviderImpl.fireTCAbortUser(this.getTcapDialog(), mapUserAbortChoice, this.extContainer,
                    this.getReturnMessageOnError());
            this.extContainer = null;

            this.setState(MAPDialogState.EXPUNGED);
        } finally {
            this.getTcapDialog().getDialogLock().unlock();
        }
    }

    public void refuse(Reason reason) throws MAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        try {
            this.getTcapDialog().getDialogLock().lock();

            // Dialog must be in the InitialReceived state
            if (this.getState() != MAPDialogState.INITIAL_RECEIVED) {
                throw new MAPException("Refuse can be called in the Dialog InitialReceived state");
            }

            this.mapProviderImpl.fireTCAbortRefused(this.getTcapDialog(), reason, this.extContainer,
                    this.getReturnMessageOnError());
            this.extContainer = null;

            this.setState(MAPDialogState.EXPUNGED);
        } finally {
            this.getTcapDialog().getDialogLock().unlock();
        }
    }

    public void close(boolean prearrangedEnd) throws MAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        try {
            this.getTcapDialog().getDialogLock().lock();

            switch (this.tcapDialog.getState()) {
                case InitialReceived:
                    ApplicationContextName acn = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
                            .createApplicationContextName(this.appCntx.getOID());

                    this.mapProviderImpl.fireTCEnd(this.getTcapDialog(), true, prearrangedEnd, acn, this.extContainer,
                            this.getReturnMessageOnError());
                    this.extContainer = null;

                    this.setState(MAPDialogState.EXPUNGED);
                    break;

                case Active:
                    this.mapProviderImpl.fireTCEnd(this.getTcapDialog(), false, prearrangedEnd, null, null,
                            this.getReturnMessageOnError());

                    this.setState(MAPDialogState.EXPUNGED);
                    break;

                case Idle:
                    throw new MAPException("Awaiting TC-BEGIN to be sent, can not send another dialog initiating primitive!");
                case InitialSent: // we have sent TC-BEGIN already, need to wait
                    throw new MAPException("Awaiting TC-BEGIN response, can not send another dialog initiating primitive!");
                case Expunged: // dialog has been terminated on TC level, cant send
                    throw new MAPException("Dialog has been terminated, can not send primitives!");
            }
        } finally {
            this.getTcapDialog().getDialogLock().unlock();
        }
    }

    public void closeDelayed(boolean prearrangedEnd) throws MAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        if (this.delayedAreaState == null) {
            this.close(prearrangedEnd);
        } else {
            if (prearrangedEnd) {
                switch (this.delayedAreaState) {
                    case No:
                    case Continue:
                    case End:
                        this.delayedAreaState = MAPDialogImpl.DelayedAreaState.PrearrangedEnd;
                        break;
                }
            } else {
                switch (this.delayedAreaState) {
                    case No:
                    case Continue:
                        this.delayedAreaState = MAPDialogImpl.DelayedAreaState.End;
                        break;
                }
            }
        }
    }

    public void send() throws MAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        try {
            this.getTcapDialog().getDialogLock().lock();

            switch (this.tcapDialog.getState()) {

                case Idle:
                    ApplicationContextName acn = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
                            .createApplicationContextName(this.appCntx.getOID());

                    this.setState(MAPDialogState.INITIAL_SENT);

                    this.mapProviderImpl.fireTCBegin(this.getTcapDialog(), acn, destReference, origReference,
                            this.extContainer, this.eriStyle, this.eriImsi, this.eriVlrNo, this.getReturnMessageOnError());
                    this.extContainer = null;
                    break;

                case Active:
                    // Its Active send TC-CONTINUE

                    this.mapProviderImpl
                            .fireTCContinue(this.getTcapDialog(), false, null, null, this.getReturnMessageOnError());
                    break;

                case InitialReceived:
                    // Its first Reply to TC-Begin

                    ApplicationContextName acn1 = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
                            .createApplicationContextName(this.appCntx.getOID());

                    this.mapProviderImpl.fireTCContinue(this.getTcapDialog(), true, acn1, this.extContainer,
                            this.getReturnMessageOnError());
                    this.extContainer = null;

                    this.setState(MAPDialogState.ACTIVE);
                    break;

                case InitialSent: // we have sent TC-BEGIN already, need to wait
                    throw new MAPException("Awaiting TC-BEGIN response, can not send another dialog initiating primitive!");
                case Expunged: // dialog has been terminated on TC level, cant send
                    throw new MAPException("Dialog has been terminated, can not send primitives!");
            }
        } finally {
            this.getTcapDialog().getDialogLock().unlock();
        }
    }

    public void sendDelayed() throws MAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        if (this.delayedAreaState == null) {
            this.send();
        } else {
            switch (this.delayedAreaState) {
                case No:
                    this.delayedAreaState = MAPDialogImpl.DelayedAreaState.Continue;
                    break;
            }
        }
    }

    public MAPApplicationContext getApplicationContext() {
        return appCntx;
    }

    public MAPDialogState getState() {
        return state;
    }

    protected synchronized void setState(MAPDialogState newState) {
        // add checks?
        if (this.state == MAPDialogState.EXPUNGED) {
            return;
        }

        this.state = newState;
        // if (newState == MAPDialogState.EXPUNGED) {
        // this.mapProviderImpl.removeDialog(tcapDialog.getDialogId());
        // this.mapProviderImpl.deliverDialogResease(this);
        // }
    }

    public void processInvokeWithoutAnswer(Long invokeId) {

        if (this.tcapDialog.getPreviewMode())
            return;

        this.tcapDialog.processInvokeWithoutAnswer(invokeId);
    }

    public void sendInvokeComponent(Invoke invoke) throws MAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        try {
            this.tcapDialog.sendComponent(invoke);
        } catch (TCAPSendException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    public void sendReturnResultComponent(ReturnResult returnResult) throws MAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        try {
            this.tcapDialog.sendComponent(returnResult);
        } catch (TCAPSendException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    public void sendReturnResultLastComponent(ReturnResultLast returnResultLast) throws MAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        // this.removeIncomingInvokeId(returnResultLast.getInvokeId());

        try {
            this.tcapDialog.sendComponent(returnResultLast);
        } catch (TCAPSendException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    public void sendErrorComponent(Long invokeId, MAPErrorMessage mem) throws MAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        MAPErrorMessageImpl mapErrorMessage = (MAPErrorMessageImpl) mem;

        // this.removeIncomingInvokeId(invokeId);

        ReturnError returnError = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCReturnErrorRequest();

        try {
            returnError.setInvokeId(invokeId);

            // Error Code
            ErrorCode ec = TcapFactory.createErrorCode();
            ec.setLocalErrorCode(mapErrorMessage.getErrorCode());
            returnError.setErrorCode(ec);

            AsnOutputStream aos = new AsnOutputStream();
            mapErrorMessage.encodeData(aos);
            byte[] buf = aos.toByteArray();
            if (buf.length != 0) {
                Parameter p = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
                p.setTagClass(mapErrorMessage.getTagClass());
                p.setPrimitive(mapErrorMessage.getIsPrimitive());
                p.setTag(mapErrorMessage.getTag());
                p.setData(buf);
                returnError.setParameter(p);
            }

            this.tcapDialog.sendComponent(returnError);

        } catch (TCAPSendException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    public void sendRejectComponent(Long invokeId, Problem problem) throws MAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        // if (invokeId != null && problem != null && problem.getInvokeProblemType() != null)
        // this.removeIncomingInvokeId(invokeId);

        Reject reject = this.mapProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCRejectRequest();

        try {
            reject.setInvokeId(invokeId);

            // Error Code
            reject.setProblem(problem);

            this.tcapDialog.sendComponent(reject);

        } catch (TCAPSendException e) {
            throw new MAPException(e.getMessage(), e);
        }
    }

    public void resetInvokeTimer(Long invokeId) throws MAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        try {
            this.getTcapDialog().resetTimer(invokeId);
        } catch (TCAPException e) {
            throw new MAPException("TCAPException occure: " + e.getMessage(), e);
        }
    }

    public boolean cancelInvocation(Long invokeId) throws MAPException {

        if (this.tcapDialog.getPreviewMode())
            return false;

        try {
            return this.getTcapDialog().cancelInvocation(invokeId);
        } catch (TCAPException e) {
            throw new MAPException("TCAPException occure: " + e.getMessage(), e);
        }
    }

    public Object getUserObject() {
        return this.tcapDialog.getUserObject();
    }

    public void setUserObject(Object userObject) {
        this.tcapDialog.setUserObject(userObject);
    }

    public int getMaxUserDataLength() {
        return this.getTcapDialog().getMaxUserDataLength();
    }

    /**
     * Return the MAP message length (in bytes) that will be after encoding if TC-BEGIN or TC-CONTINUE cases This value must not
     * exceed getMaxUserDataLength() value
     *
     * @return
     */
    public int getMessageUserDataLengthOnSend() throws MAPException {

        try {
            switch (this.tcapDialog.getState()) {
                case Idle:
                    ApplicationContextName acn = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
                            .createApplicationContextName(this.appCntx.getOID());

                    TCBeginRequest tb = this.mapProviderImpl.encodeTCBegin(this.getTcapDialog(), acn, destReference,
                            origReference, this.extContainer, this.eriStyle, this.eriImsi, this.eriVlrNo);
                    return tcapDialog.getDataLength(tb);

                case Active:
                    // Its Active send TC-CONTINUE

                    TCContinueRequest tc = this.mapProviderImpl.encodeTCContinue(this.getTcapDialog(), false, null, null);
                    return tcapDialog.getDataLength(tc);

                case InitialReceived:
                    // Its first Reply to TC-Begin

                    ApplicationContextName acn1 = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
                            .createApplicationContextName(this.appCntx.getOID());

                    tc = this.mapProviderImpl.encodeTCContinue(this.getTcapDialog(), true, acn1, this.extContainer);
                    return tcapDialog.getDataLength(tc);
            }
        } catch (TCAPSendException e) {
            throw new MAPException("TCAPSendException when getMessageUserDataLengthOnSend", e);
        }

        throw new MAPException("Bad TCAP Dialog state: " + this.tcapDialog.getState());
    }

    /**
     * Return the MAP message length (in bytes) that will be after encoding if TC-END case This value must not exceed
     * getMaxUserDataLength() value
     *
     * @param prearrangedEnd
     * @return
     */
    public int getMessageUserDataLengthOnClose(boolean prearrangedEnd) throws MAPException {

        try {
            switch (this.tcapDialog.getState()) {
                case InitialReceived:
                    ApplicationContextName acn = this.mapProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
                            .createApplicationContextName(this.appCntx.getOID());

                    TCEndRequest te = this.mapProviderImpl.encodeTCEnd(this.getTcapDialog(), true, prearrangedEnd, acn,
                            this.extContainer);
                    return tcapDialog.getDataLength(te);

                case Active:
                    te = this.mapProviderImpl.encodeTCEnd(this.getTcapDialog(), false, prearrangedEnd, null, null);
                    return tcapDialog.getDataLength(te);
            }
        } catch (TCAPSendException e) {
            throw new MAPException("TCAPSendException when getMessageUserDataLengthOnSend", e);
        }

        throw new MAPException("Bad TCAP Dialog state: " + this.tcapDialog.getState());
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("MAPDialog: LocalDialogId=").append(this.getLocalDialogId()).append(" RemoteDialogId=")
                .append(this.getRemoteDialogId()).append(" MAPDialogState=").append(this.getState())
                .append(" MAPApplicationContext=").append(this.appCntx).append(" TCAPDialogState=")
                .append(this.tcapDialog.getState());
        return sb.toString();
    }

    public void addEricssonData(IMSI imsi, AddressString vlrNo) {
        this.eriStyle = true;
        this.eriImsi = imsi;
        this.eriVlrNo = vlrNo;
    }

    protected enum DelayedAreaState {
        No, Continue, End, PrearrangedEnd;
    }

    public long getIdleTaskTimeout() {
        return tcapDialog.getIdleTaskTimeout();
    }

    public void setIdleTaskTimeout(long idleTaskTimeoutMs) {
        tcapDialog.setIdleTaskTimeout(idleTaskTimeoutMs);
    }
}
