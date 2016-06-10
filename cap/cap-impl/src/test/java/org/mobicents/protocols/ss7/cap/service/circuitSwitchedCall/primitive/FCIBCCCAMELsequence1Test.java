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
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FreeFormatData;
import org.mobicents.protocols.ss7.cap.primitives.SendingSideIDImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class FCIBCCCAMELsequence1Test {

    public byte[] getData1() {
        return new byte[] { 48, 14, (byte) 128, 4, 4, 5, 6, 7, (byte) 161, 3, (byte) 128, 1, 2, (byte) 130, 1, 1 };
    }

    public byte[] getDataFFD() {
        return new byte[] { 4, 5, 6, 7 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        FCIBCCCAMELsequence1Impl elem = new FCIBCCCAMELsequence1Impl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertTrue(Arrays.equals(elem.getFreeFormatData().getData(), this.getDataFFD()));
        assertEquals(elem.getPartyToCharge().getSendingSideID(), LegType.leg2);
        assertEquals(elem.getAppendFreeFormatData(), AppendFreeFormatData.append);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        SendingSideIDImpl partyToCharge = new SendingSideIDImpl(LegType.leg2);
        FreeFormatData ffd = new FreeFormatDataImpl(getDataFFD());
        FCIBCCCAMELsequence1Impl elem = new FCIBCCCAMELsequence1Impl(ffd, partyToCharge, AppendFreeFormatData.append);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        // byte[] freeFormatData, SendingSideID partyToCharge, AppendFreeFormatData appendFreeFormatData
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        FreeFormatData ffd = new FreeFormatDataImpl(getDataFFD());
        SendingSideIDImpl partyToCharge = new SendingSideIDImpl(LegType.leg2);
        FCIBCCCAMELsequence1Impl original = new FCIBCCCAMELsequence1Impl(ffd, partyToCharge, AppendFreeFormatData.append);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "fciBCCCAMELsequence1", FCIBCCCAMELsequence1Impl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        FCIBCCCAMELsequence1Impl copy = reader.read("fciBCCCAMELsequence1", FCIBCCCAMELsequence1Impl.class);

        assertEquals(copy.getFreeFormatData().getData(), getDataFFD());
        assertEquals(copy.getPartyToCharge().getSendingSideID(), LegType.leg2);
        assertEquals(copy.getAppendFreeFormatData(), AppendFreeFormatData.append);

        original = new FCIBCCCAMELsequence1Impl(ffd, null, null);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "fciBCCCAMELsequence1", FCIBCCCAMELsequence1Impl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("fciBCCCAMELsequence1", FCIBCCCAMELsequence1Impl.class);

        assertEquals(copy.getFreeFormatData().getData(), getDataFFD());
        assertNull(copy.getPartyToCharge());
        assertNull(copy.getAppendFreeFormatData());
    }
}