/**
 * Start time:14:11:03 2009-04-23<br>
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
import org.mobicents.ss7.isup.impl.message.parameter.CalledDirectoryNumberImpl;
import org.mobicents.ss7.isup.impl.message.parameter.CalledINNumberImpl;
import org.mobicents.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.mobicents.ss7.isup.message.parameter.GenericNumber;

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

		super.goodBodies.add(getBody(GenericNumberImpl._NQIA_CONNECTED_NUMBER, false, GenericNumber._NAI_NATIONAL_SN, GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN,
				GenericNumberImpl._APRI_NOT_AVAILABLE, GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED, getSixDigits()));

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

	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		GenericNumberImpl bci = new GenericNumberImpl(getBody(GenericNumberImpl._NQIA_CONNECTED_NUMBER, false, GenericNumber._NAI_NATIONAL_SN, GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN,
				GenericNumberImpl._APRI_NOT_AVAILABLE, GenericNumberImpl._SI_USER_PROVIDED_VERIFIED_FAILED, getSixDigits()));

		String[] methodNames = { "getNumberQualifierIndicator", "getNatureOfAddressIndicator", "isNumberIncomplete", "getNumberingPlanIndicator", "getAddressRepresentationRestrictedIndicator",
				"getScreeningIndicator", "getAddress" };
		Object[] expectedValues = { GenericNumberImpl._NQIA_CONNECTED_NUMBER, GenericNumber._NAI_NATIONAL_SN, GenericNumberImpl._NI_COMPLETE, GenericNumberImpl._NPI_ISDN, GenericNumberImpl._APRI_NOT_AVAILABLE,
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
	@Override
	public ISUPComponent getTestedComponent() {
		return new GenericNumberImpl(0, "1", 1, 1, 1, false, 1);
	}

}
