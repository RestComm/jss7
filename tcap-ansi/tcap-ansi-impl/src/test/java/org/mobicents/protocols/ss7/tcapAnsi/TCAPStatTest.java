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

package org.mobicents.protocols.ss7.tcapAnsi;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Map;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.statistics.api.LongValue;
import org.mobicents.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPCounterProvider;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCListener;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultNotLast;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCNoticeIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniIndication;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortIndication;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TcapFactory;
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

        peer1Address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 1, null, 8);
        peer2Address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 2, null, 8);

        this.tcapStack1 = new TCAPStackImpl("Test", this.sccpProvider1, 8);
        this.tcapStack2 = new TCAPStackImpl("Test", this.sccpProvider2, 8);

        this.tcapListenerWrapper = new TCAPListenerWrapper();
        this.tcapStack1.getProvider().addTCListener(tcapListenerWrapper);

        this.tcapStack1.setInvokeTimeout(0);
        this.tcapStack2.setInvokeTimeout(0);

        this.tcapStack1.start();
        this.tcapStack2.start();
        this.tcapStack1.setStatisticsEnabled(true);
        this.tcapStack2.setStatisticsEnabled(true);
        // create test classes
        this.client = new Client(this.tcapStack1, peer1Address, peer2Address);
        this.server = new Server(this.tcapStack2, peer2Address, peer1Address);

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
        check1.InvokeNotLastSentCount += 1;
        check2.InvokeNotLastReceivedCount += 1;
        check1.InvokeLastSentCount += 1;
        check2.InvokeLastReceivedCount += 1;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

        server.sendContinue(false);
        client.waitFor(WAIT_TIME);

        check2.TcContinueSentCount++;
        check1.TcContinueReceivedCount++;
        check2.InvokeNotLastSentCount += 1;
        check1.InvokeNotLastReceivedCount += 1;
        check2.ReturnResultLastSentCount += 1;
        check1.ReturnResultLastReceivedCount += 1;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

        server.sendContinue(false);
        client.waitFor(WAIT_TIME);

        check2.TcContinueSentCount++;
        check1.TcContinueReceivedCount++;
        check2.InvokeNotLastSentCount += 1;
        check2.ReturnResultLastSentCount += 1;
        check1.InvokeNotLastReceivedCount += 1;
        check1.RejectSentCount += 1;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());

        client.sendEnd(false);
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
        check1.InvokeNotLastSentCount += 1;
        check2.InvokeNotLastReceivedCount += 1;

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
        check1.InvokeNotLastSentCount += 1;
        check1.InvokeLastSentCount += 1;
        check2.InvokeNotLastReceivedCount += 1;
        check2.InvokeLastReceivedCount += 1;
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
        check1.InvokeNotLastSentCount += 1;
        check1.InvokeLastSentCount += 1;
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
        check2.AllDialogsDuration = 100000;
        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());
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

        Invoke invoke = cpFactory.createTCInvokeRequestNotLast();
        invoke.setInvokeId(client.dialog.getNewInvokeId());
        OperationCode oc = cpFactory.createOperationCode();
        oc.setPrivateOperationCode(new Long(12));
        invoke.setOperationCode(oc);
        client.dialog.sendComponent(invoke);

        invoke = cpFactory.createTCInvokeRequestNotLast(InvokeClass.Class1);
        invoke.setInvokeId(client.dialog.getNewInvokeId());
        oc = cpFactory.createOperationCode();
        oc.setPrivateOperationCode(new Long(13));
        invoke.setOperationCode(oc);
        client.dialog.sendComponent(invoke);

        client.sendBegin2();
        client.waitFor(WAIT_TIME);

        check1.TcBeginSentCount++;
        check1.InvokeNotLastSentCount += 2;
        LongValue v = new LongValue();
        v.updateValue();
        check1.OutgoingDialogsPerApplicatioContextName.put("[0, 4, 0, 0, 1, 0, 19, 2]", v);
        v = new LongValue();
        v.updateValue();
        check2.IncomingDialogsPerApplicatioContextName.put("[0, 4, 0, 0, 1, 0, 19, 2]", v);
        v = new LongValue();
        v.updateValue();
        check1.OutgoingInvokesPerOperationCode.put("POC=12", v);
        v = new LongValue();
        v.updateValue();
        check1.OutgoingInvokesPerOperationCode.put("POC=13", v);
        v = new LongValue();
        v.updateValue();
        check2.IncomingInvokesPerOperationCode.put("POC=12", v);
        v = new LongValue();
        v.updateValue();
        check2.IncomingInvokesPerOperationCode.put("POC=13", v);

        check2.AllRemoteEstablishedDialogsCount++;
        check2.AllEstablishedDialogsCount++;
        check2.TcBeginReceivedCount++;
        check2.InvokeNotLastReceivedCount += 2;
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
        invoke = cpFactory.createTCInvokeRequestNotLast();
        invoke.setInvokeId(client.dialog.getNewInvokeId());
        oc = cpFactory.createOperationCode();
        oc.setPrivateOperationCode(new Long(12));
        invoke.setOperationCode(oc);
        client.dialog.sendComponent(invoke);
        long invokeId1 = invoke.getInvokeId();

        invoke = cpFactory.createTCInvokeRequestNotLast(InvokeClass.Class1);
        invoke.setInvokeId(client.dialog.getNewInvokeId());
        oc = cpFactory.createOperationCode();
        oc.setPrivateOperationCode(new Long(14));
        invoke.setOperationCode(oc);
        client.dialog.sendComponent(invoke);
        long invokeId2 = invoke.getInvokeId();

        client.sendBegin2();
        client.waitFor(WAIT_TIME);

        check1.TcBeginSentCount++;
        check1.InvokeNotLastSentCount += 2;
        v = check1.OutgoingDialogsPerApplicatioContextName.get("[0, 4, 0, 0, 1, 0, 19, 2]");
        v.updateValue();
        v = check2.IncomingDialogsPerApplicatioContextName.get("[0, 4, 0, 0, 1, 0, 19, 2]");
        v.updateValue();
        v = check1.OutgoingInvokesPerOperationCode.get("POC=12");
        v.updateValue();
        v = new LongValue();
        v.updateValue();
        check1.OutgoingInvokesPerOperationCode.put("POC=14", v);
        v = check2.IncomingInvokesPerOperationCode.get("POC=12");
        v.updateValue();
        v = new LongValue();
        v.updateValue();
        check2.IncomingInvokesPerOperationCode.put("POC=14", v);

        check2.AllRemoteEstablishedDialogsCount++;
        check2.AllEstablishedDialogsCount++;
        check2.TcBeginReceivedCount++;
        check2.InvokeNotLastReceivedCount += 2;
        check1.MaxDialogsCount = 2;
        check2.MaxDialogsCount = 2;

        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());
        check1.check(this.tcapStack1.getCounterProvider(), "a4");
        check2.check(this.tcapStack2.getCounterProvider(), "a4");


        // sending error & reject server->client in dialog2

        // sending TC-CONTINUE + returnResultNotLast + errorCode(8) + reject(DuplicateInvokeID)
        ReturnResultNotLast rr = cpFactory.createTCResultNotLastRequest();
        rr.setCorrelationId(invokeId1);
