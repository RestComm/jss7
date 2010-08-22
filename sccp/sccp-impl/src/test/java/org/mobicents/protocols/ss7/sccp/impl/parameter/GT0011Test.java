/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;

/**
 *
 * @author kulikov
 */
public class GT0011Test {

    private byte[] data = new byte[] {0, 0x12, 0x09,0x32,0x26,0x59,0x18};
    
    public GT0011Test() {
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
     * Test of decode method, of class GT0011.
     */
    @Test
    public void testDecode() throws Exception {
        //wrap data with input stream
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        
        //create GT object and read data from stream
        GT0011 gt1 = new GT0011();
        gt1.decode(in);
        
        //check results
        assertEquals(0, gt1.getTranslationType());
        assertEquals(NumberingPlan.ISDN_TELEPHONY, gt1.getNp());
        assertEquals("9023629581", gt1.getDigits());
    }

    /**
     * Test of encode method, of class GT0011.
     */
    @Test
    public void testEncode() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GT0011 gt = new GT0011(0, NumberingPlan.ISDN_TELEPHONY, "9023629581");
        
        gt.encode(bout);
        
        byte[] res = bout.toByteArray();
        
        boolean correct = Arrays.equals(data, res);        
        assertTrue("Incorrect encoding", correct);
    }

}