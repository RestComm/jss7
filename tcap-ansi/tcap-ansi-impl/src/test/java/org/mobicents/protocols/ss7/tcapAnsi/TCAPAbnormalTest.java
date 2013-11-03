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

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcapAnsi.DialogImpl;
import org.mobicents.protocols.ss7.tcapAnsi.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.TRPseudoState;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcapAnsi.asn.UserInformationElementImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test for abnormal situation processing
 *
 * @author sergey vetyutnev
 *
 */
public class TCAPAbnormalTest extends SccpHarness {

    public static final long WAIT_TIME = 500;
    public static final long INVOKE_WAIT_TIME = 500;
    private static final int _DIALOG_TIMEOUT = 5000;

    private TCAPStackImpl tcapStack1;
    private TCAPStackImpl tcapStack2;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;
    private Client client;
    private Server server;

    public TCAPAbnormalTest() {

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

        this.tcapStack1.setInvokeTimeout(0);
        this.tcapStack2.setInvokeTimeout(0);
        this.tcapStack1.setDialogIdleTimeout(_DIALOG_TIMEOUT);
        this.tcapStack2.setDialogIdleTimeout(_DIALOG_TIMEOUT);

        this.tcapStack1.start();
        this.tcapStack2.start();
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

    /**
     * A case of receiving TC-Begin + AARQ apdu + unsupported protocol version (supported only V2)
     * TC-BEGIN (unsupported protocol version)
     *   TC-ABORT + PAbortCauseType.NoCommonDialogPortion
     */
    @Test(groups = { "functional.flow" })
    public void badDialogProtocolVersionTest() throws Exception {

        // TODO:
        // we do not test this now because incorrect protocolVersion is not processed  

//        long stamp = System.currentTimeMillis();
//        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
//        TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, null, 0, stamp);
//        clientExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
//        clientExpectedEvents.add(te);
//
//        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
//
//        client.startClientDialog();
//        SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address, peer1Address,
//                getMessageWithUnsupportedProtocolVersion(), 0, 0, false, null, null);
//        this.sccpProvider1.send(message);
//        client.waitFor(WAIT_TIME);
//
//        client.compareEvents(clientExpectedEvents);
//        server.compareEvents(serverExpectedEvents);
//
//        assertEquals(client.pAbortCauseType, PAbortCause.NoCommonDialoguePortion);
    }

    /**
     * Case when receiving a dialog the dialog count exceeds the MaxDialogs count we setMaxDialogs for Server ==1
     * TC-BEGIN
     *   TC-ABORT + PAbortCauseType.ResourceLimitation
     */
    @Test(groups = { "functional.flow" })
    public void dialogCountExceedTest() throws Exception {

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Begin, null, 2, stamp + WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.PAbort, null, 3, stamp + WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + WAIT_TIME);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);

        this.tcapStack2.setMaxDialogs(1);
        client.startClientDialog();
        client.sendBegin();
        client.releaseDialog();
        Thread.sleep(WAIT_TIME);
        client.startClientDialog();
        client.sendBegin();
        Thread.sleep(WAIT_TIME);
        Thread.sleep(_DIALOG_TIMEOUT);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

        assertEquals(client.pAbortCauseType, PAbortCause.ResourceUnavailable);
    }

    /**
     * Case of receiving TC-Query that has a bad structure TC-Query (bad dialog portion formatted)
     *   TC-ABORT + PAbortCauseType.BadlyStructuredDialoguePortion
     */
    @Test(groups = { "functional.flow" })
    public void badSyntaxMessageTest_PAbort() throws Exception {

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, null, 0, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

        client.startClientDialog();
        SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address, peer1Address,
                getMessageBadSyntax(), 0, 0, false, null, null);
        this.sccpProvider1.send(message);
        client.waitFor(WAIT_TIME);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

        assertEquals(client.pAbortCauseType, PAbortCause.BadlyStructuredDialoguePortion);
    }

    /**
     * Case of receiving TC-Query that has a bad structure TC-Query (no dialog portion + bad component portion)
     *   TC-End + PAbortCauseType.BadlyStructuredDialoguePortion
     */
    @Test(groups = { "functional.flow" })
    public void badSyntaxMessageTest_Reject() throws Exception {

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.End, null, 2, stamp + WAIT_TIME * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME * 2);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 2, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.End, null, 3, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);

        client.startClientDialog();
        client.sendBegin();
        Thread.sleep(WAIT_TIME);

        server.sendContinue(false);
        Thread.sleep(WAIT_TIME);

        assertNull(client.rejectProblem);

        SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address, peer1Address,
                getMessageBadSyntax2(), 0, 0, false, null, null);
        this.sccpProvider1.send(message);
        Thread.sleep(WAIT_TIME);
