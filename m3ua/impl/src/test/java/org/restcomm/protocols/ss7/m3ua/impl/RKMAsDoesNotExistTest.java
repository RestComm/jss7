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

package org.restcomm.protocols.ss7.m3ua.impl;

import static org.testng.Assert.assertEquals;
import javolution.util.FastList;

import org.apache.log4j.Logger;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.Util;
import org.restcomm.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.impl.message.rkm.DeregistrationRequestImpl;
import org.restcomm.protocols.ss7.m3ua.impl.message.rkm.RegistrationRequestImpl;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.RoutingKeyImpl;
import org.restcomm.protocols.ss7.m3ua.message.MessageClass;
import org.restcomm.protocols.ss7.m3ua.message.MessageType;
import org.restcomm.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.restcomm.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.OPCList;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.ServiceIndicators;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.restcomm.protocols.ss7.mtp.Mtp3EndCongestionPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPartListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.sun.nio.sctp.SctpChannel;

/**
 *
 * @author amit bhayani
 *
 */
@SuppressWarnings("restriction")
public class RKMAsDoesNotExistTest {

    private static final Logger logger = Logger.getLogger(RKMAsDoesNotExistTest.class);
    
    private MessageFactoryImpl messageFactory = new MessageFactoryImpl();
    private ParameterFactoryImpl parmFactory = new ParameterFactoryImpl();

    private static final String SERVER_NAME = "testserver";
    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 2345;

    private static final String SERVER_ASSOCIATION_NAME = "serverAsscoiation";
    private static final String CLIENT_ASSOCIATION_NAME = "clientAsscoiation";

    private static final String CLIENT_HOST = "127.0.0.1";
    private static final int CLIENT_PORT = 2346;

    private Management serverSctpManagement = null;
    private Management clientSctpManagement = null;
    private M3UAManagementImpl serverM3uaMgmt = null;
    private M3UAManagementImpl clientM3uaMgmt = null;
    private ParameterFactoryImpl factory = new ParameterFactoryImpl();

    private AsImpl remAs;
    private AspImpl remAsp;
    @SuppressWarnings("unused")
    private AspFactoryImpl remAspFactory;

    private AsImpl localAs;
    private AspImpl localAsp;
    @SuppressWarnings("unused")
    private AspFactoryImpl localAspFactory;

    private Server server;
    private Client client;

    private Mtp3UserPartListenerImpl mtp3UserPartListener = null;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws Exception {

        mtp3UserPartListener = new Mtp3UserPartListenerImpl();

        client = new Client();
        server = new Server();

        this.clientSctpManagement = getSctpManagement("RKMTest-client");
        this.serverSctpManagement = getSctpManagement("RKMTest-server");
        this.clientM3uaMgmt = getM3uaManagement("RKMTest-client", this.clientSctpManagement);
        this.serverM3uaMgmt = getM3uaManagement("RKMTest-server", this.serverSctpManagement);
        this.serverM3uaMgmt.setRoutingKeyManagementEnabled(true);
    }

    @AfterMethod
    public void tearDown() throws Exception {

        this.serverSctpManagement.stop();
        this.clientSctpManagement.stop();
        this.serverM3uaMgmt.stop();
        this.clientM3uaMgmt.stop();
    }

    @Test
    public void testSingleAspInAs() throws Exception {
 
        logger.info("Starting server");
        server.start();
        Thread.sleep(100);
        logger.info("Starting Client");
        client.start();
        Thread.sleep(10000);

        assertEquals(AspState.getState(remAsp.getPeerFSM().getState().getName()), AspState.ACTIVE);
        assertEquals(AsState.getState(remAs.getLocalFSM().getState().getName()), AsState.ACTIVE);

        assertEquals(AspState.getState(localAsp.getLocalFSM().getState().getName()), AspState.ACTIVE);
        assertEquals(AsState.getState(localAs.getPeerFSM().getState().getName()), AsState.ACTIVE);
        
        //will create new AS and Routes
        client.sendRkmRegister();
        Thread.sleep(2000);
        //check number of ass and route
        assertEquals(serverM3uaMgmt.getAppServers().size(), 2);
        assertEquals(serverM3uaMgmt.getRoute().size(), 3);

        client.sendRkmDeregister();
        Thread.sleep(2000);
        assertEquals(serverM3uaMgmt.getAppServers().size(), 1);
        assertEquals(serverM3uaMgmt.getRoute().size(), 1);

        client.stop();
        server.stop();
        Thread.sleep(100);
    }

