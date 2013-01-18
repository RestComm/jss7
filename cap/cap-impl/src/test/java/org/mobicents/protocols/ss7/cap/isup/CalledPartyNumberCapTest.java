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

package org.mobicents.protocols.ss7.cap.isup;

import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingCategory;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.USSDStringImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSRequestImpl;
import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class CalledPartyNumberCapTest {

	public byte[] getData() {
		return new byte[] { (byte) 130, 7, 3, (byte) 144, 33, 114, 16, (byte) 144, 0 };
	}

	public byte[] getIntData() {
		return new byte[] { 3, (byte) 144, 33, 114, 16, (byte) 144, 0 };
	}

	@Test(groups = { "functional.decode","isup"})
	public void testDecode() throws Exception {

		byte[] data = this.getData();
		AsnInputStream ais = new AsnInputStream(data);
		CalledPartyNumberCapImpl elem = new CalledPartyNumberCapImpl();
		int tag = ais.readTag();
		elem.decodeAll(ais);
		CalledPartyNumber cpn = elem.getCalledPartyNumber();
		assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
		assertFalse(cpn.isOddFlag());
		assertEquals(cpn.getNumberingPlanIndicator(), 1);
		assertEquals(cpn.getInternalNetworkNumberIndicator(), 1);
		assertEquals(cpn.getNatureOfAddressIndicator(), 3);
		assertTrue(cpn.getAddress().equals("1227010900"));
	}

	@Test(groups = { "functional.encode","isup"})
	public void testEncode() throws Exception {

		CalledPartyNumberCapImpl elem = new CalledPartyNumberCapImpl(this.getIntData());
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 2);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));

		CalledPartyNumber cpn = new CalledPartyNumberImpl(3, "1227010900", 1, 1);
		elem = new CalledPartyNumberCapImpl(cpn);
		aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 2);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData()));
		
		// int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator
	}

	@Test(groups = { "functional.xml.serialize", "service.ussd" })
	public void testXMLSerialize() throws Exception {

		CalledPartyNumberCapImpl original = new CalledPartyNumberCapImpl(new CalledPartyNumberImpl(CalledPartyNumber._NAI_INTERNATIONAL_NUMBER, "664422",
				CalledPartyNumber._NPI_ISDN, CalledPartyNumber._NAI_NRNINNF));

		// Writes the area to a file.
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
		// writer.setBinding(binding); // Optional.
		writer.setIndentation("\t"); // Optional (use tabulation for indentation).
		writer.write(original, "calledPartyNumberCap", CalledPartyNumberCapImpl.class);
		writer.close();

		byte[] rawData = baos.toByteArray();
		String serializedEvent = new String(rawData);

		System.out.println(serializedEvent);

		ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
		XMLObjectReader reader = XMLObjectReader.newInstance(bais);
		CalledPartyNumberCapImpl copy = reader.read("calledPartyNumberCap", CalledPartyNumberCapImpl.class);

		assertEquals(copy.getCalledPartyNumber().getNatureOfAddressIndicator(), original.getCalledPartyNumber().getNatureOfAddressIndicator());
		assertEquals(copy.getCalledPartyNumber().getAddress(), original.getCalledPartyNumber().getAddress());
		assertEquals(copy.getCalledPartyNumber().getNumberingPlanIndicator(), original.getCalledPartyNumber().getNumberingPlanIndicator());
		assertEquals(copy.getCalledPartyNumber().getInternalNetworkNumberIndicator(), original.getCalledPartyNumber().getInternalNetworkNumberIndicator());

	}
}

