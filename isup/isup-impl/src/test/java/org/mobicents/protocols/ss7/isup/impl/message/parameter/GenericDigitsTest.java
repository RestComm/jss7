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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class GenericDigitsTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    private byte[] getData() {
        return new byte[] { 35, 0x21, 0x43, 0x65 }; // "123456" EVEN
    }

    private byte[] getEncodedData() {
        return new byte[] { 0x21, 0x43, 0x65 };
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecode() throws Exception {

        GenericDigitsImpl prim = new GenericDigitsImpl();
        prim.decode(getData());

        assertEquals(prim.getEncodingScheme(), GenericDigits._ENCODING_SCHEME_BCD_ODD);
        assertEquals(prim.getTypeOfDigits(), GenericDigits._TOD_BGCI);
        assertEquals(prim.getEncodedDigits(), getEncodedData());
    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncode() throws Exception {

        GenericDigitsImpl prim = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BCD_ODD, GenericDigits._TOD_BGCI,
                getEncodedData());
        // int encodingScheme, int typeOfDigits, byte[] digits

        byte[] data = getData();
        byte[] encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));

    }

    @Test(groups = { "functional.xml.serialize", "parameter" })
    public void testXMLSerialize() throws Exception {

        GenericDigitsImpl original = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BCD_ODD, GenericDigits._TOD_BGCI,
                getEncodedData());

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "genericDigits", GenericDigitsImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        GenericDigitsImpl copy = reader.read("genericDigits", GenericDigitsImpl.class);

        assertEquals(copy.getEncodingScheme(), original.getEncodingScheme());
        assertEquals(copy.getTypeOfDigits(), original.getTypeOfDigits());
        assertEquals(copy.getEncodedDigits(), original.getEncodedDigits());
    }
}
