/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.impl.router;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;

/**
 *
 * @author kulikov
 */
public class AddressInformationTest {
    private final static String ADDRESS_INFORMATION_1 = " #ISDN_MOBILE#NATIONAL#9023629581# ";
    
    public AddressInformationTest() {
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
     * Test of getInstance method, of class AddressInformation.
     */
    @Test
    public void testGetInstance() {
        AddressInformation ai = AddressInformation.getInstance(ADDRESS_INFORMATION_1);
        assertEquals(-1, ai.getTranslationType());
        assertEquals(NumberingPlan.ISDN_MOBILE, ai.getNumberingPlan());
        assertEquals(NatureOfAddress.NATIONAL, ai.getNatureOfAddress());
        assertEquals("9023629581", ai.getDigits());
        assertEquals(-1, ai.getSubsystem());
    }

    /**
     * Test of getTranslationType method, of class AddressInformation.
     */
    @Test
    public void testToString() {
        AddressInformation ai = new AddressInformation(-1, NumberingPlan.ISDN_MOBILE, 
                NatureOfAddress.NATIONAL, "9023629581", -1);
        assertEquals(ADDRESS_INFORMATION_1, ai.toString());
    }


}