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
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

import javolution.util.FastList;
import javolution.util.FastSet;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.oam.M3UAOAMMessages;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.NetworkAppearanceImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.RoutingContextImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.TrafficModeTypeImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageFactory;
import org.mobicents.protocols.ss7.m3ua.message.transfer.PayloadData;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
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
	private static final String NETWORK_APPEARANCE = "networkAppearance";
	private static final String TRAFFIC_MODE = "trafficMode";
	private static final String DEFAULT_TRAFFIC_MODE = "defTrafficMode";
	private static final String ASP_LIST = "asps";
	private static final String MIN_ASP_ACT_LB = "minAspActiveForLb";

	public static final String ATTRIBUTE_ASP = "asp";

	protected int minAspActiveForLb = 1;

	// List of all the ASP's for this AS
	protected FastList<Asp> appServerProcs = new FastList<Asp>();

	// List of As state listeners
	private FastSet<AsStateListener> asStateListeners = new FastSet<AsStateListener>();

	protected String name;
	protected RoutingContext rc;
	protected TrafficModeType trMode;

	protected TrafficModeType defaultTrafModType;

	protected ConcurrentLinkedQueue<PayloadData> penQueue = new ConcurrentLinkedQueue<PayloadData>();

	/**
	 * Peer FSM maintains state such that it receives the NTFY from other side
	 */
	private FSM peerFSM;

	/**
	 * Local FSM maintains state such that it sends the NTFY to other side
	 */
	protected FSM localFSM;

	protected ParameterFactory parameterFactory = new ParameterFactoryImpl();

	protected MessageFactory messageFactory = new MessageFactoryImpl();

	protected M3UAManagement m3UAManagement = null;

	private Functionality functionality = null;
	private ExchangeType exchangeType = null;
	private IPSPType ipspType = null;
	private NetworkAppearance networkAppearance = null;

	public As() {

	}

	public As(String name, RoutingContext rc, TrafficModeType trMode, Functionality functionality,
			ExchangeType exchangeType, IPSPType ipspType, NetworkAppearance networkAppearance) {
		this.name = name;
		this.rc = rc;
		this.trMode = trMode;
		this.functionality = functionality;
		this.exchangeType = exchangeType;
		this.ipspType = ipspType;
		this.defaultTrafModType = this.parameterFactory.createTrafficModeType(TrafficModeType.Loadshare);
		this.networkAppearance = networkAppearance;
		init();
	}

	public void init() {

		switch (this.functionality) {
		case IPSP:
			if (this.exchangeType == ExchangeType.SE) {
				if (this.ipspType == IPSPType.CLIENT) {
					// If this AS is IPSP and client side, it should wait for
					// NTFY from other side
					this.initPeerFSM();
				} else {
					// else this will send NTFY to other side
					this.initLocalFSM();
				}
			} else {
				// If this is IPSP and DE, it should maintain two states. One
				// for sending NTFY to other side and other for receiving NTFY
				// form other side
				this.initPeerFSM();
				this.initLocalFSM();
			}
			break;
		case AS:
			if (this.exchangeType == ExchangeType.SE) {
				// If this is AS side, it should always receive NTFY from other
				// side
				this.initPeerFSM();
			} else {
				// If DE, it should maintain two states.
				this.initPeerFSM();
				this.initLocalFSM();
			}
			break;
		case SGW:
			if (this.exchangeType == ExchangeType.SE) {
				// If this is SGW, it should always send the NTFY to other side
				this.initLocalFSM();
			} else {
				// If DE, it should maintain two states.
				this.initPeerFSM();
				this.initLocalFSM();
			}
			break;
		}
	}

	/**
	 * Initialize FSM for AS side
	 **/
	private void initPeerFSM() {
		this.peerFSM = new FSM(this.name + "_PEER");
		// Define states
		this.peerFSM.createState(AsState.DOWN.toString());
		this.peerFSM.createState(AsState.ACTIVE.toString());
		this.peerFSM.createState(AsState.INACTIVE.toString());
		this.peerFSM.createState(AsState.PENDING.toString())
				.setOnTimeOut(new AsStatePenTimeout(this, this.peerFSM), 2000)
				.setOnEnter(new AsStateEnterPen(this, this.peerFSM));

		this.peerFSM.setStart(AsState.DOWN.toString());
		this.peerFSM.setEnd(AsState.DOWN.toString());
		// Define Transitions

		// ******************************************************************/
		// STATE DOWN /
		// ******************************************************************/
		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.DOWN.toString(),
				AsState.INACTIVE.toString());
		this.peerFSM.createTransition(TransitionState.ASP_DOWN, AsState.DOWN.toString(), AsState.DOWN.toString());

		// ******************************************************************/
		// STATE INACTIVE /
		// ******************************************************************/
		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.INACTIVE.toString(),
				AsState.INACTIVE.toString());

		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.INACTIVE.toString(),
				AsState.ACTIVE.toString()).setHandler(new THPeerAsInActToAct(this, this.peerFSM));

		this.peerFSM.createTransition(TransitionState.ASP_DOWN, AsState.INACTIVE.toString(), AsState.DOWN.toString())
				.setHandler(new THPeerAsInActToDwn(this, this.peerFSM));

		// ******************************************************************/
		// STATE ACTIVE /
		// ******************************************************************/
		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.ACTIVE.toString(),
				AsState.ACTIVE.toString());

		this.peerFSM.createTransition(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE, AsState.ACTIVE.toString(),
				AsState.ACTIVE.toString()).setHandler(new THPeerAsActToActNtfyAltAspAct(this, this.peerFSM));

		this.peerFSM.createTransition(TransitionState.OTHER_INSUFFICIENT_ASP, AsState.ACTIVE.toString(),
				AsState.ACTIVE.toString()).setHandler(new THPeerAsActToActNtfyInsAsp(this, this.peerFSM));

		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_PENDING, AsState.ACTIVE.toString(),
				AsState.PENDING.toString());

		this.peerFSM.createTransition(TransitionState.ASP_DOWN, AsState.ACTIVE.toString(), AsState.PENDING.toString())
				.setHandler(new THPeerAsActToPen(this, this.peerFSM));

		// ******************************************************************/
		// STATE PENDING /
		// ******************************************************************/
		// As transitions to DOWN from PENDING when Pending Timer timesout
		this.peerFSM.createTransition(TransitionState.AS_DOWN, AsState.PENDING.toString(), AsState.DOWN.toString());

		// As transitions to INACTIVE from PENDING when Pending Timer
		// timesout
		this.peerFSM.createTransition(TransitionState.AS_INACTIVE, AsState.PENDING.toString(),
				AsState.INACTIVE.toString());

		// If in PENDING and one of the ASP is ACTIVE again
		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.PENDING.toString(),
				AsState.ACTIVE.toString()).setHandler(new THPeerAsPendToAct(this, this.peerFSM));

		// If As is PENDING and far end sends INACTIVE we still remain
		// PENDING as that message from pending queue can be sent once As
		// becomes ACTIVE before T(r) expires
		this.peerFSM.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.PENDING.toString(),
				AsState.INACTIVE.toString()).setHandler(new THNoTrans());

		this.peerFSM.createTransition(TransitionState.ASP_DOWN, AsState.PENDING.toString(), AsState.PENDING.toString())
				.setHandler(new THNoTrans());
	}

	/**
	 * Initialize FSM for SGW side
	 **/
	private void initLocalFSM() {
		this.localFSM = new FSM(this.name + "_LOCAL");

		// Define states
		this.localFSM.createState(AsState.DOWN.toString());
		this.localFSM.createState(AsState.ACTIVE.toString());
		this.localFSM.createState(AsState.INACTIVE.toString());
		this.localFSM.createState(AsState.PENDING.toString()).setOnTimeOut(
				new RemAsStatePenTimeout(this, this.localFSM), 2000);

		this.localFSM.setStart(AsState.DOWN.toString());
		this.localFSM.setEnd(AsState.DOWN.toString());
		// Define Transitions

		// ******************************************************************/
		// STATE DOWN /
		// ******************************************************************/
		this.localFSM.createTransition(TransitionState.ASP_UP, AsState.DOWN.toString(), AsState.INACTIVE.toString())
				.setHandler(new THLocalAsDwnToInact(this, this.localFSM));
		this.localFSM.createTransition(TransitionState.ASP_DOWN, AsState.DOWN.toString(), AsState.DOWN.toString());

		// ******************************************************************/
		// STATE INACTIVE /
		// ******************************************************************/
		// TODO : Add Pluggable policy for AS?
		this.localFSM.createTransition(TransitionState.ASP_ACTIVE, AsState.INACTIVE.toString(),
				AsState.ACTIVE.toString()).setHandler(new THLocalAsInactToAct(this, this.localFSM));

		this.localFSM.createTransition(TransitionState.ASP_DOWN, AsState.INACTIVE.toString(), AsState.DOWN.toString())
				.setHandler(new THLocalAsInactToDwn(this, this.localFSM));

		this.localFSM
				.createTransition(TransitionState.ASP_UP, AsState.INACTIVE.toString(), AsState.INACTIVE.toString())
				.setHandler(new THLocalAsInactToInact(this, this.localFSM));

		// ******************************************************************/
		// STATE ACTIVE /
		// ******************************************************************/
		this.localFSM.createTransition(TransitionState.ASP_INACTIVE, AsState.ACTIVE.toString(),
				AsState.PENDING.toString()).setHandler(new THLocalAsActToPendRemAspInac(this, this.localFSM));

		this.localFSM.createTransition(TransitionState.ASP_DOWN, AsState.ACTIVE.toString(), AsState.PENDING.toString())
				.setHandler(new THLocalAsActToPendRemAspDwn(this, this.localFSM));

		this.localFSM
				.createTransition(TransitionState.ASP_ACTIVE, AsState.ACTIVE.toString(), AsState.ACTIVE.toString())
				.setHandler(new THLocalAsActToActRemAspAct(this, this.localFSM));

		this.localFSM.createTransition(TransitionState.ASP_UP, AsState.ACTIVE.toString(), AsState.PENDING.toString())
				.setHandler(new THLocalAsActToPendRemAspInac(this, this.localFSM));

		// ******************************************************************/
		// STATE PENDING /
		// ******************************************************************/
		this.localFSM.createTransition(TransitionState.ASP_DOWN, AsState.PENDING.toString(), AsState.DOWN.toString())
				.setHandler(new THNoTrans());

		this.localFSM.createTransition(TransitionState.ASP_UP, AsState.PENDING.toString(), AsState.INACTIVE.toString())
				.setHandler(new THNoTrans());

		this.localFSM.createTransition(TransitionState.ASP_ACTIVE, AsState.PENDING.toString(),
				AsState.ACTIVE.toString()).setHandler(new THLocalAsPendToAct(this, this.localFSM));

		this.localFSM.createTransition(TransitionState.AS_DOWN, AsState.PENDING.toString(), AsState.DOWN.toString());
		this.localFSM.createTransition(TransitionState.AS_INACTIVE, AsState.PENDING.toString(),
				AsState.INACTIVE.toString());
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
		 return AsState.getState(this.peerFSM.getState().getName());
	}

	public FSM getPeerFSM() {
		return peerFSM;
	}

	public FSM getLocalFSM() {
		return localFSM;
	}

	/**
	 * Get the {@link RoutingContext} for this As
	 * 
	 * @return
	 */
	public RoutingContext getRoutingContext() {
		return this.rc;
	}

	public Functionality getFunctionality() {
		return functionality;
	}

	public ExchangeType getExchangeType() {
		return exchangeType;
	}

	public IPSPType getIpspType() {
		return ipspType;
	}

	public NetworkAppearance getNetworkAppearance() {
		return networkAppearance;
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

	public TrafficModeType getDefaultTrafficModeType() {
		return this.defaultTrafModType;
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
	 * Add new {@link Asp} for this As.
	 * 
	 * @param asp
	 * @throws Exception
	 *             throws exception if the Asp with same name already exist
	 */
	protected void addAppServerProcess(Asp asp) throws Exception {
		asp.setAs(this);
		appServerProcs.add(asp);
	}

	protected Asp removeAppServerProcess(String aspName) throws Exception {
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

		FSM aspLocalFSM = asp.getLocalFSM();
		if (aspLocalFSM != null) {
			AspState aspLocalState = AspState.getState(aspLocalFSM.getState().getName());

			if (aspLocalState != AspState.DOWN) {
				throw new Exception(String.format(
						"ASP=%s local FSM is still %s. Bring it DOWN before removing from this As", aspName,
						aspLocalState));
			}
		}

		FSM aspPeerFSM = asp.getPeerFSM();
		if (aspPeerFSM != null) {
			AspState aspPeerState = AspState.getState(aspPeerFSM.getState().getName());

			if (aspPeerState != AspState.DOWN) {
				throw new Exception(String.format(
						"ASP=%s peer FSM is still %s. Bring it DOWN before removing from this As", aspName,
						aspPeerState));
			}
		}

		this.appServerProcs.remove(asp);
		asp.setAs(null);

		if (aspLocalFSM != null) {
			aspLocalFSM.cancel();
		}

		if (aspPeerFSM != null) {
			aspPeerFSM.cancel();
		}
		return asp;
	}

	/**
	 * write the {@link PayloadData} to underlying {@link Asp}. If the state of
	 * As is PENDING, the PayloadData is stored in pending queue.
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void write(PayloadData message) throws IOException {

		FSM fsm = null;
		boolean isASPLocalFsm = true;

		if (this.functionality == Functionality.AS
				|| (this.functionality == Functionality.SGW && this.exchangeType == ExchangeType.DE)
				|| (this.functionality == Functionality.IPSP && this.ipspType == IPSPType.CLIENT)
				|| (this.functionality == Functionality.IPSP && this.ipspType == IPSPType.SERVER && this.exchangeType == ExchangeType.DE)) {
			fsm = this.peerFSM;
		} else {
			fsm = this.localFSM;
			isASPLocalFsm = false;
		}

		switch (AsState.getState(fsm.getState().getName())) {
		case ACTIVE:
			boolean aspFound = false;
			// TODO : Algo to select correct ASP
			for (FastList.Node<Asp> n = this.appServerProcs.head(), end = this.appServerProcs.tail(); (n = n.getNext()) != end;) {
				Asp aspTemp = n.getValue();
				FSM aspFsm = null;

				if (isASPLocalFsm) {
					aspFsm = aspTemp.getLocalFSM();
				} else {
					aspFsm = aspTemp.getPeerFSM();
				}

				if (AspState.getState(aspFsm.getState().getName()) == AspState.ACTIVE) {
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

			as.functionality = Functionality.getFunctionality(xml.getAttribute("functionality", ""));
			as.exchangeType = ExchangeType.getExchangeType(xml.getAttribute("exchangeType", ""));
			as.ipspType = IPSPType.getIPSPType(xml.getAttribute("ipspType", ""));

			as.rc = xml.get(ROUTING_CONTEXT, RoutingContextImpl.class);
			as.networkAppearance = xml.get(NETWORK_APPEARANCE, NetworkAppearanceImpl.class);
			as.trMode = xml.get(TRAFFIC_MODE, TrafficModeTypeImpl.class);
			as.defaultTrafModType = xml.get(DEFAULT_TRAFFIC_MODE, TrafficModeTypeImpl.class);
			as.appServerProcs = xml.get(ASP_LIST, FastList.class);
			as.init();
		}

		@Override
		public void write(As as, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
			xml.setAttribute(NAME, as.name);
			xml.setAttribute(MIN_ASP_ACT_LB, as.minAspActiveForLb);
			xml.setAttribute("functionality", as.functionality.getType());
			xml.setAttribute("exchangeType", as.exchangeType.getType());
			if (as.ipspType != null) {
				xml.setAttribute("ipspType", as.ipspType.getType());
			}

			xml.add((RoutingContextImpl) as.rc, ROUTING_CONTEXT, RoutingContextImpl.class);
			xml.add((NetworkAppearanceImpl) as.networkAppearance, NETWORK_APPEARANCE, NetworkAppearanceImpl.class);
			xml.add((TrafficModeTypeImpl) as.trMode, TRAFFIC_MODE, TrafficModeTypeImpl.class);
			xml.add((TrafficModeTypeImpl) as.defaultTrafModType, DEFAULT_TRAFFIC_MODE, TrafficModeTypeImpl.class);
			xml.add(as.appServerProcs, ASP_LIST, FastList.class);

		}
	};

	public void show(StringBuffer sb) {
		sb.append(M3UAOAMMessages.SHOW_AS_NAME).append(this.name).append(M3UAOAMMessages.SHOW_FUNCTIONALITY)
				.append(this.functionality).append(M3UAOAMMessages.SHOW_MODE).append(this.exchangeType);

		if (this.functionality == Functionality.IPSP) {
			sb.append(M3UAOAMMessages.SHOW_IPSP_TYPE).append(this.ipspType);
		}

		if (this.rc != null) {
			sb.append(" rc=").append(Arrays.toString(this.rc.getRoutingContexts()));
		}

		if (this.trMode != null) {
			sb.append(" trMode=").append(this.trMode.getMode());
		}

		sb.append(" defaultTrMode=").append(this.defaultTrafModType.getMode());

		if (this.networkAppearance != null) {
			sb.append(" na=").append(this.networkAppearance.getNetApp());
		}

		if (this.getLocalFSM() != null) {
			sb.append(M3UAOAMMessages.SHOW_LOCAL_FSM_STATE).append(this.getLocalFSM().getState());
		}

		if (this.getPeerFSM() != null) {
			sb.append(M3UAOAMMessages.SHOW_PEER_FSM_STATE).append(this.getPeerFSM().getState());
		}

		sb.append(M3UAOAMMessages.NEW_LINE);
		sb.append(M3UAOAMMessages.SHOW_ASSIGNED_TO);

		for (FastList.Node<Asp> n = this.appServerProcs.head(), end = this.appServerProcs.tail(); (n = n.getNext()) != end;) {
			Asp aspTemp = n.getValue();
			AspFactory aspFactory = aspTemp.getAspFactory();
			sb.append(M3UAOAMMessages.TAB).append(M3UAOAMMessages.SHOW_ASP_NAME).append(aspFactory.getName())
					.append(M3UAOAMMessages.SHOW_STARTED).append(aspFactory.getStatus());
			sb.append(M3UAOAMMessages.NEW_LINE);
		}
	}

	/**
	 * Add the {@link AsStateListener} listening for As state
	 * 
	 * @param listener
	 */
	protected void addAsStateListener(AsStateListener listener) {
		this.asStateListeners.add(listener);
	}

	/**
	 * Remove already added {@link AsStateListener}
	 * 
	 * @param listener
	 */
	protected void removeAsStateListener(AsStateListener listener) {
		this.asStateListeners.remove(listener);
	}

	public FastSet<AsStateListener> getAsStateListeners() {
		return asStateListeners;
	}
}
