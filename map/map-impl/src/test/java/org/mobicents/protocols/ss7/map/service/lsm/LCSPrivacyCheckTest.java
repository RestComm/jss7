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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MapServiceFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.mobicents.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;

/**
 * @author amit bhayani
 * 
 */
public class LCSPrivacyCheckTest {
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
		byte[] data = new byte[] { 0x30, 0x06, (byte) 0x80, 0x01, 0x00, (byte) 0x81, 0x01, 0x02 };

		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		LCSPrivacyCheck lcsPrivacyCheck = new LCSPrivacyCheckImpl();
		lcsPrivacyCheck.decodeAll(asn);

		assertEquals(PrivacyCheckRelatedAction.allowedWithoutNotification, lcsPrivacyCheck.getCallSessionUnrelated());
		assertEquals(PrivacyCheckRelatedAction.allowedIfNoResponse, lcsPrivacyCheck.getCallSessionRelated());

	}

	@Test
	public void testEncode() throws Exception {
		byte[] data = new byte[] { 0x30, 0x06, (byte) 0x80, 0x01, 0x00, (byte) 0x81, 0x01, 0x02 };

		PrivacyCheckRelatedAction callSessionUnrelated = PrivacyCheckRelatedAction.allowedWithoutNotification;
		PrivacyCheckRelatedAction callSessionRelated = PrivacyCheckRelatedAction.allowedIfNoResponse;

		LCSPrivacyCheck lcsPrivacyCheck = new LCSPrivacyCheckImpl(callSessionUnrelated, callSessionRelated);
		AsnOutputStream asnOS = new AsnOutputStream();
		lcsPrivacyCheck.encodeAll(asnOS, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);

		byte[] encodedData = asnOS.toByteArray();

		assertTrue(Arrays.equals(data, encodedData));
	}
}
