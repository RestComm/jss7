/**
 * Start time:11:34:01 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import org.mobicents.ss7.isup.ISUPComponent;
import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.impl.message.parameter.EchoControlInformationImpl;
import org.mobicents.ss7.isup.impl.message.parameter.MCIDRequestIndicatorsImpl;

/**
 * Start time:11:34:01 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class MCIDRequestIndicatorsTest extends ParameterHarness {
	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;

	public MCIDRequestIndicatorsTest() {
		super();
		super.goodBodies.add(new byte[] { 3 });
		super.badBodies.add(new byte[2]);
	}

	private byte[] getBody(boolean mcidRequest, boolean holdingRequested) {
		int b0 = 0;

		b0 |= (mcidRequest ? _TURN_ON : _TURN_OFF);
		b0 |= ((holdingRequested ? _TURN_ON : _TURN_OFF)) << 1;

		return new byte[] { (byte) b0 };
	}

	public void testBody1EncodedValues() throws ParameterRangeInvalidException {
		MCIDRequestIndicatorsImpl eci = new MCIDRequestIndicatorsImpl(getBody(MCIDRequestIndicatorsImpl._INDICATOR_REQUESTED, MCIDRequestIndicatorsImpl._INDICATOR_REQUESTED));

		String[] methodNames = { "isMcidRequestIndicator", "isHoldingIndicator" };
		Object[] expectedValues = { MCIDRequestIndicatorsImpl._INDICATOR_REQUESTED, MCIDRequestIndicatorsImpl._INDICATOR_REQUESTED };
		super.testValues(eci, methodNames, expectedValues);
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
		return new MCIDRequestIndicatorsImpl(new byte[1]);
	}

}
