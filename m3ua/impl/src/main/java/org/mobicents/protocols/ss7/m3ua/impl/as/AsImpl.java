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

import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AsState;
import org.mobicents.protocols.ss7.m3ua.impl.TransitionState;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * @author amit bhayani
 */
public class AsImpl extends As {

	public AsImpl(String name, RoutingContext rc, RoutingKey rk, TrafficModeType trMode, M3UAProvider provider) {
		super(name, rc, rk, trMode, provider);

		// Define states
		fsm.createState(AsState.DOWN.toString());
		fsm.createState(AsState.ACTIVE.toString());
		fsm.createState(AsState.INACTIVE.toString());
		fsm.createState(AsState.PENDING.toString()).setOnTimeOut(new AsStatePenTimeout(this, this.fsm), 2000)
				.setOnEnter(new AsStateEnterPen(this, this.fsm));

		fsm.setStart(AsState.DOWN.toString());
		fsm.setEnd(AsState.DOWN.toString());
		// Define Transitions

		// ******************************************************************/
		// STATE DOWN /
		// ******************************************************************/
		fsm.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.DOWN.toString(),
				AsState.INACTIVE.toString());

		// ******************************************************************/
		// STATE INACTIVE /
		// ******************************************************************/
		fsm.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.INACTIVE.toString(),
				AsState.INACTIVE.toString());

		fsm.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.INACTIVE.toString(),
				AsState.ACTIVE.toString());

		// ******************************************************************/
		// STATE ACTIVE /
		// ******************************************************************/
		fsm.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.ACTIVE.toString(),
				AsState.ACTIVE.toString());

		fsm.createTransition(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE, AsState.ACTIVE.toString(),
				AsState.ACTIVE.toString()).setHandler(new AsTransActToActNtfyAltAspAct(this, this.fsm));

		fsm.createTransition(TransitionState.OTHER_INSUFFICIENT_ASP, AsState.ACTIVE.toString(),
				AsState.ACTIVE.toString()).setHandler(new AsTransActToActNtfyInsAsp(this, this.fsm));

		fsm.createTransition(TransitionState.AS_STATE_CHANGE_PENDING, AsState.ACTIVE.toString(),
				AsState.PENDING.toString());

		fsm.createTransition(TransitionState.ASP_DOWN, AsState.ACTIVE.toString(), AsState.PENDING.toString())
				.setHandler(new AsTransActToPen(this, this.fsm));

		// ******************************************************************/
		// STATE PENDING /
		// ******************************************************************/
		// As transitions to DOWN from PENDING when Pending Timer timesout
		fsm.createTransition(TransitionState.AS_DOWN, AsState.PENDING.toString(), AsState.DOWN.toString());

		// As transitions to INACTIVE from PENDING when Pending Timer timesout
		fsm.createTransition(TransitionState.AS_INACTIVE, AsState.PENDING.toString(), AsState.INACTIVE.toString());

		// If in PENDING and one of the ASP is ACTIVE again
		fsm.createTransition(TransitionState.AS_STATE_CHANGE_ACTIVE, AsState.PENDING.toString(),
				AsState.ACTIVE.toString()).setHandler(new AsTransPendToAct(this, this.fsm));

		// If As is PENDING and far end sends INACTIVE we still remain PENDING
		// as that message from pending queue can be sent once As becomes ACTIVE
		// before T(r) expires
		fsm.createTransition(TransitionState.AS_STATE_CHANGE_INACTIVE, AsState.PENDING.toString(),
				AsState.INACTIVE.toString()).setHandler(new AsNoTrans());

	}
}
