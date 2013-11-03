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

package org.mobicents.protocols.ss7.map.service.callhandling;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NAEACIC;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.ProtocolId;
import org.mobicents.protocols.ss7.map.api.primitives.SignalInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.AllowedServices;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.mobicents.protocols.ss7.map.api.service.callhandling.RoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.UnavailabilityCause;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformation;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberState;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateChoice;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingOptions;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.ExternalSignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.primitives.NAEACICImpl;
import org.mobicents.protocols.ss7.map.primitives.NAEAPreferredCIImpl;
import org.mobicents.protocols.ss7.map.primitives.SignalInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.SubscriberInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.SubscriberStateImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CUGInterlockImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ForwardingOptionsImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/*
 *
 * @author cristian veliscu
 * @author sergey vetyutnev
 *
 */
public class SendRoutingInformationResponseTest {
    Logger logger = Logger.getLogger(SendRoutingInformationResponseTest.class);

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    private byte[] getMAPV3ParameterTestData() {
        return new byte[] { -93, -126, 1, 94, -119, 8, 16, 33, 2, 2, 16, -119, 34, -9, 48, 12, -123, 7, -111, -105, 114, 99,
                80, 24, -7, -122, 1, 36, -93, 49, 4, 4, 1, 2, 3, 4, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13,
                14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -122, 0,
                -89, 50, -96, 44, 2, 1, 1, -128, 8, 16, 0, 0, 0, 0, 0, 0, 0, -127, 7, -111, -105, 114, 99, 80, 24, -7, -93, 9,
                -128, 7, 39, -12, 67, 121, -98, 41, -96, -122, 7, -111, -105, 114, 99, 80, 24, -7, -119, 0, -95, 2, -128, 0,
                -95, 3, 4, 1, 96, -91, 3, -126, 1, 22, -124, 0, -126, 7, -111, -105, 114, 99, 80, 24, -7, -96, 39, -96, 32, 48,
                10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26,
                -95, 3, 31, 32, 33, -86, 46, -128, 3, 15, 48, 5, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -85, 45, -128, 0,
                -127, 0, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42,
                3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -116, 7, -111, -105, 114, 99, 80, 24, -7, -115, 1, 5, -114,
                1, 5, -113, 2, 4, -32, -112, 2, 1, -2, -79, 9, 4, 7, -111, -105, 114, 99, 80, 24, -7, -78, 3, 4, 1, 96, -77, 3,
                -126, 1, 22, -108, 2, 6, -64, -107, 1, 4, -106, 0, -73, 9, 10, 1, 2, 4, 4, 10, 20, 30, 40 };
    }

