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
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribed;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.PDPContextImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class GPRSSubscriptionDataTest {

    public byte[] getData() {
        return new byte[] { 48, -127, -93, -95, 109, 48, 107, 2, 1, 1, -112, 2, 5, 3, -111, 3, 5, 6, 7, -110, 3, 4, 7, 7, -108, 2, 6, 7, -75, 39, -96, 32, 48,
                10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 2, 1,
                7, -127, 2, 6, 5, -126, 2, 1, 8, -125, 2, 2, 6, -124, 1, 2, -123, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12, -122, 2, 6, 5, -121, 3, 4, 6, 5,
                -120, 1, 0, -119, 1, 2, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
                23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12 };
    }

    public byte[] getAPNOIReplacementData() {
        return new byte[] { 48, 12, 17, 17, 119, 22, 62, 34, 12 };
    };

    public byte[] getPDPTypeData() {
        return new byte[] { 5, 3 };
    };

    public byte[] getPDPAddressData() {
        return new byte[] { 5, 6, 7 };
    };

    public byte[] getPDPAddressData2() {
        return new byte[] { 4, 6, 5 };
    };

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
    };

    public byte[] getAPNData() {
        return new byte[] { 6, 7 };
    };

    public byte[] getExtQoSSubscribedData() {
        return new byte[] { 1, 7 };
    };

    public byte[] getExt2QoSSubscribedData() {
        return new byte[] { 1, 8 };
    };

    public byte[] getExt3QoSSubscribedData() {
        return new byte[] { 2, 6 };
    };

    public byte[] getChargingCharacteristicsData() {
        return new byte[] { 6, 5 };
    };

    public byte[] getExtPDPTypeData() {
        return new byte[] { 6, 5 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        GPRSSubscriptionDataImpl prim = new GPRSSubscriptionDataImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(!prim.getCompleteDataListIncluded());
        ArrayList<PDPContext> gprsDataList = prim.getGPRSDataList();
        assertNotNull(gprsDataList);
        assertEquals(gprsDataList.size(), 1);
        PDPContext pdpContext = gprsDataList.get(0);
        assertNotNull(pdpContext);
        APN apn = pdpContext.getAPN();
        assertTrue(Arrays.equals(apn.getData(), this.getAPNData()));
        APNOIReplacement apnoiReplacement = pdpContext.getAPNOIReplacement();
        assertTrue(Arrays.equals(apnoiReplacement.getData(), this.getAPNOIReplacementData()));
        ChargingCharacteristics chargingCharacteristics = pdpContext.getChargingCharacteristics();
        assertTrue(Arrays.equals(chargingCharacteristics.getData(), this.getChargingCharacteristicsData()));

        Ext2QoSSubscribed ext2QoSSubscribed = pdpContext.getExt2QoSSubscribed();
        assertTrue(Arrays.equals(ext2QoSSubscribed.getData(), this.getExt2QoSSubscribedData()));
        Ext3QoSSubscribed ext3QoSSubscribed = pdpContext.getExt3QoSSubscribed();
        assertTrue(Arrays.equals(ext3QoSSubscribed.getData(), this.getExt3QoSSubscribedData()));
        Ext4QoSSubscribed ext4QoSSubscribed = pdpContext.getExt4QoSSubscribed();
        assertEquals(ext4QoSSubscribed.getData(), 2);
        MAPExtensionContainer pdpContextExtensionContainer = pdpContext.getExtensionContainer();
        assertNotNull(pdpContextExtensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdpContextExtensionContainer));
        PDPAddress extpdpAddress = pdpContext.getExtPDPAddress();
        assertTrue(Arrays.equals(extpdpAddress.getData(), this.getPDPAddressData2()));
        ExtPDPType extpdpType = pdpContext.getExtPDPType();
        assertTrue(Arrays.equals(extpdpType.getData(), this.getExtPDPTypeData()));
        ExtQoSSubscribed extQoSSubscribed = pdpContext.getExtQoSSubscribed();
        assertTrue(Arrays.equals(extQoSSubscribed.getData(), this.getExtQoSSubscribedData()));
        assertEquals(pdpContext.getLIPAPermission(), LIPAPermission.lipaConditional);
        PDPAddress pdpAddress = pdpContext.getPDPAddress();
        assertTrue(Arrays.equals(pdpAddress.getData(), this.getPDPAddressData()));
        assertEquals(pdpContext.getPDPContextId(), 1);
        PDPType pdpType = pdpContext.getPDPType();
        assertTrue(Arrays.equals(pdpType.getData(), this.getPDPTypeData()));
        QoSSubscribed qosSubscribed = pdpContext.getQoSSubscribed();
        assertTrue(Arrays.equals(qosSubscribed.getData(), this.getQoSSubscribedData()));
        assertEquals(pdpContext.getSIPTOPermission(), SIPTOPermission.siptoAllowed);

        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        APNOIReplacement apnOiReplacement = prim.getApnOiReplacement();
        assertNotNull(apnOiReplacement);
        assertTrue(Arrays.equals(apnOiReplacement.getData(), this.getAPNOIReplacementData()));
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        int pdpContextId = 1;
        PDPType pdpType = new PDPTypeImpl(this.getPDPTypeData());
        PDPAddress pdpAddress = new PDPAddressImpl(this.getPDPAddressData());
        QoSSubscribed qosSubscribed = new QoSSubscribedImpl(this.getQoSSubscribedData());
        boolean vplmnAddressAllowed = false;
        APN apn = new APNImpl(this.getAPNData());
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ExtQoSSubscribed extQoSSubscribed = new ExtQoSSubscribedImpl(this.getExtQoSSubscribedData());
        ChargingCharacteristics chargingCharacteristics = new ChargingCharacteristicsImpl(this.getChargingCharacteristicsData());
        Ext2QoSSubscribed ext2QoSSubscribed = new Ext2QoSSubscribedImpl(this.getExt2QoSSubscribedData());
        Ext3QoSSubscribed ext3QoSSubscribed = new Ext3QoSSubscribedImpl(this.getExt3QoSSubscribedData());
        Ext4QoSSubscribed ext4QoSSubscribed = new Ext4QoSSubscribedImpl(2);
        APNOIReplacement apnoiReplacement = new APNOIReplacementImpl(this.getAPNOIReplacementData());
        ExtPDPType extpdpType = new ExtPDPTypeImpl(this.getExtPDPTypeData());
        PDPAddress extpdpAddress = new PDPAddressImpl(this.getPDPAddressData2());
        ;
        SIPTOPermission sipToPermission = SIPTOPermission.siptoAllowed;
        LIPAPermission lipaPermission = LIPAPermission.lipaConditional;

        PDPContext pdpContext = new PDPContextImpl(pdpContextId, pdpType, pdpAddress, qosSubscribed, vplmnAddressAllowed, apn,
                extensionContainer, extQoSSubscribed, chargingCharacteristics, ext2QoSSubscribed, ext3QoSSubscribed,
                ext4QoSSubscribed, apnoiReplacement, extpdpType, extpdpAddress, sipToPermission, lipaPermission);
        ArrayList<PDPContext> gprsDataList = new ArrayList<PDPContext>();
        gprsDataList.add(pdpContext);

        APNOIReplacement apnOiReplacement = new APNOIReplacementImpl(this.getAPNOIReplacementData());
        GPRSSubscriptionDataImpl prim = new GPRSSubscriptionDataImpl(false, gprsDataList, extensionContainer, apnOiReplacement);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        byte[] encodedData = asn.toByteArray();
        assertTrue(Arrays.equals(encodedData, this.getData()));
    }
}
