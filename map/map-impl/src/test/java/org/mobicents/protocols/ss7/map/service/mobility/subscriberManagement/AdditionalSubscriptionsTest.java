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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.testng.annotations.Test;


/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class AdditionalSubscriptionsTest {

	private byte[] getEncodedData() {
		return new byte[] { 3, 2, 5, -96 };
	}

	@Test(groups = { "functional.decode","service.lsm"})
	public void testDecode() throws Exception {

		byte[] rawData = getEncodedData();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		AdditionalSubscriptionsImpl imp = new AdditionalSubscriptionsImpl();
		imp.decodeAll(asn);

		assertEquals(tag, Tag.STRING_BIT);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
		
		assertTrue(imp.getPrivilegedUplinkRequest());
		assertFalse(imp.getEmergencyUplinkRequest());
		assertTrue(imp.getEmergencyReset());
	}

	@Test(groups = { "functional.encode","service.lsm"})
	public void testEncode() throws Exception {
		AdditionalSubscriptionsImpl imp = new AdditionalSubscriptionsImpl(true, false, true);
		AsnOutputStream asnOS = new AsnOutputStream();
		imp.encodeAll(asnOS);
		assertTrue( Arrays.equals( getEncodedData(),asnOS.toByteArray()));
	}

}
