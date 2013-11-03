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
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPActive;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPActiveAck;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPInactive;
import org.mobicents.protocols.ss7.m3ua.message.asptm.ASPInactiveAck;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 *
 * @author amit bhayani
 *
 */
public class AspTrafficMaintenanceHandler extends MessageHandler {

    private static final Logger logger = Logger.getLogger(AspTrafficMaintenanceHandler.class);

    public AspTrafficMaintenanceHandler(AspFactoryImpl aspFactoryImpl) {
        super(aspFactoryImpl);
    }

    private void handleAspInactive(AspImpl aspImpl, ASPInactive aspInactive) {
        AsImpl appServer = (AsImpl) aspImpl.getAs();

        FSM aspPeerFSM = aspImpl.getPeerFSM();
        if (aspPeerFSM == null) {
            logger.error(String.format("Received ASPINACTIVE=%s for ASP=%s. But peer FSM for ASP is null.", aspInactive,
                    this.aspFactoryImpl.getName()));
            return;
        }

        FSM asLocalFSM = appServer.getLocalFSM();
        if (asLocalFSM == null) {
            logger.error(String.format("Received ASPINACTIVE=%s for ASP=%s. But local FSM for AS is null.", aspInactive,
                    this.aspFactoryImpl.getName()));
            return;
        }

        ASPInactiveAck aspInactAck = (ASPInactiveAck) this.aspFactoryImpl.messageFactory.createMessage(
                MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_INACTIVE_ACK);
        aspInactAck.setRoutingContext(appServer.getRoutingContext());

        this.aspFactoryImpl.write(aspInactAck);

        try {
            aspPeerFSM.setAttribute(FSM.ATTRIBUTE_MESSAGE, aspInactive);
            aspPeerFSM.signal(TransitionState.ASP_INACTIVE);

            // Signal AS to transition
            asLocalFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
            asLocalFSM.signal(TransitionState.ASP_INACTIVE);

        } catch (UnknownTransitionException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void handleAspActive(AspImpl aspImpl, ASPActive aspActive) {
        AsImpl appServer = (AsImpl) aspImpl.getAs();

        TrafficModeType trfModType = aspActive.getTrafficModeType();

        if (appServer.getTrafficModeType() != null) {
            // AppServer has Traffic Mode Type defined check if it
            // matches with sent ASP ACTIVE Message
            if (trfModType != null && appServer.getTrafficModeType().getMode() != trfModType.getMode()) {

                // Traffic Mode Type mismatch. Send Error.
                // TODO should send error or drop message?
                ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory
                        .createErrorCode(ErrorCode.Unsupported_Traffic_Mode_Type);
                this.sendError(appServer.getRoutingContext(), errorCodeObj);
                return;
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

        FSM aspPeerFSM = aspImpl.getPeerFSM();
        if (aspPeerFSM == null) {
            logger.error(String.format("Received ASPACTIVE=%s for ASP=%s. But peer FSM for ASP is null.", aspActive,
                    this.aspFactoryImpl.getName()));
            return;
        }

        FSM asLocalFSM = appServer.getLocalFSM();
        if (asLocalFSM == null) {
            logger.error(String.format("Received ASPACTIVE=%s for ASP=%s. But local FSM for AS is null.", aspActive,
                    this.aspFactoryImpl.getName()));
            return;
        }

        ASPActiveAck aspActAck = (ASPActiveAck) this.aspFactoryImpl.messageFactory.createMessage(
                MessageClass.ASP_TRAFFIC_MAINTENANCE, MessageType.ASP_ACTIVE_ACK);
        aspActAck.setTrafficModeType(appServer.getTrafficModeType());
        aspActAck.setRoutingContext(appServer.getRoutingContext());

        this.aspFactoryImpl.write(aspActAck);

        try {
            aspPeerFSM.setAttribute(FSM.ATTRIBUTE_MESSAGE, aspActive);
            aspPeerFSM.signal(TransitionState.ASP_ACTIVE);

            // Signal AS to transition
            asLocalFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
            asLocalFSM.signal(TransitionState.ASP_ACTIVE);

        } catch (UnknownTransitionException e) {
            logger.error(e.getMessage(), e);
        }
    }

    protected void handleAspActive(ASPActive aspActive) {

        RoutingContext rc = aspActive.getRoutingContext();

        if (aspFactoryImpl.getFunctionality() == Functionality.SGW
                || (aspFactoryImpl.getFunctionality() == Functionality.AS && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP
                        && aspFactoryImpl.getExchangeType() == ExchangeType.SE && aspFactoryImpl.getIpspType() == IPSPType.SERVER)) {
            if (rc == null) {
                AspImpl aspImpl = this.getAspForNullRc();

                if (aspImpl == null) {
                    // Error condition
                    logger.error(String
                            .format("Rx : ASP ACTIVE=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sent back Error",
                                    aspActive, this.aspFactoryImpl.getName()));
                    return;
                }
                handleAspActive(aspImpl, aspActive);
            } else {
                long[] rcs = rc.getRoutingContexts();

                for (int count = 0; count < rcs.length; count++) {
                    AspImpl aspImpl = this.aspFactoryImpl.getAsp(rcs[count]);

                    if (aspImpl == null) {
                        // this is error. Send back error
                        RoutingContext rcObj = this.aspFactoryImpl.parameterFactory
                                .createRoutingContext(new long[] { rcs[count] });
                        ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory
                                .createErrorCode(ErrorCode.Invalid_Routing_Context);
                        sendError(rcObj, errorCodeObj);
                        logger.error(String
                                .format("Rx : ASPACTIVE=%s with RC=%d for Aspfactory=%s. But no ASP configured for this RC. Sending back Error",
                                        aspActive, rcs[count], this.aspFactoryImpl.getName()));
                        continue;
                    }
                    handleAspActive(aspImpl, aspActive);
                }// for
            }

        } else {
            // TODO : Should we silently drop ASPACTIVE?

            // ASPUP_ACK is unexpected in this state
            ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
            sendError(null, errorCodeObj);
        }
    }

    private void handleAspActiveAck(AspImpl aspImpl, ASPActiveAck aspActiveAck, TrafficModeType trMode) {
        AsImpl asImpl = (AsImpl) aspImpl.getAs();

        if (trMode == null) {
            trMode = aspImpl.getAs().getDefaultTrafficModeType();
        }

        asImpl.setTrafficModeType(trMode);

        FSM aspLocalFSM = aspImpl.getLocalFSM();
        if (aspLocalFSM == null) {
            logger.error(String.format("Received ASPACTIVE_ACK=%s for ASP=%s. But local FSM is null.", aspActiveAck,
                    this.aspFactoryImpl.getName()));
            return;
        }

        try {
            aspLocalFSM.signal(TransitionState.ASP_ACTIVE_ACK);

            if (aspFactoryImpl.getFunctionality() == Functionality.IPSP) {
                // If its IPSP, we know NTFY will not be received,
                // so transition AS FSM here
                FSM asPeerFSM = asImpl.getPeerFSM();

                if (asPeerFSM == null) {
                    logger.error(String.format("Received ASPACTIVE_ACK=%s for ASP=%s. But Peer FSM of AS=%s is null.",
                            aspActiveAck, this.aspFactoryImpl.getName(), asImpl));
                    return;
                }

                asPeerFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                asPeerFSM.signal(TransitionState.AS_STATE_CHANGE_ACTIVE);
            }
        } catch (UnknownTransitionException e) {
            logger.error(e.getMessage(), e);
        }
    }

    protected void handleAspActiveAck(ASPActiveAck aspActiveAck) {
        if (!this.aspFactoryImpl.started) {
            // If management stopped this ASP, ignore ASPActiveAck
            return;
        }

        RoutingContext rc = aspActiveAck.getRoutingContext();

        if (aspFactoryImpl.getFunctionality() == Functionality.AS
                || (aspFactoryImpl.getFunctionality() == Functionality.SGW && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP
                        && aspFactoryImpl.getExchangeType() == ExchangeType.SE && aspFactoryImpl.getIpspType() == IPSPType.CLIENT)) {

            TrafficModeType trMode = aspActiveAck.getTrafficModeType();

            if (rc == null) {
                AspImpl aspImpl = this.getAspForNullRc();

                if (aspImpl == null) {
                    // Error condition
                    logger.error(String
                            .format("Rx : ASP ACTIVE_ACK=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sent back Error",
                                    aspActiveAck, this.aspFactoryImpl.getName()));
                    return;
                }
                handleAspActiveAck(aspImpl, aspActiveAck, trMode);
            } else {
                long[] rcs = rc.getRoutingContexts();
                for (int count = 0; count < rcs.length; count++) {
                    AspImpl aspImpl = this.aspFactoryImpl.getAsp(rcs[count]);
                    handleAspActiveAck(aspImpl, aspActiveAck, trMode);
                }// for
            }

        } else {
            // TODO : Should we silently drop ASPACTIVE_ACK?

            // ASPACTIVE_ACK is unexpected in this state
            ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
            sendError(rc, errorCodeObj);
        }

    }

    protected void handleAspInactive(ASPInactive aspInactive) {
        RoutingContext rc = aspInactive.getRoutingContext();

        if (aspFactoryImpl.getFunctionality() == Functionality.SGW
                || (aspFactoryImpl.getFunctionality() == Functionality.AS && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP
                        && aspFactoryImpl.getExchangeType() == ExchangeType.SE && aspFactoryImpl.getIpspType() == IPSPType.SERVER)) {
            if (rc == null) {
                AspImpl aspImpl = this.getAspForNullRc();

                if (aspImpl == null) {
                    // Error condition
                    logger.error(String
                            .format("Rx : ASPINACTIVE=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sent back Error",
                                    aspInactive, this.aspFactoryImpl.getName()));
                    return;
                }
                handleAspInactive(aspImpl, aspInactive);
            } else {
                long[] rcs = rc.getRoutingContexts();

                for (int count = 0; count < rcs.length; count++) {
                    AspImpl aspImpl = this.aspFactoryImpl.getAsp(rcs[count]);

                    if (aspImpl == null) {
                        // this is error. Send back error
                        RoutingContext rcObj = this.aspFactoryImpl.parameterFactory
                                .createRoutingContext(new long[] { rcs[count] });
                        ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory
                                .createErrorCode(ErrorCode.Invalid_Routing_Context);
                        sendError(rcObj, errorCodeObj);
                        logger.error(String
                                .format("Rx : ASPINACTIVE=%s with RC=%d for Aspfactory=%s. But no ASP configured for this RC. Sending back Error",
                                        aspInactive, rcs[count], this.aspFactoryImpl.getName()));
                        continue;
                    }
                    handleAspInactive(aspImpl, aspInactive);
                }// for
            }

        } else {
            // TODO : Should we silently drop ASPINACTIVE?

            // ASPUP_ACK is unexpected in this state
            ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
            sendError(null, errorCodeObj);
        }
    }

    protected void handleAspInactiveAck(ASPInactiveAck aspInactiveAck) {
        if (!this.aspFactoryImpl.started) {
            // If management stopped this ASP, ignore ASPInactiveAck
            return;
        }

        RoutingContext rc = aspInactiveAck.getRoutingContext();

        if (aspFactoryImpl.getFunctionality() == Functionality.AS
                || (aspFactoryImpl.getFunctionality() == Functionality.SGW && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP
                        && aspFactoryImpl.getExchangeType() == ExchangeType.SE && aspFactoryImpl.getIpspType() == IPSPType.CLIENT)) {

            if (rc == null) {
                AspImpl aspImpl = this.getAspForNullRc();

                if (aspImpl == null) {
                    // Error condition
                    logger.error(String
                            .format("Rx : ASPINACTIVE_ACK=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sent back Error",
                                    aspInactiveAck, this.aspFactoryImpl.getName()));
                    return;
                }
                handleAspInactiveAck(aspImpl, aspInactiveAck);
            } else {

                long[] rcs = aspInactiveAck.getRoutingContext().getRoutingContexts();
                for (int count = 0; count < rcs.length; count++) {
                    AspImpl aspImpl = this.aspFactoryImpl.getAsp(rcs[count]);

                    if (aspImpl == null) {
                        // this is error. Send back error
                        RoutingContext rcObj = this.aspFactoryImpl.parameterFactory
                                .createRoutingContext(new long[] { rcs[count] });
                        ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory
                                .createErrorCode(ErrorCode.Invalid_Routing_Context);
                        sendError(rcObj, errorCodeObj);
                        logger.error(String
                                .format("Rx : ASPINACTIVE_ACK=%s with RC=%d for Aspfactory=%s. But no ASP configured for this RC. Sending back Error",
                                        aspInactiveAck, rcs[count], this.aspFactoryImpl.getName()));
                        continue;
                    }

                    handleAspInactiveAck(aspImpl, aspInactiveAck);
                }// for
            }

        } else {
            // TODO : Should we silently drop ASPINACTIVE_ACK?

            // ASPINACTIVE_ACK is unexpected in this state
            ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
            sendError(rc, errorCodeObj);
        }

    }

    private void handleAspInactiveAck(AspImpl aspImpl, ASPInactiveAck aspInactiveAck) {
        FSM aspLocalFSM = aspImpl.getLocalFSM();
        if (aspLocalFSM == null) {
            logger.error(String.format("Received ASPINACTIVE_ACK=%s for ASP=%s. But local FSM is null.", aspInactiveAck,
                    this.aspFactoryImpl.getName()));
            return;
        }

        AsImpl asImpl = (AsImpl) aspImpl.getAs();

        try {
            aspLocalFSM.signal(TransitionState.ASP_INACTIVE_ACK);

            if (this.aspFactoryImpl.getFunctionality() == Functionality.IPSP) {
                // If its IPSP, we know NTFY will not be received,
                // so transition AS FSM here
                FSM asPeerFSM = asImpl.getPeerFSM();

                if (asPeerFSM == null) {
                    logger.error(String.format("Received ASPINACTIVE_ACK=%s for ASP=%s. But Peer FSM of AS=%s is null.",
                            aspInactiveAck, this.aspFactoryImpl.getName(), asImpl));
                    return;
                }

                if (asImpl.getTrafficModeType().getMode() == TrafficModeType.Loadshare) {
                    // If it is loadshare and if there is atleast one other ASP
                    // who ACTIVE, dont transition AS to INACTIVE
                    for (FastList.Node<Asp> n = asImpl.appServerProcs.head(), end = asImpl.appServerProcs.tail(); (n = n
                            .getNext()) != end;) {
                        AspImpl remAspImpl = (AspImpl) n.getValue();

                        FSM aspPeerFSM = remAspImpl.getPeerFSM();
                        AspState aspState = AspState.getState(aspPeerFSM.getState().getName());

                        if (aspState == AspState.ACTIVE) {
                            return;
                        }
                    }
                }

                // TODO : Check if other ASP are INACTIVE, if yes ACTIVATE them
                asPeerFSM.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                asPeerFSM.signal(TransitionState.AS_STATE_CHANGE_PENDING);
            }
        } catch (UnknownTransitionException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
