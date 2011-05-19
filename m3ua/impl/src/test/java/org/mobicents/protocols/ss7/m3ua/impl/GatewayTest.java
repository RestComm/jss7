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
import java.nio.ByteBuffer;

import javolution.util.FastList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.m3ua.impl.as.ClientM3UAManagement;
import org.mobicents.protocols.ss7.m3ua.impl.as.ClientM3UAProcess;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ProtocolDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.sg.ServerM3UAManagement;
import org.mobicents.protocols.ss7.m3ua.impl.sg.ServerM3UAProcess;
import org.mobicents.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.ServiceIndicators;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * 
 * @author amit bhayani
 * 
 */
public class GatewayTest {

	private ParameterFactoryImpl parmFactory = new ParameterFactoryImpl();

	As remAs;
	Asp remAsp;
	AspFactory remAspFactory;

	As localAs;
	Asp localAsp;
	AspFactory localAspFactory;

	private Server server;
	private Client client;

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws IOException {
		// Clean up
		ClientM3UAManagement clientM3UAMgmt = new ClientM3UAManagement();
		clientM3UAMgmt.start();
		clientM3UAMgmt.getAppServers().clear();
		clientM3UAMgmt.getAspfactories().clear();
		clientM3UAMgmt.getDpcVsAsName().clear();
		clientM3UAMgmt.stop();

		ServerM3UAManagement serverM3UAMgmt = new ServerM3UAManagement();
		serverM3UAMgmt.start();
		serverM3UAMgmt.getAppServers().clear();
		serverM3UAMgmt.getAspfactories().clear();
		serverM3UAMgmt.stop();
		
		RoutingContext rc = parmFactory.createRoutingContext(new long[] { 100 });

		DestinationPointCode[] dpc = new DestinationPointCode[] { parmFactory
				.createDestinationPointCode(123, (short) 0) };

		ServiceIndicators[] servInds = new ServiceIndicators[] { parmFactory.createServiceIndicators(new short[] { 3 }) };

		TrafficModeType trModType = parmFactory.createTrafficModeType(TrafficModeType.Override);
		LocalRKIdentifier lRkId = parmFactory.createLocalRKIdentifier(1);
		RoutingKey rKey = parmFactory.createRoutingKey(lRkId, rc, null, null, dpc, servInds, null);

		client = new Client(rc, rKey, trModType);
		server = new Server(rc, rKey, trModType);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testSingleAspInAs() throws Exception {
		// 5.1.1. Single ASP in an Application Server ("1+0" sparing),

		System.out.println("Starting server");
		server.start();
		Thread.sleep(100);

		System.out.println("Starting Client");
		client.start();

		Thread.sleep(1000);

		// Both AS and ASP should be ACTIVE now
		assertEquals(AspState.ACTIVE, remAsp.getState());
		assertEquals(AsState.ACTIVE, remAs.getState());

		assertEquals(AspState.ACTIVE, localAsp.getState());
		assertEquals(AsState.ACTIVE, localAs.getState());

		client.sendPayload();
		server.sendPayload();

		Thread.sleep(1000);

		client.stop();
		// Give time to exchnge ASP_DOWN messages
		Thread.sleep(100);

		// The AS is Pending
		assertEquals(AsState.PENDING, localAs.getState());
		assertEquals(AsState.PENDING, remAs.getState());

		// Let the AS go in DOWN state
		Thread.sleep(2000);

		// The AS is Pending
		assertEquals(AsState.DOWN, localAs.getState());
		assertEquals(AsState.DOWN, remAs.getState());

		client.stopClient();
		server.stop();

		Thread.sleep(100);

		assertEquals(1, server.getReceivedData().size());
		assertEquals(1, client.getReceivedData().size());

	}

	private class Client implements Runnable {
		ByteBuffer rxBuffer = ByteBuffer.allocateDirect(1000);
		ByteBuffer txBuffer = ByteBuffer.allocateDirect(1000);

		RoutingContext rc;
		RoutingKey rKey;
		TrafficModeType trModType;
		ClientM3UAProcess rsgw;
		ClientM3UAManagement clientM3UAMgmt;
		private FastList<byte[]> receivedData = new FastList<byte[]>();
		private volatile boolean started = false;

		public Client(RoutingContext rc, RoutingKey rKey, TrafficModeType trModType) {
			this.rc = rc;
			this.rKey = rKey;
			this.trModType = trModType;
		}

		public FastList<byte[]> getReceivedData() {
			return receivedData;
		}

		public void start() throws Exception {
			// Set-up Rem Signaling Gateway
			clientM3UAMgmt = new ClientM3UAManagement();
			clientM3UAMgmt.start();

			rsgw = new ClientM3UAProcess();
			rsgw.setClientM3UAManagement(clientM3UAMgmt);
			rsgw.start();

			// m3ua as create rc <rc> <ras-name>
			localAs = clientM3UAMgmt.createAppServer("m3ua as create rc 100 client-testas".split(" "));
			// m3ua asp create ip <local-ip> port <local-port> remip <remip>
			// remport <remport> <asp-name>
			// localAspFactory = rsgw.createAspFactory("client-testasp",
			// "127.0.0.1", 3777, "127.0.0.1", 3112);
			localAspFactory = clientM3UAMgmt
					.createAspFactory("m3ua asp create ip 127.0.0.1 port 3777 remip 127.0.0.1 remport 3112 client-testasp"
							.split(" "));
			localAsp = clientM3UAMgmt.assignAspToAs("client-testas", "client-testasp");

			// Define Route
			clientM3UAMgmt.addRouteAsForDpc(123, "client-testas");

			clientM3UAMgmt.startAsp("client-testasp");

			started = true;
			new Thread(this).start();
		}

		public void stop() throws Exception {
			clientM3UAMgmt.stopAsp("client-testasp");
			clientM3UAMgmt.stop();
		}

		public void stopClient() {
			started = false;
		}

		public void sendPayload() throws Exception {
			ProtocolDataImpl p1 = (ProtocolDataImpl) parmFactory.createProtocolData(1408, 123, 3, 1, 0, 1, new byte[] {
					1, 2, 3, 4 });
			txBuffer.clear();
			txBuffer.put(p1.getMsu());
			txBuffer.flip();
			rsgw.write(txBuffer);
		}

		public void run() {
			while (started) {
				try {
					rsgw.execute();
					rxBuffer.clear();
					int rxBytes = rsgw.read(rxBuffer);

					if (rxBytes > 0) {
						byte[] data = new byte[rxBytes];
						rxBuffer.flip();
						rxBuffer.get(data);
						receivedData.add(data);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private class Server implements Runnable {
		ByteBuffer rxBuffer = ByteBuffer.allocateDirect(1000);
		ByteBuffer txBuffer = ByteBuffer.allocateDirect(1000);

		RoutingContext rc;
		RoutingKey rKey;
		TrafficModeType trModType;
		ServerM3UAProcess sgw;
		ServerM3UAManagement serverM3UAMgmt;

		private volatile boolean started = false;

		private FastList<byte[]> receivedData = new FastList<byte[]>();

		public Server(RoutingContext rc, RoutingKey rKey, TrafficModeType trModType) {
			this.rc = rc;
			this.rKey = rKey;
			this.trModType = trModType;
		}

		public FastList<byte[]> getReceivedData() {
			return receivedData;
		}

		public void start() throws Exception {
			serverM3UAMgmt = new ServerM3UAManagement();
			serverM3UAMgmt.start();

			// Set-up Signaling Gateway
			sgw = new ServerM3UAProcess("127.0.0.1", 3112);
			sgw.setServerM3UAManagement(serverM3UAMgmt);
			sgw.start();

			// m3ua ras create rc <rc> rk dpc <dpc> opc <opc-list> si <si-list>
			// traffic-mode {broadcast|loadshare|override} <ras-name>
			remAs = serverM3UAMgmt
					.createAppServer("m3ua ras create rc 100 rk dpc 123 si 3 traffic-mode override server-testas"
							.split(" "));
			// m3ua rasp create ip <ip> port <port> <asp-name>"
			remAspFactory = serverM3UAMgmt.createAspFactory("m3ua rasp create ip 127.0.0.1 port 3777 server-testasp"
					.split(" "));
			remAsp = serverM3UAMgmt.assignAspToAs("server-testas", "server-testasp");

			started = true;
			new Thread(this).start();
		}

		public void stop() throws IOException {
			started = false;
			sgw.stop();
		}

		public void sendPayload() throws Exception {
			ProtocolDataImpl p1 = (ProtocolDataImpl) parmFactory.createProtocolData(1408, 123, 3, 1, 0, 1, new byte[] {
					1, 2, 3, 4 });
			txBuffer.clear();
			txBuffer.put(p1.getMsu());
			txBuffer.flip();
			sgw.write(txBuffer);
		}

		public void run() {
			while (started) {
				try {
					sgw.execute();
					rxBuffer.clear();
					int rxBytes = sgw.read(rxBuffer);

					if (rxBytes > 0) {
						byte[] data = new byte[rxBytes];
						rxBuffer.flip();
						rxBuffer.get(data);
						receivedData.add(data);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
