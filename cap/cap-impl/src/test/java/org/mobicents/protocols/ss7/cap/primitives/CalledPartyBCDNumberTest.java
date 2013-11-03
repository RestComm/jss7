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

package org.mobicents.protocols.ss7.cap.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CalledPartyBCDNumberTest {

    public byte[] getData1() {
        return new byte[] { (byte) 159, 56, 7, (byte) 145, 20, (byte) 135, 8, 80, 64, (byte) 247 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 159, 56, 6, (byte) 149, (byte) 232, 50, (byte) 155, (byte) 253, 6 };
    }

    public byte[] getIntData1() {
        return new byte[] { (byte) 145, 20, (byte) 135, 8, 80, 64, (byte) 247 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        CalledPartyBCDNumberImpl elem = new CalledPartyBCDNumberImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertTrue(Arrays.equals(elem.getData(), this.getIntData1()));
        assertEquals(elem.getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(elem.getAddress().equals("41788005047"));
        assertFalse(elem.isExtension());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new CalledPartyBCDNumberImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getNumberingPlan(), NumberingPlan.spare_5);
        assertTrue(elem.getAddress().equals("hello"));
        assertFalse(elem.isExtension());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        CalledPartyBCDNumberImpl elem = new CalledPartyBCDNumberImpl(this.getIntData1());
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 56);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        elem = new CalledPartyBCDNumberImpl(AddressNature.international_number, NumberingPlan.ISDN, "41788005047");
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 56);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        // GSM 7-bit default alphabet definition and the SMS packing rules
        elem = new CalledPartyBCDNumberImpl(AddressNature.international_number, NumberingPlan.spare_5, "hello");
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 56);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }

    @Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        CalledPartyBCDNumberImpl original = new CalledPartyBCDNumberImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "41788005047");

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "calledPartyBCDNumber", CalledPartyBCDNumberImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CalledPartyBCDNumberImpl copy = reader.read("calledPartyBCDNumber", CalledPartyBCDNumberImpl.class);

        assertEquals(copy.getAddressNature(), original.getAddressNature());
        assertEquals(copy.getNumberingPlan(), original.getNumberingPlan());
        assertEquals(copy.getAddress(), original.getAddress());
        assertEquals(copy.isExtension(), original.isExtension());

    }
}
