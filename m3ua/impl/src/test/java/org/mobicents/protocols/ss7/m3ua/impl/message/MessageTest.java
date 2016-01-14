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

package org.mobicents.protocols.ss7.m3ua.impl.message;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.ss7.m3ua.impl.message.aspsm.ASPDownAckImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.aspsm.ASPDownImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.aspsm.ASPUpAckImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.aspsm.ASPUpImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.aspsm.HeartbeatAckImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.aspsm.HeartbeatImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.asptm.ASPActiveAckImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.asptm.ASPActiveImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.asptm.ASPInactiveAckImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.asptm.ASPInactiveImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.mgmt.ErrorImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.mgmt.NotifyImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.rkm.DeregistrationRequestImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.rkm.DeregistrationResponseImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.rkm.RegistrationRequestImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.rkm.RegistrationResponseImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.DestinationAvailableImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.DestinationUPUnavailableImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.DestinationUnavailableImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.ssnm.SignallingCongestionImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.transfer.PayloadDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ProtocolDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.RoutingKeyImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.Heartbeat;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.HeartbeatAck;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.SignallingCongestion;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.ConcernedDPC;
import org.mobicents.protocols.ss7.m3ua.parameter.CongestedIndication;
import org.mobicents.protocols.ss7.m3ua.parameter.CongestedIndication.CongestionLevel;
import org.mobicents.protocols.ss7.m3ua.parameter.DeregistrationResult;
import org.mobicents.protocols.ss7.m3ua.parameter.DeregistrationStatus;
import org.mobicents.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.DiagnosticInfo;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.HeartbeatData;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.OPCList;
import org.mobicents.protocols.ss7.m3ua.parameter.RegistrationResult;
import org.mobicents.protocols.ss7.m3ua.parameter.RegistrationStatus;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.ServiceIndicators;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.m3ua.parameter.UserCause;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author kulikov
 */
public class MessageTest {

    private ParameterFactoryImpl parmFactory = new ParameterFactoryImpl();
    private MessageFactoryImpl messageFactory = new MessageFactoryImpl();

    public MessageTest() {
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
    public void testPayloadData() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        PayloadDataImpl msg = (PayloadDataImpl) messageFactory.createMessage(MessageClass.TRANSFER_MESSAGES,
                MessageType.PAYLOAD);
        byte[] payload = new byte[] { 1, 2, 3, 4 };
        ProtocolDataImpl p1 = (ProtocolDataImpl) parmFactory.createProtocolData(1408, 14150, 1, 1, 0, 0, payload);
        msg.setData(p1);
        msg.encode(byteBuf);

        PayloadDataImpl msg1 = (PayloadDataImpl) messageFactory.createMessage(byteBuf);

        ProtocolDataImpl p2 = (ProtocolDataImpl) msg1.getData();

        assertEquals(p1.getTag(), p2.getTag());
        assertEquals(p1.getOpc(), p2.getOpc());
        assertEquals(p1.getDpc(), p2.getDpc());
        assertEquals(p1.getSI(), p2.getSI());
        assertEquals(p1.getNI(), p2.getNI());
        assertEquals(p1.getMP(), p2.getMP());
        assertEquals(p1.getSLS(), p2.getSLS());
        assertEquals(payload, p1.getData());
        assertEquals(payload, p2.getData());
    }

    @Test
    public void testPayloadData_Padding() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        PayloadDataImpl msg = (PayloadDataImpl) messageFactory.createMessage(MessageClass.TRANSFER_MESSAGES,
                MessageType.PAYLOAD);
        byte[] payload = new byte[] { 1, 2, 3, 4, 5 };
        ProtocolDataImpl p1 = (ProtocolDataImpl) parmFactory.createProtocolData(1408, 14150, 1, 1, 0, 0, payload);
        msg.setData(p1);
        msg.encode(byteBuf);

        PayloadDataImpl msg1 = (PayloadDataImpl) messageFactory.createMessage(byteBuf);

        ProtocolDataImpl p2 = (ProtocolDataImpl) msg1.getData();

