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
import org.mobicents.ss7.isup.impl.message.parameter.OriginatingParticipatingServiceProviderImpl;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class OriginatingParticipatingServiceProviderTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public OriginatingParticipatingServiceProviderTest() throws IOException {
		
		super.badBodies.add(getBody3());
		super.badBodies.add(getBody4());
		super.badBodies.add(getBody5());

		super.goodBodies.add(new byte[1]);
		super.goodBodies.add(getBody1());
	
	}

	private byte[] getBody1() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB

		bos.write(3);
		bos.write(super.getSixDigits());
		return bos.toByteArray();
	}

	

	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		OriginatingParticipatingServiceProviderImpl bci = new OriginatingParticipatingServiceProviderImpl(getBody1());
	
		String[] methodNames = { "isOddFlag", "getAddress", "getOpspLengthIndicator" };
		Object[] expectedValues = {  false, super.getSixDigitsString(),3 };
		super.testValues(bci, methodNames, expectedValues);
	}


	
	private byte[] getBody2() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB

		bos.write(3 | 0x80);
		bos.write(super.getFiveDigits());
		return bos.toByteArray();
	}

	

	public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		OriginatingParticipatingServiceProviderImpl bci = new OriginatingParticipatingServiceProviderImpl(getBody2());
	
		String[] methodNames = { "isOddFlag", "getAddress", "getOpspLengthIndicator" };
		Object[] expectedValues = {  true, super.getFiveDigitsString(),3 };
		super.testValues(bci, methodNames, expectedValues);
	}

	
	
	
	private byte[] getBody3() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB

		bos.write(5 | 0x80);
		bos.write(super.getFiveDigits());
		return bos.toByteArray();
	}
	private byte[] getBody4() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB

		bos.write(2 );
		bos.write(super.getSixDigits());
		return bos.toByteArray();
	}
	private byte[] getBody5() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB

		bos.write(3 );
		bos.write(new byte[]{1,2,3,4,5});
		return bos.toByteArray();
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
		return new OriginatingParticipatingServiceProviderImpl("1234");
	}

}
