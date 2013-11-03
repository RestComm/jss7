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
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;

/**
 * NTFY is received by this ASP stating that other ASP is ACTIVE and corresponding AS is Override traffic mode. Hence this ASP
 * should be moved to INACTIVE state
 *
 * @author amit bhayani
 *
 */
public class THPeerAsActToActNtfyAltAspAct implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(THPeerAsActToActNtfyAltAspAct.class);

    private AsImpl asImpl = null;
    private FSM fsm;

    public THPeerAsActToActNtfyAltAspAct(AsImpl asImpl, FSM fsm) {
        this.asImpl = asImpl;
        this.fsm = fsm;
    }

    public boolean process(FSMState state) {
        AspImpl causeAsp = (AspImpl) this.fsm.getAttribute(AsImpl.ATTRIBUTE_ASP);

        try {
            FSM aspLocalFSM = causeAsp.getLocalFSM();
            aspLocalFSM.signal(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE);
        } catch (UnknownTransitionException e) {
            logger.error(e.getMessage(), e);
        }
        return true;
    }

}
