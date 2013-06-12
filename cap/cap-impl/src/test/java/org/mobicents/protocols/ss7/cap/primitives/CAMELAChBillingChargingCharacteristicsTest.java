/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.cap.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristicsImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAMELAChBillingChargingCharacteristicsTest {

    public byte[] getData1() {
        return new byte[] { (byte) 128, 11, (byte) 160, 9, (byte) 128, 2, 46, (byte) 224, (byte) 161, 3, 1, 1, (byte) 255 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 128, 35, (byte) 160, 33, (byte) 128, 2, 39, 16, (byte) 161, 23, 1, 1, (byte) 255,
                (byte) 170, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255, (byte) 130,
                2, 3, (byte) 232 };
    }

    public byte[] getData3() {
        return new byte[] { (byte) 128, 33, (byte) 160, 31, (byte) 128, 2, 39, 16, (byte) 129, 1, (byte) 255, (byte) 130, 2, 3,
                (byte) 232, (byte) 164, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        CAMELAChBillingChargingCharacteristicsImpl elem = new CAMELAChBillingChargingCharacteristicsImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getMaxCallPeriodDuration(), 12000);
        assertTrue(elem.getReleaseIfdurationExceeded());
        assertNull(elem.getTariffSwitchInterval());
        assertNull(elem.getAudibleIndicator());
        assertNull(elem.getExtensions());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new CAMELAChBillingChargingCharacteristicsImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getMaxCallPeriodDuration(), 10000);
        assertTrue(elem.getReleaseIfdurationExceeded());
        assertEquals((int) (long) elem.getTariffSwitchInterval(), 1000);
        assertNull(elem.getAudibleIndicator());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));

        data = this.getData3();
        ais = new AsnInputStream(data);
        elem = new CAMELAChBillingChargingCharacteristicsImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getMaxCallPeriodDuration(), 10000);
        assertTrue(elem.getReleaseIfdurationExceeded());
        assertEquals((int) (long) elem.getTariffSwitchInterval(), 1000);
        assertNull(elem.getAudibleIndicator());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));

        // TODO: implement unimplemented parameters:
        // audibleIndicator
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        CAMELAChBillingChargingCharacteristicsImpl elem = new CAMELAChBillingChargingCharacteristicsImpl(12000, true, null,
                null, null, false);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        elem = new CAMELAChBillingChargingCharacteristicsImpl(10000, true, 1000L, null,
                CAPExtensionsTest.createTestCAPExtensions(), false);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        elem = new CAMELAChBillingChargingCharacteristicsImpl(10000, true, 1000L, null,
                CAPExtensionsTest.createTestCAPExtensions(), true);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));

        // TODO: implement unimplemented parameters:
        // audibleIndicator

        // long maxCallPeriodDuration, boolean releaseIfdurationExceeded, Long tariffSwitchInterval,AudibleIndicator
        // audibleIndicator, CAPExtensions extensions, boolean isCAPVersion3orLater
    }
}
