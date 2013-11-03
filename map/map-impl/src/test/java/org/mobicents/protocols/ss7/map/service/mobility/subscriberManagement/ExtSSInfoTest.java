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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.FTNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGSubscription;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.EMLPPInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptions;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.InterCUGRestrictions;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.IntraCUGOptions;
import org.mobicents.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSSubscriptionOption;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.SSSubscriptionOptionImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ExtSSInfoTest {

    public byte[] getData() {
        return new byte[] { -96, 117, 4, 1, 0, 48, 71, 48, 69, -126, 1, 22, -124, 1, 3, -123, 4, -111, 34, 34, -8, -120, 2, 2,
                5, -122, 1, -92, -121, 1, 2, -87, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -118, 4, -111, 34, 34, -9, -96, 39, -96,
                32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24,
                25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getData1() {
        return new byte[] { -95, 95, 4, 1, 0, 48, 49, 48, 47, -126, 1, 22, -124, 1, 3, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4,
                11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33,
                48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21,
                22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getData2() {
        return new byte[] { -94, -127, -99, 48, 60, 48, 58, 2, 1, 1, 4, 4, 1, 2, 3, 4, 10, 1, 0, 48, 3, -126, 1, 22, -96, 39,
                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
                24, 25, 26, -95, 3, 31, 32, 33, 48, 52, 48, 50, -126, 1, 22, 2, 1, 1, 4, 1, 0, 48, 39, -96, 32, 48, 10, 6, 3,
                42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3,
                31, 32, 33, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3,
                42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getData3() {
        return new byte[] { -93, 55, 4, 1, 0, -124, 1, 3, -126, 1, 0, 48, 3, -126, 1, 22, -91, 39, -96, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31,
                32, 33 };
    };

    public byte[] getData4() {
        return new byte[] { -92, 47, 2, 1, 2, 2, 1, 1, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6,
                3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    private byte[] getISDNSubaddressStringData() {
        return new byte[] { 2, 5 };
    }

    private byte[] getcugData() {
        return new byte[] { 1, 2, 3, 4 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        // option 1
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        ExtSSInfoImpl prim = new ExtSSInfoImpl();
        prim.decodeAll(asn);

        assertEquals(tag, ExtSSInfoImpl._TAG_forwardingInfo);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        ExtForwInfo forwardingInfo = prim.getForwardingInfo();
        ExtCallBarInfo callBarringInfo = prim.getCallBarringInfo();
        CUGInfo cugInfo = prim.getCugInfo();
        ExtSSData ssData = prim.getSsData();
        EMLPPInfo emlppInfo = prim.getEmlppInfo();

        assertNotNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNull(cugInfo);
        assertNull(ssData);
        assertNull(emlppInfo);

        MAPExtensionContainer extensionContainer = forwardingInfo.getExtensionContainer();
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
        assertEquals(forwardingInfo.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allServices);

        ArrayList<ExtForwFeature> forwardingFeatureList = forwardingInfo.getForwardingFeatureList();
        ;
        assertNotNull(forwardingFeatureList);
        assertEquals(forwardingFeatureList.size(), 1);
        ExtForwFeature extForwFeature = forwardingFeatureList.get(0);
        assertNotNull(extForwFeature);

        assertEquals(extForwFeature.getBasicService().getExtBearerService().getBearerServiceCodeValue(),
                BearerServiceCodeValue.Asynchronous9_6kbps);
        assertNull(extForwFeature.getBasicService().getExtTeleservice());
        assertNotNull(extForwFeature.getSsStatus());
        assertTrue(extForwFeature.getSsStatus().getBitA());
        assertTrue(!extForwFeature.getSsStatus().getBitP());
        assertTrue(!extForwFeature.getSsStatus().getBitQ());
        assertTrue(extForwFeature.getSsStatus().getBitR());

        ISDNAddressString forwardedToNumber = extForwFeature.getForwardedToNumber();
        assertNotNull(forwardedToNumber);
        assertTrue(forwardedToNumber.getAddress().equals("22228"));
        assertEquals(forwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(forwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(Arrays.equals(extForwFeature.getForwardedToSubaddress().getData(), this.getISDNSubaddressStringData()));
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToCallingParty());
        assertTrue(extForwFeature.getForwardingOptions().getNotificationToForwardingParty());
        assertTrue(!extForwFeature.getForwardingOptions().getRedirectingPresentation());
        assertEquals(extForwFeature.getForwardingOptions().getExtForwOptionsForwardingReason(),
                ExtForwOptionsForwardingReason.msBusy);
        assertNotNull(extForwFeature.getNoReplyConditionTime());
        assertEquals(extForwFeature.getNoReplyConditionTime().intValue(), 2);
        FTNAddressString longForwardedToNumber = extForwFeature.getLongForwardedToNumber();
        assertNotNull(longForwardedToNumber);
        assertTrue(longForwardedToNumber.getAddress().equals("22227"));
        assertEquals(longForwardedToNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(longForwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);

        // option 2
        data = this.getData1();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ExtSSInfoImpl();
        prim.decodeAll(asn);

        assertEquals(tag, ExtSSInfoImpl._TAG_callBarringInfo);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        forwardingInfo = prim.getForwardingInfo();
        callBarringInfo = prim.getCallBarringInfo();
        cugInfo = prim.getCugInfo();
        ssData = prim.getSsData();
        emlppInfo = prim.getEmlppInfo();

        assertNull(forwardingInfo);
        assertNotNull(callBarringInfo);
        assertNull(cugInfo);
        assertNull(ssData);
        assertNull(emlppInfo);

        extensionContainer = callBarringInfo.getExtensionContainer();
        assertEquals(callBarringInfo.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allServices);
        assertNotNull(callBarringInfo.getCallBarringFeatureList());
        assertEquals(callBarringInfo.getCallBarringFeatureList().size(), 1);
        assertNotNull(callBarringInfo.getCallBarringFeatureList().get(0));
        assertNotNull(extensionContainer);

        // option 3
        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ExtSSInfoImpl();
        prim.decodeAll(asn);

        assertEquals(tag, ExtSSInfoImpl._TAG_cugInfo);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        forwardingInfo = prim.getForwardingInfo();
        callBarringInfo = prim.getCallBarringInfo();
        cugInfo = prim.getCugInfo();
        ssData = prim.getSsData();
        emlppInfo = prim.getEmlppInfo();

        assertNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNotNull(cugInfo);
        assertNull(ssData);
        assertNull(emlppInfo);

        extensionContainer = cugInfo.getExtensionContainer();
        assertNotNull(cugInfo.getCUGSubscriptionList());
        assertEquals(cugInfo.getCUGSubscriptionList().size(), 1);
        assertNotNull(cugInfo.getCUGSubscriptionList().get(0));
        assertNotNull(cugInfo.getCUGFeatureList());
        assertEquals(cugInfo.getCUGFeatureList().size(), 1);
        assertNotNull(cugInfo.getCUGFeatureList().get(0));
        assertNotNull(extensionContainer);

        // option 4
        data = this.getData3();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ExtSSInfoImpl();
        prim.decodeAll(asn);

        assertEquals(tag, ExtSSInfoImpl._TAG_ssData);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        forwardingInfo = prim.getForwardingInfo();
        callBarringInfo = prim.getCallBarringInfo();
        cugInfo = prim.getCugInfo();
        ssData = prim.getSsData();
        emlppInfo = prim.getEmlppInfo();

        assertNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNull(cugInfo);
        assertNotNull(ssData);
        assertNull(emlppInfo);

        extensionContainer = ssData.getExtensionContainer();
        assertEquals(ssData.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allServices);
        assertNotNull(ssData.getSsStatus());
        assertTrue(ssData.getSsStatus().getBitA());
        assertTrue(!ssData.getSsStatus().getBitP());
        assertTrue(!ssData.getSsStatus().getBitQ());
        assertTrue(ssData.getSsStatus().getBitR());
        assertNotNull(ssData.getSSSubscriptionOption());
        assertNotNull(ssData.getSSSubscriptionOption().getCliRestrictionOption());
        assertEquals(ssData.getSSSubscriptionOption().getCliRestrictionOption(), CliRestrictionOption.permanent);
        assertNull(ssData.getSSSubscriptionOption().getOverrideCategory());
        assertNotNull(extensionContainer);

        // option 5
        data = this.getData4();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ExtSSInfoImpl();
        prim.decodeAll(asn);

        assertEquals(tag, ExtSSInfoImpl._TAG_emlppInfo);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        forwardingInfo = prim.getForwardingInfo();
        callBarringInfo = prim.getCallBarringInfo();
        cugInfo = prim.getCugInfo();
        ssData = prim.getSsData();
        emlppInfo = prim.getEmlppInfo();

        assertNull(forwardingInfo);
        assertNull(callBarringInfo);
        assertNull(cugInfo);
        assertNull(ssData);
        assertNotNull(emlppInfo);

        extensionContainer = emlppInfo.getExtensionContainer();
        assertEquals(emlppInfo.getMaximumentitledPriority(), 2);
        assertEquals(emlppInfo.getDefaultPriority(), 1);
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.Asynchronous9_6kbps);
        ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ExtSSStatusImpl ssStatus = new ExtSSStatusImpl(false, false, true, true);
        ISDNAddressString forwardedToNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22228");
        ISDNSubaddressString forwardedToSubaddress = new ISDNSubaddressStringImpl(this.getISDNSubaddressStringData());
        ExtForwOptions forwardingOptions = new ExtForwOptionsImpl(true, false, true, ExtForwOptionsForwardingReason.msBusy);
        Integer noReplyConditionTime = new Integer(2);
        FTNAddressString longForwardedToNumber = new FTNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "22227");

        ExtForwFeatureImpl extForwFeature = new ExtForwFeatureImpl(basicService, ssStatus, forwardedToNumber,
                forwardedToSubaddress, forwardingOptions, noReplyConditionTime, extensionContainer, longForwardedToNumber);

        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.allServices);
        ArrayList<ExtForwFeature> forwardingFeatureList = new ArrayList<ExtForwFeature>();
        forwardingFeatureList.add(extForwFeature);
        ExtForwInfo forwardingInfo = new ExtForwInfoImpl(ssCode, forwardingFeatureList, extensionContainer);

        ExtCallBarringFeatureImpl callBarringFeature = new ExtCallBarringFeatureImpl(basicService, ssStatus, extensionContainer);
        ArrayList<ExtCallBarringFeature> callBarringFeatureList = new ArrayList<ExtCallBarringFeature>();
        callBarringFeatureList.add(callBarringFeature);

        ExtCallBarInfo callBarringInfo = new ExtCallBarInfoImpl(ssCode, callBarringFeatureList, extensionContainer);

        Integer preferentialCugIndicator = new Integer(1);
        InterCUGRestrictions interCugRestrictions = new InterCUGRestrictionsImpl(0);
        CUGFeatureImpl cugFeature = new CUGFeatureImpl(basicService, preferentialCugIndicator, interCugRestrictions,
                extensionContainer);
        ArrayList<CUGFeature> cugFeatureList = new ArrayList<CUGFeature>();
        cugFeatureList.add(cugFeature);

        ArrayList<CUGSubscription> cugSubscriptionList = new ArrayList<CUGSubscription>();
        int cugIndex = 1;
        CUGInterlock cugInterlock = new CUGInterlockImpl(getcugData());
        IntraCUGOptions intraCugOptions = IntraCUGOptions.getInstance(0);
        ArrayList<ExtBasicServiceCode> basicServiceList = new ArrayList<ExtBasicServiceCode>();
        basicServiceList.add(basicService);
        CUGSubscriptionImpl cugSubscription = new CUGSubscriptionImpl(cugIndex, cugInterlock, intraCugOptions,
                basicServiceList, extensionContainer);
        cugSubscriptionList.add(cugSubscription);

        CUGInfoImpl cugInfo = new CUGInfoImpl(cugSubscriptionList, cugFeatureList, extensionContainer);

        SSSubscriptionOption ssSubscriptionOption = new SSSubscriptionOptionImpl(CliRestrictionOption.permanent);

        ArrayList<ExtBasicServiceCode> basicServiceGroupList = new ArrayList<ExtBasicServiceCode>();
        basicServiceGroupList.add(basicService);

        ExtSSData ssData = new ExtSSDataImpl(ssCode, ssStatus, ssSubscriptionOption, basicServiceGroupList, extensionContainer);

        EMLPPInfoImpl emlppInfo = new EMLPPInfoImpl(2, 1, extensionContainer);

        // option 1
        ExtSSInfoImpl prim = new ExtSSInfoImpl(forwardingInfo);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        // option 2
        prim = new ExtSSInfoImpl(callBarringInfo);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

        // option 3
        prim = new ExtSSInfoImpl(cugInfo);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));

        // option 4
        prim = new ExtSSInfoImpl(ssData);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData3()));

        // option 5
        prim = new ExtSSInfoImpl(emlppInfo);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData4()));
    }
}
