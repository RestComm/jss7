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

package org.mobicents.protocols.ss7.tcap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.mobicents.protocols.ss7.tcap.asn.EncodeException;
import org.mobicents.protocols.ss7.tcap.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcap.asn.OperationCodeImpl;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.InvokeProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnErrorProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultProblemType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test for component processing
 *
 * @author sergey vetyutnev
 *
 */
public class TCAPComponentsTest extends SccpHarness {

    public static final long MINI_WAIT_TIME = 100;
    public static final long WAIT_TIME = 500;
    private static final int _DIALOG_TIMEOUT = 5000000;

    private TCAPStackImpl tcapStack1;
    private TCAPStackImpl tcapStack2;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;
    private ClientComponent client;
    private ServerComponent server;

    public TCAPComponentsTest() {

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
     * Testing diplicateInvokeId case All Invokes are with a little invokeTimeout(removed before an answer from a Server) !!!
     *
     * TC-BEGIN + Invoke (invokeId==1) TC-CONTINUE + ReturnResult (invokeId==1) TC-CONTINUE + Reject(unrecognizedInvokeId) +
     * Invoke (invokeId==1) TC-CONTINUE + Reject (duplicateInvokeId) TC-CONTINUE + Invoke (invokeId==2) TC-CONTINUE +
     * ReturnResultLast (invokeId==1) + ReturnError (invokeId==2) TC-CONTINUE + Invoke (invokeId==1, for this message we will
     * invoke processWithoutAnswer()) + Invoke (invokeId==2) TC-CONTINUE TC-CONTINUE + Invoke (invokeId==1) + Invoke
     * (invokeId==2) * TC-END + Reject (duplicateInvokeId for invokeId==2)
     */
    @Test(groups = { "functional.flow" })
    public void DuplicateInvokeIdTest() throws Exception {

        this.client = new ClientComponent(this.tcapStack1, peer1Address, peer2Address) {

            @Override
            public void onTCContinue(TCContinueIndication ind) {
                super.onTCContinue(ind);

                step++;

                try {
                    switch (step) {
                        case 1:
                            assertEquals(ind.getComponents().length, 1);
                            Component c = ind.getComponents()[0];
                            assertEquals(c.getType(), ComponentType.Reject);
                            Reject r = (Reject) c;
                            assertEquals((long) r.getInvokeId(), 1);
                            assertEquals(r.getProblem().getReturnResultProblemType(),
                                    ReturnResultProblemType.UnrecognizedInvokeID);
                            assertTrue(r.isLocalOriginated());

                            this.addNewInvoke(1L, 5L);
                            this.sendContinue();
                            break;

                        case 2:
                            assertEquals(ind.getComponents().length, 1);
                            c = ind.getComponents()[0];
                            assertEquals(c.getType(), ComponentType.Reject);
                            r = (Reject) c;
                            assertEquals((long) r.getInvokeId(), 1);
                            assertEquals(r.getProblem().getInvokeProblemType(), InvokeProblemType.DuplicateInvokeID);
                            assertFalse(r.isLocalOriginated());

                            this.addNewInvoke(2L, 5L);
                            this.sendContinue();
                            break;

                        case 3:
                            assertEquals(ind.getComponents().length, 2);
                            c = ind.getComponents()[0];
                            assertEquals(c.getType(), ComponentType.Reject);
                            r = (Reject) c;
                            assertEquals((long) r.getInvokeId(), 1);
                            assertEquals(r.getProblem().getReturnResultProblemType(),
                                    ReturnResultProblemType.UnrecognizedInvokeID);
                            assertTrue(r.isLocalOriginated());

                            c = ind.getComponents()[1];
                            assertEquals(c.getType(), ComponentType.Reject);
                            r = (Reject) c;
                            assertEquals((long) r.getInvokeId(), 2);
                            assertEquals(r.getProblem().getReturnErrorProblemType(),
                                    ReturnErrorProblemType.UnrecognizedInvokeID);
                            assertTrue(r.isLocalOriginated());

                            this.addNewInvoke(1L, 5L);
                            this.addNewInvoke(2L, 5L);
                            this.sendContinue();
                            break;

                        case 4:
                            this.addNewInvoke(1L, 10000L);
                            this.addNewInvoke(2L, 10000L);
                            this.sendContinue();
                            break;
                    }
                } catch (Exception e) {
                    fail("Exception when sendComponent / send message 2", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onTCEnd(TCEndIndication ind) {
                super.onTCEnd(ind);

                try {
                    assertEquals(ind.getComponents().length, 1);
                    Component c = ind.getComponents()[0];
                    assertEquals(c.getType(), ComponentType.Reject);
                    Reject r = (Reject) c;
                    assertEquals((long) r.getInvokeId(), 2);
                    assertEquals(r.getProblem().getInvokeProblemType(), InvokeProblemType.DuplicateInvokeID);
                    assertFalse(r.isLocalOriginated());
                } catch (Exception e) {
                    fail("Exception when sendComponent / send message 3", e);
                    e.printStackTrace();
                }
            }
        };

        this.server = new ServerComponent(this.tcapStack2, peer2Address, peer1Address) {

            @Override
            public void onTCBegin(TCBeginIndication ind) {
                super.onTCBegin(ind);

                // waiting for Invoke timeout at a client side
                client.waitFor(MINI_WAIT_TIME);

                try {

                    this.addNewReturnResult(1L);
                    this.sendContinue();
                } catch (Exception e) {
                    fail("Exception when sendComponent / send message 1", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onTCContinue(TCContinueIndication ind) {
                super.onTCContinue(ind);

                // waiting for Invoke timeout at a client side
                client.waitFor(MINI_WAIT_TIME);

                step++;

                try {
                    switch (step) {
                        case 1:
                            assertEquals(ind.getComponents().length, 2);

                            Component c = ind.getComponents()[0];
                            assertEquals(c.getType(), ComponentType.Reject);
                            Reject r = (Reject) c;
                            assertEquals((long) r.getInvokeId(), 1);
                            assertEquals(r.getProblem().getReturnResultProblemType(),
                                    ReturnResultProblemType.UnrecognizedInvokeID);
                            assertFalse(r.isLocalOriginated());

                            c = ind.getComponents()[1];
                            assertEquals(c.getType(), ComponentType.Reject);
                            r = (Reject) c;
                            assertEquals((long) r.getInvokeId(), 1);
                            assertEquals(r.getProblem().getInvokeProblemType(), InvokeProblemType.DuplicateInvokeID);
                            assertTrue(r.isLocalOriginated());

                            this.sendContinue();
                            break;

                        case 2:
                            this.addNewReturnResultLast(1L);
                            this.addNewReturnError(2L);
                            this.sendContinue();
                            break;

                        case 3:
                            this.dialog.processInvokeWithoutAnswer(1L);

                            this.sendContinue();
                            break;

                        case 4:
                            assertEquals(ind.getComponents().length, 2);

                            c = ind.getComponents()[1];
                            assertEquals(c.getType(), ComponentType.Reject);
                            r = (Reject) c;
                            assertEquals((long) r.getInvokeId(), 2);
                            assertEquals(r.getProblem().getInvokeProblemType(), InvokeProblemType.DuplicateInvokeID);
                            assertTrue(r.isLocalOriginated());

                            this.sendEnd(TerminationType.Basic);
                            break;
                    }
                } catch (Exception e) {
                    fail("Exception when sendComponent / send message 2", e);
                    e.printStackTrace();
                }
            }

        };

        long stamp = System.currentTimeMillis();
        int cnt = 0;
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Begin, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, cnt++, stamp + MINI_WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, cnt++, stamp + MINI_WAIT_TIME * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 4);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 4);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 4);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 4);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.End, null, cnt++, stamp + MINI_WAIT_TIME * 5);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 5);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp + MINI_WAIT_TIME * 5);
        clientExpectedEvents.add(te);

        cnt = 0;
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, cnt++, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.ReturnResult, null, cnt++, stamp + MINI_WAIT_TIME);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.ReturnResultLast, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.ReturnError, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 3);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 4);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, cnt++, stamp + MINI_WAIT_TIME * 4);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp + MINI_WAIT_TIME * 4);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp + MINI_WAIT_TIME * 4);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.End, null, cnt++, stamp + MINI_WAIT_TIME * 5);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp + MINI_WAIT_TIME * 5);
        serverExpectedEvents.add(te);

        client.startClientDialog();
        client.addNewInvoke(1L, 5L);
        client.sendBegin();

        client.waitFor(WAIT_TIME * 2);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    /**
     * Sending unrecognizedComponent
     *
     * TC-BEGIN + bad component (with component type != Invoke,ReturnResult,...) + Invoke TC-END + Reject
     * (unrecognizedComponent)
     */
    @Test(groups = { "functional.flow" })
    public void UnrecognizedComponentTest() throws Exception {

        this.client = new ClientComponent(this.tcapStack1, peer1Address, peer2Address) {

            @Override
            public void onTCEnd(TCEndIndication ind) {
                super.onTCEnd(ind);

                assertEquals(ind.getComponents().length, 1);
                Component c = ind.getComponents()[0];
                assertEquals(c.getType(), ComponentType.Reject);
                Reject r = (Reject) c;
                assertNull(r.getInvokeId());
                assertEquals(r.getProblem().getGeneralProblemType(), GeneralProblemType.UnrecognizedComponent);
                assertFalse(r.isLocalOriginated());
            }
        };

        this.server = new ServerComponent(this.tcapStack2, peer2Address, peer1Address) {

            @Override
            public void onTCBegin(TCBeginIndication ind) {
                super.onTCBegin(ind);

                assertEquals(ind.getComponents().length, 2);
                Component c = ind.getComponents()[0];
                assertEquals(c.getType(), ComponentType.Reject);
                Reject r = (Reject) c;
                assertNull(r.getInvokeId());
                assertEquals(r.getProblem().getGeneralProblemType(), GeneralProblemType.UnrecognizedComponent);
                assertTrue(r.isLocalOriginated());

                try {
                    this.sendEnd(TerminationType.Basic);
                } catch (Exception e) {
                    fail("Exception when sendComponent / send message 1", e);
                    e.printStackTrace();
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int cnt = 0;
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Begin, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.End, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
        clientExpectedEvents.add(te);

        cnt = 0;
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, cnt++, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.End, null, cnt++, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
        serverExpectedEvents.add(te);

        client.startClientDialog();

        Component badComp = new BadComponentUnrecognizedComponent();
        client.dialog.sendComponent(badComp);

        client.addNewInvoke(1L, 10000L);
        client.sendBegin();

        client.waitFor(WAIT_TIME);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Sending MistypedComponent Component
     *
     * TC-BEGIN + Invoke with an extra bad component + Invoke TC-END + Reject (mistypedComponent)
     */
    @Test(groups = { "functional.flow" })
    public void MistypedComponentTest() throws Exception {

        this.client = new ClientComponent(this.tcapStack1, peer1Address, peer2Address) {

            @Override
            public void onTCEnd(TCEndIndication ind) {
                super.onTCEnd(ind);

                assertEquals(ind.getComponents().length, 1);
                Component c = ind.getComponents()[0];
                assertEquals(c.getType(), ComponentType.Reject);
                Reject r = (Reject) c;
                assertEquals((long) r.getInvokeId(), 1);
                assertEquals(r.getProblem().getGeneralProblemType(), GeneralProblemType.MistypedComponent);
                assertFalse(r.isLocalOriginated());
            }
        };

        this.server = new ServerComponent(this.tcapStack2, peer2Address, peer1Address) {

            @Override
            public void onTCBegin(TCBeginIndication ind) {
                super.onTCBegin(ind);

                assertEquals(ind.getComponents().length, 2);
                Component c = ind.getComponents()[0];
                assertEquals(c.getType(), ComponentType.Reject);
                Reject r = (Reject) c;
                assertEquals((long) r.getInvokeId(), 1);
                assertEquals(r.getProblem().getGeneralProblemType(), GeneralProblemType.MistypedComponent);
                assertTrue(r.isLocalOriginated());

                try {
                    this.sendEnd(TerminationType.Basic);
                } catch (Exception e) {
                    fail("Exception when sendComponent / send message 1", e);
                    e.printStackTrace();
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int cnt = 0;
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Begin, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.End, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
        clientExpectedEvents.add(te);

        cnt = 0;
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, cnt++, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.End, null, cnt++, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
        serverExpectedEvents.add(te);

        client.startClientDialog();

        Component badComp = new BadComponentMistypedComponent();
        badComp.setInvokeId(1L);
        client.dialog.sendComponent(badComp);

        client.addNewInvoke(2L, 10000L);
        client.sendBegin();

        client.waitFor(WAIT_TIME);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    /**
     * Sending BadlyStructuredComponent Component
     *
     * TC-BEGIN + Invoke with BadlyStructuredComponent + Invoke TC-END + Reject (mistypedComponent)
     */
    @Test(groups = { "functional.flow" })
    public void BadlyStructuredComponentTest() throws Exception {

        this.client = new ClientComponent(this.tcapStack1, peer1Address, peer2Address) {

            @Override
            public void onTCEnd(TCEndIndication ind) {
                super.onTCEnd(ind);

                assertEquals(ind.getComponents().length, 1);
                Component c = ind.getComponents()[0];
                assertEquals(c.getType(), ComponentType.Reject);
                Reject r = (Reject) c;
                assertNull(r.getInvokeId());
                assertEquals(r.getProblem().getGeneralProblemType(), GeneralProblemType.BadlyStructuredComponent);
                assertFalse(r.isLocalOriginated());
            }
        };

        this.server = new ServerComponent(this.tcapStack2, peer2Address, peer1Address) {

            @Override
            public void onTCBegin(TCBeginIndication ind) {
                super.onTCBegin(ind);

                assertEquals(ind.getComponents().length, 2);
                Component c = ind.getComponents()[0];
                assertEquals(c.getType(), ComponentType.Reject);
                Reject r = (Reject) c;
                assertNull(r.getInvokeId());
                assertEquals(r.getProblem().getGeneralProblemType(), GeneralProblemType.BadlyStructuredComponent);
                assertTrue(r.isLocalOriginated());

                try {
                    this.sendEnd(TerminationType.Basic);
                } catch (Exception e) {
                    fail("Exception when sendComponent / send message 1", e);
                    e.printStackTrace();
                }
            }
        };

        long stamp = System.currentTimeMillis();
        int cnt = 0;
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Invoke, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Begin, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.End, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
        clientExpectedEvents.add(te);

        cnt = 0;
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, cnt++, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Reject, null, cnt++, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Invoke, null, cnt++, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.End, null, cnt++, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, cnt++, stamp);
        serverExpectedEvents.add(te);

        client.startClientDialog();

        Component badComp = new BadComponentBadlyStructuredComponent();
        badComp.setInvokeId(1L);
        client.dialog.sendComponent(badComp);

        client.addNewInvoke(2L, 10000L);
        client.sendBegin();

        client.waitFor(WAIT_TIME);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    public class ClientComponent extends EventTestHarness {

        protected int step = 0;
        private Invoke lastSentInvoke;

        public ClientComponent(TCAPStack stack, SccpAddress thisAddress, SccpAddress remoteAddress) {
            super(stack, thisAddress, remoteAddress);

        }

        public DialogImpl getCurDialog() {
            return (DialogImpl) this.dialog;
        }

        @Override
        public void onTCContinue(TCContinueIndication ind) {
            super.onTCContinue(ind);

            procComponents(ind.getComponents());
        }

        @Override
        public void onTCEnd(TCEndIndication ind) {
            super.onTCEnd(ind);

            procComponents(ind.getComponents());
        }

        private void procComponents(Component[] compp) {
            if (compp != null) {
                for (Component c : compp) {
                    EventType et = null;
                    if (c.getType() == ComponentType.Invoke) {
                        et = EventType.Invoke;
                    }
                    if (c.getType() == ComponentType.ReturnResult) {
                        et = EventType.ReturnResult;
                    }
                    if (c.getType() == ComponentType.ReturnResultLast) {
                        et = EventType.ReturnResultLast;
                    }
                    if (c.getType() == ComponentType.ReturnError) {
                        et = EventType.ReturnError;
                    }
                    if (c.getType() == ComponentType.Reject) {
                        et = EventType.Reject;
                    }

                    TestEvent te = TestEvent.createReceivedEvent(et, c, sequence++);
                    this.observerdEvents.add(te);
                }
            }
        }

        public void addNewInvoke(Long invokeId, Long timout) throws Exception {

            Invoke invoke = this.tcapProvider.getComponentPrimitiveFactory().createTCInvokeRequest();
            invoke.setInvokeId(invokeId);

            OperationCode oc = TcapFactory.createOperationCode();

            oc.setLocalOperationCode(10L);
            invoke.setOperationCode(oc);

            invoke.setTimeout(timout);

            // Parameter p1 = TcapFactory.createParameter();
            // p1.setTagClass(Tag.CLASS_UNIVERSAL);
            // p1.setTag(Tag.STRING_OCTET);
            // p1.setData(new byte[]{0x0F});
            //
            // Parameter p2 = TcapFactory.createParameter();
            // p2.setTagClass(Tag.CLASS_UNIVERSAL);
            // p2.setTag(Tag.STRING_OCTET);
            // p2.setData(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac, (byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19,
            // 0x0e, 0x37, (byte) 0xcb, (byte) 0xe5,
            // 0x72, (byte) 0xb9, 0x11 });
            //
            // Parameter pm = TcapFactory.createParameter();
            // pm.setTagClass(Tag.CLASS_UNIVERSAL);
            // pm.setTag(Tag.SEQUENCE);
            // pm.setParameters(new Parameter[]{p1, p2});
            // invoke.setParameter(pm);

            TestEvent te = TestEvent.createSentEvent(EventType.Invoke, null, sequence++);
            this.observerdEvents.add(te);

            this.dialog.sendComponent(invoke);

            lastSentInvoke = invoke;
        }
    }

    public class ServerComponent extends EventTestHarness {

        protected int step = 0;

        private Component[] components;

        /**
         * @param stack
         * @param thisAddress
         * @param remoteAddress
         */
        public ServerComponent(TCAPStack stack, SccpAddress thisAddress, SccpAddress remoteAddress) {
            super(stack, thisAddress, remoteAddress);
            // TODO Auto-generated constructor stub
        }

        public void addNewReturnResult(Long invokeId) throws Exception {

            ReturnResult rr = this.tcapProvider.getComponentPrimitiveFactory().createTCResultRequest();
            rr.setInvokeId(invokeId);

            OperationCode oc = TcapFactory.createOperationCode();

            oc.setLocalOperationCode(10L);
            rr.setOperationCode(oc);

            TestEvent te = TestEvent.createSentEvent(EventType.ReturnResult, null, sequence++);
            this.observerdEvents.add(te);

            this.dialog.sendComponent(rr);
        }

        public void addNewReturnResultLast(Long invokeId) throws Exception {

            ReturnResultLast rr = this.tcapProvider.getComponentPrimitiveFactory().createTCResultLastRequest();
            rr.setInvokeId(invokeId);

            OperationCode oc = TcapFactory.createOperationCode();

            oc.setLocalOperationCode(10L);
            rr.setOperationCode(oc);

            TestEvent te = TestEvent.createSentEvent(EventType.ReturnResultLast, null, sequence++);
            this.observerdEvents.add(te);

            this.dialog.sendComponent(rr);
        }

        public void addNewReturnError(Long invokeId) throws Exception {

            ReturnError err = this.tcapProvider.getComponentPrimitiveFactory().createTCReturnErrorRequest();
            err.setInvokeId(invokeId);

            ErrorCode ec = this.tcapProvider.getComponentPrimitiveFactory().createErrorCode();
            ec.setLocalErrorCode(10L);
            err.setErrorCode(ec);

            TestEvent te = TestEvent.createSentEvent(EventType.ReturnError, null, sequence++);
            this.observerdEvents.add(te);

            this.dialog.sendComponent(err);
        }

        @Override
        public void onTCBegin(TCBeginIndication ind) {
            super.onTCBegin(ind);

            procComponents(ind.getComponents());
        }

        @Override
        public void onTCContinue(TCContinueIndication ind) {
            super.onTCContinue(ind);

            procComponents(ind.getComponents());
        }

        private void procComponents(Component[] compp) {
            if (compp != null) {
                for (Component c : compp) {
                    EventType et = null;
                    if (c.getType() == ComponentType.Invoke) {
                        et = EventType.Invoke;
                    }
                    if (c.getType() == ComponentType.ReturnResult) {
                        et = EventType.ReturnResult;
                    }
                    if (c.getType() == ComponentType.ReturnResultLast) {
                        et = EventType.ReturnResultLast;
                    }
                    if (c.getType() == ComponentType.ReturnError) {
                        et = EventType.ReturnError;
                    }
                    if (c.getType() == ComponentType.Reject) {
                        et = EventType.Reject;
                    }

                    TestEvent te = TestEvent.createReceivedEvent(et, c, sequence++);
                    this.observerdEvents.add(te);
                }
            }
        }
    }

    /**
     * A bad component with UnrecognizedComponent (unrecognized component tag)
     *
     */
    class BadComponentUnrecognizedComponent implements Component {

        @Override
        public void encode(AsnOutputStream aos) throws EncodeException {
            try {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 20);
                int pos = aos.StartContentDefiniteLength();

                aos.writeNull();

                aos.FinalizeContent(pos);

            } catch (IOException e) {
                throw new EncodeException("IOException while encoding Reject: " + e.getMessage(), e);
            } catch (AsnException e) {
                throw new EncodeException("AsnException while encoding Reject: " + e.getMessage(), e);
            }
        }

        @Override
        public void decode(AsnInputStream ais) throws ParseException {
            // TODO Auto-generated method stub

        }

        @Override
        public void setInvokeId(Long i) {
            // TODO Auto-generated method stub

        }

        @Override
        public Long getInvokeId() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ComponentType getType() {
            // TODO Auto-generated method stub
            return null;
        }

    }

    /**
     * A bad component with MistypedComponent
     *
     */
    class BadComponentMistypedComponent extends InvokeImpl {

        @Override
        public void encode(AsnOutputStream aos) throws EncodeException {
            try {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG);
                int pos = aos.StartContentDefiniteLength();

                aos.writeInteger(this.getInvokeId());

                // unexpected parameter
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 30, 100);

                OperationCode oc = new OperationCodeImpl();
                oc.setLocalOperationCode(20L);
                oc.encode(aos);

                aos.FinalizeContent(pos);

            } catch (IOException e) {
                throw new EncodeException("IOException while encoding Invoke: " + e.getMessage(), e);
            } catch (AsnException e) {
                throw new EncodeException("AsnException while encoding Invoke: " + e.getMessage(), e);
            }
        }

    }

    /**
     * A bad component with BadlyStructuredComponent
     *
     */
    class BadComponentBadlyStructuredComponent extends InvokeImpl {

        @Override
        public void encode(AsnOutputStream aos) throws EncodeException {
            try {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, _TAG);
                int pos = aos.StartContentDefiniteLength();

                OperationCode oc = new OperationCodeImpl();
                oc.setLocalOperationCode(20L);
                oc.encode(aos);

                aos.FinalizeContent(pos);

            } catch (AsnException e) {
                throw new EncodeException("AsnException while encoding Invoke: " + e.getMessage(), e);
            }
        }

    }
}
