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

import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.impl.As;
import org.mobicents.protocols.ss7.m3ua.impl.AsState;
import org.mobicents.protocols.ss7.m3ua.impl.TransitionState;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * 
 * @author amit bhayani
 */
public class RemAsImpl extends As {

	public RemAsImpl(String name, RoutingContext rc, RoutingKey rk, TrafficModeType trMode, M3UAProvider provider) {
		super(name, rc, rk, trMode, provider);

		// Define states
		fsm.createState(AsState.DOWN.toString());
		fsm.createState(AsState.ACTIVE.toString());
		fsm.createState(AsState.INACTIVE.toString());
		fsm.createState(AsState.PENDING.toString()).setOnTimeOut(new RemAsStatePenTimeout(this, this.fsm), 2000);

		fsm.setStart(AsState.DOWN.toString());
		fsm.setEnd(AsState.DOWN.toString());
		// Define Transitions

		// ******************************************************************/
		// STATE DOWN /
		// ******************************************************************/
		fsm.createTransition(TransitionState.ASP_UP, AsState.DOWN.toString(), AsState.INACTIVE.toString()).setHandler(
				new RemAsTransDwnToInact(this, this.fsm));

		// ******************************************************************/
		// STATE INACTIVE /
		// ******************************************************************/
		// TODO : Add Pluggable policy for AS?
		fsm.createTransition(TransitionState.ASP_ACTIVE, AsState.INACTIVE.toString(), AsState.ACTIVE.toString())
				.setHandler(new RemAsTransInactToAct(this, this.fsm));

		fsm.createTransition(TransitionState.ASP_DOWN, AsState.INACTIVE.toString(), AsState.DOWN.toString())
				.setHandler(new RemAsTransInactToDwn(this, this.fsm));

		fsm.createTransition(TransitionState.ASP_UP, AsState.INACTIVE.toString(), AsState.INACTIVE.toString())
				.setHandler(new RemAsTransInactToInact(this, this.fsm));

		// ******************************************************************/
		// STATE ACTIVE /
		// ******************************************************************/
		fsm.createTransition(TransitionState.ASP_INACTIVE, AsState.ACTIVE.toString(), AsState.PENDING.toString())
				.setHandler(new RemAsTransActToPendRemAspInac(this, this.fsm));

		fsm.createTransition(TransitionState.ASP_DOWN, AsState.ACTIVE.toString(), AsState.PENDING.toString())
				.setHandler(new RemAsTransActToPendRemAspDwn(this, this.fsm));

		fsm.createTransition(TransitionState.ASP_ACTIVE, AsState.ACTIVE.toString(), AsState.ACTIVE.toString())
				.setHandler(new RemAsTransActToActRemAspAct(this, this.fsm));

		fsm.createTransition(TransitionState.ASP_UP, AsState.ACTIVE.toString(), AsState.PENDING.toString()).setHandler(
				new RemAsTransActToPendRemAspInac(this, this.fsm));

		// ******************************************************************/
		// STATE PENDING /
		// ******************************************************************/
		fsm.createTransition(TransitionState.ASP_DOWN, AsState.PENDING.toString(), AsState.DOWN.toString()).setHandler(
				new RemAsNoTrans());

		fsm.createTransition(TransitionState.ASP_UP, AsState.PENDING.toString(), AsState.INACTIVE.toString())
				.setHandler(new RemAsNoTrans());

		fsm.createTransition(TransitionState.ASP_ACTIVE, AsState.PENDING.toString(), AsState.ACTIVE.toString())
				.setHandler(new RemAsTransPendToAct(this, this.fsm));

		fsm.createTransition(TransitionState.AS_DOWN, AsState.PENDING.toString(), AsState.DOWN.toString());
		fsm.createTransition(TransitionState.AS_INACTIVE, AsState.PENDING.toString(), AsState.INACTIVE.toString());
	}

}
