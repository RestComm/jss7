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
package org.mobicents.protocols.ss7.sccp.impl.router;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
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
    ParameterFactory factory = new ParameterFactoryImpl();
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

        SccpAddress pattern1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("800/????/9", 1), 0, 0);
        RuleImpl rule1 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern1, "R/K/R", 0);

        RuleImpl rule1a = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.LOCAL,
                pattern1, "R/K/R", 0);

        SccpAddress pattern2 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);
        RuleImpl rule2 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern2, "K", 0);

        RuleImpl rule2a = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.LOCAL,
                pattern2, "K", 0);

        SccpAddress pattern3 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("9/?/9/*", 1), 0, 0);
        RuleImpl rule3 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern3,
                "K/K/K/K", 0);

        RuleImpl rule3a = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.REMOTE,
                pattern3, "K/K/K/K", 0);

        SccpAddress pattern4 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("80/??/0/???/9", 1), 0, 0);
        RuleImpl rule4 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern4,
                "R/K/R/K/R", 0);

        SccpAddress pattern5 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("800/?????/9", 1), 0, 0);
        RuleImpl rule5 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern5, "R/K/R", 0);

        SccpAddress pattern6 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("123456", 1), 0, 0);
        RuleImpl rule6 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern6, "K", 0);

        SccpAddress pattern7 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("1234567890", 1), 0, 0);
        RuleImpl rule7 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern7, "R/K/R", 0);

        SccpAddress pattern8 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("999/*", 1), 0, 0);
        RuleImpl rule8 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern8, "R/K", 0);
        

        SccpAddress pattern9 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("9999/*", 1), 0, 0);
        RuleImpl rule9 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern9, "R/K", 0);

        // This is unsorted
        RuleImpl[] rules = new RuleImpl[] { rule1, rule2, rule3, rule4, rule5, rule6, rule7, rule8, rule1a, rule2a, rule3a, rule9 };

        // Sort
        Arrays.sort(rules, ruleComparator);

        assertEquals(rule1a, rules[0]);
        assertEquals(rule3a, rules[1]);
        assertEquals(rule2a, rules[2]);

        assertEquals(rule7, rules[3]);
        assertEquals(rule6, rules[4]);
        assertEquals(rule1, rules[5]);
        assertEquals(rule5, rules[6]);
        assertEquals(rule4, rules[7]);
        assertEquals(rule9, rules[8]);
        assertEquals(rule8, rules[9]);
        assertEquals(rule3, rules[10]);
        assertEquals(rule2, rules[11]);
    }
}
