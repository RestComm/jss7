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

/**
 * Start time:14:21:35 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:14:21:35 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface UserTeleserviceInformation extends ISUPParameter {
    // NOTE: Q.931 4.5.17 High layer compatibility --> it has the same structure
    // and encoding
    int _PARAMETER_CODE = 0x34;

    // FIXME: add C defs
    /**
     * See Q.931 4.5.17 Coding standard : ITU-T standardized coding,
     */
    int _CODING_STANDARD_ITU_T = 0;
    /**
     * See Q.931 4.5.17 Coding standard : ISO/IEC standard
     */
    int _CODING_STANDARD_ISO_IEC = 1;
    /**
     * See Q.931 4.5.17 Coding standard : National standard
     */
    int _CODING_STANDARD_NATIONAL = 2;
    /**
     * See Q.931 4.5.17 Coding standard : Standard defined for the network (either public or private) present on the network
     * side of the interface
     */
    int _CODING_STANDARD_DFTN = 3;

    /**
     * See Q.931 4.5.17 Interpretation : First (primary or only) high layer characteristics identification (in octet 4) to be
     * used in the call All other values are reserved
     */
    int _INTERPRETATION_FHGCI = 4;
    /**
     * See Q.931 4.5.17 Presentation method of protocol profile : High layer protocol profile (without specification of
     * attributes) All other values are reserved.
     */
    int _PRESENTATION_METHOD_HLPP = 1;

    /**
     * See Q.931 4.5.17 High layer characteristics identification : telephony
     */
    int _HLCI_TELEPHONY = 1;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Facsimile Group 2/3 (Recommendation F.182 [68])
     */
    int _HLCI_FG_2_3 = 4;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Facsimile Group 4 Class I (Recommendation F.184 [69])
     */
    int _HLCI_FG_4 = 0x21;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Facsimile service Group 4, Classes II ad III (Recommendation
     * F.184)
     */
    int _HLCI_FG_4_C_II_III = 0x24;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Syntax based Videotex (Recommendation F.300 [73] and T.102
     * [74])
     */
    int _HLCI_SBVT = 0x32;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : International Videotex interworking via gateways or
     * interworking units (Recommendation F.300 and T.101 [75])
     */
    int _HLCI_IVTI = 0x33;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Telex service (Recommendation F.60 [76])
     */
    int _HLCI_TLS = 0x35;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Message Handling Systems (MHS) (X.400-series Recommendation
     * [77])
     */
    int _HLCI_MHS = 0x38;

    /**
     * See Q.931 4.5.17 High layer characteristics identification : OSI application (Note 6) (X.200-series Recommendations [78])
     */
    int _HLCI_OSIA = 0x41;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : FTAM application (ISO 8571)
     */
    int _HLCI_FTAM = 0x42;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Reserved for maintenance (Note 8)
     */
    int _HLCI_MAINTAINENCE = 0x5E;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Reserved for management (Note 8)
     */
    int _HLCI_MANAGEMENT = 0x5F;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Videotelephony (Recommendations F.720 [91] and F.721 [79])
     * and F.731 profile 1a) (Note 9)
     */
    int _HLCI_VIDEOTELEPHONY = 0x60;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Videoconferencing Recommendation F.702 [94] and F.731 [97]
     * Profile 1b (Note 9)
     */
    int _HLCI_VIDEO_CONF = 0x61;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Audiographic conferencing Recommendations F.702 [94] and
     * F.731 [97] (including at least profile 2a2 and optionally 2a1, 2a3, 2b1, 2b2, and 2bc) (Notes 9 and 10)
     */
    int _HLCI_AUDIOGRAPHIC_CONF = 0x62;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Reserved for audiovisual service (F.700-series
     * Recommendations [80]) - minimal value in reserved range
     */
    int _HLCI_AUDIO_VID_LOW_RANGE = 0x63;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Reserved for audiovisual service (F.700-series
     * Recommendations [80]) - maximum value in reserved range
     */
    int _HLCI_AUDIO_VID_HIGH_RANGE = 0x67;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Multimedia services F.700-series Recommendations [80] (Note
     * 9)
     */
    int _HLCI_MMS = 0x68;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Reserved for audiovisual service (F.700-series
     * Recommendations [80]) - minimal value in reserved range
     */
    int _HLCI_AUDIO_VID_LOW_RANGE2 = 0x69;
    /**
     * See Q.931 4.5.17 High layer characteristics identification : Reserved for audiovisual service (F.700-series
     * Recommendations [80]) - maximum value in reserved range
     */
    int _HLCI_AUDIO_VID_HIGH_RANGE2 = 0x6F;

    /**
     * See Q.931 4.5.17 Extended High layer characteristics identification : Capability set of initial channel associated with
     * an active 3.1 kHz audio or speech call
     */
    int _EACI_CSIC_AA_3_1_CALL = 0x21;

    /**
     * See Q.931 4.5.17 Extended High layer characteristics identification : Capability set of initial channel of H.221
     */
    int _EACI_CSIC_H221 = 0x01;

    /**
     * See Q.931 4.5.17 Extended High layer characteristics identification : Capability set of subsequent channel of H.221
     */
    int _EACI_CSSC_H221 = 0x02;

    int getCodingStandard();

    void setCodingStandard(int codingStandard);

    int getInterpretation();

    void setInterpretation(int interpretation);

    int getPresentationMethod();

    void setPresentationMethod(int presentationMethod);

    int getHighLayerCharIdentification();

    void setHighLayerCharIdentification(int highLayerCharIdentification);

    int getEHighLayerCharIdentification();

    void setEHighLayerCharIdentification(int highLayerCharIdentification);

    int getEVideoTelephonyCharIdentification();

    void setEVideoTelephonyCharIdentification(int eVidedoTelephonyCharIdentification);

    boolean isEHighLayerCharIdentificationPresent();

    boolean isEVideoTelephonyCharIdentificationPresent();

}
