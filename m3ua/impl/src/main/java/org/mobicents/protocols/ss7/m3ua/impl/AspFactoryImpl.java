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

package org.mobicents.protocols.ss7.m3ua.impl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.AssociationListener;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.ss7.m3ua.Asp;
import org.mobicents.protocols.ss7.m3ua.AspFactory;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.oam.M3UAOAMMessages;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPDown;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPDownAck;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPUp;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPUpAck;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.Heartbeat;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPActive;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPActiveAck;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPInactive;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPInactiveAck;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationAvailable;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationRestricted;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationStateAudit;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationUPUnavailable;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.DestinationUnavailable;
import org.mobicents.protocols.ss7.m3ua.message.ssnm.SignallingCongestion;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.mtp.Mtp3EndCongestionPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusCause;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;

/**
 *
 * @author amit bhayani
 *
 */
public class AspFactoryImpl implements AssociationListener, XMLSerializable, AspFactory {

    private static final Logger logger = Logger.getLogger(AspFactoryImpl.class);

    private static long ASP_ID_COUNT = 1L;

    private static final int SCTP_PAYLOAD_PROT_ID_M3UA = 3;

    private static final String NAME = "name";
    private static final String STARTED = "started";
    private static final String ASSOCIATION_NAME = "assocName";
    private static final String MAX_SEQUENCE_NUMBER = "maxseqnumber";
    private static final String ASP_ID = "aspid";
    private static final String HEART_BEAT = "heartbeat";

    protected String name;

    protected boolean started = false;

    protected Association association = null;
    protected String associationName = null;

    protected FastList<Asp> aspList = new FastList<Asp>();

    private ByteBuffer txBuffer = ByteBuffer.allocateDirect(8192);

    // data buffer for incoming TCP data
    private CompositeByteBuf tcpIncBuffer;

    protected Management transportManagement = null;

    protected M3UAManagementImpl m3UAManagementImpl = null;

    private ASPIdentifier aspid;

    protected ParameterFactory parameterFactory = new ParameterFactoryImpl();
    protected MessageFactory messageFactory = new MessageFactoryImpl();

    private TransferMessageHandler transferMessageHandler = new TransferMessageHandler(this);
    private SignalingNetworkManagementHandler signalingNetworkManagementHandler = new SignalingNetworkManagementHandler(this);
    private ManagementMessageHandler managementMessageHandler = new ManagementMessageHandler(this);
    private AspStateMaintenanceHandler aspStateMaintenanceHandler = new AspStateMaintenanceHandler(this);
    private AspTrafficMaintenanceHandler aspTrafficMaintenanceHandler = new AspTrafficMaintenanceHandler(this);
    private RoutingKeyManagementHandler routingKeyManagementHandler = new RoutingKeyManagementHandler(this);

    protected Functionality functionality = null;
    protected IPSPType ipspType = null;
    protected ExchangeType exchangeType = null;

    private long aspupSentTime = 0L;

    private int maxSequenceNumber = M3UAManagementImpl.MAX_SEQUENCE_NUMBER;
    private int[] slsTable = null;
    private int maxOutboundStreams;

    protected AspFactoryStopTimer aspFactoryStopTimer = null;

    protected HeartBeatTimer heartBeatTimer = null;
    private boolean isHeartBeatEnabled = false;

    private FastMap<Integer, AtomicInteger> congDpcList = new FastMap<Integer, AtomicInteger>().shared();

    public AspFactoryImpl() {
        // clean transmission buffer
        txBuffer.clear();
        txBuffer.rewind();
        txBuffer.flip();

        this.heartBeatTimer = new HeartBeatTimer(this);
    }

    public AspFactoryImpl(String name, int maxSequenceNumber, long aspId, boolean isHeartBeatEnabled) {
        this();
        this.name = name;
        this.maxSequenceNumber = maxSequenceNumber;
        this.slsTable = new int[this.maxSequenceNumber];
        this.aspid = parameterFactory.createASPIdentifier(aspId);

        this.isHeartBeatEnabled = isHeartBeatEnabled;
    }

