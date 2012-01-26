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

import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.router.Router;

/**
 * @author amit bhayani
 * 
 */
public abstract class SccpHarness {
	
	protected String sccpStack1Name = null;
	protected String sccpStack2Name = null;

	protected SccpStackImpl sccpStack1;
	protected SccpProvider sccpProvider1;

	protected SccpStackImpl sccpStack2;
	protected SccpProvider sccpProvider2;

//	protected ConcurrentLinkedQueue<byte[]> data1 = new ConcurrentLinkedQueue<byte[]>();
//	protected ConcurrentLinkedQueue<byte[]> data2 = new ConcurrentLinkedQueue<byte[]>();

	protected Mtp3UserPartImpl mtp3UserPart1 = new Mtp3UserPartImpl();
	protected Mtp3UserPartImpl mtp3UserPart2 = new Mtp3UserPartImpl();

	protected Router router1 = null;
	protected Router router2 = null;

	protected SccpResource resource1 = null;
	protected SccpResource resource2 = null;

	/**
	 * 
	 */
	public SccpHarness() {
		mtp3UserPart1.setOtherPart(mtp3UserPart2);
		mtp3UserPart2.setOtherPart(mtp3UserPart1);
	}

	protected void createStack1()
	{
		sccpStack1 = new SccpStackImpl(sccpStack1Name);
	}
	protected void setUpStack1() {
		createStack1();
		
		sccpStack1.setLocalSpc(getStack1PC());
		sccpStack1.setNi(2);
		sccpStack1.setMtp3UserPart(mtp3UserPart1);
		sccpStack1.start();
		
		sccpProvider1 = sccpStack1.getSccpProvider();
		
		router1 = sccpStack1.getRouter();

		resource1 = sccpStack1.getSccpResource();

		resource1.getRemoteSpcs().put(1111333, new RemoteSignalingPointCode(getStack2PC(), 0, 0));
		resource1.getRemoteSsns().put(getSSN(), new RemoteSubSystem(getStack2PC(), getSSN(), 0));
		
	}
	protected void createStack2()
	{
		sccpStack2 = new SccpStackImpl(sccpStack2Name);
	}
	protected void setUpStack2() {
		createStack2();
		
		sccpStack2.setLocalSpc(getStack2PC());
		sccpStack2.setNi(2);
		sccpStack2.setMtp3UserPart(mtp3UserPart2);
		sccpStack2.start();
		
		sccpProvider2 = sccpStack2.getSccpProvider();
		
		router2 = sccpStack2.getRouter();
		router2.getRules().clear();
		router2.getPrimaryAddresses().clear();
		router2.getBackupAddresses().clear();

		resource2 = sccpStack2.getSccpResource();
		resource2.getRemoteSpcs().clear();
		resource2.getRemoteSsns().clear();

		resource2.getRemoteSpcs().put(02, new RemoteSignalingPointCode(getStack1PC(), 0, 0));
		resource2.getRemoteSsns().put(getSSN(), new RemoteSubSystem(getStack1PC(), getSSN(), 0));

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
		router2.getRules().clear();
		router2.getPrimaryAddresses().clear();
		router2.getBackupAddresses().clear();
		router2.stop();

		resource2.getRemoteSpcs().clear();
		resource2.getRemoteSsns().clear();
		resource2.stop();

		sccpStack2.stop();

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
