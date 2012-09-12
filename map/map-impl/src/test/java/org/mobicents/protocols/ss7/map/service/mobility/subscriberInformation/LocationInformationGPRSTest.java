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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class LocationInformationGPRSTest {

	private byte[] getEncodedData() {
		return new byte[] { 16, 57, -96, 7, -127, 5, 82, -16, 16, 17, 92, -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 31, 32, 33, 34, 35, 36, 37, 38, -125, 4,
				-111, 86, 52, 18, -124, 3, 91, 92, 93, -122, 0, -121, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -120, 0, -119, 1, 13 };
	}

	private byte[] getEncodedDataRAIdentity() {
		return new byte[] { 11, 12, 13, 14, 15, 16 };
	}

	private byte[] getGeographicalInformation() {
		return new byte[] { 31, 32, 33, 34, 35, 36, 37, 38 };
	}

	private byte[] getEncodedDataLSAIdentity() {
		return new byte[] { 91, 92, 93 };
	}

	private byte[] getGeodeticInformation() {
		return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9,10 };
	}

	@Test(groups = { "functional.decode","subscriberInformation"})
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedData();
		
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		LocationInformationGPRSImpl impl = new LocationInformationGPRSImpl();
		impl.decodeAll(asn);
		assertEquals(tag, Tag.SEQUENCE);

		assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC(), 250);
		assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC(), 1);
		assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac(), 4444);
		assertTrue(Arrays.equals(impl.getRouteingAreaIdentity().getData(), this.getEncodedDataRAIdentity()));
		assertTrue(Arrays.equals(impl.getGeographicalInformation().getData(), this.getGeographicalInformation()));
		assertTrue(impl.getSGSNNumber().getAddress().equals("654321"));
		assertTrue(Arrays.equals(impl.getLSAIdentity().getData(), this.getEncodedDataLSAIdentity()));
		assertTrue(impl.isSaiPresent());
		assertTrue(Arrays.equals(impl.getGeodeticInformation().getData(), this.getGeodeticInformation()));
		assertTrue(impl.isCurrentLocationRetrieved());
		assertEquals((int)impl.getAgeOfLocationInformation(), 13);
	}
	
	@Test(groups = { "functional.encode","subscriberInformation"})
	public void testEncode() throws Exception {

		LAIFixedLengthImpl lai = new LAIFixedLengthImpl(250, 1, 4444);
		CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
		RAIdentityImpl ra = new RAIdentityImpl(this.getEncodedDataRAIdentity());
		GeographicalInformationImpl ggi = new GeographicalInformationImpl(this.getGeographicalInformation());
		ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "654321");
		LSAIdentityImpl lsa = new LSAIdentityImpl(this.getEncodedDataLSAIdentity());
		GeodeticInformationImpl gdi = new GeodeticInformationImpl(this.getGeodeticInformation());

		LocationInformationGPRSImpl impl = new LocationInformationGPRSImpl(cgi, ra, ggi, sgsn, lsa, null, true, gdi, true, 13);
		AsnOutputStream asnOS = new AsnOutputStream();
		impl.encodeAll(asnOS);
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();		
		
		// TODO: fix a test
//		assertTrue( Arrays.equals(rawData,encodedData));
	}

}
