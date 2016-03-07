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

package org.mobicents.protocols.ss7.oam.common.tcap;

import java.util.Map;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.oam.common.jmx.MBeanHost;
import org.mobicents.protocols.ss7.oam.common.jmx.MBeanType;
import org.mobicents.protocols.ss7.oam.common.jmxss7.Ss7Layer;
import org.mobicents.protocols.ss7.oam.common.statistics.ComplexValueImpl;
import org.mobicents.protocols.ss7.oam.common.statistics.CounterDefImpl;
import org.mobicents.protocols.ss7.oam.common.statistics.CounterDefSetImpl;
import org.mobicents.protocols.ss7.oam.common.statistics.SourceValueCounterImpl;
import org.mobicents.protocols.ss7.oam.common.statistics.SourceValueObjectImpl;
import org.mobicents.protocols.ss7.oam.common.statistics.SourceValueSetImpl;
import org.mobicents.protocols.ss7.oam.common.statistics.api.ComplexValue;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterDef;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterDefSet;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterMediator;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterType;
import org.mobicents.protocols.ss7.oam.common.statistics.api.SourceValueSet;
import org.mobicents.protocols.ss7.statistics.api.LongValue;
import org.mobicents.protocols.ss7.tcap.api.TCAPCounterProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;

/**
*
* @author sergey vetyutnev
*
*/
public class TcapManagementJmx implements TcapManagementJmxMBean, CounterMediator {

    protected final Logger logger;

    private final MBeanHost ss7Management;
    private final TCAPStack wrappedTCAPStack;

    private FastMap<String, CounterDefSet> lstCounters = new FastMap<String, CounterDefSet>();

    public TcapManagementJmx(MBeanHost ss7Management, TCAPStack wrappedTCAPStack) {
        this.ss7Management = ss7Management;
        this.wrappedTCAPStack = wrappedTCAPStack;

        this.logger = Logger.getLogger(TcapManagementJmx.class.getCanonicalName() + "-" + wrappedTCAPStack.getName());
    }

    /**
     * methods - bean life-cycle
     */

    @Override
    public void start() throws Exception {
        logger.info("Starting ...");

        setupCounterList();

        this.ss7Management.registerMBean(Ss7Layer.TCAP, TcapManagementType.MANAGEMENT, this.getName(), this);

        logger.info("Started ...");
    }

    @Override
    public void stop() {
        logger.info("Stopping ...");
        logger.info("Stopped ...");
    }

    @Override
    public String getName() {
        return this.wrappedTCAPStack.getName();
    }

    @Override
    public int getSubSystemNumber(){
        return this.wrappedTCAPStack.getSubSystemNumber();
    }

    @Override
    public String getPersistDir() {
        return this.wrappedTCAPStack.getPersistDir();
    }

    @Override
    public long getDialogIdRangeEnd() {
        return wrappedTCAPStack.getDialogIdRangeEnd();
    }

    @Override
    public long getDialogIdRangeStart() {
        return wrappedTCAPStack.getDialogIdRangeStart();
    }

    @Override
    public long getDialogIdleTimeout() {
        return wrappedTCAPStack.getDialogIdleTimeout();
    }

    @Override
    public long getInvokeTimeout() {
        return wrappedTCAPStack.getInvokeTimeout();
    }

    @Override
    public int getMaxDialogs() {
        return wrappedTCAPStack.getMaxDialogs();
    }

    @Override
    public boolean getPreviewMode() {
        return wrappedTCAPStack.getPreviewMode();
    }

    @Override
    public TCAPProvider getProvider() {
        return null;
    }

    @Override
    public boolean isStarted() {
        return wrappedTCAPStack.isStarted();
    }

    @Override
    public void setDialogIdRangeEnd(long arg0) throws Exception {
        wrappedTCAPStack.setDialogIdRangeEnd(arg0);
    }

    @Override
    public void setDialogIdRangeStart(long arg0) throws Exception {
        wrappedTCAPStack.setDialogIdRangeStart(arg0);
    }

    @Override
    public void setDialogIdleTimeout(long arg0) throws Exception {
        wrappedTCAPStack.setDialogIdleTimeout(arg0);
    }

    @Override
    public void setInvokeTimeout(long arg0) throws Exception {
        wrappedTCAPStack.setInvokeTimeout(arg0);
    }

    @Override
    public void setMaxDialogs(int arg0) throws Exception {
        wrappedTCAPStack.setMaxDialogs(arg0);
    }

    @Override
    public void setPreviewMode(boolean arg0) throws Exception {
        wrappedTCAPStack.setPreviewMode(arg0);
    }

