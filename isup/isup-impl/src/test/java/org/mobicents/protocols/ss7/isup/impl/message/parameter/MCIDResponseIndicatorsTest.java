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
public class MCIDResponseIndicatorsTest extends ParameterHarness {
	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;

	public MCIDResponseIndicatorsTest() {
		super();
		super.goodBodies.add(new byte[] { 3 });
		super.badBodies.add(new byte[2]);
	}

	private byte[] getBody(boolean mcidRequest, boolean holdingRequested) {
		int b0 = 0;

		b0 |= (mcidRequest ? _TURN_ON : _TURN_OFF);
		b0 |= ((holdingRequested ? _TURN_ON : _TURN_OFF)) << 1;

		return new byte[] { (byte) b0 };
	}
	@Test(groups = { "functional.encode","functional.decode","parameter"})
	public void testBody1EncodedValues() throws ParameterException {
		MCIDResponseIndicatorsImpl eci = new MCIDResponseIndicatorsImpl(getBody(MCIDResponseIndicatorsImpl._INDICATOR_PROVIDED, MCIDResponseIndicatorsImpl._INDICATOR_NOT_PROVIDED));

		String[] methodNames = { "isMcidIncludedIndicator", "isHoldingProvidedIndicator" };
		Object[] expectedValues = { MCIDResponseIndicatorsImpl._INDICATOR_PROVIDED, MCIDResponseIndicatorsImpl._INDICATOR_NOT_PROVIDED };
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
		return new MCIDResponseIndicatorsImpl(new byte[1]);
	}

}