//        rr.setOperationCode(i);
        server.dialog.sendComponent(rr);

        ReturnError re = cpFactory.createTCReturnErrorRequest();
        re.setCorrelationId(invokeId1);
        ErrorCode ec = cpFactory.createErrorCode();
        ec.setNationalErrorCode(8L);
        re.setErrorCode(ec);
        server.dialog.sendComponent(re);

        Reject rej = cpFactory.createTCRejectRequest();
        rej.setProblem(RejectProblem.invokeDuplicateInvocation);
        server.dialog.sendComponent(rej);

        server.sendContinue2();
        server.waitFor(WAIT_TIME);

        check2.TcContinueSentCount++;
        check2.ReturnErrorSentCount += 1;
        check2.ReturnResultNotLastSentCount += 1;
        check2.RejectSentCount += 1;
        v = new LongValue();
        v.updateValue();
        check2.OutgoingErrorsPerErrorCode.put("NEC=8", v);
        v = new LongValue();
        v.updateValue();
        check2.OutgoingRejectPerProblem.put("invokeDuplicateInvocation", v);

        check1.TcContinueReceivedCount++;
        check1.ReturnResultNotLastReceivedCount += 1;
        check1.ReturnErrorReceivedCount += 1;
        check1.RejectReceivedCount += 1;
        v = new LongValue();
        v.updateValue();
        check1.IncomingErrorsPerErrorCode.put("NEC=8", v);
        v = new LongValue();
        v.updateValue();
        check1.IncomingRejectPerProblem.put("invokeDuplicateInvocation", v);

        check1.check(this.tcapStack1.getCounterProvider());
        check2.check(this.tcapStack2.getCounterProvider());
        check1.check(this.tcapStack1.getCounterProvider(), "a5");
        check2.check(this.tcapStack2.getCounterProvider(), "a5");

        UserInformationElement userInformationElement = TcapFactory.createUserInformationElement();
        userInformationElement.setOid(true);
        userInformationElement.setOidValue(new long[] { 1, 2, 3 });
        userInformationElement.setAsn(true);
        userInformationElement.setEncodeType(new byte[] { 11, 22, 33 });
        client.sendAbort(null, userInformationElement);
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

        public long InvokeNotLastReceivedCount;
        public long InvokeNotLastSentCount;
        public long InvokeLastReceivedCount;
        public long InvokeLastSentCount;
        public long ReturnResultNotLastReceivedCount;
        public long ReturnResultNotLastSentCount;
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
            assertEquals(TcBeginSentCount, prov.getTcQuerySentCount());
            assertEquals(TcBeginReceivedCount, prov.getTcQueryReceivedCount());
            assertEquals(TcContinueReceivedCount, prov.getTcConversationReceivedCount());
            assertEquals(TcContinueSentCount, prov.getTcConversationSentCount());
            assertEquals(TcEndReceivedCount, prov.getTcResponseReceivedCount());
            assertEquals(TcEndSentCount, prov.getTcResponseSentCount());
            assertEquals(TcPAbortReceivedCount, prov.getTcPAbortReceivedCount());
            assertEquals(TcPAbortSentCount, prov.getTcPAbortSentCount());
            assertEquals(TcUserAbortReceivedCount, prov.getTcUserAbortReceivedCount());
            assertEquals(TcUserAbortSentCount, prov.getTcUserAbortSentCount());

            assertEquals(InvokeNotLastReceivedCount, prov.getInvokeNotLastReceivedCount());
            assertEquals(InvokeNotLastSentCount, prov.getInvokeNotLastSentCount());
            assertEquals(InvokeLastReceivedCount, prov.getInvokeLastReceivedCount());
            assertEquals(InvokeLastSentCount, prov.getInvokeLastSentCount());
            assertEquals(ReturnResultNotLastReceivedCount, prov.getReturnResultNotLastReceivedCount());
            assertEquals(ReturnResultNotLastSentCount, prov.getReturnResultNotLastSentCount());
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
        public void onTCQuery(TCQueryIndication ind) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTCConversation(TCConversationIndication ind) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onTCResponse(TCResponseIndication ind) {
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
