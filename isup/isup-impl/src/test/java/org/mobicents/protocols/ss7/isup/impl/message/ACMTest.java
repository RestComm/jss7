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
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import org.junit.Test;
import org.mobicents.protocols.ss7.isup.message.AddressCompleteMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;

/**
 * Start time:09:26:46 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * Test for ACM
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ACMTest extends MessageHarness {

	@Test
	public void testTwo_Params() throws Exception {

		byte[] message = getDefaultBody();

		// AddressCompleteMessageImpl acm=new
		// AddressCompleteMessageImpl(this,message);
		AddressCompleteMessage acm = super.messageFactory.createACM();
		((AbstractISUPMessage)acm).decode(message,parameterFactory);

		assertNotNull("BackwardCallIndicator is null", acm.getBackwardCallIndicators());
		assertNotNull("OptionalBackwardCallIndicator is null", acm.getOptionalBackwardCallIndicators());
		assertNotNull("Cause Indicator is null", acm.getCauseIndicators());

		BackwardCallIndicators bci = acm.getBackwardCallIndicators();
		assertEquals("BackwardCallIndicator charge indicator does not match", bci.getChargeIndicator(), 1);
		assertEquals("BackwardCallIndicator called party status does not match", bci.getCalledPartysStatusIndicator(), 0);
		assertEquals("BackwardCallIndicator called party category does not match", bci.getCalledPartysCategoryIndicator(), 0);
		assertFalse(bci.isInterworkingIndicator());
		assertFalse(bci.isEndToEndInformationIndicator());
		assertFalse(bci.isIsdnAccessIndicator());
		assertFalse(bci.isHoldingIndicator());
		assertTrue(bci.isEchoControlDeviceIndicator());
		assertEquals("BackwardCallIndicator sccp method does not match", bci.getSccpMethodIndicator(), 0);

		CircuitIdentificationCode cic = acm.getCircuitIdentificationCode();
		assertNotNull("CircuitIdentificationCode must not be null", cic);
		assertEquals("CircuitIdentificationCode value does not match", cic.getCIC(), getDefaultCIC());

	}

	
	protected byte[] getDefaultBody() {
		byte[] message = {

		0x0C, (byte) 0x0B, 0x06, 0x01, 0x20, 0x01, 0x29, 0x01, 0x01, 0x12, 0x02, (byte) 0x82, (byte) 0x9C, 0x00
		
		};
		return message;
	}

	
	protected ISUPMessage getDefaultMessage() {
		return super.messageFactory.createACM();
	}
}
