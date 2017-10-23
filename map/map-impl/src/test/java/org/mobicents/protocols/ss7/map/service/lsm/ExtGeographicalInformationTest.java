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

package org.mobicents.protocols.ss7.map.service.lsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ExtGeographicalInformationTest {

    private byte[] getEncodedData_EllipsoidPointWithUncertaintyCircle() {
        return new byte[] { 4, 8, 16, 92, 113, -57, -106, 11, 97, 7 };
    }

    private byte[] getEncodedData_EllipsoidPointWithUncertaintyEllipse() {
        return new byte[] { 4, 11, 48, -36, 113, -57, 22, 11, 96, 25, 48, 11, 23 };
    }

    private byte[] getEncodedData_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid() {
        return new byte[] { 4, 14, (byte) 144, -35, 39, -46, 22, 65, -3, -128, 17, 18, 41, 14, 14, 29 };
    }

    private byte[] getEncodedData_EllipsoidArc() {
        return new byte[] { 4, 13, (byte) 160, 1, 108, 22, 121, -48, 54, 23, 112, 9, 11, 12, 39 };
    }

    private byte[] getEncodedData_EllipsoidPoint() {
        return new byte[] { 4, 7, 0, 0, 0, 0, -3, -35, -34 };
    }

    @Test(groups = { "functional.decode", "lsm" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData_EllipsoidPointWithUncertaintyCircle();
        AsnInputStream asn = new AsnInputStream(rawData);
        int tag = asn.readTag();
        ExtGeographicalInformationImpl impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - 65) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - (-149)) < 0.01);  // -31
        assertTrue(Math.abs(impl.getUncertainty() - 9.48) < 0.01);

        rawData = getEncodedData_EllipsoidPointWithUncertaintyEllipse();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyEllipse);
        assertTrue(Math.abs(impl.getLatitude() - (-65)) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - 31) < 0.01);
        assertTrue(Math.abs(impl.getUncertaintySemiMajorAxis() - 98.35) < 0.01);
        assertTrue(Math.abs(impl.getUncertaintySemiMinorAxis() - 960.17) < 0.01);
        assertTrue(Math.abs(impl.getAngleOfMajorAxis() - 22) < 0.01);
        assertEquals(impl.getConfidence(), 23);

        rawData = getEncodedData_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid);
        assertTrue(Math.abs(impl.getLatitude() - (-65.5)) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - 31.3) < 0.01);
        assertEquals(impl.getAltitude(), -17);
        assertTrue(Math.abs(impl.getUncertaintySemiMajorAxis() - 45.60) < 0.01);
        assertTrue(Math.abs(impl.getUncertaintySemiMinorAxis() - 487.85) < 0.01);
        assertTrue(Math.abs(impl.getAngleOfMajorAxis() - 28) < 0.01);
        assertTrue(Math.abs(impl.getUncertaintyAltitude() - 27.97) < 0.01);
        assertEquals(impl.getConfidence(), 29);

        rawData = getEncodedData_EllipsoidArc();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidArc);
        assertTrue(Math.abs(impl.getLatitude() - 1) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - 171.3) < 0.01);
        assertEquals(impl.getInnerRadius(), 6000);
        assertTrue(Math.abs(impl.getUncertaintyRadius() - 13.58) < 0.01);
        assertTrue(Math.abs(impl.getOffsetAngle() - 22) < 0.01);
        assertTrue(Math.abs(impl.getIncludedAngle() - 24) < 0.01);
        assertEquals(impl.getConfidence(), 39);

        rawData = getEncodedData_EllipsoidPoint();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new ExtGeographicalInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPoint);
        assertTrue(Math.abs(impl.getLatitude() - 0) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - (-3)) < 0.01); // -177
    }

    @Test(groups = { "functional.encode", "lsm" })
    public void testEncode() throws Exception {

        ExtGeographicalInformationImpl impl = new ExtGeographicalInformationImpl(
                TypeOfShape.EllipsoidPointWithUncertaintyCircle, 65, -149, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        // TypeOfShape typeOfShape, double latitude, double longitude, double uncertainty, double uncertaintySemiMajorAxis,
        // double uncertaintySemiMinorAxis, double angleOfMajorAxis, int confidence, int altitude, double uncertaintyAltitude,
        // int innerRadius,
        // double uncertaintyRadius, double offsetAngle, double includedAngle
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData_EllipsoidPointWithUncertaintyCircle();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithUncertaintyEllipse, -65, 31, 0, 100, 1000, 22,
                23, 0, 0, 0, 0, 0, 0);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_EllipsoidPointWithUncertaintyEllipse();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPointWithAltitudeAndUncertaintyEllipsoid, -65.5, 31.3,
                28, 50, 500, 28, 29, -17, 29, 0, 0, 0, 0);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_EllipsoidPointWithAltitudeAndUncertaintyEllipsoid();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidArc, 1, 171.3, 0, 0, 0, 0, 39, 0, 0, 6000, 15, 22, 24);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_EllipsoidArc();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new ExtGeographicalInformationImpl(TypeOfShape.EllipsoidPoint, 0, -3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_EllipsoidPoint();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
