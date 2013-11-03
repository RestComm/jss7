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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.FQDN;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWIdentity;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SpecificAPNInfoTest {

    public byte[] getData() {
        return new byte[] { 48, 110, -128, 2, 11, 12, -95, 63, -128, 3, 5, 6, 7, -127, 3, 5, 6, 7, -126, 10, 4, 1, 6, 8, 3, 2,
                5, 6, 1, 7, -93, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3,
                42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14,
                15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
    };

    public byte[] getPDPAddressData() {
        return new byte[] { 5, 6, 7 };
    };

    public byte[] getFQDNData() {
        return new byte[] { 4, 1, 6, 8, 3, 2, 5, 6, 1, 7 };
    };

    public byte[] getDataAPN() {
        return new byte[] { 11, 12 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        SpecificAPNInfoImpl prim = new SpecificAPNInfoImpl();

        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        PDNGWIdentity pdnGWIdentity = prim.getPdnGwIdentity();
        PDPAddress pdnGwIpv4Address = pdnGWIdentity.getPdnGwIpv4Address();
        assertNotNull(pdnGwIpv4Address);
        assertTrue(Arrays.equals(this.getPDPAddressData(), pdnGwIpv4Address.getData()));
        PDPAddress pdnGwIpv6Address = pdnGWIdentity.getPdnGwIpv6Address();
        assertNotNull(pdnGwIpv6Address);
        assertTrue(Arrays.equals(this.getPDPAddressData(), pdnGwIpv6Address.getData()));
        FQDN pdnGwName = pdnGWIdentity.getPdnGwName();
        assertNotNull(pdnGwName);
        assertTrue(Arrays.equals(this.getFQDNData(), pdnGwName.getData()));
        assertNotNull(pdnGWIdentity.getExtensionContainer());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdnGWIdentity.getExtensionContainer()));

        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

        assertEquals(prim.getAPN().getData(), this.getDataAPN());

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        APN apn = new APNImpl(this.getDataAPN());
        PDPAddress pdnGwIpv4Address = new PDPAddressImpl(this.getPDPAddressData());
        PDPAddress pdnGwIpv6Address = new PDPAddressImpl(this.getPDPAddressData());
        FQDN pdnGwName = new FQDNImpl(this.getFQDNData());
        PDNGWIdentityImpl pdnGwIdentity = new PDNGWIdentityImpl(pdnGwIpv4Address, pdnGwIpv6Address, pdnGwName,
                extensionContainer);
        SpecificAPNInfoImpl prim = new SpecificAPNInfoImpl(apn, pdnGwIdentity, extensionContainer);
        AsnOutputStream asn = new AsnOutputStream();

        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }
}
