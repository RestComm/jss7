package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CallTypeCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValueCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DPAnalysedInfoCriterium;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultGPRSHandling;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultSMSHandling;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MGCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MTSMSTPDUType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCAMELTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCamelData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CauseValueImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.DCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.DPAnalysedInfoCriteriumImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtTeleserviceCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.GPRSCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.GPRSCamelTDPDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.MCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.MGCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.MMCodeImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteriaImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OBcsmCamelTDPDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OBcsmCamelTdpCriteriaImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SMSCAMELTDPDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SMSCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SSCSIImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SSCamelDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SpecificCSIWithdrawImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TBcsmCamelTDPDataImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TBcsmCamelTdpCriteriaImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TCSIImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 *
 * @author vadim subbotin
 *
 */
public class CAMELSubscriptionInfoTest {
    private byte[] data = {48, -127, -10, -96, 28, 48, 19, 48, 17, 10, 1, 2, 2, 1, 3, -128, 6, -111,
            33, 67, 101, -121, 9, -127, 1, 1, -128, 1, 4, -127, 0, -126, 0, -95, 8, 48, 6, 10, 1, 4,
            -126, 1, 0, -94, 33, -96, 24, 48, 22, 4, 6, -111, 33, 67, 101, -121, -7, 2, 1, 2, 4, 6,
            -111, 33, 67, 101, -121, 9, 2, 1, 0, -127, 1, 4, -125, 0, -124, 0, -93, 28, 48, 19, 48, 17,
            10, 1, 13, 2, 1, 5, -128, 6, -111, 33, 67, 101, -121, 9, -127, 1, 1, -128, 1, 4, -127, 0,
            -126, 0, -121, 0, -120, 0, -87, 24, -96, 19, 48, 17, -128, 1, 1, -127, 1, 6, -126, 6, -111,
            33, 67, 101, -121, 9, -125, 1, 0, -127, 1, 4, -86, 28, -96, 19, 48, 17, -128, 1, 2, -127, 1,
            7, -126, 6, -111, 33, 67, 101, -121, 9, -125, 1, 0, -127, 1, 4, -125, 0, -124, 0, -85, 15,
            48, 13, 48, 3, 4, 1, -128, 4, 6, -111, 33, 67, 101, -121, 9, -84, 16, 48, 3, 4, 1, -125, 2,
            1, 8, -128, 6, -111, 33, 67, 101, -121, 9, -83, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12,
            13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
            31, 32, 33, -114, 3, 2, -2, 0};

