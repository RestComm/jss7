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

package org.mobicents.protocols.ss7.cap;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.CAPApplicationContext;
import org.mobicents.protocols.ss7.cap.api.CAPDialog;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPServiceBase;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPDialogState;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGeneralAbortReason;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.mobicents.protocols.ss7.cap.api.dialog.CAPUserAbortReason;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessage;
import org.mobicents.protocols.ss7.cap.errors.CAPErrorMessageImpl;
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
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;

/**
 *
 * @author sergey vetyutnev
 */
public abstract class CAPDialogImpl implements CAPDialog {

    private static final Logger logger = Logger.getLogger(CAPDialogImpl.class);

    protected Dialog tcapDialog = null;
    protected CAPProviderImpl capProviderImpl = null;
    protected CAPServiceBase capService = null;

    // Application Context of this Dialog
    protected CAPApplicationContext appCntx;

    protected CAPGprsReferenceNumber gprsReferenceNumber = null;
    protected CAPGprsReferenceNumber receivedGprsReferenceNumber;

    protected CAPDialogState state = CAPDialogState.Idle;

    // protected boolean normalDialogShutDown = false;

    // private Set<Long> incomingInvokeList = new HashSet<Long>();

    boolean returnMessageOnError = false;
    protected MessageType tcapMessageType;
    protected DelayedAreaState delayedAreaState;

    protected CAPDialogImpl(CAPApplicationContext appCntx, Dialog tcapDialog, CAPProviderImpl capProviderImpl,
            CAPServiceBase capService) {
        this.appCntx = appCntx;
        this.tcapDialog = tcapDialog;
        this.capProviderImpl = capProviderImpl;
        this.capService = capService;
    }

    public SccpAddress getLocalAddress() {
        return this.tcapDialog.getLocalAddress();
    }

    public void setLocalAddress(SccpAddress localAddress) {
        this.tcapDialog.setLocalAddress(localAddress);
    }

    public SccpAddress getRemoteAddress() {
        return this.tcapDialog.getRemoteAddress();
    }

    public void setRemoteAddress(SccpAddress remoteAddress) {
        this.tcapDialog.setRemoteAddress(remoteAddress);
    }

    @Override
    public void setReturnMessageOnError(boolean val) {
        returnMessageOnError = val;
    }

    @Override
    public boolean getReturnMessageOnError() {
        return returnMessageOnError;
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

    @Override
    public void keepAlive() {
        this.tcapDialog.keepAlive();
    }

    public Long getLocalDialogId() {
        return tcapDialog.getLocalDialogId();
    }

    public Long getRemoteDialogId() {
        return tcapDialog.getRemoteDialogId();
    }

    public CAPServiceBase getService() {
        return this.capService;
    }

    public Dialog getTcapDialog() {
        return tcapDialog;
    }

    public void release() {
        // this.setNormalDialogShutDown();
        this.setState(CAPDialogState.Expunged);

        if (this.tcapDialog != null)
            this.tcapDialog.release();
    }

    /**
     * Setting that the CAP Dialog is normally shutting down - to prevent performing onDialogReleased()
     */
    // protected void setNormalDialogShutDown() {
    // this.normalDialogShutDown = true;
    // }
    //
    // protected Boolean getNormalDialogShutDown() {
    // return this.normalDialogShutDown;
    // }

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

    public CAPDialogState getState() {
        return state;
    }

    protected synchronized void setState(CAPDialogState newState) {
        if (this.state == CAPDialogState.Expunged) {
            return;
        }

        this.state = newState;
        // if (newState == CAPDialogState.Expunged) {
        // this.capProviderImpl.removeDialog(tcapDialog.getDialogId());
        // this.capProviderImpl.deliverDialogRelease(this);
        // }
    }

    public void setGprsReferenceNumber(CAPGprsReferenceNumber gprsReferenceNumber) {
        this.gprsReferenceNumber = gprsReferenceNumber;
    }

    public CAPGprsReferenceNumber getGprsReferenceNumber() {
        return this.gprsReferenceNumber;
    }

    public CAPGprsReferenceNumber getReceivedGprsReferenceNumber() {
        return receivedGprsReferenceNumber;
    }

    public void send() throws CAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        try {
            this.getTcapDialog().getDialogLock().lock();

            switch (this.tcapDialog.getState()) {

                case Idle:
                    ApplicationContextName acn = this.capProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
                            .createApplicationContextName(this.appCntx.getOID());

                    this.setState(CAPDialogState.InitialSent);

                    this.capProviderImpl.fireTCBegin(this.getTcapDialog(), acn, this.gprsReferenceNumber,
                            this.getReturnMessageOnError());
                    this.gprsReferenceNumber = null;
                    break;

                case Active:
                    // Its Active send TC-CONTINUE

                    this.capProviderImpl.fireTCContinue(this.getTcapDialog(), null, null, this.getReturnMessageOnError());
                    break;

                case InitialReceived:
                    // Its first Reply to TC-Begin

                    ApplicationContextName acn1 = this.capProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
                            .createApplicationContextName(this.appCntx.getOID());

                    this.capProviderImpl.fireTCContinue(this.getTcapDialog(), acn1, this.gprsReferenceNumber,
                            this.getReturnMessageOnError());
                    this.gprsReferenceNumber = null;

                    this.setState(CAPDialogState.Active);
                    break;

                case InitialSent: // we have sent TC-BEGIN already, need to wait
                    throw new CAPException("Awaiting TC-BEGIN response, can not send another dialog initiating primitive!");
                case Expunged: // dialog has been terminated on TC level, cant send
                    throw new CAPException("Dialog has been terminated, can not send primitives!");
            }
        } finally {
            this.getTcapDialog().getDialogLock().unlock();
        }
    }

