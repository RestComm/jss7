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
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSMState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;

/**
 *
 * @author amit bhayani
 *
 */
public class THLocalAsInactToInact implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(THLocalAsInactToInact.class);

    private AsImpl asImpl = null;
    private FSM fsm;

    public THLocalAsInactToInact(AsImpl asImpl, FSM fsm) {
        this.asImpl = asImpl;
        this.fsm = fsm;
    }

    public boolean process(FSMState state) {
        try {
            if (asImpl.getFunctionality() != Functionality.IPSP) {
                // Send Notify only for ASP or SGW
                AspImpl remAsp = (AspImpl) this.fsm.getAttribute(AsImpl.ATTRIBUTE_ASP);

                if (remAsp == null) {
                    logger.error(String.format("No ASP found. %s", this.fsm.toString()));
                    return false;
                }

                Notify msg = createNotify(remAsp);
                remAsp.getAspFactory().write(msg);
            }
            return true;
        } catch (Exception e) {
            logger.error(String.format("Error while translating Rem AS to INACTIVE message. %s", this.fsm.toString()), e);
        }
        return false;
    }

    private Notify createNotify(AspImpl remAsp) {
        Notify msg = (Notify) this.asImpl.getMessageFactory().createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);

        Status status = this.asImpl.getParameterFactory().createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_INACTIVE);
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
