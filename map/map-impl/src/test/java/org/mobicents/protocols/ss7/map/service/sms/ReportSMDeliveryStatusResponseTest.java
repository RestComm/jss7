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
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ReportSMDeliveryStatusResponseTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 49, 4, 6, -111, -120, 120, 119, 102, -10, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13,
                14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    private byte[] getEncodedDataV2() {
        return new byte[] { 4, 6, -111, -120, 120, 119, 102, -10 };
    }

    @Test(groups = { "functional.decode", "service.sms" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        ReportSMDeliveryStatusResponseImpl ind = new ReportSMDeliveryStatusResponseImpl(3);
        ind.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(ind.getStoredMSISDN().getAddressNature(), AddressNature.international_number);
        assertEquals(ind.getStoredMSISDN().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(ind.getStoredMSISDN().getAddress(), "888777666");
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));

        rawData = getEncodedDataV2();
        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        ind = new ReportSMDeliveryStatusResponseImpl(2);
        ind.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(ind.getStoredMSISDN().getAddressNature(), AddressNature.international_number);
        assertEquals(ind.getStoredMSISDN().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(ind.getStoredMSISDN().getAddress(), "888777666");
        assertNull(ind.getExtensionContainer());
    }

    @Test(groups = { "functional.encode", "service.sms" })
    public void testEncode() throws Exception {

        ISDNAddressString storedMSISDN = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "888777666");
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ReportSMDeliveryStatusResponseImpl ind = new ReportSMDeliveryStatusResponseImpl(3, storedMSISDN, extensionContainer);

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        ind = new ReportSMDeliveryStatusResponseImpl(2, storedMSISDN, null);

        asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedDataV2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
