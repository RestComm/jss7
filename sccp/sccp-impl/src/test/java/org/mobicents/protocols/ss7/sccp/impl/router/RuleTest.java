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
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.sccp.message.SccpMessage;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle0100;
import org.mobicents.protocols.ss7.sccp.parameter.ParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * @author amit bhayani
 * @author kulikov
 */
public class RuleTest {
    private static final String RULE = "1;pattern(ROUTING_BASED_ON_GLOBAL_TITLE#tt= #np= #noa=NATIONAL#digits=9023629581#ssn= #dpc=0#dpcProhibited=false);translation(ROUTING_BASED_ON_GLOBAL_TITLE#tt= #np= #noa=INTERNATIONAL#digits=79023629581#ssn= #dpc=345#dpcProhibited=false);\n";
    ParameterFactory factory = new ParameterFactoryImpl();
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
        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle( "917797706077/*",0,
                NumberingPlan.ISDN_TELEPHONY, null, NatureOfAddress.INTERNATIONAL), 0, 8);
        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, factory.createGlobalTitle( "917797706077/-",0, NumberingPlan.ISDN_TELEPHONY, null, NatureOfAddress.INTERNATIONAL), 792,
                8);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K/R", 0, null);
        rule.setPrimaryAddressId(1);

        SccpAddress address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("917797706077",0,
                NumberingPlan.ISDN_TELEPHONY, null, NatureOfAddress.INTERNATIONAL), 0, 8);

        assertTrue(rule.matches(address, null, false, 0));

        SccpAddress translatedAddress = rule.translate(address, primaryAddress);

        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN);
        assertEquals(
                translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS);
        assertEquals(translatedAddress.getSignalingPointCode(), 792);
        assertEquals(translatedAddress.getSubsystemNumber(), 8);
        assertEquals(translatedAddress.getGlobalTitle().getDigits(), "917797706077");

        address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("91779770607720",0,
                NumberingPlan.ISDN_TELEPHONY, null, NatureOfAddress.INTERNATIONAL), 0, 8);

        assertTrue(rule.matches(address, null, false, 0));

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
        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("123456789", 1), 0, 0);
        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, factory.createGlobalTitle("-"), 123, 8);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "R", 0, null);
        rule.setPrimaryAddressId(1);

        SccpAddress address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("123456789", 1), 0, 0);

        assertTrue(rule.matches(address, null, false, 0));

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

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("123/???/7", 1), 0, 0);
        SccpAddress patternDefaultCalling = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);

        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("333/---/4", 1), 123, 0);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "R/K/R", 0, patternDefaultCalling);
        rule.setPrimaryAddressId(1);

        SccpAddress address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("1234567", 1), 0, 0);

        assertTrue(rule.matches(address, null, false, 0));

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

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("441425/*", 1), 0, 0);
        SccpAddress patternDefaultCalling = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);

        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("-/-"), 123, 8);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "R/K", 0, patternDefaultCalling);
        rule.setPrimaryAddressId(1);

        SccpAddress address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("4414257897897", 1), 0, 0);

        assertTrue(rule.matches(address, null, false, 0));

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

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);
        SccpAddress patternDefaultCalling = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);

        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, factory.createGlobalTitle("-"), 123, 8);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K", 0, patternDefaultCalling);
        rule.setPrimaryAddressId(1);

        SccpAddress address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("4414257897897", 1), 0, 0);

        assertTrue(rule.matches(address, null, false, 0));

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

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle( "*",0,
                NumberingPlan.valueOf(1),null, NatureOfAddress.valueOf(4)), 0, 6);
        SccpAddress patternDefaultCalling = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);

        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("-"), 6045, 6);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K", 0, patternDefaultCalling);
        rule.setPrimaryAddressId(1);

        SccpAddress address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("4414257897897",0,
                NumberingPlan.valueOf(1), null, NatureOfAddress.valueOf(4)), 0, 6);

        assertTrue(rule.matches(address, null, false, 0));

        SccpAddress translatedAddress = rule.translate(address, primaryAddress);

        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
        assertEquals(
                translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS);
        assertEquals(translatedAddress.getSignalingPointCode(), 6045);
        assertEquals(translatedAddress.getSubsystemNumber(), 6);
        assertEquals(translatedAddress.getGlobalTitle().getDigits(), "4414257897897");

        GlobalTitle0100 gt = (GlobalTitle0100) translatedAddress.getGlobalTitle();
        assertEquals(gt.getTranslationType(), 0);
        assertEquals(gt.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gt.getNatureOfAddress(), NatureOfAddress.INTERNATIONAL);
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate6() throws Exception {
        // Match any GT Digits, keep the SSN from original address and add PC 123

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);
        SccpAddress patternDefaultCalling = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);

        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("-", 1), 123, 0);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K", 0, patternDefaultCalling);
        rule.setPrimaryAddressId(1);

        SccpAddress address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("1234567", 1), 0, 8);

        assertTrue(rule.matches(address, null, false, 0));

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

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("555", 1), 0, 0);
        SccpAddress patternDefaultCalling = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);

        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("-", 1), 123, 0);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K", 0, patternDefaultCalling);
        rule.setPrimaryAddressId(1);

        SccpAddress address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("55", 1), 0, 8);

        // TODO: the exception is here
        assertFalse(rule.matches(address, null, false, 0));
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate8() throws Exception {
        // Some bad pattern

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*/5555", 1), 0, 0);
        SccpAddress patternDefaultCalling = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);

        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("-/-", 1), 123, 0);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K/K", 0, patternDefaultCalling);
        rule.setPrimaryAddressId(1);

        SccpAddress address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("222", 1), 0, 8);

        assertTrue(rule.matches(address, null, false, 0));

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

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);
        SccpAddress patternDefaultCalling = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);

        SccpAddress address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("222", 1), 0, 8);

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
        SccpMessage msgRemoteOrig = mesFact.createMessage(type, 101, 102, 0, buf, SccpProtocolVersion.ITU, 0);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.LOCAL,
                pattern, "K", 0, null);
        rule.setPrimaryAddressId(1);
        assertTrue(rule.matches(address, address, msgLocalOrig.getIsMtpOriginated(), 0));
        assertFalse(rule.matches(address, address, msgRemoteOrig.getIsMtpOriginated(), 0));

        rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.REMOTE, pattern, "K", 0, patternDefaultCalling);
        rule.setPrimaryAddressId(1);
        assertFalse(rule.matches(address, address, msgLocalOrig.getIsMtpOriginated(), 0));
        assertTrue(rule.matches(address, address, msgRemoteOrig.getIsMtpOriginated(), 0));

        rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K", 0, patternDefaultCalling);
        rule.setPrimaryAddressId(1);
        assertTrue(rule.matches(address, address, msgLocalOrig.getIsMtpOriginated(), 0));
        assertTrue(rule.matches(address, address, msgRemoteOrig.getIsMtpOriginated(), 0));
    }

    @Test(groups = { "router", "functional.translate" })
    public void testTranslate10() throws Exception {
        // Test Broadcast RuleType

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("92300010020", 0,
                NumberingPlan.ISDN_TELEPHONY, null, NatureOfAddress.INTERNATIONAL), 0, 146);
        SccpAddress patternDefaultCalling = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);

        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("92300010020", 0, NumberingPlan.ISDN_TELEPHONY, null, NatureOfAddress.INTERNATIONAL), 7574, 146);

        SccpAddress secondaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("92300001009", 0, NumberingPlan.ISDN_TELEPHONY, null, NatureOfAddress.INTERNATIONAL), 2186, 146);
        //TODO: XXX: this is not used at all ?
        SccpAddress newClgPartyAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("92300010321", 0, NumberingPlan.ISDN_TELEPHONY, null, NatureOfAddress.INTERNATIONAL), 0, 146);

        RuleImpl rule = new RuleImpl(RuleType.BROADCAST, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "R", 0, patternDefaultCalling);
        rule.setPrimaryAddressId(1);
        rule.setSecondaryAddressId(2);
        rule.setNewCallingPartyAddressId(3);

        SccpAddress address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("92300010020", 0,
                NumberingPlan.ISDN_TELEPHONY, null, NatureOfAddress.INTERNATIONAL), 0, 146);

        assertTrue(rule.matches(address, null, true, 0));

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

    @Test(groups = { "router", "functional.translate" })
    public void testTranslateHex() throws Exception {
        // Test Hex

        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("48??117/*", 0,
                NumberingPlan.ISDN_TELEPHONY, null, NatureOfAddress.INTERNATIONAL), 0, 146);
        SccpAddress patternDefaultCalling = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);

        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("00/00", 0, NumberingPlan.ISDN_TELEPHONY, null, NatureOfAddress.INTERNATIONAL), 7574, 146);

        RuleImpl rule = new RuleImpl(RuleType.DOMINANT, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "K/K", 0, patternDefaultCalling);
        rule.setPrimaryAddressId(1);
        rule.setNewCallingPartyAddressId(3);

        SccpAddress address = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("48CC117735979971", 0,
                NumberingPlan.ISDN_TELEPHONY, null, NatureOfAddress.INTERNATIONAL), 0, 146);

        SccpAddress address1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);

        assertTrue(rule.matches(address, address1, true, 0));

        SccpAddress translatedAddress = rule.translate(address, primaryAddress);

