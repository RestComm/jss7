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

package org.mobicents.protocols.ss7.m3ua.impl;

import java.nio.ByteBuffer;

import javolution.util.FastList;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.AssociationListener;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
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

/**
 * 
 * @author amit bhayani
 * 
 */
public class AspFactory implements AssociationListener, XMLSerializable {

	private static final Logger logger = Logger.getLogger(AspFactory.class);

	private static long ASP_ID = 1l;

	private static final String NAME = "name";
	private static final String STARTED = "started";
	private static final String ASSOCIATION_NAME = "assocName";

	private volatile boolean channelConnected = false;

	protected String name;

	protected boolean started = false;

	protected Association association = null;
	protected String associationName = null;

	protected FastList<Asp> aspList = new FastList<Asp>();

	private ByteBuffer txBuffer = ByteBuffer.allocateDirect(8192);

	protected Management transportManagement = null;

	private ASPIdentifier aspid;

	protected ParameterFactory parameterFactory = new ParameterFactoryImpl();
	protected MessageFactory messageFactory = new MessageFactoryImpl();

	private TransferMessageHandler transferMessageHandler = new TransferMessageHandler(this);
	private SignalingNetworkManagementHandler signalingNetworkManagementHandler = new SignalingNetworkManagementHandler(
			this);
	private ManagementMessageHandler managementMessageHandler = new ManagementMessageHandler(this);
	private AspStateMaintenanceHandler aspStateMaintenanceHandler = new AspStateMaintenanceHandler(this);
	private AspTrafficMaintenanceHandler aspTrafficMaintenanceHandler = new AspTrafficMaintenanceHandler(this);
	private RoutingKeyManagementHandler routingKeyManagementHandler = new RoutingKeyManagementHandler(this);

	protected Functionality functionality = null;
	protected IPSPType ipspType = null;
	protected ExchangeType exchangeType = null;

	private long aspupSentTime = 0l;

	public AspFactory() {

		this.aspid = parameterFactory.createASPIdentifier(this.generateId());

		// clean transmission buffer
		txBuffer.clear();
		txBuffer.rewind();
		txBuffer.flip();
	}

	public AspFactory(String name) {
		this();
		this.name = name;
	}

	public void start() throws Exception {
		this.transportManagement.startAssociation(this.association.getName());
		this.started = true;
	}

