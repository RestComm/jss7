/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.tcap;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class PrevewDialogData {

    private static final Logger logger = Logger.getLogger(PrevewDialogData.class);

    private ApplicationContextName lastACN;
    private InvokeImpl[] operationsSentA;
    private InvokeImpl[] operationsSentB;

    private Object upperDialog;

    private TCAPProviderImpl.PrevewDialogDataKey prevewDialogDataKey1;
    private TCAPProviderImpl.PrevewDialogDataKey prevewDialogDataKey2;

    private ReentrantLock dialogLock = new ReentrantLock();
    private Future idleTimerFuture;
    private ScheduledExecutorService executor;
    private TCAPProviderImpl provider;
    private long idleTaskTimeout;
    private Long dialogId;

    public PrevewDialogData(TCAPProviderImpl provider, Long dialogId) {
        this.provider = provider;
        this.dialogId = dialogId;
        TCAPStack stack = provider.getStack();
        this.idleTaskTimeout = stack.getDialogIdleTimeout();
        this.executor = provider._EXECUTOR;
    }

    public ApplicationContextName getLastACN() {
        return lastACN;
    }

    public Long getDialogId() {
        return dialogId;
    }

    public InvokeImpl[] getOperationsSentA() {
        return operationsSentA;
    }

    public InvokeImpl[] getOperationsSentB() {
        return operationsSentB;
    }

    public Object getUpperDialog() {
        return upperDialog;
    }

    public void setLastACN(ApplicationContextName val) {
        lastACN = val;
    }

    public void setOperationsSentA(InvokeImpl[] val) {
        operationsSentA = val;
    }

    public void setOperationsSentB(InvokeImpl[] val) {
        operationsSentB = val;
    }

    public void setUpperDialog(Object val) {
        upperDialog = val;
    }

    protected TCAPProviderImpl.PrevewDialogDataKey getPrevewDialogDataKey1() {
        return prevewDialogDataKey1;
    }

    protected TCAPProviderImpl.PrevewDialogDataKey getPrevewDialogDataKey2() {
        return prevewDialogDataKey2;
    }

    protected void setPrevewDialogDataKey1(TCAPProviderImpl.PrevewDialogDataKey val) {
        prevewDialogDataKey1 = val;
    }

    protected void setPrevewDialogDataKey2(TCAPProviderImpl.PrevewDialogDataKey val) {
        prevewDialogDataKey2 = val;
    }

    protected void startIdleTimer() {

        try {
            this.dialogLock.lock();
            if (this.idleTimerFuture != null) {
                throw new IllegalStateException();
            }

            IdleTimerTask t = new IdleTimerTask();
            t.pdd = this;
            this.idleTimerFuture = this.executor.schedule(t, this.idleTaskTimeout, TimeUnit.MILLISECONDS);

        } finally {
            this.dialogLock.unlock();
        }
    }

    protected void stopIdleTimer() {
        try {
            this.dialogLock.lock();
            if (this.idleTimerFuture != null) {
                this.idleTimerFuture.cancel(false);
                this.idleTimerFuture = null;
            }

        } finally {
            this.dialogLock.unlock();
        }
    }

    protected void restartIdleTimer() {
        stopIdleTimer();
        startIdleTimer();
    }

    private class IdleTimerTask implements Runnable {
        PrevewDialogData pdd;

        public void run() {
            try {
                dialogLock.lock();

//              Dialog d1 = new DialogImpl(localAddress, remoteAddress, seqControl, provider._EXECUTOR, provider, pdd, sideB);
                DialogImpl dlg = (DialogImpl)provider.getPreviewDialog(prevewDialogDataKey1, null, null, null, 0);
                provider.timeout(dlg);
                provider.removePreviewDialog(dlg);

//                provider.removePreviewDialog(pdd);
//
//                if (logger.isEnabledFor(Level.ERROR)) {
//                    StringBuilder sb = new StringBuilder();
//                    if (this.pdd.prevewDialogDataKey1 != null) {
//                        sb.append(", trId1=");
//                        sb.append(this.pdd.prevewDialogDataKey1.origTxId);
//                    }
//                    if (this.pdd.prevewDialogDataKey2 != null) {
//                        sb.append(", trId2=");
//                        sb.append(this.pdd.prevewDialogDataKey2.origTxId);
//                    }
//                }
            } finally {
                dialogLock.unlock();
            }
        }

    }
}
