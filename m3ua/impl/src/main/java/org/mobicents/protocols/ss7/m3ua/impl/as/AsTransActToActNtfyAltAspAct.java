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

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.TransitionState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;

public class AsTransActToActNtfyAltAspAct implements TransitionHandler {

    private static final Logger logger = Logger.getLogger(AsTransActToActNtfyAltAspAct.class);

    private AsImpl as = null;
    private FSM fsm;

    public AsTransActToActNtfyAltAspAct(AsImpl as, FSM fsm) {
        this.as = as;
        this.fsm = fsm;
    }

    public boolean process(State state) {
        AspImpl causeAsp = (AspImpl) this.fsm.getAttribute(AsImpl.ATTRIBUTE_ASP);

        try {
            causeAsp.getFSM().signal(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE);
        } catch (UnknownTransitionException e) {
        	logger.error(e.getMessage(), e);
        }
        return true;
    }

}
