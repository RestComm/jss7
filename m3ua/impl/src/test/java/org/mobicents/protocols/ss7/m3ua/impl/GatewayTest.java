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

import java.io.IOException;

import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.sctp.Management;
import org.mobicents.protocols.ss7.m3ua.impl.as.ClientM3UAManagement;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.sg.ServerM3UAManagement;
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
	private ServerM3UAManagement serverM3UAMgmt = null;
	private ClientM3UAManagement clientM3UAMgmt = null;

	private ParameterFactoryImpl parmFactory = new ParameterFactoryImpl();

	private As remAs;
	private Asp remAsp;
	private AspFactory remAspFactory;

	private As localAs;
	private Asp localAsp;
	private AspFactory localAspFactory;

	private Server server;
	private Client client;

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		client = new Client();
		server = new Server();

		this.sctpManagement = new Management("server-management");
		this.sctpManagement.setSingleThread(true);
		this.sctpManagement.setConnectDelay(1000 * 5);// setting connection
														// delay to 5 secs
		this.sctpManagement.start();

		this.serverM3UAMgmt = new ServerM3UAManagement();
		this.serverM3UAMgmt.setSctpManagement(this.sctpManagement);
		this.serverM3UAMgmt.addMtp3UserPartListener(server);
		this.serverM3UAMgmt.start();

		this.clientM3UAMgmt = new ClientM3UAManagement();
		this.clientM3UAMgmt.setSctpManagement(this.sctpManagement);
		this.clientM3UAMgmt.addMtp3UserPartListener(client);
		this.clientM3UAMgmt.start();

	}

	@After
	public void tearDown() throws Exception {

		this.sctpManagement.stop();
		this.serverM3UAMgmt.stop();
		this.clientM3UAMgmt.stop();
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
		assertEquals(AspState.ACTIVE, remAsp.getState());
		assertEquals(AsState.ACTIVE, remAs.getState());

		assertEquals(AspState.ACTIVE, localAsp.getState());
		assertEquals(AsState.ACTIVE, localAs.getState());

		client.sendPayload();
		server.sendPayload();

		Thread.sleep(1000);

		client.stop();
		logger.debug("Stopped Client");
		// Give time to exchnge ASP_DOWN messages
		Thread.sleep(100);

		// The AS is Pending
		assertEquals(AsState.PENDING, localAs.getState());
		assertEquals(AsState.PENDING, remAs.getState());

		// Let the AS go in DOWN state
		Thread.sleep(4000);
		logger.debug("Woke from 4000 sleep");

		// The AS is Pending
		assertEquals(AsState.DOWN, localAs.getState());
		assertEquals(AsState.DOWN, remAs.getState());

		client.stopClient();
		server.stop();

		Thread.sleep(100);

		assertEquals(1, server.getReceivedData().size());
		assertEquals(1, client.getReceivedData().size());

	}

	private class Client implements Mtp3UserPartListener {

		private FastList<Mtp3TransferPrimitive> receivedData = new FastList<Mtp3TransferPrimitive>();

		public Client() {
		}

		public FastList<Mtp3TransferPrimitive> getReceivedData() {
			return receivedData;
		}

		public void start() throws Exception {

			// 1. Create SCTP Association
			sctpManagement.createAssociation(CLIENT_HOST, CLIENT_PORT, SERVER_HOST, SERVER_PORT,
					CLIENT_ASSOCIATION_NAME);

			// 2. Create AS
			// m3ua as create rc <rc> <ras-name>
			localAs = clientM3UAMgmt.createAppServer("m3ua as create rc 100 client-testas".split(" "));

			// 3. Create ASP
			// m3ua asp create ip <local-ip> port <local-port> remip <remip>
			// remport <remport> <asp-name>
			localAspFactory = clientM3UAMgmt
					.createAspFactory(("m3ua asp create client-testasp " + CLIENT_ASSOCIATION_NAME).split(" "));

			// 4. Assign ASP to AS
			localAsp = clientM3UAMgmt.assignAspToAs("client-testas", "client-testasp");

			// 5. Define Route
			// Define Route
			clientM3UAMgmt.addRouteAsForDpc(1408, "client-testas");

			// 6. Start ASP
			clientM3UAMgmt.managementStartAsp("client-testasp");

		}

		public void stop() throws Exception {
			// 1. stop ASP
			clientM3UAMgmt.managementStopAsp("client-testasp");

		}

		public void stopClient() throws Exception {

			// 2.Remove route
			clientM3UAMgmt.removeRouteAsForDpc(1408, "client-testas");

			// 3. Unassign ASP from AS
			// clientM3UAMgmt.
			clientM3UAMgmt.unassignAspFromAs("client-testas", "client-testasp");

			// 4. destroy aspFactory
			clientM3UAMgmt.destroyAspFactory("client-testasp");

			// 5. Destroy As
			clientM3UAMgmt.destroyAs("client-testas");
			
			// 6. remove sctp
			sctpManagement.removeAssociation(CLIENT_ASSOCIATION_NAME);
		}

		public void sendPayload() throws Exception {
			Mtp3TransferPrimitive mtp3TransferPrimitive = new Mtp3TransferPrimitive(3, 1, 0, 123, 1408, 1, new byte[] {
					1, 2, 3, 4 });
			clientM3UAMgmt.sendMessage(mtp3TransferPrimitive);
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

	private class Server implements Mtp3UserPartListener {

		private FastList<Mtp3TransferPrimitive> receivedData = new FastList<Mtp3TransferPrimitive>();

		public Server() {

		}

		public FastList<Mtp3TransferPrimitive> getReceivedData() {
			return receivedData;
		}

		private void start() throws Exception {

			// 1. Create SCTP Server
			sctpManagement.createServer(SERVER_NAME, SERVER_HOST, SERVER_PORT);

			// 2. Create SCTP Server Association
			sctpManagement.createServerAssociation(CLIENT_HOST, CLIENT_PORT, SERVER_NAME, SERVER_ASSOCIATION_NAME);

			// 3. Start Server
			sctpManagement.startServer(SERVER_NAME);

			// 4. Create RAS
			// m3ua ras create rc <rc> rk dpc <dpc> opc <opc-list> si <si-list>
			// traffic-mode {broadcast|loadshare|override} <ras-name>
			remAs = serverM3UAMgmt
					.createAppServer("m3ua ras create rc 100 rk dpc 123 si 3 traffic-mode override server-testas"
							.split(" "));

			// 5. Create RASP
			// m3ua rasp create <asp-name> <assoc-name>"
			remAspFactory = serverM3UAMgmt
					.createAspFactory(("m3ua rasp create server-testasp " + SERVER_ASSOCIATION_NAME).split(" "));

			// 6. Assign ASP to AS
			remAsp = serverM3UAMgmt.assignAspToAs("server-testas", "server-testasp");

			// 7. Start ASP
			serverM3UAMgmt.managementStartAsp("server-testasp");

		}

		public void stop() throws Exception {
			serverM3UAMgmt.managementStopAsp("server-testasp");
			
			serverM3UAMgmt.unassignAspFromAs("server-testas", "server-testasp");

			// 4. destroy aspFactory
			serverM3UAMgmt.destroyAspFactory("server-testasp");

			// 5. Destroy As
			serverM3UAMgmt.destroyAs("server-testas");
			
			
			sctpManagement.removeAssociation(SERVER_ASSOCIATION_NAME);
			
			sctpManagement.stopServer(SERVER_NAME);
			sctpManagement.removeServer(SERVER_NAME);
		}

		public void sendPayload() throws Exception {
			Mtp3TransferPrimitive mtp3TransferPrimitive = new Mtp3TransferPrimitive(3, 1, 0, 1408, 123, 1, new byte[] {
					1, 2, 3, 4 });
			serverM3UAMgmt.sendMessage(mtp3TransferPrimitive);
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
