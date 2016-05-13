/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package org.mobicents.protocols.ss7.m3ua.impl.oam;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBufAllocator;

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
import org.mobicents.protocols.ss7.m3ua.Util;
import org.mobicents.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 
 * @author amit bhayani
 * 
 */
public class M3UAShellExecutorTest {

    M3UAShellExecutor m3uaExec = null;
    private TransportManagement transportManagement = null;
    M3UAManagementImpl clientM3UAMgmt = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws Exception {
        m3uaExec = new M3UAShellExecutor();

        this.transportManagement = new TransportManagement();
        this.transportManagement.setPersistDir(Util.getTmpTestDir());

        this.clientM3UAMgmt = new M3UAManagementImpl("M3UAShellExecutorTest", null);
        this.clientM3UAMgmt.setTransportManagement(this.transportManagement);
        this.clientM3UAMgmt.setPersistDir(Util.getTmpTestDir());
        this.clientM3UAMgmt.start();

    }

    @AfterMethod
    public void tearDown() throws Exception {
        // Clean up
        clientM3UAMgmt.removeAllResourses();
        clientM3UAMgmt.stop();

    }

    @Test
    public void testServerCommands() throws Exception {

        FastMap<String, M3UAManagementImpl> m3uaManagements = new FastMap<String, M3UAManagementImpl>();
        m3uaManagements.put(clientM3UAMgmt.getName(), clientM3UAMgmt);
        m3uaExec.setM3uaManagements(m3uaManagements);

        Association sctpAssociation = this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc1");

        Association sctpAssociation2 = this.transportManagement.addAssociation(null, 0, null, 0, "testAssoc2");

        String result = m3uaExec.execute("m3ua as show stackname M3UAShellExecutorTest".split(" "));
        assertEquals(result, String.format(M3UAOAMMessages.NO_AS_DEFINED_YET, this.clientM3UAMgmt.getName()));

        result = m3uaExec.execute("m3ua asp show stackname M3UAShellExecutorTest".split(" "));
        assertEquals(result, String.format(M3UAOAMMessages.NO_ASP_DEFINED_YET, this.clientM3UAMgmt.getName()));

        // Test creating new AS testas
        result = m3uaExec.execute("m3ua as create testas AS mode SE rc 100 traffic-mode loadshare".split(" "));
        assertEquals(result, String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, "testas", this.clientM3UAMgmt.getName()));

