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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.isup.Digits;
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericDigits;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class VariablePartTest {

    public byte[] getData1() {
        return new byte[] { (byte) 128, 1, 17 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 129, 4, 96, 18, 17, 16 };
    }

    public byte[] getGenericDigitsData() {
        return new byte[] { 18, 17, 16 };
    }

    public byte[] getData3() {
        return new byte[] { (byte) 130, 2, 0, 52 };
    }

    public byte[] getData4() {
        return new byte[] { (byte) 131, 4, 2, 33, 48, 18 };
    }

    public byte[] getData5() {
        return new byte[] { (byte) 132, 4, 0, 1, 0, 0 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        VariablePartImpl elem = new VariablePartImpl();
        int tag = ais.readTag();
        assertEquals(tag, 0);
        elem.decodeAll(ais);
        assertEquals((int) elem.getInteger(), 17);
        assertNull(elem.getNumber());
        assertNull(elem.getTime());
        assertNull(elem.getDate());
        assertNull(elem.getPrice());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new VariablePartImpl();
        tag = ais.readTag();
        assertEquals(tag, 1);
        elem.decodeAll(ais);
        assertNull(elem.getInteger());
        assertEquals(elem.getNumber().getGenericDigits().getEncodingScheme(), 3);
        assertEquals(elem.getNumber().getGenericDigits().getTypeOfDigits(), 0);
        assertTrue(Arrays.equals(elem.getNumber().getGenericDigits().getEncodedDigits(), getGenericDigitsData()));
        assertNull(elem.getTime());
        assertNull(elem.getDate());
        assertNull(elem.getPrice());

        data = this.getData3();
        ais = new AsnInputStream(data);
        elem = new VariablePartImpl();
        tag = ais.readTag();
        assertEquals(tag, 2);
        elem.decodeAll(ais);
        assertNull(elem.getInteger());
        assertNull(elem.getNumber());
        assertEquals(elem.getTime().getHour(), 0);
        assertEquals(elem.getTime().getMinute(), 43);
        assertNull(elem.getDate());
        assertNull(elem.getPrice());

        data = this.getData4();
        ais = new AsnInputStream(data);
        elem = new VariablePartImpl();
        tag = ais.readTag();
        assertEquals(tag, 3);
        elem.decodeAll(ais);
        assertNull(elem.getInteger());
        assertNull(elem.getNumber());
        assertNull(elem.getTime());
        assertEquals(elem.getDate().getYear(), 2012);
        assertEquals(elem.getDate().getMonth(), 3);
        assertEquals(elem.getDate().getDay(), 21);
        assertNull(elem.getPrice());

        data = this.getData5();
        ais = new AsnInputStream(data);
        elem = new VariablePartImpl();
        tag = ais.readTag();
        assertEquals(tag, 4);
        elem.decodeAll(ais);
        assertNull(elem.getInteger());
        assertNull(elem.getNumber());
        assertNull(elem.getTime());
        assertNull(elem.getDate());
        assertEquals(elem.getPrice().getPriceIntegerPart(), 1000);
        assertEquals(elem.getPrice().getPriceHundredthPart(), 0);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        VariablePartImpl elem = new VariablePartImpl(17);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        GenericDigitsImpl genericDigits = new GenericDigitsImpl(3, 0, getGenericDigitsData());
        // int encodingScheme, int typeOfDigits, int[] digits
        DigitsImpl digits = new DigitsImpl(genericDigits);
        elem = new VariablePartImpl(digits);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        VariablePartTimeImpl time = new VariablePartTimeImpl(0, 43);
        elem = new VariablePartImpl(time);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));

        VariablePartDateImpl date = new VariablePartDateImpl(2012, 3, 21);
        elem = new VariablePartImpl(date);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData4()));

        VariablePartPriceImpl price = new VariablePartPriceImpl(1000, 0);
        elem = new VariablePartImpl(price);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData5()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        Integer integer = 15;
        VariablePartImpl original = new VariablePartImpl(integer);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "variablePart", VariablePartImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        VariablePartImpl copy = reader.read("variablePart", VariablePartImpl.class);

        assertEquals(copy.getInteger(), original.getInteger());
        assertNull(copy.getNumber());
        assertNull(copy.getTime());
        assertNull(copy.getDate());
        assertNull(copy.getPrice());


        int encodingScheme = GenericDigits._ENCODING_SCHEME_BCD_ODD;
        int typeOfDigits = GenericDigits._TOD_BGCI;
        byte[] digits = new byte[] { 35, 0x21, 0x43, 0x65 };
        GenericDigits genericDigits = new GenericDigitsImpl(encodingScheme, typeOfDigits, digits);
        Digits number = new DigitsImpl(genericDigits);
        original = new VariablePartImpl(number);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "variablePart", VariablePartImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("variablePart", VariablePartImpl.class);

        assertNull(copy.getInteger());
        assertEquals(copy.getNumber().getGenericDigits().getEncodingScheme(), encodingScheme);
        assertEquals(copy.getNumber().getGenericDigits().getTypeOfDigits(), typeOfDigits);
        assertEquals(copy.getNumber().getGenericDigits().getEncodedDigits(), digits);
        assertNull(copy.getTime());
        assertNull(copy.getDate());
        assertNull(copy.getPrice());


        int hour = 2;
        int minute = 14;
        VariablePartTimeImpl time = new VariablePartTimeImpl(hour, minute);
        original = new VariablePartImpl(time);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "variablePart", VariablePartImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("variablePart", VariablePartImpl.class);

        assertNull(copy.getInteger());
        assertNull(copy.getNumber());
        assertEquals(copy.getTime().getHour(), hour);
        assertEquals(copy.getTime().getMinute(), minute);
        assertNull(copy.getDate());
        assertNull(copy.getPrice());


        int year = 2014;
        int month = 4;
        int day = 16;
        VariablePartDateImpl date = new VariablePartDateImpl(year, month, day);
        original = new VariablePartImpl(date);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "variablePart", VariablePartImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("variablePart", VariablePartImpl.class);

        assertNull(copy.getInteger());
        assertNull(copy.getNumber());
        assertNull(copy.getTime());
        assertEquals(copy.getDate().getYear(), year);
        assertEquals(copy.getDate().getMonth(), month);
        assertEquals(copy.getDate().getDay(), day);
        assertNull(copy.getPrice());


        int integerPart = 234;
        int hundredthPart = 21;
        VariablePartPriceImpl price = new VariablePartPriceImpl(integerPart, hundredthPart);
        original = new VariablePartImpl(price);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "variablePart", VariablePartImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("variablePart", VariablePartImpl.class);

        assertNull(copy.getInteger());
        assertNull(copy.getNumber());
        assertNull(copy.getTime());
        assertNull(copy.getDate());
        assertEquals(copy.getPrice().getPriceIntegerPart(), integerPart);
        assertEquals(copy.getPrice().getPriceHundredthPart(), hundredthPart);
    }
}
