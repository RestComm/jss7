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
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author sergey vetyutnev
 */
public class GenericNumberTest {

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
        return new byte[] { 5, (byte) 131, (byte) 194, 0x21, 0x43, 0x05 };
    }

    private byte[] getData2() {
        return new byte[] { 8, 0, 11 };
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecode() throws Exception {

        GenericNumberImpl prim = new GenericNumberImpl();
        prim.decode(getData());

        assertEquals(prim.getNatureOfAddressIndicator(), GenericNumber._NAI_NATIONAL_SN);
        assertEquals(prim.getAddress(), "12345");
        assertEquals(prim.getNumberQualifierIndicator(), GenericNumber._NQIA_CONNECTED_NUMBER);
        assertEquals(prim.getNumberingPlanIndicator(), GenericNumber._NPI_TELEX);
        assertEquals(prim.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_ALLOWED);
        assertEquals(prim.isNumberIncomplete(), GenericNumber._NI_INCOMPLETE);
        assertEquals(prim.getScreeningIndicator(), GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        assertTrue(prim.isOddFlag());

        prim = new GenericNumberImpl();
        prim.decode(getData2());

        assertEquals(prim.getNatureOfAddressIndicator(), 0);
        assertEquals(prim.getAddress(), "");
        assertEquals(prim.getNumberQualifierIndicator(), GenericNumber._NQIA_REDIRECTING_NUMBER);
        assertEquals(prim.getNumberingPlanIndicator(), 0);
        assertEquals(prim.getAddressRepresentationRestrictedIndicator(), GenericNumber._APRI_NOT_AVAILABLE);
        assertEquals(prim.isNumberIncomplete(), GenericNumber._NI_COMPLETE);
        assertEquals(prim.getScreeningIndicator(), GenericNumber._SI_NETWORK_PROVIDED);
        assertFalse(prim.isOddFlag());
    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncode() throws Exception {

        GenericNumberImpl prim = new GenericNumberImpl(GenericNumber._NAI_NATIONAL_SN, "12345",
                GenericNumber._NQIA_CONNECTED_NUMBER, GenericNumber._NPI_TELEX, GenericNumber._APRI_ALLOWED,
                GenericNumber._NI_INCOMPLETE, GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        // int natureOfAddresIndicator, String address, int numberQualifierIndicator, int numberingPlanIndicator, int
        // addressRepresentationREstrictedIndicator,
        // boolean numberIncomplete, int screeningIndicator

        byte[] data = getData();
        byte[] encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));

        prim = new GenericNumberImpl(0, "", GenericNumber._NQIA_REDIRECTING_NUMBER, 0, GenericNumber._APRI_NOT_AVAILABLE,
                GenericNumber._NI_COMPLETE, GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        // int natureOfAddresIndicator, String address, int numberQualifierIndicator, int numberingPlanIndicator, int
        // addressRepresentationREstrictedIndicator,
        // boolean numberIncomplete, int screeningIndicator

        data = getData2();
        encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.xml.serialize", "parameter" })
    public void testXMLSerialize() throws Exception {

        GenericNumberImpl original = new GenericNumberImpl(GenericNumber._NAI_NATIONAL_SN, "12345",
                GenericNumber._NQIA_CONNECTED_NUMBER, GenericNumber._NPI_TELEX, GenericNumber._APRI_ALLOWED,
                GenericNumber._NI_INCOMPLETE, GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "genericNumber", GenericNumberImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        GenericNumberImpl copy = reader.read("genericNumber", GenericNumberImpl.class);

        assertEquals(copy.getNatureOfAddressIndicator(), original.getNatureOfAddressIndicator());
        assertEquals(copy.getAddress(), original.getAddress());
        assertEquals(copy.getNumberQualifierIndicator(), original.getNumberQualifierIndicator());
        assertEquals(copy.getNumberingPlanIndicator(), original.getNumberingPlanIndicator());
        assertEquals(copy.isNumberIncomplete(), original.isNumberIncomplete());
        assertEquals(copy.getAddressRepresentationRestrictedIndicator(), original.getAddressRepresentationRestrictedIndicator());
        assertEquals(copy.getScreeningIndicator(), original.getScreeningIndicator());
        assertEquals(copy.isOddFlag(), original.isOddFlag());
    }

    // /**
    // * @throws IOException
    // */
    // public GenericNumberTest() throws IOException {
    // super.badBodies.add(new byte[1]);
    //
    // // super.goodBodies.add(getBody(GenericNumberImpl._NQIA_CONNECTED_NUMBER, false, GenericNumber._NAI_NATIONAL_SN,
    // GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN,
    // // GenericNumberImpl._APRI_NOT_AVAILABLE, GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED, getSixDigits()));
    // super.goodBodies.add(getBody(GenericNumberImpl._NQIA_CONNECTED_NUMBER, false, GenericNumber._NAI_NATIONAL_SN,
    // GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN,
    // GenericNumberImpl._APRI_ALLOWED, GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED, getSixDigits()));
    //
    // }
    //
    // private byte[] getBody(int _NQI, boolean isODD, int _NAI, boolean _NI, int _NPI, int _APR, int _SI, byte[] digits) throws
    // IOException {
    // ByteArrayOutputStream bos = new ByteArrayOutputStream();
    // // we will use odd number of digits, so we leave zero as MSB
    //
    // int nai = _NAI;
    // if (isODD)
    // nai |= 0x01 << 7;
    // int bit3 = _SI;
    // bit3 |= _APR << 2;
    // bit3 |= _NPI << 4;
    // if (_NI)
    // bit3 |= 0x01 << 7;
    //
    // bos.write(_NQI);
    // bos.write(nai);
    // bos.write(bit3);
    // bos.write(digits);
    // return bos.toByteArray();
    // }
    //
    // @Test(groups = { "functional.encode","functional.decode","parameter"})
    // public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException,
    // IllegalAccessException, InvocationTargetException, IOException, ParameterException {
    // // GenericNumberImpl bci = new GenericNumberImpl(getBody(GenericNumberImpl._NQIA_CONNECTED_NUMBER, false,
    // GenericNumber._NAI_NATIONAL_SN, GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN,
    // // GenericNumberImpl._APRI_NOT_AVAILABLE, GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED, getSixDigits()));
    // GenericNumberImpl bci = new GenericNumberImpl(getBody(GenericNumberImpl._NQIA_CONNECTED_NUMBER, false,
    // GenericNumber._NAI_NATIONAL_SN, GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN,
    // GenericNumberImpl._APRI_ALLOWED, GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED, getSixDigits()));
    //
    // String[] methodNames = { "getNumberQualifierIndicator", "getNatureOfAddressIndicator", "isNumberIncomplete",
    // "getNumberingPlanIndicator", "getAddressRepresentationRestrictedIndicator",
    // "getScreeningIndicator", "getAddress" };
    // // Object[] expectedValues = { GenericNumberImpl._NQIA_CONNECTED_NUMBER, GenericNumber._NAI_NATIONAL_SN,
    // GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN, GenericNumberImpl._APRI_NOT_AVAILABLE,
    // // GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED, getSixDigitsString() };
    // Object[] expectedValues = { GenericNumberImpl._NQIA_CONNECTED_NUMBER, GenericNumber._NAI_NATIONAL_SN,
    // GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN, GenericNumberImpl._APRI_ALLOWED,
    // GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED, getSixDigitsString() };
    // super.testValues(bci, methodNames, expectedValues);
    // }
    //
    // /*
    // * (non-Javadoc)
    // *
    // * @see
    // * org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent
    // * ()
    // */
    //
    // public AbstractISUPParameter getTestedComponent() {
    // // return new GenericNumberImpl(0, "1", 1, 1, 1, false, 1);
    // return new GenericNumberImpl(GenericNumber._NAI_NATIONAL_SN, getSixDigitsString(),
    // GenericNumberImpl._NQIA_CONNECTED_NUMBER,
    // GenericNumberImpl._NPI_ISDN, GenericNumberImpl._APRI_ALLOWED, false,
    // GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED);
    // // int natureOfAddresIndicator, String address, int numberQualifierIndicator, int numberingPlanIndicator, int
    // addressRepresentationREstrictedIndicator,
    // // boolean numberIncomplete, int screeningIndicator
    // }

}
