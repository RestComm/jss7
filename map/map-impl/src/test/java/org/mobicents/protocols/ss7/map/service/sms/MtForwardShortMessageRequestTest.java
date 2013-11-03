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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MtForwardShortMessageRequestTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 73, -128, 8, 16, 33, 34, 34, 17, -126, 21, -12, -124, 7, -111, -127, 33, 105, 0, -112, -10, 4,
                52, 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 };
    }

    private byte[] getEncodedDataFull() {
        return new byte[] { 48, 70, -128, 8, 1, -128, 56, 67, 84, 101, 118, -9, -124, 6, -111, 17, 17, 33, 34, 34, 4, 7, 11,
                22, 33, 44, 55, 66, 77, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3,
                6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        MtForwardShortMessageRequestImpl ind = new MtForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        SM_RP_DA da = ind.getSM_RP_DA();
        SM_RP_OA oa = ind.getSM_RP_OA();
        SmsSignalInfo ui = ind.getSM_RP_UI();
        // assertEquals( (long) da.getIMSI().getMCC(),11);
        // assertEquals( (long) da.getIMSI().getMNC(),22);
        assertEquals(da.getIMSI().getData(), "011222221128514");
        assertEquals(oa.getServiceCentreAddressOA().getAddressNature(), AddressNature.international_number);
        assertEquals(oa.getServiceCentreAddressOA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oa.getServiceCentreAddressOA().getAddress(), "18129600096");
        assertTrue(Arrays.equals(ui.getData(), new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8, 1, 2, 2,
                2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11, 11, 0 }));

        rawData = getEncodedDataFull();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new MtForwardShortMessageRequestImpl();
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        da = ind.getSM_RP_DA();
        oa = ind.getSM_RP_OA();
        ui = ind.getSM_RP_UI();
        Boolean moreMesToSend = ind.getMoreMessagesToSend();
        // assertEquals( (long) da.getIMSI().getMCC(),100);
        // assertEquals( (long) da.getIMSI().getMNC(),88);
        assertEquals(da.getIMSI().getData(), "100883344556677");
        assertEquals(oa.getServiceCentreAddressOA().getAddressNature(), AddressNature.international_number);
        assertEquals(oa.getServiceCentreAddressOA().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(oa.getServiceCentreAddressOA().getAddress(), "1111122222");
        assertTrue(Arrays.equals(ui.getData(), new byte[] { 11, 22, 33, 44, 55, 66, 77 }));
        assertTrue(moreMesToSend);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        IMSI imsi = new IMSIImpl("011222221128514");
        SM_RP_DA sm_RP_DA = new SM_RP_DAImpl(imsi);
        AddressString sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "18129600096");
        SM_RP_OAImpl sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setServiceCentreAddressOA(sca);
        SmsSignalInfo sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77, 0, 1, 2, 3, 4, 5, 6, 7, 9, 8,
                1, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 99, 88, 77, 66, 55, 44, 44, 33, 22, 11,
                11, 0 }, null);
        MtForwardShortMessageRequestImpl ind = new MtForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, false, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        imsi = new IMSIImpl("100883344556677");
        sm_RP_DA = new SM_RP_DAImpl(imsi);
        sca = new AddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1111122222");
        sm_RP_OA = new SM_RP_OAImpl();
        sm_RP_OA.setServiceCentreAddressOA(sca);
        sm_RP_UI = new SmsSignalInfoImpl(new byte[] { 11, 22, 33, 44, 55, 66, 77 }, null);
        ind = new MtForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI, true,
                MAPExtensionContainerTest.GetTestExtensionContainer());

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataFull();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
