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
import org.mobicents.protocols.ss7.isup.message.parameter.CalledINNumber;
import org.testng.annotations.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class CalledINNumberTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public CalledINNumberTest() throws IOException {
		super.badBodies.add(new byte[1]);
		

		super.goodBodies.add(getBody1());
		super.goodBodies.add(getBody2());
	}

	private byte[] getBody1() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB

		bos.write(CalledINNumber._NAI_SUBSCRIBER_NUMBER);
		int v = CalledINNumberImpl._APRI_ALLOWED << 2;
		v |= CalledINNumberImpl._NPI_ISDN << 4;
		bos.write(v);
		bos.write(super.getSixDigits());
		return bos.toByteArray();
	}

	private byte[] getBody2() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		bos.write(CalledINNumber._NAI_SUBSCRIBER_NUMBER | (0x01 << 7));
		int v = CalledINNumberImpl._APRI_ALLOWED << 2;
		v |= CalledINNumberImpl._NPI_ISDN << 4;
		bos.write(v);
		bos.write(super.getFiveDigits());
		return bos.toByteArray();
	}
	@Test(groups = { "functional.encode","functional.decode","parameter"})
	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterException {
		CalledINNumberImpl bci = new CalledINNumberImpl(getBody1());
	
		String[] methodNames = { "getNumberingPlanIndicator", "getAddressRepresentationRestrictedIndicator", "getNatureOfAddressIndicator", "isOddFlag", "getAddress" };
		Object[] expectedValues = { CalledINNumberImpl._NPI_ISDN, CalledINNumberImpl._APRI_ALLOWED, CalledINNumber._NAI_SUBSCRIBER_NUMBER, false, super.getSixDigitsString() };
		super.testValues(bci, methodNames, expectedValues);
	}
	@Test(groups = { "functional.encode","functional.decode","parameter"})
	public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterException {
		CalledINNumberImpl bci = new CalledINNumberImpl(getBody2());

		String[] methodNames = { "getNumberingPlanIndicator", "getAddressRepresentationRestrictedIndicator", "getNatureOfAddressIndicator", "isOddFlag", "getAddress" };
		Object[] expectedValues = { CalledINNumberImpl._NPI_ISDN, CalledINNumberImpl._APRI_ALLOWED, CalledINNumber._NAI_SUBSCRIBER_NUMBER, true, super.getFiveDigitsString() };
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
		return new CalledINNumberImpl(0, "1", 1, 1);
	}

}
