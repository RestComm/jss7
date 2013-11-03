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

package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:14:20:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski</a>
 * @author <a href="mailto:oifa.yulian@gmail.com">Yulian Oifa</a>
 * @author sergey vetyutnev
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
    int _CS_CCITT = 0;

    int _CS_INTERNATIONAL = 1;

    int _CS_NATIONAL = 2;

    int _CS_NETWORK = 3;

    // INFORMATION TRANSFER CAPABILITIES OPTIONS
    int _ITS_SPEECH = 0;

    int _ITS_UNRESTRICTED_DIGITAL = 8;

    int _ITS_RESTRICTED_DIGITAL = 9;

    int _ITS_3_1_KHZ = 16;

    int _ITS_UNRESTRICTED_DIGITAL_WITH_TONES = 17;

    int _ITS_VIDEO = 24;

    // TRANSFER MODE OPTIONS
    int _TM_CIRCUIT = 0;

    int _TM_PACKET = 2;

    // INFORMATION TRANSFER RATE OPTIONS
    int _ITR_PACKET_MODE = 0;

    int _ITR_64 = 16;

    int _ITR_64x2 = 17;

    int _ITR_384 = 19;

    int _ITR_1536 = 21;

    int _ITR_1920 = 23;

    int _ITR_MULTIRATE = 24;

    // SYNC/ASYNC OPTIONS
    int _SA_SYNC = 0;

    int _SA_ASYNC = 1;

    // NEGOTIATION OPTIONS
    int _NG_INBAND_NOT_POSSIBLE = 0;

    int _NG_INBAND_POSSIBLE = 1;

    // USER RATE OPTIONS
    int _UR_EBITS = 0;

    int _UR_0_6 = 1;

    int _UR_1_2 = 2;

    int _UR_2_4 = 3;

    int _UR_3_6 = 4;

    int _UR_4_8 = 5;

    int _UR_7_2 = 6;

    int _UR_8_0 = 7;

    int _UR_9_6 = 8;

    int _UR_14_4 = 9;

    int _UR_16_0 = 10;

    int _UR_19_2 = 11;

    int _UR_32_0 = 12;

    int _UR_38_4 = 13;

    int _UR_48_0 = 14;

    int _UR_56_0 = 15;

    int _UR_57_6 = 18;

    int _UR_28_8 = 19;

    int _UR_24_0 = 20;

    int _UR_0_1345 = 21;

    int _UR_0_1 = 22;

    int _UR_0_075_ON_1_2 = 23;

    int _UR_1_2_ON_0_075 = 24;

    int _UR_0_050 = 25;

    int _UR_0_075 = 26;

    int _UR_0_110 = 27;

    int _UR_0_150 = 28;

    int _UR_0_200 = 29;

    int _UR_0_300 = 30;

    int _UR_12_0 = 31;

    // INTERMEDIATE RATE OPTIONS
    int _IR_NOT_USED = 0;

    int _IR_8_0 = 1;

    int _IR_16_0 = 2;

    int _IR_32_0 = 3;

    // NETWORK INDEPENDENT CLOCK ON TX
    int _NICTX_NOT_REQUIRED = 0;

    int _NICTX_REQUIRED = 1;

    // NETWORK INDEPENDENT CLOCK ON RX
    int _NICRX_CANNOT_ACCEPT = 0;

    int _NICRX_CAN_ACCEPT = 1;

    // FLOW CONTROL ON TX
    int _FCTX_NOT_REQUIRED = 0;

    int _FCTX_REQUIRED = 1;

    // FLOW CONTROL ON RX
    int _FCRX_CANNOT_ACCEPT = 0;

    int _FCRX_CAN_ACCEPT = 1;

    // RATE ADAPTATION HEADER OPTIONS
    int _HDR_INCLUDED = 0;

    int _HDR_NOT_INCLUDED = 1;

    // MULTIFRAME OPTIONS
    int _MF_NOT_SUPPORTED = 0;

    int _MF_SUPPORTED = 1;

    // MODE OPTIONS
    int _MODE_BIT_TRANSPARENT = 0;

    int _MODE_PROTOCOL_SENSITIVE = 1;

    // LOGICAL LINK IDENTIFIER OPTIONS
    int _LLI_256 = 0;

    int _LLI_FULL_NEGOTIATION = 1;

    // ASSIGNOR / ASSIGNEE OPTIONS
    int _ASS_DEFAULT_ASSIGNEE = 0;

    int _ASS_ASSIGNOR_ONLY = 1;

    // INBAND/OUT OF BAND NEGOTIATION OPTIONS
    int _NEG_USER_INFORMATION = 0;

    int _NEG_INBAND = 1;

    // STOP BITS OPTIONS
    int _SB_NOT_USED = 0;

    int _SB_1BIT = 1;

    int _SB_1_5BITS = 2;

    int _SB_2BITS = 3;

    // DATA BITS OPTIONS
    int _DB_NOT_USED = 0;

    int _DB_5BITS = 1;

    int _DB_7BITS = 2;

    int _DB_8BITS = 3;

    // PARITY INFORMATION
    int _PAR_ODD = 0;

    int _PAR_EVEN = 2;

    int _PAR_NONE = 3;

    int _PAR_FORCED_0 = 4;

    int _PAR_FORCED_1 = 5;

    // DUPLEX INFORMATION
    int _DUP_HALF = 0;

    int _DUP_FULL = 1;

    // MODEM TYPE INFORMATION
    int _MODEM_V21 = 17;

    int _MODEM_V22 = 18;

    int _MODEM_V22_BIS = 19;

    int _MODEM_V23 = 20;

    int _MODEM_V26 = 21;

    int _MODEM_V26_BIS = 22;

    int _MODEM_V26_TER = 23;

    int _MODEM_V27 = 24;

    int _MODEM_V27_BIS = 25;

    int _MODEM_V27_TER = 26;

    int _MODEM_V29 = 27;

    int _MODEM_V32 = 29;

    int _MODEM_V34 = 30;

    // LAYER 1 USER INFORMATION OPTIONS
    int _L1_ITUT_110 = 1;

    int _L1_G11_MU = 2;

    int _L1_G711_A = 3;

    int _L1_G721_ADPCM = 4;

    int _L1_G722_G725 = 5;

    int _L1_H261 = 6;

    int _L1_NON_ITUT = 7;

    int _L1_ITUT_120 = 8;

    int _L1_ITUT_X31 = 9;

    // LAYER 2 USER INFORMATION OPTIONS
    int _L2_BASIC = 1;

    int _L2_Q921 = 2;

    int _L2_X25_SLP = 6;

    int _L2_X25_MLP = 7;

    int _L2_T71 = 8;

    int _L2_HDLC_ARM = 9;

    int _L2_HDLC_NRM = 10;

    int _L2_HDLC_ABM = 11;

    int _L2_LAN_LLC = 12;

    int _L2_X75_SLP = 13;

    int _L2_Q922 = 14;

    int _L2_USR_SPEC = 16;

    int _L2_T90 = 17;

    // LAYER 3 USER INFORMATION OPTIONS
    int _L3_Q931 = 2;

    int _L3_T90 = 5;

    int _L3_X25_PLP = 6;

    int _L3_ISO_8208 = 7;

    int _L3_ISO_8348 = 8;

    int _L3_ISO_8473 = 9;

    int _L3_T70 = 10;

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
