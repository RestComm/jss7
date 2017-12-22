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

package org.mobicents.protocols.ss7.tcap;

import static org.testng.Assert.*;

import java.util.Map;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.statistics.api.LongValue;
import org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcap.api.TCAPCounterProvider;
import org.mobicents.protocols.ss7.tcap.api.TCListener;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCNoticeIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Statistic
 *
 * @author sergey vetyutnev
 *
 */
public class TCAPStatTest extends SccpHarness {
    public static final long WAIT_TIME = 500;
    private static final int _WAIT_TIMEOUT = 90000;
    private static final int _WAIT_REMOVE = 30000;
    private static final int _DIALOG_TIMEOUT = 5000;
    public static final long[] _ACN_ = new long[] { 0, 4, 0, 0, 1, 0, 19, 2 };
    private TCAPStackImpl tcapStack1;
    private TCAPStackImpl tcapStack2;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;
    private Client client;
    private Server server;
    private TCAPListenerWrapper tcapListenerWrapper;

    public TCAPStatTest() {
    }

    @BeforeClass
    public void setUpClass() {
        this.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
        this.sccpStack2Name = "TCAPFunctionalTestSccpStack2";
        System.out.println("setUpClass");
    }

    @AfterClass
    public void tearDownClass() throws Exception {
        System.out.println("tearDownClass");
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @BeforeMethod
    public void setUp() throws Exception {
        System.out.println("setUp");
        super.setUp();

        peer1Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
        peer2Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

        this.tcapStack1 = new TCAPStackImpl("TCAPStatTest1", this.sccpProvider1, 8);
        this.tcapStack2 = new TCAPStackImpl("TCAPStatTest2", this.sccpProvider2, 8);

        this.tcapListenerWrapper = new TCAPListenerWrapper();
        this.tcapStack1.getProvider().addTCListener(tcapListenerWrapper);

        this.tcapStack1.start();
        this.tcapStack2.start();

        this.tcapStack1.setDoNotSendProtocolVersion(false);
        this.tcapStack2.setDoNotSendProtocolVersion(false);
        this.tcapStack1.setInvokeTimeout(0);
        this.tcapStack2.setInvokeTimeout(0);
        this.tcapStack1.setStatisticsEnabled(true);
        this.tcapStack2.setStatisticsEnabled(true);
        // create test classes
        this.client = new Client(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address);
        this.server = new Server(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address);

    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @AfterMethod
    public void tearDown() {
        this.tcapStack1.stop();
        this.tcapStack2.stop();
        super.tearDown();

    }

    @Test(groups = { "functional.flow" })
    public void simpleTCWithDialogTest() throws Exception {

        TCAPCounterProviderChecker check1 = new TCAPCounterProviderChecker();
        TCAPCounterProviderChecker check2 = new TCAPCounterProviderChecker();

        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

        client.startClientDialog();

        check1.AllLocalEstablishedDialogsCount++;
        check1.AllEstablishedDialogsCount++;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

        client.sendBegin();
        client.waitFor(WAIT_TIME);

        check2.AllRemoteEstablishedDialogsCount++;
        check2.AllEstablishedDialogsCount++;
        check1.TcBeginSentCount++;
        check2.TcBeginReceivedCount++;
        check1.InvokeSentCount += 2;
        check2.InvokeReceivedCount += 2;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

        server.sendContinue();
        client.waitFor(WAIT_TIME);

        check2.TcContinueSentCount++;
        check1.TcContinueReceivedCount++;
        check2.InvokeSentCount += 1;
        check1.InvokeReceivedCount += 1;
        check2.ReturnResultLastSentCount += 1;
        check1.ReturnResultLastReceivedCount += 1;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

        server.sendContinue();
        client.waitFor(WAIT_TIME);

        check2.TcContinueSentCount++;
        check1.TcContinueReceivedCount++;
        check2.InvokeSentCount += 1;
        check2.ReturnResultLastSentCount += 1;
        check1.InvokeReceivedCount += 1;
        check1.RejectSentCount += 1;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

        client.sendEnd(TerminationType.Basic);
        client.waitFor(WAIT_TIME);

        check1.TcEndSentCount++;
        check2.TcEndReceivedCount++;
        check2.RejectReceivedCount += 1;
        check1.DialogReleaseCount++;
        check2.DialogReleaseCount++;
        check1.AllDialogsDuration = 100000;
        check2.AllDialogsDuration = 100000;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

    }

    @Test(groups = { "functional.flow" })
    public void uniMsgTest() throws Exception {

        TCAPCounterProviderChecker check1 = new TCAPCounterProviderChecker();
        TCAPCounterProviderChecker check2 = new TCAPCounterProviderChecker();

        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

        client.startUniDialog();
        client.sendUni();
        client.waitFor(WAIT_TIME);

        check1.TcUniSentCount++;
        check2.TcUniReceivedCount++;
        check1.InvokeSentCount += 1;
        check2.InvokeReceivedCount += 1;

        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());
    }

    /**
     * Case when receiving a dialog the dialog count exceeds the MaxDialogs count we setMaxDialogs for Server ==1 TC-BEGIN
     * TC-BEGIN TC-ABORT + PAbortCauseType.ResourceLimitation
     */
    @Test(groups = { "functional.flow" })
    public void dialogCountExceedTest() throws Exception {

        this.tcapStack1.setDialogIdleTimeout(_DIALOG_TIMEOUT);
        this.tcapStack2.setDialogIdleTimeout(_DIALOG_TIMEOUT);

        TCAPCounterProviderChecker check1 = new TCAPCounterProviderChecker();
        TCAPCounterProviderChecker check2 = new TCAPCounterProviderChecker();

        this.tcapStack2.setMaxDialogs(1);
        client.startClientDialog();

        check1.AllLocalEstablishedDialogsCount++;
        check1.AllEstablishedDialogsCount++;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

        client.sendBegin();
        Thread.sleep(WAIT_TIME);

        check2.AllRemoteEstablishedDialogsCount++;
        check2.AllEstablishedDialogsCount++;
        check1.TcBeginSentCount++;
        check2.TcBeginReceivedCount++;
        check1.InvokeSentCount += 2;
        check2.InvokeReceivedCount += 2;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

        client.releaseDialog();
        Thread.sleep(WAIT_TIME);

        check1.DialogReleaseCount += 1;
        check1.AllDialogsDuration = 100000;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());
        
        client.startClientDialog();

        check1.AllLocalEstablishedDialogsCount++;
        check1.AllEstablishedDialogsCount++;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

        client.sendBegin();
        Thread.sleep(WAIT_TIME);

        check1.TcBeginSentCount++;
        check1.InvokeSentCount += 2;
        check2.TcPAbortSentCount += 1;
        check1.TcPAbortReceivedCount += 1;
        check1.DialogReleaseCount += 1;

        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

//        long i1 = ((TCAPProviderImpl)this.tcapStack1.getProvider()).getCurrentDialogsCount();
//        long i2 = ((TCAPProviderImpl)this.tcapStack2.getProvider()).getCurrentDialogsCount();
        
        Thread.sleep(_DIALOG_TIMEOUT);

//        long i11 = ((TCAPProviderImpl)this.tcapStack1.getProvider()).getCurrentDialogsCount();
//        long i22 = ((TCAPProviderImpl)this.tcapStack2.getProvider()).getCurrentDialogsCount();

        check2.DialogReleaseCount += 1;
        check2.DialogTimeoutCount += 1;
        check2.TcPAbortSentCount += 1;
        check2.TcPAbortReceivedCount += 1;
        check2.AllDialogsDuration = 100000;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

        this.tcapStack2.setMaxDialogs(1000);
    }

