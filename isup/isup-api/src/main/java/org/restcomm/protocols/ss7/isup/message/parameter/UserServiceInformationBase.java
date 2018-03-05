/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.isup.message.parameter;

/**
 * Start time:14:20:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski</a>
 * @author <a href="mailto:oifa.yulian@gmail.com">Yulian Oifa</a>
 * @author sergey vetyutnev
 *
 * Possible extra into:
 * http://www.detewe.ru/q931/4.5.5.htm
 * http://www.acacia-net.com/wwwcla/protocol/q931_ie.htm
 */
public interface UserServiceInformationBase extends ISUPParameter {

    // for parameters list see ITU-T Q.763 (12/1999) 3.57
    // Recommendation Q.931 (05/98) Table 4-6/Q.931 Bearer capability information element
    // Dialogic User Service Information structure :
    // http://www.dialogic.com/webhelp/NASignaling/Release%205.1/NA_ISUP_Layer_Dev_Ref_Manual/user_service_information.htm

    // LAYER IDENTIFIERS
    int _LAYER1_IDENTIFIER = 0x1;

    int _LAYER2_IDENTIFIER = 0x2;

    int _LAYER3_IDENTIFIER = 0x3;

    // CODING STANDART OPTIONS
    /**
     * ITU-T standardized coding
     */
    int _CS_CCITT = 0;

    /**
     * ISO/IEC Standard
     */
    int _CS_INTERNATIONAL = 1;

    int _CS_NATIONAL = 2;

    int _CS_NETWORK = 3;

    // INFORMATION TRANSFER CAPABILITIES OPTIONS
    /**
     * Speech
     */
    int _ITS_SPEECH = 0;

    /**
     * Unrestricted digital information
     */
    int _ITS_UNRESTRICTED_DIGITAL = 8;

    /**
     * Restricted digital information
     */
    int _ITS_RESTRICTED_DIGITAL = 9;

    /**
     * 3.1 kHz audio
     */
    int _ITS_3_1_KHZ = 16;

    /**
     * Unrestricted digital information with tones/announcements. Unrestricted
     * digital information with tones/announcements (UDI-TA) is the new
     * information transfer attribute value that had previously been named
     * "7 kHz audio" in Recommendation Q.931 (1988).
     */
    int _ITS_UNRESTRICTED_DIGITAL_WITH_TONES = 17;

    /**
     * Video
     */
    int _ITS_VIDEO = 24;

    // TRANSFER MODE OPTIONS
    /**
     * Circuit mode
     */
    int _TM_CIRCUIT = 0;

    /**
     * Packet mode
     */
    int _TM_PACKET = 2;

    // INFORMATION TRANSFER RATE OPTIONS
    /**
     * This code shall be used for packet-mode calls
     */
    int _ITR_PACKET_MODE = 0;

    /**
     * Circuit mode - 64 kbit/s
     */
    int _ITR_64 = 16;

    /**
     * Circuit mode - 2 * 64 kbit/s
     */
    int _ITR_64x2 = 17;

    /**
     * Circuit mode - 384 kbit/s
     */
    int _ITR_384 = 19;

    /**
     * Circuit mode - 1536 kbit/s
     */
    int _ITR_1536 = 21;

    /**
     * Circuit mode - 1920 kbit/s
     */
    int _ITR_1920 = 23;

    /**
     * Circuit mode - Multirate (64 kbit/s base rate)
     */
    int _ITR_MULTIRATE = 24;

    // SYNC/ASYNC OPTIONS
    /**
     * Synchronous data. Octets 5b-5d may be omitted in the case of synchronous
     * user rates.
     */
    int _SA_SYNC = 0;

    /**
     * Asynchronous data
     */
    int _SA_ASYNC = 1;

    // NEGOTIATION OPTIONS
    /**
     * In-band negotiation not possible
     */
    int _NG_INBAND_NOT_POSSIBLE = 0;

    /**
     * In-band negotiation possible
     */
    int _NG_INBAND_POSSIBLE = 1;

    // USER RATE OPTIONS
    /**
     * For I.460, rate is specified by bits 7, 6 of octet 5b, intermediate rate.
     * For V.110 and X.30, rate is indicated by E-bits (synchronous data only)
     * or may be negotiated in-band. For V.120, rate is unspecified or may be
     * negotiated in-band.
     */
    int _UR_EBITS = 0;

    /**
     * 0.6 kbit/s Recommendation X.1
     */
    int _UR_0_6 = 1;

    /**
     * 1.2 kbit/s
     */
    int _UR_1_2 = 2;

    /**
     * 2.4 kbit/s Recommendation X.1
     */
    int _UR_2_4 = 3;

