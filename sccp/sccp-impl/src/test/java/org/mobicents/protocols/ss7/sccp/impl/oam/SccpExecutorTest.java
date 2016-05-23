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

package org.mobicents.protocols.ss7.sccp.impl.oam;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import javolution.util.FastMap;

import org.mobicents.protocols.ss7.Util;
import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitiveFactory;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;
import org.mobicents.protocols.ss7.mtp.RoutingLabelFormat;
import org.mobicents.protocols.ss7.sccp.ConcernedSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.LongMessageRule;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.Mtp3Destination;
import org.mobicents.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.RemoteSubSystem;
import org.mobicents.protocols.ss7.sccp.Router;
import org.mobicents.protocols.ss7.sccp.Rule;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.SccpResource;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle0100;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.ss7.congestion.ExecutorCongestionMonitor;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class SccpExecutorTest {

    private Router router = null;
    private SccpResource sccpResource = null;
    private SccpStackImpl sccpStack = null;

    private SccpExecutor sccpExecutor = null;

    /**
	 *
	 */
    public SccpExecutorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws IllegalStateException {
        Mtp3UserPartImpl mtp3UserPartImpl = new Mtp3UserPartImpl();

        this.sccpStack = new SccpStackImpl("SccpExecutorTest");
        this.sccpStack.setPersistDir(Util.getTmpTestDir());
        this.sccpStack.setMtp3UserPart(1, mtp3UserPartImpl);
        this.sccpStack.start();
        this.sccpStack.removeAllResourses();
        this.router = this.sccpStack.getRouter();
        this.sccpResource = this.sccpStack.getSccpResource();

        sccpExecutor = new SccpExecutor();
        
        FastMap<String, SccpStackImpl> sccpStacks = new FastMap<String, SccpStackImpl>();
        sccpStacks.put(this.sccpStack.getName(), this.sccpStack);
        
        sccpExecutor.setSccpStacks(sccpStacks);
    }

    @AfterMethod
    public void tearDown() {
        this.sccpStack.stop();
    }

    @Test(groups = { "oam", "functional.mgmt" })
    public void testManageRule() {

        String addressCmd = "sccp rule modify 4 K 18 0 146 0 1 4 * loadshared 2 backup-addressid 3 loadsharing-algo bit0";
        String result = this.sccpExecutor.execute(addressCmd.split(" "));

        addressCmd = "sccp address create 1 71 2 8 0 0 3 123456789";
        result = this.sccpExecutor.execute(addressCmd.split(" "));
        assertEquals(result, String.format(SccpOAMMessage.ADDRESS_SUCCESSFULLY_ADDED, this.sccpStack.getName()));
        assertEquals(this.router.getRoutingAddresses().size(), 1);

        String createRuleCmd = "sccp rule create 1 R 71 2 8 0 0 3 123456789 solitary 1";

        result = this.sccpExecutor.execute(createRuleCmd.split(" "));
        assertEquals(result, String.format(SccpOAMMessage.RULE_SUCCESSFULLY_ADDED, this.sccpStack.getName()));
        assertEquals(this.router.getRules().size(), 1);
        assertEquals(this.router.getRules().get(1).getPrimaryAddressId(), 1);

        // test show
        createRuleCmd = "sccp rule show";
        result = this.sccpExecutor.execute(createRuleCmd.split(" "));

        assertEquals(
                result,
                "key=1  Rule=ruleId(1);ruleType(Solitary);originatingType(All);patternSccpAddress(pc=2,ssn=8,AI=71,gt=GlobalTitle0001Impl [digits=123456789, natureOfAddress=NATIONAL, encodingScheme=BCDOddEncodingScheme[type=BCD_ODD, code=1]]);paddress(1);saddress(-1);mask(R);networkId(0)\n");

        // TODO: this update for fixing "sccp rule create needs not zero pc"
//        createRuleCmd = "sccp rule create 2 K 18 0 180 0 1 4 * solitary 1";
        createRuleCmd = "sccp rule create 2 K 18 1 180 0 1 4 * solitary 1";
        result = this.sccpExecutor.execute(createRuleCmd.split(" "));
        assertEquals(result, String.format(SccpOAMMessage.RULE_SUCCESSFULLY_ADDED, this.sccpStack.getName()));

        assertEquals(this.router.getRules().size(), 2);

        Rule rule = this.router.getRules().get(2);
        assertNotNull(rule);
        SccpAddress pattern = rule.getPattern();
        assertNotNull(pattern);
//        assertEquals((int) pattern.getAddressIndicator().getValue(SccpProtocolVersion.ITU), 18);
        assertEquals((int) pattern.getAddressIndicator().getValue(SccpProtocolVersion.ITU), 19);
        assertEquals(pattern.getAddressIndicator().getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
        assertEquals(
                pattern.getGlobalTitle().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS);
        GlobalTitle0100 gt = (GlobalTitle0100) pattern.getGlobalTitle();
        assertEquals(gt.getTranslationType(), 0);
        assertEquals(gt.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gt.getNatureOfAddress(), NatureOfAddress.INTERNATIONAL);
        assertEquals(rule.getRuleType(), RuleType.SOLITARY);

        String sec_addressCmd = "sccp address create 2 71 3 8 0 0 3 123456789";
        result = this.sccpExecutor.execute(sec_addressCmd.split(" "));
        assertEquals(result, String.format(SccpOAMMessage.ADDRESS_SUCCESSFULLY_ADDED, this.sccpStack.getName()));
        assertEquals(this.router.getRoutingAddresses().size(), 2);

        String createRuleCmd2 = "sccp rule create 3 R 71 2 8 0 0 3 123456789 dominant 1 backup-addressid 2";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));
        assertEquals(result, String.format(SccpOAMMessage.RULE_SUCCESSFULLY_ADDED, this.sccpStack.getName()));
        assertEquals(this.router.getRules().size(), 3);
        assertEquals(this.router.getRule(3).getRuleType(), RuleType.DOMINANT);

        createRuleCmd2 = "sccp rule create 4 R 71 2 8 0 0 3 123456789 loadshared 1 backup-addressid 2 loadsharing-algo bit3";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));
        assertEquals(result, String.format(SccpOAMMessage.RULE_SUCCESSFULLY_ADDED, this.sccpStack.getName()));
        assertEquals(this.router.getRules().size(), 4);
        assertEquals(this.router.getRule(4).getRuleType(), RuleType.LOADSHARED);
        assertEquals(this.router.getRule(4).getLoadSharingAlgorithm(), LoadSharingAlgorithm.Bit3);

        createRuleCmd2 = "sccp rule create 11 R 71 2 8 0 0 3 123456789 dominant 1 backup-addressid 3";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));
        assertTrue(result.substring(0, 10).equals(SccpOAMMessage.NO_BACKUP_ADDRESS.substring(0, 10)));
        assertEquals(this.router.getRules().size(), 4);

        createRuleCmd2 = "sccp rule create 11 R 71 2 8 0 0 3 123456789 dominant 4 backup-addressid 3";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));
        assertTrue(result.substring(0, 10).equals(SccpOAMMessage.NO_PRIMARY_ADDRESS.substring(0, 10)));
        assertEquals(this.router.getRules().size(), 4);

        // Full rule command
        createRuleCmd2 = "sccp rule create 12 R 71 2 8 0 0 3 123456789 dominant 2 backup-addressid 1 loadsharing-algo bit3 newcgparty-addressid 1 origination-type remoteoriginated";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));
        assertEquals(result, String.format(SccpOAMMessage.RULE_SUCCESSFULLY_ADDED, this.sccpStack.getName()));
        assertEquals(this.router.getRules().size(), 5);

        rule = this.router.getRule(12);
        assertEquals(rule.getOriginationType(), OriginationType.REMOTE);

        // Test Modify Rule

        createRuleCmd2 = "sccp rule modify 1 R 71 2 8 0 0 3 123456789 loadshared 1 backup-addressid 2 loadsharing-algo bit4";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));
        assertEquals(result, String.format(SccpOAMMessage.RULE_SUCCESSFULLY_MODIFIED, this.sccpStack.getName()));
        assertEquals(this.router.getRules().size(), 5);
        assertEquals(this.router.getRule(1).getRuleType(), RuleType.LOADSHARED);
        assertEquals(this.router.getRule(1).getLoadSharingAlgorithm(), LoadSharingAlgorithm.Bit4);

        createRuleCmd2 = "sccp rule modify 1 R 71 2 8 0 0 3 123456789 dominant 1 backup-addressid 2";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));
        assertEquals(result, String.format(SccpOAMMessage.RULE_SUCCESSFULLY_MODIFIED, this.sccpStack.getName()));
        assertEquals(this.router.getRules().size(), 5);
        assertEquals(this.router.getRule(1).getRuleType(), RuleType.DOMINANT);

        createRuleCmd2 = "sccp rule modify 1 R 71 2 8 0 0 3 123456789 solitary 1 backup-addressid 2";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));
        assertEquals(result, String.format(SccpOAMMessage.RULE_SUCCESSFULLY_MODIFIED, this.sccpStack.getName()));
        assertEquals(this.router.getRules().size(), 5);
        assertEquals(this.router.getRule(1).getRuleType(), RuleType.SOLITARY);

        createRuleCmd2 = "sccp rule modify 1 R 71 2 8 0 0 3 123456789 dominant 1";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));
        assertEquals(result, SccpOAMMessage.RULETYPE_NOT_SOLI_SEC_ADD_MANDATORY);
        assertEquals(this.router.getRules().size(), 5);
        assertEquals(this.router.getRule(1).getRuleType(), RuleType.SOLITARY);

        createRuleCmd2 = "sccp rule modify 1 R 71 2 8 0 0 3 123456789 dominant 1 backup-addressid 3";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));
        assertTrue(result.substring(0, 10).equals(SccpOAMMessage.NO_BACKUP_ADDRESS.substring(0, 10)));
        assertEquals(this.router.getRules().size(), 5);

        createRuleCmd2 = "sccp rule modify 1 R 71 2 8 0 0 3 123456789 dominant 3 backup-addressid 3";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));
        assertTrue(result.substring(0, 10).equals(SccpOAMMessage.NO_PRIMARY_ADDRESS.substring(0, 10)));
        assertEquals(this.router.getRules().size(), 5);

        createRuleCmd2 = "sccp rule modify 15 R 71 2 8 0 0 3 123456789 dominant 1 backup-addressid 2";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));
        assertEquals(result, String.format(SccpOAMMessage.RULE_DOESNT_EXIST, this.sccpStack.getName()));
        assertEquals(this.router.getRules().size(), 5);

        createRuleCmd2 = "sccp rule delete 15";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));
        assertEquals(result, String.format(SccpOAMMessage.RULE_DOESNT_EXIST, this.sccpStack.getName()));
        assertEquals(this.router.getRules().size(), 5);

        createRuleCmd2 = "sccp rule delete 1";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));
        assertEquals(result, String.format(SccpOAMMessage.RULE_SUCCESSFULLY_REMOVED, this.sccpStack.getName()));
        assertEquals(this.router.getRules().size(), 4);

        createRuleCmd2 = "sccp rule show 2";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));

        createRuleCmd2 = "sccp rule show";
        result = this.sccpExecutor.execute(createRuleCmd2.split(" "));

    }
    
    @Test(groups = { "oam", "functional.mgmt" })
    public void testMaskSectionsValidations() {

        String incorrect_prim_addressCmd = "sccp address create 1 71 6535 8 0 0 12 93707100007";
        String incorrect_prim_address_deleteCmd = "sccp address delete 1";
        String correct_prim_addressCmd = "sccp address create 1 71 6535 8 0 0 12 -/-";

        // TODO: this update for fixing "sccp rule create needs not zero pc"
//        String incorrectCreateRuleCmd = "sccp rule create 2 R/K 18 0 180 0 1 4 * solitary 1";
//        String correctCreateRuleCmd = "sccp rule create 2 R/K 18 0 180 0 1 4 937/* solitary 1";
        String incorrectCreateRuleCmd = "sccp rule create 2 R/K 18 1 180 0 1 4 * solitary 1";
        String correctCreateRuleCmd = "sccp rule create 2 R/K 18 1 180 0 1 4 937/* solitary 1";

        String incorrect_sec_addressCmd = "sccp address create 3 71 6535 8 0 0 12 93707100007";
        // TODO: this update for fixing "sccp rule create needs not zero pc"
//        String correctCreateRuleCmdWithSecId = "sccp rule create 2 R/K 18 0 180 0 1 4 937/* solitary 1 backup-addressid 3";
        String correctCreateRuleCmdWithSecId = "sccp rule create 2 R/K 18 1 180 0 1 4 937/* solitary 1 backup-addressid 3";

        String result = this.sccpExecutor.execute(incorrectCreateRuleCmd.split(" "));
        assertEquals(result, SccpOAMMessage.SEC_MISMATCH_PATTERN);

        this.sccpExecutor.execute(incorrect_prim_addressCmd.split(" "));
        result = this.sccpExecutor.execute(correctCreateRuleCmd.split(" "));
        assertEquals(result, SccpOAMMessage.SEC_MISMATCH_PRIMADDRESS);

        this.sccpExecutor.execute(incorrect_prim_address_deleteCmd.split(" "));
        this.sccpExecutor.execute(correct_prim_addressCmd.split(" "));
        this.sccpExecutor.execute(incorrect_sec_addressCmd.split(" "));
        result = this.sccpExecutor.execute(correctCreateRuleCmdWithSecId.split(" "));

        assertEquals(result, SccpOAMMessage.SEC_MISMATCH_SECADDRESS);
    }
    
    /**
     * Test for bug http://code.google.com/p/mobicents/issues/detail?id=3057 NPE when creating SCCP primary address via CLI
     */
    @Test(groups = { "oam", "functional.mgmt" })
    public void testManageAddress() {
        String prim_addressCmd = "sccp address create 1 71 6535 8 0 0 12 93707100007";
        String result = this.sccpExecutor.execute(prim_addressCmd.split(" "));
        assertEquals(result, String.format(SccpOAMMessage.ADDRESS_SUCCESSFULLY_ADDED, this.sccpStack.getName()));
        assertEquals(this.router.getRoutingAddresses().size(), 1);

        // test show
        prim_addressCmd = "sccp address show";
        result = this.sccpExecutor.execute(prim_addressCmd.split(" "));
        assertEquals(result, "key=1  pc=6535,ssn=8,AI=71,gt=GlobalTitle0001Impl [digits=93707100007, natureOfAddress=SPARE_12, encodingScheme=BCDOddEncodingScheme[type=BCD_ODD, code=1]]\n");
    }

    @Test(groups = { "oam", "functional.mgmt" })
    public void testPrimAddress() {

        String rspCmd = "sccp address create 11 71 6535 8 0 0 12 93707100007";
        // <id> <address-indicator> <point-code> <subsystem-number>
        // <translation-type> <numbering-plan> <nature-of-address-indicator>
        // <digits>
        String res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getRoutingAddresses().size(), 1);
        SccpAddress addr = this.router.getRoutingAddress(11);
        assertEquals(addr.getAddressIndicator().getValue(SccpProtocolVersion.ITU), 71);
        assertEquals(addr.getSignalingPointCode(), 6535);
        assertEquals(addr.getSubsystemNumber(), 8);
        assertTrue(addr.getGlobalTitle().getDigits().equals("93707100007"));

        rspCmd = "sccp address create 11 71 6536 8 0 0 12 93707100007";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.ADDRESS_ALREADY_EXIST));
        assertEquals(this.router.getRoutingAddresses().size(), 1);
        addr = this.router.getRoutingAddress(11);
        assertEquals(addr.getSignalingPointCode(), 6535);

        rspCmd = "sccp address modify 11 71 6537 8 0 0 12 93707100007";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getRoutingAddresses().size(), 1);
        addr = this.router.getRoutingAddress(11);
        assertEquals(addr.getSignalingPointCode(), 6537);

        rspCmd = "sccp address modify 12 71 6538 8 0 0 12 93707100007";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.ADDRESS_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.router.getRoutingAddresses().size(), 1);
        addr = this.router.getRoutingAddress(11);
        assertEquals(addr.getSignalingPointCode(), 6537);

        rspCmd = "sccp address show 11";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp address show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp address delete 12";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.ADDRESS_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.router.getRoutingAddresses().size(), 1);

        rspCmd = "sccp address delete 11";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getRoutingAddresses().size(), 0);
    }   
    
    @Test(groups = { "oam", "functional.mgmt" })
    public void testLmr() {

        String rspCmd = "sccp lmr create 1 11 12 udt";
        String res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 1);
        LongMessageRule lmr = this.router.getLongMessageRule(1);
        assertEquals(lmr.getFirstSpc(), 11);
        assertEquals(lmr.getLastSpc(), 12);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.LONG_MESSAGE_FORBBIDEN);

        // Test Show
        rspCmd = "sccp lmr show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, "key=1  firstSpc=11, lastSpc=12, ruleType=LONG_MESSAGE_FORBBIDEN\n");

        rspCmd = "sccp lmr create 2 13 14 xudt";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 2);
        lmr = this.router.getLongMessageRule(2);
        assertEquals(lmr.getFirstSpc(), 13);
        assertEquals(lmr.getLastSpc(), 14);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.XUDT_ENABLED);

        rspCmd = "sccp lmr create 3 15 16 ludt";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 3);
        lmr = this.router.getLongMessageRule(3);
        assertEquals(lmr.getFirstSpc(), 15);
        assertEquals(lmr.getLastSpc(), 16);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.LUDT_ENABLED);

        rspCmd = "sccp lmr create 4 17 18 ludt_segm";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 4);
        lmr = this.router.getLongMessageRule(4);
        assertEquals(lmr.getFirstSpc(), 17);
        assertEquals(lmr.getLastSpc(), 18);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.LUDT_ENABLED_WITH_SEGMENTATION);

        rspCmd = "sccp lmr create 4 19 20 ludt_segm";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.LMR_ALREADY_EXIST));
        assertEquals(this.router.getLongMessageRules().size(), 4);
        lmr = this.router.getLongMessageRule(4);

        rspCmd = "sccp lmr modify 4 21 22 udt";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 4);
        lmr = this.router.getLongMessageRule(4);
        assertEquals(lmr.getFirstSpc(), 21);
        assertEquals(lmr.getLastSpc(), 22);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.LONG_MESSAGE_FORBBIDEN);

        rspCmd = "sccp lmr modify 4 21 22 xudt";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 4);
        lmr = this.router.getLongMessageRule(4);
        assertEquals(lmr.getFirstSpc(), 21);
        assertEquals(lmr.getLastSpc(), 22);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.XUDT_ENABLED);

        rspCmd = "sccp lmr modify 4 21 22 ludt";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 4);
        lmr = this.router.getLongMessageRule(4);
        assertEquals(lmr.getFirstSpc(), 21);
        assertEquals(lmr.getLastSpc(), 22);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.LUDT_ENABLED);

        rspCmd = "sccp lmr modify 4 21 22 ludt_segm";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 4);
        lmr = this.router.getLongMessageRule(4);
        assertEquals(lmr.getFirstSpc(), 21);
        assertEquals(lmr.getLastSpc(), 22);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.LUDT_ENABLED_WITH_SEGMENTATION);

        rspCmd = "sccp lmr modify 5 23 24 udt";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.LMR_DOESNT_EXIST,this.sccpStack.getName() )));
        assertEquals(this.router.getLongMessageRules().size(), 4);
        lmr = this.router.getLongMessageRule(4);
        assertEquals(lmr.getFirstSpc(), 21);
        assertEquals(lmr.getLastSpc(), 22);
        assertEquals(lmr.getLongMessageRuleType(), LongMessageRuleType.LUDT_ENABLED_WITH_SEGMENTATION);

        rspCmd = "sccp lmr show 1";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp lmr show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp lmr delete 10";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.LMR_DOESNT_EXIST,this.sccpStack.getName() )));
        assertEquals(this.router.getLongMessageRules().size(), 4);

        rspCmd = "sccp lmr delete 4";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getLongMessageRules().size(), 3);
    }
    
    @Test(groups = { "oam", "functional.mgmt" })
    public void testSap() {

        String rspCmd = "sccp sap create 5 101 11 2";
        String res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.MUP_DOESNT_EXIST));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 0);

        rspCmd = "sccp sap create 5 1 11 2";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        Mtp3ServiceAccessPoint sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 11);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        // test show
        rspCmd = "sccp sap show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, "key=5  mtp3Id=1, opc=11, ni=2, networkId=0, dpcList=[]\n");

        rspCmd = "sccp sap create 5 1 11 2";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.SAP_ALREADY_EXIST));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 11);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        rspCmd = "sccp sap modify 5 2 12 2";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.MUP_DOESNT_EXIST));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 11);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        rspCmd = "sccp sap modify 5 1 13 2";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        rspCmd = "sccp sap modify 6 2 14 2";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        rspCmd = "sccp dest create 1 7 31 32 3 4 255";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.SAP_DOESNT_EXIST,this.sccpStack.getName())));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        rspCmd = "sccp dest create 5 7 31 32 3 4 255";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        Mtp3Destination dest = sap.getMtp3Destination(7);
        assertEquals(dest.getFirstDpc(), 31);
        assertEquals(dest.getLastDpc(), 32);
        assertEquals(dest.getFirstSls(), 3);
        assertEquals(dest.getLastSls(), 4);
        assertEquals(dest.getSlsMask(), 255);

        rspCmd = "sccp dest create 5 7 33 34 3 4 255";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.DEST_ALREADY_EXIST));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        dest = sap.getMtp3Destination(7);
        assertEquals(dest.getFirstDpc(), 31);

        rspCmd = "sccp dest modify 1 7 35 36 3 4 15";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        dest = sap.getMtp3Destination(7);
        assertEquals(dest.getFirstDpc(), 31);

        rspCmd = "sccp dest modify 5 9 38 39 3 4 15";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.DEST_DOESNT_EXIST,this.sccpStack.getName())));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        dest = sap.getMtp3Destination(7);
        assertEquals(dest.getFirstDpc(), 31);

        rspCmd = "sccp dest modify 5 7 40 41 3 4 15";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        dest = sap.getMtp3Destination(7);
        assertEquals(dest.getFirstDpc(), 40);
        assertEquals(dest.getSlsMask(), 15);

        rspCmd = "sccp dest show 5 7";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp dest show 5";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp sap show 5";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp sap show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp dest delete 1 7";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        dest = sap.getMtp3Destination(7);
        assertEquals(dest.getFirstDpc(), 40);

        rspCmd = "sccp dest delete 5 9";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.DEST_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 1);
        dest = sap.getMtp3Destination(7);
        assertEquals(dest.getFirstDpc(), 40);

        rspCmd = "sccp dest delete 5 7";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        rspCmd = "sccp sap delete 1";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.SAP_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 1);
        sap = this.router.getMtp3ServiceAccessPoint(5);
        assertEquals(sap.getOpc(), 13);
        assertEquals(sap.getMtp3Destinations().size(), 0);

        rspCmd = "sccp sap delete 5";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.router.getMtp3ServiceAccessPoints().size(), 0);
    }
    
    @Test(groups = { "oam", "functional.mgmt" })
    public void testRsp() {

        String rspCmd = "sccp rsp create 1 11 0 0";
        String res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getRemoteSpcs().size(), 1);
        RemoteSignalingPointCode spc = this.sccpResource.getRemoteSpc(1);
        assertEquals(spc.getRemoteSpc(), 11);

        // test show
        rspCmd = "sccp rsp show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, "key=1  rsp=11 rsp-flag=0 mask=0 rsp-prohibited=false rsccp-prohibited=false rl=0 rsl=0\n");

        rspCmd = "sccp rsp create 1 12 0 0";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.RSPC_ALREADY_EXIST));
        assertEquals(this.sccpResource.getRemoteSpcs().size(), 1);
        spc = this.sccpResource.getRemoteSpc(1);
        assertEquals(spc.getRemoteSpc(), 11);

        rspCmd = "sccp rsp modify 2 12 0 0";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.RSPC_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.sccpResource.getRemoteSpcs().size(), 1);
        spc = this.sccpResource.getRemoteSpc(1);
        assertEquals(spc.getRemoteSpc(), 11);

        rspCmd = "sccp rsp modify 1 12 0 0";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getRemoteSpcs().size(), 1);
        spc = this.sccpResource.getRemoteSpc(1);
        assertEquals(spc.getRemoteSpc(), 12);

        rspCmd = "sccp rsp show 1";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp rsp show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp rsp delete 5";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.RSPC_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.sccpResource.getRemoteSpcs().size(), 1);
        spc = this.sccpResource.getRemoteSpc(1);
        assertEquals(spc.getRemoteSpc(), 12);

        rspCmd = "sccp rsp delete 1";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getRemoteSpcs().size(), 0);
    }
    
    @Test(groups = { "oam", "functional.mgmt" })
    public void testRss() {

        String rspCmd = "sccp rss create 2 11 8 0";
        String res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 1);
        RemoteSubSystem rss = this.sccpResource.getRemoteSsn(2);
        assertEquals(rss.getRemoteSpc(), 11);
        assertEquals(rss.getRemoteSsn(), 8);
        assertFalse(rss.getMarkProhibitedWhenSpcResuming());

        // Test show
        rspCmd = "sccp rss show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, "key=2  rsp=11 rss=8 rss-flag=0 rss-prohibited=false\n");

        rspCmd = "sccp rss delete 5";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, String.format(SccpOAMMessage.RSS_DOESNT_EXIST, this.sccpStack.getName()));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 1);
        rss = this.sccpResource.getRemoteSsn(2);
        assertEquals(rss.getRemoteSpc(), 11);

        rspCmd = "sccp rss delete 2";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 0);

        rspCmd = "sccp rss create 2 12 8 0 prohibitedwhenspcresuming true";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, String.format(SccpOAMMessage.RSS_SUCCESSFULLY_ADDED, this.sccpStack.getName()));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 1);
        rss = this.sccpResource.getRemoteSsn(2);
        assertEquals(rss.getRemoteSpc(), 12);
        assertEquals(rss.getRemoteSsn(), 8);
        assertTrue(rss.getMarkProhibitedWhenSpcResuming());

        rspCmd = "sccp rss create 2 12 8 0";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.RSS_ALREADY_EXIST));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 1);
        rss = this.sccpResource.getRemoteSsn(2);
        assertEquals(rss.getRemoteSpc(), 12);
        assertEquals(rss.getRemoteSsn(), 8);
        assertTrue(rss.getMarkProhibitedWhenSpcResuming());

        rspCmd = "sccp rss modify 2 13 18 0";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 1);
        rss = this.sccpResource.getRemoteSsn(2);
        assertEquals(rss.getRemoteSpc(), 13);
        assertEquals(rss.getRemoteSsn(), 18);
        assertFalse(rss.getMarkProhibitedWhenSpcResuming());

        rspCmd = "sccp rss modify 2 14 19 0 prohibitedwhenspcresuming true";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, String.format(SccpOAMMessage.RSS_SUCCESSFULLY_MODIFIED, this.sccpStack.getName()));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 1);
        rss = this.sccpResource.getRemoteSsn(2);
        assertEquals(rss.getRemoteSpc(), 14);
        assertEquals(rss.getRemoteSsn(), 19);
        assertTrue(rss.getMarkProhibitedWhenSpcResuming());

        rspCmd = "sccp rss modify 3 15 19 0 prohibitedwhenspcresuming true";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, String.format(SccpOAMMessage.RSS_DOESNT_EXIST, this.sccpStack.getName()));
        assertEquals(this.sccpResource.getRemoteSsns().size(), 1);
        rss = this.sccpResource.getRemoteSsn(2);
        assertEquals(rss.getRemoteSpc(), 14);
        assertEquals(rss.getRemoteSsn(), 19);
        assertTrue(rss.getMarkProhibitedWhenSpcResuming());

        rspCmd = "sccp rss show 1";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp rss show 2";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp rss show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
    } 

    @Test(groups = { "oam", "functional.mgmt" })
    public void testConcernedSpc() {

        String rspCmd = "sccp csp create 3 21";
        String res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getConcernedSpcs().size(), 1);
        ConcernedSignalingPointCode cspc = this.sccpResource.getConcernedSpc(3);
        assertEquals(cspc.getRemoteSpc(), 21);

        // Show test
        rspCmd = "sccp csp show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, "key=3  rsp=21\n");

        rspCmd = "sccp csp create 3 22";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(SccpOAMMessage.CS_ALREADY_EXIST),res);
        assertEquals(this.sccpResource.getConcernedSpcs().size(), 1);
        cspc = this.sccpResource.getConcernedSpc(3);
        assertEquals(cspc.getRemoteSpc(), 21);

        rspCmd = "sccp csp modify 3 23";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getConcernedSpcs().size(), 1);
        cspc = this.sccpResource.getConcernedSpc(3);
        assertEquals(cspc.getRemoteSpc(), 23);

        rspCmd = "sccp csp modify 33 24";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.CS_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.sccpResource.getConcernedSpcs().size(), 1);
        cspc = this.sccpResource.getConcernedSpc(3);
        assertEquals(cspc.getRemoteSpc(), 23);

        rspCmd = "sccp csp show 3";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp csp show";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp csp delete 33";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertTrue(res.equals(String.format(SccpOAMMessage.CS_DOESNT_EXIST, this.sccpStack.getName())));
        assertEquals(this.sccpResource.getConcernedSpcs().size(), 1);
        cspc = this.sccpResource.getConcernedSpc(3);
        assertEquals(cspc.getRemoteSpc(), 23);

        rspCmd = "sccp csp delete 3";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpResource.getConcernedSpcs().size(), 0);
    }    

    @Test(groups = { "oam", "functional.mgmt" })
    public void testParameters() {

        String rspCmd = "sccp set xxx 200";
        String res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, SccpOAMMessage.INVALID_COMMAND);

        rspCmd = "sccp set zMarginXudtMessage 200 stackname SccpExecutorTest";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(res, String.format(SccpOAMMessage.PARAMETER_SUCCESSFULLY_SET, this.sccpStack.getName()));
        
        assertEquals(this.sccpStack.getZMarginXudtMessage(), 200);

        rspCmd = "sccp set reassemblyTimerDelay 10000";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getReassemblyTimerDelay(), 10000);

        rspCmd = "sccp set maxDataMessage 3000";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getMaxDataMessage(), 3000);

        rspCmd = "sccp set removeSpc false";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.isRemoveSpc(), false);

        rspCmd = "sccp set sstTimerDuration_Min 6000";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getSstTimerDuration_Min(), 6000);

        rspCmd = "sccp set sstTimerDuration_Max 1000000";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getSstTimerDuration_Max(), 1000000);

        rspCmd = "sccp set sstTimerDuration_IncreaseFactor 2.55";
        res = this.sccpExecutor.execute(rspCmd.split(" "));
        assertEquals(this.sccpStack.getSstTimerDuration_IncreaseFactor(), 2.55);

        rspCmd = "sccp get zMarginXudtMessage";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp get reassemblyTimerDelay";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp get maxDataMessage";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp get removeSpc";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp get sstTimerDuration_Min";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp get sstTimerDuration_Max";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp get sstTimerDuration_IncreaseFactor";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

        rspCmd = "sccp get";
        res = this.sccpExecutor.execute(rspCmd.split(" "));

    }

    class Mtp3UserPartImpl implements Mtp3UserPart {

        public void addMtp3UserPartListener(Mtp3UserPartListener arg0) {
            // TODO Auto-generated method stub

        }

        public int getMaxUserDataLength(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        public void removeMtp3UserPartListener(Mtp3UserPartListener arg0) {
            // TODO Auto-generated method stub

        }

        public void sendMessage(Mtp3TransferPrimitive arg0) throws IOException {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         *
         * @see org.mobicents.protocols.ss7.mtp.Mtp3UserPart# getMtp3TransferPrimitiveFactory()
         */
        @Override
        public Mtp3TransferPrimitiveFactory getMtp3TransferPrimitiveFactory() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.mobicents.protocols.ss7.mtp.Mtp3UserPart#getRoutingLabelFormat()
         */
        @Override
        public RoutingLabelFormat getRoutingLabelFormat() {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.mobicents.protocols.ss7.mtp.Mtp3UserPart#setRoutingLabelFormat
         * (org.mobicents.protocols.ss7.mtp.RoutingLabelFormat)
         */
        @Override
        public void setRoutingLabelFormat(RoutingLabelFormat arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isUseLsbForLinksetSelection() {
            return false;
        }

        @Override
        public void setUseLsbForLinksetSelection(boolean arg0) {

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
