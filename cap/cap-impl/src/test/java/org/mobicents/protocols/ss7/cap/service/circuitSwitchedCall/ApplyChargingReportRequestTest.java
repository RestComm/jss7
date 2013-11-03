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
import org.mobicents.protocols.ss7.cap.primitives.ReceivingSideIDImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.TimeDurationChargingResultImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.TimeInformationImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class ApplyChargingReportRequestTest {

    public byte[] getData1() {
        return new byte[] { 4, 15, (byte) 160, 13, (byte) 160, 3, (byte) 129, 1, 1, (byte) 161, 3, (byte) 128, 1, 26,
                (byte) 130, 1, 0 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        ApplyChargingReportRequestImpl elem = new ApplyChargingReportRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertEquals(elem.getTimeDurationChargingResult().getPartyToCharge().getReceivingSideID(), LegType.leg1);
        assertEquals((int) elem.getTimeDurationChargingResult().getTimeInformation().getTimeIfNoTariffSwitch(), 26);
        assertFalse(elem.getTimeDurationChargingResult().getLegActive());
        assertFalse(elem.getTimeDurationChargingResult().getCallLegReleasedAtTcpExpiry());
        assertNull(elem.getTimeDurationChargingResult().getExtensions());
        assertNull(elem.getTimeDurationChargingResult().getAChChargingAddress());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        ReceivingSideIDImpl partyToCharge = new ReceivingSideIDImpl(LegType.leg1);
        TimeInformationImpl timeInformation = new TimeInformationImpl(26);
        TimeDurationChargingResultImpl timeDurationChargingResult = new TimeDurationChargingResultImpl(partyToCharge,
                timeInformation, false, false, null, null);
        // ReceivingSideID partyToCharge, TimeInformation timeInformation,
        // boolean legActive,
        // boolean callLegReleasedAtTcpExpiry, CAPExtensions extensions,
        // AChChargingAddress aChChargingAddress

        ApplyChargingReportRequestImpl elem = new ApplyChargingReportRequestImpl(timeDurationChargingResult);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerializaion() throws Exception {

        ReceivingSideIDImpl partyToCharge = new ReceivingSideIDImpl(LegType.leg1);
        TimeInformationImpl timeInformation = new TimeInformationImpl(26);
        TimeDurationChargingResultImpl timeDurationChargingResult = new TimeDurationChargingResultImpl(partyToCharge,
                timeInformation, false, false, null, null);

        ApplyChargingReportRequestImpl original = new ApplyChargingReportRequestImpl(timeDurationChargingResult);
        original.setInvokeId(24);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "applyChargingReportRequest", ApplyChargingReportRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ApplyChargingReportRequestImpl copy = reader.read("applyChargingReportRequest", ApplyChargingReportRequestImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
        assertEquals(copy.getTimeDurationChargingResult().getPartyToCharge().getReceivingSideID(), original
                .getTimeDurationChargingResult().getPartyToCharge().getReceivingSideID());

    }
}
