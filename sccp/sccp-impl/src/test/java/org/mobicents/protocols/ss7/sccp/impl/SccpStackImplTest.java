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

package org.mobicents.protocols.ss7.sccp.impl;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.impl.router.RouterImpl;
import org.mobicents.protocols.ss7.sccp.message.MessageFactory;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.message.UnitData;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author kulikov
 */
public class SccpStackImplTest {

    private SccpStackImpl stack = new SccpStackImpl();
    private SccpAddress a1, a2;
    private RouterImpl router = new RouterImpl();
    
    public SccpStackImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws IllegalStateException {
    	
		try {
			router.start();
			router.deleteRule("Rule1");
			router.deleteRule("Rule2");
			router.deleteRule("Rule3");
			router.deleteRule("Rule4");
			router.stop();
		} catch (Exception e) {
			// ignore
		}
		
        router.start();
        
        stack.setRouter(router);
        stack.start();
        
        GlobalTitle gt1 = GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "1234");
        GlobalTitle gt2 = GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "5678");
        
        a1 = new SccpAddress(gt1, 0);
        a2 = new SccpAddress(gt2, 0);
    }

    @After
    public void tearDown() {
        stack.stop();
    }

    /**
     * Test of configure method, of class SccpStackImpl.
     */
    @Test
    public void testLocalRouting() throws Exception {
        User u1 = new User(stack.getSccpProvider(), a1, a2);
        User u2 = new User(stack.getSccpProvider(), a2, a1);
        
        u1.send();
        u2.send();
        
        Thread.currentThread().sleep(1000);
        
        assertTrue("Message not received", u1.check());
        assertTrue("Message not received", u2.check());
    }

    private class User implements SccpListener {
        private SccpProvider provider;
        private SccpAddress address;
        private SccpAddress dest;
        
        private SccpMessage msg;
        
        public User(SccpProvider provider, SccpAddress address, SccpAddress dest) {
            this.provider = provider;
            this.address = address;
            this.dest = dest;
            provider.registerSccpListener(address, this);
        }
        
        public boolean check() {
            if (msg == null) {
                return false;
            }
            
            if (msg.getType() != UnitData.MESSAGE_TYPE) {
                return false;
            }
            
            UnitData udt = (UnitData) msg;
            if (!address.equals(udt.getCalledPartyAddress())) {
                return false;
            }
            
            if (!dest.equals(udt.getCallingPartyAddress())) {
                return false;
            }
            
            return true;
        }
        
        private void send() throws IOException {
            MessageFactory messageFactory = provider.getMessageFactory();
            ParameterFactory paramFactory = provider.getParameterFactory();
            
            ProtocolClass pClass = paramFactory.createProtocolClass(0, 0);
            UnitData udt = messageFactory.createUnitData(pClass, address, dest);
            udt.setData(new byte[10]);
            provider.send(udt);
        }
        
        public void onMessage(SccpMessage message) {
            this.msg = message;
        }
        
    }
}