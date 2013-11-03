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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultGPRSHandling;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class GPRSCSITest {

    public byte[] getData() {
        return new byte[] { 48, 108, -96, 58, 48, 56, -128, 1, 2, -127, 1, 2, -126, 4, -111, 34, 34, -8, -125, 1, 1, -92, 39,
                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
                24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 4, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48,
                5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        GPRSCSIImpl prim = new GPRSCSIImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertNotNull(prim.getGPRSCamelTDPDataList());
        assertEquals(prim.getGPRSCamelTDPDataList().size(), 1);
        GPRSCamelTDPData gprsCamelTDPData = prim.getGPRSCamelTDPDataList().get(0);

        MAPExtensionContainer extensionContainergprsCamelTDPData = gprsCamelTDPData.getExtensionContainer();
        ISDNAddressString gsmSCFAddress = gprsCamelTDPData.getGsmSCFAddress();
        assertTrue(gsmSCFAddress.getAddress().equals("22228"));
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);

        assertEquals(gprsCamelTDPData.getDefaultSessionHandling(), DefaultGPRSHandling.releaseTransaction);
        assertEquals(gprsCamelTDPData.getGPRSTriggerDetectionPoint(), GPRSTriggerDetectionPoint.attachChangeOfPosition);
        assertEquals(gprsCamelTDPData.getServiceKey(), 2);

        assertNotNull(extensionContainergprsCamelTDPData);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainergprsCamelTDPData));

        assertEquals(prim.getCamelCapabilityHandling().intValue(), 4);
        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        assertTrue(prim.getCsiActive());
        assertTrue(prim.getNotificationToCSE());

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        GPRSTriggerDetectionPoint gprsTriggerDetectionPoint = GPRSTriggerDetectionPoint.attachChangeOfPosition;
        long serviceKey = 2;
        ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        DefaultGPRSHandling defaultSessionHandling = DefaultGPRSHandling.releaseTransaction;

        GPRSCamelTDPDataImpl gprsCamelTDPData = new GPRSCamelTDPDataImpl(gprsTriggerDetectionPoint, serviceKey, gsmSCFAddress,
                defaultSessionHandling, extensionContainer);

        ArrayList<GPRSCamelTDPData> gprsCamelTDPDataList = new ArrayList<GPRSCamelTDPData>();
        gprsCamelTDPDataList.add(gprsCamelTDPData);
        Integer camelCapabilityHandling = new Integer(4);
        boolean notificationToCSE = true;
        boolean csiActive = true;

        GPRSCSIImpl prim = new GPRSCSIImpl(gprsCamelTDPDataList, camelCapabilityHandling, extensionContainer,
                notificationToCSE, csiActive);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }

}
