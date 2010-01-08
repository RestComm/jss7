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
import org.mobicents.ss7.isup.message.parameter.CalledDirectoryNumber;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class CalledDirectoryNumberTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public CalledDirectoryNumberTest() throws IOException {
		//super.badBodies.add(new byte[1]);
		

		//super.goodBodies.add(getBody1());
		//super.goodBodies.add(getBody2());
	}

	private byte[] getBody1() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB

		bos.write(CalledDirectoryNumber._NAI_SUBSCRIBER_NUMBER);
		int v = CalledDirectoryNumberImpl._INNI_RESERVED << 7;
		v |= CalledDirectoryNumberImpl._NPI_ISDN_NP << 4;
		bos.write(v);
		bos.write(super.getSixDigits());
		return bos.toByteArray();
	}

	private byte[] getBody2() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		bos.write(CalledDirectoryNumber._NAI_SUBSCRIBER_NUMBER | (0x01 << 7));
		int v = CalledDirectoryNumberImpl._INNI_RESERVED << 7;
		v |= CalledDirectoryNumberImpl._NPI_ISDN_NP << 4;
		bos.write(v);
		bos.write(super.getFiveDigits());
		return bos.toByteArray();
	}

	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		CalledDirectoryNumberImpl bci = new CalledDirectoryNumberImpl(getBody1());
		bci.getNumberingPlanIndicator();
		String[] methodNames = { "getNumberingPlanIndicator", "getInternalNetworkNumberIndicator", "getNatureOfAddressIndicator", "isOddFlag", "getAddress" };
		Object[] expectedValues = { CalledDirectoryNumberImpl._NPI_ISDN_NP, CalledDirectoryNumberImpl._INNI_RESERVED, CalledDirectoryNumber._NAI_SUBSCRIBER_NUMBER, false, super.getSixDigitsString() };
		super.testValues(bci, methodNames, expectedValues);
	}

	public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		CalledDirectoryNumberImpl bci = new CalledDirectoryNumberImpl(getBody2());

		String[] methodNames = { "getNumberingPlanIndicator", "getInternalNetworkNumberIndicator", "getNatureOfAddressIndicator", "isOddFlag", "getAddress" };
		Object[] expectedValues = { CalledDirectoryNumberImpl._NPI_ISDN_NP, CalledDirectoryNumberImpl._INNI_RESERVED, CalledDirectoryNumber._NAI_SUBSCRIBER_NUMBER, true, super.getFiveDigitsString() };
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
		return new CalledDirectoryNumberImpl(0, "1", 1, 1);
	}

}
