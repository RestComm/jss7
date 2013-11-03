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

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingCategory;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ExtProtocolId;
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.ProtocolId;
import org.mobicents.protocols.ss7.map.api.primitives.SignalInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallDiversionTreatmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallDiversionTreatmentIndicatorValue;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CallReferenceNumber;
import org.mobicents.protocols.ss7.map.api.service.callhandling.CamelInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.mobicents.protocols.ss7.map.api.service.callhandling.SuppressMTSS;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.map.primitives.ExtExternalSignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.ExternalSignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.primitives.SignalInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CUGInterlockImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBearerServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
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
public class SendRoutingInformationRequestTest {
    Logger logger = Logger.getLogger(SendRoutingInformationRequestTest.class);

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

    public byte[] getData1() {
        return new byte[] { 48, -126, 1, 0, -128, 7, -111, -110, 17, 19, 50, 19, -15, -95, 49, 4, 4, 1, 2, 3, 4, 5, 0, 48, 39,
                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
                24, 25, 26, -95, 3, 31, 32, 33, -126, 1, 5, -125, 1, 1, -124, 0, -123, 1, 5, -122, 7, -111, -108, -120, 115, 0,
                -110, -14, -121, 5, 19, -6, 61, 61, -22, -120, 1, 2, -87, 3, -126, 1, 22, -86, 9, 10, 1, 2, 4, 4, 10, 20, 30,
                40, -85, 4, 3, 2, 4, -32, -116, 0, -83, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3,
                42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -114, 1, 7, -113, 0, -112, 1, 5,
                -79, 50, 10, 1, 1, 4, 4, 10, 20, 30, 40, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6,
                3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -110, 1, 1, -109, 0, -108, 1,
                2, -107, 0, -106, 0, -105, 0, -104, 0, -71, 3, -126, 1, 22, -70, 9, 10, 1, 2, 4, 4, 10, 20, 30, 40, -101, 2, 6,
                -64, -100, 0, -99, 1, 1 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 74, -128, 7, -111, -110, 17, 19, 50, 19, -15, -95, 49, 4, 4, 1, 2, 3, 4, 5, 0, 48, 39, -96, 32,
                48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33, -126, 1, 5, -86, 9, 10, 1, 2, 4, 4, 10, 20, 30, 40 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 23, -128, 7, -111, -110, 17, 19, 50, 19, -15, -126, 1, 5, -86, 9, 10, 1, 2, 4, 4, 10, 20, 30,
                40 };
    }

    private byte[] getGugData() {
        return new byte[] { 1, 2, 3, 4 };
    }

    public byte[] getCallReferenceNumberData() {
        return new byte[] { 19, -6, 61, 61, -22 };
    };

    public byte[] getSignalInfoData() {
        return new byte[] { 10, 20, 30, 40 };
    };

    private byte[] getExtBearerServiceData() {
        return new byte[] { 22 };
    }

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {

        // Test MAP V3 Params
        byte[] data = getData1();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        SendRoutingInformationRequestImpl prim = new SendRoutingInformationRequestImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ISDNAddressString msisdn = prim.getMsisdn();
        InterrogationType type = prim.getInterogationType();
        ISDNAddressString gmsc = prim.getGmscOrGsmSCFAddress();

        assertNotNull(msisdn);
        assertNotNull(type);
        assertNotNull(gmsc);
        assertTrue(msisdn.getAddressNature() == AddressNature.international_number);
        assertTrue(msisdn.getNumberingPlan() == NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "29113123311");
        assertTrue(gmsc.getAddressNature() == AddressNature.international_number);
        assertTrue(gmsc.getNumberingPlan() == NumberingPlan.ISDN);
        assertEquals(gmsc.getAddress(), "49883700292");
        assertEquals(type, InterrogationType.forwarding);

        // cugCheckInfo
        CUGCheckInfo cugCheckInfo = prim.getCUGCheckInfo();
        assertTrue(Arrays.equals(cugCheckInfo.getCUGInterlock().getData(), getGugData()));
        assertTrue(cugCheckInfo.getCUGOutgoingAccess());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(cugCheckInfo.getExtensionContainer()));

        // numberOfForwarding
        assertEquals(prim.getNumberOfForwarding().intValue(), 5);