//        assertEquals(translatedAddress.getAddressIndicator().getRoutingIndicator(),
//                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
//        assertEquals(
//                translatedAddress.getAddressIndicator().getGlobalTitleIndicator(),
//                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS);
//        assertEquals(translatedAddress.getSignalingPointCode(), 7574);
//        assertEquals(translatedAddress.getSubsystemNumber(), 146);
//        assertEquals(translatedAddress.getGlobalTitle().getDigits(), "92300010020");
//
//        SccpAddress translatedAddress2 = rule.translate(address, secondaryAddress);
//
//        assertEquals(translatedAddress2.getAddressIndicator().getRoutingIndicator(),
//                RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
//        assertEquals(
//                translatedAddress2.getAddressIndicator().getGlobalTitleIndicator(),
//                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS);
//        assertEquals(translatedAddress2.getSignalingPointCode(), 2186);
//        assertEquals(translatedAddress2.getSubsystemNumber(), 146);
//        assertEquals(translatedAddress2.getGlobalTitle().getDigits(), "92300001009");
    }

    @Test(groups = { "router", "functional.encode" })
    public void testSerialization() throws Exception {
        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("441425/*", 1), 0, 0);
        SccpAddress patternDefaultCalling = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("5678/92", 1), 0, 0);

        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("-/-"), 123, 8);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "R/K", 0,patternDefaultCalling);
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
        assertEquals(aiOut.getRuleType(), RuleType.SOLITARY);
        assertEquals(aiOut.getLoadSharingAlgorithm(), LoadSharingAlgorithm.Undefined);
        assertEquals(aiOut.getOriginationType(), OriginationType.ALL);
        assertTrue(aiOut.getPattern().getGlobalTitle().getDigits().equals("441425/*"));
        assertTrue(aiOut.getMask().equals("R/K"));
        assertEquals(aiOut.getPrimaryAddressId(), 1);
        assertEquals(aiOut.getSecondaryAddressId(), 0);
        assertNull(aiOut.getNewCallingPartyAddressId());
        assertTrue( aiOut.getPatternCallingAddress().getGlobalTitle().getDigits().equals( "5678/92") );

        rule = new RuleImpl(RuleType.BROADCAST, LoadSharingAlgorithm.Bit2, OriginationType.LOCAL, pattern, "R/K", 0, patternDefaultCalling);
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
        assertEquals(aiOut.getRuleType(), RuleType.BROADCAST);
        assertEquals(aiOut.getLoadSharingAlgorithm(), LoadSharingAlgorithm.Bit2);
        assertEquals(aiOut.getOriginationType(), OriginationType.LOCAL);
        assertTrue(aiOut.getPattern().getGlobalTitle().getDigits().equals("441425/*"));
        assertTrue(aiOut.getMask().equals("R/K"));
        assertEquals(aiOut.getPrimaryAddressId(), 11);
        assertEquals(aiOut.getSecondaryAddressId(), 12);
        assertEquals((int) aiOut.getNewCallingPartyAddressId(), 13);
        assertTrue( aiOut.getPatternCallingAddress().getGlobalTitle().getDigits().equals( "5678/92") );

    }

    @Test(groups = { "router", "functional.encode" })
    public void testSerialization2() throws Exception {
        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("441425/*", 1), 0, 0);

        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("-/-"), 123, 8);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "R/K", 0, null);
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
        assertEquals(aiOut.getRuleType(), RuleType.SOLITARY);
        assertEquals(aiOut.getLoadSharingAlgorithm(), LoadSharingAlgorithm.Undefined);
        assertEquals(aiOut.getOriginationType(), OriginationType.ALL);
        assertTrue(aiOut.getPattern().getGlobalTitle().getDigits().equals("441425/*"));
        assertTrue(aiOut.getMask().equals("R/K"));
        assertEquals(aiOut.getPrimaryAddressId(), 1);
        assertEquals(aiOut.getSecondaryAddressId(), 0);
        assertNull(aiOut.getNewCallingPartyAddressId());
        assertNull(aiOut.getPatternCallingAddress());

        rule = new RuleImpl(RuleType.BROADCAST, LoadSharingAlgorithm.Bit2, OriginationType.LOCAL, pattern, "R/K", 0, null);
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
        assertEquals(aiOut.getRuleType(), RuleType.BROADCAST);
        assertEquals(aiOut.getLoadSharingAlgorithm(), LoadSharingAlgorithm.Bit2);
        assertEquals(aiOut.getOriginationType(), OriginationType.LOCAL);
        assertTrue(aiOut.getPattern().getGlobalTitle().getDigits().equals("441425/*"));
        assertTrue(aiOut.getMask().equals("R/K"));
        assertEquals(aiOut.getPrimaryAddressId(), 11);
        assertEquals(aiOut.getSecondaryAddressId(), 12);
        assertEquals((int) aiOut.getNewCallingPartyAddressId(), 13);
        assertNull(aiOut.getPatternCallingAddress());

    }

    /**
     * Test of toString method, of class Rule.
     */
    @Test(groups = { "router", "functional.encode" })
    public void testToString() {
        SccpAddress pattern = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("123/???/7", 1), 0, 0);
        SccpAddress patternDefaultCalling = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("*", 1), 0, 0);

        SccpAddress primaryAddress = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, factory.createGlobalTitle("333/---/4", 1), 123, 0);

        RuleImpl rule = new RuleImpl(RuleType.SOLITARY, LoadSharingAlgorithm.Undefined, OriginationType.ALL, pattern, "R/K/R", 0, patternDefaultCalling);
        rule.setPrimaryAddressId(1);
        rule.setSecondaryAddressId(2);

        System.out.println(rule.toString());

        // assertEquals( rule.toString(),RULE);
    }
}
