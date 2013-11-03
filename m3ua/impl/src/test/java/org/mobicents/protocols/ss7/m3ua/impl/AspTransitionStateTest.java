/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.m3ua.impl;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.mgmt.NotifyImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author amit bhayani
 *
 */
public class AspTransitionStateTest {

    private ParameterFactoryImpl parmFactory = new ParameterFactoryImpl();
    private MessageFactoryImpl messageFactory = new MessageFactoryImpl();

    public AspTransitionStateTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void testTrfrMessages() throws IOException {
        M3UAMessageImpl msg = messageFactory.createMessage(MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD);
        String transition = TransitionState.getTransition(msg);

        assertEquals(TransitionState.PAYLOAD, transition);
    }

    @Test
    public void testSsnmMessages() throws IOException {
        M3UAMessageImpl msg = messageFactory.createMessage(MessageClass.SIGNALING_NETWORK_MANAGEMENT,
                MessageType.DESTINATION_UNAVAILABLE);
        String transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.DUNA, transition);

        msg = messageFactory.createMessage(MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.DESTINATION_AVAILABLE);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.DAVA, transition);

        msg = messageFactory.createMessage(MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.DESTINATION_STATE_AUDIT);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.DAUD, transition);

        msg = messageFactory.createMessage(MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.SIGNALING_CONGESTION);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.SCON, transition);

        msg = messageFactory.createMessage(MessageClass.SIGNALING_NETWORK_MANAGEMENT,
                MessageType.DESTINATION_USER_PART_UNAVAILABLE);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.DUPU, transition);

        msg = messageFactory.createMessage(MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.DESTINATION_RESTRICTED);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.DRST, transition);
    }

    @Test
    public void testAssmMessages() throws IOException {
        M3UAMessageImpl msg = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
        String transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.ASP_UP, transition);

        msg = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.ASP_UP_ACK, transition);

        msg = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.ASP_DOWN, transition);

        msg = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.ASP_DOWN_ACK, transition);

        msg = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.HEARTBEAT);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.HEARTBEAT, transition);

        msg = messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.HEARTBEAT_ACK);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.HEARTBEAT_ACK, transition);
    }

    @Test
    public void testAstmMessages() throws IOException {
        M3UAMessageImpl msg = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
        String transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.ASP_ACTIVE, transition);

        msg = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.ASP_ACTIVE_ACK, transition);

        msg = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.ASP_INACTIVE, transition);

        msg = messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE_ACK);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.ASP_INACTIVE_ACK, transition);
    }

    @Test
    public void testMgmtMessages() throws IOException {
        // As State Change
        NotifyImpl msg = (NotifyImpl) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        Status status = this.parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_Reserved);
        msg.setStatus(status);
        String transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.AS_STATE_CHANGE_RESERVE, transition);

        msg = (NotifyImpl) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        status = this.parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        msg.setStatus(status);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.AS_STATE_CHANGE_INACTIVE, transition);

        msg = (NotifyImpl) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        status = this.parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        msg.setStatus(status);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.AS_STATE_CHANGE_ACTIVE, transition);

        msg = (NotifyImpl) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        status = this.parmFactory.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_PENDING);
        msg.setStatus(status);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.AS_STATE_CHANGE_PENDING, transition);

        // Other
        msg = (NotifyImpl) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        status = this.parmFactory.createStatus(Status.STATUS_Other, Status.INFO_Insufficient_ASP_Resources_Active);
        msg.setStatus(status);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.OTHER_INSUFFICIENT_ASP, transition);

        msg = (NotifyImpl) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        status = this.parmFactory.createStatus(Status.STATUS_Other, Status.INFO_Alternate_ASP_Active);
        msg.setStatus(status);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE, transition);

        msg = (NotifyImpl) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);
        status = this.parmFactory.createStatus(Status.STATUS_Other, Status.INFO_Alternate_ASP_Failure);
        msg.setStatus(status);
        transition = TransitionState.getTransition(msg);
        assertEquals(TransitionState.OTHER_ALTERNATE_ASP_FAILURE, transition);

        // TODO Error

    }

}
