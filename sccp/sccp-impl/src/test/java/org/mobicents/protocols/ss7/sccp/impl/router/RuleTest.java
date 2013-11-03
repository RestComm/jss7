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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.mobicents.protocols.ss7.sccp.OriginationType;
import org.mobicents.protocols.ss7.sccp.RuleType;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.GT0100;
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
public class RuleTest {
    private static final String RULE = "1;pattern(ROUTING_BASED_ON_GLOBAL_TITLE#tt= #np= #noa=NATIONAL#digits=9023629581#ssn= #dpc=0#dpcProhibited=false);translation(ROUTING_BASED_ON_GLOBAL_TITLE#tt= #np= #noa=INTERNATIONAL#digits=79023629581#ssn= #dpc=345#dpcProhibited=false);\n";

    XMLBinding binding = new XMLBinding();

    public RuleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() {
        binding.setClassAttribute("type");
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate0() throws Exception {

        // Match digits starting with 447797706077 and add PC and SSN.
        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(0,
                NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, "917797706077/*"), 8);
        SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 792,
                GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, "917797706077/-"), 8);

        RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "K/R");
        rule.setPrimaryAddressId(1);

        SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(0,
                NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, "917797706077"), 8);

        assertTrue(rule.matches(address, false));

        SccpAddress translatedAddress = rule.translate(address, primaryAddress);

        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN);
        assertEquals(
                translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS);
        assertEquals(translatedAddress.getSignalingPointCode(), 792);
        assertEquals(translatedAddress.getSubsystemNumber(), 8);
        assertEquals(translatedAddress.getGlobalTitle().getDigits(), "917797706077");

        address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(0,
                NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, "91779770607720"), 8);

        assertTrue(rule.matches(address, false));

        translatedAddress = rule.translate(address, primaryAddress);

        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN);
        assertEquals(
                translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS);
        assertEquals(translatedAddress.getSignalingPointCode(), 792);
        assertEquals(translatedAddress.getSubsystemNumber(), 8);
        assertEquals(translatedAddress.getGlobalTitle().getDigits(), "917797706077");
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate1() throws Exception {

        // Match digits 123456789 and replace with PC and SSN. It removes the GT
        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "123456789"), 0);
        SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 123,
                GlobalTitle.getInstance("-"), 8);

        RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "R");
        rule.setPrimaryAddressId(1);

        SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "123456789"), 0);

        assertTrue(rule.matches(address, false));

        SccpAddress translatedAddress = rule.translate(address, primaryAddress);

        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN);
        assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED);
        assertEquals(translatedAddress.getSignalingPointCode(), 123);
        assertEquals(translatedAddress.getSubsystemNumber(), 8);
        assertNull(translatedAddress.getGlobalTitle());
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate2() throws Exception {
        // Match a seven digit number starting "123", followed by any three
        // digits, then "7".

        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "123/???/7"), 0);

        SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
                GlobalTitle.getInstance(1, "333/---/4"), 0);

        RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "R/K/R");
        rule.setPrimaryAddressId(1);

        SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "1234567"), 0);

        assertTrue(rule.matches(address, false));

        SccpAddress translatedAddress = rule.translate(address, primaryAddress);

        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
        assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY);
        assertEquals(translatedAddress.getSignalingPointCode(), 123);
        assertEquals(translatedAddress.getSubsystemNumber(), 0);
        assertEquals(translatedAddress.getGlobalTitle().getDigits(), "3334564");
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate3() throws Exception {
        // Match "441425", followed by any digits Remove the first six digits.
        // Keep any following digits in the Input. Add a PC(123) & SSN (8).

        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "441425/*"), 0);

        SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
                GlobalTitle.getInstance("-/-"), 8);

        RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "R/K");
        rule.setPrimaryAddressId(1);

        SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "4414257897897"), 0);

        assertTrue(rule.matches(address, false));

        SccpAddress translatedAddress = rule.translate(address, primaryAddress);

        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
        assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY);
        assertEquals(translatedAddress.getSignalingPointCode(), 123);
        assertEquals(translatedAddress.getSubsystemNumber(), 8);
        assertEquals(translatedAddress.getGlobalTitle().getDigits(), "7897897");
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate4() throws Exception {
        // Match any digits keep the digits in the and add a PC(123) & SSN (8).

        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
                GlobalTitle.getInstance(1, "*"), 0);

        SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 123,
                GlobalTitle.getInstance("-"), 8);

        RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "K");
        rule.setPrimaryAddressId(1);

        SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "4414257897897"), 0);

        assertTrue(rule.matches(address, false));

        SccpAddress translatedAddress = rule.translate(address, primaryAddress);

        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN);
        assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY);
        assertEquals(translatedAddress.getSignalingPointCode(), 123);
        assertEquals(translatedAddress.getSubsystemNumber(), 8);
        assertEquals(translatedAddress.getGlobalTitle().getDigits(), "4414257897897");
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate5() throws Exception {
        // Match any digits keep the digits in the and add a PC(123) & SSN (8).

        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(0,
                NumberingPlan.valueOf(1), NatureOfAddress.valueOf(4), "*"), 6);

        SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 6045,
                GlobalTitle.getInstance("-"), 6);

        RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "K");
        rule.setPrimaryAddressId(1);

        SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(0,
                NumberingPlan.valueOf(1), NatureOfAddress.valueOf(4), "4414257897897"), 6);

        assertTrue(rule.matches(address, false));

        SccpAddress translatedAddress = rule.translate(address, primaryAddress);

        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
        assertEquals(
                translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS);
        assertEquals(translatedAddress.getSignalingPointCode(), 6045);
        assertEquals(translatedAddress.getSubsystemNumber(), 6);
        assertEquals(translatedAddress.getGlobalTitle().getDigits(), "4414257897897");

        GT0100 gt = (GT0100) translatedAddress.getGlobalTitle();
        assertEquals(gt.getTranslationType(), 0);
        assertEquals(gt.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gt.getNatureOfAddress(), NatureOfAddress.INTERNATIONAL);
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate6() throws Exception {
        // Match any GT Digits, keep the SSN from original address and add PC 123

        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
                GlobalTitle.getInstance(1, "*"), 0);

        SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
                GlobalTitle.getInstance(1, "-"), 0);

        RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "K");
        rule.setPrimaryAddressId(1);

        SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "1234567"), 8);

        assertTrue(rule.matches(address, false));

        SccpAddress translatedAddress = rule.translate(address, primaryAddress);

        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
        assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY);
        assertEquals(translatedAddress.getSignalingPointCode(), 123);
        assertEquals(translatedAddress.getSubsystemNumber(), 8);
        assertEquals(translatedAddress.getGlobalTitle().getDigits(), "1234567");
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate7() throws Exception {
        // The case when address length is less then size

        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "555"), 0);

        SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
                GlobalTitle.getInstance(1, "-"), 0);

        RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "K");
        rule.setPrimaryAddressId(1);

        SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "55"), 8);

        // TODO: the exception is here
        assertFalse(rule.matches(address, false));
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate8() throws Exception {
        // Some bad pattern

        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "*/5555"), 0);

        SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
                GlobalTitle.getInstance(1, "-/-"), 0);

        RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "K/K");
        rule.setPrimaryAddressId(1);

        SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "222"), 8);

        assertTrue(rule.matches(address, false));

        // TODO: the exception is here
        SccpAddress translatedAddress = rule.translate(address, primaryAddress);

        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
        assertEquals(translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_ONLY);
        assertEquals(translatedAddress.getSignalingPointCode(), 123);
        assertEquals(translatedAddress.getSubsystemNumber(), 8);
        assertEquals(translatedAddress.getGlobalTitle().getDigits(), "222");
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate9() throws Exception {
        // OriginationType checking

        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
                GlobalTitle.getInstance(1, "*"), 0);

        SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "222"), 8);

        SccpStackImpl stack = new SccpStackImpl("test");
        MessageFactoryImpl mesFact = new MessageFactoryImpl(stack);
        SccpMessage msgLocalOrig = mesFact.createDataMessageClass1(address, null, new byte[5], 0, 6, false, null, null);
        byte[] b = new byte[] { 9, 0x01, 0x03, 0x05, 0x09, 0x02, 0x42, 0x08, 0x04, 0x43, 0x01, 0x00, 0x08, 0x5D, 0x62, 0x5B,
                0x48, 0x04, 0x00, 0x02, 0x00, 0x30, 0x6B, 0x1A, 0x28, 0x18, 0x06, 0x07, 0x00, 0x11, (byte) 0x86, 0x05, 0x01,
                0x01, 0x01, (byte) 0xA0, 0x0D, 0x60, 0x0B, (byte) 0xA1, 0x09, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x00, 0x19,
                0x02, 0x6C, 0x37, (byte) 0xA1, 0x35, 0x02, 0x01, 0x01, 0x02, 0x01, 0x2E, 0x30, 0x2D, (byte) 0x80, 0x05,
                (byte) 0x89, 0x67, 0x45, 0x23, (byte) 0xF1, (byte) 0x84, 0x06, (byte) 0xA1, 0x21, 0x43, 0x65, (byte) 0x87,
                (byte) 0xF9, 0x04, 0x1C, 0x2C, 0x09, 0x04, 0x21, 0x43, 0x65, (byte) 0x87, (byte) 0xF9, 0x04, 0x00, 0x11, 0x30,
                (byte) 0x92, 0x60, 0x60, 0x62, 0x00, 0x0B, (byte) 0xC8, 0x32, (byte) 0x9B, (byte) 0xFD, 0x06, 0x5D,
                (byte) 0xDF, 0x72, 0x36, 0x19 };
        ByteArrayInputStream buf = new ByteArrayInputStream(b);
        int type = buf.read();
        SccpMessage msgRemoteOrig = mesFact.createMessage(type, 101, 102, 0, buf);

        RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.LocalOriginated,
                pattern, "K");
        rule.setPrimaryAddressId(1);
        assertTrue(rule.matches(address, msgLocalOrig.getIsMtpOriginated()));
        assertFalse(rule.matches(address, msgRemoteOrig.getIsMtpOriginated()));

        rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.RemoteOriginated, pattern, "K");
        rule.setPrimaryAddressId(1);
        assertFalse(rule.matches(address, msgLocalOrig.getIsMtpOriginated()));
        assertTrue(rule.matches(address, msgRemoteOrig.getIsMtpOriginated()));

        rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "K");
        rule.setPrimaryAddressId(1);
        assertTrue(rule.matches(address, msgLocalOrig.getIsMtpOriginated()));
        assertTrue(rule.matches(address, msgRemoteOrig.getIsMtpOriginated()));
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate10() throws Exception {
        // Test Broadcast RuleType

        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(0,
                NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, "92300010020"), 146);

        SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 7574,
                GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, "92300010020"), 146);

        SccpAddress secondaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 2186,
                GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, "92300001009"), 146);

        SccpAddress newClgPartyAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0,
                GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, "92300010321"), 146);

        RuleImpl rule = new RuleImpl(RuleType.Broadcast, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "R");
        rule.setPrimaryAddressId(1);
        rule.setSecondaryAddressId(2);
        rule.setNewCallingPartyAddressId(3);

        SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(0,
                NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, "92300010020"), 146);

        assertTrue(rule.matches(address, true));

        SccpAddress translatedAddress = rule.translate(address, primaryAddress);

        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
        assertEquals(
                translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS);
        assertEquals(translatedAddress.getSignalingPointCode(), 7574);
        assertEquals(translatedAddress.getSubsystemNumber(), 146);
        assertEquals(translatedAddress.getGlobalTitle().getDigits(), "92300010020");

        SccpAddress translatedAddress2 = rule.translate(address, secondaryAddress);

        assertEquals(translatedAddress2.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
        assertEquals(
                translatedAddress2.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS);
        assertEquals(translatedAddress2.getSignalingPointCode(), 2186);
        assertEquals(translatedAddress2.getSubsystemNumber(), 146);
        assertEquals(translatedAddress2.getGlobalTitle().getDigits(), "92300001009");
    }

    @Test(groups = { "router", "functional.encode" })
    public void testSerialization() throws Exception {
        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "441425/*"), 0);

        SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
                GlobalTitle.getInstance("-/-"), 8);

        RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "R/K");
        rule.setPrimaryAddressId(1);

        // Writes
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t"); // Optional (use tabulation for
        // indentation).
        writer.write(rule, "Rule", RuleImpl.class);
        writer.close();

        System.out.println(output.toString());

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        RuleImpl aiOut = reader.read("Rule", RuleImpl.class);

        assertNotNull(aiOut);
        assertEquals(aiOut.getRuleType(), RuleType.Solitary);
        assertEquals(aiOut.getLoadSharingAlgorithm(), LoadSharingAlgorithm.Undefined);
        assertEquals(aiOut.getOriginationType(), OriginationType.All);
        assertTrue(aiOut.getPattern().getGlobalTitle().getDigits().equals("441425/*"));
        assertTrue(aiOut.getMask().equals("R/K"));
        assertEquals(aiOut.getPrimaryAddressId(), 1);
        assertEquals(aiOut.getSecondaryAddressId(), 0);
        assertNull(aiOut.getNewCallingPartyAddressId());

        rule = new RuleImpl(RuleType.Broadcast, LoadSharingAlgorithm.Bit2, OriginationType.LocalOriginated, pattern, "R/K");
        rule.setPrimaryAddressId(11);
        rule.setSecondaryAddressId(12);
        rule.setNewCallingPartyAddressId(13);

        // Writes
        output = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t"); // Optional (use tabulation for
        // indentation).
        writer.write(rule, "Rule", RuleImpl.class);
        writer.close();

        System.out.println(output.toString());

        input = new ByteArrayInputStream(output.toByteArray());
        reader = XMLObjectReader.newInstance(input);
        aiOut = reader.read("Rule", RuleImpl.class);

        assertNotNull(aiOut);
        assertEquals(aiOut.getRuleType(), RuleType.Broadcast);
        assertEquals(aiOut.getLoadSharingAlgorithm(), LoadSharingAlgorithm.Bit2);
        assertEquals(aiOut.getOriginationType(), OriginationType.LocalOriginated);
        assertTrue(aiOut.getPattern().getGlobalTitle().getDigits().equals("441425/*"));
        assertTrue(aiOut.getMask().equals("R/K"));
        assertEquals(aiOut.getPrimaryAddressId(), 11);
        assertEquals(aiOut.getSecondaryAddressId(), 12);
        assertEquals((int) aiOut.getNewCallingPartyAddressId(), 13);

    }

    /**
     * Test of toString method, of class Rule.
     */
    @Test(groups = { "router", "functional.encode" })
    public void testToString() {
        SccpAddress pattern = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, GlobalTitle.getInstance(1,
                "123/???/7"), 0);

        SccpAddress primaryAddress = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 123,
                GlobalTitle.getInstance(1, "333/---/4"), 0);

        RuleImpl rule = new RuleImpl(RuleType.Solitary, LoadSharingAlgorithm.Undefined, OriginationType.All, pattern, "R/K/R");
        rule.setPrimaryAddressId(1);
        rule.setSecondaryAddressId(2);

        System.out.println(rule.toString());

        // assertEquals( rule.toString(),RULE);
    }
}