    /**
     * 3.6 kbit/s
     */
    int _UR_3_6 = 4;

    /**
     * 4.8 kbit/s Recommendation X.1
     */
    int _UR_4_8 = 5;

    /**
     * 7.2 kbit/s
     */
    int _UR_7_2 = 6;

    /**
     * 8 kbit/s Recommendation I.460
     */
    int _UR_8_0 = 7;

    /**
     * 9.6 kbit/s Recommendation X.1
     */
    int _UR_9_6 = 8;

    /**
     * 14.4 kbit/s
     */
    int _UR_14_4 = 9;

    /**
     * 16 kbit/s Recommendation I.460
     */
    int _UR_16_0 = 10;

    /**
     * 19.2 kbit/s
     */
    int _UR_19_2 = 11;

    /**
     * 32 kbit/s Recommendation I.460
     */
    int _UR_32_0 = 12;

    /**
     * 38.4 kbit/s Recommendation V.110
     */
    int _UR_38_4 = 13;

    /**
     * 48 kbit/s Recommendations X.1
     */
    int _UR_48_0 = 14;

    /**
     * 56 kbit/s
     */
    int _UR_56_0 = 15;

    /**
     * 57.6 kbit/s Recommendation V.14 extended
     */
    int _UR_57_6 = 18;

    /**
     * 28.8 kbit/s Recommendation V.110
     */
    int _UR_28_8 = 19;

    /**
     * 24 kbit/s Recommendation V.110
     */
    int _UR_24_0 = 20;

    /**
     * 0.1345 kbit/s Recommendation X.1
     */
    int _UR_0_1345 = 21;

    /**
     * 0.100 kbit/s Recommendation X.1
     */
    int _UR_0_1 = 22;

    /**
     * 0.075/1.2 kbit/s Recommendation X.1
     */
    int _UR_0_075_ON_1_2 = 23;

    /**
     * 1.2/0.075 kbit/s Recommendation X.1
     */
    int _UR_1_2_ON_0_075 = 24;

    /**
     * 0.050 kbit/s Recommendation X.1
     */
    int _UR_0_050 = 25;

    /**
     * 0.075 kbit/s Recommendation X.1
     */
    int _UR_0_075 = 26;

    /**
     * 0.110 kbit/s Recommendation X.1
     */
    int _UR_0_110 = 27;

    /**
     * 0.150 kbit/s Recommendation X.1
     */
    int _UR_0_150 = 28;

    /**
     * 0.200 kbit/s Recommendation X.1
     */
    int _UR_0_200 = 29;

    /**
     * 0.300 kbit/s Recommendation X.1
     */
    int _UR_0_300 = 30;

    /**
     * 12 kbit/s
     */
    int _UR_12_0 = 31;

    // INTERMEDIATE RATE OPTIONS
    /**
     * Not used
     */
    int _IR_NOT_USED = 0;

    /**
     * 8 kbit/s
     */
    int _IR_8_0 = 1;

    /**
     * 16 kbit/s
     */
    int _IR_16_0 = 2;

    /**
     * 32 kbit/s
     */
    int _IR_32_0 = 3;

    // NETWORK INDEPENDENT CLOCK ON TX
    /**
     * Not required to send data with network independent clock
     */
    int _NICTX_NOT_REQUIRED = 0;

    /**
     * Required to send data with network independent clock
     */
    int _NICTX_REQUIRED = 1;

    // NETWORK INDEPENDENT CLOCK ON RX
    /**
     * Cannot accept data with network independent clock (i.e. sender does not
     * support this optional procedure).
     */
    int _NICRX_CANNOT_ACCEPT = 0;

    /**
     * Can accept data with network independent clock (i.e. sender does support
     * this optional procedure).
     */
    int _NICRX_CAN_ACCEPT = 1;

    // FLOW CONTROL ON TX
    /**
     * Not required to send data with flow control mechanism
     */
    int _FCTX_NOT_REQUIRED = 0;

    /**
     * Required to send data with flow control mechanism
     */
    int _FCTX_REQUIRED = 1;

    // FLOW CONTROL ON RX
    /**
     * Cannot accept data with flow control mechanism (i.e. sender does not
     * support this optional procedure)
     */
    int _FCRX_CANNOT_ACCEPT = 0;

    /**
     * Can accept data with flow control mechanism (i.e. sender does support
     * this optional procedure)
     */
    int _FCRX_CAN_ACCEPT = 1;

    // RATE ADAPTATION HEADER OPTIONS
    /**
     * Rate adaption header not included
     */
    int _HDR_NOT_INCLUDED = 0;

    /**
     * Rate adaption header included
     */
    int _HDR_INCLUDED = 1;

