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
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientInternalID;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExternalClient;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GMLCRestriction;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.NotificationToMSUser;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ServiceType;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.lsm.LCSClientExternalIDImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class LCSPrivacyClassTest {

    public byte[] getData() {
        return new byte[] { 48, -126, 1, 56, 4, 1, 35, 4, 1, 10, -128, 1, 3, -95, 98, 48, 96, 48, 47, -128, 4, -111, 34, 34,
                -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3,
                5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3,
                4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32,
                33, -94, 6, 2, 1, 0, 2, 1, 1, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -92, 98, 48, 96, 48, 47, -128, 4, -111,
                34, 34, -8, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3,
                42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3,
                42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33, -91, 52, 48, 50, 2, 1, 1, -128, 1, 0, -127, 1, 3, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12,
                13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        LCSPrivacyClassImpl prim = new LCSPrivacyClassImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(prim.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.cellBroadcast);
        ExtSSStatus ssStatus = prim.getSsStatus();
        assertFalse(ssStatus.getBitA());
        assertFalse(ssStatus.getBitP());
        assertTrue(ssStatus.getBitQ());
        assertTrue(ssStatus.getBitR());

        assertEquals(prim.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);

        ArrayList<ExternalClient> externalClientList = prim.getExternalClientList();
        assertNotNull(externalClientList);
        assertEquals(externalClientList.size(), 1);
        ExternalClient externalClient = externalClientList.get(0);
        MAPExtensionContainer extensionContainerExternalClient = externalClient.getExtensionContainer();
        LCSClientExternalID clientIdentity = externalClient.getClientIdentity();
        ISDNAddressString externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(extensionContainerExternalClient);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerExternalClient));

        ArrayList<LCSClientInternalID> plmnClientList = prim.getPLMNClientList();
        assertNotNull(plmnClientList);
        assertEquals(plmnClientList.size(), 2);
        assertEquals(plmnClientList.get(0), LCSClientInternalID.broadcastService);
        assertEquals(plmnClientList.get(1), LCSClientInternalID.oandMHPLMN);

        ArrayList<ExternalClient> extExternalClientList = prim.getExtExternalClientList();
        assertNotNull(extExternalClientList);
        assertEquals(extExternalClientList.size(), 1);
        externalClient = extExternalClientList.get(0);
        extensionContainerExternalClient = externalClient.getExtensionContainer();
        clientIdentity = externalClient.getClientIdentity();
        externalAddress = clientIdentity.getExternalAddress();
        assertTrue(externalAddress.getAddress().equals("22228"));
        assertEquals(externalAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(externalAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(clientIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(clientIdentity.getExtensionContainer()));
        assertEquals(externalClient.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(externalClient.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(extensionContainerExternalClient);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerExternalClient));

        ArrayList<ServiceType> serviceTypeList = prim.getServiceTypeList();
        assertNotNull(serviceTypeList);
        assertEquals(serviceTypeList.size(), 1);
        ServiceType serviceType = serviceTypeList.get(0);
        MAPExtensionContainer extensionContainerServiceType = serviceType.getExtensionContainer();
        assertEquals(serviceType.getServiceTypeIdentity(), 1);
        assertEquals(serviceType.getGMLCRestriction(), GMLCRestriction.gmlcList);
        assertEquals(serviceType.getNotificationToMSUser(), NotificationToMSUser.locationNotAllowed);
        assertNotNull(extensionContainerServiceType);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainerServiceType));

        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.cellBroadcast);
        ExtSSStatus ssStatus = new ExtSSStatusImpl(true, false, true, false);
        NotificationToMSUser notificationToMSUser = NotificationToMSUser.locationNotAllowed;
        ArrayList<ExternalClient> externalClientList = new ArrayList<ExternalClient>();

        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ISDNAddressString externalAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        LCSClientExternalID clientIdentity = new LCSClientExternalIDImpl(externalAddress, extensionContainer);
        GMLCRestriction gmlcRestriction = GMLCRestriction.gmlcList;
        ExternalClient externalClient = new ExternalClientImpl(clientIdentity, gmlcRestriction, notificationToMSUser,
                extensionContainer);
        externalClientList.add(externalClient);

        ArrayList<LCSClientInternalID> plmnClientList = new ArrayList<LCSClientInternalID>();
        LCSClientInternalID lcsClientInternalIdOne = LCSClientInternalID.broadcastService;
        LCSClientInternalID lcsClientInternalIdTwo = LCSClientInternalID.oandMHPLMN;
        plmnClientList.add(lcsClientInternalIdOne);
        plmnClientList.add(lcsClientInternalIdTwo);

        ArrayList<ExternalClient> extExternalClientList = new ArrayList<ExternalClient>();
        extExternalClientList.add(externalClient);

        ArrayList<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
        int serviceTypeIdentity = 1;
        ServiceType serviceType = new ServiceTypeImpl(serviceTypeIdentity, gmlcRestriction, notificationToMSUser,
                extensionContainer);
        serviceTypeList.add(serviceType);

        LCSPrivacyClassImpl prim = new LCSPrivacyClassImpl(ssCode, ssStatus, notificationToMSUser, externalClientList,
                plmnClientList, extensionContainer, extExternalClientList, serviceTypeList);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }
}
