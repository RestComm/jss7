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
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class DigitsTest {

    public byte[] getData1() {
        return new byte[] { (byte) 157, 5, 65, 5, 6, 7, 8 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 157, 7, 3, (byte) 132, 33, 7, 1, 9, 0 };
    }

    public byte[] getGenericDigitsInt() {
        return new byte[] { 5, 6, 7, 8 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        DigitsImpl elem = new DigitsImpl();
        int tag = ais.readTag();
        assertEquals(tag, 29);
        elem.decodeAll(ais);
        elem.setIsGenericDigits();
        GenericDigits gd = elem.getGenericDigits();
        assertEquals(gd.getEncodingScheme(), 2);
        assertEquals(gd.getTypeOfDigits(), 1);
        assertTrue(Arrays.equals(gd.getEncodedDigits(), getGenericDigitsInt()));

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new DigitsImpl();
        tag = ais.readTag();
        assertEquals(tag, 29);
        elem.decodeAll(ais);
        elem.setIsGenericNumber();
        GenericNumber gn = elem.getGenericNumber();
        assertEquals(gn.getNatureOfAddressIndicator(), 4);
        assertTrue(gn.getAddress().equals("7010900"));
        assertEquals(gn.getNumberQualifierIndicator(), 3);
        assertEquals(gn.getNumberingPlanIndicator(), 2);
        assertEquals(gn.getAddressRepresentationRestrictedIndicator(), 0);
        assertEquals(gn.getScreeningIndicator(), 1);
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {

        GenericDigitsImpl genericDigits = new GenericDigitsImpl(2, 1, getGenericDigitsInt());
        DigitsImpl elem = new DigitsImpl(genericDigits);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 29);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
        // int encodingScheme, int typeOfDigits, int[] digits

        GenericNumber rn = new GenericNumberImpl(4, "7010900", 3, 2, 0, false, 1);
        elem = new DigitsImpl(rn);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 29);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
        // int natureOfAddresIndicator, String address, int numberQualifierIndicator, int numberingPlanIndicator, int
        // addressRepresentationREstrictedIndicator,
        // boolean numberIncomplete, int screeningIndicator
    }

    private byte[] getEncodedData() {
        return new byte[] { 0x21, 0x43, 0x65 };
    }

    @Test(groups = { "functional.xml.serialize", "isup" })
    public void testXMLSerialize() throws Exception {

        GenericDigitsImpl gd = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BCD_ODD, GenericDigits._TOD_BGCI,
                getEncodedData());
        DigitsImpl original = new DigitsImpl(gd);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "digits", DigitsImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        DigitsImpl copy = reader.read("digits", DigitsImpl.class);

        assertEquals(copy.getIsGenericDigits(), original.getIsGenericDigits());
        assertEquals(copy.getIsGenericNumber(), original.getIsGenericNumber());

        assertEquals(copy.getGenericDigits().getEncodingScheme(), original.getGenericDigits().getEncodingScheme());
        assertEquals(copy.getGenericDigits().getTypeOfDigits(), original.getGenericDigits().getTypeOfDigits());
        assertEquals(copy.getGenericDigits().getEncodedDigits(), original.getGenericDigits().getEncodedDigits());

        GenericNumberImpl gn = new GenericNumberImpl(GenericNumber._NAI_NATIONAL_SN, "12345",
                GenericNumber._NQIA_CONNECTED_NUMBER, GenericNumber._NPI_TELEX, GenericNumber._APRI_ALLOWED,
                GenericNumber._NI_INCOMPLETE, GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        original = new DigitsImpl(gn);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "digits", DigitsImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("digits", DigitsImpl.class);

        assertEquals(copy.getIsGenericDigits(), original.getIsGenericDigits());
        assertEquals(copy.getIsGenericNumber(), original.getIsGenericNumber());

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
