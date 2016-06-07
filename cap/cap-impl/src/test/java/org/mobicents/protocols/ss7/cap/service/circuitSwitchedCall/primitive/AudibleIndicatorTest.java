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

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.primitives.Burst;
import org.mobicents.protocols.ss7.cap.api.primitives.BurstList;
import org.mobicents.protocols.ss7.cap.primitives.BurstImpl;
import org.mobicents.protocols.ss7.cap.primitives.BurstListImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AudibleIndicatorTest {

    public byte[] getData1() {
        return new byte[] { 1, 1, 0 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 161, 8, (byte) 128, 1, 1, (byte) 161, 3, (byte) 128, 1, 2 };
    }

    public byte[] getData3() {
        return new byte[] { (byte) 163, 10, (byte) 161, 8, (byte) 128, 1, 1, (byte) 161, 3, (byte) 128, 1, 2 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        AudibleIndicatorImpl elem = new AudibleIndicatorImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.BOOLEAN);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);
        elem.decodeAll(ais);
        assertFalse(elem.getTone());
        assertNull(elem.getBurstList());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new AudibleIndicatorImpl();
        tag = ais.readTag();
        assertEquals(tag, AudibleIndicatorImpl._ID_burstList);
        assertEquals(ais.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        elem.decodeAll(ais);
        assertNull(elem.getTone());
        assertEquals((int) elem.getBurstList().getWarningPeriod(), 1);
        assertEquals((int) elem.getBurstList().getBursts().getNumberOfBursts(), 2);

        data = this.getData3();
        ais = new AsnInputStream(data);
        elem = new AudibleIndicatorImpl();
        tag = ais.readTag();
        assertEquals(tag, CAMELAChBillingChargingCharacteristicsImpl._ID_audibleIndicator);
        AsnInputStream ais2 = ais.readSequenceStream();
        ais2.readTag();
        elem.decodeAll(ais2);
        assertNull(elem.getTone());
        assertEquals((int) elem.getBurstList().getWarningPeriod(), 1);
        assertEquals((int) elem.getBurstList().getBursts().getNumberOfBursts(), 2);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        AudibleIndicatorImpl elem = new AudibleIndicatorImpl(false);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        Burst burst = new BurstImpl(2, null, null, null, null);
        BurstList burstList = new BurstListImpl(1, burst);
        // Integer warningPeriod, Burst burst
        elem = new AudibleIndicatorImpl(burstList);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        burst = new BurstImpl(2, null, null, null, null);
        burstList = new BurstListImpl(1, burst);
        // Integer warningPeriod, Burst burst
        elem = new AudibleIndicatorImpl(burstList);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, CAMELAChBillingChargingCharacteristicsImpl._ID_audibleIndicator);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerialize() throws Exception {
        AudibleIndicatorImpl original = new AudibleIndicatorImpl(false);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "audibleIndicator", AudibleIndicatorImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        AudibleIndicatorImpl copy = reader.read("audibleIndicator", AudibleIndicatorImpl.class);

        assertEquals(copy.getTone(), original.getTone());
        assertNull(copy.getBurstList());

        Burst burst = new BurstImpl(2, null, null, null, null);
        BurstList burstList = new BurstListImpl(1, burst);
        original = new AudibleIndicatorImpl(burstList);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "audibleIndicator", AudibleIndicatorImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("audibleIndicator", AudibleIndicatorImpl.class);

        assertNull(copy.getTone());
        assertEquals((int) copy.getBurstList().getWarningPeriod(), (int) original.getBurstList().getWarningPeriod());
        assertEquals((int) copy.getBurstList().getBursts().getNumberOfBursts(),
                (int) original.getBurstList().getBursts().getNumberOfBursts());
    }
}
