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
        return new byte[] { 4, 8, 16, -28, 6, 95, -128, 91, 6, 20 }; // 5
    }

    private byte[] getEncodedData01() {
        return new byte[] { 4, 8, 16, (byte) 0xAC, 0x16, (byte) 0xC1, (byte) 0xA5, (byte) 0xB0, 0x5B, 0 };
    }

    private byte[] getEncodedData02() {
        return new byte[] { 4, 8, 16, (byte) 0x2C, 0x16, (byte) 0xC1, (byte) 0x25, (byte) 0xB0, 0x5B, 0 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData01();

        AsnInputStream asn = new AsnInputStream(rawData);

        int tag = asn.readTag();
        GeographicalInformationImpl impl = new GeographicalInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - (-31)) < 0.0001);
        assertTrue(Math.abs(impl.getLongitude() - (-127.00001)) < 0.0001);
        assertTrue(Math.abs(impl.getUncertainty() - 0) < 0.01);

        rawData = getEncodedData02();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new GeographicalInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - 31) < 0.0001);
        assertTrue(Math.abs(impl.getLongitude() - 53) < 0.0001);
        assertTrue(Math.abs(impl.getUncertainty() - 0) < 0.01);

        rawData = getEncodedData();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new GeographicalInformationImpl();
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
        assertTrue(Math.abs(impl.getLongitude() - (-179.5)) < 0.0001);
        assertTrue(Math.abs(impl.getUncertainty() - 57.27) < 0.01);
    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {

        GeographicalInformationImpl impl = new GeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle,
                -31, -127.00001, 0);
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData01();
        assertEquals(rawData, encodedData);

        impl = new GeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle,
                31, 53, 0);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData02();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new GeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle,
                21.5, 171, 0);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new GeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyCircle, -70.33, -179.5, 58);
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
