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

package org.mobicents.protocols.ss7.m3ua.impl;

import static org.junit.Assert.assertEquals;
import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.sctp.ManagementImpl;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;

/**
 * 
 * @author amit bhayani
 * 
 */
public class GatewayTest {

	private static final Logger logger = Logger.getLogger(GatewayTest.class);

	private static final String SERVER_NAME = "testserver";
	private static final String SERVER_HOST = "127.0.0.1";
	private static final int SERVER_PORT = 2345;

	private static final String SERVER_ASSOCIATION_NAME = "serverAsscoiation";
	private static final String CLIENT_ASSOCIATION_NAME = "clientAsscoiation";

	private static final String CLIENT_HOST = "127.0.0.1";
	private static final int CLIENT_PORT = 2346;

	private Management sctpManagement = null;
	private M3UAManagement m3uaMgmt = null;
	private ParameterFactoryImpl factory = new ParameterFactoryImpl();

	private As remAs;
	private Asp remAsp;
	private AspFactory remAspFactory;

	private As localAs;
	private Asp localAsp;
	private AspFactory localAspFactory;

	private Server server;
	private Client client;

	private Mtp3UserPartListenerImpl mtp3UserPartListener = null;

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		mtp3UserPartListener = new Mtp3UserPartListenerImpl();

		client = new Client();
		server = new Server();

		this.sctpManagement = new ManagementImpl("GatewayTest");
		this.sctpManagement.setSingleThread(true);
		this.sctpManagement.setConnectDelay(1000 * 5);// setting connection
														// delay to 5 secs
		this.sctpManagement.start();

