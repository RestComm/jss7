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

package org.mobicents.protocols.ss7.sccp.impl;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.SccpConnection;
import org.mobicents.protocols.ss7.sccp.SccpConnectionState;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness3;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.User;
import org.mobicents.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ResetCauseImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ReleaseCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.ResetCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.scheduler.Clock;
import org.mobicents.protocols.ss7.scheduler.DefaultClock;
import org.mobicents.protocols.ss7.scheduler.Scheduler;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ConnectionCouplingTest extends SccpHarness3 {

    private SccpAddress a1, a2, a3, a3gt;
    private Clock clock;
    private Scheduler scheduler1;
    private Scheduler scheduler2;
    private Scheduler scheduler3;

    public ConnectionCouplingTest() {
        clock = new DefaultClock();
    }

    @DataProvider(name="ConnectionTestDataProvider")
    public static Object[][] createData() {
        return new Object[][] {
                new Object[] {false},
                new Object[] {true}
        };
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "TransitTestStack1";
        this.sccpStack2Name = "TransitTestStack2";
        this.sccpStack3Name = "TransitTestStack3";
    }

    @AfterClass
    public void tearDownClass() throws Exception {
    }

    protected void createStack1() {
        scheduler1 = new Scheduler();
        scheduler1.setClock(clock);
        scheduler1.start();
        sccpStack1 = createStack(scheduler1, sccpStack1Name);
        sccpProvider1 = sccpStack1.getSccpProvider();
        sccpStack1.start();
    }

    protected void createStack2() {
        if (onlyOneStack) {
            return;
        }
        scheduler2 = new Scheduler();
        scheduler2.setClock(clock);
        scheduler2.start();
        sccpStack2 = createStack(scheduler2, sccpStack2Name);
        sccpProvider2 = sccpStack2.getSccpProvider();
        sccpStack2.start();
    }

    protected void createStack3() {
        if (onlyOneStack) {
            return;
        }
        scheduler3 = new Scheduler();
        scheduler3.setClock(clock);
        scheduler3.start();
        sccpStack3 = createStack(scheduler3, sccpStack3Name);
        sccpProvider3 = sccpStack3.getSccpProvider();
        sccpStack3.start();
    }

    protected SccpStackImpl createStack(Scheduler scheduler, String name) {
        SccpStackImpl stack = new SccpStackImpl(scheduler, name);
        final String dir = Util.getTmpTestDir();
        if (dir != null) {
            stack.setPersistDir(dir);
        }
        return stack;
    }

    @BeforeMethod
    public void setUp() throws Exception {
        ssn2 = 99;
        ssn3 = 6;
        super.setUp();
        sccpStack2.setCanRelay(true);
    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();

        // to avoid stack configuration propagation between test cases
//        deleteDir(sccpStack1.getPersistDir());
//        deleteDir(sccpStack2.getPersistDir());
//        deleteDir(sccpStack3.getPersistDir());
    }

//    private void deleteDir(String pathname) {
//        File index = new File(pathname);
//        String[] files = index.list();
//        for(String file: files){
//            File current = new File(index.getPath(), file);
//            current.delete();
//        }
//    }

    private void stackParameterInit() {
        sccpStack1.referenceNumberCounter = 20;
        sccpStack2.referenceNumberCounter = 50;
        sccpStack3.referenceNumberCounter = 70;

        sccpStack1.iasTimerDelay = 7500 * 60;
        sccpStack1.iarTimerDelay = 16000 * 60;
        sccpStack2.iasTimerDelay = 7500 * 60;
        sccpStack2.iarTimerDelay = 16000 * 60;
        sccpStack3.iasTimerDelay = 7500 * 60;
        sccpStack3.iarTimerDelay = 16000 * 60;

        sccpStack1.sstTimerDuration_Min = 10000;
        sccpStack2.sstTimerDuration_Min = 10000;
        sccpStack3.sstTimerDuration_Min = 10000;

        sccpStack1.relTimerDelay = 15000;
        sccpStack1.repeatRelTimerDelay = 15000;
        sccpStack1.intTimerDelay = 30000;

        sccpStack2.relTimerDelay = 15000;
        sccpStack2.repeatRelTimerDelay = 15000;
        sccpStack2.intTimerDelay = 30000;

        sccpStack3.relTimerDelay = 15000;
        sccpStack3.repeatRelTimerDelay = 15000;
        sccpStack3.intTimerDelay = 30000;

        sccpStack1.connEstTimerDelay = 15000;
        sccpStack2.connEstTimerDelay = 15000;
        sccpStack3.connEstTimerDelay = 15000;
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testDataTransitProtocolClass2() throws Exception {
        stackParameterInit();
        testMessageTransit(2);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testDataTransitProtocolClass3() throws Exception {
        stackParameterInit();
        testMessageTransit(3);
    }

    private void testMessageTransit(int protocolClass) throws Exception {
        sccpStack1.connEstTimerDelay = 1000000000;
        sccpStack2.connEstTimerDelay = 1000000000;
        sccpStack3.connEstTimerDelay = 1000000000;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());
        a3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack3PC(), getSSN3());

        SccpAddress a3gt = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack2PC(), getSSN3());

        SccpAddress primaryAddress = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack3PC(), 0);
        SccpAddress pattern = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), 0, 0);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, null, getSSN());
        User u3 = new User(sccpStack3.getSccpProvider(), a3, null, getSSN3());

        sccpStack2.getRouter().addRoutingAddress(1, primaryAddress);
        sccpStack2.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K",
                1, -1, null, 0, null);

        u1.register();
        u3.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a3gt, a1,
                new byte[] { 0x01, 0x02, (byte) 0xFF }, new ImportanceImpl((byte) 1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(protocolClass));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(protocolClass));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack3.getConnectionsNumber(), 1);
        assertEquals(sccpStack2.getConnectionsNumber(), 2);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);
        SccpConnection conn3 = sccpProvider3.getConnections().values().iterator().next();

        SccpConnection conn21 = (SccpConnection) sccpProvider2.getConnections().values().toArray()[0];
        SccpConnection conn22 = (SccpConnection) sccpProvider2.getConnections().values().toArray()[1];

        Thread.sleep(100);

        conn1.send(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        conn3.send(new byte[]{1, 2, 3, 4, 5, 6});

        Thread.sleep(300);

        assertEquals(u1.getReceivedData().size(), 1);
        assertEquals(u3.getReceivedData().size(), 1);

        Thread.sleep(200);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), null); // new byte[] { (byte) 0x91, (byte) 0x92, (byte) 0x93 }

        Thread.sleep(200);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);
        assertEquals(sccpStack3.getConnectionsNumber(), 0);

        assertEquals(conn3.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);

        assertEquals(conn21.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn22.getState(), SccpConnectionState.CLOSED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testDataTransitDisabledProtocolClass2() throws Exception {
        stackParameterInit();
        testMessageTransitDisabled(2);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testDataTransitDisabledProtocolClass3() throws Exception {
        stackParameterInit();
        testMessageTransitDisabled(3);
    }

    private void testMessageTransitDisabled(int protocolClass) throws Exception {
        sccpStack2.setCanRelay(false);

        sccpStack1.connEstTimerDelay = 1000000000;
        sccpStack2.connEstTimerDelay = 1000000000;
        sccpStack3.connEstTimerDelay = 1000000000;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());
        a3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack3PC(), getSSN3());

        SccpAddress a3gt = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack2PC(), getSSN3());

        SccpAddress primaryAddress = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack3PC(), 0);
        SccpAddress pattern = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), 0, 0);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, null, getSSN());
        User u3 = new User(sccpStack3.getSccpProvider(), a3, null, getSSN3());

        sccpStack2.getRouter().addRoutingAddress(1, primaryAddress);
        sccpStack2.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K",
                1, -1, null, 0, null);

        u1.register();
        u3.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a3gt, a1, null, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(protocolClass));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(protocolClass));
        conn1.establish(crMsg);

        Thread.sleep(100);

        SccpConnection conn3 = sccpProvider3.getConnections().values().iterator().next();

        assertEquals(sccpStack3.getConnectionsNumber(), 1);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);

        assertEquals(conn1.getState(), SccpConnectionState.ESTABLISHED);
        assertEquals(conn3.getState(), SccpConnectionState.ESTABLISHED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testRefuseProtocolClass2() throws Exception {
        stackParameterInit();
        testRefuseConnection(2);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testRefuseProtocolClass3() throws Exception {
        stackParameterInit();
        testRefuseConnection(3);
    }

    private void testRefuseConnection(int protocolClass) throws Exception {
        sccpStack1.connEstTimerDelay = 1000000000;
        sccpStack2.connEstTimerDelay = 1000000000;
        sccpStack3.connEstTimerDelay = 1000000000;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());
        a3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack3PC(), getSSN3());

        SccpAddress a3gt = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack2PC(), getSSN3());

        SccpAddress primaryAddress = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack3PC(), 0);
        SccpAddress pattern = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), 0, 0);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, null, getSSN());
        User u3 = new User(sccpStack3.getSccpProvider(), a3, null, getSSN3());

        u3.setRefuseConnections(true);

        sccpStack2.getRouter().addRoutingAddress(1, primaryAddress);
        sccpStack2.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K",
                1, -1, null, 0, null);

        u1.register();
        u3.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a3gt, a1, new byte[] { 0x11, 0x12, 0x13, 0x14, 0x15 }, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(protocolClass));
        if (protocolClass == 3) {
            crMsg.setCredit(new CreditImpl(100));
        }

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(protocolClass));
        conn1.establish(crMsg);

        Thread.sleep(200);

        assertEquals(u1.getRefusedCount(), 1);
        assertEquals(u3.getRefusedCount(), 1);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);
        assertEquals(sccpStack3.getConnectionsNumber(), 0);

        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testTransitReset() throws Exception {
        stackParameterInit();
        sccpStack1.connEstTimerDelay = 1000000000;
        sccpStack2.connEstTimerDelay = 1000000000;
        sccpStack3.connEstTimerDelay = 1000000000;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());
        a3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack3PC(), getSSN3());

        SccpAddress a3gt = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack2PC(), getSSN3());

        SccpAddress primaryAddress = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack3PC(), 0);
        SccpAddress pattern = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), 0, 0);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, null, getSSN());
        User u3 = new User(sccpStack3.getSccpProvider(), a3, null, getSSN3());

        sccpStack2.getRouter().addRoutingAddress(1, primaryAddress);
        sccpStack2.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K",
                1, -1, null, 0, null);

        u1.register();
        u3.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a3gt, a1, null, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        Thread.sleep(200); // 100

        assertEquals(sccpStack3.getConnectionsNumber(), 1);
        assertEquals(sccpStack2.getConnectionsNumber(), 2);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);
        SccpConnection conn3 = sccpProvider3.getConnections().values().iterator().next();

        SccpConnection conn21 = (SccpConnection) sccpProvider2.getConnections().values().toArray()[0];
        SccpConnection conn22 = (SccpConnection) sccpProvider2.getConnections().values().toArray()[1];

        Thread.sleep(100);

        conn1.send(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        conn3.send(new byte[]{1, 2, 3, 4, 5, 6});

        Thread.sleep(300);

        assertEquals(u1.getReceivedData().size(), 1);
        assertEquals(u3.getReceivedData().size(), 1);

        Thread.sleep(200);

        conn1.reset(new ResetCauseImpl(ResetCauseValue.UNQUALIFIED));
        Thread.sleep(100);

        assertEquals(u1.getResetCount(), 1);
        assertEquals(u3.getResetCount(), 1);

        assertEquals(conn3.getState(), SccpConnectionState.ESTABLISHED);
        assertEquals(conn1.getState(), SccpConnectionState.ESTABLISHED);

        assertEquals(conn21.getState(), SccpConnectionState.ESTABLISHED);
        assertEquals(conn22.getState(), SccpConnectionState.ESTABLISHED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testSegmentedDataTransitProtocolClass2() throws Exception {
        stackParameterInit();
        testSegmentedMessages(2);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testSegmentedDataTransitProtocolClass3() throws Exception {
        stackParameterInit();
        testSegmentedMessages(3);
    }

    private void testSegmentedMessages(int protocolClass) throws Exception {
        sccpStack1.connEstTimerDelay = 1000000000;
        sccpStack2.connEstTimerDelay = 1000000000;
        sccpStack3.connEstTimerDelay = 1000000000;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());
        a3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack3PC(), getSSN3());

        SccpAddress a3gt = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack2PC(), getSSN3());

        SccpAddress primaryAddress = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack3PC(), 0);
        SccpAddress pattern = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), 0, 0);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, null, getSSN());
        User u3 = new User(sccpStack3.getSccpProvider(), a3, null, getSSN3());

        sccpStack2.getRouter().addRoutingAddress(1, primaryAddress);
        sccpStack2.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K",
                1, -1, null, 0, null);

        u1.register();
        u3.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a3gt, a1,
                new byte[] { 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29 }, new ImportanceImpl((byte) 1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(protocolClass));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(protocolClass));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack3.getConnectionsNumber(), 1);
        assertEquals(sccpStack2.getConnectionsNumber(), 2);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);
        SccpConnection conn3 = sccpProvider3.getConnections().values().iterator().next();

        SccpConnection conn21 = (SccpConnection) sccpProvider2.getConnections().values().toArray()[0];
        SccpConnection conn22 = (SccpConnection) sccpProvider2.getConnections().values().toArray()[1];

        Thread.sleep(100);

        byte[] largeData = new byte[255*3 + 10];
        for (int i = 0; i < 255*3 + 10; i++) {
            largeData[i] = (byte)i;
        }

        conn1.send(largeData);

        Thread.sleep(300);

        assertEquals(u1.getReceivedData().size(), 0);
        assertEquals(u3.getReceivedData().size(), 1);
        assertEquals(u3.getReceivedData().iterator().next(), largeData); //check if an incoming message content is the same as was sent

        Thread.sleep(200);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), null);

        Thread.sleep(200);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);
        assertEquals(sccpStack3.getConnectionsNumber(), 0);

        assertEquals(conn3.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);

        assertEquals(conn21.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn22.getState(), SccpConnectionState.CLOSED);
    }


    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testTransitNodeGetsNoDataProtocolClass2() throws Exception {
        stackParameterInit();
        testTransitNodeGetsNoData(2);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testTransitNodeGetsNoDataProtocolClass3() throws Exception {
        stackParameterInit();
        testTransitNodeGetsNoData(3);
    }

    private void testTransitNodeGetsNoData(int protocolClass) throws Exception {
        sccpStack1.connEstTimerDelay = 1000000000;
        sccpStack2.connEstTimerDelay = 1000000000;
        sccpStack3.connEstTimerDelay = 1000000000;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());
        a3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack3PC(), getSSN3());

        SccpAddress a3gt = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack2PC(), getSSN3());

        SccpAddress primaryAddress = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack3PC(), 0);
        SccpAddress pattern = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), 0, 0);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, null, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, null, getSSN3());
        User u3 = new User(sccpStack3.getSccpProvider(), a3, null, getSSN3());

        sccpStack2.getRouter().addRoutingAddress(1, primaryAddress);
        sccpStack2.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K",
                1, -1, null, 0, null);

        u1.register();
        u2.register();
        u3.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a3gt, a1,
                new byte[] { 0x01, 0x02, (byte) 0xFF }, new ImportanceImpl((byte) 1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(protocolClass));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(protocolClass));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack3.getConnectionsNumber(), 1);
        assertEquals(sccpStack2.getConnectionsNumber(), 2);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);
        SccpConnection conn3 = sccpProvider3.getConnections().values().iterator().next();

        SccpConnection conn21 = (SccpConnection) sccpProvider2.getConnections().values().toArray()[0];
        SccpConnection conn22 = (SccpConnection) sccpProvider2.getConnections().values().toArray()[1];

        Thread.sleep(100);

        conn1.send(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        conn3.send(new byte[]{1, 2, 3, 4, 5, 6});

        Thread.sleep(300);

        assertEquals(u1.getReceivedData().size(), 1);
        assertEquals(u2.getReceivedData().size(), 0);
        assertEquals(u3.getReceivedData().size(), 1);

        Thread.sleep(200);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), null); // new byte[] { (byte) 0x91, (byte) 0x92, (byte) 0x93 }

        Thread.sleep(200);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);
        assertEquals(sccpStack3.getConnectionsNumber(), 0);

        assertEquals(conn3.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);

        assertEquals(conn21.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn22.getState(), SccpConnectionState.CLOSED);
    }
}
