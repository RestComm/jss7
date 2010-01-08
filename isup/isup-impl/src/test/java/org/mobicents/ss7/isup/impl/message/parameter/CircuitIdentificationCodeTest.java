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
import org.mobicents.ss7.isup.message.parameter.CalledINNumber;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class CircuitIdentificationCodeTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public CircuitIdentificationCodeTest() throws IOException {
		super.goodBodies.add(getBody1());
		super.goodBodies.add(getBody2());
		
		
		super.badBodies.add(new byte[1]);
		super.badBodies.add(new byte[3]);
	}

	private byte[] getBody1() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB

		
		return new byte[]{(byte) 0xFF,0x0F};
	}
	
	private byte[] getBody2() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB

		
		return new byte[]{(byte) 0xAB,0x0C};
	}

	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		CircuitIdentificationCodeImpl bci = new CircuitIdentificationCodeImpl();
		bci.decodeElement(getBody1());
		String[] methodNames = { "getCIC"};
		Object[] expectedValues = { (long)0xFFF };
		super.testValues(bci, methodNames, expectedValues);
	}

	public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		CircuitIdentificationCodeImpl bci = new CircuitIdentificationCodeImpl();
		bci.decodeElement(getBody2());

		String[] methodNames = { "getCIC"};
		Object[] expectedValues = { (long)0xCAB };
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
		return new CircuitIdentificationCodeImpl();
	}

}
