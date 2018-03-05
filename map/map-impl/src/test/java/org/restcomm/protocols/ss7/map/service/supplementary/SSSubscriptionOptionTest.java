/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.service.supplementary.CliRestrictionOption;
import org.restcomm.protocols.ss7.map.api.service.supplementary.OverrideCategory;
import org.restcomm.protocols.ss7.map.service.supplementary.SSSubscriptionOptionImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SSSubscriptionOptionTest {

    private byte[] getData1() {
        return new byte[] { -126, 1, 0 };
    }

    private byte[] getData2() {
        return new byte[] { -127, 1, 1 };
    }

    @Test(groups = { "functional.decode", "service.supplementary" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        SSSubscriptionOptionImpl prim = new SSSubscriptionOptionImpl();
        prim.decodeAll(asn);

        assertEquals(tag, SSSubscriptionOptionImpl._TAG_cliRestrictionOption);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        assertNotNull(prim.getCliRestrictionOption());
        assertNull(prim.getOverrideCategory());
        assertTrue(prim.getCliRestrictionOption().getCode() == CliRestrictionOption.permanent.getCode());

        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new SSSubscriptionOptionImpl();
        prim.decodeAll(asn);

        assertEquals(tag, SSSubscriptionOptionImpl._TAG_overrideCategory);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        assertNull(prim.getCliRestrictionOption());
        assertNotNull(prim.getOverrideCategory());
        assertTrue(prim.getOverrideCategory().getCode() == OverrideCategory.overrideDisabled.getCode());

    }

    @Test(groups = { "functional.encode", "service.supplementary" })
    public void testEncode() throws Exception {

        SSSubscriptionOptionImpl impl = new SSSubscriptionOptionImpl(CliRestrictionOption.permanent);
        AsnOutputStream asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);
        assertEquals(asnOS.toByteArray(), this.getData1());

        impl = new SSSubscriptionOptionImpl(OverrideCategory.overrideDisabled);
        asnOS = new AsnOutputStream();

        impl.encodeAll(asnOS);
        assertEquals(asnOS.toByteArray(), this.getData2());

    }
}
