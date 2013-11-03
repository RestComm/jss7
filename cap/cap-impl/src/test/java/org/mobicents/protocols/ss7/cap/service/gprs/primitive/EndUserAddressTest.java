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
package org.mobicents.protocols.ss7.cap.service.gprs.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumberValue;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganizationValue;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class EndUserAddressTest {

    public byte[] getData() {
        return new byte[] { 48, 11, -128, 1, -15, -127, 1, 1, -126, 3, 4, 7, 7 };
    };

    public byte[] getPDPAddressData() {
        return new byte[] { 4, 7, 7 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        EndUserAddressImpl prim = new EndUserAddressImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(prim.getPDPTypeNumber().getPDPTypeNumberValue(), PDPTypeNumberValue.PPP);
        assertEquals(prim.getPDPTypeOrganization().getPDPTypeOrganizationValue(), PDPTypeOrganizationValue.ETSI);
        assertTrue(Arrays.equals(prim.getPDPAddress().getData(), this.getPDPAddressData()));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        PDPAddressImpl pdpAddress = new PDPAddressImpl(getPDPAddressData());
        PDPTypeNumberImpl pdpTypeNumber = new PDPTypeNumberImpl(PDPTypeNumberValue.PPP);
        PDPTypeOrganizationImpl pdpTypeOrganization = new PDPTypeOrganizationImpl(PDPTypeOrganizationValue.ETSI);

        EndUserAddressImpl prim = new EndUserAddressImpl(pdpTypeOrganization, pdpTypeNumber, pdpAddress);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }

}
