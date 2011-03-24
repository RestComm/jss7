/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

import java.io.IOException;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.TransitionState;
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
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPInactiveAck;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * 
 * @author amit bhayani
 * 
 */
public class LocalAspFactory extends AspFactory {
	private static final Logger logger = Logger.getLogger(LocalAspFactory.class);

	private static long ASP_ID = 1l;

	private String remIp;
	private int remPort;

	private final ASPIdentifier aspid;

	public LocalAspFactory(String name, String localIp, int localPort, String remIp, int remPort, M3UAProvider provider) {
		super(name, localIp, localPort, provider);
		this.remIp = remIp;
		this.remPort = remPort;
		this.aspid = this.m3UAProvider.getParameterFactory().createASPIdentifier(this.generateId());
	}

	public String getRemIp() {
		return remIp;
	}

	public int getRemPort() {
		return remPort;
	}

	@Override
	public Asp createAsp() {
		AspImpl remAsp = new AspImpl(this.name, this.m3UAProvider, this);
		remAsp.setASPIdentifier(aspid);
		this.aspList.add(remAsp);
		return remAsp;
	}

	@Override
	public void start() {
		this.started = true;
	}

	@Override
	public void stop() {
		this.started = false;
		ASPDown aspDown = (ASPDown) this.m3UAProvider.getMessageFactory().createMessage(
				MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN);
		this.write(aspDown);
		for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();

			try {
				asp.getFSM().signal(TransitionState.ASP_DOWN_SENT);
				As as = asp.getAs();
				as.aspStateChange(asp, TransitionState.ASP_DOWN);
			} catch (UnknownTransitionException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public void read(M3UAMessage message) {
		switch (message.getMessageClass()) {
		case MessageClass.MANAGEMENT:
			switch (message.getMessageType()) {
			case MessageType.ERROR:
				break;
			case MessageType.NOTIFY:
				Notify notify = (Notify) message;
				this.handleNotify(notify);
				break;
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
			case MessageType.ASP_UP_ACK:
				ASPUpAck aspUpAck = (ASPUpAck) message;
				this.handleAspUpAck(aspUpAck);
				break;
			case MessageType.ASP_DOWN_ACK:
				ASPDownAck aspDownAck = (ASPDownAck) message;
				this.handleAspDownAck(aspDownAck);
				break;
			case MessageType.HEARTBEAT:
				break;
			default:
				break;
			}

			break;

		case MessageClass.ASP_TRAFFIC_MAINTENANCE:
			switch (message.getMessageType()) {
			case MessageType.ASP_ACTIVE_ACK:
				ASPActiveAck aspAciveAck = (ASPActiveAck) message;
				this.handleAspActiveAck(aspAciveAck);
				break;
			case MessageType.ASP_INACTIVE_ACK:
				ASPInactiveAck aspInaciveAck = (ASPInactiveAck) message;
				this.handleAspInactiveAck(aspInaciveAck);
				break;
			}
			break;

		case MessageClass.ROUTING_KEY_MANAGEMENT:
			logger.error(String.format("Received %s. Handling of RKM message is not supported", message));
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

	private void handleNotify(Notify notify) {
		if (!this.started) {
			// If management stopped this ASP, ignore Notify
			return;
		}

		long[] rcs = notify.getRoutingContext().getRoutingContexts();
		for (int count = 0; count < rcs.length; count++) {
			Asp asp = this.getAsp(rcs[count]);
			try {
				asp.getAs().aspStateChange(asp, TransitionState.getTransition(notify));
			} catch (UnknownTransitionException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void handleAspUpAck(ASPUpAck aspUpAck) {
		if (!this.started) {
			// If management stopped this ASP, ignore ASPUpAck
			return;
		}

		for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();
			boolean transToActive = this.activate(asp);

			if (!transToActive) {
				// Transition to INACTIVE
				try {
					asp.getFSM().signal(TransitionState.ASP_INACTIVE);
				} catch (UnknownTransitionException e) {
					logger.error(e.getMessage(), e);
				}
			} else {
				// Transition to ACTIVE_SENT
				try {
					asp.getFSM().signal(TransitionState.ASP_ACTIVE_SENT);
				} catch (UnknownTransitionException e) {
					logger.error(e.getMessage(), e);
				}
			}// if..else
		}// for
	}

	private void handleAspDownAck(ASPDownAck aspUpAck) {

		if (!this.started) {
			
			for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
				Asp asp = n.getValue();
				try {
					asp.getFSM().signal(TransitionState.ASP_DOWN_ACK);
				} catch (UnknownTransitionException e) {
					logger.error(e.getMessage(), e);
				}
			}
			
			// Close Channel if management stopped this
			if (channel != null) {
				try {
					channel.close();
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Closed the channel for LocalAspFactory name=%s", this.getName()));
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void handleAspActiveAck(ASPActiveAck aspActiveAck) {
		if (!this.started) {
			// If management stopped this ASP, ignore ASPActiveAck
			return;
		}

		TrafficModeType trMode = aspActiveAck.getTrafficModeType();

		long[] rcs = aspActiveAck.getRoutingContext().getRoutingContexts();
		for (int count = 0; count < rcs.length; count++) {
			Asp asp = this.getAsp(rcs[count]);
			asp.getAs().setTrafficModeType(trMode);
			try {
				asp.getFSM().signal(TransitionState.ASP_ACTIVE_ACK);
			} catch (UnknownTransitionException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void handleAspInactiveAck(ASPInactiveAck aspInactiveAck) {
		if (!this.started) {
			// If management stopped this ASP, ignore ASPInactiveAck
			return;
		}

		long[] rcs = aspInactiveAck.getRoutingContext().getRoutingContexts();
		for (int count = 0; count < rcs.length; count++) {
			Asp asp = this.getAsp(rcs[count]);
			try {
				asp.getFSM().signal(TransitionState.ASP_INACTIVE_ACK);
			} catch (UnknownTransitionException e) {
				logger.error(e.getMessage(), e);
			}
		}

	}

	private void handleCommUp() {
		ASPUp aspUp = (ASPUp) this.m3UAProvider.getMessageFactory().createMessage(MessageClass.ASP_STATE_MAINTENANCE,
				MessageType.ASP_UP);
		aspUp.setASPIdentifier(this.aspid);
		this.write(aspUp);

		for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();
			try {
				asp.getFSM().signal(TransitionState.COMM_UP);
			} catch (UnknownTransitionException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void handleCommDown() {
		for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();
			try {
				asp.getFSM().signal(TransitionState.COMM_DOWN);
				asp.getAs().aspStateChange(asp, TransitionState.ASP_DOWN);
			} catch (UnknownTransitionException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	protected boolean activate(Asp asp) {
		// If its loadshare, we want to send ASP_ACTIVE else if its
		// Override and there is already one ACTIVE, we leave this one
		// as INACTIVE

		As as = asp.getAs();

		// By default we assume Traffic Mode is Loadshare
		if (as.getTrafficModeType() == null || as.getTrafficModeType().getMode() == TrafficModeType.Loadshare) {
			// Activate this ASP
			this.sendAspActive(as);
			return true;
		} else if (as.getTrafficModeType().getMode() == TrafficModeType.Override) {

			for (FastList.Node<Asp> n = as.getAspList().head(), end = as.getAspList().tail(); (n = n.getNext()) != end;) {
				Asp asptemp = n.getValue();
				if (asptemp.getName().compareTo(asp.getName()) != 0
						&& (asptemp.getState() == AspState.ACTIVE_SENT || asptemp.getState() == AspState.ACTIVE)) {
					return false;
				}
			}// for

			this.sendAspActive(as);
			return true;

		}
		return false;
	}

	// Private Methods
	protected void sendAspActive(As as) {
		ASPActive aspActive = (ASPActive) this.m3UAProvider.getMessageFactory().createMessage(
				MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE);
		aspActive.setRoutingContext(as.getRoutingContext());
		aspActive.setTrafficModeType(as.getTrafficModeType());
		this.write(aspActive);
	}

	private Asp getAsp(long rc) {
		for (FastList.Node<Asp> n = aspList.head(), end = aspList.tail(); (n = n.getNext()) != end;) {
			Asp asp = n.getValue();
			if (asp.getAs().getRoutingContext().getRoutingContexts()[0] == rc) {
				return asp;
			}
		}
		return null;
	}

	public void onCommStateChange(CommunicationState state) {
		switch (state) {
		case UP:
			this.handleCommUp();
			break;
		case SHUTDOWN:
			this.handleCommDown();
			break;
		case LOST:
			this.handleCommDown();
			break;
		}
	}

	private long generateId() {
		ASP_ID++;
		if (ASP_ID == 4294967295l) {
			ASP_ID = 1l;
		}
		return ASP_ID;
	}
}
