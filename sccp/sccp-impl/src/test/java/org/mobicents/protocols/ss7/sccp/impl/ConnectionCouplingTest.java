package org.mobicents.protocols.ss7.sccp.impl;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.SccpConnection;
import org.mobicents.protocols.ss7.sccp.SccpConnectionState;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness3;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImplProxy;
import org.mobicents.protocols.ss7.sccp.impl.User;
import org.mobicents.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ResetCauseImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle0010;
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
import org.testng.annotations.Test;

import java.io.File;

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
        scheduler2 = new Scheduler();
        scheduler2.setClock(clock);
        scheduler2.start();
        sccpStack2 = createStack(scheduler2, sccpStack2Name);
        sccpProvider2 = sccpStack2.getSccpProvider();
        sccpStack2.start();
    }

    protected void createStack3() {
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
        super.setUp();
        sccpStack2.setCanRelay(true);
    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();

        // to avoid stack configuration propagation between test cases
        deleteDir(sccpStack1.getPersistDir());
        deleteDir(sccpStack2.getPersistDir());
        deleteDir(sccpStack3.getPersistDir());
    }

    private void deleteDir(String pathname) {
        File index = new File(pathname);
        String[] files = index.list();
        for(String file: files){
            File current = new File(index.getPath(), file);
            current.delete();
        }
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testDataTransitProtocolClass2() throws Exception {
        testMessageTransit(2);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testDataTransitProtocolClass3() throws Exception {
        testMessageTransit(3);
    }

    private void testMessageTransit(int protocolClass) throws Exception {
        sccpStack1.connEstTimerDelay = 1000000000;
        sccpStack2.connEstTimerDelay = 1000000000;
        sccpStack3.connEstTimerDelay = 1000000000;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);
        a3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack3PC(), 8);

        SccpAddress a3gt = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack2PC(), 0);

        SccpAddress primaryAddress = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack3PC(), 8);
        SccpAddress pattern = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), 0, 0);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, null, getSSN());
        User u3 = new User(sccpStack3.getSccpProvider(), a3, null, getSSN());

        sccpStack2.getRouter().addRoutingAddress(1, primaryAddress);
        sccpStack2.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K",
                1, -1, null, 0, null);

        u1.register();
        u3.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a3gt, a1, new byte[] {}, new ImportanceImpl((byte)1));
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

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

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
        testMessageTransitDisabled(2);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testDataTransitDisabledProtocolClass3() throws Exception {
        testMessageTransitDisabled(3);
    }

    private void testMessageTransitDisabled(int protocolClass) throws Exception {
        sccpStack2.setCanRelay(false);

        sccpStack1.connEstTimerDelay = 1000000000;
        sccpStack2.connEstTimerDelay = 1000000000;
        sccpStack3.connEstTimerDelay = 1000000000;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);
        a3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack3PC(), 8);

        SccpAddress a3gt = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack2PC(), 0);

        SccpAddress primaryAddress = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack3PC(), 8);
        SccpAddress pattern = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), 0, 0);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, null, getSSN());
        User u3 = new User(sccpStack3.getSccpProvider(), a3, null, getSSN());

        sccpStack2.getRouter().addRoutingAddress(1, primaryAddress);
        sccpStack2.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K",
                1, -1, null, 0, null);

        u1.register();
        u3.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a3gt, a1, new byte[] {}, new ImportanceImpl((byte)1));
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
        testRefuseConnection(2);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testRefuseProtocolClass3() throws Exception {
        testRefuseConnection(3);
    }

    private void testRefuseConnection(int protocolClass) throws Exception {
        sccpStack1.connEstTimerDelay = 1000000000;
        sccpStack2.connEstTimerDelay = 1000000000;
        sccpStack3.connEstTimerDelay = 1000000000;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);
        a3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack3PC(), 8);

        SccpAddress a3gt = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack2PC(), 0);

        SccpAddress primaryAddress = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack3PC(), 8);
        SccpAddress pattern = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), 0, 0);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, null, getSSN());
        User u3 = new User(sccpStack3.getSccpProvider(), a3, null, getSSN());

        u3.setRefuseConnections(true);

        sccpStack2.getRouter().addRoutingAddress(1, primaryAddress);
        sccpStack2.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K",
                1, -1, null, 0, null);

        u1.register();
        u3.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a3gt, a1, new byte[] {}, new ImportanceImpl((byte)1));
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
        sccpStack1.connEstTimerDelay = 1000000000;
        sccpStack2.connEstTimerDelay = 1000000000;
        sccpStack3.connEstTimerDelay = 1000000000;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);
        a3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack3PC(), 8);

        SccpAddress a3gt = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack2PC(), 0);

        SccpAddress primaryAddress = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack3PC(), 8);
        SccpAddress pattern = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), 0, 0);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, null, getSSN());
        User u3 = new User(sccpStack3.getSccpProvider(), a3, null, getSSN());

        sccpStack2.getRouter().addRoutingAddress(1, primaryAddress);
        sccpStack2.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K",
                1, -1, null, 0, null);

        u1.register();
        u3.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a3gt, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(3));
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
        testSegmentedMessages(2);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testSegmentedDataTransitProtocolClass3() throws Exception {
        testSegmentedMessages(3);
    }

    private void testSegmentedMessages(int protocolClass) throws Exception {
        sccpStack1.connEstTimerDelay = 1000000000;
        sccpStack2.connEstTimerDelay = 1000000000;
        sccpStack3.connEstTimerDelay = 1000000000;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);
        a3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack3PC(), 8);

        SccpAddress a3gt = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack2PC(), 0);

        SccpAddress primaryAddress = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack3PC(), 8);
        SccpAddress pattern = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), 0, 0);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, null, getSSN());
        User u3 = new User(sccpStack3.getSccpProvider(), a3, null, getSSN());

        sccpStack2.getRouter().addRoutingAddress(1, primaryAddress);
        sccpStack2.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K",
                1, -1, null, 0, null);

        u1.register();
        u3.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a3gt, a1, new byte[] {}, new ImportanceImpl((byte)1));
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

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

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
