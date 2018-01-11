/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import io.netty.buffer.ByteBufAllocator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import javolution.util.FastMap;

import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.AssociationListener;
import org.mobicents.protocols.api.AssociationType;
import org.mobicents.protocols.api.CongestionListener;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.api.ManagementEventListener;
import org.mobicents.protocols.api.PayloadData;
import org.mobicents.protocols.api.Server;
import org.mobicents.protocols.api.ServerListener;
import org.mobicents.protocols.ss7.m3ua.As;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.RouteAs;
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
    private NettyTransportManagement transportManagement = null;
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
        this.transportManagement = new NettyTransportManagement();

        this.m3uaMgmt = new M3UAManagementImpl("M3UAManagementTest", null);
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

        M3UAManagementImpl m3uaMgmt1 = new M3UAManagementImpl("M3UAManagementTest", null);
        m3uaMgmt1.setPersistDir(Util.getTmpTestDir());
        m3uaMgmt1.setTransportManagement(this.transportManagement);
        m3uaMgmt1.start();

        assertEquals(1, m3uaMgmt1.getAppServers().size());
        assertEquals(1, m3uaMgmt1.getAspfactories().size());
        Map<String, RouteAs> route = m3uaMgmt1.getRoute();
        assertEquals(1, route.size());

        // Make sure AS is not null
        RouteAs routeAs1 = route.get("123:1:1");
        As[] asList = routeAs1.getAsArray();
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

    @Test
    public void testSerializationFromOldVerToNewVers() throws Exception {
        // Prepare path for file
        
        String M3UA_PERSIST_DIR_KEY = "m3ua.persist.dir";
        String USER_DIR_KEY = "user.dir";
        String PERSIST_FILE_NAME = "m3ua.xml";
        String name = this.m3uaMgmt.getName()+"_OldVerToNewVers";

        String persistDir = this.m3uaMgmt.getPersistDir();
        StringBuffer persistFile = new StringBuffer();
        
        if (persistDir != null) {
            persistFile.append(persistDir).append(File.separator).append(name).append("_").append(PERSIST_FILE_NAME);
        } else {
            persistFile.append(System.getProperty(M3UA_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
                    .append(File.separator).append(name).append("_").append(PERSIST_FILE_NAME);
        }

        String oldXmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><heartbeattime value=\"10000\"/><aspFactoryList><aspFactory name=\"testasp\" assocName=\"test\" started=\"false\" maxseqnumber=\"256\" aspid=\"2\" heartbeat=\"false\"/><aspFactory name=\"testasp1\" assocName=\"test1\" started=\"false\" maxseqnumber=\"256\" aspid=\"3\" heartbeat=\"false\"/></aspFactoryList><asList><as name=\"testAs\" minAspActiveForLb=\"0\" functionality=\"AS\" exchangeType=\"SE\" ipspType=\"CLIENT\"><trafficMode mode=\"2\"/><defTrafficMode mode=\"2\"/><asps><asp name=\"testasp\"/></asps></as><as name=\"testAs1\" minAspActiveForLb=\"0\" functionality=\"AS\" exchangeType=\"SE\" ipspType=\"CLIENT\"><trafficMode mode=\"2\"/><defTrafficMode mode=\"2\"/><asps><asp name=\"testasp1\"/></asps></as></asList><route><key value=\"2:-1:-1\"/><value value=\"testAs,testAs1\"/></route>";
        File f = new File(persistFile.toString());
        if(f.exists()){
            f.delete();
        }
        
        //write to old file
        f.createNewFile();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
        bw.write(oldXmlData);
        bw.close();
        
        Association association = this.transportManagement.addAssociation(null, 0, null, 0, "test");
        association = this.transportManagement.addAssociation(null, 0, null, 0, "test1");
        
        //now start M3UA again and it should read from old data file
        M3UAManagementImpl m3uaMgmt1 = new M3UAManagementImpl(name, null);
        m3uaMgmt1.setPersistDir(Util.getTmpTestDir());
        m3uaMgmt1.setTransportManagement(this.transportManagement);
        m3uaMgmt1.start();
        
        assertEquals(m3uaMgmt1.getAppServers().size(), 2 );
        assertEquals(m3uaMgmt1.getAspfactories().size(), 2);
        Map<String, RouteAs> route = m3uaMgmt1.getRoute();
        assertEquals(1, route.size());

        // Make sure AS is not null
        RouteAs routeAs1 = route.get("2:-1:-1");
        As[] asList = routeAs1.getAsArray();
        As routeAs = asList[0];
        assertNotNull(routeAs);
        
        //Now stop M3UA management and check new file created
        m3uaMgmt1.stop();
        
    }

    @Test
    public void testPersistFileName() throws Exception {
        M3UAManagementImpl m3ua = new M3UAManagementImpl("test", null);
        m3ua.setMaxAsForRoute(10);

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

        @Override
        public ByteBufAllocator getByteBufAllocator() throws Exception {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getCongestionLevel() {
            // TODO Auto-generated method stub
            return 0;
        }

    }

    class NettyTransportManagement implements Management {

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

        @Override
        public double getCongControl_BackToNormalDelayThreshold_1() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public double getCongControl_BackToNormalDelayThreshold_2() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public double getCongControl_BackToNormalDelayThreshold_3() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public double getCongControl_DelayThreshold_1() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public double getCongControl_DelayThreshold_2() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public double getCongControl_DelayThreshold_3() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setCongControl_BackToNormalDelayThreshold_1(double arg0) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCongControl_BackToNormalDelayThreshold_2(double arg0) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCongControl_BackToNormalDelayThreshold_3(double arg0) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCongControl_DelayThreshold_1(double arg0) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCongControl_DelayThreshold_2(double arg0) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCongControl_DelayThreshold_3(double arg0) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Boolean getOptionSctpDisableFragments() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Integer getOptionSctpFragmentInterleave() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Boolean getOptionSctpNodelay() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Integer getOptionSoLinger() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Integer getOptionSoRcvbuf() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Integer getOptionSoSndbuf() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setOptionSctpDisableFragments(Boolean arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setOptionSctpFragmentInterleave(Integer arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setOptionSctpNodelay(Boolean arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setOptionSoLinger(Integer arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setOptionSoRcvbuf(Integer arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setOptionSoSndbuf(Integer arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Integer getOptionSctpInitMaxstreams_MaxInStreams() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Integer getOptionSctpInitMaxstreams_MaxOutStreams() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setOptionSctpInitMaxstreams_MaxInStreams(Integer arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setOptionSctpInitMaxstreams_MaxOutStreams(Integer arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void addCongestionListener(CongestionListener arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void removeCongestionListener(CongestionListener arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public int getBufferSize() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setBufferSize(int arg0) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void modifyServer(String serverName, String hostAddress, Integer port, IpChannelType ipChannelType,
                Boolean acceptAnonymousConnections, Integer maxConcurrentConnectionsCount, String[] extraHostAddresses)
                throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void modifyServerAssociation(String assocName, String peerAddress, Integer peerPort, String serverName,
                IpChannelType ipChannelType) throws Exception {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void modifyAssociation(String hostAddress, Integer hostPort, String peerAddress, Integer peerPort,
                String assocName, IpChannelType ipChannelType, String[] extraHostAddresses) throws Exception {
            // TODO Auto-generated method stub
            
        }

    }
}
