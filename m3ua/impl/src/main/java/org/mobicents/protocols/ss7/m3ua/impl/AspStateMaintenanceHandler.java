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

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.Asp;
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

    public AspStateMaintenanceHandler(AspFactoryImpl aspFactoryImpl) {
        super(aspFactoryImpl);
    }

    protected void handleAspUp(ASPUp aspUp) {

        if (aspFactoryImpl.getFunctionality() == Functionality.SGW
                || (aspFactoryImpl.getFunctionality() == Functionality.AS && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP
                        && aspFactoryImpl.getExchangeType() == ExchangeType.SE && aspFactoryImpl.getIpspType() == IPSPType.SERVER)) {

            ASPUpAck aspUpAck = (ASPUpAck) this.aspFactoryImpl.messageFactory.createMessage(MessageClass.ASP_STATE_MAINTENANCE,
                    MessageType.ASP_UP_ACK);

            ASPIdentifier aspId = aspUp.getASPIdentifier();
            aspUpAck.setASPIdentifier(aspId);
            this.aspFactoryImpl.write(aspUpAck);

            // If an ASP Up message is received and, internally, the remote ASP
            // is in the ASP-ACTIVE state, an ASP Up Ack message is returned, as
            // well as an Error message ("Unexpected Message"). In addition, the
            // remote ASP state is changed to ASP-INACTIVE in all relevant
            // Application Servers, and all registered Routing Keys are
            // considered deregistered.

            for (FastList.Node<Asp> n = this.aspFactoryImpl.aspList.head(), end = this.aspFactoryImpl.aspList.tail(); (n = n
                    .getNext()) != end;) {
                AspImpl aspImpl = (AspImpl) n.getValue();

                // Set the ASPIdentifier for each ASP
                aspImpl.setASPIdentifier(aspUp.getASPIdentifier());

                FSM aspPeerFSM = aspImpl.getPeerFSM();
                if (aspPeerFSM == null) {
                    logger.error(String.format("Received ASPUP=%s for ASP=%s. But peer FSM is null.", aspUp,
                            this.aspFactoryImpl.getName()));
                    return;
                }

                if (AspState.getState(aspPeerFSM.getState().getName()) == AspState.ACTIVE) {
                    // Check if ASP is in ACTIVE, its error state

                    ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);

                    this.sendError(aspImpl.getAs().getRoutingContext(), errorCodeObj);
                    // break;
                }

                try {
                    // Signal ASP about state change
                    aspPeerFSM.setAttribute(FSM.ATTRIBUTE_MESSAGE, aspUp);
                    aspPeerFSM.signal(TransitionState.ASP_UP);

                    // Signal corresponding AS about ASP's state transition
                    FSM asLocalFSM = ((AsImpl) aspImpl.getAs()).getLocalFSM();

                    asLocalFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                    asLocalFSM.signal(TransitionState.ASP_UP);
                } catch (UnknownTransitionException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        } else {
            // TODO : Should we silently drop ASPUP_ACK?

            // ASPUP_ACK is unexpected in this state
            ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
            sendError(null, errorCodeObj);
        }

    }

    protected void handleAspUpAck(ASPUpAck aspUpAck) {

        if (!this.aspFactoryImpl.started) {
            // If management stopped this ASP, ignore ASPUpAck
            return;
        }

        if (aspFactoryImpl.getFunctionality() == Functionality.AS
                || (aspFactoryImpl.getFunctionality() == Functionality.SGW && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP
                        && aspFactoryImpl.getExchangeType() == ExchangeType.SE && aspFactoryImpl.getIpspType() == IPSPType.CLIENT)) {

            for (FastList.Node<Asp> n = this.aspFactoryImpl.aspList.head(), end = this.aspFactoryImpl.aspList.tail(); (n = n
                    .getNext()) != end;) {
                AspImpl aspImpl = (AspImpl) n.getValue();

                FSM aspLocalFSM = aspImpl.getLocalFSM();
                if (aspLocalFSM == null) {
                    logger.error(String.format("Received ASPUP_ACK=%s for ASP=%s. But local FSM is null.", aspUpAck,
                            this.aspFactoryImpl.getName()));
                    return;
                }

                boolean transToActive = this.activate(aspImpl);

                if (!transToActive) {
                    // Transition to INACTIVE
                    try {
                        aspLocalFSM.signal(TransitionState.ASP_INACTIVE);
                    } catch (UnknownTransitionException e) {
                        logger.error(e.getMessage(), e);
                    }
                } else {
                    // Transition to ACTIVE_SENT
                    try {
                        aspLocalFSM.signal(TransitionState.ASP_ACTIVE_SENT);

                        if (aspFactoryImpl.getFunctionality() == Functionality.IPSP) {
                            // If its IPSP, we know NTFY will not be received,
                            // so transition AS FSM here
                            AsImpl asImpl = (AsImpl) aspImpl.getAs();
                            FSM asPeerFSM = asImpl.getPeerFSM();

                            if (asPeerFSM == null) {
                                logger.error(String.format("Received ASPUP_ACK=%s for ASP=%s. But Peer FSM of AS=%s is null.",
                                        aspUpAck, this.aspFactoryImpl.getName(), asImpl));
                                return;
                            }

                            AsState asPeerFSMState = AsState.getState(asPeerFSM.getState().getName());
                            if (AsState.DOWN == asPeerFSMState) {
                                // Transition to INACTIVE only if its DOWN
                                asPeerFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                                asPeerFSM.signal(TransitionState.AS_STATE_CHANGE_INACTIVE);
                            }
                        }
                    } catch (UnknownTransitionException e) {
                        logger.error(e.getMessage(), e);
                    }
                }// if..else
            }// for

        } else {
            // TODO : Should we silently drop ASPUP_ACK?

            // ASPUP_ACK is unexpected in this state
            ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
            sendError(null, errorCodeObj);
        }
    }

    protected void handleAspDown(ASPDown aspDown) {

        if (aspFactoryImpl.getFunctionality() == Functionality.SGW
                || (aspFactoryImpl.getFunctionality() == Functionality.AS && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP
                        && aspFactoryImpl.getExchangeType() == ExchangeType.SE && aspFactoryImpl.getIpspType() == IPSPType.SERVER)) {

            ASPDownAck aspDwnAck = (ASPDownAck) this.aspFactoryImpl.messageFactory.createMessage(
                    MessageClass.ASP_STATE_MAINTENANCE, MessageType.ASP_DOWN_ACK);
            this.aspFactoryImpl.write(aspDwnAck);

            // ASPSM should be given to all ASP's
            for (FastList.Node<Asp> n = this.aspFactoryImpl.aspList.head(), end = this.aspFactoryImpl.aspList.tail(); (n = n
                    .getNext()) != end;) {
                AspImpl aspImpl = (AspImpl) n.getValue();

                FSM aspPeerFSM = aspImpl.getPeerFSM();
                if (aspPeerFSM == null) {
                    logger.error(String.format("Received ASPDOWN=%s for ASP=%s. But peer FSM is null.", aspDown,
                            this.aspFactoryImpl.getName()));
                    return;
                }

                try {
                    // Signal ASP about state change
                    aspPeerFSM.setAttribute(FSM.ATTRIBUTE_MESSAGE, aspDown);
                    aspPeerFSM.signal(TransitionState.ASP_DOWN);

                    // Signal corresponding AS about ASP's state transition
                    FSM asLocalFSM = ((AsImpl) aspImpl.getAs()).getLocalFSM();

                    asLocalFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                    asLocalFSM.signal(TransitionState.ASP_DOWN);

                } catch (UnknownTransitionException e) {
                    logger.error(e.getMessage(), e);
                }

            }

        } else {
            // TODO : Should we silently drop ASPUP_ACK?

            // ASPUP_ACK is unexpected in this state
            ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
            sendError(null, errorCodeObj);
        }
    }

    protected void handleAspDownAck(ASPDownAck aspUpAck) {

        if (!this.aspFactoryImpl.started) {

            boolean stopAssociation = true;

            for (FastList.Node<Asp> n = this.aspFactoryImpl.aspList.head(), end = this.aspFactoryImpl.aspList.tail(); (n = n
                    .getNext()) != end;) {
                AspImpl aspImpl = (AspImpl) n.getValue();
                FSM fsm = aspImpl.getLocalFSM();
                if (fsm == null) {
                    logger.error(String.format("Received ASPDOWN_ACK=%s for ASP=%s. But local FSM is null.", aspUpAck,
                            this.aspFactoryImpl.getName()));
                    continue;
                }

                FSM fsmPeer = aspImpl.getPeerFSM();

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
                                this.aspFactoryImpl.association.getName(), this.aspFactoryImpl.getName()));
            }

            try {
                if (this.aspFactoryImpl.aspFactoryStopTimer != null) {
                    this.aspFactoryImpl.aspFactoryStopTimer.cancel();
                }
                this.aspFactoryImpl.transportManagement.stopAssociation(this.aspFactoryImpl.association.getName());
            } catch (Exception e) {
                logger.error(
                        String.format("Exception while starting the Association=%s", this.aspFactoryImpl.association.getName()),
                        e);
            }
        } else {
            logger.error(String.format("Received ASPDOWN_ACK=%s for ASPFactory=%s. But Aspfactory is down.", aspUpAck,
                    this.aspFactoryImpl.getName()));
        }
    }

    /**
     * If we receive Heartbeat, we send back response
     *
     * @param hrtBeat
     */
    public void handleHeartbeat(Heartbeat hrtBeat) {
        HeartbeatAck hrtBeatAck = (HeartbeatAck) this.aspFactoryImpl.messageFactory.createMessage(
                MessageClass.ASP_STATE_MAINTENANCE, MessageType.HEARTBEAT_ACK);
        hrtBeatAck.setHeartbeatData(hrtBeat.getHeartbeatData());
        this.aspFactoryImpl.write(hrtBeatAck);

    }

    private boolean activate(AspImpl aspImpl) {
        // If its loadshare, we want to send ASP_ACTIVE else if its
        // Override and there is already one ACTIVE, we leave this one
        // as INACTIVE

        AsImpl asImpl = (AsImpl) aspImpl.getAs();

        // By default we assume Traffic Mode is Loadshare
        if (asImpl.getTrafficModeType() == null || asImpl.getTrafficModeType().getMode() == TrafficModeType.Loadshare) {
            // Activate this ASP
            this.aspFactoryImpl.sendAspActive(asImpl);
            return true;
        } else if (asImpl.getTrafficModeType().getMode() == TrafficModeType.Override) {

            for (FastList.Node<Asp> n = asImpl.appServerProcs.head(), end = asImpl.appServerProcs.tail(); (n = n.getNext()) != end;) {
                AspImpl asptemp = (AspImpl) n.getValue();
                FSM fsm = asptemp.getLocalFSM();
                AspState aspState = AspState.getState(fsm.getState().getName());

                if (!asptemp.getName().equals(aspImpl.getName())
                        && (aspState == AspState.ACTIVE_SENT || aspState == AspState.ACTIVE)) {
                    return false;
                }
            }// for

            this.aspFactoryImpl.sendAspActive(asImpl);
            return true;

        }
        return false;
    }

}
