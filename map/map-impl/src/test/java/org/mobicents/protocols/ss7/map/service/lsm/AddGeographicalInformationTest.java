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
public class AddGeographicalInformationTest {

    private byte[] getEncodedData_EllipsoidPointWithUncertaintyCircle() {
        return new byte[] { 4, 8, 16, 92, 113, -57, -106, 11, 96, 7 };
    }

    @Test(groups = { "functional.decode", "lsm" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData_EllipsoidPointWithUncertaintyCircle();
        AsnInputStream asn = new AsnInputStream(rawData);
        int tag = asn.readTag();
        AddGeographicalInformationImpl impl = new AddGeographicalInformationImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
        assertTrue(Math.abs(impl.getLatitude() - 65) < 0.01);
        assertTrue(Math.abs(impl.getLongitude() - (-31)) < 0.01);
        assertTrue(Math.abs(impl.getUncertainty() - 9.48) < 0.01);
    }

    @Test(groups = { "functional.encode", "lsm" })
    public void testEncode() throws Exception {

        AddGeographicalInformationImpl impl = new AddGeographicalInformationImpl(
                TypeOfShape.EllipsoidPointWithUncertaintyCircle, 65, -31, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        // TypeOfShape typeOfShape, double latitude, double longitude, double uncertainty, double uncertaintySemiMajorAxis,
        // double uncertaintySemiMinorAxis, double angleOfMajorAxis, int confidence, int altitude, double uncertaintyAltitude,
        // int innerRadius,
        // double uncertaintyRadius, double offsetAngle, double includedAngle
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData_EllipsoidPointWithUncertaintyCircle();
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    // TODO: add processing missed: TypeOfShape.Polygon, TypeOfShape.EllipsoidPointWithAltitude

}