    @Override
    public TCAPCounterProvider getCounterProvider() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDoNotSendProtocolVersion(boolean val) throws Exception {
        this.wrappedTCAPStack.setDoNotSendProtocolVersion(val);
    }

    @Override
    public boolean getDoNotSendProtocolVersion() {
        return this.wrappedTCAPStack.getDoNotSendProtocolVersion();
    }

    @Override
    public void setStatisticsEnabled(boolean val) throws Exception {
        this.wrappedTCAPStack.setStatisticsEnabled(val);
    }

    @Override
    public boolean getStatisticsEnabled() {
        return this.wrappedTCAPStack.getStatisticsEnabled();
    }

    @Override
    public boolean isCongControl_blockingIncomingTcapMessages() {
        return this.wrappedTCAPStack.isCongControl_blockingIncomingTcapMessages();
    }

    @Override
    public void setCongControl_blockingIncomingTcapMessages(boolean value) throws Exception {
        this.wrappedTCAPStack.setCongControl_blockingIncomingTcapMessages(value);
    }

    @Override
    public double getCongControl_ExecutorDelayThreshold_1() {
        return this.wrappedTCAPStack.getCongControl_ExecutorDelayThreshold_1();
    }

    @Override
    public double getCongControl_ExecutorDelayThreshold_2() {
        return this.wrappedTCAPStack.getCongControl_ExecutorDelayThreshold_2();
    }

    @Override
    public double getCongControl_ExecutorDelayThreshold_3() {
        return this.wrappedTCAPStack.getCongControl_ExecutorDelayThreshold_3();
    }

    @Override
    public void setCongControl_ExecutorDelayThreshold_1(double value) throws Exception {
        this.wrappedTCAPStack.setCongControl_ExecutorDelayThreshold_1(value);
    }

    @Override
    public void setCongControl_ExecutorDelayThreshold_2(double value) throws Exception {
        this.wrappedTCAPStack.setCongControl_ExecutorDelayThreshold_2(value);
    }

    @Override
    public void setCongControl_ExecutorDelayThreshold_3(double value) throws Exception {
        this.wrappedTCAPStack.setCongControl_ExecutorDelayThreshold_3(value);
    }

    @Override
    public double getCongControl_ExecutorBackToNormalDelayThreshold_1() {
        return this.wrappedTCAPStack.getCongControl_ExecutorBackToNormalDelayThreshold_1();
    }

    @Override
    public double getCongControl_ExecutorBackToNormalDelayThreshold_2() {
        return this.wrappedTCAPStack.getCongControl_ExecutorBackToNormalDelayThreshold_2();
    }

    @Override
    public double getCongControl_ExecutorBackToNormalDelayThreshold_3() {
        return this.wrappedTCAPStack.getCongControl_ExecutorBackToNormalDelayThreshold_3();
    }

    @Override
    public void setCongControl_ExecutorBackToNormalDelayThreshold_1(double value) throws Exception {
        this.wrappedTCAPStack.setCongControl_ExecutorBackToNormalDelayThreshold_1(value);
    }

    @Override
    public void setCongControl_ExecutorBackToNormalDelayThreshold_2(double value) throws Exception {
        this.wrappedTCAPStack.setCongControl_ExecutorBackToNormalDelayThreshold_2(value);
    }

    @Override
    public void setCongControl_ExecutorBackToNormalDelayThreshold_3(double value) throws Exception {
        this.wrappedTCAPStack.setCongControl_ExecutorBackToNormalDelayThreshold_3(value);
    }

    @Override
    public double getCongControl_MemoryThreshold_1() {
        return this.wrappedTCAPStack.getCongControl_MemoryThreshold_1();
    }

    @Override
    public double getCongControl_MemoryThreshold_2() {
        return this.wrappedTCAPStack.getCongControl_MemoryThreshold_2();
    }

    @Override
    public double getCongControl_MemoryThreshold_3() {
        return this.wrappedTCAPStack.getCongControl_MemoryThreshold_3();
    }

    @Override
    public void setCongControl_MemoryThreshold_1(double value) throws Exception {
        this.wrappedTCAPStack.setCongControl_MemoryThreshold_1(value);
    }

    @Override
    public void setCongControl_MemoryThreshold_2(double value) throws Exception {
        this.wrappedTCAPStack.setCongControl_MemoryThreshold_2(value);
    }

    @Override
    public void setCongControl_MemoryThreshold_3(double value) throws Exception {
        this.wrappedTCAPStack.setCongControl_MemoryThreshold_3(value);
    }

