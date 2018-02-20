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
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InbandInfo;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageID;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Tone;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InbandInfoImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InformationToSendImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.MessageIDImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ToneImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class InformationToSendTest {

    public byte[] getData1() {
        return new byte[] { (byte) 160, 8, (byte) 160, 3, (byte) 128, 1, 91, (byte) 129, 1, 1 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 161, 6, (byte) 128, 1, 5, (byte) 129, 1, 100 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        InformationToSendImpl elem = new InformationToSendImpl();
        int tag = ais.readTag();
        assertEquals(tag, 0);
        elem.decodeAll(ais);
        assertEquals((int) elem.getInbandInfo().getMessageID().getElementaryMessageID(), 91);
        assertEquals((int) elem.getInbandInfo().getNumberOfRepetitions(), 1);
        assertNull(elem.getInbandInfo().getDuration());
        assertNull(elem.getInbandInfo().getInterval());
        assertNull(elem.getTone());

        data = this.getData2();
        ais = new AsnInputStream(data);
        elem = new InformationToSendImpl();
        tag = ais.readTag();
        assertEquals(tag, 1);
        elem.decodeAll(ais);
        assertNull(elem.getInbandInfo());
        assertEquals(elem.getTone().getToneID(), 5);
        assertEquals((int) elem.getTone().getDuration(), 100);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        MessageIDImpl messageID = new MessageIDImpl(91);
        InbandInfoImpl inbandInfo = new InbandInfoImpl(messageID, 1, null, null);
        // MessageID messageID, Integer numberOfRepetitions, Integer duration, Integer interval
        InformationToSendImpl elem = new InformationToSendImpl(inbandInfo);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        ToneImpl tone = new ToneImpl(5, 100);
        // int toneID, Integer duration
        elem = new InformationToSendImpl(tone);
        aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        MessageID messageID = new MessageIDImpl(10);
        InbandInfo inbandInfo = new InbandInfoImpl(messageID, null, null, null);
        InformationToSendImpl original = new InformationToSendImpl(inbandInfo);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "informationToSend", InformationToSendImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        InformationToSendImpl copy = reader.read("informationToSend", InformationToSendImpl.class);

        assertEquals((int) copy.getInbandInfo().getMessageID().getElementaryMessageID(), 10);
        assertNull(copy.getTone());

        Tone tone = new ToneImpl(15, null);
        original = new InformationToSendImpl(tone);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "informationToSend", InformationToSendImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("informationToSend", InformationToSendImpl.class);

        assertNull(copy.getInbandInfo());
        assertEquals(copy.getTone().getToneID(), 15);
    }
}
