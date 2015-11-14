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

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallDiversionTreatmentIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallingPartyRestrictionIndicator;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ConferenceTreatmentIndicator;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class ForwardServiceInteractionIndTest {

    public byte[] getData1() {
        return new byte[] { 48, 9, (byte) 129, 1, 2, (byte) 130, 1, 1, (byte) 132, 1, 2 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        ForwardServiceInteractionIndImpl elem = new ForwardServiceInteractionIndImpl();
        int tag = ais.readTag();
        assertEquals(ais.getTag(), Tag.SEQUENCE);
        assertEquals(ais.getTagClass(), Tag.CLASS_UNIVERSAL);

        elem.decodeAll(ais);

        assertEquals(elem.getConferenceTreatmentIndicator(), ConferenceTreatmentIndicator.rejectConferenceRequest);
        assertEquals(elem.getCallDiversionTreatmentIndicator(), CallDiversionTreatmentIndicator.callDiversionAllowed);
        assertEquals(elem.getCallingPartyRestrictionIndicator(), CallingPartyRestrictionIndicator.presentationRestricted);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {

        ForwardServiceInteractionIndImpl elem = new ForwardServiceInteractionIndImpl(ConferenceTreatmentIndicator.rejectConferenceRequest,
                CallDiversionTreatmentIndicator.callDiversionAllowed, CallingPartyRestrictionIndicator.presentationRestricted);
//        ConferenceTreatmentIndicator conferenceTreatmentIndicator,
//        CallDiversionTreatmentIndicator callDiversionTreatmentIndicator, CallingPartyRestrictionIndicator callingPartyRestrictionIndicator

        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerialize() throws Exception {

        ForwardServiceInteractionIndImpl original = new ForwardServiceInteractionIndImpl(ConferenceTreatmentIndicator.rejectConferenceRequest,
                CallDiversionTreatmentIndicator.callDiversionAllowed, CallingPartyRestrictionIndicator.presentationRestricted);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "forwardServiceInteractionInd", ForwardServiceInteractionIndImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ForwardServiceInteractionIndImpl copy = reader.read("forwardServiceInteractionInd", ForwardServiceInteractionIndImpl.class);

        assertEquals(original.getConferenceTreatmentIndicator(), copy.getConferenceTreatmentIndicator());
        assertEquals(original.getCallDiversionTreatmentIndicator(), copy.getCallDiversionTreatmentIndicator());
        assertEquals(original.getCallingPartyRestrictionIndicator(), copy.getCallingPartyRestrictionIndicator());
    }
}
