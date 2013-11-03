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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.primitives.ErrorTreatment;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CollectedDigitsTest {

    public byte[] getData1() {
        return new byte[] { 48, 34, (byte) 128, 1, 15, (byte) 129, 1, 30, (byte) 130, 1, 1, (byte) 131, 2, 2, 2, (byte) 132, 1,
                55, (byte) 133, 1, 100, (byte) 134, 1, 101, (byte) 135, 1, 2, (byte) 136, 1, 0, (byte) 137, 1, (byte) 255,
                (byte) 138, 1, 0 };
    }

    public byte[] getEndOfReplyDigit() {
        return new byte[] { 1 };
    }

    public byte[] getCancelDigit() {
        return new byte[] { 2, 2 };
    }

    public byte[] getStartDigit() {
        return new byte[] { 55 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        CollectedDigitsImpl elem = new CollectedDigitsImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        elem.decodeAll(ais);
        assertEquals((int) elem.getMinimumNbOfDigits(), 15);
        assertEquals((int) elem.getMaximumNbOfDigits(), 30);
        assertTrue(Arrays.equals(elem.getEndOfReplyDigit(), getEndOfReplyDigit()));
        assertTrue(Arrays.equals(elem.getCancelDigit(), getCancelDigit()));
        assertTrue(Arrays.equals(elem.getStartDigit(), getStartDigit()));
        assertEquals((int) elem.getFirstDigitTimeOut(), 100);
        assertEquals((int) elem.getInterDigitTimeOut(), 101);
        assertEquals(elem.getErrorTreatment(), ErrorTreatment.repeatPrompt);
        assertFalse((boolean) elem.getInterruptableAnnInd());
        assertTrue((boolean) elem.getVoiceInformation());
        assertFalse((boolean) elem.getVoiceBack());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        CollectedDigitsImpl elem = new CollectedDigitsImpl(15, 30, getEndOfReplyDigit(), getCancelDigit(), getStartDigit(),
                100, 101, ErrorTreatment.repeatPrompt, false, true, false);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        // Integer minimumNbOfDigits, int maximumNbOfDigits, byte[] endOfReplyDigit, byte[] cancelDigit, byte[] startDigit,
        // Integer firstDigitTimeOut, Integer interDigitTimeOut, ErrorTreatment errorTreatment, Boolean interruptableAnnInd,
        // Boolean voiceInformation,
        // Boolean voiceBack
    }
}
