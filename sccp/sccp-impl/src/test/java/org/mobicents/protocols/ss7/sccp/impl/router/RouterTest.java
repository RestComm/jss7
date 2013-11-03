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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.Util;
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
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpResource;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
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

        primaryAddr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123, GlobalTitle.getInstance(1, "333"),
                0);
        primaryAddr2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 321, GlobalTitle.getInstance(1, "333"),
                0);

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

        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "123456789"), 0);

        router.addRule(1, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "R", 2, 2, null);
        assertEquals(router.getRules().size(), 1);

        router.addRule(2, RuleType.Loadshared, LoadSharingAlgorithm.Bit4, OriginationType.All, pattern, "K", 2, 2, null);
        assertEquals(router.getRules().size(), 2);

        router.removeRule(2);
        Rule rule = router.getRules().values().iterator().next();
        assertNotNull(rule);
        assertEquals(rule.getRuleType(), RuleType.Solitary);
        assertEquals(router.getRules().size(), 1);

        router.addLongMessageRule(1, 1, 2, LongMessageRuleType.XudtEnabled);
        assertEquals(router.getLongMessageRules().size(), 1);
        router.addLongMessageRule(2, 3, 4, LongMessageRuleType.LudtEnabled);
        assertEquals(router.getLongMessageRules().size(), 2);
        router.removeLongMessageRule(2);
        LongMessageRule lmr = router.getLongMessageRules().values().iterator().next();
        assertNotNull(lmr);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.XudtEnabled);
        assertEquals(router.getLongMessageRules().size(), 1);

        router.addMtp3ServiceAccessPoint(1, 1, 11, 2);
        assertEquals(router.getMtp3ServiceAccessPoints().size(), 1);
        router.addMtp3ServiceAccessPoint(2, 2, 12, 2);
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

    @Test(groups = { "router", "functional.encode" })
    public void testSerialization() throws Exception {
        router.addRoutingAddress(1, primaryAddr1);
        router.addRoutingAddress(2, primaryAddr2);

        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "123456789"), 0);
        router.addRule(1, RuleType.Loadshared, LoadSharingAlgorithm.Bit4, OriginationType.RemoteOriginated, pattern, "K", 1, 2,
                null);

        router.addLongMessageRule(1, 1, 2, LongMessageRuleType.XudtEnabled);
        router.addMtp3ServiceAccessPoint(3, 1, 11, 2);
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
        assertEquals(rl.getOriginationType(), OriginationType.RemoteOriginated);
        assertNull(rl.getNewCallingPartyAddressId());
        assertEquals(adp.getSignalingPointCode(), primaryAddr2.getSignalingPointCode());
        assertEquals(lmr.getFirstSpc(), 1);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        assertEquals(dst.getLastDpc(), 110);

        router1.stop();
    }

    /**
     * Test of Ordering.
     */
    @Test(groups = { "router", "functional.order" })
    public void testOrdering() throws Exception {
        primaryAddr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123, GlobalTitle.getInstance(1,
                "333/---/4"), 0);
        router.addRoutingAddress(1, primaryAddr1);

        SccpAddress pattern1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "800/????/9"), 0);
        router.addRule(1, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern1, "R/K/R", 1, -1,
                null);

        // Rule 2
        SccpAddress pattern2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "*"), 0);
        SccpAddress primaryAddr2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
                GlobalTitle.getInstance(1, "-"), 0);
        router.addRoutingAddress(2, primaryAddr2);

        router.addRule(2, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern2, "K", 2, -1, null);

        // Rule 3
        SccpAddress pattern3 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "9/?/9/*"), 0);
        SccpAddress primaryAddr3 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
                GlobalTitle.getInstance(1, "-/-/-/-"), 0);
        router.addRoutingAddress(3, primaryAddr3);
        router.addRule(3, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern3, "K/K/K/K", 3, -1,
                null);

        // Rule 4
        SccpAddress pattern4 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "80/??/0/???/9"), 0);
        SccpAddress primaryAddr4 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
                GlobalTitle.getInstance(1, "90/-/1/-/7"), 0);
        router.addRoutingAddress(4, primaryAddr4);
        router.addRule(4, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern4, "R/K/R/K/R", 4, -1,
                null);

        // Rule 5
        SccpAddress pattern5 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "800/?????/9"), 0);
        SccpAddress primaryAddr5 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
                GlobalTitle.getInstance(1, "90/-/7"), 0);
        router.addRoutingAddress(5, primaryAddr5);
        router.addRule(5, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern5, "R/K/R", 5, -1,
                null);

        // Rule 6
        SccpAddress pattern6 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "123456"), 0);
        SccpAddress primaryAddr6 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
                GlobalTitle.getInstance(1, "-"), 0);
        router.addRoutingAddress(6, primaryAddr6);
        router.addRule(6, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern6, "K", 6, -1, null);

        // Rule 7
        SccpAddress pattern7 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "1234567890"), 0);
        SccpAddress primaryAddr7 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
                GlobalTitle.getInstance(1, "-"), 0);
        router.addRoutingAddress(7, primaryAddr7);
        router.addRule(7, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern7, "K", 7, -1, null);

        // Rule 8

        SccpAddress pattern8 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "999/*"), 0);
        SccpAddress primaryAddr8 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
                GlobalTitle.getInstance(1, "111/-"), 0);
        router.addRoutingAddress(8, primaryAddr8);
        router.addRule(8, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern8, "R/K", 8, -1, null);

        // TEST find rule

        // Rule 6
        SccpAddress calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "123456"), 0);
        Rule rule = router.findRule(calledParty, false);

        assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
        assertEquals(pattern6, rule.getPattern());
        assertEquals(RuleType.Solitary, rule.getRuleType());
        assertEquals(-1, rule.getSecondaryAddressId());
        assertEquals("K", rule.getMask());

        // Rule 7
        calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "1234567890"), 0);
        rule = router.findRule(calledParty, false);
        assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
        assertEquals(pattern7, rule.getPattern());
        assertEquals(RuleType.Solitary, rule.getRuleType());
        assertEquals(-1, rule.getSecondaryAddressId());
        assertEquals("K", rule.getMask());

        // Rule 1
        calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
                GlobalTitle.getInstance(1, "80012039"), 0);
        rule = router.findRule(calledParty, false);
        assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
        assertEquals(pattern1, rule.getPattern());
        assertEquals(RuleType.Solitary, rule.getRuleType());
        assertEquals(-1, rule.getSecondaryAddressId());
        assertEquals("R/K/R", rule.getMask());

        // Rule 5
        calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
                GlobalTitle.getInstance(1, "800120349"), 0);
        rule = router.findRule(calledParty, false);
        assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
        assertEquals(pattern5, rule.getPattern());
        assertEquals(RuleType.Solitary, rule.getRuleType());
        assertEquals(-1, rule.getSecondaryAddressId());
        assertEquals("R/K/R", rule.getMask());

        // Rule 4
        calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
                GlobalTitle.getInstance(1, "801203459"), 0);
        rule = router.findRule(calledParty, false);
        assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
        assertEquals(pattern4, rule.getPattern());
        assertEquals(RuleType.Solitary, rule.getRuleType());
        assertEquals(-1, rule.getSecondaryAddressId());
        assertEquals("R/K/R/K/R", rule.getMask());

        // Rule 8
        calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
                GlobalTitle.getInstance(1, "999123456"), 0);
        rule = router.findRule(calledParty, false);
        assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
        assertEquals(pattern8, rule.getPattern());
        assertEquals(RuleType.Solitary, rule.getRuleType());
        assertEquals(-1, rule.getSecondaryAddressId());
        assertEquals("R/K", rule.getMask());

        // Rule 3
        calledParty = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
                GlobalTitle.getInstance(1, "919123456"), 0);
        rule = router.findRule(calledParty, false);
        assertEquals(LoadSharingAlgorithm.Undefined, rule.getLoadSharingAlgorithm());
        assertEquals(pattern3, rule.getPattern());
        assertEquals(RuleType.Solitary, rule.getRuleType());
        assertEquals(-1, rule.getSecondaryAddressId());
        assertEquals("K/K/K/K", rule.getMask());

    }

    /**
     * Test of Ordering with OriginationType.
     */
    @Test(groups = { "router", "functional.order" })
    public void testOrderingWithOriginationType() throws Exception {
        // Rule 1
        primaryAddr1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123, GlobalTitle.getInstance(1, "999"),
                0);
        router.addRoutingAddress(1, primaryAddr1);

        SccpAddress pattern1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "*"), 0);
        router.addRule(1, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern1, "K", 1, -1, null);

        // Rule 2
        router.addRule(2, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.LocalOriginated, pattern1, "K", 1,
                -1, null);

        // Rule 3
        SccpAddress pattern2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "999"), 0);
        router.addRule(3, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern2, "K", 1, -1, null);

        // Rule 4
        router.addRule(4, RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.RemoteOriginated, pattern2, "K",
                1, -1, null);

        // TEST find rule
        boolean localOriginatedSign = false;
        boolean remoteOriginatedSign = true;

        SccpAddress calledParty1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(
                1, "123456"), 0);
        SccpAddress calledParty2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(
                1, "999"), 0);

        Rule rule1 = router.findRule(calledParty1, localOriginatedSign);
        Rule rule2 = router.findRule(calledParty1, remoteOriginatedSign);
        Rule rule3 = router.findRule(calledParty2, localOriginatedSign);
        Rule rule4 = router.findRule(calledParty2, remoteOriginatedSign);

        assertTrue(rule1.getPattern().getGlobalTitle().getDigits().equals("*"));
        assertEquals(rule1.getOriginationType(), OriginationType.LocalOriginated);

        assertTrue(rule2.getPattern().getGlobalTitle().getDigits().equals("*"));
        assertEquals(rule2.getOriginationType(), OriginationType.All);

        assertTrue(rule3.getPattern().getGlobalTitle().getDigits().equals("*"));
        assertEquals(rule3.getOriginationType(), OriginationType.LocalOriginated);

        assertTrue(rule4.getPattern().getGlobalTitle().getDigits().equals("999"));
        assertEquals(rule4.getOriginationType(), OriginationType.RemoteOriginated);

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
            // TODO Auto-generated method stub
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

    }
}
