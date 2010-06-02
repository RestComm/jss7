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

import org.mobicents.protocols.ss7.isup.ISUPComponent;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class GVNSUserGroupTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public GVNSUserGroupTest() throws IOException {

		super.goodBodies.add(getBody(getSixDigits(), false));

	}

	private byte[] getBody(byte[] digits, boolean isODD) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB
		int header = digits.length;
		if (isODD) {
			header |= 0x01 << 7;
		}
		bos.write(header);
		bos.write(digits);
		return bos.toByteArray();
	}

	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		GVNSUserGroupImpl bci = new GVNSUserGroupImpl(getBody(getSixDigits(), false));

		String[] methodNames = { "getAddress", "getGugLengthIndicator" };
		Object[] expectedValues = { getSixDigitsString(), 3 };
		super.testValues(bci, methodNames, expectedValues);
	}

	public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		GVNSUserGroupImpl bci = new GVNSUserGroupImpl(getBody(getFiveDigits(), true));

		String[] methodNames = { "getAddress", "getGugLengthIndicator" };
		Object[] expectedValues = { getFiveDigitsString(), 3 };
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
		return new GVNSUserGroupImpl("12");
	}

}
