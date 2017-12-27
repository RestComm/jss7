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
import org.mobicents.protocols.ss7.sccp.SccpConnection;
import org.mobicents.protocols.ss7.sccp.SccpConnectionState;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnDt2MessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpConnRlsdMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SequenceNumberImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.ReleaseCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.scheduler.Clock;
import org.mobicents.protocols.ss7.scheduler.DefaultClock;
import org.mobicents.protocols.ss7.scheduler.Scheduler;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mobicents.protocols.ss7.sccp.SccpConnectionState.CLOSED;
import static org.mobicents.protocols.ss7.sccp.parameter.ErrorCauseValue.LRN_MISMATCH_INCONSISTENT_SOURCE_LRN;
import static org.mobicents.protocols.ss7.sccp.parameter.ReleaseCauseValue.SCCP_FAILURE;
import static org.mobicents.protocols.ss7.sccp.parameter.ReleaseCauseValue.SUBSYSTEM_FAILURE;
import static org.testng.Assert.assertEquals;

public class ConnectionErrorTest extends SccpHarness {

    private SccpAddress a1, a2;
    private Clock clock;
    private Scheduler scheduler1;
    private Scheduler scheduler2;

    public ConnectionErrorTest() {
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
        this.sccpStack1Name = "ConnectionErrorTestStack1";
        this.sccpStack2Name = "ConnectionErrorTestStack2";
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
        SccpStackImpl stack = new SccpStackImpl(scheduler, name);
        final String dir = Util.getTmpTestDir();
        if (dir != null) {
            stack.setPersistDir(dir);
        }
        return stack;
    }

