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
import org.testng.annotations.Test;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class ChargingRollOverTest {

    public byte[] getData() {
        return new byte[] { -128, 3, -128, 1, 25 };
    };

    public byte[] getData1() {
        return new byte[] { -96, 8, -95, 6, -128, 1, 12, -127, 1, 24 };
    };

    public byte[] getData2() {
        return new byte[] { -127, 3, -128, 1, 24 };
    };

    public byte[] getData3() {
        return new byte[] { -95, 8, -95, 6, -128, 1, 12, -127, 1, 24 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
        // Option 0
        byte[] data = this.getData();
        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();
        ChargingRollOverImpl prim = new ChargingRollOverImpl();
        prim.decodeAll(asn);
        assertEquals(tag, ChargingRollOverImpl._ID_transferredVolumeRollOver);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertTrue(prim.getIsPrimitive());
        assertEquals(prim.getTransferredVolumeRollOver().getROVolumeIfNoTariffSwitch().longValue(), 25);
        assertNull(prim.getElapsedTimeRollOver());

        // Option 1
        data = this.getData1();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ChargingRollOverImpl();
        prim.decodeAll(asn);
        assertEquals(tag, ChargingRollOverImpl._ID_transferredVolumeRollOver);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertFalse(prim.getIsPrimitive());
        assertNull(prim.getTransferredVolumeRollOver().getROVolumeIfNoTariffSwitch());
        assertEquals(prim.getTransferredVolumeRollOver().getROVolumeIfTariffSwitch().getROVolumeSinceLastTariffSwitch()
                .intValue(), 12);
        assertEquals(prim.getTransferredVolumeRollOver().getROVolumeIfTariffSwitch().getROVolumeTariffSwitchInterval()
                .intValue(), 24);
        assertNull(prim.getElapsedTimeRollOver());

        // Option 2
        data = this.getData2();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ChargingRollOverImpl();
        prim.decodeAll(asn);
        assertEquals(tag, ChargingRollOverImpl._ID_elapsedTimeRollOver);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertTrue(prim.getIsPrimitive());
        assertEquals(prim.getElapsedTimeRollOver().getROTimeGPRSIfNoTariffSwitch().intValue(), 24);
        assertNull(prim.getTransferredVolumeRollOver());

        // Option 3
        data = this.getData3();
        asn = new AsnInputStream(data);
        tag = asn.readTag();
        prim = new ChargingRollOverImpl();
        prim.decodeAll(asn);
        assertEquals(tag, ChargingRollOverImpl._ID_elapsedTimeRollOver);
        assertEquals(asn.getTagClass(), Tag.CLASS_CONTEXT_SPECIFIC);
        assertFalse(prim.getIsPrimitive());
        assertNull(prim.getElapsedTimeRollOver().getROTimeGPRSIfNoTariffSwitch());
        assertEquals(prim.getElapsedTimeRollOver().getROTimeGPRSIfTariffSwitch().getROTimeGPRSSinceLastTariffSwitch()
                .intValue(), 12);
        assertEquals(
                prim.getElapsedTimeRollOver().getROTimeGPRSIfTariffSwitch().getROTimeGPRSTariffSwitchInterval().intValue(), 24);
        assertNull(prim.getTransferredVolumeRollOver());

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
        // Option 0
        TransferredVolumeRollOverImpl transferredVolumeRollOver = new TransferredVolumeRollOverImpl(new Integer(25));
        ChargingRollOverImpl prim = new ChargingRollOverImpl(transferredVolumeRollOver);
        AsnOutputStream asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));

        // Option 1
        ROVolumeIfTariffSwitchImpl roVolumeIfTariffSwitch = new ROVolumeIfTariffSwitchImpl(new Integer(12), new Integer(24));
        transferredVolumeRollOver = new TransferredVolumeRollOverImpl(roVolumeIfTariffSwitch);
        prim = new ChargingRollOverImpl(transferredVolumeRollOver);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData1()));

        // Option 2
        ElapsedTimeRollOverImpl elapsedTimeRollOver = new ElapsedTimeRollOverImpl(new Integer(24));
        prim = new ChargingRollOverImpl(elapsedTimeRollOver);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData2()));

        // Option 3
        ROTimeGPRSIfTariffSwitchImpl roTimeGPRSIfTariffSwitch = new ROTimeGPRSIfTariffSwitchImpl(new Integer(12), new Integer(
                24));
        elapsedTimeRollOver = new ElapsedTimeRollOverImpl(roTimeGPRSIfTariffSwitch);
        prim = new ChargingRollOverImpl(elapsedTimeRollOver);
        asn = new AsnOutputStream();
        prim.encodeAll(asn);
        assertTrue(Arrays.equals(asn.toByteArray(), this.getData3()));
    }

}
