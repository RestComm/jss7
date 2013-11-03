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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class GeodeticInformationTest {

    private byte[] getEncodedData() {
        return new byte[] { 4, 10, 3, 16, 30, -109, -23, 121, -103, -103, 0, 11 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        GeodeticInformationImpl impl = new GeodeticInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getScreeningAndPresentationIndicators(), 3);
        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - 21.5) < 0.0001);
        assertTrue(Math.abs(impl.getLongitude() - 171) < 0.0001);
        assertTrue(Math.abs(impl.getUncertainty() - 0) < 0.01);
        assertEquals(impl.getConfidence(), 11);
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {

        GeodeticInformationImpl impl = new GeodeticInformationImpl(3, TypeOfShape.EllipsoidPointWithUncertaintyCircle, 21.5,
                171, 0, 11);
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.xml.serialize", "subscriberInformation" })
    public void testXMLSerialize() throws Exception {

        GeodeticInformationImpl original = new GeodeticInformationImpl(3, TypeOfShape.EllipsoidPointWithUncertaintyCircle,
                21.5, 171, 8, 11);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "geodeticInformation", GeodeticInformationImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        GeodeticInformationImpl copy = reader.read("geodeticInformation", GeodeticInformationImpl.class);

        assertEquals(copy.getScreeningAndPresentationIndicators(), original.getScreeningAndPresentationIndicators());
        assertEquals(copy.getTypeOfShape(), original.getTypeOfShape());
        assertEquals(copy.getLatitude(), original.getLatitude());
        assertEquals(copy.getLongitude(), original.getLongitude());
        assertEquals(copy.getUncertainty(), original.getUncertainty());
        assertEquals(copy.getConfidence(), original.getConfidence());

    }

}
