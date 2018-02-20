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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
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
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedDigits;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedInfo;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSend;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.Tone;
import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.PromptAndCollectUserInformationRequestImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CollectedDigitsImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CollectedInfoImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InformationToSendImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ToneImpl;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class PromptAndCollectUserInformationRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 47, (byte) 160, 5, (byte) 160, 3, (byte) 129, 1, 10, (byte) 129, 1, 0, (byte) 162, 8,
                (byte) 161, 6, (byte) 128, 1, 10, (byte) 129, 1, 100, (byte) 163, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2,
                1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255, (byte) 132, 1, 22, (byte) 159, 51, 1, 0 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        PromptAndCollectUserInformationRequestImpl elem = new PromptAndCollectUserInformationRequestImpl();
        int tag = ais.readTag();
        assertEquals(tag, Tag.SEQUENCE);
        elem.decodeAll(ais);
        assertEquals(elem.getCollectedInfo().getCollectedDigits().getMaximumNbOfDigits(), 10);
        assertNull(elem.getCollectedInfo().getCollectedDigits().getMinimumNbOfDigits());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getEndOfReplyDigit());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getCancelDigit());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getMinimumNbOfDigits());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getStartDigit());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getFirstDigitTimeOut());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getInterDigitTimeOut());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getErrorTreatment());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getInterruptableAnnInd());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getVoiceInformation());
        assertNull(elem.getCollectedInfo().getCollectedDigits().getVoiceBack());
        assertFalse(elem.getDisconnectFromIPForbidden());
        assertEquals(elem.getInformationToSend().getTone().getToneID(), 10);
        assertEquals((int) elem.getInformationToSend().getTone().getDuration(), 100);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertEquals((int) elem.getCallSegmentID(), 22);
        assertFalse(elem.getRequestAnnouncementStartedNotification());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        CollectedDigitsImpl collectedDigits = new CollectedDigitsImpl(null, 10, null, null, null, null, null, null, null, null,
                null);
        // Integer minimumNbOfDigits, int maximumNbOfDigits, byte[] endOfReplyDigit, byte[] cancelDigit, byte[] startDigit,
        // Integer firstDigitTimeOut, Integer interDigitTimeOut, ErrorTreatment errorTreatment, Boolean interruptableAnnInd,
        // Boolean voiceInformation,
        // Boolean voiceBack
        CollectedInfoImpl collectedInfo = new CollectedInfoImpl(collectedDigits);

        ToneImpl tone = new ToneImpl(10, 100);
        // int toneID, Integer duration
        InformationToSendImpl informationToSend = new InformationToSendImpl(tone);

        PromptAndCollectUserInformationRequestImpl elem = new PromptAndCollectUserInformationRequestImpl(collectedInfo, false,
                informationToSend, CAPExtensionsTest.createTestCAPExtensions(), 22, false);
        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

        // CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
        // InformationToSend informationToSend, CAPExtensions extensions, Integer callSegmentID, Boolean
        // requestAnnouncementStartedNotification }
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        CollectedDigits collectedDigits = new CollectedDigitsImpl(11, 12, null, null, null, null, null, null, null, null, null);
        CollectedInfo collectedInfo = new CollectedInfoImpl(collectedDigits);
        Tone tone = new ToneImpl(7, 8);
        InformationToSend informationToSend = new InformationToSendImpl(tone);
        PromptAndCollectUserInformationRequestImpl original = new PromptAndCollectUserInformationRequestImpl(collectedInfo,
                true, informationToSend, CAPExtensionsTest.createTestCAPExtensions(), 18, false);
//        CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
//        InformationToSend informationToSend, CAPExtensions extensions, Integer callSegmentID,
//        Boolean requestAnnouncementStartedNotification
        original.setInvokeId(26);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "promptAndCollectUserInformationRequest", PromptAndCollectUserInformationRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        PromptAndCollectUserInformationRequestImpl copy = reader.read("promptAndCollectUserInformationRequest", PromptAndCollectUserInformationRequestImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
        assertEquals((int) copy.getCollectedInfo().getCollectedDigits().getMinimumNbOfDigits(), 11);
        assertEquals(copy.getCollectedInfo().getCollectedDigits().getMaximumNbOfDigits(), 12);
        assertTrue(copy.getDisconnectFromIPForbidden());
        assertEquals(copy.getInformationToSend().getTone().getToneID(), 7);
        assertEquals((int) copy.getInformationToSend().getTone().getDuration(), 8);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(copy.getExtensions()));
        assertEquals((int) copy.getCallSegmentID(), 18);
        assertFalse(copy.getRequestAnnouncementStartedNotification());


        original = new PromptAndCollectUserInformationRequestImpl(collectedInfo, null, null, null, null, null);
        original.setInvokeId(26);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "promptAndCollectUserInformationRequest", PromptAndCollectUserInformationRequestImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("promptAndCollectUserInformationRequest", PromptAndCollectUserInformationRequestImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
        assertEquals((int) copy.getCollectedInfo().getCollectedDigits().getMinimumNbOfDigits(), 11);
        assertEquals(copy.getCollectedInfo().getCollectedDigits().getMaximumNbOfDigits(), 12);
        assertNull(copy.getDisconnectFromIPForbidden());
        assertNull(copy.getInformationToSend());
        assertNull(copy.getExtensions());
        assertNull(copy.getCallSegmentID());
        assertNull(copy.getRequestAnnouncementStartedNotification());
    }
}
