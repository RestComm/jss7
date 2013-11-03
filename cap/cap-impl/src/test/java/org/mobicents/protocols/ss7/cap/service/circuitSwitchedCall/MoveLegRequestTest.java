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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MoveLegRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 25, (byte) 160, 3, (byte) 129, 1, 6, (byte) 162, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1,
                (byte) 255 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        MoveLegRequestImpl elem = new MoveLegRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getLegIDToMove().getReceivingSideID(), LegType.leg6);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        LegIDImpl legIDToMove = new LegIDImpl(false, LegType.leg6);
        MoveLegRequestImpl elem = new MoveLegRequestImpl(legIDToMove, CAPExtensionsTest.createTestCAPExtensions());

        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        LegIDImpl legIDToMove = new LegIDImpl(false, LegType.leg6);
        MoveLegRequestImpl original = new MoveLegRequestImpl(legIDToMove, CAPExtensionsTest.createTestCAPExtensions());

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "moveLegRequest", MoveLegRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        MoveLegRequestImpl copy = reader.read("moveLegRequest", MoveLegRequestImpl.class);

        assertEquals(original.getLegIDToMove().getReceivingSideID(), copy.getLegIDToMove().getReceivingSideID());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(original.getExtensions()));
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(copy.getExtensions()));
    }

}
