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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.primitives.AChChargingAddressImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.inap.primitives.LegIDImpl;
import org.testng.annotations.Test;

import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;

/**
 * @author alerant appngin
 */
public class AChChargingAddressTest {

	// context-specific, primitive
	private final int CS_P = 0x80;
	// context-specific, constructed
	private final int CS_C = 0xa0;

	private AChChargingAddressImpl getValue1(){
		return new AChChargingAddressImpl(new LegIDImpl(
				true, LegType.leg2));
	}
	private AChChargingAddressImpl getValue2(){
		return new AChChargingAddressImpl(1000);
	}
	
	private byte[] getData1() {
		return new byte[] {
				// outer tag + length
				(byte) (CS_C + 0), 5,
				// TAG legID, 3 bytes
				(byte) (CS_C + 2), 3,
				// sendingSideID [0], 1 byte, OCTET_STRING SIZE 1 "02"
				(byte) (CS_P + 0), 1, 2 };
	}

	private byte[] getData2() {
		return new byte[] {
				// outer tag + length
				(byte) (CS_C + 0), 5,
				// TAG srfConnection [50] long tag form, 2 bytes,			
				// CallSegmentID::Integer4 1000
				(byte) (CS_P + Tag.TAG_MASK), 50, 2, 3, (byte) 232 };
	}
	

	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {

		byte[] data = this.getData1();
		AsnInputStream ais = new AsnInputStream(data);
		AChChargingAddressImpl elem = new AChChargingAddressImpl();
		int tag = ais.readTag();
		elem.decodeAll(ais);
		
		assertEquals(elem, getValue1());
		assertNotEquals(elem, getValue2());
		
		data = this.getData2();
		ais = new AsnInputStream(data);
		elem = new AChChargingAddressImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		
		assertNotEquals(elem, getValue1());
		assertEquals(elem, getValue2());
		
	}

	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {

		AChChargingAddressImpl elem = new AChChargingAddressImpl(new LegIDImpl(
				true, LegType.leg2));
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

		elem = new AChChargingAddressImpl(1000);
		aos = new AsnOutputStream();
		elem.encodeAll(aos, Tag.CLASS_CONTEXT_SPECIFIC, 0);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));

	}

	@Test(groups = { "functional.xml.serialize", "primitives" })
	public void testXMLSerialization() throws Exception {
		AChChargingAddressImpl original = new AChChargingAddressImpl(
				new LegIDImpl(true, LegType.leg2));

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLObjectWriter writer = XMLObjectWriter.newInstance(baos)
				.setIndentation("\t");
		writer.write(original, "achChargingAddress",
				AChChargingAddressImpl.class);
		writer.close();

		byte[] rawData = baos.toByteArray();
		String serializedEvent = new String(rawData);

		System.out.println(serializedEvent);

		ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
		XMLObjectReader reader = XMLObjectReader.newInstance(bais);
		AChChargingAddressImpl copy = reader.read("achChargingAddress",
				AChChargingAddressImpl.class);

		assertEquals(copy, original);
	}
}
