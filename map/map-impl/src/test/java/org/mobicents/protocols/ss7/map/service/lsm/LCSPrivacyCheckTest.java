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
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.mobicents.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;

/**
 * @author amit bhayani
 * 
 */
public class LCSPrivacyCheckTest {
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
		byte[] data = new byte[] { 0x30, 0x06, (byte) 0x80, 0x01, 0x00, (byte) 0x81, 0x01, 0x02 };

		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		LCSPrivacyCheckImpl lcsPrivacyCheck = new LCSPrivacyCheckImpl();
		lcsPrivacyCheck.decodeAll(asn);

		assertEquals( lcsPrivacyCheck.getCallSessionUnrelated(),PrivacyCheckRelatedAction.allowedWithoutNotification);
		assertEquals( lcsPrivacyCheck.getCallSessionRelated(),PrivacyCheckRelatedAction.allowedIfNoResponse);

	}

	@Test(groups = { "functional.encode","service.lsm"})
	public void testEncode() throws Exception {
		byte[] data = new byte[] { 0x30, 0x06, (byte) 0x80, 0x01, 0x00, (byte) 0x81, 0x01, 0x02 };

		PrivacyCheckRelatedAction callSessionUnrelated = PrivacyCheckRelatedAction.allowedWithoutNotification;
		PrivacyCheckRelatedAction callSessionRelated = PrivacyCheckRelatedAction.allowedIfNoResponse;

		LCSPrivacyCheckImpl lcsPrivacyCheck = new LCSPrivacyCheckImpl(callSessionUnrelated, callSessionRelated);
		AsnOutputStream asnOS = new AsnOutputStream();
		lcsPrivacyCheck.encodeAll(asnOS, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);

		byte[] encodedData = asnOS.toByteArray();

		assertTrue( Arrays.equals(data,encodedData));
	}
}
