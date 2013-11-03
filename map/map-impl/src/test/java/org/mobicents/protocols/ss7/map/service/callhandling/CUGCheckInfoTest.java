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

package org.mobicents.protocols.ss7.map.service.callhandling;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlock;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CUGInterlockImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CUGCheckInfoTest {

    private byte[] getEncodedData() {
        return new byte[] { -95, 49, 4, 4, 1, 2, 3, 4, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48,
                5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    }

    private byte[] getGugData() {
        return new byte[] { 1, 2, 3, 4 };
    }

    @Test(groups = { "functional.decode", "service.callhandling" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();
        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        CUGCheckInfoImpl ind = new CUGCheckInfoImpl();
        assertEquals(tag, 1);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        ind.decodeAll(asn);

        assertTrue(Arrays.equals(ind.getCUGInterlock().getData(), getGugData()));
        assertTrue(ind.getCUGOutgoingAccess());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(ind.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "service.callhandling" })
    public void testEncode() throws Exception {

        CUGInterlock cugInterlock = new CUGInterlockImpl(getGugData());
        CUGCheckInfoImpl ind = new CUGCheckInfoImpl(cugInterlock, true, MAPExtensionContainerTest.GetTestExtensionContainer());
        // CUGInterlock cugInterlock, boolean cugOutgoingAccess, MAPExtensionContainer extensionContainer

        AsnOutputStream asnOS = new AsnOutputStream();
        ind.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 1);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
