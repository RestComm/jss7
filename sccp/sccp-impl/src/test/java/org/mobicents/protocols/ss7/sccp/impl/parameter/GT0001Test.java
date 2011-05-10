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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.sccp.parameter.GT0001;

/**
 * @author amit bhayani
 * @author kulikov
 * @author baranowb
 */
public class GT0001Test {

	private byte[] dataEven = new byte[] {              3,0x09,0x32,0x26,0x59,0x18};
	private byte[] dataOdd = new byte[] {(byte)(3 | 0x80),0x09,0x32,0x26,0x59,0x08};
    private GT0001Codec codec = new GT0001Codec();
    
    public GT0001Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of decode method, of class GT0001Codec.
     */
    @Test
    public void testDecodeEven() throws Exception {
        //wrap data with input stream
        ByteArrayInputStream in = new ByteArrayInputStream(dataEven);
        
        //create GT object and read data from stream
        GT0001 gt1 = (GT0001) codec.decode(in);
        
        //check results
        assertEquals(NatureOfAddress.NATIONAL, gt1.getNoA());
        assertEquals("9023629581", gt1.getDigits());
    }

    /**
     * Test of encode method, of class GT0001Codec.
     */
    @Test
    public void testEncodeEven() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GT0001 gt = new GT0001(NatureOfAddress.NATIONAL, "9023629581");
        
        codec.encode(gt, bout);
        
        byte[] res = bout.toByteArray();
        
        boolean correct = Arrays.equals(dataEven, res);        
        assertTrue("Incorrect encoding", correct);
    }
    

    /**
     * Test of decode method, of class GT0001Codec.
     */
    @Test
    public void testDecodeOdd() throws Exception {
        //wrap data with input stream
        ByteArrayInputStream in = new ByteArrayInputStream(dataOdd);
        
        //create GT object and read data from stream
        GT0001 gt1 = (GT0001) codec.decode(in);
        
        //check results
        assertEquals(NatureOfAddress.NATIONAL, gt1.getNoA());
        assertEquals("902362958", gt1.getDigits());
    }

    /**
     * Test of encode method, of class GT0001Codec.
     */
    @Test
    public void testEncodeOdd() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GT0001 gt = new GT0001(NatureOfAddress.NATIONAL, "902362958");
        
        codec.encode(gt, bout);
        
        byte[] res = bout.toByteArray();
        
        boolean correct = Arrays.equals(dataOdd, res);        
        assertTrue("Incorrect encoding", correct);
    }
    
    
    @Test
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
		
        //check results
        assertEquals(NatureOfAddress.NATIONAL, aiOut.getNoA());
        assertEquals("9023629581", aiOut.getDigits());
    }
}