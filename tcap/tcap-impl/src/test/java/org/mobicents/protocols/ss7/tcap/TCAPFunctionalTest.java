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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * Test for call flow.
 * @author baranowb
 *
 */
public class TCAPFunctionalTest extends SccpHarness {

    private static final int _WAIT_TIMEOUT = 90000;
    public static final long[] _ACN_ = new long[]{0, 4, 0, 0, 1, 0, 19, 2};
    private TCAPStackImpl tcapStack1;
    private TCAPStackImpl tcapStack2;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;
    private Client client;
    private Server server;
    
    public TCAPFunctionalTest(){
    	
    }
    
	@BeforeClass
	public static void setUpClass() throws Exception {
		System.out.println("setUpClass");
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		System.out.println("tearDownClass");
	}

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
	@Before
	public void setUp() throws IllegalStateException {
		System.out.println("setUp");
        super.setUp();
       
        peer1Address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 1, null, 8);
        peer2Address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 2,  null, 8);
        
        this.tcapStack1 = new TCAPStackImpl(this.sccpProvider1, 8);
        this.tcapStack2 = new TCAPStackImpl(this.sccpProvider2, 8);
        
        this.tcapStack1.start();
        this.tcapStack2.start();
        //create test classes
        this.client = new Client(this.tcapStack1, peer1Address, peer2Address);
        this.server = new Server(this.tcapStack2, peer2Address, peer1Address);

    }
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
	@After
	public void tearDown() {
        this.tcapStack1.stop();
        this.tcapStack2.stop();
        super.tearDown();

    }
	
    @Test
    public void testSimpleTCWithDialog() throws Exception {
        client.start();
        waitForEnd();
        assertTrue("Client side did not finish: " + client.getStatus(), client.isFinished());
        assertTrue("Server side did not finish: " + server.getStatus(), server.isFinished());
    }

    private void waitForEnd() {
        try {
            Thread.currentThread().sleep(_WAIT_TIMEOUT);
        } catch (InterruptedException e) {
            fail("Interrupted on wait!");
        }
    }
}
