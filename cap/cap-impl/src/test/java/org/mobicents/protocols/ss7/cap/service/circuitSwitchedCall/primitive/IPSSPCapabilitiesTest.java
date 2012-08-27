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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.testng.*;import org.testng.annotations.*;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class IPSSPCapabilitiesTest {


	public byte[] getData1() {
		return new byte[] { 4, 1, 5 };
	}

	public byte[] getData2() {
		return new byte[] { 4, 4, 26, 11, 22, 33 };
	}

	public byte[] getIntData1() {
		return new byte[] { 11, 22, 33 };
	}

	@Test(groups = { "functional.decode","circuitSwitchedCall.primitive"})
	public void testDecode() throws Exception {

		byte[] data = this.getData1();
		AsnInputStream ais = new AsnInputStream(data);
		IPSSPCapabilitiesImpl elem = new IPSSPCapabilitiesImpl();
		int tag = ais.readTag();
		elem.decodeAll(ais);
		assertTrue(elem.getIPRoutingAddressSupported());
		assertFalse(elem.getVoiceBackSupported());
		assertTrue(elem.getVoiceInformationSupportedViaSpeechRecognition());
		assertFalse(elem.getVoiceInformationSupportedViaVoiceRecognition());
		assertFalse(elem.getGenerationOfVoiceAnnouncementsFromTextSupported());
		assertNull(elem.getExtraData());

		data = this.getData2();
		ais = new AsnInputStream(data);
		elem = new IPSSPCapabilitiesImpl();
		tag = ais.readTag();
		elem.decodeAll(ais);
		assertFalse(elem.getIPRoutingAddressSupported());
		assertTrue(elem.getVoiceBackSupported());
		assertFalse(elem.getVoiceInformationSupportedViaSpeechRecognition());
		assertTrue(elem.getVoiceInformationSupportedViaVoiceRecognition());
		assertTrue(elem.getGenerationOfVoiceAnnouncementsFromTextSupported());
		assertTrue(Arrays.equals(elem.getExtraData(), this.getIntData1()));
	}

	@Test(groups = { "functional.encode","circuitSwitchedCall.primitive"})
	public void testEncode() throws Exception {

		IPSSPCapabilitiesImpl elem = new IPSSPCapabilitiesImpl(true, false, true, false, false, null);
		// boolean IPRoutingAddressSupported, boolean VoiceBackSupported, boolean VoiceInformationSupportedViaSpeechRecognition,
		// boolean VoiceInformationSupportedViaVoiceRecognition, boolean GenerationOfVoiceAnnouncementsFromTextSupported, byte[] extraData
		AsnOutputStream aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData1()));

		elem = new IPSSPCapabilitiesImpl(false, true, false, true, true, getIntData1());
		// boolean IPRoutingAddressSupported, boolean VoiceBackSupported, boolean VoiceInformationSupportedViaSpeechRecognition,
		// boolean VoiceInformationSupportedViaVoiceRecognition, boolean GenerationOfVoiceAnnouncementsFromTextSupported, byte[] extraData
		aos = new AsnOutputStream();
		elem.encodeAll(aos);
		assertTrue(Arrays.equals(aos.toByteArray(), this.getData2()));
	}
}

