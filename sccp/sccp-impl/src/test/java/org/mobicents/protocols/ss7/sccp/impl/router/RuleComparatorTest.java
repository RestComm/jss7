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
package org.mobicents.protocols.ss7.sccp.impl.router;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 *
 */
public class RuleComparatorTest {

    RuleComparator ruleComparator = new RuleComparator();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test(groups = { "comparator", "functional.sort" })
    public void testSorting() throws Exception {

        SccpAddress pattern1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "800/????/9"), 0);
        RuleImpl rule1 = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern1, "R/K/R");

        RuleImpl rule1a = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.LocalOriginated,
                pattern1, "R/K/R");

        SccpAddress pattern2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "*"), 0);
        RuleImpl rule2 = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern2, "K");

        RuleImpl rule2a = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.LocalOriginated,
                pattern2, "K");

        SccpAddress pattern3 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "9/?/9/*"), 0);
        RuleImpl rule3 = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern3,
                "K/K/K/K");

        RuleImpl rule3a = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.RemoteOriginated,
                pattern3, "K/K/K/K");

        SccpAddress pattern4 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "80/??/0/???/9"), 0);
        RuleImpl rule4 = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern4,
                "R/K/R/K/R");

        SccpAddress pattern5 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "800/?????/9"), 0);
        RuleImpl rule5 = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern5, "R/K/R");

        SccpAddress pattern6 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "123456"), 0);
        RuleImpl rule6 = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern6, "K");

        SccpAddress pattern7 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "1234567890"), 0);
        RuleImpl rule7 = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern7, "R/K/R");

        SccpAddress pattern8 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "999/*"), 0);
        RuleImpl rule8 = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern8, "R/K");

        // This is unsorted
        RuleImpl[] rules = new RuleImpl[] { rule1, rule2, rule3, rule4, rule5, rule6, rule7, rule8, rule1a, rule2a, rule3a };

        // Sort
        Arrays.sort(rules, ruleComparator);

        assertEquals(rule1a, rules[0]);
        assertEquals(rule3a, rules[1]);
        assertEquals(rule2a, rules[2]);

        assertEquals(rule6, rules[3]);
        assertEquals(rule7, rules[4]);
        assertEquals(rule1, rules[5]);
        assertEquals(rule5, rules[6]);
        assertEquals(rule4, rules[7]);
        assertEquals(rule8, rules[8]);
        assertEquals(rule3, rules[9]);
        assertEquals(rule2, rules[10]);
    }
}
