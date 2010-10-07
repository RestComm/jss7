/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.parameter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mobicents.protocols.ss7.indicator.AddressIndicator;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;

/**
 *
 * @author kulikov
 */
public class SccpAddressTest {

    public SccpAddressTest() {
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
     * Test of getAddressIndicator method, of class SccpAddress.
     */
    @Test
    public void testEquals() {
        GlobalTitle gt = GlobalTitle.getInstance(NatureOfAddress.NATIONAL, "123");
        SccpAddress a1 = new SccpAddress(gt, 0);
        SccpAddress a2 = new SccpAddress(gt, 0);
        assertEquals(a1, a2);
    }


}