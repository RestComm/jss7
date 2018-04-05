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

package org.restcomm.protocols.ss7.sccp.impl.router;

import static org.testng.Assert.assertEquals;

import org.restcomm.protocols.ss7.Util;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.Mtp3UserPartImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImplProxy;
import org.restcomm.protocols.ss7.sccp.impl.User;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtInterface;
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
public class RouteOnGtTest extends SccpHarness {

    private SccpAddress a1, a2;

    public RouteOnGtTest() {
    }

    public byte[] getDataSrc() {
        return new byte[] { 11, 12, 13, 14, 15 };
    }

    public byte[] getDataUdt1() {
        return new byte[] { 0x09, (byte) 0x81, 0x03, 0x0e, 0x19, 0x0b, 0x12, 0x06, 0x00, 0x12, 0x04, 0x45, 0x23, 0x00, 0x21, 0x00,
                0x50, 0x0b, 0x12, 0x07, 0x00, 0x11, 0x04, 0x79, 0x52, 0x14, 0x02, 0x26, 0x05, 0x5b, 0x62, 0x59, 0x48, 0x04,
                0x00, 0x00, 0x00, 0x05, 0x6b, 0x1e, 0x28, 0x1c, 0x06, 0x07, 0x00, 0x11, (byte) 0x86, 0x05, 0x01, 0x01, 0x01,
                (byte) 0xa0, 0x11, 0x60, 0x0f, (byte) 0x80, 0x02, 0x07, (byte) 0x80, (byte) 0xa1, 0x09, 0x06, 0x07, 0x04, 0x00,
                0x00, 0x01, 0x00, 0x05, 0x03, 0x6c, 0x31, (byte) 0xa1, 0x2f, 0x02, 0x01, 0x01, 0x02, 0x01, 0x16, 0x30, 0x27,
                (byte) 0x80, 0x08, (byte) 0x91, 0x45, 0x19, 0x61, 0x34, 0x11, 0x79, (byte) 0xf7, (byte) 0x83, 0x01, 0x00,
                (byte) 0x86, 0x07, (byte) 0x91, 0x79, 0x52, 0x14, 0x02, 0x26, (byte) 0xf5, (byte) 0x87, 0x05, 0x7b,
                (byte) 0xb6, (byte) 0xa0, 0x41, 0x56, (byte) 0xab, 0x08, 0x03, 0x02, 0x04, (byte) 0xf0, (byte) 0x80, 0x02,
                0x01, (byte) 0xfe };
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "RouteOnGtTestStack1";
        this.sccpStack2Name = "RouteOnGtTestStack2";
    }

    @AfterClass
    public void tearDownClass() throws Exception {
    }

    @Override
    protected void createStack1() {
        sccpStack1 = createStack(sccpStack1Name, null);
        sccpProvider1 = sccpStack1.getSccpProvider();
    }

    @Override
    protected void createStack2() {
        sccpStack2 = createStack(sccpStack2Name, null);
        sccpProvider2 = sccpStack2.getSccpProvider();
    }

    @Override
    protected SccpStackImpl createStack(String name, Ss7ExtInterface ss7ExtInterface) {
        SccpStackImpl stack = new SccpStackImplProxy(name, ss7ExtInterface);
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

    @Test(groups = { "SccpMessage", "functional.transfer" })
    public void testSend() throws Exception {
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        u1.register();
        u2.register();

        SccpAddress a3 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                sccpProvider1.getParameterFactory().createGlobalTitle("111111", 1), 0, 0);

        SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(a3, a1, getDataSrc(), 0, 8,
                true, null, null);
        sccpProvider1.send(message);
        Thread.sleep(100);
        assertEquals(u1.getMessages().size(), 1);
        assertEquals(u2.getMessages().size(), 0);

        SccpMessage mes1 = u1.getMessages().get(0);
        SccpNoticeMessage mes11 = (SccpNoticeMessage) mes1;
        ReturnCause returnCause = mes11.getReturnCause();
        assertEquals(returnCause.getValue(), ReturnCauseValue.ERR_IN_LOCAL_PROCESSING);
    }

    @Test(groups = { "SccpMessage", "functional.transfer" })
    public void testReceive() throws Exception {
        a1 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack1PC(), 8);
        a2 = sccpProvider1.getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, getStack2PC(), 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        u1.register();
        u2.register();

        Mtp3UserPartImpl mup = (Mtp3UserPartImpl) sccpStack1.getMtp3UserPart(1);
        mup.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(), getDataUdt1());

        Thread.sleep(100);
        assertEquals(u1.getMessages().size(), 0);
        assertEquals(u2.getMessages().size(), 0);

    }

}
