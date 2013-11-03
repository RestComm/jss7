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
import org.mobicents.protocols.ss7.map.api.service.lsm.VelocityType;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class VelocityEstimateTest {

    private byte[] getEncodedData_HorizontalVelocity() {
        return new byte[] { 4, 4, 0, 90, 0, 59 };
    }

    private byte[] getEncodedData_HorizontalWithVerticalVelocity() {
        return new byte[] { 4, 5, 17, 44, 39, 16, -56 };
    }

    private byte[] getEncodedData_HorizontalVelocityWithUncertainty() {
        return new byte[] { 4, 5, 33, 45, 39, 17, -57 };
    }

    private byte[] getEncodedData_HorizontalWithVerticalVelocityAndUncertainty() {
        return new byte[] { 4, 7, 49, 46, 39, 18, -54, -58, -59 };
    }

    @Test(groups = { "functional.decode", "lsm" })
    public void testDecode() throws Exception {

        byte[] rawData = getEncodedData_HorizontalVelocity();
        AsnInputStream asn = new AsnInputStream(rawData);
        int tag = asn.readTag();
        VelocityEstimateImpl impl = new VelocityEstimateImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getVelocityType(), VelocityType.HorizontalVelocity);
        assertEquals(impl.getHorizontalSpeed(), 59);
        assertEquals(impl.getBearing(), 90);

        rawData = getEncodedData_HorizontalWithVerticalVelocity();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new VelocityEstimateImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getVelocityType(), VelocityType.HorizontalWithVerticalVelocity);
        assertEquals(impl.getHorizontalSpeed(), 10000);
        assertEquals(impl.getBearing(), 300);
        assertEquals(impl.getVerticalSpeed(), 200);

        rawData = getEncodedData_HorizontalVelocityWithUncertainty();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new VelocityEstimateImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getVelocityType(), VelocityType.HorizontalVelocityWithUncertainty);
        assertEquals(impl.getHorizontalSpeed(), 10001);
        assertEquals(impl.getBearing(), 301);
        assertEquals(impl.getUncertaintyHorizontalSpeed(), 199);

        rawData = getEncodedData_HorizontalWithVerticalVelocityAndUncertainty();
        asn = new AsnInputStream(rawData);
        tag = asn.readTag();
        impl = new VelocityEstimateImpl();
        impl.decodeAll(asn);

        assertEquals(impl.getVelocityType(), VelocityType.HorizontalWithVerticalVelocityAndUncertainty);
        assertEquals(impl.getHorizontalSpeed(), 10002);
        assertEquals(impl.getBearing(), 302);
        assertEquals(impl.getVerticalSpeed(), 202);
        assertEquals(impl.getUncertaintyHorizontalSpeed(), 198);
        assertEquals(impl.getUncertaintyVerticalSpeed(), 197);
    }

    @Test(groups = { "functional.encode", "lsm" })
    public void testEncode() throws Exception {

        VelocityEstimateImpl impl = new VelocityEstimateImpl(VelocityType.HorizontalVelocity, 59, 90, 0, 0, 0);
        // VelocityType velocityType, int horizontalSpeed, int bearing, int verticalSpeed, int uncertaintyHorizontalSpeed, int
        // uncertaintyVerticalSpeed
        AsnOutputStream asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        byte[] encodedData = asnOS.toByteArray();
        byte[] rawData = getEncodedData_HorizontalVelocity();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new VelocityEstimateImpl(VelocityType.HorizontalWithVerticalVelocity, 10000, 300, 200, 0, 0);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_HorizontalWithVerticalVelocity();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new VelocityEstimateImpl(VelocityType.HorizontalVelocityWithUncertainty, 10001, 301, 0, 199, 0);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_HorizontalVelocityWithUncertainty();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new VelocityEstimateImpl(VelocityType.HorizontalWithVerticalVelocityAndUncertainty, 10002, 302, 202, 198, 197);
        asnOS = new AsnOutputStream();
        impl.encodeAll(asnOS);
        encodedData = asnOS.toByteArray();
        rawData = getEncodedData_HorizontalWithVerticalVelocityAndUncertainty();
        assertTrue(Arrays.equals(rawData, encodedData));

    }

}
