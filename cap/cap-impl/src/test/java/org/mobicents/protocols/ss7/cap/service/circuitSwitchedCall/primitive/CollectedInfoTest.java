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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.primitives.ErrorTreatment;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CollectedInfoTest {

    public byte[] getData1() {
        return new byte[] { (byte) 160, 24, (byte) 128, 1, 2, (byte) 129, 1, 9, (byte) 130, 1, 1, (byte) 133, 1, 50,
                (byte) 135, 1, 0, (byte) 136, 1, (byte) 255, (byte) 137, 1, 0, (byte) 138, 1, 0 };
    }

    public byte[] getEndOfReplyDigit() {
        return new byte[] { 1 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        CollectedInfoImpl elem = new CollectedInfoImpl();
        int tag = ais.readTag();
        assertEquals(tag, 0);
        elem.decodeAll(ais);
        assertEquals((int) elem.getCollectedDigits().getMinimumNbOfDigits(), 2);
        assertEquals((int) elem.getCollectedDigits().getMaximumNbOfDigits(), 9);
        assertTrue(Arrays.equals(elem.getCollectedDigits().getEndOfReplyDigit(), getEndOfReplyDigit()));
        assertNull(elem.getCollectedDigits().getCancelDigit());
        assertNull(elem.getCollectedDigits().getStartDigit());
        assertEquals((int) elem.getCollectedDigits().getFirstDigitTimeOut(), 50);
        assertNull(elem.getCollectedDigits().getInterDigitTimeOut());
        assertEquals(elem.getCollectedDigits().getErrorTreatment(), ErrorTreatment.stdErrorAndInfo);
        assertTrue((boolean) elem.getCollectedDigits().getInterruptableAnnInd());
        assertFalse((boolean) elem.getCollectedDigits().getVoiceInformation());
        assertFalse((boolean) elem.getCollectedDigits().getVoiceBack());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        CollectedDigitsImpl cd = new CollectedDigitsImpl(2, 9, getEndOfReplyDigit(), null, null, 50, null,
                ErrorTreatment.stdErrorAndInfo, true, false, false);
        CollectedInfoImpl elem = new CollectedInfoImpl(cd);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        // Integer minimumNbOfDigits, int maximumNbOfDigits, byte[] endOfReplyDigit, byte[] cancelDigit, byte[] startDigit,
        // Integer firstDigitTimeOut, Integer interDigitTimeOut, ErrorTreatment errorTreatment, Boolean interruptableAnnInd,
        // Boolean voiceInformation,
        // Boolean voiceBack
    }
}
