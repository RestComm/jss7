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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.NumberPortabilityStatus;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MNPInfoResTest {

    private byte[] getEncodedData() {
        return new byte[] { 16, 23, -128, 3, -112, 120, -10, -127, 6, 82, 48, 3, 33, 67, -11, -126, 5, -111, 17, 34, 51, 68,
                -125, 1, 5 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        MNPInfoResImpl impl = new MNPInfoResImpl();
        impl.decodeAll(asn);
        assertEquals(tag, Tag.SEQUENCE);

        assertTrue(impl.getRouteingNumber().getRouteingNumber().equals("09876"));
        assertTrue(impl.getIMSI().getData().equals("25033012345"));
        assertTrue(impl.getMSISDN().getAddress().equals("11223344"));
        assertEquals(impl.getMSISDN().getAddressNature(), AddressNature.international_number);
        assertEquals(impl.getMSISDN().getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(impl.getNumberPortabilityStatus(), NumberPortabilityStatus.foreignNumberPortedIn);
        assertNull(impl.getExtensionContainer());

    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {

        RouteingNumberImpl rn = new RouteingNumberImpl("09876");
        IMSIImpl imsi = new IMSIImpl("25033012345");
        ISDNAddressStringImpl isdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "11223344");

        MNPInfoResImpl impl = new MNPInfoResImpl(rn, imsi, isdn, NumberPortabilityStatus.foreignNumberPortedIn, null);
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();

        // TODO: fix a test
        // assertTrue( Arrays.equals(rawData,encodedData));
    }
}
