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

package org.mobicents.protocols.ss7.sccp.impl;

import org.mobicents.protocols.ss7.sccp.impl.SccpProviderImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpRoutingControl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl.State;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.Router;

/**
 * @author baranowb
 * 
 */
public class SccpStackImplProxy extends SccpStackImpl {

	/**
	 * 
	 */
	public SccpStackImplProxy(String name) {
		super(name);
	}

	public SccpManagementProxy getManagementProxy() {
		return (SccpManagementProxy) super.sccpManagement;
	}

	@Override
	public void start() {
		this.messageFactory = new MessageFactoryImpl(this.getRemoveSpc());

		this.sccpProvider = new SccpProviderImpl(this);

		super.sccpManagement = new SccpManagementProxy(this.getName(), sccpProvider, this);
		super.sccpRoutingControl = new SccpRoutingControl(sccpProvider, this);

		super.sccpManagement.setSccpRoutingControl(sccpRoutingControl);
		super.sccpRoutingControl.setSccpManagement(sccpManagement);

		this.router = new Router(this.getName());
		this.router.setPersistDir(this.getPersistDir());
		this.router.start();

		this.sccpResource = new SccpResource(this.getName());
		this.sccpResource.setPersistDir(this.getPersistDir());
		this.sccpResource.start();

		this.sccpRoutingControl.start();
		this.sccpManagement.start();
		// layer3exec.execute(new MtpStreamHandler());

		this.mtp3UserPart.addMtp3UserPartListener(this);

		this.state = State.RUNNING;
	}
}