    /**
     * @return the aspid
     */
    public ASPIdentifier getAspid() {
        return aspid;
    }

    /**
     * @return the isHeartBeatEnabled
     */
    public boolean isHeartBeatEnabled() {
        return isHeartBeatEnabled;
    }

    public void setM3UAManagement(M3UAManagementImpl m3uaManagement) {
        this.m3UAManagementImpl = m3uaManagement;
        this.transferMessageHandler.setM3UAManagement(m3uaManagement);
    }

    public M3UAManagementImpl getM3UAManagement() {
        return m3UAManagementImpl;
    }

    protected void start() throws Exception {
        this.transportManagement.startAssociation(this.association.getName());
        this.started = true;
    }

    protected void stop() throws Exception {
        this.started = false;

        if (this.association == null)
            return;

        if (this.isHeartBeatEnabled()) {
            this.heartBeatTimer.cancel();
        }

        if (this.functionality == Functionality.AS
                || (this.functionality == Functionality.SGW && this.exchangeType == ExchangeType.DE)
                || (this.functionality == Functionality.IPSP && this.exchangeType == ExchangeType.DE)
                || (this.functionality == Functionality.IPSP && this.exchangeType == ExchangeType.SE && this.ipspType == IPSPType.CLIENT)) {

            if (this.association.isConnected()) {
                ASPDown aspDown = (ASPDown) this.messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
                        MessageType.ASP_DOWN);
                this.write(aspDown);
                for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
                    AspImpl aspImpl = (AspImpl) n.getValue();

                    try {
                        FSM aspLocalFSM = aspImpl.getLocalFSM();
                        aspLocalFSM.signal(TransitionState.ASP_DOWN_SENT);

                        AsImpl peerAs = (AsImpl) aspImpl.getAs();
                        FSM asPeerFSM = peerAs.getPeerFSM();

                        asPeerFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                        asPeerFSM.signal(TransitionState.ASP_DOWN);

                    } catch (UnknownTransitionException e) {
                        logger.error(e.getMessage(), e);
                    }
                }

                // Start the timer to kill the underlying transport Association
                aspFactoryStopTimer = new AspFactoryStopTimer(this);
                this.m3UAManagementImpl.m3uaScheduler.execute(aspFactoryStopTimer);
            } else {
                for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
                    AspImpl aspImpl = (AspImpl) n.getValue();

                    try {
                        FSM aspLocalFSM = aspImpl.getLocalFSM();
                        aspLocalFSM.signal(TransitionState.COMM_DOWN);

                        AsImpl peerAs = (AsImpl) aspImpl.getAs();
                        FSM asPeerFSM = peerAs.getPeerFSM();
                        asPeerFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                        asPeerFSM.signal(TransitionState.ASP_DOWN);
                    } catch (UnknownTransitionException e) {
                        logger.error(e.getMessage(), e);
                    }
                }

