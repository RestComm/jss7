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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationNumberMapImpl;
import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class LocationNumberMapTest {
	
	private byte[] getData() {
		return new byte[] { (byte)130, 8, (byte)132, (byte)151, 8, 2, (byte)151, 1, 32, (byte)144 };
	}
	
	private byte[] getIntData() {
		return new byte[] { (byte)132, (byte)151, 8, 2, (byte)151, 1, 32, (byte)144 };
	}

	@Test(groups = { "functional.decode","primitives"})
	public void testDecode() throws Exception {
		
		byte[] rawData = getData();
		
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		LocationNumberMapImpl impl = new LocationNumberMapImpl();
		impl.decodeAll(asn);
		LocationNumber ln = impl.getLocationNumber();

		assertTrue(Arrays.equals(impl.getData(), this.getIntData()));
		assertEquals(ln.getNatureOfAddressIndicator(), 4);
		assertTrue(ln.getAddress().equals("80207910020"));
		assertEquals(ln.getNumberingPlanIndicator(), 1);
		assertEquals(ln.getInternalNetworkNumberIndicator(), 1);
		assertEquals(ln.getAddressRepresentationRestrictedIndicator(), 1);
		assertEquals(ln.getScreeningIndicator(), 3);
}
	
	@Test(groups = { "functional.encode","primitives"})
	public void testEncode() throws Exception {
		
		LocationNumberMapImpl impl = new LocationNumberMapImpl(this.getIntData());
		AsnOutputStream asnOS = new AsnOutputStream();
		impl.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 2);
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getData();		
		assertTrue( Arrays.equals(rawData,encodedData));
		
		// TODO: ISUP encoding failure 
		LocationNumberImpl ln = new LocationNumberImpl(4, "80207910020", 1, 1, 1, 3);
		impl = new LocationNumberMapImpl(ln);
		asnOS = new AsnOutputStream();
		impl.encodeAll(asnOS, Tag.CLASS_CONTEXT_SPECIFIC, 2);
		encodedData = asnOS.toByteArray();
		rawData = getData();		
//		assertTrue( Arrays.equals(rawData,encodedData));
		
		// int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator, int addressRepresentationREstrictedIndicator,
		// int screeningIndicator
	}

}
