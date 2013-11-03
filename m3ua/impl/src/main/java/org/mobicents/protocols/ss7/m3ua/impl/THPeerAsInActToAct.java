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

import javolution.util.FastSet;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSMState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;

/**
 *
 * @author amit bhayani
 *
 */
public class THPeerAsInActToAct implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(THPeerAsInActToAct.class);

    private AsImpl asImpl = null;
    private FSM fsm;

    THPeerAsInActToAct(AsImpl asImpl, FSM fsm) {
        this.asImpl = asImpl;
        this.fsm = fsm;
    }

    @Override
    public boolean process(FSMState state) {
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
        return true;
    }

}
