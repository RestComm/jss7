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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class BearerServiceCodeTest {

    private byte[] getEncodedData1() {
        return new byte[] { (byte) 130, 1, 22 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData1();
        AsnInputStream asn = new AsnInputStream(rawData);
        int tag = asn.readTag();
        BearerServiceCodeImpl impl = new BearerServiceCodeImpl();
        impl.decodeAll(asn);
        assertEquals(impl.getData(), 22);
        assertEquals(impl.getBearerServiceCodeValue(), BearerServiceCodeValue.Asynchronous9_6kbps);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        BearerServiceCodeImpl impl = new BearerServiceCodeImpl(22);
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 2);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new BearerServiceCodeImpl(BearerServiceCodeValue.Asynchronous9_6kbps);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 2);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
