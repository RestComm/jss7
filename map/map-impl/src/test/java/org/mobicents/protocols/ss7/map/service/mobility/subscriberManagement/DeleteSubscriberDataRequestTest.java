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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSSubscriptionDataWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionDataWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAInformationWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SpecificCSIWithdraw;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class DeleteSubscriberDataRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 8, (byte) 128, 6, 17, 33, 34, 51, 67, 68 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 74, -128, 6, 17, 33, 34, 51, 67, 68, -95, 3, -126, 1, 48, -94, 6, 4, 1, 33, 4, 1, 17, -124, 0, -123, 2, 0, 11, -121, 0, -120,
                0, -119, 0, -90, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 48, 42, (byte) 128, 6, 17, 33, 34, 51, 67, 68, (byte) 170, 2, 5, 0, (byte) 139, 0, (byte) 172, 2, 5, 0, (byte) 141, 0, (byte) 142,
                0, (byte) 143, 3, 2, (byte) 144, 0, (byte) 144, 0, (byte) 145, 0, (byte) 178, 5, 48, 3, 2, 1, 15, (byte) 147, 0, (byte) 148, 0 };
    }

    @Test(groups = { "functional.decode", "service.mobility.subscriberManagement" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        DeleteSubscriberDataRequestImpl asc = new DeleteSubscriberDataRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        IMSI imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));

        assertNull(asc.getBasicServiceList());
        assertNull(asc.getSsList());
        assertFalse(asc.getRoamingRestrictionDueToUnsupportedFeature());
        assertNull(asc.getRegionalSubscriptionIdentifier());
        assertFalse(asc.getVbsGroupIndication());
        assertFalse(asc.getVgcsGroupIndication());
        assertFalse(asc.getCamelSubscriptionInfoWithdraw());
        assertNull(asc.getExtensionContainer());
        assertNull(asc.getGPRSSubscriptionDataWithdraw());
        assertFalse(asc.getRoamingRestrictedInSgsnDueToUnsuppportedFeature());
        assertNull(asc.getLSAInformationWithdraw());
        assertFalse(asc.getGmlcListWithdraw());
        assertFalse(asc.getIstInformationWithdraw());
        assertNull(asc.getSpecificCSIWithdraw());
        assertFalse(asc.getChargingCharacteristicsWithdraw());
        assertFalse(asc.getStnSrWithdraw());
        assertNull(asc.getEPSSubscriptionDataWithdraw());
        assertFalse(asc.getApnOiReplacementWithdraw());
        assertFalse(asc.getCsgSubscriptionDeleted());


        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new DeleteSubscriberDataRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));

        assertEquals(asc.getBasicServiceList().size(), 1);
        assertEquals(asc.getBasicServiceList().get(0).getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.allAlternateSpeech_DataCDA);
        assertEquals(asc.getSsList().size(), 2);
        assertEquals(asc.getSsList().get(0).getSupplementaryCodeValue(), SupplementaryCodeValue.cfu);
        assertEquals(asc.getSsList().get(1).getSupplementaryCodeValue(), SupplementaryCodeValue.clip);
        assertTrue(asc.getRoamingRestrictionDueToUnsupportedFeature());
        assertEquals(asc.getRegionalSubscriptionIdentifier().getValue(), 11);
        assertTrue(asc.getVbsGroupIndication());
        assertTrue(asc.getVgcsGroupIndication());
        assertTrue(asc.getCamelSubscriptionInfoWithdraw());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(asc.getExtensionContainer()));

        assertNull(asc.getGPRSSubscriptionDataWithdraw());
        assertFalse(asc.getRoamingRestrictedInSgsnDueToUnsuppportedFeature());
        assertNull(asc.getLSAInformationWithdraw());
        assertFalse(asc.getGmlcListWithdraw());
        assertFalse(asc.getIstInformationWithdraw());
        assertNull(asc.getSpecificCSIWithdraw());
        assertFalse(asc.getChargingCharacteristicsWithdraw());
        assertFalse(asc.getStnSrWithdraw());
        assertNull(asc.getEPSSubscriptionDataWithdraw());
        assertFalse(asc.getApnOiReplacementWithdraw());
        assertFalse(asc.getCsgSubscriptionDeleted());


        rawData = getEncodedData3();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        asc = new DeleteSubscriberDataRequestImpl();
        asc.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        imsi = asc.getImsi();
        assertTrue(imsi.getData().equals("111222333444"));

        assertNull(asc.getBasicServiceList());
        assertNull(asc.getSsList());
        assertFalse(asc.getRoamingRestrictionDueToUnsupportedFeature());
        assertNull(asc.getRegionalSubscriptionIdentifier());
        assertFalse(asc.getVbsGroupIndication());
        assertFalse(asc.getVgcsGroupIndication());
        assertFalse(asc.getCamelSubscriptionInfoWithdraw());
        assertNull(asc.getExtensionContainer());

        assertTrue(asc.getGPRSSubscriptionDataWithdraw().getAllGPRSData());
        assertTrue(asc.getRoamingRestrictedInSgsnDueToUnsuppportedFeature());
        assertTrue(asc.getLSAInformationWithdraw().getAllLSAData());
        assertTrue(asc.getGmlcListWithdraw());
        assertTrue(asc.getIstInformationWithdraw());

        SpecificCSIWithdraw specificCSIWithdraw = asc.getSpecificCSIWithdraw();
        assertTrue(specificCSIWithdraw.getOCsi());
        assertFalse(specificCSIWithdraw.getSsCsi());
        assertFalse(specificCSIWithdraw.getTifCsi());
        assertTrue(specificCSIWithdraw.getDCsi());
        assertFalse(specificCSIWithdraw.getVtCsi());

        assertTrue(asc.getChargingCharacteristicsWithdraw());
        assertTrue(asc.getStnSrWithdraw());
        assertEquals(asc.getEPSSubscriptionDataWithdraw().getContextIdList().size(), 1);
        assertEquals((int) asc.getEPSSubscriptionDataWithdraw().getContextIdList().get(0), 15);
        assertTrue(asc.getApnOiReplacementWithdraw());
        assertTrue(asc.getCsgSubscriptionDeleted());
    }

    @Test(groups = { "functional.encode", "service.mobility.subscriberManagement" })
    public void testEncode() throws Exception {

        IMSIImpl imsi = new IMSIImpl("111222333444");
        DeleteSubscriberDataRequestImpl asc = new DeleteSubscriberDataRequestImpl(imsi, null, null, false, null, false, false, false, null, null, false, null,
                false, false, null, false, false, null, false, false);

        AsnOutputStream asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));


        ArrayList<ExtBasicServiceCode> basicServiceList = new ArrayList<ExtBasicServiceCode>();
        ExtBearerServiceCode extBearerService = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.allAlternateSpeech_DataCDA);
        ExtBasicServiceCode basicService = new ExtBasicServiceCodeImpl(extBearerService);
        basicServiceList.add(basicService);
        ArrayList<SSCode> ssList = new ArrayList<SSCode>();
        SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.cfu);
        SSCode ssCode2 = new SSCodeImpl(SupplementaryCodeValue.clip);
        ssList.add(ssCode);
        ssList.add(ssCode2);
        ZoneCode regionalSubscriptionIdentifier = new ZoneCodeImpl(11);
        asc = new DeleteSubscriberDataRequestImpl(imsi, basicServiceList, ssList, true, regionalSubscriptionIdentifier, true, true, true,
                MAPExtensionContainerTest.GetTestExtensionContainer(), null, false, null, false, false, null, false, false, null, false, false);

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));


        GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw = new GPRSSubscriptionDataWithdrawImpl(true);
        LSAInformationWithdraw lsaInformationWithdraw = new LSAInformationWithdrawImpl(true);
        SpecificCSIWithdraw specificCSIWithdraw = new SpecificCSIWithdrawImpl(true, false, false, true, false, false, false, false, false, false, false, false,
                false, false);
        ArrayList<Integer> contextIdList = new ArrayList<Integer>();
        contextIdList.add(15);
        EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw = new EPSSubscriptionDataWithdrawImpl(contextIdList);
        asc = new DeleteSubscriberDataRequestImpl(imsi, null, null, false, null, false, false, false, null, gprsSubscriptionDataWithdraw, true,
                lsaInformationWithdraw, true, true, specificCSIWithdraw, true, true, epsSubscriptionDataWithdraw, true, true);
//        IMSI imsi, ArrayList<ExtBasicServiceCode> basicServiceList, ArrayList<SSCode> ssList,
//        boolean roamingRestrictionDueToUnsupportedFeature, ZoneCode regionalSubscriptionIdentifier, boolean vbsGroupIndication,
//        boolean vgcsGroupIndication, boolean camelSubscriptionInfoWithdraw, MAPExtensionContainer extensionContainer,
//        GPRSSubscriptionDataWithdraw gprsSubscriptionDataWithdraw, boolean roamingRestrictedInSgsnDueToUnsuppportedFeature,
//        LSAInformationWithdraw lsaInformationWithdraw, boolean gmlcListWithdraw, boolean istInformationWithdraw, SpecificCSIWithdraw specificCSIWithdraw,
//        boolean chargingCharacteristicsWithdraw, boolean stnSrWithdraw, EPSSubscriptionDataWithdraw epsSubscriptionDataWithdraw,
//        boolean apnOiReplacementWithdraw, boolean csgSubscriptionDeleted        

        asnOS = new AsnOutputStream();
        asc.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
