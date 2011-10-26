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
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.impl.as.AsNoTrans;
import org.mobicents.protocols.ss7.m3ua.impl.as.AsStateEnterPen;
import org.mobicents.protocols.ss7.m3ua.impl.as.AsStatePenTimeout;
import org.mobicents.protocols.ss7.m3ua.impl.as.AsTransActToActNtfyAltAspAct;
import org.mobicents.protocols.ss7.m3ua.impl.as.AsTransActToActNtfyInsAsp;
import org.mobicents.protocols.ss7.m3ua.impl.as.AsTransActToPen;
import org.mobicents.protocols.ss7.m3ua.impl.as.AsTransInActToDwn;
import org.mobicents.protocols.ss7.m3ua.impl.as.AsTransPendToAct;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.sg.RemAsNoTrans;
import org.mobicents.protocols.ss7.m3ua.impl.sg.RemAsStatePenTimeout;
import org.mobicents.protocols.ss7.m3ua.impl.sg.RemAsTransActToActRemAspAct;
import org.mobicents.protocols.ss7.m3ua.impl.sg.RemAsTransActToPendRemAspDwn;
import org.mobicents.protocols.ss7.m3ua.impl.sg.RemAsTransActToPendRemAspInac;
import org.mobicents.protocols.ss7.m3ua.impl.sg.RemAsTransDwnToInact;
import org.mobicents.protocols.ss7.m3ua.impl.sg.RemAsTransInactToAct;
import org.mobicents.protocols.ss7.m3ua.impl.sg.RemAsTransInactToDwn;
import org.mobicents.protocols.ss7.m3ua.impl.sg.RemAsTransInactToInact;
import org.mobicents.protocols.ss7.m3ua.impl.sg.RemAsTransPendToAct;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * 
 * @author amit bhayani
 * 
 */
public class As implements XMLSerializable {

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

	protected TrafficModeType defaultTrafModType;

	protected ConcurrentLinkedQueue<PayloadData> penQueue = new ConcurrentLinkedQueue<PayloadData>();

	protected FSM fsm;

	protected ParameterFactory parameterFactory = new ParameterFactoryImpl();

	protected MessageFactory messageFactory = new MessageFactoryImpl();

	protected M3UAManagement m3UAManagement = null;

	private Functionality functionality = null;

	public As() {

	}

	public As(String name, RoutingContext rc, RoutingKey rk, TrafficModeType trMode, Functionality functionality) {
		this.name = name;
		this.rc = rc;
		this.rk = rk;
		this.trMode = trMode;
		this.functionality = functionality;
		this.defaultTrafModType = this.parameterFactory.createTrafficModeType(TrafficModeType.Loadshare);
		init();
	}

	public void init() {
		this.fsm = new FSM(this.name);

		if (this.functionality == Functionality.IPSP) {
			// Define states
			fsm.createState(AsState.DOWN.toString());
			fsm.createState(AsState.ACTIVE.toString());
			fsm.createState(AsState.INACTIVE.toString());
			fsm.createState(AsState.PENDING.toString()).setOnTimeOut(new AsStatePenTimeout(this, this.fsm), 2000)
					.setOnEnter(new AsStateEnterPen(this, this.fsm));

			fsm.setStart(AsState.DOWN.toString());
			fsm.setEnd(AsState.DOWN.toString());
			// Define Transitions

			// ******************************************************************/
			// STATE DOWN /
			// ******************************************************************/
			fsm.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.DOWN.toString(),
					AsState.INACTIVE.toString());
			fsm.createTransition(TransitionState.ASP_DOWN, AsState.DOWN.toString(), AsState.DOWN.toString());

			// ******************************************************************/
			// STATE INACTIVE /
			// ******************************************************************/
			fsm.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.INACTIVE.toString(),
					AsState.INACTIVE.toString());

