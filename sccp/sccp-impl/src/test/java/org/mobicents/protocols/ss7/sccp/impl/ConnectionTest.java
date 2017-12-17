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
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.SccpConnection;
import org.mobicents.protocols.ss7.sccp.SccpConnectionState;
import org.mobicents.protocols.ss7.sccp.impl.parameter.BCDEvenEncodingScheme;
import org.mobicents.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ResetCauseImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpConnCrMessage;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
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

import static org.testng.Assert.assertEquals;

public class ConnectionTest extends SccpHarness {

    private SccpAddress a1, a2;
    private Clock clock;
    private Scheduler scheduler1;
    private Scheduler scheduler2;

    public ConnectionTest() {
        clock = new DefaultClock();
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "MessageTransferTestStack1";
        this.sccpStack2Name = "MessageTransferTestStack2";
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
        SccpStackImpl stack = new SccpStackImpl(scheduler, name);
        final String dir = Util.getTmpTestDir();
        if (dir != null) {
            stack.setPersistDir(dir);
        }
        return stack;
    }

    @BeforeMethod
    public void setUp() throws Exception {
        ssn2 = 6;
        super.setUp();

        GlobalTitle gtPattern = super.parameterFactory.createGlobalTitle("*", 0, NumberingPlan.ISDN_TELEPHONY, new BCDEvenEncodingScheme(), NatureOfAddress.INTERNATIONAL);
        SccpAddress pattern = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gtPattern, 0, 0);

