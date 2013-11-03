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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.primitives.TimerID;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ResetTimerTest {

    public byte[] getData1() {
        return new byte[] { 48, 30, (byte) 128, 1, 0, (byte) 129, 2, 3, (byte) 232, (byte) 162, 18, 48, 5, 2, 1, 2, (byte) 129,
                0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255, (byte) 131, 1, 100 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        ResetTimerRequestImpl elem = new ResetTimerRequestImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);
        assertEquals(elem.getTimerID(), TimerID.tssf);
        assertEquals(elem.getTimerValue(), 1000);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertEquals((int) elem.getCallSegmentID(), 100);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        ResetTimerRequestImpl elem = new ResetTimerRequestImpl(TimerID.tssf, 1000, CAPExtensionsTest.createTestCAPExtensions(),
                100);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }
}
