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
public class PreviewDialogData {

    private static final Logger logger = Logger.getLogger(PreviewDialogData.class);

    private ApplicationContextName lastACN;
    private InvokeImpl[] operationsSentA;
    private InvokeImpl[] operationsSentB;

    private Object upperDialog;

    private PreviewDialogDataKey prevewDialogDataKey1;
    private PreviewDialogDataKey prevewDialogDataKey2;

    private ReentrantLock dialogLock = new ReentrantLock();
    private Future idleTimerFuture;
    private ScheduledExecutorService executor;
    private TCAPProviderImpl provider;
    private long idleTaskTimeout;
    private Long dialogId;

    public PreviewDialogData(TCAPProviderImpl provider, Long dialogId) {
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

    protected PreviewDialogDataKey getPrevewDialogDataKey1() {
        return prevewDialogDataKey1;
    }

    protected PreviewDialogDataKey getPrevewDialogDataKey2() {
        return prevewDialogDataKey2;
    }

    protected void setPrevewDialogDataKey1(PreviewDialogDataKey val) {
        prevewDialogDataKey1 = val;
    }

    protected void setPrevewDialogDataKey2(PreviewDialogDataKey val) {
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
        try {
            this.dialogLock.lock();

            stopIdleTimer();
            startIdleTimer();
        } finally {
            this.dialogLock.unlock();
        }
    }

    private class IdleTimerTask implements Runnable {
        PreviewDialogData pdd;

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
