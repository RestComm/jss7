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

/**
 * Start time:14:20:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:14:20:07 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski</a>
 * * @author <a href="mailto:oifa.yulian@gmail.com">Yulian Oifa</a>
 */
public interface UserServiceInformation extends ISUPParameter {
	public static final int _PARAMETER_CODE = 0x1D;
	
	//for parameters list see ITU-T Q.763 (12/1999) 3.57
	//Recommendation Q.931 (05/98) Table 4-6/Q.931 – Bearer capability information element
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
	
	public static final int _L3_USR_SPEC=16;
	
	
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
}
