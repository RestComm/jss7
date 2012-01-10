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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

/**
 * 
 IPSSPCapabilities {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE(
bound.&minIPSSPCapabilitiesLength .. bound.&maxIPSSPCapabilitiesLength)) (SIZE = 1..4)
-- Indicates the gsmSRF resources available. The parameter has two parts, a standard and a
-- bilateral part. The standard part indicates capabilities defined as optional in CAP
-- that shall be recognised (but not necessarily supported) by a gsmSCF. The bilateral
-- part contains further information that is not specified in this standard, but which is set
-- according to bilateral agreements between network operators and/or equipment vendors.
-- The last octet of the standard part is indicated by bit 7 being set to 0, otherwise Bit 7 of
-- a standard part octet is set to 1 indicating that the standard part continues in the following
-- octet. Coding is as follows:
-- Octet 1 Standard Part for CAP
-- Bit Value Meaning
-- 0 0 IPRoutingAddress not supported
-- 1 IPRoutingAddress supported
-- 1 0 VoiceBack not supported
-- 1 VoiceBack supported
-- 2 0 VoiceInformation not supported, via speech recognition
-- 1 VoiceInformation supported, via speech recognition
-- 3 0 VoiceInformation not supported, via voice recognition
-- 1 VoiceInformation supported, via voice recognition
-- 4 0 Generation of voice announcements from Text not supported
-- 1 Generation of voice announcements from Text supported
-- 5 - Reserved
-- 6 - Reserved
-- 7 0 End of standard part
-- 1 This value is reserved in CAP
--
-- Octets 2 to 4 Bilateral Part: Network operator/equipment vendor specific
 * 
 * @author sergey vetyutnev
 * 
 */
public interface IPSSPCapabilities {

	public byte[] getData();

	public boolean IPRoutingAddressSupported();

	public boolean VoiceBackSupported();

	public boolean VoiceInformationSupportedViaSpeechRecognition();

	public boolean VoiceInformationSupportedViaVoiceRecognition();

	public boolean GenerationOfVoiceAnnouncementsFromTextSupported();

	/**
	 * @return 2, 3 and 3 byte array: Bilateral Part: Network operator/equipment
	 *         vendor specific (if present)
	 */
	public byte[] getExtraData();

}
