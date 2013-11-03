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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 *
 */
public class RequestedInfoTest {

    // Real Trace
    byte[] data = new byte[] { (byte) 0xa1, 0x04, (byte) 0x80, 0x00, (byte) 0x81, 0x00 };
    byte[] dataFull = new byte[] { 48, 56, -128, 0, -127, 0, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48,
            5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 0, -124, 1, 1, -122,
            0, -123, 0, -121, 0 };

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        assertEquals(tag, 1);

        RequestedInfoImpl requestedInfo = new RequestedInfoImpl();
        requestedInfo.decodeAll(asn);

        assertTrue(requestedInfo.getLocationInformation());
        assertTrue(requestedInfo.getSubscriberState());
        assertNull(requestedInfo.getExtensionContainer());
        assertFalse(requestedInfo.getCurrentLocation());
        assertNull(requestedInfo.getRequestedDomain());
        assertFalse(requestedInfo.getImei());
        assertFalse(requestedInfo.getMsClassmark());
        assertFalse(requestedInfo.getMnpRequestedInfo());

        asn = new AsnInputStream(dataFull);
        tag = asn.readTag();
        assertEquals(tag, Tag.SEQUENCE);

        requestedInfo = new RequestedInfoImpl();
        requestedInfo.decodeAll(asn);

        assertTrue(requestedInfo.getLocationInformation());
        assertTrue(requestedInfo.getSubscriberState());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(requestedInfo.getExtensionContainer()));
        assertTrue(requestedInfo.getCurrentLocation());
        assertEquals(requestedInfo.getRequestedDomain(), DomainType.psDomain);
        assertTrue(requestedInfo.getImei());
        assertTrue(requestedInfo.getMsClassmark());
        assertTrue(requestedInfo.getMnpRequestedInfo());

    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
        RequestedInfoImpl requestedInfo = new RequestedInfoImpl(true, true, null, false, null, false, false, false);
        AsnOutputStream asnOS = new AsnOutputStream();
        requestedInfo.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 1);

        byte[] encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(encodedData, data));

        requestedInfo = new RequestedInfoImpl(true, true, MAPExtensionContainerTest.GetTestExtensionContainer(), true,
                DomainType.psDomain, true, true, true);
        asnOS = new AsnOutputStream();
        requestedInfo.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        assertTrue(Arrays.equals(encodedData, dataFull));
    }
}
