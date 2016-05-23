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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;
import org.mobicents.protocols.ss7.mtp.RoutingLabelFormat;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.LongMessageRule;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.Mtp3Destination;
import org.mobicents.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.Router;
import org.mobicents.protocols.ss7.sccp.Rule;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.SccpCongestionControlAlgo;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpResource;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.ss7.congestion.ExecutorCongestionMonitor;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class RouterTest {

    private SccpAddress primaryAddr1, primaryAddr2;

    private RouterImpl router = null;

    private TestSccpStackImpl testSccpStackImpl = null;
    private ParameterFactory factory = null;
    public RouterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws IOException {
        testSccpStackImpl = new TestSccpStackImpl();
        factory = new ParameterFactoryImpl();
        GlobalTitle gt = factory.createGlobalTitle("333",1);
        primaryAddr1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 123, 0);
        primaryAddr2 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 321, 0);

        // cleans config file
        router = new RouterImpl("RouterTest", testSccpStackImpl);
        router.setPersistDir(Util.getTmpTestDir());
        router.start();
        router.removeAllResourses();

    }

    @AfterMethod
    public void tearDown() {
        router.removeAllResourses();
        router.stop();
    }

    /**
     * Test of add method, of class RouterImpl.
     */
    @Test(groups = { "router", "functional" })
    public void testRouter() throws Exception {
        router.addRoutingAddress(1, primaryAddr1);
        assertEquals(router.getRoutingAddresses().size(), 1);

        router.addRoutingAddress(2, primaryAddr2);
        assertEquals(router.getRoutingAddresses().size(), 2);

        router.removeRoutingAddress(1);
        SccpAddress pa = router.getRoutingAddresses().values().iterator().next();
        assertNotNull(pa);
        assertEquals(pa.getSignalingPointCode(), 321);
        assertEquals(router.getRoutingAddresses().size(), 1);

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("123456789",1),0, 0);

        router.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "R", 2, 2, null, 0);
        assertEquals(router.getRules().size(), 1);

        router.addRule(2, RuleType.LOADSHARED, LoadSharingAlgorithm.Bit4, OriginationType.ALL, pattern, "K", 2, 2, null, 0);
        assertEquals(router.getRules().size(), 2);

        router.removeRule(2);
        Rule rule = router.getRules().values().iterator().next();
        assertNotNull(rule);
        assertEquals(rule.getRuleType(), RuleType.SOLITARY);
        assertEquals(router.getRules().size(), 1);

        router.addLongMessageRule(1, 1, 2, LongMessageRuleType.XUDT_ENABLED);
        assertEquals(router.getLongMessageRules().size(), 1);
        router.addLongMessageRule(2, 3, 4, LongMessageRuleType.LUDT_ENABLED);
        assertEquals(router.getLongMessageRules().size(), 2);
        router.removeLongMessageRule(2);
        LongMessageRule lmr = router.getLongMessageRules().values().iterator().next();
        assertNotNull(lmr);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.XUDT_ENABLED);
        assertEquals(router.getLongMessageRules().size(), 1);

        router.addMtp3ServiceAccessPoint(1, 1, 11, 2, 0);
        assertEquals(router.getMtp3ServiceAccessPoints().size(), 1);
        router.addMtp3ServiceAccessPoint(2, 2, 12, 2, 0);
        assertEquals(router.getMtp3ServiceAccessPoints().size(), 2);
        router.removeMtp3ServiceAccessPoint(2);
        Mtp3ServiceAccessPoint sap = router.getMtp3ServiceAccessPoints().values().iterator().next();
        assertNotNull(sap);
        assertEquals(sap.getOpc(), 11);
        assertEquals(router.getLongMessageRules().size(), 1);

        router.addMtp3Destination(1, 1, 101, 110, 0, 255, 255);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        router.addMtp3Destination(1, 2, 111, 120, 0, 255, 255);
        assertEquals(sap.getMtp3Destinations().size(), 2);
        router.removeMtp3Destination(1, 2);
        Mtp3Destination dest = sap.getMtp3Destinations().values().iterator().next();
        assertNotNull(dest);
        assertEquals(dest.getFirstDpc(), 101);
        assertEquals(sap.getMtp3Destinations().size(), 1);
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate11() throws Exception {
        // Match any digits and pattern SSN=0 (management message) keep the digits in the and add a PC(123) & SSN (8).

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                factory.createGlobalTitle("*", 1), 0, 0);

        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                factory.createGlobalTitle("-"), 123, 146);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K", 0);
        rule.setPrimaryAddressId(1);

        SccpAddress address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                factory.createGlobalTitle("4414257897897", 1), 0, 146);

        assertTrue(rule.matches(address, false, 0));

        SccpAddress translatedAddress = rule.translate(address, primaryAddress);

        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN);
        assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY);
        assertEquals(translatedAddress.getSignalingPointCode(), 123);
        assertEquals(translatedAddress.getSubsystemNumber(), 146);
        assertEquals(translatedAddress.getGlobalTitle().getDigits(), "4414257897897");
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate12() throws Exception {
        // Match any digits and pattern SSN>0 - pattern SSN present flag must be set.

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                factory.createGlobalTitle("*", 1), 0, 146);

        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN,
                factory.createGlobalTitle("-"), 123, 146);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K", 0);
        rule.setPrimaryAddressId(1);

        SccpAddress address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                factory.createGlobalTitle("4414257897897", 1), 0, 146);

        assertTrue(rule.matches(address, false, 0));

        SccpAddress translatedAddress = rule.translate(address, primaryAddress);

        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN);
        assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY);
        assertEquals(translatedAddress.getSignalingPointCode(), 123);
        assertEquals(translatedAddress.getSubsystemNumber(), 146);
        assertEquals(translatedAddress.getGlobalTitle().getDigits(), "4414257897897");
    }

   @Test(groups = { "router", "functional.encode" })
    public void testSerialization() throws Exception {
        router.addRoutingAddress(1, primaryAddr1);
        router.addRoutingAddress(2, primaryAddr2);

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("123456789",1),0, 0);
        router.addRule(1, RuleType.LOADSHARED, LoadSharingAlgorithm.Bit4, OriginationType.REMOTE, pattern, "K", 1, 2,
                null, 6);

        router.addLongMessageRule(1, 1, 2, LongMessageRuleType.XUDT_ENABLED);
        router.addMtp3ServiceAccessPoint(3, 1, 11, 2, 5);
        router.addMtp3Destination(3, 1, 101, 110, 0, 255, 255);
        router.stop();

        RouterImpl router1 = new RouterImpl(router.getName(), null);
        router1.setPersistDir(Util.getTmpTestDir());
        router1.start();

        Rule rl = router1.getRule(1);
        SccpAddress adp = router1.getRoutingAddress(2);
        LongMessageRule lmr = router1.getLongMessageRule(1);
        Mtp3ServiceAccessPoint sap = router1.getMtp3ServiceAccessPoint(3);
        Mtp3Destination dst = sap.getMtp3Destination(1);

        assertEquals(rl.getPrimaryAddressId(), 1);
        assertEquals(rl.getSecondaryAddressId(), 2);
        assertNull(rl.getNewCallingPartyAddressId());
        assertEquals(rl.getLoadSharingAlgorithm(), LoadSharingAlgorithm.Bit4);
        assertEquals(rl.getOriginationType(), OriginationType.REMOTE);
        assertNull(rl.getNewCallingPartyAddressId());
        assertEquals(rl.getNetworkId(), 6);
        assertEquals(adp.getSignalingPointCode(), primaryAddr2.getSignalingPointCode());
        assertEquals(lmr.getFirstSpc(), 1);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        assertEquals(sap.getNetworkId(), 5);
        assertEquals(dst.getLastDpc(), 110);

        router1.stop();
    }

    /**
     * Test of Ordering.
     */
    @Test(groups = { "router", "functional.order" })
    public void testOrdering() throws Exception {

        primaryAddr1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                factory.createGlobalTitle("333/---/4", 1), 123, 0);
        router.addRoutingAddress(1, primaryAddr1);

        SccpAddress pattern1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                factory.createGlobalTitle("800/????/9", 1), 0, 0);
        router.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern1, "R/K/R", 1, -1,
                null, 0);

        // Rule 2
        SccpAddress pattern2 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                factory.createGlobalTitle("*", 1), 0, 0);
        SccpAddress primaryAddr2 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                factory.createGlobalTitle("-", 1), 123, 0);
        router.addRoutingAddress(2, primaryAddr2);

        router.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern2, "K", 2, -1, null, 0);

        // Rule 3
        SccpAddress pattern3 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                factory.createGlobalTitle("9/?/9/*", 1), 0, 0);
        SccpAddress primaryAddr3 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                factory.createGlobalTitle("-/-/-/-", 1), 123, 0);
        router.addRoutingAddress(3, primaryAddr3);
        router.addRule(3, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern3, "K/K/K/K", 3, -1,
                null, 0);

        // Rule 4
        SccpAddress pattern4 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("80/??/0/???/9", 1),0, 0);
        SccpAddress primaryAddr4 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle( "90/-/1/-/7", 1),123,
                 0);
        router.addRoutingAddress(4, primaryAddr4);
        router.addRule(4, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern4, "R/K/R/K/R", 4, -1,
                null, 0);

        // Rule 5
        SccpAddress pattern5 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,factory.createGlobalTitle("800/?????/9", 1), 0,  0);
        SccpAddress primaryAddr5 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle( "90/-/7",1), 123,
                0);
        router.addRoutingAddress(5, primaryAddr5);
        router.addRule(5, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern5, "R/K/R", 5, -1,
                null, 0);

        // Rule 6
        SccpAddress pattern6 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("123456",1), 0, 0);
        SccpAddress primaryAddr6 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("-", 1),123, 0);
        router.addRoutingAddress(6, primaryAddr6);
        router.addRule(6, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern6, "K", 6, -1, null, 0);

        // Rule 7
        SccpAddress pattern7 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("1234567890", 1), 0, 0);
        SccpAddress primaryAddr7 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("-", 1), 123, 0);
        router.addRoutingAddress(7, primaryAddr7);
        router.addRule(7, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern7, "K", 7, -1, null, 0);

        // Rule 8

        SccpAddress pattern8 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("999/*", 1), 0, 0);
        SccpAddress primaryAddr8 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("111/-", 1), 123, 0);
        router.addRoutingAddress(8, primaryAddr8);
        router.addRule(8, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern8, "R/K", 8, -1, null, 0);

        // TEST find rule

        // Rule 6
        SccpAddress calledParty = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("123456", 1), 0, 0);
        Rule rule = router.findRule(calledParty, false, 0);

        assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
        assertEquals(pattern6, rule.getPattern());
        assertEquals(RuleType.SOLITARY, rule.getRuleType());
        assertEquals(-1, rule.getSecondaryAddressId());
        assertEquals("K", rule.getMask());

        // Rule 7
        calledParty = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("1234567890", 1), 0, 0);
        rule = router.findRule(calledParty, false, 0);
        assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
        assertEquals(pattern7, rule.getPattern());
        assertEquals(RuleType.SOLITARY, rule.getRuleType());
        assertEquals(-1, rule.getSecondaryAddressId());
        assertEquals("K", rule.getMask());

        // Rule 1
        calledParty = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("80012039", 1), 0, 0);
        rule = router.findRule(calledParty, false, 0);
        assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
        assertEquals(pattern1, rule.getPattern());
        assertEquals(RuleType.SOLITARY, rule.getRuleType());
        assertEquals(-1, rule.getSecondaryAddressId());
        assertEquals("R/K/R", rule.getMask());

        // Rule 5
        calledParty = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("800120349", 1), 0, 0);
        rule = router.findRule(calledParty, false, 0);
        assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
        assertEquals(pattern5, rule.getPattern());
        assertEquals(RuleType.SOLITARY, rule.getRuleType());
        assertEquals(-1, rule.getSecondaryAddressId());
        assertEquals("R/K/R", rule.getMask());

        // Rule 4
        calledParty = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("801203459", 1), 0, 0);
        rule = router.findRule(calledParty, false, 0);
        assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
        assertEquals(pattern4, rule.getPattern());
        assertEquals(RuleType.SOLITARY, rule.getRuleType());
        assertEquals(-1, rule.getSecondaryAddressId());
        assertEquals("R/K/R/K/R", rule.getMask());

        // Rule 8
        calledParty = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("999123456", 1), 0, 0);
        rule = router.findRule(calledParty, false, 0);
        assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
        assertEquals(pattern8, rule.getPattern());
        assertEquals(RuleType.SOLITARY, rule.getRuleType());
        assertEquals(-1, rule.getSecondaryAddressId());
        assertEquals("R/K", rule.getMask());

        // Rule 3
        calledParty = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle( "919123456", 1), 0, 0);
        rule = router.findRule(calledParty, false, 0);
        assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
        assertEquals(pattern3, rule.getPattern());
        assertEquals(RuleType.SOLITARY, rule.getRuleType());
        assertEquals(-1, rule.getSecondaryAddressId());
        assertEquals("K/K/K/K", rule.getMask());

    }

    /**
     * Test of Ordering with OriginationType.
     */
    @Test(groups = { "router", "functional.order" })
    public void testOrderingWithOriginationType() throws Exception {
        // Rule 1
        primaryAddr1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("999", 1),123, 
                0);
        router.addRoutingAddress(1, primaryAddr1);

        SccpAddress pattern1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);
        router.addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern1, "K", 1, -1, null, 0);

        // Rule 2
        router.addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.LOCAL, pattern1, "K", 1,
                -1, null, 0);

        // Rule 3
        SccpAddress pattern2 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("999", 1), 0, 0);
        router.addRule(3, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern2, "K", 1, -1, null, 0);

        // Rule 4
        router.addRule(4, RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.REMOTE, pattern2, "K",
                1, -1, null, 0);

        // TEST find rule
        boolean localOriginatedSign = false;
        boolean remoteOriginatedSign = true;

        SccpAddress calledParty1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                factory.createGlobalTitle("123456", 1), 0, 0);
        SccpAddress calledParty2 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,
                factory.createGlobalTitle("999", 1), 0, 0);

        Rule rule1 = router.findRule(calledParty1, localOriginatedSign, 0);
        Rule rule2 = router.findRule(calledParty1, remoteOriginatedSign, 0);
        Rule rule3 = router.findRule(calledParty2, localOriginatedSign, 0);
        Rule rule4 = router.findRule(calledParty2, remoteOriginatedSign, 0);

        assertTrue(rule1.getPattern().getGlobalTitle().getDigits().equals("*"));
        assertEquals(rule1.getOriginationType(), OriginationType.LOCAL);

        assertTrue(rule2.getPattern().getGlobalTitle().getDigits().equals("*"));
        assertEquals(rule2.getOriginationType(), OriginationType.ALL);

        assertTrue(rule3.getPattern().getGlobalTitle().getDigits().equals("*"));
        assertEquals(rule3.getOriginationType(), OriginationType.LOCAL);

        assertTrue(rule4.getPattern().getGlobalTitle().getDigits().equals("999"));
        assertEquals(rule4.getOriginationType(), OriginationType.REMOTE);

    }

    private class TestSccpStackImpl implements SccpStack {

        protected FastMap<Integer, Mtp3UserPart> mtp3UserParts = new FastMap<Integer, Mtp3UserPart>();

        TestSccpStackImpl() {
            Mtp3UserPartImpl mtp3UserPartImpl1 = new Mtp3UserPartImpl();
            Mtp3UserPartImpl mtp3UserPartImpl2 = new Mtp3UserPartImpl();

            mtp3UserParts.put(1, mtp3UserPartImpl1);
            mtp3UserParts.put(2, mtp3UserPartImpl2);
        }

        @Override
        public void start() throws IllegalStateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void stop() {
            // TODO Auto-generated method stub

        }

        @Override
        public SccpProvider getSccpProvider() {
            return null;
        }

        @Override
        public String getPersistDir() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setPersistDir(String persistDir) {
            // TODO Auto-generated method stub

        }

        @Override
        public void setRemoveSpc(boolean removeSpc) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isRemoveSpc() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public SccpResource getSccpResource() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Map<Integer, Mtp3UserPart> getMtp3UserParts() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Mtp3UserPart getMtp3UserPart(int id) {
            return this.mtp3UserParts.get(id);
        }

        @Override
        public String getName() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getSstTimerDuration_Min() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getSstTimerDuration_Max() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public double getSstTimerDuration_IncreaseFactor() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getZMarginXudtMessage() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getMaxDataMessage() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getReassemblyTimerDelay() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public Router getRouter() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setPreviewMode(boolean previewMode) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isPreviewMode() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void setSstTimerDuration_Min(int sstTimerDuration_Min) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setSstTimerDuration_Max(int sstTimerDuration_Max) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setSstTimerDuration_IncreaseFactor(double sstTimerDuration_IncreaseFactor) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setZMarginXudtMessage(int zMarginXudtMessage) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setMaxDataMessage(int maxDataMessage) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setReassemblyTimerDelay(int reassemblyTimerDelay) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setMtp3UserParts(Map<Integer, Mtp3UserPart> mtp3UserPartsTemp) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setSccpProtocolVersion(SccpProtocolVersion sccpProtocolVersion) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public SccpProtocolVersion getSccpProtocolVersion() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public int getCongControlTIMER_A() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setCongControlTIMER_A(int value) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public int getCongControlTIMER_D() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setCongControlTIMER_D(int value) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public SccpCongestionControlAlgo getCongControl_Algo() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isCongControl_blockingOutgoungSccpMessages() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void setCongControl_blockingOutgoungSccpMessages(boolean value) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCongControl_Algo(SccpCongestionControlAlgo value) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isStarted() {
            // TODO Auto-generated method stub
            return false;
        }

    }

    private class Mtp3UserPartImpl implements Mtp3UserPart {

        @Override
        public void addMtp3UserPartListener(Mtp3UserPartListener arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getMaxUserDataLength(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public Mtp3TransferPrimitiveFactory getMtp3TransferPrimitiveFactory() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public RoutingLabelFormat getRoutingLabelFormat() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isUseLsbForLinksetSelection() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void removeMtp3UserPartListener(Mtp3UserPartListener arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void sendMessage(Mtp3TransferPrimitive arg0) throws IOException {
            // TODO Auto-generated method stub

        }

        @Override
        public void setRoutingLabelFormat(RoutingLabelFormat arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void setUseLsbForLinksetSelection(boolean arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getDeliveryMessageThreadCount() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setDeliveryMessageThreadCount(int deliveryMessageThreadCount) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public ExecutorCongestionMonitor getExecutorCongestionMonitor() {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
