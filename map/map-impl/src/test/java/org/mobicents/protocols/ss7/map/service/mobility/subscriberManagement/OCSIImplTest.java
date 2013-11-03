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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class OCSIImplTest {

    private byte[] getEncodedData() {
        return new byte[] { -96, 23, 48, 18, 48, 16, 10, 1, 4, 2, 1, 3, -128, 5, -111, 17, 34, 51, -13, -127, 1, 1, -128, 1, 2 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { -96, 68, 48, 18, 48, 16, 10, 1, 4, 2, 1, 3, -128, 5, -111, 17, 34, 51, -13, -127, 1, 1, 48, 39,
                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
                24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 4, -127, 0, -126, 0 };
    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberManagement" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        OCSIImpl ind = new OCSIImpl();
        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        ind.decodeAll(asn);

        ArrayList<OBcsmCamelTDPData> lst = ind.getOBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        OBcsmCamelTDPData cd = lst.get(0);
        assertEquals(cd.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(cd.getServiceKey(), 3);
        assertEquals(cd.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(cd.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(cd.getGsmSCFAddress().getAddress().equals("1122333"));
        assertEquals(cd.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(cd.getExtensionContainer());

        assertNull(ind.getExtensionContainer());
        assertEquals((int) ind.getCamelCapabilityHandling(), 2);
        assertFalse(ind.getNotificationToCSE());
        assertFalse(ind.getCsiActive());

        rawData = getEncodedDataFull();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new OCSIImpl();
        assertEquals(tag, 0);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        ind.decodeAll(asn);

        lst = ind.getOBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        cd = lst.get(0);
        assertEquals(cd.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(cd.getServiceKey(), 3);
        assertEquals(cd.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(cd.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(cd.getGsmSCFAddress().getAddress().equals("1122333"));
        assertEquals(cd.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(cd.getExtensionContainer());

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
        assertEquals((int) ind.getCamelCapabilityHandling(), 4);
        assertTrue(ind.getNotificationToCSE());
        assertTrue(ind.getCsiActive());

    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberManagement" })
    public void testEncode() throws Exception {

        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "1122333");
        OBcsmCamelTDPDataImpl cind = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.routeSelectFailure, 3, gsmSCFAddress,
                DefaultCallHandling.releaseCall, null);
        ArrayList<OBcsmCamelTDPData> lst = new ArrayList<OBcsmCamelTDPData>();
        lst.add(cind);
        OCSIImpl ind = new OCSIImpl(lst, null, 2, false, false);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        ind = new OCSIImpl(lst, MAPExtensionContainerTest.GetTestExtensionContainer(), 4, true, true);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 0);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));

    }

}