        // Try adding same again
        result = m3uaExec.execute("m3ua as create testas AS mode SE rc 100 traffic-mode loadshare".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_AS_FAIL_NAME_EXIST, "testas"), result);

        // Create AS with only mandatory params
        result = m3uaExec.execute("m3ua as create testas1 AS".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, "testas1", this.clientM3UAMgmt.getName()), result);

        // Create AS with all params
        result = m3uaExec
                .execute("m3ua as create testas2 AS mode DE ipspType CLIENT rc 100 traffic-mode loadshare network-appearance 12"
                        .split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, "testas2", this.clientM3UAMgmt.getName()), result);

        // Create AS of type IPSP
        result = m3uaExec.execute("m3ua as create MTUAS IPSP mode DE ipspType server rc 1 traffic-mode loadshare".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_AS_SUCESSFULL, "MTUAS", this.clientM3UAMgmt.getName()), result);

        // create ASP with only mandatory params
        result = m3uaExec.execute("m3ua asp create testasp1 testAssoc1".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_ASP_SUCESSFULL, "testasp1", this.clientM3UAMgmt.getName()), result);

        // create ASP with all params but with same aspid
        result = m3uaExec.execute("m3ua asp create testasp2 testAssoc2 aspid 2".split(" "));
        assertEquals(result, String.format(M3UAOAMMessages.ASP_ID_TAKEN, 2));

        // create ASP with all params but with unique aspid
        result = m3uaExec.execute("m3ua asp create testasp2 testAssoc2 aspid 3".split(" "));
        assertEquals(result, String.format(M3UAOAMMessages.CREATE_ASP_SUCESSFULL, "testasp2", this.clientM3UAMgmt.getName()));

        // Error for same name
        result = m3uaExec.execute("m3ua asp create testasp1 testAssoc1".split(" "));
        assertEquals(String.format(M3UAOAMMessages.CREATE_ASP_FAIL_NAME_EXIST, "testasp1"), result);

        // Error : Try to start Asp without assiging to any As
        result = m3uaExec.execute("m3ua asp start testasp1".split(" "));
        assertEquals(String.format(M3UAOAMMessages.ASP_NOT_ASSIGNED_TO_AS, "testasp1"), result);

        // assign ASP to AS
        result = m3uaExec.execute("m3ua as add testas testasp1".split(" "));
        assertEquals(
                String.format(M3UAOAMMessages.ADD_ASP_TO_AS_SUCESSFULL, "testasp1", "testas", this.clientM3UAMgmt.getName()),
                result);

        // add again
        result = m3uaExec.execute("m3ua as add testas testasp1".split(" "));
        assertEquals(String.format(M3UAOAMMessages.ADD_ASP_TO_AS_FAIL_ALREADY_ASSIGNED_TO_THIS_AS, "testasp1", "testas"),
                result);

        // Test Routes
        result = m3uaExec.execute("m3ua route add testas 2 -1 -1".split(" "));
        assertEquals(
                String.format(M3UAOAMMessages.ADD_ROUTE_AS_FOR_DPC_SUCCESSFULL, "testas", 2, this.clientM3UAMgmt.getName()),
                result);

        // Start Asp
        result = m3uaExec.execute("m3ua asp start testasp1".split(" "));
        assertEquals(String.format(M3UAOAMMessages.ASP_START_SUCESSFULL, "testasp1", this.clientM3UAMgmt.getName()), result);
        assertTrue(sctpAssociation.isStarted());

        // manually make Association up
        ((TestAssociation) sctpAssociation).signalCommUp();

        // Error : starting Asp again
        result = m3uaExec.execute("m3ua asp start testasp1".split(" "));
        assertEquals(String.format(M3UAOAMMessages.ASP_ALREADY_STARTED, "testasp1"), result);

        // Stop Asp
        result = m3uaExec.execute("m3ua asp stop testasp1".split(" "));
        assertEquals(String.format(M3UAOAMMessages.ASP_STOP_SUCESSFULL, "testasp1", this.clientM3UAMgmt.getName()), result);

        // Lets wait for 3 seconds so underlying transport is killed
        Thread.sleep(3500);

        assertTrue(!sctpAssociation.isStarted());

        // manually bring down
        ((TestAssociation) sctpAssociation).signalCommLost();

        // Remove Asp
        result = m3uaExec.execute("m3ua as remove testas testasp1".split(" "));
        assertEquals(
                result,
                String.format(M3UAOAMMessages.REMOVE_ASP_FROM_AS_SUCESSFULL, "testasp1", "testas",
                        this.clientM3UAMgmt.getName()));

        // Destroy Asp
        result = m3uaExec.execute("m3ua asp destroy testasp1".split(" "));
        assertEquals(result, String.format(M3UAOAMMessages.DESTROY_ASP_SUCESSFULL, "testasp1", this.clientM3UAMgmt.getName()));

        // Error : Destroy As
        result = m3uaExec.execute("m3ua as destroy testas".split(" "));
        assertEquals(result, String.format(M3UAOAMMessages.AS_USED_IN_ROUTE_ERROR, "testas", "2:-1:-1"));

        // Remove route
        result = m3uaExec.execute("m3ua route remove testas 2 -1 -1".split(" "));
        assertEquals(
                String.format(M3UAOAMMessages.REMOVE_AS_ROUTE_FOR_DPC_SUCCESSFULL, "testas", 2, this.clientM3UAMgmt.getName()),
                result);

        // Destroy As
        result = m3uaExec.execute("m3ua as destroy testas".split(" "));
        assertEquals(result, String.format(M3UAOAMMessages.DESTROY_AS_SUCESSFULL, "testas", this.clientM3UAMgmt.getName()));

    }

    class TestAssociation implements Association {

        // Is the Association been started by management?
        private volatile boolean started = false;
        // Is the Association up (connection is established)
        protected volatile boolean up = false;

        private AssociationListener associationListener = null;
        private String name = null;

        TestAssociation(String name) {
            this.name = name;
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
            return this.started;
        }

        @Override
        public void send(PayloadData payloadData) throws Exception {
        }

        @Override
        public void setAssociationListener(AssociationListener associationListener) {
            this.associationListener = associationListener;
        }

        public void signalCommUp() {
            this.up = true;
            this.associationListener.onCommunicationUp(this, 1, 1);
        }

        public void signalCommLost() {
            this.up = false;
            this.associationListener.onCommunicationLost(this);
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
            return started && up;
        }

        protected void start() {
            this.started = true;
        }

        protected void stop() {
            this.started = false;
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

    class TransportManagement implements Management {

        private FastMap<String, Association> associations = new FastMap<String, Association>();

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
            return associations.unmodifiable();
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
            Association association = this.associations.get(assocName);
            if (association != null) {
                ((TestAssociation) association).start();
            }
        }

        @Override
        public void startServer(String arg0) throws Exception {
            // TODO Auto-generated method stub

        }

        @Override
        public void stop() throws Exception {

        }

        @Override
        public void stopAssociation(String assocName) throws Exception {
            Association association = this.associations.get(assocName);
            if (association != null) {
                ((TestAssociation) association).stop();
            }
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

    }
}