        // ORInterrogation
        assertTrue(prim.getORInterrogation());

        // orCapability
        assertEquals(prim.getORCapability().intValue(), 5);

        // callReferenceNumber
        assertNotNull(prim.getCallReferenceNumber());

        // forwardingReason
        assertEquals(prim.getForwardingReason(), ForwardingReason.noReply);

        // basicServiceGroup
        assertTrue(Arrays.equals(prim.getBasicServiceGroup().getExtBearerService().getData(), this.getExtBearerServiceData()));
        assertNull(prim.getBasicServiceGroup().getExtTeleservice());

        // networkSignalInfo
        ProtocolId protocolId = prim.getNetworkSignalInfo().getProtocolId();
        byte[] signalInfo = prim.getNetworkSignalInfo().getSignalInfo().getData();
        assertTrue(Arrays.equals(getSignalInfoData(), signalInfo));
        assertNotNull(protocolId);
        assertEquals(protocolId, ProtocolId.gsm_0806);

        // camelInfo
        SupportedCamelPhases scf = prim.getCamelInfo().getSupportedCamelPhases();
        assertTrue(scf.getPhase1Supported());
        assertTrue(scf.getPhase2Supported());
        assertTrue(scf.getPhase3Supported());
        assertFalse(scf.getPhase4Supported());
        assertFalse(prim.getCamelInfo().getSuppressTCSI());
        assertNull(prim.getCamelInfo().getExtensionContainer());
        assertNull(prim.getCamelInfo().getOfferedCamel4CSIs());

        // suppressionOfAnnouncement
        assertTrue(prim.getSuppressionOfAnnouncement());

        // extensionContainer
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

        // alertingPattern
        assertNull(prim.getAlertingPattern().getAlertingLevel());
        assertNotNull(prim.getAlertingPattern().getAlertingCategory());
        assertEquals(prim.getAlertingPattern().getAlertingCategory(), AlertingCategory.Category4);

        // ccbsCall
        assertTrue(prim.getCCBSCall());

        // supportedCCBSPhase
        assertEquals(prim.getSupportedCCBSPhase().intValue(), 5);

