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

import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.GenericNumber;
import org.testng.annotations.Test;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class GenericNumberTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public GenericNumberTest() throws IOException {
		super.badBodies.add(new byte[1]);

//		super.goodBodies.add(getBody(GenericNumberImpl._NQIA_CONNECTED_NUMBER, false, GenericNumber._NAI_NATIONAL_SN, GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN,
//				GenericNumberImpl._APRI_NOT_AVAILABLE, GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED, getSixDigits()));
		super.goodBodies.add(getBody(GenericNumberImpl._NQIA_CONNECTED_NUMBER, false, GenericNumber._NAI_NATIONAL_SN, GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN,
				GenericNumberImpl._APRI_ALLOWED, GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED, getSixDigits()));

	}

	private byte[] getBody(int _NQI, boolean isODD, int _NAI, boolean _NI, int _NPI, int _APR, int _SI, byte[] digits) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB

		int nai = _NAI;
		if (isODD)
			nai |= 0x01 << 7;
		int bit3 = _SI;
		bit3 |= _APR << 2;
		bit3 |= _NPI << 4;
		if (_NI)
			bit3 |= 0x01 << 7;

		bos.write(_NQI);
		bos.write(nai);
		bos.write(bit3);
		bos.write(digits);
		return bos.toByteArray();
	}
	
	@Test(groups = { "functional.encode","functional.decode","parameter"})
	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterException {
//		GenericNumberImpl bci = new GenericNumberImpl(getBody(GenericNumberImpl._NQIA_CONNECTED_NUMBER, false, GenericNumber._NAI_NATIONAL_SN, GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN,
//				GenericNumberImpl._APRI_NOT_AVAILABLE, GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED, getSixDigits()));
		GenericNumberImpl bci = new GenericNumberImpl(getBody(GenericNumberImpl._NQIA_CONNECTED_NUMBER, false, GenericNumber._NAI_NATIONAL_SN, GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN,
				GenericNumberImpl._APRI_ALLOWED, GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED, getSixDigits()));

		String[] methodNames = { "getNumberQualifierIndicator", "getNatureOfAddressIndicator", "isNumberIncomplete", "getNumberingPlanIndicator", "getAddressRepresentationRestrictedIndicator",
				"getScreeningIndicator", "getAddress" };
//		Object[] expectedValues = { GenericNumberImpl._NQIA_CONNECTED_NUMBER, GenericNumber._NAI_NATIONAL_SN, GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN, GenericNumberImpl._APRI_NOT_AVAILABLE,
//				GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED, getSixDigitsString() };
		Object[] expectedValues = { GenericNumberImpl._NQIA_CONNECTED_NUMBER, GenericNumber._NAI_NATIONAL_SN, GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN, GenericNumberImpl._APRI_ALLOWED,
				GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED, getSixDigitsString() };
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
//		return new GenericNumberImpl(0, "1", 1, 1, 1, false, 1);
		return new GenericNumberImpl(GenericNumber._NAI_NATIONAL_SN, getSixDigitsString(), GenericNumberImpl._NQIA_CONNECTED_NUMBER,
				GenericNumberImpl._NPI_ISDN, GenericNumberImpl._APRI_ALLOWED, false, GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED);
//		int natureOfAddresIndicator, String address, int numberQualifierIndicator, int numberingPlanIndicator, int addressRepresentationREstrictedIndicator,
//		boolean numberIncomplete, int screeningIndicator
	}

}
