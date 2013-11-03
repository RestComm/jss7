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
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class GenericNumberCapTest {

    public byte[] getData() {
        return new byte[] { (byte) 157, 7, 1, (byte) 131, 20, 7, 1, 9, 0 };
    }

    public byte[] getIntData() {
        return new byte[] { 1, -125, 20, 7, 1, 9, 0 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {

        byte[] data = this.getData();
        AsnInputStream ais = new AsnInputStream(data);
        GenericNumberCapImpl elem = new GenericNumberCapImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        GenericNumber gn = elem.getGenericNumber();
        assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
        assertEquals(gn.getNatureOfAddressIndicator(), 3);
        assertTrue(gn.getAddress().equals("7010900"));
        assertEquals(gn.getNumberingPlanIndicator(), 1);
        assertEquals(gn.getAddressRepresentationRestrictedIndicator(), 1);
        assertEquals(gn.getNumberQualifierIndicator(), 1);
        assertEquals(gn.getScreeningIndicator(), 0);
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {

        GenericNumberCapImpl elem = new GenericNumberCapImpl(this.getIntData());
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 29);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

        GenericNumber rn = new GenericNumberImpl(3, "7010900", 1, 1, 1, false, 0);
        elem = new GenericNumberCapImpl(rn);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 29);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

        // int natureOfAddresIndicator, String address, int numberQualifierIndicator, int numberingPlanIndicator, int
        // addressRepresentationREstrictedIndicator,
        // boolean numberIncomplete, int screeningIndicator
    }

    @Test(groups = { "functional.xml.serialize", "isup" })
    public void testXMLSerialize() throws Exception {

        GenericNumberImpl gn = new GenericNumberImpl(GenericNumber._NAI_NATIONAL_SN, "12345",
                GenericNumber._NQIA_CONNECTED_NUMBER, GenericNumber._NPI_TELEX, GenericNumber._APRI_ALLOWED,
                GenericNumber._NI_INCOMPLETE, GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        GenericNumberCapImpl original = new GenericNumberCapImpl(gn);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "genericNumberCap", GenericNumberCapImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        GenericNumberCapImpl copy = reader.read("genericNumberCap", GenericNumberCapImpl.class);

        assertEquals(copy.getGenericNumber().getNatureOfAddressIndicator(), original.getGenericNumber()
                .getNatureOfAddressIndicator());
        assertEquals(copy.getGenericNumber().getAddress(), original.getGenericNumber().getAddress());
        assertEquals(copy.getGenericNumber().getNumberQualifierIndicator(), original.getGenericNumber()
                .getNumberQualifierIndicator());
        assertEquals(copy.getGenericNumber().getNumberingPlanIndicator(), original.getGenericNumber()
                .getNumberingPlanIndicator());
        assertEquals(copy.getGenericNumber().isNumberIncomplete(), original.getGenericNumber().isNumberIncomplete());
        assertEquals(copy.getGenericNumber().getAddressRepresentationRestrictedIndicator(), original.getGenericNumber()
                .getAddressRepresentationRestrictedIndicator());
        assertEquals(copy.getGenericNumber().getScreeningIndicator(), original.getGenericNumber().getScreeningIndicator());
        assertEquals(copy.getGenericNumber().isOddFlag(), original.getGenericNumber().isOddFlag());

    }
}
