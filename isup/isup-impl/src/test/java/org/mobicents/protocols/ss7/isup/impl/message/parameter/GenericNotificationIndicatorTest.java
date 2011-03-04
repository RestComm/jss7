/**
 * Start time:11:34:01 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.ss7.isup.ParameterException;

/**
 * Start time:11:34:01 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class GenericNotificationIndicatorTest extends ParameterHarness {

	public GenericNotificationIndicatorTest() {
		super();
		super.goodBodies.add(new byte[] { 67, 12, 13, 14, 15, 16, 17, (byte) (18 | (0x01 << 7)) });
		super.badBodies.add(new byte[1]);
	}

	private byte[] getBody() {
		return super.goodBodies.get(0);
	}

	public void testBody1EncodedValues() throws IOException, ParameterException {
		GenericNotificationIndicatorImpl eci = new GenericNotificationIndicatorImpl(getBody());
		byte[] body = getBody();
		byte[] encoded = eci.encode();
		boolean equal = Arrays.equals(body, encoded);
		assertTrue("Body index: \n" + makeCompare(body, encoded), equal);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent
	 * ()
	 */
	
	public AbstractISUPParameter getTestedComponent() throws ParameterException {
		return new GenericNotificationIndicatorImpl(new byte[2]);
	}

}
