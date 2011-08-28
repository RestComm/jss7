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

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.m3ua.M3UAProvider;
import org.mobicents.protocols.ss7.m3ua.impl.Asp;
import org.mobicents.protocols.ss7.m3ua.impl.AspFactory;
import org.mobicents.protocols.ss7.m3ua.impl.AspState;
import org.mobicents.protocols.ss7.m3ua.impl.TransitionState;

/**
 * @author amit bhayani
 */
public class AspImpl extends Asp {

	public AspImpl() {

	}

	public AspImpl(String name, M3UAProvider m3UAProvider, AspFactory aspFactory) {
		super(name, m3UAProvider, aspFactory);

		// Define states
		fsm.createState(AspState.DOWN_SENT.toString());
		fsm.createState(AspState.DOWN.toString());
		fsm.createState(AspState.UP_SENT.toString());
		fsm.createState(AspState.INACTIVE.toString());
		fsm.createState(AspState.ACTIVE_SENT.toString());
		fsm.createState(AspState.ACTIVE.toString());
		fsm.createState(AspState.INACTIVE_SENT.toString());

		fsm.setStart(AspState.DOWN.toString());
		fsm.setEnd(AspState.DOWN.toString());

		// Define Transitions

		// ******************************************************************/
		// DOWN /
		// ******************************************************************/
		fsm.createTransition(TransitionState.COMM_UP, AspState.DOWN.toString(), AspState.UP_SENT.toString());
		// .setHandler(new AspTransDwnToAspUpSnt(this, this.fsm));
		
		fsm.createTransition(TransitionState.COMM_DOWN, AspState.DOWN.toString(), AspState.DOWN.toString());

		// ******************************************************************/
		// UP_SENT/
		// ******************************************************************/
		// TODO Keep sending ASP_UP. Also wrong logic here, every ASP will send
		// ASP_UP ?
		fsm.createTimeoutTransition(AspState.UP_SENT.toString(), AspState.UP_SENT.toString(), 2000).setHandler(
				new AspTransDwnToAspUpSnt(this, this.fsm));

		fsm.createTransition(TransitionState.ASP_INACTIVE, AspState.UP_SENT.toString(), AspState.INACTIVE.toString());

		fsm.createTransition(TransitionState.ASP_ACTIVE_SENT, AspState.UP_SENT.toString(),
				AspState.ACTIVE_SENT.toString());

		fsm.createTransition(TransitionState.ASP_DOWN_SENT, AspState.UP_SENT.toString(), AspState.DOWN_SENT.toString());

		fsm.createTransition(TransitionState.COMM_DOWN, AspState.UP_SENT.toString(), AspState.DOWN.toString());

		// ******************************************************************/
		// ACTIVE_SENT/
		// ******************************************************************/
		// TODO Keep sending ASP_ACTIVE ?
		fsm.createTimeoutTransition(AspState.ACTIVE_SENT.toString(), AspState.ACTIVE_SENT.toString(), 2000);

		fsm.createTransition(TransitionState.ASP_ACTIVE_ACK, AspState.ACTIVE_SENT.toString(),
				AspState.ACTIVE.toString());
		// .setHandler(new AspTransActSntToAct(this, this.fsm));

		fsm.createTransition(TransitionState.ASP_DOWN_SENT, AspState.ACTIVE_SENT.toString(),
				AspState.DOWN_SENT.toString());

		fsm.createTransition(TransitionState.COMM_DOWN, AspState.ACTIVE_SENT.toString(), AspState.DOWN.toString());

		// ******************************************************************/
		// ACTIVE/
		// ******************************************************************/
		fsm.createTransition(TransitionState.ASP_INACTIVE_SENT, AspState.ACTIVE.toString(),
				AspState.INACTIVE_SENT.toString());
		// .setHandler(new AspTransActToInactSnt(this, this.fsm));

		fsm.createTransition(TransitionState.OTHER_ALTERNATE_ASP_ACTIVE, AspState.ACTIVE.toString(),
				AspState.INACTIVE.toString());

		fsm.createTransition(TransitionState.ASP_DOWN_SENT, AspState.ACTIVE.toString(), AspState.DOWN_SENT.toString());

		fsm.createTransition(TransitionState.COMM_DOWN, AspState.ACTIVE.toString(), AspState.DOWN.toString());
		// .setHandler(new AspTransActToDwn(this, this.fsm));

		// ******************************************************************/
		// INACTIVE/
		// ******************************************************************/
		fsm.createTransition(TransitionState.COMM_DOWN, AspState.INACTIVE.toString(), AspState.DOWN.toString());
		// .setHandler(new AspTransInactToDwn(this, this.fsm));

		fsm.createTransition(TransitionState.ASP_ACTIVE_SENT, AspState.INACTIVE.toString(),
				AspState.ACTIVE_SENT.toString());

		fsm.createTransition(TransitionState.ASP_DOWN_SENT, AspState.INACTIVE.toString(), AspState.DOWN_SENT.toString());

		// ******************************************************************/
		// INACTIVE_SENT/
		// ******************************************************************/
		// TODO keep sending INACTIVE ASP ?
		fsm.createTimeoutTransition(AspState.INACTIVE_SENT.toString(), AspState.INACTIVE_SENT.toString(), 2000);
		// TODO Take care of this .setHandler(new AspTransActToInactSnt(this,
		// this.fsm));

		fsm.createTransition(TransitionState.ASP_INACTIVE_ACK, AspState.INACTIVE_SENT.toString(),
				AspState.INACTIVE.toString());

		fsm.createTransition(TransitionState.ASP_DOWN_SENT, AspState.INACTIVE_SENT.toString(),
				AspState.DOWN_SENT.toString());

		fsm.createTransition(TransitionState.COMM_DOWN, AspState.INACTIVE_SENT.toString(), AspState.DOWN.toString());

		// ******************************************************************/
		// DOWN_SENT/
		// ******************************************************************/
		fsm.createTransition(TransitionState.ASP_DOWN_ACK, AspState.DOWN_SENT.toString(), AspState.DOWN.toString());

		fsm.createTransition(TransitionState.COMM_DOWN, AspState.DOWN_SENT.toString(), AspState.DOWN.toString());

	}

	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<AspImpl> ASP_IMPL_XML = new XMLFormat<AspImpl>(AspImpl.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, AspImpl aspImpl) throws XMLStreamException {
			ASP_XML.read(xml, aspImpl);
		}

		@Override
		public void write(AspImpl aspImpl, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
			ASP_XML.write(aspImpl, xml);
		}
	};
}
