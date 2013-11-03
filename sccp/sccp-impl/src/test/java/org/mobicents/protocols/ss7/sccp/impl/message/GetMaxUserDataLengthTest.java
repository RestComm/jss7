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

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.impl.Mtp3UserPartImpl;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

        SccpAddress a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 2, null, 8);
        SccpAddress a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(0,
                "1122334455"), 18);

        Mtp3UserPartImpl_2 mtp3UserPart = new Mtp3UserPartImpl_2();
        stack.setMtp3UserPart(1, mtp3UserPart);
        stack.getRouter().addMtp3ServiceAccessPoint(1, 1, 1, 2);

        stack.getRouter().addMtp3Destination(1, 1, 2, 2, 0, 255, 255);

        SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 2,
                GlobalTitle.getInstance(0, "1122334455"), 18);
        stack.getRouter().addRoutingAddress(1, primaryAddress);
        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 2, GlobalTitle.getInstance(0,
                "1122334455"), 18);
        stack.getRouter().addRule(1, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "K", 1,
                -1, null);

        int len = stack.getSccpProvider().getMaxUserDataLength(a1, a2);
        assertEquals(len, 248);

        len = stack.getSccpProvider().getMaxUserDataLength(a2, a1);
        assertEquals(len, 248);

        stack.getRouter().addLongMessageRule(1, 2, 2, LongMessageRuleType.XudtEnabled);

        len = stack.getSccpProvider().getMaxUserDataLength(a1, a2);
        assertEquals(len, 2560);
        stack.getRouter().removeLongMessageRule(1);
        stack.getRouter().addLongMessageRule(1, 2, 2, LongMessageRuleType.LudtEnabled);

        len = stack.getSccpProvider().getMaxUserDataLength(a1, a2);
        assertEquals(len, 231);

        mtp3UserPart.setMtpMsgLen(4000);
        len = stack.getSccpProvider().getMaxUserDataLength(a1, a2);
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
