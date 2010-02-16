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
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledDirectoryNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.CalledINNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.ConnectedNumberImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.ConnectionRequestImpl;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class ConnectionRequestTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public ConnectionRequestTest() throws IOException {
		super.badBodies.add(new byte[1]);

		super.goodBodies.add(getBody1());
		// This will fail, cause this body has APRI allowed, so hardcoded body
		// does nto match encoded body :)
		// super.goodBodies.add(getBody2());
	}

	private byte[] getBody1() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB
		//Local reference
		bos.write(12);
		bos.write(120);
		bos.write(38);
		
		//Signaling point code
		bos.write(120);
		bos.write(45);
		//protocol class
		bos.write(120);
		//credit
		bos.write(69);
		return bos.toByteArray();
	}
	
	private byte[] getBody2() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		//Local reference
		bos.write(12);
		bos.write(120);
		bos.write(38);
		
		//Signaling point code
		bos.write(120);
		bos.write(12);
		return bos.toByteArray();
	}

	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		ConnectionRequestImpl bci = new ConnectionRequestImpl(getBody1());
		int localRef = 12;
		localRef|=120<<8;
		localRef|=38<<16;
		int signalingPointCode = 120;
		signalingPointCode |= 45<<8;
		
		int protocolClass = 120;
		int credit = 69;
		String[] methodNames = { "getLocalReference","getSignalingPointCode","getProtocolClass","getCredit"};
		Object[] expectedValues = { localRef,signalingPointCode,protocolClass,credit};
		super.testValues(bci, methodNames, expectedValues);
	}

	public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		ConnectionRequestImpl bci = new ConnectionRequestImpl(getBody2());

		int localRef = 12;
		localRef|=120<<8;
		localRef|=38<<16;
		int signalingPointCode = 120;
		signalingPointCode |= 12<<8;
		
		String[] methodNames = { "getLocalReference","getSignalingPointCode"};
		Object[] expectedValues = { localRef,signalingPointCode};
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
		return new ConnectionRequestImpl(new byte[5]);
	}

}
