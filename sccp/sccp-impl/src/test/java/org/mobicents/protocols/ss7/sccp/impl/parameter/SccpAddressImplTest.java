/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.sccp.impl.parameter;

import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import static org.junit.Assert.*;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;

/**
 *
 * @author kulikov
 */
public class SccpAddressImplTest {

    private byte[] data = new byte[] {0x12, (byte)0x92, 0x00, 0x11, 0x04, (byte)0x97, 0x20, (byte)0x73, 0x00, (byte)0x92, 0x09};
    
    public SccpAddressImplTest() {
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
     * Test of decode method, of class SccpAddressImpl.
     */
    @Test
    public void testDecode() throws Exception {
        SccpAddressImpl address = new SccpAddressImpl();
        address.decode(data);

        assertEquals(0, address.getSignalingPointCode());
        assertEquals(146, address.getSubsystemNumber());
        assertEquals("79023700299", address.getGlobalTitle().getDigits());
    }

    /**
     * Test of encode method, of class SccpAddressImpl.
     */
    @Test
    public void testEncode() throws Exception {
        AddressFactoryImpl factory = new AddressFactoryImpl();
        GlobalTitle gt = GTFactory.getInstance(0, 
                NumberingPlan.ISDN_TELEPHONY, 
                NatureOfAddress.INTERNATIONAL, 
                "79023700299");
        SccpAddressImpl address = (SccpAddressImpl) factory.getAddress(gt, 146);
        byte[] bin = address.encode();
        assertTrue("Wrong encoding", Arrays.equals(data, bin));
    }

}