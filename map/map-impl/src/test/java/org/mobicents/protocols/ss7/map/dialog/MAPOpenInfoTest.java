/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.map.dialog;

import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MapServiceFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;

/**
 * 
 * @author amit bhayani
 * @author sergey vetyutnev
 * 
 */
public class MAPOpenInfoTest extends TestCase {

	private byte[] getDataFull() {
		return new byte[] { -96, 61, -128, 9, -106, 2, 36, -128, 3, 0, -128, 0, -14, -127, 7, -111, 19, 38, -104, -122, 3, -16, 48, 39, -96, 32, 48, 10, 6, 3,
				42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33 };
	}
	
	@org.junit.Test
	public void testDecode() throws Exception {

		// The raw data is from packet 2 of nad1053.pcap
		byte[] data = new byte[] { (byte) 0xa0, (byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24, (byte) 0x80,
				0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26, (byte) 0x98,
				(byte) 0x86, 0x03, (byte) 0xf0, 0x00, 0x00 };

		AsnInputStream asnIs = new AsnInputStream(data);

		int tag = asnIs.readTag();
		assertEquals(0, tag);

		MAPOpenInfoImpl mapOpenInfoImpl = new MAPOpenInfoImpl();
		mapOpenInfoImpl.decodeAll(asnIs);

		AddressString destRef = mapOpenInfoImpl.getDestReference();
		AddressString origRef = mapOpenInfoImpl.getOrigReference();

		assertNotNull(destRef);

		assertEquals(AddressNature.international_number, destRef.getAddressNature());
		assertEquals(NumberingPlan.land_mobile, destRef.getNumberingPlan());
		assertEquals("204208300008002", destRef.getAddress());

		assertNotNull(origRef);

		assertEquals(AddressNature.international_number, origRef.getAddressNature());
		assertEquals(NumberingPlan.ISDN, origRef.getNumberingPlan());
		assertEquals("31628968300", origRef.getAddress());

		
		asnIs = new AsnInputStream(this.getDataFull());

		tag = asnIs.readTag();
		assertEquals(0, tag);

		mapOpenInfoImpl = new MAPOpenInfoImpl();
		mapOpenInfoImpl.decodeAll(asnIs);

		destRef = mapOpenInfoImpl.getDestReference();
		origRef = mapOpenInfoImpl.getOrigReference();

		assertNotNull(destRef);

		assertEquals(AddressNature.international_number, destRef.getAddressNature());
		assertEquals(NumberingPlan.land_mobile, destRef.getNumberingPlan());
		assertEquals("204208300008002", destRef.getAddress());

		assertNotNull(origRef);

		assertEquals(AddressNature.international_number, origRef.getAddressNature());
		assertEquals(NumberingPlan.ISDN, origRef.getNumberingPlan());
		assertEquals("31628968300", origRef.getAddress());

		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(mapOpenInfoImpl.getExtensionContainer()));
		
	}

	//TODO Fix this
	
//	@org.junit.Test
//	public void testDecode1() throws Exception {
//
//		// The raw data is from packet 2 of nad1053.pcap
//		byte[] data = new byte[] { (byte) 0xa1, 0x12, 0x02, 0x01, 0x00, 0x02, 0x01, 0x3b, 0x30, 0x0a, 0x04, 0x01, 0x0f,
//				0x04, 0x05, 0x2a, 0x59, 0x6c, 0x36, 0x02 };
//
//		ByteArrayInputStream baIs = new ByteArrayInputStream(data);
//		AsnInputStream asnIs = new AsnInputStream(baIs);
//
//		int tag = asnIs.readTag();
//
//		MAPOpenInfoImpl mapOpenInfoImpl = new MAPOpenInfoImpl();
//		mapOpenInfoImpl.decode(asnIs);
//
//		AddressString destRef = mapOpenInfoImpl.getDestReference();
//		AddressString origRef = mapOpenInfoImpl.getOrigReference();
//
//		assertNotNull(destRef);
//
//		assertEquals(AddressNature.international_number, destRef.getAddressNature());
//		assertEquals(NumberingPlan.land_mobile, destRef.getNumberingPlan());
//		assertEquals("204208300008002", destRef.getAddress());
//
//		assertNotNull(origRef);
//
//		assertEquals(AddressNature.international_number, origRef.getAddressNature());
//		assertEquals(NumberingPlan.ISDN, origRef.getNumberingPlan());
//		assertEquals("31628968300", origRef.getAddress());
//
//	}

	@org.junit.Test
	public void testEncode() throws Exception {

		MapServiceFactory servFact = new MapServiceFactoryImpl();

		MAPOpenInfoImpl mapOpenInfoImpl = new MAPOpenInfoImpl();
		AddressString destReference = servFact.createAddressString(AddressNature.international_number,
				NumberingPlan.land_mobile, "204208300008002");
		mapOpenInfoImpl.setDestReference(destReference);
		AddressString origReference = servFact.createAddressString(AddressNature.international_number,
				NumberingPlan.ISDN, "31628968300");
		mapOpenInfoImpl.setOrigReference(origReference);
		AsnOutputStream asnOS = new AsnOutputStream();

		mapOpenInfoImpl.encodeAll(asnOS);
		byte[] data = asnOS.toByteArray();
		// System.out.println(dump(data, data.length, false));
		assertTrue(Arrays.equals(new byte[] { (byte) 0xa0, (byte) 0x14, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24,
				(byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2, (byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26,
				(byte) 0x98, (byte) 0x86, 0x03, (byte) 0xf0 }, data));

		
		mapOpenInfoImpl = new MAPOpenInfoImpl();
		destReference = servFact.createAddressString(AddressNature.international_number, NumberingPlan.land_mobile, "204208300008002");
		mapOpenInfoImpl.setDestReference(destReference);
		origReference = servFact.createAddressString(AddressNature.international_number, NumberingPlan.ISDN, "31628968300");
		mapOpenInfoImpl.setOrigReference(origReference);
		mapOpenInfoImpl.setExtensionContainer(MAPExtensionContainerTest.GetTestExtensionContainer());

		asnOS = new AsnOutputStream();
		mapOpenInfoImpl.encodeAll(asnOS);
		data = asnOS.toByteArray();
		assertTrue(Arrays.equals(this.getDataFull(), data));
	}
}