//        client.waitFor(WAIT_TIME);

        server.sendEnd(false);
        
        client.waitFor(WAIT_TIME);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

        assertEquals(client.rejectProblem, RejectProblem.generalUnrecognisedComponentType);
    }

    @Test(groups = { "functional.flow" })
    /**
     * Case of receiving a reply for TC-Begin the message with a bad TAG
     * TC-BEGIN (bad message Tag - not Begin, Continue, ...)
     *   TC-ABORT + PAbortCauseType.UnrecognizedMessageType
     */
    public void badMessageTagTest() throws Exception {

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 2, stamp + WAIT_TIME + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.PAbort, null, 2, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);

        client.startClientDialog();
        client.sendBegin();
        Thread.sleep(WAIT_TIME);

        server.sendContinue(false);
        Thread.sleep(WAIT_TIME);

        SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer1Address, peer2Address,
                getMessageBadTag(), 0, 0, false, null, null);
        this.sccpProvider2.send(message);
        Thread.sleep(WAIT_TIME + _DIALOG_TIMEOUT);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

        // assertEquals(client.pAbortCauseType, PAbortCauseType.UnrecognizedMessageType);
        assertEquals(server.pAbortCauseType, PAbortCause.UnrecognizedDialoguePortionID.UnrecognizedPackageType);
    }

    @Test(groups = { "functional.flow" })
    /**
     * Case of receiving a message TC-Continue when a local Dialog has been released
     * TC-BEGIN
     *   TC-CONTINUE
     * we are destroying a Dialog at a client side
     *   TC-CONTINUE
     * TC-ABORT + PAbortCauseType.UnrecognizedTxID
     */
    public void noDialogTest() throws Exception {

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + WAIT_TIME * 2);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 2, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.End, null, 3, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);

        client.startClientDialog();
        client.sendBegin();
        Thread.sleep(WAIT_TIME);

        server.sendContinue(false);
        Thread.sleep(WAIT_TIME);

        client.releaseDialog();
        server.sendContinue(false);
        Thread.sleep(WAIT_TIME);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

        assertNull(server.pAbortCauseType);
        assertEquals(server.rejectProblem, RejectProblem.transactionUnassignedRespondingTransID);
    }

    /**
     * Case of receiving a message TC-Continue without AARE apdu at the InitialSent state of a Dialog.
     * This will cause an error
     * TC-BEGIN
     *   TC-CONTINUE we are setting a State of a Client Dialog to TRPseudoState.InitialSent like it has just been sent a
     * TC-BEGIN message 
     *   TC-CONTINUE
     * TC-ABORT + PAbortCauseType.AbnormalDialogue
     */
    @Test(groups = { "functional.flow" })
    public void abnormalDialogTest() throws Exception {

        // TODO:
        // we do not test this now because apdu's are not used in AMSI  

//        long stamp = System.currentTimeMillis();
//        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
//        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
//        clientExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
//        clientExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.PAbort, null, 2, stamp + WAIT_TIME * 2);
//        clientExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME * 2);
//        clientExpectedEvents.add(te);
//
//        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
//        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
//        serverExpectedEvents.add(te);
//        te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
//        serverExpectedEvents.add(te);
//        te = TestEvent.createSentEvent(EventType.Continue, null, 2, stamp + WAIT_TIME * 2);
//        serverExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.PAbort, null, 3, stamp + WAIT_TIME * 2);
//        serverExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + WAIT_TIME * 2);
//        serverExpectedEvents.add(te);
//
//        client.startClientDialog();
//        client.sendBegin();
//        Thread.sleep(WAIT_TIME);
//
//        server.sendContinue();
//        Thread.sleep(WAIT_TIME);
//
//        client.getCurDialog().setState(TRPseudoState.InitialSent);
//        server.sendContinue();
//        Thread.sleep(WAIT_TIME);
//
//        client.compareEvents(clientExpectedEvents);
//        server.compareEvents(serverExpectedEvents);
//
//        assertEquals(client.pAbortCauseType, PAbortCause.AbnormalDialogue);
//        assertEquals(server.pAbortCauseType, PAbortCause.AbnormalDialogue);
    }

    /**
     * TC-U-Abort as a response to TC-Begin
     *
     * TC-BEGIN
     *   TC-ABORT + UserAbort by TCAP user
     */
    @Test(groups = { "functional.flow" })
    public void userAbortTest() throws Exception {

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.UAbort, null, 1, stamp + WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + WAIT_TIME);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.UAbort, null, 1, stamp + WAIT_TIME);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + WAIT_TIME);
        serverExpectedEvents.add(te);

        client.startClientDialog();
        client.sendBegin();
        Thread.sleep(WAIT_TIME);

        UserInformationElement uie = new UserInformationElementImpl();
        uie.setOid(true);
        uie.setOidValue(new long[] { 1, 2, 3 });
        uie.setAsn(true);
        uie.setEncodeType(new byte[] { 11, 22, 33 });
        ApplicationContext ac = TcapFactory.createApplicationContext(new long[] { 1, 2, 3 });
        server.sendAbort(ac, uie);
        Thread.sleep(WAIT_TIME);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    /**
     * Sending a message with unreachable CalledPartyAddress TC-BEGIN
     */
    @Test(groups = { "functional.flow" })
    public void badAddressMessage1Test() throws Exception {

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

        client.startClientDialog();
        client.sendBeginUnreachableAddress(false);
        Thread.sleep(WAIT_TIME);
        Thread.sleep(_DIALOG_TIMEOUT);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    /**
     * Sending a message with unreachable CalledPartyAddress + returnMessageOnError -> TC-Notice
     * TC-BEGIN + returnMessageOnError
     *   TC-NOTICE
     */
    @Test(groups = { "functional.flow" })
    public void badAddressMessage2Test() throws Exception {

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Notice, null, 1, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

        client.startClientDialog();
        client.sendBeginUnreachableAddress(true);
        Thread.sleep(WAIT_TIME);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    /**
     * Invoke timeouts before dialog timeout TC-BEGIN InvokeTimeout DialogTimeout
     */
    @Test(groups = { "functional.flow" })
    public void invokeTimeoutTest1() throws Exception {

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, 1, stamp + INVOKE_WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 2, stamp + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

        client.startClientDialog();

        DialogImpl tcapDialog = client.getCurDialog();
        Invoke invoke = client.createNewInvoke();
        invoke.setTimeout(INVOKE_WAIT_TIME);
        tcapDialog.sendComponent(invoke);

        client.sendBeginUnreachableAddress(false);
        Thread.sleep(WAIT_TIME);
        Thread.sleep(_DIALOG_TIMEOUT);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    /**
     * Invoke timeouts after dialog timeout TC-BEGIN DialogTimeout
     */
    @Test(groups = { "functional.flow" })
    public void invokeTimeoutTest2() throws Exception {

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
        clientExpectedEvents.add(te);

        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + (_DIALOG_TIMEOUT));
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + (_DIALOG_TIMEOUT));
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

        // ....................
//        this.saveTrafficInFile();
//        server.stack.setDialogIdleTimeout(30000);
        // ....................

        client.startClientDialog();

        DialogImpl tcapDialog = client.getCurDialog();
        Invoke invoke = client.createNewInvoke();
        invoke.setTimeout(_DIALOG_TIMEOUT * 2);
        tcapDialog.sendComponent(invoke);

        client.sendBeginUnreachableAddress(false);
        Thread.sleep(WAIT_TIME);
        Thread.sleep(_DIALOG_TIMEOUT * 2);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    public static byte[] getMessageWithUnsupportedProtocolVersion() {
        return new byte[] { (byte) 0xe2, 0x2a, (byte) 0xc7, 0x04, 0x00, 0x00, 0x00, 0x01, (byte) 0xf9, 0x0c, (byte) 0xda, 0x01, 0x04, (byte) 0xdc, 0x07, 0x04,
                0x00, 0x00, 0x01, 0x00, 0x13, 0x02, (byte) 0xe8, 0x14, (byte) 0xed, 0x08, (byte) 0xcf, 0x01, 0x01, (byte) 0xd1, 0x01, 0x0c, (byte) 0xf0, 0x00,
                (byte) 0xe9, 0x08, (byte) 0xcf, 0x01, 0x02, (byte) 0xd1, 0x01, 0x0d, (byte) 0xf0, 0x00 };
    }

    // bad structured dialog portion -> PAbort
    public static byte[] getMessageBadSyntax() {
        return new byte[] { (byte) 0xe2, 0x2a, (byte) 0xc7, 0x04, 0x00, 0x00, 0x00, 0x01, (byte) 0xf9, 0x0c, (byte) 0xda, 0x01, 0x03, (byte) 0xff, 0x07, 0x04,
                0x00, 0x00, 0x01, 0x00, 0x13, 0x02, (byte) 0xe8, 0x14, (byte) 0xed, 0x08, (byte) 0xcf, 0x01, 0x01, (byte) 0xd1, 0x01, 0x0c, (byte) 0xf0, 0x00,
                (byte) 0xe9, 0x08, (byte) 0xcf, 0x01, 0x02, (byte) 0xd1, 0x01, 0x0d, (byte) 0xf0, 0x00 };
    }

    // bad structured component portion -> Reject
    public static byte[] getMessageBadSyntax2() {
        return new byte[] { (byte) 0xe5, 13, (byte) 0xc7, 8, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0 };
    }

    public static byte[] getMessageBadTag() {
        return new byte[] { 106, 13, (byte) 0xc7, 8, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0 };
    }

//    @Test(groups = { "functional.flow" })
//    public void UnrecognizedMessageTypeTest() throws Exception {
//
//        // case of receiving TC-Begin + AARQ apdu + unsupported protocol version (supported only V2)
//        long stamp = System.currentTimeMillis();
//        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
//        TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, null, 0, stamp);
//        clientExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
//        clientExpectedEvents.add(te);
//
//        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
//
//        client.startClientDialog();
//        SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address, peer1Address,
//                getUnrecognizedMessageTypeMessage(), 0, 0, false, null, null);
//        this.sccpProvider1.send(message);
//        client.waitFor(WAIT_TIME);
//
//        client.compareEvents(clientExpectedEvents);
//        server.compareEvents(serverExpectedEvents);
//
//        assertEquals(client.pAbortCauseType, PAbortCause.UnrecognizedMessageType);
//    }
//
//    public static byte[] getUnrecognizedMessageTypeMessage() {
//        return new byte[] { 105, 6, 72, 4, 0, 0, 0, 1 };
//    }
}