        assertEquals(p1.getTag(), p2.getTag());
        assertEquals(p1.getOpc(), p2.getOpc());
        assertEquals(p1.getDpc(), p2.getDpc());
        assertEquals(p1.getSI(), p2.getSI());
        assertEquals(p1.getNI(), p2.getNI());
        assertEquals(p1.getMP(), p2.getMP());
        assertEquals(p1.getSLS(), p2.getSLS());
        assertEquals(payload, p1.getData());
        assertEquals(payload, p2.getData());
    }

    @Test
    public void testDestinationUnavailable() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        DestinationUnavailableImpl msg = (DestinationUnavailableImpl) messageFactory.createMessage(
                MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.DESTINATION_UNAVAILABLE);
        NetworkAppearance netApp = parmFactory.createNetworkAppearance(1233);
        msg.setNetworkAppearance(netApp);

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 12 });
        msg.setRoutingContexts(rc);

        AffectedPointCode afpc = parmFactory.createAffectedPointCode(new int[] { 123 }, new short[] { 0 });
        msg.setAffectedPointCodes(afpc);

        msg.encode(byteBuf);

        DestinationUnavailableImpl msg1 = (DestinationUnavailableImpl) messageFactory.createMessage(byteBuf);

        NetworkAppearance netApp1 = (NetworkAppearance) msg1.getNetworkAppearance();

        assertEquals(netApp.getNetApp(), netApp1.getNetApp());
    }

    @Test
    public void testDestinationAvailable() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        DestinationAvailableImpl msg = (DestinationAvailableImpl) messageFactory.createMessage(
                MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.DESTINATION_AVAILABLE);
        NetworkAppearance netApp = parmFactory.createNetworkAppearance(11233);
        msg.setNetworkAppearance(netApp);

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 12, 13 });
        msg.setRoutingContexts(rc);

        AffectedPointCode afpc = parmFactory.createAffectedPointCode(new int[] { 123, 456 }, new short[] { 0, 1 });
        msg.setAffectedPointCodes(afpc);

        InfoString str = parmFactory.createInfoString("Some debug message");
        msg.setInfoString(str);

        msg.encode(byteBuf);

        DestinationAvailableImpl msg1 = (DestinationAvailableImpl) messageFactory.createMessage(byteBuf);

        NetworkAppearance netApp1 = (NetworkAppearance) msg1.getNetworkAppearance();

        assertEquals(netApp.getNetApp(), netApp1.getNetApp());

        RoutingContext rc1 = msg1.getRoutingContexts();

        assertTrue(Arrays.equals(rc.getRoutingContexts(), rc1.getRoutingContexts()));
    }

    @Test
    public void testSignallingCongestion() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        SignallingCongestion msg = (SignallingCongestion) messageFactory.createMessage(
                MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.SIGNALING_CONGESTION);
        NetworkAppearance netApp = parmFactory.createNetworkAppearance(11233);
        msg.setNetworkAppearance(netApp);

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 12, 13 });
        msg.setRoutingContexts(rc);

        AffectedPointCode afpc = parmFactory.createAffectedPointCode(new int[] { 123, 456 }, new short[] { 0, 1 });
        msg.setAffectedPointCodes(afpc);

        InfoString str = parmFactory.createInfoString("Some debug message");
        msg.setInfoString(str);

        ConcernedDPC pointCode = parmFactory.createConcernedDPC(234567);
        msg.setConcernedDPC(pointCode);

        CongestedIndication congInd = parmFactory.createCongestedIndication(CongestionLevel.LEVEL1);
        msg.setCongestedIndication(congInd);

        ((SignallingCongestionImpl) msg).encode(byteBuf);

        SignallingCongestion msg1 = (SignallingCongestion) messageFactory.createMessage(byteBuf);

        NetworkAppearance netApp1 = (NetworkAppearance) msg1.getNetworkAppearance();

        assertEquals(netApp.getNetApp(), netApp1.getNetApp());

        RoutingContext rc1 = msg1.getRoutingContexts();

        assertTrue(Arrays.equals(rc.getRoutingContexts(), rc1.getRoutingContexts()));

        assertEquals(msg.getConcernedDPC().getPointCode(), msg1.getConcernedDPC().getPointCode());
        assertEquals(msg.getCongestedIndication().getCongestionLevel(), msg1.getCongestedIndication().getCongestionLevel());
    }

    @Test
    public void testDestinationUPUnavailable() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        DestinationUPUnavailableImpl msg = (DestinationUPUnavailableImpl) messageFactory.createMessage(
                MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.DESTINATION_USER_PART_UNAVAILABLE);
        NetworkAppearance netApp = parmFactory.createNetworkAppearance(1233);
        msg.setNetworkAppearance(netApp);

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 12 });
        msg.setRoutingContext(rc);

        AffectedPointCode afpc = parmFactory.createAffectedPointCode(new int[] { 123 }, new short[] { 0 });
        msg.setAffectedPointCode(afpc);

        UserCause usrCau = parmFactory.createUserCause(5, 0);
        msg.setUserCause(usrCau);

        msg.encode(byteBuf);

        DestinationUPUnavailableImpl msg1 = (DestinationUPUnavailableImpl) messageFactory.createMessage(byteBuf);

        NetworkAppearance netApp1 = (NetworkAppearance) msg1.getNetworkAppearance();

        assertEquals(netApp.getNetApp(), netApp1.getNetApp());

        assertEquals(msg.getUserCause().getUser(), msg1.getUserCause().getUser());
        assertEquals(msg.getUserCause().getCause(), msg1.getUserCause().getCause());
    }

    @Test
    public void testASPUp() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        ASPUpImpl msg = (ASPUpImpl) messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
        ASPIdentifier aspId = parmFactory.createASPIdentifier(1234);
        msg.setASPIdentifier(aspId);

        msg.encode(byteBuf);

        ASPUpImpl msg1 = (ASPUpImpl) messageFactory.createMessage(byteBuf);

        assertEquals(msg.getASPIdentifier().getAspId(), msg1.getASPIdentifier().getAspId());
    }

    @Test
    public void testASPUpAck() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        ASPUpAckImpl msg = (ASPUpAckImpl) messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
                MessageType.ASP_UP_ACK);
        ASPIdentifier aspId = parmFactory.createASPIdentifier(1234);
        msg.setASPIdentifier(aspId);

        InfoString infStr = parmFactory.createInfoString("Hello World");
        msg.setInfoString(infStr);

        msg.encode(byteBuf);

        ASPUpAckImpl msg1 = (ASPUpAckImpl) messageFactory.createMessage(byteBuf);

        assertEquals(msg.getASPIdentifier().getAspId(), msg1.getASPIdentifier().getAspId());
        assertEquals(msg.getInfoString().getString(), msg1.getInfoString().getString());
    }

    @Test
    public void testASPDown() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        ASPDownImpl msg = (ASPDownImpl) messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN);

        msg.encode(byteBuf);

        ASPDownImpl msg1 = (ASPDownImpl) messageFactory.createMessage(byteBuf);

        assertNotNull(msg1);
    }

    @Test
    public void testASPDownAck() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        ASPDownAckImpl msg = (ASPDownAckImpl) messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
                MessageType.ASP_DOWN_ACK);

        msg.encode(byteBuf);

        ASPDownAckImpl msg1 = (ASPDownAckImpl) messageFactory.createMessage(byteBuf);

        assertNotNull(msg1);
    }

    @Test
    public void testRegistrationRequest() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        RegistrationRequestImpl msg = (RegistrationRequestImpl) messageFactory.createMessage(
                MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.REG_REQUEST);
        ASPIdentifier aspId = parmFactory.createASPIdentifier(1234);

        LocalRKIdentifier localRkId = parmFactory.createLocalRKIdentifier(12);
        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 1 });
        TrafficModeType trafMdTy = parmFactory.createTrafficModeType(1);
        NetworkAppearance netApp = parmFactory.createNetworkAppearance(1);
        DestinationPointCode[] dpc = new DestinationPointCode[] { parmFactory.createDestinationPointCode(123, (short) 0),
                parmFactory.createDestinationPointCode(456, (short) 1) };
        ServiceIndicators[] servInds = new ServiceIndicators[] { parmFactory.createServiceIndicators(new short[] { 1, 2 }),
                parmFactory.createServiceIndicators(new short[] { 1, 2 }) };
        OPCList[] opcList = new OPCList[] { parmFactory.createOPCList(new int[] { 1, 2, 3 }, new short[] { 0, 0, 0 }),
                parmFactory.createOPCList(new int[] { 4, 5, 6 }, new short[] { 0, 0, 0 }) };

        RoutingKeyImpl routKey = (RoutingKeyImpl) parmFactory.createRoutingKey(localRkId, rc, trafMdTy, netApp, dpc, servInds,
                opcList);

        msg.setRoutingKey(routKey);

        msg.encode(byteBuf);

        RegistrationRequestImpl msg1 = (RegistrationRequestImpl) messageFactory.createMessage(byteBuf);

        assertEquals(msg.getRoutingKey().getLocalRKIdentifier().getId(), msg1.getRoutingKey().getLocalRKIdentifier().getId());
    }

    @Test
    public void testRegistrationResponse() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        RegistrationResponseImpl msg = (RegistrationResponseImpl) messageFactory.createMessage(
                MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.REG_RESPONSE);

        LocalRKIdentifier localRkId = parmFactory.createLocalRKIdentifier(12);
        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 1 });
        RegistrationStatus status = parmFactory.createRegistrationStatus(0);

        RegistrationResult result = (RegistrationResult) parmFactory.createRegistrationResult(localRkId, status, rc);

        msg.setRegistrationResult(result);

        msg.encode(byteBuf);

        RegistrationResponseImpl msg1 = (RegistrationResponseImpl) messageFactory.createMessage(byteBuf);

        assertEquals(msg.getRegistrationResult().getLocalRKIdentifier().getId(), msg1.getRegistrationResult()
                .getLocalRKIdentifier().getId());
    }

    @Test
    public void testDeregistrationRequest() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        DeregistrationRequestImpl msg = (DeregistrationRequestImpl) messageFactory.createMessage(
                MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.DEREG_REQUEST);

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 1 });

        msg.setRoutingContext(rc);

        msg.encode(byteBuf);

        DeregistrationRequestImpl msg1 = (DeregistrationRequestImpl) messageFactory.createMessage(byteBuf);

        assertTrue(Arrays.equals(msg.getRoutingContext().getRoutingContexts(), msg1.getRoutingContext().getRoutingContexts()));
    }

    @Test
    public void testDeregistrationResponse() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        DeregistrationResponseImpl msg = (DeregistrationResponseImpl) messageFactory.createMessage(
                MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.DEREG_RESPONSE);

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 1 });
        DeregistrationStatus status = parmFactory.createDeregistrationStatus(0);

        DeregistrationResult result = (DeregistrationResult) parmFactory.createDeregistrationResult(rc, status);

        msg.setDeregistrationResult(result);

        msg.encode(byteBuf);

        DeregistrationResponseImpl msg1 = (DeregistrationResponseImpl) messageFactory.createMessage(byteBuf);

        assertEquals(msg.getDeregistrationResult().getDeregistrationStatus().getStatus(), msg1.getDeregistrationResult()
                .getDeregistrationStatus().getStatus());
    }

    @Test
    public void testASPActive() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        ASPActiveImpl msg = (ASPActiveImpl) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE);

        TrafficModeType mode = parmFactory.createTrafficModeType(1);
        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 1 });
        InfoString str = parmFactory.createInfoString("There it is");
        msg.setTrafficModeType(mode);
        msg.setRoutingContext(rc);
        msg.setInfoString(str);

        msg.encode(byteBuf);

        ASPActiveImpl msg1 = (ASPActiveImpl) messageFactory.createMessage(byteBuf);

        assertEquals(msg.getTrafficModeType().getMode(), msg1.getTrafficModeType().getMode());
        assertEquals(msg.getInfoString().getString(), msg1.getInfoString().getString());
    }

    @Test
    public void testASPActiveAck() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        ASPActiveAckImpl msg = (ASPActiveAckImpl) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE_ACK);

        TrafficModeType mode = parmFactory.createTrafficModeType(1);
        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 1 });
        InfoString str = parmFactory.createInfoString("There it is");
        msg.setTrafficModeType(mode);
        msg.setRoutingContext(rc);
        msg.setInfoString(str);

        msg.encode(byteBuf);

        ASPActiveAckImpl msg1 = (ASPActiveAckImpl) messageFactory.createMessage(byteBuf);

        assertEquals(msg.getTrafficModeType().getMode(), msg1.getTrafficModeType().getMode());
        assertEquals(msg.getInfoString().getString(), msg1.getInfoString().getString());
        assertTrue(Arrays.equals(msg.getRoutingContext().getRoutingContexts(), msg1.getRoutingContext().getRoutingContexts()));
    }

    @Test
    public void testASPInactive() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        ASPInactiveImpl msg = (ASPInactiveImpl) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_INACTIVE);

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 1 });
        InfoString str = parmFactory.createInfoString("There it is");
        msg.setRoutingContext(rc);
        msg.setInfoString(str);

        msg.encode(byteBuf);

        ASPInactiveImpl msg1 = (ASPInactiveImpl) messageFactory.createMessage(byteBuf);

        assertEquals(msg.getInfoString().getString(), msg1.getInfoString().getString());

        assertTrue(Arrays.equals(msg.getRoutingContext().getRoutingContexts(), msg1.getRoutingContext().getRoutingContexts()));
    }

    @Test
    public void testASPInactiveAck() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        ASPInactiveAckImpl msg = (ASPInactiveAckImpl) messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_INACTIVE_ACK);

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 1 });
        InfoString str = parmFactory.createInfoString("There it is");
        msg.setRoutingContext(rc);
        msg.setInfoString(str);

        msg.encode(byteBuf);

        ASPInactiveAckImpl msg1 = (ASPInactiveAckImpl) messageFactory.createMessage(byteBuf);

        assertEquals(msg.getInfoString().getString(), msg1.getInfoString().getString());
        assertTrue(Arrays.equals(msg.getRoutingContext().getRoutingContexts(), msg1.getRoutingContext().getRoutingContexts()));
    }

    @Test
    public void testError() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        ErrorImpl msg = (ErrorImpl) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.ERROR);

        ErrorCode code = parmFactory.createErrorCode(0x08);
        msg.setErrorCode(code);

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 1 });
        msg.setRoutingContext(rc);

        AffectedPointCode affPc = parmFactory.createAffectedPointCode(new int[] { 1, 2 }, new short[] { 0, 0 });
        msg.setAffectedPointCode(affPc);

        NetworkAppearance netApp = parmFactory.createNetworkAppearance(12345l);
        msg.setNetworkAppearance(netApp);

        DiagnosticInfo str = parmFactory.createDiagnosticInfo("There it is");
        msg.setDiagnosticInfo(str);

        msg.encode(byteBuf);

        ErrorImpl msg1 = (ErrorImpl) messageFactory.createMessage(byteBuf);

        assertEquals(msg.getErrorCode().getCode(), msg1.getErrorCode().getCode());
        assertTrue(Arrays.equals(msg.getRoutingContext().getRoutingContexts(), msg1.getRoutingContext().getRoutingContexts()));
        assertTrue(Arrays.equals(msg.getAffectedPointCode().getPointCodes(), msg1.getAffectedPointCode().getPointCodes()));
        assertTrue(Arrays.equals(msg.getAffectedPointCode().getMasks(), msg1.getAffectedPointCode().getMasks()));

        assertEquals(msg.getNetworkAppearance().getNetApp(), msg1.getNetworkAppearance().getNetApp());

        assertEquals(msg.getDiagnosticInfo().getInfo(), msg1.getDiagnosticInfo().getInfo());
    }

    @Test
    public void testNotify() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        NotifyImpl msg = (NotifyImpl) messageFactory.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);

        Status status = parmFactory.createStatus(1, 4);
        msg.setStatus(status);

        ASPIdentifier aspId = parmFactory.createASPIdentifier(123);
        msg.setASPIdentifier(aspId);

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 1 });
        msg.setRoutingContext(rc);

        msg.encode(byteBuf);

        NotifyImpl msg1 = (NotifyImpl) messageFactory.createMessage(byteBuf);

        assertEquals(msg.getStatus().getType(), msg1.getStatus().getType());
        assertEquals(msg.getStatus().getInfo(), msg1.getStatus().getInfo());
        assertEquals(msg.getASPIdentifier().getAspId(), msg1.getASPIdentifier().getAspId());
        assertTrue(Arrays.equals(msg.getRoutingContext().getRoutingContexts(), msg1.getRoutingContext().getRoutingContexts()));
    }

    @Test
    public void encodeTwoMessages() throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();

        PayloadDataImpl payloadMsg = (PayloadDataImpl) messageFactory.createMessage(MessageClass.TRANSFER_MESSAGES,
                MessageType.PAYLOAD);
        ProtocolDataImpl p1 = (ProtocolDataImpl) parmFactory.createProtocolData(1408, 14150, 1, 1, 0, 0, new byte[] { 1, 2, 3,
                4 });
        payloadMsg.setData(p1);
        payloadMsg.encode(byteBuf);

        DestinationUnavailableImpl dunaMsg = (DestinationUnavailableImpl) messageFactory.createMessage(
                MessageClass.SIGNALING_NETWORK_MANAGEMENT, MessageType.DESTINATION_UNAVAILABLE);
        NetworkAppearance netApp = parmFactory.createNetworkAppearance(1233);
        dunaMsg.setNetworkAppearance(netApp);

        RoutingContext rc = parmFactory.createRoutingContext(new long[] { 12 });
        dunaMsg.setRoutingContexts(rc);

        AffectedPointCode afpc = parmFactory.createAffectedPointCode(new int[] { 123 }, new short[] { 0 });
        dunaMsg.setAffectedPointCodes(afpc);

        dunaMsg.encode(byteBuf);

        // Paylod decode
        PayloadDataImpl payloadMsg1 = (PayloadDataImpl) messageFactory.createMessage(byteBuf);

        ProtocolDataImpl p2 = (ProtocolDataImpl) payloadMsg1.getData();

        assertEquals(p1.getTag(), p2.getTag());
        assertEquals(p1.getOpc(), p2.getOpc());
        assertEquals(p1.getDpc(), p2.getDpc());
        assertEquals(p1.getSI(), p2.getSI());
        assertEquals(p1.getNI(), p2.getNI());
        assertEquals(p1.getMP(), p2.getMP());
        assertEquals(p1.getSLS(), p2.getSLS());

        assertTrue(byteBuf.readableBytes() > 0);

        DestinationUnavailableImpl dunaMsg1 = (DestinationUnavailableImpl) messageFactory.createMessage(byteBuf);

        NetworkAppearance netApp1 = (NetworkAppearance) dunaMsg1.getNetworkAppearance();

        assertEquals(netApp.getNetApp(), netApp1.getNetApp());

    }

    @Test
    public void testHeartBeat() throws IOException {
        // Test encode
        ByteBuf byteBuf = Unpooled.buffer();

        byte[] data = new byte[] { 0x01, 0x00, 0x03, 0x03, 0x00, 0x00, 0x00, 0x1c, 0x00, 0x09, 0x00, 0x14, 0x00, 0x02, 0x00,
                0x00, 0x00, 0x07, (byte) 0xaf, 0x3e, 0x75, 0x40, 0x03, 0x13, 0x05, 0x07, 0x11, 0x20 };

        byte[] heratbeatData = new byte[] { 0x00, 0x02, 0x00, 0x00, 0x00, 0x07, (byte) 0xaf, 0x3e, 0x75, 0x40, 0x03, 0x13,
                0x05, 0x07, 0x11, 0x20 };
        Heartbeat heartbeat = (Heartbeat) messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
                MessageType.HEARTBEAT);

        HeartbeatData hrBtData = parmFactory.createHeartbeatData(heratbeatData);

        heartbeat.setHeartbeatData(hrBtData);

        ((HeartbeatImpl) heartbeat).encode(byteBuf);

