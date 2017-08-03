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

package org.mobicents.protocols.ss7.sccp.impl.messageflow;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.SccpConnection;
import org.mobicents.protocols.ss7.sccp.SccpConnectionState;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
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
import org.mobicents.protocols.ss7.sccp.parameter.ReleaseCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.ResetCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ConnectionTest extends SccpHarness {

    private SccpAddress a1, a2;

    public ConnectionTest() {
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
        sccpStack1 = createStack(sccpStack1Name);
        sccpProvider1 = sccpStack1.getSccpProvider();
        sccpStack1.start();
    }

    protected void createStack2() {
        sccpStack2 = createStack(sccpStack2Name);
        sccpProvider2 = sccpStack2.getSccpProvider();
        sccpStack2.start();
    }

    @Override
    protected SccpStackImpl createStack(String name) {
        SccpStackImpl stack = new SccpStackImplProxy(name);
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
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testConnectionEstablish() throws Exception {
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

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
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testConnectionRelease() throws Exception {
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

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
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

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

        assertEquals(1, u1.getResetCount());
        assertEquals(1, u2.getResetCount());
        assertEquals(sccpProvider2.getConnections().values().iterator().next().getState(), SccpConnectionState.ESTABLISHED);
        assertEquals(sccpProvider1.getConnections().values().iterator().next().getState(), SccpConnectionState.ESTABLISHED);
    }

    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testSendDataProtocolClass2() throws Exception {
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

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
        assertEquals(u2.getReceivedData().iterator().next(), new byte[] {1, 2, 3, 4, 5});

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
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

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

        conn1.send(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});

        for (int i=0; i<=127; i++) {
            conn2.send(new byte[]{1, (byte)i, (byte)i, (byte)i, (byte)i});
        }

        conn2.send(new byte[]{1, 2, 3, 4, 5, 6});

        Thread.sleep(100);

        assertEquals(u1.getReceivedData().size(), 129);
        assertEquals(u2.getReceivedData().size(), 1);

        Thread.sleep(200);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

        Thread.sleep(200);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);

        assertEquals(conn2.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
    }


    @Test(groups = { "SccpMessage", "functional.connection" })
    public void testSendSegmentedDataProtocolClass2() throws Exception {
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

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
        assertEquals(u2.getReceivedData().iterator().next(), largeData);

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
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

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
        assertEquals(u2.getReceivedData().iterator().next(), largeData);

        Thread.sleep(100);

        conn1.disconnect(new ReleaseCauseImpl(ReleaseCauseValue.UNQUALIFIED), new byte[] {});

        Thread.sleep(200);

        assertEquals(sccpStack1.getConnectionsNumber(), 0);
        assertEquals(sccpStack2.getConnectionsNumber(), 0);

        assertEquals(conn2.getState(), SccpConnectionState.CLOSED);
        assertEquals(conn1.getState(), SccpConnectionState.CLOSED);
    }
}
