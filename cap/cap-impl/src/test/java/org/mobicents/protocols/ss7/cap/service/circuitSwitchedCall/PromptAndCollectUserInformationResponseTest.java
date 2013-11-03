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
import org.mobicents.protocols.ss7.cap.isup.DigitsImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class PromptAndCollectUserInformationResponseTest {

    public byte[] getData1() {
        return new byte[] { (byte) 128, 4, 65, 44, 55, 66 };
    }

    public byte[] getDigits() {
        return new byte[] { 44, 55, 66 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        PromptAndCollectUserInformationResponseImpl elem = new PromptAndCollectUserInformationResponseImpl();
        int tag = ais.readTag();
        assertEquals(tag, 0);
        elem.decodeAll(ais);
        assertEquals(elem.getDigitsResponse().getGenericDigits().getEncodingScheme(), 2);
        assertEquals(elem.getDigitsResponse().getGenericDigits().getTypeOfDigits(), 1);
        assertTrue(Arrays.equals(elem.getDigitsResponse().getGenericDigits().getEncodedDigits(), this.getDigits()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        GenericDigitsImpl genericDigits = new GenericDigitsImpl(2, 1, getDigits());
        // int encodingScheme, int typeOfDigits, int[] digits
        DigitsImpl digitsResponse = new DigitsImpl(genericDigits);

        PromptAndCollectUserInformationResponseImpl elem = new PromptAndCollectUserInformationResponseImpl(digitsResponse);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }
}
