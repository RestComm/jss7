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
package org.mobicents.protocols.ss7.cap.service.gprs;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.GPRSEventSpecificInformationImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfoMessageType;
import org.mobicents.protocols.ss7.inap.primitives.MiscCallInfoImpl;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRS;
import org.mobicents.protocols.ss7.map.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LAIFixedLengthImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GeodeticInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation.RAIdentityImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.LSAIdentityImpl;
import org.testng.annotations.Test;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class EventReportGPRSRequestTest {
	
	public byte[] getData() {
		return new byte[] { 48, 72, -128, 1, 2, -95, 3, -128, 1, 1, -94, 59,
				-96, 57, -96, 7, -127, 5, 82, -16, 16, 17, 92, -127, 6, 11, 12,
				13, 14, 15, 16, -126, 8, 31, 32, 33, 34, 35, 36, 37, 38, -125,
				4, -111, 86, 52, 18, -124, 3, 91, 92, 93, -122, 0, -121, 10, 1,
				2, 3, 4, 5, 6, 7, 8, 9, 10, -120, 0, -119, 1, 13, -125, 1, 1 };
	};
	
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
	
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		byte[] data = this.getData();
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		EventReportGPRSRequestImpl prim = new EventReportGPRSRequestImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, Tag.SEQUENCE);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

		assertEquals(prim.getGPRSEventType(), GPRSEventType.attachChangeOfPosition);
		assertNotNull(prim.getMiscGPRSInfo().getMessageType());
		assertNull(prim.getMiscGPRSInfo().getDpAssignment());
		assertEquals(prim.getMiscGPRSInfo().getMessageType(), MiscCallInfoMessageType.notification);
		
		
		assertEquals(prim.getGPRSEventSpecificInformation().getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC(), 250);
		assertEquals(prim.getGPRSEventSpecificInformation().getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC(), 1);
		assertEquals(prim.getGPRSEventSpecificInformation().getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac(), 4444);
	
		assertEquals(prim.getPDPID().getId(),1);
	}
	
	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {	
		
		GPRSEventType gprsEventType = GPRSEventType.attachChangeOfPosition;
		MiscCallInfo miscGPRSInfo = new MiscCallInfoImpl(MiscCallInfoMessageType.notification, null);
		
		//gprsEventSpecificInformation
		LAIFixedLengthImpl lai = new LAIFixedLengthImpl(250, 1, 4444);
		CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
		RAIdentityImpl ra = new RAIdentityImpl(this.getEncodedDataRAIdentity());
		GeographicalInformationImpl ggi = new GeographicalInformationImpl(this.getGeographicalInformation());
		ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "654321");
		LSAIdentityImpl lsa = new LSAIdentityImpl(this.getEncodedDataLSAIdentity());
		GeodeticInformationImpl gdi = new GeodeticInformationImpl(this.getGeodeticInformation());
		LocationInformationGPRS locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra, ggi, sgsn, lsa, null, true, gdi, true, 13);
		GPRSEventSpecificInformation gprsEventSpecificInformation = new GPRSEventSpecificInformationImpl(locationInformationGPRS);
	
		//pdpID
		PDPID pdpID = new PDPIDImpl(1);
		
		EventReportGPRSRequestImpl prim = new EventReportGPRSRequestImpl(gprsEventType, miscGPRSInfo, gprsEventSpecificInformation, pdpID);
		AsnOutputStream asn = new AsnOutputStream();
		prim.encodeAll(asn);
		assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
	}
	
}
