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
package org.mobicents.protocols.ss7.m3ua.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBufAllocator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javolution.util.FastMap;

import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.AssociationListener;
import org.mobicents.protocols.api.AssociationType;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.api.ManagementEventListener;
import org.mobicents.protocols.api.PayloadData;
import org.mobicents.protocols.api.Server;
import org.mobicents.protocols.api.ServerListener;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.transfer.PayloadDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ProtocolDataImpl;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPActive;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPActiveAck;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.mtp.Mtp3EndCongestionPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3Primitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests for FSM of IPSP acting as CLIENT
 *
 * @author amit bhayani
 *
 */
public class IPSPClientFSMTest {
    private ParameterFactoryImpl parmFactory = new ParameterFactoryImpl();
    private MessageFactoryImpl messageFactory = new MessageFactoryImpl();
    private M3UAManagementImpl clientM3UAMgmt = null;
    private Mtp3UserPartListenerimpl mtp3UserPartListener = null;
    private NettyTransportManagement transportManagement = null;

    private Semaphore semaphore = null;

    public IPSPClientFSMTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws Exception {
        semaphore = new Semaphore(0);
        this.transportManagement = new NettyTransportManagement();
        this.clientM3UAMgmt = new M3UAManagementImpl("IPSPClientFSMTest", null);
        this.clientM3UAMgmt.setTransportManagement(this.transportManagement);
        this.mtp3UserPartListener = new Mtp3UserPartListenerimpl();
        this.clientM3UAMgmt.addMtp3UserPartListener(this.mtp3UserPartListener);
        this.clientM3UAMgmt.start();

    }

    @AfterMethod
    public void tearDown() throws Exception {
        clientM3UAMgmt.removeAllResourses();
        clientM3UAMgmt.stop();
    }

    private AspState getAspState(FSM fsm) {
        return AspState.getState(fsm.getState().getName());
    }

    private AsState getAsState(FSM fsm) {
        return AsState.getState(fsm.getState().getName());
    }

    /**
     * Test with RC Set for Single Exchange message
     *
     * @throws Exception
     */
    @Test
    public void testSingleAspInAsWithRCSE() throws Exception {

        // 5.1.1. Single ASP in an Application Server ("1+0" sparing),
        this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc1");

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

        // As as = rsgw.createAppServer("testas", rc, rKey, trModType);
        AsImpl asImpl = (AsImpl) this.clientM3UAMgmt.createAs("testas", Functionality.IPSP, ExchangeType.SE, null, rc, null, 1,
                null);

        AspFactoryImpl localAspFactory = (AspFactoryImpl) this.clientM3UAMgmt.createAspFactory("testasp", "testAssoc1", false);
        localAspFactory.start();

        AspImpl aspImpl = this.clientM3UAMgmt.assignAspToAs("testas", "testasp");

        // Create Route. Adding 3 routes
        this.clientM3UAMgmt.addRoute(3, -1, -1, "testas");
        this.clientM3UAMgmt.addRoute(2, 10, -1, "testas");
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas");

        // Signal for Communication UP
        TestAssociation testAssociation = (TestAssociation) this.transportManagement.getAssociation("testAssoc1");
        testAssociation.signalCommUp();

        // Once communication is UP, ASP_UP should have been sent.
        FSM aspLocalFSM = aspImpl.getLocalFSM();
        assertEquals(AspState.UP_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));

        // The other side will send ASP_UP_ACK and *no* NTFY(AS-INACTIVE)
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        localAspFactory.read(message);

