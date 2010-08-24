/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;

/**
 *
 * @author kulikov
 */
public class GTFactoryTest {

    private byte[] data1 = new byte[] {3,0x09,0x32,0x26,0x59,0x18};
    
    public GTFactoryTest() {
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
     * Test of getInstance method, of class GTFactory.
     */
    @Test
    public void testGetInstance0001() {
        GT0001 gt = (GT0001) GTFactory.getInstance(NatureOfAddress.NATIONAL, "9023629581");
        
        assertEquals(NatureOfAddress.NATIONAL, gt.getNoA());
        assertEquals("9023629581", gt.getDigits());
    }

    /**
     * Test of getInstance method, of class GTFactory.
     */
    @Test
    public void testGetInstance0010() {
        GT0010 gt = (GT0010) GTFactory.getInstance(0, "9023629581");
        assertEquals(0, gt.getTranslationType());
        assertEquals("9023629581", gt.getDigits());
    }

    /**
     * Test of getInstance method, of class GTFactory.
     */
    @Test
    public void testGetInstance0011() {
        GT0011 gt = (GT0011) GTFactory.getInstance(0, NumberingPlan.ISDN_TELEPHONY, "9023629581");
        assertEquals(0, gt.getTranslationType());
        assertEquals(NumberingPlan.ISDN_TELEPHONY, gt.getNp());
        assertEquals("9023629581", gt.getDigits());
    }

    /**
     * Test of getInstance method, of class GTFactory.
     */
    @Test
    public void testGetInstance0100() {
        GT0100 gt = (GT0100) GTFactory.getInstance(0, NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.NATIONAL, "9023629581");
        assertEquals(0, gt.getTranslationType());
        //assertEquals(NumberingPlan.ISDN_TELEPHONY, gt.getNp());
        assertEquals("9023629581", gt.getDigits());
    }

}