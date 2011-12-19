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

package org.mobicents.protocols.ss7.tcap;

import java.io.IOException;

import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartBaseImpl;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.impl.RemoteSubSystem;
import org.mobicents.protocols.ss7.sccp.impl.SccpResource;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.Router;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

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

	// private ConcurrentLinkedQueue<byte[]> data1 = new
	// ConcurrentLinkedQueue<byte[]>();
	// private ConcurrentLinkedQueue<byte[]> data2 = new
	// ConcurrentLinkedQueue<byte[]>();

	private Mtp3UserPartImpl mtp3UserPart1 = new Mtp3UserPartImpl();
	private Mtp3UserPartImpl mtp3UserPart2 = new Mtp3UserPartImpl();

	protected Router router1;
	protected Router router2;

	protected SccpResource resource1;
	protected SccpResource resource2;

	/**
	 * 
	 */
	public SccpHarness() {
		mtp3UserPart1.setOtherPart(mtp3UserPart2);
		mtp3UserPart2.setOtherPart(mtp3UserPart1);
	}

	protected void createStack1() {
		sccpStack1 = new SccpStackImpl(sccpStack1Name);
		sccpProvider1 = sccpStack1.getSccpProvider();
	}

	private void setUpStack1() {
		createStack1();

		sccpStack1.setLocalSpc(1);
		sccpStack1.setNi(2);
		sccpStack1.setMtp3UserPart(mtp3UserPart1);
		sccpStack1.start();

		router1 = sccpStack1.getRouter();

		resource1 = sccpStack1.getSccpResource();

		resource1.getRemoteSpcs().put(1, new RemoteSignalingPointCode(2, 0, 0));
		resource1.getRemoteSsns().put(1, new RemoteSubSystem(2, 8, 0));

	}

	protected void createStack2() {
		sccpStack2 = new SccpStackImpl(sccpStack2Name);
		sccpProvider2 = sccpStack2.getSccpProvider();
	}

	private void setUpStack2() {
		createStack2();
		
		sccpStack2.setLocalSpc(2);
		sccpStack2.setNi(2);
		sccpStack2.setMtp3UserPart(mtp3UserPart2);
		sccpStack2.start();
		
		router2 = sccpStack2.getRouter();
		
		resource2 = sccpStack2.getSccpResource();

		resource2.getRemoteSpcs().put(1, new RemoteSignalingPointCode(1, 0, 0));
		resource2.getRemoteSsns().put(1, new RemoteSubSystem(1, 8, 0));


	}

	private void tearDownStack1() {
		router1.getRules().clear();
		router1.getPrimaryAddresses().clear();
		router1.getBackupAddresses().clear();

		resource1.getRemoteSpcs().clear();
		resource1.getRemoteSsns().clear();

		sccpStack1.stop();

	}

	private void tearDownStack2() {
		router2.getRules().clear();
		router2.getPrimaryAddresses().clear();
		router2.getBackupAddresses().clear();

		resource2.getRemoteSpcs().clear();
		resource2.getRemoteSsns().clear();

		sccpStack2.stop();

	}

	@BeforeMethod
	public void setUp() throws IllegalStateException {
		this.setUpStack1();
		this.setUpStack2();

	}

	@AfterMethod
	public void tearDown() throws IllegalStateException {
		this.tearDownStack1();
		this.tearDownStack2();
	}

	private class Mtp3UserPartImpl extends Mtp3UserPartBaseImpl {

		// private ConcurrentLinkedQueue<byte[]> readFrom;
		// private ConcurrentLinkedQueue<byte[]> writeTo;

		private Mtp3UserPartImpl otherPart;

		Mtp3UserPartImpl() {
			try {
				this.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void setOtherPart(Mtp3UserPartImpl otherPart) {
			this.otherPart = otherPart;
		}

		@Override
		public void sendMessage(Mtp3TransferPrimitive msg) throws IOException {

			this.otherPart.sendTransferMessageToLocalUser(msg, msg.getSls());
		}
	}

}
