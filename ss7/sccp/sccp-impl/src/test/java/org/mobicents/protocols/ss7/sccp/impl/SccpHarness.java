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

import java.util.concurrent.ConcurrentLinkedQueue;

import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.router.Router;

/**
 * @author amit bhayani
 * 
 */
public abstract class SccpHarness {

	protected SccpStackImpl sccpStack1;
	protected SccpProvider sccpProvider1;

	protected SccpStackImpl sccpStack2;
	protected SccpProvider sccpProvider2;

	protected ConcurrentLinkedQueue<byte[]> data1 = new ConcurrentLinkedQueue<byte[]>();
	protected ConcurrentLinkedQueue<byte[]> data2 = new ConcurrentLinkedQueue<byte[]>();

	protected Mtp3UserPart mtp3UserPart1 = new Mtp3UserPartImpl(data1, data2);
	protected Mtp3UserPart mtp3UserPart2 = new Mtp3UserPartImpl(data2, data1);

	protected Router router1 = new Router();
	protected Router router2 = new Router();

	protected SccpResource resource1 = new SccpResource();
	protected SccpResource resource2 = new SccpResource();

	/**
	 * 
	 */
	public SccpHarness() {
		// TODO Auto-generated constructor stub
	}

	protected void createStack1()
	{
		sccpStack1 = new SccpStackImpl();
		sccpProvider1 = sccpStack1.getSccpProvider();
	}
	protected void setUpStack1() {
		createStack1();
		try {
			router1.start();
			router1.getRules().clear();
			router1.getPrimaryAddresses().clear();
			router1.getBackupAddresses().clear();
		} catch (Exception e) {
			// ignore
		}

		resource1.start();
		resource1.getRemoteSpcs().clear();
		resource1.getRemoteSsns().clear();

		resource1.getRemoteSpcs().put(1111333, new RemoteSignalingPointCode(getStack2PC(), 0, 0));
		resource1.getRemoteSsns().put(getSSN(), new RemoteSubSystem(getStack2PC(), getSSN(), 0));

		sccpStack1.setRouter(router1);
		sccpStack1.setSccpResource(resource1);
		sccpStack1.setLocalSpc(getStack1PC());
		sccpStack1.setNi(2);
		sccpStack1.setMtp3UserPart(mtp3UserPart1);
		sccpStack1.start();
	}
	protected void createStack2()
	{
		sccpStack2 = new SccpStackImpl();
		sccpProvider2 = sccpStack2.getSccpProvider();
	}
	protected void setUpStack2() {
		createStack2();
		try {
			router2.start();
			router2.getRules().clear();
			router2.getPrimaryAddresses().clear();
			router2.getBackupAddresses().clear();
		} catch (Exception e) {
			// ignore
		}

		resource2.start();
		resource2.getRemoteSpcs().clear();
		resource2.getRemoteSsns().clear();

		resource2.getRemoteSpcs().put(02, new RemoteSignalingPointCode(getStack1PC(), 0, 0));
		resource2.getRemoteSsns().put(getSSN(), new RemoteSubSystem(getStack1PC(), getSSN(), 0));

		sccpStack2.setRouter(router2);
		sccpStack2.setSccpResource(resource2);
		sccpStack2.setLocalSpc(getStack2PC());
		sccpStack2.setNi(2);
		sccpStack2.setMtp3UserPart(mtp3UserPart2);
		sccpStack2.start();
	}

	private void tearDownStack1() {
		router1.getRules().clear();
		router1.getPrimaryAddresses().clear();
		router1.getBackupAddresses().clear();
		router1.stop();

		resource1.getRemoteSpcs().clear();
		resource1.getRemoteSsns().clear();
		resource1.stop();

		sccpStack1.stop();

	}

	private void tearDownStack2() {
		router1.getRules().clear();
		router1.getPrimaryAddresses().clear();
		router1.getBackupAddresses().clear();
		router1.stop();

		resource1.getRemoteSpcs().clear();
		resource1.getRemoteSsns().clear();
		resource1.stop();

		sccpStack1.stop();

	}
	
	protected int getStack1PC()
	{return 1;}
	
	protected int getStack2PC()
	{return 2;}
	
	protected int getSSN()
	{return 8;}
	
	public void setUp() throws IllegalStateException {
		this.setUpStack1();
		this.setUpStack2();
	}

	public void tearDown() {
		this.tearDownStack1();
		this.tearDownStack2();
	}


}
