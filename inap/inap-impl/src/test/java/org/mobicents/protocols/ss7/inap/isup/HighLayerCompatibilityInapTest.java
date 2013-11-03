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

package org.mobicents.protocols.ss7.inap.isup;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.UserTeleserviceInformationImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class HighLayerCompatibilityInapTest {

    public byte[] getData() {
        return new byte[] { (byte) 151, 2, (byte) 145, (byte) 129 };
    }

    public byte[] getIntData() {
        return new byte[] { (byte) 145, (byte) 129 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {

        byte[] data = this.getData();
        AsnInputStream ais = new AsnInputStream(data);
        HighLayerCompatibilityInapImpl elem = new HighLayerCompatibilityInapImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        UserTeleserviceInformation hlc = elem.getHighLayerCompatibility();
        assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
        assertEquals(hlc.getCodingStandard(), 0);
        assertEquals(hlc.getEHighLayerCharIdentification(), 0);
        assertEquals(hlc.getEVideoTelephonyCharIdentification(), 0);
        assertEquals(hlc.getInterpretation(), 4);
        assertEquals(hlc.getPresentationMethod(), 1);
        assertEquals(hlc.getHighLayerCharIdentification(), 1);
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {

        HighLayerCompatibilityInapImpl elem = new HighLayerCompatibilityInapImpl(this.getIntData());
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 23);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

        UserTeleserviceInformation hlc = new UserTeleserviceInformationImpl(0, 4, 1, 1);
        elem = new HighLayerCompatibilityInapImpl(hlc);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 23);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

        // int codingStandard, int interpretation, int presentationMethod, int highLayerCharIdentification
    }

    @Test(groups = { "functional.xml.serialize", "isup" })
    public void testXMLSerialize() throws Exception {

        UserTeleserviceInformationImpl prim = new UserTeleserviceInformationImpl(
                UserTeleserviceInformation._CODING_STANDARD_NATIONAL, UserTeleserviceInformation._INTERPRETATION_FHGCI,
                UserTeleserviceInformation._PRESENTATION_METHOD_HLPP, UserTeleserviceInformation._HLCI_IVTI);
        HighLayerCompatibilityInapImpl original = new HighLayerCompatibilityInapImpl(prim);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "highLayerCompatibilityInap", HighLayerCompatibilityInapImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        HighLayerCompatibilityInapImpl copy = reader.read("highLayerCompatibilityInap", HighLayerCompatibilityInapImpl.class);

        assertEquals(copy.getHighLayerCompatibility().getCodingStandard(), original.getHighLayerCompatibility()
                .getCodingStandard());
        assertEquals(copy.getHighLayerCompatibility().getInterpretation(), original.getHighLayerCompatibility()
                .getInterpretation());
        assertEquals(copy.getHighLayerCompatibility().getPresentationMethod(), original.getHighLayerCompatibility()
                .getPresentationMethod());
        assertEquals(copy.getHighLayerCompatibility().getHighLayerCharIdentification(), original.getHighLayerCompatibility()
                .getHighLayerCharIdentification());

    }
}
