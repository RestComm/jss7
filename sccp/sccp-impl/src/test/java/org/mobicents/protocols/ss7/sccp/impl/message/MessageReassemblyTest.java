/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.sccp.impl.message;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImplProxy;
import org.mobicents.protocols.ss7.sccp.impl.User;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCauseValue;
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
public class MessageReassemblyTest extends SccpHarness {

    private SccpAddress a1, a2;

    public MessageReassemblyTest() {
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "MessageReassemblyTestStack1";
        this.sccpStack2Name = "MessageReassemblyTestStack2";
    }

    @AfterClass
    public void tearDownClass() throws Exception {
    }

    @Override
    protected void createStack1() {
        this.sccpStack1 = createStack(sccpStack1Name);
        this.sccpProvider1 = sccpStack1.getSccpProvider();
    }

    @Override
    protected void createStack2() {
        this.sccpStack2 = createStack(sccpStack2Name);
        this.sccpProvider2 = sccpStack2.getSccpProvider();
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

    public byte[] getDataXudt1() {
        return new byte[] { 17, (byte) 129, 15, 4, 6, 10, 15, 2, 66, 8, 4, 67, 1, 0, 6, 5, 11, 12, 13, 14, 15, 16, 4,
                (byte) 192, 100, 0, 0, 18, 1, 7, 0 };
    }

    @Test(groups = { "SccpMessage", "functional.decode" })
    public void testReassembly() throws Exception {

        a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), null, 8);
        a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), null, 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        u1.register();
        u2.register();

        sccpStack1.setReassemblyTimerDelay(3000);
        Thread.sleep(100);

        // Receiving a chain of 3 XUDT segments -> success
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(), MessageSegmentationTest.getDataSegm1());
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 1);
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(), MessageSegmentationTest.getDataSegm2());
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 1);
        assertEquals(u1.getMessages().size(), 0);
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(), MessageSegmentationTest.getDataSegm3());
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
        assertEquals(u1.getMessages().size(), 1);
        SccpDataMessage dMsg = (SccpDataMessage) u1.getMessages().get(0);
        assertTrue(Arrays.equals(dMsg.getData(), MessageSegmentationTest.getDataA()));
        assertEquals(u2.getMessages().size(), 0);

        // Receiving a single XUDT message without segments -> success
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(), getDataXudt1());
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
        assertEquals(u1.getMessages().size(), 2);
        dMsg = (SccpDataMessage) u1.getMessages().get(1);
        assertTrue(Arrays.equals(dMsg.getData(), SccpDataMessageTest.getDataXudt1Src()));
        assertEquals(u2.getMessages().size(), 0);

        // Receiving a chain of 3 XUDTS segments -> success
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(),
                MessageSegmentationTest.getDataSegm1_S());
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 1);
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(),
                MessageSegmentationTest.getDataSegm2_S());
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 1);
        assertEquals(u1.getMessages().size(), 2);
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(),
                MessageSegmentationTest.getDataSegm3_S());
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
        assertEquals(u1.getMessages().size(), 3);
        SccpNoticeMessage nMsg = (SccpNoticeMessage) u1.getMessages().get(2);
        assertTrue(Arrays.equals(nMsg.getData(), MessageSegmentationTest.getDataA()));
        assertEquals(u2.getMessages().size(), 0);

        // Receiving an only the first segment of 3--segmented chain of 3 XUDT segments -> timeout
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(), MessageSegmentationTest.getDataSegm1());
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 1);
        Thread.sleep(5000); // waiting for timeout - current timeout is 3 sec
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
        assertEquals(u2.getMessages().size(), 1);
        assertEquals(((SccpNoticeMessage) u2.getMessages().get(0)).getReturnCause().getValue(),
                ReturnCauseValue.CANNOT_REASEMBLE);

        // Receiving an only the second segment of 3--segmented chain of 3 XUDT segments -> error
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(), MessageSegmentationTest.getDataSegm2());
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
        assertEquals(u1.getMessages().size(), 3);
        assertEquals(u2.getMessages().size(), 1);

        // Receiving only th 1 and 3 message from a chain of 3 XUDTS segments -> error
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(),
                MessageSegmentationTest.getDataSegm1_S());
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 1);
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(),
                MessageSegmentationTest.getDataSegm3_S());
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
        assertEquals(u1.getMessages().size(), 3);
        assertEquals(u2.getMessages().size(), 1); // no error for service messages

        // Receiving two chains of 3 XUDT and XUDTS segments -> success
        assertEquals(u1.getMessages().size(), 3);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(), MessageSegmentationTest.getDataSegm1());
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(),
                MessageSegmentationTest.getDataSegm1_S());
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 2);
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(), MessageSegmentationTest.getDataSegm2());
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(),
                MessageSegmentationTest.getDataSegm2_S());
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 2);
        assertEquals(u1.getMessages().size(), 3);
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(), MessageSegmentationTest.getDataSegm3());
        this.mtp3UserPart1.sendTransferMessageToLocalUser(getStack2PC(), getStack1PC(),
                MessageSegmentationTest.getDataSegm3_S());
        Thread.sleep(100);
        assertEquals(((SccpStackImplProxy) this.sccpStack1).getReassemplyCacheSize(), 0);
        assertEquals(u1.getMessages().size(), 5);
        dMsg = (SccpDataMessage) u1.getMessages().get(3);
        nMsg = (SccpNoticeMessage) u1.getMessages().get(4);
        assertTrue(Arrays.equals(dMsg.getData(), MessageSegmentationTest.getDataA()));
        assertTrue(Arrays.equals(nMsg.getData(), MessageSegmentationTest.getDataA()));
        assertEquals(u2.getMessages().size(), 1);

        int i = 1;
        i++;
    }
}
