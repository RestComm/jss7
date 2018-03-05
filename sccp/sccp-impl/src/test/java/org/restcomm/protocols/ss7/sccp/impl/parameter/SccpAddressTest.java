/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.restcomm.protocols.ss7.sccp.impl.parameter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.restcomm.protocols.ss7.indicator.GlobalTitleIndicator;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.parameter.BCDOddEncodingScheme;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle0011;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author kulikov
 * @author sergey vetyunev
 */
public class SccpAddressTest {

    // private SccpAddressCodec codec = new SccpAddressCodec(false);
    private byte[] data = new byte[] { 0x12, (byte) 0x92, 0x00, 0x11, 0x04, (byte) 0x97, 0x20, (byte) 0x73, 0x00, (byte) 0x92,
            0x09 };

    private byte[] data4 = new byte[] { -123, -110, 0, 17, -105, 32, 115, 0, -110, 9 };
    private byte[] data5 = new byte[] { -121, -110, 0, 18, 122, 0, 17, -105, 32, 115, 0, -110, 9 };

    private ParameterFactoryImpl factory = new ParameterFactoryImpl();
    public SccpAddressTest() {
    }

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

    /**
     * Test of decode method, of class SccpAddressCodec.
     */
    @Test(groups = { "parameter", "functional.decode" })
    public void testDecode1() throws Exception {
        SccpAddressImpl address = new SccpAddressImpl();
        address.decode(new ByteArrayInputStream(data), factory, SccpProtocolVersion.ITU);
        assertEquals(address.getSignalingPointCode(), 0);
        assertEquals(address.getSubsystemNumber(), 146);
        assertEquals(address.getGlobalTitle().getDigits(), "79023700299");
    }

    @Test(groups = { "parameter", "functional.decode" })
    public void testDecode2() throws Exception {
        SccpAddressImpl address = new SccpAddressImpl();
        address.decode(new ByteArrayInputStream(new byte[] { 0x42, 0x08 }), factory, SccpProtocolVersion.ITU);
        assertEquals(address.getSignalingPointCode(), 0);
        assertEquals(address.getSubsystemNumber(), 8);
        assertNull(address.getGlobalTitle());
    }

