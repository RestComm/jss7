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
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.restcomm.protocols.ss7.cap.api.isup.Digits;
import org.restcomm.protocols.ss7.cap.isup.DigitsImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.PromptAndCollectUserInformationResponseImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;
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

    public byte[] getDigits() {
        return new byte[] { 44, 55, 66 };
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
        assertTrue(Arrays.equals(elem.getDigitsResponse().getGenericDigits().getEncodedDigits(), this.getDigits()));
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

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        GenericNumber genericNumber = new GenericNumberImpl(1, "987", 0, 2, 3, true, 0);
//        int natureOfAddresIndicator, String address, int numberQualifierIndicator,
//        int numberingPlanIndicator, int addressRepresentationREstrictedIndicator, boolean numberIncomplete,
//        int screeningIndicator
        Digits digitsResponse = new DigitsImpl(genericNumber);
        PromptAndCollectUserInformationResponseImpl original = new PromptAndCollectUserInformationResponseImpl(digitsResponse);
        original.setInvokeId(21);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "promptAndCollectUserInformationResponse", PromptAndCollectUserInformationResponseImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        PromptAndCollectUserInformationResponseImpl copy = reader.read("promptAndCollectUserInformationResponse", PromptAndCollectUserInformationResponseImpl.class);

        assertEquals(copy.getInvokeId(), original.getInvokeId());
        assertEquals(copy.getDigitsResponse().getGenericNumber().getNatureOfAddressIndicator(), 1);
        assertEquals(copy.getDigitsResponse().getGenericNumber().getAddress(), "987");
        assertEquals(copy.getDigitsResponse().getGenericNumber().getNumberQualifierIndicator(), 0);
        assertEquals(copy.getDigitsResponse().getGenericNumber().getNumberingPlanIndicator(), 2);
    }
}
