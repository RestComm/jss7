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

package org.mobicents.protocols.ss7.m3ua.impl.as;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AsState;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.CommunicationListener.CommunicationState;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.transfer.PayloadDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ProtocolDataImpl;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPActiveAck;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * 
 * @author amit bhayani
 * 
 */
public class RemSgFSMTest {

	private ParameterFactoryImpl parmFactory = new ParameterFactoryImpl();
	private MessageFactoryImpl messageFactory = new MessageFactoryImpl();

	public RemSgFSMTest() {
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

		RemSgpImpl rsgw = new RemSgpImpl();
		rsgw.start();

		RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

		// As as = rsgw.createAppServer("testas", rc, rKey, trModType);
		As as = rsgw.createAppServer("m3ua as create rc 100 testas".split(" "));
		// AspFactory localAspFactory = rsgw.createAspFactory("testasp",
		// "127.0.0.1", 2777, "127.0.0.1", 2778);
		AspFactory localAspFactory = rsgw
				.createAspFactory("m3ua asp create ip 127.0.0.1 port 2777 remip 127.0.0.1 remport 2778 testasp"
						.split(" "));
		localAspFactory.start();

		Asp asp = rsgw.assignAspToAs("testas", "testasp");

		// Check for Communication UP
		localAspFactory.onCommStateChange(CommunicationState.UP);

		// Once comunication is UP, ASP_UP should have been sent.
		assertEquals(AspState.UP_SENT, asp.getState());
		assertTrue(validateMessage(localAspFactory, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));

		// The other side will send ASP_UP_ACK and after that NTFY(AS-INACTIVE)
		M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
				MessageType.ASP_UP_ACK);
		localAspFactory.read(message);

		Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
		notify.setRoutingContext(rc);
		Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
		notify.setStatus(status);
		localAspFactory.read(notify);

		assertEquals(AspState.ACTIVE_SENT, asp.getState());
		assertTrue(validateMessage(localAspFactory, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1,
				-1));
		// also the AS should be INACTIVE now
		assertEquals(AsState.INACTIVE, as.getState());

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

		assertEquals(AspState.ACTIVE, asp.getState());
		// also the AS should be ACTIVE now
		assertEquals(AsState.ACTIVE, as.getState());

		// Lets stop ASP Factory
		localAspFactory.stop();

		assertEquals(AspState.DOWN_SENT, asp.getState());
		assertTrue(validateMessage(localAspFactory, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));
		// also the AS is PENDING
		assertEquals(AsState.PENDING, as.getState());

		// Make sure we don't have any more
		assertNull(localAspFactory.txPoll());

