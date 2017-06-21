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

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javolution.util.FastList;

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.parameter.BCDEvenEncodingScheme;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class MultiSsnTest extends SccpHarness {
    public static final long WAIT_TIME = 500;

    public static final int SIDE_1_EXTRA_LOCAL_SSN_1 = 12;
    public static final int SIDE_1_EXTRA_LOCAL_SSN_2 = 13;
    public static final int SIDE_2_EXTRA_LOCAL_SSN_1 = 23;
    public static final int SIDE_2_EXTRA_LOCAL_SSN_2 = 23;

    private TCAPStackImpl tcapStack1;
    private TCAPStackImpl tcapStack2;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;
    private Client client;
    private Server server;

    private SccpAddress peer1AddressExtraSsn;
    private SccpAddress peer2AddressExtraSsn;
    private SccpAddress peer1AddressWrongSsn;
    private SccpAddress peer2AddressWrongSsn;
    private SccpAddress peer1AddressExtraSsnGt;
    private SccpAddress peer2AddressExtraSsnGt;
    private SccpAddress peer1AddressWrongSsnGt;
    private SccpAddress peer2AddressWrongSsnGt;

    public MultiSsnTest() {
    }

    @BeforeClass
    public void setUpClass() {
        this.sccpStack1Name = "TCAPCongestionTestSccpStack1";
        this.sccpStack2Name = "TCAPCongestionTestSccpStack2";
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

        // adding of extra remote SSNs
        resource1.addRemoteSsn(2, getStack2PC(), SIDE_2_EXTRA_LOCAL_SSN_1, 0, false);
        resource1.addRemoteSsn(3, getStack2PC(), SIDE_2_EXTRA_LOCAL_SSN_2, 0, false);
        resource1.addRemoteSsn(4, getStack2PC(), 123, 0, false);
        resource2.addRemoteSsn(2, getStack1PC(), SIDE_1_EXTRA_LOCAL_SSN_1, 0, false);
        resource2.addRemoteSsn(3, getStack1PC(), SIDE_1_EXTRA_LOCAL_SSN_2, 0, false);

        // adding of routing rules for GT type
        GlobalTitle gtPattern = super.parameterFactory.createGlobalTitle("*", 0, NumberingPlan.ISDN_TELEPHONY, new BCDEvenEncodingScheme(), NatureOfAddress.INTERNATIONAL);
        SccpAddress pattern = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gtPattern, 0, 0);

        GlobalTitle gtRa11 = super.parameterFactory.createGlobalTitle("1111", 0, NumberingPlan.ISDN_TELEPHONY, new BCDEvenEncodingScheme(), NatureOfAddress.INTERNATIONAL);
//        GlobalTitle gtRa12 = super.parameterFactory.createGlobalTitle("11112222", 0, NumberingPlan.ISDN_TELEPHONY, new BCDEvenEncodingScheme(), NatureOfAddress.INTERNATIONAL);
        SccpAddress routingAddress11 = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gtRa11, 2, 0);
        SccpAddress routingAddress12 = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gtRa11, 1, 0);
        router1.addRoutingAddress(1, routingAddress11);
        router1.addRoutingAddress(2, routingAddress12);
        router1.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 1, -1, null, 0, null);
        router1.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE, pattern, "K", 2, -1, null, 0, null);