        // additionalSignalInfo
        ExtExternalSignalInfo additionalSignalInfo = prim.getAdditionalSignalInfo();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(additionalSignalInfo.getExtensionContainer()));
        assertEquals(additionalSignalInfo.getExtProtocolId(), ExtProtocolId.ets_300356);
        additionalSignalInfo.getSignalInfo();
        byte[] signalInfoAdd = additionalSignalInfo.getSignalInfo().getData();
        assertTrue(Arrays.equals(getSignalInfoData(), signalInfoAdd));

        // istSupportIndicator
        assertEquals(prim.getIstSupportIndicator(), ISTSupportIndicator.istCommandSupported);

        // prePagingSupported
        assertTrue(prim.getPrePagingSupported());

        // callDiversionTreatmentIndicator
        assertEquals(prim.getCallDiversionTreatmentIndicator().getCallDiversionTreatmentIndicatorValue(),
                CallDiversionTreatmentIndicatorValue.callDiversionNotAllowed);

        assertTrue(prim.getLongFTNSupported());
        assertTrue(prim.getSuppressVtCSI());
        assertTrue(prim.getSuppressIncomingCallBarring());
        assertTrue(prim.getGsmSCFInitiatedCall());

        // basicServiceGroup2
        assertTrue(Arrays.equals(prim.getBasicServiceGroup2().getExtBearerService().getData(), this.getExtBearerServiceData()));
        assertNull(prim.getBasicServiceGroup2().getExtTeleservice());

        // networkSignalInfo2
        ProtocolId protocolId2 = prim.getNetworkSignalInfo2().getProtocolId();
        byte[] signalInfo2 = prim.getNetworkSignalInfo2().getSignalInfo().getData();
        assertTrue(Arrays.equals(getSignalInfoData(), signalInfo2));
        assertNotNull(protocolId2);
        assertEquals(protocolId2, ProtocolId.gsm_0806);

        // suppressMTSS
        SuppressMTSS suppressMTSS = prim.getSuppressMTSS();
        assertTrue(suppressMTSS.getSuppressCCBS());
        assertTrue(suppressMTSS.getSuppressCUG());

        // mtRoamingRetrySupported
        assertTrue(prim.getMTRoamingRetrySupported());

        // callPriority
        assertEquals(prim.getCallPriority(), EMLPPPriority.priorityLevel1);
        assertEquals(prim.getMapProtocolVersion(), 3);

        // Test MAP V 2 message paramenters
        data = getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new SendRoutingInformationRequestImpl(2);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        msisdn = prim.getMsisdn();
        assertNull(prim.getInterogationType());
        assertNull(prim.getGmscOrGsmSCFAddress());
        assertNotNull(msisdn);
        assertTrue(msisdn.getAddressNature() == AddressNature.international_number);
        assertTrue(msisdn.getNumberingPlan() == NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "29113123311");
        // cugCheckInfo
        cugCheckInfo = prim.getCUGCheckInfo();
        assertTrue(Arrays.equals(cugCheckInfo.getCUGInterlock().getData(), getGugData()));
        assertTrue(cugCheckInfo.getCUGOutgoingAccess());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(cugCheckInfo.getExtensionContainer()));
        // numberOfForwarding
        assertEquals(prim.getNumberOfForwarding().intValue(), 5);
        // ORInterrogation
        assertFalse(prim.getORInterrogation());
        // orCapability
        assertNull(prim.getORCapability());
        // callReferenceNumber
        assertNull(prim.getCallReferenceNumber());
        // forwardingReason
        assertNull(prim.getForwardingReason());
        // basicServiceGroup
        assertNull(prim.getBasicServiceGroup());
        // networkSignalInfo
        protocolId = prim.getNetworkSignalInfo().getProtocolId();
        signalInfo = prim.getNetworkSignalInfo().getSignalInfo().getData();
        assertTrue(Arrays.equals(getSignalInfoData(), signalInfo));
        assertNotNull(protocolId);
        assertEquals(protocolId, ProtocolId.gsm_0806);
        // camelInfo
        assertNull(prim.getCamelInfo());
        // suppressionOfAnnouncement
        assertFalse(prim.getSuppressionOfAnnouncement());
        // extensionContainer
        assertNull(prim.getExtensionContainer());
        // alertingPattern
        assertNull(prim.getAlertingPattern());
        // ccbsCall
        assertFalse(prim.getCCBSCall());
        // supportedCCBSPhase
        assertNull(prim.getSupportedCCBSPhase());
        // additionalSignalInfo
        assertNull(prim.getAdditionalSignalInfo());
        // istSupportIndicator
        assertNull(prim.getIstSupportIndicator());
        // prePagingSupported
        assertFalse(prim.getPrePagingSupported());
        // callDiversionTreatmentIndicator
        assertNull(prim.getCallDiversionTreatmentIndicator());
        assertFalse(prim.getLongFTNSupported());
        assertFalse(prim.getSuppressVtCSI());
        assertFalse(prim.getSuppressIncomingCallBarring());
        assertFalse(prim.getGsmSCFInitiatedCall());
        // basicServiceGroup2
        assertNull(prim.getBasicServiceGroup2());
        // networkSignalInfo2
        assertNull(prim.getNetworkSignalInfo2());
        // suppressMTSS
        assertNull(prim.getSuppressMTSS());
        // mtRoamingRetrySupported
        assertFalse(prim.getMTRoamingRetrySupported());
        // callPriority
        assertNull(prim.getCallPriority());
        assertEquals(prim.getMapProtocolVersion(), 2);

        // Test MAP V 1 message paramenters
        data = getData3();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new SendRoutingInformationRequestImpl(1);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
        msisdn = prim.getMsisdn();
        assertNull(prim.getInterogationType());
        assertNull(prim.getGmscOrGsmSCFAddress());
        assertNotNull(msisdn);
        assertTrue(msisdn.getAddressNature() == AddressNature.international_number);
        assertTrue(msisdn.getNumberingPlan() == NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "29113123311");
        // cugCheckInfo
        assertNull(prim.getCUGCheckInfo());
        // numberOfForwarding
        assertEquals(prim.getNumberOfForwarding().intValue(), 5);
        // ORInterrogation
        assertFalse(prim.getORInterrogation());
        // orCapability
        assertNull(prim.getORCapability());
        // callReferenceNumber
        assertNull(prim.getCallReferenceNumber());
        // forwardingReason
        assertNull(prim.getForwardingReason());
        // basicServiceGroup
        assertNull(prim.getBasicServiceGroup());
        // networkSignalInfo
        protocolId = prim.getNetworkSignalInfo().getProtocolId();
        signalInfo = prim.getNetworkSignalInfo().getSignalInfo().getData();
        assertTrue(Arrays.equals(getSignalInfoData(), signalInfo));
        assertNotNull(protocolId);
        assertEquals(protocolId, ProtocolId.gsm_0806);
        // camelInfo
        assertNull(prim.getCamelInfo());
        // suppressionOfAnnouncement
        assertFalse(prim.getSuppressionOfAnnouncement());
        // extensionContainer
        assertNull(prim.getExtensionContainer());
        // alertingPattern
        assertNull(prim.getAlertingPattern());
        // ccbsCall
        assertFalse(prim.getCCBSCall());
        // supportedCCBSPhase
        assertNull(prim.getSupportedCCBSPhase());
        // additionalSignalInfo
        assertNull(prim.getAdditionalSignalInfo());
        // istSupportIndicator
        assertNull(prim.getIstSupportIndicator());
        // prePagingSupported
        assertFalse(prim.getPrePagingSupported());
        // callDiversionTreatmentIndicator
        assertNull(prim.getCallDiversionTreatmentIndicator());
        assertFalse(prim.getLongFTNSupported());
        assertFalse(prim.getSuppressVtCSI());
        assertFalse(prim.getSuppressIncomingCallBarring());
        assertFalse(prim.getGsmSCFInitiatedCall());
        // basicServiceGroup2
        assertNull(prim.getBasicServiceGroup2());
        // networkSignalInfo2
        assertNull(prim.getNetworkSignalInfo2());
        // suppressMTSS
        assertNull(prim.getSuppressMTSS());
        // mtRoamingRetrySupported
        assertFalse(prim.getMTRoamingRetrySupported());
        // callPriority
        assertNull(prim.getCallPriority());
        assertEquals(prim.getMapProtocolVersion(), 1);

    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {
        // MAP V 3 Message Testing Starts
        // msisdn
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "29113123311");

        // cugCheckInfo
        CUGInterlock cugInterlock = new CUGInterlockImpl(getGugData());
        CUGCheckInfo cugCheckInfo = new CUGCheckInfoImpl(cugInterlock, true,
                MAPExtensionContainerTest.GetTestExtensionContainer());

        // numberOfForwarding
        Integer numberOfForwarding = new Integer(5);

        // interrogationType
        InterrogationType interrogationType = InterrogationType.forwarding;

        // orInterrogation
        boolean orInterrogation = true;

        // orCapability
        Integer orCapability = new Integer(5);

        // gmscAddress
        ISDNAddressString gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "49883700292");

        // callReferenceNumber
        CallReferenceNumber callReferenceNumber = new CallReferenceNumberImpl(getCallReferenceNumberData());

        // forwardingReason
        ForwardingReason forwardingReason = ForwardingReason.noReply;

        // basicServiceGroup
        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(this.getExtBearerServiceData());
        ExtBasicServiceCodeImpl basicServiceGroup = new ExtBasicServiceCodeImpl(b);

        // networkSignalInfo
        SignalInfo signalInfo = new SignalInfoImpl(getSignalInfoData());
        ProtocolId protocolId = ProtocolId.gsm_0806;
        ExternalSignalInfo networkSignalInfo = new ExternalSignalInfoImpl(signalInfo, protocolId, null);

        // camelInfo
        SupportedCamelPhases scf = new SupportedCamelPhasesImpl(true, true, true, false);
        CamelInfo camelInfo = new CamelInfoImpl(scf, false, null, null);

        // suppressionOfAnnouncement
        boolean suppressionOfAnnouncement = true;

        // extensionContainer
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        // alertingPattern
        AlertingPattern alertingPattern = new AlertingPatternImpl(AlertingCategory.Category4);

        // ccbsCall
        boolean ccbsCall = true;

        // supportedCCBSPhase
        Integer supportedCCBSPhase = new Integer(5);

        // additionalSignalInfo
        ExtExternalSignalInfo additionalSignalInfo = new ExtExternalSignalInfoImpl(signalInfo, ExtProtocolId.ets_300356,
                extensionContainer);

        // istSupportIndicator
        ISTSupportIndicator istSupportIndicator = ISTSupportIndicator.istCommandSupported;

        // prePagingSupported
        boolean prePagingSupported = true;

        // callDiversionTreatmentIndicator
        CallDiversionTreatmentIndicator callDiversionTreatmentIndicator = new CallDiversionTreatmentIndicatorImpl(
                CallDiversionTreatmentIndicatorValue.callDiversionNotAllowed);

        boolean longFTNSupported = true;
        boolean suppressVtCSI = true;
        boolean suppressIncomingCallBarring = true;
        boolean gsmSCFInitiatedCall = true;

        // basicServiceGroup2
        ExtBasicServiceCode basicServiceGroup2 = new ExtBasicServiceCodeImpl(b);

        // networkSignalInfo2
        ExternalSignalInfo networkSignalInfo2 = new ExternalSignalInfoImpl(signalInfo, protocolId, null);

        // suppressMTSS
        SuppressMTSS suppressMTSS = new SuppressMTSSImpl(true, true);

        boolean mtRoamingRetrySupported = true;

        EMLPPPriority callPriority = EMLPPPriority.priorityLevel1;
        long mapProtocolVersion = 3L;

        SendRoutingInformationRequestImpl prim = new SendRoutingInformationRequestImpl(mapProtocolVersion, msisdn,
                cugCheckInfo, numberOfForwarding, interrogationType, orInterrogation, orCapability, gmscAddress,
                callReferenceNumber, forwardingReason, basicServiceGroup, networkSignalInfo, camelInfo,
                suppressionOfAnnouncement, extensionContainer, alertingPattern, ccbsCall, supportedCCBSPhase,
                additionalSignalInfo, istSupportIndicator, prePagingSupported, callDiversionTreatmentIndicator,
                longFTNSupported, suppressVtCSI, suppressIncomingCallBarring, gsmSCFInitiatedCall, basicServiceGroup2,
                networkSignalInfo2, suppressMTSS, mtRoamingRetrySupported, callPriority);

        AsnOutputStream asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);
        assertTrue(Arrays.equals(getData1(), asnOS.toByteArray()));

        // MAP V2 message starts
        mapProtocolVersion = 2L;
        prim = new SendRoutingInformationRequestImpl(mapProtocolVersion, msisdn, cugCheckInfo, numberOfForwarding,
                interrogationType, orInterrogation, orCapability, gmscAddress, callReferenceNumber, forwardingReason,
                basicServiceGroup, networkSignalInfo, camelInfo, suppressionOfAnnouncement, extensionContainer,
                alertingPattern, ccbsCall, supportedCCBSPhase, additionalSignalInfo, istSupportIndicator, prePagingSupported,
                callDiversionTreatmentIndicator, longFTNSupported, suppressVtCSI, suppressIncomingCallBarring,
                gsmSCFInitiatedCall, basicServiceGroup2, networkSignalInfo2, suppressMTSS, mtRoamingRetrySupported,
                callPriority);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);
        assertTrue(Arrays.equals(getData2(), asnOS.toByteArray()));

        // MAP V1 message starts
        mapProtocolVersion = 1L;
        prim = new SendRoutingInformationRequestImpl(mapProtocolVersion, msisdn, cugCheckInfo, numberOfForwarding,
                interrogationType, orInterrogation, orCapability, gmscAddress, callReferenceNumber, forwardingReason,
                basicServiceGroup, networkSignalInfo, camelInfo, suppressionOfAnnouncement, extensionContainer,
                alertingPattern, ccbsCall, supportedCCBSPhase, additionalSignalInfo, istSupportIndicator, prePagingSupported,
                callDiversionTreatmentIndicator, longFTNSupported, suppressVtCSI, suppressIncomingCallBarring,
                gsmSCFInitiatedCall, basicServiceGroup2, networkSignalInfo2, suppressMTSS, mtRoamingRetrySupported,
                callPriority);

        asnOS = new AsnOutputStream();
        prim.encodeAll(asnOS);

        assertTrue(Arrays.equals(getData3(), asnOS.toByteArray()));
    }

    @Test(groups = { "functional.serialize", "service.callhandling" })
    public void testSerialization() throws Exception {

    }
}
