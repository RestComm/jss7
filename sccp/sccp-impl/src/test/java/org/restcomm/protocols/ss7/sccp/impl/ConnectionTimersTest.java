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

import org.restcomm.protocols.ss7.Util;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.MaxConnectionCountReached;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpConnectionState;
import org.restcomm.protocols.ss7.sccp.impl.SccpConnectionImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpConnectionWithFlowControlImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpRoutingControl;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnItMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpConnMessage;
import org.restcomm.protocols.ss7.sccp.parameter.LocalReference;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.scheduler.Clock;
import org.restcomm.protocols.ss7.scheduler.DefaultClock;
import org.restcomm.protocols.ss7.scheduler.Scheduler;
import org.testng.annotations.*;

import static junit.framework.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

public class ConnectionTimersTest extends SccpHarness {

    private SccpAddress a1, a2;
    private Clock clock;
    private Scheduler scheduler1;
    private Scheduler scheduler2;

    public ConnectionTimersTest() {
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
        this.sccpStack1Name = "ConnectionTimersTestStack1";
        this.sccpStack2Name = "ConnectionTimersTestStack2";
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

        sccpStack1.relTimerDelay = 15000;
        sccpStack2.relTimerDelay = 15000;
        sccpStack1.repeatRelTimerDelay = 15000;
        sccpStack2.repeatRelTimerDelay = 15000;

        sccpStack1.intTimerDelay = 30000;
        sccpStack2.intTimerDelay = 30000;
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testInactivityProtocolClass2(boolean onlyOneStack) throws Exception {
        stackParameterInit();
        testInactivity(new ProtocolClassImpl(2));
    }

    @Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testInactivityProtocolClass3(boolean onlyOneStack) throws Exception {
        stackParameterInit();
        testInactivity(new ProtocolClassImpl(3));
    }

    private void testInactivity(ProtocolClass protocolClass) throws Exception {
        sccpStack1.iasTimerDelay = 100;
        sccpStack2.iasTimerDelay = 100;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(protocolClass);
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, protocolClass);
        conn1.establish(crMsg);

        Thread.sleep(500);

        assertBothConnectionsExist();
        SccpConnection conn2 = getConn2();

        Thread.sleep(400);

        assertEquals(conn1.getState(), SccpConnectionState.ESTABLISHED);
        assertEquals(conn2.getState(), SccpConnectionState.ESTABLISHED);
        assertTrue(((ScppConnectionWithStats)conn1).getReceivedItMessagesCount() > 0);
        assertTrue(((ScppConnectionWithStats)conn2).getReceivedItMessagesCount() > 0);
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testRlsdRepeatProtocolClass2(boolean onlyOneStack) throws Exception {
        stackParameterInit();
        testRlsdRepeat(new ProtocolClassImpl(2));
    }

    @Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testRlsdRepeatProtocolClass3(boolean onlyOneStack) throws Exception {
        stackParameterInit();
        testRlsdRepeat(new ProtocolClassImpl(3));
    }

    private void testRlsdRepeat(ProtocolClass protocolClass) throws Exception {
        sccpStack1.relTimerDelay = 100;
        sccpStack1.repeatRelTimerDelay = 100;
        sccpStack1.intTimerDelay = 1500;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(protocolClass);
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, protocolClass);
        conn1.establish(crMsg);

        Thread.sleep(200);

        assertBothConnectionsExist();
        SccpConnection conn2 = getConn2();

        Thread.sleep(200);

        ((ScppConnectionWithStats)conn2).setSkipRlsd(true);
        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.END_USER_ORIGINATED), new byte[] {});

        Thread.sleep(500);
        assertTrue(((ScppConnectionWithStats)conn2).getReceivedRlsdMessagesCount() > 2);

        Thread.sleep(2000);
        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn2.getState(), SccpConnectionState.ESTABLISHED); // haven't closed due to inactivity yet
    }

    // instantiates connection using proxy class
    private class SccpStackImplConnProxy extends SccpStackImpl {

        public SccpStackImplConnProxy(Scheduler scheduler, String name) {
            super(scheduler, name);
        }

        protected SccpConnectionImpl newConnection(int localSsn, ProtocolClass protocol) throws MaxConnectionCountReached {
            SccpConnectionImpl conn;
            Integer refNumber = newReferenceNumber();

            if (protocol.getProtocolClass() == 2) {
                conn = new SccpConnectionImplProxy(localSsn, new LocalReferenceImpl(refNumber), protocol, this, sccpRoutingControl);
            } else if (protocol.getProtocolClass() == 3) {
                conn = new SccpConnectionWithFlowControlImplProxy(localSsn, new LocalReferenceImpl(refNumber), protocol, this, sccpRoutingControl);
            } else {
                logger.error(String.format("Unsupported connection class %d", protocol.getProtocolClass()));
                throw new IllegalArgumentException();
            }

            connections.put(refNumber, conn);
            return conn;
        }

        @Override
        protected void removeConnection(LocalReference ref) {
            SccpConnectionImpl connection = getConnection(ref);
            if (!((ScppConnectionWithStats)connection).isSkipRlsd()) {
                super.removeConnection(ref);
            }
        }
    }

    private class SccpConnectionWithFlowControlImplProxy extends SccpConnectionWithFlowControlImpl implements ScppConnectionWithStats {
        private int receivedItMessages;
        private boolean skipRlsd = false;
        private int receivedRlsdMessages;

        public SccpConnectionWithFlowControlImplProxy(int localSsn, LocalReference localReference, ProtocolClass protocol,
                                                      SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
            super(localSsn, localReference, protocol, stack, sccpRoutingControl);
        }

        protected void receiveMessage(SccpConnMessage message) throws Exception {
            super.receiveMessage(message);
            if (message instanceof SccpConnItMessageImpl) {
                receivedItMessages++;
            }
        }

        @Override
        public int getReceivedItMessagesCount() {
            return receivedItMessages;
        }

        @Override
        protected void confirmRelease() throws Exception {
            receivedRlsdMessages++;
            if (!skipRlsd) {
                super.confirmRelease();
            }
        }

        @Override
        public int getReceivedRlsdMessagesCount() {
            return receivedRlsdMessages;
        }

        @Override
        public void setSkipRlsd(boolean skipRlsd) {
            this.skipRlsd = skipRlsd;
        }

        @Override
        public boolean isSkipRlsd() {
            return skipRlsd;
        }
    }

    private class SccpConnectionImplProxy extends SccpConnectionImpl implements ScppConnectionWithStats {
        private int receivedItMessages;
        private boolean skipRlsd = false;
        private int receivedRlsdMessages;

        protected void receiveMessage(SccpConnMessage message) throws Exception {
            super.receiveMessage(message);
            if (message instanceof SccpConnItMessageImpl) {
                receivedItMessages++;
            }
        }

        public SccpConnectionImplProxy(int localSsn, LocalReference localReference, ProtocolClass protocol,
                                                      SccpStackImpl stack, SccpRoutingControl sccpRoutingControl) {
            super(localSsn, localReference, protocol, stack, sccpRoutingControl);
        }

        @Override
        public int getReceivedItMessagesCount() {
            return receivedItMessages;
        }

        @Override
        protected void confirmRelease() throws Exception {
            receivedRlsdMessages++;
            if (!skipRlsd) {
                super.confirmRelease();
            }
        }

        @Override
        public int getReceivedRlsdMessagesCount() {
            return receivedRlsdMessages;
        }

        @Override
        public void setSkipRlsd(boolean skipRlsd) {
            this.skipRlsd = skipRlsd;
        }

        @Override
        public boolean isSkipRlsd() {
            return skipRlsd;
        }
    }

    private interface ScppConnectionWithStats {
        int getReceivedItMessagesCount();
        int getReceivedRlsdMessagesCount();
        void setSkipRlsd(boolean skipRlsd);
        boolean isSkipRlsd();
    }
}