//        GlobalTitle gtRa21 = super.parameterFactory.createGlobalTitle("11112222", 0, NumberingPlan.ISDN_TELEPHONY, new BCDEvenEncodingScheme(), NatureOfAddress.INTERNATIONAL);
//        GlobalTitle gtRa22 = super.parameterFactory.createGlobalTitle("11111111", 0, NumberingPlan.ISDN_TELEPHONY, new BCDEvenEncodingScheme(), NatureOfAddress.INTERNATIONAL);
        SccpAddress routingAddress21 = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gtRa11, 1, 0);
        SccpAddress routingAddress22 = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gtRa11, 2, 0);
        router2.addRoutingAddress(1, routingAddress21);
        router2.addRoutingAddress(2, routingAddress22);
        router2.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 1, -1, null, 0, null);
        router2.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE, pattern, "K", 2, -1, null, 0, null);

        peer1Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
        peer2Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

        peer1AddressExtraSsn = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1,
                SIDE_1_EXTRA_LOCAL_SSN_1);
        peer2AddressExtraSsn = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2,
                SIDE_2_EXTRA_LOCAL_SSN_2);
        peer1AddressWrongSsn = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1,
                112);
        peer2AddressWrongSsn = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2,
                123);

        GlobalTitle gt1 = super.parameterFactory.createGlobalTitle("11111111", 0, NumberingPlan.ISDN_TELEPHONY, new BCDEvenEncodingScheme(), NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt2 = super.parameterFactory.createGlobalTitle("11112222", 0, NumberingPlan.ISDN_TELEPHONY, new BCDEvenEncodingScheme(), NatureOfAddress.INTERNATIONAL);
        
        peer1AddressExtraSsnGt = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 1,
                SIDE_1_EXTRA_LOCAL_SSN_1);
        peer2AddressExtraSsnGt = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, 2,
                SIDE_2_EXTRA_LOCAL_SSN_2);
        peer1AddressWrongSsnGt = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 1,
                112);
        peer2AddressWrongSsnGt = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, 2,
                123);


        this.tcapStack1 = new TCAPStackImpl("TCAPCongestionTest1", this.sccpProvider1, 8);
        this.tcapStack2 = new TCAPStackImpl("TCAPCongestionTest2", this.sccpProvider2, 8);

        List<Integer> extraSsnsNew1 = new FastList<Integer>();
        extraSsnsNew1.add(SIDE_1_EXTRA_LOCAL_SSN_1);
        extraSsnsNew1.add(SIDE_1_EXTRA_LOCAL_SSN_2);

        List<Integer> extraSsnsNew2 = new FastList<Integer>();
        extraSsnsNew2.add(SIDE_2_EXTRA_LOCAL_SSN_1);
        extraSsnsNew2.add(SIDE_2_EXTRA_LOCAL_SSN_2);

        this.tcapStack1.setExtraSsns(extraSsnsNew1);
        this.tcapStack2.setExtraSsns(extraSsnsNew2);

        this.tcapStack1.start();
        this.tcapStack2.start();

        this.tcapStack1.setDoNotSendProtocolVersion(false);
        this.tcapStack2.setDoNotSendProtocolVersion(false);
        this.tcapStack1.setInvokeTimeout(0);
        this.tcapStack2.setInvokeTimeout(0);
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

    private List<TestEvent> fillServerEvent(long stamp) {
        TestEvent te;
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
        serverExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
        serverExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.End, null, 2, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);
        // te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3,stamp+WAIT_TIME*2+_WAIT_REMOVE);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME * 2);
        serverExpectedEvents.add(te);
        return serverExpectedEvents;
    }

    private List<TestEvent> fillClientEvent(long stamp) {
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
        clientExpectedEvents.add(te);
        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
        clientExpectedEvents.add(te);
        te = TestEvent.createSentEvent(EventType.End, null, 2, stamp + WAIT_TIME * 2);
        clientExpectedEvents.add(te);
        // te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3,stamp+WAIT_TIME*2+_WAIT_REMOVE);
        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME * 2);
        clientExpectedEvents.add(te);
        return clientExpectedEvents;
    }

    @Test(groups = { "functional.multissn" })
    public void succTest() throws Exception {
        assertTrue(this.tcapStack1.isExtraSsnPresent(8));
        assertTrue(this.tcapStack1.isExtraSsnPresent(12));
        assertTrue(this.tcapStack1.isExtraSsnPresent(13));
        assertFalse(this.tcapStack1.isExtraSsnPresent(1));
        assertFalse(this.tcapStack1.isExtraSsnPresent(0));

        long stamp = System.currentTimeMillis();
        TestEvent te;
        List<TestEvent> clientExpectedEvents = fillClientEvent(stamp);
        List<TestEvent> serverExpectedEvents = fillServerEvent(stamp);

        client.startClientDialog(peer1AddressExtraSsn, peer2AddressExtraSsn);
        assertNotNull(client.dialog.getLocalAddress());
        assertNull(client.dialog.getRemoteDialogId());

        client.sendBegin();
        client.waitFor(WAIT_TIME);

        assertEquals(server.components.length, 2);

        server.sendContinue();
        assertNotNull(server.dialog.getLocalAddress());
        assertNotNull(server.dialog.getRemoteDialogId());

        client.waitFor(WAIT_TIME);
        client.sendEnd(TerminationType.Basic);
        assertNotNull(client.dialog.getLocalAddress());
        assertNotNull(client.dialog.getRemoteDialogId());

        client.waitFor(WAIT_TIME);
        // waitForEnd();

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    @Test(groups = { "functional.multissn" })
    public void succGtTest() throws Exception {
        assertTrue(this.tcapStack1.isExtraSsnPresent(8));
        assertTrue(this.tcapStack1.isExtraSsnPresent(12));
        assertTrue(this.tcapStack1.isExtraSsnPresent(13));
        assertFalse(this.tcapStack1.isExtraSsnPresent(1));
        assertFalse(this.tcapStack1.isExtraSsnPresent(0));

        long stamp = System.currentTimeMillis();
        TestEvent te;
        List<TestEvent> clientExpectedEvents = fillClientEvent(stamp);
        List<TestEvent> serverExpectedEvents = fillServerEvent(stamp);

        client.startClientDialog(peer1AddressExtraSsnGt, peer2AddressExtraSsnGt);
        assertNotNull(client.dialog.getLocalAddress());
        assertNull(client.dialog.getRemoteDialogId());

        client.sendBegin();
        client.waitFor(WAIT_TIME);

        assertEquals(server.components.length, 2);

        server.sendContinue();
        assertNotNull(server.dialog.getLocalAddress());
        assertNotNull(server.dialog.getRemoteDialogId());

        client.waitFor(WAIT_TIME);
        client.sendEnd(TerminationType.Basic);
        assertNotNull(client.dialog.getLocalAddress());
        assertNotNull(client.dialog.getRemoteDialogId());

        client.waitFor(WAIT_TIME);
        // waitForEnd();

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    private List<TestEvent> fillServerEventErr(long stamp) {
        TestEvent te;
        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
//        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
//        serverExpectedEvents.add(te);
        return serverExpectedEvents;
    }

    private List<TestEvent> fillClientEventErr(long stamp) {
        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
        clientExpectedEvents.add(te);
        return clientExpectedEvents;
    }

    @Test(groups = { "functional.multissn" })
    public void err1Test() throws Exception {
        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = fillClientEventErr(stamp);
        List<TestEvent> serverExpectedEvents = fillServerEventErr(stamp);

        client.startClientDialog(peer1AddressWrongSsn, peer2AddressWrongSsn);
        assertNotNull(client.dialog.getLocalAddress());
        assertNull(client.dialog.getRemoteDialogId());

        client.sendBegin();
        client.waitFor(WAIT_TIME);

        assertNull(server.components);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

    @Test(groups = { "functional.multissn" })
    public void err2Test() throws Exception {
        long stamp = System.currentTimeMillis();
        List<TestEvent> clientExpectedEvents = fillClientEventErr(stamp);
        List<TestEvent> serverExpectedEvents = fillServerEventErr(stamp);

        this.saveTrafficInFile();

        client.startClientDialog(peer1AddressWrongSsnGt, peer2AddressWrongSsnGt);
        assertNotNull(client.dialog.getLocalAddress());
        assertNull(client.dialog.getRemoteDialogId());

        client.sendBegin();
        client.waitFor(WAIT_TIME);

        assertNull(server.components);

        client.compareEvents(clientExpectedEvents);
        server.compareEvents(serverExpectedEvents);

    }

}
