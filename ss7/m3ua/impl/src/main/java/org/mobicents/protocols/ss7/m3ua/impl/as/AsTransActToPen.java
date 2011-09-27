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

package org.mobicents.protocols.ss7.m3ua.impl.as;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;

/**
 * 
 * @author amit bhayani
 *
 */
public class AsTransActToPen implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(AsTransActToPen.class);

    private AsImpl as = null;
    private FSM fsm;

    public AsTransActToPen(AsImpl as, FSM fsm) {
        this.as = as;
        this.fsm = fsm;
    }

    public boolean process(State state) {
        AspImpl causeAsp = (AspImpl) this.fsm.getAttribute(AsImpl.ATTRIBUTE_ASP);

        // check if there is atleast one other ASP in ACTIVE state. If
        // yes this AS remains in ACTIVE state else goes in PENDING state.
        for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList().tail(); (n = n
                .getNext()) != end;) {
            AspImpl asp = (AspImpl) n.getValue();
            if (asp != causeAsp && asp.getState() == AspState.ACTIVE) {
                return false;
            }
        }
        return true;
    }

}
