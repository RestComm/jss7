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
import org.mobicents.protocols.ss7.sccp.parameter.GT0001;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * @author kulikov
 * @author baranowb
 */
public class GT0001Test {

    private byte[] dataEven = new byte[] { 3, 0x09, 0x32, 0x26, 0x59, 0x18 };
    private byte[] dataOdd = new byte[] { (byte) (3 | 0x80), 0x09, 0x32, 0x26, 0x59, 0x08 };
    private GT0001Codec codec = new GT0001Codec();

    public GT0001Test() {
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
     * Test of decode method, of class GT0001Codec.
     */
    @Test(groups = { "parameter", "functional.decode" })
    public void testDecodeEven() throws Exception {
        // wrap data with input stream
        ByteArrayInputStream in = new ByteArrayInputStream(dataEven);

        // create GT object and read data from stream
        GT0001 gt1 = (GT0001) codec.decode(in);

        // check results
        assertEquals(gt1.getNoA(), NatureOfAddress.NATIONAL);
        assertEquals(gt1.getDigits(), "9023629581");
    }

    /**
     * Test of encode method, of class GT0001Codec.
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncodeEven() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GT0001 gt = new GT0001(NatureOfAddress.NATIONAL, "9023629581");

        codec.encode(gt, bout);

        byte[] res = bout.toByteArray();

        boolean correct = Arrays.equals(dataEven, res);
        assertTrue(correct, "Incorrect encoding");
    }

    /**
     * Test of decode method, of class GT0001Codec.
     */
    @Test(groups = { "parameter", "functional.decode" })
    public void testDecodeOdd() throws Exception {
        // wrap data with input stream
        ByteArrayInputStream in = new ByteArrayInputStream(dataOdd);

        // create GT object and read data from stream
        GT0001 gt1 = (GT0001) codec.decode(in);

        // check results
        assertEquals(gt1.getNoA(), NatureOfAddress.NATIONAL);
        assertEquals(gt1.getDigits(), "902362958");
    }

    /**
     * Test of encode method, of class GT0001Codec.
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncodeOdd() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GT0001 gt = new GT0001(NatureOfAddress.NATIONAL, "902362958");

        codec.encode(gt, bout);

        byte[] res = bout.toByteArray();

        boolean correct = Arrays.equals(dataOdd, res);
        assertTrue(correct, "Incorrect encoding");
    }

    @Test(groups = { "parameter", "functional.encode" })
    public void testSerialization() throws Exception {
        GT0001 gt = new GT0001(NatureOfAddress.NATIONAL, "9023629581");

        // Writes
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t"); // Optional (use tabulation for
        // indentation).
        writer.write(gt, "GT0001", GT0001.class);
        writer.close();

        System.out.println(output.toString());

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        GT0001 aiOut = reader.read("GT0001", GT0001.class);

        // check results
        assertEquals(aiOut.getNoA(), NatureOfAddress.NATIONAL);
        assertEquals(aiOut.getDigits(), "9023629581");
    }
}
