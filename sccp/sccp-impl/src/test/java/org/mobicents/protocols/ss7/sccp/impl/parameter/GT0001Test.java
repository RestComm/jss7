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
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
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
    private ParameterFactoryImpl factory = new ParameterFactoryImpl();

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
        GlobalTitle0001Impl gt1 = new GlobalTitle0001Impl();
        gt1.decode(in, factory, SccpProtocolVersion.ITU);

        // check results
        assertEquals(gt1.getNatureOfAddress(), NatureOfAddress.NATIONAL);
        assertEquals(gt1.getDigits(), "9023629581");
    }

    /**
     * Test of encode method, of class GT0001Codec.
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncodeEven() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GlobalTitle0001Impl gt = new GlobalTitle0001Impl( "9023629581",NatureOfAddress.NATIONAL);

        gt.encode(bout, false, SccpProtocolVersion.ITU);

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
        GlobalTitle0001Impl gt1 = new GlobalTitle0001Impl();
        gt1.decode(in, factory, SccpProtocolVersion.ITU);
        // check results
        assertEquals(gt1.getNatureOfAddress(), NatureOfAddress.NATIONAL);
        assertEquals(gt1.getDigits(), "902362958");
    }

    /**
     * Test of encode method, of class GT0001Codec.
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncodeOdd() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GlobalTitle0001Impl gt = new GlobalTitle0001Impl("902362958",NatureOfAddress.NATIONAL);

        gt.encode(bout, false, SccpProtocolVersion.ITU);

        byte[] res = bout.toByteArray();

        boolean correct = Arrays.equals(dataOdd, res);
        assertTrue(correct, "Incorrect encoding");
    }

    @Test(groups = { "parameter", "functional.encode" })
    public void testSerialization() throws Exception {
        GlobalTitle0001Impl gt = new GlobalTitle0001Impl("9023629581",NatureOfAddress.NATIONAL);

        // Writes
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(output);
        writer.setIndentation("\t"); // Optional (use tabulation for
        // indentation).
        writer.write(gt, "GT0001", GlobalTitle0001Impl.class);
        writer.close();

        System.out.println(output.toString());

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        XMLObjectReader reader = XMLObjectReader.newInstance(input);
        GlobalTitle0001Impl aiOut = reader.read("GT0001", GlobalTitle0001Impl.class);

        // check results
        assertEquals(aiOut.getNatureOfAddress(), NatureOfAddress.NATIONAL);
        assertEquals(aiOut.getDigits(), "9023629581");
    }
}
