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
 * Start time:11:36:27 2009-04-27<br>
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
 * Start time:11:36:27 2009-04-27<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class UserTeleserviceInformationTest extends ParameterHarness {

	public UserTeleserviceInformationTest() {
		super();
		super.goodBodies.add(getBody(UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP, UserTeleserviceInformationImpl._INTERPRETATION_FHGCI, UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
				UserTeleserviceInformationImpl._HLCI_IVTI));
		super.goodBodies.add(getBody(UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP, UserTeleserviceInformationImpl._INTERPRETATION_FHGCI, UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
				UserTeleserviceInformationImpl._HLCI_AUDIO_VID_LOW_RANGE));
		super.goodBodies.add(getBody(UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP, UserTeleserviceInformationImpl._INTERPRETATION_FHGCI, UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
				UserTeleserviceInformationImpl._HLCI_AUDIO_VID_LOW_RANGE, UserTeleserviceInformationImpl._EACI_CSIC_AA_3_1_CALL));
	}
	@Test(groups = { "functional.encode","functional.decode","parameter"})
	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterException {
		UserTeleserviceInformationImpl bci = new UserTeleserviceInformationImpl(getBody(UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP, UserTeleserviceInformationImpl._INTERPRETATION_FHGCI,
				UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC, UserTeleserviceInformationImpl._HLCI_IVTI));

		String[] methodNames = { "getPresentationMethod", "getInterpretation", "getCodingStandard", "getHighLayerCharIdentification" };
		Object[] expectedValues = { UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP, UserTeleserviceInformationImpl._INTERPRETATION_FHGCI, UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
				UserTeleserviceInformationImpl._HLCI_IVTI };
		super.testValues(bci, methodNames, expectedValues);
	}
	@Test(groups = { "functional.encode","functional.decode","parameter"})
	public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterException {
		UserTeleserviceInformationImpl bci = new UserTeleserviceInformationImpl(getBody(UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP, UserTeleserviceInformationImpl._INTERPRETATION_FHGCI,
				UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC, UserTeleserviceInformationImpl._HLCI_AUDIO_VID_LOW_RANGE, UserTeleserviceInformationImpl._EACI_CSIC_AA_3_1_CALL));

		String[] methodNames = { "getPresentationMethod", "getInterpretation", "getCodingStandard", "getHighLayerCharIdentification", "getEVidedoTelephonyCharIdentification" };
		Object[] expectedValues = { UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP, UserTeleserviceInformationImpl._INTERPRETATION_FHGCI, UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
				UserTeleserviceInformationImpl._HLCI_AUDIO_VID_LOW_RANGE, UserTeleserviceInformationImpl._EACI_CSIC_AA_3_1_CALL };
		super.testValues(bci, methodNames, expectedValues);
	}

	private byte[] getBody(int _PRESENTATION_METHOD, int _INTERPRETATION, int _CODING_STANDARD, int _HLCI) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bos.write(0x80 | (_CODING_STANDARD << 5) | (_INTERPRETATION << 2) | _PRESENTATION_METHOD);
		bos.write(0x80 | _HLCI);
		return bos.toByteArray();
	}

	private byte[] getBody(int _PRESENTATION_METHOD, int _INTERPRETATION, int _CODING_STANDARD, int _HLCI, int _EACI) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bos.write(0x80 | (_CODING_STANDARD << 5) | (_INTERPRETATION << 2) | _PRESENTATION_METHOD);
		bos.write(_HLCI);
		bos.write(0x80 | _EACI);
		return bos.toByteArray();
	}

	
	public AbstractISUPParameter getTestedComponent() {
		return new UserTeleserviceInformationImpl(1, 1, 1, 1);
	}

}
