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
 * Start time:11:34:01 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.testng.annotations.Test;

/**
 * Start time:11:34:01 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class EchoControlInformationTest extends ParameterHarness {

	public EchoControlInformationTest() {
		super();
		super.goodBodies.add(new byte[] { 67 });
		super.badBodies.add(new byte[2]);
	}

	private byte[] getBody(int _OUT_E_CDII, int _IN_E_CDII, int _IN_E_CDRI, int _OUT_E_CDRI) {
		byte[] b = new byte[1];
		int v = _OUT_E_CDII;
		v |= _IN_E_CDII << 2;
		v |= _OUT_E_CDRI << 4;
		v |= _IN_E_CDRI << 6;
		b[0] = (byte) v;

		return b;
	}
	@Test(groups = { "functional.encode","functional.decode","parameter"})
	public void testBody1EncodedValues() throws ParameterException {
		EchoControlInformationImpl eci = new EchoControlInformationImpl(getBody(EchoControlInformationImpl._OUTGOING_ECHO_CDII_NINA, EchoControlInformationImpl._INCOMING_ECHO_CDII_INCLUDED,
				EchoControlInformationImpl._INCOMING_ECHO_CDRI_AR, EchoControlInformationImpl._OUTGOING_ECHO_CDRI_NOINFO));

		String[] methodNames = { "getOutgoingEchoControlDeviceInformationIndicator", "getIncomingEchoControlDeviceInformationIndicator", "getIncomingEchoControlDeviceInformationRequestIndicator",
				"getOutgoingEchoControlDeviceInformationRequestIndicator" };
		Object[] expectedValues = { EchoControlInformationImpl._OUTGOING_ECHO_CDII_NINA, EchoControlInformationImpl._INCOMING_ECHO_CDII_INCLUDED, EchoControlInformationImpl._INCOMING_ECHO_CDRI_AR,
				EchoControlInformationImpl._OUTGOING_ECHO_CDRI_NOINFO };
		super.testValues(eci, methodNames, expectedValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent
	 * ()
	 */
	
	public AbstractISUPParameter getTestedComponent() throws ParameterException {
		return new EchoControlInformationImpl(new byte[1]);
	}

}
