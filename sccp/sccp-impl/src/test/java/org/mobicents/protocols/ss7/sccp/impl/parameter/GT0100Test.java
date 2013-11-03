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
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.parameter.GT0100;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author kulikov
 * @author baranowb
 */
public class GT0100Test {

    private byte[] dataEven = new byte[] { 0, 0x12, 0x03, 0x09, 0x32, 0x26, 0x59, 0x18 }; // Es.Even -> 0x12 & 0x0F
    private byte[] dataOdd = new byte[] { 0, 0x11, 0x03, 0x09, 0x32, 0x26, 0x59, 0x08 }; // Es.Odd -> 0x11 & 0x0F - thus leading
                                                                                         // zero in last hex
    private GT0100Codec codec = new GT0100Codec();

    public GT0100Test() {
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
     * Test of decode method, of class GT0011.
     */
    @Test(groups = { "parameter", "functional.decode" })
    public void testDecodeEven() throws Exception {
        // wrap data with input stream
        ByteArrayInputStream in = new ByteArrayInputStream(dataEven);

        // create GT object and read data from stream
        GT0100 gt1 = (GT0100) codec.decode(in);

        // check results
        assertEquals(gt1.getTranslationType(), 0);
        assertEquals(gt1.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gt1.getDigits(), "9023629581");
    }

    /**
     * Test of encode method, of class GT0011.
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncodeEven() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GT0100 gt = new GT0100(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.NATIONAL, "9023629581");

        codec.encode(gt, bout);

        byte[] res = bout.toByteArray();

        boolean correct = Arrays.equals(dataEven, res);
        assertTrue(correct, "Incorrect encoding");
    }

    /**
     * Test of decode method, of class GT0011.
     */
    @Test(groups = { "parameter", "functional.decode" })
    public void testDecodeOdd() throws Exception {
        // wrap data with input stream
        ByteArrayInputStream in = new ByteArrayInputStream(dataOdd);

        // create GT object and read data from stream
        GT0100 gt1 = (GT0100) codec.decode(in);

        // check results
        assertEquals(gt1.getTranslationType(), 0);
        assertEquals(gt1.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gt1.getDigits(), "902362958");
    }

    /**
     * Test of encode method, of class GT0011.
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncodeOdd() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GT0100 gt = new GT0100(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.NATIONAL, "902362958");

        codec.encode(gt, bout);

        byte[] res = bout.toByteArray();

        boolean correct = Arrays.equals(dataOdd, res);
        assertTrue(correct, "Incorrect encoding");
    }

    @Test(groups = { "parameter", "functional.encode" })
    public void testSerialization() throws Exception {
        GT0100 gt = new GT0100(0, NumberingPlan.ISDN_MOBILE, NatureOfAddress.NATIONAL, "9023629581");

        // Writes
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t"); // Optional (use tabulation for
        // indentation).
        writer.write(gt, "GT0100", GT0100.class);
        writer.close();

        System.out.println(output.toString());

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        GT0100 aiOut = reader.read("GT0100", GT0100.class);

        // check results
        assertEquals(aiOut.getNatureOfAddress(), NatureOfAddress.NATIONAL);
        assertEquals(aiOut.getTranslationType(), 0);
        assertEquals(aiOut.getNumberingPlan(), NumberingPlan.ISDN_MOBILE);
        assertEquals(aiOut.getDigits(), "9023629581");
    }

}
