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

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;

/**
 * @author amit bhayani
 * 
 */
public class AsTransPendToAct implements TransitionHandler {

	private static final Logger logger = Logger.getLogger(AsTransPendToAct.class);

	private As as = null;
	private FSM fsm;

	/**
	 * 
	 */
	public AsTransPendToAct(As as, FSM fsm) {
		this.as = as;
		this.fsm = fsm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler#process(org
	 * .mobicents.protocols.ss7.m3ua.impl.fsm.State)
	 */
	@Override
	public boolean process(State state) {
		
		// Send the PayloadData (if any) from pending queue to other side
		Asp causeAsp = (Asp) this.fsm.getAttribute(As.ATTRIBUTE_ASP);
		this.as.sendPendingPayloadData(causeAsp);
		
		return true;
	}

}
