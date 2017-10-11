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

package org.mobicents.protocols.ss7.sccp.impl.message;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.impl.Mtp3UserPartImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class GetMaxUserDataLengthTest {

    private SccpStackImpl stack = new SccpStackImpl("TestStack");

    @BeforeMethod
    public void setUp() {
        stack.setPersistDir(Util.getTmpTestDir());
        stack.start();
        stack.removeAllResourses();
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test(groups = { "SccpMessage", "MessageLength" })
    public void testMessageLength() throws Exception {

        SccpAddress a1 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,2,  8);
        SccpAddress a2 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, stack.getSccpProvider().getParameterFactory().createGlobalTitle("1122334455",0) ,0 ,18);

        Mtp3UserPartImpl_2 mtp3UserPart = new Mtp3UserPartImpl_2();
        stack.setMtp3UserPart(1, mtp3UserPart);
        stack.getRouter().addMtp3ServiceAccessPoint(1, 1, 1, 2, 0, null);

        stack.getRouter().addMtp3Destination(1, 1, 2, 2, 0, 255, 255);

        SccpAddress primaryAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, stack.getSccpProvider().getParameterFactory().createGlobalTitle("1122334455",0),2,
                 18);
        stack.getRouter().addRoutingAddress(1, primaryAddress);
        SccpAddress pattern = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, stack.getSccpProvider().getParameterFactory().createGlobalTitle("1122334455",0), 2, 18);
        stack.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K", 1,
                -1, null, 0, null);

        int len = stack.getSccpProvider().getMaxUserDataLength(a1, a2, 0);
        assertEquals(len, 248);

        len = stack.getSccpProvider().getMaxUserDataLength(a2, a1, 0);
        assertEquals(len, 248);

        stack.getRouter().addLongMessageRule(1, 2, 2, LongMessageRuleType.XUDT_ENABLED);

        len = stack.getSccpProvider().getMaxUserDataLength(a1, a2, 0);
        assertEquals(len, 2560);
        stack.getRouter().removeLongMessageRule(1);
        stack.getRouter().addLongMessageRule(1, 2, 2, LongMessageRuleType.LUDT_ENABLED);

        len = stack.getSccpProvider().getMaxUserDataLength(a1, a2, 0);
        assertEquals(len, 231);

        mtp3UserPart.setMtpMsgLen(4000);
        len = stack.getSccpProvider().getMaxUserDataLength(a1, a2, 0);
        assertEquals(len, 2560);

    }

    private class Mtp3UserPartImpl_2 extends Mtp3UserPartImpl {

        private int mtpMsgLen = 268;

        public void setMtpMsgLen(int mtpMsgLen) {
            this.mtpMsgLen = mtpMsgLen;
        }

        @Override
        public int getMaxUserDataLength(int dpc) {
            return mtpMsgLen;
        }

    }
}
