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

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javolution.util.FastList;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.ProtocolData;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.mtp.Mtp3Primitive;

/**
 * 
 * @author amit bhayani
 * 
 */
public abstract class As implements XMLSerializable {

	private static final Logger logger = Logger.getLogger(As.class);

	private static final String NAME = "name";
	private static final String ROUTING_CONTEXT = "routingContext";
	private static final String ROUTINIG_KEY = "routingKey";
	private static final String TRAFFIC_MODE = "trafficMode";
	private static final String DEFAULT_TRAFFIC_MODE = "defTrafficMode";
	private static final String ASP_LIST = "asps";
	private static final String MIN_ASP_ACT_LB = "minAspActiveForLb";

	public static final String ATTRIBUTE_ASP = "asp";

	protected int minAspActiveForLb = 1;

	// List of all the ASP's for this AS
	protected FastList<Asp> appServerProcs = new FastList<Asp>();

	protected String name;
	protected RoutingContext rc;
	private RoutingKey rk;
	protected TrafficModeType trMode;
	protected M3UAProvider m3UAProvider = null;

	protected TrafficModeType defaultTrafModType;

	protected ConcurrentLinkedQueue<PayloadData> penQueue = new ConcurrentLinkedQueue<PayloadData>();

	// Queue for incoming Payload messages. Message received from peer
	protected ConcurrentLinkedQueue<byte[]> rxQueue = new ConcurrentLinkedQueue<byte[]>();

	protected FSM fsm;

	public As() {

	}

	public As(String name, RoutingContext rc, RoutingKey rk, TrafficModeType trMode, M3UAProvider provider) {
		this.name = name;
		this.rc = rc;
		this.rk = rk;
		this.trMode = trMode;
		this.m3UAProvider = provider;

		this.defaultTrafModType = this.m3UAProvider.getParameterFactory().createTrafficModeType(
				TrafficModeType.Loadshare);
		init();
	}

	public abstract void init();

	/**
	 * Every As has unique name
	 * 
	 * @return String name of this As
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the list of {@link Asp} for this As
	 * 
	 * @return
	 */
	public FastList<Asp> getAspList() {
		return this.appServerProcs;
	}

	/**
	 * Get the {@link AsState} of this As
	 * 
	 * @return
	 */
	public AsState getState() {
		return AsState.getState(this.fsm.getState().getName());
	}

	/**
	 * Get the {@link RoutingContext} for this As
	 * 
	 * @return
	 */
	public RoutingContext getRoutingContext() {
		return this.rc;
	}

	/**
	 * Get the {@link RoutingKey} configured for this As
	 * 
	 * @return
	 */
	public RoutingKey getRoutingKey() {
		return this.rk;
	}

	/**
	 * Set the {@link TrafficModeType}
	 * 
	 * @param trMode
	 */
	public void setTrafficModeType(TrafficModeType trMode) {
		// TODO : Check if TrafficModeType is not null throw error?
		this.trMode = trMode;
	}

	/**
	 * Get the {@link TrafficModeType}
	 * 
	 * @return
	 */
	public TrafficModeType getTrafficModeType() {
		return this.trMode;
	}

	/**
	 * Set default {@link TrafficModeType} which is loadshare
	 */
	public void setDefaultTrafficModeType() {
		// TODO : Check if TrafficModeType is not null throw error?
		this.trMode = this.defaultTrafModType;
	}

	/**
	 * Get {@link M3UAProvider}
	 * 
	 * @return
	 */
	public M3UAProvider getM3UAProvider() {
		return m3UAProvider;
	}

	/**
	 * 
	 * @param m3uaProvider
	 */
	public void setM3UAProvider(M3UAProvider m3uaProvider) {
		m3UAProvider = m3uaProvider;
	}

	/**
	 * If the {@link TrafficModeType} is loadshare, set the minimum number of
	 * {@link Asp} that should be
	 * {@link org.mobicents.protocols.ss7.m3ua.impl.AspState#ACTIVE} before
	 * state of this As becomes {@link AsState#ACTIVE}
	 * 
	 * @param lb
	 */
	public void setMinAspActiveForLb(int lb) {
		this.minAspActiveForLb = lb;
	}

	/**
	 * Get the minimum number of {@link Asp} that should be
	 * {@link org.mobicents.protocols.ss7.m3ua.impl.AspState#ACTIVE} before
	 * state of this As becomes {@link AsState#ACTIVE}. Used only if
	 * {@link TrafficModeType} is loadshare
	 * 
	 * @return
	 */
	public int getMinAspActiveForLb() {
		return this.minAspActiveForLb;
	}

