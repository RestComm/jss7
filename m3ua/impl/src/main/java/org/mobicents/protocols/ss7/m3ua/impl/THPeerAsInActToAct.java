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

import javolution.util.FastSet;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
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
	public boolean process(State state) {
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
