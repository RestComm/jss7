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
import static org.testng.Assert.assertNull;
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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CallTypeCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DPAnalysedInfoCriterium;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultSMSHandling;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.DestinationNumberCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtTeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MMCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MTSMSTPDUType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MTsmsCAMELTDPCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.MatchType;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCAMELTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SSCamelData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTdpCriteria;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TCSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class VlrCamelSubscriptionInfoTest {

    public byte[] getData() {
        return new byte[] { 48, -126, 2, -56, -96, 23, 48, 18, 48, 16, 10, 1, 4, 2, 1, 3, -128, 5, -111, 17, 34, 51, -13, -127,
                1, 1, -128, 1, 2, -95, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11,
                6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 97, 48, 52, 48, 3, 4, 1, 96, 4, 4, -111, 34,
                50, -11, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42,
                3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15,
                48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 0, -92, 92,
                48, 90, 10, 1, 2, -96, 28, -128, 1, 1, -95, 12, 4, 4, -111, 34, 50, -12, 4, 4, -111, 34, 50, -11, -94, 9, 2, 1,
                2, 2, 1, 4, 2, 1, 1, -95, 6, -126, 1, 22, -125, 1, 0, -126, 1, 0, -93, 3, 4, 1, 7, -92, 39, -96, 32, 48, 10, 6,
                3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33, -91, 60, 48, 6, 4, 1, -125, 4, 1, 2, 2, 1, 3, -128, 4, -111, 34, 50, -11, -95, 39, -96, 32, 48, 10,
                6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95,
                3, 31, 32, 33, -125, 0, -90, 106, -96, 58, 48, 56, -128, 1, 1, -127, 1, 3, -126, 4, -111, 34, 50, -11, -125, 1,
                0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
                21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 8, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13,
                14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -124, 0,
                -89, 22, 48, 17, 48, 15, 10, 1, 12, 2, 1, 3, -128, 4, -111, 34, 50, -11, -127, 1, 1, -128, 1, 2, -88, 21, 48,
                19, 10, 1, 13, -96, 6, -126, 1, 22, -125, 1, 0, -95, 6, 4, 1, 7, 4, 1, 6, -87, 111, -96, 61, 48, 59, 4, 4,
                -111, 34, 50, -12, 2, 1, 7, 4, 4, -111, 34, 50, -11, 2, 1, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12,
                13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1,
                2, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
                21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 0, -86, 106, -96, 58, 48, 56, -128, 1, 1, -127, 1,
                3, -126, 4, -111, 34, 50, -11, -125, 1, 0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5,
                6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -127, 1, 8, -94, 39, -96,
                32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
                25, 26, -95, 3, 31, 32, 33, -124, 0, -85, 13, 48, 11, 10, 1, 1, -96, 6, 10, 1, 0, 10, 1, 2 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        VlrCamelSubscriptionInfoImpl prim = new VlrCamelSubscriptionInfoImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        // oCsi
        OCSI oCsi = prim.getOCsi();
        ArrayList<OBcsmCamelTDPData> lst = oCsi.getOBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        OBcsmCamelTDPData cd = lst.get(0);
        assertEquals(cd.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.routeSelectFailure);
        assertEquals(cd.getServiceKey(), 3);
        assertEquals(cd.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(cd.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(cd.getGsmSCFAddress().getAddress().equals("1122333"));
        assertEquals(cd.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(cd.getExtensionContainer());

        assertNull(oCsi.getExtensionContainer());
        assertEquals((int) oCsi.getCamelCapabilityHandling(), 2);
        assertFalse(oCsi.getNotificationToCSE());
        assertFalse(oCsi.getCsiActive());

        // extensionContainer
        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

        // ssCsi
        SSCSI ssCsi = prim.getSsCsi();
        SSCamelData ssCamelData = ssCsi.getSsCamelData();

        ArrayList<SSCode> ssEventList = ssCamelData.getSsEventList();
        assertNotNull(ssEventList);
        assertEquals(ssEventList.size(), 1);
        SSCode one = ssEventList.get(0);
        assertNotNull(one);
        assertEquals(one.getSupplementaryCodeValue(), SupplementaryCodeValue.allFacsimileTransmissionServices);
        ISDNAddressString gsmSCFAddress = ssCamelData.getGsmSCFAddress();
        assertTrue(gsmSCFAddress.getAddress().equals("22235"));
        assertEquals(gsmSCFAddress.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddress.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(ssCamelData.getExtensionContainer());
        assertNotNull(ssCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ssCsi.getExtensionContainer()));
        assertTrue(ssCsi.getCsiActive());
        assertTrue(!ssCsi.getNotificationToCSE());

        // oBcsmCamelTDPCriteriaList
        ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = prim.getOBcsmCamelTDPCriteriaList();
        assertNotNull(oBcsmCamelTDPCriteriaList);
        assertEquals(oBcsmCamelTDPCriteriaList.size(), 1);
        OBcsmCamelTdpCriteria oBcsmCamelTdpCriteria = oBcsmCamelTDPCriteriaList.get(0);
        assertNotNull(oBcsmCamelTdpCriteria);

        DestinationNumberCriteria destinationNumberCriteria = oBcsmCamelTdpCriteria.getDestinationNumberCriteria();
        ArrayList<ISDNAddressString> destinationNumberList = destinationNumberCriteria.getDestinationNumberList();
        assertNotNull(destinationNumberList);
        assertEquals(destinationNumberList.size(), 2);
        ISDNAddressString destinationNumberOne = destinationNumberList.get(0);
        assertNotNull(destinationNumberOne);
        assertTrue(destinationNumberOne.getAddress().equals("22234"));
        assertEquals(destinationNumberOne.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberOne.getNumberingPlan(), NumberingPlan.ISDN);
        ISDNAddressString destinationNumberTwo = destinationNumberList.get(1);
        assertNotNull(destinationNumberTwo);
        assertTrue(destinationNumberTwo.getAddress().equals("22235"));
        assertEquals(destinationNumberTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(destinationNumberTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(destinationNumberCriteria.getMatchType().getCode(), MatchType.enabling.getCode());
        ArrayList<Integer> destinationNumberLengthList = destinationNumberCriteria.getDestinationNumberLengthList();
        assertNotNull(destinationNumberLengthList);
        assertEquals(destinationNumberLengthList.size(), 3);
        assertEquals(destinationNumberLengthList.get(0).intValue(), 2);
        assertEquals(destinationNumberLengthList.get(1).intValue(), 4);
        assertEquals(destinationNumberLengthList.get(2).intValue(), 1);
        assertEquals(oBcsmCamelTdpCriteria.getOBcsmTriggerDetectionPoint(), OBcsmTriggerDetectionPoint.collectedInfo);
        assertNotNull(oBcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(oBcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        ExtBasicServiceCode basicServiceOne = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(0);
        assertNotNull(basicServiceOne);
        assertEquals(basicServiceOne.getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.Asynchronous9_6kbps);

        ExtBasicServiceCode basicServiceTwo = oBcsmCamelTdpCriteria.getBasicServiceCriteria().get(1);
        assertNotNull(basicServiceTwo);
        assertEquals(basicServiceTwo.getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.allServices);

        assertEquals(oBcsmCamelTdpCriteria.getCallTypeCriteria(), CallTypeCriteria.forwarded);
        ArrayList<CauseValue> oCauseValueCriteria = oBcsmCamelTdpCriteria.getOCauseValueCriteria();
        assertNotNull(oCauseValueCriteria);
        assertEquals(oCauseValueCriteria.size(), 1);
        assertNotNull(oCauseValueCriteria.get(0));
        assertEquals(oCauseValueCriteria.get(0).getData(), 7);

        // TifCsi
        assertFalse(prim.getTifCsi());

        // mCsi
        MCSI mCsi = prim.getMCsi();
        ArrayList<MMCode> mobilityTriggers = mCsi.getMobilityTriggers();
        assertNotNull(mobilityTriggers);
        assertEquals(mobilityTriggers.size(), 2);
        MMCode mmCode = mobilityTriggers.get(0);
        assertNotNull(mmCode);
        assertEquals(MMCodeValue.GPRSAttach, mmCode.getMMCodeValue());
        MMCode mmCode2 = mobilityTriggers.get(1);
        assertNotNull(mmCode2);
        assertEquals(MMCodeValue.IMSIAttach, mmCode2.getMMCodeValue());
        assertNotNull(mCsi.getServiceKey());
        assertEquals(mCsi.getServiceKey(), 3);
        ISDNAddressString gsmSCFAddressTwo = mCsi.getGsmSCFAddress();
        assertTrue(gsmSCFAddressTwo.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressTwo.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressTwo.getNumberingPlan(), NumberingPlan.ISDN);
        assertNotNull(mCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mCsi.getExtensionContainer()));
        assertTrue(mCsi.getCsiActive());
        assertTrue(!mCsi.getNotificationToCSE());

        // smsCsi
        SMSCSI smsCsi = prim.getSmsCsi();
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = smsCsi.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataList);
        assertEquals(smsCamelTdpDataList.size(), 1);
        SMSCAMELTDPData smsCAMELTDPData = smsCamelTdpDataList.get(0);
        assertNotNull(smsCAMELTDPData);
        assertEquals(smsCAMELTDPData.getServiceKey(), 3);
        assertEquals(smsCAMELTDPData.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressString gsmSCFAddressSmsCAMELTDPData = smsCAMELTDPData.getGsmSCFAddress();
        assertTrue(gsmSCFAddressSmsCAMELTDPData.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressSmsCAMELTDPData.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPData.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPData.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPData.getExtensionContainer()));
        assertTrue(smsCsi.getCsiActive());
        assertTrue(!smsCsi.getNotificationToCSE());
        assertEquals(smsCsi.getCamelCapabilityHandling().intValue(), 8);

        // vtCsi
        TCSI vtCsi = prim.getVtCsi();
        ArrayList<TBcsmCamelTDPData> tbcsmCamelTDPDatalst = vtCsi.getTBcsmCamelTDPDataList();
        assertEquals(lst.size(), 1);
        TBcsmCamelTDPData tbcsmCamelTDPData = tbcsmCamelTDPDatalst.get(0);
        assertEquals(tbcsmCamelTDPData.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.termAttemptAuthorized);
        assertEquals(tbcsmCamelTDPData.getServiceKey(), 3);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(tbcsmCamelTDPData.getGsmSCFAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(tbcsmCamelTDPData.getGsmSCFAddress().getAddress().equals("22235"));
        assertEquals(tbcsmCamelTDPData.getDefaultCallHandling(), DefaultCallHandling.releaseCall);
        assertNull(tbcsmCamelTDPData.getExtensionContainer());
        assertNull(vtCsi.getExtensionContainer());
        assertEquals((int) vtCsi.getCamelCapabilityHandling(), 2);
        assertFalse(vtCsi.getNotificationToCSE());
        assertFalse(vtCsi.getCsiActive());

        // tBcsmCamelTdpCriteriaList
        ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList = prim.getTBcsmCamelTdpCriteriaList();
        assertNotNull(tBcsmCamelTdpCriteriaList);
        assertEquals(tBcsmCamelTdpCriteriaList.size(), 1);
        assertNotNull(tBcsmCamelTdpCriteriaList.get(0));
        TBcsmCamelTdpCriteria tbcsmCamelTdpCriteria = tBcsmCamelTdpCriteriaList.get(0);
        assertEquals(tbcsmCamelTdpCriteria.getTBcsmTriggerDetectionPoint(), TBcsmTriggerDetectionPoint.tBusy);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria());
        assertEquals(tbcsmCamelTdpCriteria.getBasicServiceCriteria().size(), 2);
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(0));
        assertNotNull(tbcsmCamelTdpCriteria.getBasicServiceCriteria().get(1));
        ArrayList<CauseValue> oCauseValueCriteriaLst = tbcsmCamelTdpCriteria.getTCauseValueCriteria();
        assertNotNull(oCauseValueCriteriaLst);
        assertEquals(oCauseValueCriteriaLst.size(), 2);
        assertNotNull(oCauseValueCriteriaLst.get(0));
        assertEquals(oCauseValueCriteriaLst.get(0).getData(), 7);
        assertNotNull(oCauseValueCriteriaLst.get(1));
        assertEquals(oCauseValueCriteriaLst.get(1).getData(), 6);

        // dCsi
        DCSI dCsi = prim.getDCsi();
        ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = dCsi.getDPAnalysedInfoCriteriaList();
        assertNotNull(dpAnalysedInfoCriteriaList);
        assertEquals(dpAnalysedInfoCriteriaList.size(), 1);
        DPAnalysedInfoCriterium dpAnalysedInfoCriterium = dpAnalysedInfoCriteriaList.get(0);
        assertNotNull(dpAnalysedInfoCriterium);
        ISDNAddressString dialledNumber = dpAnalysedInfoCriterium.getDialledNumber();
        assertTrue(dialledNumber.getAddress().equals("22234"));
        assertEquals(dialledNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(dialledNumber.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getServiceKey(), 7);
        ISDNAddressString gsmSCFAddressDp = dpAnalysedInfoCriterium.getGsmSCFAddress();
        assertTrue(gsmSCFAddressDp.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressDp.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressDp.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(dpAnalysedInfoCriterium.getDefaultCallHandling(), DefaultCallHandling.continueCall);
        assertNotNull(dCsi.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(dCsi.getExtensionContainer()));
        assertEquals(dCsi.getCamelCapabilityHandling().intValue(), 2);
        assertTrue(dCsi.getCsiActive());
        assertTrue(dCsi.getNotificationToCSE());

        // mtSmsCSI
        SMSCSI mtSmsCSI = prim.getMtSmsCSI();
        ArrayList<SMSCAMELTDPData> smsCamelTdpDataListOfmtSmsCSI = mtSmsCSI.getSmsCamelTdpDataList();
        assertNotNull(smsCamelTdpDataListOfmtSmsCSI);
        assertEquals(smsCamelTdpDataListOfmtSmsCSI.size(), 1);
        SMSCAMELTDPData smsCAMELTDPDataOfMtSmsCSI = smsCamelTdpDataListOfmtSmsCSI.get(0);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getServiceKey(), 3);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);
        ISDNAddressString gsmSCFAddressOfMtSmsCSI = smsCAMELTDPDataOfMtSmsCSI.getGsmSCFAddress();
        assertTrue(gsmSCFAddressOfMtSmsCSI.getAddress().equals("22235"));
        assertEquals(gsmSCFAddressOfMtSmsCSI.getAddressNature(), AddressNature.international_number);
        assertEquals(gsmSCFAddressOfMtSmsCSI.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(smsCAMELTDPDataOfMtSmsCSI.getDefaultSMSHandling(), DefaultSMSHandling.continueTransaction);
        assertNotNull(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(smsCAMELTDPDataOfMtSmsCSI.getExtensionContainer()));
        assertNotNull(mtSmsCSI.getExtensionContainer());
        assertTrue(mtSmsCSI.getCsiActive());
        assertTrue(!mtSmsCSI.getNotificationToCSE());
        assertEquals(mtSmsCSI.getCamelCapabilityHandling().intValue(), 8);

        // mtSmsCamelTdpCriteriaList
        ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList = prim.getMtSmsCamelTdpCriteriaList();
        assertNotNull(mtSmsCamelTdpCriteriaList);
        assertEquals(mtSmsCamelTdpCriteriaList.size(), 1);
        MTsmsCAMELTDPCriteria mtsmsCAMELTDPCriteria = mtSmsCamelTdpCriteriaList.get(0);

        ArrayList<MTSMSTPDUType> tPDUTypeCriterion = mtsmsCAMELTDPCriteria.getTPDUTypeCriterion();
        assertNotNull(tPDUTypeCriterion);
        assertEquals(tPDUTypeCriterion.size(), 2);
        MTSMSTPDUType mtSMSTPDUTypeOne = tPDUTypeCriterion.get(0);
        assertNotNull(mtSMSTPDUTypeOne);
        assertEquals(mtSMSTPDUTypeOne, MTSMSTPDUType.smsDELIVER);

        MTSMSTPDUType mtSMSTPDUTypeTwo = tPDUTypeCriterion.get(1);
        assertNotNull(mtSMSTPDUTypeTwo);
        assertTrue(mtSMSTPDUTypeTwo == MTSMSTPDUType.smsSTATUSREPORT);
        assertEquals(mtsmsCAMELTDPCriteria.getSMSTriggerDetectionPoint(), SMSTriggerDetectionPoint.smsCollectedInfo);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint = TBcsmTriggerDetectionPoint.tBusy;
        ArrayList<ExtBasicServiceCode> basicServiceCriteria = new ArrayList<ExtBasicServiceCode>();
        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.Asynchronous9_6kbps);
        ExtTeleserviceCode extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.allServices);
        ExtBasicServiceCodeImpl basicServiceOne = new ExtBasicServiceCodeImpl(b);
        ExtBasicServiceCodeImpl basicServiceTwo = new ExtBasicServiceCodeImpl(extTeleservice);
        basicServiceCriteria.add(basicServiceOne);
        basicServiceCriteria.add(basicServiceTwo);

        ArrayList<CauseValue> tCauseValueCriteria = new ArrayList<CauseValue>();
        tCauseValueCriteria.add(new CauseValueImpl(7));
        tCauseValueCriteria.add(new CauseValueImpl(6));

        ISDNAddressStringImpl gsmSCFAddressOne = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "1122333");
        OBcsmCamelTDPDataImpl cind = new OBcsmCamelTDPDataImpl(OBcsmTriggerDetectionPoint.routeSelectFailure, 3,
                gsmSCFAddressOne, DefaultCallHandling.releaseCall, null);
        ArrayList<OBcsmCamelTDPData> lst = new ArrayList<OBcsmCamelTDPData>();
        lst.add(cind);

        OCSI oCsi = new OCSIImpl(lst, null, 2, false, false);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ArrayList<SSCode> ssEventList = new ArrayList<SSCode>();
        ssEventList.add(new SSCodeImpl(SupplementaryCodeValue.allFacsimileTransmissionServices.getCode()));
        ISDNAddressString gsmSCFAddressTwo = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");
        SSCamelData ssCamelData = new SSCamelDataImpl(ssEventList, gsmSCFAddressTwo, extensionContainer);
        boolean notificationToCSE = false;
        boolean csiActive = true;

        SSCSI ssCsi = new SSCSIImpl(ssCamelData, extensionContainer, notificationToCSE, csiActive);

        ArrayList<OBcsmCamelTdpCriteria> oBcsmCamelTDPCriteriaList = new ArrayList<OBcsmCamelTdpCriteria>();
        OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint = OBcsmTriggerDetectionPoint.collectedInfo;
        ISDNAddressStringImpl destinationNumberOne = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22234");
        ISDNAddressStringImpl destinationNumberTwo = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22235");
        ArrayList<ISDNAddressString> destinationNumberList = new ArrayList<ISDNAddressString>();
        destinationNumberList.add(destinationNumberOne);
        destinationNumberList.add(destinationNumberTwo);
        ArrayList<Integer> destinationNumberLengthList = new ArrayList<Integer>();
        destinationNumberLengthList.add(new Integer(2));
        destinationNumberLengthList.add(new Integer(4));
        destinationNumberLengthList.add(new Integer(1));
        DestinationNumberCriteria destinationNumberCriteria = new DestinationNumberCriteriaImpl(MatchType.enabling,
                destinationNumberList, destinationNumberLengthList);

        CallTypeCriteria callTypeCriteria = CallTypeCriteria.forwarded;
        ArrayList<CauseValue> oCauseValueCriteria = new ArrayList<CauseValue>();
        oCauseValueCriteria.add(new CauseValueImpl(7));

        OBcsmCamelTdpCriteriaImpl oBcsmCamelTdpCriteria = new OBcsmCamelTdpCriteriaImpl(oBcsmTriggerDetectionPoint,
                destinationNumberCriteria, basicServiceCriteria, callTypeCriteria, oCauseValueCriteria, extensionContainer);
        oBcsmCamelTDPCriteriaList.add(oBcsmCamelTdpCriteria);

        boolean tifCsi = false;

        ArrayList<MMCode> mobilityTriggers = new ArrayList<MMCode>();
        Long serviceKey = new Long(3);
        ISDNAddressString gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");
        ;
        mobilityTriggers.add(new MMCodeImpl(MMCodeValue.GPRSAttach));
        mobilityTriggers.add(new MMCodeImpl(MMCodeValue.IMSIAttach));

        MCSI mCsi = new MCSIImpl(mobilityTriggers, serviceKey, gsmSCFAddress, extensionContainer, notificationToCSE, csiActive);

        SMSTriggerDetectionPoint smsTriggerDetectionPoint = SMSTriggerDetectionPoint.smsCollectedInfo;
        DefaultSMSHandling defaultSMSHandling = DefaultSMSHandling.continueTransaction;

        ArrayList<SMSCAMELTDPData> smsCamelTdpDataList = new ArrayList<SMSCAMELTDPData>();
        SMSCAMELTDPDataImpl smsCAMELTDPData = new SMSCAMELTDPDataImpl(smsTriggerDetectionPoint, serviceKey, gsmSCFAddress,
                defaultSMSHandling, extensionContainer);
        smsCamelTdpDataList.add(smsCAMELTDPData);

        Integer camelCapabilityHandling = new Integer(8);

        SMSCSI smsCsi = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE,
                csiActive);

        TBcsmCamelTDPDataImpl tBcsmCamelTDPData = new TBcsmCamelTDPDataImpl(TBcsmTriggerDetectionPoint.termAttemptAuthorized,
                3, gsmSCFAddress, DefaultCallHandling.releaseCall, null);
        ArrayList<TBcsmCamelTDPData> tBcsmCamelTDPDatalst = new ArrayList<TBcsmCamelTDPData>();
        tBcsmCamelTDPDatalst.add(tBcsmCamelTDPData);
        TCSI vtCsi = new TCSIImpl(tBcsmCamelTDPDatalst, null, 2, false, false);

        TBcsmCamelTdpCriteriaImpl tBcsmCamelTdpCriteria = new TBcsmCamelTdpCriteriaImpl(tBcsmTriggerDetectionPoint,
                basicServiceCriteria, tCauseValueCriteria);
        ArrayList<TBcsmCamelTdpCriteria> tBcsmCamelTdpCriteriaList = new ArrayList<TBcsmCamelTdpCriteria>();
        tBcsmCamelTdpCriteriaList.add(tBcsmCamelTdpCriteria);

        ISDNAddressStringImpl dialledNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22234");

        DPAnalysedInfoCriteriumImpl dpAnalysedInfoCriterium = new DPAnalysedInfoCriteriumImpl(dialledNumber, 7, gsmSCFAddress,
                DefaultCallHandling.continueCall, extensionContainer);

        ArrayList<DPAnalysedInfoCriterium> dpAnalysedInfoCriteriaList = new ArrayList<DPAnalysedInfoCriterium>();
        dpAnalysedInfoCriteriaList.add(dpAnalysedInfoCriterium);

        DCSI dCsi = new DCSIImpl(dpAnalysedInfoCriteriaList, 2, extensionContainer, true, true);

        SMSCSI mtSmsCSI = new SMSCSIImpl(smsCamelTdpDataList, camelCapabilityHandling, extensionContainer, notificationToCSE,
                csiActive);
        ArrayList<MTsmsCAMELTDPCriteria> mtSmsCamelTdpCriteriaList = new ArrayList<MTsmsCAMELTDPCriteria>();
        ArrayList<MTSMSTPDUType> tPDUTypeCriterion = new ArrayList<MTSMSTPDUType>();
        tPDUTypeCriterion.add(MTSMSTPDUType.smsDELIVER);
        tPDUTypeCriterion.add(MTSMSTPDUType.smsSTATUSREPORT);

        MTsmsCAMELTDPCriteriaImpl mTsmsCAMELTDPCriteria = new MTsmsCAMELTDPCriteriaImpl(smsTriggerDetectionPoint,
                tPDUTypeCriterion);
        mtSmsCamelTdpCriteriaList.add(mTsmsCAMELTDPCriteria);

        VlrCamelSubscriptionInfoImpl prim = new VlrCamelSubscriptionInfoImpl(oCsi, extensionContainer, ssCsi,
                oBcsmCamelTDPCriteriaList, tifCsi, mCsi, smsCsi, vtCsi, tBcsmCamelTdpCriteriaList, dCsi, mtSmsCSI,
                mtSmsCamelTdpCriteriaList);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }
}
