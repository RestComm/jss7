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
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.primitives.TMSIImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SendIdentificationRequestTest {

    public byte[] getData1() {
        return new byte[] { 4, 4, 1, 2, 3, 4 };
    };

    public byte[] getData2() {
        return new byte[] { 48, 82, 4, 4, 1, 2, 3, 4, 2, 1, 2, 5, 0, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14,
                15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 4, 4, -111, 34,
                50, -12, -128, 5, 16, 97, 66, 1, 77, -127, 1, 4, -126, 0, -125, 4, -111, 34, 50, -11, -124, 4, 1, 2, 3, 4 };
    };

    public byte[] getDataTmsi() {
        return new byte[] { 1, 2, 3, 4 };
    };

    public byte[] getDataLmsi() {
        return new byte[] { 1, 2, 3, 4 };
    };

    @Test(groups = { "functional.decode" })
    public void testDecode() throws Exception {
        // version 2
        byte[] data = this.getData1();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        SendIdentificationRequestImpl prim = new SendIdentificationRequestImpl(2);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.STRING_OCTET);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(Arrays.equals(prim.getTmsi().getData(), getDataTmsi()));

        // version 3
        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();

        prim = new SendIdentificationRequestImpl(3);
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertTrue(Arrays.equals(prim.getTmsi().getData(), getDataTmsi()));
        assertTrue(prim.getNumberOfRequestedVectors().equals(2));
        assertTrue(prim.getSegmentationProhibited());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));
        ISDNAddressString mscNumber = prim.getMscNumber();
        assertTrue(mscNumber.getAddress().equals("22234"));
        assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertEquals(prim.getPreviousLAI().getMCC(), 11);
        assertEquals(prim.getPreviousLAI().getMNC(), 246);
        assertEquals(prim.getPreviousLAI().getLac(), 333);

        assertTrue(prim.getHopCounter().equals(4));
        assertTrue(prim.getMtRoamingForwardingSupported());

        ISDNAddressString newVLRNumber = prim.getNewVLRNumber();
        assertTrue(newVLRNumber.getAddress().equals("22235"));
        assertEquals(newVLRNumber.getAddressNature(), AddressNature.international_number);
        assertEquals(newVLRNumber.getNumberingPlan(), NumberingPlan.ISDN);

        assertTrue(Arrays.equals(prim.getNewLmsi().getData(), getDataLmsi()));
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(prim.getExtensionContainer()));

    }

    @Test(groups = { "functional.encode" })
    public void testEncode() throws Exception {
        // version 2
        TMSIImpl tmsi = new TMSIImpl(getDataTmsi());
        SendIdentificationRequestImpl prim = new SendIdentificationRequestImpl(tmsi, 2);

        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

        // version 3
        tmsi = new TMSIImpl(getDataTmsi());
        Integer numberOfRequestedVectors = 2;
        boolean segmentationProhibited = true;
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
        ISDNAddressString mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");

        LAIFixedLength previousLAI = new LAIFixedLengthImpl(11, 246, 333);
        Integer hopCounter = 4;
        boolean mtRoamingForwardingSupported = true;
        ISDNAddressString newVLRNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "22235");
        LMSI lmsi = new LMSIImpl(getDataLmsi());
        prim = new SendIdentificationRequestImpl(tmsi, numberOfRequestedVectors, segmentationProhibited, extensionContainer,
                mscNumber, previousLAI, hopCounter, mtRoamingForwardingSupported, newVLRNumber, lmsi, 3);

        asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));
    }
}
