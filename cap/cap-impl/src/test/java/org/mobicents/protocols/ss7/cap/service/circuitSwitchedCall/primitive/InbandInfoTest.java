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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageID;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class InbandInfoTest {

    public byte[] getData1() {
        return new byte[] { 48, 5, (byte) 160, 3, (byte) 128, 1, 11 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 19, (byte) 160, 8, (byte) 161, 6, (byte) 128, 4, 73, 110, 102, 111, (byte) 129, 1, 3,
                (byte) 130, 1, 2, (byte) 131, 1, 1 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        InbandInfoImpl elem = new InbandInfoImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        elem.decodeAll(ais);
        assertEquals((int) elem.getMessageID().getElementaryMessageID(), 11);
        assertNull(elem.getNumberOfRepetitions());
        assertNull(elem.getDuration());
        assertNull(elem.getInterval());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new InbandInfoImpl();
        tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        elem.decodeAll(ais);
        assertTrue(elem.getMessageID().getText().getMessageContent().equals("Info"));
        assertEquals((int) elem.getNumberOfRepetitions(), 3);
        assertEquals((int) elem.getDuration(), 2);
        assertEquals((int) elem.getInterval(), 1);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        MessageIDImpl messageID = new MessageIDImpl(11);
        InbandInfoImpl elem = new InbandInfoImpl(messageID, null, null, null);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        MessageIDTextImpl text = new MessageIDTextImpl("Info", null);
        messageID = new MessageIDImpl(text);
        elem = new InbandInfoImpl(messageID, 3, 2, 1);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

        // MessageID messageID, Integer numberOfRepetitions, Integer duration, Integer interval
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        MessageID messageID = new MessageIDImpl(10);
        InbandInfoImpl original = new InbandInfoImpl(messageID, null, null, null);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "inbandInfo", InbandInfoImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        InbandInfoImpl copy = reader.read("inbandInfo", InbandInfoImpl.class);

        assertEquals((int) copy.getMessageID().getElementaryMessageID(), 10);
        assertNull(copy.getNumberOfRepetitions());
        assertNull(copy.getDuration());
        assertNull(copy.getInterval());


        original = new InbandInfoImpl(messageID, 1, 2, 3);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "inbandInfo", InbandInfoImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("inbandInfo", InbandInfoImpl.class);

        assertEquals((int) copy.getMessageID().getElementaryMessageID(), 10);
        assertEquals((int) copy.getNumberOfRepetitions(), 1);
        assertEquals((int) copy.getDuration(), 2);
        assertEquals((int) copy.getInterval(), 3);
    }
}
