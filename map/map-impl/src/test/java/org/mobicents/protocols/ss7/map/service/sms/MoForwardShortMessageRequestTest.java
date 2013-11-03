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

package org.mobicents.protocols.ss7.map.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MoForwardShortMessageRequestTest {

    private byte[] getEncodedDataSimple() {
        return new byte[] { 48, 38, -124, 7, -111, 34, 51, 67, -103, 32, 50, -126, 8, -111, 50, 17, 50, 33, 67, 51, -12, 4, 17,
                11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8 };
    }

    private byte[] getEncodedDataComplex() {
        return new byte[] { 48, 71, -124, 8, -111, 50, 17, 50, 33, 67, 51, -12, -126, 7, -111, 34, 51, 67, -103, 32, 50, 4, 40,
                11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4,
                4, 4, 4, 4, 4, 4, 8, 0, 1, 33, 50, 51, -108, 9, -14 };
    }

    private byte[] getEncodedDataNoDaOa() {
        return new byte[] { 48, 58, -123, 0, -123, 0, 4, 52, 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2,
                2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 80, -128, 8, 2, 1, 17, 50, 84, 118, -104, -16, -124, 6, -74, 16, 50, 84, 118, -104, 4, 9, 11,
                22, 33, 44, 55, 66, 77, 88, 99, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42,
                3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 4, 8, 66, -128, 24, 33, 50, 67, 84,
                -11 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedDataSimple();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        MoForwardShortMessageRequestImpl ind = new MoForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        SM_RP_DA da = ind.getSM_RP_DA();
        SM_RP_OA oa = ind.getSM_RP_OA();
        SmsSignalInfo ui = ind.getSM_RP_UI();
        assertEquals(da.getServiceCentreAddressDA().getAddressNature(), AddressNature.international_number);
        assertEquals(da.getServiceCentreAddressDA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(da.getServiceCentreAddressDA().getAddress(), "223334990223");
        assertEquals(oa.getMsisdn().getAddressNature(), AddressNature.international_number);
        assertEquals(oa.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oa.getMsisdn().getAddress(), "2311231234334");
        assertTrue(Arrays.equals(ui.getData(), new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8 }));

        rawData = getEncodedDataComplex();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new MoForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        da = ind.getSM_RP_DA();
        oa = ind.getSM_RP_OA();
        ui = ind.getSM_RP_UI();
        IMSI imsi = ind.getIMSI();
        assertEquals(da.getServiceCentreAddressDA().getAddressNature(), AddressNature.international_number);
        assertEquals(da.getServiceCentreAddressDA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(da.getServiceCentreAddressDA().getAddress(), "2311231234334");
        assertEquals(oa.getMsisdn().getAddressNature(), AddressNature.international_number);
        assertEquals(oa.getMsisdn().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oa.getMsisdn().getAddress(), "223334990223");
        assertTrue(Arrays.equals(ui.getData(), new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2,
                2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4 }));
        // assertEquals( (long)imsi.getMCC(),1);
        // assertEquals( (long)imsi.getMNC(),1);
        assertEquals(imsi.getData(), "001012233349902");

        rawData = getEncodedDataNoDaOa();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new MoForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        da = ind.getSM_RP_DA();
        oa = ind.getSM_RP_OA();
        ui = ind.getSM_RP_UI();
        assertNull(da.getServiceCentreAddressDA());
        assertNull(da.getIMSI());
        assertNull(da.getLMSI());
        assertNull(oa.getMsisdn());
        assertNull(oa.getServiceCentreAddressOA());
        assertTrue(Arrays.equals(ui.getData(), new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2,
                2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 }));

        rawData = getEncodedDataFull();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new MoForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        da = ind.getSM_RP_DA();
        oa = ind.getSM_RP_OA();
        ui = ind.getSM_RP_UI();
        imsi = ind.getIMSI();
        // assertEquals( (long)da.getIMSI().getMCC(),201);
        // assertEquals( (long)da.getIMSI().getMNC(),1);
        assertEquals(da.getIMSI().getData(), "201011234567890");
        assertEquals(oa.getServiceCentreAddressOA().getAddressNature(), AddressNature.network_specific_number);
        assertEquals(oa.getServiceCentreAddressOA().getNumberingPlan(), NumberingPlan.land_mobile);
        assertEquals(oa.getServiceCentreAddressOA().getAddress(), "0123456789");
        assertTrue(Arrays.equals(ui.getData(), new byte[] { 11, 22, 33, 44, 55, 66, 77, 88, 99 }));
        // assertEquals( (long)imsi.getMCC(),240);
        // assertEquals( (long)imsi.getMNC(),88);
        assertEquals(imsi.getData(), "240881122334455");
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        AddressString sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "223334990223");
        SM_RP_DA sm_RP_DA = new SM_RP_DAImpl(sca);
        ISDNAddressString msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "2311231234334");
        SM_RP_OAImpl sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setMsisdn(msisdn);
        SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8 },
                null);
        MoForwardShortMessageRequestImpl ind = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedDataSimple();
        assertTrue(Arrays.equals(rawData, encodedData));

        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "2311231234334");
        sm_RP_DA = new SM_RP_DAImpl(sca);
        msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "223334990223");
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setMsisdn(msisdn);
        sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2,
                2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4 }, null);
        IMSI imsi = new IMSIImpl("001012233349902");
        ind = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, imsi);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataComplex();
        assertTrue(Arrays.equals(rawData, encodedData));

        sm_RP_DA = new SM_RP_DAImpl();
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2,
                2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 }, null);
        ind = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, null, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataNoDaOa();
        assertTrue(Arrays.equals(rawData, encodedData));

        IMSI imsi0 = new IMSIImpl("201011234567890");
        sm_RP_DA = new SM_RP_DAImpl(imsi0);
        msisdn = new ISDNAddressStringImpl(AddressNature.network_specific_number, NumberingPlan.land_mobile, "0123456789");
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setServiceCentreAddressOA(msisdn);
        sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77, 88, 99 }, null);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        imsi = new IMSIImpl("240881122334455");
        ind = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, extensionContainer, imsi);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
