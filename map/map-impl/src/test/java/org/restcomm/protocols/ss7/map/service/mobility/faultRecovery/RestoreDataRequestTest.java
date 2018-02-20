/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.map.service.mobility.faultRecovery;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.map.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.mobility.faultRecovery.RestoreDataRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.VLRCapabilityImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class RestoreDataRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 9, 4, 7, 17, 33, 34, 51, 67, 68, 85 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 64, 4, 7, 17, 33, 34, 51, 67, 68, 85, 4, 4, 22, 33, 44, 55, 48, 39, (byte) 160, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33, (byte) 166, 4, (byte) 128, 2, 4, (byte) 192,
                (byte) 135, 0 };
    }

    private byte[] getLmsiData() {
        return new byte[] { 22, 33, 44, 55 };
    }

    @Test
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        RestoreDataRequestImpl prim = new RestoreDataRequestImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        IMSI imsi = prim.getImsi();
        assertTrue(imsi.getData().equals("11122233344455"));

        assertNull(prim.getLmsi());
        assertNull(prim.getExtensionContainer());
        assertNull(prim.getVLRCapability());
        assertFalse(prim.getRestorationIndicator());


        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        prim = new RestoreDataRequestImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = prim.getImsi();
        assertTrue(imsi.getData().equals("11122233344455"));

        assertEquals(prim.getLmsi().getData(), getLmsiData());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        VLRCapability vlrCapability = prim.getVLRCapability();
        SupportedCamelPhases supportedCamelPhases = vlrCapability.getSupportedCamelPhases();
        assertTrue(supportedCamelPhases.getPhase1Supported());
        assertTrue(supportedCamelPhases.getPhase2Supported());
        assertFalse(supportedCamelPhases.getPhase3Supported());
        assertFalse(supportedCamelPhases.getPhase4Supported());

        assertTrue(prim.getRestorationIndicator());

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        IMSIImpl imsi = new IMSIImpl("11122233344455");
        RestoreDataRequestImpl prim = new RestoreDataRequestImpl(imsi, null, null, null, false);
        // IMSI imsi, LMSI lmsi, VLRCapability vlrCapability, MAPExtensionContainer extensionContainer, boolean restorationIndicator

        AsnOutputStream asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        LMSI lmsi = new LMSIImpl(getLmsiData());
        SupportedCamelPhases supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, false, false);
        VLRCapability vlrCapability = new VLRCapabilityImpl(supportedCamelPhases, null, false, null, null, false, null, null, null, false, false);
//      SupportedCamelPhases supportedCamelPhases, MAPExtensionContainer extensionContainer,
//      boolean solsaSupportIndicator, ISTSupportIndicator istSupportIndicator,
//      SuperChargerInfo superChargerSupportedInServingNetworkEntity, boolean longFtnSupported,
//      SupportedLCSCapabilitySets supportedLCSCapabilitySets, OfferedCamel4CSIs offeredCamel4CSIs,
//      SupportedRATTypes supportedRATTypesIndicator, boolean longGroupIDSupported, boolean mtRoamingForwardingSupported        
        prim = new RestoreDataRequestImpl(imsi, lmsi, vlrCapability, MAPExtensionContainerTest.GetTestExtensionContainer(), true);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

    }

}
