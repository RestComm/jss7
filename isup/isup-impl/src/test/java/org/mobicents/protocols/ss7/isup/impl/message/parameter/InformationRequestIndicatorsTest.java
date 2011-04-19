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

/**
 * Start time:11:34:01 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class InformationRequestIndicatorsTest extends ParameterHarness {
	private final static int _TURN_ON = 1;
	private final static int _TURN_OFF = 0;

	public InformationRequestIndicatorsTest() {
		super();
		//super.goodBodies.add(new byte[] { 67, 12 });
		//super.badBodies.add(new byte[3]);
		//super.badBodies.add(new byte[1]);
	}

	private byte[] getBody(boolean callingPartAddressRequestIndicator, boolean holdingIndicator, boolean callingpartysCategoryRequestIndicator, boolean chargeInformationRequestIndicator,
			boolean maliciousCallIdentificationRequestIndicator, int reserved) {

		int b0 = 0;
		int b1 = 0;
		b0 |= callingPartAddressRequestIndicator ? _TURN_ON : _TURN_OFF;
		b0 |= (holdingIndicator ? _TURN_ON : _TURN_OFF) << 1;
		b0 |= (callingpartysCategoryRequestIndicator ? _TURN_ON : _TURN_OFF) << 3;
		b0 |= (chargeInformationRequestIndicator ? _TURN_ON : _TURN_OFF) << 4;
		b0 |= (maliciousCallIdentificationRequestIndicator ? _TURN_ON : _TURN_OFF) << 7;

		b1 |= (reserved & 0x0F) << 4;

		return new byte[] { (byte) b0, (byte) b1 };

	}

	public void testBody1EncodedValues() throws ParameterException {
		InformationRequestIndicatorsImpl eci = new InformationRequestIndicatorsImpl(getBody(InformationRequestIndicatorsImpl._INDICATOR_REQUESTED, InformationRequestIndicatorsImpl._INDICATOR_NOT_REQUESTED,
				InformationRequestIndicatorsImpl._INDICATOR_REQUESTED, InformationRequestIndicatorsImpl._INDICATOR_NOT_REQUESTED, InformationRequestIndicatorsImpl._INDICATOR_NOT_REQUESTED, 10));

		String[] methodNames = { "isCallingPartAddressRequestIndicator", "isHoldingIndicator", "isCallingpartysCategoryRequestIndicator", "isChargeInformationRequestIndicator",
				"isMaliciousCallIdentificationRequestIndicator", "getReserved" };
		Object[] expectedValues = { InformationRequestIndicatorsImpl._INDICATOR_REQUESTED, InformationRequestIndicatorsImpl._INDICATOR_NOT_REQUESTED, InformationRequestIndicatorsImpl._INDICATOR_REQUESTED,
				InformationRequestIndicatorsImpl._INDICATOR_NOT_REQUESTED, InformationRequestIndicatorsImpl._INDICATOR_NOT_REQUESTED, 10 };
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
		return new InformationRequestIndicatorsImpl(new byte[2]);
	}

}
