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
 * Start time:09:16:42 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.ss7.isup.ISUPMessageFactory;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.ISUPParameterFactoryImpl;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;

/**
 * Start time:09:16:42 2009-04-22<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public abstract class MessageHarness extends TestCase {
	protected ISUPParameterFactory parameterFactory = new ISUPParameterFactoryImpl();
	protected ISUPMessageFactory messageFactory = new ISUPMessageFactoryImpl(parameterFactory);

	// FIXME: add code to check values :)
	protected boolean makeCompare(byte[] b1, byte[] b2) {
		if (b1.length != b2.length)
			return false;

		for (int index = 0; index < b1.length; index++) {
			if (b1[index] != b2[index])
				return false;
		}

		return true;

	}

	protected String makeStringCompare(byte[] b1, byte[] b2) {
		int totalLength = 0;
		if (b1.length >= b2.length) {
			totalLength = b1.length;
		} else {
			totalLength = b2.length;
		}

		String out = "";

		for (int index = 0; index < totalLength; index++) {
			if (b1.length > index) {
				out += "b1[" + Integer.toHexString(b1[index]) + "]";
			} else {
				out += "b1[NOP]";
			}

			if (b2.length > index) {
				out += "b2[" + Integer.toHexString(b2[index]) + "]";
			} else {
				out += "b2[NOP]";
			}
			out += "\n";
		}

		return out;
	}

	protected abstract byte[] getDefaultBody();

	protected abstract ISUPMessage getDefaultMessage();

	public void testOne() throws Exception {

		byte[] defaultBody = getDefaultBody();
		// AddressCompleteMessageImpl acm=new
		// AddressCompleteMessageImpl(this,message);
		AbstractISUPMessage msg = (AbstractISUPMessage) getDefaultMessage();
		msg.decode(defaultBody,parameterFactory);
		byte[] encodedBody = msg.encode();
		boolean equal = Arrays.equals(defaultBody, encodedBody);
		assertTrue(makeStringCompare(defaultBody, encodedBody), equal);
		CircuitIdentificationCode cic = msg.getCircuitIdentificationCode();
		assertNotNull("CircuitIdentificationCode must not be null", cic);
		assertEquals("CircuitIdentificationCode value does not match", cic.getCIC(), getDefaultCIC());

	}

	protected long getDefaultCIC() {
		return 0xB0C;
	}



}
