/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.primitives;

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
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * 
 */
public class CellGlobalIdOrServiceAreaIdOrLAITest {
	MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

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

	@Test(groups = { "functional.decode","service.lsm"})
	public void testDecode() throws Exception {
		byte[] data = new byte[] { (byte) 0x80, 0x07, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b };

		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();


		CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl();
		cellGlobalIdOrServiceAreaIdOrLAI.decodeAll(asn);

		assertNotNull(cellGlobalIdOrServiceAreaIdOrLAI.getCellGlobalIdOrServiceAreaIdFixedLength());
		assertTrue(Arrays.equals(new byte[] { 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b }, cellGlobalIdOrServiceAreaIdOrLAI
				.getCellGlobalIdOrServiceAreaIdFixedLength().getData()));

	}

	@Test(groups = { "functional.encode","service.lsm"})
	public void testEncode() throws Exception {

		byte[] data = new byte[] { (byte) 0x80, 0x07, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b };

		CellGlobalIdOrServiceAreaIdFixedLength par = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(new byte[] { 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b });
		CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(par);
		AsnOutputStream asnOS = new AsnOutputStream();
		cellGlobalIdOrServiceAreaIdOrLAI.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();

		assertTrue( Arrays.equals(data,encodedData));

	}

	@Test(groups = { "functional.xml.serialize", "service.lsm" })
	public void testSerialization() throws Exception {
		CellGlobalIdOrServiceAreaIdFixedLength par = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(250, 1, 4444, 3333);
		CellGlobalIdOrServiceAreaIdOrLAIImpl original = new CellGlobalIdOrServiceAreaIdOrLAIImpl(par);

		// Writes the area to a file.
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
		// writer.setBinding(binding); // Optional.
		writer.setIndentation("\t"); // Optional (use tabulation for indentation).
		writer.write(original, "cellGlobalIdOrServiceAreaIdOrLAI", CellGlobalIdOrServiceAreaIdOrLAIImpl.class);
		writer.close();

		byte[] rawData = baos.toByteArray();
		String serializedEvent = new String(rawData);

		System.out.println(serializedEvent);

		ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
		XMLObjectReader reader = XMLObjectReader.newInstance(bais);
		CellGlobalIdOrServiceAreaIdOrLAIImpl copy = reader.read("cellGlobalIdOrServiceAreaIdOrLAI", CellGlobalIdOrServiceAreaIdOrLAIImpl.class);

		assertEquals(copy.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), original.getCellGlobalIdOrServiceAreaIdFixedLength().getMCC());
		assertEquals(copy.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), original.getCellGlobalIdOrServiceAreaIdFixedLength().getMNC());
		assertEquals(copy.getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), original.getCellGlobalIdOrServiceAreaIdFixedLength().getLac());
		assertEquals(copy.getCellGlobalIdOrServiceAreaIdFixedLength().getCellId(), original.getCellGlobalIdOrServiceAreaIdFixedLength().getCellId());


		LAIFixedLengthImpl par2 = new LAIFixedLengthImpl(250, 1, 4444);
		original = new CellGlobalIdOrServiceAreaIdOrLAIImpl(par2);

		// Writes the area to a file.
		baos = new ByteArrayOutputStream();
		writer = XMLObjectWriter.newInstance(baos);
		// writer.setBinding(binding); // Optional.
		writer.setIndentation("\t"); // Optional (use tabulation for indentation).
		writer.write(original, "cellGlobalIdOrServiceAreaIdOrLAI", CellGlobalIdOrServiceAreaIdOrLAIImpl.class);
		writer.close();

		rawData = baos.toByteArray();
		serializedEvent = new String(rawData);

		System.out.println(serializedEvent);

		bais = new ByteArrayInputStream(rawData);
		reader = XMLObjectReader.newInstance(bais);
		copy = reader.read("cellGlobalIdOrServiceAreaIdOrLAI", CellGlobalIdOrServiceAreaIdOrLAIImpl.class);

		assertEquals(copy.getLAIFixedLength().getMCC(), original.getLAIFixedLength().getMCC());
		assertEquals(copy.getLAIFixedLength().getMNC(), original.getLAIFixedLength().getMNC());
		assertEquals(copy.getLAIFixedLength().getLac(), original.getLAIFixedLength().getLac());
	}
}