    // MULTIFRAME OPTIONS
    /**
     * Multiple frame establishment not supported. Only UI frames allowed
     */
    int _MF_NOT_SUPPORTED = 0;

    /**
     * Multiple frame establishment supported
     */
    int _MF_SUPPORTED = 1;

    // Mode of operation OPTIONS
    /**
     * Bit transparent mode of operation
     */
    int _MODE_BIT_TRANSPARENT = 0;

    /**
     * Protocol sensitive mode of operation
     */
    int _MODE_PROTOCOL_SENSITIVE = 1;

    // LOGICAL LINK IDENTIFIER OPTIONS
    /**
     * Default, LLI = 256 only
     */
    int _LLI_256 = 0;

    /**
     * Full protocol negotiation
     */
    int _LLI_FULL_NEGOTIATION = 1;

    // ASSIGNOR / ASSIGNEE OPTIONS
    /**
     * Message originator is "Default assignee"
     */
    int _ASS_DEFAULT_ASSIGNEE = 0;

    /**
     * Message originator is "Assignor only"
     */
    int _ASS_ASSIGNOR_ONLY = 1;

    // INBAND/OUT OF BAND NEGOTIATION OPTIONS
    /**
     * Negotiation is done with USER INFORMATION messages on a temporary
     * signalling connection
     */
    int _NEG_USER_INFORMATION = 0;

    /**
     * Negotiation is done in-band using logical link zero
     */
    int _NEG_INBAND = 1;

    // STOP BITS OPTIONS
    /**
     * Not used
     */
    int _SB_NOT_USED = 0;

    /**
     * 1 bit
     */
    int _SB_1BIT = 1;

    /**
     * 1.5 bits
     */
    int _SB_1_5BITS = 2;

    /**
     * 2 bits
     */
    int _SB_2BITS = 3;

    // DATA BITS OPTIONS
    /**
     * Not used
     */
    int _DB_NOT_USED = 0;

    /**
     * 5 bits
     */
    int _DB_5BITS = 1;

    /**
     * 7 bits
     */
    int _DB_7BITS = 2;

    /**
     * 8 bits
     */
    int _DB_8BITS = 3;

    // PARITY INFORMATION
    /**
     * Odd
     */
    int _PAR_ODD = 0;

    /**
     * Even
     */
    int _PAR_EVEN = 2;

    /**
     * None
     */
    int _PAR_NONE = 3;

    /**
     * Forced to 0
     */
    int _PAR_FORCED_0 = 4;

    /**
     * Forced to 1
     */
    int _PAR_FORCED_1 = 5;

    // DUPLEX INFORMATION
    /**
     * Half duplex
     */
    int _DUP_HALF = 0;

    /**
     * Full duplex
     */
    int _DUP_FULL = 1;

    // MODEM TYPE INFORMATION
    /**
     * Recommendation V.21
     */
    int _MODEM_V21 = 17;

    /**
     * Recommendation V.22
     */
    int _MODEM_V22 = 18;

    /**
     * Recommendation V.22 bis
     */
    int _MODEM_V22_BIS = 19;

    /**
     * Recommendation V.23
     */
    int _MODEM_V23 = 20;

    /**
     * Recommendation V.26
     */
    int _MODEM_V26 = 21;

    /**
     * Recommendation V.26 bis
     */
    int _MODEM_V26_BIS = 22;

    /**
     * Recommendation V.26 ter
     */
    int _MODEM_V26_TER = 23;

    /**
     * Recommendation V.27
     */
    int _MODEM_V27 = 24;

    /**
     * Recommendation V.27 bis
     */
    int _MODEM_V27_BIS = 25;

    /**
     * Recommendation V.27 ter
     */
    int _MODEM_V27_TER = 26;

    /**
     * Recommendation V.29
     */
    int _MODEM_V29 = 27;

    /**
     * Recommendation V.32
     */
    int _MODEM_V32 = 29;

    /**
     * Recommendation V.34
     */
    int _MODEM_V34 = 30;

    // LAYER 1 USER INFORMATION OPTIONS
    /**
     * ITU-T standardized rate adaption V.110, I.460 and X.30. This implies the
     * presence of octet 5a and optionally octets 5b, 5c and 5d
     */
    int _L1_ITUT_110 = 1;

    /**
     * Recommendation G.711 [10] qu-law
     */
    int _L1_G711_MU = 2;

    /**
     * Recommendation G.711 A-law
     */
    int _L1_G711_A = 3;

    /**
     * Recommendation G.721 [11] 32 kbit/s ADPCM and Recommendation I.460
     */
    int _L1_G721_ADPCM = 4;

    /**
     * Recommendations H.221 and H.242
     */
    int _L1_G722_G725 = 5;

