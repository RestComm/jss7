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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4FunctionalitiesImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class InitiateCallAttemptResponseTest {

    public byte[] getData1() {
        return new byte[] { 48, 32, (byte) 128, 2, 4, (byte) 224, (byte) 162, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1,
                (byte) 255, (byte) 129, 4, 4, (byte) 128, 0, 0, (byte) 131, 0 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {

        byte[] data = this.getData1();
        AsnInputStream ais = new AsnInputStream(data);
        InitiateCallAttemptResponseImpl elem = new InitiateCallAttemptResponseImpl();
        int tag = ais.readTag();
        elem.decodeAll(ais);

        assertTrue(elem.getSupportedCamelPhases().getPhase1Supported());
        assertTrue(elem.getSupportedCamelPhases().getPhase2Supported());
        assertTrue(elem.getSupportedCamelPhases().getPhase3Supported());
        assertFalse(elem.getSupportedCamelPhases().getPhase4Supported());
        assertTrue(elem.getOfferedCamel4Functionalities().getInitiateCallAttempt());
        assertFalse(elem.getOfferedCamel4Functionalities().getCollectInformation());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertTrue(elem.getReleaseCallArgExtensionAllowed());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {

        SupportedCamelPhasesImpl supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, false);
        OfferedCamel4FunctionalitiesImpl offeredCamel4Functionalities = new OfferedCamel4FunctionalitiesImpl(true, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false, false, false, false);
        InitiateCallAttemptResponseImpl elem = new InitiateCallAttemptResponseImpl(supportedCamelPhases, offeredCamel4Functionalities,
                CAPExtensionsTest.createTestCAPExtensions(), true);
//        SupportedCamelPhases supportedCamelPhases,
//        OfferedCamel4Functionalities offeredCamel4Functionalities, CAPExtensions extensions,
//        boolean releaseCallArgExtensionAllowed

        AsnOutputStream aos = new AsnOutputStream();
        elem.encodeAll(aos);
        assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));
    }

    @Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        SupportedCamelPhasesImpl supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, false);
        OfferedCamel4FunctionalitiesImpl offeredCamel4Functionalities = new OfferedCamel4FunctionalitiesImpl(true, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false, false, false, false);
        InitiateCallAttemptResponseImpl original = new InitiateCallAttemptResponseImpl(supportedCamelPhases, offeredCamel4Functionalities,
                CAPExtensionsTest.createTestCAPExtensions(), true);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "initiateCallAttemptResponse", InitiateCallAttemptResponseImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        InitiateCallAttemptResponseImpl copy = reader.read("initiateCallAttemptResponse", InitiateCallAttemptResponseImpl.class);

        assertEquals(original.getSupportedCamelPhases().getPhase1Supported(), copy.getSupportedCamelPhases().getPhase1Supported());
        assertEquals(original.getSupportedCamelPhases().getPhase2Supported(), copy.getSupportedCamelPhases().getPhase2Supported());
        assertEquals(original.getSupportedCamelPhases().getPhase3Supported(), copy.getSupportedCamelPhases().getPhase3Supported());
        assertEquals(original.getSupportedCamelPhases().getPhase4Supported(), copy.getSupportedCamelPhases().getPhase4Supported());
        assertEquals(original.getOfferedCamel4Functionalities().getInitiateCallAttempt(), copy.getOfferedCamel4Functionalities().getInitiateCallAttempt());
        assertEquals(original.getOfferedCamel4Functionalities().getCollectInformation(), copy.getOfferedCamel4Functionalities().getCollectInformation());
        assertEquals(original.getReleaseCallArgExtensionAllowed(), copy.getReleaseCallArgExtensionAllowed());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(original.getExtensions()));
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(copy.getExtensions()));

    }

}
