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
package org.mobicents.protocols.ss7.cap.service.gprs;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ChargingCharacteristics;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.ChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ApplyChargingGPRSRequestTest {

    public byte[] getData() {
        return new byte[] { 48, 12, -96, 4, -128, 2, 0, -56, -127, 1, 24, -126, 1, 2 };
    };

    public byte[] getDataLiveTrace() {
        return new byte[] { 0x30, 0x07, (byte) 0xa0, 0x05, (byte) 0x80, 0x03, 0x30, 0x00, 0x00 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        ApplyChargingGPRSRequestImpl prim = new ApplyChargingGPRSRequestImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(prim.getChargingCharacteristics().getMaxTransferredVolume(), 200L);
        assertEquals(prim.getTariffSwitchInterval().intValue(), 24);
        assertEquals(prim.getPDPID().getId(), 2);
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecodeLiveTrace() throws Exception {
        byte[] data = this.getDataLiveTrace();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        ApplyChargingGPRSRequestImpl prim = new ApplyChargingGPRSRequestImpl();
        prim.decodeAll(asn);

        assertEquals(tag, Tag.SEQUENCE);
        assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

        assertEquals(prim.getChargingCharacteristics().getMaxTransferredVolume(), 3145728L);
        assertNull(prim.getTariffSwitchInterval());
        assertNull(prim.getPDPID());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {

        ChargingCharacteristics chargingCharacteristics = new ChargingCharacteristicsImpl(200L);
        ;
        Integer tariffSwitchInterval = new Integer(24);
        PDPID pdpID = new PDPIDImpl(2);

        ApplyChargingGPRSRequestImpl prim = new ApplyChargingGPRSRequestImpl(chargingCharacteristics, tariffSwitchInterval,
                pdpID);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncodeLiveTrace() throws Exception {

        ChargingCharacteristics chargingCharacteristics = new ChargingCharacteristicsImpl(3145728L);
        ;

        ApplyChargingGPRSRequestImpl prim = new ApplyChargingGPRSRequestImpl(chargingCharacteristics, null, null);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);

        assertTrue(Arrays.equals(asn.toByteArray(), this.getDataLiveTrace()));
    }

}
