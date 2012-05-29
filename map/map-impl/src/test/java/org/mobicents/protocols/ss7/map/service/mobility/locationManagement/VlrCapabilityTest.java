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
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IstSupportIndicator;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SuperChargerInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.subscriberManagement.SupportedCamelPhases;
import org.mobicents.protocols.ss7.map.service.subscriberManagement.SupportedCamelPhasesImpl;
import org.testng.annotations.Test;

public class VlrCapabilityTest {

	private byte[] getEncodedData() {
		return new byte[] { 48, 11, (byte) 128, 2, 4, (byte) 192, (byte) 129, 1, 1, (byte) 133, 2, 4, (byte) 240 };
	}

	private byte[] getEncodedDataEC() {
		return new byte[] { 48, 26, (byte) 128, 2, 5, (byte) 224, 48, 20, (byte) 160, 18, 48, 16, 6, 8, 42, (byte) 134, 8, 8, 8, 8, 8, 1, 48, 4, 3, 2, 6, 64 };
	}

	private long[] getECOid() {
		return new long[] { 1, 2, 776, 8, 8, 8, 8, 1 };
	}

	private byte[] getECData() {
		return new byte[] { 48, 4, 3, 2, 6, 64 };
	}

	@Test
	public void testDecode() throws Exception {

		byte[] rawData = getEncodedData();
		AsnInputStream asn = new AsnInputStream(rawData);

		int tag = asn.readTag();
		VlrCapabilityImpl asc = new VlrCapabilityImpl();
		asc.decodeAll(asn);

		assertEquals( tag,Tag.SEQUENCE);
		assertEquals( asn.getTagClass(),Tag.CLASS_UNIVERSAL);

		SupportedCamelPhases scph = asc.getSupportedCamelPhases();
		assertTrue(scph.getPhase1Supported());
		assertTrue(scph.getPhase2Supported());
		assertFalse(scph.getPhase3Supported());
		assertFalse(scph.getPhase4Supported());

		assertNull(asc.getExtensionContainer());
		
		assertFalse(asc.getSolsaSupportIndicator());
		
		assertEquals(asc.getIstSupportIndicator(), IstSupportIndicator.istCommandSupported);
		
		assertNull(asc.getSuperChargerSupportedInServingNetworkEntity());
		assertFalse(asc.getLongFtnSupported());
		
		SupportedLCSCapabilitySets slcs = asc.getSupportedLCSCapabilitySets();
		assertTrue(slcs.getCapabilitySetRelease98_99());
		assertTrue(slcs.getCapabilitySetRelease4());
		assertTrue(slcs.getCapabilitySetRelease5());
		assertTrue(slcs.getCapabilitySetRelease6());
		assertFalse(slcs.getCapabilitySetRelease7());		

		assertNull(asc.getOfferedCamel4CSIs());
		assertNull(asc.getSupportedRATTypesIndicator());
		assertFalse(asc.getLongGroupIDSupported());		
		assertFalse(asc.getMtRoamingForwardingSupported());		


		rawData = getEncodedDataEC();
		asn = new AsnInputStream(rawData);

		tag = asn.readTag();
		asc = new VlrCapabilityImpl();
		asc.decodeAll(asn);

		assertEquals( tag,Tag.SEQUENCE);
		assertEquals( asn.getTagClass(),Tag.CLASS_UNIVERSAL);

		scph = asc.getSupportedCamelPhases();
		assertTrue(scph.getPhase1Supported());
		assertTrue(scph.getPhase2Supported());
		assertTrue(scph.getPhase3Supported());
		assertFalse(scph.getPhase4Supported());

		MAPExtensionContainer ext = asc.getExtensionContainer();
		assertTrue(Arrays.equals(ext.getPrivateExtensionList().get(0).getOId(), getECOid()));
		assertTrue(Arrays.equals(ext.getPrivateExtensionList().get(0).getData(), getECData()));
		
		assertFalse(asc.getSolsaSupportIndicator());
		
		assertNull(asc.getIstSupportIndicator());
		
		assertNull(asc.getSuperChargerSupportedInServingNetworkEntity());
		assertFalse(asc.getLongFtnSupported());
		
		assertNull(asc.getSupportedLCSCapabilitySets());

		assertNull(asc.getOfferedCamel4CSIs());
		assertNull(asc.getSupportedRATTypesIndicator());
		assertFalse(asc.getLongGroupIDSupported());		
		assertFalse(asc.getMtRoamingForwardingSupported());		
	}

	@Test(groups = { "functional.encode"})
	public void testEncode() throws Exception {

		SupportedCamelPhases scp = new SupportedCamelPhasesImpl(true, true, false, false);
		SupportedLCSCapabilitySets slcs = new SupportedLCSCapabilitySetsImpl(true, true, true, true, false);
		VlrCapabilityImpl asc = new VlrCapabilityImpl(scp, null, false, IstSupportIndicator.istCommandSupported, null, false, slcs, null, null, false, false);
//		SupportedCamelPhases supportedCamelPhases, MAPExtensionContainer extensionContainer, boolean solsaSupportIndicator,
//		IstSupportIndicator istSupportIndicator, SuperChargerInfo superChargerSupportedInServingNetworkEntity, boolean longFtnSupported,
//		SupportedLCSCapabilitySets supportedLCSCapabilitySets, OfferedCamel4CSIs offeredCamel4CSIs, SupportedRATTypes supportedRATTypesIndicator,
//		boolean longGroupIDSupported, boolean mtRoamingForwardingSupported

		AsnOutputStream asnOS = new AsnOutputStream();
		asc.encodeAll(asnOS);
		
		byte[] encodedData = asnOS.toByteArray();
		byte[] rawData = getEncodedData();		
		assertTrue( Arrays.equals(rawData,encodedData));
	}
}

