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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SupportedFeaturesTest {

    public byte[] getData() {
        return new byte[] { 3, 5, 6, 85, 85, 85, 64 };
    };

    public byte[] getData1() {
        return new byte[] { 3, 5, 6, -86, -86, -86, -128 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        // test one
        byte[] data = this.getData();

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        SupportedFeaturesImpl prim = new SupportedFeaturesImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(!prim.getOdbAllApn());
        assertTrue(prim.getOdbHPLMNApn());
        assertTrue(!prim.getOdbVPLMNApn());
        assertTrue(prim.getOdbAllOg());
        assertTrue(!prim.getOdbAllInternationalOg());
        assertTrue(prim.getOdbAllIntOgNotToHPLMNCountry());
        assertTrue(!prim.getOdbAllInterzonalOg());
        assertTrue(prim.getOdbAllInterzonalOgNotToHPLMNCountry());
        assertTrue(!prim.getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry());
        assertTrue(prim.getRegSub());
        assertTrue(!prim.getTrace());
        assertTrue(prim.getLcsAllPrivExcep());
        assertTrue(!prim.getLcsUniversal());
        assertTrue(prim.getLcsCallSessionRelated());
        assertTrue(!prim.getLcsCallSessionUnrelated());
        assertTrue(prim.getLcsPLMNOperator());
        assertTrue(!prim.getLcsServiceType());
        assertTrue(prim.getLcsAllMOLRSS());
        assertTrue(!prim.getLcsBasicSelfLocation());
        assertTrue(prim.getLcsAutonomousSelfLocation());
        assertTrue(!prim.getLcsTransferToThirdParty());
        assertTrue(prim.getSmMoPp());
        assertTrue(!prim.getBarringOutgoingCalls());
        assertTrue(prim.getBaoc());
        assertTrue(!prim.getBoic());
        assertTrue(prim.getBoicExHC());

        // test two
        data = this.getData1();

        asn = new AsnInputStream(data);
        tag = asn.readTag();

        prim = new SupportedFeaturesImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_BIT);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(prim.getOdbAllApn());
        assertTrue(!prim.getOdbHPLMNApn());
        assertTrue(prim.getOdbVPLMNApn());
        assertTrue(!prim.getOdbAllOg());
        assertTrue(prim.getOdbAllInternationalOg());
        assertTrue(!prim.getOdbAllIntOgNotToHPLMNCountry());
        assertTrue(prim.getOdbAllInterzonalOg());
        assertTrue(!prim.getOdbAllInterzonalOgNotToHPLMNCountry());
        assertTrue(prim.getOdbAllInterzonalOgandInternatOgNotToHPLMNCountry());
        assertTrue(!prim.getRegSub());
        assertTrue(prim.getTrace());
        assertTrue(!prim.getLcsAllPrivExcep());
        assertTrue(prim.getLcsUniversal());
        assertTrue(!prim.getLcsCallSessionRelated());
        assertTrue(prim.getLcsCallSessionUnrelated());
        assertTrue(!prim.getLcsPLMNOperator());
        assertTrue(prim.getLcsServiceType());
        assertTrue(!prim.getLcsAllMOLRSS());
        assertTrue(prim.getLcsBasicSelfLocation());
        assertTrue(!prim.getLcsAutonomousSelfLocation());
        assertTrue(prim.getLcsTransferToThirdParty());
        assertTrue(!prim.getSmMoPp());
        assertTrue(prim.getBarringOutgoingCalls());
        assertTrue(!prim.getBaoc());
        assertTrue(prim.getBoic());
        assertTrue(!prim.getBoicExHC());

    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testEncode() throws Exception {
        // Test one
        SupportedFeaturesImpl prim = new SupportedFeaturesImpl(false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        // Tes two
        prim = new SupportedFeaturesImpl(true, false, true, false, true, false, true, false, true, false, true, false, true,
                false, true, false, true, false, true, false, true, false, true, false, true, false);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

    }

}
