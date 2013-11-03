/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

/**
 *
IPSSPCapabilities {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE( bound.&minIPSSPCapabilitiesLength ..bound.&maxIPSSPCapabilitiesLength)) (SIZE = 1..4)
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
-- -- Octets 2 to 4 Bilateral Part: Network operator/equipment vendor specific
 *
 * @author sergey vetyutnev
 *
 */
public interface IPSSPCapabilities extends Serializable {

    byte[] getData();

    boolean getIPRoutingAddressSupported();

    boolean getVoiceBackSupported();

    boolean getVoiceInformationSupportedViaSpeechRecognition();

    boolean getVoiceInformationSupportedViaVoiceRecognition();

    boolean getGenerationOfVoiceAnnouncementsFromTextSupported();

    /**
     * @return 2, 3 and 3 byte array: Bilateral Part: Network operator/equipment vendor specific (if present)
     */
    byte[] getExtraData();

}