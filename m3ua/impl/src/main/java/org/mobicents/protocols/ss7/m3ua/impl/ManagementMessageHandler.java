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

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Error;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 *
 * @author amit bhayani
 *
 */
public class ManagementMessageHandler extends MessageHandler {

    private static final Logger logger = Logger.getLogger(ManagementMessageHandler.class);

    public ManagementMessageHandler(AspFactoryImpl aspFactoryImpl) {
        super(aspFactoryImpl);
    }

    public void handleNotify(Notify notify) {

        RoutingContext rc = notify.getRoutingContext();

        if (aspFactoryImpl.getFunctionality() == Functionality.AS
                || (aspFactoryImpl.getFunctionality() == Functionality.SGW && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP && aspFactoryImpl.getExchangeType() == ExchangeType.DE)
                || (aspFactoryImpl.getFunctionality() == Functionality.IPSP
                        && aspFactoryImpl.getExchangeType() == ExchangeType.SE && aspFactoryImpl.getIpspType() == IPSPType.CLIENT)) {

            if (rc == null) {

                AspImpl aspImpl = this.getAspForNullRc();
                if (aspImpl == null) {
                    logger.error(String
                            .format("Rx : NTFY=%s with null RC for Aspfactory=%s. But no ASP configured for null RC. Sending back Error",
                                    notify, this.aspFactoryImpl.getName()));
                    return;
                }

                try {
                    // Received NTFY, so peer FSM has to be used.
                    FSM fsm = ((AsImpl) aspImpl.getAs()).getPeerFSM();

                    if (fsm == null) {
                        logger.error(String.format("Received NTFY=%s for ASP=%s. But Peer FSM is null.", notify,
                                this.aspFactoryImpl.getName()));
                        return;
                    }
                    fsm.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                    fsm.signal(TransitionState.getTransition(notify));
                } catch (UnknownTransitionException e) {
                    logger.error(e.getMessage(), e);
                }
            } else {
                long[] rcs = notify.getRoutingContext().getRoutingContexts();
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
                                .format("Rx : NTFY=%s with RC=%d for Aspfactory=%s. But no ASP configured for this RC. Sending back Error",
                                        notify, rcs[count], this.aspFactoryImpl.getName()));
                        continue;
                    }

                    try {
                        // Received NTFY, so peer FSM has to be set.
                        FSM fsm = ((AsImpl) aspImpl.getAs()).getPeerFSM();

                        if (fsm == null) {
                            logger.error(String.format("Received NTFY=%s for ASP=%s. But Peer FSM is null.", notify,
                                    this.aspFactoryImpl.getName()));
                            return;
                        }
                        fsm.setAttribute(AsImpl.ATTRIBUTE_ASP, aspImpl);
                        fsm.signal(TransitionState.getTransition(notify));
                    } catch (UnknownTransitionException e) {
                        logger.error(e.getMessage(), e);
                    }
                }// end of for
            }// if (rc == null) {
        } else {
            // NTFY is unexpected in this state
            ErrorCode errorCodeObj = this.aspFactoryImpl.parameterFactory.createErrorCode(ErrorCode.Unexpected_Message);
            sendError(rc, errorCodeObj);
        }

    }

    public void handleError(Error error) {
        logger.error(error);
    }

}
