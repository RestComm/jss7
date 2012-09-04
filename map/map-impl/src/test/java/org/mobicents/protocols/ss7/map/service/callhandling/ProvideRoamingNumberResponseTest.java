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

package org.mobicents.protocols.ss7.map.service.callhandling;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrivateExtension;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.mobicents.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingOptions;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.Assert.*;
import org.testng.*;
import org.testng.annotations.*;

/*
 * 
 * @author cristian veliscu
 * 
 */
public class ProvideRoamingNumberResponseTest {
	Logger logger = Logger.getLogger(ProvideRoamingNumberResponseTest.class);

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

	private byte[] getEncodedData() {
		return new byte[] { 48, 59, 4, 7, -111, -108, -120, 115, 0, -110, -14, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, 4, 7, -111, -110, 17, 19, 50, 19, -15 };
	}

	private byte[] getEncodedData1() {
		return new byte[] { 4, 7, -111, -108, -120, 115, 0, -110, -14 };
	}

	public static MAPExtensionContainer GetTestExtensionContainer() {
		MAPParameterFactory mapServiceFactory = new MAPParameterFactoryImpl();

		ArrayList<MAPPrivateExtension> al = new ArrayList<MAPPrivateExtension>();
		al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2,
				3, 4 }, new byte[] { 11, 12, 13, 14, 15 }));
		al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2,
				3, 6 }, null));
		al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2,
				3, 5 }, new byte[] { 21, 22, 23, 24, 25, 26 }));

		MAPExtensionContainer cnt = mapServiceFactory
				.createMAPExtensionContainer(al, new byte[] { 31, 32, 33 });

		return cnt;
	}

	 @Test(groups = { "functional.decode", "service.callhandling" })
	 public void testDecode() throws Exception {

	 AsnInputStream asn = new AsnInputStream(getEncodedData());
	 int tag = asn.readTag();
	
	 ProvideRoamingNumberResponseImpl prn = new
			 ProvideRoamingNumberResponseImpl(3);
	 prn.decodeAll(asn);
	
	 
		ISDNAddressString roamingNumber = prn.getRoamingNumber();
		MAPExtensionContainer extensionContainer = prn.getExtensionContainer();
		boolean releaseResourcesSupported = prn.getReleaseResourcesSupported();
		ISDNAddressString vmscAddress = prn.getVmscAddress(); 
		long mapProtocolVersion = prn.getMapProtocolVersion();
	
	    assertNotNull(roamingNumber);
		assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
		assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
		assertEquals(roamingNumber.getAddress(), "49883700292");	
		
		
	    assertNotNull(extensionContainer);
	    assertFalse(releaseResourcesSupported);
	    assertNotNull(vmscAddress);
		assertEquals(vmscAddress.getAddressNature(), AddressNature.international_number);
		assertEquals(vmscAddress.getNumberingPlan(), NumberingPlan.ISDN);
		assertEquals(vmscAddress.getAddress(), "29113123311");	
		assertEquals(mapProtocolVersion, 3);
		
		System.out.println("Success");
		
	 

	 }

	@Test(groups = { "functional.encode", "service.callhandling" })
	public void testEncode() throws Exception {

		ISDNAddressString roamingNumber = new ISDNAddressStringImpl(
				AddressNature.international_number, NumberingPlan.ISDN,
				"49883700292");
		MAPExtensionContainer extensionContainer = GetTestExtensionContainer();
		boolean releaseResourcesSupported = false;
		ISDNAddressString vmscAddress = new ISDNAddressStringImpl(
				AddressNature.international_number, NumberingPlan.ISDN,
				"29113123311");
		long mapProtocolVersion = 3;

		ProvideRoamingNumberResponseImpl prn = new ProvideRoamingNumberResponseImpl(
				roamingNumber, extensionContainer, releaseResourcesSupported,
				vmscAddress, mapProtocolVersion);

		AsnOutputStream asnOS = new AsnOutputStream();
		prn.encodeAll(asnOS);

		byte[] encodedData = asnOS.toByteArray();
		System.out.println("0   :   " + Arrays.toString(encodedData));
		assertTrue(Arrays.equals(getEncodedData(), encodedData));

		// 2
		mapProtocolVersion = 2;
		prn = new ProvideRoamingNumberResponseImpl(roamingNumber, null, false,
				null, mapProtocolVersion);

		asnOS = new AsnOutputStream();
		prn.encodeAll(asnOS);

		encodedData = asnOS.toByteArray();
		System.out.println("1   :   " + Arrays.toString(encodedData));
		assertTrue(Arrays.equals(getEncodedData1(), encodedData));

	}

}
