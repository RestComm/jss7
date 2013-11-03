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

package org.mobicents.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingCategory;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.USSDStringImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Real trace.
 *
 * TODO get trace with optional parameters and test
 *
 * @author amit bhayani
 *
 */
public class ProcessUnstructuredSSRequestTest {
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

    @Test(groups = { "functional.decode", "service.ussd" })
    public void testDecode() throws Exception {
        byte[] data = new byte[] { 0x30, 0x0a, 0x04, 0x01, 0x0f, 0x04, 0x05, 0x2a, (byte) 0xd9, (byte) 0x8c, 0x36, 0x02 };

        AsnInputStream asn = new AsnInputStream(data);
        int tag = asn.readTag();

        ProcessUnstructuredSSRequestImpl addNum = new ProcessUnstructuredSSRequestImpl();
        addNum.decodeAll(asn);
        CBSDataCodingScheme dataCodingScheme = addNum.getDataCodingScheme();
        assertEquals(dataCodingScheme.getCode(), 0x0f);

        USSDString ussdString = addNum.getUSSDString();
        assertNotNull(ussdString);

        assertTrue(ussdString.getString(null).equals("*234#"));

    }

    @Test(groups = { "functional.encode", "service.ussd" })
    public void testEncode() throws Exception {
        byte[] data = new byte[] { 0x30, 0x0a, 0x04, 0x01, 0x0f, 0x04, 0x05, 0x2a, (byte) 0xd9, (byte) 0x8c, 0x36, 0x02 };

        USSDString ussdStr = new USSDStringImpl("*234#", null, null);
        ProcessUnstructuredSSRequestImpl addNum = new ProcessUnstructuredSSRequestImpl(new CBSDataCodingSchemeImpl(0x0f),
                ussdStr, null, null);

        AsnOutputStream asnOS = new AsnOutputStream();
        addNum.encodeAll(asnOS);

        byte[] encodedData = asnOS.toByteArray();

        assertTrue(Arrays.equals(data, encodedData));
    }

    @Test(groups = { "functional.xml.serialize", "service.ussd" })
    public void testXMLSerialize() throws Exception {

        ISDNAddressStringImpl isdnAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "79273605819");
        AlertingPatternImpl alertingPattern = new AlertingPatternImpl(AlertingCategory.Category3);
        USSDString ussdStr = new USSDStringImpl("*234#", null, null);
        ProcessUnstructuredSSRequestImpl original = new ProcessUnstructuredSSRequestImpl(new CBSDataCodingSchemeImpl(0x0f),
                ussdStr, alertingPattern, isdnAddress);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "processUnstructuredSSRequest", ProcessUnstructuredSSRequestImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ProcessUnstructuredSSRequestImpl copy = reader.read("processUnstructuredSSRequest",
                ProcessUnstructuredSSRequestImpl.class);

        assertEquals(copy.getMSISDNAddressString(), original.getMSISDNAddressString());
        assertEquals(copy.getDataCodingScheme().getCode(), original.getDataCodingScheme().getCode());
        assertEquals(copy.getUSSDString(), original.getUSSDString());
        assertEquals(copy.getAlertingPattern(), original.getAlertingPattern());

    }
}
