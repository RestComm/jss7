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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class VariablePartPriceTest {

    public byte[] getData1() {
        return new byte[] { (byte) 132, 4, 0, 0, 32, 69 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 132, 4, (byte) 135, (byte) 152, (byte) 137, (byte) 151 };
    }

    public byte[] getData3() {
        return new byte[] {};
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        VariablePartPriceImpl elem = new VariablePartPriceImpl();
        int tag = ais.readTag();
        assertEquals(tag, 4);
        elem.decodeAll(ais);
        assertEquals(elem.getPriceIntegerPart(), 2);
        assertEquals(elem.getPriceHundredthPart(), 54);

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new VariablePartPriceImpl();
        tag = ais.readTag();
        assertEquals(tag, 4);
        elem.decodeAll(ais);
        assertEquals(elem.getPriceIntegerPart(), 788998);
        assertEquals(elem.getPriceHundredthPart(), 79);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        VariablePartPriceImpl elem = new VariablePartPriceImpl(2, 54);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 4);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        elem = new VariablePartPriceImpl(99788998, 79);
        aos = new AsnOutputStream();
        elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 4);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }
}
