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
import org.mobicents.ss7.isup.impl.message.parameter.HTRInformationImpl;
import org.mobicents.ss7.isup.message.parameter.HTRInformation;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class HTRInformationTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public HTRInformationTest() throws IOException {
		super.badBodies.add(new byte[1]);

		super.goodBodies.add(getBody(true,HTRInformation._NAI_NATIONAL_SN,HTRInformationImpl._NPI_ISDN, getFiveDigits()));

	}

	private byte[] getBody( boolean isODD, int _NAI, int _NPI, byte[] digits) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB

		int nai = _NAI;
		if (isODD)
			nai |= 0x01 << 7;
		int bit3 = 0;
	
		bit3 |= _NPI << 4;
		
		bos.write(nai);
		bos.write(bit3);
		bos.write(digits);
		return bos.toByteArray();
	}

	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		HTRInformationImpl bci = new HTRInformationImpl(getBody(false,HTRInformation._NAI_NATIONAL_SN,HTRInformationImpl._NPI_ISDN, getSixDigits()));

		String[] methodNames = { "isOddFlag", "getNatureOfAddressIndicator", "getNumberingPlanIndicator", "getAddress" };
		Object[] expectedValues = { false,HTRInformation._NAI_NATIONAL_SN,HTRInformationImpl._NPI_ISDN, getSixDigitsString()};
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
	public ISUPComponent getTestedComponent() throws ParameterRangeInvalidException {
		return new HTRInformationImpl(new byte[3]);
	}

}
