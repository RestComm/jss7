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

import org.mobicents.protocols.ss7.tcap.api.TCAPCounterProvider;

public class TCAPCounterProviderImpl implements TCAPCounterProvider {

    private UUID sessionId = UUID.randomUUID();

    private AtomicLong tcUniRecievedCount = new AtomicLong();
    private AtomicLong tcUniSentCount = new AtomicLong();
    private AtomicLong tcBeginRecievedCount = new AtomicLong();
    private AtomicLong tcBeginSentCount = new AtomicLong();
    private AtomicLong tcContinueRecievedCount = new AtomicLong();
    private AtomicLong tcContinueSentCount = new AtomicLong();
    private AtomicLong tcEndRecievedCount = new AtomicLong();
    private AtomicLong tcEndSentCount = new AtomicLong();
    private AtomicLong tcPAbortRecievedCount = new AtomicLong();
    private AtomicLong tcPAbortSentCount = new AtomicLong();
    private AtomicLong tcUserAbortRecievedCount = new AtomicLong();
    private AtomicLong tcUserAbortSentCount = new AtomicLong();


    public TCAPCounterProviderImpl() {
    }


    @Override
    public UUID getSessionId() {
        return sessionId;
    }


    @Override
    public long getTcUniRecievedCount() {
        return tcUniRecievedCount.get();
    }

    public void addTcUniRecievedCount() {
        tcUniRecievedCount.addAndGet(1);
    }

    @Override
    public long getTcUniSentCount() {
        return tcUniSentCount.get();
    }

    public void addTcUniSentCount() {
        tcUniSentCount.addAndGet(1);
    }

    @Override
    public long getTcBeginRecievedCount() {
        return tcBeginRecievedCount.get();
    }

    public void addTcBeginRecievedCount() {
        tcBeginRecievedCount.addAndGet(1);
    }

    @Override
    public long getTcBeginSentCount() {
        return tcBeginSentCount.get();
    }

    public void addTcBeginSentCount() {
        tcBeginSentCount.addAndGet(1);
    }

    @Override
    public long getTcContinueRecievedCount() {
        return tcContinueRecievedCount.get();
    }

    public void addTcContinueRecievedCount() {
        tcContinueRecievedCount.addAndGet(1);
    }

    @Override
    public long getTcContinueSentCount() {
        return tcContinueSentCount.get();
    }

    public void addTcContinueSentCount() {
        tcContinueSentCount.addAndGet(1);
    }

    @Override
    public long getTcEndRecievedCount() {
        return tcEndRecievedCount.get();
    }

    public void addTcEndRecievedCount() {
        tcEndRecievedCount.addAndGet(1);
    }

    @Override
    public long getTcEndSentCount() {
        return tcEndSentCount.get();
    }

    public void addTcEndSentCount() {
        tcEndSentCount.addAndGet(1);
    }

    @Override
    public long getTcPAbortRecievedCount() {
        return tcPAbortRecievedCount.get();
    }

    public void addTcPAbortRecievedCount() {
        tcPAbortRecievedCount.addAndGet(1);
    }

    @Override
    public long getTcPAbortSentCount() {
        return tcPAbortSentCount.get();
    }

    public void addTcPAbortSentCount() {
        tcPAbortSentCount.addAndGet(1);
    }

    @Override
    public long getTcUserAbortRecievedCount() {
        return tcUserAbortRecievedCount.get();
    }

    public void addTcUserAbortRecievedCount() {
        tcUserAbortRecievedCount.addAndGet(1);
    }

    @Override
    public long getTcUserAbortSentCount() {
        return tcUserAbortSentCount.get();
    }

    public void addTcUserAbortSentCount() {
        tcUserAbortSentCount.addAndGet(1);
    }



    //.............................

    @Override
    public long getInvokeRecievedCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getInvokeSentCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getReturnResultRecievedCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getReturnResultSentCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getReturnResultLastRecievedCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getReturnResultLastSentCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getReturnErrorRecievedCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getReturnErrorSentCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getRejectRecievedCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getRejectSentCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getDialogRequestCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getDialogTimeoutCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getDialogReleaseCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getCurrentDialogsCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getAllDialogsCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Long getMinDialogsCount(String compainName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getMaxDialogsCount(String compainName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getAllDialogsDuration() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Map<String, Long> getOutgoingDialogsPerApplicatioContextName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Long> getIncomingDialogsPerApplicatioContextName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Long> getOutgoingInvokesPerOperationCode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Long> getIncomingInvokesPerOperationCode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Long> getOutgoingErrorsPerErrorCode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Long> getIncomingErrorsPerErrorCode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Long> getOutgoingRejectPerProblem() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Long> getIncomingRejectPerProblem() {
        // TODO Auto-generated method stub
        return null;
    }

}
