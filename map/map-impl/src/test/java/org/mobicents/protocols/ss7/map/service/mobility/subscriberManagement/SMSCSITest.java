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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultSMSHandling;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCAMELTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SMSCSITest {

    public byte[] getData() {
        return new byte[] { 48, 104, -96, 58, 48, 56, -128, 1, 1, -127, 1, 4, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 39,
                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
                24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 8, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48,
                5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        SMSCSIImpl prim = new SMSCSIImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = prim.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataList);
        assertEquals(smsCamelTdpDataList.size(), 1);
        SMSCAMELTDPData one = smsCamelTdpDataList.get(0);
        assertNotNull(one);
        assertEquals(one.getServiceKey(), 4);
        assertEquals(one.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressString gsmSCFAddress = one.getGsmSCFAddress();
        assertTrue(gsmSCFAddress.getAddress().equals("22235"));
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(one.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(one.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(one.getExtensionContainer()));

        assertNotNull(prim.getExtensionContainer());
        assertTrue(!prim.getCsiActive());
        assertTrue(!prim.getNotificationToCSE());
        assertEquals(prim.getCamelCapabilityHandling().intValue(), 8);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        SMSTriggerDetectionPoint smsTriggerDetectionPoint = SMSTriggerDetectionPoint.smsCollectedInfo;
        long serviceKey = 4;
        ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");
        ;
        DefaultSMSHandling defaultSMSHandling = DefaultSMSHandling.continueTransaction;

        ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = new ArrayList<SMSCAMELTDPData>();
        SMSCAMELTDPDataImpl smsCAMELTDPData = new SMSCAMELTDPDataImpl(smsTriggerDetectionPoint, serviceKey, gsmSCFAddress,
                defaultSMSHandling, extensionContainer);
        smsCamelTdpDataList.add(smsCAMELTDPData);

        Integer camelCapabilityHandling = new Integer(8);
        boolean notificationToCSE = false;
        boolean csiActive = false;

        SMSCSIImpl prim = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE,
                csiActive);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }

}