    /**
     * @return true if sctp is supported by this OS and false in not
     */
    public static boolean checkSctpEnabled() {
        try {
            SctpChannel socketChannel = SctpChannel.open();
            socketChannel.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private class Client {

        public Client() {
        }

        public void start() throws Exception {

            IpChannelType ipChannelType = IpChannelType.TCP;
            if (checkSctpEnabled())
                ipChannelType = IpChannelType.SCTP;

            clientSctpManagement.addAssociation(CLIENT_HOST, CLIENT_PORT, SERVER_HOST, SERVER_PORT, CLIENT_ASSOCIATION_NAME,ipChannelType, null);
            RoutingContext rc = factory.createRoutingContext(new long[] { 100l });
            TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
            localAs = (AsImpl) clientM3uaMgmt.createAs("client-testas", Functionality.IPSP, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, 1, null);
            localAspFactory = (AspFactoryImpl) clientM3uaMgmt.createAspFactory("client-testasp", CLIENT_ASSOCIATION_NAME, false);
            localAsp = clientM3uaMgmt.assignAspToAs("client-testas", "client-testasp");
            clientM3uaMgmt.addRoute(1408, -1, -1, "client-testas");
            clientM3uaMgmt.startAsp("client-testasp");
        }

        public void stop() throws Exception {
            clientM3uaMgmt.stopAsp("client-testasp");
            Thread.sleep(100);
            Thread.sleep(4000);
            clientM3uaMgmt.removeRoute(1408, -1, -1, "client-testas");
            clientM3uaMgmt.unassignAspFromAs("client-testas", "client-testasp");
            clientM3uaMgmt.destroyAspFactory("client-testasp");
            clientM3uaMgmt.destroyAs("client-testas");
            clientSctpManagement.removeAssociation(CLIENT_ASSOCIATION_NAME);
        }

        public void sendRkmRegister() throws Exception {
            RegistrationRequestImpl msg = (RegistrationRequestImpl) messageFactory.createMessage(MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.REG_REQUEST);
            LocalRKIdentifier localRkId = parmFactory.createLocalRKIdentifier(12);
            RoutingContext rc = parmFactory.createRoutingContext(new long[] { 200 });
            TrafficModeType trafMdTy = parmFactory.createTrafficModeType(2);
            NetworkAppearance netApp = parmFactory.createNetworkAppearance(1);
            DestinationPointCode[] dpc = new DestinationPointCode[] { parmFactory.createDestinationPointCode(123, (short) 0)};
            ServiceIndicators[] servInds = new ServiceIndicators[] { parmFactory.createServiceIndicators(new short[] { 1, 2 })};
            OPCList[] opcList = new OPCList[] { parmFactory.createOPCList(new int[] { 1 }, new short[] { 0 }) };
            RoutingKeyImpl routKey = (RoutingKeyImpl) parmFactory.createRoutingKey(localRkId, rc, trafMdTy, netApp, dpc, servInds, opcList);
            msg.setRoutingKey(routKey);
            localAsp.aspFactoryImpl.write(msg);
        }
        
        public void sendRkmDeregister() throws Exception {
            DeregistrationRequestImpl msg = (DeregistrationRequestImpl) messageFactory.createMessage(MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.DEREG_REQUEST);
            RoutingContext rc = parmFactory.createRoutingContext(new long[] { 200 });
            msg.setRoutingContext(rc);
            localAsp.aspFactoryImpl.write(msg);
        }
    }

    private class Server {

        public Server() {

        }

        private void start() throws Exception {

            IpChannelType ipChannelType = IpChannelType.TCP;
            if (checkSctpEnabled())
                ipChannelType = IpChannelType.SCTP;

            serverSctpManagement.addServer(SERVER_NAME, SERVER_HOST, SERVER_PORT, ipChannelType, null);
            serverSctpManagement.addServerAssociation(CLIENT_HOST, CLIENT_PORT, SERVER_NAME, SERVER_ASSOCIATION_NAME, ipChannelType);
            serverSctpManagement.startServer(SERVER_NAME);
            RoutingContext rc = factory.createRoutingContext(new long[] { 100l });
            TrafficModeType trafficModeType = factory.createTrafficModeType(TrafficModeType.Loadshare);
            remAs = (AsImpl) serverM3uaMgmt.createAs("server-testas", Functionality.IPSP, ExchangeType.SE, IPSPType.SERVER, rc,
                    trafficModeType, 1, null);
            remAspFactory = (AspFactoryImpl) serverM3uaMgmt.createAspFactory("server-testasp", SERVER_ASSOCIATION_NAME, false);
            remAsp = serverM3uaMgmt.assignAspToAs("server-testas", "server-testasp");
            serverM3uaMgmt.addRoute(123, -1, -1, "server-testas");
            serverM3uaMgmt.startAsp("server-testasp");
        }

        public void stop() throws Exception {
            serverM3uaMgmt.stopAsp("server-testasp");
            serverM3uaMgmt.removeRoute(123, -1, -1, "server-testas");
            serverM3uaMgmt.unassignAspFromAs("server-testas", "server-testasp");
            serverM3uaMgmt.destroyAspFactory("server-testasp");
            serverM3uaMgmt.destroyAs("server-testas");
            serverSctpManagement.removeAssociation(SERVER_ASSOCIATION_NAME);
            serverSctpManagement.stopServer(SERVER_NAME);
            serverSctpManagement.removeServer(SERVER_NAME);
        }
    }

    private class Mtp3UserPartListenerImpl implements Mtp3UserPartListener {

        @Override
        public void onMtp3PauseMessage(Mtp3PausePrimitive arg0) {
        }

        @Override
        public void onMtp3ResumeMessage(Mtp3ResumePrimitive arg0) {
        }

        @Override
        public void onMtp3StatusMessage(Mtp3StatusPrimitive arg0) {
        }

        @Override
        public void onMtp3TransferMessage(Mtp3TransferPrimitive value) {
        }

        @Override
        public void onMtp3EndCongestionMessage(Mtp3EndCongestionPrimitive msg) {
        }

    }
    
    private Management getSctpManagement(String s) throws Exception {
        Management sctpManagement = new NettySctpManagementImpl(s);
        sctpManagement.setPersistDir(Util.getTmpTestDir());
        sctpManagement.setSingleThread(true);
        sctpManagement.start();
        sctpManagement.setConnectDelay(1000 * 5);
        sctpManagement.removeAllResourses();
        return sctpManagement;
    }
    
    private M3UAManagementImpl getM3uaManagement(String s, Management sctpManagement) throws Exception {
        M3UAManagementImpl m3uaMgmt = new M3UAManagementImpl(s, null, null);
        m3uaMgmt.setPersistDir(Util.getTmpTestDir());
        m3uaMgmt.setTransportManagement(sctpManagement);
        m3uaMgmt.addMtp3UserPartListener(mtp3UserPartListener);
        m3uaMgmt.start();
        m3uaMgmt.removeAllResourses();
        return m3uaMgmt;
    }
}

