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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.mobicents.protocols.ss7.statistics.StatDataCollectionImpl;
import org.mobicents.protocols.ss7.statistics.api.LongValue;
import org.mobicents.protocols.ss7.statistics.api.StatDataCollection;
import org.mobicents.protocols.ss7.statistics.api.StatDataCollectorType;
import org.mobicents.protocols.ss7.statistics.api.StatResult;
import org.mobicents.protocols.ss7.tcap.api.TCAPCounterProvider;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TCAPCounterProviderImpl implements TCAPCounterProvider {

    private UUID sessionId = UUID.randomUUID();

    private TCAPProviderImpl provider;
    private StatDataCollection statDataCollection = new StatDataCollectionImpl();

    private AtomicLong tcUniReceivedCount = new AtomicLong();
    private AtomicLong tcUniSentCount = new AtomicLong();
    private AtomicLong tcBeginReceivedCount = new AtomicLong();
    private AtomicLong tcBeginSentCount = new AtomicLong();
    private AtomicLong tcContinueReceivedCount = new AtomicLong();
    private AtomicLong tcContinueSentCount = new AtomicLong();
    private AtomicLong tcEndReceivedCount = new AtomicLong();
    private AtomicLong tcEndSentCount = new AtomicLong();
    private AtomicLong tcPAbortReceivedCount = new AtomicLong();
    private AtomicLong tcPAbortSentCount = new AtomicLong();
    private AtomicLong tcUserAbortReceivedCount = new AtomicLong();
    private AtomicLong tcUserAbortSentCount = new AtomicLong();

    private AtomicLong invokeReceivedCount = new AtomicLong();
    private AtomicLong invokeSentCount = new AtomicLong();
    private AtomicLong returnResultReceivedCount = new AtomicLong();
    private AtomicLong returnResultSentCount = new AtomicLong();
    private AtomicLong returnResultLastReceivedCount = new AtomicLong();
    private AtomicLong returnResultLastSentCount = new AtomicLong();
    private AtomicLong returnErrorReceivedCount = new AtomicLong();
    private AtomicLong returnErrorSentCount = new AtomicLong();
    private AtomicLong rejectReceivedCount = new AtomicLong();
    private AtomicLong rejectSentCount = new AtomicLong();

    private AtomicLong dialogTimeoutCount = new AtomicLong();
    private AtomicLong dialogReleaseCount = new AtomicLong();

    private AtomicLong allEstablishedDialogsCount = new AtomicLong();
    private AtomicLong allLocalEstablishedDialogsCount = new AtomicLong();
    private AtomicLong allRemoteEstablishedDialogsCount = new AtomicLong();

    private AtomicLong allDialogsDuration = new AtomicLong();

    private static String OUTGOING_DIALOGS_PER_APPLICATION_CONTEXT_NAME = "outgoingDialogsPerApplicationContextName";
    private static String INCOMING_DIALOGS_PER_APPLICATION_CONTEXT_NAME = "incomingDialogsPerApplicationContextName";
    private static String OUTGOING_INVOKES_PER_OPERATION_CODE = "outgoingInvokesPerOperationCode";
    private static String INCOMING_INVOKES_PER_OPERATION_CODE = "incomingInvokesPerOperationCode";
    private static String OUTGOING_ERRORS_PER_ERROR_CODE = "outgoingErrorsPerErrorCode";
    private static String INCOMING_ERRORS_PER_ERROR_CODE = "incomingErrorsPerErrorCode";
    private static String OUTGOING_REJECT_PER_PROBLEM = "outgoingRejectPerProblem";
    private static String INCOMING_REJECT_PER_PROBLEM = "incomingRejectPerProblem";

    private static String MIN_DIALOGS_COUNT = "MinDialogsCount";
    private static String MAX_DIALOGS_COUNT = "MaxDialogsCount";

    private List<TCAPCounterProviderImplListener> listeners = new ArrayList<TCAPCounterProviderImplListener>();

    public TCAPCounterProviderImpl(TCAPProviderImpl provider) {
        this.provider = provider;

        this.statDataCollection.registerStatCounterCollector(MIN_DIALOGS_COUNT, StatDataCollectorType.MIN);
        this.statDataCollection.registerStatCounterCollector(MAX_DIALOGS_COUNT, StatDataCollectorType.MAX);

        this.statDataCollection.registerStatCounterCollector(OUTGOING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(INCOMING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(OUTGOING_INVOKES_PER_OPERATION_CODE, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(INCOMING_INVOKES_PER_OPERATION_CODE, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(OUTGOING_ERRORS_PER_ERROR_CODE, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(INCOMING_ERRORS_PER_ERROR_CODE, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(OUTGOING_REJECT_PER_PROBLEM, StatDataCollectorType.StringLongMap);
        this.statDataCollection.registerStatCounterCollector(INCOMING_REJECT_PER_PROBLEM, StatDataCollectorType.StringLongMap);
    }

    public void addListener(TCAPCounterProviderImplListener listener) {
        List<TCAPCounterProviderImplListener> newListeners = new ArrayList<TCAPCounterProviderImplListener>(listeners);
        if (!newListeners.contains(listener)) {
            newListeners.add(listener);
            this.listeners = newListeners;
        }
    }

    public boolean removeListener(TCAPCounterProviderImplListener listener) {
        List<TCAPCounterProviderImplListener> newListeners = new ArrayList<TCAPCounterProviderImplListener>(listeners);
        if (newListeners.remove(listener)) {
            this.listeners = newListeners;
            return true;
        }
        return false;
    }

    @Override
    public UUID getSessionId() {
        return sessionId;
    }


    @Override
    public long getTcUniReceivedCount() {
        return tcUniReceivedCount.get();
    }

    public void updateTcUniReceivedCount(Dialog dialog) {
        tcUniReceivedCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateTcUniReceivedCount(dialog);
        }
    }

    @Override
    public long getTcUniSentCount() {
        return tcUniSentCount.get();
    }

    public void updateTcUniSentCount(Dialog dialog) {
        tcUniSentCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateTcUniSentCount(dialog);
        }
    }

    @Override
    public long getTcBeginReceivedCount() {
        return tcBeginReceivedCount.get();
    }

    public void updateTcBeginReceivedCount(Dialog dialog) {
        tcBeginReceivedCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateTcBeginReceivedCount(dialog);
        }
    }

    @Override
    public long getTcBeginSentCount() {
        return tcBeginSentCount.get();
    }

    public void updateTcBeginSentCount(Dialog dialog) {
        tcBeginSentCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateTcBeginSentCount(dialog);
        }
    }

    @Override
    public long getTcContinueReceivedCount() {
        return tcContinueReceivedCount.get();
    }

    public void updateTcContinueReceivedCount(Dialog dialog) {
        tcContinueReceivedCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateTcContinueReceivedCount(dialog);
        }
    }

    @Override
    public long getTcContinueSentCount() {
        return tcContinueSentCount.get();
    }

    public void updateTcContinueSentCount(Dialog dialog) {
        tcContinueSentCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateTcContinueSentCount(dialog);
        }
    }

    @Override
    public long getTcEndReceivedCount() {
        return tcEndReceivedCount.get();
    }

    public void updateTcEndReceivedCount(Dialog dialog) {
        tcEndReceivedCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateTcEndReceivedCount(dialog);
        }
    }

    @Override
    public long getTcEndSentCount() {
        return tcEndSentCount.get();
    }

    public void updateTcEndSentCount(Dialog dialog) {
        tcEndSentCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateTcEndSentCount(dialog);
        }
    }

    @Override
    public long getTcPAbortReceivedCount() {
        return tcPAbortReceivedCount.get();
    }

    public void updateTcPAbortReceivedCount(Dialog dialog, PAbortCauseType cause) {
        tcPAbortReceivedCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateTcPAbortReceivedCount(dialog, cause);
        }
    }

    @Override
    public long getTcPAbortSentCount() {
        return tcPAbortSentCount.get();
    }

    public void updateTcPAbortSentCount(byte[] originatingTransactionId, PAbortCauseType cause) {
        tcPAbortSentCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateTcPAbortSentCount(originatingTransactionId, cause);
        }
    }

    @Override
    public long getTcUserAbortReceivedCount() {
        return tcUserAbortReceivedCount.get();
    }

    public void updateTcUserAbortReceivedCount(Dialog dialog) {
        tcUserAbortReceivedCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateTcUserAbortReceivedCount(dialog);
        }
    }

    @Override
    public long getTcUserAbortSentCount() {
        return tcUserAbortSentCount.get();
    }

    public void updateTcUserAbortSentCount(Dialog dialog) {
        tcUserAbortSentCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateTcUserAbortSentCount(dialog);
        }
    }

    @Override
    public long getInvokeReceivedCount() {
        return invokeReceivedCount.get();
    }

    public void updateInvokeReceivedCount(Dialog dialog, Invoke invoke) {
        invokeReceivedCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateInvokeReceivedCount(dialog, invoke);
        }
    }

    @Override
    public long getInvokeSentCount() {
        return invokeSentCount.get();
    }

    public void updateInvokeSentCount(Dialog dialog, Invoke invoke) {
        invokeSentCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateInvokeSentCount(dialog, invoke);
        }
    }

    @Override
    public long getReturnResultReceivedCount() {
        return returnResultReceivedCount.get();
    }

    public void updateReturnResultReceivedCount(Dialog dialog) {
        returnResultReceivedCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateReturnResultReceivedCount(dialog);
        }
    }

    @Override
    public long getReturnResultSentCount() {
        return returnResultSentCount.get();
    }

    public void updateReturnResultSentCount(Dialog dialog) {
        returnResultSentCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateReturnResultSentCount(dialog);
        }
    }

    @Override
    public long getReturnResultLastReceivedCount() {
        return returnResultLastReceivedCount.get();
    }

    public void updateReturnResultLastReceivedCount(Dialog dialog) {
        returnResultLastReceivedCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateReturnResultLastReceivedCount(dialog);
        }
    }

    @Override
    public long getReturnResultLastSentCount() {
        return returnResultLastSentCount.get();
    }

    public void updateReturnResultLastSentCount(Dialog dialog) {
        returnResultLastSentCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateReturnResultLastSentCount(dialog);
        }
    }

    @Override
    public long getReturnErrorReceivedCount() {
        return returnErrorReceivedCount.get();
    }

    public void updateReturnErrorReceivedCount(Dialog dialog) {
        returnErrorReceivedCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateReturnErrorReceivedCount(dialog);
        }
    }

    @Override
    public long getReturnErrorSentCount() {
        return returnErrorSentCount.get();
    }

    public void updateReturnErrorSentCount(Dialog dialog) {
        returnErrorSentCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateReturnErrorSentCount(dialog);
        }
    }

    @Override
    public long getRejectReceivedCount() {
        return rejectReceivedCount.get();
    }

    public void updateRejectReceivedCount(Dialog dialog) {
        rejectReceivedCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateRejectReceivedCount(dialog);
        }
    }

    @Override
    public long getRejectSentCount() {
        return rejectSentCount.get();
    }

    public void updateRejectSentCount(Dialog dialog) {
        rejectSentCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateRejectSentCount(dialog);
        }
    }

    @Override
    public long getDialogTimeoutCount() {
        return dialogTimeoutCount.get();
    }

    public void updateDialogTimeoutCount(Dialog dialog) {
        dialogTimeoutCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateDialogTimeoutCount(dialog);
        }
    }

    @Override
    public long getDialogReleaseCount() {
        return dialogReleaseCount.get();
    }

    public void updateDialogReleaseCount(Dialog dialog) {
        dialogReleaseCount.addAndGet(1);
        for (TCAPCounterProviderImplListener listener : listeners) {
            listener.updateDialogReleaseCount(dialog);
        }
    }


    @Override
    public long getCurrentDialogsCount() {
        return provider.getCurrentDialogsCount();
    }

    @Override
    public long getAllEstablishedDialogsCount() {
        return allEstablishedDialogsCount.get();
    }

    public void updateAllEstablishedDialogsCount() {
        allEstablishedDialogsCount.addAndGet(1);
    }

    @Override
    public long getAllLocalEstablishedDialogsCount() {
        return allLocalEstablishedDialogsCount.get();
    }

    public void updateAllLocalEstablishedDialogsCount() {
        allLocalEstablishedDialogsCount.addAndGet(1);
    }

    @Override
    public long getAllRemoteEstablishedDialogsCount() {
        return allRemoteEstablishedDialogsCount.get();
    }

    public void updateAllRemoteEstablishedDialogsCount() {
        allRemoteEstablishedDialogsCount.addAndGet(1);
    }

    @Override
    public Long getMinDialogsCount(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MIN_DIALOGS_COUNT, compainName);
        this.statDataCollection.updateData(MIN_DIALOGS_COUNT, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMinDialogsCount(long newVal) {
        this.statDataCollection.updateData(MIN_DIALOGS_COUNT, newVal);
    }

    @Override
    public Long getMaxDialogsCount(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MAX_DIALOGS_COUNT, compainName);
        this.statDataCollection.updateData(MAX_DIALOGS_COUNT, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMaxDialogsCount(long newVal) {
        this.statDataCollection.updateData(MAX_DIALOGS_COUNT, newVal);
    }

    @Override
    public long getAllDialogsDuration() {
        return allDialogsDuration.get();
    }

    public void updateAllDialogsDuration(long diff) {
        allDialogsDuration.addAndGet(diff);
    }

    @Override
    public Map<String, LongValue> getOutgoingDialogsPerApplicatioContextName(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(OUTGOING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, compainName);
        this.statDataCollection.updateData(OUTGOING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateOutgoingDialogsPerApplicatioContextName(String name) {
        this.statDataCollection.updateData(OUTGOING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, name);
    }

    @Override
    public Map<String, LongValue> getIncomingDialogsPerApplicatioContextName(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(INCOMING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, compainName);
        this.statDataCollection.updateData(INCOMING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateIncomingDialogsPerApplicatioContextName(String name) {
        this.statDataCollection.updateData(INCOMING_DIALOGS_PER_APPLICATION_CONTEXT_NAME, name);
    }

    @Override
    public Map<String, LongValue> getOutgoingInvokesPerOperationCode(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(OUTGOING_INVOKES_PER_OPERATION_CODE, compainName);
        this.statDataCollection.updateData(OUTGOING_INVOKES_PER_OPERATION_CODE, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateOutgoingInvokesPerOperationCode(String name) {
        this.statDataCollection.updateData(OUTGOING_INVOKES_PER_OPERATION_CODE, name);
    }

    @Override
    public Map<String, LongValue> getIncomingInvokesPerOperationCode(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(INCOMING_INVOKES_PER_OPERATION_CODE, compainName);
        this.statDataCollection.updateData(INCOMING_INVOKES_PER_OPERATION_CODE, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateIncomingInvokesPerOperationCode(String name) {
        this.statDataCollection.updateData(INCOMING_INVOKES_PER_OPERATION_CODE, name);
    }

    @Override
    public Map<String, LongValue> getOutgoingErrorsPerErrorCode(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(OUTGOING_ERRORS_PER_ERROR_CODE, compainName);
        this.statDataCollection.updateData(OUTGOING_ERRORS_PER_ERROR_CODE, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateOutgoingErrorsPerErrorCode(String name) {
        this.statDataCollection.updateData(OUTGOING_ERRORS_PER_ERROR_CODE, name);
    }

    @Override
    public Map<String, LongValue> getIncomingErrorsPerErrorCode(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(INCOMING_ERRORS_PER_ERROR_CODE, compainName);
        this.statDataCollection.updateData(INCOMING_ERRORS_PER_ERROR_CODE, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateIncomingErrorsPerErrorCode(String name) {
        this.statDataCollection.updateData(INCOMING_ERRORS_PER_ERROR_CODE, name);
    }

    @Override
    public Map<String, LongValue> getOutgoingRejectPerProblem(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(OUTGOING_REJECT_PER_PROBLEM, compainName);
        this.statDataCollection.updateData(OUTGOING_REJECT_PER_PROBLEM, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateOutgoingRejectPerProblem(String name) {
        this.statDataCollection.updateData(OUTGOING_REJECT_PER_PROBLEM, name);
    }

    @Override
    public Map<String, LongValue> getIncomingRejectPerProblem(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(INCOMING_REJECT_PER_PROBLEM, compainName);
        this.statDataCollection.updateData(INCOMING_REJECT_PER_PROBLEM, provider.getCurrentDialogsCount());
        if (res != null)
            return res.getStringLongValue();
        else
            return null;
    }

    public void updateIncomingRejectPerProblem(String name) {
        this.statDataCollection.updateData(INCOMING_REJECT_PER_PROBLEM, name);
    }

}
