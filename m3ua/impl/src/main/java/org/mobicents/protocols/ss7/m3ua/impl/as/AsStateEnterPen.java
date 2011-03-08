/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.mobicents.protocols.ss7.m3ua.impl.as;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.TransitionState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.StateEventHandler;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;

public class AsStateEnterPen implements StateEventHandler {

    private static final Logger logger = Logger.getLogger(AsStateEnterPen.class);

    private AsImpl as = null;
    private FSM fsm;

    public AsStateEnterPen(AsImpl as, FSM fsm) {
        this.as = as;
        this.fsm = fsm;
    }

    public void onEvent(State state) {
        // If there is even one ASP in INACTIVE state for this AS, ACTIVATE it
        for (FastList.Node<Asp> n = as.getAspList().head(), end = as.getAspList().tail(); (n = n
                .getNext()) != end;) {
            AspImpl asp = (AspImpl) n.getValue();
            
            if (asp.getState() == AspState.INACTIVE) {
                LocalAspFactory localAspFactory = (LocalAspFactory) asp.getAspFactory();
                localAspFactory.sendAspActive(this.as);

                // Transition the state of ASP to ACTIVE_SENT
                try {
                    asp.getFSM().signal(TransitionState.ASP_ACTIVE_SENT);
                } catch (UnknownTransitionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
