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

package org.mobicents.protocols.ss7.map.service.lsm;

import static org.testng.Assert.*;import org.testng.*;import org.testng.annotations.*;

import java.util.Arrays;


import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MapParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MapParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientExternalID;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * //TODO add ExtensionContainer and test
 * 
 * @author amit bhayani
 * 
 */
public class LCSClientExternalIDTest {

	MapParameterFactory MapParameterFactory = new MapParameterFactoryImpl();

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
		byte[] data = new byte[] { (byte)0xb0, 0x07, (byte) 0x80, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70 };
		
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		
		LCSClientExternalIDImpl lcsClientExterId = new LCSClientExternalIDImpl();
		lcsClientExterId.decodeAll(asn);
		
		assertNotNull(lcsClientExterId.getExternalAddress());
		assertEquals( lcsClientExterId.getExternalAddress().getAddress(),"55619007");
		
	}

	@Test(groups = { "functional.encode","service.lsm"})
	public void testEncode() throws Exception {
		
		byte[] data = new byte[] { (byte)0xb0, 0x07, (byte) 0x80, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70 };

		ISDNAddressString externalAddress = MapParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "55619007");
		LCSClientExternalIDImpl lcsClientExterId = new LCSClientExternalIDImpl(externalAddress, null);
		AsnOutputStream asnOS = new AsnOutputStream();
		lcsClientExterId.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, Tag.SEQUENCE);
		
		byte[] encodedData = asnOS.toByteArray();

		assertTrue( Arrays.equals(data,encodedData));

	}
}
