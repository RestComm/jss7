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
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class LegIDTest {

    private byte[] getData1() {
        return new byte[] { (byte) 128, 1, 2 };
    }

    private byte[] getData2() {
        return new byte[] { (byte) 129, 1, 1 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        LegIDImpl legId = new LegIDImpl();
        int tag = ais.readTag();
        legId.decodeAll(ais);
        assertNotNull(legId.getSendingSideID());
        assertNull(legId.getReceivingSideID());
        assertEquals(legId.getSendingSideID(), LegType.leg2);

        data = this.getData2();
        ais = new AsnInputStream(data);
        legId = new LegIDImpl();
        tag = ais.readTag();
        legId.decodeAll(ais);
        assertNull(legId.getSendingSideID());
        assertNotNull(legId.getReceivingSideID());
        assertEquals(legId.getReceivingSideID(), LegType.leg1);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        LegIDImpl legId = new LegIDImpl(true, LegType.leg2);
        AsnOutputStream aos = new AsnOutputStream();
        legId.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        legId = new LegIDImpl(false, LegType.leg1);
        aos.reset();
        legId.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

    }

    @Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        LegIDImpl original = new LegIDImpl(true, LegType.leg1);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "legID", LegIDImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        LegIDImpl copy = reader.read("legID", LegIDImpl.class);

        assertEquals(copy.getSendingSideID(), original.getSendingSideID());
        assertEquals(copy.getReceivingSideID(), original.getReceivingSideID());

        original = new LegIDImpl(false, LegType.leg2);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "legID", LegIDImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("legID", LegIDImpl.class);

        assertEquals(copy.getSendingSideID(), original.getSendingSideID());
        assertEquals(copy.getReceivingSideID(), original.getReceivingSideID());
    }
}
