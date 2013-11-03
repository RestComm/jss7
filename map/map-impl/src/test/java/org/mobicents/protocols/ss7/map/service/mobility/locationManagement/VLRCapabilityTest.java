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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrivateExtension;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPPrivateExtensionImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.testng.annotations.Test;

public class VLRCapabilityTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 11, (byte) 128, 2, 4, (byte) 192, (byte) 129, 1, 1, (byte) 133, 2, 3, (byte) 240 };
    }

    private byte[] getEncodedDataEC() {
        return new byte[] { 48, 26, (byte) 128, 2, 4, (byte) 224, 48, 20, (byte) 160, 18, 48, 16, 6, 8, 42, (byte) 134, 8, 8,
                8, 8, 8, 1, 48, 4, 3, 2, 6, 64 };
    }

    private long[] getECOid() {
        return new long[] { 1, 2, 776, 8, 8, 8, 8, 1 };
    }

    private byte[] getECData() {
        return new byte[] { 48, 4, 3, 2, 6, 64 };
    }

    private byte[] getEncodedDataSuperChargerInfo() {
        return new byte[] { 48, 4, (byte) 163, 2, (byte) 128, 0 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 16, -126, 0, -124, 0, -122, 2, 1, 14, -121, 2, 3, 80, -120, 0, -119, 0 };
    }

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        VLRCapabilityImpl asc = new VLRCapabilityImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        SupportedCamelPhases scph = asc.getSupportedCamelPhases();
        assertTrue(scph.getPhase1Supported());
        assertTrue(scph.getPhase2Supported());
        assertFalse(scph.getPhase3Supported());
        assertFalse(scph.getPhase4Supported());

        assertNull(asc.getExtensionContainer());

        assertFalse(asc.getSolsaSupportIndicator());

        assertEquals(asc.getIstSupportIndicator(), ISTSupportIndicator.istCommandSupported);

        assertNull(asc.getSuperChargerSupportedInServingNetworkEntity());
        assertFalse(asc.getLongFtnSupported());

        SupportedLCSCapabilitySets slcs = asc.getSupportedLCSCapabilitySets();
        assertTrue(slcs.getCapabilitySetRelease98_99());
        assertTrue(slcs.getCapabilitySetRelease4());
        assertTrue(slcs.getCapabilitySetRelease5());
        assertTrue(slcs.getCapabilitySetRelease6());
        assertFalse(slcs.getCapabilitySetRelease7());

        assertNull(asc.getOfferedCamel4CSIs());
        assertNull(asc.getSupportedRATTypesIndicator());
        assertFalse(asc.getLongGroupIDSupported());
        assertFalse(asc.getMtRoamingForwardingSupported());

        rawData = getEncodedDataEC();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new VLRCapabilityImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        scph = asc.getSupportedCamelPhases();
        assertTrue(scph.getPhase1Supported());
        assertTrue(scph.getPhase2Supported());
        assertTrue(scph.getPhase3Supported());
        assertFalse(scph.getPhase4Supported());

        MAPExtensionContainer ext = asc.getExtensionContainer();
        assertTrue(Arrays.equals(ext.getPrivateExtensionList().get(0).getOId(), getECOid()));
        assertTrue(Arrays.equals(ext.getPrivateExtensionList().get(0).getData(), getECData()));

        assertFalse(asc.getSolsaSupportIndicator());

        assertNull(asc.getIstSupportIndicator());

        assertNull(asc.getSuperChargerSupportedInServingNetworkEntity());
        assertFalse(asc.getLongFtnSupported());

        assertNull(asc.getSupportedLCSCapabilitySets());

        assertNull(asc.getOfferedCamel4CSIs());
        assertNull(asc.getSupportedRATTypesIndicator());
        assertFalse(asc.getLongGroupIDSupported());
        assertFalse(asc.getMtRoamingForwardingSupported());

        rawData = getEncodedDataSuperChargerInfo();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new VLRCapabilityImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertNull(asc.getSupportedCamelPhases());

        assertNull(asc.getExtensionContainer());
        assertFalse(asc.getSolsaSupportIndicator());

        assertNull(asc.getIstSupportIndicator());

        assertTrue(asc.getSuperChargerSupportedInServingNetworkEntity().getSendSubscriberData());
        assertFalse(asc.getLongFtnSupported());

        assertNull(asc.getSupportedLCSCapabilitySets());

        assertNull(asc.getOfferedCamel4CSIs());
        assertNull(asc.getSupportedRATTypesIndicator());
        assertFalse(asc.getLongGroupIDSupported());
        assertFalse(asc.getMtRoamingForwardingSupported());

        rawData = getEncodedDataFull();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new VLRCapabilityImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertNull(asc.getSupportedCamelPhases());
        assertNull(asc.getExtensionContainer());
        assertTrue(asc.getSolsaSupportIndicator());

        assertNull(asc.getIstSupportIndicator());
        assertNull(asc.getSuperChargerSupportedInServingNetworkEntity());
        assertTrue(asc.getLongFtnSupported());

        assertNull(asc.getSupportedLCSCapabilitySets());

        OfferedCamel4CSIs offeredCamel4CSIs = asc.getOfferedCamel4CSIs();
        // boolean oCsi, boolean dCsi, boolean vtCsi, boolean tCsi, boolean mtSMSCsi, boolean mgCsi, boolean psiEnhancements
        assertFalse(offeredCamel4CSIs.getOCsi());
        assertFalse(offeredCamel4CSIs.getDCsi());
        assertFalse(offeredCamel4CSIs.getVtCsi());
        assertFalse(offeredCamel4CSIs.getTCsi());
        assertTrue(offeredCamel4CSIs.getMtSmsCsi());
        assertTrue(offeredCamel4CSIs.getMgCsi());
        assertTrue(offeredCamel4CSIs.getPsiEnhancements());

        SupportedRATTypes rat = asc.getSupportedRATTypesIndicator();
        // boolean utran, boolean geran, boolean gan, boolean i_hspa_evolution, boolean e_utran
        assertFalse(rat.getUtran());
        assertTrue(rat.getGeran());
        assertFalse(rat.getGan());
        assertTrue(rat.getIHspaEvolution());
        assertFalse(rat.getEUtran());

        assertTrue(asc.getLongGroupIDSupported());
        assertTrue(asc.getMtRoamingForwardingSupported());
    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {

        SupportedCamelPhases scp = new SupportedCamelPhasesImpl(true, true, false, false);
        SupportedLCSCapabilitySets slcs = new SupportedLCSCapabilitySetsImpl(true, true, true, true, false);
        VLRCapabilityImpl asc = new VLRCapabilityImpl(scp, null, false, ISTSupportIndicator.istCommandSupported, null, false,
                slcs, null, null, false, false);
        // SupportedCamelPhases supportedCamelPhases, MAPExtensionContainer extensionContainer, boolean solsaSupportIndicator,
        // IstSupportIndicator istSupportIndicator, SuperChargerInfo superChargerSupportedInServingNetworkEntity, boolean
        // longFtnSupported,
        // SupportedLCSCapabilitySets supportedLCSCapabilitySets, OfferedCamel4CSIs offeredCamel4CSIs, SupportedRATTypes
        // supportedRATTypesIndicator,
        // boolean longGroupIDSupported, boolean mtRoamingForwardingSupported

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        scp = new SupportedCamelPhasesImpl(true, true, true, false);
        ArrayList<MAPPrivateExtension> privateExtensionList = new ArrayList<MAPPrivateExtension>();
        MAPPrivateExtensionImpl pe = new MAPPrivateExtensionImpl(getECOid(), getECData());
        privateExtensionList.add(pe);
        MAPExtensionContainerImpl ext = new MAPExtensionContainerImpl(privateExtensionList, null);
        asc = new VLRCapabilityImpl(scp, ext, false, null, null, false, null, null, null, false, false);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataEC();
        assertTrue(Arrays.equals(rawData, encodedData));

        SuperChargerInfo sci = new SuperChargerInfoImpl(true);
        asc = new VLRCapabilityImpl(null, null, false, null, sci, false, null, null, null, false, false);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataSuperChargerInfo();
        assertTrue(Arrays.equals(rawData, encodedData));

        OfferedCamel4CSIsImpl offeredCamel4CSIs = new OfferedCamel4CSIsImpl(false, false, false, false, true, true, true);
        SupportedRATTypesImpl rat = new SupportedRATTypesImpl(false, true, false, true, false);
        asc = new VLRCapabilityImpl(null, null, true, null, null, true, null, offeredCamel4CSIs, rat, true, true);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
