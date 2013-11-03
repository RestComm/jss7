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
package org.mobicents.protocols.ss7.cap.service.gprs.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.TimeGPRSIfTariffSwitch;
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ChargingResultTest {

    public byte[] getData() {
        return new byte[] { -95, 3, -128, 1, 24 };
    };

    public byte[] getData1() {
        return new byte[] { -95, 8, -95, 6, -128, 1, 12, -127, 1, 24 };
    };

    public byte[] getData2() {
        return new byte[] { -96, 3, -128, 1, 25 };
    };

    public byte[] getData3() {
        return new byte[] { -96, 8, -95, 6, -128, 1, 12, -127, 1, 24 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        // Option 0
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        ChargingResultImpl prim = new ChargingResultImpl();
        prim.decodeAll(asn);
        assertEquals(tag, ChargingResultImpl._ID_elapsedTime);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertFalse(prim.getIsPrimitive());
        assertEquals(prim.getElapsedTime().getTimeGPRSIfNoTariffSwitch().intValue(), 24);
        assertNull(prim.getTransferredVolume());

        // Option 1
        data = this.getData1();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ChargingResultImpl();
        prim.decodeAll(asn);
        assertEquals(tag, ChargingResultImpl._ID_elapsedTime);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertFalse(prim.getIsPrimitive());
        assertNull(prim.getElapsedTime().getTimeGPRSIfNoTariffSwitch());
        assertEquals(prim.getElapsedTime().getTimeGPRSIfTariffSwitch().getTimeGPRSSinceLastTariffSwitch(), 12);
        assertEquals(prim.getElapsedTime().getTimeGPRSIfTariffSwitch().getTimeGPRSTariffSwitchInterval().intValue(), 24);
        assertNull(prim.getTransferredVolume());

        // Option 2
        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ChargingResultImpl();
        prim.decodeAll(asn);
        assertEquals(tag, ChargingResultImpl._ID_transferredVolume);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertFalse(prim.getIsPrimitive());
        assertEquals(prim.getTransferredVolume().getVolumeIfNoTariffSwitch().longValue(), 25);
        assertNull(prim.getElapsedTime());

        // Option 3
        data = this.getData3();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ChargingResultImpl();
        prim.decodeAll(asn);
        assertEquals(tag, ChargingResultImpl._ID_transferredVolume);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertFalse(prim.getIsPrimitive());
        assertNull(prim.getTransferredVolume().getVolumeIfNoTariffSwitch());
        assertEquals(prim.getTransferredVolume().getVolumeIfTariffSwitch().getVolumeSinceLastTariffSwitch(), 12);
        assertEquals(prim.getTransferredVolume().getVolumeIfTariffSwitch().getVolumeTariffSwitchInterval().longValue(), 24);
        assertNull(prim.getElapsedTime());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        // Option 0
        ElapsedTimeImpl elapsedTime = new ElapsedTimeImpl(new Integer(24));
        ChargingResultImpl prim = new ChargingResultImpl(elapsedTime);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        // Option 1
        TimeGPRSIfTariffSwitch timeGPRSIfTariffSwitch = new TimeGPRSIfTariffSwitchImpl(12, new Integer(24));
        elapsedTime = new ElapsedTimeImpl(timeGPRSIfTariffSwitch);
        prim = new ChargingResultImpl(elapsedTime);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

        // Option 2
        TransferredVolumeImpl extQoSSubscribed = new TransferredVolumeImpl(new Long(25));
        prim = new ChargingResultImpl(extQoSSubscribed);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));

        // Option 3
        VolumeIfTariffSwitchImpl volumeIfTariffSwitch = new VolumeIfTariffSwitchImpl(12, new Long(24));
        extQoSSubscribed = new TransferredVolumeImpl(volumeIfTariffSwitch);
        prim = new ChargingResultImpl(extQoSSubscribed);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData3()));
    }

}
