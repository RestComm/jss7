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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.isup.CalledPartyNumberCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.BothwayThroughConnectionInd;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ConnectToResourceTest {

    public byte[] getData1() {
        return new byte[] { 48, 16, (byte) 128, 5, (byte) 131, (byte) 160, (byte) 137, 103, 5, (byte) 167, 3, (byte) 130, 1, 0,
                (byte) 159, 50, 1, 21 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 31, (byte) 131, 0, (byte) 164, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1,
                (byte) 129, 1, (byte) 255, (byte) 167, 3, (byte) 130, 1, 0, (byte) 159, 50, 1, 21 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        ConnectToResourceRequestImpl elem = new ConnectToResourceRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getResourceAddress_IPRoutingAddress().getCalledPartyNumber().getNatureOfAddressIndicator(), 3);
        assertTrue(elem.getResourceAddress_IPRoutingAddress().getCalledPartyNumber().getAddress().endsWith("98765"));
        assertEquals(elem.getResourceAddress_IPRoutingAddress().getCalledPartyNumber().getNumberingPlanIndicator(), 2);
        assertEquals(elem.getResourceAddress_IPRoutingAddress().getCalledPartyNumber().getInternalNetworkNumberIndicator(), 1);
        assertFalse(elem.getResourceAddress_Null());
        assertNull(elem.getExtensions());
        assertEquals(elem.getServiceInteractionIndicatorsTwo().getBothwayThroughConnectionInd(),
                BothwayThroughConnectionInd.bothwayPathRequired);
        assertEquals((int) elem.getCallSegmentID(), 21);

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new ConnectToResourceRequestImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertNull(elem.getResourceAddress_IPRoutingAddress());
        assertTrue(elem.getResourceAddress_Null());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertEquals(elem.getServiceInteractionIndicatorsTwo().getBothwayThroughConnectionInd(),
                BothwayThroughConnectionInd.bothwayPathRequired);
        assertEquals((int) elem.getCallSegmentID(), 21);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(3, "98765", 2, 1);
        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator
        CalledPartyNumberCapImpl resourceAddress_IPRoutingAddress = new CalledPartyNumberCapImpl(calledPartyNumber);
        ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl(null,
                null, BothwayThroughConnectionInd.bothwayPathRequired, null, false, null, null, null);

        ConnectToResourceRequestImpl elem = new ConnectToResourceRequestImpl(resourceAddress_IPRoutingAddress, false, null,
                serviceInteractionIndicatorsTwo, 21);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        elem = new ConnectToResourceRequestImpl(null, true, CAPExtensionsTest.createTestCAPExtensions(),
                serviceInteractionIndicatorsTwo, 21);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        // CalledPartyNumberCap resourceAddress_IPRoutingAddress, boolean resourceAddress_Null, CAPExtensions extensions,
        // ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID

    }
}
