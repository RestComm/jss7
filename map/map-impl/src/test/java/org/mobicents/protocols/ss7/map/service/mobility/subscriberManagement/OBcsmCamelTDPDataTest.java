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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class OBcsmCamelTDPDataTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 17, 10, 1, 2, 2, 1, 3, -128, 6, -111, 51, 35, 34, 17, -15, -127, 1, 1 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 58, 10, 1, 2, 2, 1, 3, -128, 6, -111, 51, 35, 34, 17, -15, -127, 1, 1, -94, 39, -96, 32, 48,
                10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26,
                -95, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberManagement" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        OBcsmCamelTDPDataImpl ind = new OBcsmCamelTDPDataImpl();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ind.decodeAll(asn);

        assertEquals(ind.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertEquals(ind.getServiceKey(), 3);
        assertEquals(ind.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(ind.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(ind.getGsmSCFAddress().getAddress().equals("333222111"));
        assertEquals(ind.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(ind.getExtensionContainer());

        rawData = getEncodedDataFull();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new OBcsmCamelTDPDataImpl();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ind.decodeAll(asn);

        assertEquals(ind.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertEquals(ind.getServiceKey(), 3);
        assertEquals(ind.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(ind.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(ind.getGsmSCFAddress().getAddress().equals("333222111"));
        assertEquals(ind.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberManagement" })
    public void testEncode() throws Exception {

        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "333222111");
        OBcsmCamelTDPDataImpl ind = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.collectedInfo, 3, gsmSCFAddress,
                DefaultCallHandling.releaseCall, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        ind = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.collectedInfo, 3, gsmSCFAddress,
                DefaultCallHandling.releaseCall, MAPExtensionContainerTest.GetTestExtensionContainer());

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));

    }

}
