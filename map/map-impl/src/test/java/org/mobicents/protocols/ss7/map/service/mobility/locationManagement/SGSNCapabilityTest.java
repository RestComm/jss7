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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SGSNCapabilityTest {

    public byte[] getData() {
        return new byte[] { 48, 81, 5, 0, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6,
                48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 2, -128, 0, -125, 0, -124, 2, 4, -16,
                -123, 2, 3, -8, -122, 2, 1, -2, -121, 0, -120, 2, 3, -8, -119, 5, 6, -1, -1, -1, -64, -121, 0, -118, 0, -117,
                1, -1 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        SGSNCapabilityImpl prim = new SGSNCapabilityImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(prim.getSolsaSupportIndicator());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        assertTrue(prim.getSuperChargerSupportedInServingNetworkEntity().getSendSubscriberData());
        assertTrue(prim.getGprsEnhancementsSupportIndicator());
        assertTrue(prim.getSupportedCamelPhases().getPhase1Supported());
        assertTrue(prim.getSupportedLCSCapabilitySets().getCapabilitySetRelease4());
        assertTrue(prim.getOfferedCamel4CSIs().getDCsi());
        assertTrue(prim.getSmsCallBarringSupportIndicator());
        assertTrue(prim.getSupportedRATTypesIndicator().getEUtran());
        assertTrue(prim.getSupportedFeatures().getBaoc());

        assertTrue(prim.getTAdsDataRetrieval());
        assertTrue(prim.getHomogeneousSupportOfIMSVoiceOverPSSessions());

    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testEncode() throws Exception {
        boolean solsaSupportIndicator = true;
        ;
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        SuperChargerInfo superChargerSupportedInServingNetworkEntity = new SuperChargerInfoImpl(true);
        ;
        boolean gprsEnhancementsSupportIndicator = true;
        SupportedCamelPhases supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, true);
        SupportedLCSCapabilitySets supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, true, true, true, true);
        OfferedCamel4CSIs offeredCamel4CSIs = new OfferedCamel4CSIsImpl(true, true, true, true, true, true, true);
        boolean smsCallBarringSupportIndicator = true;
        SupportedRATTypes supportedRATTypesIndicator = new SupportedRATTypesImpl(true, true, true, true, true);
        SupportedFeatures supportedFeatures = new SupportedFeaturesImpl(true, true, true, true, true, true, true, true, true,
                true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
        boolean tAdsDataRetrieval = true;
        Boolean homogeneousSupportOfIMSVoiceOverPSSessions = Boolean.TRUE;

        SGSNCapabilityImpl prim = new SGSNCapabilityImpl(solsaSupportIndicator, extensionContainer,
                superChargerSupportedInServingNetworkEntity, gprsEnhancementsSupportIndicator, supportedCamelPhases,
                supportedLCSCapabilitySets, offeredCamel4CSIs, smsCallBarringSupportIndicator, supportedRATTypesIndicator,
                supportedFeatures, tAdsDataRetrieval, homogeneousSupportOfIMSVoiceOverPSSessions);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

    }

}
