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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePart;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MessageIDTest {

    public byte[] getData1() {
        return new byte[] { (byte) 128, 1, 123 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 161, 7, (byte) 128, 5, 84, 111, 100, 97, 121 };
    }

    public byte[] getData3() {
        return new byte[] { (byte) 189, 6, 2, 1, 0, 2, 1, (byte) 255 };
    }

    public byte[] getData4() {
        return new byte[] { (byte) 190, 8, (byte) 128, 1, 99, (byte) 161, 3, (byte) 128, 1, 28 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        MessageIDImpl elem = new MessageIDImpl();
        int tag = ais.readTag();
        assertEquals(tag, 0);
        elem.decodeAll(ais);
        assertEquals((int) elem.getElementaryMessageID(), 123);
        assertNull(elem.getText());
        assertNull(elem.getElementaryMessageIDs());
        assertNull(elem.getVariableMessage());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new MessageIDImpl();
        tag = ais.readTag();
        assertEquals(tag, 1);
        elem.decodeAll(ais);
        assertNull(elem.getElementaryMessageID());
        assertTrue(elem.getText().getMessageContent().equals("Today"));
        assertNull(elem.getText().getAttributes());
        assertNull(elem.getElementaryMessageIDs());
        assertNull(elem.getVariableMessage());

        data = this.getData3();
        ais = new AsnInputStream(data);
        elem = new MessageIDImpl();
        tag = ais.readTag();
        assertEquals(tag, 29);
        elem.decodeAll(ais);
        assertNull(elem.getElementaryMessageID());
        assertNull(elem.getText());
        assertEquals(elem.getElementaryMessageIDs().size(), 2);
        assertEquals((int) elem.getElementaryMessageIDs().get(0), 0);
        assertEquals((int) elem.getElementaryMessageIDs().get(1), -1);
        assertNull(elem.getVariableMessage());

        data = this.getData4();
        ais = new AsnInputStream(data);
        elem = new MessageIDImpl();
        tag = ais.readTag();
        assertEquals(tag, 30);
        elem.decodeAll(ais);
        assertNull(elem.getElementaryMessageID());
        assertNull(elem.getText());
        assertNull(elem.getElementaryMessageIDs());
        assertEquals(elem.getVariableMessage().getElementaryMessageID(), 99);
        assertEquals(elem.getVariableMessage().getVariableParts().size(), 1);
        assertEquals((int) elem.getVariableMessage().getVariableParts().get(0).getInteger(), 28);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        MessageIDImpl elem = new MessageIDImpl(123);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        MessageIDTextImpl text = new MessageIDTextImpl("Today", null);
        elem = new MessageIDImpl(text);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        ArrayList<Integer> elementaryMessageIDs = new ArrayList<Integer>();
        elementaryMessageIDs.add(0);
        elementaryMessageIDs.add(-1);
        elem = new MessageIDImpl(elementaryMessageIDs);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData3()));

        ArrayList<VariablePart> variableParts = new ArrayList<VariablePart>();
        VariablePartImpl vp = new VariablePartImpl(28);
        variableParts.add(vp);
        VariableMessageImpl vm = new VariableMessageImpl(99, variableParts);
        elem = new MessageIDImpl(vm);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData4()));
        // int elementaryMessageID, ArrayList<VariablePart> variableParts
    }
}
