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
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

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
public class TimeAndTimezoneTest {

    public byte[] getData1() {
        return new byte[] { (byte) 159, 57, 8, 2, 17, 33, 3, 1, 112, (byte) 129, 35 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 159, 57, 8, 2, 17, 33, 3, 1, 112, (byte) 129, 43 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        TimeAndTimezoneImpl elem = new TimeAndTimezoneImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getYear(), 2011);
        assertEquals(elem.getMonth(), 12);
        assertEquals(elem.getDay(), 30);
        assertEquals(elem.getHour(), 10);
        assertEquals(elem.getMinute(), 7);
        assertEquals(elem.getSecond(), 18);
        assertEquals(elem.getTimeZone(), 32);

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new TimeAndTimezoneImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getYear(), 2011);
        assertEquals(elem.getMonth(), 12);
        assertEquals(elem.getDay(), 30);
        assertEquals(elem.getHour(), 10);
        assertEquals(elem.getMinute(), 7);
        assertEquals(elem.getSecond(), 18);
        assertEquals(elem.getTimeZone(), -32);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        TimeAndTimezoneImpl elem = new TimeAndTimezoneImpl(2011, 12, 30, 10, 7, 18, 32);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 57);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        elem = new TimeAndTimezoneImpl(2011, 12, 30, 10, 7, 18, -32);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 57);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }

    @Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        TimeAndTimezoneImpl original = new TimeAndTimezoneImpl(2011, 12, 30, 10, 7, 18, 32);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "timeAndTimezone", TimeAndTimezoneImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        TimeAndTimezoneImpl copy = reader.read("timeAndTimezone", TimeAndTimezoneImpl.class);

        assertEquals(copy.getYear(), original.getYear());
        assertEquals(copy.getMonth(), original.getMonth());
        assertEquals(copy.getDay(), original.getDay());
        assertEquals(copy.getHour(), original.getHour());
        assertEquals(copy.getMinute(), original.getMinute());
        assertEquals(copy.getSecond(), original.getSecond());
        assertEquals(copy.getTimeZone(), original.getTimeZone());

    }
}
