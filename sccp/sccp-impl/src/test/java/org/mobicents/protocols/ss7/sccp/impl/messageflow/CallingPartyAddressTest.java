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

package org.mobicents.protocols.ss7.sccp.impl.messageflow;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImplProxy;
import org.mobicents.protocols.ss7.sccp.impl.User;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CallingPartyAddressTest extends SccpHarness {

    private SccpAddress a1, a2;

    public CallingPartyAddressTest() {
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "CallingPartyAddressTestStack1";
        this.sccpStack2Name = "CallingPartyAddressTestStack2";
    }

    @AfterClass
    public void tearDownClass() throws Exception {
    }

    protected void createStack1() {
        sccpStack1 = createStack(sccpStack1Name);
        sccpProvider1 = sccpStack1.getSccpProvider();
    }

    protected void createStack2() {
        sccpStack2 = createStack(sccpStack2Name);
        sccpProvider2 = sccpStack2.getSccpProvider();
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

        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,
                getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,
                getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        u1.register();
        u2.register();

        Thread.sleep(100);

        // no newCallingPartyAddress
        SccpAddress primaryAddress = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), getStack2PC(), 8);
        sccpStack1.getRouter().addRoutingAddress(1, primaryAddress);
        SccpAddress newCallingPartyAddress = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("222222", 1), 0, 8);
        sccpStack1.getRouter().addRoutingAddress(2, newCallingPartyAddress);
        SccpAddress pattern = sccpProvider1.getParameterFactory().createSccpAddress(
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), 0, 0);
        sccpStack1.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K",
                1, -1, null, 0);

        SccpAddress a3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), 0, 0);
        SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(a3, a1, getDataSrc(), 0, 8,
                true, null, null);
        sccpProvider1.send(message);
        Thread.sleep(100);

        assertEquals(u1.getMessages().size(), 0);
        assertEquals(u2.getMessages().size(), 1);
        SccpDataMessage dMsg = (SccpDataMessage) u2.getMessages().get(0);
        assertNull(dMsg.getCallingPartyAddress().getGlobalTitle());

        // present newCallingPartyAddress
        sccpStack1.getRouter().removeRule(1);
        sccpStack1.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K",
                1, -1, 2, 0);

        message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(a3, a1, getDataSrc(), 0, 8, true, null, null);
        sccpProvider1.send(message);
        Thread.sleep(100);

        assertEquals(u1.getMessages().size(), 0);
        assertEquals(u2.getMessages().size(), 2);
        dMsg = (SccpDataMessage) u2.getMessages().get(1);
        assertTrue(dMsg.getCallingPartyAddress().getGlobalTitle().getDigits().equals("222222"));
    }
}
