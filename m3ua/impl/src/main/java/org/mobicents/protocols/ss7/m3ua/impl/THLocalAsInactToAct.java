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
import javolution.util.FastSet;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.Asp;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSMState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 *
 * @author amit bhayani
 *
 */
public class THLocalAsInactToAct implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(THLocalAsInactToAct.class);

    private AsImpl asImpl = null;
    private FSM fsm;

    private int lbCount = 0;

    public THLocalAsInactToAct(AsImpl asImpl, FSM fsm) {
        this.asImpl = asImpl;
        this.fsm = fsm;
    }

    public boolean process(FSMState state) {
        try {

            if (this.asImpl.getTrafficModeType().getMode() == TrafficModeType.Broadcast) {
                // We don't handle this
                return false;
            }

            // For Traffic Mode Type = load-balancing, need to check policy to
            // have 'minAspActiveForLb' ASP's ACTIVE before AS_ACTIVE NOTIFY is
            // sent.
            if (this.asImpl.getTrafficModeType().getMode() == TrafficModeType.Loadshare) {
                lbCount = this.asImpl.getMinAspActiveForLb();

                // Find out how many ASP's are ACTIVE now
                for (FastList.Node<Asp> n = this.asImpl.appServerProcs.head(), end = this.asImpl.appServerProcs.tail(); (n = n
                        .getNext()) != end;) {
                    AspImpl remAspImpl = (AspImpl) n.getValue();
                    FSM aspPeerFSM = remAspImpl.getPeerFSM();
                    AspState aspState = AspState.getState(aspPeerFSM.getState().getName());

                    if (aspState == AspState.ACTIVE) {
                        lbCount--;
                    }
                }

                if (lbCount > 0) {
                    // We still need more ASP ACTIVE before AS is ACTIVE
                    return false;
                }
            }

            // Iterate through ASP's and send AS_ACTIVE to ASP's who
            // are INACTIVE or ACTIVE
            if (asImpl.getFunctionality() != Functionality.IPSP) {
                // Send Notify only for ASP or SGW

                for (FastList.Node<Asp> n = this.asImpl.appServerProcs.head(), end = this.asImpl.appServerProcs.tail(); (n = n
                        .getNext()) != end;) {
                    AspImpl remAspImpl = (AspImpl) n.getValue();

                    FSM aspPeerFSM = remAspImpl.getPeerFSM();
                    AspState aspState = AspState.getState(aspPeerFSM.getState().getName());

                    if (aspState == AspState.INACTIVE || aspState == AspState.ACTIVE) {
                        Notify msg = createNotify(remAspImpl);
                        remAspImpl.getAspFactory().write(msg);
                    }
                }// for
            }

            // We want to pass MTP3 RESUME only for SE. If its DE the peer transition handler will take care of MTP3 RESUME
            if (asImpl.getExchangeType() == ExchangeType.SE) {
                FastSet<AsStateListener> asStateListeners = this.asImpl.getAsStateListeners();
                for (FastSet.Record r = asStateListeners.head(), end = asStateListeners.tail(); (r = r.getNext()) != end;) {
                    AsStateListener asAsStateListener = asStateListeners.valueOf(r);
                    try {
                        asAsStateListener.onAsActive(this.asImpl);
                    } catch (Exception e) {
                        logger.error(String.format("Error while calling AsStateListener=%s onAsActive method for As=%s",
                                asAsStateListener, this.asImpl));
                    }
                }
            }

            return true;
        } catch (Exception e) {
            logger.error(String.format("Error while translating Rem AS to INACTIVE. %s", this.fsm.toString()), e);
        }
        // something wrong
        return false;
    }

    private Notify createNotify(AspImpl remAsp) {
        Notify msg = (Notify) this.asImpl.getMessageFactory().createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);

        Status status = this.asImpl.getParameterFactory().createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
        msg.setStatus(status);

        if (remAsp.getASPIdentifier() != null) {
            msg.setASPIdentifier(remAsp.getASPIdentifier());
        }

        if (this.asImpl.getRoutingContext() != null) {
            msg.setRoutingContext(this.asImpl.getRoutingContext());
        }

        return msg;
    }

}
