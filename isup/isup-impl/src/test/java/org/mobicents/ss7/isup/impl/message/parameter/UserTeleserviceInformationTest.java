/**
 * Start time:11:36:27 2009-04-27<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.mobicents.ss7.isup.ISUPComponent;
import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.impl.message.parameter.TransitNetworkSelectionImpl;
import org.mobicents.ss7.isup.impl.message.parameter.UserTeleserviceInformationImpl;

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

	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		UserTeleserviceInformationImpl bci = new UserTeleserviceInformationImpl(getBody(UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP, UserTeleserviceInformationImpl._INTERPRETATION_FHGCI,
				UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC, UserTeleserviceInformationImpl._HLCI_IVTI));

		String[] methodNames = { "getPresentationMethod", "getInterpretation", "getCodingStandard", "getHighLayerCharIdentification" };
		Object[] expectedValues = { UserTeleserviceInformationImpl._PRESENTATION_METHOD_HLPP, UserTeleserviceInformationImpl._INTERPRETATION_FHGCI, UserTeleserviceInformationImpl._CODING_STANDARD_ISO_IEC,
				UserTeleserviceInformationImpl._HLCI_IVTI };
		super.testValues(bci, methodNames, expectedValues);
	}

	public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
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

	@Override
	public ISUPComponent getTestedComponent() {
		return new UserTeleserviceInformationImpl(1, 1, 1, 1);
	}

}