//        byte[] rawData = new byte[buffer.limit()];
//        buffer.get(rawData);

//        assertTrue(Arrays.equals(data, buf));

        // Test Decode
        M3UAMessageImpl m3uaMessageImpl = messageFactory.createMessage(byteBuf);
        assertTrue((m3uaMessageImpl instanceof HeartbeatImpl));
        assertNotNull(((HeartbeatImpl) m3uaMessageImpl).getHeartbeatData());

        assertTrue(Arrays.equals(heratbeatData, ((HeartbeatImpl) m3uaMessageImpl).getHeartbeatData().getData()));

    }

    @Test
    public void testHeartBeatAck() throws IOException {
        // Test encode
        ByteBuf byteBuf = Unpooled.buffer();

        byte[] data = new byte[] { 0x01, 0x00, 0x03, 0x06, 0x00, 0x00, 0x00, 0x1c, 0x00, 0x09, 0x00, 0x14, 0x00, 0x02, 0x00,
                0x00, 0x00, 0x07, (byte) 0xaf, 0x3e, 0x75, 0x40, 0x03, 0x13, 0x05, 0x07, 0x11, 0x20 };

        byte[] heratbeatAckData = new byte[] { 0x00, 0x02, 0x00, 0x00, 0x00, 0x07, (byte) 0xaf, 0x3e, 0x75, 0x40, 0x03, 0x13,
                0x05, 0x07, 0x11, 0x20 };
        HeartbeatAck heartbeatAck = (HeartbeatAck) messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
                MessageType.HEARTBEAT_ACK);

        HeartbeatData hrBtData = parmFactory.createHeartbeatData(heratbeatAckData);

        heartbeatAck.setHeartbeatData(hrBtData);

        ((HeartbeatAckImpl) heartbeatAck).encode(byteBuf);

//        byte[] rawData = new byte[buffer.limit()];
//        buffer.get(rawData);

//        assertTrue(Arrays.equals(data, buf));

        // Test Decode
        M3UAMessageImpl m3uaMessageImpl = messageFactory.createMessage(byteBuf);
        assertTrue((m3uaMessageImpl instanceof HeartbeatAckImpl));
        assertNotNull(((HeartbeatAckImpl) m3uaMessageImpl).getHeartbeatData());

        assertTrue(Arrays.equals(heratbeatAckData, ((HeartbeatAckImpl) m3uaMessageImpl).getHeartbeatData().getData()));

    }
}