    @Override
    public double getCongControl_BackToNormalMemoryThreshold_1() {
        return this.wrappedTCAPStack.getCongControl_BackToNormalMemoryThreshold_1();
    }

    @Override
    public double getCongControl_BackToNormalMemoryThreshold_2() {
        return this.wrappedTCAPStack.getCongControl_BackToNormalMemoryThreshold_2();
    }

    @Override
    public double getCongControl_BackToNormalMemoryThreshold_3() {
        return this.wrappedTCAPStack.getCongControl_BackToNormalMemoryThreshold_3();
    }

    @Override
    public void setCongControl_BackToNormalMemoryThreshold_1(double value) throws Exception {
        this.wrappedTCAPStack.setCongControl_BackToNormalMemoryThreshold_1(value);
    }

    @Override
    public void setCongControl_BackToNormalMemoryThreshold_2(double value) throws Exception {
        this.wrappedTCAPStack.setCongControl_BackToNormalMemoryThreshold_2(value);
    }

    @Override
    public void setCongControl_BackToNormalMemoryThreshold_3(double value) throws Exception {
        this.wrappedTCAPStack.setCongControl_BackToNormalMemoryThreshold_3(value);
    }

    // Statistics part

    @Override
    public String getCounterMediatorName() {
        return "Tcap-" + this.getName();
    }