    @BeforeMethod
    public void setUp(Object[] testArgs) throws Exception {
        boolean onlyOneStack = false;
        if (testArgs.length > 0) {
            onlyOneStack = (Boolean) testArgs[0];
        }
        this.onlyOneStack = onlyOneStack;

        mtp3UserPart1 = new Mtp3UserPartImpl(this);
        mtp3UserPart2 = new Mtp3UserPartImpl(this);

        super.setUp();

        if (onlyOneStack) {
            sccpStack2 = sccpStack1;
            sccpProvider2 = sccpProvider1;
            sccpStack2Name = sccpStack1Name;
            ssn2 = 7;

            mtp3UserPart1.setOtherPart(mtp3UserPart1);
            mtp3UserPart1.addDpc(getStack1PC());

            sccpStack1.setMtp3UserPart(1, mtp3UserPart1);
        } else {
            mtp3UserPart1.setOtherPart(mtp3UserPart2);
            mtp3UserPart2.setOtherPart(mtp3UserPart1);

            sccpStack1.setMtp3UserPart(1, mtp3UserPart1);
            sccpStack2.setMtp3UserPart(1, mtp3UserPart2);
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

    protected int getStack2PC() {
        if (onlyOneStack) {
            return getStack1PC();
        }

        if (sccpStack1.getSccpProtocolVersion() == SccpProtocolVersion.ANSI)
            return 8000002;
        else
            return 2;
    }

    private void stackParameterInit() {
        sccpStack1.referenceNumberCounter = 20;
        sccpStack2.referenceNumberCounter = 50;

        sccpStack1.iasTimerDelay = 7500 * 60;
        sccpStack1.iarTimerDelay = 16000 * 60;
        sccpStack2.iasTimerDelay = 7500 * 60;
        sccpStack2.iarTimerDelay = 16000 * 60;

        sccpStack1.sstTimerDuration_Min = 10000;
        sccpStack2.sstTimerDuration_Min = 10000;

        sccpStack1.relTimerDelay = 15000;
        sccpStack1.repeatRelTimerDelay = 15000;
        sccpStack1.intTimerDelay = 30000;

        sccpStack2.relTimerDelay = 15000;
        sccpStack2.repeatRelTimerDelay = 15000;
        sccpStack2.intTimerDelay = 30000;
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testErrProtocolClass2(boolean onlyOneStack) throws Exception {
        stackParameterInit();
        testErr(new ProtocolClassImpl(2));
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testErrProtocolClass3(boolean onlyOneStack) throws Exception {
        stackParameterInit();
        testErr(new ProtocolClassImpl(3));
    }

    public void testErr(ProtocolClass protocolClass) throws Exception {
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(protocolClass);
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(getSSN(), protocolClass);
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertBothConnectionsExist();
        SccpConnection conn2 = getConn2();

        Thread.sleep(100);

        SccpConnRlsdMessageImpl msg = new SccpConnRlsdMessageImpl(conn1.getSls(), conn1.getLocalSsn());
        msg.setDestinationLocalReferenceNumber(conn1.getRemoteReference());
        msg.setReleaseCause(new ReleaseCauseImpl(ReleaseCauseValue.END_USER_ORIGINATED));
        msg.setSourceLocalReferenceNumber(new LocalReferenceImpl(100)); // wrong number
        msg.setUserData(new byte[] {1, 1, 1, 1, 1});
        msg.setOutgoingDpc(getStack2PC());

        sccpStack1.sccpRoutingControl.sendConn(msg);

        Thread.sleep(200);

        if (!onlyOneStack) {
            assertEquals(sccpStack1.getConnectionsNumber(), 0);
            assertEquals(sccpStack2.getConnectionsNumber(), 1);
        } else {
            assertEquals(sccpStack1.getConnectionsNumber(), 1);
        }

        assertEquals(conn1.getState(), CLOSED);
        assertEquals(conn2.getState(), SccpConnectionState.ESTABLISHED); // will be closed later due to no messages received timeout
        assertEquals(u1.getStats().getDisconnectError().getValue(), LRN_MISMATCH_INCONSISTENT_SOURCE_LRN);
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testReleaseDueToErrorProtocolClass2(boolean onlyOneStack) throws Exception {
        stackParameterInit();
        testReleaseDueToError(new ProtocolClassImpl(2));
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testReleaseDueToErrorProtocolClass3(boolean onlyOneStack) throws Exception {
        stackParameterInit();
        testReleaseDueToError(new ProtocolClassImpl(3));
    }

    public void testReleaseDueToError(ProtocolClass protocolClass) throws Exception {
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(protocolClass);
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(getSSN(), protocolClass);
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertBothConnectionsExist();
        SccpConnection conn2 = sccpProvider2.getConnections().values().iterator().next();

        u2.deregister(); // there will be no listener for SSN=8 and this will cause error when receiving next messages

        Thread.sleep(100);

        conn1.send(new byte[] {1, 1, 1, 1, 1});

        Thread.sleep(200);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);

        assertEquals(conn1.getState(), CLOSED);
        assertEquals(conn2.getState(), CLOSED); // will be closed later due to no messages received timeout
        assertEquals(u1.getStats().getReleaseCause().getValue(), SUBSYSTEM_FAILURE);
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testSendDataNoConnectionProtocolClass2(boolean onlyOneStack) throws Exception {
        stackParameterInit();
        testSendDataNoConnection(new ProtocolClassImpl(2));
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testSendDataNoConnectionProtocolClass3(boolean onlyOneStack) throws Exception {
        stackParameterInit();
        testSendDataNoConnection(new ProtocolClassImpl(3));
    }

    private void testSendDataNoConnection(ProtocolClass protocolClass) throws Exception {
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(protocolClass);
        if (protocolClass.getProtocolClass() == 3) {
            crMsg.setCredit(new CreditImpl(100));
        }

        SccpConnection conn1 = sccpProvider1.newConnection(getSSN(), protocolClass);
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertBothConnectionsExist();

        sccpStack2.connections.clear();

        Thread.sleep(100);

        conn1.send(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});

        Thread.sleep(100);

        // i. e. message was discarded
        assertTrue(u2.getReceivedData().isEmpty());
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" })
    public void testSendDataChannelIsDownProtocolClass2() throws Exception {
        //no dataProvider = "ConnectionTestDataProvider" because this test needs network
        stackParameterInit();
        testSendDataNoConnection(new ProtocolClassImpl(2));
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" })
    public void testSendDataChannelIsDownProtocolClass3() throws Exception {
        //no dataProvider = "ConnectionTestDataProvider" because this test needs network
        stackParameterInit();
        testSendDataChannelIsDown(new ProtocolClassImpl(3));
    }

    private void testSendDataChannelIsDown(ProtocolClass protocolClass) throws Exception {
        sccpStack1.sstTimerDuration_Min = 100*1000;
        sccpStack2.sstTimerDuration_Min = 100*1000;

        sccpStack1.relTimerDelay = 100;
        sccpStack1.repeatRelTimerDelay = 100;
        sccpStack1.intTimerDelay = 200;

        sccpStack2.relTimerDelay = 100;
        sccpStack2.repeatRelTimerDelay = 100;
        sccpStack2.intTimerDelay = 200;

        sccpStack1.intTimerDelay = 100;
        sccpStack2.intTimerDelay = 100;

        sccpStack2.iarTimerDelay = 500;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(protocolClass);
        if (protocolClass.getProtocolClass() == 3) {
            crMsg.setCredit(new CreditImpl(100));
        }

        SccpConnection conn1 = sccpProvider1.newConnection(getSSN(), protocolClass);
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertBothConnectionsExist();
        SccpConnection conn2 = getConn2();

        sccpStack1.sccpManagement.handleMtp3Pause(getStack2PC());
        sccpStack2.sccpManagement.handleMtp3Pause(getStack1PC());

        Thread.sleep(100);

        conn1.send(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});

        Thread.sleep(500);

        assertEquals(conn1.getState(), CLOSED);
        assertNotNull(u1.getStats().getReleaseCause());
        assertEquals(u1.getStats().getReleaseCause().getValue(), SCCP_FAILURE);

        Thread.sleep(500);
        assertEquals(conn2.getState(), CLOSED);
        assertNotNull(u2.getStats().getReleaseCause());
        assertEquals(u2.getStats().getReleaseCause().getValue(), SCCP_FAILURE);


        assertTrue(u2.getReceivedData().isEmpty());
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testBadSequenceNumberProtocolClass3(boolean onlyOneStack) throws Exception {
        stackParameterInit();
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(100));


        SccpConnection conn1 = sccpProvider1.newConnection(getSSN(), new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        Thread.sleep(200);

        assertBothConnectionsExist();
//        SccpConnection conn2 = sccpProvider2.getConnections().values().iterator().next();

        Thread.sleep(100);

        SccpConnDt2MessageImpl dataMessage = new SccpConnDt2MessageImpl(255, conn1.getSls(), conn1.getLocalSsn());
        dataMessage.setDestinationLocalReferenceNumber(conn1.getRemoteReference());
        dataMessage.setSourceLocalReferenceNumber(conn1.getLocalReference());
        dataMessage.setUserData(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        dataMessage.setMoreData(false);

        ((SccpConnectionBaseImpl)conn1).prepareMessageForSending(dataMessage);
        int originalSendSequenceNumber = dataMessage.getSequencingSegmenting().getSendSequenceNumber().getValue();
        dataMessage.getSequencingSegmenting().setSendSequenceNumber(new SequenceNumberImpl(originalSendSequenceNumber + 1, false));

        ((SccpConnectionBaseImpl) conn1).stack.sccpRoutingControl.routeMssgFromSccpUserConn(dataMessage);
        Thread.sleep(200);

        assertEquals(u1.getResetCount(), 1);
        assertEquals(u2.getResetCount(), 1);
        assertTrue(u2.getReceivedData().isEmpty());
    }

    @org.testng.annotations.Test(groups = { "SccpMessage", "functional.connection" }, dataProvider = "ConnectionTestDataProvider")
    public void testBadSequenceConfirmationProtocolClass3(boolean onlyOneStack) throws Exception {
        stackParameterInit();
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(100));


        SccpConnection conn1 = sccpProvider1.newConnection(getSSN(), new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertBothConnectionsExist();
//        SccpConnection conn2 = sccpProvider2.getConnections().values().iterator().next();

        Thread.sleep(100);

        SccpConnDt2MessageImpl dataMessage = new SccpConnDt2MessageImpl(255, conn1.getSls(), conn1.getLocalSsn());
        dataMessage.setDestinationLocalReferenceNumber(conn1.getRemoteReference());
        dataMessage.setSourceLocalReferenceNumber(conn1.getLocalReference());
        dataMessage.setUserData(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        dataMessage.setMoreData(false);

        ((SccpConnectionBaseImpl)conn1).prepareMessageForSending(dataMessage);
        int originalConfirmationNumber = dataMessage.getSequencingSegmenting().getReceiveSequenceNumber().getValue();
        dataMessage.getSequencingSegmenting().setReceiveSequenceNumber(new SequenceNumberImpl(originalConfirmationNumber + 1, false));

        ((SccpConnectionBaseImpl) conn1).stack.sccpRoutingControl.routeMssgFromSccpUserConn(dataMessage);
        Thread.sleep(200);

        assertEquals(u1.getResetCount(), 1);
        assertEquals(u2.getResetCount(), 1);
        assertTrue(u2.getReceivedData().isEmpty());
    }
}
