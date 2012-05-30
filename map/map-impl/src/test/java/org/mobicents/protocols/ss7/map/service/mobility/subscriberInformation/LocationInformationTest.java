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
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationNumberMapImpl;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class LocationInformationTest {

	private byte[] getEncodedData() {
		return new byte[] { 16, 22, 2, 1, 0, -127, 6, -111, 0, 32, 34, 17, -15, -93, 9, -128, 7, 82, -16, 16, 0, 111, 8, -92 };
	}

	private byte[] getEncodedData2() {
		return new byte[] { 16, 32, 2, 1, 0, -127, 6, -111, 0, 32, 34, 17, -15, -126, 8, -124, -105, 8, 2, -105, 1, 32, -112, -93, 9, -128, 7, 82, -16, 16, 0,
				111, 8, -92 };
	}
	
	// TODO: implement tests for others members
	// GeographicalInformation geographicalInformation
	// MAPExtensionContainer extensionContainer,
	// LSAIdentity selectedLSAId,
	// ISDNAddressString mscNumber,
	// GeodeticInformation geodeticInformation,
	// boolean currentLocationRetrieved,
	// boolean saiPresent,
	// LocationInformationEPS locationInformationEPS,
	// UserCSGInformation userCSGInformation

	@Test(groups = { "functional.decode","primitives"})
	public void testDecode() throws Exception {
		
		byte[] rawData = getEncodedData();
		
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		LocationInformationImpl impl = new LocationInformationImpl();
		impl.decodeAll(asn);

		assertEquals((int) impl.getAgeOfLocationInformation(), 0);
		assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 250);
		assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 1);
		assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getCellId(), 2212);
		assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 111);
		assertEquals(impl.getVlrNumber().getAddressNature(), AddressNature.international_number);
		assertEquals(impl.getVlrNumber().getNumberingPlan(), NumberingPlan.ISDN);
		assertTrue(impl.getVlrNumber().getAddress().equals("000222111"));

		rawData = getEncodedData2();

		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		impl = new LocationInformationImpl();
		impl.decodeAll(asn);

		assertEquals((int) impl.getAgeOfLocationInformation(), 0);
		assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMCC(), 250);
		assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getMNC(), 1);
		assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getCellId(), 2212);
		assertEquals(impl.getCellGlobalIdOrServiceAreaIdOrLAI().getCellGlobalIdOrServiceAreaIdFixedLength().getLac(), 111);
		assertEquals(impl.getVlrNumber().getAddressNature(), AddressNature.international_number);
		assertEquals(impl.getVlrNumber().getNumberingPlan(), NumberingPlan.ISDN);
		assertTrue(impl.getVlrNumber().getAddress().equals("000222111"));
		LocationNumber ln = impl.getLocationNumber().getLocationNumber();
		assertEquals(ln.getNatureOfAddressIndicator(), 4);
		assertTrue(ln.getAddress().equals("80207910020"));
		assertEquals(ln.getNumberingPlanIndicator(), 1);
		assertEquals(ln.getInternalNetworkNumberIndicator(), 1);
		assertEquals(ln.getAddressRepresentationRestrictedIndicator(), 1);
		assertEquals(ln.getScreeningIndicator(), 3);
	}
	
	@Test(groups = { "functional.encode","primitives"})
	public void testEncode() throws Exception {

		ISDNAddressString vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "000222111");
		CellGlobalIdOrServiceAreaIdFixedLengthImpl cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(250, 1, 111, 2212);
		CellGlobalIdOrServiceAreaIdOrLAIImpl cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
		LocationInformationImpl impl = new LocationInformationImpl(0, null, vlrNumber, null, cellGlobalIdOrServiceAreaIdOrLAI, null, null, null, null, false,
				false, null, null);
		AsnOutputStream asnOS = new AsnOutputStream();
		impl.encodeAll(asnOS, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();		
		assertTrue( Arrays.equals(rawData,encodedData));

		vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "000222111");
		cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(250, 1, 111, 2212);
		cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAIImpl(cellGlobalIdOrServiceAreaIdFixedLength);
		LocationNumberMapImpl locationNumber = new LocationNumberMapImpl(new byte[] { (byte) 132, (byte) 151, 8, 2, (byte) 151, 1, 32, (byte) 144 });
		impl = new LocationInformationImpl(0, null, vlrNumber, locationNumber, cellGlobalIdOrServiceAreaIdOrLAI, null, null, null, null, false, false, null,
				null);
		asnOS = new AsnOutputStream();
		impl.encodeAll(asnOS, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
		encodedData = asnOS.toByteArray();
		rawData = getEncodedData2();
		assertTrue(Arrays.equals(rawData, encodedData));
		
//		Integer ageOfLocationInformation, GeographicalInformation geographicalInformation, ISDNAddressString vlrNumber,
//		LocationNumber locationNumber, CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, MAPExtensionContainer extensionContainer,
//		LSAIdentity selectedLSAId, ISDNAddressString mscNumber, GeodeticInformation geodeticInformation, boolean currentLocationRetrieved,
//		boolean saiPresent, LocationInformationEPS locationInformationEPS, UserCSGInformation userCSGInformation
	}

}
