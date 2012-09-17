/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
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
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingCategory;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
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
		byte dataCodingScheme = addNum.getDataCodingScheme();
		assertEquals(dataCodingScheme, (byte) 0x0f);

		USSDString ussdString = addNum.getUSSDString();
		assertNotNull(ussdString);

		assertEquals(ussdString.getString(), "*234#");

	}

	@Test(groups = { "functional.encode", "service.ussd" })
	public void testEncode() throws Exception {
		byte[] data = new byte[] { 0x30, 0x0a, 0x04, 0x01, 0x0f, 0x04, 0x05, 0x2a, (byte) 0xd9, (byte) 0x8c, 0x36, 0x02 };

		USSDString ussdStr = new USSDStringImpl("*234#", null);
		ProcessUnstructuredSSRequestImpl addNum = new ProcessUnstructuredSSRequestImpl((byte) 0x0f,
				ussdStr, null, null);

		AsnOutputStream asnOS = new AsnOutputStream();
		addNum.encodeAll(asnOS);

		byte[] encodedData = asnOS.toByteArray();

		assertTrue(Arrays.equals(data, encodedData));
	}

	@Test(groups = { "functional.xml.serialize", "service.ussd" })
	public void testXMLSerialize() throws Exception {

		ISDNAddressStringImpl isdnAddress = new ISDNAddressStringImpl(AddressNature.international_number,
				NumberingPlan.ISDN, "79273605819");
		AlertingPatternImpl alertingPattern = new AlertingPatternImpl(AlertingCategory.Category3);
		USSDString ussdStr = new USSDStringImpl("*234#", null);
		ProcessUnstructuredSSRequestImpl original = new ProcessUnstructuredSSRequestImpl(
				(byte) 0x0f, ussdStr, alertingPattern, isdnAddress);

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
		assertEquals(copy.getDataCodingScheme(), original.getDataCodingScheme());
		assertEquals(copy.getUSSDString(), original.getUSSDString());
		assertEquals(copy.getAlertingPattern(), original.getAlertingPattern());

	}
}
