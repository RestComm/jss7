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

package org.mobicents.protocols.ss7.map.service.sms;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.sms.SMDeliveryNotIntended;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.TeleserviceCodeImpl;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SendRoutingInfoForSMRequestTest {

    private byte[] getEncodedDataSimple() {
        return new byte[] { 48, 20, -128, 7, -111, 49, 84, 119, 84, 85, -15, -127, 1, 0, -126, 6, -111, -119, 18, 17, 51, 51 };
    }

    private byte[] getEncodedDataComplex() {
        return new byte[] { 48, 30, -128, 7, -111, 49, 84, 119, 84, 85, -15, -127, 1, 0, -126, 6, -111, -119, 18, 17, 51, 51,
                -121, 0, -119, 6, -111, 105, 49, 3, -105, 97 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 70, -128, 6, -111, 17, 33, 34, 51, -13, -127, 1, -1, -126, 3, -72, 68, 68, -90, 39, -96, 32,
                48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25,
                26, -95, 3, 31, 32, 33, -121, 0, -120, 1, 1, -119, 6, -111, 105, 49, 3, -105, 97 };
    }

    private byte[] getEncodedData1() {
        return new byte[] { 48, 20, -128, 5, -111, 17, 17, 17, 17, -127, 1, 0, -126, 5, -111, 34, 34, 34, 34, -123, 1, 33 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48,25,-128,5,-111,17,17,17,17,-127,1,-1,-126,5,-111,34,34,34,34,-121,0,-117,0,-114,0,-115,0 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 48,20,-128,5,-111,17,17,17,17,-127,1,0,-126,5,-111,34,34,34,34,-118,1,0 };
    }


    private byte[] getEncodedData4() {
        return new byte[] { 48,25,-128,5,-111,17,17,17,17,-127,1,0,-126,5,-111,34,34,34,34,-116,6,81,20,69,81,20,69};
    }



    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedDataSimple();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        SendRoutingInfoForSMRequestImpl ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        ISDNAddressString msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "13457745551");
        assertEquals((boolean) ind.getSm_RP_PRI(), false);
        AddressString sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.international_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sca.getAddress(), "9821113333");
        assertNull(ind.getTeleservice());

        rawData = getEncodedDataComplex();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "13457745551");
        assertEquals((boolean) ind.getSm_RP_PRI(), false);
        sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.international_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sca.getAddress(), "9821113333");
        assertEquals((boolean) ind.getGprsSupportIndicator(), true);
        assertTrue(Arrays.equals(new byte[] { -111, 105, 49, 3, -105, 97 }, ind.getSM_RP_SMEA().getData()));
        assertNull(ind.getTeleservice());

        rawData = getEncodedDataFull();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "111222333");
        assertEquals((boolean) ind.getSm_RP_PRI(), true);
        sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.network_specific_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.national);
        assertEquals(sca.getAddress(), "4444");
        assertEquals((boolean) ind.getGprsSupportIndicator(), true);
        assertTrue(Arrays.equals(new byte[] { -111, 105, 49, 3, -105, 97 }, ind.getSM_RP_SMEA().getData()));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
        assertEquals(ind.getSM_RP_MTI(), SM_RP_MTI.SMS_Status_Report);
        assertNull(ind.getTeleservice());

        rawData = getEncodedData1();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new SendRoutingInfoForSMRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        msisdn = ind.getMsisdn();
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(msisdn.getAddress(), "11111111");
        assertEquals((boolean) ind.getSm_RP_PRI(), false);
        sca = ind.getServiceCentreAddress();
        assertEquals(sca.getAddressNature(), AddressNature.international_number);
        assertEquals(sca.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(sca.getAddress(), "22222222");
        assertEquals(ind.getGprsSupportIndicator(), false);
        assertNull(ind.getSM_RP_SMEA());
        assertNull(ind.getExtensionContainer());
        assertNull(ind.getSM_RP_MTI());
        assertEquals(ind.getTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.shortMessageMT_PP);
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        //msisdn + sca
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "13457745551");
        AddressString sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "9821113333");
        SendRoutingInfoForSMRequestImpl ind = new SendRoutingInfoForSMRequestImpl(msisdn, false, sca, null, false, null, null,
                null, false, null, false, false, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedDataSimple();
        assertTrue(Arrays.equals(rawData, encodedData));

        // msisdn + sca + sm_rp_smea
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "13457745551");
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "9821113333");
        SM_RP_SMEA sm_rp_smea = new SM_RP_SMEAImpl(new byte[] { -111, 105, 49, 3, -105, 97 });
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, false, sca, null, true, null, sm_rp_smea, null, false, null, false, false, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataComplex();
        assertTrue(Arrays.equals(rawData, encodedData));

        //msisdn + sca + sm_rp_smea + extContainer + SM_RP_MTI
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "111222333");
        sca = new AddressStringImpl(AddressNature.network_specific_number, NumberingPlan.national, "4444");
        sm_rp_smea = new SM_RP_SMEAImpl(new byte[] { -111, 105, 49, 3, -105, 97 });
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, true, sca, MAPExtensionContainerTest.GetTestExtensionContainer(),
                true, SM_RP_MTI.SMS_Status_Report, sm_rp_smea, null, false, null, false, false, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));

        //msisdn + sca + tc
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "11111111");
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22222222");
        TeleserviceCodeImpl tc = new TeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMT_PP);
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, false, sca, null, false, null, null, null, false, null, false, false, tc);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        //msisdn + sca + sm_RP_PRI + gprsSupportIndicator + ipSmGwGuidanceIndicator + t4TriggerIndicator + this.singleAttemptDelivery
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "11111111");
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22222222");
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, true, sca, null, true, null, null, null, true, null, true, true, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));


        //msisdn + sca + smDeliveryNotIntended
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "11111111");
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22222222");
        SMDeliveryNotIntended smDeliveryNotIntended = SMDeliveryNotIntended.onlyIMSIRequested;
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, false, sca, null, false, null, null, smDeliveryNotIntended, false, null, false, false, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));

        //msisdn + sca + imsi
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "11111111");
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22222222");
        IMSI imsi = new IMSIImpl("154154154154");
        ind = new SendRoutingInfoForSMRequestImpl(msisdn, false, sca, null, false, null, null, null, false, imsi, false, false, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData4();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
