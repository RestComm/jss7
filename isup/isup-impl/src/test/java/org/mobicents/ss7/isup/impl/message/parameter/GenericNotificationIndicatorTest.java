/**
 * Start time:11:34:01 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.IOException;
import java.util.Arrays;

import org.mobicents.ss7.isup.ISUPComponent;
import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.impl.message.parameter.EchoControlInformationImpl;
import org.mobicents.ss7.isup.impl.message.parameter.EventInformationImpl;
import org.mobicents.ss7.isup.impl.message.parameter.GenericNotificationIndicatorImpl;

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

	public void testBody1EncodedValues() throws IOException, ParameterRangeInvalidException {
		GenericNotificationIndicatorImpl eci = new GenericNotificationIndicatorImpl(getBody());
		byte[] body = getBody();
		byte[] encoded = eci.encodeElement();
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
	@Override
	public ISUPComponent getTestedComponent() throws ParameterRangeInvalidException {
		return new GenericNotificationIndicatorImpl(new byte[2]);
	}

}
