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
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSMState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;

/**
 * @author amit bhayani
 *
 */
public class THPeerAsPendToAct implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(THPeerAsPendToAct.class);

    private AsImpl asImpl = null;
    private FSM fsm;

    /**
     *
     */
    public THPeerAsPendToAct(AsImpl asImpl, FSM fsm) {
        this.asImpl = asImpl;
        this.fsm = fsm;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler#process(org
     * .mobicents.protocols.ss7.m3ua.impl.fsm.State)
     */
    @Override
    public boolean process(FSMState state) {

        // Send the PayloadData (if any) from pending queue to other side
        AspImpl causeAsp = (AspImpl) this.fsm.getAttribute(AsImpl.ATTRIBUTE_ASP);
        this.asImpl.sendPendingPayloadData(causeAsp);

        return true;
    }

}
