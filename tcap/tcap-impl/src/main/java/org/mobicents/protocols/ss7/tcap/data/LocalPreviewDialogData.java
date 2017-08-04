/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LocalPreviewDialogData implements IPreviewDialogData {

    private static final Logger logger = Logger.getLogger(LocalPreviewDialogData.class);
    private LocalPreviewTCAPOperation[] operationsSentA = new LocalPreviewTCAPOperation[256];
    private LocalPreviewTCAPOperation[] operationsSentB = new LocalPreviewTCAPOperation[256];;


    private Object upperDialog;

    private PreviewDialogDataKey previewDialogDataKey1;
    private PreviewDialogDataKey previewDialogDataKey2;

    private ReentrantLock dialogLock = new ReentrantLock();
    private long idleTaskTimeout;
    private Long dialogId;

    public static final int _INVOKE_TABLE_SHIFT=128;

    private IDialog dialog;
    private Long localTransactionIdObject;
    private long localTransactionId;
    private byte[] remoteTransactionId;
    private Long remoteTransactionIdObject;
    private SccpAddress localAddress;
    private SccpAddress remoteAddress;
    private Object idleTimerHandle;
    private boolean idleTimerActionTaken;
    private boolean idleTimerInvoked;
    private TRPseudoState state;
    private boolean structured;
    private int seqControl;
    private boolean dpSentInBegin;
    private long startDialogTime;
    private int networkId;
    private Long localRelayedTransactionIdObject;
    private int localSsn;

    void setDialog(IDialog d){
        dialog=d;
    }
    IDialog getDialog() {
        return dialog;
    }

    private static int getIndexFromInvokeId(Long l) {
        int tmp = l.intValue();
        return tmp + _INVOKE_TABLE_SHIFT;
    }

    private static Long getInvokeIdFromIndex(int index) {
        int tmp = index - _INVOKE_TABLE_SHIFT;
        return new Long(tmp);
    }

    public LocalPreviewDialogData(TCAPProviderImpl provider, Long dialogId) {
        this.dialogId = dialogId;
        TCAPStack stack = provider.getStack();
        this.idleTaskTimeout = stack.getDialogIdleTimeout();
    }

    @Override
    public PreviewDialogDataKey getPreviewDialogDataKey1() {
        return previewDialogDataKey1;
    }

    @Override
    public PreviewDialogDataKey getPreviewDialogDataKey2() {
        return previewDialogDataKey2;
    }

    @Override
    public void setPreviewDialogDataKey1(PreviewDialogDataKey val) {
        previewDialogDataKey1=val;
    }

    @Override
    public void setPreviewDialogDataKey2(PreviewDialogDataKey val) {
        previewDialogDataKey2=val;
    }

    @Override
    public ITCAPOperation getTCAPOperation(boolean sideB, Long invokeId) {
        int index=getIndexFromInvokeId(invokeId);
        if(sideB)
            return this.operationsSentA[index];
        else
            return this.operationsSentB[index];
    }

    @Override
    public ITCAPOperation newTCAPOperation(boolean sideB, PreviewDialogImpl previewDialog, Invoke ci) {
        LocalPreviewTCAPOperation pto=new LocalPreviewTCAPOperation(sideB,previewDialog,ci);
        if(sideB) {
            operationsSentB[getIndexFromInvokeId(ci.getInvokeId())] = pto;
        } else {
            operationsSentA[getIndexFromInvokeId(ci.getInvokeId())] = pto;
        }
        return pto;
    }

    public void freeTcapOperation(ITCAPOperation op) {
        if(!(op instanceof LocalPreviewTCAPOperation))
            return;
        LocalPreviewTCAPOperation lop=(LocalPreviewTCAPOperation)op;
        if(lop.isSideB()) {
            op = this.operationsSentB[getIndexFromInvokeId(op.getInvokeId())];
            this.operationsSentB[getIndexFromInvokeId(lop.getInvokeId())] = null;
        } else {
            op = this.operationsSentA[getIndexFromInvokeId(op.getInvokeId())];
            this.operationsSentA[getIndexFromInvokeId(lop.getInvokeId())] = null;
        }
    }

    Object userObject;

    @Override
    public Object getUserObject() {
        return userObject;
    }

    @Override
    public void setUserObject(Object userObject) {
        this.userObject=userObject;
    }

    @Override
    public ReentrantLock getDialogLock() {
        return dialogLock;
    }

    @Override
    public long getIdleTaskTimeout() {
        return idleTaskTimeout;
    }

    @Override
    public void setIdleTaskTimeout(long idleTaskTimeout) {
        this.idleTaskTimeout=idleTaskTimeout;
    }
    ApplicationContextName lastACN;
    @Override
    public ApplicationContextName getLastACN() {
        return lastACN;
    }

    @Override
    public void setLastACN(ApplicationContextName lastACN) {
        this.lastACN=lastACN;
    }


    UserInformation lastUI;
    @Override
    public UserInformation getLastUI() {
        return lastUI;
    }

    @Override
    public void setLastUI(UserInformation lastUI) {
        this.lastUI=lastUI;
    }

    @Override
    public Long getLocalTransactionIdObject() {
        return localTransactionIdObject;
    }

    @Override
    public void setLocalTransactionIdObject(Long localTransactionIdObject) {
        this.localTransactionIdObject=localTransactionIdObject;
    }

    @Override
    public long getLocalTransactionId() {
        return localTransactionId;
    }

    @Override
    public void setLocalTransactionId(long localTransactionId) {
        this.localTransactionId=localTransactionId;
    }

    @Override
    public byte[] getRemoteTransactionId() {
        return remoteTransactionId;
    }

    @Override
    public void setRemoteTransactionId(byte[] remoteTransactionId) {
        this.remoteTransactionId=remoteTransactionId;

    }

    @Override
    public Long getRemoteTransactionIdObject() {
        return remoteTransactionIdObject;
    }

    @Override
    public void setRemoteTransactionIdObject(Long remoteTransactionIdObject) {
        this.remoteTransactionIdObject=remoteTransactionIdObject;
    }

    @Override
    public SccpAddress getLocalAddress() {
        return localAddress;
    }

    @Override
    public void setLocalAddress(SccpAddress localAddress) {
        this.localAddress=localAddress;
    }

    @Override
    public SccpAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public void setRemoteAddress(SccpAddress remoteAddress) {
        this.remoteAddress=remoteAddress;
    }

    @Override
    public Object getIdleTimerHandle() {

        return idleTimerHandle;
    }

    @Override
    public void setIdleTimerHandle(Object idleTimerHandle) {
        this.idleTimerHandle=idleTimerHandle;
    }

    @Override
    public boolean isIdleTimerActionTaken() {
        return idleTimerActionTaken;
    }

    @Override
    public void setIdleTimerActionTaken(boolean idleTimerActionTaken) {
        this.idleTimerActionTaken=idleTimerActionTaken;
    }

    @Override
    public boolean isIdleTimerInvoked() {
        return idleTimerInvoked;
    }

    @Override
    public void setIdleTimerInvoked(boolean idleTimerInvoked) {
        this.idleTimerInvoked=idleTimerInvoked;
    }

    @Override
    public TRPseudoState getState() {
        return state;
    }

    @Override
    public void setState(TRPseudoState state) {
        this.state=state;

    }

    @Override
    public boolean isStructured() {
        return structured;
    }

    @Override
    public void setStructured(boolean structured) {
        this.structured=structured;
    }

    @Override
    public boolean addIncomingInvoke(Long invokeId) {
        return false;
    }

    @Override
    public boolean removeIncomingInvoke(Long invokeId) {
        return false;
    }

    @Override
    public int getSeqControl() {
        return seqControl;
    }

    @Override
    public void setSeqControl(int seqControl) {
        this.seqControl=seqControl;
    }

    @Override
    public boolean isDpSentInBegin() {
        return dpSentInBegin;
    }

    @Override
    public void setDpSentInBegin(boolean dpSentInBegin) {
        this.dpSentInBegin=dpSentInBegin;
    }

    @Override
    public long getStartDialogTime() {
        return startDialogTime;
    }

    @Override
    public void setStartDialogTime(long startDialogTime) {
        this.startDialogTime=startDialogTime;
    }

    @Override
    public int getNetworkId() {
        return networkId;
    }

    @Override
    public void setNetworkId(int networkId) {
        this.networkId=networkId;
    }

    @Override
    public Long getLocalRelayedTransactionIdObject() {
        return localRelayedTransactionIdObject;
    }

    @Override
    public void setLocalRelayedTransactionIdObject(Long l) {
        this.localRelayedTransactionIdObject=l;
    }

    @Override
    public int getLocalSsn() { return localSsn;  }

    @Override
    public void setLocalSsn(int newSsn) {  localSsn=newSsn; }

    public void setDialogId(Long dialogId) {
        this.dialogId = dialogId;
    }


    @Override
    public void setUpperDialog(Object o) {
        upperDialog=o;
    }

    @Override
    public Object getUpperDialog() {
        return upperDialog;
    }
}
