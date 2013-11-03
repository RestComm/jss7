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

package org.mobicents.protocols.ss7.sccp.impl.translation;

import static org.testng.Assert.assertTrue;

import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.impl.SccpHarness;
import org.mobicents.protocols.ss7.sccp.impl.User;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.GT0011;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * @author baranowb
 */
public class GT0011SccpStackImplTest extends SccpHarness {

    private SccpAddress a1, a2;

    public GT0011SccpStackImplTest() {
    }

    @BeforeClass
    public void setUpClass() throws Exception {
        this.sccpStack1Name = "GT0011TestSccpStack1";
        this.sccpStack2Name = "GT0011TestSccpStack2";
    }

    @AfterClass
    public void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();

    }

    @AfterMethod
    public void tearDown() {
        super.tearDown();
    }

    protected static final String GT1_digits = "1234567890";
    protected static final String GT2_digits = "098764321";

    protected static final String GT1_pattern_digits = "1/???????/90";
    protected static final String GT2_pattern_digits = "0/??????/21";

    @Test(groups = { "gtt", "functional.route" })
    public void testRemoteRoutingBasedOnGT_DPC_SSN() throws Exception {

        GT0011 gt1 = new GT0011(0, NumberingPlan.ISDN_MOBILE, GT1_digits);
        GT0011 gt2 = new GT0011(0, NumberingPlan.ISDN_TELEPHONY, GT2_digits);

        a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt1, getSSN());
        a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt2, getSSN());

        // add addresses to translate
        SccpAddress primary1SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(),
                GlobalTitle.getInstance("-/-/-"), getSSN());
        SccpAddress primary2SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(),
                GlobalTitle.getInstance("-/-/-"), getSSN());

        super.router1.addRoutingAddress(22, primary1SccpAddress);
        super.router2.addRoutingAddress(33, primary2SccpAddress);

        SccpAddress rule1SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, new GT0011(0,
                NumberingPlan.ISDN_TELEPHONY, GT2_pattern_digits), getSSN());
        SccpAddress rule2SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, new GT0011(0,
                NumberingPlan.ISDN_MOBILE, GT1_pattern_digits), getSSN());
        super.router1.addRule(1, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, rule1SccpAddress,
                "K/R/K", 22, -1, null);
        super.router2.addRule(1, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, rule2SccpAddress,
                "R/R/R", 33, -1, null);

        // now create users, we need to override matchX methods, since our rules
        // do kinky stuff with digits, plus
        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN()) {

            protected boolean matchCalledPartyAddress() {
                SccpMessage msg = messages.get(0);
                SccpDataMessage udt = (SccpDataMessage) msg;
                SccpAddress addressToMatch = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(),
                        null, getSSN());
                if (!addressToMatch.equals(udt.getCalledPartyAddress())) {
                    return false;
                }
                return true;
            }

        };
        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN()) {

            protected boolean matchCalledPartyAddress() {
                SccpMessage msg = messages.get(0);
                SccpDataMessage udt = (SccpDataMessage) msg;
                SccpAddress addressToMatch = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, getStack2PC(),
                        new GT0011(0, NumberingPlan.ISDN_TELEPHONY, "021"), getSSN());
                if (!addressToMatch.equals(udt.getCalledPartyAddress())) {
                    return false;
                }
                return true;
            }

        };

        u1.register();
        u2.register();

        u1.send();
        u2.send();

        Thread.currentThread().sleep(3000);

        assertTrue(u1.check(), "Message not received");
        assertTrue(u2.check(), "Message not received");
    }

    @Test(groups = { "gtt", "functional.route" })
    public void testRemoteRoutingBasedOnGT() throws Exception {

        // here we do as above, however receiving stack needs also rule, to
        // match it localy.
        GT0011 gt1 = new GT0011(0, NumberingPlan.ISDN_MOBILE, GT1_digits);
        GT0011 gt2 = new GT0011(0, NumberingPlan.ISDN_TELEPHONY, GT2_digits);

        a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt1, getSSN());
        a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt2, getSSN());

        // add addresses to translate
        SccpAddress primary1SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, getStack2PC(),
                GlobalTitle.getInstance("-/-/-"), getSSN());
        SccpAddress primary2SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, getStack1PC(),
                GlobalTitle.getInstance("-/-/-"), getSSN());
        super.router1.addRoutingAddress(22, primary1SccpAddress);
        super.router2.addRoutingAddress(33, primary2SccpAddress);

        SccpAddress rule1SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, new GT0011(0,
                NumberingPlan.ISDN_TELEPHONY, GT2_pattern_digits), getSSN());
        SccpAddress rule2SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, new GT0011(0,
                NumberingPlan.ISDN_MOBILE, GT1_pattern_digits), getSSN());
        super.router1.addRule(1, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, rule1SccpAddress,
                "K/R/K", 22, -1, null);
        super.router2.addRule(1, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, rule2SccpAddress,
                "R/K/R", 33, -1, null);

        // add rules for incoming messages,

        // 1. add primary addresses
        // NOTE PC passed in address match local PC for stack
        primary1SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(),
                GlobalTitle.getInstance("-/-/-"), getSSN());
        primary2SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(),
                GlobalTitle.getInstance("-/-"), getSSN());
        super.router1.addRoutingAddress(44, primary1SccpAddress);
        super.router2.addRoutingAddress(66, primary2SccpAddress);
        // 2. add rules to make translation to above
        rule1SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, new GT0011(0,
                NumberingPlan.ISDN_MOBILE, "23456/?/8"), getSSN());
        rule2SccpAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, new GT0011(0,
                NumberingPlan.ISDN_TELEPHONY, "02/?"), getSSN());
        super.router1.addRule(2, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, rule1SccpAddress,
                "K/K/K", 44, -1, null);
        super.router2.addRule(2, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, rule2SccpAddress,
                "K/K", 66, -1, null);

        // now create users, we need to override matchX methods, since our rules
        // do kinky stuff with digits, plus
        User u1 = new User(sccpStack1.getSccpProvider(), a1, a2, getSSN()) {

            protected boolean matchCalledPartyAddress() {
                SccpMessage msg = messages.get(0);
                SccpDataMessage udt = (SccpDataMessage) msg;
                // pc=1,ssn=8,gt=GLOBAL_TITLE_INCLUDES_NATURE_OF_ADDRESS_INDICATOR_ONLY
                // 2345678
                SccpAddress addressToMatch = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack1PC(),
                        new GT0011(0, NumberingPlan.ISDN_MOBILE, "2345678"), getSSN());
                if (!addressToMatch.equals(udt.getCalledPartyAddress())) {
                    return false;
                }
                return true;
            }

        };

        User u2 = new User(sccpStack2.getSccpProvider(), a2, a1, getSSN()) {

            protected boolean matchCalledPartyAddress() {
                SccpMessage msg = messages.get(0);
                SccpDataMessage udt = (SccpDataMessage) msg;
                SccpAddress addressToMatch = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, getStack2PC(),
                        new GT0011(0, NumberingPlan.ISDN_TELEPHONY, "021"), getSSN());
                if (!addressToMatch.equals(udt.getCalledPartyAddress())) {
                    return false;
                }
                return true;
            }

        };

        u1.register();
        u2.register();

        u1.send();
        u2.send();

        Thread.currentThread().sleep(3000);

        assertTrue(u1.check(), "Message not received");
        assertTrue(u2.check(), "Message not received");
    }
}
