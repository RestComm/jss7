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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.indicator.GlobalTitleIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author kulikov
 */
public class SccpAddressTest {

    // private SccpAddressCodec codec = new SccpAddressCodec(false);
    private byte[] data = new byte[] { 0x12, (byte) 0x92, 0x00, 0x11, 0x04, (byte) 0x97, 0x20, (byte) 0x73, 0x00, (byte) 0x92,
            0x09 };

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
        SccpAddress address = SccpAddressCodec.decode(data);

        assertEquals(address.getSignalingPointCode(), 0);
        assertEquals(address.getSubsystemNumber(), 146);
        assertEquals(address.getGlobalTitle().getDigits(), "79023700299");
    }

    @Test(groups = { "parameter", "functional.decode" })
    public void testDecode2() throws Exception {
        SccpAddress address = SccpAddressCodec.decode(new byte[] { 0x42, 0x08 });

        assertEquals(address.getSignalingPointCode(), 0);
        assertEquals(address.getSubsystemNumber(), 8);
        assertNull(address.getGlobalTitle());
    }

    /**
     * Test of encode method, of class SccpAddressCodec.
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncode() throws Exception {
        GlobalTitle gt = GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, "79023700299");
        SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt, 146);
        byte[] bin = SccpAddressCodec.encode(address, false);
        assertTrue(Arrays.equals(data, bin), "Wrong encoding");
    }

    @Test(groups = { "parameter", "functional.encode" })
    public void testEncode2() throws Exception {
        SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 0, null, 8);
        byte[] bin = SccpAddressCodec.encode(address, false);
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

        GlobalTitle gt = GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, "93702994006");
        SccpAddress address = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 5530, gt, 6);
        byte[] bin = SccpAddressCodec.encode(address, true);
        assertTrue(Arrays.equals(data1, bin), "Wrong encoding");

        // Now test decode

    }

    /**
     * Test of getAddressIndicator method, of class SccpAddress.
     */
    @Test
    public void testEquals() {
        GlobalTitle gt = GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "123");
        SccpAddress a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt, 0);
        SccpAddress a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 0, gt, 0);
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    public void testEquals1() {
        GlobalTitle gt = GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, "79023700271");
        SccpAddress a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 146, gt, 0);

        HashMap<SccpAddress, Integer> map = new HashMap();
        map.put(a1, 1);

        SccpAddress a2 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 146, gt, 0);
        Integer i = map.get(a2);

        if (i == null) {
            fail("Address did not match");
        }

        assertEquals(new Integer(1), i);
    }

    @Test
    public void testSerialization() throws Exception {
        GlobalTitle gt = GlobalTitle.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.INTERNATIONAL, "79023700271");
        SccpAddress a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, 146, gt, 0);

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
        SccpAddress aiOut = reader.read("SccpAddress", SccpAddress.class);

        assertEquals(
                GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS,
                aiOut.getAddressIndicator().getGlobalTitleIndicator());
        assertEquals(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, aiOut.getAddressIndicator().getRoutingIndicator());
        assertTrue(aiOut.getAddressIndicator().pcPresent());
        assertFalse(aiOut.getAddressIndicator().ssnPresent());

        assertEquals(146, aiOut.getSignalingPointCode());
        assertEquals(0, aiOut.getSubsystemNumber());

        assertEquals("79023700271", aiOut.getGlobalTitle().getDigits());
    }

    @Test
    public void testSerialization1() throws Exception {

        SccpAddress a1 = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, 146, null, 8);

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
        SccpAddress aiOut = reader.read("SccpAddress", SccpAddress.class);

        assertEquals(GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED, aiOut.getAddressIndicator().getGlobalTitleIndicator());
        assertEquals(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, aiOut.getAddressIndicator().getRoutingIndicator());
        assertTrue(aiOut.getAddressIndicator().pcPresent());
        assertTrue(aiOut.getAddressIndicator().ssnPresent());

        assertEquals(146, aiOut.getSignalingPointCode());
        assertEquals(8, aiOut.getSubsystemNumber());

        assertNull(aiOut.getGlobalTitle());
    }

}
