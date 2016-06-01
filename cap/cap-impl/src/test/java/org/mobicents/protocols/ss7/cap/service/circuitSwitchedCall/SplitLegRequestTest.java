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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;
import org.testng.annotations.Test;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

public class SplitLegRequestTest {
    
    public byte[] getData1() {
        return new byte[] {0x30, 0x08, (byte)0xa0, 0x03, (byte)0x80, 0x01, 0x02, (byte)0x81, 0x01, 0x02};
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        SplitLegRequestImpl elem = new SplitLegRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getLegToBeSplit().toString(), new LegIDImpl(true, LegType.leg2).toString());
        assertEquals(elem.getNewCallSegment(), new Integer(2));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        LegIDImpl legToBeSplit = new LegIDImpl(true, LegType.leg2);
        SplitLegRequestImpl elem = new SplitLegRequestImpl(legToBeSplit, new Integer(2), null);
                
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);        
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));        
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        LegIDImpl legToBeSplit = new LegIDImpl(true, LegType.leg2);
        SplitLegRequestImpl original = new SplitLegRequestImpl(legToBeSplit, new Integer(2), CAPExtensionsTest.createTestCAPExtensions());        

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "splitLegRequest", SplitLegRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        SplitLegRequestImpl copy = reader.read("splitLegRequest", SplitLegRequestImpl.class);

        assertEquals(original.getLegToBeSplit().getSendingSideID(), copy.getLegToBeSplit().getSendingSideID());
        assertEquals(original.getNewCallSegment(), copy.getNewCallSegment());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(original.getExtensions()));
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(copy.getExtensions()));
    }
}
