/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.LegOrCallSegmentImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.LegID;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.primitives.LegIDImpl;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class LegOrCallSegmentTest {

    public byte[] getData1() {
        return new byte[] { (byte) 128, 1, 10 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 161, 3, (byte) 128, 1, 3 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        LegOrCallSegmentImpl elem = new LegOrCallSegmentImpl();
        int tag = ais.readTag();

        assertEquals(tag, LegOrCallSegmentImpl._ID_callSegmentID);
        elem.decodeAll(ais);
        assertEquals((int)elem.getCallSegmentID(), 10);
        assertNull(elem.getLegID());


        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new LegOrCallSegmentImpl();
        tag = ais.readTag();

        assertEquals(tag, LegOrCallSegmentImpl._ID_legID);
        elem.decodeAll(ais);
        assertNull(elem.getCallSegmentID());
        assertNull(elem.getLegID().getReceivingSideID());
        assertEquals(elem.getLegID().getSendingSideID(), LegType.leg3);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        LegOrCallSegmentImpl elem = new LegOrCallSegmentImpl(10);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        LegID legId = new LegIDImpl(true, LegType.leg3);
        elem = new LegOrCallSegmentImpl(legId);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerializaion() throws Exception {
        LegOrCallSegmentImpl original = new LegOrCallSegmentImpl(10);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "legOrCallSegment", LegOrCallSegmentImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        LegOrCallSegmentImpl copy = reader.read("legOrCallSegment", LegOrCallSegmentImpl.class);

        assertEquals((int)copy.getCallSegmentID(), (int)original.getCallSegmentID());
        assertNull(copy.getLegID());
        assertNull(original.getLegID());


        LegID legId = new LegIDImpl(true, LegType.leg3);
        original = new LegOrCallSegmentImpl(legId);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "legOrCallSegment", LegOrCallSegmentImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("legOrCallSegment", LegOrCallSegmentImpl.class);

        assertEquals(copy.getLegID().getSendingSideID(), original.getLegID().getSendingSideID());
        assertNull(copy.getCallSegmentID());
        assertNull(original.getCallSegmentID());
        assertNull(copy.getLegID().getReceivingSideID());
        assertNull(original.getLegID().getReceivingSideID());

    }

}