    public void sendDelayed() throws CAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        if (this.delayedAreaState == null) {
            this.send();
        } else {
            switch (this.delayedAreaState) {
                case No:
                    this.delayedAreaState = CAPDialogImpl.DelayedAreaState.Continue;
                    break;
            }
        }
    }

    public void close(boolean prearrangedEnd) throws CAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        try {
            this.getTcapDialog().getDialogLock().lock();

            switch (this.tcapDialog.getState()) {
                case InitialReceived:
                    ApplicationContextName acn = this.capProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
                            .createApplicationContextName(this.appCntx.getOID());

                    // this.setNormalDialogShutDown();
                    this.capProviderImpl.fireTCEnd(this.getTcapDialog(), prearrangedEnd, acn, this.gprsReferenceNumber,
                            this.getReturnMessageOnError());
                    this.gprsReferenceNumber = null;

                    this.setState(CAPDialogState.Expunged);
                    break;

                case Active:
                    // this.setNormalDialogShutDown();
                    this.capProviderImpl.fireTCEnd(this.getTcapDialog(), prearrangedEnd, null, null,
                            this.getReturnMessageOnError());

                    this.setState(CAPDialogState.Expunged);
                    break;

                case Idle:
                    throw new CAPException("Awaiting TC-BEGIN to be sent, can not send another dialog initiating primitive!");
                case InitialSent: // we have sent TC-BEGIN already, need to wait
                    throw new CAPException("Awaiting TC-BEGIN response, can not send another dialog initiating primitive!");
                case Expunged: // dialog has been terminated on TC level, cant send
                    throw new CAPException("Dialog has been terminated, can not send primitives!");
            }
        } finally {
            this.getTcapDialog().getDialogLock().unlock();
        }
    }

    public void closeDelayed(boolean prearrangedEnd) throws CAPException {

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
                        this.delayedAreaState = CAPDialogImpl.DelayedAreaState.PrearrangedEnd;
                        break;
                }
            } else {
                switch (this.delayedAreaState) {
                    case No:
                    case Continue:
                        this.delayedAreaState = CAPDialogImpl.DelayedAreaState.End;
                        break;
                }
            }
        }
    }

    @Override
    public void abort(CAPUserAbortReason abortReason) throws CAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        try {
            this.getTcapDialog().getDialogLock().lock();

            // Dialog is not started or has expunged - we need not send
            // TC-U-ABORT,
            // only Dialog removing
            if (this.getState() == CAPDialogState.Expunged || this.getState() == CAPDialogState.Idle) {
                this.setState(CAPDialogState.Expunged);
                return;
            }

            // this.setNormalDialogShutDown();
            this.capProviderImpl.fireTCAbort(this.getTcapDialog(), CAPGeneralAbortReason.UserSpecific, abortReason,
                    this.getReturnMessageOnError());

            this.setState(CAPDialogState.Expunged);
        } finally {
            this.getTcapDialog().getDialogLock().unlock();
        }
    }

    @Override
    public void processInvokeWithoutAnswer(Long invokeId) {

        if (this.tcapDialog.getPreviewMode())
            return;

        this.tcapDialog.processInvokeWithoutAnswer(invokeId);
    }

    @Override
    public void sendInvokeComponent(Invoke invoke) throws CAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        try {
            this.tcapDialog.sendComponent(invoke);
        } catch (TCAPSendException e) {
            throw new CAPException(e.getMessage(), e);
        }
    }

    @Override
    public void sendReturnResultLastComponent(ReturnResultLast returnResultLast) throws CAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        // this.removeIncomingInvokeId(returnResultLast.getInvokeId());

        try {
            this.tcapDialog.sendComponent(returnResultLast);
        } catch (TCAPSendException e) {
            throw new CAPException(e.getMessage(), e);
        }
    }

    @Override
    public void sendErrorComponent(Long invokeId, CAPErrorMessage mem) throws CAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        CAPErrorMessageImpl capErrorMessage = (CAPErrorMessageImpl) mem;

        // this.removeIncomingInvokeId(invokeId);

        ReturnError returnError = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory()
                .createTCReturnErrorRequest();

        try {
            returnError.setInvokeId(invokeId);

            // Error Code
            ErrorCode ec = TcapFactory.createErrorCode();
            ec.setLocalErrorCode(capErrorMessage.getErrorCode());
            returnError.setErrorCode(ec);

            AsnOutputStream aos = new AsnOutputStream();
            capErrorMessage.encodeData(aos);
            byte[] buf = aos.toByteArray();
            if (buf.length != 0) {
                Parameter p = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createParameter();
                p.setTagClass(capErrorMessage.getTagClass());
                p.setPrimitive(capErrorMessage.getIsPrimitive());
                p.setTag(capErrorMessage.getTag());
                p.setData(buf);
                returnError.setParameter(p);
            }

            this.tcapDialog.sendComponent(returnError);

        } catch (TCAPSendException e) {
            throw new CAPException(e.getMessage(), e);
        }
    }

    @Override
    public void sendRejectComponent(Long invokeId, Problem problem) throws CAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        // if (invokeId != null && problem != null && problem.getInvokeProblemType() != null)
        // this.removeIncomingInvokeId(invokeId);

        Reject reject = this.capProviderImpl.getTCAPProvider().getComponentPrimitiveFactory().createTCRejectRequest();

        try {
            reject.setInvokeId(invokeId);

            // Error Code
            reject.setProblem(problem);

            this.tcapDialog.sendComponent(reject);

        } catch (TCAPSendException e) {
            throw new CAPException(e.getMessage(), e);
        }
    }

    @Override
    public void resetInvokeTimer(Long invokeId) throws CAPException {

        if (this.tcapDialog.getPreviewMode())
            return;

        try {
            this.getTcapDialog().resetTimer(invokeId);
        } catch (TCAPException e) {
            throw new CAPException("TCAPException occure: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean cancelInvocation(Long invokeId) throws CAPException {
        try {
            return this.getTcapDialog().cancelInvocation(invokeId);
        } catch (TCAPException e) {
            throw new CAPException("TCAPException occure: " + e.getMessage(), e);
        }
    }

    public Object getUserObject() {
        return this.tcapDialog.getUserObject();
    }

    public void setUserObject(Object userObject) {
        this.tcapDialog.setUserObject(userObject);
    }

    @Override
    public CAPApplicationContext getApplicationContext() {
        return appCntx;
    }

    @Override
    public int getMaxUserDataLength() {
        return this.getTcapDialog().getMaxUserDataLength();
    }

    @Override
    public int getMessageUserDataLengthOnSend() throws CAPException {

        try {
            switch (this.tcapDialog.getState()) {
                case Idle:
                    ApplicationContextName acn = this.capProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
                            .createApplicationContextName(this.appCntx.getOID());

                    TCBeginRequest tb = this.capProviderImpl.encodeTCBegin(this.getTcapDialog(), acn, this.gprsReferenceNumber);
                    return tcapDialog.getDataLength(tb);

                case Active:
                    // Its Active send TC-CONTINUE

                    TCContinueRequest tc = this.capProviderImpl.encodeTCContinue(this.getTcapDialog(), null, null);
                    return tcapDialog.getDataLength(tc);

                case InitialReceived:
                    // Its first Reply to TC-Begin

                    ApplicationContextName acn1 = this.capProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
                            .createApplicationContextName(this.appCntx.getOID());

                    tc = this.capProviderImpl.encodeTCContinue(this.getTcapDialog(), acn1, this.gprsReferenceNumber);
                    return tcapDialog.getDataLength(tc);
            }
        } catch (TCAPSendException e) {
            throw new CAPException("TCAPSendException when getMessageUserDataLengthOnSend", e);
        }

        throw new CAPException("Bad TCAP Dialog state: " + this.tcapDialog.getState());
    }

    @Override
    public int getMessageUserDataLengthOnClose(boolean prearrangedEnd) throws CAPException {

        try {
            switch (this.tcapDialog.getState()) {
                case InitialReceived:
                    ApplicationContextName acn = this.capProviderImpl.getTCAPProvider().getDialogPrimitiveFactory()
                            .createApplicationContextName(this.appCntx.getOID());

                    TCEndRequest te = this.capProviderImpl.encodeTCEnd(this.getTcapDialog(), prearrangedEnd, acn,
                            this.gprsReferenceNumber);
                    return tcapDialog.getDataLength(te);

                case Active:
                    te = this.capProviderImpl.encodeTCEnd(this.getTcapDialog(), prearrangedEnd, null, null);
                    return tcapDialog.getDataLength(te);
            }
        } catch (TCAPSendException e) {
            throw new CAPException("TCAPSendException when getMessageUserDataLengthOnSend", e);
        }

        throw new CAPException("Bad TCAP Dialog state: " + this.tcapDialog.getState());
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CAPDialog: LocalDialogId=").append(this.getLocalDialogId()).append(" RemoteDialogId=")
                .append(this.getRemoteDialogId()).append(" CAPDialogState=").append(this.getState())
                .append(" CAPApplicationContext=").append(this.appCntx).append(" TCAPDialogState=")
                .append(this.tcapDialog.getState());
        return sb.toString();
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
