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
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectedNumber;
import org.testng.annotations.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class ConnectedNumberTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public ConnectedNumberTest() throws IOException {
		super.badBodies.add(new byte[1]);

		super.goodBodies.add(getBody1());
		// This will fail, cause this body has APRI allowed, so hardcoded body
		// does nto match encoded body :)
		// super.goodBodies.add(getBody2());
	}

	private byte[] getBody1() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB
		int v = ConnectedNumber._NAI_SUBSCRIBER_NUMBER;
		bos.write(v);
		v = 0;
		v = ConnectedNumberImpl._NPI_TELEX << 4;
		v |= ConnectedNumberImpl._APRI_NOT_AVAILABLE << 2;
		v |= ConnectedNumberImpl._SI_NETWORK_PROVIDED;
		bos.write(v & 0x7F);

		bos.write(super.getSixDigits());
		return bos.toByteArray();
	}

	private byte[] getBody2() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		// We have odd number
		int v = ConnectedNumber._NAI_SUBSCRIBER_NUMBER | (0x01 << 7);
		bos.write(v);
		v = 0;
		v = ConnectedNumberImpl._NPI_TELEX << 4;
		v |= ConnectedNumberImpl._APRI_ALLOWED << 2;
		v |= ConnectedNumberImpl._SI_NETWORK_PROVIDED;
		bos.write(v & 0x7F);
		bos.write(super.getFiveDigits());
		return bos.toByteArray();
	}
	@Test(groups = { "functional.encode","functional.decode","parameter"})
	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterException {
		ConnectedNumberImpl bci = new ConnectedNumberImpl(getBody1());

		String[] methodNames = { "getNumberingPlanIndicator", "getAddressRepresentationRestrictedIndicator", "getNatureOfAddressIndicator", "getScreeningIndicator", "isOddFlag", "getAddress" };
		Object[] expectedValues = { ConnectedNumberImpl._NPI_TELEX, ConnectedNumberImpl._APRI_NOT_AVAILABLE, ConnectedNumber._NAI_SUBSCRIBER_NUMBER, ConnectedNumberImpl._SI_NETWORK_PROVIDED, false,
				super.getSixDigitsString() };
		super.testValues(bci, methodNames, expectedValues);
	}
	@Test(groups = { "functional.encode","functional.decode","parameter"})
	public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterException {
		ConnectedNumberImpl bci = new ConnectedNumberImpl(getBody2());
		
		String[] methodNames = { "getNumberingPlanIndicator", "getAddressRepresentationRestrictedIndicator", "getNatureOfAddressIndicator", "getScreeningIndicator", "isOddFlag", "getAddress" };
		Object[] expectedValues = { ConnectedNumberImpl._NPI_TELEX, ConnectedNumberImpl._APRI_ALLOWED, ConnectedNumber._NAI_SUBSCRIBER_NUMBER, ConnectedNumberImpl._SI_NETWORK_PROVIDED, true, super.getFiveDigitsString() };
		super.testValues(bci, methodNames, expectedValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent
	 * ()
	 */
	
	public AbstractISUPParameter getTestedComponent() {
		return new ConnectedNumberImpl(1, "1", 1, 1, 1);
	}

}
