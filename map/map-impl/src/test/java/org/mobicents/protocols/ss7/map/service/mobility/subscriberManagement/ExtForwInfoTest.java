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
package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.FTNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.BearerServiceCodeValue;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptions;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SSCode;
import org.mobicents.protocols.ss7.map.api.service.supplementary.SupplementaryCodeValue;
import org.mobicents.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.map.service.supplementary.SSCodeImpl;
import org.testng.annotations.Test;


/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class ExtForwInfoTest {
	
	public byte[] getData() {
		return new byte[] { 48, 117, 4, 1, 0, 48, 71, 48, 69, -126, 1, 22,
				-124, 1, 3, -123, 4, -111, 34, 34, -8, -120, 2, 2, 5, -122, 1,
				-92, -121, 1, 2, -87, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11,
				12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5,
				21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -118, 4, -111, 34,
				34, -9, -96, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13,
				14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22,
				23, 24, 25, 26, -95, 3, 31, 32, 33 };
	};
	
	private byte[] getISDNSubaddressStringData() {
		return new byte[] { 2,5 };
	}

	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		byte[] data = this.getData();
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		ExtForwInfoImpl prim = new ExtForwInfoImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, Tag.SEQUENCE);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);
		
		MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
		assertEquals(prim.getSsCode().getSupplementaryCodeValue(), SupplementaryCodeValue.allServices);
	
		ArrayList<ExtForwFeature> forwardingFeatureList = prim.getForwardingFeatureList();;
		assertNotNull(forwardingFeatureList);
		assertTrue(forwardingFeatureList.size() == 1);
		ExtForwFeature extForwFeature = forwardingFeatureList.get(0);
		assertNotNull(extForwFeature);
	
		assertEquals(extForwFeature.getBasicService().getExtBearerService().getBearerServiceCodeValue(), BearerServiceCodeValue.Asynchronous9_6kbps);
		assertNull(extForwFeature.getBasicService().getExtTeleservice());
		assertNotNull(extForwFeature.getSsStatus());
		assertTrue(extForwFeature.getSsStatus().getBitA());
		assertTrue(!extForwFeature.getSsStatus().getBitP());
		assertTrue(!extForwFeature.getSsStatus().getBitQ());
		assertTrue(extForwFeature.getSsStatus().getBitR());
		
		ISDNAddressString forwardedToNumber = extForwFeature.getForwardedToNumber();
		assertNotNull(forwardedToNumber);
		assertTrue(forwardedToNumber.getAddress().equals("22228"));
		assertEquals(forwardedToNumber.getAddressNature(), AddressNature.international_number);
		assertEquals(forwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);
		
		assertTrue(Arrays.equals(extForwFeature.getForwardedToSubaddress().getData(), 
				this.getISDNSubaddressStringData()));
		assertTrue(extForwFeature.getForwardingOptions().getNotificationToCallingParty());
		assertTrue(extForwFeature.getForwardingOptions().getNotificationToForwardingParty());
		assertTrue(!extForwFeature.getForwardingOptions().getRedirectingPresentation());
		assertTrue(extForwFeature.getForwardingOptions().getExtForwOptionsForwardingReason().getCode()
				== ExtForwOptionsForwardingReason.msBusy.getCode());
		assertNotNull(extForwFeature.getNoReplyConditionTime());
		assertTrue(extForwFeature.getNoReplyConditionTime().equals(new Integer(2)));
		FTNAddressString longForwardedToNumber = extForwFeature.getLongForwardedToNumber();
		assertNotNull(longForwardedToNumber);
		assertTrue(longForwardedToNumber.getAddress().equals("22227"));
		assertEquals(longForwardedToNumber.getAddressNature(), AddressNature.international_number);
		assertEquals(longForwardedToNumber.getNumberingPlan(), NumberingPlan.ISDN);
		assertNotNull(extensionContainer);
		assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));

	}
	
	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {
		
		ExtBearerServiceCodeImpl b = new ExtBearerServiceCodeImpl(BearerServiceCodeValue.Asynchronous9_6kbps);
		ExtBasicServiceCodeImpl basicService = new ExtBasicServiceCodeImpl(b);
		MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();
		ExtSSStatusImpl ssStatus =  new ExtSSStatusImpl(false, false, true, true);
		ISDNAddressString forwardedToNumber = new ISDNAddressStringImpl(
				AddressNature.international_number, NumberingPlan.ISDN, "22228");
		ISDNSubaddressString forwardedToSubaddress = new ISDNSubaddressStringImpl(this.getISDNSubaddressStringData());
		ExtForwOptions forwardingOptions = new ExtForwOptionsImpl(true,
				false, true, ExtForwOptionsForwardingReason.msBusy);
		Integer noReplyConditionTime = new Integer(2);
		FTNAddressString longForwardedToNumber = new FTNAddressStringImpl(AddressNature.international_number,
				NumberingPlan.ISDN, "22227");
		
		ExtForwFeatureImpl extForwFeature = new ExtForwFeatureImpl(basicService, ssStatus,
				forwardedToNumber, forwardedToSubaddress, forwardingOptions, 
				noReplyConditionTime, extensionContainer, longForwardedToNumber);
		
		SSCode ssCode = new SSCodeImpl(SupplementaryCodeValue.allServices);
		ArrayList<ExtForwFeature> forwardingFeatureList = new ArrayList<ExtForwFeature>();
		forwardingFeatureList.add(extForwFeature);
		ExtForwInfoImpl prim = new ExtForwInfoImpl(ssCode, forwardingFeatureList, extensionContainer);
		AsnOutputStream asn = new AsnOutputStream();
		prim.encodeAll(asn);

		assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
	}
}
