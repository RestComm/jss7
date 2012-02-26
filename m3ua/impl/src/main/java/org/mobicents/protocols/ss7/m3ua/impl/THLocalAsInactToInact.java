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

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;

/**
 * 
 * @author amit bhayani
 * 
 */
public class THLocalAsInactToInact implements TransitionHandler {

	private static final Logger logger = Logger.getLogger(THLocalAsInactToInact.class);

	private As as = null;
	private FSM fsm;

	public THLocalAsInactToInact(As as, FSM fsm) {
		this.as = as;
		this.fsm = fsm;
	}

	public boolean process(State state) {
		try {
			if (as.getFunctionality() != Functionality.IPSP) {
				//Send Notify only for ASP or SGW
				Asp remAsp = (Asp) this.fsm.getAttribute(As.ATTRIBUTE_ASP);

				if (remAsp == null) {
					logger.error(String.format("No ASP found. %s", this.fsm.toString()));
					return false;
				}

				Notify msg = createNotify(remAsp);
				remAsp.getAspFactory().write(msg);
			}
			return true;
		} catch (Exception e) {
			logger.error(String.format("Error while translating Rem AS to INACTIVE message. %s", this.fsm.toString()),
					e);
		}
		return false;
	}

	private Notify createNotify(Asp remAsp) {
		Notify msg = (Notify) this.as.getMessageFactory().createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);

		Status status = this.as.getParameterFactory().createStatus(Status.STATUS_AS_State_Change,
				Status.INFO_AS_INACTIVE);
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
