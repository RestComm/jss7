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

package org.restcomm.protocols.ss7.sccp.impl.messageflow;

import static org.testng.Assert.assertEquals;

import org.restcomm.protocols.ss7.Util;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.restcomm.protocols.ss7.sccp.OriginationType;
import org.restcomm.protocols.ss7.sccp.Router;
import org.restcomm.protocols.ss7.sccp.RuleType;
import org.restcomm.protocols.ss7.sccp.SccpProvider;
import org.restcomm.protocols.ss7.sccp.SccpResource;
import org.restcomm.protocols.ss7.sccp.impl.AdvancedUser;
import org.restcomm.protocols.ss7.sccp.impl.Mtp3UserPartImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImplProxy;
import org.restcomm.protocols.ss7.sccp.impl.User;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Nosach Kostyantyn
 *
 */
public class RoutingOnGtWithPrimAndSecAddrTest extends SccpHarness {

    private SccpAddress a1, a2, a3;
    private SccpAddress addr1, addr2, addr3;
    private String sccpStack3Name;
    private SccpStackImpl sccpStack3;
    protected SccpProvider sccpProvider3;
    protected Mtp3UserPartImpl mtp3UserPart3 = new Mtp3UserPartImpl(this);
    protected Router router3 = null;
    protected SccpResource resource3 = null;
    

    public RoutingOnGtWithPrimAndSecAddrTest() {
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "MessageTransferTestStack1";
        this.sccpStack2Name = "MessageTransferTestStack2";
        this.sccpStack3Name = "MessageTransferTestStack3";
    }

    @AfterClass
    public void tearDownClass() throws Exception {
    }

    protected void createStack1() {
        sccpStack1 = createStack(sccpStack1Name);
        sccpProvider1 = sccpStack1.getSccpProvider();
        sccpStack1.start();
    }

    protected void createStack2() {
        sccpStack2 = createStack(sccpStack2Name);
        sccpProvider2 = sccpStack2.getSccpProvider();
        sccpStack2.start();
        try {
            sccpStack2.setRespectPc(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected SccpStackImpl createStack(String name) {
        SccpStackImpl stack = new SccpStackImplProxy(name);
        final String dir = Util.getTmpTestDir();
        if (dir != null) {
            stack.setPersistDir(dir);
        }
        return stack;
    }

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        createStack3();
    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();
    }

    public byte[] getDataSrc() {
        return new byte[] { 11, 12, 13, 14, 15 };
    }

    @Test(groups = { "SccpMessage", "functional.transfer" })
    public void testTransfer() throws Exception {
        
        mtp3UserPart1.addDpc(1);
        mtp3UserPart3.addDpc(3);
        
        mtp3UserPart3.setOtherPart(mtp3UserPart2);
        mtp3UserPart2.setOtherPart(mtp3UserPart3);

        GlobalTitle gt1 = sccpProvider1.getParameterFactory().createGlobalTitle("11111",NatureOfAddress.NATIONAL);
        GlobalTitle gt2 = sccpProvider1.getParameterFactory().createGlobalTitle("22222",NatureOfAddress.NATIONAL);
        GlobalTitle gt3 = sccpProvider1.getParameterFactory().createGlobalTitle("33333",NatureOfAddress.NATIONAL);
        
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, -1, 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, -1, 8);
        a3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt3, -1, 8);
        
        addr1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, 2, 8);
        addr2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 1, 8);
        addr3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt3, 3, 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new AdvancedUser(sccpStack2.getSccpProvider(), a2, a1, getSSN());
        User u3 = new User(sccpStack3.getSccpProvider(), a3, a2, getSSN());
        
        
        LoadSharingAlgorithm algo = LoadSharingAlgorithm.valueOf("Undefined");
        SccpAddress pattern1 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, gt2, -1, 8);
        SccpAddress pattern2 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, gt1, -1, 8);
        sccpStack2.getRouter().addRoutingAddress(0, addr1);
        sccpStack2.getRouter().addRoutingAddress(1, addr2);
        sccpStack2.getRouter().addRoutingAddress(2, addr3);
        
        resource2.addRemoteSpc(1, 3, 0, 0);
        resource2.addRemoteSsn(2, 3, getSSN(), 0, false);
        sccpStack2.getRouter().addMtp3Destination(1, 2, 3, 3, 0, 255, 255);
        sccpStack2.getRouter().addRule(1, RuleType.valueOf("SOLITARY"), algo, OriginationType.REMOTE, pattern1, "K", 0, -1, null, 0, null);
        sccpStack2.getRouter().addRule(2, RuleType.valueOf("DOMINANT"), algo, OriginationType.LOCAL, pattern2, "K", 2, 1, null, 0, null);

        sccpStack1.getRouter().addRoutingAddress(0, addr2);
        sccpStack1.getRouter().addRule(1, RuleType.valueOf("SOLITARY"), algo, OriginationType.REMOTE, pattern2, "K", 0, -1, null, 0, null);
        sccpStack1.getRouter().addRoutingAddress(1, addr1);
        sccpStack1.getRouter().addRule(2, RuleType.valueOf("SOLITARY"), algo, OriginationType.LOCAL, pattern1, "K", 1, -1, null, 0, null);
        
        u1.register();
        u2.register();
        u3.register();

        Thread.sleep(100);

        SccpDataMessage message = null;
        message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(a2, a1, getDataSrc(), 0, 8, true, null, null);
        sccpProvider1.send(message);
        Thread.sleep(1000);
        assertEquals(u2.getMessages().size(), 1);
        assertEquals(u1.getMessages().size(), 1);
    }
    
    private void createStack3() throws Exception {
        sccpStack3 = createStack(sccpStack3Name);
        sccpStack3.setMtp3UserPart(1, mtp3UserPart3);
        sccpStack3.start();
        sccpStack3.removeAllResourses();
        sccpStack3.getRouter().addMtp3ServiceAccessPoint(1, 1, 3, 2, 0, null);
        sccpStack3.getRouter().addMtp3Destination(1, 1, getStack2PC(), getStack2PC(), 0, 255, 255);
        sccpProvider3 = sccpStack3.getSccpProvider();
        router3 = sccpStack3.getRouter();
        resource3 = sccpStack3.getSccpResource();
        resource3.addRemoteSpc(1, getStack2PC(), 0, 0);
        resource3.addRemoteSsn(1, getStack2PC(), getSSN(), 0, false);
    }
}