    private byte[] data1 = {48, -127, -8, -92, 15, 48, 13, 10, 1, 13, -96, 3, -125, 1, 32, -95, 3, 4, 1,
            83, -91, 28, 48, 19, 48, 17, 10, 1, 13, 2, 1, 5, -128, 6, -111, 33, 67, 101, -121, 9, -127,
            1, 1, -128, 1, 4, -127, 0, -126, 0, -90, 15, 48, 13, 10, 1, 13, -96, 3, -125, 1, 32, -95, 3,
            4, 1, 83, -81, 28, -96, 19, 48, 17, -128, 1, 2, -127, 1, 7, -126, 6, -111, 33, 67, 101, -121,
            9, -125, 1, 0, -127, 1, 4, -125, 0, -124, 0, -80, 10, 48, 8, 10, 1, 1, -96, 3, 10, 1, 0, -79,
            18, 48, 3, 4, 1, -125, 2, 1, 10, -128, 6, -111, 33, 67, 101, -121, 9, -125, 0, -78, 28, 48, 19,
            48, 17, 10, 1, 2, 2, 1, 3, -128, 6, -111, 33, 67, 101, -121, 9, -127, 1, 1, -128, 1, 4, -127, 0,
            -126, 0, -77, 8, 48, 6, 10, 1, 4, -126, 1, 0, -76, 33, -96, 24, 48, 22, 4, 6, -111, 33, 67, 101,
            -121, -7, 2, 1, 2, 4, 6, -111, 33, 67, 101, -121, 9, 2, 1, 0, -127, 1, 4, -125, 0, -124, 0, -75,
            28, 48, 19, 48, 17, 10, 1, 13, 2, 1, 5, -128, 6, -111, 33, 67, 101, -121, 9, -127, 1, 1, -128, 1,
            4, -127, 0, -126, 0, -74, 15, 48, 13, 10, 1, 13, -96, 3, -125, 1, 32, -95, 3, 4, 1, 83};

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode() throws Exception {
        AsnInputStream asn = new AsnInputStream(data);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        CAMELSubscriptionInfoImpl camelSubscriptionInfo = new CAMELSubscriptionInfoImpl();
        camelSubscriptionInfo.decodeAll(asn);

        // check o-CSI
        OCSI ocsi = camelSubscriptionInfo.getOCsi();
        assertNotNull(ocsi);
        assertNull(ocsi.getExtensionContainer());
        assertEquals(ocsi.getCamelCapabilityHandling().intValue(), 4);

        List<OBcsmCamelTDPData> oBcsmCamelTDPDataList = ocsi.getOBcsmCamelTDPDataList();
        assertNotNull(oBcsmCamelTDPDataList);
        assertEquals(oBcsmCamelTDPDataList.size(), 1);
        assertTrue(ocsi.getNotificationToCSE());
        assertTrue(ocsi.getCsiActive());

        OBcsmCamelTDPData oBcsmCamelTDPData = oBcsmCamelTDPDataList.get(0);
        ISDNAddressString gsmSCFAddress = oBcsmCamelTDPData.getGsmSCFAddress();
        assertEquals(oBcsmCamelTDPData.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertEquals(oBcsmCamelTDPData.getServiceKey(), 3);
        assertEquals(oBcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(oBcsmCamelTDPData.getExtensionContainer());
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gsmSCFAddress.getAddress(), "1234567890");

        // check o-BcsmCamelTDP-CriteriaList
        assertNotNull(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList());
        assertEquals(camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().size(), 1);

        OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = camelSubscriptionInfo.getOBcsmCamelTDPCriteriaList().get(0);
        assertEquals(oBcsmCamelTdpCriteria.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertNull(oBcsmCamelTdpCriteria.getDestinationNumberCriteria());
        assertNull(oBcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(oBcsmCamelTdpCriteria.getCallTypeCriteria(), CallTypeCriteria.forwarded);
        assertNull(oBcsmCamelTdpCriteria.getOCauseValueCriteria());
        assertNull(oBcsmCamelTdpCriteria.getExtensionContainer());

        // check d-CSI
        assertNotNull(camelSubscriptionInfo.getDCsi());

        DCSI dcsi = camelSubscriptionInfo.getDCsi();
        assertNotNull(dcsi.getDPAnalysedInfoCriteriaList());
        assertEquals(dcsi.getDPAnalysedInfoCriteriaList().size(), 1);
        assertEquals(dcsi.getCamelCapabilityHandling().intValue(), 4);
        assertNull(dcsi.getExtensionContainer());
        assertTrue(dcsi.getNotificationToCSE());
        assertTrue(dcsi.getCsiActive());

        DPAnalysedInfoCriterium dpAnalysedInfoCriterium = dcsi.getDPAnalysedInfoCriteriaList().get(0);
        ISDNAddressString dialedNumber = dpAnalysedInfoCriterium.getDialledNumber();
        gsmSCFAddress = dpAnalysedInfoCriterium.getGsmSCFAddress();
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 2);
        assertNull(dpAnalysedInfoCriterium.getExtensionContainer());
        assertEquals(dialedNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(dialedNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dialedNumber.getAddress(), "123456789");
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gsmSCFAddress.getAddress(), "1234567890");

        // check t-CSI
        assertNotNull(camelSubscriptionInfo.getTCsi());

        TCSI tcsi = camelSubscriptionInfo.getTCsi();
        assertNotNull(tcsi.getTBcsmCamelTDPDataList());
        assertEquals(tcsi.getTBcsmCamelTDPDataList().size(), 1);
        assertNull(tcsi.getExtensionContainer());
        assertEquals(tcsi.getCamelCapabilityHandling().intValue(), 4);
        assertTrue(tcsi.getNotificationToCSE());
        assertTrue(tcsi.getCsiActive());

        TBcsmCamelTDPData tBcsmCamelTDPData = tcsi.getTBcsmCamelTDPDataList().get(0);
        gsmSCFAddress = tBcsmCamelTDPData.getGsmSCFAddress();
        assertEquals(tBcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertEquals(tBcsmCamelTDPData.getServiceKey(), 5);
        assertEquals(tBcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(tBcsmCamelTDPData.getExtensionContainer());
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gsmSCFAddress.getAddress(), "1234567890");

        // check gprs-CSI
        assertNotNull(camelSubscriptionInfo.getGprsCsi());

        GPRSCSI gprscsi = camelSubscriptionInfo.getGprsCsi();
        assertNotNull(gprscsi.getGPRSCamelTDPDataList());
        assertEquals(gprscsi.getGPRSCamelTDPDataList().size(), 1);
        assertEquals(gprscsi.getCamelCapabilityHandling().intValue(), 4);
        assertNull(gprscsi.getExtensionContainer());
        assertFalse(gprscsi.getNotificationToCSE());
        assertFalse(gprscsi.getCsiActive());

        GPRSCamelTDPData gprsCamelTDPData = gprscsi.getGPRSCamelTDPDataList().get(0);
        gsmSCFAddress = gprsCamelTDPData.getGsmSCFAddress();
        assertEquals(gprsCamelTDPData.getGPRSTriggerDetectionPoint(), GPRSTriggerDetectionPoint.attach);
        assertEquals(gprsCamelTDPData.getServiceKey(), 6);
        assertEquals(gprsCamelTDPData.getDefaultSessionHandling(), DefaultGPRSHandling.continueTransaction);
        assertNull(gprsCamelTDPData.getExtensionContainer());
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gsmSCFAddress.getAddress(), "1234567890");

        // check mo-sms-CSI
        assertNotNull(camelSubscriptionInfo.getMoSmsCsi());

        SMSCSI smscsi = camelSubscriptionInfo.getMoSmsCsi();
        assertNotNull(smscsi.getSmsCamelTdpDataList());
        assertEquals(smscsi.getSmsCamelTdpDataList().size(), 1);
        assertEquals(smscsi.getCamelCapabilityHandling().intValue(), 4);
        assertNull(smscsi.getExtensionContainer());
        assertTrue(smscsi.getNotificationToCSE());
        assertTrue(smscsi.getCsiActive());

        SMSCAMELTDPData smsCamelTdpData = smscsi.getSmsCamelTdpDataList().get(0);
        gsmSCFAddress = smsCamelTdpData.getGsmSCFAddress();
        assertEquals(smsCamelTdpData.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsDeliveryRequest);
        assertEquals(smsCamelTdpData.getServiceKey(), 7);
        assertEquals(smsCamelTdpData.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNull(smsCamelTdpData.getExtensionContainer());
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gsmSCFAddress.getAddress(), "1234567890");

        // check ss-CSI
        assertNotNull(camelSubscriptionInfo.getSsCsi());

        SSCSI sscsi = camelSubscriptionInfo.getSsCsi();
        assertNotNull(sscsi.getSsCamelData());
        assertNull(sscsi.getExtensionContainer());
        assertFalse(sscsi.getNotificationToCSE());
        assertFalse(sscsi.getCsiActive());

        SSCamelData ssCamelData = sscsi.getSsCamelData();
        gsmSCFAddress = ssCamelData.getGsmSCFAddress();
        assertNotNull(ssCamelData.getSsEventList());
        assertEquals(ssCamelData.getSsEventList().size(), 1);
        assertNull(ssCamelData.getExtensionContainer());
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gsmSCFAddress.getAddress(), "1234567890");

        SSCode ssCode = ssCamelData.getSsEventList().get(0);
        assertEquals(ssCode.getSupplementaryCodeValue(), SupplementaryCodeValue.allAdditionalInfoTransferSS);

        // check m-CSI
        assertNotNull(camelSubscriptionInfo.getMCsi());

        MCSI mcsi = camelSubscriptionInfo.getMCsi();
        gsmSCFAddress = mcsi.getGsmSCFAddress();
        assertNotNull(mcsi.getMobilityTriggers());
        assertEquals(mcsi.getMobilityTriggers().size(), 1);
        assertEquals(mcsi.getServiceKey(), 8);
        assertNull(mcsi.getExtensionContainer());
        assertFalse(mcsi.getNotificationToCSE());
        assertFalse(mcsi.getCsiActive());
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(gsmSCFAddress.getAddress(), "1234567890");

        MMCode mmCode = mcsi.getMobilityTriggers().get(0);
        assertEquals(mmCode.getMMCodeValue(), MMCodeValue.GPRSAttach);

        // check extensionContainer
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(camelSubscriptionInfo.getExtensionContainer()));

        // check specificCSIDeletedList
        assertNotNull(camelSubscriptionInfo.getSpecificCSIDeletedList());

        SpecificCSIWithdraw specificCSIWithdraw = camelSubscriptionInfo.getSpecificCSIDeletedList();
        assertTrue(specificCSIWithdraw.getOCsi());
        assertTrue(specificCSIWithdraw.getSsCsi());
        assertTrue(specificCSIWithdraw.getTifCsi());
        assertTrue(specificCSIWithdraw.getDCsi());
        assertTrue(specificCSIWithdraw.getVtCsi());
        assertTrue(specificCSIWithdraw.getMoSmsCsi());
        assertTrue(specificCSIWithdraw.getMCsi());
        assertFalse(specificCSIWithdraw.getGprsCsi());
        assertFalse(specificCSIWithdraw.getTCsi());
        assertFalse(specificCSIWithdraw.getMtSmsCsi());
        assertFalse(specificCSIWithdraw.getMgCsi());
        assertFalse(specificCSIWithdraw.getOImCsi());
        assertFalse(specificCSIWithdraw.getDImCsi());
        assertFalse(specificCSIWithdraw.getVtImCsi());

        // other
        assertNull(camelSubscriptionInfo.getMtSmsCsi());
        assertNull(camelSubscriptionInfo.getMtSmsCamelTdpCriteriaList());
        assertNull(camelSubscriptionInfo.getMgCsi());
        assertNull(camelSubscriptionInfo.geToImCsi());
        assertNull(camelSubscriptionInfo.getOImBcsmCamelTdpCriteriaList());
        assertNull(camelSubscriptionInfo.getDImCsi());
        assertNull(camelSubscriptionInfo.getVtImCsi());
        assertNull(camelSubscriptionInfo.getVtImBcsmCamelTdpCriteriaList());
    }

