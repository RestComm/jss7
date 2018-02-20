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

package org.restcomm.protocols.ss7.map.service.callhandling;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UUI;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UUIndicator;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.service.callhandling.UUDataImpl;
import org.restcomm.protocols.ss7.map.service.callhandling.UUIImpl;
import org.restcomm.protocols.ss7.map.service.callhandling.UUIndicatorImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class UUDataTest {

    public byte[] getData1() {
        return new byte[] { 48, 3, (byte) 128, 1, (byte) 140 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 53, (byte) 128, 1, (byte) 140, (byte) 129, 5, 1, 2, 3, 4, 5, (byte) 130, 0, (byte) 163, 39, (byte) 160, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, (byte) 161, 3, 31, 32, 33 };
    }

    public byte[] getUUIData() {
        return new byte[] { 1, 2, 3, 4, 5 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        UUDataImpl elem = new UUDataImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);
        elem.decodeAll(ais);

        assertEquals(elem.getUUIndicator().getData(), 140);
        assertNull(elem.getUUI());
        assertFalse(elem.getUusCFInteraction());
        assertNull(elem.getExtensionContainer());


        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new UUDataImpl();
        tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);
        elem.decodeAll(ais);

        assertEquals(elem.getUUIndicator().getData(), 140);
        assertEquals(elem.getUUI().getData(), getUUIData());
        assertTrue(elem.getUusCFInteraction());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(elem.getExtensionContainer()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        UUIndicator uuIndicator = new UUIndicatorImpl(140);
        UUDataImpl elem = new UUDataImpl(uuIndicator, null, false, null);
        // UUIndicator uuIndicator, UUI uuI, boolean uusCFInteraction, MAPExtensionContainer extensionContainer

        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));


        UUI uuI = new UUIImpl(getUUIData());
        elem = new UUDataImpl(uuIndicator, uuI, true, MAPExtensionContainerTest.GetTestExtensionContainer());

        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerialize() throws Exception {

        UUIndicator uuIndicator = new UUIndicatorImpl(140);
        UUDataImpl original = new UUDataImpl(uuIndicator, null, false, null);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "uuData", UUDataImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        UUDataImpl copy = reader.read("uuData", UUDataImpl.class);

        assertEquals(copy.getUUIndicator().getData(), original.getUUIndicator().getData());
        assertNull(copy.getUUI());
        assertEquals(copy.getUusCFInteraction(), original.getUusCFInteraction());
        assertNull(copy.getExtensionContainer());


        UUI uuI = new UUIImpl(getUUIData());
        original = new UUDataImpl(uuIndicator, uuI, true, MAPExtensionContainerTest.GetTestExtensionContainer());

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "uuData", UUDataImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("uuData", UUDataImpl.class);

        assertEquals(copy.getUUIndicator().getData(), original.getUUIndicator().getData());
        assertEquals(copy.getUUI().getData(), original.getUUI().getData());
        assertEquals(copy.getUusCFInteraction(), original.getUusCFInteraction());
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(copy.getExtensionContainer()));
    }

}
