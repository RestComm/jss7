package org.mobicents.protocols.ss7.m3ua.impl;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPDown;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPDownAck;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPUp;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.ASPUpAck;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.Heartbeat;
import org.mobicents.protocols.ss7.m3ua.message.aspsm.HeartbeatAck;
import org.mobicents.protocols.ss7.m3ua.parameter.ASPIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * 
 * @author amit bhayani
 * 
 */
public class AspStateMaintenanceHandler extends MessageHandler {

	private static final Logger logger = Logger.getLogger(AspStateMaintenanceHandler.class);

	public AspStateMaintenanceHandler(AspFactory aspFactory) {
		super(aspFactory);
	}

	protected void handleAspUp(ASPUp aspUp) {

		if (aspFactory.getFunctionality() == Functionality.SGW
				|| (aspFactory.getFunctionality() == Functionality.AS && aspFactory.getExchangeType() == ExchangeType.DE)
				|| (aspFactory.getFunctionality() == Functionality.IPSP && aspFactory.getExchangeType() == ExchangeType.DE)
				|| (aspFactory.getFunctionality() == Functionality.IPSP
						&& aspFactory.getExchangeType() == ExchangeType.SE && aspFactory.getIpspType() == IPSPType.SERVER)) {

			ASPUpAck aspUpAck = (ASPUpAck) this.aspFactory.messageFactory.createMessage(
					MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_UP_ACK);

			ASPIdentifier aspId = aspUp.getASPIdentifier();
			aspUpAck.setASPIdentifier(aspId);
			this.aspFactory.write(aspUpAck);

			// If an ASP Up message is received and, internally, the remote ASP
			// is in the ASP-ACTIVE state, an ASP Up Ack message is returned, as
			// well as an Error message ("Unexpected Message"). In addition, the
			// remote ASP state is changed to ASP-INACTIVE in all relevant
			// Application Servers, and all registered Routing Keys are
			// considered deregistered.

			for (FastList.Node<Asp> n = this.aspFactory.aspList.head(), end = this.aspFactory.aspList.tail(); (n = n
					.getNext()) != end;) {
				Asp asp = n.getValue();

				// Set the ASPIdentifier for each ASP
				asp.setASPIdentifier(aspUp.getASPIdentifier());

				FSM aspPeerFSM = asp.getPeerFSM();
				if (aspPeerFSM == null) {
					logger.error(String.format("Received ASPUP=%s for ASP=%s. But peer FSM is null.", aspUp,
							this.aspFactory.getName()));
					return;
				}

				if (AspState.getState(aspPeerFSM.getState().getName()) == AspState.ACTIVE) {
					// Check if ASP is in ACTIVE, its error state

					ErrorCode errorCodeObj = this.aspFactory.parameterFactory
							.createErrorCode(ErrorCode.Unexpected_Message);

					this.sendError(asp.getAs().getRoutingContext(), errorCodeObj);
					// break;
				}

				try {
					// Signal ASP about state change
					aspPeerFSM.setAttribute(FSM.ATTRIBUTE_MESSAGE, aspUp);
					aspPeerFSM.signal(TransitionState.ASP_UP);

					// Signal corresponding AS about ASP's state transition
					FSM asLocalFSM = asp.getAs().getLocalFSM();

					asLocalFSM.setAttribute(As.ATTRIBUTE_ASP, asp);
					asLocalFSM.signal(TransitionState.ASP_UP);
				} catch (UnknownTransitionException e) {
					logger.error(e.getMessage(), e);
				}
			}
		} else {
			// TODO : Should we silently drop ASPUP_ACK?

			// ASPUP_ACK is unexpected in this state
			ErrorCode errorCodeObj = this.aspFactory.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
			sendError(null, errorCodeObj);
		}

	}

	protected void handleAspUpAck(ASPUpAck aspUpAck) {

		if (!this.aspFactory.started) {
			// If management stopped this ASP, ignore ASPUpAck
			return;
		}

		if (aspFactory.getFunctionality() == Functionality.AS
				|| (aspFactory.getFunctionality() == Functionality.SGW && aspFactory.getExchangeType() == ExchangeType.DE)
				|| (aspFactory.getFunctionality() == Functionality.IPSP && aspFactory.getExchangeType() == ExchangeType.DE)
				|| (aspFactory.getFunctionality() == Functionality.IPSP
						&& aspFactory.getExchangeType() == ExchangeType.SE && aspFactory.getIpspType() == IPSPType.CLIENT)) {

			for (FastList.Node<Asp> n = this.aspFactory.aspList.head(), end = this.aspFactory.aspList.tail(); (n = n
					.getNext()) != end;) {
				Asp asp = n.getValue();

				FSM fsm = asp.getLocalFSM();
				if (fsm == null) {
					logger.error(String.format("Received ASPUP_ACK=%s for ASP=%s. But local FSM is null.", aspUpAck,
							this.aspFactory.getName()));
					return;
				}

				boolean transToActive = this.activate(asp);

				if (!transToActive) {
					// Transition to INACTIVE
					try {
						fsm.signal(TransitionState.ASP_INACTIVE);
					} catch (UnknownTransitionException e) {
						logger.error(e.getMessage(), e);
					}
				} else {
					// Transition to ACTIVE_SENT
					try {
						fsm.signal(TransitionState.ASP_ACTIVE_SENT);
					} catch (UnknownTransitionException e) {
						logger.error(e.getMessage(), e);
					}
				}// if..else
			}// for

		} else {
			// TODO : Should we silently drop ASPUP_ACK?

			// ASPUP_ACK is unexpected in this state
			ErrorCode errorCodeObj = this.aspFactory.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
			sendError(null, errorCodeObj);
		}
	}