    @Test(groups = { "parameter", "functional.decode" })
    public void testDecode4() throws Exception {
        SccpAddressImpl address = new SccpAddressImpl();
        address.decode(new ByteArrayInputStream(data4), factory, SccpProtocolVersion.ANSI);
        assertEquals(address.getSignalingPointCode(), 0);
        assertEquals(address.getSubsystemNumber(), 146);
        assertEquals(address.getAddressIndicator().getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
        assertFalse(address.getAddressIndicator().isPCPresent());
        assertTrue(address.getAddressIndicator().isSSNPresent());
        assertTrue(address.getAddressIndicator().isReservedForNationalUseBit());
        assertEquals(address.getAddressIndicator().getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME);
        GlobalTitle gt = address.getGlobalTitle();
        assertEquals(gt.getDigits(), "79023700299");
        assertEquals(gt.getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME);
        GlobalTitle0011 gtt = (GlobalTitle0011)gt;
        assertEquals(gtt.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gtt.getEncodingScheme(), BCDOddEncodingScheme.INSTANCE);
        assertEquals(gtt.getTranslationType(), 0);

        address.decode(new ByteArrayInputStream(data5), factory, SccpProtocolVersion.ANSI);
        assertEquals(address.getSignalingPointCode(), 8000000);
        assertEquals(address.getSubsystemNumber(), 146);
        assertEquals(address.getAddressIndicator().getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
        assertTrue(address.getAddressIndicator().isPCPresent());
        assertTrue(address.getAddressIndicator().isSSNPresent());
        assertTrue(address.getAddressIndicator().isReservedForNationalUseBit());
        assertEquals(address.getAddressIndicator().getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME);
        gt = address.getGlobalTitle();
        assertEquals(gt.getDigits(), "79023700299");
        assertEquals(gt.getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME);
        gtt = (GlobalTitle0011)gt;
        assertEquals(gtt.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gtt.getEncodingScheme(), BCDOddEncodingScheme.INSTANCE);
        assertEquals(gtt.getTranslationType(), 0);
    }

    /**
     * Test of encode method, of class SccpAddressCodec.
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncode() throws Exception {
        GlobalTitle gt = factory.createGlobalTitle("79023700299",0,NumberingPlan.ISDN_TELEPHONY, BCDOddEncodingScheme.INSTANCE,NatureOfAddress.INTERNATIONAL); 
        SccpAddressImpl address = (SccpAddressImpl)factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,gt, 0, 146); 
                
        byte[] bin = address.encode(false, SccpProtocolVersion.ITU);
        assertTrue(Arrays.equals(data, bin), "Wrong encoding");
    }

    @Test(groups = { "parameter", "functional.encode" })
    public void testEncode2() throws Exception {
        SccpAddressImpl address = (SccpAddressImpl) factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,0,  8);
        byte[] bin = address.encode(false, SccpProtocolVersion.ITU);
        assertTrue(Arrays.equals(new byte[] { 0x42, 0x08 }, bin), "Wrong encoding");
    }

    /**
     * Test to see if the DPC is removed from the SCCP Address when instructed
     *
     * @throws Exception
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncode3() throws Exception {
        byte[] data1 = new byte[] { 0x12, 0x06, 0x00, 0x11, 0x04, 0x39, 0x07, (byte) 0x92, 0x49, 0x00, 0x06 };
        // SccpAddressCodec codec = new SccpAddressCodec(true);

        GlobalTitle gt = factory.createGlobalTitle("93702994006",0, NumberingPlan.ISDN_TELEPHONY, BCDOddEncodingScheme.INSTANCE,NatureOfAddress.INTERNATIONAL);
        SccpAddressImpl address = (SccpAddressImpl) factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 5530, 6);
        byte[] bin = address.encode(true, SccpProtocolVersion.ITU);
        assertTrue(Arrays.equals(data1, bin), "Wrong encoding");

        // Now test decode

    }

    @Test(groups = { "parameter", "functional.encode" })
    public void testEncode4() throws Exception {
        GlobalTitle gt = factory.createGlobalTitle("79023700299", 0, NumberingPlan.ISDN_TELEPHONY, BCDOddEncodingScheme.INSTANCE);
        SccpAddressImpl address = (SccpAddressImpl) factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 146);

        byte[] bin = address.encode(false, SccpProtocolVersion.ANSI);
        assertTrue(Arrays.equals(data4, bin), "Wrong encoding");

        bin = address.encode(true, SccpProtocolVersion.ANSI);
        assertTrue(Arrays.equals(data4, bin), "Wrong encoding");

        address = (SccpAddressImpl) factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 8000000, 146);

        bin = address.encode(false, SccpProtocolVersion.ANSI);
        assertTrue(Arrays.equals(data5, bin), "Wrong encoding");

        bin = address.encode(true, SccpProtocolVersion.ANSI);
        assertTrue(Arrays.equals(data4, bin), "Wrong encoding");
    }

    /**
     * Test of getAddressIndicator method, of class SccpAddress.
     */
    @Test
    public void testEquals() {
        GlobalTitle gt = factory.createGlobalTitle("123",NatureOfAddress.NATIONAL);
        SccpAddress a1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 0);
        SccpAddress a2 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 0);
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    public void testEquals1() {
        GlobalTitle gt1 = factory.createGlobalTitle("79023700271",0,NumberingPlan.ISDN_TELEPHONY,null,NatureOfAddress.INTERNATIONAL); 
        SccpAddress a1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 146, 0);

        GlobalTitle gt2 = factory.createGlobalTitle("79023700271",0,NumberingPlan.ISDN_TELEPHONY,null,NatureOfAddress.INTERNATIONAL);
        SccpAddress a2 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, 146, 0);


        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    public void testSerialization() throws Exception {

        GlobalTitle gt = factory.createGlobalTitle("79023700271",0,NumberingPlan.ISDN_TELEPHONY,null,NatureOfAddress.INTERNATIONAL); 
        SccpAddress a1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 146, 0);

        // Writes
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t"); // Optional (use tabulation for
        // indentation).
        writer.write(a1, "SccpAddress", SccpAddress.class);
        writer.close();

        System.out.println(output.toString());

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        SccpAddress aiOut = reader.read("SccpAddress", SccpAddressImpl.class);

        assertEquals(
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS,
                aiOut.getAddressIndicator().getGlobalTitleIndicator());
        assertEquals(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, aiOut.getAddressIndicator().getRoutingIndicator());
        assertTrue(aiOut.getAddressIndicator().isPCPresent());
        assertFalse(aiOut.getAddressIndicator().isSSNPresent());

        assertEquals(146, aiOut.getSignalingPointCode());
        assertEquals(0, aiOut.getSubsystemNumber());

        assertEquals("79023700271", aiOut.getGlobalTitle().getDigits());
    }

    @Test
    public void testSerialization1() throws Exception {

        SccpAddressImpl a1 = (SccpAddressImpl) factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 146, 8);

        // Writes
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t"); // Optional (use tabulation for
        // indentation).
        writer.write(a1, "SccpAddress", SccpAddressImpl.class);
        writer.close();

        System.out.println(output.toString());

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        SccpAddress aiOut = reader.read("SccpAddress", SccpAddressImpl.class);

        assertEquals(GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED, aiOut.getAddressIndicator().getGlobalTitleIndicator());
        assertEquals(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, aiOut.getAddressIndicator().getRoutingIndicator());
        assertTrue(aiOut.getAddressIndicator().isPCPresent());
        assertTrue(aiOut.getAddressIndicator().isSSNPresent());

        assertEquals(146, aiOut.getSignalingPointCode());
        assertEquals(8, aiOut.getSubsystemNumber());

        assertNull(aiOut.getGlobalTitle());
    }

}