		rsgw.stop();
	}

	@Test
	public void testSingleAspInMultipleAs() throws Exception {
		// 5.1.1.3. Single ASP in Multiple Application Servers (Each with "1+0"
		// Sparing)

		RemSgpImpl rsgw = new RemSgpImpl();
		rsgw.start();

		// Define 1st AS
		RoutingContext rc1 = parmFactory.createRoutingContext(new long[] { 100 });

		// As remAs1 = rsgw.createAppServer("testas1", rc1, rKey1, trModType1);
		As remAs1 = rsgw.createAppServer("m3ua as create rc 100 testas1".split(" "));

		// Define 2nd AS
		RoutingContext rc2 = parmFactory.createRoutingContext(new long[] { 200 });

		// As remAs2 = rsgw.createAppServer("testas2", rc2, rKey2, trModType2);
		As remAs2 = rsgw.createAppServer("m3ua as create rc 200 testas2".split(" "));

		// AspFactory aspFactory = rsgw.createAspFactory("testasp", "127.0.0.1",
		// 2777, "127.0.0.1", 2778);
		AspFactory aspFactory = rsgw
				.createAspFactory("m3ua asp create ip 127.0.0.1 port 3777 remip 127.0.0.1 remport 3112 testasp"
						.split(" "));
		aspFactory.start();

		// Both ASP uses same underlying M3UAChannel
		Asp remAsp1 = rsgw.assignAspToAs("testas1", "testasp");
		Asp remAsp2 = rsgw.assignAspToAs("testas2", "testasp");

		// Check for Communication UP
		aspFactory.onCommStateChange(CommunicationState.UP);

		assertEquals(AspState.UP_SENT, remAsp1.getState());
		assertEquals(AspState.UP_SENT, remAsp2.getState());
		// Once communication is UP, ASP_UP should have been sent.
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));

		// Both the AS is still DOWN
		assertEquals(AsState.DOWN, remAs1.getState());
		assertEquals(AsState.DOWN, remAs2.getState());

		// The other side will send ASP_UP_ACK and after that NTFY(AS-INACTIVE)
		// for both the AS
		M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
				MessageType.ASP_UP_ACK);
		aspFactory.read(message);
		assertEquals(AspState.ACTIVE_SENT, remAsp1.getState());
		assertEquals(AspState.ACTIVE_SENT, remAsp2.getState());
		// ASP_ACTIVE for both ASP in txQueue
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));

		Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
		notify.setRoutingContext(rc1);
		Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
		notify.setStatus(status);
		aspFactory.read(notify);
		// the AS1 should be INACTIVE now but AS2 still DOWN
		assertEquals(AsState.INACTIVE, remAs1.getState());
		assertEquals(AsState.DOWN, remAs2.getState());

		notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
		notify.setRoutingContext(rc2);// RC 200
		status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
		notify.setStatus(status);
		aspFactory.read(notify);
		// AS2 should be INACTIVE now
		assertEquals(AsState.INACTIVE, remAs2.getState());

		// The other side will send ASP_ACTIVE_ACK and after that
		// NTFY(AS-ACTIVE)
		ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
				MessageType.ASP_ACTIVE_ACK);
		aspActiveAck.setRoutingContext(this.parmFactory.createRoutingContext(new long[] { 100, 200 }));
		aspFactory.read(aspActiveAck);

		// Both ASP are ACTIVE now
		assertEquals(AspState.ACTIVE, remAsp1.getState());
		assertEquals(AspState.ACTIVE, remAsp2.getState());

		notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
		notify.setRoutingContext(rc1);
		status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
		notify.setStatus(status);
		aspFactory.read(notify);
		// the AS1 should be ACTIVE now but AS2 still INACTIVE
		assertEquals(AsState.ACTIVE, remAs1.getState());
		assertEquals(AsState.INACTIVE, remAs2.getState());

		notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
		notify.setRoutingContext(rc2);
		status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
		notify.setStatus(status);
		aspFactory.read(notify);
		// the AS2 is also ACTIVE now
		assertEquals(AsState.ACTIVE, remAs1.getState());
		assertEquals(AsState.ACTIVE, remAs2.getState());

		// Lets stop ASP Factory
		aspFactory.stop();

		assertEquals(AspState.DOWN_SENT, remAsp1.getState());
		assertEquals(AspState.DOWN_SENT, remAsp2.getState());
		assertTrue(validateMessage(aspFactory, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));
		// also the both AS is PENDING
		assertEquals(AsState.PENDING, remAs1.getState());
		assertEquals(AsState.PENDING, remAs2.getState());

		// Make sure we don't have any more
		assertNull(aspFactory.txPoll());

		rsgw.stop();
	}

	@Test
	public void testTwoAspInAsOverride() throws Exception {
		// 5.1.2. Two ASPs in Application Server ("1+1" Sparing)

		RemSgpImpl rsgw = new RemSgpImpl();
		rsgw.start();

		RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

		TrafficModeType trModType = parmFactory.createTrafficModeType(TrafficModeType.Override);

		// As remAs = rsgw.createAppServer("testas", rc, rKey, trModType);
		As remAs = rsgw.createAppServer("m3ua as create rc 100 testas".split(" "));

		// AspFactory aspFactory1 = rsgw.createAspFactory("testasp1",
		// "127.0.0.1", 2777, "127.0.0.1", 2777);
		AspFactory aspFactory1 = rsgw
				.createAspFactory("m3ua asp create ip 127.0.0.1 port 3777 remip 127.0.0.1 remport 3112 testasp1"
						.split(" "));
		aspFactory1.start();

		// AspFactory aspFactory2 = rsgw.createAspFactory("testasp2",
		// "127.0.0.1", 2777, "127.0.0.1", 2778);
		AspFactory aspFactory2 = rsgw
				.createAspFactory("m3ua asp create ip 127.0.0.1 port 3778 remip 127.0.0.1 remport 3112 testasp2"
						.split(" "));
		aspFactory2.start();

		Asp remAsp1 = rsgw.assignAspToAs("testas", "testasp1");
		Asp remAsp2 = rsgw.assignAspToAs("testas", "testasp2");

		// Check for Communication UP for ASP1
		aspFactory1.onCommStateChange(CommunicationState.UP);
		assertEquals(AspState.UP_SENT, remAsp1.getState());
		// ASP_UP should have been sent.
		assertTrue(validateMessage(aspFactory1, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));
		// But AS is still Down
		assertEquals(AsState.DOWN, remAs.getState());

		// Far end send ASP_UP_ACK and NTFY
		M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
				MessageType.ASP_UP_ACK);
		aspFactory1.read(message);
		assertEquals(AspState.ACTIVE_SENT, remAsp1.getState());
		assertTrue(validateMessage(aspFactory1, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));

		Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
		notify.setRoutingContext(rc);
		Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
		notify.setStatus(status);
		aspFactory1.read(notify);
		// the AS1 should be INACTIVE
		assertEquals(AsState.INACTIVE, remAs.getState());

		// The other side will send ASP_ACTIVE_ACK and after that
		// NTFY(AS-ACTIVE)
		ASPActiveAck aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
				MessageType.ASP_ACTIVE_ACK);
		aspActiveAck.setRoutingContext(rc);
		aspActiveAck.setTrafficModeType(trModType);
		aspFactory1.read(aspActiveAck);

		assertEquals(AspState.ACTIVE, remAsp1.getState());

		notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
		notify.setRoutingContext(rc);
		status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
		notify.setStatus(status);
		aspFactory1.read(notify);
		aspFactory2.read(notify);

		assertEquals(AsState.ACTIVE, remAs.getState());

		// Communication UP for ASP2
		aspFactory2.onCommStateChange(CommunicationState.UP);
		assertEquals(AspState.UP_SENT, remAsp2.getState());
		// ASP_UP should have been sent.
		assertTrue(validateMessage(aspFactory2, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));
		// Far end send ASP_UP_ACK
		message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
		aspFactory2.read(message);
		// ASP2 now is INACTIVE as ASP1 is still ACTIVATING
		assertEquals(AspState.INACTIVE, remAsp2.getState());

		// Bring down ASP1
		// 5.2.1. 1+1 Sparing, Withdrawal of ASP, Backup Override
		aspFactory1.onCommStateChange(CommunicationState.LOST);
		// the ASP is DOWN and AS goes in PENDING STATE
		assertEquals(AspState.DOWN, remAsp1.getState());
		assertEquals(AsState.PENDING, remAs.getState());

		// Aslo the ASP_ACTIVE for ASP2 should have been sent
		assertEquals(AspState.ACTIVE_SENT, remAsp2.getState());
		assertTrue(validateMessage(aspFactory2, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1, -1));

		// We will not get Alternate ASP Active as this ASP's channel is dead
		// The other side will send ASP_ACTIVE_ACK and after that
		// NTFY(AS-ACTIVE)
		aspActiveAck = (ASPActiveAck) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
				MessageType.ASP_ACTIVE_ACK);
		aspActiveAck.setRoutingContext(rc);
		aspFactory2.read(aspActiveAck);
		assertEquals(AspState.ACTIVE, remAsp2.getState());

		// We should get Notify that AS is ACTIVE
		notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
		notify.setRoutingContext(rc);
		status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
		notify.setStatus(status);
		aspFactory2.read(notify);

		assertEquals(AsState.ACTIVE, remAs.getState());

		assertNull(aspFactory1.txPoll());
		assertNull(aspFactory2.txPoll());

		rsgw.stop();
	}

	@Test
	public void testPendingQueue() throws Exception {
		RemSgpImpl rsgw = new RemSgpImpl();
		rsgw.start();

		RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

		// As as = rsgw.createAppServer("testas", rc, rKey, trModType);
		As as = rsgw.createAppServer("m3ua as create rc 100 testas".split(" "));
		// AspFactory localAspFactory = rsgw.createAspFactory("testasp",
		// "127.0.0.1", 2777, "127.0.0.1", 2778);
		AspFactory localAspFactory = rsgw
				.createAspFactory("m3ua asp create ip 127.0.0.1 port 2777 remip 127.0.0.1 remport 2778 testasp"
						.split(" "));
		localAspFactory.start();

		Asp asp = rsgw.assignAspToAs("testas", "testasp");

		// Check for Communication UP
		localAspFactory.onCommStateChange(CommunicationState.UP);

		// Once comunication is UP, ASP_UP should have been sent.
		assertEquals(AspState.UP_SENT, asp.getState());
		assertTrue(validateMessage(localAspFactory, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));

		// The other side will send ASP_UP_ACK and after that NTFY(AS-INACTIVE)
		M3UAMessageImpl message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
				MessageType.ASP_UP_ACK);
		localAspFactory.read(message);

		Notify notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
		notify.setRoutingContext(rc);
		Status status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
		notify.setStatus(status);
		localAspFactory.read(notify);

		assertEquals(AspState.ACTIVE_SENT, asp.getState());
		assertTrue(validateMessage(localAspFactory, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1,
				-1));
		// also the AS should be INACTIVE now
		assertEquals(AsState.INACTIVE, as.getState());

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

		assertEquals(AspState.ACTIVE, asp.getState());
		// also the AS should be ACTIVE now
		assertEquals(AsState.ACTIVE, as.getState());

		// Lets stop ASP Factory
		localAspFactory.stop();

		assertEquals(AspState.DOWN_SENT, asp.getState());
		assertTrue(validateMessage(localAspFactory, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN, -1, -1));
		// also the AS is PENDING
		assertEquals(AsState.PENDING, as.getState());

		// The far end sends DOWN_ACK
		message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK);
		localAspFactory.read(message);

		
		//start the ASP Factory again
		localAspFactory.start();
		
		// Now lets add some PayloadData
		PayloadDataImpl payload = (PayloadDataImpl) messageFactory.createMessage(MessageClass.TRANSFER_MESSAGES,
				MessageType.PAYLOAD);
		ProtocolDataImpl p1 = (ProtocolDataImpl) parmFactory.createProtocolData(1408, 123, 3, 1, 0, 1, new byte[] { 1,
				2, 3, 4 });
		payload.setRoutingContext(rc);
		payload.setData(p1);

		as.write(payload);

		// Now again the ASP is brought up
		localAspFactory.onCommStateChange(CommunicationState.UP);

		// Once communication is UP, ASP_UP should have been sent.
		assertEquals(AspState.UP_SENT, asp.getState());
		assertTrue(validateMessage(localAspFactory, MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP, -1, -1));

		// The other side will send ASP_UP_ACK and after that NTFY(AS-INACTIVE)
		message = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
		localAspFactory.read(message);

		notify = (Notify) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
		notify.setRoutingContext(rc);
		status = parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
		notify.setStatus(status);
		localAspFactory.read(notify);

		assertEquals(AspState.ACTIVE_SENT, asp.getState());
		assertTrue(validateMessage(localAspFactory, MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE, -1,
				-1));
		// AS should still be PENDING
		assertEquals(AsState.PENDING, as.getState());

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

		assertEquals(AspState.ACTIVE, asp.getState());
		// also the AS should be ACTIVE now
		assertEquals(AsState.ACTIVE, as.getState());
		
		// Also we should have PayloadData
		M3UAMessage payLoadTemp = localAspFactory.txPoll();
		assertNotNull(payLoadTemp);
		assertEquals(MessageClass.TRANSFER_MESSAGES, payLoadTemp.getMessageClass());
		assertEquals(MessageType.PAYLOAD, payLoadTemp.getMessageType());

		// Make sure we don't have any more
		assertNull(localAspFactory.txPoll());

		rsgw.stop();
	}

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
			}

			// TODO take care of Error?
			return true;
		} else {
			return true;
		}

	}

}
