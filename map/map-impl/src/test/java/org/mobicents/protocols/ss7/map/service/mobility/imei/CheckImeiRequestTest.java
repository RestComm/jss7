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

package org.mobicents.protocols.ss7.map.service.mobility.imei;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.primitives.IMEIImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author normandes
 *
 */
public class CheckImeiRequestTest {

    // Real Trace
    private byte[] getEncodedDataV2() {
        return new byte[] { 0x04, 0x08, 0x53, 0x08, 0x19, 0x10, (byte) 0x86, 0x35, 0x55, (byte) 0xf0 };
    }

    private byte[] getEncodedDataV3() {
        // TODO this is self generated trace. We need trace from operator
        return new byte[] { 48, 14, 4, 8, 83, 8, 25, 16, -122, 53, 85, -16, 3, 2, 6, -128 };
    }

    private byte[] getEncodedDataV3Full() {
        // TODO this is self generated trace. We need trace from operator
        return new byte[] { 48, 55, 4, 8, 83, 8, 25, 16, -122, 53, 85, -16, 3, 2, 6, -128, 48, 39, -96, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31,
                32, 33 };
    }

    // Huawei trace with IMSI
    private byte[] getEncodedDataV2_Huawei() {
        return new byte[] { 0x04, 0x08, 0x53, 0x46, 0x76, 0x40, (byte) 0x94, (byte) 0x98, 0x19, (byte) 0xf0, 0x00, 0x08, 0x27,
                0x34, 0x04, 0x03, 0x30, 0x58, 0x67, (byte) 0xf3 };
    }

    private byte[] getEncodedDataImeiLengthLessThan15() {
        return new byte[] { 4, 1, -15 };
    }

    @Test(groups = { "functional.decode", "imei" })
    public void testDecode() throws Exception {
        // Testing version 3
        byte[] rawData = getEncodedDataV3();
        AsnInputStream asnIS = new AsnInputStream(rawData);

        int tag = asnIS.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        CheckImeiRequestImpl checkImeiImpl = new CheckImeiRequestImpl(3);
        checkImeiImpl.decodeAll(asnIS);

        assertTrue(checkImeiImpl.getIMEI().getIMEI().equals("358091016853550"));
        assertTrue(checkImeiImpl.getRequestedEquipmentInfo().getEquipmentStatus());
        assertFalse(checkImeiImpl.getRequestedEquipmentInfo().getBmuef());
        assertNull(checkImeiImpl.getIMSI());

        // Testing version 3 Full
        rawData = getEncodedDataV3Full();
        asnIS = new AsnInputStream(rawData);

        tag = asnIS.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        checkImeiImpl = new CheckImeiRequestImpl(3);
        checkImeiImpl.decodeAll(asnIS);

        assertTrue(checkImeiImpl.getIMEI().getIMEI().equals("358091016853550"));
        assertTrue(checkImeiImpl.getRequestedEquipmentInfo().getEquipmentStatus());
        assertFalse(checkImeiImpl.getRequestedEquipmentInfo().getBmuef());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(checkImeiImpl.getExtensionContainer()));
        assertNull(checkImeiImpl.getIMSI());

        // Testing version 1 and 2
        rawData = getEncodedDataV2();
        asnIS = new AsnInputStream(rawData);

        tag = asnIS.readTag();
        assertEquals(tag, Tag.STRING_OCTET);
        checkImeiImpl = new CheckImeiRequestImpl(2);
        checkImeiImpl.decodeAll(asnIS);

        assertTrue(checkImeiImpl.getIMEI().getIMEI().equals("358091016853550"));
        assertNull(checkImeiImpl.getIMSI());

        // Testing version 1 and 2 with Huawei trace
        rawData = getEncodedDataV2_Huawei();
        asnIS = new AsnInputStream(rawData);

        tag = asnIS.readTag();
        assertEquals(tag, Tag.STRING_OCTET);
        checkImeiImpl = new CheckImeiRequestImpl(2);
        checkImeiImpl.decodeAll(asnIS);

        assertTrue(checkImeiImpl.getIMEI().getIMEI().equals("356467044989910"));
        assertTrue(checkImeiImpl.getIMSI().getData().equals("724340300385763"));

        // Testing IMEI length != 15
        rawData = getEncodedDataImeiLengthLessThan15();
        asnIS = new AsnInputStream(rawData);

        tag = asnIS.readTag();
        assertEquals(tag, Tag.STRING_OCTET);
        checkImeiImpl = new CheckImeiRequestImpl(2);
        checkImeiImpl.decodeAll(asnIS);

        assertTrue(checkImeiImpl.getIMEI().getIMEI().equals("1"));
        assertNull(checkImeiImpl.getIMSI());
    }

    @Test(groups = { "functional.encode", "imei" })
    public void testEncode() throws Exception {
        // Testing version 3
        IMEIImpl imei = new IMEIImpl("358091016853550");
        RequestedEquipmentInfoImpl requestedEquipmentInfo = new RequestedEquipmentInfoImpl(true, false);

        CheckImeiRequestImpl checkImei = new CheckImeiRequestImpl(3, imei, requestedEquipmentInfo, null);
        AsnOutputStream asnOS = new AsnOutputStream();
        checkImei.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedDataV3();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Testing version 3 Full
        imei = new IMEIImpl("358091016853550");
        requestedEquipmentInfo = new RequestedEquipmentInfoImpl(true, false);
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        checkImei = new CheckImeiRequestImpl(3, imei, requestedEquipmentInfo, extensionContainer);
        asnOS = new AsnOutputStream();
        checkImei.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataV3Full();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Testing version 1 and 2
        imei = new IMEIImpl("358091016853550");
        checkImei = new CheckImeiRequestImpl(2, imei, null, null);

        asnOS = new AsnOutputStream();
        checkImei.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataV2();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Testing version 1 and 2 with Huawei trace
        imei = new IMEIImpl("356467044989910");
        IMSIImpl imsi = new IMSIImpl("724340300385763");
        checkImei = new CheckImeiRequestImpl(2, imei, null, null);
        checkImei.setIMSI(imsi);

        asnOS = new AsnOutputStream();
        checkImei.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataV2_Huawei();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Testing IMEI length != 15
        imei = new IMEIImpl("1");
        checkImei = new CheckImeiRequestImpl(2, imei, null, null);

        asnOS = new AsnOutputStream();
        checkImei.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataImeiLengthLessThan15();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}