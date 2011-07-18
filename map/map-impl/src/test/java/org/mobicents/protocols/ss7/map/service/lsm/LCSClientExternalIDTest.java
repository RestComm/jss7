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
package org.mobicents.protocols.ss7.map.service.lsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.map.MapServiceFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
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

	MapServiceFactory mapServiceFactory = new MapServiceFactoryImpl();

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testDecode() throws Exception {
		byte[] data = new byte[] { (byte) 0x80, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70 };
		
		Parameter p = TcapFactory.createParameter();
		p.setPrimitive(false);
		p.setData(data);
		
		LCSClientExternalID lcsClientExterId = new LCSClientExternalIDImpl();
		lcsClientExterId.decode(p);
		
		assertNotNull(lcsClientExterId.getExternalAddress());
		assertEquals("55619007", lcsClientExterId.getExternalAddress().getAddress());
		
	}

	@Test
	public void testEncode() throws Exception {
		
		byte[] data = new byte[] { (byte) 0x80, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70 };

		ISDNAddressString externalAddress = mapServiceFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "55619007");
		LCSClientExternalID lcsClientExterId = new LCSClientExternalIDImpl(externalAddress, null);
		Parameter param = lcsClientExterId.encode();
		assertNotNull(param);
		assertTrue(param.isPrimitive());

		
		assertTrue(Arrays.equals(data, param.getData()));

	}
}
