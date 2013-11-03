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

import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class GPRSMSClassTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 11, -128, 3, 1, 2, 3, -127, 4, 11, 22, 33, 44 };
    }

    private byte[] getEncodedDataNetworkCapability() {
        return new byte[] { 1, 2, 3 };
    }

    private byte[] getEncodedDataRadioAccessCapability() {
        return new byte[] { 11, 22, 33, 44 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        GPRSMSClassImpl impl = new GPRSMSClassImpl();

        // TODO: fix a test

        // impl.decodeAll(asn);
        // assertEquals(tag, Tag.SEQUENCE);
        //
        // assertTrue(Arrays.equals(impl.getMSNetworkCapability().getData(), this.getEncodedDataNetworkCapability()));
        // assertTrue(Arrays.equals(impl.getMSRadioAccessCapability().getData(), this.getEncodedDataRadioAccessCapability()));
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {

        MSNetworkCapabilityImpl nc = new MSNetworkCapabilityImpl(this.getEncodedDataNetworkCapability());
        MSRadioAccessCapabilityImpl rac = new MSRadioAccessCapabilityImpl(this.getEncodedDataRadioAccessCapability());
        GPRSMSClassImpl impl = new GPRSMSClassImpl(nc, rac);
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
