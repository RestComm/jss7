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

import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.parameter.GT0011;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author kulikov
 */
public class GT0011Test {

    private byte[] data = new byte[] { 0, 0x12, 0x09, 0x32, 0x26, 0x59, 0x18 };
    private GT0011Codec codec = new GT0011Codec();

    public GT0011Test() {
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
     * Test of decode method, of class GT0011Codec.
     */
    @Test(groups = { "parameter", "functional.decode" })
    public void testDecode() throws Exception {
        // wrap data with input stream
        ByteArrayInputStream in = new ByteArrayInputStream(data);

        // create GT object and read data from stream
        GT0011 gt1 = (GT0011) codec.decode(in);

        // check results
        assertEquals(gt1.getTranslationType(), 0);
        assertEquals(gt1.getNp(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gt1.getDigits(), "9023629581");
    }

    /**
     * Test of encode method, of class GT0011Codec.
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncode() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GT0011 gt = new GT0011(0, NumberingPlan.ISDN_TELEPHONY, "9023629581");

        codec.encode(gt, bout);

        byte[] res = bout.toByteArray();

        boolean correct = Arrays.equals(data, res);
        assertTrue(correct, "Incorrect encoding");
    }

    @Test(groups = { "parameter", "functional.encode" })
    public void testSerialization() throws Exception {
        GT0011 gt = new GT0011(0, NumberingPlan.ISDN_TELEPHONY, "9023629581");

        // Writes
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t"); // Optional (use tabulation for
        // indentation).
        writer.write(gt, "GT0011", GT0011.class);
        writer.close();

        System.out.println(output.toString());

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        GT0011 aiOut = reader.read("GT0011", GT0011.class);

        // check results
        assertEquals(aiOut.getTranslationType(), 0);
        assertEquals(aiOut.getNp(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(aiOut.getDigits(), "9023629581");
    }

}
