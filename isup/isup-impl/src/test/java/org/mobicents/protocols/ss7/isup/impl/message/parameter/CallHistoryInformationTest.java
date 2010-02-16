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
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CallHistoryInformationImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledDirectoryNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledINNumberImpl;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class CallHistoryInformationTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public CallHistoryInformationTest() throws IOException {
		super.badBodies.add(new byte[1]);
		super.badBodies.add(new byte[3]);

		super.goodBodies.add(getBody1());
		
	}

	private byte[] getBody1() throws IOException {
		
		// we will use odd number of digits, so we leave zero as MSB
		byte[] body = new byte[2];
		body[0]=12;
		body[1]=120;
		return body;
	}

	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		CallHistoryInformationImpl bci = new CallHistoryInformationImpl(getBody1());
	
		String[] methodNames = { "getCallHistory"};
		Object[] expectedValues = { ((12<<8 )| 120	) & 0xFFFF};
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
		return new CallHistoryInformationImpl(new byte[2]);
	}

}
