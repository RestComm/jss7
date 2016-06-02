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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePart;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InbandInfoImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InformationToSendImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.MessageIDImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ToneImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.VariableMessageImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.VariablePartDateImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.VariablePartImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.VariablePartTimeImpl;
import org.testng.annotations.Test;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class PlayAnnouncementRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 43, (byte) 160, 8, (byte) 161, 6, (byte) 128, 1, 10, (byte) 129, 1, 100, (byte) 129, 1,
                (byte) 255, (byte) 130, 1, 0, (byte) 163, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1,
                (byte) 129, 1, (byte) 255, (byte) 133, 1, 22, (byte) 159, 51, 1, (byte) 255 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        PlayAnnouncementRequestImpl elem = new PlayAnnouncementRequestImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        elem.decodeAll(ais);
        assertEquals(elem.getInformationToSend().getTone().getToneID(), 10);
        assertEquals((int) elem.getInformationToSend().getTone().getDuration(), 100);
        assertTrue(elem.getDisconnectFromIPForbidden());
        assertFalse(elem.getRequestAnnouncementCompleteNotification());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertEquals((int) elem.getCallSegmentID(), 22);
        assertTrue(elem.getRequestAnnouncementStartedNotification());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        ToneImpl tone = new ToneImpl(10, 100);
        // int toneID, Integer duration
        InformationToSendImpl informationToSend = new InformationToSendImpl(tone);

        PlayAnnouncementRequestImpl elem = new PlayAnnouncementRequestImpl(informationToSend, true, false,
                CAPExtensionsTest.createTestCAPExtensions(), 22, true);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        // InformationToSend informationToSend, boolean disconnectFromIPForbidden,
        // boolean requestAnnouncementCompleteNotification, CAPExtensions extensions, Integer callSegmentID, boolean
        // requestAnnouncementStartedNotification
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        ArrayList<VariablePart> aL = new ArrayList<VariablePart>();
        aL.add(new VariablePartImpl(new VariablePartDateImpl(2015, 6, 27)));
        aL.add(new VariablePartImpl(new VariablePartTimeImpl(15, 10)));
        aL.add(new VariablePartImpl(new Integer(145)));
        VariableMessageImpl vm = new VariableMessageImpl(145, aL);
        MessageIDImpl mi = new MessageIDImpl(vm);
        InbandInfoImpl inbandInfo = new InbandInfoImpl(mi, new Integer(5), new Integer(8), new Integer(2));
        InformationToSendImpl informationToSend = new InformationToSendImpl(inbandInfo);

        PlayAnnouncementRequestImpl original = new PlayAnnouncementRequestImpl(informationToSend, Boolean.TRUE, Boolean.TRUE,
                null, new Integer(1), Boolean.FALSE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "playAnnouncementArg", PlayAnnouncementRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        PlayAnnouncementRequestImpl copy = reader.read("playAnnouncementArg", PlayAnnouncementRequestImpl.class);

        assertTrue(isEqual(original, copy));
    }

    private boolean isEqual(PlayAnnouncementRequestImpl o1, PlayAnnouncementRequestImpl o2) {
        if (o1 == o2)
            return true;
        if (o1 == null && o2 != null || o1 != null && o2 == null)
            return false;
        if (o1 == null && o2 == null)
            return true;
        if (!o1.toString().equals(o2.toString()))
            return false;
        return true;
    }
}