		this.m3uaMgmt = new M3UAManagement("GatewayTest");
		this.m3uaMgmt.setTransportManagement(this.sctpManagement);
		this.m3uaMgmt.addMtp3UserPartListener(mtp3UserPartListener);
		this.m3uaMgmt.start();

	}

	@After
	public void tearDown() throws Exception {

		this.sctpManagement.stop();
		this.m3uaMgmt.stop();
	}

	@Test
	public void testSingleAspInAs() throws Exception {
		// 5.1.1. Single ASP in an Application Server ("1+0" sparing),

		System.out.println("Starting server");
		server.start();
		Thread.sleep(100);

		System.out.println("Starting Client");
		client.start();

		Thread.sleep(10000);

		// Both AS and ASP should be ACTIVE now
		assertEquals(AspState.ACTIVE, AspState.getState(remAsp.getPeerFSM().getState().getName()));
		assertEquals(AsState.ACTIVE, AsState.getState(remAs.getLocalFSM().getState().getName()));

		assertEquals(AspState.ACTIVE, AspState.getState(localAsp.getLocalFSM().getState().getName()));
		assertEquals(AsState.ACTIVE, AsState.getState(localAs.getPeerFSM().getState().getName()));

		client.sendPayload();
		server.sendPayload();

		Thread.sleep(1000);

		client.stop();
		logger.debug("Stopped Client");
		// Give time to exchnge ASP_DOWN messages
		Thread.sleep(100);

		// The AS is Pending
		assertEquals(AsState.PENDING, AsState.getState(localAs.getPeerFSM().getState().getName()));
		assertEquals(AsState.PENDING, AsState.getState(remAs.getLocalFSM().getState().getName()));

		// Let the AS go in DOWN state
		Thread.sleep(4000);
		logger.debug("Woke from 4000 sleep");

		// The AS is Pending
		assertEquals(AsState.DOWN, AsState.getState(localAs.getPeerFSM().getState().getName()));
		assertEquals(AsState.DOWN, AsState.getState(remAs.getLocalFSM().getState().getName()));

		client.stopClient();
		server.stop();

		Thread.sleep(100);

		// we should receive two MTP3 data
		assertEquals(2, mtp3UserPartListener.getReceivedData().size());

	}

	private class Client {

		public Client() {
		}

		public void start() throws Exception {

			// 1. Create SCTP Association
			sctpManagement.addAssociation(CLIENT_HOST, CLIENT_PORT, SERVER_HOST, SERVER_PORT, CLIENT_ASSOCIATION_NAME);

			// 2. Create AS
			// m3ua as create rc <rc> <ras-name>
			RoutingContext rc = factory.createRoutingContext(new long[] { 100l });
			TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
			localAs = m3uaMgmt.createAs("client-testas", Functionality.AS, ExchangeType.SE, IPSPType.CLIENT, rc,
					trafficModeType);

			// 3. Create ASP
			// m3ua asp create ip <local-ip> port <local-port> remip <remip>
			// remport <remport> <asp-name>
			localAspFactory = m3uaMgmt.createAspFactory("client-testasp", CLIENT_ASSOCIATION_NAME);

			// 4. Assign ASP to AS
			localAsp = m3uaMgmt.assignAspToAs("client-testas", "client-testasp");

			// 5. Define Route
			// Define Route
			m3uaMgmt.addRoute(1408, -1, -1, "client-testas");

			// 6. Start ASP
			m3uaMgmt.startAsp("client-testasp");

		}

		public void stop() throws Exception {
			// 1. stop ASP
			m3uaMgmt.stopAsp("client-testasp");

		}

		public void stopClient() throws Exception {

			// 2.Remove route
			m3uaMgmt.removeRoute(1408, -1, -1, "client-testas");

			// 3. Unassign ASP from AS
			// clientM3UAMgmt.
			m3uaMgmt.unassignAspFromAs("client-testas", "client-testasp");

			// 4. destroy aspFactory
			m3uaMgmt.destroyAspFactory("client-testasp");

			// 5. Destroy As
			m3uaMgmt.destroyAs("client-testas");

			// 6. remove sctp
			sctpManagement.removeAssociation(CLIENT_ASSOCIATION_NAME);
		}

		public void sendPayload() throws Exception {
			Mtp3TransferPrimitive mtp3TransferPrimitive = new Mtp3TransferPrimitive(3, 1, 0, 123, 1408, 1, new byte[] {
					1, 2, 3, 4 });
			m3uaMgmt.sendMessage(mtp3TransferPrimitive);
		}
	}

	private class Server {

		public Server() {

		}

		private void start() throws Exception {

			// 1. Create SCTP Server
			sctpManagement.addServer(SERVER_NAME, SERVER_HOST, SERVER_PORT);

			// 2. Create SCTP Server Association
			sctpManagement.addServerAssociation(CLIENT_HOST, CLIENT_PORT, SERVER_NAME, SERVER_ASSOCIATION_NAME);

			// 3. Start Server
			sctpManagement.startServer(SERVER_NAME);

			// 4. Create RAS
			// m3ua ras create rc <rc> rk dpc <dpc> opc <opc-list> si <si-list>
			// traffic-mode {broadcast|loadshare|override} <ras-name>
			RoutingContext rc = factory.createRoutingContext(new long[] { 100l });
			TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
			remAs = m3uaMgmt.createAs("server-testas", Functionality.SGW, ExchangeType.SE, IPSPType.CLIENT, rc,
					trafficModeType);

			// 5. Create RASP
			// m3ua rasp create <asp-name> <assoc-name>"
			remAspFactory = m3uaMgmt.createAspFactory("server-testasp", SERVER_ASSOCIATION_NAME);

			// 6. Assign ASP to AS
			remAsp = m3uaMgmt.assignAspToAs("server-testas", "server-testasp");

			// 5. Define Route
			// Define Route
			m3uaMgmt.addRoute(123, -1, -1, "server-testas");

			// 7. Start ASP
			m3uaMgmt.startAsp("server-testasp");

		}

		public void stop() throws Exception {
			m3uaMgmt.stopAsp("server-testasp");
			
			// 2.Remove route
			m3uaMgmt.removeRoute(123, -1, -1, "server-testas");

			m3uaMgmt.unassignAspFromAs("server-testas", "server-testasp");

			// 4. destroy aspFactory
			m3uaMgmt.destroyAspFactory("server-testasp");

			// 5. Destroy As
			m3uaMgmt.destroyAs("server-testas");

			sctpManagement.removeAssociation(SERVER_ASSOCIATION_NAME);

			sctpManagement.stopServer(SERVER_NAME);
			sctpManagement.removeServer(SERVER_NAME);
		}

		public void sendPayload() throws Exception {
			Mtp3TransferPrimitive mtp3TransferPrimitive = new Mtp3TransferPrimitive(3, 1, 0, 1408, 123, 1, new byte[] {
					1, 2, 3, 4 });
			m3uaMgmt.sendMessage(mtp3TransferPrimitive);
		}

	}

	private class Mtp3UserPartListenerImpl implements Mtp3UserPartListener {

		private FastList<Mtp3TransferPrimitive> receivedData = new FastList<Mtp3TransferPrimitive>();

		public FastList<Mtp3TransferPrimitive> getReceivedData() {
			return receivedData;
		}

		@Override
		public void onMtp3PauseMessage(Mtp3PausePrimitive arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMtp3ResumeMessage(Mtp3ResumePrimitive arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMtp3StatusMessage(Mtp3StatusPrimitive arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMtp3TransferMessage(Mtp3TransferPrimitive value) {
			receivedData.add(value);
		}

	}
}
