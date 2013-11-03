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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAI_GSM0224Test {

    public byte[] getData1() {
        return new byte[] { 48, 21, (byte) 128, 1, 1, (byte) 129, 1, 2, (byte) 130, 1, 3, (byte) 131, 1, 4, (byte) 132, 1, 5,
                (byte) 133, 1, 6, (byte) 134, 1, 7 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        CAI_GSM0224Impl elem = new CAI_GSM0224Impl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals((int) elem.getE1(), 1);
        assertEquals((int) elem.getE2(), 2);
        assertEquals((int) elem.getE3(), 3);
        assertEquals((int) elem.getE4(), 4);
        assertEquals((int) elem.getE5(), 5);
        assertEquals((int) elem.getE6(), 6);
        assertEquals((int) elem.getE7(), 7);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        CAI_GSM0224Impl elem = new CAI_GSM0224Impl(1, 2, 3, 4, 5, 6, 7);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }
}
