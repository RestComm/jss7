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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBufAllocator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javolution.util.FastList;
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
import org.mobicents.protocols.ss7.m3ua.As;
import org.mobicents.protocols.ss7.m3ua.Asp;
import org.mobicents.protocols.ss7.m3ua.AspFactory;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.M3UAManagementEventListener;
import org.mobicents.protocols.ss7.m3ua.State;
import org.mobicents.protocols.ss7.m3ua.Util;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.transfer.PayloadDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ProtocolDataImpl;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPDownAck;
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
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the FSM of client side AS and ASP's
 *
 * @author amit bhayani
 *
 */
public class RemSgFSMTest {

    private ParameterFactoryImpl parmFactory = new ParameterFactoryImpl();
    private MessageFactoryImpl messageFactory = new MessageFactoryImpl();
    private M3UAManagementImpl clientM3UAMgmt = null;
    private Mtp3UserPartListenerimpl mtp3UserPartListener = null;

    private Semaphore semaphore = null;

    private NettyTransportManagement transportManagement = null;

    private M3UAManagementEventListenerImpl m3uaManagementEventListenerImpl = null;

    public RemSgFSMTest() {
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
        this.transportManagement.setPersistDir(Util.getTmpTestDir());
        this.m3uaManagementEventListenerImpl = new M3UAManagementEventListenerImpl();
        this.clientM3UAMgmt = new M3UAManagementImpl("RemSgFSMTest", null);
        this.clientM3UAMgmt.setPersistDir(Util.getTmpTestDir());
        this.clientM3UAMgmt.addM3UAManagementEventListener(this.m3uaManagementEventListenerImpl);
        this.clientM3UAMgmt.setTransportManagement(this.transportManagement);
        this.mtp3UserPartListener = new Mtp3UserPartListenerimpl();
        this.clientM3UAMgmt.addMtp3UserPartListener(this.mtp3UserPartListener);
        this.clientM3UAMgmt.setPersistDir(Util.getTmpTestDir());
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
     * Test with RC Set
     *
     * @throws Exception
     */
    @Test
    public void testSingleAspInAsWithRC() throws Exception {

        // 5.1.1. Single ASP in an Application Server ("1+0" sparing),
        int m3uaManagementEventsSeq = 0;
        this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc1");

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

        AsImpl asImpl = (AsImpl) this.clientM3UAMgmt.createAs("testas", Functionality.AS, ExchangeType.SE, null, rc, null, 1,
                null);
        // Check if M3UAManagementEventListener received event
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsCreated, System
                .currentTimeMillis(), new Object[] { asImpl }, m3uaManagementEventsSeq++)));

        AspFactoryImpl localAspFactory = (AspFactoryImpl) this.clientM3UAMgmt.createAspFactory("testasp", "testAssoc1", false);
        // Check if M3UAManagementEventListener received event
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspFactoryCreated, System
                .currentTimeMillis(), new Object[] { localAspFactory }, m3uaManagementEventsSeq++)));

        AspImpl aspImpl = clientM3UAMgmt.assignAspToAs("testas", "testasp");
        // Check if M3UAManagementEventListener received event
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspAssignedToAs, System
                .currentTimeMillis(), new Object[] { asImpl, aspImpl }, m3uaManagementEventsSeq++)));

        this.clientM3UAMgmt.startAsp("testasp");

        // Check if M3UAManagementEventListener received event
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspFactoryStarted, System
                .currentTimeMillis(), new Object[] { localAspFactory }, m3uaManagementEventsSeq++)));

        // Create Route. Adding 3 routes
        this.clientM3UAMgmt.addRoute(3, -1, -1, "testas");
        this.clientM3UAMgmt.addRoute(2, 10, -1, "testas");
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas");

        // Signal for Communication UP
        TestAssociation testAssociation = (TestAssociation) this.transportManagement.getAssociation("testAssoc1");
        testAssociation.signalCommUp();

        // Once comunication is UP, ASP_UP should have been sent.
        FSM aspLocalFSM = aspImpl.getLocalFSM();
        assertEquals(AspState.UP_SENT, this.getAspState(aspLocalFSM));
        assertEquals(aspImpl.getState().getName(), State.STATE_DOWN);
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));

        // The other side will send ASP_UP_ACK and after that NTFY(AS-INACTIVE)
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        localAspFactory.read(message);

        assertEquals(aspImpl.getState().getName(), State.STATE_INACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspInactive, System
                .currentTimeMillis(), new Object[] { aspImpl }, m3uaManagementEventsSeq++)));

        Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);

        assertEquals(asImpl.getState().getName(), State.STATE_INACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsInactive, System
                .currentTimeMillis(), new Object[] { asImpl }, m3uaManagementEventsSeq++)));

        assertEquals(AspState.ACTIVE_SENT, this.getAspState(aspLocalFSM));

        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));

        FSM asPeerFSM = asImpl.getPeerFSM();
        // also the AS should be INACTIVE now
        assertEquals(AsState.INACTIVE, this.getAsState(asPeerFSM));

        // The other side will send ASP_ACTIVE_ACK and after that
        // NTFY(AS-ACTIVE)
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        localAspFactory.read(aspActiveAck);

        assertEquals(AspState.ACTIVE, this.getAspState(aspLocalFSM));
        assertEquals(aspImpl.getState().getName(), State.STATE_ACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspActive, System
                .currentTimeMillis(), new Object[] { aspImpl }, m3uaManagementEventsSeq++)));

        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);

        // also the AS should be ACTIVE now
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));
        assertEquals(asImpl.getState().getName(), State.STATE_ACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsActive, System
                .currentTimeMillis(), new Object[] { asImpl }, m3uaManagementEventsSeq++)));

        // Check if MTP3 RESUME received
        // lets wait for 2second to receive the MTP3 primitive before giving up
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);
        // RESUME for DPC 3
        Mtp3Primitive mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(3, mtp3Primitive.getAffectedDpc());

        // lets wait for 2second to receive the MTP3 primitive before giving up
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);
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
        assertEquals(aspImpl.getState().getName(), State.STATE_DOWN);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspDown, System
                .currentTimeMillis(), new Object[] { aspImpl }, m3uaManagementEventsSeq++)));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));

        // also the AS is PENDING
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));
        assertEquals(asImpl.getState().getName(), State.STATE_PENDING);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsPending, System
                .currentTimeMillis(), new Object[] { asImpl }, m3uaManagementEventsSeq++)));

        // lets wait for 3 seconds to receive the MTP3 primitive before giving
        // up. We know Pending timeout is 2 secs
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);
        // PAUSE for DPC 3
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(3, mtp3Primitive.getAffectedDpc());

        // lets wait for 3 seconds to receive the MTP3 primitive before giving
        // up. We know Pending timeout is 2 secs
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);
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

        // also the AS is DOWN
        assertEquals(AsState.DOWN, this.getAsState(asPeerFSM));
        assertEquals(asImpl.getState().getName(), State.STATE_DOWN);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsDown, System
                .currentTimeMillis(), new Object[] { asImpl }, m3uaManagementEventsSeq++)));

        ASPDownAck aspDownAck = (ASPDownAck) messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
                MessageType.ASP_DOWN_ACK);
        localAspFactory.read(aspDownAck);

    }

    /**
     * Test with RC Set
     *
     * @throws Exception
     */
    @Test
    public void testSingleAspInAsWithoutRC() throws Exception {

        // 5.1.1. Single ASP in an Application Server ("1+0" sparing),
        int m3uaManagementEventsSeq = 0;
        this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc1");

        AsImpl asImpl = (AsImpl) this.clientM3UAMgmt.createAs("testas", Functionality.AS, ExchangeType.SE, null, null, null, 1,
                null);
        // Check if M3UAManagementEventListener received event
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsCreated, System
                .currentTimeMillis(), new Object[] { asImpl }, m3uaManagementEventsSeq++)));

        AspFactoryImpl localAspFactory = (AspFactoryImpl) this.clientM3UAMgmt.createAspFactory("testasp", "testAssoc1", false);
        // Check if M3UAManagementEventListener received event
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspFactoryCreated, System
                .currentTimeMillis(), new Object[] { localAspFactory }, m3uaManagementEventsSeq++)));

        AspImpl aspImpl = clientM3UAMgmt.assignAspToAs("testas", "testasp");
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspAssignedToAs, System
                .currentTimeMillis(), new Object[] { asImpl, aspImpl }, m3uaManagementEventsSeq++)));

        this.clientM3UAMgmt.startAsp("testasp");
        // Check if M3UAManagementEventListener received event
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspFactoryStarted, System
                .currentTimeMillis(), new Object[] { localAspFactory }, m3uaManagementEventsSeq++)));

        // Create Route
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas");

        // Signal for Communication UP
        TestAssociation testAssociation = (TestAssociation) this.transportManagement.getAssociation("testAssoc1");
        testAssociation.signalCommUp();

        // Once communication is UP, ASP_UP should have been sent.
        FSM aspLocalFSM = aspImpl.getLocalFSM();
        assertEquals(AspState.UP_SENT, this.getAspState(aspLocalFSM));
        assertEquals(aspImpl.getState().getName(), State.STATE_DOWN);
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));

        // The other side will send ASP_UP_ACK and after that NTFY(AS-INACTIVE)
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        localAspFactory.read(message);

        assertEquals(aspImpl.getState().getName(), State.STATE_INACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspInactive, System
                .currentTimeMillis(), new Object[] { aspImpl }, m3uaManagementEventsSeq++)));

        Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);

        assertEquals(asImpl.getState().getName(), State.STATE_INACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsInactive, System
                .currentTimeMillis(), new Object[] { asImpl }, m3uaManagementEventsSeq++)));

        assertEquals(AspState.ACTIVE_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));

        FSM asPeerFSM = asImpl.getPeerFSM();
        // also the AS should be INACTIVE now
        assertEquals(AsState.INACTIVE, this.getAsState(asPeerFSM));

        // The other side will send ASP_ACTIVE_ACK and after that
        // NTFY(AS-ACTIVE)
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);
        localAspFactory.read(aspActiveAck);

        assertEquals(AspState.ACTIVE, this.getAspState(aspLocalFSM));
        assertEquals(aspImpl.getState().getName(), State.STATE_ACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspActive, System
                .currentTimeMillis(), new Object[] { aspImpl }, m3uaManagementEventsSeq++)));

        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);

        // also the AS should be ACTIVE now
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));
        assertEquals(asImpl.getState().getName(), State.STATE_ACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsActive, System
                .currentTimeMillis(), new Object[] { asImpl }, m3uaManagementEventsSeq++)));

        // Check if MTP3 RESUME received
        // lets wait for 2second to receive the MTP3 primitive before giving up
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);

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
        this.clientM3UAMgmt.stopAsp("testasp");

        assertEquals(AspState.DOWN_SENT, this.getAspState(aspLocalFSM));
        assertEquals(aspImpl.getState().getName(), State.STATE_DOWN);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspDown, System
                .currentTimeMillis(), new Object[] { aspImpl }, m3uaManagementEventsSeq++)));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));

        // also the AS is PENDING
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));
        assertEquals(asImpl.getState().getName(), State.STATE_PENDING);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsPending, System
                .currentTimeMillis(), new Object[] { asImpl }, m3uaManagementEventsSeq++)));

        // Remember AspFactoryStopped will be called after AspDown and AsPending
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspFactoryStopped, System
                .currentTimeMillis(), new Object[] { localAspFactory }, m3uaManagementEventsSeq++)));

        // lets wait for 3 seconds to receive the MTP3 primitive before giving
        // up. We know Pending timeout is 2 secs
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);

        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Make sure we don't have any more
        assertNull(testAssociation.txPoll());

        // also the AS is DOWN
        assertEquals(AsState.DOWN, this.getAsState(asPeerFSM));
        assertEquals(asImpl.getState().getName(), State.STATE_DOWN);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsDown, System
                .currentTimeMillis(), new Object[] { asImpl }, m3uaManagementEventsSeq++)));

        ASPDownAck aspDownAck = (ASPDownAck) messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
                MessageType.ASP_DOWN_ACK);
        localAspFactory.read(aspDownAck);
    }

    @Test
    public void testTwoAsInLoadBalance() throws Exception {
        int m3uaManagementEventsSeq = 0;

        Mtp3TransferPrimitiveFactory factory = this.clientM3UAMgmt.getMtp3TransferPrimitiveFactory();

        this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc1");

        this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc2");

        // Define 1st AS
        AsImpl remAs1 = (AsImpl) this.clientM3UAMgmt.createAs("testas1", Functionality.AS, ExchangeType.SE, null, null, null,
                1, null);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsCreated, System
                .currentTimeMillis(), new Object[] { remAs1 }, m3uaManagementEventsSeq++)));

        // Define 2nd AS
        AsImpl remAs2 = (AsImpl) clientM3UAMgmt.createAs("testas2", Functionality.AS, ExchangeType.SE, null, null, null, 1,
                null);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsCreated, System
                .currentTimeMillis(), new Object[] { remAs2 }, m3uaManagementEventsSeq++)));

        // Define AspFactory 1
        AspFactoryImpl aspFactoryImpl1 = (AspFactoryImpl) clientM3UAMgmt.createAspFactory("testasp1", "testAssoc1", false);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspFactoryCreated, System
                .currentTimeMillis(), new Object[] { aspFactoryImpl1 }, m3uaManagementEventsSeq++)));

        // Define AspFactory 2
        AspFactoryImpl aspFactoryImpl2 = (AspFactoryImpl) clientM3UAMgmt.createAspFactory("testasp2", "testAssoc2", false);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspFactoryCreated, System
                .currentTimeMillis(), new Object[] { aspFactoryImpl2 }, m3uaManagementEventsSeq++)));

        // TODO : Call start from management
        aspFactoryImpl1.start();
        aspFactoryImpl2.start();

        AspImpl remAsp1 = clientM3UAMgmt.assignAspToAs("testas1", "testasp1");
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspAssignedToAs, System
                .currentTimeMillis(), new Object[] { remAs1, remAsp1 }, m3uaManagementEventsSeq++)));

        AspImpl remAsp2 = clientM3UAMgmt.assignAspToAs("testas2", "testasp2");
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspAssignedToAs, System
                .currentTimeMillis(), new Object[] { remAs2, remAsp2 }, m3uaManagementEventsSeq++)));

        // Create Route
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas1");
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas2");

        // Signal for Communication UP
        TestAssociation testAssociation1 = (TestAssociation) this.transportManagement.getAssociation("testAssoc1");
        testAssociation1.signalCommUp();

        // Signal for Communication UP
        TestAssociation testAssociation2 = (TestAssociation) this.transportManagement.getAssociation("testAssoc2");
        testAssociation2.signalCommUp();

        // Once communication is UP, ASP_UP should have been sent.
        assertTrue(validateMessage(testAssociation1, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));
        assertTrue(validateMessage(testAssociation2, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));

        // The other side will send ASP_UP_ACK and after that NTFY(AS-INACTIVE)
        // for both the AS
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        aspFactoryImpl1.read(message);
        aspFactoryImpl2.read(message);

        Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        aspFactoryImpl1.read(notify);
        aspFactoryImpl2.read(notify);

        // The other side will send ASP_ACTIVE_ACK and after that NTFY(AS-ACTIVE)
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);
        aspFactoryImpl1.read(aspActiveAck);
        aspFactoryImpl2.read(aspActiveAck);

        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        aspFactoryImpl1.read(notify);

        // Check if MTP3 RESUME received
        // lets wait for 2second to receive the MTP3 primitive before giving up
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);

        // The route should be RESUME
        Mtp3Primitive mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());

        aspFactoryImpl2.read(notify);

        // Send Transfer Message and check load balancing behavior
        // int si, int ni, int mp, int opc, int dpc, int sls, byte[] data,
        // RoutingLabelFormat pointCodeFormat

        testAssociation1.clearRxMessages();
        testAssociation2.clearRxMessages();

        for (int sls = 0; sls < 256; sls++) {
            Mtp3TransferPrimitive mtp3TransferPrimitive = factory.createMtp3TransferPrimitive(3, 1, 0, 1, 2, sls, new byte[] {
                    1, 2, 3, 4 });
            clientM3UAMgmt.sendMessage(mtp3TransferPrimitive);
        }

        for (int count = 0; count < 128; count++) {
            assertTrue(validateMessage(testAssociation1, MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD, -1, -1));
        }

        for (int count = 0; count < 128; count++) {
            assertTrue(validateMessage(testAssociation2, MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD, -1, -1));
        }

        // No more messages to be transmitted
        assertFalse(validateMessage(testAssociation1, MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD, -1, -1));
        assertFalse(validateMessage(testAssociation2, MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD, -1, -1));

        // Bring down one AS1
        // Lets stop ASP Factory
        aspFactoryImpl1.stop();

        ASPDownAck aspDownAck = (ASPDownAck) messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
                MessageType.ASP_DOWN_ACK);
        aspFactoryImpl1.read(aspDownAck);

        // lets wait for 3 seconds to receive the MTP3 primitive before giving
        // up. We know Pending timeout is 2 secs
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);
        // PAUSE for DPC 2
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNull(mtp3Primitive);

        // Lets send the Payload again and this time it will be always go from AS2
        testAssociation1.clearRxMessages();
        testAssociation2.clearRxMessages();

        for (int sls = 0; sls < 256; sls++) {
            Mtp3TransferPrimitive mtp3TransferPrimitive = factory.createMtp3TransferPrimitive(3, 1, 0, 1, 2, sls, new byte[] {
                    1, 2, 3, 4 });
            clientM3UAMgmt.sendMessage(mtp3TransferPrimitive);
        }

        for (int count = 0; count < 256; count++) {
            assertTrue(validateMessage(testAssociation2, MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD, -1, -1));
        }

        assertFalse(validateMessage(testAssociation1, MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD, -1, -1));
        assertFalse(validateMessage(testAssociation2, MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD, -1, -1));

        // Bring down one AS2
        // Lets stop ASP Factory
        aspFactoryImpl2.stop();
        aspFactoryImpl2.read(aspDownAck);

        // lets wait for 3 seconds to receive the MTP3 primitive before giving
        // up. We know Pending timeout is 2 secs
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);
        // PAUSE for DPC 2
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
    }

    @Test
    public void testSingleAspInMultipleAs() throws Exception {
        // 5.1.1.3. Single ASP in Multiple Application Servers (Each with "1+0"
        // Sparing)
        int m3uaManagementEventsSeq = 0;
        this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc1");

        // Define 1st AS
        RoutingContext rc1 = parmFactory.createRoutingContext(new long[] { 100 });

        AsImpl remAs1 = (AsImpl) this.clientM3UAMgmt.createAs("testas1", Functionality.AS, ExchangeType.SE, null, rc1, null, 1,
                null);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsCreated, System
                .currentTimeMillis(), new Object[] { remAs1 }, m3uaManagementEventsSeq++)));

        // Define 2nd AS
        RoutingContext rc2 = parmFactory.createRoutingContext(new long[] { 200 });

        AsImpl remAs2 = (AsImpl) clientM3UAMgmt
                .createAs("testas2", Functionality.AS, ExchangeType.SE, null, rc2, null, 1, null);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsCreated, System
                .currentTimeMillis(), new Object[] { remAs2 }, m3uaManagementEventsSeq++)));

        AspFactoryImpl aspFactoryImpl = (AspFactoryImpl) clientM3UAMgmt.createAspFactory("testasp", "testAssoc1", false);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspFactoryCreated, System
                .currentTimeMillis(), new Object[] { aspFactoryImpl }, m3uaManagementEventsSeq++)));

        // TODO : Call start from management
        aspFactoryImpl.start();

        // Both ASP uses same underlying M3UAChannel
        AspImpl remAsp1 = clientM3UAMgmt.assignAspToAs("testas1", "testasp");
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspAssignedToAs, System
                .currentTimeMillis(), new Object[] { remAs1, remAsp1 }, m3uaManagementEventsSeq++)));

        AspImpl remAsp2 = clientM3UAMgmt.assignAspToAs("testas2", "testasp");
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspAssignedToAs, System
                .currentTimeMillis(), new Object[] { remAs2, remAsp2 }, m3uaManagementEventsSeq++)));

        // Create Route
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas1");
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas2");

        this.clientM3UAMgmt.addRoute(3, -1, -1, "testas1");
        this.clientM3UAMgmt.addRoute(3, -1, -1, "testas2");

        // Signal for Communication UP
        TestAssociation testAssociation = (TestAssociation) this.transportManagement.getAssociation("testAssoc1");
        testAssociation.signalCommUp();

        FSM asp1LocalFSM = remAsp1.getLocalFSM();
        FSM asp2LocalFSM = remAsp2.getLocalFSM();

        assertNull(remAsp1.getPeerFSM());
        assertNull(remAsp2.getPeerFSM());

        assertEquals(AspState.UP_SENT, this.getAspState(asp1LocalFSM));
        assertEquals(remAsp1.getState().getName(), State.STATE_DOWN);

        assertEquals(AspState.UP_SENT, this.getAspState(asp2LocalFSM));
        assertEquals(remAsp2.getState().getName(), State.STATE_DOWN);

        // Once communication is UP, ASP_UP should have been sent.
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));

        FSM as1PeerFSM = remAs1.getPeerFSM();
        FSM as2PeerFSM = remAs2.getPeerFSM();

        assertNull(remAs1.getLocalFSM());
        assertNull(remAs2.getLocalFSM());

        // Both the AS is still DOWN
        assertEquals(AsState.DOWN, this.getAsState(as1PeerFSM));
        assertEquals(AsState.DOWN, this.getAsState(as2PeerFSM));

        // The other side will send ASP_UP_ACK and after that NTFY(AS-INACTIVE)
        // for both the AS
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        aspFactoryImpl.read(message);

        assertEquals(AspState.ACTIVE_SENT, this.getAspState(asp1LocalFSM));
        assertEquals(remAsp1.getState().getName(), State.STATE_INACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspInactive, System
                .currentTimeMillis(), new Object[] { remAsp1 }, m3uaManagementEventsSeq++)));

        assertEquals(AspState.ACTIVE_SENT, this.getAspState(asp2LocalFSM));
        assertEquals(remAsp2.getState().getName(), State.STATE_INACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspInactive, System
                .currentTimeMillis(), new Object[] { remAsp2 }, m3uaManagementEventsSeq++)));

        // ASP_ACTIVE for both ASP in txQueue
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));

        Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc1);
        Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        aspFactoryImpl.read(notify);

        // the AS1 should be INACTIVE now but AS2 still DOWN
        assertEquals(AsState.INACTIVE, this.getAsState(as1PeerFSM));
        assertEquals(remAs1.getState().getName(), State.STATE_INACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsInactive, System
                .currentTimeMillis(), new Object[] { remAs1 }, m3uaManagementEventsSeq++)));

        assertEquals(AsState.DOWN, this.getAsState(as2PeerFSM));
        assertEquals(remAs2.getState().getName(), State.STATE_DOWN);

        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc2);// RC 200
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        aspFactoryImpl.read(notify);

        // AS2 should be INACTIVE now
        assertEquals(AsState.INACTIVE, this.getAsState(as2PeerFSM));
        assertEquals(remAs2.getState().getName(), State.STATE_INACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsInactive, System
                .currentTimeMillis(), new Object[] { remAs2 }, m3uaManagementEventsSeq++)));

        // The other side will send ASP_ACTIVE_ACK and after that
        // NTFY(AS-ACTIVE)
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(this.parmFactory.createRoutingContext(new long[] { 100, 200 }));
        aspFactoryImpl.read(aspActiveAck);

        // Both ASP are ACTIVE now
        assertEquals(AspState.ACTIVE, this.getAspState(asp1LocalFSM));
        assertEquals(remAsp1.getState().getName(), State.STATE_ACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspActive, System
                .currentTimeMillis(), new Object[] { remAsp1 }, m3uaManagementEventsSeq++)));

        assertEquals(AspState.ACTIVE, this.getAspState(asp2LocalFSM));
        assertEquals(remAsp2.getState().getName(), State.STATE_ACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspActive, System
                .currentTimeMillis(), new Object[] { remAsp2 }, m3uaManagementEventsSeq++)));

        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc1);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        aspFactoryImpl.read(notify);

        // the AS1 should be ACTIVE now but AS2 still INACTIVE
        assertEquals(AsState.ACTIVE, this.getAsState(as1PeerFSM));
        assertEquals(remAs1.getState().getName(), State.STATE_ACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsActive, System
                .currentTimeMillis(), new Object[] { remAs1 }, m3uaManagementEventsSeq++)));

        assertEquals(AsState.INACTIVE, this.getAsState(as2PeerFSM));
        assertEquals(remAs2.getState().getName(), State.STATE_INACTIVE);

        // Check if MTP3 RESUME received
        // lets wait for 2second to receive the MTP3 primitive before giving up
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);
        // RESUME for DPC 2
        Mtp3Primitive mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());

        // lets wait for 2second to receive the MTP3 primitive before giving up
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);
        // RESUME for DPC 3
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(3, mtp3Primitive.getAffectedDpc());

        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Check the TrafficMode for AS1
        assertEquals(TrafficModeType.Loadshare, remAs1.getTrafficModeType().getMode());

        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc2);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        aspFactoryImpl.read(notify);

        // the AS2 is also ACTIVE now
        assertEquals(AsState.ACTIVE, this.getAsState(as1PeerFSM));
        assertEquals(AsState.ACTIVE, this.getAsState(as2PeerFSM));
        assertEquals(remAs2.getState().getName(), State.STATE_ACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsActive, System
                .currentTimeMillis(), new Object[] { remAs2 }, m3uaManagementEventsSeq++)));

        // But RESUME shouldn't have been fired
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Check the TrafficMode for AS2
        assertEquals(TrafficModeType.Loadshare, remAs2.getTrafficModeType().getMode());

        // Lets stop ASP Factory
        aspFactoryImpl.stop();

        assertEquals(AspState.DOWN_SENT, this.getAspState(asp1LocalFSM));
        assertEquals(remAsp1.getState().getName(), State.STATE_DOWN);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspDown, System
                .currentTimeMillis(), new Object[] { remAsp1 }, m3uaManagementEventsSeq++)));

        assertEquals(remAs1.getState().getName(), State.STATE_PENDING);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsPending, System
                .currentTimeMillis(), new Object[] { remAs1 }, m3uaManagementEventsSeq++)));

        assertEquals(AspState.DOWN_SENT, this.getAspState(asp2LocalFSM));
        assertEquals(remAsp2.getState().getName(), State.STATE_DOWN);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspDown, System
                .currentTimeMillis(), new Object[] { remAsp2 }, m3uaManagementEventsSeq++)));

        assertEquals(remAs2.getState().getName(), State.STATE_PENDING);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsPending, System
                .currentTimeMillis(), new Object[] { remAs2 }, m3uaManagementEventsSeq++)));

        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));
        // also the both AS is PENDING
        assertEquals(AsState.PENDING, this.getAsState(as1PeerFSM));
        assertEquals(AsState.PENDING, this.getAsState(as2PeerFSM));

        // lets wait for 3 seconds to receive the MTP3 primitive before giving
        // up. We know Pending timeout is 2 secs
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);
        // PAUSE for DPC 2
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());

        // lets wait for 3 seconds to receive the MTP3 primitive before giving
        // up. We know Pending timeout is 2 secs
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);
        // PAUSE for DPC 3
        mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.PAUSE, mtp3Primitive.getType());
        assertEquals(3, mtp3Primitive.getAffectedDpc());

        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Make sure we don't have any more
        assertNull(testAssociation.txPoll());

        // also the AS is DOWN
        assertEquals(AsState.DOWN, this.getAsState(as1PeerFSM));
        assertEquals(remAs1.getState().getName(), State.STATE_DOWN);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsDown, System
                .currentTimeMillis(), new Object[] { remAs1 }, m3uaManagementEventsSeq++)));

        // TODO : Below test is failing. Fix it
        // assertEquals(this.getAsState(as2PeerFSM), AsState.DOWN);
        // assertEquals(remAs2.getState().getName(), State.STATE_DOWN);
        // assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new
        // TestEvent(TestEventType.AsDown, System
        // .currentTimeMillis(), new Object[] { remAs2 },
        // m3uaManagementEventsSeq++)));

        ASPDownAck aspDownAck = (ASPDownAck) messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
                MessageType.ASP_DOWN_ACK);
        aspFactoryImpl.read(aspDownAck);
    }

    @Test
    public void testTwoAspInAsOverride() throws Exception {
        // 5.1.2. Two ASPs in Application Server ("1+1" Sparing)

        int m3uaManagementEventsSeq = 0;

        TestAssociation testAssociation1 = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0,
                "testAssoc1");
        TestAssociation testAssociation2 = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0,
                "testAssoc2");

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

        TrafficModeType trModType = parmFactory.createTrafficModeType(TrafficModeType.Override);

        // As remAs = rsgw.createAppServer("testas", rc, rKey, trModType);
        AsImpl remAs = (AsImpl) this.clientM3UAMgmt.createAs("testas", Functionality.AS, ExchangeType.SE, null, rc, null, 1,
                null);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsCreated, System
                .currentTimeMillis(), new Object[] { remAs }, m3uaManagementEventsSeq++)));

        AspFactoryImpl aspFactory1 = (AspFactoryImpl) this.clientM3UAMgmt.createAspFactory("testasp1", "testAssoc1", false);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspFactoryCreated, System
                .currentTimeMillis(), new Object[] { aspFactory1 }, m3uaManagementEventsSeq++)));

        // TODO : Use Management to start asp
        aspFactory1.start();

        AspFactoryImpl aspFactory2 = (AspFactoryImpl) this.clientM3UAMgmt.createAspFactory("testasp2", "testAssoc2", false);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspFactoryCreated, System
                .currentTimeMillis(), new Object[] { aspFactory2 }, m3uaManagementEventsSeq++)));

        // TODO : Use Management to start asp
        aspFactory2.start();

        AspImpl remAsp1 = clientM3UAMgmt.assignAspToAs("testas", "testasp1");
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspAssignedToAs, System
                .currentTimeMillis(), new Object[] { remAs, remAsp1 }, m3uaManagementEventsSeq++)));

        AspImpl remAsp2 = clientM3UAMgmt.assignAspToAs("testas", "testasp2");
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspAssignedToAs, System
                .currentTimeMillis(), new Object[] { remAs, remAsp2 }, m3uaManagementEventsSeq++)));

        // Create Route
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas");

        FSM asp1LocalFSM = remAsp1.getLocalFSM();
        FSM asp2LocalFSM = remAsp2.getLocalFSM();

        assertNull(remAsp1.getPeerFSM());
        assertNull(remAsp2.getPeerFSM());

        // Check for Communication UP for ASP1
        testAssociation1.signalCommUp();
        assertEquals(AspState.UP_SENT, this.getAspState(asp1LocalFSM));
        // ASP_UP should have been sent.
        assertTrue(validateMessage(testAssociation1, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));
        // But AS is still Down
        FSM asPeerFSM = remAs.getPeerFSM();

        assertEquals(AsState.DOWN, this.getAsState(asPeerFSM));
        assertEquals(remAsp1.getState().getName(), State.STATE_DOWN);

        // Far end send ASP_UP_ACK and NTFY
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        aspFactory1.read(message);

        assertEquals(AspState.ACTIVE_SENT, this.getAspState(asp1LocalFSM));
        assertEquals(remAsp1.getState().getName(), State.STATE_INACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspInactive, System
                .currentTimeMillis(), new Object[] { remAsp1 }, m3uaManagementEventsSeq++)));
        assertTrue(validateMessage(testAssociation1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));

        Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        aspFactory1.read(notify);
        // the AS1 should be INACTIVE
        assertEquals(AsState.INACTIVE, this.getAsState(asPeerFSM));
        assertEquals(remAs.getState().getName(), State.STATE_INACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsInactive, System
                .currentTimeMillis(), new Object[] { remAs }, m3uaManagementEventsSeq++)));

        // The other side will send ASP_ACTIVE_ACK and after that
        // NTFY(AS-ACTIVE)
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        aspActiveAck.setTrafficModeType(trModType);
        aspFactory1.read(aspActiveAck);

        assertEquals(AspState.ACTIVE, this.getAspState(asp1LocalFSM));
        assertEquals(remAsp1.getState().getName(), State.STATE_ACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspActive, System
                .currentTimeMillis(), new Object[] { remAsp1 }, m3uaManagementEventsSeq++)));

        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        aspFactory1.read(notify);
        aspFactory2.read(notify);

        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));
        assertEquals(remAs.getState().getName(), State.STATE_ACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsActive, System
                .currentTimeMillis(), new Object[] { remAs }, m3uaManagementEventsSeq++)));

        // Check if MTP3 RESUME received
        // lets wait for 2second to receive the MTP3 primitive before giving up
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);

        Mtp3Primitive mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Communication UP for ASP2
        testAssociation2.signalCommUp();
        assertEquals(AspState.UP_SENT, this.getAspState(asp2LocalFSM));
        assertEquals(remAsp2.getState().getName(), State.STATE_DOWN);

        // ASP_UP should have been sent.
        assertTrue(validateMessage(testAssociation2, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));
        // Far end send ASP_UP_ACK
        message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        aspFactory2.read(message);

        // ASP2 now is INACTIVE as ASP1 is still ACTIVATING
        assertEquals(AspState.INACTIVE, this.getAspState(asp2LocalFSM));
        assertEquals(remAsp2.getState().getName(), State.STATE_INACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspInactive, System
                .currentTimeMillis(), new Object[] { remAsp2 }, m3uaManagementEventsSeq++)));

        // Bring down ASP1
        // 5.2.1. 1+1 Sparing, Withdrawal of ASP, Backup Override
        testAssociation1.signalCommLost();
        // the ASP is DOWN and AS goes in PENDING STATE
        assertEquals(AspState.DOWN, this.getAspState(asp1LocalFSM));
        assertEquals(remAsp1.getState().getName(), State.STATE_DOWN);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspDown, System
                .currentTimeMillis(), new Object[] { remAsp1 }, m3uaManagementEventsSeq++)));

        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));
        assertEquals(remAs.getState().getName(), State.STATE_PENDING);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsPending, System
                .currentTimeMillis(), new Object[] { remAs }, m3uaManagementEventsSeq++)));

        // Aslo the ASP_ACTIVE for ASP2 should have been sent
        assertEquals(AspState.ACTIVE_SENT, this.getAspState(asp2LocalFSM));
        assertTrue(validateMessage(testAssociation2, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));

        // We will not get Alternate ASP Active as this ASP's channel is dead
        // The other side will send ASP_ACTIVE_ACK and after that
        // NTFY(AS-ACTIVE)
        aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        aspFactory2.read(aspActiveAck);

        assertEquals(AspState.ACTIVE, this.getAspState(asp2LocalFSM));
        assertEquals(remAsp2.getState().getName(), State.STATE_ACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspActive, System
                .currentTimeMillis(), new Object[] { remAsp2 }, m3uaManagementEventsSeq++)));

        // We should get Notify that AS is ACTIVE
        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        aspFactory2.read(notify);

        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));
        assertEquals(remAs.getState().getName(), State.STATE_ACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsActive, System
                .currentTimeMillis(), new Object[] { remAs }, m3uaManagementEventsSeq++)));

        assertNull(testAssociation1.txPoll());
        assertNull(testAssociation2.txPoll());

        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Lets stop ASP Factory
        aspFactory1.stop();
        aspFactory2.stop();

        assertEquals(AspState.DOWN_SENT, this.getAspState(asp2LocalFSM));
        assertTrue(validateMessage(testAssociation2, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));
        // also the AS is PENDING
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));

        // The far end sends DOWN_ACK
        message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK);
        aspFactory2.read(message);

    }
    
    @Test
    public void testTwoAspInAsLoadShareDifferentAs() throws Exception {
        // 5.1.3.  Two ASPs in an Application Server ("1+1" Sparing, Loadsharing Case)
        // However in this case we are assuming AS are from different VM, and if AS NTFY(ACTIVE) received, 
        // ASP should accept and change status to start traffic
        int m3uaManagementEventsSeq = 0;

        TestAssociation testAssociation1 = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0,
                "testAssoc1");

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

        TrafficModeType trModType = parmFactory.createTrafficModeType(TrafficModeType.Loadshare);

        // As remAs = rsgw.createAppServer("testas", rc, rKey, trModType);
        AsImpl remAs = (AsImpl) this.clientM3UAMgmt.createAs("testas", Functionality.AS, ExchangeType.SE, null, rc, null, 1,
                null);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsCreated, System
                .currentTimeMillis(), new Object[] { remAs }, m3uaManagementEventsSeq++)));

        AspFactoryImpl aspFactory1 = (AspFactoryImpl) this.clientM3UAMgmt.createAspFactory("testasp1", "testAssoc1", false);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspFactoryCreated, System
                .currentTimeMillis(), new Object[] { aspFactory1 }, m3uaManagementEventsSeq++)));

        // TODO : Use Management to start asp
        aspFactory1.start();

        AspImpl remAsp1 = clientM3UAMgmt.assignAspToAs("testas", "testasp1");
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspAssignedToAs, System
                .currentTimeMillis(), new Object[] { remAs, remAsp1 }, m3uaManagementEventsSeq++)));

        // Create Route
        this.clientM3UAMgmt.addRoute(2, -1, -1, "testas");

        FSM asp1LocalFSM = remAsp1.getLocalFSM();

        assertNull(remAsp1.getPeerFSM());

        // Check for Communication UP for ASP1
        testAssociation1.signalCommUp();
        assertEquals(AspState.UP_SENT, this.getAspState(asp1LocalFSM));
        // ASP_UP should have been sent.
        assertTrue(validateMessage(testAssociation1, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));
        // But AS is still Down
        FSM asPeerFSM = remAs.getPeerFSM();

        assertEquals(AsState.DOWN, this.getAsState(asPeerFSM));
        assertEquals(remAsp1.getState().getName(), State.STATE_DOWN);

        // Far end send ASP_UP_ACK and NTFY
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        aspFactory1.read(message);

        assertEquals(AspState.ACTIVE_SENT, this.getAspState(asp1LocalFSM));
        assertEquals(remAsp1.getState().getName(), State.STATE_INACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspInactive, System
                .currentTimeMillis(), new Object[] { remAsp1 }, m3uaManagementEventsSeq++)));
        assertTrue(validateMessage(testAssociation1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));

        Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        aspFactory1.read(notify);
        // the AS1 should be ACTIVE
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));
        assertEquals(remAs.getState().getName(), State.STATE_ACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsActive, System
                .currentTimeMillis(), new Object[] { remAs }, m3uaManagementEventsSeq++)));

        // The other side will send ASP_ACTIVE_ACK and after that
        // NTFY(AS-ACTIVE)
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        aspActiveAck.setTrafficModeType(trModType);
        aspFactory1.read(aspActiveAck);

        assertEquals(AspState.ACTIVE, this.getAspState(asp1LocalFSM));
        assertEquals(remAsp1.getState().getName(), State.STATE_ACTIVE);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspActive, System
                .currentTimeMillis(), new Object[] { remAsp1 }, m3uaManagementEventsSeq++)));

       
        // Check if MTP3 RESUME received
        // lets wait for 2second to receive the MTP3 primitive before giving up
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);

        Mtp3Primitive mtp3Primitive = this.mtp3UserPartListener.rxMtp3PrimitivePoll();
        assertNotNull(mtp3Primitive);
        assertEquals(Mtp3Primitive.RESUME, mtp3Primitive.getType());
        assertEquals(2, mtp3Primitive.getAffectedDpc());
        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Bring down ASP1
        // 5.2.1. 1+1 Sparing, Withdrawal of ASP, Backup Override
        testAssociation1.signalCommLost();
        // the ASP is DOWN and AS goes in PENDING STATE
        assertEquals(AspState.DOWN, this.getAspState(asp1LocalFSM));
        assertEquals(remAsp1.getState().getName(), State.STATE_DOWN);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AspDown, System
                .currentTimeMillis(), new Object[] { remAsp1 }, m3uaManagementEventsSeq++)));

        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));
        assertEquals(remAs.getState().getName(), State.STATE_PENDING);
        assertTrue(this.m3uaManagementEventListenerImpl.validateEvent(new TestEvent(TestEventType.AsPending, System
                .currentTimeMillis(), new Object[] { remAs }, m3uaManagementEventsSeq++)));

        // We will not get Alternate ASP Active as this ASP's channel is dead
        // The other side will send ASP_ACTIVE_ACK and after that
        // NTFY(AS-ACTIVE)
        aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);

        assertNull(testAssociation1.txPoll());

        // No more MTP3 Primitive or message
        assertNull(this.mtp3UserPartListener.rxMtp3PrimitivePoll());
        assertNull(this.mtp3UserPartListener.rxMtp3TransferPrimitivePoll());

        // Lets stop ASP Factory
        aspFactory1.stop();


    }    

    @Test
    public void testPendingQueue() throws Exception {

        TestAssociation testAssociation = (TestAssociation) this.transportManagement.addAssociation(null, 0, null, 0,
                "testAssoc");

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

        // As as = rsgw.createAppServer("testas", rc, rKey, trModType);
        AsImpl asImpl = (AsImpl) this.clientM3UAMgmt.createAs("testas", Functionality.AS, ExchangeType.SE, null, rc, null, 1,
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

        // The other side will send ASP_UP_ACK and after that NTFY(AS-INACTIVE)
        M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        localAspFactory.read(message);

        Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);

        assertEquals(AspState.ACTIVE_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
        // also the AS should be INACTIVE now
        assertEquals(AsState.INACTIVE, this.getAsState(asPeerFSM));

        // The other side will send ASP_ACTIVE_ACK and after that
        // NTFY(AS-ACTIVE)
        ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        localAspFactory.read(aspActiveAck);

        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);

        assertEquals(AspState.ACTIVE, this.getAspState(aspLocalFSM));
        // also the AS should be ACTIVE now
        assertEquals(AsState.ACTIVE, this.getAsState(asPeerFSM));

        // Check if MTP3 RESUME received
        // lets wait for 2second to receive the MTP3 primitive before giving up
        semaphore.tryAcquire(5000, TimeUnit.MILLISECONDS);

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

        // The other side will send ASP_UP_ACK and after that NTFY(AS-INACTIVE)
        message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        localAspFactory.read(message);

        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);

        assertEquals(AspState.ACTIVE_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
        // AS should still be PENDING
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));

        // The other side will send ASP_ACTIVE_ACK and after that
        // NTFY(AS-ACTIVE)
        aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);
        aspActiveAck.setRoutingContext(rc);
        localAspFactory.read(aspActiveAck);

        notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        notify.setRoutingContext(rc);
        status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        notify.setStatus(status);
        localAspFactory.read(notify);

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

        // Lets stop ASP Factory
        localAspFactory.stop();

        assertEquals(AspState.DOWN_SENT, this.getAspState(aspLocalFSM));
        assertTrue(validateMessage(testAssociation, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));
        // also the AS is PENDING
        assertEquals(AsState.PENDING, this.getAsState(asPeerFSM));

        // The far end sends DOWN_ACK
        message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK);
        localAspFactory.read(message);

    }

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

        // Is the Association been started by management?
        private volatile boolean started = false;
        // Is the Association up (connection is established)
        protected volatile boolean up = false;

        private AssociationListener associationListener = null;
        private String name = null;
        private LinkedList<M3UAMessage> messageRxFromUserPart = new LinkedList<M3UAMessage>();

        TestAssociation(String name) {
            this.name = name;
        }

        void clearRxMessages() {
            this.messageRxFromUserPart.clear();
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
            return this.name;
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
            return this.started;
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
            this.started = true;
            this.up = true;
            this.associationListener.onCommunicationUp(this, 1, 1);
        }

        public void signalCommLost() {
            this.up = false;
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

        /*
         * (non-Javadoc)
         *
         * @see org.mobicents.protocols.api.Association#getExtraHostAddresses()
         */
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
            return this.started && this.up;
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

        protected void stop() {
            this.started = false;
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
        public void stopAssociation(String assocName) throws Exception {
            Association association = this.associations.get(assocName);
            ((TestAssociation) association).stop();
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

    private class M3UAManagementEventListenerImpl implements M3UAManagementEventListener {

        private FastList<TestEvent> testEvents = new FastList<TestEvent>();
        private int sequence = 0;

        @Override
        public void onAsCreated(As as) {
            TestEvent testEvent = new TestEvent(TestEventType.AsCreated, System.currentTimeMillis(), new Object[] { as },
                    sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onAsDestroyed(As as) {
            TestEvent testEvent = new TestEvent(TestEventType.AsDestroyed, System.currentTimeMillis(), new Object[] { as },
                    sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onAspFactoryCreated(AspFactory aspFactory) {
            TestEvent testEvent = new TestEvent(TestEventType.AspFactoryCreated, System.currentTimeMillis(),
                    new Object[] { aspFactory }, sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onAspFactoryDestroyed(AspFactory aspFactory) {
            TestEvent testEvent = new TestEvent(TestEventType.AspFactoryDestroyed, System.currentTimeMillis(),
                    new Object[] { aspFactory }, sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onAspAssignedToAs(As as, Asp asp) {
            TestEvent testEvent = new TestEvent(TestEventType.AspAssignedToAs, System.currentTimeMillis(), new Object[] { as,
                    asp }, sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onAspUnassignedFromAs(As as, Asp asp) {
            TestEvent testEvent = new TestEvent(TestEventType.AspUnassignedFromAs, System.currentTimeMillis(), new Object[] {
                    as, asp }, sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onRemoveAllResources() {
            TestEvent testEvent = new TestEvent(TestEventType.RemoveAllResources, System.currentTimeMillis(), null, sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onAspFactoryStarted(AspFactory aspFactory) {
            TestEvent testEvent = new TestEvent(TestEventType.AspFactoryStarted, System.currentTimeMillis(),
                    new Object[] { aspFactory }, sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onAspFactoryStopped(AspFactory aspFactory) {
            TestEvent testEvent = new TestEvent(TestEventType.AspFactoryStopped, System.currentTimeMillis(),
                    new Object[] { aspFactory }, sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onAspActive(Asp asp, State oldState) {
            TestEvent testEvent = new TestEvent(TestEventType.AspActive, System.currentTimeMillis(), new Object[] { asp },
                    sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onAspInactive(Asp asp, State oldState) {
            TestEvent testEvent = new TestEvent(TestEventType.AspInactive, System.currentTimeMillis(), new Object[] { asp },
                    sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onAspDown(Asp asp, State oldState) {
            TestEvent testEvent = new TestEvent(TestEventType.AspDown, System.currentTimeMillis(), new Object[] { asp },
                    sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onAsActive(As as, State oldState) {
            TestEvent testEvent = new TestEvent(TestEventType.AsActive, System.currentTimeMillis(), new Object[] { as },
                    sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onAsPending(As as, State oldState) {
            TestEvent testEvent = new TestEvent(TestEventType.AsPending, System.currentTimeMillis(), new Object[] { as },
                    sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onAsInactive(As as, State oldState) {
            TestEvent testEvent = new TestEvent(TestEventType.AsInactive, System.currentTimeMillis(), new Object[] { as },
                    sequence++);
            this.testEvents.add(testEvent);
        }

        @Override
        public void onAsDown(As as, State oldState) {
            TestEvent testEvent = new TestEvent(TestEventType.AsDown, System.currentTimeMillis(), new Object[] { as },
                    sequence++);
            this.testEvents.add(testEvent);
        }

        public boolean validateEvent(TestEvent testEventExpected) {

            TestEvent testEventActual = this.testEvents.removeFirst();

            if (testEventActual == null) {
                return false;
            }

            return testEventExpected.equals(testEventActual);
        }

        @Override
        public void onServiceStarted() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onServiceStopped() {
            // TODO Auto-generated method stub

        }
    }
}
