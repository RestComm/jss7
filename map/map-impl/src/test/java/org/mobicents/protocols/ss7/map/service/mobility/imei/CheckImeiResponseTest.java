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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author normandes
 *
 */
public class CheckImeiResponseTest {

    // Real Trace
    private byte[] getEncodedDataV2() {
        return new byte[] { 0x0a, 0x01, 0x00 };
    }

    private byte[] getEncodedDataV3() {
        // TODO this is self generated trace. We need trace from operator
        return new byte[] { 48, 13, 10, 1, 0, 48, 8, -128, 2, 7, -128, -127, 2, 7, 0 };
    }

    private byte[] getEncodedDataV3Full() {
        // TODO this is self generated trace. We need trace from operator
        return new byte[] { 48, 54, 10, 1, 0, 48, 8, -128, 2, 7, -128, -127, 2, 7, 0, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4,
                11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    @Test(groups = { "functional.decode", "imei" })
    public void testDecode() throws Exception {
        // Testing version 3
        byte[] rawData = getEncodedDataV3();
        AsnInputStream asnIS = new AsnInputStream(rawData);

        int tag = asnIS.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        CheckImeiResponseImpl checkImeiImpl = new CheckImeiResponseImpl(3);
        checkImeiImpl.decodeAll(asnIS);

        assertEquals(checkImeiImpl.getEquipmentStatus(), EquipmentStatus.whiteListed);
        assertTrue(checkImeiImpl.getBmuef().getUESBI_IuA().getData().get(0));
        assertFalse(checkImeiImpl.getBmuef().getUESBI_IuB().getData().get(0));

        // Testing version 3 Full
        rawData = getEncodedDataV3Full();
        asnIS = new AsnInputStream(rawData);

        tag = asnIS.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        checkImeiImpl = new CheckImeiResponseImpl(3);
        checkImeiImpl.decodeAll(asnIS);

        assertEquals(checkImeiImpl.getEquipmentStatus(), EquipmentStatus.whiteListed);
        assertTrue(checkImeiImpl.getBmuef().getUESBI_IuA().getData().get(0));
        assertFalse(checkImeiImpl.getBmuef().getUESBI_IuB().getData().get(0));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(checkImeiImpl.getExtensionContainer()));

        // Testing version 1 and 2
        rawData = getEncodedDataV2();
        asnIS = new AsnInputStream(rawData);

        tag = asnIS.readTag();
        assertEquals(tag, Tag.ENUMERATED);
        checkImeiImpl = new CheckImeiResponseImpl(2);
        checkImeiImpl.decodeAll(asnIS);

        assertEquals(checkImeiImpl.getEquipmentStatus(), EquipmentStatus.whiteListed);
    }

    @Test(groups = { "functional.encode", "imei" })
    public void testEncode() throws Exception {
        // Testing version 3
        BitSetStrictLength bsUESBIIuA = new BitSetStrictLength(1);
        bsUESBIIuA.set(0);
        UESBIIuAImpl impUESBIIuA = new UESBIIuAImpl(bsUESBIIuA);

        BitSetStrictLength bsUESBIIuB = new BitSetStrictLength(1);
        UESBIIuBImpl impUESBIIuB = new UESBIIuBImpl(bsUESBIIuB);

        UESBIIuImpl bmuef = new UESBIIuImpl(impUESBIIuA, impUESBIIuB);
        CheckImeiResponseImpl checkImei = new CheckImeiResponseImpl(3, EquipmentStatus.whiteListed, bmuef, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        checkImei.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedDataV3();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Testing version 3 Full
        bsUESBIIuA = new BitSetStrictLength(1);
        bsUESBIIuA.set(0);
        impUESBIIuA = new UESBIIuAImpl(bsUESBIIuA);

        bsUESBIIuB = new BitSetStrictLength(1);
        impUESBIIuB = new UESBIIuBImpl(bsUESBIIuB);

        bmuef = new UESBIIuImpl(impUESBIIuA, impUESBIIuB);

        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

        checkImei = new CheckImeiResponseImpl(3, EquipmentStatus.whiteListed, bmuef, extensionContainer);

        asnOS = new AsnOutputStream();
        checkImei.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataV3Full();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Testing version 1 and 2
        checkImei = new CheckImeiResponseImpl(2, EquipmentStatus.whiteListed, null, null);

        asnOS = new AsnOutputStream();
        checkImei.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataV2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}