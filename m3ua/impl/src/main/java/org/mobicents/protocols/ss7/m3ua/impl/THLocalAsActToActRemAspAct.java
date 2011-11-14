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

package org.mobicents.protocols.ss7.m3ua.impl;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * Called when one of the ASP's of this AS becomes ACTIVE while AS is already
 * ACTIVE
 * 
 * @author amit bhayani
 * 
 */
public class THLocalAsActToActRemAspAct implements TransitionHandler {

	private static final Logger logger = Logger.getLogger(THLocalAsActToActRemAspAct.class);

	private As as = null;
	private FSM fsm;

	public THLocalAsActToActRemAspAct(As as, FSM fsm) {
		this.as = as;
		this.fsm = fsm;
	}

	public boolean process(State state) {
		try {

			if (this.as.getTrafficModeType().getMode() == TrafficModeType.Broadcast) {
				// We don't handle this
				return false;
			}

			Asp remAsp = (Asp) this.fsm.getAttribute(As.ATTRIBUTE_ASP);

			if (this.as.getTrafficModeType().getMode() == TrafficModeType.Loadshare) {
				// Iterate through ASP's and send AS_ACTIVE to ASP's who
				// are INACTIVE
				for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList().tail(); (n = n
						.getNext()) != end;) {
					Asp remAspImpl = n.getValue();

					FSM aspPeerFSM = remAspImpl.getPeerFSM();
					AspState aspState = AspState.getState(aspPeerFSM.getState().getName());

					if (aspState == AspState.INACTIVE) {
						Notify msg = createNotify(remAspImpl, Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
						remAspImpl.getAspFactory().write(msg);
					}
				}
			} else if (this.as.getTrafficModeType().getMode() == TrafficModeType.Override) {
				// Look at 5.2.2. 1+1 Sparing, Backup Override

				for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList().tail(); (n = n
						.getNext()) != end;) {
					Asp remAspImpl = n.getValue();

					FSM aspPeerFSM = remAspImpl.getPeerFSM();
					AspState aspState = AspState.getState(aspPeerFSM.getState().getName());

					// Transition the other ASP to INACTIVE
					if (aspState == AspState.ACTIVE && !(remAspImpl.getName().equals(remAsp.getName()))) {
						Notify msg = createNotify(remAspImpl, Status.STATUS_Other, Status.INFO_Alternate_ASP_Active);
						remAspImpl.getAspFactory().write(msg);

						// Transition this ASP to INACTIVE
						aspPeerFSM.signal(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE);

						break;
					}
				}
			}

			return true;
		} catch (Exception e) {
			logger.error(String.format("Error while translating Rem AS to INACTIVE. %s", this.fsm.toString()), e);
		}
		// something wrong
		return false;
	}

	private Notify createNotify(Asp remAsp, int type, int info) {
		Notify msg = (Notify) this.as.getMessageFactory().createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);

		Status status = this.as.getParameterFactory().createStatus(type, info);
		msg.setStatus(status);

		if (remAsp.getASPIdentifier() != null) {
			msg.setASPIdentifier(remAsp.getASPIdentifier());
		}

		if (this.as.getRoutingContext() != null) {
			msg.setRoutingContext(this.as.getRoutingContext());
		}

		return msg;
	}

}
