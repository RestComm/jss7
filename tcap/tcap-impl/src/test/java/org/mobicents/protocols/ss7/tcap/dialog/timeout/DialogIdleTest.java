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

package org.mobicents.protocols.ss7.tcap.dialog.timeout;

import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.EventType;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.TestEvent;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;
import org.mobicents.protocols.ss7.tcap.api.TCAPSendException;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
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
public class DialogIdleTest extends SccpHarness {

    private static final int _WAIT_TIMEOUT = 90000;
    private static final int _WAIT_REMOVE = 30000;
    private static final int _DIALOG_TIMEOUT = 5000;
    private static final int _WAIT = _DIALOG_TIMEOUT / 2;
    private TCAPStackImpl tcapStack1;
    private TCAPStackImpl tcapStack2;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;
    private Client client;
    private Server server;

    public DialogIdleTest() {

    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "DialogIdleTestSccpStack1";
        this.sccpStack2Name = "DialogIdleTestSccpStack2";
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

        this.client = new Client(tcapStack1, peer1Address, peer2Address);
        this.server = new Server(tcapStack2, peer2Address, peer1Address);

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

    @Test(groups = { "functional.timeout.idle" })
    public void testCreateOnly() throws TCAPException {
        long stamp = System.currentTimeMillis();
        List<TestEvent> expectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 0, stamp + _DIALOG_TIMEOUT);
        expectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp + _DIALOG_TIMEOUT);
        expectedEvents.add(te);

        client.startClientDialog();
        client.waitFor(_WAIT * 3);
        // waitForEnd();
        client.compareEvents(expectedEvents);

    }

    @Test(groups = { "functional.timeout.idle" })
    public void testAfterBeginOnly() throws TCAPException, TCAPSendException {
        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _WAIT + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + _WAIT + _DIALOG_TIMEOUT);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _WAIT + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + _WAIT + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);

        client.startClientDialog();
        client.waitFor(_WAIT);
        client.sendBegin();
        client.waitFor(_WAIT * 3);
        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    @Test(groups = { "functional.timeout.idle" })
    public void testAfterContinue() throws TCAPException, TCAPSendException {
        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 2, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
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
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);

        client.startClientDialog();
        try {
            client.waitFor(_WAIT);
            client.sendBegin();
            client.waitFor(_WAIT);
            server.sendContinue();
            client.waitFor(_WAIT * 3);
        } finally {
            client.compareEvents(clientExpectedEvents);
            server.compareEvents(serverExpectedEvents);
        }

    }

    @Test(groups = { "functional.timeout.idle" })
    public void testAfterContinue2() throws TCAPException, TCAPSendException {
        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 2, stamp + _WAIT * 3);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 3, stamp + _WAIT * 3 + _DIALOG_TIMEOUT);
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
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _WAIT * 3 + _DIALOG_TIMEOUT);
        serverExpectedEvents.add(te);
        client.startClientDialog();
        try {
            client.waitFor(_WAIT);
            client.sendBegin();
            client.waitFor(_WAIT);
            server.sendContinue();
            client.waitFor(_WAIT);
            client.sendContinue();
            client.waitFor(_WAIT * 3);
        } finally {
            client.compareEvents(clientExpectedEvents);
            server.compareEvents(serverExpectedEvents);
        }

    }

    @Test(groups = { "functional.timeout.idle" })
    public void testAfterEnd() throws TCAPException, TCAPSendException {
        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.End, null, 2, stamp + _WAIT * 3);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _WAIT * 3);
        clientExpectedEvents.add(te);

        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.End, null, 2, stamp + _WAIT * 3);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _WAIT * 3);
        serverExpectedEvents.add(te);
        client.startClientDialog();
        try {
            client.waitFor(_WAIT);
            client.sendBegin();
            client.waitFor(_WAIT);
            server.sendContinue();
            client.waitFor(_WAIT);
            client.sendEnd(TerminationType.Basic);
            client.waitFor(_WAIT * 3);
        } finally {
            client.compareEvents(clientExpectedEvents);
            server.compareEvents(serverExpectedEvents);
        }

    }

    private void waitForEnd() {
        try {
            Thread.currentThread().sleep(_WAIT_TIMEOUT);
        } catch (InterruptedException e) {
            fail("Interrupted on wait!");
        }
    }

}
