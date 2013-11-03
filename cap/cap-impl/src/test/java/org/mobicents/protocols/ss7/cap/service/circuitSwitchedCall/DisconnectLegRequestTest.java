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
import org.mobicents.protocols.ss7.cap.isup.CauseCapImpl;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
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
public class DisconnectLegRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 29, (byte) 160, 3, (byte) 129, 1, 6, (byte) 129, 2, (byte) 128, (byte) 134, (byte) 162, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48,
                9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        DisconnectLegRequestImpl elem = new DisconnectLegRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getLegToBeReleased().getReceivingSideID(), LegType.leg6);
        CauseIndicators ci = elem.getReleaseCause().getCauseIndicators();
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getCauseValue(), 6);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        LegIDImpl legToBeReleased = new LegIDImpl(false, LegType.leg6);
        CauseIndicatorsImpl causeIndicators = new CauseIndicatorsImpl(0, 0, 0, 6, null);
        CauseCapImpl releaseCause = new CauseCapImpl(causeIndicators);
        DisconnectLegRequestImpl elem = new DisconnectLegRequestImpl(legToBeReleased, releaseCause, CAPExtensionsTest.createTestCAPExtensions());

        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        LegIDImpl legToBeReleased = new LegIDImpl(false, LegType.leg6);
        CauseIndicatorsImpl causeIndicators = new CauseIndicatorsImpl(0, 0, 0, 6, null);
        CauseCapImpl releaseCause = new CauseCapImpl(causeIndicators);
        DisconnectLegRequestImpl original = new DisconnectLegRequestImpl(legToBeReleased, releaseCause, CAPExtensionsTest.createTestCAPExtensions());

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "disconnectLegRequest", DisconnectLegRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        DisconnectLegRequestImpl copy = reader.read("disconnectLegRequest", DisconnectLegRequestImpl.class);

        assertEquals(original.getLegToBeReleased().getReceivingSideID(), copy.getLegToBeReleased().getReceivingSideID());
        CauseIndicators ci = original.getReleaseCause().getCauseIndicators();
        CauseIndicators ci2 = copy.getReleaseCause().getCauseIndicators();
        assertEquals(ci.getCodingStandard(), ci2.getCodingStandard());
        assertEquals(ci.getCauseValue(), ci2.getCauseValue());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(original.getExtensions()));
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(copy.getExtensions()));


        original = new DisconnectLegRequestImpl(legToBeReleased, null, null);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "disconnectLegRequest", DisconnectLegRequestImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("disconnectLegRequest", DisconnectLegRequestImpl.class);

        assertEquals(original.getLegToBeReleased().getReceivingSideID(), copy.getLegToBeReleased().getReceivingSideID());
        assertNull(original.getReleaseCause());
        assertNull(copy.getReleaseCause());
        assertNull(original.getExtensions());
        assertNull(copy.getExtensions());
    }

}
