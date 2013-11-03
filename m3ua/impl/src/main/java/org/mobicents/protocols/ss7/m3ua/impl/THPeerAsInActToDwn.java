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
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSMState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;

/**
 *
 * @author amit bhayani
 *
 */
public class THPeerAsInActToDwn implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(THPeerAsActToPen.class);

    private AsImpl asImpl = null;
    private FSM fsm;

    public THPeerAsInActToDwn(AsImpl asImpl, FSM fsm) {
        this.asImpl = asImpl;
        this.fsm = fsm;
    }

    public boolean process(FSMState state) {
        AspImpl causeAsp = (AspImpl) this.fsm.getAttribute(AsImpl.ATTRIBUTE_ASP);

        // check if there is atleast one other ASP in INACTIVE state. If
        // yes this AS remains in INACTIVE state else goes in DOWN state.
        for (FastList.Node<Asp> n = this.asImpl.appServerProcs.head(), end = this.asImpl.appServerProcs.tail(); (n = n
                .getNext()) != end;) {
            AspImpl aspImpl = (AspImpl) n.getValue();

            FSM aspLocalFSM = aspImpl.getLocalFSM();
            AspState aspState = AspState.getState(aspLocalFSM.getState().getName());

            if (aspImpl != causeAsp && aspState == AspState.INACTIVE) {
                return false;
            }
        }
        return true;
    }

}
