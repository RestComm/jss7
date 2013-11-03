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

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePart;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class VariableMessageTest {

    public byte[] getData1() {
        return new byte[] { 48, 13, (byte) 128, 2, 3, 32, (byte) 161, 7, (byte) 128, 1, 111, (byte) 130, 2, 50, (byte) 149 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        VariableMessageImpl elem = new VariableMessageImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        elem.decodeAll(ais);
        assertEquals(elem.getElementaryMessageID(), 800);
        assertEquals(elem.getVariableParts().size(), 2);
        assertEquals((int) elem.getVariableParts().get(0).getInteger(), 111);
        assertEquals((int) elem.getVariableParts().get(1).getTime().getHour(), 23);
        assertEquals((int) elem.getVariableParts().get(1).getTime().getMinute(), 59);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        ArrayList<VariablePart> variableParts = new ArrayList<VariablePart>();
        VariablePartImpl vp = new VariablePartImpl(111);
        variableParts.add(vp);
        VariablePartTimeImpl time = new VariablePartTimeImpl(23, 59);
        vp = new VariablePartImpl(time);
        variableParts.add(vp);

        VariableMessageImpl elem = new VariableMessageImpl(800, variableParts);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        // int elementaryMessageID, ArrayList<VariablePart> variableParts
    }
}
