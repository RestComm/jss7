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
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedFeatures;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ODBGeneralData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.RegionalSubscriptionResponse;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.locationManagement.SupportedFeaturesImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class InsertSubscriberDataResponseTest {

    public byte[] getData() {
        return new byte[] { 48, 81, -95, 3, 4, 1, 16, -94, 3, 4, 1, 22, -93, 3, 4, 1, 0, -124, 5, 3, 74, -43, 85, 80, -123, 1,
                1, -122, 2, 4, -16, -89, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48,
                11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -120, 2, 1, -2, -119, 5, 6, 85, 85, 85, 64 };
    };

    public byte[] getData1() {
        return new byte[] { 48, 25, -95, 3, 4, 1, 16, -94, 3, 4, 1, 22, -93, 3, 4, 1, 0, -124, 5, 3, 74, -43, 85, 80, -123, 1,
                1 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        // ISD Response V3 Test
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        InsertSubscriberDataResponseImpl prim = new InsertSubscriberDataResponseImpl(3);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        // teleserviceList
        ArrayList<ExtTeleserviceCode> teleserviceList = prim.getTeleserviceList();
        assertNotNull(teleserviceList);
        assertEquals(teleserviceList.size(), 1);
        ExtTeleserviceCode extTeleserviceCode = teleserviceList.get(0);
        assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        // bearerServiceList
        ArrayList<ExtBearerServiceCode> bearerServiceList = prim.getBearerServiceList();
        assertNotNull(bearerServiceList);
        assertEquals(bearerServiceList.size(), 1);
        ExtBearerServiceCode extBearerServiceCode = bearerServiceList.get(0);
        assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.Asynchronous9_6kbps);

        // ssList
        ArrayList<SSCode> ssList = prim.getSSList();
        assertNotNull(ssList);
        assertEquals(ssList.size(), 1);
        SSCode ssCode = ssList.get(0);
        assertEquals(ssCode.getSupplementaryCodeValue(), SupplementaryCodeValue.allServices);

        ODBGeneralData odbGeneralData = prim.getODBGeneralData();
        assertTrue(!odbGeneralData.getAllOGCallsBarred());
        assertTrue(odbGeneralData.getInternationalOGCallsBarred());
        assertTrue(!odbGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsBarred());
        assertTrue(!odbGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!odbGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(odbGeneralData.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(!odbGeneralData.getSsAccessBarred());
        assertTrue(odbGeneralData.getAllECTBarred());
        assertTrue(!odbGeneralData.getChargeableECTBarred());
        assertTrue(odbGeneralData.getInternationalECTBarred());
        assertTrue(!odbGeneralData.getInterzonalECTBarred());
        assertTrue(odbGeneralData.getDoublyChargeableECTBarred());
        assertTrue(!odbGeneralData.getMultipleECTBarred());
        assertTrue(odbGeneralData.getAllPacketOrientedServicesBarred());
        assertTrue(!odbGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertTrue(odbGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertTrue(!odbGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(odbGeneralData.getAllICCallsBarred());
        assertTrue(!odbGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(odbGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertTrue(!odbGeneralData.getRoamingOutsidePLMNBarred());
        assertTrue(odbGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertTrue(!odbGeneralData.getRegistrationAllCFBarred());
        assertTrue(odbGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertTrue(!odbGeneralData.getRegistrationInterzonalCFBarred());
        assertTrue(odbGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(!odbGeneralData.getRegistrationInternationalCFBarred());

        assertEquals(prim.getRegionalSubscriptionResponse(), RegionalSubscriptionResponse.tooManyZoneCodes);

        SupportedCamelPhases supportedCamelPhases = prim.getSupportedCamelPhases();
        assertTrue(supportedCamelPhases.getPhase1Supported());
        assertTrue(supportedCamelPhases.getPhase2Supported());
        assertTrue(supportedCamelPhases.getPhase3Supported());
        assertTrue(supportedCamelPhases.getPhase4Supported());

        assertNotNull(prim.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

        OfferedCamel4CSIs offeredCamel4CSIs = prim.getOfferedCamel4CSIs();
        assertTrue(offeredCamel4CSIs.getDCsi());
        assertTrue(offeredCamel4CSIs.getMgCsi());
        assertTrue(offeredCamel4CSIs.getMtSmsCsi());
        assertTrue(offeredCamel4CSIs.getOCsi());
        assertTrue(offeredCamel4CSIs.getPsiEnhancements());
        assertTrue(offeredCamel4CSIs.getTCsi());
        assertTrue(offeredCamel4CSIs.getVtCsi());

        SupportedFeatures supportedFeatures = prim.getSupportedFeatures();
        assertTrue(!supportedFeatures.getOdbAllApn());
        assertTrue(supportedFeatures.getOdbHPLMNApn());
        assertTrue(!supportedFeatures.getOdbVPLMNApn());
        assertTrue(supportedFeatures.getOdbAllOg());
        assertTrue(!supportedFeatures.getOdbAllInternationalOg());
        assertTrue(supportedFeatures.getOdbAllIntOgNotToHPLMNCountry());
        assertTrue(!supportedFeatures.getOdbAllInterzonalOg());
        assertTrue(supportedFeatures.getOdbAllInterzonalOgNotToHPLMNCountry());
        assertTrue(!supportedFeatures.getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry());
        assertTrue(supportedFeatures.getRegSub());
        assertTrue(!supportedFeatures.getTrace());
        assertTrue(supportedFeatures.getLcsAllPrivExcep());
        assertTrue(!supportedFeatures.getLcsUniversal());
        assertTrue(supportedFeatures.getLcsCallSessionRelated());
        assertTrue(!supportedFeatures.getLcsCallSessionUnrelated());
        assertTrue(supportedFeatures.getLcsPLMNOperator());
        assertTrue(!supportedFeatures.getLcsServiceType());
        assertTrue(supportedFeatures.getLcsAllMOLRSS());
        assertTrue(!supportedFeatures.getLcsBasicSelfLocation());
        assertTrue(supportedFeatures.getLcsAutonomousSelfLocation());
        assertTrue(!supportedFeatures.getLcsTransferToThirdParty());
        assertTrue(supportedFeatures.getSmMoPp());
        assertTrue(!supportedFeatures.getBarringOutgoingCalls());
        assertTrue(supportedFeatures.getBaoc());
        assertTrue(!supportedFeatures.getBoic());
        assertTrue(supportedFeatures.getBoicExHC());

        // IST Response V2 Test
        data = this.getData();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new InsertSubscriberDataResponseImpl(3);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        // teleserviceList
        teleserviceList = prim.getTeleserviceList();
        assertNotNull(teleserviceList);
        assertEquals(teleserviceList.size(), 1);
        extTeleserviceCode = teleserviceList.get(0);
        assertEquals(extTeleserviceCode.getTeleserviceCodeValue(), TeleserviceCodeValue.allSpeechTransmissionServices);

        // bearerServiceList
        bearerServiceList = prim.getBearerServiceList();
        assertNotNull(bearerServiceList);
        assertEquals(bearerServiceList.size(), 1);
        extBearerServiceCode = bearerServiceList.get(0);
        assertEquals(extBearerServiceCode.getBearerServiceCodeValue(), BearerServiceCodeValue.Asynchronous9_6kbps);

        // ssList
        ssList = prim.getSSList();
        assertNotNull(ssList);
        assertEquals(ssList.size(), 1);
        ssCode = ssList.get(0);
        assertEquals(ssCode.getSupplementaryCodeValue(), SupplementaryCodeValue.allServices);

        odbGeneralData = prim.getODBGeneralData();
        assertTrue(!odbGeneralData.getAllOGCallsBarred());
        assertTrue(odbGeneralData.getInternationalOGCallsBarred());
        assertTrue(!odbGeneralData.getInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsBarred());
        assertTrue(!odbGeneralData.getInterzonalOGCallsNotToHPLMNCountryBarred());
        assertTrue(odbGeneralData.getInterzonalOGCallsAndInternationalOGCallsNotToHPLMNCountryBarred());
        assertTrue(!odbGeneralData.getPremiumRateInformationOGCallsBarred());
        assertTrue(odbGeneralData.getPremiumRateEntertainementOGCallsBarred());
        assertTrue(!odbGeneralData.getSsAccessBarred());
        assertTrue(odbGeneralData.getAllECTBarred());
        assertTrue(!odbGeneralData.getChargeableECTBarred());
        assertTrue(odbGeneralData.getInternationalECTBarred());
        assertTrue(!odbGeneralData.getInterzonalECTBarred());
        assertTrue(odbGeneralData.getDoublyChargeableECTBarred());
        assertTrue(!odbGeneralData.getMultipleECTBarred());
        assertTrue(odbGeneralData.getAllPacketOrientedServicesBarred());
        assertTrue(!odbGeneralData.getRoamerAccessToHPLMNAPBarred());
        assertTrue(odbGeneralData.getRoamerAccessToVPLMNAPBarred());
        assertTrue(!odbGeneralData.getRoamingOutsidePLMNOGCallsBarred());
        assertTrue(odbGeneralData.getAllICCallsBarred());
        assertTrue(!odbGeneralData.getRoamingOutsidePLMNICCallsBarred());
        assertTrue(odbGeneralData.getRoamingOutsidePLMNICountryICCallsBarred());
        assertTrue(!odbGeneralData.getRoamingOutsidePLMNBarred());
        assertTrue(odbGeneralData.getRoamingOutsidePLMNCountryBarred());
        assertTrue(!odbGeneralData.getRegistrationAllCFBarred());
        assertTrue(odbGeneralData.getRegistrationCFNotToHPLMNBarred());
        assertTrue(!odbGeneralData.getRegistrationInterzonalCFBarred());
        assertTrue(odbGeneralData.getRegistrationInterzonalCFNotToHPLMNBarred());
        assertTrue(!odbGeneralData.getRegistrationInternationalCFBarred());

        assertEquals(prim.getRegionalSubscriptionResponse(), RegionalSubscriptionResponse.tooManyZoneCodes);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        // Start ISD Response Vesrion 3 Test

        // teleserviceList
        ArrayList<ExtTeleserviceCode> teleserviceList = new ArrayList<ExtTeleserviceCode>();
        ExtTeleserviceCode extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allSpeechTransmissionServices);
        teleserviceList.add(extTeleservice);

        // bearerServiceList
        ArrayList<ExtBearerServiceCode> bearerServiceList = new ArrayList<ExtBearerServiceCode>();
        ExtBearerServiceCodeImpl extBearerServiceCode = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.Asynchronous9_6kbps);
        bearerServiceList.add(extBearerServiceCode);

        // ssList
        ArrayList<SSCode> ssList = new ArrayList<SSCode>();
        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.allServices);
        ssList.add(ssCode);

        ODBGeneralData odbGeneralData = new ODBGeneralDataImpl(false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false,
                true, false);

        RegionalSubscriptionResponse regionalSubscriptionResponse = RegionalSubscriptionResponse.tooManyZoneCodes;

        SupportedCamelPhases supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, true);

        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        OfferedCamel4CSIs offeredCamel4CSIs = new OfferedCamel4CSIsImpl(true, true, true, true, true, true, true);

        SupportedFeatures supportedFeatures = new SupportedFeaturesImpl(false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false,
                true);

        InsertSubscriberDataResponseImpl prim = new InsertSubscriberDataResponseImpl(3, teleserviceList, bearerServiceList,
                ssList, odbGeneralData, regionalSubscriptionResponse, supportedCamelPhases, extensionContainer,
                offeredCamel4CSIs, supportedFeatures);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        // Start ISD Response Vesrion 2 Test
        prim = new InsertSubscriberDataResponseImpl(2, teleserviceList, bearerServiceList, ssList, odbGeneralData,
                regionalSubscriptionResponse);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

    }
}