                // Finally stop the association
                this.transportManagement.stopAssociation(this.association.getName());
            }

        } else {
            // if (this.association.isConnected()) {
            // throw new
            // Exception("Still few ASP's are connected. Bring down the ASP's first");
            // }

            this.transportManagement.stopAssociation(this.association.getName());
        }
    }

    public boolean getStatus() {
        return this.started;
    }

    // public boolean isConnected() {
    // return started && up;
    // }

    public Functionality getFunctionality() {
        return functionality;
    }

    protected void setFunctionality(Functionality functionality) {
        this.functionality = functionality;
    }

    public IPSPType getIpspType() {
        return ipspType;
    }

    protected void setIpspType(IPSPType ipspType) {
        this.ipspType = ipspType;
    }

    public ExchangeType getExchangeType() {
        return exchangeType;
    }

    protected void setExchangeType(ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }

    protected void setTransportManagement(Management transportManagement) {
        this.transportManagement = transportManagement;
    }

    public Association getAssociation() {
        return this.association;
    }

    protected void setAssociation(Association association) {
        // Unset the listener to previous association
        if (this.association != null) {
            this.association.setAssociationListener(null);
        }
        this.association = association;
        this.associationName = this.association.getName();
        // Set the listener for new association
        this.association.setAssociationListener(this);
    }

    protected void unsetAssociation() throws Exception {
        if (this.association != null) {
            if (this.association.isStarted()) {
                throw new Exception(String.format("Association=%s is still started. Stop first", this.association.getName()));
            }
            this.association.setAssociationListener(null);
            this.association = null;
        }
    }

    public String getName() {
        return this.name;
    }

    protected void read(M3UAMessage message) {
        switch (message.getMessageClass()) {
            case MessageClass.MANAGEMENT:
                switch (message.getMessageType()) {
                    case MessageType.ERROR:
                        this.managementMessageHandler
                                .handleError((org.mobicents.protocols.ss7.m3ua.message.mgmt.Error) message);
                        break;
                    case MessageType.NOTIFY:
                        Notify notify = (Notify) message;
                        this.managementMessageHandler.handleNotify(notify);
                        break;
                    default:
                        logger.error(String.format("Rx : MGMT with invalid MessageType=%d message=%s",
                                message.getMessageType(), message));
                        break;
                }
                break;

            case MessageClass.TRANSFER_MESSAGES:
                switch (message.getMessageType()) {
                    case MessageType.PAYLOAD:
                        PayloadData payload = (PayloadData) message;
                        this.transferMessageHandler.handlePayload(payload);
                        break;
                    default:
                        logger.error(String.format("Rx : Transfer message with invalid MessageType=%d message=%s",
                                message.getMessageType(), message));
                        break;
                }
                break;

            case MessageClass.SIGNALING_NETWORK_MANAGEMENT:
                switch (message.getMessageType()) {
                    case MessageType.DESTINATION_UNAVAILABLE:
                        DestinationUnavailable duna = (DestinationUnavailable) message;
                        this.signalingNetworkManagementHandler.handleDestinationUnavailable(duna);
                        break;
                    case MessageType.DESTINATION_AVAILABLE:
                        DestinationAvailable dava = (DestinationAvailable) message;
                        this.signalingNetworkManagementHandler.handleDestinationAvailable(dava);
                        break;
                    case MessageType.DESTINATION_STATE_AUDIT:
                        DestinationStateAudit daud = (DestinationStateAudit) message;
                        this.signalingNetworkManagementHandler.handleDestinationStateAudit(daud);
                        break;
                    case MessageType.SIGNALING_CONGESTION:
                        SignallingCongestion scon = (SignallingCongestion) message;
                        this.signalingNetworkManagementHandler.handleSignallingCongestion(scon);
                        break;
                    case MessageType.DESTINATION_USER_PART_UNAVAILABLE:
                        DestinationUPUnavailable dupu = (DestinationUPUnavailable) message;
                        this.signalingNetworkManagementHandler.handleDestinationUPUnavailable(dupu);
                        break;
                    case MessageType.DESTINATION_RESTRICTED:
                        DestinationRestricted drst = (DestinationRestricted) message;
                        this.signalingNetworkManagementHandler.handleDestinationRestricted(drst);
                        break;
                    default:
                        logger.error(String.format("Received SSNM with invalid MessageType=%d message=%s",
                                message.getMessageType(), message));
                        break;
                }
                break;

            case MessageClass.ASP_STATE_MAINTENANCE:
                switch (message.getMessageType()) {
                    case MessageType.ASP_UP:
                        ASPUp aspUp = (ASPUp) message;
                        this.aspStateMaintenanceHandler.handleAspUp(aspUp);
                        break;
                    case MessageType.ASP_UP_ACK:
                        ASPUpAck aspUpAck = (ASPUpAck) message;
                        this.aspStateMaintenanceHandler.handleAspUpAck(aspUpAck);
                        break;
                    case MessageType.ASP_DOWN:
                        ASPDown aspDown = (ASPDown) message;
                        this.aspStateMaintenanceHandler.handleAspDown(aspDown);
                        break;
                    case MessageType.ASP_DOWN_ACK:
                        ASPDownAck aspDownAck = (ASPDownAck) message;
                        this.aspStateMaintenanceHandler.handleAspDownAck(aspDownAck);
                        break;
                    case MessageType.HEARTBEAT:
                        Heartbeat hrtBeat = (Heartbeat) message;
                        this.aspStateMaintenanceHandler.handleHeartbeat(hrtBeat);
                        break;
                    case MessageType.HEARTBEAT_ACK:
                        // Nothing to do
                        break;
                    default:
                        logger.error(String.format("Received ASPSM with invalid MessageType=%d message=%s",
                                message.getMessageType(), message));
                        break;
                }

                break;

            case MessageClass.ASP_TRAFFIC_MAINTENANCE:
                switch (message.getMessageType()) {
                    case MessageType.ASP_ACTIVE:
                        ASPActive aspActive = (ASPActive) message;
                        this.aspTrafficMaintenanceHandler.handleAspActive(aspActive);
                        break;
                    case MessageType.ASP_ACTIVE_ACK:
                        ASPActiveAck aspAciveAck = (ASPActiveAck) message;
                        this.aspTrafficMaintenanceHandler.handleAspActiveAck(aspAciveAck);
                        break;
                    case MessageType.ASP_INACTIVE:
                        ASPInactive aspInactive = (ASPInactive) message;
                        this.aspTrafficMaintenanceHandler.handleAspInactive(aspInactive);
                        break;
                    case MessageType.ASP_INACTIVE_ACK:
                        ASPInactiveAck aspInaciveAck = (ASPInactiveAck) message;
                        this.aspTrafficMaintenanceHandler.handleAspInactiveAck(aspInaciveAck);
                        break;
                    default:
                        logger.error(String.format("Received ASPTM with invalid MessageType=%d message=%s",
                                message.getMessageType(), message));
                        break;
                }
                break;

            case MessageClass.ROUTING_KEY_MANAGEMENT:
                break;
            default:
                logger.error(String.format("Received message with invalid MessageClass=%d message=%s",
                        message.getMessageClass(), message));
                break;
        }
    }

    protected void write(M3UAMessage message) {
        try {
            ByteBufAllocator byteBufAllocator = this.association.getByteBufAllocator();
            ByteBuf byteBuf;
            if (byteBufAllocator != null) {
                byteBuf = byteBufAllocator.buffer();
            } else {
                byteBuf = Unpooled.buffer();
            }

            ((M3UAMessageImpl) message).encode(byteBuf);

            org.mobicents.protocols.api.PayloadData payloadData = null;

            if (this.m3UAManagementImpl.isSctpLibNettySupport()) {
                switch (message.getMessageClass()) {
                    case MessageClass.ASP_STATE_MAINTENANCE:
                    case MessageClass.MANAGEMENT:
                    case MessageClass.ROUTING_KEY_MANAGEMENT:
                        payloadData = new org.mobicents.protocols.api.PayloadData(byteBuf.readableBytes(), byteBuf, true, true,
                                SCTP_PAYLOAD_PROT_ID_M3UA, 0);
                        break;
                    case MessageClass.TRANSFER_MESSAGES:
                        PayloadData payload = (PayloadData) message;
                        int seqControl = payload.getData().getSLS();
                        payloadData = new org.mobicents.protocols.api.PayloadData(byteBuf.readableBytes(), byteBuf, true,
                                false, SCTP_PAYLOAD_PROT_ID_M3UA, this.slsTable[seqControl]);
                        break;
                    default:
                        payloadData = new org.mobicents.protocols.api.PayloadData(byteBuf.readableBytes(), byteBuf, true, true,
                                SCTP_PAYLOAD_PROT_ID_M3UA, 0);
                        break;
                }

                this.association.send(payloadData);

                // congestion control - we will send MTP-PAUSE every 8 messages
                int congLevel = this.association.getCongestionLevel();
                if (message instanceof PayloadData) {
                    PayloadData payloadData2 = (PayloadData) message;
                    if (congLevel > 0) {
                        sendCongestionInfoToMtp3Users(congLevel, payloadData2.getData().getDpc());
                    } else {
                        sendCongestionEndInfoToMtp3Users(congLevel, payloadData2.getData().getDpc());
                    }
                }
            } else {
                byte[] bf = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bf);
                synchronized (txBuffer) {
                    switch (message.getMessageClass()) {
                        case MessageClass.ASP_STATE_MAINTENANCE:
                        case MessageClass.MANAGEMENT:
                        case MessageClass.ROUTING_KEY_MANAGEMENT:
                            payloadData = new org.mobicents.protocols.api.PayloadData(byteBuf.readableBytes(), bf, true, true,
                                    SCTP_PAYLOAD_PROT_ID_M3UA, 0);
                            break;
                        case MessageClass.TRANSFER_MESSAGES:
                            PayloadData payload = (PayloadData) message;
                            int seqControl = payload.getData().getSLS();
                            payloadData = new org.mobicents.protocols.api.PayloadData(byteBuf.readableBytes(), bf, true, false,
                                    SCTP_PAYLOAD_PROT_ID_M3UA, this.slsTable[seqControl]);
                            break;
                        default:
                            payloadData = new org.mobicents.protocols.api.PayloadData(byteBuf.readableBytes(), bf, true, true,
                                    SCTP_PAYLOAD_PROT_ID_M3UA, 0);
                            break;
                    }

                    this.association.send(payloadData);
                }
            }
        } catch (Throwable e) {
            logger.error(String.format("Error while trying to send PayloadData to SCTP layer. M3UAMessage=%s", message), e);
        }
    }

    private void sendCongestionInfoToMtp3Users(int congLevel, int dpc) {
        AtomicInteger ai = congDpcList.get(dpc);
        if (ai == null) {
            ai = new AtomicInteger();
            congDpcList.put(dpc, ai);
        }
        if (ai.incrementAndGet() % 8 == 0) {
            Mtp3StatusPrimitive statusPrimitive = new Mtp3StatusPrimitive(dpc, Mtp3StatusCause.SignallingNetworkCongested,
                    congLevel, 0);
            this.m3UAManagementImpl.sendStatusMessageToLocalUser(statusPrimitive);
        }
    }

    private void sendCongestionEndInfoToMtp3Users(int congLevel, int dpc) {
        AtomicInteger ai = congDpcList.get(dpc);
        if (ai == null) {
            return;
        }

        ai = new AtomicInteger();
        congDpcList.remove(dpc);
        Mtp3EndCongestionPrimitive endCongestionPrimitive = new Mtp3EndCongestionPrimitive(dpc);
        this.m3UAManagementImpl.sendEndCongestionMessageToLocalUser(endCongestionPrimitive);
    }

    protected AspImpl createAsp() {
        AspImpl remAsp = new AspImpl(this.name, this);

        // We set ASP IP only if its AS or IPSP Client side
        if (this.getFunctionality() == Functionality.AS
                || (this.getFunctionality() == Functionality.IPSP && this.getIpspType() == IPSPType.CLIENT)) {
            remAsp.setASPIdentifier(aspid);
        }

        this.aspList.add(remAsp);
        return remAsp;
    }

    protected boolean destroyAsp(AspImpl aspImpl) {
        aspImpl.aspFactoryImpl = null;
        return this.aspList.remove(aspImpl);
    }

    public List<Asp> getAspList() {
        return this.aspList.unmodifiable();
    }

    protected AspImpl getAsp(long rc) {
        for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
            Asp aspImpl = n.getValue();
            if (aspImpl.getAs().getRoutingContext() != null
                    && aspImpl.getAs().getRoutingContext().getRoutingContexts()[0] == rc) {
                return (AspImpl) aspImpl;
            }
        }
        return null;
    }

    protected void sendAspActive(AsImpl asImpl) {
        ASPActive aspActive = (ASPActive) this.messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
                MessageType.ASP_ACTIVE);
        aspActive.setRoutingContext(asImpl.getRoutingContext());
        aspActive.setTrafficModeType(asImpl.getTrafficModeType());
        this.write(aspActive);
    }

    protected static long generateId() {
        ASP_ID_COUNT++;
        if (ASP_ID_COUNT == 4294967295L) {
            ASP_ID_COUNT = 1L;
        }
        return ASP_ID_COUNT;
    }

    private void handleCommDown() {

        if (this.isHeartBeatEnabled()) {
            this.heartBeatTimer.cancel();
        }

        for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
            AspImpl aspImpl = (AspImpl) n.getValue();
            try {
                FSM aspLocalFSM = aspImpl.getLocalFSM();
                if (aspLocalFSM != null) {
                    aspLocalFSM.signal(TransitionState.COMM_DOWN);
                }

                FSM aspPeerFSM = aspImpl.getPeerFSM();
                if (aspPeerFSM != null) {
                    aspPeerFSM.signal(TransitionState.COMM_DOWN);
                }

                AsImpl asImpl = (AsImpl) aspImpl.getAs();

                FSM asLocalFSM = asImpl.getLocalFSM();
                if (asLocalFSM != null) {
                    asLocalFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                    asLocalFSM.signal(TransitionState.ASP_DOWN);
                }

                FSM asPeerFSM = asImpl.getPeerFSM();
                if (asPeerFSM != null) {
                    asPeerFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                    asPeerFSM.signal(TransitionState.ASP_DOWN);
                }
            } catch (UnknownTransitionException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    protected void sendAspUp() {
        // TODO : Possibility of race condition?
        long now = System.currentTimeMillis();
        if ((now - aspupSentTime) > 2000) {
            ASPUp aspUp = (ASPUp) this.messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP);
            aspUp.setASPIdentifier(this.aspid);
            this.write(aspUp);
            aspupSentTime = now;
        }
    }

    private void handleCommUp() {

        if (this.isHeartBeatEnabled()) {
            this.heartBeatTimer.start();
            this.heartBeatTimer.reset();
            this.m3UAManagementImpl.m3uaScheduler.execute(this.heartBeatTimer);
        }

        if (this.functionality == Functionality.AS
                || (this.functionality == Functionality.SGW && this.exchangeType == ExchangeType.DE)
                || (this.functionality == Functionality.IPSP && this.exchangeType == ExchangeType.DE)
                || (this.functionality == Functionality.IPSP && this.exchangeType == ExchangeType.SE && this.ipspType == IPSPType.CLIENT)) {
            this.aspupSentTime = 0L;
            this.sendAspUp();
        }

        for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
            AspImpl aspImpl = (AspImpl) n.getValue();
            try {
                FSM aspLocalFSM = aspImpl.getLocalFSM();
                if (aspLocalFSM != null) {
                    aspLocalFSM.signal(TransitionState.COMM_UP);
                }

                FSM aspPeerFSM = aspImpl.getPeerFSM();
                if (aspPeerFSM != null) {
                    aspPeerFSM.signal(TransitionState.COMM_UP);
                }

            } catch (UnknownTransitionException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<AspFactoryImpl> ASP_FACTORY_XML = new XMLFormat<AspFactoryImpl>(AspFactoryImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, AspFactoryImpl aspFactoryImpl) throws XMLStreamException {
            aspFactoryImpl.name = xml.getAttribute(NAME, "");
            aspFactoryImpl.associationName = xml.getAttribute(ASSOCIATION_NAME, "");
            aspFactoryImpl.started = xml.getAttribute(STARTED).toBoolean();
            aspFactoryImpl.maxSequenceNumber = xml.getAttribute(MAX_SEQUENCE_NUMBER, M3UAManagementImpl.MAX_SEQUENCE_NUMBER);
            aspFactoryImpl.slsTable = new int[aspFactoryImpl.maxSequenceNumber];

            // For backward compatible
            long aspIdTemp = xml.getAttribute(ASP_ID, aspFactoryImpl.generateId());

            aspFactoryImpl.aspid = aspFactoryImpl.parameterFactory.createASPIdentifier(aspIdTemp);

            aspFactoryImpl.isHeartBeatEnabled = xml.getAttribute(HEART_BEAT, false);
        }

        @Override
        public void write(AspFactoryImpl aspFactoryImpl, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(NAME, aspFactoryImpl.name);
            xml.setAttribute(ASSOCIATION_NAME, aspFactoryImpl.associationName);
            xml.setAttribute(STARTED, aspFactoryImpl.started);
            xml.setAttribute(MAX_SEQUENCE_NUMBER, aspFactoryImpl.maxSequenceNumber);
            xml.setAttribute(ASP_ID, aspFactoryImpl.aspid.getAspId());
            xml.setAttribute(HEART_BEAT, aspFactoryImpl.isHeartBeatEnabled);
        }
    };

    /**
     * AssociationListener methods
     */

    @Override
    public void onCommunicationLost(Association association) {
        logger.warn(String.format("Communication channel lost for AspFactroy=%s Association=%s", this.name,
                association.getName()));
        this.handleCommDown();
    }

    @Override
    public void onCommunicationRestart(Association association) {
        logger.warn(String.format("Communication channel restart for AspFactroy=%s Association=%s", this.name,
                association.getName()));

        //TODO : Is this correct way to handle?

        try {
            this.transportManagement.stopAssociation(this.associationName);
        } catch (Exception e) {
            logger.warn(String.format("Error while trying to stop underlying Association for AspFactpry=%s",
                    this.getName()), e);
        }

        try {
            this.transportManagement.startAssociation(this.associationName);
        } catch (Exception e) {
            logger.error(String.format("Error while trying to start underlying Association for AspFactpry=%s",
                    this.getName()), e);
        }
    }

    @Override
    public void onCommunicationShutdown(Association association) {
        logger.warn(String.format("Communication channel shutdown for AspFactroy=%s Association=%s", this.name,
                association.getName()));
        this.handleCommDown();

    }

    @Override
    public void onCommunicationUp(Association association, int maxInboundStreams, int maxOutboundStreams) {
        this.maxOutboundStreams = maxOutboundStreams;
        // Recreate SLS table. Minimum of two is correct?
        this.createSLSTable(Math.min(maxInboundStreams, maxOutboundStreams) - 1);
        this.handleCommUp();
    }

    protected void createSLSTable(int minimumBoundStream) {
        if (minimumBoundStream == 0) { // special case - only 1 stream
            for (int i = 0; i < this.maxSequenceNumber; i++) {
                slsTable[i] = 0;
            }
        } else {
            // SCTP Stream 0 is for management messages, we start from 1
            int stream = 1;
            for (int i = 0; i < this.maxSequenceNumber; i++) {
                if (stream > minimumBoundStream) {
                    stream = 1;
                }
                slsTable[i] = stream++;
            }
        }
    }

    @Override
    public void onPayload(Association association, org.mobicents.protocols.api.PayloadData payloadData) {
        try {
            M3UAMessage m3UAMessage;
            if (this.m3UAManagementImpl.sctpLibNettySupport) {
                ByteBuf byteBuf = payloadData.getByteBuf();
                processPayload(association.getIpChannelType(), byteBuf);
            } else {
                byte[] m3uadata = payloadData.getData();
                ByteBuf byteBuf = Unpooled.wrappedBuffer(m3uadata);
                processPayload(association.getIpChannelType(), byteBuf);
            }
        } catch (Throwable e) {
            logger.error(
                    String.format("Error while trying to process PayloadData from SCTP layer. payloadData=%s", payloadData), e);
        }
    }

    private void processPayload(IpChannelType ipChannelType, ByteBuf byteBuf) {
        M3UAMessage m3UAMessage;
        if (ipChannelType == IpChannelType.SCTP) {
            try {
                // TODO where is streamNumber stored?
                m3UAMessage = this.messageFactory.createMessage(byteBuf);
                if (this.isHeartBeatEnabled()) {
                    this.heartBeatTimer.reset();
                }
                this.read(m3UAMessage);
            } finally {
                ReferenceCountUtil.release(byteBuf);
            }
        } else {
            if (tcpIncBuffer == null) {
                tcpIncBuffer = byteBuf.alloc().compositeBuffer();
            }
            tcpIncBuffer.addComponent(byteBuf);
            tcpIncBuffer.writerIndex(tcpIncBuffer.capacity());

            while (true) {
                m3UAMessage = this.messageFactory.createMessage(tcpIncBuffer);
                if (m3UAMessage == null)
                    break;

                if (this.isHeartBeatEnabled()) {
                    this.heartBeatTimer.reset();
                }
                this.read(m3UAMessage);
            }
            tcpIncBuffer.discardReadBytes();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.api.AssociationListener#inValidStreamId(org.mobicents .protocols.api.PayloadData)
     */
    @Override
    public void inValidStreamId(org.mobicents.protocols.api.PayloadData payloadData) {
        logger.error(String
                .format("Tx : PayloadData with streamNumber=%d which is greater than or equal to maxSequenceNumber=%d. Droping PayloadData=%s",
                        payloadData.getStreamNumber(), this.maxOutboundStreams, payloadData));
    }

    public void show(StringBuffer sb) {
        sb.append(M3UAOAMMessages.SHOW_ASP_NAME).append(this.name).append(M3UAOAMMessages.SHOW_ASPID)
                .append(this.aspid.getAspId()).append(M3UAOAMMessages.SHOW_HEARTBEAT_ENABLED).append(this.isHeartBeatEnabled())
                .append(M3UAOAMMessages.SHOW_SCTP_ASSOC).append(this.associationName).append(M3UAOAMMessages.SHOW_STARTED)
                .append(this.started);

        sb.append(M3UAOAMMessages.NEW_LINE);
        sb.append(M3UAOAMMessages.SHOW_ASSIGNED_TO);

        for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
            AspImpl aspImpl = (AspImpl) n.getValue();
            sb.append(M3UAOAMMessages.TAB).append(M3UAOAMMessages.SHOW_AS_NAME).append(aspImpl.getAs().getName())
                    .append(M3UAOAMMessages.SHOW_FUNCTIONALITY).append(this.functionality).append(M3UAOAMMessages.SHOW_MODE)
                    .append(this.exchangeType);

            if (this.functionality == Functionality.IPSP) {
                sb.append(M3UAOAMMessages.SHOW_IPSP_TYPE).append(this.ipspType);
            }

            if (aspImpl.getLocalFSM() != null) {
                sb.append(M3UAOAMMessages.SHOW_LOCAL_FSM_STATE).append(aspImpl.getLocalFSM().getState());
            }

            if (aspImpl.getPeerFSM() != null) {
                sb.append(M3UAOAMMessages.SHOW_PEER_FSM_STATE).append(aspImpl.getPeerFSM().getState());
            }

            sb.append(M3UAOAMMessages.NEW_LINE);
        }
    }

}
