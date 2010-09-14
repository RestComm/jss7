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
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.parameter.GT0100;

/**
 *
 * @author kulikov
 */
public class GT0100Test {

    private byte[] data = new byte[] {0, 0x12, 0x03, 0x09,0x32,0x26,0x59,0x18};
    private GT0100Codec codec = new GT0100Codec();
    
    public GT0100Test() {
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
        GT0100 gt1 = (GT0100) codec.decode(in);
        
        //check results
        assertEquals(0, gt1.getTranslationType());
        assertEquals(NumberingPlan.ISDN_TELEPHONY, gt1.getNumberingPlan());
        assertEquals("9023629581", gt1.getDigits());
    }

    /**
     * Test of encode method, of class GT0011.
     */
    @Test
    public void testEncode() throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        GT0100 gt = new GT0100(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.NATIONAL, "9023629581");
        
        codec.encode(gt, bout);
        
        byte[] res = bout.toByteArray();
        
        boolean correct = Arrays.equals(data, res);        
        assertTrue("Incorrect encoding", correct);
    }

}