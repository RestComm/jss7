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

package org.mobicents.protocols.ss7.tcapAnsi;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.mobicents.protocols.ss7.statistics.StatDataCollectionImpl;
import org.mobicents.protocols.ss7.statistics.api.LongValue;
import org.mobicents.protocols.ss7.statistics.api.StatDataCollection;
import org.mobicents.protocols.ss7.statistics.api.StatDataCollectorType;
import org.mobicents.protocols.ss7.statistics.api.StatResult;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPCounterEventsListener;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPCounterProvider;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;

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
    private AtomicLong tcQueryReceivedCount = new AtomicLong();
    private AtomicLong tcQuerySentCount = new AtomicLong();
    private AtomicLong tcConversationReceivedCount = new AtomicLong();
    private AtomicLong tcConversationSentCount = new AtomicLong();
    private AtomicLong tcResponseReceivedCount = new AtomicLong();
    private AtomicLong tcResponseSentCount = new AtomicLong();
    private AtomicLong tcPAbortReceivedCount = new AtomicLong();
    private AtomicLong tcPAbortSentCount = new AtomicLong();
    private AtomicLong tcUserAbortReceivedCount = new AtomicLong();
    private AtomicLong tcUserAbortSentCount = new AtomicLong();

    private AtomicLong invokeLastReceivedCount = new AtomicLong();
    private AtomicLong invokeNotLastReceivedCount = new AtomicLong();
    private AtomicLong invokeLastSentCount = new AtomicLong();
    private AtomicLong invokeNotLastSentCount = new AtomicLong();
    private AtomicLong returnResultNotLastReceivedCount = new AtomicLong();
    private AtomicLong returnResultNotLastSentCount = new AtomicLong();
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

    private static String MAX_NETWORK_ID_AREAS_NOT_AVAILABLE = "MaxNetworkIdAreasNotAvailable";
    private static String MAX_NETWORK_ID_AREAS_CONGLEVEL_1 = "MaxNetworkIdAreasCongLevel_1";
    private static String MAX_NETWORK_ID_AREAS_CONGLEVEL_2 = "MaxNetworkIdAreasCongLevel_2";
    private static String MAX_NETWORK_ID_AREAS_CONGLEVEL_3 = "MaxNetworkIdAreasCongLevel_3";
    private static String MAX_EXECUTORS_CONG_LEVEL_1 = "MaxExecutorsCongLevel_1";
    private static String MAX_EXECUTORS_CONG_LEVEL_2 = "MaxExecutorsCongLevel_2";
    private static String MAX_EXECUTORS_CONG_LEVEL_3 = "MaxExecutorsCongLevel_3";
    private static String MAX_MEMORY_CONG_LEVEL = "MaxMemoryCongLevel";
    private static String MAX_USER_PARTS_CONG_LEVEL_1 = "MaxUserPartsCongLevel_1";
    private static String MAX_USER_PARTS_CONG_LEVEL_2 = "MaxUserPartsCongLevel_2";
    private static String MAX_USER_PARTS_CONG_LEVEL_3 = "MaxUserPartsCongLevel_3";

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

        this.statDataCollection.registerStatCounterCollector(MAX_NETWORK_ID_AREAS_NOT_AVAILABLE, StatDataCollectorType.MAX);
        this.statDataCollection.registerStatCounterCollector(MAX_NETWORK_ID_AREAS_CONGLEVEL_1, StatDataCollectorType.MAX);
        this.statDataCollection.registerStatCounterCollector(MAX_NETWORK_ID_AREAS_CONGLEVEL_2, StatDataCollectorType.MAX);
        this.statDataCollection.registerStatCounterCollector(MAX_NETWORK_ID_AREAS_CONGLEVEL_3, StatDataCollectorType.MAX);
        this.statDataCollection.registerStatCounterCollector(MAX_EXECUTORS_CONG_LEVEL_1, StatDataCollectorType.MAX);
        this.statDataCollection.registerStatCounterCollector(MAX_EXECUTORS_CONG_LEVEL_2, StatDataCollectorType.MAX);
        this.statDataCollection.registerStatCounterCollector(MAX_EXECUTORS_CONG_LEVEL_3, StatDataCollectorType.MAX);
        this.statDataCollection.registerStatCounterCollector(MAX_MEMORY_CONG_LEVEL, StatDataCollectorType.MAX);
        this.statDataCollection.registerStatCounterCollector(MAX_USER_PARTS_CONG_LEVEL_1, StatDataCollectorType.MAX);
        this.statDataCollection.registerStatCounterCollector(MAX_USER_PARTS_CONG_LEVEL_2, StatDataCollectorType.MAX);
        this.statDataCollection.registerStatCounterCollector(MAX_USER_PARTS_CONG_LEVEL_3, StatDataCollectorType.MAX);
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
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateTcUniReceivedCount(dialog);
        }
    }

    @Override
    public long getTcUniSentCount() {
        return tcUniSentCount.get();
    }

    public void updateTcUniSentCount(Dialog dialog) {
        tcUniSentCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateTcUniSentCount(dialog);
        }
    }

    @Override
    public long getTcQueryReceivedCount() {
        return tcQueryReceivedCount.get();
    }

    public void updateTcQueryReceivedCount(Dialog dialog) {
        tcQueryReceivedCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateTcQueryReceivedCount(dialog);
        }
    }

    @Override
    public long getTcQuerySentCount() {
        return tcQuerySentCount.get();
    }

    public void updateTcQuerySentCount(Dialog dialog) {
        tcQuerySentCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateTcQuerySentCount(dialog);
        }
    }

    @Override
    public long getTcConversationReceivedCount() {
        return tcConversationReceivedCount.get();
    }

    public void updateTcConversationReceivedCount(Dialog dialog) {
        tcConversationReceivedCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateTcConversationReceivedCount(dialog);
        }
    }

    @Override
    public long getTcConversationSentCount() {
        return tcConversationSentCount.get();
    }

    public void updateTcConversationSentCount(Dialog dialog) {
        tcConversationSentCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateTcConversationSentCount(dialog);
        }
    }

    @Override
    public long getTcResponseReceivedCount() {
        return tcResponseReceivedCount.get();
    }

    public void updateTcResponseReceivedCount(Dialog dialog) {
        tcResponseReceivedCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateTcResponseReceivedCount(dialog);
        }
    }

    @Override
    public long getTcResponseSentCount() {
        return tcResponseSentCount.get();
    }

    public void updateTcResponseSentCount(Dialog dialog) {
        tcResponseSentCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateTcResponseSentCount(dialog);
        }
    }

    @Override
    public long getTcPAbortReceivedCount() {
        return tcPAbortReceivedCount.get();
    }

    public void updateTcPAbortReceivedCount(Dialog dialog, PAbortCause cause) {
        tcPAbortReceivedCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateTcPAbortReceivedCount(dialog, cause);
        }
    }

    @Override
    public long getTcPAbortSentCount() {
        return tcPAbortSentCount.get();
    }

    public void updateTcPAbortSentCount(byte[] originatingTransactionId, PAbortCause cause) {
        tcPAbortSentCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateTcPAbortSentCount(originatingTransactionId, cause);
        }
    }

    @Override
    public long getTcUserAbortReceivedCount() {
        return tcUserAbortReceivedCount.get();
    }

    public void updateTcUserAbortReceivedCount(Dialog dialog) {
        tcUserAbortReceivedCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateTcUserAbortReceivedCount(dialog);
        }
    }

    @Override
    public long getTcUserAbortSentCount() {
        return tcUserAbortSentCount.get();
    }

    public void updateTcUserAbortSentCount(Dialog dialog) {
        tcUserAbortSentCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateTcUserAbortSentCount(dialog);
        }
    }

    @Override
    public long getInvokeLastReceivedCount() {
        return invokeLastReceivedCount.get();
    }

    public void updateInvokeLastReceivedCount(Dialog dialog, Invoke invoke) {
        invokeLastReceivedCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateInvokeLastReceivedCount(dialog, invoke);
        }
    }

    @Override
    public long getInvokeNotLastReceivedCount() {
        return invokeNotLastReceivedCount.get();
    }

    public void updateInvokeNotLastReceivedCount(Dialog dialog, Invoke invoke) {
        invokeNotLastReceivedCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateInvokeNotLastReceivedCount(dialog, invoke);
        }
    }

    @Override
    public long getInvokeLastSentCount() {
        return invokeLastSentCount.get();
    }

    public void updateInvokeLastSentCount(Dialog dialog, Invoke invoke) {
        invokeLastSentCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateInvokeLastSentCount(dialog, invoke);
        }
    }

    @Override
    public long getInvokeNotLastSentCount() {
        return invokeNotLastSentCount.get();
    }

    public void updateInvokeNotLastSentCount(Dialog dialog, Invoke invoke) {
        invokeNotLastSentCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateInvokeNotLastSentCount(dialog, invoke);
        }
    }

    @Override
    public long getReturnResultNotLastReceivedCount() {
        return returnResultNotLastReceivedCount.get();
    }

    public void updateReturnResultNotLastReceivedCount(Dialog dialog) {
        returnResultNotLastReceivedCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateReturnResultNotLastReceivedCount(dialog);
        }
    }

    @Override
    public long getReturnResultNotLastSentCount() {
        return returnResultNotLastSentCount.get();
    }

    public void updateReturnResultNotLastSentCount(Dialog dialog) {
        returnResultNotLastSentCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateReturnResultNotLastSentCount(dialog);
        }
    }

    @Override
    public long getReturnResultLastReceivedCount() {
        return returnResultLastReceivedCount.get();
    }

    public void updateReturnResultLastReceivedCount(Dialog dialog) {
        returnResultLastReceivedCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateReturnResultLastReceivedCount(dialog);
        }
    }

    @Override
    public long getReturnResultLastSentCount() {
        return returnResultLastSentCount.get();
    }

    public void updateReturnResultLastSentCount(Dialog dialog) {
        returnResultLastSentCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateReturnResultLastSentCount(dialog);
        }
    }

    @Override
    public long getReturnErrorReceivedCount() {
        return returnErrorReceivedCount.get();
    }

    public void updateReturnErrorReceivedCount(Dialog dialog) {
        returnErrorReceivedCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateReturnErrorReceivedCount(dialog);
        }
    }

    @Override
    public long getReturnErrorSentCount() {
        return returnErrorSentCount.get();
    }

    public void updateReturnErrorSentCount(Dialog dialog) {
        returnErrorSentCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateReturnErrorSentCount(dialog);
        }
    }

    @Override
    public long getRejectReceivedCount() {
        return rejectReceivedCount.get();
    }

    public void updateRejectReceivedCount(Dialog dialog) {
        rejectReceivedCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateRejectReceivedCount(dialog);
        }
    }

    @Override
    public long getRejectSentCount() {
        return rejectSentCount.get();
    }

    public void updateRejectSentCount(Dialog dialog) {
        rejectSentCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateRejectSentCount(dialog);
        }
    }

    @Override
    public long getDialogTimeoutCount() {
        return dialogTimeoutCount.get();
    }

    public void updateDialogTimeoutCount(Dialog dialog) {
        dialogTimeoutCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
            listener.updateDialogTimeoutCount(dialog);
        }
    }

    @Override
    public long getDialogReleaseCount() {
        return dialogReleaseCount.get();
    }

    public void updateDialogReleaseCount(Dialog dialog) {
        dialogReleaseCount.addAndGet(1);
        TCAPCounterEventsListener listener = provider.getStack().getTCAPCounterEventsListener();
        if (listener != null) {
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

    @Override
    public Long getMaxNetworkIdAreasNotAvailable(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MAX_NETWORK_ID_AREAS_NOT_AVAILABLE, compainName);
        this.statDataCollection.updateData(MAX_NETWORK_ID_AREAS_NOT_AVAILABLE, provider.getNetworkIdAreasNotAvailableCount());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMaxNetworkIdAreasNotAvailable(long newVal) {
        this.statDataCollection.updateData(MAX_NETWORK_ID_AREAS_NOT_AVAILABLE, newVal);
    }

    @Override
    public Long getMaxNetworkIdAreasCongLevel_1(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MAX_NETWORK_ID_AREAS_CONGLEVEL_1, compainName);
        this.statDataCollection.updateData(MAX_NETWORK_ID_AREAS_CONGLEVEL_1, provider.getNetworkIdAreasCongLevel_1_Count());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMaxNetworkIdAreasCongLevel_1(long newVal) {
        this.statDataCollection.updateData(MAX_NETWORK_ID_AREAS_CONGLEVEL_1, newVal);
    }

    @Override
    public Long getMaxNetworkIdAreasCongLevel_2(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MAX_NETWORK_ID_AREAS_CONGLEVEL_2, compainName);
        this.statDataCollection.updateData(MAX_NETWORK_ID_AREAS_CONGLEVEL_2, provider.getNetworkIdAreasCongLevel_2_Count());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMaxNetworkIdAreasCongLevel_2(long newVal) {
        this.statDataCollection.updateData(MAX_NETWORK_ID_AREAS_CONGLEVEL_2, newVal);
    }

    @Override
    public Long getMaxNetworkIdAreasCongLevel_3(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MAX_NETWORK_ID_AREAS_CONGLEVEL_3, compainName);
        this.statDataCollection.updateData(MAX_NETWORK_ID_AREAS_CONGLEVEL_3, provider.getNetworkIdAreasCongLevel_3_Count());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMaxNetworkIdAreasCongLevel_3(long newVal) {
        this.statDataCollection.updateData(MAX_NETWORK_ID_AREAS_CONGLEVEL_3, newVal);
    }

    @Override
    public Long getMaxExecutorsCongLevel_1(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MAX_EXECUTORS_CONG_LEVEL_1, compainName);
        this.statDataCollection.updateData(MAX_EXECUTORS_CONG_LEVEL_1, provider.getExecutorCountWithCongestionLevel_1());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMaxExecutorsCongLevel_1(long newVal) {
        this.statDataCollection.updateData(MAX_EXECUTORS_CONG_LEVEL_1, newVal);
    }

    @Override
    public Long getMaxExecutorsCongLevel_2(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MAX_EXECUTORS_CONG_LEVEL_2, compainName);
        this.statDataCollection.updateData(MAX_EXECUTORS_CONG_LEVEL_2, provider.getExecutorCountWithCongestionLevel_2());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMaxExecutorsCongLevel_2(long newVal) {
        this.statDataCollection.updateData(MAX_EXECUTORS_CONG_LEVEL_2, newVal);
    }

    @Override
    public Long getMaxExecutorsCongLevel_3(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MAX_EXECUTORS_CONG_LEVEL_3, compainName);
        this.statDataCollection.updateData(MAX_EXECUTORS_CONG_LEVEL_3, provider.getExecutorCountWithCongestionLevel_3());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMaxExecutorsCongLevel_3(long newVal) {
        this.statDataCollection.updateData(MAX_EXECUTORS_CONG_LEVEL_3, newVal);
    }

    @Override
    public Long getMaxMemoryCongLevel(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MAX_MEMORY_CONG_LEVEL, compainName);
        this.statDataCollection.updateData(MAX_MEMORY_CONG_LEVEL, provider.getMemoryCongestionLevel());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMaxMemoryCongLevel(long newVal) {
        this.statDataCollection.updateData(MAX_MEMORY_CONG_LEVEL, newVal);
    }

    @Override
    public Long getMaxUserPartsCongLevel_1(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MAX_USER_PARTS_CONG_LEVEL_1, compainName);
        this.statDataCollection.updateData(MAX_USER_PARTS_CONG_LEVEL_1, provider.getUserPartCongestionLevel_1());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMaxUserPartsCongLevel_1(long newVal) {
        this.statDataCollection.updateData(MAX_USER_PARTS_CONG_LEVEL_1, newVal);
    }

    @Override
    public Long getMaxUserPartsCongLevel_2(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MAX_USER_PARTS_CONG_LEVEL_2, compainName);
        this.statDataCollection.updateData(MAX_USER_PARTS_CONG_LEVEL_2, provider.getUserPartCongestionLevel_2());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMaxUserPartsCongLevel_2(long newVal) {
        this.statDataCollection.updateData(MAX_USER_PARTS_CONG_LEVEL_2, newVal);
    }

    @Override
    public Long getMaxUserPartsCongLevel_3(String compainName) {
        StatResult res = this.statDataCollection.restartAndGet(MAX_USER_PARTS_CONG_LEVEL_3, compainName);
        this.statDataCollection.updateData(MAX_USER_PARTS_CONG_LEVEL_3, provider.getUserPartCongestionLevel_3());
        if (res != null)
            return res.getLongValue();
        else
            return null;
    }

    public void updateMaxUserPartsCongLevel_3(long newVal) {
        this.statDataCollection.updateData(MAX_USER_PARTS_CONG_LEVEL_3, newVal);
    }

}
