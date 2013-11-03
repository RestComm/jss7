/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.m3ua.impl;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import javolution.util.FastMap;

import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.AssociationListener;
import org.mobicents.protocols.api.AssociationType;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.api.ManagementEventListener;
import org.mobicents.protocols.api.PayloadData;
import org.mobicents.protocols.api.Server;
import org.mobicents.protocols.api.ServerListener;
import org.mobicents.protocols.ss7.m3ua.As;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.Util;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test the serialization/de-serialization
 *
 * @author amit bhayani
 *
 */
public class M3UAManagementTest {

    private M3UAManagementImpl m3uaMgmt = null;
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

        this.m3uaMgmt = new M3UAManagementImpl("M3UAManagementTest");
        this.m3uaMgmt.setPersistDir(Util.getTmpTestDir());
        this.m3uaMgmt.setTransportManagement(this.transportManagement);
        this.m3uaMgmt.start();
        this.m3uaMgmt.removeAllResourses();
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
        AsImpl as1 = (AsImpl) this.m3uaMgmt.createAs("AS1", Functionality.AS, ExchangeType.SE, null, rc, null, 1, na);

        AspFactoryImpl aspFactoryImpl = (AspFactoryImpl) this.m3uaMgmt.createAspFactory("ASP1", "ASPAssoc1", false);

        this.m3uaMgmt.assignAspToAs("AS1", "ASP1");

        this.m3uaMgmt.addRoute(123, 1, 1, "AS1");

        this.m3uaMgmt.startAsp("ASP1");

        this.m3uaMgmt.stop();

        M3UAManagementImpl m3uaMgmt1 = new M3UAManagementImpl("M3UAManagementTest");
        m3uaMgmt1.setPersistDir(Util.getTmpTestDir());
        m3uaMgmt1.setTransportManagement(this.transportManagement);
        m3uaMgmt1.start();

        assertEquals(1, m3uaMgmt1.getAppServers().size());
        assertEquals(1, m3uaMgmt1.getAspfactories().size());
        Map<String, As[]> route = m3uaMgmt1.getRoute();
        assertEquals(1, route.size());

        // Make sure AS is not null
        As[] asList = route.get("123:1:1");
        As routeAs = asList[0];
        assertNotNull(routeAs);

        AsImpl managementAs = (AsImpl) m3uaMgmt1.getAppServers().get(0);

        // Make sure both m3uamanagament and route are pointing to same AS instance
        assertEquals(routeAs, managementAs);

        assertEquals(2, ((TestAssociation) association).getNoOfTimeStartCalled());

        m3uaMgmt1.stopAsp("ASP1");

        m3uaMgmt1.unassignAspFromAs("AS1", "ASP1");

        m3uaMgmt1.removeRoute(123, 1, 1, "AS1");

        m3uaMgmt1.destroyAspFactory("ASP1");

        m3uaMgmt1.destroyAs("AS1");

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
            this.associationListener.onCommunicationUp(this, 1, 1);
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

        @Override
        public String[] getExtraHostAddresses() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.mobicents.protocols.api.Association#isConnected()
         */
        @Override
        public boolean isConnected() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void acceptAnonymousAssociation(AssociationListener arg0) throws Exception {
            // TODO Auto-generated method stub

        }

        @Override
        public void rejectAnonymousAssociation() {
            // TODO Auto-generated method stub

        }

        @Override
        public void stopAnonymousAssociation() throws Exception {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isUp() {
            // TODO Auto-generated method stub
            return false;
        }

    }

    class TransportManagement implements Management {

        private FastMap<String, TestAssociation> associations = new FastMap<String, TestAssociation>();

        @Override
        public Association addAssociation(String hostAddress, int hostPort, String peerAddress, int peerPort, String assocName)
                throws Exception {
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
        public Association addAssociation(String arg0, int arg1, String arg2, int arg3, String arg4, IpChannelType arg5,
                String[] extraHostAddresses) throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Server addServer(String arg0, String arg1, int arg2, IpChannelType arg3, String[] extraHostAddresses)
                throws Exception {
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

        @Override
        public void addManagementEventListener(ManagementEventListener arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public Server addServer(String arg0, String arg1, int arg2, IpChannelType arg3, boolean arg4, int arg5, String[] arg6)
                throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ServerListener getServerListener() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void removeManagementEventListener(ManagementEventListener arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void setServerListener(ServerListener arg0) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         *
         * @see org.mobicents.protocols.api.Management#isStarted()
         */
        @Override
        public boolean isStarted() {
            // TODO Auto-generated method stub
            return false;
        }

    }
}
