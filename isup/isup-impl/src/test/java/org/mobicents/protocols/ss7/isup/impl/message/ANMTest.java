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

package org.mobicents.protocols.ss7.isup.impl.message;

import org.mobicents.protocols.ss7.isup.message.AnswerMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CallReference;
import org.mobicents.protocols.ss7.isup.message.parameter.ServiceActivation;

/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * Test for ANM
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ANMTest extends MessageHarness {

	

	
	public void testTwo_Params() throws Exception
	{
		byte[] message = getDefaultBody();

		//AnswerMessageImpl ANM=new AnswerMessageImpl(this,message);
		AnswerMessage ANM=super.messageFactory.createANM();
		((AbstractISUPMessage)ANM).decode(message,parameterFactory);
		try{
			CallReference cr = (CallReference) ANM.getParameter(CallReference._PARAMETER_CODE);
			assertNotNull("Call Reference return is null, it should not be",cr);
			if(cr == null)
				return;
			super.assertEquals("CallIdentity missmatch",65793, cr.getCallIdentity());
			super.assertEquals("SignalingPointCode missmatch",478, cr.getSignalingPointCode());
			
		}catch(Exception e)
		{
			e.printStackTrace();
			super.fail("Failed on get parameter["+CallReference._PARAMETER_CODE+"]:"+e);
		}
		try{
			ServiceActivation sa = (ServiceActivation) ANM.getParameter(ServiceActivation._PARAMETER_CODE);
			assertNotNull("Service Activation return is null, it should not be",sa);
			if(sa == null)
				return;
			
			byte[] b=sa.getFeatureCodes();
			assertNotNull("ServerActivation.getFeatureCodes() is null",b);
			if(b == null)
			{
				return;
			}	
			assertEquals("Length of param is wrong",7 ,b.length);
			if(b.length != 7)
				return;
			assertTrue("Content of ServiceActivation.getFeatureCodes is wrong", super.makeCompare(b, new byte[]{0x01
					,0x02
					,0x03
					,0x04
					,0x05
					,0x06
					,0x07}));
			
		}catch(Exception e)
		{
			e.printStackTrace();
			super.fail("Failed on get parameter["+CallReference._PARAMETER_CODE+"]:"+e);
		}

	}
	
	protected byte[] getDefaultBody() {
		byte[] message={

				0x0C
				,(byte) 0x0B
				,AnswerMessage.MESSAGE_CODE
				//No mandatory varaible part, no ptr
				,0x01 // ptr to optional part
				
				
				//Call reference
				,0x01
				,0x05
				,0x01
				,0x01
				,0x01
				,(byte)0xDE
				,0x01
				//ServiceActivation
				,0x33
				,0x07
				,0x01
				,0x02
				,0x03
				,0x04
				,0x05
				,0x06
				,0x07
				
				
				
				
				//End of optional part
				,0x0

				

		};
		return message;
	}

	
	protected ISUPMessage getDefaultMessage() {
		return super.messageFactory.createANM();
	}
}
