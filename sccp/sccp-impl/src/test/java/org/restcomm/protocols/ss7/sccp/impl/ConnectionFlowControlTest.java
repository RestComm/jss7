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

package org.restcomm.protocols.ss7.sccp.impl;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.Util;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.MaxConnectionCountReached;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.impl.SccpConnectionBaseImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpConnectionImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpConnectionWithFlowControlImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpFlowControl;
import org.restcomm.protocols.ss7.sccp.impl.SccpRoutingControl;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnDt2MessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ResetCauseImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.scheduler.Clock;
import org.restcomm.protocols.ss7.scheduler.DefaultClock;
import org.restcomm.protocols.ss7.scheduler.Scheduler;
import org.testng.annotations.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import static org.restcomm.protocols.ss7.sccp.SccpConnectionState.CLOSED;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ConnectionFlowControlTest extends SccpHarness {

    private static final byte[] DATA0 = {1, 0, 0, 0, 0, 0, 0, 1};
    private static final byte[] DATA1 = {1, 1, 1, 1, 1, 1, 1, 1};
    private static final byte[] DATA2 = {2, 2, 2, 2, 2, 2, 2, 2};
    private static final byte[] DATA3 = {3, 3, 3, 3, 3, 3, 3, 3};
    private static final byte[] DATA4 = {4, 4, 4, 4, 4, 4, 4, 4};
    private static final byte[] DATA42 = {4, 4, 4, 4, 4, 4, 4, 5};
    private static final byte[] DATA5 = {5, 5, 5, 5, 5, 5, 5, 5};
    private static final byte[] DATA6 = {6, 6, 6, 6, 6, 6, 6, 6};

    private SccpAddress a1, a2;
    private Clock clock;
    private Scheduler scheduler1;
    private Scheduler scheduler2;

    public ConnectionFlowControlTest() {
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
    public void setUp(Object[] testArgs) throws Exception {
        boolean onlyOneStack = (Boolean)testArgs[0];
        this.onlyOneStack = onlyOneStack;

        super.setUp();

        if (onlyOneStack) {
            sccpStack2 = sccpStack1;
            sccpProvider2 = sccpProvider1;
            sccpStack2Name = sccpStack1Name;
            ssn2 = 7;
        }
    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();

        // to avoid stack configuration propagation between test cases
//        deleteDir(sccpStack1.getPersistDir());
//        deleteDir(sccpStack2.getPersistDir());
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

        sccpStack1.iasTimerDelay = 7500 * 60;
        sccpStack1.iarTimerDelay = 16000 * 60;
        sccpStack2.iasTimerDelay = 7500 * 60;
        sccpStack2.iarTimerDelay = 16000 * 60;
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testWaitingForWindow(boolean onlyOneStack) throws Exception {
//        this.saveTrafficInFile();
//        this.saveLogFile("aaaa.log");
        stackParameterInit();

        ((SccpStackImplConnProxy)sccpStack1).setAkAutoSending(false);
        ((SccpStackImplConnProxy)sccpStack2).setAkAutoSending(false);

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        int credit = 2;

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a2, a1, null, new ImportanceImpl((byte)1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(credit));

        SccpConnectionWithFlowControlImplProxy conn1 = (SccpConnectionWithFlowControlImplProxy) sccpProvider1
                .newConnection(getSSN(), new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertBothConnectionsExist();

        SccpConnectionWithFlowControlImplProxy conn2 = (SccpConnectionWithFlowControlImplProxy) getConn2();

        Thread.sleep(100);

        conn1.send(DATA0);

        conn2.send(DATA1);
        conn2.send(DATA2);
        conn2.send(DATA3);
        conn2.send(DATA4);

        Thread.sleep(200);

        assertTrue(u1.getReceivedData().size() >= 2);
        assertEquals(u1.getReceivedData().get(0), DATA1);
        assertEquals(u1.getReceivedData().get(1), DATA2);

        //if (!conn1.isPreemptiveAck()) {
        //    assertTrue(conn1.isShouldSendAk());
        //}
        if (conn2.getTransmitQueueSize() != 0) {
            // assertNotEquals(conn2.getTransmitQueueSize(), 0);

            conn1.sendAk();
        }
        Thread.sleep(200);

        assertEquals(u1.getReceivedData().size(), 4);
        assertEquals(u1.getReceivedData().get(2), DATA3);
        assertEquals(u1.getReceivedData().get(3), DATA4);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] { 2, 3, 3, 4, 4, 5, 5 });

        Thread.sleep(200);

        assertEquals(conn1.getState(), CLOSED);
        assertEquals(conn2.getState(), CLOSED);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testLowCredit(boolean onlyOneStack) throws Exception {
        stackParameterInit();

        ((SccpStackImplConnProxy)sccpStack1).setAkAutoSending(true);
        ((SccpStackImplConnProxy)sccpStack2).setAkAutoSending(true);

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        int credit = 2;

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(credit));

        SccpConnectionWithFlowControlImplProxy conn1 = (SccpConnectionWithFlowControlImplProxy) sccpProvider1
                .newConnection(getSSN(), new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertBothConnectionsExist();

        SccpConnectionWithFlowControlImplProxy conn2 = (SccpConnectionWithFlowControlImplProxy) getConn2();

        Thread.sleep(100);

        conn1.send(DATA0);

        conn2.send(DATA1);
        conn2.send(DATA2);
        conn2.send(DATA3);
        conn2.send(DATA4);

        Thread.sleep(200);

        assertEquals(u2.getReceivedData().size(), 1);
        assertEquals(u1.getReceivedData().size(), 4);
        assertEquals(u1.getReceivedData().get(0), DATA1);
        assertEquals(u1.getReceivedData().get(1), DATA2);
        assertEquals(u1.getReceivedData().get(2), DATA3);
        assertEquals(u1.getReceivedData().get(3), DATA4);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

        Thread.sleep(100);

        assertEquals(conn1.getState(), CLOSED);
        assertEquals(conn2.getState(), CLOSED);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testOverloading(boolean onlyOneStack) throws Exception {
        stackParameterInit();

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        int credit = 4;

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(credit));

        SccpConnectionWithFlowControlImplProxy conn1 = (SccpConnectionWithFlowControlImplProxy) sccpProvider1
                .newConnection(getSSN(), new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertBothConnectionsExist();

        SccpConnectionWithFlowControlImplProxy conn2 = (SccpConnectionWithFlowControlImplProxy) getConn2();

        Thread.sleep(100);

        conn1.send(DATA0);
        conn2.send(DATA1);
        conn2.send(DATA2);
        conn2.send(DATA3);
        conn2.send(DATA4);
        conn2.send(DATA42);

        Thread.sleep(500);

        assertEquals(u1.getReceivedData().size(), 5);
        assertEquals(u1.getReceivedData().get(0), DATA1);
        assertEquals(u1.getReceivedData().get(1), DATA2);
        assertEquals(u1.getReceivedData().get(2), DATA3);
        assertEquals(u1.getReceivedData().get(3), DATA4);
        assertEquals(u1.getReceivedData().get(4), DATA42);

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
        assertEquals(u1.getReceivedData().size(), 5);

        conn1.setOverloaded(false);
        Thread.sleep(300);

        assertTrue(!conn1.isShouldSendAk());
        assertEquals(conn2.getTransmitQueueSize(), 0);
        assertEquals(u1.getReceivedData().size(), 7);

        assertEquals(u1.getReceivedData().get(5), DATA5);
        assertEquals(u1.getReceivedData().get(6), DATA6);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

        Thread.sleep(100);

        assertEquals(conn1.getState(), CLOSED);
        assertEquals(conn2.getState(), CLOSED);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);
    }

    @Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testBigCredit(boolean onlyOneStack) throws Exception {
        stackParameterInit();

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        int credit = 127;

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(credit));

        SccpConnection conn1 = sccpProvider1.newConnection(getSSN(), new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertBothConnectionsExist();
        SccpConnection conn2 = getConn2();

        Thread.sleep(100);

        for (int i=0; i<=127*3; i++) {
            conn2.send(new byte[]{2, (byte)i, (byte)i, (byte)i, (byte)i});
        }

        Thread.sleep(1000);

        assertEquals(u1.getReceivedData().size(), 127*3 + 1);

        for (int i=0; i<=127*3; i++) {
            assertEquals(u1.getReceivedData().get(i), new byte[]{2, (byte)i, (byte)i, (byte)i, (byte)i});
        }

        Thread.sleep(100);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

        Thread.sleep(100);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);

        assertEquals(conn2.getState(), CLOSED);
        assertEquals(conn1.getState(), CLOSED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider", timeOut = 1800000)
    public void testBigCreditTwoDirections(boolean onlyOneStack) throws Exception {
        System.gc(); // explicit gc (useful when @Test(invocationCount) is high
        Thread.sleep(3000);

        stackParameterInit();
        // saveLogFile("testBigCreditTwoDirections-log-" + iteration++ + ".txt");

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        int credit = 127;

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a2, a1, new byte[] { 9, 8, 7, 6, 5 }, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(credit));

        SccpConnection conn1 = sccpProvider1.newConnection(getSSN(), new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        while (!isBothConnectionsExist()) {
            Thread.sleep(500);
        }

        assertBothConnectionsExist();
        SccpConnection conn2 = getConn2();

        Thread.sleep(100);

        ReentrantLock starter = new ReentrantLock();
        starter.lock();
        SenderThread sender1 = new SenderThread(starter, conn1, 1);
        SenderThread sender2 = new SenderThread(starter, conn2, 2);
        Thread senderThread1 = new Thread(sender1);
        Thread senderThread2 = new Thread(sender2);
        senderThread1.start();
        senderThread2.start();

        starter.unlock();

        Thread.sleep(100);

        while (!sender1.finished.get() || !sender2.finished.get()) {
            Thread.sleep(100);
        }

        Thread.sleep(1000);

//        while (u1.receivedData.size() != 382) {
//            int iii1 = 01;
//            iii1++;
//            Thread.sleep(200);
//        }
//        while (u2.receivedData.size() != 382) {
//            int iii1 = 01;
//            iii1++;
//            Thread.sleep(200);
//        }


        // timeOut option will timeout test if it took too long
        while (u1.receivedData.size() < 382 || u2.receivedData.size() < 382) {
            Thread.sleep(500);
        }

        assertEquals(u1.receivedData.size(), 382);
        assertEquals(u2.receivedData.size(), 382);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), null);

        while (sccpStack1.getConnectionsNumber() > 0 || sccpStack2.getConnectionsNumber() > 0) {
            Thread.sleep(500);
        }

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);

        while (conn1.getState() != CLOSED || conn2.getState() != CLOSED) {
            Thread.sleep(500);
        }

        assertEquals(conn2.getState(), CLOSED);
        assertEquals(conn1.getState(), CLOSED);

        assertFalse(sender1.failed);
        assertFalse(sender2.failed);
    }

    public static class SenderThread implements Runnable {
        private final Logger logger;
        private ReentrantLock starter;
        private SccpConnection conn;
        private byte workerNumber;
        private boolean failed;
        private Exception failedException;
        private AtomicBoolean finished = new AtomicBoolean(false);

        public SenderThread(ReentrantLock starter, SccpConnection conn, int workerNumber) {
            this.starter = starter;
            this.conn = conn;
            this.workerNumber = (byte) workerNumber;
            this.logger = Logger.getLogger(SenderThread.class.getCanonicalName()
                    + "-" + conn.getLocalReference()
                    + "-" + workerNumber
                    + "-" + ((SccpConnectionBaseImpl)conn).stack.name);
        }

        @Override
        public void run() {
            while (starter.isLocked()) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    failed = true;
                    failedException = e;
                    return;
                }
            }
            for (int i=0; i<=127*3; i++) {
                try {
                    conn.send(new byte[]{workerNumber, (byte)i, (byte)i, (byte)i, (byte)i});
                } catch (Exception e) {
                    failed = true;
                    failedException = e;
                    return;
                }
            }
            finished.set(true);
        }
    }

    @Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testSendDataAfterReset(boolean onlyOneStack) throws Exception {
        stackParameterInit();
        this.saveLogFile("~/conn-log.txt");

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertBothConnectionsExist();
        SccpConnection conn2 = getConn2();

        Thread.sleep(100);

        for (int i=0; i<=127*2; i++) {
            byte val = (byte)(i % 128);
            conn1.send(new byte[]{1, val, val, val, val});
        }

        for (int i=0; i<=127*2; i++) {
            byte val = (byte)(i % 128);
            conn2.send(new byte[]{2, val, val, val, val});
        }

        Thread.sleep(1600);

        conn1.reset(new ResetCauseImpl(ResetCauseValue.END_USER_ORIGINATED));
        Thread.sleep(500);

        for (int i=0; i<=127*2; i++) {
            byte val = (byte)(i % 128);
            conn1.send(new byte[]{1, val, val, val, val});
        }

        for (int i=0; i<=127*2; i++) {
            byte val = (byte)(i % 128);
            conn2.send(new byte[]{2, val, val, val, val});
        }

        Thread.sleep(1600);

        assertEquals(u1.getReceivedData().size(), 127*4 + 2);
        for (int i=0; i<=127*2; i++) {
            byte val = (byte)(i % 128);
            assertEquals(u1.getReceivedData().get(i), new byte[] {2, val, val, val, val});
        }
        for (int i=0; i<=127*2; i++) {
            byte val = (byte)(i % 128);
            assertEquals(u1.getReceivedData().get(127*2+1+i), new byte[] {2, val, val, val, val});
        }

        assertEquals(u2.getReceivedData().size(), 127*4 + 2);
        for (int i=0; i<=127*2; i++) {
            byte val = (byte)(i % 128);
            assertEquals(u2.getReceivedData().get(i), new byte[] {1, val, val, val, val});
        }
        for (int i=0; i<=127*2; i++) {
            byte val = (byte)(i % 128);
            assertEquals(u2.getReceivedData().get(127*2+1+i), new byte[] {1, val, val, val, val});
        }

        Thread.sleep(200);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), null);

        Thread.sleep(200);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);

        assertEquals(conn2.getState(), CLOSED);
        assertEquals(conn1.getState(), CLOSED);
    }

    // instantiates connection using proxy class
    private class SccpStackImplConnProxy extends SccpStackImpl {
        private boolean akAutoSending = true;

        public SccpStackImplConnProxy(Scheduler scheduler, String name) {
            super(scheduler, name);
        }

        protected SccpConnectionImpl newConnection(int localSsn, ProtocolClass protocol) throws MaxConnectionCountReached {
            SccpConnectionImpl conn;
            Integer refNumber = newReferenceNumber();

            if (protocol.getProtocolClass() != 3) {
                throw new IllegalArgumentException();
            }
            conn = new SccpConnectionWithFlowControlImplProxy(localSsn, new LocalReferenceImpl(refNumber), protocol,
                    this, sccpRoutingControl, akAutoSending);

            connections.put(refNumber, conn);
            return conn;
        }

        public void setAkAutoSending(boolean akAutoSending) {
            this.akAutoSending = akAutoSending;
        }
    }

    // allows to block automatic AK message sending and instead updates status 'AK should be sent'
    private class SccpConnectionWithFlowControlImplProxy extends SccpConnectionWithFlowControlImpl {

        private boolean akAutoSending;

        public SccpConnectionWithFlowControlImplProxy(int localSsn, LocalReference localReference, ProtocolClass protocol,
                                                      SccpStackImpl stack, SccpRoutingControl sccpRoutingControl,
                                                      boolean akAutoSending) {
            super(localSsn, localReference, protocol, stack, sccpRoutingControl);
            this.akAutoSending = akAutoSending;
        }

        protected SccpFlowControl newSccpFlowControl(Credit credit) {
            return new SccpFlowControlProxy(stack.name, credit.getValue(), connectionLock, akAutoSending);
        }

        @Override
        protected void sendAk(Credit credit) throws Exception {
            super.sendAk(credit);
            ((SccpFlowControlProxy)this.flow).resetShouldSendAk();
        }

        public boolean isShouldSendAk() {
            try {
                connectionLock.lock();
                return ((SccpFlowControlProxy)flow).isShouldSendAk();
            } finally {
                connectionLock.unlock();
            }
        }

        public void sendAk() throws Exception {
            super.sendAk();
        }
    }

    // allows to block automatic AK message sending
    private class SccpFlowControlProxy extends SccpFlowControl {
        private boolean shouldSendAk;
        private boolean akAutoSending;

        public SccpFlowControlProxy(String name, int maximumWindowSize, ReentrantLock lock, boolean akAutoSending) {
            super(name, maximumWindowSize);
            this.akAutoSending = akAutoSending;
        }

        @Override
        public boolean isAkSendCriterion(SccpConnDt2MessageImpl msg) {

            boolean value = super.isAkSendCriterion(msg);
            shouldSendAk = value;

            return akAutoSending && value;
        }

        public boolean isShouldSendAk() {
            return shouldSendAk;
        }

        public void resetShouldSendAk() {
            this.shouldSendAk = false;
        }
    }
}