	public void stop() throws Exception {
		this.started = false;

		if (this.functionality == Functionality.AS
				|| (this.functionality == Functionality.SGW && this.exchangeType == ExchangeType.DE)
				|| (this.functionality == Functionality.IPSP && this.exchangeType == ExchangeType.DE)
				|| (this.functionality == Functionality.IPSP && this.exchangeType == ExchangeType.SE && this.ipspType == IPSPType.CLIENT)) {

			if (this.channelConnected) {
				ASPDown aspDown = (ASPDown) this.messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
						MessageType.ASP_DOWN);
				this.write(aspDown);
				for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
					Asp asp = n.getValue();

					try {
						FSM aspLocalFSM = asp.getLocalFSM();
						aspLocalFSM.signal(TransitionState.ASP_DOWN_SENT);

						As peerAs = asp.getAs();
						FSM asPeerFSM = peerAs.getPeerFSM();

						asPeerFSM.setAttribute(As.ATTRIBUTE_ASP, asp);
						asPeerFSM.signal(TransitionState.ASP_DOWN);

					} catch (UnknownTransitionException e) {
						logger.error(e.getMessage(), e);
					}
				}
			} else {
				for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
					Asp asp = n.getValue();

					try {
						FSM aspLocalFSM = asp.getLocalFSM();
						aspLocalFSM.signal(TransitionState.COMM_DOWN);

						As peerAs = asp.getAs();
						FSM asPeerFSM = peerAs.getPeerFSM();
						asPeerFSM.setAttribute(As.ATTRIBUTE_ASP, asp);
						asPeerFSM.signal(TransitionState.ASP_DOWN);
					} catch (UnknownTransitionException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}

		} else {
			if (this.channelConnected) {
				throw new Exception("Still few ASP's are connected. Bring down the ASP's first");
			}

			this.transportManagement.stopAssociation(this.association.getName());
		}
	}

	public boolean getStatus() {
		return this.started;
	}

	public Functionality getFunctionality() {
		return functionality;
	}

	public void setFunctionality(Functionality functionality) {
		this.functionality = functionality;
	}

	public IPSPType getIpspType() {
		return ipspType;
	}

	public void setIpspType(IPSPType ipspType) {
		this.ipspType = ipspType;
	}

	public ExchangeType getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(ExchangeType exchangeType) {
		this.exchangeType = exchangeType;
	}

	public void setTransportManagement(Management transportManagement) {
		this.transportManagement = transportManagement;
	}

	public void setAssociation(Association association) {
		// Unset the listener to previous association
		if (this.association != null) {
			this.association.setAssociationListener(null);
		}
		this.association = association;
		this.associationName = this.association.getName();
		// Set the listener for new association
		this.association.setAssociationListener(this);
	}

	public void unsetAssociation() throws Exception {
		if (this.association != null) {
			if (this.association.isStarted()) {
				throw new Exception(String.format("Association=%s is still started. Stop first",
						this.association.getName()));
			}
			this.association.setAssociationListener(null);
			this.association = null;
		}
	}

	public String getName() {
		return this.name;
	}

	public void read(M3UAMessage message) {
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

	public void write(M3UAMessage message) {

		synchronized (txBuffer) {
			try {
				txBuffer.clear();
				((M3UAMessageImpl) message).encode(txBuffer);
				txBuffer.flip();

				byte[] data = new byte[txBuffer.limit()];
				txBuffer.get(data);

				org.mobicents.protocols.api.PayloadData payloadData = null;

				switch (message.getMessageClass()) {
				case MessageClass.ASP_STATE_MAINTENANCE:
				case MessageClass.MANAGEMENT:
				case MessageClass.ROUTING_KEY_MANAGEMENT:
					payloadData = new org.mobicents.protocols.api.PayloadData(data.length, data, true, true, 3, 0);
					break;
				case MessageClass.TRANSFER_MESSAGES:
					PayloadData payload = (PayloadData) message;
					payloadData = new org.mobicents.protocols.api.PayloadData(data.length, data, true, false, 3,
							payload.getData().getSLS());
					break;
				default:
					payloadData = new org.mobicents.protocols.api.PayloadData(data.length, data, true, true, 3, 0);
					break;
				}

				this.association.send(payloadData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected Asp createAsp() {
		Asp remAsp = new Asp(this.name, this);

		// We set ASP IP only if its AS or IPSP Client side
		if (this.getFunctionality() == Functionality.AS
				|| (this.getFunctionality() == Functionality.IPSP && this.getIpspType() == IPSPType.CLIENT)) {
			remAsp.setASPIdentifier(aspid);
		}

		this.aspList.add(remAsp);
		return remAsp;
	}

	public boolean destroyAsp(Asp asp) {
		asp.aspFactory = null;
		return this.aspList.remove(asp);
	}

	public FastList<Asp> getAspList() {
		return this.aspList;
	}

	protected Asp getAsp(long rc) {
		for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();
			if (asp.getAs().getRoutingContext().getRoutingContexts()[0] == rc) {
				return asp;
			}
		}
		return null;
	}

	protected void sendAspActive(As as) {
		ASPActive aspActive = (ASPActive) this.messageFactory.createMessage(MessageClass.ASP_TRAFFIC_MAINTENANCE,
				MessageType.ASP_ACTIVE);
		aspActive.setRoutingContext(as.getRoutingContext());
		aspActive.setTrafficModeType(as.getTrafficModeType());
		this.write(aspActive);
	}

	private long generateId() {
		ASP_ID++;
		if (ASP_ID == 4294967295l) {
			ASP_ID = 1l;
		}
		return ASP_ID;
	}

	private void handleCommDown() {

		this.channelConnected = false;
		for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();
			try {
				FSM aspLocalFSM = asp.getLocalFSM();
				if (aspLocalFSM != null) {
					aspLocalFSM.signal(TransitionState.COMM_DOWN);
				}

				FSM aspPeerFSM = asp.getPeerFSM();
				if (aspPeerFSM != null) {
					aspPeerFSM.signal(TransitionState.COMM_DOWN);
				}

				As as = asp.getAs();

				FSM asLocalFSM = as.getLocalFSM();
				if (asLocalFSM != null) {
					asLocalFSM.setAttribute(As.ATTRIBUTE_ASP, asp);
					asLocalFSM.signal(TransitionState.ASP_DOWN);
				}

				FSM asPeerFSM = as.getPeerFSM();
				if (asPeerFSM != null) {
					asPeerFSM.setAttribute(As.ATTRIBUTE_ASP, asp);
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
			ASPUp aspUp = (ASPUp) this.messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
					MessageType.ASP_UP);
			aspUp.setASPIdentifier(this.aspid);
			this.write(aspUp);
			aspupSentTime = now;
		}
	}

	private void handleCommUp() {
		this.channelConnected = true;
		if (this.functionality == Functionality.AS
				|| (this.functionality == Functionality.SGW && this.exchangeType == ExchangeType.DE)
				|| (this.functionality == Functionality.IPSP && this.exchangeType == ExchangeType.DE)
				|| (this.functionality == Functionality.IPSP && this.exchangeType == ExchangeType.SE && this.ipspType == IPSPType.CLIENT)) {
			this.aspupSentTime = 0l;
			this.sendAspUp();
		}

		for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();
			try {
				FSM aspLocalFSM = asp.getLocalFSM();
				if (aspLocalFSM != null) {
					aspLocalFSM.signal(TransitionState.COMM_UP);
				}

				FSM aspPeerFSM = asp.getPeerFSM();
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
	protected static final XMLFormat<AspFactory> ASP_FACTORY_XML = new XMLFormat<AspFactory>(AspFactory.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, AspFactory aspFactory) throws XMLStreamException {
			aspFactory.name = xml.getAttribute(NAME, "");
			aspFactory.associationName = xml.getAttribute(ASSOCIATION_NAME, "");
			aspFactory.started = xml.getAttribute(STARTED).toBoolean();
		}

		@Override
		public void write(AspFactory aspFactory, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
			xml.setAttribute(NAME, aspFactory.name);
			xml.setAttribute(ASSOCIATION_NAME, aspFactory.associationName);
			xml.setAttribute(STARTED, aspFactory.started);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onCommunicationShutdown(Association association) {
		logger.warn(String.format("Communication channel shutdown for AspFactroy=%s Association=%s", this.name,
				association.getName()));
		this.handleCommDown();

	}

	@Override
	public void onCommunicationUp(Association association) {
		this.handleCommUp();
	}

	@Override
	public void onPayload(Association association, org.mobicents.protocols.api.PayloadData payloadData) {

		byte[] m3uadata = payloadData.getData();
		M3UAMessage m3UAMessage;
		if (association.getIpChannelType() == IpChannelType.SCTP) {
			// TODO where is streamNumber stored?
			m3UAMessage = this.messageFactory.createSctpMessage(m3uadata);
			this.read(m3UAMessage);
		} else {
			ByteBuffer buffer = ByteBuffer.wrap(m3uadata);
			while (true) {
				m3UAMessage = this.messageFactory.createMessage(buffer);
				if (m3UAMessage == null)
					break;
				this.read(m3UAMessage);
			}
		}
	}

	public void show(StringBuffer sb) {
		sb.append(M3UAOAMMessages.SHOW_ASP_NAME).append(this.name).append(M3UAOAMMessages.SHOW_SCTP_ASSOC)
				.append(this.associationName).append(M3UAOAMMessages.SHOW_STARTED).append(this.started);

		sb.append(M3UAOAMMessages.NEW_LINE);
		sb.append(M3UAOAMMessages.SHOW_ASSIGNED_TO);

		for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();
			sb.append(M3UAOAMMessages.TAB).append(M3UAOAMMessages.SHOW_AS_NAME).append(asp.getAs().getName())
					.append(M3UAOAMMessages.SHOW_FUNCTIONALITY).append(this.functionality)
					.append(M3UAOAMMessages.SHOW_MODE).append(this.exchangeType);

			if (this.functionality == Functionality.IPSP) {
				sb.append(M3UAOAMMessages.SHOW_IPSP_TYPE).append(this.ipspType);
			}

			if (asp.getLocalFSM() != null) {
				sb.append(M3UAOAMMessages.SHOW_LOCAL_FSM_STATE).append(asp.getLocalFSM().getState());
			}

			if (asp.getPeerFSM() != null) {
				sb.append(M3UAOAMMessages.SHOW_PEER_FSM_STATE).append(asp.getPeerFSM().getState());
			}
			
			sb.append(M3UAOAMMessages.NEW_LINE);
		}
	}
}
