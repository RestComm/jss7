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
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class DpSpecificCriteriaTest {

    public byte[] getData1() {
        return new byte[] { (byte) 129, 2, 3, (byte) 232 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        DpSpecificCriteriaImpl elem = new DpSpecificCriteriaImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals((int) elem.getApplicationTimer(), 1000);
        assertNull(elem.getMidCallControlInfo());
        assertNull(elem.getDpSpecificCriteriaAlt());

        // TODO: implement other choices
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        DpSpecificCriteriaImpl elem = new DpSpecificCriteriaImpl(1000);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        // TODO: implement other choices

        // CauseIndicators ci = new CauseIndicatorsImpl(0, 4, 16, null);
        // elem = new CauseCapImpl(ci);
        // aos = new AsnOutputStream();
        // elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
        // assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        DpSpecificCriteriaImpl original = new DpSpecificCriteriaImpl(1000);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "dpSpecificCriteria", DpSpecificCriteriaImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        DpSpecificCriteriaImpl copy = reader.read("dpSpecificCriteria", DpSpecificCriteriaImpl.class);

        assertEquals((int) copy.getApplicationTimer(), (int) original.getApplicationTimer());

        // TODO: implement other choices
    }
}
