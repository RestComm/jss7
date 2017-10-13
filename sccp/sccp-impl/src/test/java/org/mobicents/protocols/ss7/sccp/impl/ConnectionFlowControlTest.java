package org.mobicents.protocols.ss7.sccp.impl;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.MaxConnectionCountReached;
import org.mobicents.protocols.ss7.sccp.SccpConnection;
import org.mobicents.protocols.ss7.sccp.SccpConnectionState;
import org.mobicents.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.mobicents.protocols.ss7.sccp.parameter.Credit;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.ReleaseCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.scheduler.Clock;
import org.mobicents.protocols.ss7.scheduler.DefaultClock;
import org.mobicents.protocols.ss7.scheduler.Scheduler;
import org.testng.annotations.*;
import org.testng.annotations.Test;

import java.io.File;
import java.util.concurrent.locks.ReentrantLock;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ConnectionFlowControlTest extends SccpHarness {

    private static final byte[] DATA0 = {1, 0, 0, 0, 0, 0, 0, 1};
    private static final byte[] DATA1 = {1, 1, 1, 1, 1, 1, 1, 1};
    private static final byte[] DATA2 = {2, 2, 2, 2, 2, 2, 2, 2};
    private static final byte[] DATA3 = {3, 3, 3, 3, 3, 3, 3, 3};
    private static final byte[] DATA4 = {4, 4, 4, 4, 4, 4, 4, 4};
    private static final byte[] DATA5 = {5, 5, 5, 5, 5, 5, 5, 5};
    private static final byte[] DATA6 = {6, 6, 6, 6, 6, 6, 6, 6};

    private SccpAddress a1, a2;
    private Clock clock;
    private Scheduler scheduler1;
    private Scheduler scheduler2;

    public ConnectionFlowControlTest() {
        clock = new DefaultClock();
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "ConnectionFlowControlTestStack1";
        this.sccpStack2Name = "ConnectionFlowControlTestStack2";
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

    protected SccpStackImpl createStack(Scheduler scheduler, String name) {
        SccpStackImpl stack = new SccpStackImplConnProxy(scheduler, name);
        final String dir = Util.getTmpTestDir();
        if (dir != null) {
            stack.setPersistDir(dir);
        }
        return stack;
    }

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();

        // to avoid stack configuration propagation between test cases
        deleteDir(sccpStack1.getPersistDir());
        deleteDir(sccpStack2.getPersistDir());
    }

    private void deleteDir(String pathname) {
        File index = new File(pathname);
        String[] files = index.list();
        for(String file: files){
            File current = new File(index.getPath(), file);
            current.delete();
        }
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" })
    public void testLowCredit() throws Exception {
        ((SccpStackImplConnProxy)sccpStack1).setAkAutoSending(false);
        ((SccpStackImplConnProxy)sccpStack2).setAkAutoSending(false);

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        u1.register();
        u2.register();

        Thread.sleep(100);

        int credit = 2;

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(credit));

        SccpConnectionWithFlowControlImplProxy conn1 = (SccpConnectionWithFlowControlImplProxy) sccpProvider1
                .newConnection(8, new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);

        SccpConnectionWithFlowControlImplProxy conn2 = (SccpConnectionWithFlowControlImplProxy) sccpProvider2
                .getConnections().values().iterator().next();//TODO2

        Thread.sleep(100);

        conn1.send(DATA0);

        conn2.send(DATA1);
        conn2.send(DATA2);
        conn2.send(DATA3);
        conn2.send(DATA4);

        Thread.sleep(100);

        assertEquals(u1.getReceivedData().size(), 2);
        assertEquals(u1.getReceivedData().get(0), DATA1);
        assertEquals(u1.getReceivedData().get(1), DATA2);

        assertTrue(conn1.isShouldSendAk());
        assertTrue(conn2.getTransmitQueueSize() == 2);

        conn1.sendAk();

        Thread.sleep(100);

        assertEquals(u1.getReceivedData().size(), 4);
        assertEquals(u1.getReceivedData().get(2), DATA3);
        assertEquals(u1.getReceivedData().get(3), DATA4);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

        Thread.sleep(100);

        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn2.getState(), SccpConnectionState.CLOSED);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" })
    public void testOverloading() throws Exception {
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        u1.register();
        u2.register();

        Thread.sleep(100);

        int credit = 10;

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(credit));

        SccpConnectionWithFlowControlImplProxy conn1 = (SccpConnectionWithFlowControlImplProxy) sccpProvider1
                .newConnection(8, new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);

        SccpConnectionWithFlowControlImplProxy conn2 = (SccpConnectionWithFlowControlImplProxy) sccpProvider2
                .getConnections().values().iterator().next();

        Thread.sleep(100);

        conn1.send(DATA0);
        conn2.send(DATA1);
        conn2.send(DATA2);
        conn2.send(DATA3);
        conn2.send(DATA4);

        Thread.sleep(300);

        assertEquals(u1.getReceivedData().size(), 4);
        assertEquals(u1.getReceivedData().get(0), DATA1);
        assertEquals(u1.getReceivedData().get(1), DATA2);
        assertEquals(u1.getReceivedData().get(2), DATA3);
        assertEquals(u1.getReceivedData().get(3), DATA4);

        assertTrue(!conn1.isShouldSendAk());
        assertEquals(conn2.getTransmitQueueSize(), 0);

        conn1.setOverloaded(true);
        Thread.sleep(100);

        conn2.send(DATA5);
        conn2.send(DATA6);

        Thread.sleep(100);
        assertEquals(conn2.getTransmitQueueSize(), 2);


        assertTrue(!conn1.isShouldSendAk());
        assertEquals(conn2.getTransmitQueueSize(), 2);
        assertEquals(u1.getReceivedData().size(), 4);

        conn1.setOverloaded(false);
        Thread.sleep(300);

        assertTrue(!conn1.isShouldSendAk());
        assertEquals(conn2.getTransmitQueueSize(), 0);
        assertEquals(u1.getReceivedData().size(), 6);

        assertEquals(u1.getReceivedData().get(4), DATA5);
        assertEquals(u1.getReceivedData().get(5), DATA6);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

        Thread.sleep(100);

        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn2.getState(), SccpConnectionState.CLOSED);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testBigCredit() throws Exception {
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        u1.register();
        u2.register();

        Thread.sleep(100);

        int credit = 127;

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(credit));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);
        SccpConnection conn2 = sccpProvider2.getConnections().values().iterator().next();

        Thread.sleep(100);

        for (int i=0; i<=127*3; i++) {
            conn2.send(new byte[]{2, (byte)i, (byte)i, (byte)i, (byte)i});
        }

        Thread.sleep(500);

        assertEquals(u1.getReceivedData().size(), 127*3 + 1);

        for (int i=0; i<=127*3; i++) {
            assertEquals(u1.getReceivedData().get(i), new byte[]{2, (byte)i, (byte)i, (byte)i, (byte)i});
        }

        Thread.sleep(100);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

        Thread.sleep(100);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);

        assertEquals(conn2.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
    }

    // instantiates connection using proxy class
    private class SccpStackImplConnProxy extends SccpStackImpl {
        private boolean akAutoSending = true;

        public SccpStackImplConnProxy(Scheduler scheduler, String name) {
            super(scheduler, name);
        }

        protected SccpConnectionImpl newConnection(int localSsn, ProtocolClass protocol) throws MaxConnectionCountReached {
            SccpConnectionImpl conn;
            Integer refNumber;
            synchronized (this) {
                refNumber = newReferenceNumber();

                if (protocol.getProtocolClass() != 3) {
                    throw new IllegalArgumentException();
                }
                conn = new SccpConnectionWithFlowControlImplProxy(localSsn, new LocalReferenceImpl(refNumber), protocol,
                        this, sccpRoutingControl, akAutoSending);

                connections.put(refNumber, conn);
            }
            return conn;
        }

        public void setAkAutoSending(boolean akAutoSending) {
            this.akAutoSending = akAutoSending;
        }
    }

    // allows to block automatic AK message sending and instead updates status 'AK should be sent'
    private class SccpConnectionWithFlowControlImplProxy extends SccpConnectionWithFlowControlImpl {

        public SccpConnectionWithFlowControlImplProxy(int localSsn, LocalReference localReference, ProtocolClass protocol,
                                                      SccpStackImpl stack, SccpRoutingControl sccpRoutingControl,
                                                      boolean akAutoSending) {
            super(localSsn, localReference, protocol, stack, sccpRoutingControl);
            this.windows = new FlowControlWindowsProxy(stack.name, connectionLock, akAutoSending);
        }

        protected void sendAk(Credit credit) throws Exception {
            super.sendAk(credit);
            ((FlowControlWindowsProxy)this.windows).resetShouldSendAk();
        }

        public boolean isShouldSendAk() {
            try {
                connectionLock.lock();
                return ((FlowControlWindowsProxy)windows).isShouldSendAk();
            } finally {
                connectionLock.unlock();
            }
        }

        public void sendAk() throws Exception {
            super.sendAk();
        }
    }

    // allows to block automatic AK message sending
    private class FlowControlWindowsProxy extends FlowControlWindows {
        private boolean shouldSendAk;
        private boolean akAutoSending;

        public FlowControlWindowsProxy(String name, ReentrantLock lock, boolean akAutoSending) {
            super(name, lock);
            this.akAutoSending = akAutoSending;
        }

        public SendSequenceNumberHandlingResult handleSequenceNumbers(Byte sendSequenceNumber, byte receiveSequenceNumber) {
            try {
                windowLock.lock();

                SendSequenceNumberHandlingResult result = super.handleSequenceNumbers(sendSequenceNumber, receiveSequenceNumber);
                shouldSendAk = result.isAkNeeded();
                return new SendSequenceNumberHandlingResult(result.isResetNeeded(), result.getResetCause(),
                        akAutoSending && result.isAkNeeded());

            } finally {
                windowLock.unlock();
            }
        }

        public boolean isShouldSendAk() {
            try {
                windowLock.lock();
                return shouldSendAk;
            } finally {
                windowLock.unlock();
            }
        }

        public void resetShouldSendAk() {
            try {
                windowLock.lock();
                this.shouldSendAk = false;
            } finally {
                windowLock.unlock();
            }
        }
    }
}
