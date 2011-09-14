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
import org.mobicents.protocols.ss7.map.MapServiceFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.SubscriberIdentity;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * Trace is from Brazil Operator
 * 
 * @author amit bhayani
 * 
 */
public class SendRoutingInfoForLCSResponseIndicationTest {
	MapServiceFactory mapServiceFactory = new MapServiceFactoryImpl();

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
	public void testDecodeProvideSubscriberLocationRequestIndication() throws Exception {
		// The trace is from Brazilian operator
		byte[] data = new byte[] { 0x30, 0x14, (byte) 0xa0, 0x09, (byte) 0x81, 0x07, (byte) 0x91, 0x55, 0x16, 0x28, (byte) 0x81, 0x00, 0x70, (byte) 0xa1, 0x07, 0x04, 0x05,
				(byte) 0x91, 0x55, 0x16, 0x09, 0x00 };

		AsnInputStream asn = new AsnInputStream(data);

		int tag = asn.readTag();

		SendRoutingInfoForLCSResponseIndicationImpl rtgInfnoForLCSresInd = new SendRoutingInfoForLCSResponseIndicationImpl();
		rtgInfnoForLCSresInd.decodeAll(asn);

		SubscriberIdentity subsIdent = rtgInfnoForLCSresInd.getTargetMS();
		assertNotNull(subsIdent);

		IMSI imsi = subsIdent.getIMSI();
		ISDNAddressString msisdn = subsIdent.getMSISDN();

		assertNotNull(msisdn);
		assertNull(imsi);

		assertEquals( msisdn.getAddressNature(),AddressNature.international_number);
		assertEquals( msisdn.getNumberingPlan(),NumberingPlan.ISDN);
		assertEquals( msisdn.getAddress(),"556182180007");
		
		LCSLocationInfo lcsLocInfo = rtgInfnoForLCSresInd.getLCSLocationInfo();
		assertNotNull(lcsLocInfo);

		ISDNAddressString networkNodeNumber = lcsLocInfo.getNetworkNodeNumber();
		assertNotNull(networkNodeNumber);
		assertEquals( networkNodeNumber.getAddressNature(),AddressNature.international_number);
		assertEquals( networkNodeNumber.getNumberingPlan(),NumberingPlan.ISDN);
		assertEquals( networkNodeNumber.getAddress(),"55619000");
	}

	@Test(groups = { "functional.encode","service.lsm"})
	public void testEncode() throws Exception {
		// The trace is from Brazilian operator
		byte[] data = new byte[] { 0x30, 0x14, (byte) 0xa0, 0x09, (byte) 0x81, 0x07, (byte) 0x91, 0x55, 0x16, 0x28, (byte) 0x81, 0x00, 0x70, (byte) 0xa1, 0x07, 0x04, 0x05,
				(byte) 0x91, 0x55, 0x16, 0x09, 0x00 };

		ISDNAddressString msisdn = this.mapServiceFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "556182180007");
		SubscriberIdentity subsIdent = new SubscriberIdentityImpl(msisdn);

		ISDNAddressString networkNodeNumber = this.mapServiceFactory
				.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "55619000");

		LCSLocationInfo lcsLocInfo = new LCSLocationInfoImpl(networkNodeNumber, null, null, null, null, null, null);

		SendRoutingInfoForLCSResponseIndicationImpl rtgInfnoForLCSresInd = new SendRoutingInfoForLCSResponseIndicationImpl(subsIdent, lcsLocInfo, null, null,
				null, null, null);

		AsnOutputStream asnOS = new AsnOutputStream();
		rtgInfnoForLCSresInd.encodeAll(asnOS);

		byte[] encodedData = asnOS.toByteArray();
		assertTrue( Arrays.equals(data,encodedData));
	}
}
