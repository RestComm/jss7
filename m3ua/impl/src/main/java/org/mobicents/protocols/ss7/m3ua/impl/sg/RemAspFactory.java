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

import javolution.util.FastList;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.TransitionState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;
import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPDown;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPDownAck;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPUp;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPUpAck;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPActive;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPActiveAck;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPInactive;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPInactiveAck;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * 
 * @author amit bhayani
 * 
 */
public class RemAspFactory extends AspFactory {

	private static Logger logger = Logger.getLogger(RemAspFactory.class);
	
	public RemAspFactory(){
		super();
	}

	public RemAspFactory(String name, String ip, int port, M3UAProvider provider) {
		super(name, ip, port, provider);
	}

	@Override
	public Asp createAsp() {
		RemAspImpl remAsp = new RemAspImpl(this.name, this.m3UAProvider, this);
		this.aspList.add(remAsp);
		return remAsp;
	}

	@Override
	public void read(M3UAMessage message) {

		switch (message.getMessageClass()) {

		case MessageClass.MANAGEMENT:
			switch (message.getMessageType()) {
			case MessageType.ERROR:
			case MessageType.NOTIFY:
			}
			break;

		case MessageClass.TRANSFER_MESSAGES:
			switch (message.getMessageType()) {
			case MessageType.PAYLOAD:
				PayloadData payload = (PayloadData) message;
				this.handlePayload(payload);
				break;
			default:
				break;
			}
			break;

		case MessageClass.SIGNALING_NETWORK_MANAGEMENT:
			logger.warn(String.format("Received %s. Handling of SSNM message is not yet implemented", message));
			break;

		case MessageClass.ASP_STATE_MAINTENANCE:
			switch (message.getMessageType()) {
			case MessageType.ASP_UP:
				ASPUp aspUp = (ASPUp) message;
				this.handleAspUp(aspUp);
				break;
			case MessageType.ASP_DOWN:
				// The SGP MUST send an ASP Down Ack message in response to a
				// received ASP Down message from the ASP even if the ASP is
				// already marked as ASP-DOWN at the SGP.
				ASPDown aspDown = (ASPDown) message;
				this.handleAspDown(aspDown);
				break;
			case MessageType.HEARTBEAT:
				break;
			default:
				break;
			}

			break;

		case MessageClass.ASP_TRAFFIC_MAINTENANCE:
			// ASPTM should be given to only respective ASP's
			switch (message.getMessageType()) {
			case MessageType.ASP_ACTIVE:
				ASPActive aspActive = (ASPActive) message;
				this.handleAspActive(aspActive);
				break;
			case MessageType.ASP_INACTIVE:
				ASPInactive aspInactive = (ASPInactive) message;
				this.handleAspInactive(aspInactive);
				break;
			default:
				break;
			}

			break;

		case MessageClass.ROUTING_KEY_MANAGEMENT:
			break;

		default:

			break;

		}
	}

	private void handlePayload(PayloadData payload) {
		// Payload is always for single AS
		long rc = payload.getRoutingContext().getRoutingContexts()[0];
		Asp asp = this.getAsp(rc);

		if (asp == null) {
			logger.error(String.format("received PayloadData for RoutingContext=%d. But no ASP found. Message=%s", rc,
					payload));
			return;
		}

		if (asp.getState() == AspState.ACTIVE) {
			asp.getAs().received(payload);
		} else {
			logger.error(String.format("Received PayloadData for RoutingContext=%d. But ASP State=%s. Message=%s", rc,
					asp.getState(), payload));
		}

	}

