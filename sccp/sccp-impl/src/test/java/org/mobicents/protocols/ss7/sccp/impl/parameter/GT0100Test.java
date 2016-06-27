/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
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
    private byte[] dataHex = new byte[] { 0, 17, 3, 9, -94, -53, 89, 8 };
    private ParameterFactoryImpl factory = new ParameterFactoryImpl();

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
        GlobalTitle0100Impl gt1 = new GlobalTitle0100Impl();
        gt1.decode(in, factory, SccpProtocolVersion.ITU);

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
        GlobalTitle0100Impl gt = new GlobalTitle0100Impl("9023629581",0, BCDEvenEncodingScheme.INSTANCE,NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.NATIONAL);

        gt.encode(bout, false, SccpProtocolVersion.ITU);

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
        GlobalTitle0100Impl gt1 = new GlobalTitle0100Impl();
        gt1.decode(in, factory, SccpProtocolVersion.ITU);

        // check results
        assertEquals(gt1.getTranslationType(), 0);
        assertEquals(gt1.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gt1.getDigits(), "902362958");
    }

    @Test(groups = { "parameter", "functional.encode" })
    public void testEncodeHex() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GlobalTitle0100Impl gt = new GlobalTitle0100Impl("902ABC958",0, BCDOddEncodingScheme.INSTANCE,NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.NATIONAL);

        gt.encode(bout, false, SccpProtocolVersion.ITU);

        byte[] res = bout.toByteArray();

        boolean correct = Arrays.equals(dataHex, res);
        assertTrue(correct, "Incorrect encoding");
    }

    @Test(groups = { "parameter", "functional.decode" })
    public void testDecodeHex() throws Exception {
        // wrap data with input stream
        ByteArrayInputStream in = new ByteArrayInputStream(dataHex);

        // create GT object and read data from stream
        GlobalTitle0100Impl gt1 = new GlobalTitle0100Impl();
        gt1.decode(in, factory, SccpProtocolVersion.ITU);

        // check results
        assertEquals(gt1.getTranslationType(), 0);
        assertEquals(gt1.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gt1.getDigits(), "902ABC958");
    }

    /**
     * Test of encode method, of class GT0011.
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncodeOdd() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
   
        GlobalTitle0100Impl gt = new GlobalTitle0100Impl("902362958",0, BCDOddEncodingScheme.INSTANCE,NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.NATIONAL);

        gt.encode(bout, false, SccpProtocolVersion.ITU);

        byte[] res = bout.toByteArray();

        boolean correct = Arrays.equals(dataOdd, res);
        assertTrue(correct, "Incorrect encoding");
    }

    @Test(groups = { "parameter", "functional.encode" })
    public void testSerialization() throws Exception {

        GlobalTitle0100Impl gt = new GlobalTitle0100Impl("9023629581",0, BCDEvenEncodingScheme.INSTANCE,NumberingPlan.ISDN_MOBILE, NatureOfAddress.NATIONAL);

        // Writes
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t"); // Optional (use tabulation for
        // indentation).
        writer.write(gt, "GT0100", GlobalTitle0100Impl.class);
        writer.close();

        System.out.println(output.toString());

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        GlobalTitle0100Impl aiOut = reader.read("GT0100", GlobalTitle0100Impl.class);

        // check results
        assertEquals(aiOut.getNatureOfAddress(), NatureOfAddress.NATIONAL);
        assertEquals(aiOut.getTranslationType(), 0);
        assertEquals(aiOut.getNumberingPlan(), NumberingPlan.ISDN_MOBILE);
        assertEquals(aiOut.getDigits(), "9023629581");
    }

}