    private byte[] getMAPV3ParameterTestData2() {
        return new byte[] { 48, 70, 4, 8, 16, 33, 2, 2, 16, -119, 34, -9, 4, 7, -111, -105, 114, 99, 80, 24, -7, 48, 49, 4, 4,
                1, 2, 3, 4, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11,
                6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    private byte[] getMAPV3ParameterTestData3() {
        return new byte[] { 48, 19, 4, 8, 16, 33, 2, 2, 16, -119, 34, -9, 4, 7, -111, -105, 114, 99, 80, 24, -7 };
    }

    public byte[] getData1() {
        return new byte[] { (byte) 163, 19, (byte) 137, 8, 16, 33, 2, 2, 16, -119, 34, -9, 4, 7, -111, -105, 114, 99, 80, 24,
                -7 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 163, 24, (byte) 137, 8, 16, 33, 2, 2, 16, -119, 34, -9, 48, 12, (byte) 133, 7, -111, -105,
                114, 99, 80, 24, -7, (byte) 134, 1, 36 };
    }

    byte[] dataGeographicalInformation = new byte[] { 16, 0, 0, 0, 0, 0, 0, 0 };

    private byte[] getGugData() {
        return new byte[] { 1, 2, 3, 4 };
    }

    private byte[] getExtBearerServiceData() {
        return new byte[] { 22 };
    }

    public byte[] getNAEACICIData() {
        return new byte[] { 15, 48, 5 };
    };

    public byte[] getSignalInfoData() {
        return new byte[] { 10, 20, 30, 40 };
    };

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {
        byte[] data = getData1();
        byte[] data_ = getData2();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        SendRoutingInformationResponseImpl sri = new SendRoutingInformationResponseImpl();
        sri.decodeAll(asn);

        IMSI imsi = sri.getIMSI();
        ExtendedRoutingInfo extRoutingInfo = sri.getExtendedRoutingInfo();
        RoutingInfo routingInfo = extRoutingInfo.getRoutingInfo();
        ISDNAddressString roamingNumber = routingInfo.getRoamingNumber();

        assertNotNull(imsi);
        assertEquals(imsi.getData(), "011220200198227");
        assertNotNull(roamingNumber);
        // logger.info(":::::::" + roamingNumber.getAddress());
        assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(roamingNumber.getAddress(), "79273605819");

        // :::::::::::::::::::::::::::::::::
        AsnInputStream asn_ = new AsnInputStream(data_);
        int tag_ = asn_.readTag();

        SendRoutingInformationResponseImpl sri_ = new SendRoutingInformationResponseImpl();
        sri_.decodeAll(asn_);

        IMSI imsi_ = sri_.getIMSI();
        ExtendedRoutingInfo extRoutingInfo_ = sri_.getExtendedRoutingInfo();
        RoutingInfo routingInfo_ = extRoutingInfo_.getRoutingInfo();
        ForwardingData forwardingData_ = routingInfo_.getForwardingData();
        ISDNAddressString isdnAdd_ = forwardingData_.getForwardedToNumber();
        ForwardingOptions forwardingOptions_ = forwardingData_.getForwardingOptions();

        assertNotNull(imsi_);
        assertNotNull(forwardingData_);
        assertNotNull(forwardingOptions_);
        assertNotNull(isdnAdd_);
        assertEquals(imsi_.getData(), "011220200198227");
        assertEquals(isdnAdd_.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd_.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(isdnAdd_.getAddress(), "79273605819");
        assertTrue(!forwardingOptions_.isNotificationToForwardingParty());
        assertTrue(!forwardingOptions_.isRedirectingPresentation());
        assertTrue(forwardingOptions_.isNotificationToCallingParty());
        assertTrue(forwardingOptions_.getForwardingReason() == ForwardingReason.busy);

        // MAP V3 All parameter test
        data = this.getMAPV3ParameterTestData();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        SendRoutingInformationResponseImpl prim = new SendRoutingInformationResponseImpl();
        prim.decodeAll(asn);

        assertEquals(tag, SendRoutingInformationResponseImpl.TAG_sendRoutingInfoRes);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        imsi = prim.getIMSI();
        extRoutingInfo = prim.getExtendedRoutingInfo();
        routingInfo = extRoutingInfo.getRoutingInfo();
        roamingNumber = routingInfo.getRoamingNumber();
        assertNotNull(imsi);
        assertEquals(imsi.getData(), "011220200198227");
        assertNull(roamingNumber);
        ForwardingData forwardingData = routingInfo.getForwardingData();
        ISDNAddressString isdnAdd = forwardingData_.getForwardedToNumber();
        assertEquals(isdnAdd.getAddressNature(), AddressNature.international_number);
        assertEquals(isdnAdd.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(isdnAdd.getAddress(), "79273605819");
        ForwardingOptions forwardingOptions = forwardingData.getForwardingOptions();
        assertTrue(!forwardingOptions.isNotificationToForwardingParty());
        assertTrue(!forwardingOptions.isRedirectingPresentation());
        assertTrue(forwardingOptions.isNotificationToCallingParty());
        assertTrue(forwardingOptions.getForwardingReason() == ForwardingReason.busy);

        // cugCheckInfo
        assertTrue(Arrays.equals(prim.getCUGCheckInfo().getCUGInterlock().getData(), getGugData()));
        assertTrue(prim.getCUGCheckInfo().getCUGOutgoingAccess());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getCUGCheckInfo().getExtensionContainer()));
        // cugSubscriptionFlag
        assertTrue(prim.getCUGSubscriptionFlag());

        // subscriberInfo
        LocationInformation locInfo = prim.getSubscriberInfo().getLocationInformation();
        assertNotNull(locInfo);
        assertEquals((int) locInfo.getAgeOfLocationInformation(), 1);
        assertTrue(Arrays.equals(locInfo.getGeographicalInformation().getData(), dataGeographicalInformation));
        ISDNAddressString vlrN = locInfo.getVlrNumber();
        assertTrue(vlrN.getAddress().equals("79273605819"));
        assertEquals(vlrN.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrN.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 724);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 34);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 31134);
        assertEquals(locInfo.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength()
                .getCellIdOrServiceAreaCode(), 10656);
        ISDNAddressString mscN = locInfo.getVlrNumber();
        assertTrue(mscN.getAddress().equals("79273605819"));
        assertEquals(mscN.getAddressNature(), AddressNature.international_number);
        assertEquals(mscN.getNumberingPlan(), NumberingPlan.ISDN);
        assertFalse(locInfo.getCurrentLocationRetrieved());
        assertTrue(locInfo.getSaiPresent());
        SubscriberState subState = prim.getSubscriberInfo().getSubscriberState();
        assertEquals(subState.getSubscriberStateChoice(), SubscriberStateChoice.assumedIdle);
        // ssList
        assertNotNull(prim.getSSList());
        assertEquals(prim.getSSList().size(), 1);
        assertEquals(prim.getSSList().get(0).getSupplementaryCodeValue(),
                SupplementaryCodeValue.allFacsimileTransmissionServices);

        // basicService
        assertTrue(Arrays.equals(prim.getBasicService().getExtBearerService().getData(), this.getExtBearerServiceData()));
        assertNull(prim.getBasicService().getExtTeleservice());
        // forwardingInterrogationRequired
        assertTrue(prim.getForwardingInterrogationRequired());
        // vmscAddress
        ISDNAddressString vmscAddress = prim.getVmscAddress();
        assertTrue(vmscAddress.getAddress().equals("79273605819"));
        assertEquals(vmscAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(vmscAddress.getNumberingPlan(), NumberingPlan.ISDN);
        // naeaPreferredCI
        assertEquals(prim.getNaeaPreferredCI().getNaeaPreferredCIC().getData(), this.getNAEACICIData());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getNaeaPreferredCI().getExtensionContainer()));
        // ccbsIndicators
        assertTrue(prim.getCCBSIndicators().getCCBSPossible());
        assertTrue(prim.getCCBSIndicators().getKeepCCBSCallIndicator());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getCCBSIndicators().getMAPExtensionContainer()));
        // msisdn
        ISDNAddressString msisdn = prim.getMsisdn();
        assertTrue(msisdn.getAddress().equals("79273605819"));
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        // nrPortabilityStatus
        assertEquals(prim.getNumberPortabilityStatus(), NumberPortabilityStatus.foreignNumberPortedIn);
        assertEquals(prim.getISTAlertTimer().intValue(), 5);
        // supportedCamelPhases
        SupportedCamelPhases scf = prim.getSupportedCamelPhasesInVMSC();
        assertTrue(scf.getPhase1Supported());
        assertTrue(scf.getPhase2Supported());
        assertTrue(scf.getPhase3Supported());
        assertFalse(scf.getPhase4Supported());
        // offeredCamel4CSIs
        OfferedCamel4CSIs offeredCamel4CSIs = prim.getOfferedCamel4CSIsInVMSC();
        assertTrue(offeredCamel4CSIs.getDCsi());
        assertTrue(offeredCamel4CSIs.getMgCsi());
        assertTrue(offeredCamel4CSIs.getMtSmsCsi());
        assertTrue(offeredCamel4CSIs.getOCsi());
        assertTrue(offeredCamel4CSIs.getPsiEnhancements());
        assertTrue(offeredCamel4CSIs.getTCsi());
        assertTrue(offeredCamel4CSIs.getVtCsi());
        // /getRoutingInfo2
        roamingNumber = prim.getRoutingInfo2().getRoamingNumber();
        assertNotNull(roamingNumber);
        assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(roamingNumber.getAddress(), "79273605819");
        // ssList2
        assertNotNull(prim.getSSList2());
        assertEquals(prim.getSSList2().size(), 1);
        assertEquals(prim.getSSList2().get(0).getSupplementaryCodeValue(),
                SupplementaryCodeValue.allFacsimileTransmissionServices);
        // basicService2
        assertTrue(Arrays.equals(prim.getBasicService2().getExtBearerService().getData(), this.getExtBearerServiceData()));
        assertNull(prim.getBasicService2().getExtTeleservice());
        // allowedServices
        AllowedServices allowedServices = prim.getAllowedServices();
        assertTrue(allowedServices.getFirstServiceAllowed());
        assertTrue(allowedServices.getSecondServiceAllowed());
        // unavailabilityCause
        assertEquals(prim.getUnavailabilityCause(), UnavailabilityCause.busySubscriber);
        // releaseResourcesSupported
        assertTrue(prim.getReleaseResourcesSupported());
        // gsmBearerCapability
        ProtocolId protocolId2 = prim.getGsmBearerCapability().getProtocolId();
        byte[] signalInfo2 = prim.getGsmBearerCapability().getSignalInfo().getData();
        assertTrue(Arrays.equals(getSignalInfoData(), signalInfo2));
        assertNotNull(protocolId2);
        assertEquals(protocolId2, ProtocolId.gsm_0806);
        assertEquals(prim.getMapProtocolVersion(), 3);

        // MAP V2
        data = this.getMAPV3ParameterTestData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new SendRoutingInformationResponseImpl(2);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertNull(prim.getExtensionContainer());
        imsi = prim.getIMSI();
        assertNull(prim.getExtendedRoutingInfo());
        assertNotNull(imsi);
        assertEquals(imsi.getData(), "011220200198227");

        // cugCheckInfo
        assertTrue(Arrays.equals(prim.getCUGCheckInfo().getCUGInterlock().getData(), getGugData()));
        assertTrue(prim.getCUGCheckInfo().getCUGOutgoingAccess());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getCUGCheckInfo().getExtensionContainer()));
        // cugSubscriptionFlag
        assertFalse(prim.getCUGSubscriptionFlag());
        // subscriberInfo
        assertNull(prim.getSubscriberInfo());
        // ssList
        assertNull(prim.getSSList());

        // basicService
        assertNull(prim.getBasicService());

        // forwardingInterrogationRequired
        assertFalse(prim.getForwardingInterrogationRequired());
        // vmscAddress
        assertNull(prim.getVmscAddress());

        // naeaPreferredCI
        assertNull(prim.getNaeaPreferredCI());
        // ccbsIndicators
        assertNull(prim.getCCBSIndicators());
        // msisdn
        assertNull(prim.getMsisdn());
        // nrPortabilityStatus
        assertNull(prim.getNumberPortabilityStatus());
        // supportedCamelPhases
        assertNull(prim.getSupportedCamelPhasesInVMSC());
        // offeredCamel4CSIs
        assertNull(prim.getOfferedCamel4CSIsInVMSC());
        // /getRoutingInfo2
        roamingNumber = prim.getRoutingInfo2().getRoamingNumber();
        assertNotNull(roamingNumber);
        assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(roamingNumber.getAddress(), "79273605819");
        // ssList2
        assertNull(prim.getSSList2());
        // basicService2
        assertNull(prim.getBasicService2());
        // allowedServices
        assertNull(prim.getAllowedServices());
        // unavailabilityCause
        assertNull(prim.getUnavailabilityCause());
        // releaseResourcesSupported
        assertFalse(prim.getReleaseResourcesSupported());
        // gsmBearerCapability
        assertNull(prim.getGsmBearerCapability());
        assertEquals(prim.getMapProtocolVersion(), 2);

        // MAP V1
        data = this.getMAPV3ParameterTestData3();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new SendRoutingInformationResponseImpl(2);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertNull(prim.getExtensionContainer());
        imsi = prim.getIMSI();
        assertNull(prim.getExtendedRoutingInfo());
        assertNotNull(imsi);
        assertEquals(imsi.getData(), "011220200198227");

        // cugCheckInfo
        assertNull(prim.getCUGCheckInfo());
        // cugSubscriptionFlag
        assertFalse(prim.getCUGSubscriptionFlag());
        // subscriberInfo
        assertNull(prim.getSubscriberInfo());
        // ssList
        assertNull(prim.getSSList());

        // basicService
        assertNull(prim.getBasicService());

        // forwardingInterrogationRequired
        assertFalse(prim.getForwardingInterrogationRequired());
        // vmscAddress
        assertNull(prim.getVmscAddress());

        // naeaPreferredCI
        assertNull(prim.getNaeaPreferredCI());
        // ccbsIndicators
        assertNull(prim.getCCBSIndicators());
        // msisdn
        assertNull(prim.getMsisdn());
        // nrPortabilityStatus
        assertNull(prim.getNumberPortabilityStatus());
        // supportedCamelPhases
        assertNull(prim.getSupportedCamelPhasesInVMSC());
        // offeredCamel4CSIs
        assertNull(prim.getOfferedCamel4CSIsInVMSC());
        // /getRoutingInfo2
        roamingNumber = prim.getRoutingInfo2().getRoamingNumber();
        assertNotNull(roamingNumber);
        assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(roamingNumber.getAddress(), "79273605819");
        // ssList2
        assertNull(prim.getSSList2());
        // basicService2
        assertNull(prim.getBasicService2());
        // allowedServices
        assertNull(prim.getAllowedServices());
        // unavailabilityCause
        assertNull(prim.getUnavailabilityCause());
        // releaseResourcesSupported
        assertFalse(prim.getReleaseResourcesSupported());
        // gsmBearerCapability
        assertNull(prim.getGsmBearerCapability());
        assertEquals(prim.getMapProtocolVersion(), 2);
    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {
        byte[] data = getData1();
        byte[] data_ = getData2();

        IMSIImpl imsi = new IMSIImpl("011220200198227");
        ISDNAddressString roamingNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        RoutingInfoImpl routingInfo = new RoutingInfoImpl(roamingNumber);
        ExtendedRoutingInfoImpl extRoutingInfo = new ExtendedRoutingInfoImpl(routingInfo);
        SendRoutingInformationResponseImpl sri = new SendRoutingInformationResponseImpl(imsi, extRoutingInfo, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        sri.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data, encodedData));

        // :::::::::::::::::::::::::::::::::
        IMSIImpl imsi_ = new IMSIImpl("011220200198227");
        ISDNAddressString isdnAdd_ = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        ForwardingOptions forwardingOptions_ = new ForwardingOptionsImpl(false, false, true, ForwardingReason.busy);
        ForwardingData forwardingData_ = new ForwardingDataImpl(isdnAdd_, null, forwardingOptions_, null, null);
        RoutingInfoImpl routingInfo_ = new RoutingInfoImpl(forwardingData_);
        ExtendedRoutingInfoImpl extRoutingInfo_ = new ExtendedRoutingInfoImpl(routingInfo_);
        SendRoutingInformationResponseImpl sri_ = new SendRoutingInformationResponseImpl(imsi_, extRoutingInfo_, null, null);

        AsnOutputStream asnOS_ = new AsnOutputStream();
        sri_.encodeAll(asnOS_);

        byte[] encodedData_ = asnOS_.toByteArray();
        assertTrue(Arrays.equals(data_, encodedData_));

        // MAP V3 Parameter test
        // cugCheckInfo
        CUGInterlock cugInterlock = new CUGInterlockImpl(getGugData());
        CUGCheckInfo cugCheckInfo = new CUGCheckInfoImpl(cugInterlock, true,
                MAPExtensionContainerTest.GetTestExtensionContainer());
        // cugSubscriptionFlag
        boolean cugSubscriptionFlag = true;
        // subscriberInfo
        ISDNAddressStringImpl vlrN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        ISDNAddressStringImpl mscN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        CellGlobalIdOrServiceAreaIdFixedLengthImpl c0 = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(724, 34, 31134, 10656);
        CellGlobalIdOrServiceAreaIdOrLAI c = new CellGlobalIdOrServiceAreaIdOrLAIImpl(c0);
        GeographicalInformationImpl gi = new GeographicalInformationImpl(dataGeographicalInformation);
        LocationInformationImpl li = new LocationInformationImpl(1, gi, vlrN, null, c, null, null, mscN, null, false, true,
                null, null);
        SubscriberStateImpl ss = new SubscriberStateImpl(SubscriberStateChoice.assumedIdle, null);
        SubscriberInfo subscriberInfo = new SubscriberInfoImpl(li, ss, null, null, null, null, null, null, null);
        // ssList
        ArrayList<SSCode> ssList = new ArrayList<SSCode>();
        ssList.add(new SSCodeImpl(SupplementaryCodeValue.allFacsimileTransmissionServices));
        // basicService
        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(this.getExtBearerServiceData());
        ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);
        // forwardingInterrogationRequired
        boolean forwardingInterrogationRequired = true;
        // vmscAddress
        ISDNAddressString vmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        // extensionContainer
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        // naeaPreferredCI
        NAEACIC naeaPreferredCIC = new NAEACICImpl(this.getNAEACICIData());
        NAEAPreferredCIImpl naeaPreferredCI = new NAEAPreferredCIImpl(naeaPreferredCIC,
                MAPExtensionContainerTest.GetTestExtensionContainer());
        // ccbsIndicators
        CCBSIndicatorsImpl ccbsIndicators = new CCBSIndicatorsImpl(true, true,
                MAPExtensionContainerTest.GetTestExtensionContainer());
        // msisdn
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        // nrPortabilityStatus
        NumberPortabilityStatus nrPortabilityStatus = NumberPortabilityStatus.foreignNumberPortedIn;
        // istAlertTimer
        Integer istAlertTimer = new Integer(5);
        // supportedCamelPhases
        SupportedCamelPhases supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, false);
        ;
        // offeredCamel4CSIs
        OfferedCamel4CSIs offeredCamel4CSIs = new OfferedCamel4CSIsImpl(true, true, true, true, true, true, true);
        // routingInfo2
        ISDNAddressString isdnAdd = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        RoutingInfoImpl routingInfo2 = new RoutingInfoImpl(isdnAdd);
        // ssList2
        ArrayList<SSCode> ssList2 = new ArrayList<SSCode>();
        ssList2.add(new SSCodeImpl(SupplementaryCodeValue.allFacsimileTransmissionServices));
        // basicService2
        ExtBasicServiceCodeImpl basicService2 = new ExtBasicServiceCodeImpl(b);
        // allowedServices
        AllowedServices allowedServices = new AllowedServicesImpl(true, true);
        // unavailabilityCause
        UnavailabilityCause unavailabilityCause = UnavailabilityCause.busySubscriber;
        // releaseResourcesSupported
        boolean releaseResourcesSupported = true;
        // gsmBearerCapability
        SignalInfo signalInfo = new SignalInfoImpl(getSignalInfoData());
        ProtocolId protocolId = ProtocolId.gsm_0806;
        ExternalSignalInfo gsmBearerCapability = new ExternalSignalInfoImpl(signalInfo, protocolId, null);
        long mapProtocolVersion = 3;

        SendRoutingInformationResponseImpl prim = new SendRoutingInformationResponseImpl(mapProtocolVersion, imsi_,
                extRoutingInfo_, cugCheckInfo, cugSubscriptionFlag, subscriberInfo, ssList, basicService,
                forwardingInterrogationRequired, vmscAddress, extensionContainer, naeaPreferredCI, ccbsIndicators, msisdn,
                nrPortabilityStatus, istAlertTimer, supportedCamelPhases, offeredCamel4CSIs, routingInfo2, ssList2,
                basicService2, allowedServices, unavailabilityCause, releaseResourcesSupported, gsmBearerCapability);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        assertTrue(Arrays.equals(getMAPV3ParameterTestData(), asnOS.toByteArray()));

        // MAP V 2
        mapProtocolVersion = 2;

        prim = new SendRoutingInformationResponseImpl(mapProtocolVersion, imsi_, extRoutingInfo_, cugCheckInfo,
                cugSubscriptionFlag, subscriberInfo, ssList, basicService, forwardingInterrogationRequired, vmscAddress,
                extensionContainer, naeaPreferredCI, ccbsIndicators, msisdn, nrPortabilityStatus, istAlertTimer,
                supportedCamelPhases, offeredCamel4CSIs, routingInfo2, ssList2, basicService2, allowedServices,
                unavailabilityCause, releaseResourcesSupported, gsmBearerCapability);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        assertTrue(Arrays.equals(getMAPV3ParameterTestData2(), asnOS.toByteArray()));

        // MAP V 1
        mapProtocolVersion = 1;
        prim = new SendRoutingInformationResponseImpl(mapProtocolVersion, imsi_, extRoutingInfo_, cugCheckInfo,
                cugSubscriptionFlag, subscriberInfo, ssList, basicService, forwardingInterrogationRequired, vmscAddress,
                extensionContainer, naeaPreferredCI, ccbsIndicators, msisdn, nrPortabilityStatus, istAlertTimer,
                supportedCamelPhases, offeredCamel4CSIs, routingInfo2, ssList2, basicService2, allowedServices,
                unavailabilityCause, releaseResourcesSupported, gsmBearerCapability);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        assertTrue(Arrays.equals(getMAPV3ParameterTestData3(), asnOS.toByteArray()));

    }

    @Test(groups = { "functional.serialize", "service.callhandling" })
    public void testSerialization() throws Exception {

    }
}