			fsm.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.INACTIVE.toString(),
					AsState.ACTIVE.toString());

			fsm.createTransition(TransitionState.ASP_DOWN, AsState.INACTIVE.toString(), AsState.DOWN.toString())
					.setHandler(new AsTransInActToDwn(this, this.fsm));

			// ******************************************************************/
			// STATE ACTIVE /
			// ******************************************************************/
			fsm.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.ACTIVE.toString(),
					AsState.ACTIVE.toString());

			fsm.createTransition(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE, AsState.ACTIVE.toString(),
					AsState.ACTIVE.toString()).setHandler(new AsTransActToActNtfyAltAspAct(this, this.fsm));

			fsm.createTransition(TransitionState.OTHER_INSUFFICIENT_ASP, AsState.ACTIVE.toString(),
					AsState.ACTIVE.toString()).setHandler(new AsTransActToActNtfyInsAsp(this, this.fsm));

			fsm.createTransition(TransitionState.AS_STATE_CHANGE_PENDING, AsState.ACTIVE.toString(),
					AsState.PENDING.toString());

			fsm.createTransition(TransitionState.ASP_DOWN, AsState.ACTIVE.toString(), AsState.PENDING.toString())
					.setHandler(new AsTransActToPen(this, this.fsm));

			// ******************************************************************/
			// STATE PENDING /
			// ******************************************************************/
			// As transitions to DOWN from PENDING when Pending Timer timesout
			fsm.createTransition(TransitionState.AS_DOWN, AsState.PENDING.toString(), AsState.DOWN.toString());

			// As transitions to INACTIVE from PENDING when Pending Timer
			// timesout
			fsm.createTransition(TransitionState.AS_INACTIVE, AsState.PENDING.toString(), AsState.INACTIVE.toString());

			// If in PENDING and one of the ASP is ACTIVE again
			fsm.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.PENDING.toString(),
					AsState.ACTIVE.toString()).setHandler(new AsTransPendToAct(this, this.fsm));

			// If As is PENDING and far end sends INACTIVE we still remain
			// PENDING as that message from pending queue can be sent once As
			// becomes ACTIVE before T(r) expires
			fsm.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.PENDING.toString(),
					AsState.INACTIVE.toString()).setHandler(new AsNoTrans());

			fsm.createTransition(TransitionState.ASP_DOWN, AsState.PENDING.toString(), AsState.PENDING.toString())
					.setHandler(new AsNoTrans());
		} else {
			// Define states
			fsm.createState(AsState.DOWN.toString());
			fsm.createState(AsState.ACTIVE.toString());
			fsm.createState(AsState.INACTIVE.toString());
			fsm.createState(AsState.PENDING.toString()).setOnTimeOut(new RemAsStatePenTimeout(this, this.fsm), 2000);

			fsm.setStart(AsState.DOWN.toString());
			fsm.setEnd(AsState.DOWN.toString());
			// Define Transitions

			// ******************************************************************/
			// STATE DOWN /
			// ******************************************************************/
			fsm.createTransition(TransitionState.ASP_UP, AsState.DOWN.toString(), AsState.INACTIVE.toString())
					.setHandler(new RemAsTransDwnToInact(this, this.fsm));

			// ******************************************************************/
			// STATE INACTIVE /
			// ******************************************************************/
			// TODO : Add Pluggable policy for AS?
			fsm.createTransition(TransitionState.ASP_ACTIVE, AsState.INACTIVE.toString(), AsState.ACTIVE.toString())
					.setHandler(new RemAsTransInactToAct(this, this.fsm));

			fsm.createTransition(TransitionState.ASP_DOWN, AsState.INACTIVE.toString(), AsState.DOWN.toString())
					.setHandler(new RemAsTransInactToDwn(this, this.fsm));

			fsm.createTransition(TransitionState.ASP_UP, AsState.INACTIVE.toString(), AsState.INACTIVE.toString())
					.setHandler(new RemAsTransInactToInact(this, this.fsm));

			// ******************************************************************/
			// STATE ACTIVE /
			// ******************************************************************/
			fsm.createTransition(TransitionState.ASP_INACTIVE, AsState.ACTIVE.toString(), AsState.PENDING.toString())
					.setHandler(new RemAsTransActToPendRemAspInac(this, this.fsm));

			fsm.createTransition(TransitionState.ASP_DOWN, AsState.ACTIVE.toString(), AsState.PENDING.toString())
					.setHandler(new RemAsTransActToPendRemAspDwn(this, this.fsm));

			fsm.createTransition(TransitionState.ASP_ACTIVE, AsState.ACTIVE.toString(), AsState.ACTIVE.toString())
					.setHandler(new RemAsTransActToActRemAspAct(this, this.fsm));

			fsm.createTransition(TransitionState.ASP_UP, AsState.ACTIVE.toString(), AsState.PENDING.toString())
					.setHandler(new RemAsTransActToPendRemAspInac(this, this.fsm));

			// ******************************************************************/
			// STATE PENDING /
			// ******************************************************************/
			fsm.createTransition(TransitionState.ASP_DOWN, AsState.PENDING.toString(), AsState.DOWN.toString())
					.setHandler(new RemAsNoTrans());

			fsm.createTransition(TransitionState.ASP_UP, AsState.PENDING.toString(), AsState.INACTIVE.toString())
					.setHandler(new RemAsNoTrans());

			fsm.createTransition(TransitionState.ASP_ACTIVE, AsState.PENDING.toString(), AsState.ACTIVE.toString())
					.setHandler(new RemAsTransPendToAct(this, this.fsm));

			fsm.createTransition(TransitionState.AS_DOWN, AsState.PENDING.toString(), AsState.DOWN.toString());
			fsm.createTransition(TransitionState.AS_INACTIVE, AsState.PENDING.toString(), AsState.INACTIVE.toString());
		}
	}

	/**
	 * Every As has unique name
	 * 
	 * @return String name of this As
	 */
	public String getName() {
		return this.name;
	}

	public void setM3UAManagement(M3UAManagement m3uaManagement) {
		m3UAManagement = m3uaManagement;
	}

	public M3UAManagement getM3UAManagement() {
		return m3UAManagement;
	}

	public MessageFactory getMessageFactory() {
		return messageFactory;
	}

	public ParameterFactory getParameterFactory() {
		return parameterFactory;
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

	public Asp removeAppServerProcess(String aspName) throws Exception {
		Asp asp = null;
		for (FastList.Node<Asp> n = this.appServerProcs.head(), end = this.appServerProcs.tail(); (n = n.getNext()) != end;) {
			Asp aspTemp = n.getValue();
			if (aspTemp.getName().equals(aspName)) {
				asp = aspTemp;
				break;
			}
		}

		if (asp == null) {
			throw new Exception(String.format("No ASP found for name=%s", aspName));
		}

		if (asp.getState() != AspState.DOWN) {
			throw new Exception(String.format("ASP=%s is still %s. Bring it DOWN before removing from this As",
					aspName, asp.getState()));
		}

		this.appServerProcs.remove(asp);
		asp.setAs(null);
		asp.getFSM().cancel();
		return asp;
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
			boolean aspFound = false;
			// TODO : Algo to select correct ASP
			for (FastList.Node<Asp> n = this.appServerProcs.head(), end = this.appServerProcs.tail(); (n = n.getNext()) != end;) {
				Asp aspTemp = n.getValue();
				if (aspTemp.getState() == AspState.ACTIVE) {
					aspTemp.getAspFactory().write(message);
					aspFound = true;
					break;
				}
			}

			if (!aspFound) {
				// This should never happen.
				logger.error(String.format("Tx : no ACTIVE Asp for message=%s", message));
			}

			break;
		case PENDING:
			if (logger.isInfoEnabled()) {
				logger.info(String.format("Adding the PayloadData=%s to PendingQueue for AS=%s", message.toString(),
						this.name));
			}
			this.penQueue.add(message);
			break;
		default:
			throw new IOException(String.format("As name=%s is not ACTIVE", this.name));
		}
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
