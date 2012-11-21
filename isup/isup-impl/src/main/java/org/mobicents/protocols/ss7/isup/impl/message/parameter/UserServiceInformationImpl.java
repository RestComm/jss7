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
 * Start time:12:36:18 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski</a>
 * @author <a href="mailto:oifa.yulian@gmail.com">Yulian Oifa</a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.UserServiceInformation;

/**
 * Start time:12:36:18 2009-04-04<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski</a>
 * @author <a href="mailto:oifa.yulian@gmail.com"> Yulian Oifa</a>
 */
public class UserServiceInformationImpl extends AbstractISUPParameter implements UserServiceInformation{

	private int codingStandart = 0;
	private int informationTransferCapability = 0;
	private int transferMode = 0;
	private int customInformationTransferRate = 0;
	private int informationTransferRate = 0;
	private int l1UserInformation = 0;
	private int l2UserInformation = 0;
	private int l3UserInformation = 0;	

	private int syncMode=0;
	private int negotiation=0;
	private int userRate=0;
	private int intermediateRate=0;
	private int nicOnTx=0;
	private int nicOnRx=0;
	private int fcOnTx=0;
	private int fcOnRx=0;
	private int hdr=0;
	private int multiframe=0;
	private int mode=0;
	private int lli=0;
	private int assignor=0;
	private int inBandNegotiation=0;
	private int stopBits=0;
	private int dataBits=0;
	private int parity=0;
	private int duplexMode=0;
	private int modemType=0;
	private int l3Protocol=0;
	
	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length < 2 || b.length>13) {
			throw new IllegalArgumentException("byte[] must not be null and should be between 2 and 13 bytes in length");
		}
		
		int v = 0;
		int index=0;
		int tempValue=0;
		
		//byte 0 bit 1-5 information transfer capability , 6-7 coding standart
		v = b[index++];		
		this.informationTransferCapability=v & 0x1F;		
		this.codingStandart = (v >> 5) & 0x03;
		
		//byte 1 bit 1-5 information transfer rate , 6-7 transfer mode
		v = b[index++];
		this.informationTransferRate=v & 0x1F;		
		this.transferMode = (v >> 5) & 0x03;
		
		if(this.informationTransferRate==_ITR_MULTIRATE)
		{
			if(b.length<3)
				throw new IllegalArgumentException("byte[] should be at least 3 bytes in length");
			
			v = b[index++];
			this.customInformationTransferRate=v;
		}
		
		while(index<b.length)
		{
			//byte 3-5 l1-l3 user information
			v = b[index++];
			tempValue=(v >> 5) & 0x03;
			switch(tempValue)
			{
				case _LAYER1_IDENTIFIER:
					this.l1UserInformation=v & 0x1F;
					//check for bytes 5a to 5d depending on l1 information
					
					//note 2 This octet may be present if octet 3 indicates unrestricted digital information and octet 5 indicates either
					//of the ITU-T standardized rate adaptions V.110, I.460 and X.30 or V.120 [9]. It may also be present if octet 3
					//indicates 3.1 kHz audio and octet 5 indicates G.711.
					
					if(this.informationTransferCapability==_ITS_UNRESTRICTED_DIGITAL)
					{
						switch(this.l1UserInformation)
						{
							case _L1_ITUT_110:
							case _L1_NON_ITUT:
								//should have 5a
								v = b[index++];
								this.syncMode=(v >> 6) & 0x01;
								this.negotiation=(v >> 5) & 0x01;
								this.userRate=v & 0x1F;
							
								//NOTE 7 Octets 5b-5d may be omitted in the case of synchronous user rates.
								if(this.syncMode==_SA_SYNC)
								{
									//NOTE 3 This structure of octet 5b only applies if octet 5 indicates ITU-T standardized rate adaption (see
									//Recommendations V.110 [7], I.460 [15] and X.30 [8]).								
									v = b[index++];
									this.intermediateRate= (v >> 5) & 0x3;
									this.nicOnTx= (v >> 4) & 0x1;
									this.nicOnRx= (v >> 3) & 0x1;
									this.fcOnTx= (v >> 2) & 0x1;
									this.fcOnRx= (v >> 1) & 0x1;
								
									//5c
									v = b[index++];
									this.stopBits= (v>>5) & 0x03;
									this.dataBits= (v>>3) & 0x03;
									this.parity= v & 0x07;
								
									//5d
									v = b[index++];
									this.duplexMode= (v>>6) & 0x1;
									this.modemType= v & 0x1F;
								}
								break;															
							case _L1_ITUT_120:
								//should have 5a								
								v = b[index++];
								this.syncMode=(v >> 6) & 0x01;
								this.negotiation=(v >> 5) & 0x01;
								this.userRate=v & 0x1F;
								
								if(this.syncMode==_SA_SYNC)
								{																	
									//NOTE 4 This structure of octet 5b only applies if octet 5 indicates ITU-T standardized rate adaption (see
									//Recommendation V.120 [9]).								
									v = b[index++];
									this.hdr= (v >> 6) & 0x01;
									this.multiframe= (v >> 5) & 0x01;
									this.mode= (v >> 4) & 0x01;
									this.lli= (v >> 3) & 0x01;
									this.assignor= (v >> 2) & 0x01;
									this.inBandNegotiation= (v >> 1) & 0x01;
								
									//5c
									v = b[index++];
									this.stopBits= (v>>5) & 0x03;
									this.dataBits= (v>>3) & 0x03;
									this.parity= v & 0x07;
								
									//5d
									v = b[index++];
									this.duplexMode= (v>>6) & 0x1;
									this.modemType= v & 0x1F;
								}
								break;
						}												
					}
					else if(this.informationTransferCapability==_ITS_3_1_KHZ && this.transferMode==_TM_PACKET)
					{
						switch(this.l1UserInformation)
						{
							case _L1_G11_MU:
							case _L1_G711_A:
								//read 5a
								v = b[index++];
								this.syncMode=(v >> 6) & 0x01;
								this.negotiation=(v >> 5) & 0x01;
								this.userRate=v & 0x1F;
								
								//5c
								v = b[index++];
								this.stopBits= (v>>5) & 0x03;
								this.dataBits= (v>>3) & 0x03;
								this.parity= v & 0x07;
								
								//5d
								v = b[index++];
								this.duplexMode= (v>>6) & 0x1;
								this.modemType= v & 0x1F;
								break;
						}
					}
					break;
				case _LAYER2_IDENTIFIER:
					this.l2UserInformation=v & 0x1F;
					break;
				case _LAYER3_IDENTIFIER:
					this.l3UserInformation=v & 0x1F;
					//NOTE 5 This octet may be included if octet 7 indicates ISO/IEC TR 9577 (Protocol Identification in the network
					//layer).
					if(this.l3UserInformation==_L3_ISO_9577)
					{
						//check 2 next bytes
						v = b[index++];
						this.l3Protocol=(v & 0x0F)<<4;
						v = b[index++];
						this.l3Protocol |= v & 0x0F;
					}
					break;
				default:
					throw new IllegalArgumentException("invalid layer identifier");
			}					
		}
		
		return 0;
	}

	public byte[] encode() throws ParameterException {
		int byteLength=2;
		if(this.transferMode==_ITR_MULTIRATE)
			byteLength++;
		
		if(this.l1UserInformation>0)
		{
			byteLength++;
			switch(this.l1UserInformation)
			{
				case _L1_ITUT_110:
				case _L1_NON_ITUT:
				case _L1_ITUT_120:
					if(this.informationTransferCapability==_ITS_UNRESTRICTED_DIGITAL)
					{
						byteLength++;
						if(this.syncMode==_SA_SYNC)
							byteLength+=3;	
					}
					break;
				case _L1_G11_MU:
				case _L1_G711_A:
					if(this.informationTransferCapability==_ITS_3_1_KHZ && this.transferMode==_TM_PACKET)
						byteLength+=3;	
					break;
			}
		}
		
		if(this.l2UserInformation>0)
			byteLength++;
		
		if(this.l3UserInformation>0)
		{
			byteLength++;
			if(this.l3UserInformation==_L3_ISO_9577)
				byteLength+=2;
		}
		
		byte[] b = new byte[byteLength];

		b[0] |= 0x80;
		b[0] |= (this.codingStandart & 0x3) << 5;
		b[0] |=	(informationTransferCapability & 0x1f);
				
		b[1] |= 0x80;
		b[1] |= (this.transferMode & 0x3) << 5;
		b[1] |=	(informationTransferRate & 0x1f);
		
		byteLength=2;
		if(this.transferMode==_ITR_MULTIRATE)
		{
			b[byteLength] |= 0x80;
			b[byteLength++] |= customInformationTransferRate;
		}
		
		if(this.l1UserInformation>0)
		{
			b[byteLength] |= _LAYER1_IDENTIFIER << 5;
			b[byteLength++] |= l1UserInformation & 0x1f;
			
			switch(this.l1UserInformation)
			{
				case _L1_ITUT_110:
				case _L1_NON_ITUT:
					if(this.informationTransferCapability==_ITS_UNRESTRICTED_DIGITAL)
					{
						b[byteLength] |= this.syncMode << 6;						
						b[byteLength] |= this.negotiation << 5;
						b[byteLength++] |= this.userRate;
						
						if(this.syncMode==_SA_SYNC)
						{
							//5b
							b[byteLength] |= this.intermediateRate << 5;
							b[byteLength] |= this.nicOnTx << 4;
							b[byteLength] |= this.nicOnRx << 3;
							b[byteLength] |= this.fcOnTx << 2;
							b[byteLength++] |= this.fcOnRx << 1;
							
							//5c
							b[byteLength] |= this.stopBits << 5;
							b[byteLength] |= this.dataBits << 3;
							b[byteLength++] |= this.parity;
							
							//5d
							b[byteLength] |= 0x80;
							b[byteLength] |= this.duplexMode << 6;
							b[byteLength++] |= this.modemType;																					
						}	
					}
					break;
				case _L1_ITUT_120:
					if(this.informationTransferCapability==_ITS_UNRESTRICTED_DIGITAL)
					{
						b[byteLength] |= this.syncMode << 6;						
						b[byteLength] |= this.negotiation << 5;
						b[byteLength++] |= this.userRate;
						
						if(this.syncMode==_SA_SYNC)
						{
							//5b
							b[byteLength] |= this.hdr << 6;
							b[byteLength] |= this.multiframe << 5;
							b[byteLength] |= this.mode << 4;
							b[byteLength] |= this.lli << 3;
							b[byteLength] |= this.assignor << 3;
							b[byteLength++] |= this.inBandNegotiation << 1;
							
							//5c
							b[byteLength] |= this.stopBits << 5;
							b[byteLength] |= this.dataBits << 3;
							b[byteLength++] |= this.parity;
							
							//5d
							b[byteLength] |= 0x80;
							b[byteLength] |= this.duplexMode << 6;
							b[byteLength++] |= this.modemType;																					
						}	
					}
					break;
				case _L1_G11_MU:
				case _L1_G711_A:
					if(this.informationTransferCapability==_ITS_3_1_KHZ && this.transferMode==_TM_PACKET)
					{
						//read 5a
						b[byteLength] |= this.syncMode << 6;
						b[byteLength] |= this.negotiation << 5;
						b[byteLength++] |= this.userRate;
						
						//5c
						b[byteLength] |= this.stopBits << 5;
						b[byteLength] |= this.dataBits << 3;
						b[byteLength++] |= this.parity;
						
						//5d
						b[byteLength] |= 0x80;
						b[byteLength] |= this.duplexMode << 6;
						b[byteLength++] |= this.modemType;		
					}						
					break;
			}
		}
		
		if(this.l2UserInformation>0)
		{
			b[byteLength] |= 0x80;
			b[byteLength] |= _LAYER2_IDENTIFIER << 5;
			b[byteLength++] |= l2UserInformation & 0x1f;
		}
		
		if(this.l3UserInformation>0)
		{			
			b[byteLength] |= _LAYER3_IDENTIFIER << 5;
			b[byteLength++] |= l3UserInformation & 0x1f;
			
			if(this.l3UserInformation==_L3_ISO_9577)
			{
				b[byteLength] |= 0x80;
				b[byteLength++] |=(this.l3Protocol>>4) & 0x0F;
				
				b[byteLength++] |= this.l3Protocol & 0x0F;
			}	
		}
		
		return b;
	}
	
	public int getCode() {

		return _PARAMETER_CODE;
	}

	public UserServiceInformationImpl() {
		super();
		
	}
	public UserServiceInformationImpl(byte[] b) throws ParameterException {
		super();
		this.decode(b);
	}
	
	public int getCodingStandart()
	{
		return this.codingStandart;
	}

	public void setCodingStandart(int codingStandart)
	{
		this.codingStandart=codingStandart;
	}
	
	public int getInformationTransferCapability()
	{
		return this.informationTransferCapability;
	}

	public void setInformationTransferCapability(int informationTransferCapability)
	{
		this.informationTransferCapability=informationTransferCapability;
	}

	public int getTransferMode()
	{
		return this.transferMode;
	}

	public void setTransferMode(int transferMode)
	{
		this.transferMode=transferMode;
	}
	
	public int getInformationTransferRate()
	{
		return this.informationTransferRate;
	}

	public void setInformationTransferRate(int informationTransferRate)
	{
		this.informationTransferRate=informationTransferRate;
	}
	
	public int getCustomInformationTransferRate()
	{
		return this.customInformationTransferRate;
	}

	public void setCustomInformationTransferRate(int customInformationTransferRate)
	{
		this.customInformationTransferRate=customInformationTransferRate;
	}
	
	public int getL1UserInformation()
	{
		return this.l1UserInformation;
	}

	public void setL1UserInformation(int l1UserInformation)
	{
		this.l1UserInformation=l1UserInformation;
	}
	
	public int getL2UserInformation()
	{
		return this.l2UserInformation;
	}

	public void setL2UserInformation(int l2UserInformation)
	{
		this.l2UserInformation=l2UserInformation;
	}
	
	public int getL3UserInformation()
	{
		return this.l3UserInformation;
	}

	public void setL3UserInformation(int l3UserInformation)
	{
		this.l3UserInformation=l3UserInformation;
	}
	
	public int getSyncMode()
	{
		return this.syncMode;
	}

	public void setSyncMode(int syncMode)
	{
		this.syncMode=syncMode;
	}
	
	public int getNegotiation()
	{
		return this.negotiation;
	}

	public void setNegotiation(int negotiation)
	{
		this.negotiation=negotiation;
	}
	
	public int getUserRate()
	{
		return this.userRate;
	}

	public void setUserRate(int userRate)
	{
		this.userRate=userRate;
	}
	
	public int getIntermediateRate()
	{
		return this.intermediateRate;
	}

	public void setIntermediateRate(int intermediateRate)
	{
		this.intermediateRate=intermediateRate;
	}
	
	public int getNicOnTx()
	{
		return this.nicOnTx;
	}

	public void setNicOnTx(int nicOnTx)
	{
		this.nicOnTx=nicOnTx;
	}
	
	public int getNicOnRx()
	{
		return this.nicOnRx;
	}

	public void setNicOnRx(int nicOnRx)
	{
		this.nicOnRx=nicOnRx;
	}
	
	public int getFlowControlOnTx()
	{
		return this.fcOnTx;
	}

	public void setFlowControlOnTx(int fcOnTx)
	{
		this.fcOnTx=fcOnTx;
	}
	
	public int getFlowControlOnRx()
	{
		return this.fcOnRx;
	}

	public void setFlowControlOnRx(int fcOnRx)
	{
		this.fcOnRx=fcOnRx;
	}
	
	public int getHDR()
	{
		return this.hdr;
	}

	public void setHDR(int hdr)
	{
		this.hdr=hdr;
	}
	
	public int getMultiframe()
	{
		return this.multiframe;
	}

	public void setMultiframe(int multiframe)
	{
		this.multiframe=multiframe;
	}
	
	public int getMode()
	{
		return this.mode;
	}

	public void setMode(int mode)
	{
		this.mode=mode;
	}
	
	public int getLLINegotiation()
	{
		return this.lli;
	}

	public void setLLINegotiation(int lli)
	{
		this.lli=lli;
	}
	
	public int getAssignor()
	{
		return this.assignor;
	}

	public void setAssignor(int assignor)
	{
		this.assignor=assignor;
	}
	
	public int getInBandNegotiation()
	{
		return this.inBandNegotiation;
	}

	public void setInBandNegotiation(int inBandNegotiation)
	{
		this.inBandNegotiation=inBandNegotiation;
	}
	
	public int getStopBits()
	{
		return this.stopBits;
	}

	public void setStopBits(int stopBits)
	{
		this.stopBits=stopBits;
	}
	
	public int getDataBits()
	{
		return this.dataBits;
	}

	public void setDataBits(int dataBits)
	{
		this.dataBits=dataBits;
	}
	
	public int getParity()
	{
		return this.parity;
	}

	public void setParity(int parity)
	{
		this.parity=parity;
	}
	
	public int getDuplexMode()
	{
		return this.duplexMode;
	}

	public void setDuplexMode(int duplexMode)
	{
		this.duplexMode=duplexMode;
	}
	
	public int getModemType()
	{
		return this.modemType;
	}

	public void setModemType(int modemType)
	{
		this.modemType=modemType;
	}
	
	public int getL3Protocol()
	{
		return this.l3Protocol;
	}

	public void setL3Protocol(int l3Protocol)
	{
		this.l3Protocol=l3Protocol;
	}
}
