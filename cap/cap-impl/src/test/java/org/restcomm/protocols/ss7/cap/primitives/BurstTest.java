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

package org.restcomm.protocols.ss7.cap.primitives;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.restcomm.protocols.ss7.cap.primitives.BurstImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class BurstTest {

    public byte[] getData1() {
        return new byte[] { 48, 3, (byte) 129, 1, 10 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 15, (byte) 128, 1, 1, (byte) 129, 1, 10, (byte) 130, 1, 2, (byte) 131, 1, 11, (byte) 132, 1, 12 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        BurstImpl elem = new BurstImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);
        elem.decodeAll(ais);
        assertNull(elem.getNumberOfBursts());
        assertEquals((int) elem.getBurstInterval(), 10);
        assertNull(elem.getNumberOfTonesInBurst());
        assertNull(elem.getToneDuration());
        assertNull(elem.getToneInterval());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new BurstImpl();
        tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);
        elem.decodeAll(ais);
        assertEquals((int) elem.getNumberOfBursts(), 1);
        assertEquals((int) elem.getBurstInterval(), 10);
        assertEquals((int) elem.getNumberOfTonesInBurst(), 2);
        assertEquals((int) elem.getToneDuration(), 11);
        assertEquals((int) elem.getToneInterval(), 12);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        BurstImpl elem = new BurstImpl(null, 10, null, null, null);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        elem = new BurstImpl(1, 10, 2, 11, 12);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }

    @Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        BurstImpl original = new BurstImpl(null, 10, null, null, null);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "burst", BurstImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        BurstImpl copy = reader.read("burst", BurstImpl.class);

        assertNull(copy.getNumberOfBursts());
        assertEquals((int) copy.getBurstInterval(), (int) original.getBurstInterval());
        assertNull(copy.getNumberOfTonesInBurst());
        assertNull(copy.getToneDuration());
        assertNull(copy.getToneInterval());


        original = new BurstImpl(1, 10, 2, 11, 12);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "burst", BurstImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("burst", BurstImpl.class);

        assertEquals((int) copy.getNumberOfBursts(), (int) original.getNumberOfBursts());
        assertEquals((int) copy.getBurstInterval(), (int) original.getBurstInterval());
        assertEquals((int) copy.getNumberOfTonesInBurst(), (int) original.getNumberOfTonesInBurst());
        assertEquals((int) copy.getToneDuration(), (int) original.getToneDuration());
        assertEquals((int) copy.getToneInterval(), (int) original.getToneInterval());
    }

}
