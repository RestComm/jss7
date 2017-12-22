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

import java.util.Arrays;

import static org.testng.Assert.assertEquals;

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

        SccpAddress patternDefaultCalling = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);
        SccpAddress pattern1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("800/????/9", 1), 0, 0);
        RuleImpl rule1 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern1, "R/K/R", 0, patternDefaultCalling);

        RuleImpl rule1a = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.LOCAL,
                pattern1, "R/K/R", 0, patternDefaultCalling);

        SccpAddress pattern2 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);
        RuleImpl rule2 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern2, "K", 0, patternDefaultCalling);

        RuleImpl rule2a = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.LOCAL,
                pattern2, "K", 0, patternDefaultCalling);

        SccpAddress pattern3 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("9/?/9/*", 1), 0, 0);
        RuleImpl rule3 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern3,
                "K/K/K/K", 0, patternDefaultCalling);

        RuleImpl rule3a = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.REMOTE,
                pattern3, "K/K/K/K", 0, patternDefaultCalling);

        SccpAddress pattern4 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("80/??/0/???/9", 1), 0, 0);
        RuleImpl rule4 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern4,
                "R/K/R/K/R", 0, patternDefaultCalling);

        SccpAddress pattern5 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("800/?????/9", 1), 0, 0);
        RuleImpl rule5 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern5, "R/K/R", 0, patternDefaultCalling);

        SccpAddress pattern6 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("123456", 1), 0, 0);
        RuleImpl rule6 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern6, "K", 0, patternDefaultCalling);

        SccpAddress pattern7 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("1234567890", 1), 0, 0);
        RuleImpl rule7 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern7, "R/K/R", 0, patternDefaultCalling);

        SccpAddress pattern8 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("999/*", 1), 0, 0);
        RuleImpl rule8 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern8, "R/K", 0, patternDefaultCalling);
        

        SccpAddress pattern9 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("9999/*", 1), 0, 0);
        RuleImpl rule9 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern9, "R/K", 0, patternDefaultCalling);

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


        // test for checking sorting on callingAddress
        SccpAddress patternCallingDigits1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("8888", 1), 0, 0);
        pattern1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("1234", 1), 0, 0);
        rule1 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern1, "K", 0, null);

        pattern2 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("1234", 1), 0, 0);
        rule2 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern2, "K", 0, patternCallingDigits1);

        pattern3 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("5678*", 1), 0, 0);
        rule3 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern3, "K", 0, null);

        pattern4 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("12234", 1), 0, 0);
        rule4 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern4, "K", 0, null);

        SccpAddress patternCallingDigits5 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("88889", 1), 0, 0);
        pattern5 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("12234", 1), 0, 0);
        rule5 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern5, "K", 0, patternCallingDigits5);

        rules = new RuleImpl[] { rule1, rule5, rule2, rule3, rule4};
        Arrays.sort( rules, ruleComparator );
        assertEquals( rule5, rules[0]);
        assertEquals( rule4, rules[1]);
        assertEquals( rule2, rules[2]);
        assertEquals( rule1, rules[3]);
        assertEquals( rule3, rules[4]);
    }

    @Test(groups = { "comparator", "functional.sort" })
    public void testSortingById() throws Exception {

        SccpAddress patternDefaultCalling = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);
        SccpAddress pattern1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("800/????/9", 1), 0, 0);
        RuleImpl rule1 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern1, "R/K/R", 0, patternDefaultCalling);
        rule1.setRuleId( 5 );

        SccpAddress pattern2 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);
        RuleImpl rule2 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern2, "K", 0, patternDefaultCalling);

        rule2.setRuleId( 3 );


        SccpAddress pattern3 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("9/?/9/*", 1), 0, 0);
        RuleImpl rule3 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern3,
                "K/K/K/K", 0, patternDefaultCalling);
        rule3.setRuleId( 4 );

        SccpAddress pattern4 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("80/??/0/???/9", 1), 0, 0);
        RuleImpl rule4 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern4,
                "R/K/R/K/R", 0, patternDefaultCalling);
        rule4.setRuleId( 1 );

        SccpAddress patternCallingDigits1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("8888", 1), 0, 0);
        SccpAddress pattern5 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("1234", 1), 0, 0);
        RuleImpl rule5 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern5, "K", 0, null);
        rule5.setRuleId( 2 );

        SccpAddress pattern6 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("1234", 1), 0, 0);
        RuleImpl rule6 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern6, "K", 0, patternCallingDigits1);
        rule6.setRuleId( 7 );

        SccpAddress pattern7 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("5678*", 1), 0, 0);
        RuleImpl rule7 = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern7, "K", 0, null);
        rule7.setRuleId( 6 );

        // This is unsorted
        RuleImpl[] rules = new RuleImpl[] { rule1, rule2, rule3, rule4, rule5, rule6, rule7};

        RuleByIdComparator ruleByIdComparator = new RuleByIdComparator();
        // Sort
        Arrays.sort(rules, ruleByIdComparator);

        assertEquals(rule1, rules[4]);
        assertEquals(rule2, rules[2]);
        assertEquals(rule3, rules[3]);
        assertEquals(rule4, rules[0]);
        assertEquals(rule5, rules[1]);
        assertEquals(rule6, rules[6]);
        assertEquals(rule7, rules[5]);
    }
}
