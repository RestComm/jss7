/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.m3ua.impl.sg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AsState;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.CommunicationListener.CommunicationState;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.asptm.ASPActiveImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.asptm.ASPInactiveImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.transfer.PayloadDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ProtocolDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.tcp.TcpProvider;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.ServiceIndicators;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * 
 * @author amit bhayani
 * 
 */
public class SgFSMTest {

	private ParameterFactoryImpl parmFactory = new ParameterFactoryImpl();
	private MessageFactoryImpl messageFactory = new MessageFactoryImpl();
	private M3UAProvider provider = TcpProvider.provider();

	public SgFSMTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {

	}

	@After
	public void tearDown() {
	}

	@Test
	public void testSingleAspInAs() throws Exception {
		// 5.1.1. Single ASP in an Application Server ("1+0" sparing),

		SgpImpl sgw = new SgpImpl("127.0.0.1", 1112);
		sgw.start();

		RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

		DestinationPointCode[] dpc = new DestinationPointCode[] { parmFactory
				.createDestinationPointCode(123, (short) 0) };

		ServiceIndicators[] servInds = new ServiceIndicators[] { parmFactory.createServiceIndicators(new short[] { 3 }) };

		TrafficModeType trModType = parmFactory.createTrafficModeType(TrafficModeType.Override);
		LocalRKIdentifier lRkId = parmFactory.createLocalRKIdentifier(1);
		RoutingKey rKey = parmFactory.createRoutingKey(lRkId, rc, null, null, dpc, servInds, null);

		// As remAs = sgw.createAppServer("testas", rc, rKey, trModType);
		As remAs = sgw
				.createAppServer("m3ua ras create rc 100 rk dpc 123 si 3 traffic-mode override testas".split(" "));
		// AspFactory aspFactory = sgw.createAspFactory("testasp", "127.0.0.1",
		// 2777);
		AspFactory aspFactory = sgw.createAspFactory("m3ua rasp create ip 127.0.0.1 port 2777 testasp".split(" "));

		Asp remAsp = sgw.assignAspToAs("testas", "testasp");

		// Check for Communication UP
		aspFactory.onCommStateChange(CommunicationState.UP);

		assertEquals(AspState.DOWN, remAsp.getState());

		// Check for ASP_UP
		M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
		aspFactory.read(message);

		assertEquals(AspState.INACTIVE, remAsp.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
		// also the AS should be INACTIVE now
		assertEquals(AsState.INACTIVE, remAs.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE));

		// Check for ASP_ACTIVE
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		((ASPActiveImpl) message).setRoutingContext(rc);
		aspFactory.read(message);
		assertEquals(AspState.ACTIVE, remAsp.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1, -1));
		// also the AS should be ACTIVE now
		assertEquals(AsState.ACTIVE, remAs.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE));

		// Check for ASP_INACTIVE
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE);
		aspFactory.read(message);
		assertEquals(AspState.INACTIVE, remAsp.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE_ACK, -1,
				-1));
		// also the AS should be PENDING now
		assertEquals(AsState.PENDING, remAs.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_PENDING));

		// Check for ASP_DOWN
		message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN);
		aspFactory.read(message);
		assertEquals(AspState.DOWN, remAsp.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK, -1, -1));

		// Make sure we don't have any more
		assertNull(aspFactory.txPoll());

		sgw.stop();
	}

	@Test
	public void testSingleAspInMultipleAs() throws Exception {
		// 5.1.1.3. Single ASP in Multiple Application Servers (Each with "1+0"
		// Sparing)

		SgpImpl sgw = new SgpImpl("127.0.0.1", 1112);
		sgw.start();

		// Define 1st AS
		RoutingContext rc1 = parmFactory.createRoutingContext(new long[] { 100 });

		DestinationPointCode[] dpc1 = new DestinationPointCode[] { parmFactory.createDestinationPointCode(123,
				(short) 0) };

		ServiceIndicators[] servInds1 = new ServiceIndicators[] { parmFactory
				.createServiceIndicators(new short[] { 3 }) };

		TrafficModeType trModType1 = parmFactory.createTrafficModeType(TrafficModeType.Override);
		LocalRKIdentifier lRkId1 = parmFactory.createLocalRKIdentifier(1);
		RoutingKey rKey1 = parmFactory.createRoutingKey(lRkId1, rc1, null, null, dpc1, servInds1, null);

		// As remAs1 = sgw.createAppServer("testas1", rc1, rKey1, trModType1);
		As remAs1 = sgw.createAppServer("m3ua ras create rc 100 rk dpc 123 si 3 traffic-mode override testas1"
				.split(" "));

		// Define 2nd AS
		RoutingContext rc2 = parmFactory.createRoutingContext(new long[] { 200 });

		DestinationPointCode[] dpc2 = new DestinationPointCode[] { parmFactory.createDestinationPointCode(124,
				(short) 0) };

		ServiceIndicators[] servInds2 = new ServiceIndicators[] { parmFactory
				.createServiceIndicators(new short[] { 3 }) };

		TrafficModeType trModType2 = parmFactory.createTrafficModeType(TrafficModeType.Override);
		LocalRKIdentifier lRkId2 = parmFactory.createLocalRKIdentifier(1);
		RoutingKey rKey2 = parmFactory.createRoutingKey(lRkId2, rc2, null, null, dpc2, servInds2, null);

		// As remAs2 = sgw.createAppServer("testas2", rc2, rKey2, trModType2);
		As remAs2 = sgw.createAppServer("m3ua ras create rc 200 rk dpc 124 si 3 traffic-mode override testas2"
				.split(" "));

		// AspFactory aspFactory = sgw.createAspFactory("testasp", "127.0.0.1",
		// 2777);
		AspFactory aspFactory = sgw.createAspFactory("m3ua rasp create ip 127.0.0.1 port 2777 testasp".split(" "));

		// Both ASP uses same underlying M3UAChannel
		Asp remAsp1 = sgw.assignAspToAs("testas1", "testasp");
		Asp remAsp2 = sgw.assignAspToAs("testas2", "testasp");

		// Check for Communication UP
		aspFactory.onCommStateChange(CommunicationState.UP);
		assertEquals(AspState.DOWN, remAsp1.getState());
		assertEquals(AspState.DOWN, remAsp2.getState());
		// Both AS are yet DOWN
		assertEquals(AsState.DOWN, remAs1.getState());
		assertEquals(AsState.DOWN, remAs2.getState());

		// Check for ASP_UP
		M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
		aspFactory.read(message);
		assertEquals(AspState.INACTIVE, remAsp1.getState());
		assertEquals(AspState.INACTIVE, remAsp2.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
		// also both the AS should be INACTIVE now
		assertEquals(AsState.INACTIVE, remAs1.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE));
		assertEquals(AsState.INACTIVE, remAs2.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE));

		// Check for ASP_ACTIVE for both Routing Contexts
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		((ASPActiveImpl) message).setRoutingContext(this.parmFactory.createRoutingContext(new long[] { 100, 200 }));
		aspFactory.read(message);
		assertEquals(AspState.ACTIVE, remAsp1.getState());
		assertEquals(AspState.ACTIVE, remAsp2.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1, -1));
		// also both the AS should be ACTIVE now
		assertEquals(AsState.ACTIVE, remAs1.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE));
		// We will have two ACK's one each for each RC
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1, -1));

		assertEquals(AsState.ACTIVE, remAs2.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE));

		// Check for ASP_INACTIVE for ASP1
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE);
		((ASPInactiveImpl) message).setRoutingContext(this.parmFactory.createRoutingContext(new long[] { 100 }));
		aspFactory.read(message);

		assertEquals(AspState.INACTIVE, remAsp1.getState());
		// The ASP2 should still be ACTIVE as we sent ASP_INACTIVE only for 100
		// RC
		assertEquals(AspState.ACTIVE, remAsp2.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE_ACK, -1,
				-1));
		// AS1 should be PENDING now
		assertEquals(AsState.PENDING, remAs1.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_PENDING));

		// But AS2 is still ACTIVE
		assertEquals(AsState.ACTIVE, remAs2.getState());

		// Check for ASP_DOWN
		message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN);
		aspFactory.read(message);
		assertEquals(AspState.DOWN, remAsp1.getState());
		assertEquals(AspState.DOWN, remAsp2.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK, -1, -1));

		// Make sure we don't have any more messages to be sent
		assertNull(aspFactory.txPoll());

		sgw.stop();
	}

	@Test
	public void testTwoAspInAsOverride() throws Exception {
		// 5.1.2. Two ASPs in Application Server ("1+1" Sparing)
		SgpImpl sgw = new SgpImpl("127.0.0.1", 1112);
		sgw.start();

		RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

		DestinationPointCode[] dpc = new DestinationPointCode[] { parmFactory
				.createDestinationPointCode(123, (short) 0) };

		ServiceIndicators[] servInds = new ServiceIndicators[] { parmFactory.createServiceIndicators(new short[] { 3 }) };

		TrafficModeType trModType = parmFactory.createTrafficModeType(TrafficModeType.Override);
		LocalRKIdentifier lRkId = parmFactory.createLocalRKIdentifier(1);
		RoutingKey rKey = parmFactory.createRoutingKey(lRkId, rc, null, null, dpc, servInds, null);

		// As remAs = sgw.createAppServer("testas", rc, rKey, trModType);
		As remAs = sgw
				.createAppServer("m3ua ras create rc 100 rk dpc 123 si 3 traffic-mode override testas".split(" "));

		// AspFactory aspFactory1 = sgw.createAspFactory("testasp1",
		// "127.0.0.1", 2777);
		AspFactory aspFactory1 = sgw.createAspFactory("m3ua rasp create ip 127.0.0.1 port 2777 testasp1".split(" "));

		// AspFactory aspFactory2 = sgw.createAspFactory("testasp2",
		// "127.0.0.1", 2778);
		AspFactory aspFactory2 = sgw.createAspFactory("m3ua rasp create ip 127.0.0.1 port 2778 testasp2".split(" "));

		Asp remAsp1 = sgw.assignAspToAs("testas", "testasp1");
		Asp remAsp2 = sgw.assignAspToAs("testas", "testasp2");

		// Check for Communication UP for ASP1
		aspFactory1.onCommStateChange(CommunicationState.UP);
		assertEquals(AspState.DOWN, remAsp1.getState());

		// Check for Communication UP for ASP2
		aspFactory2.onCommStateChange(CommunicationState.UP);
		assertEquals(AspState.DOWN, remAsp2.getState());

		// Check for ASP_UP for ASP1
		M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
		aspFactory1.read(message);
		assertEquals(AspState.INACTIVE, remAsp1.getState());
		assertTrue(validateMessage(aspFactory1, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
		// also the AS should be INACTIVE now
		assertEquals(AsState.INACTIVE, remAs.getState());
		assertTrue(validateMessage(aspFactory1, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE));

		// Check for ASP_UP for ASP2
		aspFactory2.read(message);
		assertEquals(AspState.INACTIVE, remAsp2.getState());
		assertTrue(validateMessage(aspFactory2, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
		// also the AS should be INACTIVE now
		assertEquals(AsState.INACTIVE, remAs.getState());
		assertTrue(validateMessage(aspFactory2, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE));

		// Check for ASP_ACTIVE for ASP1
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		((ASPActiveImpl) message).setRoutingContext(rc);
		aspFactory1.read(message);
		assertEquals(AspState.ACTIVE, remAsp1.getState());
		assertTrue(validateMessage(aspFactory1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1,
				-1));
		// also the AS should be ACTIVE now and ACTIVE should be delivered to
		// both the ASPs
		assertEquals(AsState.ACTIVE, remAs.getState());
		assertTrue(validateMessage(aspFactory1, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE));
		assertTrue(validateMessage(aspFactory2, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE));

		// INACTIVATE the ASP1
		// 5.2.1. 1+1 Sparing, Withdrawal of ASP, Backup Override
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE);
		aspFactory1.read(message);
		assertEquals(AspState.INACTIVE, remAsp1.getState());
		assertTrue(validateMessage(aspFactory1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE_ACK, -1,
				-1));
		// also the AS should be PENDING now and should send PENDING NTFY to
		// both the ASPS
		assertEquals(AsState.PENDING, remAs.getState());
		assertTrue(validateMessage(aspFactory1, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_PENDING));
		assertTrue(validateMessage(aspFactory2, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_PENDING));

		// ACTIVATE ASP2
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		((ASPActiveImpl) message).setRoutingContext(rc);
		aspFactory2.read(message);
		assertEquals(AspState.ACTIVE, remAsp2.getState());
		assertTrue(validateMessage(aspFactory2, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1,
				-1));
		// also the AS should be ACTIVE now and ACTIVE should be delivered to
		// both the ASPs
		assertEquals(AsState.ACTIVE, remAs.getState());
		assertTrue(validateMessage(aspFactory2, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE));
		assertTrue(validateMessage(aspFactory1, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE));

		// 5.2.2. 1+1 Sparing, Backup Override
		// ACTIVATE ASP1 also
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		((ASPActiveImpl) message).setRoutingContext(rc);
		aspFactory1.read(message);
		assertEquals(AspState.ACTIVE, remAsp1.getState());
		assertTrue(validateMessage(aspFactory1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1,
				-1));
		// The AS remains ACTIVE and sends NTFY(Alt ASP-Act) to ASP2
		assertEquals(AsState.ACTIVE, remAs.getState());

		// ASP2 should get Alternate ASP is active
		assertTrue(validateMessage(aspFactory2, MessageClass.MANAGEMENT, MessageType.NOTIFY, Status.STATUS_Other,
				Status.INFO_Alternate_ASP_Active));
		// The state of ASP2 now should be INACTIVE
		assertEquals(AspState.INACTIVE, remAsp2.getState());

		assertNull(aspFactory1.txPoll());
		assertNull(aspFactory2.txPoll());

		sgw.stop();
	}

	@Test
	public void testTwoAspInAsLoadshare() throws Exception {
		// 5.1.2. Two ASPs in Application Server ("1+1" Sparing)

		SgpImpl sgw = new SgpImpl("127.0.0.1", 1112);
		sgw.start();

		RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

		DestinationPointCode[] dpc = new DestinationPointCode[] { parmFactory
				.createDestinationPointCode(123, (short) 0) };

		ServiceIndicators[] servInds = new ServiceIndicators[] { parmFactory.createServiceIndicators(new short[] { 3 }) };

		TrafficModeType trModType = parmFactory.createTrafficModeType(TrafficModeType.Loadshare);
		LocalRKIdentifier lRkId = parmFactory.createLocalRKIdentifier(1);
		RoutingKey rKey = parmFactory.createRoutingKey(lRkId, rc, null, null, dpc, servInds, null);

		// As remAs = sgw.createAppServer("testas", rc, rKey, trModType);
		As remAs = sgw.createAppServer("m3ua ras create rc 100 rk dpc 123 si 3 traffic-mode loadshare testas"
				.split(" "));
		// 2+0 sparing loadsharing
		remAs.setMinAspActiveForLb(2);

		// AspFactory aspFactory1 = sgw.createAspFactory("testasp1",
		// "127.0.0.1", 2777);
		AspFactory aspFactory1 = sgw.createAspFactory("m3ua rasp create ip 127.0.0.1 port 2777 testasp1".split(" "));

		// AspFactory aspFactory2 = sgw.createAspFactory("testasp2",
		// "127.0.0.1", 2778);
		AspFactory aspFactory2 = sgw.createAspFactory("m3ua rasp create ip 127.0.0.1 port 2778 testasp2".split(" "));

		Asp remAsp1 = sgw.assignAspToAs("testas", "testasp1");
		Asp remAsp2 = sgw.assignAspToAs("testas", "testasp2");

		// Check for Communication UP for ASP1
		aspFactory1.onCommStateChange(CommunicationState.UP);
		assertEquals(AspState.DOWN, remAsp1.getState());

		// Check for Communication UP for ASP2
		aspFactory2.onCommStateChange(CommunicationState.UP);
		assertEquals(AspState.DOWN, remAsp2.getState());

		// Check for ASP_UP for ASP1
		M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
		aspFactory1.read(message);
		assertEquals(AspState.INACTIVE, remAsp1.getState());
		assertTrue(validateMessage(aspFactory1, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
		// also the AS should be INACTIVE now
		assertEquals(AsState.INACTIVE, remAs.getState());
		assertTrue(validateMessage(aspFactory1, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE));

		// Check for ASP_UP for ASP2
		message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
		aspFactory2.read(message);
		assertEquals(AspState.INACTIVE, remAsp2.getState());
		assertTrue(validateMessage(aspFactory2, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
		// also the AS should be INACTIVE
		assertEquals(AsState.INACTIVE, remAs.getState());
		assertTrue(validateMessage(aspFactory2, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE));

		// Check for ASP_ACTIVE for ASP1
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		((ASPActiveImpl) message).setRoutingContext(rc);
		aspFactory1.read(message);
		assertEquals(AspState.ACTIVE, remAsp1.getState());
		assertTrue(validateMessage(aspFactory1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1,
				-1));
		// But AS still INACTIVE as atleast 2 ASP's should be ACTIVE
		assertEquals(AsState.INACTIVE, remAs.getState());

		// Check for ASP_ACTIVE for ASP2
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		((ASPActiveImpl) message).setRoutingContext(rc);
		aspFactory2.read(message);
		assertEquals(AspState.ACTIVE, remAsp2.getState());
		assertTrue(validateMessage(aspFactory2, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1,
				-1));
		// Now AS will be ACTIVE and send NTFY to both the ASP's
		assertEquals(AsState.ACTIVE, remAs.getState());
		assertTrue(validateMessage(aspFactory1, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE));
		assertTrue(validateMessage(aspFactory2, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE));

		// INACTIVATE ASP1.But AS remains ACTIVE in any case
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE);
		aspFactory1.read(message);
		assertEquals(AspState.INACTIVE, remAsp1.getState());
		assertTrue(validateMessage(aspFactory1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE_ACK, -1,
				-1));
		// ASP1 also receives NTFY Ins ASP Resource as we have fallen bellow
		// threshold
		assertTrue(validateMessage(aspFactory1, MessageClass.MANAGEMENT, MessageType.NOTIFY, Status.STATUS_Other,
				Status.INFO_Insufficient_ASP_Resources_Active));
		// AS remains ACTIVE
		assertEquals(AsState.ACTIVE, remAs.getState());

		// Bring down ASP1
		message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN);
		aspFactory1.read(message);
		assertEquals(AspState.DOWN, remAsp1.getState());
		assertTrue(validateMessage(aspFactory1, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK, -1, -1));
		assertNull(aspFactory1.txPoll());
		// AS remains ACTIVE
		assertEquals(AsState.ACTIVE, remAs.getState());

		// INACTIVATE ASP2.Now AS becomes PENDING and sends NTFY to all ASP's in
		// INACTIVE state
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE);
		aspFactory2.read(message);
		assertEquals(AspState.INACTIVE, remAsp2.getState());
		assertTrue(validateMessage(aspFactory2, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE_ACK, -1,
				-1));
		// AS remains ACTIVE
		assertEquals(AsState.PENDING, remAs.getState());
		// AS state change NTFY message
		assertTrue(validateMessage(aspFactory2, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_PENDING));

		assertNull(aspFactory1.txPoll());
		assertNull(aspFactory2.txPoll());

		sgw.stop();

	}

	@Test
	public void testAspUpReceivedWhileASPIsAlreadyUp() throws Exception {
		// Test bug http://code.google.com/p/mobicents/issues/detail?id=2436

		// 4.3.4.1. ASP Up Procedures from http://tools.ietf.org/html/rfc4666

		SgpImpl sgw = new SgpImpl("127.0.0.1", 1112);
		sgw.start();

		RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

		DestinationPointCode[] dpc = new DestinationPointCode[] { parmFactory
				.createDestinationPointCode(123, (short) 0) };

		ServiceIndicators[] servInds = new ServiceIndicators[] { parmFactory.createServiceIndicators(new short[] { 3 }) };

		TrafficModeType trModType = parmFactory.createTrafficModeType(TrafficModeType.Override);
		LocalRKIdentifier lRkId = parmFactory.createLocalRKIdentifier(1);
		RoutingKey rKey = parmFactory.createRoutingKey(lRkId, rc, null, null, dpc, servInds, null);

		// As remAs = sgw.createAppServer("testas", rc, rKey, trModType);
		As remAs = sgw
				.createAppServer("m3ua ras create rc 100 rk dpc 123 si 3 traffic-mode override testas".split(" "));
		// AspFactory aspFactory = sgw.createAspFactory("testasp", "127.0.0.1",
		// 2777);
		AspFactory aspFactory = sgw.createAspFactory("m3ua rasp create ip 127.0.0.1 port 2777 testasp".split(" "));

		Asp remAsp = sgw.assignAspToAs("testas", "testasp");

		// Check for Communication UP
		aspFactory.onCommStateChange(CommunicationState.UP);

		assertEquals(AspState.DOWN, remAsp.getState());

		// Check for ASP_UP
		M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
		aspFactory.read(message);

		assertEquals(AspState.INACTIVE, remAsp.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
		// also the AS should be INACTIVE now
		assertEquals(AsState.INACTIVE, remAs.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE));

		// Check for ASP_ACTIVE
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		((ASPActiveImpl) message).setRoutingContext(rc);
		aspFactory.read(message);
		assertEquals(AspState.ACTIVE, remAsp.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1, -1));
		// also the AS should be ACTIVE now
		assertEquals(AsState.ACTIVE, remAs.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE));

		// Check for ASP_UP received while ASP is already UP
		message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
		aspFactory.read(message);
		// The ASP Transitions to INACTIVE
		assertEquals(AspState.INACTIVE, remAsp.getState());
		// Receives ASP_UP Ack messages
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
		// As well as receives Error message
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.ERROR,
				ErrorCode.Unexpected_Message, 100));

		// also the AS should be PENDING now
		assertEquals(AsState.PENDING, remAs.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_PENDING));

		// Make sure we don't have any more
		assertNull(aspFactory.txPoll());

		sgw.stop();
	}

	@Test
	public void testPendingQueue() throws Exception {

		SgpImpl sgw = new SgpImpl("127.0.0.1", 1112);
		sgw.start();

		RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

		DestinationPointCode[] dpc = new DestinationPointCode[] { parmFactory
				.createDestinationPointCode(123, (short) 0) };

		ServiceIndicators[] servInds = new ServiceIndicators[] { parmFactory.createServiceIndicators(new short[] { 3 }) };

		TrafficModeType trModType = parmFactory.createTrafficModeType(TrafficModeType.Override);
		LocalRKIdentifier lRkId = parmFactory.createLocalRKIdentifier(1);
		RoutingKey rKey = parmFactory.createRoutingKey(lRkId, rc, null, null, dpc, servInds, null);

		// As remAs = sgw.createAppServer("testas", rc, rKey, trModType);
		As remAs = sgw
				.createAppServer("m3ua ras create rc 100 rk dpc 123 si 3 traffic-mode override testas".split(" "));
		// AspFactory aspFactory = sgw.createAspFactory("testasp", "127.0.0.1",
		// 2777);
		AspFactory aspFactory = sgw.createAspFactory("m3ua rasp create ip 127.0.0.1 port 2777 testasp".split(" "));

		Asp remAsp = sgw.assignAspToAs("testas", "testasp");

		// Check for Communication UP
		aspFactory.onCommStateChange(CommunicationState.UP);

		assertEquals(AspState.DOWN, remAsp.getState());

		// Check for ASP_UP
		M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
		aspFactory.read(message);

		assertEquals(AspState.INACTIVE, remAsp.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK, -1, -1));
		// also the AS should be INACTIVE now
		assertEquals(AsState.INACTIVE, remAs.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE));

		// Check for ASP_ACTIVE
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		((ASPActiveImpl) message).setRoutingContext(rc);
		aspFactory.read(message);
		assertEquals(AspState.ACTIVE, remAsp.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1, -1));
		// also the AS should be ACTIVE now
		assertEquals(AsState.ACTIVE, remAs.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE));

		// Check for ASP_INACTIVE
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE);
		aspFactory.read(message);
		assertEquals(AspState.INACTIVE, remAsp.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE_ACK, -1,
				-1));
		// also the AS should be PENDING now
		assertEquals(AsState.PENDING, remAs.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_PENDING));

		// Add PayloadData
		PayloadDataImpl payload = (PayloadDataImpl) messageFactory.createMessage(MessageClass.TRANSFER_MESSAGES,
				MessageType.PAYLOAD);
		ProtocolDataImpl p1 = (ProtocolDataImpl) parmFactory.createProtocolData(1408, 123, 3, 1, 0, 1, new byte[] { 1,
				2, 3, 4 });
		payload.setRoutingContext(rc);
		payload.setData(p1);

		remAs.write(payload);

		// Now bring UP the ASP
		message = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		((ASPActiveImpl) message).setRoutingContext(rc);
		aspFactory.read(message);

		assertTrue(validateMessage(aspFactory, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK, -1, -1));
		// also the AS should be ACTIVE now
		assertEquals(AsState.ACTIVE, remAs.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.MANAGEMENT, MessageType.NOTIFY,
				Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE));

		// Also we should have PayloadData
		M3UAMessage payLoadTemp = aspFactory.txPoll();
		assertNotNull(payLoadTemp);
		assertEquals(MessageClass.TRANSFER_MESSAGES, payLoadTemp.getMessageClass());
		assertEquals(MessageType.PAYLOAD, payLoadTemp.getMessageType());

		// Make sure we don't have any more
		assertNull(aspFactory.txPoll());
		
		sgw.stop();
	}

	/**
	 * 
	 * @param factory
	 * @param msgClass
	 * @param msgType
	 * @param type
	 *            The type for Notify message Or Error Code for Error Messages
	 * @param info
	 *            The Info for Notify message Or RoutingContext for Error
	 *            Message
	 * @return
	 */
	private boolean validateMessage(AspFactory factory, int msgClass, int msgType, int type, int info) {
		M3UAMessage message = factory.txPoll();
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

}