    @Test(groups = { "functional.flow" })
    public void variousTest() throws Exception {

        TCAPCounterProviderChecker check1 = new TCAPCounterProviderChecker();
        TCAPCounterProviderChecker check2 = new TCAPCounterProviderChecker();

        setupCounters("a1");
        setupCounters("a2");
        setupCounters("a3");
        setupCounters("a4");
        setupCounters("a5");
        setupCounters("a6");

        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());
        check1.check(this.tcapStack1.getCounterProvider(), "a1");
        check2.check(this.tcapStack2.getCounterProvider(), "a1");

        // creating dialog1 ------------------
        client.startClientDialog();

        check1.AllLocalEstablishedDialogsCount++;
        check1.AllEstablishedDialogsCount++;
        check1.MaxDialogsCount = 1;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());
        check1.check(this.tcapStack1.getCounterProvider(), "a2");
        check2.check(this.tcapStack2.getCounterProvider(), "a2");

        // sending TC-BEGIN + invoke(12) + invoke(13)
        ComponentPrimitiveFactory cpFactory = this.tcapStack1.getProvider().getComponentPrimitiveFactory();

        Invoke invoke = cpFactory.createTCInvokeRequest(InvokeClass.Class1);
        invoke.setInvokeId(client.dialog.getNewInvokeId());
        OperationCode oc = cpFactory.createOperationCode();
        oc.setLocalOperationCode(new Long(12));
        invoke.setOperationCode(oc);
        client.dialog.sendComponent(invoke);

        invoke = cpFactory.createTCInvokeRequest(InvokeClass.Class1);
        invoke.setInvokeId(client.dialog.getNewInvokeId());
        oc = cpFactory.createOperationCode();
        oc.setLocalOperationCode(new Long(13));
        invoke.setOperationCode(oc);
        client.dialog.sendComponent(invoke);

        client.sendBegin2();
        client.waitFor(WAIT_TIME);

        check1.TcBeginSentCount++;
        check1.InvokeSentCount += 2;
        LongValue v = new LongValue();
        v.updateValue();
        check1.OutgoingDialogsPerApplicatioContextName.put("[0, 4, 0, 0, 1, 0, 19, 2]", v);
        v = new LongValue();
        v.updateValue();
        check2.IncomingDialogsPerApplicatioContextName.put("[0, 4, 0, 0, 1, 0, 19, 2]", v);
        v = new LongValue();
        v.updateValue();
        check1.OutgoingInvokesPerOperationCode.put("12", v);
        v = new LongValue();
        v.updateValue();
        check1.OutgoingInvokesPerOperationCode.put("13", v);
        v = new LongValue();
        v.updateValue();
        check2.IncomingInvokesPerOperationCode.put("12", v);
        v = new LongValue();
        v.updateValue();
        check2.IncomingInvokesPerOperationCode.put("13", v);

        check2.AllRemoteEstablishedDialogsCount++;
        check2.AllEstablishedDialogsCount++;
        check2.TcBeginReceivedCount++;
        check2.InvokeReceivedCount += 2;
        check2.MaxDialogsCount = 1;

        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());
        check1.check(this.tcapStack1.getCounterProvider(), "a3");
        check2.check(this.tcapStack2.getCounterProvider(), "a3");


        // creating dialog2
        client.dialog = null;
        client.startClientDialog();

        check1.AllLocalEstablishedDialogsCount++;
        check1.AllEstablishedDialogsCount++;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

        // sending TC-BEGIN + invoke(12) + invoke(14)
        invoke = cpFactory.createTCInvokeRequest(InvokeClass.Class1);
        invoke.setInvokeId(client.dialog.getNewInvokeId());
        oc = cpFactory.createOperationCode();
        oc.setLocalOperationCode(new Long(12));
        invoke.setOperationCode(oc);
        client.dialog.sendComponent(invoke);
        long invokeId1 = invoke.getInvokeId();

        invoke = cpFactory.createTCInvokeRequest(InvokeClass.Class1);
        invoke.setInvokeId(client.dialog.getNewInvokeId());
        oc = cpFactory.createOperationCode();
        oc.setLocalOperationCode(new Long(14));
        invoke.setOperationCode(oc);
        client.dialog.sendComponent(invoke);
        long invokeId2 = invoke.getInvokeId();

        client.sendBegin2();
        client.waitFor(WAIT_TIME);

        check1.TcBeginSentCount++;
        check1.InvokeSentCount += 2;
        v = check1.OutgoingDialogsPerApplicatioContextName.get("[0, 4, 0, 0, 1, 0, 19, 2]");
        v.updateValue();
        v = check2.IncomingDialogsPerApplicatioContextName.get("[0, 4, 0, 0, 1, 0, 19, 2]");
        v.updateValue();
        v = check1.OutgoingInvokesPerOperationCode.get("12");
        v.updateValue();
        v = new LongValue();
        v.updateValue();
        check1.OutgoingInvokesPerOperationCode.put("14", v);
        v = check2.IncomingInvokesPerOperationCode.get("12");
        v.updateValue();
        v = new LongValue();
        v.updateValue();
        check2.IncomingInvokesPerOperationCode.put("14", v);

        check2.AllRemoteEstablishedDialogsCount++;
        check2.AllEstablishedDialogsCount++;
        check2.TcBeginReceivedCount++;
        check2.InvokeReceivedCount += 2;
        check1.MaxDialogsCount = 2;
        check2.MaxDialogsCount = 2;

        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());
        check1.check(this.tcapStack1.getCounterProvider(), "a4");
        check2.check(this.tcapStack2.getCounterProvider(), "a4");


        // sending error & reject server->client in dialog2

        // sending TC-CONTINUE + returResult + errorCode(8) + reject(DuplicateInvokeID)
        ReturnResult rr = cpFactory.createTCResultRequest();
        rr.setInvokeId(invokeId1);
        server.dialog.sendComponent(rr);

        ReturnError re = cpFactory.createTCReturnErrorRequest();
        re.setInvokeId(invokeId1);
        ErrorCode ec = cpFactory.createErrorCode();
        ec.setLocalErrorCode(8L);
        re.setErrorCode(ec);
        server.dialog.sendComponent(re);

        Reject rej = cpFactory.createTCRejectRequest();
        rej.setInvokeId(invokeId2);
        Problem p = cpFactory.createProblem(ProblemType.Invoke);
        p.setInvokeProblemType(InvokeProblemType.DuplicateInvokeID);
        rej.setProblem(p);
        server.dialog.sendComponent(rej);

        server.sendContinue2();
        server.waitFor(WAIT_TIME);

        check2.TcContinueSentCount++;
        check2.ReturnResultSentCount += 1;
        check2.ReturnErrorSentCount += 1;
        check2.RejectSentCount += 1;
        v = new LongValue();
        v.updateValue();
        check2.OutgoingErrorsPerErrorCode.put("8", v);
        v = new LongValue();
        v.updateValue();
        check2.OutgoingRejectPerProblem.put("invokeProblemType=DuplicateInvokeID", v);

        check1.TcContinueReceivedCount++;
        check1.ReturnResultReceivedCount += 1;
        check1.ReturnErrorReceivedCount += 1;
        check1.RejectReceivedCount += 1;
        v = new LongValue();
        v.updateValue();
        check1.IncomingErrorsPerErrorCode.put("8", v);
        v = new LongValue();
        v.updateValue();
        check1.IncomingRejectPerProblem.put("invokeProblemType=DuplicateInvokeID", v);

        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());
        check1.check(this.tcapStack1.getCounterProvider(), "a5");
        check2.check(this.tcapStack2.getCounterProvider(), "a5");

        UserInformation userInformation = TcapFactory.createUserInformation();
        userInformation.setOid(true);
        userInformation.setOidValue(new long[] { 1, 2, 3 });
        userInformation.setAsn(true);
        userInformation.setEncodeType(new byte[] { 11, 22, 33 });
        client.sendAbort(null, userInformation, null);
        client.waitFor(WAIT_TIME);

        check1.TcUserAbortSentCount += 1;
        check2.TcUserAbortReceivedCount += 1;
        check1.DialogReleaseCount += 1;
        check2.DialogReleaseCount += 1;
        check1.AllDialogsDuration = 100000;
        check2.AllDialogsDuration = 100000;

        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());
        check1.check(this.tcapStack1.getCounterProvider(), "a6");
        check2.check(this.tcapStack2.getCounterProvider(), "a6");

    }

    private void setupCounters(String camp) {
        doSetupCounters(camp, this.tcapStack1.getCounterProvider());
        doSetupCounters(camp, this.tcapStack2.getCounterProvider());
    }

    private void doSetupCounters(String camp, TCAPCounterProvider prov) {
        prov.getIncomingDialogsPerApplicatioContextName(camp);
        prov.getOutgoingDialogsPerApplicatioContextName(camp);
        prov.getIncomingInvokesPerOperationCode(camp);
        prov.getOutgoingInvokesPerOperationCode(camp);

        prov.getOutgoingErrorsPerErrorCode(camp);
        prov.getIncomingErrorsPerErrorCode(camp);
        prov.getOutgoingRejectPerProblem(camp);
        prov.getIncomingRejectPerProblem(camp);

        prov.getMinDialogsCount(camp);
        prov.getMaxDialogsCount(camp);
    }

    class TCAPCounterProviderChecker {
        public long TcUniReceivedCount;
        public long TcUniSentCount;
        public long TcBeginReceivedCount;
        public long TcBeginSentCount;
        public long TcContinueReceivedCount;
        public long TcContinueSentCount;
        public long TcEndReceivedCount;
        public long TcEndSentCount;
        public long TcPAbortReceivedCount;
        public long TcPAbortSentCount;
        public long TcUserAbortReceivedCount;
        public long TcUserAbortSentCount;

        public long InvokeReceivedCount;
        public long InvokeSentCount;
        public long ReturnResultReceivedCount;
        public long ReturnResultSentCount;
        public long ReturnResultLastReceivedCount;
        public long ReturnResultLastSentCount;
        public long ReturnErrorReceivedCount;
        public long ReturnErrorSentCount;
        public long RejectReceivedCount;
        public long RejectSentCount;

        public long DialogTimeoutCount;
        public long DialogReleaseCount;

        public long AllEstablishedDialogsCount;
        public long AllLocalEstablishedDialogsCount;
        public long AllRemoteEstablishedDialogsCount;

        public long AllDialogsDuration;
        public long MinDialogsCount;
        public long MaxDialogsCount;

        public FastMap<String, LongValue> IncomingDialogsPerApplicatioContextName = new FastMap<String, LongValue>();
        public FastMap<String, LongValue> OutgoingDialogsPerApplicatioContextName = new FastMap<String, LongValue>();
        public FastMap<String, LongValue> IncomingInvokesPerOperationCode = new FastMap<String, LongValue>();
        public FastMap<String, LongValue> OutgoingInvokesPerOperationCode = new FastMap<String, LongValue>();
        public FastMap<String, LongValue> OutgoingErrorsPerErrorCode = new FastMap<String, LongValue>();
        public FastMap<String, LongValue> IncomingErrorsPerErrorCode = new FastMap<String, LongValue>();
        public FastMap<String, LongValue> OutgoingRejectPerProblem = new FastMap<String, LongValue>();
        public FastMap<String, LongValue> IncomingRejectPerProblem = new FastMap<String, LongValue>();

        public void check(TCAPCounterProvider prov) {
            assertEquals(TcUniReceivedCount, prov.getTcUniReceivedCount());
            assertEquals(TcUniSentCount, prov.getTcUniSentCount());
            assertEquals(TcBeginSentCount, prov.getTcBeginSentCount());
            assertEquals(TcBeginReceivedCount, prov.getTcBeginReceivedCount());
            assertEquals(TcContinueReceivedCount, prov.getTcContinueReceivedCount());
            assertEquals(TcContinueSentCount, prov.getTcContinueSentCount());
            assertEquals(TcEndReceivedCount, prov.getTcEndReceivedCount());
            assertEquals(TcEndSentCount, prov.getTcEndSentCount());
            assertEquals(TcPAbortReceivedCount, prov.getTcPAbortReceivedCount());
            assertEquals(TcPAbortSentCount, prov.getTcPAbortSentCount());
            assertEquals(TcUserAbortReceivedCount, prov.getTcUserAbortReceivedCount());
            assertEquals(TcUserAbortSentCount, prov.getTcUserAbortSentCount());

            assertEquals(InvokeReceivedCount, prov.getInvokeReceivedCount());
            assertEquals(InvokeSentCount, prov.getInvokeSentCount());
            assertEquals(ReturnResultReceivedCount, prov.getReturnResultReceivedCount());
            assertEquals(ReturnResultSentCount, prov.getReturnResultSentCount());
            assertEquals(ReturnResultLastReceivedCount, prov.getReturnResultLastReceivedCount());
            assertEquals(ReturnResultLastSentCount, prov.getReturnResultLastSentCount());
            assertEquals(ReturnErrorReceivedCount, prov.getReturnErrorReceivedCount());
            assertEquals(ReturnErrorSentCount, prov.getReturnErrorSentCount());
            assertEquals(RejectReceivedCount, prov.getRejectReceivedCount());
            assertEquals(RejectSentCount, prov.getRejectSentCount());

            assertEquals(DialogTimeoutCount, prov.getDialogTimeoutCount());
            assertEquals(DialogReleaseCount, prov.getDialogReleaseCount());
            assertEquals(AllEstablishedDialogsCount, prov.getAllEstablishedDialogsCount());
            assertEquals(AllLocalEstablishedDialogsCount, prov.getAllLocalEstablishedDialogsCount());
            assertEquals(AllRemoteEstablishedDialogsCount, prov.getAllRemoteEstablishedDialogsCount());

            if (AllDialogsDuration < prov.getAllDialogsDuration())
                fail();
        }

        private void checkCounter(Map<String, LongValue> orig, FastMap<String, LongValue> check) {
            assertEquals(orig.size(), check.size());
            for (String key : orig.keySet()) {
                LongValue x1 = orig.get(key);
                LongValue x2 = check.get(key);
                assertEquals(x1.getValue(), x2.getValue());
            }
        }

        public void check(TCAPCounterProvider prov, String camp) {
            checkCounter(prov.getIncomingDialogsPerApplicatioContextName(camp), IncomingDialogsPerApplicatioContextName);
            checkCounter(prov.getOutgoingDialogsPerApplicatioContextName(camp), OutgoingDialogsPerApplicatioContextName);
            checkCounter(prov.getIncomingInvokesPerOperationCode(camp), IncomingInvokesPerOperationCode);
            checkCounter(prov.getOutgoingInvokesPerOperationCode(camp), OutgoingInvokesPerOperationCode);
            checkCounter(prov.getOutgoingErrorsPerErrorCode(camp), OutgoingErrorsPerErrorCode);
            checkCounter(prov.getIncomingErrorsPerErrorCode(camp), IncomingErrorsPerErrorCode);
            checkCounter(prov.getOutgoingRejectPerProblem(camp), OutgoingRejectPerProblem);
            checkCounter(prov.getIncomingRejectPerProblem(camp), IncomingRejectPerProblem);

            Long v1 = prov.getMinDialogsCount(camp);
            if (v1 != null) {
                assertEquals((long) v1, MinDialogsCount);
            }
            v1 = prov.getMaxDialogsCount(camp);
            if (v1 != null) {
                assertEquals((long) v1, MaxDialogsCount);
            }
        }
    }

    private class TCAPListenerWrapper implements TCListener {

        @Override
        public void onTCUni(TCUniIndication ind) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTCBegin(TCBeginIndication ind) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTCContinue(TCContinueIndication ind) {
//            assertEquals(ind.getComponents().length, 2);
//            ReturnResultLast rrl = (ReturnResultLast) ind.getComponents()[0];
//            Invoke inv = (Invoke) ind.getComponents()[1];
//
//            // operationCode is not sent via ReturnResultLast because it does not contain a Parameter
//            // so operationCode is taken from a sent Invoke
//            assertEquals((long) rrl.getInvokeId(), 1);
//            assertEquals((long) rrl.getOperationCode().getLocalOperationCode(), 12);
//
//            // second Invoke has its own operationCode and it has linkedId to the second sent Invoke
//            assertEquals((long) inv.getInvokeId(), 1);
//            assertEquals((long) inv.getOperationCode().getLocalOperationCode(), 14);
//            assertEquals((long) inv.getLinkedId(), 2);
//
//            // we should see operationCode of the second sent Invoke
//            Invoke linkedInv = inv.getLinkedInvoke();
//            assertEquals((long) linkedInv.getOperationCode().getLocalOperationCode(), 13);
        }

        @Override
        public void onTCEnd(TCEndIndication ind) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTCUserAbort(TCUserAbortIndication ind) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTCPAbort(TCPAbortIndication ind) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTCNotice(TCNoticeIndication ind) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onDialogReleased(Dialog d) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onInvokeTimeout(Invoke tcInvokeRequest) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onDialogTimeout(Dialog d) {
            // TODO Auto-generated method stub

        }

    }

}
