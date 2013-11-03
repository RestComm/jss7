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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AMBR;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.AllocationRetentionPriority;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ChargingCharacteristics;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSQoSSubscribed;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.FQDN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWAllocationType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNTypeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSClassIdentifier;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificAPNInfo;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class APNConfigurationTest {

    public byte[] getData() {
        return new byte[] { 48, -126, 1, -99, -128, 1, 1, -127, 1, 1, -126, 3, 5, 6, 7, -125, 2, 6, 7, -92, 96, -128, 1, 1,
                -95, 50, -128, 1, 1, -127, 1, -1, -126, 1, -1, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 39, -96, 32,
                48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33, -91, 63, -128, 3, 5, 6, 7, -127, 3, 5, 6, 7, -126, 10, 4, 1, 6, 8, 3, 2, 5, 6, 1, 7,
                -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
                21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -122, 1, 1, -121, 0, -120, 2, 6, 5, -87, 47, -128, 1, 2, -127, 1,
                4, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
                21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -86, 112, 48, 110, -128, 2, 6, 7, -95, 63, -128, 3, 5, 6, 7, -127,
                3, 5, 6, 7, -126, 10, 4, 1, 6, 8, 3, 2, 5, 6, 1, 7, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14,
                15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 39, -96,
                32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
                25, 26, -95, 3, 31, 32, 33, -85, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -116, 3, 5, 6, 7, -115, 9, 48, 12, 17,
                17, 119, 22, 62, 34, 12, -114, 1, 0, -113, 1, 2 };
    };

    public byte[] getPDPAddressData() {
        return new byte[] { 5, 6, 7 };
    };

    public byte[] getAPNData() {
        return new byte[] { 6, 7 };
    };

    public byte[] getFQDNData() {
        return new byte[] { 4, 1, 6, 8, 3, 2, 5, 6, 1, 7 };
    };

    public byte[] getChargingCharacteristicsData() {
        return new byte[] { 6, 5 };
    };

    public byte[] getAPNOIReplacementData() {
        return new byte[] { 48, 12, 17, 17, 119, 22, 62, 34, 12 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        APNConfigurationImpl prim = new APNConfigurationImpl();

        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(prim.getContextId(), 1);
        assertEquals(prim.getPDNType().getPDNTypeValue(), PDNTypeValue.IPv4);
        PDPAddress servedPartyIPIPv4Address = prim.getServedPartyIPIPv4Address();
        assertNotNull(servedPartyIPIPv4Address);
        assertTrue(Arrays.equals(this.getPDPAddressData(), servedPartyIPIPv4Address.getData()));
        assertTrue(Arrays.equals(prim.getApn().getData(), this.getAPNData()));

        EPSQoSSubscribed ePSQoSSubscribed = prim.getEPSQoSSubscribed();
        AllocationRetentionPriority allocationRetentionPriority = ePSQoSSubscribed.getAllocationRetentionPriority();
        MAPExtensionContainer extensionContainerePSQoSSubscribed = ePSQoSSubscribed.getExtensionContainer();
        assertEquals(allocationRetentionPriority.getPriorityLevel(), 1);
        assertTrue(allocationRetentionPriority.getPreEmptionCapability());
        assertTrue(allocationRetentionPriority.getPreEmptionVulnerability());
        assertNotNull(allocationRetentionPriority.getExtensionContainer());
        ;
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(allocationRetentionPriority.getExtensionContainer()));
        assertNotNull(extensionContainerePSQoSSubscribed);
        assertEquals(ePSQoSSubscribed.getQoSClassIdentifier(), QoSClassIdentifier.QCI_1);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerePSQoSSubscribed));

        PDNGWIdentity pdnGWIdentity = prim.getPdnGwIdentity();
        PDPAddress pdnGwIpv4Address = pdnGWIdentity.getPdnGwIpv4Address();
        assertNotNull(pdnGwIpv4Address);
        assertTrue(Arrays.equals(this.getPDPAddressData(), pdnGwIpv4Address.getData()));
        PDPAddress pdnGwIpv6Address = pdnGWIdentity.getPdnGwIpv6Address();
        assertNotNull(pdnGwIpv6Address);
        assertTrue(Arrays.equals(this.getPDPAddressData(), pdnGwIpv6Address.getData()));
        FQDN pdnGwName = pdnGWIdentity.getPdnGwName();
        assertNotNull(pdnGwName);
        assertTrue(Arrays.equals(this.getFQDNData(), pdnGwName.getData()));
        assertNotNull(pdnGWIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdnGWIdentity.getExtensionContainer()));

        assertEquals(prim.getPdnGwAllocationType(), PDNGWAllocationType._dynamic);
        assertTrue(prim.getVplmnAddressAllowed());
        assertTrue(Arrays.equals(this.getChargingCharacteristicsData(), prim.getChargingCharacteristics().getData()));

        AMBR ambr = prim.getAmbr();
        MAPExtensionContainer extensionContainerambr = ambr.getExtensionContainer();
        assertEquals(ambr.getMaxRequestedBandwidthDL(), 4);
        assertEquals(ambr.getMaxRequestedBandwidthUL(), 2);
        assertNotNull(extensionContainerambr);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerambr));

        ArrayList<SpecificAPNInfo> specificAPNInfoList = prim.getSpecificAPNInfoList();
        assertNotNull(specificAPNInfoList);
        assertEquals(specificAPNInfoList.size(), 1);
        SpecificAPNInfo specificAPNInfo = specificAPNInfoList.get(0);

        PDNGWIdentity pdnGWIdentitySpecificAPNInfo = specificAPNInfo.getPdnGwIdentity();
        PDPAddress pdnGwIpv4AddressSpecificAPNInfo = pdnGWIdentitySpecificAPNInfo.getPdnGwIpv4Address();
        assertNotNull(pdnGwIpv4AddressSpecificAPNInfo);
        assertTrue(Arrays.equals(this.getPDPAddressData(), pdnGwIpv4AddressSpecificAPNInfo.getData()));
        PDPAddress pdnGwIpv6AddressSpecificAPNInfo = pdnGWIdentitySpecificAPNInfo.getPdnGwIpv6Address();
        assertNotNull(pdnGwIpv6AddressSpecificAPNInfo);
        assertTrue(Arrays.equals(this.getPDPAddressData(), pdnGwIpv6AddressSpecificAPNInfo.getData()));
        FQDN pdnGwNameSpecificAPNInfo = pdnGWIdentitySpecificAPNInfo.getPdnGwName();
        assertNotNull(pdnGwNameSpecificAPNInfo);
        assertTrue(Arrays.equals(this.getFQDNData(), pdnGwNameSpecificAPNInfo.getData()));
        assertNotNull(pdnGWIdentitySpecificAPNInfo.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdnGWIdentitySpecificAPNInfo.getExtensionContainer()));
        MAPExtensionContainer extensionContainerspecificAPNInfo = specificAPNInfo.getExtensionContainer();
        assertNotNull(extensionContainerspecificAPNInfo);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerspecificAPNInfo));

        assertTrue(Arrays.equals(specificAPNInfo.getAPN().getData(), this.getAPNData()));

        PDPAddress servedPartyIPIPv6Address = prim.getServedPartyIPIPv6Address();
        assertNotNull(servedPartyIPIPv6Address);
        assertTrue(Arrays.equals(this.getPDPAddressData(), servedPartyIPIPv6Address.getData()));
        assertTrue(Arrays.equals(this.getAPNOIReplacementData(), prim.getApnOiReplacement().getData()));
        assertEquals(prim.getSiptoPermission(), SIPTOPermission.siptoAllowed);
        assertEquals(prim.getLipaPermission(), LIPAPermission.lipaConditional);
        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        int contextId = 1;
        PDNType pDNType = new PDNTypeImpl(PDNTypeValue.IPv4);
        PDPAddress servedPartyIPIPv4Address = new PDPAddressImpl(this.getPDPAddressData());
        APN apn = new APNImpl(this.getAPNData());

        QoSClassIdentifier qoSClassIdentifier = QoSClassIdentifier.QCI_1;
        AllocationRetentionPriority allocationRetentionPriority = new AllocationRetentionPriorityImpl(1, Boolean.TRUE,
                Boolean.TRUE, extensionContainer);
        EPSQoSSubscribed ePSQoSSubscribed = new EPSQoSSubscribedImpl(qoSClassIdentifier, allocationRetentionPriority,
                extensionContainer);

        PDPAddress pdnGwIpv4Address = new PDPAddressImpl(this.getPDPAddressData());
        PDPAddress pdnGwIpv6Address = new PDPAddressImpl(this.getPDPAddressData());
        FQDN pdnGwName = new FQDNImpl(this.getFQDNData());
        PDNGWIdentity pdnGwIdentity = new PDNGWIdentityImpl(pdnGwIpv4Address, pdnGwIpv6Address, pdnGwName, extensionContainer);

        PDNGWAllocationType pdnGwAllocationType = PDNGWAllocationType._dynamic;
        boolean vplmnAddressAllowed = true;
        ChargingCharacteristics chargingCharacteristics = new ChargingCharacteristicsImpl(this.getChargingCharacteristicsData());
        AMBR ambr = new AMBRImpl(2, 4, extensionContainer);

        SpecificAPNInfo specificAPNInfo = new SpecificAPNInfoImpl(apn, pdnGwIdentity, extensionContainer);
        ArrayList<SpecificAPNInfo> specificAPNInfoList = new ArrayList<SpecificAPNInfo>();
        specificAPNInfoList.add(specificAPNInfo);

        PDPAddress servedPartyIPIPv6Address = new PDPAddressImpl(this.getPDPAddressData());
        APNOIReplacement apnOiReplacement = new APNOIReplacementImpl(this.getAPNOIReplacementData());
        SIPTOPermission siptoPermission = SIPTOPermission.siptoAllowed;
        LIPAPermission lipaPermission = LIPAPermission.lipaConditional;

        APNConfigurationImpl prim = new APNConfigurationImpl(contextId, pDNType, servedPartyIPIPv4Address, apn,
                ePSQoSSubscribed, pdnGwIdentity, pdnGwAllocationType, vplmnAddressAllowed, chargingCharacteristics, ambr,
                specificAPNInfoList, extensionContainer, servedPartyIPIPv6Address, apnOiReplacement, siptoPermission,
                lipaPermission);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }

}
