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
package org.mobicents.protocols.ss7.m3ua.impl.sg;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.impl.AsState;
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
 * Transitions the {@link RemAsImpl} from {@link AsState.ACTIVE} to
 * {@link AsState.PENDING}. The transition depends on {@link TrafficModeType}
 * defined for this {@link RemAsImpl} and {@link AspState} of enclosed
 * {@link RemAsImpl}
 * </p>
 * 
 * 
 *   <ul>
 *
 *     <li>
 *     	<p> If the {@link TrafficModeType} for this {@link RemAsImpl} is Loadshare and 
 *     	there are {@link RemAsImpl} that are still {@link AspState.ACTIVE}, {@link RemAsImpl}
 *      remains {@link AsState.ACTIVE}. If number of {@link RemAsImpl} in {@link AspState.ACTIVE}
 *      is greater than minimum Asp required for load balancing, it just returns false and 
 *      transition doesn't happen. However if number of {@link RemAsImpl} in {@link AspState.ACTIVE}
 *      is less than minimum Asp required for load balancing, it sends {@link Status.INFO_Insufficient_ASP_Resources_Active}
 *      {@link Notify} to remote ASP who are {@link AspState.INACTIVE} and transition doesn't happen.
 *    	</p>
 *    
 *    	<p>
 *    		If there are no {@link RemAsImpl} in {@link AspState.ACTIVE}, it transitions to {@link AsState.PENDIING} and
 *    		sends {@link Status.INFO_AS_PENDING} {@link Notify} to remote ASP who are {@link AspState.INACTIVE}.
 *    	</p>
 *     </li>
 *
 *     <li>
 *     	<p> If the {@link TrafficModeType} for this {@link RemAsImpl} is Override 
 *     		it transitions to {@link AsState.PENDIING} and sends {@link Status.INFO_AS_PENDING} {@link Notify} 
 *     		to remote ASP who are {@link AspState.INACTIVE}.
 *    	</p>
 *     </li>
 *
 *   </ul>
 * 
 * @author amit bhayani
 * 
 */
public class RemAsTransActToPendRemAspInac implements TransitionHandler {

	private static final Logger logger = Logger.getLogger(RemAsTransActToPendRemAspInac.class);

	private RemAsImpl as = null;
	private FSM fsm;

	private int lbCount = 0;

	public RemAsTransActToPendRemAspInac(RemAsImpl as, FSM fsm) {
		this.as = as;
		this.fsm = fsm;
	}

	public boolean process(State state) {
		try {
			RemAspImpl remAsp = (RemAspImpl) this.fsm.getAttribute(RemAsImpl.ATTRIBUTE_ASP);

			if (this.as.getTrafficModeType().getMode() == TrafficModeType.Broadcast) {
				// We don't support this
				return false;

			}

			if (this.as.getTrafficModeType().getMode() == TrafficModeType.Loadshare) {
				this.lbCount = 0;

				for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList().tail(); (n = n
						.getNext()) != end;) {
					RemAspImpl remAspImpl = (RemAspImpl) n.getValue();
					if (remAspImpl.getState() == AspState.ACTIVE) {
						this.lbCount++;
					}
				}// for

				if (this.lbCount >= this.as.getMinAspActiveForLb()) {
					// we still have more ASP's ACTIVE for lb. Don't change
					// state
					return false;
				}

				// We are below minAspActiveForLb required for LB
				if (this.lbCount > 0) {
					// But In any case if we have at least one ASP that can take
					// care of traffic, don't change state but send the "Ins.
					// ASPs" to INACTIVE ASP's

					for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList().tail(); (n = n
							.getNext()) != end;) {
						remAsp = (RemAspImpl) n.getValue();
						if (remAsp.getState() == AspState.INACTIVE) {
							Notify notify = this.createNotify(remAsp, Status.STATUS_Other,
									Status.INFO_Insufficient_ASP_Resources_Active);
							remAsp.getAspFactory().write(notify);
						}
					}

					return false;
				}
			}// If Loadshare

			// We have reached here means AS is transitioning to be PENDING.
			// Send new AS STATUS to all INACTIVE APS's

			for (FastList.Node<Asp> n = this.as.getAspList().head(), end = this.as.getAspList().tail(); (n = n
					.getNext()) != end;) {
				remAsp = (RemAspImpl) n.getValue();
				if (remAsp.getState() == AspState.INACTIVE) {
					Notify notify = this.createNotify(remAsp, Status.STATUS_AS_State_Change, Status.INFO_AS_PENDING);
					remAsp.getAspFactory().write(notify);
				}
			}
		} catch (Exception e) {
			logger.error(String.format("Error while translating Rem AS to PENDING. %s", this.fsm.toString()), e);
		}
		return true;
	}

	private Notify createNotify(RemAspImpl remAsp, int type, int info) {
		Notify msg = (Notify) this.as.getM3UAProvider().getMessageFactory()
				.createMessage(MessageClass.MANAGEMENT, MessageType.NOTIFY);

		Status status = this.as.getM3UAProvider().getParameterFactory().createStatus(type, info);
		msg.setStatus(status);

		if (remAsp.getASPIdentifier() != null) {
			msg.setASPIdentifier(remAsp.getASPIdentifier());
		}

		msg.setRoutingContext(this.as.getRoutingContext());

		return msg;
	}

}
