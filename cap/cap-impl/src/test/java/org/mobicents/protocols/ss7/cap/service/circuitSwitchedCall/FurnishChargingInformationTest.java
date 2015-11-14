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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FreeFormatData;
import org.mobicents.protocols.ss7.cap.primitives.SendingSideIDImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.FCIBCCCAMELsequence1Impl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.FreeFormatDataImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class FurnishChargingInformationTest {

    public byte[] getData1() {
        return new byte[] { 4, 16, (byte) 160, 14, (byte) 128, 4, 4, 5, 6, 7, (byte) 161, 3, (byte) 128, 1, 2, (byte) 130, 1, 1 };
    }

    public byte[] getDataFFD() {
        return new byte[] { 4, 5, 6, 7 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        FurnishChargingInformationRequestImpl elem = new FurnishChargingInformationRequestImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.STRING_OCTET);
        elem.decodeAll(ais);
        assertTrue(Arrays.equals(elem.getFCIBCCCAMELsequence1().getFreeFormatData().getData(), this.getDataFFD()));
        assertEquals(elem.getFCIBCCCAMELsequence1().getPartyToCharge().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getFCIBCCCAMELsequence1().getAppendFreeFormatData(), AppendFreeFormatData.append);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        SendingSideIDImpl partyToCharge = new SendingSideIDImpl(LegType.leg2);
        FreeFormatData ffd = new FreeFormatDataImpl(getDataFFD());
        FCIBCCCAMELsequence1Impl fci = new FCIBCCCAMELsequence1Impl(ffd, partyToCharge, AppendFreeFormatData.append);
        FurnishChargingInformationRequestImpl elem = new FurnishChargingInformationRequestImpl(fci);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        SendingSideIDImpl partyToCharge = new SendingSideIDImpl(LegType.leg2);
        FreeFormatData ffd = new FreeFormatDataImpl(getDataFFD());
        FCIBCCCAMELsequence1Impl fci = new FCIBCCCAMELsequence1Impl(ffd, partyToCharge, AppendFreeFormatData.append);
        FurnishChargingInformationRequestImpl original = new FurnishChargingInformationRequestImpl(fci);
        original.setInvokeId(26);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "furnishChargingInformationRequest", FurnishChargingInformationRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        FurnishChargingInformationRequestImpl copy = reader.read("furnishChargingInformationRequest", FurnishChargingInformationRequestImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
        assertTrue(Arrays.equals(copy.getFCIBCCCAMELsequence1().getFreeFormatData().getData(), this.getDataFFD()));
        assertEquals(copy.getFCIBCCCAMELsequence1().getPartyToCharge().getSendingSideID(), LegType.leg2);
        assertEquals(copy.getFCIBCCCAMELsequence1().getAppendFreeFormatData(), AppendFreeFormatData.append);
    }
}
