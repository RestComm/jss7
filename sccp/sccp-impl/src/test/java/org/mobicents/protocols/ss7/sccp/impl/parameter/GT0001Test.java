/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.sccp.parameter.GT0001;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import static org.junit.Assert.*;

/**
 *
 * @author kulikov
 */
public class GT0001Test {

    private byte[] data = new byte[] {3,0x09,0x32,0x26,0x59,0x18};
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
    public void testDecode() throws Exception {
        //wrap data with input stream
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        
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
    public void testEncode() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GT0001 gt = new GT0001(NatureOfAddress.NATIONAL, "9023629581");
        
        codec.encode(gt, bout);
        
        byte[] res = bout.toByteArray();
        
        boolean correct = Arrays.equals(data, res);        
        assertTrue("Incorrect encoding", correct);
    }


}