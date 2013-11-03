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

package org.mobicents.protocols.ss7.inap.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfoDpAssignment;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfoMessageType;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MiscCallInfoTest {

    private byte[] getData1() {
        return new byte[] { (byte) 164, 3, (byte) 128, 1, 1 };
    }

    private byte[] getData2() {
        return new byte[] { (byte) 164, 6, (byte) 128, 1, 0, (byte) 129, 1, 0 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        MiscCallInfoImpl elem = new MiscCallInfoImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertNotNull(elem.getMessageType());
        assertNull(elem.getDpAssignment());
        assertEquals(elem.getMessageType(), MiscCallInfoMessageType.notification);

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new MiscCallInfoImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertNotNull(elem.getMessageType());
        assertNotNull(elem.getDpAssignment());
        assertEquals(elem.getMessageType(), MiscCallInfoMessageType.request);
        assertEquals(elem.getDpAssignment(), MiscCallInfoDpAssignment.individualLine);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        MiscCallInfoImpl elem = new MiscCallInfoImpl(MiscCallInfoMessageType.notification, null);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 4);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        elem = new MiscCallInfoImpl(MiscCallInfoMessageType.request, MiscCallInfoDpAssignment.individualLine);
        aos.reset();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 4);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerializaion() throws Exception {
        MiscCallInfoImpl original = new MiscCallInfoImpl(MiscCallInfoMessageType.notification, null);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "miscCallInfo", MiscCallInfoImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        MiscCallInfoImpl copy = reader.read("miscCallInfo", MiscCallInfoImpl.class);

        assertEquals(copy.getMessageType(), original.getMessageType());
        assertNull(copy.getDpAssignment());
        assertNull(original.getDpAssignment());

        original = new MiscCallInfoImpl(MiscCallInfoMessageType.request, MiscCallInfoDpAssignment.individualLine);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "miscCallInfo", MiscCallInfoImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("miscCallInfo", MiscCallInfoImpl.class);

        assertEquals(copy.getMessageType(), original.getMessageType());
        assertEquals(copy.getDpAssignment(), original.getDpAssignment());

    }
}
