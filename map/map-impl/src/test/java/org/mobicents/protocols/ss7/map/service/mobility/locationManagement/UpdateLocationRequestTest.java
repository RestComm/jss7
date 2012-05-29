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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import java.util.Arrays;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.authentication.RequestingNodeType;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.PlmnIdImpl;
import org.mobicents.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoRequestImpl;
import org.testng.annotations.Test;

/**
 * 
 * @author sergey vetyutnev
 *
 */
public class UpdateLocationRequestTest {

	private byte[] getEncodedData() {
		return new byte[] { 48, 34, 4, 8, 82, 0, 7, 2, 0, 9, -128, -8, -127, 7, -111, -105, -126, -103, 0, 0, -11, 4, 7, -111, -105, -126, -103, 0, 0, -10,
				-90, 4, -123, 2, 6, -128 };
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
		assertTrue(imsi.getData().equals("111222333444"));
//		assertEquals(asc.getRequestingNodeType(), RequestingNodeType.vlr);
//		assertEquals(asc.getNumberOfRequestedVectors(), 4);
//
//		assertNotNull(asc.getRequestingPlmnId());
//		assertTrue(Arrays.equals(asc.getRequestingPlmnId().getData(), getRequestingPlmnId()));
//		
//		assertNull(asc.getReSynchronisationInfo());
//		assertNull(asc.getExtensionContainer());
//		assertNull(asc.getNumberOfRequestedAdditionalVectors());
//
//		assertFalse(asc.getSegmentationProhibited());
//		assertFalse(asc.getImmediateResponsePreferred());
//		assertTrue(asc.getAdditionalVectorsAreForEPS());

	}

	@Test(groups = { "functional.encode"})
	public void testEncode() throws Exception {

//		IMSIImpl imsi = new IMSIImpl("111222333444");
//		PlmnIdImpl plmnId = new PlmnIdImpl(getRequestingPlmnId());
//		UpdateLocationRequestImpl asc = new UpdateLocationRequestImpl(3, imsi, 4, false, false, null, null, RequestingNodeType.vlr, plmnId,
//				null, true);
//
//		AsnOutputStream asnOS = new AsnOutputStream();
//		asc.encodeAll(asnOS);
//		
//		byte[] encodedData = asnOS.toByteArray();
//		byte[] rawData = getEncodedData();		
//		assertTrue( Arrays.equals(rawData,encodedData));
	}
}