    private void setupCounterList() {
        FastMap<String, CounterDefSet> lst = new FastMap<String, CounterDefSet>();

        CounterDefSetImpl cds = new CounterDefSetImpl(this.getCounterMediatorName() + "-Main");
        lst.put(cds.getName(), cds);

        CounterDef cd = new CounterDefImpl(CounterType.Summary, "TcUniReceivedCount", "A count of received TC-UNI messages");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "TcUniSentCount", "A count of sent TC-UNI messages");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "TcBeginReceivedCount", "A count of received TC-BEGIN messages");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "TcBeginSentCount", "A count of sent TC-BEGIN messages");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "TcContinueReceivedCount", "A count of received TC-CONTINUE messages");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "TcContinueSentCount", "A count of sent TC-CONTINUE messages");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "TcEndReceivedCount", "A count of received TC-END messages");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "TcEndSentCount", "A count of sent TC-END messages");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "TcPAbortReceivedCount", "A count of received TC-PROVIDER-ABORT messages");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "TcPAbortSentCount", "A count of sent TC-PROVIDER-ABORT messages");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "TcUserAbortReceivedCount", "A count of received TC-USER-ABORT messages");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "TcUserAbortSentCount", "A count of sent TC-USER-ABORT messages");
        cds.addCounterDef(cd);

        cd = new CounterDefImpl(CounterType.Summary, "InvokeReceivedCount", "A count of received Invoke components");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "InvokeSentCount", "A count of sent Invoke components");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "ReturnResultReceivedCount", "A count of received ReturtResult components");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "ReturnResultSentCount", "A count of sent ReturtResult components");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "ReturnResultLastReceivedCount", "A count of received ReturtResultLast components");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "ReturnResultLastSentCount", "A count of sent ReturtResultLast components");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "ReturnErrorReceivedCount", "A count of received ReturnError components");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "ReturnErrorSentCount", "A count of sent ReturnError components");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "RejectReceivedCount", "A count of received Reject components");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "RejectSentCount", "A count of sent Reject components");
        cds.addCounterDef(cd);

        cd = new CounterDefImpl(CounterType.Summary, "DialogTimeoutCount", "A count of received DialogTimeouts");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "DialogReleaseCount", "A count of received DialogReleases");
        cds.addCounterDef(cd);

        cd = new CounterDefImpl(CounterType.Summary, "AllEstablishedDialogsCount", "A count of all established Dialogs");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "AllLocalEstablishedDialogsCount", "A count of all established local originated Dialogs");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Summary, "AllRemoteEstablishedDialogsCount", "A count of all established remote originated Dialogs");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Minimal, "MinDialogsCount", "A min count of established Dialogs");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Maximal, "MaxDialogsCount", "A max count of established Dialogs");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.SummaryDouble, "AllDialogsDuration", "A total duration of all released Dialogs (in seconds)");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.Average, "AverageDialogsDuration", "An average duration of all released Dialogs (in seconds)");
        cds.addCounterDef(cd);

        cd = new CounterDefImpl(CounterType.ComplexValue, "OutgoingDialogsPerApplicatioContextName", "An outgoing Dialogs count per ApplicationContextNames (in string form)");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.ComplexValue, "IncomingDialogsPerApplicatioContextName", "An incoming Dialogs count per ApplicationContextNames (in string form)");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.ComplexValue, "OutgoingInvokesPerOperationCode", "An outgoing Invokes count per OperationCodes");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.ComplexValue, "IncomingInvokesPerOperationCode", "An incoming Invokes count per OperationCodes");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.ComplexValue, "OutgoingErrorsPerErrorCode", "An outgoing ReturtError count per ErrorCodes");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.ComplexValue, "IncomingErrorsPerErrorCode", "An incoming ReturtError count per ErrorCodes");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.ComplexValue, "OutgoingRejectPerProblem", "An outgoing Reject count per Problem");
        cds.addCounterDef(cd);
        cd = new CounterDefImpl(CounterType.ComplexValue, "IncomingRejectPerProblem", "An incoming Reject count per Problem");
        cds.addCounterDef(cd);

        lstCounters = lst;
    }

    @Override
    public String[] getCounterDefSetList() {

        String[] res = new String[lstCounters.size()];
        lstCounters.keySet().toArray(res);
        return res;
    }

    @Override
    public CounterDefSet getCounterDefSet(String counterDefSetName) {
        return lstCounters.get(counterDefSetName);
    }

    @Override
    public SourceValueSet getSourceValueSet(String counterDefSetName, String campaignName, int durationInSeconds) {

        if (durationInSeconds >= 60)
            logger.info("getSourceValueSet() - starting - campaignName=" + campaignName);
        else
            logger.debug("getSourceValueSet() - starting - campaignName=" + campaignName);

        SourceValueSetImpl svs;
        try {
            if (!this.wrappedTCAPStack.isStarted())
                return null;

            TCAPCounterProvider cp = this.wrappedTCAPStack.getCounterProvider();
            if (cp == null)
                return null;

            String[] csl = this.getCounterDefSetList();
            if (!csl[0].equals(counterDefSetName))
                return null;

            svs = new SourceValueSetImpl(cp.getSessionId());

            CounterDefSet cds = getCounterDefSet(counterDefSetName);
            for (CounterDef cd : cds.getCounterDefs()) {
                SourceValueCounterImpl scs = new SourceValueCounterImpl(cd);

                SourceValueObjectImpl svo = null;
                if (cd.getCounterName().equals("TcUniReceivedCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getTcUniReceivedCount());
                } else if (cd.getCounterName().equals("TcUniSentCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getTcUniSentCount());
                } else if (cd.getCounterName().equals("TcBeginReceivedCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getTcBeginReceivedCount());
                } else if (cd.getCounterName().equals("TcBeginSentCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getTcBeginSentCount());
                } else if (cd.getCounterName().equals("TcContinueReceivedCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getTcContinueReceivedCount());
                } else if (cd.getCounterName().equals("TcContinueSentCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getTcContinueSentCount());
                } else if (cd.getCounterName().equals("TcEndReceivedCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getTcEndReceivedCount());
                } else if (cd.getCounterName().equals("TcEndSentCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getTcEndSentCount());
                } else if (cd.getCounterName().equals("TcPAbortReceivedCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getTcPAbortReceivedCount());
                } else if (cd.getCounterName().equals("TcPAbortSentCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getTcPAbortSentCount());
                } else if (cd.getCounterName().equals("TcUserAbortReceivedCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getTcUserAbortReceivedCount());
                } else if (cd.getCounterName().equals("TcUserAbortSentCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getTcUserAbortSentCount());
                } else if (cd.getCounterName().equals("InvokeReceivedCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getInvokeReceivedCount());
                } else if (cd.getCounterName().equals("InvokeSentCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getInvokeSentCount());
                } else if (cd.getCounterName().equals("ReturnResultReceivedCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getReturnResultReceivedCount());
                } else if (cd.getCounterName().equals("ReturnResultSentCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getReturnResultSentCount());
                } else if (cd.getCounterName().equals("ReturnResultLastReceivedCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getReturnResultLastReceivedCount());
                } else if (cd.getCounterName().equals("ReturnResultLastSentCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getReturnResultLastSentCount());
                } else if (cd.getCounterName().equals("ReturnErrorReceivedCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getReturnErrorReceivedCount());
                } else if (cd.getCounterName().equals("ReturnErrorSentCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getReturnErrorSentCount());
                } else if (cd.getCounterName().equals("RejectReceivedCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getRejectReceivedCount());
                } else if (cd.getCounterName().equals("RejectSentCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getRejectSentCount());
                } else if (cd.getCounterName().equals("DialogTimeoutCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getDialogTimeoutCount());
                } else if (cd.getCounterName().equals("DialogReleaseCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getDialogReleaseCount());
                } else if (cd.getCounterName().equals("AllEstablishedDialogsCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getAllEstablishedDialogsCount());
                } else if (cd.getCounterName().equals("AllLocalEstablishedDialogsCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getAllLocalEstablishedDialogsCount());
                } else if (cd.getCounterName().equals("AllRemoteEstablishedDialogsCount")) {
                    svo = new SourceValueObjectImpl(this.getName(), cp.getAllRemoteEstablishedDialogsCount());

                } else if (cd.getCounterName().equals("MinDialogsCount")) {
                    Long res = cp.getMinDialogsCount(campaignName);
                    if (res != null)
                        svo = new SourceValueObjectImpl(this.getName(), res);
                } else if (cd.getCounterName().equals("MaxDialogsCount")) {
                    Long res = cp.getMaxDialogsCount(campaignName);
                    if (res != null)
                        svo = new SourceValueObjectImpl(this.getName(), res);
                } else if (cd.getCounterName().equals("AllDialogsDuration")) {
                    long res = cp.getAllDialogsDuration();
                    svo = new SourceValueObjectImpl(this.getName(), 0);
                    svo.setValueA((double) res / 1000);
                } else if (cd.getCounterName().equals("AverageDialogsDuration")) {
                    long dur = cp.getAllDialogsDuration();
                    long cnt = cp.getDialogReleaseCount();
                    svo = new SourceValueObjectImpl(this.getName(), 0);
                    svo.setValueA((double) dur / 1000);
                    svo.setValueB(cnt);

                } else if (cd.getCounterName().equals("OutgoingDialogsPerApplicatioContextName")) {
                    svo = createComplexValue(cp.getOutgoingDialogsPerApplicatioContextName(campaignName));
                } else if (cd.getCounterName().equals("IncomingDialogsPerApplicatioContextName")) {
                    svo = createComplexValue(cp.getIncomingDialogsPerApplicatioContextName(campaignName));
                } else if (cd.getCounterName().equals("OutgoingInvokesPerOperationCode")) {
                    svo = createComplexValue(cp.getOutgoingInvokesPerOperationCode(campaignName));
                } else if (cd.getCounterName().equals("IncomingInvokesPerOperationCode")) {
                    svo = createComplexValue(cp.getIncomingInvokesPerOperationCode(campaignName));
                } else if (cd.getCounterName().equals("OutgoingErrorsPerErrorCode")) {
                    svo = createComplexValue(cp.getOutgoingErrorsPerErrorCode(campaignName));
                } else if (cd.getCounterName().equals("IncomingErrorsPerErrorCode")) {
                    svo = createComplexValue(cp.getIncomingErrorsPerErrorCode(campaignName));
                } else if (cd.getCounterName().equals("OutgoingRejectPerProblem")) {
                    svo = createComplexValue(cp.getOutgoingRejectPerProblem(campaignName));
                } else if (cd.getCounterName().equals("IncomingRejectPerProblem")) {
                    svo = createComplexValue(cp.getIncomingRejectPerProblem(campaignName));
                }
                if (svo != null)
                    scs.addObject(svo);

                svs.addCounter(scs);
            }
        } catch (Throwable e) {
            logger.info("Exception when getSourceValueSet() - campaignName=" + campaignName + " - " + e.getMessage(), e);
            return null;
        }

        if (durationInSeconds >= 60)
            logger.info("getSourceValueSet() - return value - campaignName=" + campaignName);
        else
            logger.debug("getSourceValueSet() - return value - campaignName=" + campaignName);

        return svs;
    }

    private SourceValueObjectImpl createComplexValue(Map<String, LongValue> vv) {
        SourceValueObjectImpl svo = null;
        if (vv != null) {
            svo = new SourceValueObjectImpl(this.getName(), 0);
            ComplexValue[] vvv = new ComplexValue[vv.size()];
            int i1 = 0;
            for (String s : vv.keySet()) {
                LongValue lv = vv.get(s);
                vvv[i1++] = new ComplexValueImpl(s, lv.getValue());
            }
            svo.setComplexValue(vvv);
        }
        return svo;
    }

    public enum TcapManagementType implements MBeanType {
        MANAGEMENT("Management");

        private final String name;

        public static final String NAME_MANAGEMENT = "Management";

        private TcapManagementType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static TcapManagementType getInstance(String name) {
            if (NAME_MANAGEMENT.equals(name)) {
                return MANAGEMENT;
            }

            return null;
        }
    }

}
