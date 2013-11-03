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

package org.mobicents.protocols.ss7.tcapAnsi.dialog.timeout;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcapAnsi.EventType;
import org.mobicents.protocols.ss7.tcapAnsi.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcapAnsi.TestEvent;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPException;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test for call flow.
 *
 * @author baranowb
 *
 */
@Test
public class DialogIdleEndTest extends SccpHarness {

    private static final int _WAIT_TIMEOUT = 90000;
    private static final int _WAIT_REMOVE = 30000;
    private static final int _DIALOG_TIMEOUT = 5000;
    private static final int _WAIT = _DIALOG_TIMEOUT / 2 - 200;
    private TCAPStackImpl tcapStack1;
    private TCAPStackImpl tcapStack2;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;
    private Client client;
    private Server server;

    public DialogIdleEndTest() {

    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "DialogIdleEndTestSccpStack1";
        this.sccpStack2Name = "DialogIdleEndTestSccpStack2";
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
        this.tcapStack1.setDialogIdleTimeout(_DIALOG_TIMEOUT * 2);
        this.tcapStack2.setDialogIdleTimeout(_DIALOG_TIMEOUT); // so other side dont timeout :)

        this.tcapStack1.start();
        this.tcapStack2.start();

    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @AfterMethod
    public void tearDown() {
        System.out.println("tearDown");
        this.tcapStack1.stop();
        this.tcapStack2.stop();
        super.tearDown();

    }

    @Test(groups = { "functional.timeout.idle", "end" })
    public void testAfterBeginOnly() throws TCAPException, TCAPSendException {

        this.client = new Client(tcapStack1, peer1Address, peer2Address);

        this.server = new Server(tcapStack2, peer2Address, peer1Address) {

            @Override
            public void onDialogTimeout(Dialog d) {

                super.onDialogTimeout(d);

                // send abort :)
                try {
                    // UI is required...
                    UserInformationElement uie = this.tcapProvider.getDialogPrimitiveFactory().createUserInformationElement();
                    uie = this.tcapProvider.getDialogPrimitiveFactory().createUserInformationElement();
                    uie.setArbitrary(true);
                    BitSetStrictLength bs = new BitSetStrictLength(4);
                    bs.set(0);
                    bs.set(3);
                    uie.setEncodeBitStringType(bs);
                    uie.setAsn(false);
                    uie.setOid(true);
                    uie.setOidValue(_ACN_);
                    ApplicationContext _acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
                    sendAbort(_acn, uie);
                } catch (TCAPSendException e) {

                    e.printStackTrace();
                    fail("Got error! " + e);
                }
            }

        };

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.UAbort, null, 1, stamp + _WAIT + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + _WAIT + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _WAIT + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.UAbort, null, 2, stamp + _WAIT + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _WAIT + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);

        client.startClientDialog();
        client.waitFor(_WAIT);
        client.sendBegin();
        client.waitFor(_WAIT * 3);
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    @Test(groups = { "functional.timeout.idle", "end" })
    public void testAfterContinue() throws TCAPException, TCAPSendException {

        this.client = new Client(tcapStack1, peer1Address, peer2Address);

        this.server = new Server(tcapStack2, peer2Address, peer1Address) {

            @Override
            public void onDialogTimeout(Dialog d) {

                super.onDialogTimeout(d);

                // send abort :)
                try {
                    // UI is required...
                    UserInformationElement uie = this.tcapProvider.getDialogPrimitiveFactory().createUserInformationElement();
                    uie = this.tcapProvider.getDialogPrimitiveFactory().createUserInformationElement();
                    uie.setArbitrary(true);
                    BitSetStrictLength bs = new BitSetStrictLength(4);
                    bs.set(0);
                    bs.set(3);
                    uie.setEncodeBitStringType(bs);
                    uie.setAsn(false);
                    uie.setOid(true);
                    uie.setOidValue(_ACN_);
                    ApplicationContext _acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
                    sendAbort(_acn, uie);
                } catch (TCAPSendException e) {

                    e.printStackTrace();
                    fail("Got error! " + e);
                }
            }

        };

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.UAbort, null, 2, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 2, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.UAbort, null, 3, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);

        client.startClientDialog();
        client.waitFor(_WAIT);
        client.sendBegin();
        client.waitFor(_WAIT);
        server.sendContinue(true);
        client.waitFor(_WAIT * 3);
        // waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    @Test(groups = { "functional.timeout.idle", "end" })
    public void testAfterContinue2() throws TCAPException, TCAPSendException {

        this.client = new Client(tcapStack1, peer1Address, peer2Address);

        this.server = new Server(tcapStack2, peer2Address, peer1Address) {

            @Override
            public void onDialogTimeout(Dialog d) {

                super.onDialogTimeout(d);

                // send abort :)
                try {
                    // UI is required...
                    UserInformationElement uie = this.tcapProvider.getDialogPrimitiveFactory().createUserInformationElement();
                    uie = this.tcapProvider.getDialogPrimitiveFactory().createUserInformationElement();
                    uie.setArbitrary(true);
                    BitSetStrictLength bs = new BitSetStrictLength(4);
                    bs.set(0);
                    bs.set(3);
                    uie.setEncodeBitStringType(bs);
                    uie.setAsn(false);
                    uie.setOid(true);
                    uie.setOidValue(_ACN_);
                    ApplicationContext _acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
                    sendAbort(_acn, uie);
                } catch (TCAPSendException e) {

                    e.printStackTrace();
                    fail("Got error! " + e);
                }
            }

        };

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 2, stamp + _WAIT * 3);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.UAbort, null, 3, stamp + _WAIT * 3 + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _WAIT * 3 + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 2, stamp + _WAIT * 3);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 3, stamp + _WAIT * 3 + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.UAbort, null, 4, stamp + _WAIT * 3 + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 5, stamp + _WAIT * 3 + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);

        client.startClientDialog();
        client.waitFor(_WAIT);
        client.sendBegin();
        client.waitFor(_WAIT);
        server.sendContinue(true);
        client.waitFor(_WAIT);
        client.sendContinue(true);
        client.waitFor(_WAIT * 3);
        // waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    @Test(groups = { "functional.timeout.idle", "end" })
    public void testAfterEnd() throws TCAPException, TCAPSendException {

        this.client = new Client(tcapStack1, peer1Address, peer2Address);

        this.server = new Server(tcapStack2, peer2Address, peer1Address) {

            @Override
            public void onDialogTimeout(Dialog d) {

                super.onDialogTimeout(d);

                // send abort :)
                try {
                    // UI is required...
                    UserInformationElement uie = this.tcapProvider.getDialogPrimitiveFactory().createUserInformationElement();
                    uie = this.tcapProvider.getDialogPrimitiveFactory().createUserInformationElement();
                    uie.setArbitrary(true);
                    BitSetStrictLength bs = new BitSetStrictLength(4);
                    bs.set(0);
                    bs.set(3);
                    uie.setEncodeBitStringType(bs);
                    uie.setAsn(false);
                    uie.setOid(true);
                    uie.setOidValue(_ACN_);
                    ApplicationContext _acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
                    sendAbort(_acn, uie);
                } catch (TCAPSendException e) {

                    e.printStackTrace();
                    fail("Got error! " + e);
                }
            }

        };

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 2, stamp + _WAIT * 3);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.End, null, 3, stamp + _WAIT * 4);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _WAIT * 4);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 2, stamp + _WAIT * 3);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.End, null, 3, stamp + _WAIT * 4);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _WAIT * 4);
        serverExpectedEvents.add(te);

        client.startClientDialog();
        client.waitFor(_WAIT);
        client.sendBegin();
        client.waitFor(_WAIT);
        server.sendContinue(true);
        client.waitFor(_WAIT);
        client.sendContinue(true);
        client.waitFor(_WAIT);
        client.sendEnd(true);
        client.waitFor(_WAIT);
        // waitForEnd();
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);
    }

    @Test(groups = { "functional.timeout.idle", "end" })
    public void testAfterContinue_NoTimeout() throws TCAPException, TCAPSendException {

        this.client = new Client(tcapStack1, peer1Address, peer2Address);

        this.server = new Server(tcapStack2, peer2Address, peer1Address) {

            private boolean sendContinue = false;

            @Override
            public void onDialogTimeout(Dialog d) {

                super.onDialogTimeout(d);
                if (!sendContinue) {
                    // send continue
                    try {
                        sendContinue(true);
                    } catch (TCAPException e) {
                        e.printStackTrace();
                        fail("Received exception. Message: " + e.getMessage());
                    } catch (TCAPSendException e) {
                        e.printStackTrace();
                        fail("Received exception. Message: " + e.getMessage());
                    }
                    sendContinue = true;
                }

            }

        };

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 2, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 3, stamp + _WAIT * 4 + _DIALOG_TIMEOUT * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _WAIT * 4 + _DIALOG_TIMEOUT * 2);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 2, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 3, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 4, stamp + _WAIT * 2 + _DIALOG_TIMEOUT * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 5, stamp + _WAIT * 2 + _DIALOG_TIMEOUT * 2);
        serverExpectedEvents.add(te);

        client.startClientDialog();
        client.waitFor(_WAIT);
        client.sendBegin();
        client.waitFor(_WAIT);
        server.sendContinue(true);
        client.waitFor(_WAIT * 7);
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    @Test(groups = { "functional.timeout.idle", "end" })
    public void testKeepAlive() throws TCAPException, TCAPSendException {

        this.client = new Client(tcapStack1, peer1Address, peer2Address);

        this.server = new Server(tcapStack2, peer2Address, peer1Address) {

            @Override
            public void onDialogTimeout(Dialog d) {

                super.onDialogTimeout(d);

                d.keepAlive();
            }

        };

        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _WAIT + _DIALOG_TIMEOUT * 2); // *2 cause
                                                                                                                   // its
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + _WAIT + _DIALOG_TIMEOUT * 2);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
        serverExpectedEvents.add(te);
        for (int index = 1; index < 4; index++) {
            te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, index, stamp + _WAIT + _DIALOG_TIMEOUT * index);
            serverExpectedEvents.add(te);
        }

        client.startClientDialog();
        client.waitFor(_WAIT);
        client.sendBegin();
        client.waitFor(_WAIT * 7);
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    private void waitForEnd() {
        try {
            Thread.currentThread().sleep(_WAIT_TIMEOUT);
        } catch (InterruptedException e) {
            fail("Interrupted on wait!");
        }
    }

}