	private void handleAspDown(ASPDown aspDown) {
		System.out.println("RemAspfactory received ASP_DOWN");
		ASPDownAck aspDwnAck = (ASPDownAck) this.m3UAProvider.getMessageFactory().createMessage(
				MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK);
		this.write(aspDwnAck);

		// ASPSM should be given to all ASP's
		for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();

			try {
				// Signal ASP about state change
				asp.getFSM().setAttribute(FSM.ATTRIBUTE_MESSAGE, aspDown);
				asp.getFSM().signal(TransitionState.ASP_DOWN);

				// Signal corresponding AS about ASP's state transition
				asp.getAs().aspStateChange(asp, TransitionState.ASP_DOWN);
			} catch (UnknownTransitionException e) {
				logger.error(e.getMessage(), e);
			}

		}
	}

	private void handleAspUp(ASPUp aspUp) {
		ASPUpAck aspUpAck = (ASPUpAck) this.m3UAProvider.getMessageFactory().createMessage(
				MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);

		ASPIdentifier aspId = aspUp.getASPIdentifier();
		if (aspId != null) {
			aspUpAck.setASPIdentifier(aspId);
		}

		this.write(aspUpAck);

		// If an ASP Up message is received and, internally, the remote ASP is
		// in the ASP-ACTIVE state, an ASP Up Ack message is returned, as well
		// as an Error message ("Unexpected Message"). In addition, the remote
		// ASP state is changed to ASP-INACTIVE in all relevant Application
		// Servers, and all registered Routing Keys are considered deregistered.

		for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();

			// Set the ASPIdentifier for each ASP
			asp.setASPIdentifier(aspUp.getASPIdentifier());

			if (asp.getState() == AspState.ACTIVE) {
				// Check if ASP is in ACTIVE, its error state
				this.sendError(ErrorCode.Unexpected_Message, asp.getAs().getRoutingContext());
				// break;
			}

			try {
				// Signal ASP about state change
				asp.getFSM().setAttribute(FSM.ATTRIBUTE_MESSAGE, aspUp);
				asp.getFSM().signal(TransitionState.ASP_UP);

				// Signal corresponding AS about ASP's state transition
				asp.getAs().aspStateChange(asp, TransitionState.ASP_UP);
			} catch (UnknownTransitionException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void handleAspInactive(ASPInactive aspInactive) {
		long[] rcs = null;
		RoutingContext rcObj = aspInactive.getRoutingContext();
		if (rcObj == null) {

			if (this.aspList.isEmpty()) {
				// If the received ASP Inactive message does not contain
				// an RC parameter and the RK is not defined (by either
				// static configuration or dynamic registration), the
				// SGP/IPSP MUST respond with an ERROR message with the
				// Error Code "No configured AS for ASP".
				this.sendError(ErrorCode.No_Configured_AS_for_ASP, null);
				return;
			}

			// Send ASP Inactive to all ASP's
			rcs = new long[this.aspList.size()];
			for (int count = 0; count < rcs.length; count++) {
				rcs[count] = this.aspList.get(count).getAs().getRoutingContext().getRoutingContexts()[0];
			}
		} else {
			rcs = rcObj.getRoutingContexts();
		}

		for (long routCntx : rcs) {
			RemAspImpl remAsp = this.getAsp(routCntx);

			// RemAsp is null means Remote AS is null
			if (remAsp == null) {
				// If the received ASP Inactive message contains an RC parameter
				// that is not defined (by either static configuration or
				// dynamic registration), the SGP/IPSP MUST respond with an
				// ERROR message with the Error Code "Invalid Routing Context".
				RoutingContext rc1 = this.m3UAProvider.getParameterFactory().createRoutingContext(
						new long[] { routCntx });
				this.sendError(ErrorCode.Invalid_Routing_Context, rc1);
			}

			As appServer = remAsp.getAs();

			ASPInactiveAck aspInactAck = (ASPInactiveAck) this.m3UAProvider.getMessageFactory().createMessage(
					MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE_ACK);
			aspInactAck.setRoutingContext(appServer.getRoutingContext());

			this.write(aspInactAck);

			try {
				remAsp.getFSM().setAttribute(FSM.ATTRIBUTE_MESSAGE, aspInactive);
				remAsp.getFSM().signal(TransitionState.ASP_INACTIVE);

				// Signal AS to transition
				appServer.aspStateChange(remAsp, TransitionState.ASP_INACTIVE);
			} catch (UnknownTransitionException e) {
				logger.error(e.getMessage(), e);
			}

		}
	}

	private void handleAspActive(ASPActive aspActive) {

		RoutingContext rcObj = aspActive.getRoutingContext();
		long[] rcs = null;
		if (rcObj == null) {

			if (this.aspList.isEmpty()) {
				// If the RC parameter is not included in the ASP Active
				// message and there are no RKs defined, the peer node
				// SHOULD respond with and ERROR message with the Error
				// Code "Invalid Routing Context".
				this.sendError(ErrorCode.Invalid_Routing_Context, null);
				// TODO : Shouldn't the ErrorCode be
				// ErrorCode.No_Configured_AS_for_ASP?
				return;
			}

			// Send ASP Active to all ASP's
			rcs = new long[this.aspList.size()];
			for (int count = 0; count < rcs.length; count++) {
				rcs[count] = this.aspList.get(count).getAs().getRoutingContext().getRoutingContexts()[0];
			}
		} else {
			rcs = rcObj.getRoutingContexts();
		}

		TrafficModeType trfModType = aspActive.getTrafficModeType();

		for (long routCntx : rcs) {
			RemAspImpl remAsp = this.getAsp(routCntx);

			// RemAsp is null means Remote AS is null
			if (remAsp == null) {
				// If the RC parameter is included in the ASP Active message
				// and a corresponding RK has not been previously defined
				// (by either static configuration or dynamic registration),
				// the peer MUST respond with an ERROR message with the
				// Error Code "No configured AS for ASP".
				RoutingContext rc1 = this.m3UAProvider.getParameterFactory().createRoutingContext(
						new long[] { routCntx });
				this.sendError(ErrorCode.No_Configured_AS_for_ASP, rc1);
				// TODO : Shouldn't is be ErrorCode.Invalid_Routing_Context ?
				continue;

			}

			As appServer = remAsp.getAs();

			if (appServer.getTrafficModeType() != null) {
				// AppServer has Traffic Mode Type defined check if it
				// matches with sent ASP ACTIVE Message
				if (trfModType != null && appServer.getTrafficModeType().getMode() != trfModType.getMode()) {

					// Traffic Mode Type mismatch. Send Error.
					// TODO should send error or drop message?
					this.sendError(ErrorCode.Unsupported_Traffic_Mode_Type, appServer.getRoutingContext());
					continue;
				}

				// message doesn't have Traffic Mode Type

			} else {

				// AppServer Traffic Mode Type is optionally configured via
				// management config. If not select the first available in
				// AspUp message

				if (trfModType == null) {
					// Asp UP didn't specify the Traffic Mode either. use
					// default which is loadshare
					appServer.setDefaultTrafficModeType();
				} else {
					// Set the Traffic Mode Type passed in ASP ACTIVE
					appServer.setTrafficModeType(trfModType);
				}
			}

			ASPActiveAck aspActAck = (ASPActiveAck) this.m3UAProvider.getMessageFactory().createMessage(
					MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK);
			aspActAck.setTrafficModeType(appServer.getTrafficModeType());
			aspActAck.setRoutingContext(appServer.getRoutingContext());

			this.write(aspActAck);

			try {
				remAsp.getFSM().setAttribute(FSM.ATTRIBUTE_MESSAGE, aspActive);
				remAsp.getFSM().signal(TransitionState.ASP_ACTIVE);

				// Signal AS to transition
				appServer.aspStateChange(remAsp, TransitionState.ASP_ACTIVE);
			} catch (UnknownTransitionException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private RemAspImpl getAsp(long rc) {
		for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();
			if (asp.getAs().getRoutingContext().getRoutingContexts()[0] == rc) {
				return (RemAspImpl) asp;
			}
		}
		return null;
	}

	private void sendError(int iErrCode, RoutingContext rc) {
		// Send Error
		org.mobicents.protocols.ss7.m3ua.message.mgmt.Error error = (org.mobicents.protocols.ss7.m3ua.message.mgmt.Error) this.m3UAProvider
				.getMessageFactory().createMessage(MessageClass.MANAGEMENT, MessageType.ERROR);

		ErrorCode errCode = this.m3UAProvider.getParameterFactory().createErrorCode(iErrCode);
		error.setErrorCode(errCode);
		error.setRoutingContext(rc);
		this.write(error);
	}

	@Override
	public void start() {
		this.started = true;
	}

	@Override
	public void stop() {
		this.started = false;
	}

	public void onCommStateChange(CommunicationState state) {
		switch (state) {
		case UP:
			signalAspFsm(TransitionState.COMM_UP);
			break;
		case SHUTDOWN:
			signalAspFsm(TransitionState.COMM_DOWN);
			break;
		case LOST:
			signalAspFsm(TransitionState.COMM_DOWN);
			break;
		}
	}

	private void signalAspFsm(String asptransition) {
		for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();
			try {
				asp.getFSM().signal(asptransition);
			} catch (UnknownTransitionException e) {
				// This should never happen
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<RemAspFactory> REM_ASP_FACTORY_XML = new XMLFormat<RemAspFactory>(
			RemAspFactory.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, RemAspFactory remAspFactory)
				throws XMLStreamException {
			ASP_FACTORY_XML.read(xml, remAspFactory);
		}

		@Override
		public void write(RemAspFactory remAspFactory, javolution.xml.XMLFormat.OutputElement xml)
				throws XMLStreamException {
			ASP_FACTORY_XML.write(remAspFactory, xml);
		}
	};

}
