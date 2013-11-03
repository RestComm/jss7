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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.*;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.APNOIReplacementImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.PDPAddressImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.PDPTypeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.QoSSubscribedImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class PDPContextTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 102, 2, 1, 15, -112, 2, 11, 12, -111, 1, 21, -110, 3, 91, 92, 93, -109, 0, -108, 2, 22, 23, -75, 39, -96, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 15, -127, 2, 45,
                46, -126, 1, 52, -125, 1, 55, -124, 1, 91, -123, 9, 81, 92, 83, 84, 85, 86, 87, 88, 89, -122, 2, 58, 59, -121, 1, 60, -120, 1, 1, -119, 1, 1 };
    }

    private byte[] getEncodedPDPType() {
        return new byte[] { 11, 12 };
    }

    private byte[] getEncodedPDPAddress() {
        return new byte[] { 21 };
    }

    private byte[] getEncodedQosSubscribed() {
        return new byte[] { 91, 92, 93 };
    }

    private byte[] getEncodedExtQosSubscribed() {
        return new byte[] { 15 };
    }

    private byte[] getEncodedApn() {
        return new byte[] { 22, 23 };
    }

    private byte[] getEncodedchargingCharacteristics() {
        return new byte[] { 45, 46 };
    }

    private byte[] getEncodedqos2Subscribed() {
        return new byte[] { 52 };
    }

    private byte[] getEncodedqos3Subscribed() {
        return new byte[] { 55 };
    }

    private byte[] getAPNOIReplacement() {
        return new byte[] { 81, 92, 83, 84, 85, 86, 87, 88, 89 };
    }

    private byte[] getEncodedExtPDPType() {
        return new byte[] { 58, 59 };
    }

    private byte[] getEncodedExtPdpAddress() {
        return new byte[] { 60 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        PDPContextImpl impl = new PDPContextImpl();
        impl.decodeAll(asn);
        assertEquals(tag, Tag.SEQUENCE);

        assertEquals((int) impl.getPDPContextId(), 15);
        assertTrue(Arrays.equals(impl.getPDPType().getData(), this.getEncodedPDPType()));
        assertTrue(Arrays.equals(impl.getPDPAddress().getData(), this.getEncodedPDPAddress()));
        assertTrue(impl.isVPLMNAddressAllowed());
        assertTrue(Arrays.equals(impl.getAPN().getData(), this.getEncodedApn()));
        assertTrue(Arrays.equals(impl.getQoSSubscribed().getData(), this.getEncodedQosSubscribed()));
        assertTrue(Arrays.equals(impl.getExtQoSSubscribed().getData(), this.getEncodedExtQosSubscribed()));
        assertTrue(Arrays.equals(impl.getChargingCharacteristics().getData(), this.getEncodedchargingCharacteristics()));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(impl.getExtensionContainer()));
        assertTrue(Arrays.equals(impl.getExt2QoSSubscribed().getData(), this.getEncodedqos2Subscribed()));
        assertTrue(Arrays.equals(impl.getExt3QoSSubscribed().getData(), this.getEncodedqos3Subscribed()));
        assertEquals(impl.getExt4QoSSubscribed().getData(), 91);
        assertTrue(Arrays.equals(impl.getExtPDPType().getData(), this.getEncodedExtPDPType()));
        assertTrue(Arrays.equals(impl.getExtPDPAddress().getData(), this.getEncodedExtPdpAddress()));
        assertTrue(Arrays.equals(impl.getAPNOIReplacement().getData(), this.getAPNOIReplacement()));
        assertEquals(impl.getSIPTOPermission(), SIPTOPermission.siptoNotAllowed);
        assertEquals(impl.getLIPAPermission(), LIPAPermission.lipaOnly);

    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {

        PDPTypeImpl pdpType = new PDPTypeImpl(getEncodedPDPType());
        PDPAddressImpl pdpAddress = new PDPAddressImpl(getEncodedPDPAddress());
        APNImpl apn = new APNImpl(getEncodedApn());
        QoSSubscribedImpl qosSubscribed = new QoSSubscribedImpl(getEncodedQosSubscribed());
        ExtQoSSubscribedImpl extQoSSubscribed = new ExtQoSSubscribedImpl(getEncodedExtQosSubscribed());
        ChargingCharacteristicsImpl chargingCharacteristics = new ChargingCharacteristicsImpl(
                getEncodedchargingCharacteristics());
        Ext2QoSSubscribedImpl ext2QoSSubscribed = new Ext2QoSSubscribedImpl(getEncodedqos2Subscribed());
        Ext3QoSSubscribedImpl ext3QoSSubscribed = new Ext3QoSSubscribedImpl(getEncodedqos3Subscribed());
        Ext4QoSSubscribedImpl ext4QoSSubscribed = new Ext4QoSSubscribedImpl(91);
        ExtPDPTypeImpl extPdpType = new ExtPDPTypeImpl(getEncodedExtPDPType());
        PDPAddressImpl extPdpAddress = new PDPAddressImpl(getEncodedExtPdpAddress());
        APNOIReplacement apnoiReplacement = new APNOIReplacementImpl(this.getAPNOIReplacement());

        PDPContextImpl impl = new PDPContextImpl(15, pdpType, pdpAddress, qosSubscribed,
                true, apn, MAPExtensionContainerTest.GetTestExtensionContainer(), extQoSSubscribed,
                chargingCharacteristics, ext2QoSSubscribed,
                ext3QoSSubscribed, ext4QoSSubscribed, apnoiReplacement,
                extPdpType, extPdpAddress, SIPTOPermission.siptoNotAllowed, LIPAPermission.lipaOnly);
//        int pdpContextId, PDPType pdpType, PDPAddress pdpAddress, QoSSubscribed qosSubscribed,
//        boolean vplmnAddressAllowed, APN apn, MAPExtensionContainer extensionContainer, ExtQoSSubscribed extQoSSubscribed,
//        ChargingCharacteristics chargingCharacteristics, Ext2QoSSubscribed ext2QoSSubscribed,
//        Ext3QoSSubscribed ext3QoSSubscribed, Ext4QoSSubscribed ext4QoSSubscribed, APNOIReplacement apnoiReplacement,
//        ExtPDPType extpdpType, PDPAddress extpdpAddress, SIPTOPermission sipToPermission, LIPAPermission lipaPermission

        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
