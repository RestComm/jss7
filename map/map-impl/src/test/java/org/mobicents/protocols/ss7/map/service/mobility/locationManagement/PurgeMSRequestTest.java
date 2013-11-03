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
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.testng.annotations.Test;

/**
*
* @author Lasith Waruna Perera
*
*/
public class PurgeMSRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 13, 4, 5, 17, 17, 33, 34, 34, 4, 4, -111, 34, 50, -12 };
    };

    public byte[] getData2() {
        return new byte[] { -93, 60, 4, 5, 17, 17, 33, 34, 34, -128, 4, -111, 34, 50, -12, -127, 4, -111, 34, 50, -11, 48, 39,
                -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23,
                24, 25, 26, -95, 3, 31, 32, 33 };
    };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
        // version 2
        byte[] data = this.getData1();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        PurgeMSRequestImpl prim = new PurgeMSRequestImpl(2);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(prim.getImsi().getData().equals("1111122222"));

        ISDNAddressString vlrNumber = prim.getVlrNumber();
        assertTrue(vlrNumber.getAddress().equals("22234"));
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);

        // version 3
        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();

        prim = new PurgeMSRequestImpl(3);
        prim.decodeAll(asn);

        assertEquals(tag, PurgeMSRequestImpl._TAG_PurgeMSRequest);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);

        assertTrue(prim.getImsi().getData().equals("1111122222"));

        vlrNumber = prim.getVlrNumber();
        assertTrue(vlrNumber.getAddress().equals("22234"));
        assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);

        ISDNAddressString sgsnNumber = prim.getSgsnNumber();
        assertTrue(sgsnNumber.getAddress().equals("22235"));
        assertEquals(sgsnNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(sgsnNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
        // version 2
        ISDNAddressString vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        IMSIImpl imsi = new IMSIImpl("1111122222");

        PurgeMSRequestImpl prim = new PurgeMSRequestImpl(imsi, vlrNumber, null, extensionContainer, 2);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

        // version 3
        ISDNAddressString sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");
        prim = new PurgeMSRequestImpl(imsi, vlrNumber, sgsnNumber, extensionContainer, 3);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));
    }
}
