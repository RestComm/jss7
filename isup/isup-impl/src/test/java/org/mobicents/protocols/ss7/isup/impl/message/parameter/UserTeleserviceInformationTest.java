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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Start time:11:36:27 2009-04-27<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author sergey vetyutnev
 */
public class UserTeleserviceInformationTest extends ParameterHarness {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    private byte[] getData() {
        return new byte[] { (byte) 209, (byte) 179 };
    }

    private byte[] getData2() {
        return new byte[] { (byte) 145, 94, (byte) 181 };
    }

    private byte[] getData3() {
        return new byte[] { (byte) 145, 98, (byte) 129 };
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecode() throws Exception {

        UserTeleserviceInformationImpl prim = new UserTeleserviceInformationImpl();
        prim.decode(getData());

        assertEquals(prim.getCodingStandard(), UserTeleserviceInformation._CODING_STANDARD_NATIONAL);
        assertEquals(prim.getInterpretation(), UserTeleserviceInformation._INTERPRETATION_FHGCI);
        assertEquals(prim.getPresentationMethod(), UserTeleserviceInformation._PRESENTATION_METHOD_HLPP);
        assertEquals(prim.getHighLayerCharIdentification(), UserTeleserviceInformation._HLCI_IVTI);
        assertFalse(prim.isEHighLayerCharIdentificationPresent());
        assertFalse(prim.isEVideoTelephonyCharIdentificationPresent());

        prim = new UserTeleserviceInformationImpl();
        prim.decode(getData2());

        assertEquals(prim.getCodingStandard(), UserTeleserviceInformation._CODING_STANDARD_ITU_T);
        assertEquals(prim.getInterpretation(), UserTeleserviceInformation._INTERPRETATION_FHGCI);
        assertEquals(prim.getPresentationMethod(), UserTeleserviceInformation._PRESENTATION_METHOD_HLPP);
        assertEquals(prim.getHighLayerCharIdentification(), UserTeleserviceInformation._HLCI_MAINTAINENCE);
        assertTrue(prim.isEHighLayerCharIdentificationPresent());
        assertFalse(prim.isEVideoTelephonyCharIdentificationPresent());
        assertEquals(prim.getEHighLayerCharIdentification(), 53);

        prim = new UserTeleserviceInformationImpl();
        prim.decode(getData3());

        assertEquals(prim.getCodingStandard(), UserTeleserviceInformation._CODING_STANDARD_ITU_T);
        assertEquals(prim.getInterpretation(), UserTeleserviceInformation._INTERPRETATION_FHGCI);
        assertEquals(prim.getPresentationMethod(), UserTeleserviceInformation._PRESENTATION_METHOD_HLPP);
        assertEquals(prim.getHighLayerCharIdentification(), UserTeleserviceInformation._HLCI_AUDIOGRAPHIC_CONF);
        assertFalse(prim.isEHighLayerCharIdentificationPresent());
        assertTrue(prim.isEVideoTelephonyCharIdentificationPresent());
        assertEquals(prim.getEVideoTelephonyCharIdentification(), UserTeleserviceInformation._EACI_CSIC_H221);
    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncode() throws Exception {

        UserTeleserviceInformationImpl prim = new UserTeleserviceInformationImpl(
                UserTeleserviceInformation._CODING_STANDARD_NATIONAL, UserTeleserviceInformation._INTERPRETATION_FHGCI,
                UserTeleserviceInformation._PRESENTATION_METHOD_HLPP, UserTeleserviceInformation._HLCI_IVTI);
        // int codingStandard, int interpretation, int presentationMethod, int highLayerCharIdentification

        byte[] data = getData();
        byte[] encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));

        prim = new UserTeleserviceInformationImpl(UserTeleserviceInformation._CODING_STANDARD_ITU_T,
                UserTeleserviceInformation._INTERPRETATION_FHGCI, UserTeleserviceInformation._PRESENTATION_METHOD_HLPP,
                UserTeleserviceInformation._HLCI_MAINTAINENCE, 53);

        data = getData2();
        encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));

        prim = new UserTeleserviceInformationImpl(UserTeleserviceInformation._CODING_STANDARD_ITU_T,
                UserTeleserviceInformation._INTERPRETATION_FHGCI, UserTeleserviceInformation._PRESENTATION_METHOD_HLPP,
                UserTeleserviceInformation._HLCI_AUDIOGRAPHIC_CONF, UserTeleserviceInformation._EACI_CSIC_H221, true);

        data = getData3();
        encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));

    }

    @Test(groups = { "functional.xml.serialize", "parameter" })
    public void testXMLSerialize() throws Exception {

        UserTeleserviceInformationImpl original = new UserTeleserviceInformationImpl(
                UserTeleserviceInformation._CODING_STANDARD_ITU_T, UserTeleserviceInformation._INTERPRETATION_FHGCI,
                UserTeleserviceInformation._PRESENTATION_METHOD_HLPP, UserTeleserviceInformation._HLCI_MAINTAINENCE, 53);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "userTeleserviceInformation", UserTeleserviceInformationImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        UserTeleserviceInformationImpl copy = reader.read("userTeleserviceInformation", UserTeleserviceInformationImpl.class);

        assertEquals(copy.getCodingStandard(), original.getCodingStandard());
        assertEquals(copy.getInterpretation(), original.getInterpretation());
        assertEquals(copy.getPresentationMethod(), original.getPresentationMethod());
        assertEquals(copy.getHighLayerCharIdentification(), original.getHighLayerCharIdentification());
        assertEquals(copy.isEHighLayerCharIdentificationPresent(), original.isEHighLayerCharIdentificationPresent());
        assertEquals(copy.isEVideoTelephonyCharIdentificationPresent(), original.isEVideoTelephonyCharIdentificationPresent());
        assertEquals(copy.getEHighLayerCharIdentification(), original.getEHighLayerCharIdentification());
        assertEquals(copy.getEVideoTelephonyCharIdentification(), original.getEVideoTelephonyCharIdentification());

        original = new UserTeleserviceInformationImpl(UserTeleserviceInformation._CODING_STANDARD_ITU_T,
                UserTeleserviceInformation._INTERPRETATION_FHGCI, UserTeleserviceInformation._PRESENTATION_METHOD_HLPP,
                UserTeleserviceInformation._HLCI_AUDIOGRAPHIC_CONF, UserTeleserviceInformation._EACI_CSIC_H221, true);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "userTeleserviceInformation", UserTeleserviceInformationImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("userTeleserviceInformation", UserTeleserviceInformationImpl.class);

        assertEquals(copy.getCodingStandard(), original.getCodingStandard());
        assertEquals(copy.getInterpretation(), original.getInterpretation());
        assertEquals(copy.getPresentationMethod(), original.getPresentationMethod());
        assertEquals(copy.getHighLayerCharIdentification(), original.getHighLayerCharIdentification());
        assertEquals(copy.isEHighLayerCharIdentificationPresent(), original.isEHighLayerCharIdentificationPresent());
        assertEquals(copy.isEVideoTelephonyCharIdentificationPresent(), original.isEVideoTelephonyCharIdentificationPresent());
        assertEquals(copy.getEHighLayerCharIdentification(), original.getEHighLayerCharIdentification());
        assertEquals(copy.getEVideoTelephonyCharIdentification(), original.getEVideoTelephonyCharIdentification());
    }

    // before is old style tests
    public UserTeleserviceInformationTest() {
        super();
        super.goodBodies.add(getBody(UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP,
                UserTeleserviceInformationImpl._INTERPRETATION_FHGCI, UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
                UserTeleserviceInformationImpl._HLCI_IVTI));
        super.goodBodies.add(getBody(UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP,
                UserTeleserviceInformationImpl._INTERPRETATION_FHGCI, UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
                UserTeleserviceInformationImpl._HLCI_AUDIO_VID_LOW_RANGE));
        super.goodBodies
                .add(getBody(UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP,
                        UserTeleserviceInformationImpl._INTERPRETATION_FHGCI,
                        UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
                        UserTeleserviceInformationImpl._HLCI_AUDIO_VID_LOW_RANGE,
                        UserTeleserviceInformationImpl._EACI_CSIC_AA_3_1_CALL));
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        UserTeleserviceInformationImpl bci = new UserTeleserviceInformationImpl(getBody(
                UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP, UserTeleserviceInformationImpl._INTERPRETATION_FHGCI,
                UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC, UserTeleserviceInformationImpl._HLCI_IVTI));

        String[] methodNames = { "getPresentationMethod", "getInterpretation", "getCodingStandard",
                "getHighLayerCharIdentification" };
        Object[] expectedValues = { UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP,
                UserTeleserviceInformationImpl._INTERPRETATION_FHGCI, UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
                UserTeleserviceInformationImpl._HLCI_IVTI };
        super.testValues(bci, methodNames, expectedValues);
    }

    @Test(groups = { "functional.encode", "functional.decode", "parameter" })
    public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, IOException, ParameterException {
        UserTeleserviceInformationImpl bci = new UserTeleserviceInformationImpl(
                getBody(UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP,
                        UserTeleserviceInformationImpl._INTERPRETATION_FHGCI,
                        UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
                        UserTeleserviceInformationImpl._HLCI_AUDIO_VID_LOW_RANGE,
                        UserTeleserviceInformationImpl._EACI_CSIC_AA_3_1_CALL));

        String[] methodNames = { "getPresentationMethod", "getInterpretation", "getCodingStandard",
                "getHighLayerCharIdentification", "getEVideoTelephonyCharIdentification" };
        Object[] expectedValues = { UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP,
                UserTeleserviceInformationImpl._INTERPRETATION_FHGCI, UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
                UserTeleserviceInformationImpl._HLCI_AUDIO_VID_LOW_RANGE, UserTeleserviceInformationImpl._EACI_CSIC_AA_3_1_CALL };
        super.testValues(bci, methodNames, expectedValues);
    }

    private byte[] getBody(int _PRESENTATION_METHOD, int _INTERPRETATION, int _CODING_STANDARD, int _HLCI) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(0x80 | (_CODING_STANDARD << 5) | (_INTERPRETATION << 2) | _PRESENTATION_METHOD);
        bos.write(0x80 | _HLCI);
        return bos.toByteArray();
    }

    private byte[] getBody(int _PRESENTATION_METHOD, int _INTERPRETATION, int _CODING_STANDARD, int _HLCI, int _EACI) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(0x80 | (_CODING_STANDARD << 5) | (_INTERPRETATION << 2) | _PRESENTATION_METHOD);
        bos.write(_HLCI);
        bos.write(0x80 | _EACI);
        return bos.toByteArray();
    }

    public AbstractISUPParameter getTestedComponent() {
        return new UserTeleserviceInformationImpl(1, 1, 1, 1);
    }

}
