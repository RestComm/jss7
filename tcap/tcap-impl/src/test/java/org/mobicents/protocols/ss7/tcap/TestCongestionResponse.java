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

package org.mobicents.protocols.ss7.tcap;

import static org.testng.Assert.*;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author nosach konstantin
 *
 */
public class TestCongestionResponse extends SccpHarness {
    public static final long WAIT_TIME = 1000;

    private TCAPStackImpl tcapStack1;
    private TCAPStackImpl tcapStack2;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;
    private Client client;
    private Server server;
    private TestSccpListener sccpListener;

    public TestCongestionResponse() {
    }

    @BeforeClass
    public void setUpClass() {
        this.sccpStack1Name = "TCAPCongestionTestSccpStack1";
        this.sccpStack2Name = "TCAPCongestionTestSccpStack2";
        System.out.println("setUpClass");
    }

    @AfterClass
    public void tearDownClass() throws Exception {
        System.out.println("tearDownClass");
    }

    @BeforeMethod
    public void setUp() throws Exception {
        System.out.println("setUp");
        super.setUp();

        peer1Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
        peer2Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

        sccpListener = new TestSccpListener();
        this.sccpProvider1.registerSccpListener(1, sccpListener);
        ((SccpStackImpl)this.sccpProvider1.getSccpStack()).setCongControlM(1);
        this.tcapStack1 = new TCAPStackImpl("TCAPCongestionTest1", this.sccpProvider1, 8);
        this.tcapStack2 = new TCAPStackImpl("TCAPCongestionTest2", this.sccpProvider2, 8);

        
        this.tcapStack1.start();
        this.tcapStack2.start();
        
        this.tcapStack1.setDoNotSendProtocolVersion(false);
        this.tcapStack2.setDoNotSendProtocolVersion(false);
        this.tcapStack1.setInvokeTimeout(0);
        this.tcapStack2.setInvokeTimeout(0);

        this.client = new Client(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address);
        this.server = new Server(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address);

    }

    @AfterMethod
    public void tearDown() {
        this.tcapStack1.stop();
        this.tcapStack2.stop();
        super.tearDown();

    }

    @Test(groups = { "congestion" })
    public void simpleTest() throws Exception {
    	 this.tcapStack2.setCongControl_MemoryThreshold_1(77);
         this.tcapStack2.setCongControl_BackToNormalMemoryThreshold_1(72);
         this.tcapStack2.setCongControl_MemoryThreshold_2(87);
         this.tcapStack2.setCongControl_BackToNormalMemoryThreshold_2(82);
         this.tcapStack2.setCongControl_blockingIncomingTcapMessages(false);

        // user congestion
        this.tcapStack2.getProvider().setUserPartCongestionLevel("a1", 1);
        client.waitFor(1100);
        client.startClientDialog();
        client.sendBegin();
        client.waitFor(WAIT_TIME);
        server.sendContinue();
        client.waitFor(WAIT_TIME);

        client.releaseDialog();
        server.releaseDialog();
        assertEquals(server.observerdEvents.size(), 3);
        assertEquals(client.observerdEvents.size(), 3);

        client.startClientDialog();
        client.sendBegin();
        client.waitFor(WAIT_TIME);
        client.releaseDialog();
        server.releaseDialog();
        assertEquals(server.observerdEvents.size(), 5);
        assertEquals(client.observerdEvents.size(), 5);
        assertTrue(sccpListener.isCongestedStatusReceived());

    }

}