        GlobalTitle gtRa11 = super.parameterFactory.createGlobalTitle("1111", 0, NumberingPlan.ISDN_TELEPHONY, new BCDEvenEncodingScheme(), NatureOfAddress.INTERNATIONAL);
        SccpAddress routingAddress11 = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gtRa11, 2, 0);
        SccpAddress routingAddress12 = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gtRa11, 1, 0);
        router1.addRoutingAddress(1, routingAddress11);
        router1.addRoutingAddress(2, routingAddress12);
        router1.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 1, -1, null, 0, null);
        router1.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE, pattern, "K", 2, -1, null, 0, null);

        SccpAddress routingAddress21 = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gtRa11, 1, 0);
        SccpAddress routingAddress22 = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gtRa11, 2, 0);
        router2.addRoutingAddress(1, routingAddress21);
        router2.addRoutingAddress(2, routingAddress22);
        router2.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 1, -1, null, 0, null);
        router2.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE, pattern, "K", 2, -1, null, 0, null);
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

        sccpStack1.sstTimerDuration_Min = 10000;
        sccpStack2.sstTimerDuration_Min = 10000;

        sccpStack1.relTimerDelay = 15000;
        sccpStack1.repeatRelTimerDelay = 15000;
        sccpStack1.intTimerDelay = 30000;

        sccpStack2.relTimerDelay = 15000;
        sccpStack2.repeatRelTimerDelay = 15000;
        sccpStack2.intTimerDelay = 30000;
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testConnectionEstablish() throws Exception {
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
        crMsg.setProtocolClass(new ProtocolClassImpl(2));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(getSSN(), new ProtocolClassImpl(2));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);

        assertEquals(sccpProvider2.getConnections().values().iterator().next().getState(), SccpConnectionState.ESTABLISHED);
        assertEquals(sccpProvider1.getConnections().values().iterator().next().getState(), SccpConnectionState.ESTABLISHED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testConnectionEstablish_GT() throws Exception {
        stackParameterInit();

        GlobalTitle gt1 = sccpProvider1.getParameterFactory().createGlobalTitle("111111", 0, NumberingPlan.ISDN_TELEPHONY,
                null, NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt2 = sccpProvider1.getParameterFactory().createGlobalTitle("222222", 0, NumberingPlan.ISDN_TELEPHONY,
                null, NatureOfAddress.INTERNATIONAL);
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 0, getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, 0, getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(getSSN(), a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        // crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(2));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(getSSN(), new ProtocolClassImpl(2));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);

        assertEquals(sccpProvider2.getConnections().values().iterator().next().getState(), SccpConnectionState.ESTABLISHED);
        assertEquals(sccpProvider1.getConnections().values().iterator().next().getState(), SccpConnectionState.ESTABLISHED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testConnectionEstablishWithConfirmData() throws Exception {
        stackParameterInit();

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();
        u2.getOptions().setSendConfirmData(new byte[] {1, 2, 3});

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(2));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(2));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);

        assertEquals(sccpProvider2.getConnections().values().iterator().next().getState(), SccpConnectionState.ESTABLISHED);
        assertEquals(sccpProvider1.getConnections().values().iterator().next().getState(), SccpConnectionState.ESTABLISHED);

        assertEquals(u1.getReceivedData().size(), 1);
        assertEquals(u1.getReceivedData().iterator().next(), new byte[] {1, 2, 3});
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testConnectionRelease() throws Exception {
        stackParameterInit();

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);


        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(2));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(2));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);
        SccpConnection conn2 = sccpProvider2.getConnections().values().iterator().next();

        Thread.sleep(100);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

        Thread.sleep(200);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);

        assertEquals(conn2.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testConnectionReset() throws Exception {
        stackParameterInit();

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(2));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(2));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);

        assertEquals(sccpProvider2.getConnections().values().iterator().next().getState(), SccpConnectionState.ESTABLISHED);
        assertEquals(sccpProvider1.getConnections().values().iterator().next().getState(), SccpConnectionState.ESTABLISHED);

        conn1.reset(new ResetCauseImpl(ResetCauseValue.UNQUALIFIED));
        Thread.sleep(100);

        assertEquals(u1.getResetCount(), 1);
        assertEquals(u2.getResetCount(), 1);
        assertEquals(sccpProvider2.getConnections().values().iterator().next().getState(), SccpConnectionState.ESTABLISHED);
        assertEquals(sccpProvider1.getConnections().values().iterator().next().getState(), SccpConnectionState.ESTABLISHED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testConnectionRefuse() throws Exception {
        stackParameterInit();

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());
        u2.setRefuseConnections(true);

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(2));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(2));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(u1.getRefusedCount(), 1);
        assertEquals(u2.getRefusedCount(), 1);

        assertEquals(sccpStack2.getConnectionsNumber(), 0);
        assertEquals(sccpStack1.getConnectionsNumber(), 0);

        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testConnectionInactivityKeepAliveProtocolClass2() throws Exception {
        stackParameterInit();
        testConnectionInactivityKeepAlive(new ProtocolClassImpl(2));
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testConnectionInactivityKeepAliveProtocolClass3() throws Exception {
        stackParameterInit();
        testConnectionInactivityKeepAlive(new ProtocolClassImpl(3));
    }

    private void testConnectionInactivityKeepAlive(ProtocolClass protocolClass) throws Exception {
        sccpStack1.iasTimerDelay = 300;
        sccpStack2.iarTimerDelay = 1200;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(protocolClass);
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, protocolClass);
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);

        Thread.sleep(300);

        assertEquals(sccpProvider2.getConnections().values().iterator().next().getState(), SccpConnectionState.ESTABLISHED);
        assertEquals(sccpProvider1.getConnections().values().iterator().next().getState(), SccpConnectionState.ESTABLISHED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testConnectionInactivityReleaseProtocolClass2() throws Exception {
        stackParameterInit();
        testConnectionInactivityRelease(new ProtocolClassImpl(2));
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testConnectionInactivityReleaseProtocolClass3() throws Exception {
        stackParameterInit();
        testConnectionInactivityRelease(new ProtocolClassImpl(3));
    }

    private void testConnectionInactivityRelease(ProtocolClass protocolClass) throws Exception {
        sccpStack1.iasTimerDelay = 1200;
        sccpStack2.iarTimerDelay = 300;

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(protocolClass);
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(2));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);
        SccpConnection conn2 = sccpProvider2.getConnections().values().iterator().next();

        Thread.sleep(300);

        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn2.getState(), SccpConnectionState.CLOSED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testSendDataProtocolClass2() throws Exception {
        stackParameterInit();
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(2));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(2));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);
        SccpConnection conn2 = sccpProvider2.getConnections().values().iterator().next();

        Thread.sleep(100);

        conn1.send(new byte[]{1, 2, 3, 4, 5});

        Thread.sleep(100);

        assertEquals(u2.getReceivedData().size(), 1);
        assertEquals(u2.getReceivedData().iterator().next(), new byte[] {1, 2, 3, 4, 5}); // check if an incoming message content is the same as was sent

        Thread.sleep(100);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

        Thread.sleep(200);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);

        assertEquals(conn2.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testSendDataWhenDlnAndSlnDifferClass2() throws Exception {
        stackParameterInit();

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(2));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(2));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);
        SccpConnection conn2 = sccpProvider2.getConnections().values().iterator().next();

        Thread.sleep(100);

        conn1.send(new byte[]{1, 2, 3, 4, 5});

        Thread.sleep(100);

        assertEquals(u2.getReceivedData().size(), 1);
        assertEquals(u2.getReceivedData().iterator().next(), new byte[] {1, 2, 3, 4, 5}); // check if an incoming message content is the same as was sent

        Thread.sleep(100);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

        Thread.sleep(200);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);

        assertEquals(conn2.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testSendDataWhenSsnDifferClass2() throws Exception {
        stackParameterInit();

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 15);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 16);

        resource1.removeRemoteSsn(1);
        resource1.addRemoteSsn(1, getStack2PC(), a2.getSubsystemNumber(), 0, false);

        resource2.removeRemoteSsn(1);
        resource2.addRemoteSsn(1, getStack1PC(), a1.getSubsystemNumber(), 0, false);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, a1.getSubsystemNumber());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, a2.getSubsystemNumber());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(a1.getSubsystemNumber(), a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(2));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(a1.getSubsystemNumber(), new ProtocolClassImpl(2));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);
        SccpConnection conn2 = sccpProvider2.getConnections().values().iterator().next();

        Thread.sleep(100);

        conn1.send(new byte[]{1, 2, 3, 4, 5});

        Thread.sleep(100);

        assertEquals(u2.getReceivedData().size(), 1);
        assertEquals(u2.getReceivedData().iterator().next(), new byte[] {1, 2, 3, 4, 5}); // check if an incoming message content is the same as was sent

        Thread.sleep(100);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

        Thread.sleep(200);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);

        assertEquals(conn2.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testSendDataProtocolClass3() throws Exception {
        this.saveTrafficInFile();
        stackParameterInit();

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] { 0x51, 0x53, 0x55, 0x57 }, new ImportanceImpl((byte)1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);
        SccpConnection conn2 = sccpProvider2.getConnections().values().iterator().next();

        Thread.sleep(100);

        conn1.send(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});

        for (int i=0; i<=127; i++) {
            conn2.send(new byte[]{1, (byte)i, (byte)i, (byte)i, (byte)i});
        }

        conn2.send(new byte[]{1, 2, 3, 4, 5, 6});

        Thread.sleep(1600);

        assertEquals(u1.getReceivedData().size(), 129);
        for (int i=0; i<=127; i++) {
            assertEquals(u1.getReceivedData().get(i), new byte[]{1, (byte)i, (byte)i, (byte)i, (byte)i});
        }
        assertEquals(u1.getReceivedData().get(128), new byte[] {1, 2, 3, 4, 5, 6}); // check if an incoming message content is the same as was sent

        assertEquals(u2.getReceivedData().size(), 1);
        assertEquals(u2.getReceivedData().iterator().next(), new byte[] {1, 2, 3, 4, 5, 6, 7, 8}); // check if an incoming message content is the same as was sent

        Thread.sleep(200);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), null);

        Thread.sleep(200);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);

        assertEquals(conn2.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
    }


    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testSendSegmentedDataProtocolClass2() throws Exception {
        stackParameterInit();

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(2));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(2));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);
        SccpConnection conn2 = sccpProvider2.getConnections().values().iterator().next();

        Thread.sleep(100);

        byte[] largeData = new byte[255*3 + 10];
        for (int i = 0; i < 255*3 + 10; i++) {
            largeData[i] = (byte)i;
        }

        conn1.send(largeData);

        Thread.sleep(200);

        assertEquals(u2.getReceivedData().size(), 1);
        assertEquals(u2.getReceivedData().iterator().next(), largeData); //check if an incoming message content is the same as was sent

        Thread.sleep(100);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

        Thread.sleep(200);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);

        assertEquals(conn2.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testSendSegmentedDataProtocolClass3() throws Exception {
        stackParameterInit();

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(100));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);
        SccpConnection conn2 = sccpProvider2.getConnections().values().iterator().next();

        Thread.sleep(100);

        byte[] largeData = new byte[255*3 + 10];
        for (int i = 0; i < 255*3 + 10; i++) {
            largeData[i] = (byte)i;
        }

        conn1.send(largeData);

        Thread.sleep(200);

        assertEquals(u2.getReceivedData().size(), 1);
        assertEquals(u2.getReceivedData().iterator().next(), largeData); //check if an incoming message content is the same as was sent

        Thread.sleep(100);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

        Thread.sleep(200);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);

        assertEquals(conn2.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testIncreaseCreditByUserProtocolClass3() throws Exception {
        stackParameterInit();
        testChangeCreditByUserProtocolClass3(100, 127);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testDecreaseCreditByUserProtocolClass3() throws Exception {
        stackParameterInit();
        testChangeCreditByUserProtocolClass3(127, 100);
    }

    private void testChangeCreditByUserProtocolClass3(int initialCredit, int finalCredit) throws Exception {
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), getSSN());
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), getSSN2());

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN2());

        u1.register();
        u2.register();

        u2.getOptions().confirmCredit = new CreditImpl(finalCredit);

        Thread.sleep(100);

        SccpConnCrMessage crMsg = sccpProvider1.getMessageFactory().createConnectMessageClass2(8, a2, a1, new byte[] {}, new ImportanceImpl((byte)1));
        crMsg.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        crMsg.setProtocolClass(new ProtocolClassImpl(3));
        crMsg.setCredit(new CreditImpl(initialCredit));

        SccpConnection conn1 = sccpProvider1.newConnection(8, new ProtocolClassImpl(3));
        conn1.establish(crMsg);

        Thread.sleep(100);

        assertEquals(sccpStack2.getConnectionsNumber(), 1);
        assertEquals(sccpStack1.getConnectionsNumber(), 1);
        SccpConnection conn2 = sccpProvider2.getConnections().values().iterator().next();

        assertEquals(conn1.getReceiveCredit().getValue(), finalCredit);
        assertEquals(conn1.getSendCredit().getValue(), finalCredit);
        assertEquals(conn2.getReceiveCredit().getValue(), finalCredit);
        assertEquals(conn2.getSendCredit().getValue(), finalCredit);
    }
}
