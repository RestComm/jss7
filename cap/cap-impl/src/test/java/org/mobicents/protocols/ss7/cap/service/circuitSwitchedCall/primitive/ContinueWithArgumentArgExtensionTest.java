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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;
import org.testng.annotations.Test;

/**
*
*
* @author sergey vetyutnev
*
*/
public class ContinueWithArgumentArgExtensionTest {

    public byte[] getData1() {
        return new byte[] { 48, 11, (byte) 128, 0, (byte) 129, 0, (byte) 130, 0, (byte) 163, 3, (byte) 128, 1, 12 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 9, (byte) 129, 0, (byte) 163, 5, (byte) 161, 3, (byte) 129, 1, 4 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        ContinueWithArgumentArgExtensionImpl elem = new ContinueWithArgumentArgExtensionImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        elem.decodeAll(ais);
        assertTrue(elem.getSuppressDCsi());
        assertTrue(elem.getSuppressNCsi());
        assertTrue(elem.getSuppressOutgoingCallBarring());
        assertEquals((int) elem.getLegOrCallSegment().getCallSegmentID(), 12);

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new ContinueWithArgumentArgExtensionImpl();
        tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        elem.decodeAll(ais);
        assertFalse(elem.getSuppressDCsi());
        assertTrue(elem.getSuppressNCsi());
        assertFalse(elem.getSuppressOutgoingCallBarring());
        assertEquals(elem.getLegOrCallSegment().getLegID().getReceivingSideID(), LegType.leg4);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        LegOrCallSegmentImpl legOrCallSegment = new LegOrCallSegmentImpl(12);
        ContinueWithArgumentArgExtensionImpl elem = new ContinueWithArgumentArgExtensionImpl(true, true, true, legOrCallSegment);
//        boolean suppressDCSI, boolean suppressNCSI,
//        boolean suppressOutgoingCallBarring, LegOrCallSegment legOrCallSegment
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        LegIDImpl legID = new LegIDImpl(false, LegType.leg4);
        legOrCallSegment = new LegOrCallSegmentImpl(legID);
        elem = new ContinueWithArgumentArgExtensionImpl(false, true, false, legOrCallSegment);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerializaion() throws Exception {
        LegOrCallSegmentImpl legOrCallSegment = new LegOrCallSegmentImpl(12);
        ContinueWithArgumentArgExtensionImpl original = new ContinueWithArgumentArgExtensionImpl(true, false, true, legOrCallSegment);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "continueWithArgumentArgExtension", ContinueWithArgumentArgExtensionImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ContinueWithArgumentArgExtensionImpl copy = reader.read("continueWithArgumentArgExtension", ContinueWithArgumentArgExtensionImpl.class);

        assertEquals(original.getSuppressDCsi(), copy.getSuppressDCsi());
        assertEquals(original.getSuppressNCsi(), copy.getSuppressNCsi());
        assertEquals(original.getSuppressOutgoingCallBarring(), copy.getSuppressOutgoingCallBarring());
        assertEquals((int) original.getLegOrCallSegment().getCallSegmentID(), (int) copy.getLegOrCallSegment().getCallSegmentID());


        original = new ContinueWithArgumentArgExtensionImpl(false, true, true, null);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "continueWithArgumentArgExtension", ContinueWithArgumentArgExtensionImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("continueWithArgumentArgExtension", ContinueWithArgumentArgExtensionImpl.class);

        assertEquals(original.getSuppressDCsi(), copy.getSuppressDCsi());
        assertEquals(original.getSuppressNCsi(), copy.getSuppressNCsi());
        assertEquals(original.getSuppressOutgoingCallBarring(), copy.getSuppressOutgoingCallBarring());
        assertNull(original.getLegOrCallSegment());
        assertNull(copy.getLegOrCallSegment());
    }

}
