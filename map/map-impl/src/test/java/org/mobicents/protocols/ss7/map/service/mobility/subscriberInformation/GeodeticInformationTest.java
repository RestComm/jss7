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
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.TypeOfShape;
import org.testng.annotations.Test;

/**
*
* @author sergey vetyutnev
*
*/
public class GeodeticInformationTest {

	private byte[] getEncodedData() {
		return new byte[] { 4, 10, 3, 16, 30, -109, -23, 121, -103, -103, 0, 11 };
	}

	@Test(groups = { "functional.decode","subscriberInformation"})
	public void testDecode() throws Exception {

		byte[] rawData = getEncodedData();

		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		GeodeticInformationImpl impl = new GeodeticInformationImpl();
		impl.decodeAll(asn);

		assertEquals(impl.getScreeningAndPresentationIndicators(), 3);
		assertEquals(impl.getTypeOfShape(), TypeOfShape.EllipsoidPointWithUncertaintyCircle);
		assertTrue(Math.abs(impl.getLatitude() - 21.5) < 0.01);
		assertTrue(Math.abs(impl.getLongitude() - 171) < 0.01);
		assertTrue(Math.abs(impl.getUncertainty() - 0) < 0.01);
		assertEquals(impl.getConfidence(), 11);
	}

	@Test(groups = { "functional.encode","subscriberInformation"})
	public void testEncode() throws Exception {

		GeodeticInformationImpl impl = new GeodeticInformationImpl(3, TypeOfShape.EllipsoidPointWithUncertaintyCircle, 21.5, 171, 0, 11);
		AsnOutputStream asnOS = new AsnOutputStream();
		impl.encodeAll(asnOS);
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();
		assertTrue(Arrays.equals(rawData, encodedData));
	}

}
