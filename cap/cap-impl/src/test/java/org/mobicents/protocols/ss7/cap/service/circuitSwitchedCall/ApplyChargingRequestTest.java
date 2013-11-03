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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.primitives.SendingSideIDImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class ApplyChargingRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 14, (byte) 128, 7, (byte) 160, 5, (byte) 128, 3, 0, (byte) 140, (byte) 160, (byte) 162, 3,
                (byte) 128, 1, 1 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 34, (byte) 128, 7, (byte) 160, 5, (byte) 128, 3, 0, (byte) 140, (byte) 160, (byte) 162, 3,
                (byte) 128, 1, 1, (byte) 163, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1,
                (byte) 255 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        ApplyChargingRequestImpl elem = new ApplyChargingRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals((long) elem.getAChBillingChargingCharacteristics().getMaxCallPeriodDuration(), 36000);
        assertNull(elem.getAChBillingChargingCharacteristics().getAudibleIndicator());
        assertNull(elem.getAChBillingChargingCharacteristics().getExtensions());
        assertFalse(elem.getAChBillingChargingCharacteristics().getReleaseIfdurationExceeded());
        assertNull(elem.getAChBillingChargingCharacteristics().getTariffSwitchInterval());
        assertEquals(elem.getPartyToCharge().getSendingSideID(), LegType.leg1);
        assertNull(elem.getExtensions());
        assertNull(elem.getAChChargingAddress());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new ApplyChargingRequestImpl();
        tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals((long) elem.getAChBillingChargingCharacteristics().getMaxCallPeriodDuration(), 36000);
        assertNull(elem.getAChBillingChargingCharacteristics().getAudibleIndicator());
        assertNull(elem.getAChBillingChargingCharacteristics().getExtensions());
        assertFalse(elem.getAChBillingChargingCharacteristics().getReleaseIfdurationExceeded());
        assertNull(elem.getAChBillingChargingCharacteristics().getTariffSwitchInterval());
        assertEquals(elem.getPartyToCharge().getSendingSideID(), LegType.leg1);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertNull(elem.getAChChargingAddress());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        CAMELAChBillingChargingCharacteristicsImpl aChBillingChargingCharacteristics = new CAMELAChBillingChargingCharacteristicsImpl(
                36000, false, null, null, null, false);
        // long maxCallPeriodDuration, boolean releaseIfdurationExceeded, Long
        // tariffSwitchInterval,
        // AudibleIndicator audibleIndicator, CAPExtensions extensions, boolean
        // isCAPVersion3orLater
        SendingSideIDImpl partyToCharge = new SendingSideIDImpl(LegType.leg1);

        ApplyChargingRequestImpl elem = new ApplyChargingRequestImpl(aChBillingChargingCharacteristics, partyToCharge, null,
                null);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
        // CAMELAChBillingChargingCharacteristics
        // aChBillingChargingCharacteristics, SendingSideID partyToCharge,
        // CAPExtensions extensions, AChChargingAddress aChChargingAddress

        elem = new ApplyChargingRequestImpl(aChBillingChargingCharacteristics, partyToCharge,
                CAPExtensionsTest.createTestCAPExtensions(), null);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerializaion() throws Exception {
        CAMELAChBillingChargingCharacteristicsImpl aChBillingChargingCharacteristics = new CAMELAChBillingChargingCharacteristicsImpl(
                36000, false, null, null, null, false);
        SendingSideIDImpl partyToCharge = new SendingSideIDImpl(LegType.leg1);
        ApplyChargingRequestImpl original = new ApplyChargingRequestImpl(aChBillingChargingCharacteristics, partyToCharge,
                CAPExtensionsTest.createTestCAPExtensions(), null);
        original.setInvokeId(24);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "applyChargingRequest", ApplyChargingRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ApplyChargingRequestImpl copy = reader.read("applyChargingRequest", ApplyChargingRequestImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
        assertEquals(copy.getAChBillingChargingCharacteristics().getMaxCallPeriodDuration(), original
                .getAChBillingChargingCharacteristics().getMaxCallPeriodDuration());
        assertEquals(copy.getAChBillingChargingCharacteristics().getReleaseIfdurationExceeded(), original
                .getAChBillingChargingCharacteristics().getReleaseIfdurationExceeded());
        assertEquals(copy.getPartyToCharge().getSendingSideID(), original.getPartyToCharge().getSendingSideID());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(copy.getExtensions()));

    }
}
