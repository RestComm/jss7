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
public class GeographicalInformationTest {

    private byte[] getEncodedData() {
        return new byte[] { 4, 8, 16, 30, -109, -23, 121, -103, -103, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 4, 8, 16, -28, 6, 95, -128, 91, 5, 20 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        GeographicalInformationImpl impl = new GeographicalInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - 21.5) < 0.0001);
        assertTrue(Math.abs(impl.getLongitude() - 171) < 0.0001);
        assertTrue(Math.abs(impl.getUncertainty() - 0) < 0.01);

        rawData = getEncodedData2();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new GeographicalInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - (-70.33)) < 0.0001);
        assertTrue(Math.abs(impl.getLongitude() - (-0.5)) < 0.0001);
        assertTrue(Math.abs(impl.getUncertainty() - 57.27) < 0.01);
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {

        GeographicalInformationImpl impl = new GeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle,
                21.5, 171, 0);
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new GeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle, -70.33, -0.5, 58);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test(groups = { "functional.xml.serialize", "subscriberInformation" })
    public void testXMLSerialize() throws Exception {

        GeographicalInformationImpl original = new GeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle,
                -70.33, -0.5, 58);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "geographicalInformation", GeographicalInformationImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        GeographicalInformationImpl copy = reader.read("geographicalInformation", GeographicalInformationImpl.class);

        assertEquals(copy.getTypeOfShape(), original.getTypeOfShape());
        assertEquals(copy.getLatitude(), original.getLatitude());
        assertEquals(copy.getLongitude(), original.getLongitude());
        assertEquals(copy.getUncertainty(), original.getUncertainty());

    }

}
