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

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.UserInformation;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
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

        peer1Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
        peer2Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

        this.tcapStack1 = new TCAPStackImpl("TCAPAbnormalTest", this.sccpProvider1, 8);
        this.tcapStack2 = new TCAPStackImpl("TCAPAbnormalTest", this.sccpProvider2, 8);

        this.tcapStack1.start();
        this.tcapStack2.start();

        this.tcapStack1.setInvokeTimeout(0);
        this.tcapStack2.setInvokeTimeout(0);
        this.tcapStack1.setDialogIdleTimeout(_DIALOG_TIMEOUT);
        this.tcapStack2.setDialogIdleTimeout(_DIALOG_TIMEOUT);
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

    /**
     * A case of receiving TC-Begin + AARQ apdu + unsupported protocol version (supported only V2) TC-BEGIN + AARQ apdu
     * (unsupported protocol version) TC-ABORT + PAbortCauseType.NoCommonDialogPortion
     */
    @Test(groups = { "functional.flow" })
    public void badDialogProtocolVersionTest() throws Exception {

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, null, 0, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

        client.startClientDialog();
        SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address, peer1Address,
                getMessageWithUnsupportedProtocolVersion(), 0, 0, false, null, null);
        this.sccpProvider1.send(message);
        client.waitFor(WAIT_TIME);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

        assertEquals(client.pAbortCauseType, PAbortCauseType.NoCommonDialoguePortion);
    }

    /**
     * Case when receiving a dialog the dialog count exceeds the MaxDialogs count we setMaxDialogs for Server ==1 TC-BEGIN
     * TC-BEGIN TC-ABORT + PAbortCauseType.ResourceLimitation
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
        te = TestEvent.createReceivedEvent(EventType.PAbort, null, 2, stamp + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _DIALOG_TIMEOUT);
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

        assertEquals(client.pAbortCauseType, PAbortCauseType.ResourceLimitation);
    }

    /**
     * Case of receiving TC-Begin that has a bad structure TC-BEGIN (bad formatted) TC-ABORT +
     * PAbortCauseType.IncorrectTxPortion
     */
    @Test(groups = { "functional.flow" })
    public void badSyntaxMessageTest() throws Exception {

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

        assertEquals(client.pAbortCauseType, PAbortCauseType.IncorrectTxPortion);
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
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 2, stamp + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.PAbort, null, 3, stamp + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _DIALOG_TIMEOUT);
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

        server.sendContinue();
        Thread.sleep(WAIT_TIME);

        SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer1Address, peer2Address,
                getMessageBadTag(), 0, 0, false, null, null);
        this.sccpProvider2.send(message);
        Thread.sleep(WAIT_TIME + _DIALOG_TIMEOUT);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

        // assertEquals(client.pAbortCauseType, PAbortCauseType.UnrecognizedMessageType);
        assertEquals(server.pAbortCauseType, PAbortCauseType.UnrecognizedMessageType);
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
        te = TestEvent.createReceivedEvent(EventType.PAbort, null, 3, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);

        client.startClientDialog();
        client.sendBegin();
        Thread.sleep(WAIT_TIME);

        server.sendContinue();
        Thread.sleep(WAIT_TIME);

        client.releaseDialog();
        server.sendContinue();
        Thread.sleep(WAIT_TIME);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

        assertEquals(server.pAbortCauseType, PAbortCauseType.UnrecognizedTxID);
    }

    /**
     * Case of receiving a message TC-Continue without AARE apdu at the InitialSent state of a Dialog. This will cause an error
     * TC-BEGIN TC-CONTINUE we are setting a State of a Client Dialog to TRPseudoState.InitialSent like it has just been sent a
     * TC-BEGIN message TC-CONTINUE TC-ABORT + PAbortCauseType.AbnormalDialogue
     */
    @Test(groups = { "functional.flow" })
    public void abnormalDialogTest() throws Exception {

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.PAbort, null, 2, stamp + WAIT_TIME * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME * 2);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 2, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.PAbort, null, 3, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);

        client.startClientDialog();
        client.sendBegin();
        Thread.sleep(WAIT_TIME);

        server.sendContinue();
        Thread.sleep(WAIT_TIME);

        client.getCurDialog().setState(TRPseudoState.InitialSent);
        server.sendContinue();
        Thread.sleep(WAIT_TIME);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

        assertEquals(client.pAbortCauseType, PAbortCauseType.AbnormalDialogue);
        assertEquals(server.pAbortCauseType, PAbortCauseType.AbnormalDialogue);
    }

    /**
     * TC-U-Abort as a response to TC-Begin
     *
     * TC-BEGIN TC-ABORT + UserAbort by TCAP user
     */
    @Test(groups = { "functional.flow" })
    public void userAbortTest() throws Exception {

        //
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

        UserInformation userInformation = TcapFactory.createUserInformation();
        userInformation.setOid(true);
        userInformation.setOidValue(new long[] { 1, 2, 3 });
        userInformation.setAsn(true);
        userInformation.setEncodeType(new byte[] { 11, 22, 33 });
        server.sendAbort(null, userInformation, null);
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
     * Sending a message with unreachable CalledPartyAddress + returnMessageOnError -> TC-Notice TC-BEGIN + returnMessageOnError
     * TC-NOTICE
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
        return new byte[] { 98, 117, 72, 1, 1, 107, 69, 40, 67, 6, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) 160, 56, 96, 54,
                (byte) 128, 2, 6, 64, (byte) 161, 9, 6, 7, 4, 0, 0, 1, 0, 19, 2, (byte) 190, 37, 40, 35, 6, 7, 4, 0, 0, 1, 1,
                1, 1, (byte) 160, 24, (byte) 160, (byte) 128, (byte) 128, 9, (byte) 150, 2, 36, (byte) 128, 3, 0, (byte) 128,
                0, (byte) 242, (byte) 129, 7, (byte) 145, 19, 38, (byte) 152, (byte) 134, 3, (byte) 240, 0, 0, 108, 41,
                (byte) 161, 39, 2, 1, (byte) 128, 2, 2, 2, 79, 36, 30, 4, 1, 15, 4, 16, (byte) 170, (byte) 152, (byte) 172,
                (byte) 166, 90, (byte) 205, 98, 54, 25, 14, 55, (byte) 203, (byte) 229, 114, (byte) 185, 17, (byte) 128, 7,
                (byte) 145, 19, 38, (byte) 136, (byte) 131, 0, (byte) 242 };
    }

    public static byte[] getMessageBadSyntax() {
        return new byte[] { 98, 6, 72, 1, 1, 1, 2, 3 };
    }

    public static byte[] getMessageBadTag() {
        return new byte[] { 106, 6, 72, 1, 1, 73, 1, 1 };
    }

    @Test(groups = { "functional.flow" })
    public void UnrecognizedMessageTypeTest() throws Exception {

        // case of receiving TC-Begin + AARQ apdu + unsupported protocol version (supported only V2)
        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, null, 0, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

        client.startClientDialog();
        SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address, peer1Address,
                getUnrecognizedMessageTypeMessage(), 0, 0, false, null, null);
        this.sccpProvider1.send(message);
        client.waitFor(WAIT_TIME);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

        assertEquals(client.pAbortCauseType, PAbortCauseType.UnrecognizedMessageType);
    }

    public static byte[] getUnrecognizedMessageTypeMessage() {
        return new byte[] { 105, 6, 72, 4, 0, 0, 0, 1 };
    }
}
