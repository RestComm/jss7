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

package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;
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
public class CallingPartyCategoryTest {
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
		return new byte[] { 9 };
	}

	@Test(groups = { "functional.decode", "parameter" })
	public void testDecode() throws Exception {

		CallingPartyCategoryImpl prim = new CallingPartyCategoryImpl();
		prim.decode(getData());

		assertEquals(prim.getCallingPartyCategory(), CallingPartyCategory._OPERATOR_NATIONAL);
	}

	@Test(groups = { "functional.encode", "parameter" })
	public void testEncode() throws Exception {

		CallingPartyCategoryImpl prim = new CallingPartyCategoryImpl(CallingPartyCategory._OPERATOR_NATIONAL);

		byte[] data = getData();
		byte[] encodedData = prim.encode();

		assertTrue(Arrays.equals(data, encodedData));

	}

	@Test(groups = { "functional.xml.serialize", "parameter" })
	public void testXMLSerialize() throws Exception {

		CallingPartyCategoryImpl original = new CallingPartyCategoryImpl(CallingPartyCategory._OPERATOR_NATIONAL);

		// Writes the area to a file.
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
		// writer.setBinding(binding); // Optional.
		writer.setIndentation("\t"); // Optional (use tabulation for indentation).
		writer.write(original, "callingPartyCategory", CallingPartyCategoryImpl.class);
		writer.close();

		byte[] rawData = baos.toByteArray();
		String serializedEvent = new String(rawData);

		System.out.println(serializedEvent);

		ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
		XMLObjectReader reader = XMLObjectReader.newInstance(bais);
		CallingPartyCategoryImpl copy = reader.read("callingPartyCategory", CallingPartyCategoryImpl.class);

		assertEquals(copy.getCallingPartyCategory(), original.getCallingPartyCategory());
	}

}
