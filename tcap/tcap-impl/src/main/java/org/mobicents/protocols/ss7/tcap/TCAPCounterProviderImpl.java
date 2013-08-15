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

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.mobicents.protocols.ss7.statistics.StatDataCollection;
import org.mobicents.protocols.ss7.statistics.StatDataCollectorType;
import org.mobicents.protocols.ss7.statistics.StringLongMap;
import org.mobicents.protocols.ss7.tcap.api.TCAPCounterProvider;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TCAPCounterProviderImpl implements TCAPCounterProvider {

    private UUID sessionId = UUID.randomUUID();

    private TCAPProviderImpl provider;
    private StatDataCollection statDataCollection = new StatDataCollection();

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

    private Double allDialogsDuration = 0.0;

    private StringLongMap outgoingDialogsPerApplicatioContextName = new StringLongMap();
    private StringLongMap incomingDialogsPerApplicatioContextName = new StringLongMap();
    private StringLongMap outgoingInvokesPerOperationCode = new StringLongMap();
    private StringLongMap incomingInvokesPerOperationCode = new StringLongMap();
    private StringLongMap outgoingErrorsPerErrorCode = new StringLongMap();
    private StringLongMap incomingErrorsPerErrorCode = new StringLongMap();
    private StringLongMap outgoingRejectPerProblem = new StringLongMap();
    private StringLongMap incomingRejectPerProblem = new StringLongMap();

    private static String MIN_DIALOGS_COUNT = "MinDialogsCount";
    private static String MAX_DIALOGS_COUNT = "MaxDialogsCount";

    public TCAPCounterProviderImpl(TCAPProviderImpl provider) {
        this.provider = provider;

        this.statDataCollection.registerStatCounterCollector(MIN_DIALOGS_COUNT, StatDataCollectorType.MIN);
        this.statDataCollection.registerStatCounterCollector(MAX_DIALOGS_COUNT, StatDataCollectorType.MAX);
    }


    @Override
    public UUID getSessionId() {
        return sessionId;
    }


    @Override
    public long getTcUniReceivedCount() {
        return tcUniReceivedCount.get();
    }

    public void updateTcUniReceivedCount() {
        tcUniReceivedCount.addAndGet(1);
    }

    @Override
    public long getTcUniSentCount() {
        return tcUniSentCount.get();
    }

    public void updateTcUniSentCount() {
        tcUniSentCount.addAndGet(1);
    }

    @Override
    public long getTcBeginReceivedCount() {
        return tcBeginReceivedCount.get();
    }

    public void updateTcBeginReceivedCount() {
        tcBeginReceivedCount.addAndGet(1);
    }

    @Override
    public long getTcBeginSentCount() {
        return tcBeginSentCount.get();
    }

    public void updateTcBeginSentCount() {
        tcBeginSentCount.addAndGet(1);
    }

    @Override
    public long getTcContinueReceivedCount() {
        return tcContinueReceivedCount.get();
    }

    public void updateTcContinueReceivedCount() {
        tcContinueReceivedCount.addAndGet(1);
    }

    @Override
    public long getTcContinueSentCount() {
        return tcContinueSentCount.get();
    }

    public void updateTcContinueSentCount() {
        tcContinueSentCount.addAndGet(1);
    }

    @Override
    public long getTcEndReceivedCount() {
        return tcEndReceivedCount.get();
    }

    public void updateTcEndReceivedCount() {
        tcEndReceivedCount.addAndGet(1);
    }

    @Override
    public long getTcEndSentCount() {
        return tcEndSentCount.get();
    }

    public void updateTcEndSentCount() {
        tcEndSentCount.addAndGet(1);
    }

    @Override
    public long getTcPAbortReceivedCount() {
        return tcPAbortReceivedCount.get();
    }

    public void updateTcPAbortReceivedCount() {
        tcPAbortReceivedCount.addAndGet(1);
    }

    @Override
    public long getTcPAbortSentCount() {
        return tcPAbortSentCount.get();
    }

    public void updateTcPAbortSentCount() {
        tcPAbortSentCount.addAndGet(1);
    }

    @Override
    public long getTcUserAbortReceivedCount() {
        return tcUserAbortReceivedCount.get();
    }

    public void updateTcUserAbortReceivedCount() {
        tcUserAbortReceivedCount.addAndGet(1);
    }

    @Override
    public long getTcUserAbortSentCount() {
        return tcUserAbortSentCount.get();
    }

    public void updateTcUserAbortSentCount() {
        tcUserAbortSentCount.addAndGet(1);
    }

    @Override
    public long getInvokeReceivedCount() {
        return invokeReceivedCount.get();
    }

    public void updateInvokeReceivedCount() {
        invokeReceivedCount.addAndGet(1);
    }

    @Override
    public long getInvokeSentCount() {
        return invokeSentCount.get();
    }

    public void updateInvokeSentCount() {
        invokeSentCount.addAndGet(1);
    }

    @Override
    public long getReturnResultReceivedCount() {
        return returnResultReceivedCount.get();
    }

    public void updateReturnResultReceivedCount() {
        returnResultReceivedCount.addAndGet(1);
    }

    @Override
    public long getReturnResultSentCount() {
        return returnResultSentCount.get();
    }

    public void updateReturnResultSentCount() {
        returnResultSentCount.addAndGet(1);
    }

    @Override
    public long getReturnResultLastReceivedCount() {
        return returnResultLastReceivedCount.get();
    }

    public void updateReturnResultLastReceivedCount() {
        returnResultLastReceivedCount.addAndGet(1);
    }

    @Override
    public long getReturnResultLastSentCount() {
        return returnResultLastSentCount.get();
    }

    public void updateReturnResultLastSentCount() {
        returnResultLastSentCount.addAndGet(1);
    }

    @Override
    public long getReturnErrorReceivedCount() {
        return returnErrorReceivedCount.get();
    }

    public void updateReturnErrorReceivedCount() {
        returnErrorReceivedCount.addAndGet(1);
    }

    @Override
    public long getReturnErrorSentCount() {
        return returnErrorSentCount.get();
    }

    public void updateReturnErrorSentCount() {
        returnErrorSentCount.addAndGet(1);
    }

    @Override
    public long getRejectReceivedCount() {
        return rejectReceivedCount.get();
    }

    public void updateRejectReceivedCount() {
        rejectReceivedCount.addAndGet(1);
    }

    @Override
    public long getRejectSentCount() {
        return rejectSentCount.get();
    }

    public void updateRejectSentCount() {
        rejectSentCount.addAndGet(1);
    }

    @Override
    public long getDialogTimeoutCount() {
        return dialogTimeoutCount.get();
    }

    public void updateDialogTimeoutCount() {
        dialogTimeoutCount.addAndGet(1);
    }

    @Override
    public long getDialogReleaseCount() {
        return dialogReleaseCount.get();
    }

    public void updateDialogReleaseCount() {
        dialogReleaseCount.addAndGet(1);
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
        return this.statDataCollection.restartAndGet(MIN_DIALOGS_COUNT, compainName, provider.getCurrentDialogsCount());
    }

    public void updateMinDialogsCount(long newVal) {
        this.statDataCollection.updateData(MIN_DIALOGS_COUNT, newVal);
    }

    @Override
    public Long getMaxDialogsCount(String compainName) {
        return this.statDataCollection.restartAndGet(MAX_DIALOGS_COUNT, compainName, provider.getCurrentDialogsCount());
    }

    public void updateMaxDialogsCount(long newVal) {
        this.statDataCollection.updateData(MAX_DIALOGS_COUNT, newVal);
    }

    @Override
    public double getAllDialogsDuration() {
        return allDialogsDuration;
    }

    public void updateAllDialogsDuration(double diff) {
        synchronized (allDialogsDuration) {
            allDialogsDuration += diff;
        }
    }

    @Override
    public Map<String, Long> getOutgoingDialogsPerApplicatioContextName() {
        Map<String, Long> res = outgoingDialogsPerApplicatioContextName.restartAndGet();
        return res;
    }

    public void updateOutgoingDialogsPerApplicatioContextName(String name) {
        outgoingDialogsPerApplicatioContextName.updateData(name);
    }

    @Override
    public Map<String, Long> getIncomingDialogsPerApplicatioContextName() {
        Map<String, Long> res = incomingDialogsPerApplicatioContextName.restartAndGet();
        return res;
    }

    public void updateIncomingDialogsPerApplicatioContextName(String name) {
        incomingDialogsPerApplicatioContextName.updateData(name);
    }

    @Override
    public Map<String, Long> getOutgoingInvokesPerOperationCode() {
        Map<String, Long> res = outgoingInvokesPerOperationCode.restartAndGet();
        return res;
    }

    public void updateOutgoingInvokesPerOperationCode(String name) {
        outgoingInvokesPerOperationCode.updateData(name);
    }

    @Override
    public Map<String, Long> getIncomingInvokesPerOperationCode() {
        Map<String, Long> res = incomingInvokesPerOperationCode.restartAndGet();
        return res;
    }

    public void updateIncomingInvokesPerOperationCode(String name) {
        incomingInvokesPerOperationCode.updateData(name);
    }

    @Override
    public Map<String, Long> getOutgoingErrorsPerErrorCode() {
        Map<String, Long> res = outgoingErrorsPerErrorCode.restartAndGet();
        return res;
    }

    public void updateOutgoingErrorsPerErrorCode(String name) {
        outgoingErrorsPerErrorCode.updateData(name);
    }

    @Override
    public Map<String, Long> getIncomingErrorsPerErrorCode() {
        Map<String, Long> res = incomingErrorsPerErrorCode.restartAndGet();
        return res;
    }

    public void updateIncomingErrorsPerErrorCode(String name) {
        incomingErrorsPerErrorCode.updateData(name);
    }

    @Override
    public Map<String, Long> getOutgoingRejectPerProblem() {
        Map<String, Long> res = outgoingRejectPerProblem.restartAndGet();
        return res;
    }

    public void updateOutgoingRejectPerProblem(String name) {
        outgoingRejectPerProblem.updateData(name);
    }

    @Override
    public Map<String, Long> getIncomingRejectPerProblem() {
        Map<String, Long> res = incomingRejectPerProblem.restartAndGet();
        return res;
    }

    public void updateIncomingRejectPerProblem(String name) {
        incomingRejectPerProblem.updateData(name);
    }

}
