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
 * Start time:20:07:45 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.mobicents.protocols.ss7.isup.ParameterException;

/**
 * Start time:20:07:45 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class RedirectionInformationTest extends ParameterHarness {

	
	
	
	
	public RedirectionInformationTest() {
		super();
		super.goodBodies.add(new byte[]{(byte) 0xC5,(byte) 0x03});
		
		
		
		super.badBodies.add(new byte[]{(byte) 0xC5,(byte) 0x0F});
		super.badBodies.add(new byte[1]);
		super.badBodies.add(new byte[3]);
	}

	
	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterException {
		RedirectionInformationImpl bci = new RedirectionInformationImpl(getBody(RedirectionInformationImpl._RI_CALL_D_RNPR,RedirectionInformationImpl._ORR_UNA,1,RedirectionInformationImpl._RR_DEFLECTION_IE));
	
		String[] methodNames = { "getRedirectingIndicator", 
								 "getOriginalRedirectionReason", 
								 "getRedirectionCounter", 
								 "getRedirectionReason" };
		Object[] expectedValues = { RedirectionInformationImpl._RI_CALL_D_RNPR,
								    RedirectionInformationImpl._ORR_UNA,
								    1,
								    RedirectionInformationImpl._RR_DEFLECTION_IE };
		super.testValues(bci, methodNames, expectedValues);
	}
	
	
	
	private byte[] getBody(int riCallDRnpr, int orrUna, int counter, int rrDeflectionIe) {
		byte[] b = new byte[2];
		
		b[0] = (byte) riCallDRnpr;
		b[0]|= orrUna <<4;
		
		
		b[1] = (byte) counter;
		b[1]|= rrDeflectionIe <<4;
		return b;
	}


	/* (non-Javadoc)
	 * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent()
	 */
	
	public AbstractISUPParameter getTestedComponent() throws IllegalArgumentException, ParameterException {
		return new RedirectionInformationImpl(new byte[]{0,1});
	}

}
