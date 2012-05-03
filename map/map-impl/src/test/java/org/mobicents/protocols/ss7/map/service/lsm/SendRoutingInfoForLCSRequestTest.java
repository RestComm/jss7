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
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.primitives.SubscriberIdentityImpl;
import org.mobicents.protocols.ss7.tcap.asn.TcapFactory;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * @author amit bhayani
 * 
 */
public class SendRoutingInfoForLCSRequestTest {
	MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

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
		byte[] data = new byte[] { 0x30, 0x13, (byte) 0x80, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70, (byte) 0xa1, 0x0a, (byte) 0x80, 0x08, 0x27, (byte) 0x94,
				(byte) 0x99, 0x09, 0x00, 0x00, 0x00, (byte) 0xf7 };

		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();

		SendRoutingInfoForLCSRequestImpl rtgInfnoForLCSreqInd = new SendRoutingInfoForLCSRequestImpl();
		rtgInfnoForLCSreqInd.decodeAll(asn);
		
		ISDNAddressString mlcNum = rtgInfnoForLCSreqInd.getMLCNumber();
		assertNotNull(mlcNum);
		assertEquals( mlcNum.getAddressNature(),AddressNature.international_number);
		assertEquals( mlcNum.getNumberingPlan(),NumberingPlan.ISDN);
		assertEquals( mlcNum.getAddress(),"55619007");
		
		SubscriberIdentity subsIdent = rtgInfnoForLCSreqInd.getTargetMS();
		assertNotNull(subsIdent);
		
		IMSI imsi = subsIdent.getIMSI();
		ISDNAddressString msisdn  = subsIdent.getMSISDN();
		
		assertNotNull(imsi);
		assertNull(msisdn);
		
//		assertEquals( imsi.getMCC(),new Long(724l));
//		assertEquals( imsi.getMNC(),new Long(99l));
		assertEquals( imsi.getData(),"724999900000007");

	}

	@Test(groups = { "functional.encode","service.lsm"})
	public void testEncode() throws Exception {
		// The trace is from Brazilian operator
		byte[] data = new byte[] { 0x30, 0x13, (byte) 0x80, 0x05, (byte) 0x91, 0x55, 0x16, 0x09, 0x70, (byte) 0xa1, 0x0a, (byte) 0x80, 0x08, 0x27, (byte) 0x94,
				(byte) 0x99, 0x09, 0x00, 0x00, 0x00, (byte) 0xf7 };

		IMSI imsi = this.MAPParameterFactory.createIMSI("724999900000007");
		SubscriberIdentity subsIdent = new SubscriberIdentityImpl(imsi);

		ISDNAddressString mlcNumber = this.MAPParameterFactory.createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, "55619007");

		SendRoutingInfoForLCSRequestImpl rtgInfnoForLCSreqInd = new SendRoutingInfoForLCSRequestImpl(mlcNumber, subsIdent);

		AsnOutputStream asnOS = new AsnOutputStream();
		rtgInfnoForLCSreqInd.encodeAll(asnOS);

		byte[] encodedData = asnOS.toByteArray();

		assertTrue( Arrays.equals(data,encodedData));
	}
}
