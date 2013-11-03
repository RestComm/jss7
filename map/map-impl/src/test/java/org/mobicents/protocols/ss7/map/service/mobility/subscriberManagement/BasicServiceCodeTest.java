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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeValue;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class BasicServiceCodeTest {

    private byte[] getEncodedData1() {
        return new byte[] { -126, 1, 22 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { -125, 1, 33 };
    }

    @Test(groups = { "functional.decode", "service.subscriberManagement" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData1();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        assertEquals(tag, BasicServiceCodeImpl._TAG_bearerService);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        BasicServiceCodeImpl impl = new BasicServiceCodeImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.Asynchronous9_6kbps);
        assertNull(impl.getTeleservice());

        rawData = getEncodedData2();

        asn = new AsnInputStream(rawData);

        tag = asn.readTag();
        assertEquals(tag, BasicServiceCodeImpl._TAG_teleservice);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        impl = new BasicServiceCodeImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.shortMessageMT_PP);
        assertNull(impl.getBearerService());
    }

    @Test(groups = { "functional.encode", "service.subscriberManagement" })
    public void testEncode() throws Exception {

        BearerServiceCode bearerService = new BearerServiceCodeImpl(BearerServiceCodeValue.Asynchronous9_6kbps);
        BasicServiceCodeImpl impl = new BasicServiceCodeImpl(bearerService);
        AsnOutputStream asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        TeleserviceCode teleservice = new TeleserviceCodeImpl(TeleserviceCodeValue.shortMessageMT_PP);
        impl = new BasicServiceCodeImpl(teleservice);
        asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);

        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
