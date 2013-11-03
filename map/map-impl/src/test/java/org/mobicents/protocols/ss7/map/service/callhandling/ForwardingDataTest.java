/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.map.service.callhandling;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingOptions;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ForwardingOptionsImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/*
 *
 * @author cristian veliscu
 *
 */
public class ForwardingDataTest {
    Logger logger = Logger.getLogger(ForwardingDataTest.class);

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {
        byte[] data = new byte[] { 48, 12, (byte) 133, 7, -111, -105, 114, 99, 80, 24, -7, (byte) 134, 1, 36 };

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        ForwardingDataImpl forwardingData = new ForwardingDataImpl();
        forwardingData.decodeAll(asn);

        ForwardingOptions forwardingOptions = forwardingData.getForwardingOptions();
        ISDNAddressString isdnAdd = forwardingData.getForwardedToNumber();

        assertNotNull(isdnAdd);
        assertEquals(isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(isdnAdd.getAddress(), "79273605819");
        assertNotNull(forwardingOptions);
        assertTrue(!forwardingOptions.isNotificationToForwardingParty());
        assertTrue(!forwardingOptions.isRedirectingPresentation());
        assertTrue(forwardingOptions.isNotificationToCallingParty());
        assertTrue(forwardingOptions.getForwardingReason() == ForwardingReason.busy);
    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {
        byte[] data = new byte[] { 48, 12, (byte) 133, 7, -111, -105, 114, 99, 80, 24, -7, (byte) 134, 1, 36 };

        ISDNAddressString isdnAdd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        ForwardingOptionsImpl forwardingOptions = new ForwardingOptionsImpl(false, false, true, ForwardingReason.busy);
        ForwardingDataImpl forwardingData = new ForwardingDataImpl(isdnAdd, null, forwardingOptions, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        forwardingData.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.serialize", "service.callhandling" })
    public void testSerialization() throws Exception {

    }
}
