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
import org.mobicents.protocols.api.CongestionListener;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.api.ManagementEventListener;
import org.mobicents.protocols.api.PayloadData;
import org.mobicents.protocols.api.Server;
import org.mobicents.protocols.api.ServerListener;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.asptm.ASPActiveImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.asptm.ASPInactiveImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.transfer.PayloadDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ProtocolDataImpl;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
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
 * Test for FSM of IPSP acting as Server
 *
 * @author abhayani
 *
 */
public class IPSPServerFSMTest {
    private ParameterFactoryImpl parmFactory = new ParameterFactoryImpl();
    private MessageFactoryImpl messageFactory = new MessageFactoryImpl();
    private M3UAManagementImpl serverM3UAMgmt = null;
    private Semaphore semaphore = null;
    private Mtp3UserPartListenerimpl mtp3UserPartListener = null;

    private NettyTransportManagement transportManagement = null;

    public IPSPServerFSMTest() {
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
        this.serverM3UAMgmt = new M3UAManagementImpl("IPSPServerFSMTest", null);
        this.serverM3UAMgmt.setTransportManagement(this.transportManagement);
        this.mtp3UserPartListener = new Mtp3UserPartListenerimpl();
        this.serverM3UAMgmt.addMtp3UserPartListener(this.mtp3UserPartListener);
        this.serverM3UAMgmt.start();

    }

    @AfterMethod
    public void tearDown() throws Exception {
        serverM3UAMgmt.removeAllResourses();
        serverM3UAMgmt.stop();
    }

    private AspState getAspState(FSM fsm) {
        return AspState.getState(fsm.getState().getName());
    }

    private AsState getAsState(FSM fsm) {
        return AsState.getState(fsm.getState().getName());
    }

    @Test
    public void testSingleAspInAsWithRC() throws Exception {
        // 5.1.1. Single ASP in an Application Server ("1+0" sparing),
        TestAssociation testAssociation = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0,
                "testAssoc1");

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

        // As remAs = sgw.createAppServer("testas", rc, rKey, trModType);
        AsImpl remAs = (AsImpl) serverM3UAMgmt.createAs("testas", Functionality.IPSP, ExchangeType.SE, IPSPType.SERVER, rc,
                null, 1, null);
        FSM asLocalFSM = remAs.getLocalFSM();

        AspFactoryImpl aspFactoryImpl = (AspFactoryImpl) serverM3UAMgmt.createAspFactory("testasp", "testAssoc1", false);

        AspImpl remAsp = serverM3UAMgmt.assignAspToAs("testas", "testasp");

        // Create Route
        this.serverM3UAMgmt.addRoute(2, -1, -1, "testas");

        FSM aspPeerFSM = remAsp.getPeerFSM();

        // Check for Communication UP
        testAssociation.signalCommUp();

        assertEquals(AspState.DOWN, this.getAspState(aspPeerFSM));

        // Peer sends ASP_UP
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
        aspFactoryImpl.read(message);

        assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));

        // also the AS should be INACTIVE now an no Notify should have been sent
        assertEquals(AsState.INACTIVE, this.getAsState(asLocalFSM));

        // Peer sends ASP_ACTIVE
        message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
        ((ASPActiveImpl) message).setRoutingContext(rc);

        aspFactoryImpl.read(message);
        assertEquals(AspState.ACTIVE, this.getAspState(aspPeerFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1, -1));
        // also the AS should be ACTIVE now
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

        // Since we didn't set the Traffic Mode while creating AS, it should now
        // be set to loadshare as default
        assertEquals(TrafficModeType.Loadshare, remAs.getTrafficModeType().getMode());

        // Check for ASP_INACTIVE
        message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE);
        ((ASPInactiveImpl) message).setRoutingContext(rc);

        aspFactoryImpl.read(message);
        assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE_ACK, -1, -1));
        // also the AS should be PENDING now
        assertEquals(AsState.PENDING, this.getAsState(asLocalFSM));

        // Check for ASP_DOWN
        message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN);
        aspFactoryImpl.read(message);
        assertEquals(AspState.DOWN, this.getAspState(aspPeerFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK, -1, -1));

        // also the AS is PENDING
        assertEquals(AsState.PENDING, this.getAsState(asLocalFSM));

        // lets wait for 3 seconds to receive the MTP3 primitive before giving
        // up. We know Pending timeout is 2 secs
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
    public void testSingleAspInAsWithoutRC() throws Exception {
        // 5.1.1. Single ASP in an Application Server ("1+0" sparing),
        TestAssociation testAssociation = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0,
                "testAssoc1");

        AsImpl remAs = (AsImpl) serverM3UAMgmt.createAs("testas", Functionality.IPSP, ExchangeType.SE, IPSPType.SERVER, null,
                null, 1, null);
        FSM asLocalFSM = remAs.getLocalFSM();

        AspFactoryImpl aspFactoryImpl = (AspFactoryImpl) serverM3UAMgmt.createAspFactory("testasp", "testAssoc1", false);

        AspImpl remAsp = serverM3UAMgmt.assignAspToAs("testas", "testasp");

        // Create Route
        this.serverM3UAMgmt.addRoute(2, -1, -1, "testas");

        FSM aspPeerFSM = remAsp.getPeerFSM();

        // Check for Communication UP
        testAssociation.signalCommUp();

        assertEquals(AspState.DOWN, this.getAspState(aspPeerFSM));

        // Peer sends ASP_UP
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
        aspFactoryImpl.read(message);

        assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
        // also the AS should be INACTIVE now
        assertEquals(AsState.INACTIVE, this.getAsState(asLocalFSM));

        // Check for ASP_ACTIVE
        message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);

        aspFactoryImpl.read(message);
        assertEquals(AspState.ACTIVE, this.getAspState(aspPeerFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1, -1));
        // also the AS should be ACTIVE now
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

        // Since we didn't set the Traffic Mode while creating AS, it should now
        // be set to loadshare as default
        assertEquals(TrafficModeType.Loadshare, remAs.getTrafficModeType().getMode());

        // Peer sends ASP_INACTIVE
        message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE);
        aspFactoryImpl.read(message);
        assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE_ACK, -1, -1));
        // also the AS should be PENDING now
        assertEquals(AsState.PENDING, this.getAsState(asLocalFSM));

        // Check for ASP_DOWN
        message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN);
        aspFactoryImpl.read(message);
        assertEquals(AspState.DOWN, this.getAspState(aspPeerFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK, -1, -1));

        // also the AS is PENDING
        assertEquals(AsState.PENDING, this.getAsState(asLocalFSM));

        // lets wait for 3 seconds to receive the MTP3 primitive before giving
        // up. We know Pending timeout is 2 secs
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
    public void testAspUpReceivedWhileASPIsAlreadyUp() throws Exception {
        // Test bug http://code.google.com/p/mobicents/issues/detail?id=2436

        TestAssociation testAssociation1 = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0,
                "testAssoc1");

        // 4.3.4.1. ASP Up Procedures from http://tools.ietf.org/html/rfc4666
        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

        TrafficModeType trModType = parmFactory.createTrafficModeType(TrafficModeType.Override);

        // As remAs = sgw.createAppServer("testas", rc, rKey, trModType);

        AsImpl remAs = (AsImpl) serverM3UAMgmt.createAs("testas", Functionality.IPSP, ExchangeType.SE, IPSPType.SERVER, rc,
                trModType, 1, null);

        AspFactoryImpl aspFactoryImpl = (AspFactoryImpl) serverM3UAMgmt.createAspFactory("testasp", "testAssoc1", false);

        AspImpl remAsp = serverM3UAMgmt.assignAspToAs("testas", "testasp");

        // Create Route
        this.serverM3UAMgmt.addRoute(2, -1, -1, "testas");

        FSM aspPeerFSM = remAsp.getPeerFSM();

        FSM asLocalFSM = remAs.getLocalFSM();

        // Check for Communication UP
        testAssociation1.signalCommUp();

        assertEquals(AspState.DOWN, this.getAspState(aspPeerFSM));

        // Peer sends ASP_UP
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
        aspFactoryImpl.read(message);

        assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
        assertTrue(validateMessage(testAssociation1, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
        // also the AS should be INACTIVE now
        assertEquals(AsState.INACTIVE, this.getAsState(asLocalFSM));

        // Check for ASP_ACTIVE
        message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
        ((ASPActiveImpl) message).setRoutingContext(rc);
        aspFactoryImpl.read(message);
        assertEquals(AspState.ACTIVE, this.getAspState(aspPeerFSM));
        assertTrue(validateMessage(testAssociation1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1, -1));
        // also the AS should be ACTIVE now
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

        // Check for ASP_UP received while ASP is already ACTIVE
        message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
        aspFactoryImpl.read(message);
        // The ASP Transitions to INACTIVE
        assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
        // Receives ASP_UP Ack messages
        assertTrue(validateMessage(testAssociation1, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
        // As well as receives Error message
        assertTrue(validateMessage(testAssociation1, MessageClass.MANAGEMENT, MessageType.ERROR, ErrorCode.Unexpected_Message,
                100));

        // also the AS should be PENDING now
        assertEquals(AsState.PENDING, this.getAsState(asLocalFSM));

        // lets wait for 3 seconds to receive the MTP3 primitive before giving
        // up. We know Pending timeout is 2 secs
        semaphore.tryAcquire(3000, TimeUnit.MILLISECONDS);

        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Make sure we don't have any more
        assertNull(testAssociation1.txPoll());

    }

    @Test
    public void testPendingQueue() throws Exception {

        TestAssociation testAssociation1 = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0,
                "testAssoc1");

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

        TrafficModeType trModType = parmFactory.createTrafficModeType(TrafficModeType.Override);

        AsImpl remAs = (AsImpl) serverM3UAMgmt.createAs("testas", Functionality.IPSP, ExchangeType.SE, IPSPType.SERVER, rc,
                trModType, 1, null);
        FSM asLocalFSM = remAs.getLocalFSM();

        AspFactoryImpl aspFactoryImpl = (AspFactoryImpl) serverM3UAMgmt.createAspFactory("testasp", "testAssoc1", false);

        AspImpl remAsp = serverM3UAMgmt.assignAspToAs("testas", "testasp");

        // Create Route
        this.serverM3UAMgmt.addRoute(2, -1, -1, "testas");

        FSM aspPeerFSM = remAsp.getPeerFSM();

        // Check for Communication UP
        testAssociation1.signalCommUp();

        assertEquals(AspState.DOWN, this.getAspState(aspPeerFSM));

        // Peer sends ASP_UP
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
        aspFactoryImpl.read(message);

        assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
        assertTrue(validateMessage(testAssociation1, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
        // also the AS should be INACTIVE now
        assertEquals(AsState.INACTIVE, this.getAsState(asLocalFSM));

        // Peer sends ASP_ACTIVE
        message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
        ((ASPActiveImpl) message).setRoutingContext(rc);
        aspFactoryImpl.read(message);
        assertEquals(AspState.ACTIVE, this.getAspState(aspPeerFSM));
        assertTrue(validateMessage(testAssociation1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1, -1));
        // also the AS should be ACTIVE now
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

        // Check for ASP_INACTIVE
        message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE);
        ((ASPInactiveImpl) message).setRoutingContext(rc);
        aspFactoryImpl.read(message);
        assertEquals(AspState.INACTIVE, this.getAspState(aspPeerFSM));
        assertTrue(validateMessage(testAssociation1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE_ACK, -1, -1));
        // also the AS should be PENDING now
        assertEquals(AsState.PENDING, this.getAsState(asLocalFSM));

        // Add PayloadData
        PayloadDataImpl payload = (PayloadDataImpl) messageFactory.createMessage(MessageClass.TRANSFER_MESSAGES,
                MessageType.PAYLOAD);
        ProtocolDataImpl p1 = (ProtocolDataImpl) parmFactory.createProtocolData(1408, 123, 3, 1, 0, 1,
                new byte[] { 1, 2, 3, 4 });
        payload.setRoutingContext(rc);
        payload.setData(p1);

        remAs.write(payload);

        // Now bring UP the ASP
        message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
        ((ASPActiveImpl) message).setRoutingContext(rc);
        aspFactoryImpl.read(message);

        assertTrue(validateMessage(testAssociation1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1, -1));
        // also the AS should be ACTIVE now
        assertEquals(AsState.ACTIVE, this.getAsState(asLocalFSM));

        // Also we should have PayloadData
        M3UAMessage payLoadTemp = testAssociation1.txPoll();
        assertNotNull(payLoadTemp);
        assertEquals(MessageClass.TRANSFER_MESSAGES, payLoadTemp.getMessageClass());
        assertEquals(MessageType.PAYLOAD, payLoadTemp.getMessageType());

        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Make sure we don't have any more
        assertNull(testAssociation1.txPoll());
    }

    /**
     *
     * @param factory
     * @param msgClass
     * @param msgType
     * @param type The type for Notify message Or Error Code for Error Messages
     * @param info The Info for Notify message Or RoutingContext for Error Message
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
            } else if (message.getMessageType() == MessageType.ERROR) {
                ErrorCode errCode = ((org.mobicents.protocols.ss7.m3ua.message.mgmt.Error) message).getErrorCode();
                if (errCode.getCode() != type) {
                    return false;
                }

                RoutingContext rc = ((org.mobicents.protocols.ss7.m3ua.message.mgmt.Error) message).getRoutingContext();
                if (rc == null || rc.getRoutingContexts()[0] != info) {
                    return false;
                }

                return true;

            }
            return false;
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
            // TODO Auto-generated method stub
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

        @Override
        public void addCongestionListener(CongestionListener arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void removeCongestionListener(CongestionListener arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public int getBufferSize() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setBufferSize(int arg0) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void modifyServer(String serverName, String hostAddress, Integer port, IpChannelType ipChannelType,
                Boolean acceptAnonymousConnections, Integer maxConcurrentConnectionsCount, String[] extraHostAddresses)
                throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void modifyServerAssociation(String assocName, String peerAddress, Integer peerPort, String serverName,
                IpChannelType ipChannelType) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void modifyAssociation(String hostAddress, Integer hostPort, String peerAddress, Integer peerPort,
                String assocName, IpChannelType ipChannelType, String[] extraHostAddresses) throws Exception {
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