	/**
	 * Get the {@link FSM} for this As
	 * 
	 * @return
	 */
	public FSM getFSM() {
		return this.fsm;
	}

	/**
	 * Add new {@link Asp} for this As.
	 * 
	 * @param asp
	 * @throws Exception
	 *             throws exception if the Asp with same name already exist
	 */
	public void addAppServerProcess(Asp asp) throws Exception {
		// Check if already added?
		for (FastList.Node<Asp> n = this.appServerProcs.head(), end = this.appServerProcs.tail(); (n = n.getNext()) != end;) {
			Asp aspTemp = n.getValue();
			if (aspTemp.getName().equals(asp.getName())) {
				throw new Exception(String.format("Asp name=%s already added", asp.getName()));
			}
		}

		asp.setAs(this);
		appServerProcs.add(asp);
	}

	/**
	 * The {@link Asp} state has changed causing the state change for this As
	 * too.
	 * 
	 * @param asp
	 * @param asTransition
	 * @throws UnknownTransitionException
	 */
	public void aspStateChange(Asp asp, String asTransition) throws UnknownTransitionException {
		this.fsm.setAttribute(ATTRIBUTE_ASP, asp);
		this.fsm.signal(asTransition);
	}

	/**
	 * write the {@link PayloadData} to underlying {@link Asp}. If the state of
	 * As is PENDING, the PayloadData is stored in pending queue.
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void write(PayloadData message) throws IOException {

		switch (this.getState()) {
		case ACTIVE:
			// TODO : Algo to select correct ASP
			for (FastList.Node<Asp> n = this.appServerProcs.head(), end = this.appServerProcs.tail(); (n = n.getNext()) != end;) {
				Asp aspTemp = n.getValue();
				if (aspTemp.getState() == AspState.ACTIVE) {
					aspTemp.getAspFactory().write(message);
					break;
				}
			}

			break;
		case PENDING:
			this.penQueue.add(message);
			break;
		default:
			throw new IOException(String.format("As name=%s is not ACTIVE", this.name));
		}
	}

	/**
	 * 
	 * @param msu
	 * @throws IOException
	 */
	public void write(byte[] msu) throws IOException {
		ProtocolData data = m3UAProvider.getParameterFactory().createProtocolData(0, msu);

		PayloadData payload = (PayloadData) this.m3UAProvider.getMessageFactory().createMessage(
				MessageClass.TRANSFER_MESSAGES, MessageType.PAYLOAD);
		payload.setRoutingContext(this.rc);
		payload.setData(data);
		this.write(payload);
	}

	public void received(PayloadData payload) {
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Received PayloadData=%s for As=%s", payload.toString(), this.name));
		}
		this.rxQueue.add(payload.getData().getMsu());
	}
	
	public void received (Mtp3Primitive primitive){
		this.rxQueue.add(primitive.getValue());
	}

	public byte[] poll() {
		return this.rxQueue.poll();
	}

	public void clearPendingQueue() {
		if (logger.isDebugEnabled()) {
			if (this.penQueue.size() > 0) {
				logger.debug(String.format("Cleaning %d PayloadData message from pending queue of As name=%s",
						this.penQueue.size(), this.name));
			}
		}
		this.penQueue.clear();
	}

	public void sendPendingPayloadData(Asp asp) {
		PayloadData payload = null;
		while ((payload = this.penQueue.poll()) != null) {
			asp.getAspFactory().write(payload);
		}
	}

	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<As> AS_XML = new XMLFormat<As>(As.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, As as) throws XMLStreamException {
			as.name = xml.getAttribute(NAME, "");
			as.minAspActiveForLb = xml.getAttribute(MIN_ASP_ACT_LB).toInt();
			as.rc = xml.get(ROUTING_CONTEXT);
			as.rk = xml.get(ROUTINIG_KEY);
			as.trMode = xml.get(TRAFFIC_MODE);
			as.defaultTrafModType = xml.get(DEFAULT_TRAFFIC_MODE);
			as.appServerProcs = xml.get(ASP_LIST);
			as.init();
		}

		@Override
		public void write(As as, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
			xml.setAttribute(NAME, as.name);
			xml.setAttribute(MIN_ASP_ACT_LB, as.minAspActiveForLb);
			xml.add(as.rc, ROUTING_CONTEXT);
			xml.add(as.rk, ROUTINIG_KEY);
			xml.add(as.trMode, TRAFFIC_MODE);
			xml.add(as.defaultTrafModType, DEFAULT_TRAFFIC_MODE);
			xml.add(as.appServerProcs, ASP_LIST);
		}
	};
}
