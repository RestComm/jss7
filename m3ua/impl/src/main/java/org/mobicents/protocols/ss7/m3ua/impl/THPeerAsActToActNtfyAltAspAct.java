/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012. 
 * and individual contributors
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
package org.mobicents.protocols.ss7.m3ua.impl;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.UnknownTransitionException;

/**
 * NTFY is received by this ASP stating that other ASP is ACTIVE and
 * corresponding AS is Override traffic mode. Hence this ASP should be moved to
 * INACTIVE state
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

	public boolean process(State state) {
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
