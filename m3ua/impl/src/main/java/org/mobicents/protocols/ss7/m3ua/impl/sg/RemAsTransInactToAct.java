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

package org.mobicents.protocols.ss7.m3ua.impl.sg;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

public class RemAsTransInactToAct implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(RemAsTransInactToAct.class);

    private RemAsImpl as = null;
    private FSM fsm;

    private int lbCount = 0;

    public RemAsTransInactToAct(RemAsImpl as, FSM fsm) {
        this.as = as;
        this.fsm = fsm;
    }

    public boolean process(State state) {
        try {

            if (this.as.getTrafficModeType().getMode() == TrafficModeType.Broadcast) {
                // We don't handle this
                return false;
            }

            // For Traffic Mode Type = load-balancing, need to check policy to
            // have 'minAspActiveForLb' ASP's ACTIVE before AS_ACTIVE NOTIFY is
            // sent.
            if (this.as.getTrafficModeType().getMode() == TrafficModeType.Loadshare) {
                lbCount = this.as.getMinAspActiveForLb();

                // Find out how many ASP's are ACTIVE now
                for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList()
                        .tail(); (n = n.getNext()) != end;) {
                    RemAspImpl remAspImpl = (RemAspImpl) n.getValue();
                    if (remAspImpl.getState() == AspState.ACTIVE) {
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
            for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList()
                    .tail(); (n = n.getNext()) != end;) {
                RemAspImpl remAspImpl = (RemAspImpl) n.getValue();

                if (remAspImpl.getState() == AspState.INACTIVE || remAspImpl.getState() == AspState.ACTIVE) {
                    Notify msg = createNotify(remAspImpl);
                    remAspImpl.getAspFactory().write(msg);
                }
            }

            return true;
        } catch (Exception e) {
            logger.error(String.format("Error while translating Rem AS to INACTIVE. %s", this.fsm.toString()), e);
        }
        // something wrong
        return false;
    }

    private Notify createNotify(RemAspImpl remAsp) {
        Notify msg = (Notify) this.as.getM3UAProvider().getMessageFactory().createMessage(MessageClass.MANAGEMENT,
                MessageType.NOTIFY);

        Status status = this.as.getM3UAProvider().getParameterFactory().createStatus(Status.STATUS_AS_State_Change,
                Status.INFO_AS_ACTIVE);
        msg.setStatus(status);

        if (remAsp.getASPIdentifier() != null) {
            msg.setASPIdentifier(remAsp.getASPIdentifier());
        }

        msg.setRoutingContext(this.as.getRoutingContext());

        return msg;
    }

}
