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

package org.mobicents.protocols.ss7.sccp.impl.messageflow;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImplProxy;
import org.mobicents.protocols.ss7.sccp.impl.User;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageSegmentationTest;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.mobicents.protocols.ss7.sccp.parameter.GT0001;
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
public class MessageTransferTest extends SccpHarness {

    private SccpAddress a1, a2;

    public MessageTransferTest() {
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "MessageTransferTestStack1";
        this.sccpStack2Name = "MessageTransferTestStack2";
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

        a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), null, 8);
        a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), null, 8);

        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN());
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN());

        u1.register();
        u2.register();

        Thread.sleep(100);

        // a try of transfer a UDT message with a big length : UDTS should
        // return locally
        sccpStack1.getRouter().addLongMessageRule(1, 2, 2, LongMessageRuleType.LongMessagesForbidden);
        SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(a2, a1,
                MessageSegmentationTest.getDataA(), 0, 8, true, null, null);
        sccpProvider1.send(message);
        Thread.sleep(100);
        assertEquals(u1.getMessages().size(), 1);
        assertEquals(u2.getMessages().size(), 0);
        SccpNoticeMessage nMsg = (SccpNoticeMessage) u1.getMessages().get(0);
        assertEquals(nMsg.getReturnCause().getValue(), ReturnCauseValue.SEG_NOT_SUPPORTED);

        // transfer a UDT message: U1 -> U2
        message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(a2, a1, getDataSrc(), 0, 8, true, null, null);
        sccpProvider1.send(message);
        Thread.sleep(100);
        assertEquals(u1.getMessages().size(), 1);
        assertEquals(u2.getMessages().size(), 1);
        SccpDataMessage dMsg = (SccpDataMessage) u2.getMessages().get(0);
        assertTrue(Arrays.equals(dMsg.getData(), getDataSrc()));
        assertEquals(dMsg.getType(), SccpMessage.MESSAGE_TYPE_UDT);

        // transfer a UDT message: U1 -> U1
        message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(a1, a1, getDataSrc(), 0, 8, true, null, null);
        sccpProvider1.send(message);
        Thread.sleep(100);
        assertEquals(u1.getMessages().size(), 2);
        assertEquals(u2.getMessages().size(), 1);
        dMsg = (SccpDataMessage) u1.getMessages().get(1);
        assertTrue(Arrays.equals(dMsg.getData(), getDataSrc()));
        assertEquals(dMsg.getType(), SccpMessage.MESSAGE_TYPE_UNDEFINED); // message
                                                                          // type
                                                                          // has
                                                                          // not
                                                                          // assigned
                                                                          // (there
                                                                          // was
                                                                          // no
                                                                          // transfer
                                                                          // via
                                                                          // MTP3)

        // attempt to transfer a UDT message: U1 -> U1(unregistered ssn) ->
        // error
        SccpAddress a1_1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(), null, 18);
        message = this.sccpProvider1.getMessageFactory()
                .createDataMessageClass1(a1_1, a1, getDataSrc(), 0, 8, true, null, null);
        sccpProvider1.send(message);
        Thread.sleep(100);
        assertEquals(u1.getMessages().size(), 3);
        assertEquals(u2.getMessages().size(), 1);
        nMsg = (SccpNoticeMessage) u1.getMessages().get(2);
        assertTrue(Arrays.equals(nMsg.getData(), getDataSrc()));
        assertEquals(nMsg.getReturnCause().getValue(), ReturnCauseValue.SUBSYSTEM_FAILURE);

        // attempt to transfer a UDT message: U1 -> U2(unregistered ssn at U1)
        // -> error
        SccpAddress a2_1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(), null, 18);
        message = this.sccpProvider1.getMessageFactory()
                .createDataMessageClass1(a2_1, a1, getDataSrc(), 0, 8, true, null, null);
        sccpProvider1.send(message);
        Thread.sleep(100);
        assertEquals(u1.getMessages().size(), 4);
        assertEquals(u2.getMessages().size(), 1);
        nMsg = (SccpNoticeMessage) u1.getMessages().get(3);
        assertTrue(Arrays.equals(nMsg.getData(), getDataSrc()));
        assertEquals(nMsg.getReturnCause().getValue(), ReturnCauseValue.SCCP_FAILURE);

        // transfer a splitted XUDT message: U1 -> U2 - success
        sccpStack1.getRouter().removeLongMessageRule(1);
        sccpStack1.getRouter().addLongMessageRule(1, 2, 2, LongMessageRuleType.XudtEnabled);
        message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(a2, a1, MessageSegmentationTest.getDataA(), 0,
                8, true, null, null);
        sccpProvider1.send(message);
        Thread.sleep(100);
        assertEquals(u1.getMessages().size(), 4);
        assertEquals(u2.getMessages().size(), 2);
        dMsg = (SccpDataMessage) u2.getMessages().get(1);
        assertTrue(Arrays.equals(dMsg.getData(), MessageSegmentationTest.getDataA()));
        assertEquals(dMsg.getType(), SccpMessage.MESSAGE_TYPE_XUDT);

        // transfer a long LUDT message: U1 -> U2 - success
        sccpStack1.getRouter().removeLongMessageRule(1);
        sccpStack1.getRouter().addLongMessageRule(1, 2, 2, LongMessageRuleType.LudtEnabled);
        message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(a2, a1, MessageSegmentationTest.getDataA(), 0,
                8, true, null, null);
        sccpProvider1.send(message);
        Thread.sleep(100);
        assertEquals(u1.getMessages().size(), 4);
        assertEquals(u2.getMessages().size(), 3);
        dMsg = (SccpDataMessage) u2.getMessages().get(2);
        assertTrue(Arrays.equals(dMsg.getData(), MessageSegmentationTest.getDataA()));
        assertEquals(dMsg.getType(), SccpMessage.MESSAGE_TYPE_LUDT);

        // attempt to transfer a LUDT message: U1 -> U2 - bad translation at U1
        GT0001 gt1 = new GT0001(NatureOfAddress.NATIONAL, "12345");
        SccpAddress a2_2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt1, 18);
        message = this.sccpProvider1.getMessageFactory()
                .createDataMessageClass1(a2_2, a1, getDataSrc(), 0, 8, true, null, null);
        sccpProvider1.send(message);
        Thread.sleep(100);
        assertEquals(u1.getMessages().size(), 5);
        assertEquals(u2.getMessages().size(), 3);
        nMsg = (SccpNoticeMessage) u1.getMessages().get(4);
        assertTrue(Arrays.equals(nMsg.getData(), getDataSrc()));
        assertEquals(nMsg.getReturnCause().getValue(), ReturnCauseValue.NO_TRANSLATION_FOR_ADDRESS);

        // attempt to transfer a LUDT message: U1 -> U2 - bad translation at U2
        SccpAddress a2_3 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, getStack2PC(), gt1, 18);
        message = this.sccpProvider1.getMessageFactory()
                .createDataMessageClass1(a2_3, a1, getDataSrc(), 0, 8, true, null, null);
        sccpProvider1.send(message);
        Thread.sleep(100);
        assertEquals(u1.getMessages().size(), 6);
        assertEquals(u2.getMessages().size(), 3);
        nMsg = (SccpNoticeMessage) u1.getMessages().get(5);
        assertTrue(Arrays.equals(nMsg.getData(), getDataSrc()));
        assertEquals(nMsg.getReturnCause().getValue(), ReturnCauseValue.NO_TRANSLATION_FOR_ADDRESS);
    }
}
