/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.RequestedInfoImpl;
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