	protected void handleAspDown(ASPDown aspDown) {

		if (aspFactory.getFunctionality() == Functionality.SGW
				|| (aspFactory.getFunctionality() == Functionality.AS && aspFactory.getExchangeType() == ExchangeType.DE)
				|| (aspFactory.getFunctionality() == Functionality.IPSP && aspFactory.getExchangeType() == ExchangeType.DE)
				|| (aspFactory.getFunctionality() == Functionality.IPSP
						&& aspFactory.getExchangeType() == ExchangeType.SE && aspFactory.getIpspType() == IPSPType.SERVER)) {

			ASPDownAck aspDwnAck = (ASPDownAck) this.aspFactory.messageFactory.createMessage(
					MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK);
			this.aspFactory.write(aspDwnAck);

			// ASPSM should be given to all ASP's
			for (FastList.Node<Asp> n = this.aspFactory.aspList.head(), end = this.aspFactory.aspList.tail(); (n = n
					.getNext()) != end;) {
				Asp asp = n.getValue();

				FSM aspPeerFSM = asp.getPeerFSM();
				if (aspPeerFSM == null) {
					logger.error(String.format("Received ASPDOWN=%s for ASP=%s. But peer FSM is null.", aspDown,
							this.aspFactory.getName()));
					return;
				}

				try {
					// Signal ASP about state change
					aspPeerFSM.setAttribute(FSM.ATTRIBUTE_MESSAGE, aspDown);
					aspPeerFSM.signal(TransitionState.ASP_DOWN);

					// Signal corresponding AS about ASP's state transition
					FSM asLocalFSM = asp.getAs().getLocalFSM();

					asLocalFSM.setAttribute(As.ATTRIBUTE_ASP, asp);
					asLocalFSM.signal(TransitionState.ASP_DOWN);

				} catch (UnknownTransitionException e) {
					logger.error(e.getMessage(), e);
				}

			}

		} else {
			// TODO : Should we silently drop ASPUP_ACK?

			// ASPUP_ACK is unexpected in this state
			ErrorCode errorCodeObj = this.aspFactory.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
			sendError(null, errorCodeObj);
		}
	}

	protected void handleAspDownAck(ASPDownAck aspUpAck) {

		if (!this.aspFactory.started) {

			boolean stopAssociation = true;

			for (FastList.Node<Asp> n = this.aspFactory.aspList.head(), end = this.aspFactory.aspList.tail(); (n = n
					.getNext()) != end;) {
				Asp asp = n.getValue();
				FSM fsm = asp.getLocalFSM();
				if (fsm == null) {
					logger.error(String.format("Received ASPDOWN_ACK=%s for ASP=%s. But local FSM is null.", aspUpAck,
							this.aspFactory.getName()));
					continue;
				}

				FSM fsmPeer = asp.getPeerFSM();

				// If ASP's peer FSM is not null and it's state is not DOWN,
				// don't stop the underlying Association
				if (fsmPeer != null) {
					AspState aspPeerState = AspState.getState(fsmPeer.getState().getName());

					if (aspPeerState != AspState.DOWN) {
						stopAssociation = false;
					}
				}

				try {
					fsm.signal(TransitionState.ASP_DOWN_ACK);
				} catch (UnknownTransitionException e) {
					logger.error(e.getMessage(), e);
				}
			}// for

			if (!stopAssociation) {
				logger.error(String
						.format("Ungracefully stopping the underlying Association=%s for AspFactory=%s. Atleast one of the ASP's is Double Exchange and Peer ASP state is still not down",
								this.aspFactory.association.getName(), this.aspFactory.getName()));
			}

			try {
				this.aspFactory.transportManagement.stopAssociation(this.aspFactory.association.getName());
			} catch (Exception e) {
				logger.error(
						String.format("Exception while starting the Association=%s",
								this.aspFactory.association.getName()), e);
			}
		} else {
			logger.error(String.format("Received ASPDOWN_ACK=%s for ASPFactory=%s. But Aspfactory is down.", aspUpAck,
					this.aspFactory.getName()));
		}
	}

	/**
	 * If we receive Heartbeat, we send back response
	 * 
	 * @param hrtBeat
	 */
	public void handleHeartbeat(Heartbeat hrtBeat) {
		HeartbeatAck hrtBeatAck = (HeartbeatAck) this.aspFactory.messageFactory.createMessage(
				MessageClass.ASP_STATE_MAINTENANCE, MessageType.HEARTBEAT_ACK);
		hrtBeatAck.setHeartbeatData(hrtBeat.getHeartbeatData());
		this.aspFactory.write(hrtBeatAck);

	}

	private boolean activate(Asp asp) {
		// If its loadshare, we want to send ASP_ACTIVE else if its
		// Override and there is already one ACTIVE, we leave this one
		// as INACTIVE

		As as = asp.getAs();

		// By default we assume Traffic Mode is Loadshare
		if (as.getTrafficModeType() == null || as.getTrafficModeType().getMode() == TrafficModeType.Loadshare) {
			// Activate this ASP
			this.aspFactory.sendAspActive(as);
			return true;
		} else if (as.getTrafficModeType().getMode() == TrafficModeType.Override) {

			for (FastList.Node<Asp> n = as.getAspList().head(), end = as.getAspList().tail(); (n = n.getNext()) != end;) {
				Asp asptemp = n.getValue();
				FSM fsm = asptemp.getLocalFSM();
				AspState aspState = AspState.getState(fsm.getState().getName());

				if (!asptemp.getName().equals(asp.getName())
						&& (aspState == AspState.ACTIVE_SENT || aspState == AspState.ACTIVE)) {
					return false;
				}
			}// for

			this.aspFactory.sendAspActive(as);
			return true;

		}
		return false;
	}

}
