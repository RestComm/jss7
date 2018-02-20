/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageIDText;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariableMessage;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePart;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.MessageIDImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.MessageIDTextImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.VariableMessageImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.VariablePartImpl;
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

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        Integer elementaryMessageID = 31;
        MessageIDImpl original = new MessageIDImpl(elementaryMessageID);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "messageID", MessageIDImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        MessageIDImpl copy = reader.read("messageID", MessageIDImpl.class);

        assertEquals(copy.getElementaryMessageID(), elementaryMessageID);
        assertNull(copy.getText());
        assertNull(copy.getElementaryMessageIDs());
        assertNull(copy.getVariableMessage());


        String messageContent = "Perekop";
        MessageIDText text = new MessageIDTextImpl(messageContent, null);
        original = new MessageIDImpl(text);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "messageID", MessageIDImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("messageID", MessageIDImpl.class);

        assertNull(copy.getElementaryMessageID());
        assertEquals(copy.getText().getMessageContent(), messageContent);
        assertNull(copy.getText().getAttributes());
        assertNull(copy.getElementaryMessageIDs());
        assertNull(copy.getVariableMessage());


        ArrayList<Integer> elementaryMessageIDs = new ArrayList<Integer>();
        elementaryMessageIDs.add(12);
        elementaryMessageIDs.add(13);
        original = new MessageIDImpl(elementaryMessageIDs);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "messageID", MessageIDImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("messageID", MessageIDImpl.class);

        assertNull(copy.getElementaryMessageID());
        assertNull(copy.getText());
        ArrayList<Integer> al1 = copy.getElementaryMessageIDs();
        assertEquals(al1.size(), 2);
        assertEquals((int) al1.get(0), 12);
        assertEquals((int) al1.get(1), 13);
        assertNull(copy.getVariableMessage());


        int elementaryMessageID_2 = 3;
        ArrayList<VariablePart> variableParts = new ArrayList<VariablePart>();
        VariablePart variablePart = new VariablePartImpl(18);
        variableParts.add(variablePart);
        VariableMessage variableMessage = new VariableMessageImpl(elementaryMessageID_2, variableParts);
        original = new MessageIDImpl(variableMessage);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "messageID", MessageIDImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("messageID", MessageIDImpl.class);

        assertNull(copy.getElementaryMessageID());
        assertNull(copy.getText());
        assertNull(copy.getElementaryMessageIDs());
        assertEquals(copy.getVariableMessage().getElementaryMessageID(), elementaryMessageID_2);
        ArrayList<VariablePart> vps = copy.getVariableMessage().getVariableParts();
        assertEquals(vps.size(), 1);
        assertEquals((int) vps.get(0).getInteger(), 18);
    }
}
