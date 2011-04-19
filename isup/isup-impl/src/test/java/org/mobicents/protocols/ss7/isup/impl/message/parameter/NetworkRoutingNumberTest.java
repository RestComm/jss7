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
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.mobicents.protocols.ss7.isup.ParameterException;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class NetworkRoutingNumberTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public NetworkRoutingNumberTest() throws IOException {
		super.badBodies.add(new byte[1]);

		super.goodBodies.add(getBody(false,getSixDigits(), NetworkRoutingNumberImpl._NPI_ISDN_NP, NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF));
		super.goodBodies.add(getBody(true,getFiveDigits(), NetworkRoutingNumberImpl._NPI_ISDN_NP, NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF));
		// This will fail, cause this body has APRI allowed, so hardcoded body
		// does nto match encoded body :)
		// super.goodBodies.add(getBody2());
	}



	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterException {
		NetworkRoutingNumberImpl bci = new NetworkRoutingNumberImpl(getBody(false,getSixDigits(), NetworkRoutingNumberImpl._NPI_ISDN_NP, NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF));

		String[] methodNames = { "isOddFlag", "getNumberingPlanIndicator", "getNatureOfAddressIndicator",  "getAddress" };
		Object[] expectedValues = { false, NetworkRoutingNumberImpl._NPI_ISDN_NP, NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF,getSixDigitsString() };
		super.testValues(bci, methodNames, expectedValues);
	}
	
	public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterException {
		NetworkRoutingNumberImpl bci = new NetworkRoutingNumberImpl(getBody(true,getFiveDigits(), NetworkRoutingNumberImpl._NPI_ISDN_NP, NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF));

		String[] methodNames = { "isOddFlag", "getNumberingPlanIndicator", "getNatureOfAddressIndicator",  "getAddress" };
		Object[] expectedValues = { true, NetworkRoutingNumberImpl._NPI_ISDN_NP, NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF,getFiveDigitsString() };
		super.testValues(bci, methodNames, expectedValues);
	}

	private byte[] getBody(boolean isODD, byte[] digits, int npiIsdnNp, int naiNrniNetworkSnf) throws IOException {
		int b = 0;
		if(isODD)
		{
			b|=0x01<<7;
		}
		b|=npiIsdnNp<<4;
		b|=naiNrniNetworkSnf;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bos.write(b);
		bos.write(digits);
		return bos.toByteArray();
	}



	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent
	 * ()
	 */
	
	public AbstractISUPParameter getTestedComponent() {
		return new NetworkRoutingNumberImpl("1", 1, 1);
	}

}
