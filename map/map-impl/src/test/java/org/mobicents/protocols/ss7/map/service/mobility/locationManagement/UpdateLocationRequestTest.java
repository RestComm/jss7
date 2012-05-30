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

package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;

import static org.testng.Assert.*;
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.VlrCapability;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class UpdateLocationRequestTest {

	private byte[] getEncodedData() {
		return new byte[] { 48, 34, 4, 8, 82, 0, 7, 2, 0, 9, -128, -8, -127, 7, -111, -105, -126, -103, 0, 0, -11, 4, 7, -111, -105, -126, -103, 0, 0, -10,
				-90, 4, -123, 2, 3, -128 };
	}

	@Test
	public void testDecode() throws Exception {

		byte[] rawData = getEncodedData();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		UpdateLocationRequestImpl asc = new UpdateLocationRequestImpl(3);
		asc.decodeAll(asn);

		assertEquals( tag,Tag.SEQUENCE);
		assertEquals( asn.getTagClass(),Tag.CLASS_UNIVERSAL);
		assertEquals(asc.getMapProtocolVersion(), 3);

		IMSI imsi = asc.getImsi();
		assertTrue(imsi.getData().equals("250070200090088"));

		ISDNAddressString mscNumber = asc.getMscNumber();
		assertTrue(mscNumber.getAddress().equals("79289900005"));
		assertEquals(mscNumber.getAddressNature(), AddressNature.international_number);		
		assertEquals(mscNumber.getNumberingPlan(), NumberingPlan.ISDN);		

		ISDNAddressString vlrNumber = asc.getVlrNumber();
		assertTrue(vlrNumber.getAddress().equals("79289900006"));
		assertEquals(vlrNumber.getAddressNature(), AddressNature.international_number);		
		assertEquals(vlrNumber.getNumberingPlan(), NumberingPlan.ISDN);		

		VlrCapability vlrCap = asc.getVlrCapability();
		assertTrue(vlrCap.getSupportedLCSCapabilitySets().getCapabilitySetRelease98_99());
		assertFalse(vlrCap.getSupportedLCSCapabilitySets().getCapabilitySetRelease4());		

	}

	@Test(groups = { "functional.encode"})
	public void testEncode() throws Exception {

		IMSIImpl imsi = new IMSIImpl("250070200090088");
		ISDNAddressStringImpl mscNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79289900005");
		ISDNAddressStringImpl vlrNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79289900006");
		SupportedLCSCapabilitySets supportedLCSCapabilitySets = new SupportedLCSCapabilitySetsImpl(true, false, false, false, false);
		VlrCapability vlrCap = new VlrCapabilityImpl(null, null, false, null, null, false, supportedLCSCapabilitySets, null, null, false, false);
		UpdateLocationRequestImpl asc = new UpdateLocationRequestImpl(3, imsi, mscNumber, null, vlrNumber, null, null, vlrCap, false, false, null, null, null,
				false, false);
//		long mapProtocolVersion, IMSI imsi, ISDNAddressString mscNumber, ISDNAddressString roamingNumber,
//		ISDNAddressString vlrNumber, LMSI lmsi, MAPExtensionContainer extensionContainer, VlrCapability vlrCapability, boolean informPreviousNetworkEntity,
//		boolean csLCSNotSupportedByUE, GSNAddress vGmlcAddress, ADDInfo addInfo, PagingArea pagingArea, boolean skipSubscriberDataUpdate,
//		boolean restorationIndicator

		AsnOutputStream asnOS = new AsnOutputStream();
		asc.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();		
		assertTrue( Arrays.equals(rawData,encodedData));
	}
}

