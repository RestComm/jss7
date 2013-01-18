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
	
	//for parameters list see ITU-T Q.763 (12/1999) 3.57
	//Recommendation Q.931 (05/98) Table 4-6/Q.931 Bearer capability information element
	//Dialogic User Service Information structure : http://www.dialogic.com/webhelp/NASignaling/Release%205.1/NA_ISUP_Layer_Dev_Ref_Manual/user_service_information.htm
	
	//LAYER IDENTIFIERS
	public static final int _LAYER1_IDENTIFIER=0x1;
	
	public static final int _LAYER2_IDENTIFIER=0x2;
	
	public static final int _LAYER3_IDENTIFIER=0x0;
	
	//CODING STANDART OPTIONS
	public static final int _CS_CCITT=0;
	
	public static final int _CS_INTERNATIONAL=1;
	
	public static final int _CS_NATIONAL=2;
	
	public static final int _CS_NETWORK=3;
	
	//INFORMATION TRANSFER CAPABILITIES OPTIONS
	public static final int _ITS_SPEECH=0;
	
	public static final int _ITS_UNRESTRICTED_DIGITAL=8;
	
	public static final int _ITS_RESTRICTED_DIGITAL=9;
	
	public static final int _ITS_3_1_KHZ=16;
	
	public static final int _ITS_UNRESTRICTED_DIGITAL_WITH_TONES=17;
	
	public static final int _ITS_VIDEO=24;
	
	//TRANSFER MODE OPTIONS
	public static final int _TM_CIRCUIT=0;
	
	public static final int _TM_PACKET=2;
	
	//INFORMATION TRANSFER RATE OPTIONS
	public static final int _ITR_PACKET_MODE=0;
	
	public static final int _ITR_64=16;
	
	public static final int _ITR_64x2=17;
	
	public static final int _ITR_384=19;
	
	public static final int _ITR_1536=21;
	
	public static final int _ITR_1920=23;
	
	public static final int _ITR_MULTIRATE=24;
	
	//SYNC/ASYNC OPTIONS
	public static final int _SA_SYNC=0;
	
	public static final int _SA_ASYNC=1;
	
	//NEGOTIATION OPTIONS		
	public static final int _NG_INBAND_NOT_POSSIBLE=0;
	
	public static final int _NG_INBAND_POSSIBLE=1;
	
	//USER RATE OPTIONS
	public static final int _UR_EBITS=0;
	
	public static final int _UR_0_6=1;
	
	public static final int _UR_1_2=2;
	
	public static final int _UR_2_4=3;
	
	public static final int _UR_3_6=4;
	
	public static final int _UR_4_8=5;
	
	public static final int _UR_7_2=6;
	
	public static final int _UR_8_0=7;
	
	public static final int _UR_9_6=8;
	
	public static final int _UR_14_4=9;
	
	public static final int _UR_16_0=10;
	
	public static final int _UR_19_2=11;
	
	public static final int _UR_32_0=12;
	
	public static final int _UR_38_4=13;
	
	public static final int _UR_48_0=14;
	
	public static final int _UR_56_0=15;
	
	public static final int _UR_57_6=18;
	
	public static final int _UR_28_8=19;
	
	public static final int _UR_24_0=20;
	
	public static final int _UR_0_1345=21;
	
	public static final int _UR_0_1=22;
	
	public static final int _UR_0_075_ON_1_2=23;
	
	public static final int _UR_1_2_ON_0_075=24;
	
	public static final int _UR_0_050=25;
	
	public static final int _UR_0_075=26;
	
	public static final int _UR_0_110=27;
	
	public static final int _UR_0_150=28;
	
	public static final int _UR_0_200=29;
	
	public static final int _UR_0_300=30;
	
	public static final int _UR_12_0=31;
	
	//INTERMEDIATE RATE OPTIONS
	public static final int _IR_NOT_USED=0;
	
	public static final int _IR_8_0=1;
	
	public static final int _IR_16_0=2;
	
	public static final int _IR_32_0=3;
	
	//NETWORK INDEPENDENT CLOCK ON TX
	public static final int _NICTX_NOT_REQUIRED=0;
	
	public static final int _NICTX_REQUIRED=1;
	
	//NETWORK INDEPENDENT CLOCK ON RX
	public static final int _NICRX_CANNOT_ACCEPT=0;
	
	public static final int _NICRX_CAN_ACCEPT=1;
	
	//FLOW CONTROL ON TX
	public static final int _FCTX_NOT_REQUIRED=0;
	
	public static final int _FCTX_REQUIRED=1;
	
	//FLOW CONTROL ON RX
	public static final int _FCRX_CANNOT_ACCEPT=0;
	
	public static final int _FCRX_CAN_ACCEPT=1;
	
	//RATE ADAPTATION HEADER OPTIONS
	public static final int _HDR_INCLUDED=0;
	
	public static final int _HDR_NOT_INCLUDED=1;
	
	//MULTIFRAME OPTIONS
	public static final int _MF_NOT_SUPPORTED=0;
	
	public static final int _MF_SUPPORTED=1;
	
	//MODE OPTIONS
	public static final int _MODE_BIT_TRANSPARENT=0;
	
	public static final int _MODE_PROTOCOL_SENSITIVE=1;
	
	//LOGICAL LINK IDENTIFIER OPTIONS
	public static final int _LLI_256=0;
	
	public static final int _LLI_FULL_NEGOTIATION=1;
	
	//ASSIGNOR / ASSIGNEE OPTIONS
	public static final int _ASS_DEFAULT_ASSIGNEE=0;
	
	public static final int _ASS_ASSIGNOR_ONLY=1;
	
	//INBAND/OUT OF BAND NEGOTIATION OPTIONS
	public static final int _NEG_USER_INFORMATION=0;
		
	public static final int _NEG_INBAND=1;
		
	//STOP BITS OPTIONS
	public static final int _SB_NOT_USED=0;
	
	public static final int _SB_1BIT=1;
	
	public static final int _SB_1_5BITS=2;
	
	public static final int _SB_2BITS=3;
	
	//DATA BITS OPTIONS
	public static final int _DB_NOT_USED=0;
		
	public static final int _DB_5BITS=1;
		
	public static final int _DB_7BITS=2;
		
	public static final int _DB_8BITS=3;
		
	//PARITY INFORMATION
	public static final int _PAR_ODD=0;
			
	public static final int _PAR_EVEN=2;
			
	public static final int _PAR_NONE=3;
		
	public static final int _PAR_FORCED_0=4;
		
	public static final int _PAR_FORCED_1=5;
	
	//DUPLEX INFORMATION
	public static final int _DUP_HALF=0;
	
	public static final int _DUP_FULL=1;
			
	//MODEM TYPE INFORMATION
	public static final int _MODEM_V21=17;
	
	public static final int _MODEM_V22=18;
	
	public static final int _MODEM_V22_BIS=19;
	
	public static final int _MODEM_V23=20;
	
	public static final int _MODEM_V26=21;
	
	public static final int _MODEM_V26_BIS=22;
	
	public static final int _MODEM_V26_TER=23;
	
	public static final int _MODEM_V27=24;
	
	public static final int _MODEM_V27_BIS=25;
	
	public static final int _MODEM_V27_TER=26;
	
	public static final int _MODEM_V29=27;
	
	public static final int _MODEM_V32=29;
	
	public static final int _MODEM_V34=30;
	
	//LAYER 1 USER INFORMATION OPTIONS
	public static final int _L1_ITUT_110=1;
	
	public static final int _L1_G11_MU=2;
	
	public static final int _L1_G711_A=3;
	
	public static final int _L1_G721_ADPCM=4;
		
	public static final int _L1_G722_G725=5;
	
	public static final int _L1_H261=6;
	
	public static final int _L1_NON_ITUT=7;
	
	public static final int _L1_ITUT_120=8;
	
	public static final int _L1_ITUT_X31=9;
	
	//LAYER 2 USER INFORMATION OPTIONS
	public static final int _L2_BASIC=1;
	
	public static final int _L2_Q921=2;
	
	public static final int _L2_X25_SLP=6;
	
	public static final int _L2_X25_MLP=7;
	
	public static final int _L2_T71=8;
	
	public static final int _L2_HDLC_ARM=9;
	
	public static final int _L2_HDLC_NRM=10;
	
	public static final int _L2_HDLC_ABM=11;
	
	public static final int _L2_LAN_LLC=12;
	
	public static final int _L2_X75_SLP=13;
	
	public static final int _L2_Q922=14;
	
	public static final int _L2_USR_SPEC=16;
	
	public static final int _L2_T90=17;
	
	
	//LAYER 3 USER INFORMATION OPTIONS
	public static final int _L3_Q931=2;
	
	public static final int _L3_T90=5;
	
	public static final int _L3_X25_PLP=6;
	
	public static final int _L3_ISO_8208=7;
	
	public static final int _L3_ISO_8348=8;
	
	public static final int _L3_ISO_8473=9;
	
	public static final int _L3_T70=10;
	
	public static final int _L3_ISO_9577=11;
	
	public static final int _L3_USR_SPEC=16;
	
	
	//LAYER 3 PROTOCOL OPTIONS;
	public static final int _L3_PROT_IP=204;
	
	public static final int _L3_PROT_P2P=207;
	
	public int getCodingStandart();

	public void setCodingStandart(int codingStandart);
	
	public int getInformationTransferCapability();

	public void setInformationTransferCapability(int informationTransferCapability);

	public int getTransferMode();

	public void setTransferMode(int transferMode);
	
	public int getInformationTransferRate();

	public void setInformationTransferRate(int informationTransferRate);
	
	//custom rate in 64Kbps units
	public int getCustomInformationTransferRate();
	
	public void setCustomInformationTransferRate(int informationTransferRate);
	
	//TO CLEAR USER INFORMATION ON EACH LAYER SET IT TO 0
	public int getL1UserInformation();

	public void setL1UserInformation(int l1UserInformation);
	
	public int getL2UserInformation();

	public void setL2UserInformation(int l2UserInformation);
	
	public int getL3UserInformation();

	public void setL3UserInformation(int l3UserInformation);
	
	public int getSyncMode();

	public void setSyncMode(int syncMode);
	
	public int getNegotiation();

	public void setNegotiation(int negotiation);
	
	public int getUserRate();

	public void setUserRate(int userRate);
	
	public int getIntermediateRate();

	public void setIntermediateRate(int intermediateRate);
	
	public int getNicOnTx();

	public void setNicOnTx(int nicOnTx);
	
	public int getNicOnRx();

	public void setNicOnRx(int nicOnRx);
	
	public int getFlowControlOnTx();

	public void setFlowControlOnTx(int fcOnTx);
	
	public int getFlowControlOnRx();

	public void setFlowControlOnRx(int fcOnRx);
	
	public int getHDR();

	public void setHDR(int hdr);
	
	public int getMultiframe();

	public void setMultiframe(int multiframe);
	
	public int getMode();

	public void setMode(int mode);
	
	public int getLLINegotiation();

	public void setLLINegotiation(int lli);
	
	public int getAssignor();

	public void setAssignor(int assignor);
	
	public int getInBandNegotiation();

	public void setInBandNegotiation(int inBandNegotiation);
	
	public int getStopBits();

	public void setStopBits(int stopBits);
	
	public int getDataBits();

	public void setDataBits(int dataBits);
	
	public int getParity();

	public void setParity(int parity);
	
	public int getDuplexMode();

	public void setDuplexMode(int duplexMode);
	
	public int getModemType();

	public void setModemType(int modemType);
	
	public int getL3Protocol();

	public void setL3Protocol(int l3Protocol);

}
