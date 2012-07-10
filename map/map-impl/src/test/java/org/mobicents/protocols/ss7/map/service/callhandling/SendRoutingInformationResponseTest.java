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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import java.util.Arrays;
import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ExtendedRoutingInfo;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingOptions;
import org.mobicents.protocols.ss7.map.api.service.callhandling.ForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.callhandling.RoutingInfo;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.testng.Assert.*;
import org.testng.*;
import org.testng.annotations.*;


/*
 * 
 * @author cristian veliscu
 * 
 */
public class SendRoutingInformationResponseTest {
    Logger logger = Logger.getLogger(SendRoutingInformationResponseTest.class);
    
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
	
	private byte[] getIMSIData() {
		return new byte[] { (byte) 137, 8, 16, 33, 2, 2, 16, -119, 34, -9 };
	}
	
	private byte[] getISDNAddressData() {
		return new byte[] { 4, 7, -111, -105, 114, 99, 80, 24, -7 };
	}
	
	private byte[] getForwardingData() {
		return new byte[] { 48, 12, (byte) 133, 7, -111, -105, 114, 99, 80, 24, -7, (byte) 134, 1, 36};
	}
	
	@Test(groups = { "functional.decode", "service.callhandling" })
	public void testDecode() throws Exception {
		byte[] data = new byte[] { (byte) 163, 19, (byte) 137, 8, 16, 33, 2, 2, 16, -119, 34, -9, 4, 7, -111, -105, 114, 99, 80, 24, -7 };
		byte[] data_ = new byte[] { (byte) 163, 24, (byte) 137, 8, 16, 33, 2, 2, 16, -119, 34, -9, 48, 12, (byte) 133, 7, -111, -105, 114, 99, 80, 24, -7, (byte) 134, 1, 36 };
		
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		
		SendRoutingInformationResponseImpl sri = new SendRoutingInformationResponseImpl();
		sri.decodeAll(asn);
		
		IMSI imsi = sri.getIMSI();
		ExtendedRoutingInfo extRoutingInfo = sri.getExtendedRoutingInfo();
		RoutingInfo routingInfo = extRoutingInfo.getRoutingInfo();
		ISDNAddressString roamingNumber = routingInfo.getRoamingNumber();
		
		assertNotNull(imsi);
		assertEquals(imsi.getData(), "011220200198227");
		assertNotNull(roamingNumber);
		//logger.info(":::::::" + roamingNumber.getAddress());
		assertEquals(roamingNumber.getAddressNature(), AddressNature.international_number);
		assertEquals(roamingNumber.getNumberingPlan(), NumberingPlan.ISDN);
		assertEquals(roamingNumber.getAddress(), "79273605819");	
		
		//:::::::::::::::::::::::::::::::::
		AsnInputStream asn_ = new AsnInputStream(data_);
		int tag_ = asn_.readTag();
		
		SendRoutingInformationResponseImpl sri_ = new SendRoutingInformationResponseImpl();
		sri_.decodeAll(asn_);
		
		IMSI imsi_ = sri_.getIMSI();
		ExtendedRoutingInfo extRoutingInfo_ = sri_.getExtendedRoutingInfo();
		RoutingInfo routingInfo_ = extRoutingInfo_.getRoutingInfo();
		ForwardingData forwardingData_ = routingInfo_.getForwardingData();
		ISDNAddressString isdnAdd_ = forwardingData_.getForwardedToNumber();
		ForwardingOptions forwardingOptions_ = forwardingData_.getForwardingOptions();
		
		assertNotNull(imsi_);
		assertNotNull(forwardingData_);
		assertNotNull(forwardingOptions_);
		assertNotNull(isdnAdd_);
		assertEquals(imsi_.getData(), "011220200198227");
		assertEquals(isdnAdd_.getAddressNature(), AddressNature.international_number);
		assertEquals(isdnAdd_.getNumberingPlan(), NumberingPlan.ISDN);
		assertEquals(isdnAdd_.getAddress(), "79273605819");	
		assertTrue(!forwardingOptions_.isNotificationToForwardingParty());
		assertTrue(!forwardingOptions_.isRedirectingPresentation());
		assertTrue(forwardingOptions_.isNotificationToCallingParty());
		assertTrue(forwardingOptions_.getForwardingReason() == ForwardingReason.busy);
	} 
	
	@Test(groups = { "functional.encode", "service.callhandling" })
	public void testEncode() throws Exception {
		byte[] data = new byte[] { (byte) 163, 19, (byte) 137, 8, 16, 33, 2, 2, 16, -119, 34, -9, 4, 7, -111, -105, 114, 99, 80, 24, -7 };
		byte[] data_ = new byte[] { (byte) 163, 24, (byte) 137, 8, 16, 33, 2, 2, 16, -119, 34, -9, 48, 12, (byte) 133, 7, -111, -105, 114, 99, 80, 24, -7, (byte) 134, 1, 36 };
		
		IMSIImpl imsi = new IMSIImpl("011220200198227");
		ISDNAddressString roamingNumber = new ISDNAddressStringImpl(AddressNature.international_number, 
																	NumberingPlan.ISDN, "79273605819");
		RoutingInfoImpl routingInfo = new RoutingInfoImpl(roamingNumber);
		ExtendedRoutingInfoImpl extRoutingInfo = new ExtendedRoutingInfoImpl(routingInfo);
		SendRoutingInformationResponseImpl sri = new SendRoutingInformationResponseImpl(imsi, extRoutingInfo, null, null);
		
		AsnOutputStream asnOS = new AsnOutputStream();
	    sri.encodeAll(asnOS);
	    
	    byte[] encodedData = asnOS.toByteArray();
		assertTrue(Arrays.equals(data, encodedData));
		
		//:::::::::::::::::::::::::::::::::
		IMSIImpl imsi_ = new IMSIImpl("011220200198227");
		ISDNAddressString isdnAdd_ = new ISDNAddressStringImpl(AddressNature.international_number, 
															   NumberingPlan.ISDN, "79273605819");
		ForwardingOptions forwardingOptions_ = new ForwardingOptionsImpl(false, false, true, ForwardingReason.busy);
		ForwardingData forwardingData_ = new ForwardingDataImpl(isdnAdd_, null, forwardingOptions_, null, null);
		RoutingInfoImpl routingInfo_ = new RoutingInfoImpl(forwardingData_);
		ExtendedRoutingInfoImpl extRoutingInfo_ = new ExtendedRoutingInfoImpl(routingInfo_);
		SendRoutingInformationResponseImpl sri_ = new SendRoutingInformationResponseImpl(imsi_, extRoutingInfo_, null, null);

		AsnOutputStream asnOS_ = new AsnOutputStream();
	    sri_.encodeAll(asnOS_);
	    
	    byte[] encodedData_ = asnOS_.toByteArray();
		assertTrue(Arrays.equals(data_, encodedData_));
	}
	
	@Test(groups = { "functional.serialize", "service.callhandling" })
	public void testSerialization() throws Exception {
		
	}
}

