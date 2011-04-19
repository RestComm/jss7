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

package org.mobicents.protocols.ss7.map.functional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.RouterImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPFunctionalTest extends TestCase {

    private static Logger logger = Logger.getLogger(MAPFunctionalTest.class);
    protected static final String USSD_STRING = "*133#";
    protected static final String USSD_MENU = "Select 1)Wallpaper 2)Ringtone 3)Games";
    private static final int _WAIT_TIMEOUT = 5000;
    
    private SccpStackImpl sccpStack = new SccpStackImpl();
    
    private SccpProvider provider;
    private MAPStackImpl stack1;
    private MAPStackImpl stack2;
    private SccpAddress peer1Address;
    private SccpAddress peer2Address;
    private Client client;
    private Server server;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        RouterImpl router = new RouterImpl();
        sccpStack.setRouter(router);
        sccpStack.start();
        super.setUp();

        this.setupLog4j();

        this.provider = sccpStack.getSccpProvider();

        //create some fake addresses.
        GlobalTitle gt1 = GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "123");
        GlobalTitle gt2 = GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "321");

        peer1Address = new SccpAddress(gt1, 0);
        peer2Address = new SccpAddress(gt2, 0);


        this.stack1 = new MAPStackImpl(provider, peer1Address);
        this.stack2 = new MAPStackImpl(provider, peer2Address);

        this.stack1.start();
        this.stack2.start();
        //create test classes
        this.client = new Client(this.stack1, this, peer1Address, peer2Address);
        this.server = new Server(this.stack2, this, peer2Address, peer1Address);
    }

    private void setupLog4j() {

        InputStream inStreamLog4j = getClass().getResourceAsStream("/log4j.properties");
        Properties propertiesLog4j = new Properties();
        try {
            propertiesLog4j.load(inStreamLog4j);
            PropertyConfigurator.configure(propertiesLog4j);
        } catch (IOException e) {
            e.printStackTrace();
            BasicConfigurator.configure();
        }

        logger.debug("log4j configured");

    }
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */

    @Override
    protected void tearDown() throws Exception {
        sccpStack.stop();
        // TODO Auto-generated method stub
        super.tearDown();
        this.stack1.stop();
        this.stack2.stop();
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
