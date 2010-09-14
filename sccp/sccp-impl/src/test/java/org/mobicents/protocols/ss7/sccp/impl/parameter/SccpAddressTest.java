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
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author kulikov
 */
public class SccpAddressTest {

    private SccpAddressCodec codec = new SccpAddressCodec();
    private byte[] data = new byte[] {0x12, (byte)0x92, 0x00, 0x11, 0x04, (byte)0x97, 0x20, (byte)0x73, 0x00, (byte)0x92, 0x09};
    
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
     * Test of decode method, of class SccpAddressCodec.
     */
    @Test
    public void testDecode() throws Exception {
        SccpAddress address = codec.decode(data);

        assertEquals(0, address.getSignalingPointCode());
        assertEquals(146, address.getSubsystemNumber());
        assertEquals("79023700299", address.getGlobalTitle().getDigits());
    }

    /**
     * Test of encode method, of class SccpAddressCodec.
     */
    @Test
    public void testEncode() throws Exception {
        GlobalTitle gt = GlobalTitle.getInstance(0, 
                NumberingPlan.ISDN_TELEPHONY, 
                NatureOfAddress.INTERNATIONAL, 
                "79023700299");
        SccpAddress address = new SccpAddress(gt, 146);
        byte[] bin = codec.encode(address);
        assertTrue("Wrong encoding", Arrays.equals(data, bin));
    }

}