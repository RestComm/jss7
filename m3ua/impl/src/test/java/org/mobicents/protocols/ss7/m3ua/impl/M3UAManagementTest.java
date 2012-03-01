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


import java.util.List;
import java.util.Map;

import javolution.util.FastMap;

import org.testng.annotations.*;
import static org.testng.Assert.*;

import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.AssociationListener;
import org.mobicents.protocols.api.AssociationType;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.api.PayloadData;
import org.mobicents.protocols.api.Server;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * Test the serialization/de-serialization
 * 
 * @author amit bhayani
 * 
 */
public class M3UAManagementTest {

	private M3UAManagement m3uaMgmt = null;
	private TransportManagement transportManagement = null;
	private ParameterFactoryImpl factory = new ParameterFactoryImpl();

	/**
	 * 
	 */
	public M3UAManagementTest() {
		// TODO Auto-generated constructor stub
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@BeforeMethod
	public void setUp() throws Exception {
		this.transportManagement = new TransportManagement();

		this.m3uaMgmt = new M3UAManagement("M3UAManagementTest");
		this.m3uaMgmt.setTransportManagement(this.transportManagement);
		this.m3uaMgmt.start();
	}

	@AfterMethod
	public void tearDown() throws Exception {
		m3uaMgmt.stop();
	}

	@Test
	public void testSerialization() throws Exception {

		Association association = this.transportManagement.addAssociation(null, 0, null, 0, "ASPAssoc1");

		RoutingContext rc = factory.createRoutingContext(new long[] { 1 });
		NetworkAppearance na = factory.createNetworkAppearance(12l);
		As as1 = this.m3uaMgmt.createAs("AS1", Functionality.AS, ExchangeType.SE, null, rc, null, na);

		AspFactory aspFactory = this.m3uaMgmt.createAspFactory("ASP1", "ASPAssoc1");

		this.m3uaMgmt.assignAspToAs("AS1", "ASP1");

		this.m3uaMgmt.addRoute(123, 1, 1, "AS1");

		this.m3uaMgmt.startAsp("ASP1");

		this.m3uaMgmt.stop();

		M3UAManagement m3uaMgmt1 = new M3UAManagement("M3UAManagementTest");
		m3uaMgmt1.setTransportManagement(this.transportManagement);
		m3uaMgmt1.start();

		assertEquals(1, m3uaMgmt1.getAppServers().size());
		assertEquals(1, m3uaMgmt1.getAspfactories().size());
		FastMap<String, As[]> route = m3uaMgmt1.getRoute();
		assertEquals(1, route.size());
		
		// Make sure AS is not null
		As[] asList = route.get("123:1:1");
		As routeAs = asList[0];
		assertNotNull(routeAs);
		
		As managementAs = m3uaMgmt1.getAppServers().get(0);

		//Make sure both m3uamanagament and route are pointing to same AS instance
		assertEquals(routeAs, managementAs);
		
		assertEquals(2, ((TestAssociation) association).getNoOfTimeStartCalled());
		
		this.m3uaMgmt.stopAsp("ASP1");
		
		this.m3uaMgmt.unassignAspFromAs("AS1", "ASP1");
		
		this.m3uaMgmt.removeRoute(123, 1, 1, "AS1");
		
		this.m3uaMgmt.destroyAspFactory("ASP1");
		
		this.m3uaMgmt.destroyAs("AS1");

	}

	class TestAssociation implements Association {

		private int noOfTimeStartCalled = 0;
		private AssociationListener associationListener = null;
		private String name = null;

		TestAssociation(String name) {
			this.name = name;
		}

		public int getNoOfTimeStartCalled() {
			return noOfTimeStartCalled;
		}

		@Override
		public AssociationListener getAssociationListener() {
			return this.associationListener;
		}

		@Override
		public String getHostAddress() {
			return null;
		}

		@Override
		public int getHostPort() {
			return 0;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public String getPeerAddress() {
			return null;
		}

		@Override
		public int getPeerPort() {
			return 0;
		}

		@Override
		public String getServerName() {
			return null;
		}

		@Override
		public boolean isStarted() {
			return false;
		}

		@Override
		public void send(PayloadData payloadData) throws Exception {
		}

		@Override
		public void setAssociationListener(AssociationListener associationListener) {
			this.associationListener = associationListener;
		}

		public void signalCommUp() {
			this.associationListener.onCommunicationUp(this);
		}

		public void signalCommLost() {
			this.associationListener.onCommunicationLost(this);
		}

		protected void start() {
			this.noOfTimeStartCalled++;
		}

		protected void stop() {
			this.noOfTimeStartCalled--;
		}

		@Override
		public IpChannelType getIpChannelType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AssociationType getAssociationType() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	class TransportManagement implements Management {

		private FastMap<String, TestAssociation> associations = new FastMap<String, TestAssociation>();

		@Override
		public Association addAssociation(String hostAddress, int hostPort, String peerAddress, int peerPort,
				String assocName) throws Exception {
			TestAssociation testAssociation = new TestAssociation(assocName);
			this.associations.put(assocName, testAssociation);
			return testAssociation;
		}

		@Override
		public Server addServer(String serverName, String hostAddress, int port) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Association addServerAssociation(String peerAddress, int peerPort, String serverName, String assocName)
				throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Association getAssociation(String assocName) throws Exception {
			return this.associations.get(assocName);
		}

		@Override
		public Map<String, Association> getAssociations() {
			return null;
		}

		@Override
		public int getConnectDelay() {
			return 0;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public List<Server> getServers() {
			return null;
		}

		@Override
		public int getWorkerThreads() {
			return 0;
		}

		@Override
		public boolean isSingleThread() {
			return false;
		}

		@Override
		public void removeAssociation(String assocName) throws Exception {

		}

		@Override
		public void removeServer(String serverName) throws Exception {

		}

		@Override
		public void setConnectDelay(int connectDelay) {

		}

		@Override
		public void setSingleThread(boolean arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void setWorkerThreads(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void start() throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public void startAssociation(String assocName) throws Exception {
			TestAssociation testAssociation = this.associations.get(assocName);
			testAssociation.start();
		}

		@Override
		public void startServer(String arg0) throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public void stop() throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public void stopAssociation(String arg0) throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public void stopServer(String arg0) throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public String getPersistDir() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setPersistDir(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Association addAssociation(String arg0, int arg1, String arg2, int arg3, String arg4, IpChannelType arg5)
				throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Server addServer(String arg0, String arg1, int arg2, IpChannelType arg3) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Association addServerAssociation(String arg0, int arg1, String arg2, String arg3, IpChannelType arg4)
				throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void removeAllResourses() throws Exception {
			// TODO Auto-generated method stub
			
		}

	}
}
