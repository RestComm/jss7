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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultSMSHandling;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MGCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MTSMSTPDUType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCAMELTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SGSNCAMELSubscriptionInfoTest {

    public byte[] getData() {
        return new byte[] { 48, -126, 1, -66, -96, 108, -96, 58, 48, 56, -128, 1, 2, -127, 1, 2, -126, 4, -111, 34, 34, -8,
                -125, 1, 1, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3,
                42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 4, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11,
                12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33,
                -125, 0, -124, 0, -95, 108, -96, 58, 48, 56, -128, 1, 1, -127, 1, 2, -126, 4, -111, 34, 34, -8, -125, 1, 0,
                -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
                21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 4, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13,
                14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0,
                -124, 0, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42,
                3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -93, 104, -96, 58, 48, 56, -128, 1, 1, -127, 1, 2, -126, 4,
                -111, 34, 34, -8, -125, 1, 0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 4, -94, 39, -96, 32, 48, 10, 6,
                3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33, -92, 13, 48, 11, 10, 1, 1, -96, 6, 10, 1, 0, 10, 1, 2, -91, 62, 48, 6, 4, 1, -125, 4, 1, 2, 2, 1,
                3, -128, 4, -111, 34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -126, 0, -125, 0 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        SGSNCAMELSubscriptionInfoImpl prim = new SGSNCAMELSubscriptionInfoImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        // start gprsCsi
        GPRSCSI gprsCsi = prim.getGprsCsi();
        assertNotNull(gprsCsi.getGPRSCamelTDPDataList());
        assertEquals(gprsCsi.getGPRSCamelTDPDataList().size(), 1);
        GPRSCamelTDPData gprsCamelTDPData = gprsCsi.getGPRSCamelTDPDataList().get(0);

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

        assertEquals(gprsCsi.getCamelCapabilityHandling().intValue(), 4);
        assertNotNull(gprsCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(gprsCsi.getExtensionContainer()));
        assertTrue(gprsCsi.getCsiActive());
        assertTrue(gprsCsi.getNotificationToCSE());
        // end gprsCsi

        // start moSmsCsi
        SMSCSI moSmsCsi = prim.getMoSmsCsi();
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = moSmsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataList);
        assertEquals(smsCamelTdpDataList.size(), 1);
        SMSCAMELTDPData one = smsCamelTdpDataList.get(0);
        assertNotNull(one);
        assertEquals(one.getServiceKey(), 2);
        assertEquals(one.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressString gsmSCFAddressMoSmsCsi = one.getGsmSCFAddress();
        assertTrue(gsmSCFAddressMoSmsCsi.getAddress().equals("22228"));
        assertEquals(gsmSCFAddressMoSmsCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressMoSmsCsi.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(one.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(one.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(one.getExtensionContainer()));

        assertNotNull(moSmsCsi.getExtensionContainer());
        assertTrue(moSmsCsi.getCsiActive());
        assertTrue(moSmsCsi.getNotificationToCSE());
        assertEquals(moSmsCsi.getCamelCapabilityHandling().intValue(), 4);
        // end moSmsCsi

        // start mtSmsCsi
        SMSCSI mtSmsCsi = prim.getMtSmsCsi();
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataListMtSmsCsi = mtSmsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListMtSmsCsi);
        assertEquals(smsCamelTdpDataListMtSmsCsi.size(), 1);
        SMSCAMELTDPData oneSmsCamelTdpDataListMtSmsCsi = smsCamelTdpDataListMtSmsCsi.get(0);
        assertNotNull(oneSmsCamelTdpDataListMtSmsCsi);
        assertEquals(oneSmsCamelTdpDataListMtSmsCsi.getServiceKey(), 2);
        assertEquals(oneSmsCamelTdpDataListMtSmsCsi.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressString gsmSCFAddressmtSmsCsi = one.getGsmSCFAddress();
        assertTrue(gsmSCFAddressmtSmsCsi.getAddress().equals("22228"));
        assertEquals(gsmSCFAddressmtSmsCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressmtSmsCsi.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(one.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(one.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(one.getExtensionContainer()));

        assertNotNull(mtSmsCsi.getExtensionContainer());
        assertFalse(mtSmsCsi.getCsiActive());
        assertFalse(mtSmsCsi.getNotificationToCSE());
        assertEquals(mtSmsCsi.getCamelCapabilityHandling().intValue(), 4);
        // end mtSmsCsi

        // start mtSmsCamelTdpCriteriaList
        ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList = prim.getMtSmsCamelTdpCriteriaList();
        assertNotNull(mtSmsCamelTdpCriteriaList);
        assertEquals(mtSmsCamelTdpCriteriaList.size(), 1);
        MTsmsCAMELTDPCriteria mtSmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaList.get(0);

        ArrayList<MTSMSTPDUType> tPDUTypeCriterion = mtSmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterion);
        assertEquals(tPDUTypeCriterion.size(), 2);
        MTSMSTPDUType oneMTSMSTPDUType = tPDUTypeCriterion.get(0);
        assertNotNull(oneMTSMSTPDUType);
        assertEquals(oneMTSMSTPDUType, MTSMSTPDUType.smsDELIVER);

        MTSMSTPDUType twoMTSMSTPDUType = tPDUTypeCriterion.get(1);
        assertNotNull(twoMTSMSTPDUType);
        assertEquals(twoMTSMSTPDUType, MTSMSTPDUType.smsSTATUSREPORT);
        assertEquals(mtSmsCAMELTDPCriteria.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        // end mtSmsCamelTdpCriteriaList

        // start mgCsi
        MGCSI mgCsi = prim.getMgCsi();
        ArrayList<MMCode> mobilityTriggers = mgCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggers);
        assertEquals(mobilityTriggers.size(), 2);
        MMCode oneMMCode = mobilityTriggers.get(0);
        assertNotNull(oneMMCode);
        assertEquals(oneMMCode.getMMCodeValue(), MMCodeValue.GPRSAttach);
        MMCode twoMMCode = mobilityTriggers.get(1);
        assertNotNull(twoMMCode);
        assertEquals(twoMMCode.getMMCodeValue(), MMCodeValue.IMSIAttach);

        assertEquals(mgCsi.getServiceKey(), 3);

        ISDNAddressString gsmSCFAddressMgCsi = mgCsi.getGsmSCFAddress();
        assertTrue(gsmSCFAddressMgCsi.getAddress().equals("22228"));
        assertEquals(gsmSCFAddressMgCsi.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressMgCsi.getNumberingPlan(), NumberingPlan.ISDN);

        assertNotNull(mgCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mgCsi.getExtensionContainer()));
        assertTrue(mgCsi.getCsiActive());
        assertTrue(mgCsi.getNotificationToCSE());
        // end mgCsi

        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        // start gprsCsi
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

        GPRSCSI gprsCsi = new GPRSCSIImpl(gprsCamelTDPDataList, camelCapabilityHandling, extensionContainer, notificationToCSE,
                csiActive);
        // end gprsCsi

        // start moSmsCsi
        SMSTriggerDetectionPoint smsTriggerDetectionPoint = SMSTriggerDetectionPoint.smsCollectedInfo;
        DefaultSMSHandling defaultSMSHandling = DefaultSMSHandling.continueTransaction;

        ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = new ArrayList<SMSCAMELTDPData>();
        SMSCAMELTDPDataImpl smsCAMELTDPData = new SMSCAMELTDPDataImpl(smsTriggerDetectionPoint, serviceKey, gsmSCFAddress,
                defaultSMSHandling, extensionContainer);
        smsCamelTdpDataList.add(smsCAMELTDPData);

        SMSCSI moSmsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE,
                csiActive);
        // end moSmsCsi

        // start mtSmsCsi
        SMSCSI mtSmsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, false, false);
        // end mtSmsCsi

        // start mtSmsCamelTdpCriteriaList
        ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList = new ArrayList<MTsmsCAMELTDPCriteria>();
        ArrayList<MTSMSTPDUType> tPDUTypeCriterion = new ArrayList<MTSMSTPDUType>();
        tPDUTypeCriterion.add(MTSMSTPDUType.smsDELIVER);
        tPDUTypeCriterion.add(MTSMSTPDUType.smsSTATUSREPORT);
        MTsmsCAMELTDPCriteria mtSmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl(smsTriggerDetectionPoint, tPDUTypeCriterion);
        mtSmsCamelTdpCriteriaList.add(mtSmsCAMELTDPCriteria);
        // end mtSmsCamelTdpCriteriaList

        // start mgCsi
        ArrayList<MMCode> mobilityTriggers = new ArrayList<MMCode>();
        mobilityTriggers.add(new MMCodeImpl(MMCodeValue.GPRSAttach));
        mobilityTriggers.add(new MMCodeImpl(MMCodeValue.IMSIAttach));

        MGCSI mgCsi = new MGCSIImpl(mobilityTriggers, 3, gsmSCFAddress, extensionContainer, true, true);
        // end mgCsi

        SGSNCAMELSubscriptionInfoImpl prim = new SGSNCAMELSubscriptionInfoImpl(gprsCsi, moSmsCsi, extensionContainer, mtSmsCsi,
                mtSmsCamelTdpCriteriaList, mgCsi);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }
}
