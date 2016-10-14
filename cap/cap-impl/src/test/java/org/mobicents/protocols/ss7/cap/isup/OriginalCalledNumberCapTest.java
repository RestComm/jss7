/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.cap.isup;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.OriginalCalledNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledNumber;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class OriginalCalledNumberCapTest {

    public byte[] getData() {
        return new byte[] { (byte) 140, 6, (byte) 131, 20, 7, 1, 9, 0 };
    }

    public byte[] getIntData() {
        return new byte[] { (byte) 131, 20, 7, 1, 9, 0 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 140, 11, 4, 16, 76, (byte) 152, 8, (byte) 148, 113, 7, 41, (byte) 146, 115 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {

        byte[] data = this.getData();
        AsnInputStream ais = new AsnInputStream(data);
        OriginalCalledNumberCapImpl elem = new OriginalCalledNumberCapImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        OriginalCalledNumber ocn = elem.getOriginalCalledNumber();
        assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
        assertEquals(ocn.getNatureOfAddressIndicator(), 3);
        assertTrue(ocn.getAddress().equals("7010900"));
        assertEquals(ocn.getNumberingPlanIndicator(), 1);
        assertEquals(ocn.getAddressRepresentationRestrictedIndicator(), 1);

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new OriginalCalledNumberCapImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        ocn = elem.getOriginalCalledNumber();
        assertEquals(ocn.getNumberingPlanIndicator(), 1);
        assertEquals(ocn.getAddressRepresentationRestrictedIndicator(), 0);
        assertEquals(ocn.getNatureOfAddressIndicator(), 4);
        assertEquals(ocn.getAddress(), "c48980491770922937");
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {

        OriginalCalledNumberCapImpl elem = new OriginalCalledNumberCapImpl(this.getIntData());
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 12);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

        OriginalCalledNumber cpn = new OriginalCalledNumberImpl(3, "7010900", 1, 1);
        elem = new OriginalCalledNumberCapImpl(cpn);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 12);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

        cpn = new OriginalCalledNumberImpl(4, "c48980491770922937", 1, 0);
        elem = new OriginalCalledNumberCapImpl(cpn);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 12);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int addressRepresentationREstrictedIndicator
    }

    @Test(groups = { "functional.xml.serialize", "isup" })
    public void testXMLSerialize() throws Exception {

        OriginalCalledNumberCapImpl original = new OriginalCalledNumberCapImpl(new OriginalCalledNumberImpl(
                OriginalCalledNumber._NAI_NATIONAL_SN, "12345", OriginalCalledNumber._NPI_TELEX,
                OriginalCalledNumber._APRI_RESTRICTED));

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "originalCalledNumberCap", OriginalCalledNumberCapImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        OriginalCalledNumberCapImpl copy = reader.read("originalCalledNumberCap", OriginalCalledNumberCapImpl.class);

        assertEquals(copy.getOriginalCalledNumber().getNatureOfAddressIndicator(), original.getOriginalCalledNumber()
                .getNatureOfAddressIndicator());
        assertEquals(copy.getOriginalCalledNumber().getAddress(), original.getOriginalCalledNumber().getAddress());
        assertEquals(copy.getOriginalCalledNumber().getNumberingPlanIndicator(), original.getOriginalCalledNumber()
                .getNumberingPlanIndicator());
        assertEquals(copy.getOriginalCalledNumber().getAddressRepresentationRestrictedIndicator(), original
                .getOriginalCalledNumber().getAddressRepresentationRestrictedIndicator());

    }
}
