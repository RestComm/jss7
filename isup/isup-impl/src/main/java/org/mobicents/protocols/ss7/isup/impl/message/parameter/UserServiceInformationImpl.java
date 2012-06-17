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
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
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

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length < 2 || b.length>6) {
			throw new IllegalArgumentException("byte[] must not be null and should be between 2 and 6 bytes in length");
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
					break;
				case _LAYER2_IDENTIFIER:
					this.l3UserInformation=v & 0x1F;
					break;
				case _LAYER3_IDENTIFIER:
					this.l2UserInformation=v & 0x1F;
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
			byteLength++;
		
		if(this.l2UserInformation>0)
			byteLength++;
		
		if(this.l3UserInformation>0)
			byteLength++;
		
		byte[] b = new byte[byteLength];

		b[0] |= (this.codingStandart & 0x3) << 5;
		b[0] |=	(informationTransferCapability & 0x1f);
				
		b[1] |= (this.transferMode & 0x3) << 5;
		b[1] |=	(informationTransferRate & 0x1f);
		
		byteLength=2;
		if(this.transferMode==_ITR_MULTIRATE)
			b[byteLength++] |= customInformationTransferRate;
		
		if(this.l1UserInformation>0)
		{
			b[byteLength] |= _LAYER1_IDENTIFIER << 5;
			b[byteLength++] |= l1UserInformation & 0x1f;
		}
		
		if(this.l2UserInformation>0)
		{
			b[byteLength] |= _LAYER2_IDENTIFIER << 5;
			b[byteLength++] |= l2UserInformation & 0x1f;
		}
		
		if(this.l3UserInformation>0)
		{
			b[byteLength] |= _LAYER3_IDENTIFIER << 5;
			b[byteLength++] |= l3UserInformation & 0x1f;
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
}