    /**
     * Recommendations H.223 [92] and H.245 [93]
     */
    int _L1_H261 = 6;

    /**
     * Non-ITU-T standardized rate adaption. This implies the presence of octet 5a and,
     * optionally, octets 5b, 5c and 5d. The use of this codepoint indicates that the user rate
     * specified in octet 5a is defined by the user. Additionally, octets 5b, 5c and 5d, if
     * present, are defined in accordance with the user specified rate adaption
     */
    int _L1_NON_ITUT = 7;

    /**
     * ITU-T standardized rate adaption V.120 [9]. This implies the presence of octets 5a and
     * 5b as defined below, and optionally octets 5c and 5d
     */
    int _L1_ITUT_120 = 8;

    /**
     * ITU-T standardized rate adaption X.31 [14] HDLC flag stuffing
     */
    int _L1_ITUT_X31 = 9;

    // LAYER 2 USER INFORMATION OPTIONS
    int _L2_BASIC = 1;

    /**
     * Recommendation Q.921/I.441
     */
    int _L2_Q921 = 2;

    /**
     * Recommendation X.25 [5], link layer
     */
    int _L2_X25_SLP = 6;

    int _L2_X25_MLP = 7;

    int _L2_T71 = 8;

    int _L2_HDLC_ARM = 9;

    int _L2_HDLC_NRM = 10;

    int _L2_HDLC_ABM = 11;

    /**
     * LAN logical link control (ISO/IEC 8802-2) (Note 23)
     */
    int _L2_LAN_LLC = 12;

    int _L2_X75_SLP = 13;

    int _L2_Q922 = 14;

    int _L2_USR_SPEC = 16;

    int _L2_T90 = 17;

    // LAYER 3 USER INFORMATION OPTIONS
    /**
     * Recommendation Q.931
     */
    int _L3_Q931 = 2;

    int _L3_T90 = 5;

    /**
     * Recommendation X.25, packet layer
     */
    int _L3_X25_PLP = 6;

    int _L3_ISO_8208 = 7;

    int _L3_ISO_8348 = 8;

    int _L3_ISO_8473 = 9;

    int _L3_T70 = 10;

    /**
     * ISO/IEC TR 9577 [82] (Protocol identification in the network layer)
     */
    int _L3_ISO_9577 = 11;

    int _L3_USR_SPEC = 16;

    // LAYER 3 PROTOCOL OPTIONS;
    int _L3_PROT_IP = 204;

    int _L3_PROT_P2P = 207;

    int getCodingStandart();

    void setCodingStandart(int codingStandart);

    int getInformationTransferCapability();

    void setInformationTransferCapability(int informationTransferCapability);

    int getTransferMode();

    void setTransferMode(int transferMode);

    int getInformationTransferRate();

    void setInformationTransferRate(int informationTransferRate);

    // custom rate in 64Kbps units
    int getCustomInformationTransferRate();

    void setCustomInformationTransferRate(int informationTransferRate);

    // TO CLEAR USER INFORMATION ON EACH LAYER SET IT TO 0
    int getL1UserInformation();

    void setL1UserInformation(int l1UserInformation);

    int getL2UserInformation();

    void setL2UserInformation(int l2UserInformation);

    int getL3UserInformation();

    void setL3UserInformation(int l3UserInformation);

    int getSyncMode();

    void setSyncMode(int syncMode);

    int getNegotiation();

    void setNegotiation(int negotiation);

    int getUserRate();

    void setUserRate(int userRate);

    int getIntermediateRate();

    void setIntermediateRate(int intermediateRate);

    int getNicOnTx();

    void setNicOnTx(int nicOnTx);

    int getNicOnRx();

    void setNicOnRx(int nicOnRx);

    int getFlowControlOnTx();

    void setFlowControlOnTx(int fcOnTx);

    int getFlowControlOnRx();

    void setFlowControlOnRx(int fcOnRx);

    int getHDR();

    void setHDR(int hdr);

    int getMultiframe();

    void setMultiframe(int multiframe);

    int getMode();

    void setMode(int mode);

    int getLLINegotiation();

    void setLLINegotiation(int lli);

    int getAssignor();

    void setAssignor(int assignor);

    int getInBandNegotiation();

    void setInBandNegotiation(int inBandNegotiation);

    int getStopBits();

    void setStopBits(int stopBits);

    int getDataBits();

    void setDataBits(int dataBits);

    int getParity();

    void setParity(int parity);

    int getDuplexMode();

    void setDuplexMode(int duplexMode);

    int getModemType();

    void setModemType(int modemType);

    int getL3Protocol();

    void setL3Protocol(int l3Protocol);

}