        assertEquals(AspState.ACTIVE_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));

        FSM asPeerFSM = asImpl.getPeerFSM();
        // also the AS should be INACTIVE now
        assertEquals(AsState.INACTIVE, this.getAsState(asPeerFSM));

        // The other side will send ASP_ACTIVE_ACK and *no* NTFY(AS-ACTIVE)
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        localAspFactory.read(aspActiveAck);

        assertEquals(AspState.ACTIVE, this.getAspState(aspLocalFSM));
        // also the AS should be ACTIVE now
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));

        // Check if MTP3 RESUME received
        // lets wait for 2second to receive the MTP3 primitive before giving up
        semaphore.tryAcquire(2000, TimeUnit.MILLISECONDS);
        // RESUME for DPC 3
        Mtp3Primitive mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(3, mtp3Primitive.getAffectedDpc());

        // lets wait for 2second to receive the MTP3 primitive before giving up
        semaphore.tryAcquire(2000, TimeUnit.MILLISECONDS);
        // RESUME for DPC 2
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());

        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Lets stop ASP Factory
        localAspFactory.stop();

        assertEquals(AspState.DOWN_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));
        // also the AS is PENDING
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));

        // lets wait for 3 seconds to receive the MTP3 primitive before giving up. We know Pending timeout is 2 secs
        semaphore.tryAcquire(3000, TimeUnit.MILLISECONDS);
        // PAUSE for DPC 3
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(3, mtp3Primitive.getAffectedDpc());

        // lets wait for 3 seconds to receive the MTP3 primitive before giving up. We know Pending timeout is 2 secs
        semaphore.tryAcquire(3000, TimeUnit.MILLISECONDS);
        // PAUSE for DPC 2
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());

        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Make sure we don't have any more
        assertNull(testAssociation.txPoll());

    }

    /**
     * Test without RC Set for Single Exchange Message
     *
     * @throws Exception
     */
    @Test
    public void testSingleAspInAsWithoutRCSE() throws Exception {

        // 5.1.1. Single ASP in an Application Server ("1+0" sparing),
        this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc1");

        // As as = rsgw.createAppServer("testas", rc, rKey, trModType);
        AsImpl asImpl = (AsImpl) this.clientM3UAMgmt.createAs("testas", Functionality.IPSP, ExchangeType.SE, null, null, null,
                1, null);

        AspFactoryImpl localAspFactory = (AspFactoryImpl) this.clientM3UAMgmt.createAspFactory("testasp", "testAssoc1", false);
        localAspFactory.start();

        AspImpl aspImpl = clientM3UAMgmt.assignAspToAs("testas", "testasp");

        // Create Route
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas");

        // Signal for Communication UP
        TestAssociation testAssociation = (TestAssociation) this.transportManagement.getAssociation("testAssoc1");
        testAssociation.signalCommUp();

        // Once comunication is UP, ASP_UP should have been sent.
        FSM aspLocalFSM = aspImpl.getLocalFSM();
        assertEquals(AspState.UP_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));

        // The other side will send ASP_UP_ACK and *no* NTFY(AS-INACTIVE)
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        localAspFactory.read(message);

        assertEquals(AspState.ACTIVE_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));

        FSM asPeerFSM = asImpl.getPeerFSM();
        // also the AS should be INACTIVE now
        assertEquals(AsState.INACTIVE, this.getAsState(asPeerFSM));

        // The other side will send ASP_ACTIVE_ACK *no* NTFY(AS-ACTIVE)
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);

        localAspFactory.read(aspActiveAck);

        assertEquals(AspState.ACTIVE, this.getAspState(aspLocalFSM));
        // also the AS should be ACTIVE now
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));

        // Check if MTP3 RESUME received
        // lets wait for 2second to receive the MTP3 primitive before giving up
        semaphore.tryAcquire(2000, TimeUnit.MILLISECONDS);

        Mtp3Primitive mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Since we didn't set the Traffic Mode while creating AS, it should now
        // be set to loadshare as default
        assertEquals(TrafficModeType.Loadshare, asImpl.getTrafficModeType().getMode());

        // Lets stop ASP Factory
        localAspFactory.stop();

        assertEquals(AspState.DOWN_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));
        // also the AS is PENDING
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));

        // lets wait for 3 seconds to receive the MTP3 primitive before giving up. We know Pending timeout is 2 secs
        semaphore.tryAcquire(3000, TimeUnit.MILLISECONDS);

        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Make sure we don't have any more
        assertNull(testAssociation.txPoll());

    }

    @Test
    public void testPendingQueue() throws Exception {

        TestAssociation testAssociation = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0,
                "testAssoc");

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

        AsImpl asImpl = (AsImpl) this.clientM3UAMgmt.createAs("testas", Functionality.IPSP, ExchangeType.SE, null, rc, null, 1,
                null);
        FSM asPeerFSM = asImpl.getPeerFSM();

        AspFactoryImpl localAspFactory = (AspFactoryImpl) clientM3UAMgmt.createAspFactory("testasp", "testAssoc", false);
        localAspFactory.start();

        AspImpl aspImpl = clientM3UAMgmt.assignAspToAs("testas", "testasp");

        // Create Route
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas");

        FSM aspLocalFSM = aspImpl.getLocalFSM();

        // Check for Communication UP
        testAssociation.signalCommUp();

        // Once comunication is UP, ASP_UP should have been sent.
        assertEquals(AspState.UP_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));

        // The other side will send ASP_UP_ACK and *no* NTFY(AS-INACTIVE)
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        localAspFactory.read(message);

        assertEquals(AspState.ACTIVE_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
        // also the AS should be INACTIVE now
        assertEquals(AsState.INACTIVE, this.getAsState(asPeerFSM));

        // The other side will send ASP_ACTIVE_ACK and *no* NTFY(AS-ACTIVE)
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        localAspFactory.read(aspActiveAck);

        assertEquals(AspState.ACTIVE, this.getAspState(aspLocalFSM));
        // also the AS should be ACTIVE now
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));

        // Check if MTP3 RESUME received
        // lets wait for 2second to receive the MTP3 primitive before giving up
        semaphore.tryAcquire(2000, TimeUnit.MILLISECONDS);

        Mtp3Primitive mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Lets stop ASP Factory
        localAspFactory.stop();

        assertEquals(AspState.DOWN_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));
        // also the AS is PENDING
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));

        // The far end sends DOWN_ACK
        message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK);
        localAspFactory.read(message);

        // start the ASP Factory again
        localAspFactory.start();

        // Now lets add some PayloadData
        PayloadDataImpl payload = (PayloadDataImpl) messageFactory.createMessage(MessageClass.TRANSFER_MESSAGES,
                MessageType.PAYLOAD);
        ProtocolDataImpl p1 = (ProtocolDataImpl) parmFactory.createProtocolData(1408, 123, 3, 1, 0, 1,
                new byte[] { 1, 2, 3, 4 });
        payload.setRoutingContext(rc);
        payload.setData(p1);

        asImpl.write(payload);

        // Now again the ASP is brought up
        testAssociation.signalCommUp();

        // Once communication is UP, ASP_UP should have been sent.
        assertEquals(AspState.UP_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));

        // The other side will send ASP_UP_ACK and *no* NTFY(AS-INACTIVE)
        message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        localAspFactory.read(message);

        assertEquals(AspState.ACTIVE_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
        // AS should still be PENDING
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));

        // The other side will send ASP_ACTIVE_ACK and *no* NTFY(AS-ACTIVE)
        aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        localAspFactory.read(aspActiveAck);

        assertEquals(AspState.ACTIVE, this.getAspState(aspLocalFSM));
        // also the AS should be ACTIVE now
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));

        // Also we should have PayloadData
        M3UAMessage payLoadTemp = testAssociation.txPoll();
        assertNotNull(payLoadTemp);
        assertEquals(MessageClass.TRANSFER_MESSAGES, payLoadTemp.getMessageClass());
        assertEquals(MessageType.PAYLOAD, payLoadTemp.getMessageType());

        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Make sure we don't have any more
        assertNull(testAssociation.txPoll());

    }

    /**
     * Test with RC Set for Double Exchange message
     *
     * @throws Exception
     */
    @Test
    public void testSingleAspInAsWithRCDE() throws Exception {

        // 5.1.1. Single ASP in an Application Server ("1+0" sparing),
        this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc1");

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

        // As as = rsgw.createAppServer("testas", rc, rKey, trModType);
        AsImpl asImpl = (AsImpl) this.clientM3UAMgmt.createAs("testas", Functionality.IPSP, ExchangeType.DE, null, rc, null, 1,
                null);

        AspFactoryImpl localAspFactory = (AspFactoryImpl) this.clientM3UAMgmt.createAspFactory("testasp", "testAssoc1", false);
        localAspFactory.start();

        AspImpl aspImpl = clientM3UAMgmt.assignAspToAs("testas", "testasp");

        // Create Route
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas");

        // Signal for Communication UP
        TestAssociation testAssociation = (TestAssociation) this.transportManagement.getAssociation("testAssoc1");
        testAssociation.signalCommUp();

        FSM aspLocalFSM = aspImpl.getLocalFSM();
        FSM aspPeerFSM = aspImpl.getPeerFSM();

        assertNotNull(aspLocalFSM);
        assertNotNull(aspPeerFSM);

        FSM asPeerFSM = asImpl.getPeerFSM();
        FSM asLocalFSM = asImpl.getLocalFSM();

        assertNotNull(asPeerFSM);
        assertNotNull(asLocalFSM);

        // Once communication is UP, ASP_UP should have been sent.
        assertEquals(AspState.UP_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));

        // The other side will send ASP_UP_ACK and *no* NTFY(AS-INACTIVE) and
        // will also send ASP_UP
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        localAspFactory.read(message);

        message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
        localAspFactory.read(message);

        // aspLocalFSM should be ACTIVE_SENT and aspPeerFSM should be INACTIVE
        assertEquals(AspState.ACTIVE_SENT, this.getAspState(aspLocalFSM));
        assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));

        // Out going queue has ASP_ACTIVE and ASP_UP_ACK messages
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));

        // also the asPeerFSM and asLocalFSM should be INACTIVE now
        assertEquals(AsState.INACTIVE, this.getAsState(asPeerFSM));
        assertEquals(AsState.INACTIVE, this.getAsState(asLocalFSM));

        // The other side will send ASP_ACTIVE_ACK and *no* NTFY(AS-ACTIVE) and
        // ASP_ACTIVE
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        localAspFactory.read(aspActiveAck);

        ASPActive aspActive = (ASPActive) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE);
        aspActiveAck.setRoutingContext(rc);
        localAspFactory.read(aspActive);

        // Asp both FSM ACTIVE now
        assertEquals(AspState.ACTIVE, this.getAspState(aspLocalFSM));
        assertEquals(AspState.ACTIVE, this.getAspState(aspPeerFSM));
        // also the AS should be ACTIVE now
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));
        assertEquals(AsState.ACTIVE, this.getAsState(asLocalFSM));

        // Check if MTP3 RESUME received
        // lets wait for 2second to receive the MTP3 primitive before giving up
        semaphore.tryAcquire(2000, TimeUnit.MILLISECONDS);

        Mtp3Primitive mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // ASPACTIVE_ACK is queued to go out
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1, -1));

        // Lets stop ASP Factory
        localAspFactory.stop();

        assertEquals(AspState.DOWN_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));
        // also the AS is PENDING
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));

        // lets wait for 3 seconds to receive the MTP3 primitive before giving up. We know Pending timeout is 2 secs
        semaphore.tryAcquire(3000, TimeUnit.MILLISECONDS);

        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Make sure we don't have any more
        assertNull(testAssociation.txPoll());

    }

    /**
     * Validate that next message in Association queue if of Class and Type passed as argument. type and info are only for
     * management messages
     *
     * @param testAssociation
     * @param msgClass
     * @param msgType
     * @param type
     * @param info
     * @return
     */
    private boolean validateMessage(TestAssociation testAssociation, int msgClass, int msgType, int type, int info) {
        M3UAMessage message = testAssociation.txPoll();
        if (message == null) {
            return false;
        }

        if (message.getMessageClass() != msgClass || message.getMessageType() != msgType) {
            return false;
        }

        if (message.getMessageClass() == MessageClass.MANAGEMENT) {
            if (message.getMessageType() == MessageType.NOTIFY) {
                Status s = ((Notify) message).getStatus();
                if (s.getType() != type || s.getInfo() != info) {
                    return false;
                } else {
                    return true;
                }
            }

            // TODO take care of Error?
            return true;
        } else {
            return true;
        }

    }

    class TestAssociation implements Association {

        private AssociationListener associationListener = null;
        private String name = null;
        private LinkedList<M3UAMessage> messageRxFromUserPart = new LinkedList<M3UAMessage>();

        TestAssociation(String name) {
            this.name = name;
        }

        M3UAMessage txPoll() {
            return messageRxFromUserPart.poll();
        }

        @Override
        public AssociationListener getAssociationListener() {
            return this.associationListener;
        }

        @Override
        public String getHostAddress() {
            return null;
        }

        @Override
        public int getHostPort() {
            return 0;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getPeerAddress() {
            return null;
        }

        @Override
        public int getPeerPort() {
            return 0;
        }

        @Override
        public String getServerName() {
            return null;
        }

        @Override
        public boolean isStarted() {
            return false;
        }

        @Override
        public void send(PayloadData payloadData) throws Exception {
            M3UAMessage m3uaMessage = messageFactory.createMessage(payloadData.getByteBuf());
            this.messageRxFromUserPart.add(m3uaMessage);
        }

        @Override
        public void setAssociationListener(AssociationListener associationListener) {
            this.associationListener = associationListener;
        }

        public void signalCommUp() {
            this.associationListener.onCommunicationUp(this, 1, 1);
        }

        public void signalCommLost() {
            this.associationListener.onCommunicationLost(this);
        }

        @Override
        public IpChannelType getIpChannelType() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public AssociationType getAssociationType() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String[] getExtraHostAddresses() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.mobicents.protocols.api.Association#isConnected()
         */
        @Override
        public boolean isConnected() {
            return false;
        }

        @Override
        public void acceptAnonymousAssociation(AssociationListener arg0) throws Exception {
            // TODO Auto-generated method stub

        }

        @Override
        public void rejectAnonymousAssociation() {
            // TODO Auto-generated method stub

        }

        @Override
        public void stopAnonymousAssociation() throws Exception {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isUp() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public ByteBufAllocator getByteBufAllocator() throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getCongestionLevel() {
            // TODO Auto-generated method stub
            return 0;
        }

    }

    class NettyTransportManagement implements Management {

        private FastMap<String, Association> associations = new FastMap<String, Association>();

        @Override
        public Association addAssociation(String hostAddress, int hostPort, String peerAddress, int peerPort, String assocName)
                throws Exception {
            TestAssociation testAssociation = new TestAssociation(assocName);
            this.associations.put(assocName, testAssociation);
            return testAssociation;
        }

        @Override
        public Server addServer(String serverName, String hostAddress, int port) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Association addServerAssociation(String peerAddress, int peerPort, String serverName, String assocName)
                throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Association getAssociation(String assocName) throws Exception {
            return this.associations.get(assocName);
        }

        @Override
        public Map<String, Association> getAssociations() {
            return associations.unmodifiable();
        }

        @Override
        public int getConnectDelay() {
            return 0;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public List<Server> getServers() {
            return null;
        }

        @Override
        public int getWorkerThreads() {
            return 0;
        }

        @Override
        public boolean isSingleThread() {
            return false;
        }

        @Override
        public void removeAssociation(String assocName) throws Exception {

        }

        @Override
        public void removeServer(String serverName) throws Exception {

        }

        @Override
        public void setConnectDelay(int connectDelay) {

        }

        @Override
        public void setSingleThread(boolean arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void setWorkerThreads(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void start() throws Exception {
            // TODO Auto-generated method stub

        }

        @Override
        public void startAssociation(String arg0) throws Exception {
            // TODO Auto-generated method stub

        }

        @Override
        public void startServer(String arg0) throws Exception {
            // TODO Auto-generated method stub

        }

        @Override
        public void stop() throws Exception {
            // TODO Auto-generated method stub

        }

        @Override
        public void stopAssociation(String arg0) throws Exception {
            // TODO Auto-generated method stub

        }

        @Override
        public void stopServer(String arg0) throws Exception {
            // TODO Auto-generated method stub

        }

        @Override
        public String getPersistDir() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setPersistDir(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public Association addAssociation(String arg0, int arg1, String arg2, int arg3, String arg4, IpChannelType arg5,
                String[] extraHostAddresses) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Server addServer(String arg0, String arg1, int arg2, IpChannelType arg3, String[] extraHostAddresses)
                throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Association addServerAssociation(String arg0, int arg1, String arg2, String arg3, IpChannelType arg4)
                throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void removeAllResourses() throws Exception {
            // TODO Auto-generated method stub

        }

        @Override
        public void addManagementEventListener(ManagementEventListener arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public Server addServer(String arg0, String arg1, int arg2, IpChannelType arg3, boolean arg4, int arg5, String[] arg6)
                throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ServerListener getServerListener() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void removeManagementEventListener(ManagementEventListener arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void setServerListener(ServerListener arg0) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         *
         * @see org.mobicents.protocols.api.Management#isStarted()
         */
        @Override
        public boolean isStarted() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public double getCongControl_BackToNormalDelayThreshold_1() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public double getCongControl_BackToNormalDelayThreshold_2() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public double getCongControl_BackToNormalDelayThreshold_3() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public double getCongControl_DelayThreshold_1() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public double getCongControl_DelayThreshold_2() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public double getCongControl_DelayThreshold_3() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setCongControl_BackToNormalDelayThreshold_1(double arg0) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCongControl_BackToNormalDelayThreshold_2(double arg0) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCongControl_BackToNormalDelayThreshold_3(double arg0) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCongControl_DelayThreshold_1(double arg0) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCongControl_DelayThreshold_2(double arg0) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCongControl_DelayThreshold_3(double arg0) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Boolean getOptionSctpDisableFragments() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Integer getOptionSctpFragmentInterleave() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Boolean getOptionSctpNodelay() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Integer getOptionSoLinger() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Integer getOptionSoRcvbuf() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Integer getOptionSoSndbuf() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setOptionSctpDisableFragments(Boolean arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setOptionSctpFragmentInterleave(Integer arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setOptionSctpNodelay(Boolean arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setOptionSoLinger(Integer arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setOptionSoRcvbuf(Integer arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setOptionSoSndbuf(Integer arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Integer getOptionSctpInitMaxstreams_MaxInStreams() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Integer getOptionSctpInitMaxstreams_MaxOutStreams() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setOptionSctpInitMaxstreams_MaxInStreams(Integer arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setOptionSctpInitMaxstreams_MaxOutStreams(Integer arg0) {
            // TODO Auto-generated method stub
            
        }

    }

    class Mtp3UserPartListenerimpl implements Mtp3UserPartListener {
        private LinkedList<Mtp3Primitive> mtp3Primitives = new LinkedList<Mtp3Primitive>();
        private LinkedList<Mtp3TransferPrimitive> mtp3TransferPrimitives = new LinkedList<Mtp3TransferPrimitive>();

        Mtp3Primitive rxMtp3PrimitivePoll() {
            return this.mtp3Primitives.poll();
        }

        Mtp3TransferPrimitive rxMtp3TransferPrimitivePoll() {
            return this.mtp3TransferPrimitives.poll();
        }

        @Override
        public void onMtp3PauseMessage(Mtp3PausePrimitive pause) {
            this.mtp3Primitives.add(pause);
            semaphore.release();
        }

        @Override
        public void onMtp3ResumeMessage(Mtp3ResumePrimitive resume) {
            this.mtp3Primitives.add(resume);
            semaphore.release();
        }

        @Override
        public void onMtp3StatusMessage(Mtp3StatusPrimitive status) {
            this.mtp3Primitives.add(status);
            semaphore.release();
        }

        @Override
        public void onMtp3TransferMessage(Mtp3TransferPrimitive transfer) {
            this.mtp3TransferPrimitives.add(transfer);
            semaphore.release();
        }

        @Override
        public void onMtp3EndCongestionMessage(Mtp3EndCongestionPrimitive msg) {
            this.mtp3Primitives.add(msg);
            semaphore.release();
        }

    }
}
