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

package org.mobicents.protocols.ss7.m3ua.impl.sg;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.FSM;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.State;
import org.mobicents.protocols.ss7.m3ua.impl.fsm.TransitionHandler;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.mgmt.Notify;
import org.mobicents.protocols.ss7.m3ua.parameter.Status;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * <p>
 * Transition's the {@link RemAsImpl} from
 * {@link org.mobicents.protocols.ss7.m3ua.impl.AsState#PENDING} to
 * {@link org.mobicents.protocols.ss7.m3ua.impl.AsState#ACTIVE}. Send's
 * AS_ACTIVE notification to all {@link Asp} within this As that are
 * {@link AspState#ACTIVE}
 * </p>
 * 
 * <p>
 * Also initiates sending of PayloadData from the pending queue of this As.
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public class RemAsTransPendToAct implements TransitionHandler {

	private static final Logger logger = Logger.getLogger(RemAsTransPendToAct.class);

	private As as = null;
	private FSM fsm;

	public RemAsTransPendToAct(As as, FSM fsm) {
		this.as = as;
		this.fsm = fsm;
	}

	public boolean process(State state) {

		if (this.as.getTrafficModeType().getMode() == TrafficModeType.Broadcast) {
			// We don't handle this
			return false;
		}

		try {
			// Iterate through ASP's and send AS_ACTIVE to ASP's who
			// are INACTIVE or ACTIVE
			for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList().tail(); (n = n
					.getNext()) != end;) {
				Asp remAspImpl = n.getValue();

				if (remAspImpl.getState() == AspState.INACTIVE || remAspImpl.getState() == AspState.ACTIVE) {
					Notify msg = createNotify(remAspImpl);
					remAspImpl.getAspFactory().write(msg);
				}
			}// end of for

			// Send the PayloadData (if any) from pending queue to other side
			Asp causeAsp = (Asp) this.fsm.getAttribute(As.ATTRIBUTE_ASP);
			this.as.sendPendingPayloadData(causeAsp);

			return true;
		} catch (Exception e) {
			logger.error(String.format("Error while translating Rem AS to INACTIVE message. %s", this.fsm.toString()),
					e);
		}
		return false;
	}

	private Notify createNotify(Asp remAsp) {
		Notify msg = (Notify) this.as.getMessageFactory().createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);

		Status status = this.as.getParameterFactory()
				.createStatus(Status.STATUS_AS_State_Change, Status.INFO_AS_ACTIVE);
		msg.setStatus(status);

		if (remAsp.getASPIdentifier() != null) {
			msg.setASPIdentifier(remAsp.getASPIdentifier());
		}

		msg.setRoutingContext(this.as.getRoutingContext());

		return msg;
	}

}
