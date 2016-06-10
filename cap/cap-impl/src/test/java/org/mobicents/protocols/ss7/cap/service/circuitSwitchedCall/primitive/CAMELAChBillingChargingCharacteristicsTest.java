/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AudibleIndicator;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * @author alerant appngin
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

    public byte[] getData4() {
        return new byte[] { (byte) 128, 18, (byte) 160, 16, (byte) 128, 2, 39, 16, (byte) 129, 1, (byte) 255, (byte) 130, 2, 3, (byte) 232, (byte) 163, 3, 1, 1,
                (byte) 255 };
    }

    public byte[] getData5() {
        return new byte[] { (byte) 128, 11, (byte) 160, 9, (byte) 128, 2, 46, (byte) 224, (byte) 161, 3, 1, 1, 0 };
    }

    public byte[] getData6() {
        return new byte[] { (byte) 128, 9, (byte) 160, 7, (byte) 128, 2, 39, 16, (byte) 131, 1, (byte) 255 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        CAMELAChBillingChargingCharacteristicsImpl elem = new CAMELAChBillingChargingCharacteristicsImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getMaxCallPeriodDuration(), 12000);
        assertTrue(elem.getReleaseIfdurationExceeded());
        assertNull(elem.getTariffSwitchInterval());
        assertTrue(elem.getAudibleIndicator().getTone());
        assertNull(elem.getExtensions());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new CAMELAChBillingChargingCharacteristicsImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getMaxCallPeriodDuration(), 10000);
        assertTrue(elem.getReleaseIfdurationExceeded());
        assertEquals((int) (long) elem.getTariffSwitchInterval(), 1000);
        assertTrue(elem.getAudibleIndicator().getTone());
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


        data = this.getData4();
        ais = new AsnInputStream(data);
        elem = new CAMELAChBillingChargingCharacteristicsImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getMaxCallPeriodDuration(), 10000);
        assertTrue(elem.getReleaseIfdurationExceeded());
        assertEquals((int) (long) elem.getTariffSwitchInterval(), 1000);
        assertTrue(elem.getAudibleIndicator().getTone());
        assertNull(elem.getExtensions());

        data = this.getData5();
        ais = new AsnInputStream(data);
        elem = new CAMELAChBillingChargingCharacteristicsImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getMaxCallPeriodDuration(), 12000);
        assertTrue(elem.getReleaseIfdurationExceeded());
        assertNull(elem.getTariffSwitchInterval());
        assertNotNull(elem.getAudibleIndicator());
        assertFalse(elem.getAudibleIndicator().getTone());
        assertNull(elem.getExtensions());


        data = this.getData6();
        ais = new AsnInputStream(data);
        elem = new CAMELAChBillingChargingCharacteristicsImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getMaxCallPeriodDuration(), 10000);
        assertFalse(elem.getReleaseIfdurationExceeded());
        assertNull(elem.getTariffSwitchInterval());
        assertTrue(elem.getAudibleIndicator().getTone());
        assertNull(elem.getExtensions());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        AudibleIndicator audibleIndicator = new AudibleIndicatorImpl(true);
        CAMELAChBillingChargingCharacteristicsImpl elem = new CAMELAChBillingChargingCharacteristicsImpl(12000, true, null,
                audibleIndicator, null, 2);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));


        elem = new CAMELAChBillingChargingCharacteristicsImpl(10000, true, 1000L, audibleIndicator,
                CAPExtensionsTest.createTestCAPExtensions(), 2);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));


        elem = new CAMELAChBillingChargingCharacteristicsImpl(10000, true, 1000L, null,
                CAPExtensionsTest.createTestCAPExtensions(), 3);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));


        elem = new CAMELAChBillingChargingCharacteristicsImpl(10000, true, 1000L, audibleIndicator, null, 4);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData4()));

        // long maxCallPeriodDuration, boolean releaseIfdurationExceeded, Long
        // tariffSwitchInterval,AudibleIndicator audibleIndicator, CAPExtensions
        // extensions, boolean isCAPVersion3orLater


        elem = new CAMELAChBillingChargingCharacteristicsImpl(12000, true, null, new AudibleIndicatorImpl(false), null, 2);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData5()));


        elem = new CAMELAChBillingChargingCharacteristicsImpl(10000, false, null, audibleIndicator, null, 3);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData6()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerializaion() throws Exception {
        CAMELAChBillingChargingCharacteristicsImpl original = new CAMELAChBillingChargingCharacteristicsImpl(12000, true,
                8000L, null, CAPExtensionsTest.createTestCAPExtensions(), 2);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "camelAChBillingChargingCharacteristics", CAMELAChBillingChargingCharacteristicsImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CAMELAChBillingChargingCharacteristicsImpl copy = reader.read("camelAChBillingChargingCharacteristics",
                CAMELAChBillingChargingCharacteristicsImpl.class);

        assertEquals(copy.getMaxCallPeriodDuration(), original.getMaxCallPeriodDuration());
        assertEquals(copy.getReleaseIfdurationExceeded(), original.getReleaseIfdurationExceeded());
        assertEquals((long) copy.getTariffSwitchInterval(), (long) original.getTariffSwitchInterval());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(copy.getExtensions()));
        assertNull(copy.getAudibleIndicator());


        AudibleIndicator audibleIndicator = new AudibleIndicatorImpl(true);
        original = new CAMELAChBillingChargingCharacteristicsImpl(12000, true, 8000L, audibleIndicator, null, 3);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "camelAChBillingChargingCharacteristics", CAMELAChBillingChargingCharacteristicsImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("camelAChBillingChargingCharacteristics", CAMELAChBillingChargingCharacteristicsImpl.class);

        assertEquals(copy.getMaxCallPeriodDuration(), original.getMaxCallPeriodDuration());
        assertEquals(copy.getReleaseIfdurationExceeded(), original.getReleaseIfdurationExceeded());
        assertEquals((long) copy.getTariffSwitchInterval(), (long) original.getTariffSwitchInterval());
        assertNull(copy.getExtensions());
        assertEquals(copy.getAudibleIndicator().getTone(), original.getAudibleIndicator().getTone());
    }
}
