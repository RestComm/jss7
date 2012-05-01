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
import org.testng.annotations.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class TransitNetworkSelectionTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public TransitNetworkSelectionTest() throws IOException {
		super.badBodies.add(new byte[1]);

	}
	@Test(groups = { "functional.encode","functional.decode","parameter"})
	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterException {
		TransitNetworkSelectionImpl bci = new TransitNetworkSelectionImpl(getBody(false, TransitNetworkSelectionImpl._NIP_PDNIC, TransitNetworkSelectionImpl._TONI_ITU_T, getSixDigits()));

		String[] methodNames = { "isOddFlag", "getNetworkIdentificationPlan", "getTypeOfNetworkIdentification",  "getAddress" };
		Object[] expectedValues = { false, TransitNetworkSelectionImpl._NIP_PDNIC, TransitNetworkSelectionImpl._TONI_ITU_T, getSixDigitsString()};
		super.testValues(bci, methodNames, expectedValues);
	}

	private byte[] getBody(boolean isODD, int _NIP, int _TONI, byte[] digits) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB

		if (isODD) {
			bos.write(0x80 | (_TONI << 4) | _NIP);
		} else {
			bos.write((_TONI << 4) | _NIP);
		}

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
		return new TransitNetworkSelectionImpl("11", 1, 1);
	}

}