    @Test(groups = {"functional.decode", "subscriberInformation"})
    public void testDecode1() throws Exception {
        AsnInputStream asn = new AsnInputStream(data1);

        int tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        CAMELSubscriptionInfoImpl camelSubscriptionInfo = new CAMELSubscriptionInfoImpl();
        camelSubscriptionInfo.decodeAll(asn);

        assertNotNull(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList());
        assertEquals(camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().size(), 1);
        assertNotNull(camelSubscriptionInfo.getVtBcsmCamelTdpCriteriaList());
        assertEquals(camelSubscriptionInfo.getVtBcsmCamelTdpCriteriaList().size(), 1);
        assertNotNull(camelSubscriptionInfo.getVtCsi());
        assertFalse(camelSubscriptionInfo.getTifCsi());
        assertFalse(camelSubscriptionInfo.getTifCsiNotificationToCSE());
        assertNotNull(camelSubscriptionInfo.getMtSmsCsi());
        assertNotNull(camelSubscriptionInfo.getMtSmsCamelTdpCriteriaList());
        assertNotNull(camelSubscriptionInfo.getMgCsi());
        assertNotNull(camelSubscriptionInfo.geToImCsi());
        assertNotNull(camelSubscriptionInfo.getOImBcsmCamelTdpCriteriaList());
        assertNotNull(camelSubscriptionInfo.getDImCsi());
        assertNotNull(camelSubscriptionInfo.getVtImCsi());
        assertNotNull(camelSubscriptionInfo.getVtImBcsmCamelTdpCriteriaList());

        TBcsmCamelTdpCriteria tBcsmCamelTdpCriteria = camelSubscriptionInfo.getTBcsmCamelTdpCriteriaList().get(0);
        assertEquals(tBcsmCamelTdpCriteria.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertNotNull(tBcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(tBcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 1);
        assertNotNull(tBcsmCamelTdpCriteria.getTCauseValueCriteria());
        assertEquals(tBcsmCamelTdpCriteria.getTCauseValueCriteria().size(), 1);
        ExtBasicServiceCode basicServiceCode = tBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0);
        assertEquals(basicServiceCode.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allShortMessageServices);
        CauseValue causeValue = tBcsmCamelTdpCriteria.getTCauseValueCriteria().get(0);
        assertEquals(causeValue.getCauseValueCodeValue(), CauseValueCodeValue.ASuspendedCallExists);

        TCSI tcsi = camelSubscriptionInfo.getVtCsi();
        assertNotNull(tcsi.getTBcsmCamelTDPDataList());
        assertEquals(tcsi.getTBcsmCamelTDPDataList().size(), 1);
        assertNull(tcsi.getExtensionContainer());
        assertEquals(tcsi.getCamelCapabilityHandling().intValue(), 4);
        assertTrue(tcsi.getNotificationToCSE());
        assertTrue(tcsi.getCsiActive());

        tBcsmCamelTdpCriteria = camelSubscriptionInfo.getVtBcsmCamelTdpCriteriaList().get(0);
        assertEquals(tBcsmCamelTdpCriteria.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertNotNull(tBcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(tBcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 1);
        assertNotNull(tBcsmCamelTdpCriteria.getTCauseValueCriteria());
        assertEquals(tBcsmCamelTdpCriteria.getTCauseValueCriteria().size(), 1);
        basicServiceCode = tBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0);
        assertEquals(basicServiceCode.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allShortMessageServices);
        causeValue = tBcsmCamelTdpCriteria.getTCauseValueCriteria().get(0);
        assertEquals(causeValue.getCauseValueCodeValue(), CauseValueCodeValue.ASuspendedCallExists);

        SMSCSI smscsi = camelSubscriptionInfo.getMtSmsCsi();
        assertNotNull(smscsi.getSmsCamelTdpDataList());
        assertEquals(smscsi.getSmsCamelTdpDataList().size(), 1);
        assertEquals(smscsi.getCamelCapabilityHandling().intValue(), 4);
        assertNull(smscsi.getExtensionContainer());
        assertTrue(smscsi.getNotificationToCSE());
        assertTrue(smscsi.getCsiActive());

        MTsmsCAMELTDPCriteria mTsmsCAMELTDPCriteria = camelSubscriptionInfo.getMtSmsCamelTdpCriteriaList().get(0);
        assertEquals(mTsmsCAMELTDPCriteria.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        assertNotNull(mTsmsCAMELTDPCriteria.getTPDUTypeCriterion());
        assertEquals(mTsmsCAMELTDPCriteria.getTPDUTypeCriterion().size(), 1);
        MTSMSTPDUType mtsmstpduType = mTsmsCAMELTDPCriteria.getTPDUTypeCriterion().get(0);
        assertEquals(mtsmstpduType, MTSMSTPDUType.smsDELIVER);

        MGCSI mgcsi = camelSubscriptionInfo.getMgCsi();
        assertNotNull(mgcsi.getMobilityTriggers());
        assertEquals(mgcsi.getMobilityTriggers().size(), 1);
        assertEquals(mgcsi.getServiceKey(), 10);
        assertEquals(mgcsi.getGsmSCFAddress().getAddress(), "1234567890");
        assertFalse(mgcsi.getNotificationToCSE());
        assertTrue(mgcsi.getCsiActive());
        MMCode mmCode = mgcsi.getMobilityTriggers().get(0);
        assertEquals(mmCode.getMMCodeValue(), MMCodeValue.GPRSAttach);

        OCSI ocsi = camelSubscriptionInfo.geToImCsi();
        assertNotNull(ocsi.getOBcsmCamelTDPDataList());
        assertEquals(ocsi.getOBcsmCamelTDPDataList().size(), 1);
        assertEquals(ocsi.getCamelCapabilityHandling().intValue(), 4);
        assertTrue(ocsi.getNotificationToCSE());
        assertTrue(ocsi.getCsiActive());
        OBcsmCamelTDPData oBcsmCamelTDPData = ocsi.getOBcsmCamelTDPDataList().get(0);
        assertEquals(oBcsmCamelTDPData.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertEquals(oBcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertEquals(oBcsmCamelTDPData.getServiceKey(), 3);
        assertEquals(oBcsmCamelTDPData.getGsmSCFAddress().getAddress(), "1234567890");

        OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = camelSubscriptionInfo.getOImBcsmCamelTdpCriteriaList().get(0);
        assertEquals(oBcsmCamelTdpCriteria.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertNull(oBcsmCamelTdpCriteria.getDestinationNumberCriteria());
        assertNull(oBcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(oBcsmCamelTdpCriteria.getCallTypeCriteria(), CallTypeCriteria.forwarded);
        assertNull(oBcsmCamelTdpCriteria.getOCauseValueCriteria());
        assertNull(oBcsmCamelTdpCriteria.getExtensionContainer());

        DCSI dcsi = camelSubscriptionInfo.getDImCsi();
        assertNotNull(dcsi.getDPAnalysedInfoCriteriaList());
        assertEquals(dcsi.getDPAnalysedInfoCriteriaList().size(), 1);
        assertEquals(dcsi.getCamelCapabilityHandling().intValue(), 4);
        assertNull(dcsi.getExtensionContainer());
        assertTrue(dcsi.getNotificationToCSE());
        assertTrue(dcsi.getCsiActive());

        tcsi = camelSubscriptionInfo.getVtImCsi();
        assertNotNull(tcsi.getTBcsmCamelTDPDataList());
        assertEquals(tcsi.getTBcsmCamelTDPDataList().size(), 1);
        assertNull(tcsi.getExtensionContainer());
        assertEquals(tcsi.getCamelCapabilityHandling().intValue(), 4);
        assertTrue(tcsi.getNotificationToCSE());
        assertTrue(tcsi.getCsiActive());

        tBcsmCamelTdpCriteria = camelSubscriptionInfo.getVtImBcsmCamelTdpCriteriaList().get(0);
        assertEquals(tBcsmCamelTdpCriteria.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertNotNull(tBcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(tBcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 1);
        assertNotNull(tBcsmCamelTdpCriteria.getTCauseValueCriteria());
        assertEquals(tBcsmCamelTdpCriteria.getTCauseValueCriteria().size(), 1);
        basicServiceCode = tBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0);
        assertEquals(basicServiceCode.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allShortMessageServices);
        causeValue = tBcsmCamelTdpCriteria.getTCauseValueCriteria().get(0);
        assertEquals(causeValue.getCauseValueCodeValue(), CauseValueCodeValue.ASuspendedCallExists);
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode() throws Exception {
        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1234567890");
        final OBcsmCamelTDPData oBcsmCamelTDPData = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.collectedInfo, 3, gsmSCFAddress,
                DefaultCallHandling.releaseCall, null);
        OCSIImpl ocsi = new OCSIImpl(new ArrayList<OBcsmCamelTDPData>(){{add(oBcsmCamelTDPData);}}, null, 4, true, true);

        final OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl(OBcsmTriggerDetectionPoint.routeSelectFailure, null, null,
                CallTypeCriteria.forwarded, null, null);

        ISDNAddressStringImpl dialedNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "123456789");
        final DPAnalysedInfoCriterium dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialedNumber, 2, gsmSCFAddress,
                DefaultCallHandling.continueCall, null);
        DCSIImpl dcsi = new DCSIImpl(new ArrayList<DPAnalysedInfoCriterium>(){{add(dpAnalysedInfoCriterium);}}, 4, null, true, true);

        final TBcsmCamelTDPData tBcsmCamelTDPData = new TBcsmCamelTDPDataImpl(TBcsmTriggerDetectionPoint.tBusy, 5, gsmSCFAddress,
                DefaultCallHandling.releaseCall, null);
        TCSIImpl tcsi = new TCSIImpl(new ArrayList<TBcsmCamelTDPData>(){{add(tBcsmCamelTDPData);}}, null, 4, true, true);

        final GPRSCamelTDPData gprsCamelTDPData = new GPRSCamelTDPDataImpl(GPRSTriggerDetectionPoint.attach, 6, gsmSCFAddress,
                DefaultGPRSHandling.continueTransaction, null);
        GPRSCSIImpl gprscsi = new GPRSCSIImpl(new ArrayList<GPRSCamelTDPData>(){{add(gprsCamelTDPData);}}, 4, null, false, false);

        final SMSCAMELTDPData smscameltdpData = new SMSCAMELTDPDataImpl(SMSTriggerDetectionPoint.smsDeliveryRequest, 7, gsmSCFAddress,
                DefaultSMSHandling.continueTransaction, null);
        SMSCSIImpl smscsi = new SMSCSIImpl(new ArrayList<SMSCAMELTDPData>(){{add(smscameltdpData);}}, 4, null, true, true);

        final SSCodeImpl ssCode = new SSCodeImpl(SupplementaryCodeValue.allAdditionalInfoTransferSS);
        SSCamelDataImpl ssCamelData = new SSCamelDataImpl(new ArrayList<SSCode>(){{add(ssCode);}}, gsmSCFAddress, null);
        SSCSIImpl sscsi = new SSCSIImpl(ssCamelData, null, false, false);

        final MMCodeImpl mmCode = new MMCodeImpl(MMCodeValue.GPRSAttach);
        MCSIImpl mcsi = new MCSIImpl(new ArrayList<MMCode>() {{add(mmCode);}}, 8, gsmSCFAddress, null, false, false);

        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        SpecificCSIWithdrawImpl specificCSIWithdraw = new SpecificCSIWithdrawImpl(true, true, true, true, true, true, true, false, false, false,
                false, false, false, false);

        CAMELSubscriptionInfoImpl camelSubscriptionInfo = new CAMELSubscriptionInfoImpl(ocsi, new ArrayList<OBcsmCamelTdpCriteria>(){{add(oBcsmCamelTdpCriteria);}},
                dcsi, tcsi, null, null, null, true, true, gprscsi, smscsi, sscsi, mcsi, extensionContainer, specificCSIWithdraw,
                null, null, null, null, null, null, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        camelSubscriptionInfo.encodeAll(asnOS);
        byte[] raw = asnOS.toByteArray();
        assertTrue(Arrays.equals(raw, data));
    }

    @Test(groups = {"functional.encode", "subscriberInformation"})
    public void testEncode1() throws Exception {
        final ExtBasicServiceCode basicServiceCode = new ExtBasicServiceCodeImpl(new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allShortMessageServices));
        final TBcsmCamelTdpCriteriaImpl tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(TBcsmTriggerDetectionPoint.tBusy,
                new ArrayList<ExtBasicServiceCode>(){{add(basicServiceCode);}},
                new ArrayList<CauseValue>(){{add(new CauseValueImpl(CauseValueCodeValue.ASuspendedCallExists));}});
        ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList = new ArrayList<TBcsmCamelTdpCriteria>() {{add(tBcsmCamelTdpCriteria);}};

        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1234567890");
        final TBcsmCamelTDPData tBcsmCamelTDPData = new TBcsmCamelTDPDataImpl(TBcsmTriggerDetectionPoint.tBusy, 5, gsmSCFAddress,
                DefaultCallHandling.releaseCall, null);
        TCSIImpl tcsi = new TCSIImpl(new ArrayList<TBcsmCamelTDPData>(){{add(tBcsmCamelTDPData);}}, null, 4, true, true);

        final SMSCAMELTDPData smscameltdpData = new SMSCAMELTDPDataImpl(SMSTriggerDetectionPoint.smsDeliveryRequest, 7, gsmSCFAddress,
                DefaultSMSHandling.continueTransaction, null);
        SMSCSIImpl smscsi = new SMSCSIImpl(new ArrayList<SMSCAMELTDPData>(){{add(smscameltdpData);}}, 4, null, true, true);

        final MTsmsCAMELTDPCriteria mTsmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl(SMSTriggerDetectionPoint.smsCollectedInfo,
                new ArrayList<MTSMSTPDUType>(){{add(MTSMSTPDUType.smsDELIVER);}});
        ArrayList<MTsmsCAMELTDPCriteria> mTsmsCAMELTDPCriteriaList = new ArrayList<MTsmsCAMELTDPCriteria>(){{add(mTsmsCAMELTDPCriteria);}};

        MGCSIImpl mgcsi = new MGCSIImpl(new ArrayList<MMCode>() {{add(new MMCodeImpl(MMCodeValue.GPRSAttach));}}, 10, gsmSCFAddress, null, false, true);

        final OBcsmCamelTDPData oBcsmCamelTDPData = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.collectedInfo, 3, gsmSCFAddress,
                DefaultCallHandling.releaseCall, null);
        OCSIImpl ocsi = new OCSIImpl(new ArrayList<OBcsmCamelTDPData>(){{add(oBcsmCamelTDPData);}}, null, 4, true, true);

        final OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl(OBcsmTriggerDetectionPoint.routeSelectFailure, null, null,
                CallTypeCriteria.forwarded, null, null);

        ISDNAddressStringImpl dialedNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "123456789");
        final DPAnalysedInfoCriterium dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialedNumber, 2, gsmSCFAddress,
                DefaultCallHandling.continueCall, null);
        DCSIImpl dcsi = new DCSIImpl(new ArrayList<DPAnalysedInfoCriterium>(){{add(dpAnalysedInfoCriterium);}}, 4, null, true, true);

        CAMELSubscriptionInfoImpl camelSubscriptionInfo = new CAMELSubscriptionInfoImpl(null, null, null, null, tBcsmCamelTdpCriteriaList, tcsi,
                tBcsmCamelTdpCriteriaList, false, false, null, null, null, null, null, null, smscsi, mTsmsCAMELTDPCriteriaList, mgcsi, ocsi,
                new ArrayList<OBcsmCamelTdpCriteria>(){{add(oBcsmCamelTdpCriteria);}}, dcsi, tcsi, tBcsmCamelTdpCriteriaList);

        AsnOutputStream asnOS = new AsnOutputStream();
        camelSubscriptionInfo.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(data1, encodedData));
    }
}
