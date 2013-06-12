/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

    public int[] getDigits() {
        return new int[] { 44, 55, 66 };
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
        assertTrue(Arrays.equals(elem.getDigitsResponse().getGenericDigits().getDigits(), this.getDigits()));
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
