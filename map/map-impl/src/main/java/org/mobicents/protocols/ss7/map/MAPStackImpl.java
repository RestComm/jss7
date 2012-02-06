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

package org.mobicents.protocols.ss7.map;

import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;
import org.mobicents.ss7.congestion.CongestionListener;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPStackImpl implements MAPStack, CongestionListener {

	protected TCAPStack tcapStack = null;

	protected MAPProviderImpl mapProvider = null;

	private State state = State.IDLE;
	
	public MAPStackImpl(SccpProvider sccpPprovider, int ssn, int maxDialogs) {
		this.tcapStack = new TCAPStackImpl(sccpPprovider, ssn, maxDialogs);
		TCAPProvider tcapProvider = tcapStack.getProvider();
		mapProvider = new MAPProviderImpl(tcapProvider);

		this.state = State.CONFIGURED;
	}

	public MAPStackImpl(SccpProvider sccpPprovider, int ssn) {
		this(sccpPprovider, ssn, 5000);
	}

	public MAPStackImpl(TCAPProvider tcapProvider) {

		mapProvider = new MAPProviderImpl(tcapProvider);
		this.state = State.CONFIGURED;
	}

	public MAPProvider getMAPProvider() {
		return this.mapProvider;
	}

	public void start() throws IllegalStateException {
		if (state != State.CONFIGURED) {
			throw new IllegalStateException("Stack has not been configured or is already running!");
		}
		if (tcapStack != null) {
			// this is null in junits!
			this.tcapStack.start();
		}
		this.mapProvider.start();

		this.state = State.RUNNING;

	}

	public void stop() {
		if (state != State.RUNNING) {
			throw new IllegalStateException("Stack is not running!");
		}
		this.mapProvider.stop();
		if (tcapStack != null) {
			this.tcapStack.stop();
		}

		this.state = State.CONFIGURED;
	}

	@Override
	public TCAPStack getTCAPStack() {
		return this.tcapStack;
	}

	// // ///////////////
	// // CONF METHOD //
	// // ///////////////
	// /**
	// * @throws ConfigurationException
	// *
	// */
	// public void configure(Properties props) throws ConfigurationException {
	// if (state != State.IDLE) {
	// throw new
	// IllegalStateException("Stack already been configured or is already running!");
	// }
	// tcapStack.configure(props);
	// TCAPProvider tcapProvider = tcapStack.getProvider();
	// mapProvider = new MAPProviderImpl(tcapProvider);
	// this.state = State.CONFIGURED;
	// }

	private enum State {
		IDLE, CONFIGURED, RUNNING;
	}

	@Override
	public void onCongestionStart(String congName) {
		this.mapProvider.onCongestionStart(congName);
	}

	@Override
	public void onCongestionFinish(String congName) {
		this.mapProvider.onCongestionFinish(congName);
	}
}
