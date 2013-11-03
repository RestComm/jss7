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

import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.NAINumber;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CallingPartyNumberTest {
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
        return new byte[] { (byte) 0x83, (byte) 0xC2, 0x21, 0x43, 0x05 };
    }

    private byte[] getData2() {
        return new byte[] { (byte) 0x03, (byte) 0xC2, 0x21, 0x43, 0x65 };
    }

    private byte[] getData3() {
        return new byte[] { (byte) 0x00, 0x0B };
    }

    @Test(groups = { "functional.decode", "parameter" })
    public void testDecode() throws Exception {

        CallingPartyNumberImpl prim = new CallingPartyNumberImpl();
        prim.decode(getData());

        assertEquals(prim.getAddress(), "12345");
        assertEquals(prim.getNatureOfAddressIndicator(), NAINumber._NAI_NATIONAL_SN);
        assertEquals(prim.getNumberingPlanIndicator(), CallingPartyNumber._NPI_TELEX);
        assertEquals(prim.getNumberIncompleteIndicator(), CallingPartyNumber._NI_INCOMPLETE);
        assertEquals(prim.getAddressRepresentationRestrictedIndicator(), CallingPartyNumber._APRI_ALLOWED);
        assertEquals(prim.getScreeningIndicator(), CallingPartyNumber._SI_USER_PROVIDED_FAILED);
        assertTrue(prim.isOddFlag());

        prim = new CallingPartyNumberImpl();
        prim.decode(getData2());

        assertEquals(prim.getAddress(), "123456");
        assertEquals(prim.getNatureOfAddressIndicator(), NAINumber._NAI_NATIONAL_SN);
        assertEquals(prim.getNumberingPlanIndicator(), CallingPartyNumber._NPI_TELEX);
        assertEquals(prim.getNumberIncompleteIndicator(), CallingPartyNumber._NI_INCOMPLETE);
        assertEquals(prim.getAddressRepresentationRestrictedIndicator(), CallingPartyNumber._APRI_ALLOWED);
        assertEquals(prim.getScreeningIndicator(), CallingPartyNumber._SI_USER_PROVIDED_FAILED);
        assertFalse(prim.isOddFlag());

        prim = new CallingPartyNumberImpl();
        prim.decode(getData3());

        assertEquals(prim.getAddress(), "");
        assertEquals(prim.getNatureOfAddressIndicator(), 0);
        assertEquals(prim.getNumberingPlanIndicator(), 0);
        assertEquals(prim.getNumberIncompleteIndicator(), 0);
        assertEquals(prim.getAddressRepresentationRestrictedIndicator(), CallingPartyNumber._APRI_NOT_AVAILABLE);
        assertEquals(prim.getScreeningIndicator(), CallingPartyNumber._SI_NETWORK_PROVIDED);
        assertFalse(prim.isOddFlag());
    }

    @Test(groups = { "functional.encode", "parameter" })
    public void testEncode() throws Exception {

        CallingPartyNumberImpl prim = new CallingPartyNumberImpl(NAINumber._NAI_NATIONAL_SN, "12345",
                CallingPartyNumber._NPI_TELEX, CallingPartyNumber._NI_INCOMPLETE, CallingPartyNumber._APRI_ALLOWED,
                CallingPartyNumber._SI_USER_PROVIDED_FAILED);
        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int numberIncompleteIndicator,
        // int addressRepresentationREstrictedIndicator, int screeningIndicator

        byte[] data = getData();
        byte[] encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));

        prim = new CallingPartyNumberImpl(NAINumber._NAI_NATIONAL_SN, "123456", CallingPartyNumber._NPI_TELEX,
                CallingPartyNumber._NI_INCOMPLETE, CallingPartyNumber._APRI_ALLOWED,
                CallingPartyNumber._SI_USER_PROVIDED_FAILED);

        data = getData2();
        encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));

        prim = new CallingPartyNumberImpl(NAINumber._NAI_NATIONAL_SN, "123456", CallingPartyNumber._NPI_TELEX,
                CallingPartyNumber._NI_INCOMPLETE, CallingPartyNumber._APRI_NOT_AVAILABLE,
                CallingPartyNumber._SI_USER_PROVIDED_FAILED);

        data = getData3();
        encodedData = prim.encode();

        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.xml.serialize", "parameter" })
    public void testXMLSerialize() throws Exception {

        CallingPartyNumberImpl original = new CallingPartyNumberImpl(NAINumber._NAI_NATIONAL_SN, "12345",
                CallingPartyNumber._NPI_TELEX, CallingPartyNumber._NI_INCOMPLETE, CallingPartyNumber._APRI_ALLOWED,
                CallingPartyNumber._SI_USER_PROVIDED_FAILED);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "callingPartyNumber", CallingPartyNumberImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CallingPartyNumberImpl copy = reader.read("callingPartyNumber", CallingPartyNumberImpl.class);

        assertEquals(copy.getNatureOfAddressIndicator(), original.getNatureOfAddressIndicator());
        assertEquals(copy.getAddress(), original.getAddress());
        assertEquals(copy.getNumberingPlanIndicator(), original.getNumberingPlanIndicator());
        assertEquals(copy.getNumberIncompleteIndicator(), original.getNumberIncompleteIndicator());
        assertEquals(copy.getAddressRepresentationRestrictedIndicator(), original.getAddressRepresentationRestrictedIndicator());
        assertEquals(copy.getScreeningIndicator(), original.getScreeningIndicator());
    }

}
