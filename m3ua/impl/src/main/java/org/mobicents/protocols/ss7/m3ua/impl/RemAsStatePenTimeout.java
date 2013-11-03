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
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSMStateEventHandler;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;

/**
 * {@link RemAsStatePenTimeout#onEvent(FSMState)} is called when the pending timer T(r) expires.
 *
 * @author amit bhayani
 *
 */
public class RemAsStatePenTimeout implements FSMStateEventHandler {

    private AsImpl asImpl;
    private FSM fsm;
    private static final Logger logger = Logger.getLogger(RemAsStatePenTimeout.class);

    boolean inactive = false;

    public RemAsStatePenTimeout(AsImpl asImpl, FSM fsm) {
        this.asImpl = asImpl;
        this.fsm = fsm;
    }

    /**
     * <p>
     * An active ASP has transitioned to ASP-INACTIVE or ASP DOWN and it was the last remaining active ASP in the AS. A recovery
     * timer T(r) SHOULD be started, and all incoming signalling messages SHOULD be queued by the SGP. If an ASP becomes
     * ASP-ACTIVE before T(r) expires, the AS is moved to the AS-ACTIVE state, and all the queued messages will be sent to the
     * ASP.
     * </p>
     * <p>
     * If T(r) expires before an ASP becomes ASP-ACTIVE, and the SGP has no alternative, the SGP may stop queuing messages and
     * discard all previously queued messages. The AS will move to the AS-INACTIVE state if at least one ASP is in ASP-INACTIVE;
     * otherwise, it will move to AS-DOWN state.
     * </p>
     */
    public void onEvent(FSMState state) {
        this.inactive = false;

        // Clear the Pending Queue for this As
        this.asImpl.clearPendingQueue();

        // check if there are any ASP's who are INACTIVE, transition to
        // INACTIVE else DOWN
        for (FastList.Node<Asp> n = this.asImpl.appServerProcs.head(), end = this.asImpl.appServerProcs.tail(); (n = n
                .getNext()) != end;) {
            AspImpl remAspImpl = (AspImpl) n.getValue();

            FSM aspPeerFSM = remAspImpl.getPeerFSM();
            AspState aspState = AspState.getState(aspPeerFSM.getState().getName());

            if (aspState == AspState.INACTIVE) {
                try {

                    if (!this.inactive) {
                        this.fsm.signal(TransitionState.AS_INACTIVE);
                        inactive = true;
                    }

                    if (this.asImpl.getFunctionality() != Functionality.IPSP) {
                        Notify msg = createNotify(remAspImpl);
                        remAspImpl.getAspFactory().write(msg);
                    }
                } catch (UnknownTransitionException e) {
                    logger.error(String.format("Error while translating Rem AS to INACTIVE. %s", this.fsm.toString()), e);
                }

            }// if (remAspImpl.getState() == AspState.INACTIVE)
        }// for

        if (!this.inactive) {
            // else transition to DOWN
            try {
                this.fsm.signal(TransitionState.AS_DOWN);
                inactive = true;
            } catch (UnknownTransitionException e) {
                logger.error(String.format("Error while translating Rem AS to DOWN. %s", this.fsm.toString()), e);
            }
        }

        // We want to pass MTP3 PAUSE only for SE. If its DE the peer transition handler will take care of MTP3 PAUSE
        if (asImpl.getExchangeType() == ExchangeType.SE) {
            FastSet<AsStateListener> asStateListeners = this.asImpl.getAsStateListeners();
            for (FastSet.Record r = asStateListeners.head(), end = asStateListeners.tail(); (r = r.getNext()) != end;) {
                AsStateListener asAsStateListener = asStateListeners.valueOf(r);
                try {
                    asAsStateListener.onAsInActive(this.asImpl);
                } catch (Exception e) {
                    logger.error(String.format("Error while calling AsStateListener=%s onAsInActive method for As=%s",
                            asAsStateListener, this.asImpl));
                }
            }
        }
    }

    private Notify createNotify(AspImpl remAsp) {
        Notify msg = (Notify) this.asImpl.getMessageFactory().createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);

        Status status = this.asImpl.getParameterFactory().createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
        msg.setStatus(status);

        if (remAsp.getASPIdentifier() != null) {
            msg.setASPIdentifier(remAsp.getASPIdentifier());
        }

        msg.setRoutingContext(this.asImpl.getRoutingContext());

        return msg;
    }

}
