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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.primitives.CriticalityType;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ExtensionFieldTest {

    public byte[] getData1() {
        return new byte[] { 48, 5, 2, 1, 2, (byte) 129, 0 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 7, 6, 2, 40, 22, (byte) 129, 1, (byte) 255 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 11, 2, 2, 8, (byte) 174, 10, 1, 1, (byte) 129, 2, (byte) 253, (byte) 213 };
    }

    public long[] getDataOid() {
        return new long[] { 1, 0, 22 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        ExtensionFieldImpl elem = new ExtensionFieldImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals((int) elem.getLocalCode(), 2);
        assertEquals(elem.getCriticalityType(), CriticalityType.typeIgnore);
        ais = new AsnInputStream(elem.getData());
        ais.readNullData(elem.getData().length);

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new ExtensionFieldImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertTrue(Arrays.equals(elem.getGlobalCode(), this.getDataOid()));
        assertEquals(elem.getCriticalityType(), CriticalityType.typeIgnore);
        ais = new AsnInputStream(elem.getData());
        boolean bool = ais.readBooleanData(elem.getData().length);
        assertTrue(bool);

        data = this.getData3();
        ais = new AsnInputStream(data);
        elem = new ExtensionFieldImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals((int) elem.getLocalCode(), 2222);
        assertEquals(elem.getCriticalityType(), CriticalityType.typeAbort);
        ais = new AsnInputStream(elem.getData());
        int i1 = (int) ais.readIntegerData(elem.getData().length);
        assertEquals(i1, -555);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        AsnOutputStream aos = new AsnOutputStream();
        aos.writeNullData();
        ExtensionFieldImpl elem = new ExtensionFieldImpl(2, CriticalityType.typeIgnore, aos.toByteArray());
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        aos = new AsnOutputStream();
        aos.writeBooleanData(true);
        elem = new ExtensionFieldImpl(this.getDataOid(), null, aos.toByteArray());
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        aos = new AsnOutputStream();
        aos.writeIntegerData(-555);
        elem = new ExtensionFieldImpl(2222, CriticalityType.typeAbort, aos.toByteArray());
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));
    }

    private byte[] getDataSer() {
        return new byte[] { 1, (byte) 255, 3 };
    }

    @Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        ExtensionFieldImpl original = new ExtensionFieldImpl(234, CriticalityType.typeIgnore, getDataSer());

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "extensionField", ExtensionFieldImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ExtensionFieldImpl copy = reader.read("extensionField", ExtensionFieldImpl.class);

        assertEquals((int) copy.getLocalCode(), (int) original.getLocalCode());
        assertTrue(Arrays.equals(copy.getGlobalCode(), original.getGlobalCode()));
        assertEquals(copy.getCriticalityType(), original.getCriticalityType());
        assertEquals(copy.getData(), original.getData());

        original = new ExtensionFieldImpl(getDataOid(), null, getDataSer());

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "extensionField", ExtensionFieldImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("extensionField", ExtensionFieldImpl.class);

        assertNull(copy.getLocalCode());
        assertNull(original.getLocalCode());
        assertTrue(Arrays.equals(copy.getGlobalCode(), original.getGlobalCode()));
        assertEquals(copy.getCriticalityType(), original.getCriticalityType());
        assertEquals(copy.getData(), original.getData());

    }
}
