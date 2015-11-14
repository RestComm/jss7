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

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class MidCallControlInfoTest {

    public byte[] getData1() {
        return new byte[] { 48, 3, (byte) 128, 1, 3 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 20, (byte) 128, 1, 3, (byte) 129, 1, 4, (byte) 130, 2, 1, 10, (byte) 131, 1, 11, (byte) 132, 2, 0, 9, (byte) 134, 1, 100 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        MidCallControlInfoImpl elem = new MidCallControlInfoImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);

        elem.decodeAll(ais);
        assertEquals((int) elem.getMinimumNumberOfDigits(), 3);
        assertNull(elem.getMaximumNumberOfDigits());
        assertNull(elem.getEndOfReplyDigit());
        assertNull(elem.getCancelDigit());
        assertNull(elem.getStartDigit());
        assertNull(elem.getInterDigitTimeout());


        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new MidCallControlInfoImpl();
        tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);

        elem.decodeAll(ais);
        assertEquals((int) elem.getMinimumNumberOfDigits(), 3);
        assertEquals((int) elem.getMaximumNumberOfDigits(), 4);
        assertEquals(elem.getEndOfReplyDigit(), "1*");
        assertEquals(elem.getCancelDigit(), "#");
        assertEquals(elem.getStartDigit(), "09");
        assertEquals((int) elem.getInterDigitTimeout(), 100);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        MidCallControlInfoImpl elem = new MidCallControlInfoImpl(3, null, null, null, null, null);
//        Integer minimumNumberOfDigits, Integer maximumNumberOfDigits, String endOfReplyDigit,
//        String cancelDigit, String startDigit, Integer interDigitTimeout
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData1());


        elem = new MidCallControlInfoImpl(3, 4, "1*", "#", "09", 100);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertEquals(aos.toByteArray(), this.getData2());
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerializaion() throws Exception {
        MidCallControlInfoImpl original = new MidCallControlInfoImpl(3, null, null, null, null, null);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "midCallEvents", MidCallControlInfoImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        MidCallControlInfoImpl copy = reader.read("midCallEvents", MidCallControlInfoImpl.class);

        assertEquals((int) copy.getMinimumNumberOfDigits(), (int) original.getMinimumNumberOfDigits());
        assertNull(copy.getMaximumNumberOfDigits());
        assertNull(copy.getEndOfReplyDigit());
        assertNull(copy.getCancelDigit());
        assertNull(copy.getStartDigit());
        assertNull(copy.getInterDigitTimeout());


        original = new MidCallControlInfoImpl(3, 4, "1*", "#", "09", 100);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "midCallEvents", MidCallControlInfoImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("midCallEvents", MidCallControlInfoImpl.class);

        assertEquals((int) copy.getMinimumNumberOfDigits(), (int) original.getMinimumNumberOfDigits());
        assertEquals((int) copy.getMaximumNumberOfDigits(), (int) original.getMaximumNumberOfDigits());
        assertEquals(copy.getEndOfReplyDigit(), original.getEndOfReplyDigit());
        assertEquals(copy.getCancelDigit(), original.getCancelDigit());
        assertEquals(copy.getStartDigit(), original.getStartDigit());
        assertEquals((int) copy.getInterDigitTimeout(), (int) original.getInterDigitTimeout());
    }

}
