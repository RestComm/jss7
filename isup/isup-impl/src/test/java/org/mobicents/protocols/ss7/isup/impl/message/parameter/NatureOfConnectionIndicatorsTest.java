/**
 * Start time:13:20:04 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ISUPComponent;
import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.MLPPPrecedenceImpl;
import org.mobicents.protocols.ss7.isup.impl.message.parameter.NatureOfConnectionIndicatorsImpl;

/**
 * Start time:13:20:04 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class NatureOfConnectionIndicatorsTest extends ParameterHarness {

	public NatureOfConnectionIndicatorsTest() {
		super();
		super.badBodies.add(new byte[2]);
		super.goodBodies.add(new byte[1]);
		super.goodBodies.add(new byte[] { 0x0E });
	}

	public void testBody1EncodedValues() throws IOException, ParameterRangeInvalidException {

		NatureOfConnectionIndicatorsImpl eci = new NatureOfConnectionIndicatorsImpl(getBody(NatureOfConnectionIndicatorsImpl._SI_ONE_SATELLITE, NatureOfConnectionIndicatorsImpl._CCI_REQUIRED_ON_THIS_CIRCUIT,
				NatureOfConnectionIndicatorsImpl._ECDI_INCLUDED));

		String[] methodNames = { "getSatelliteIndicator", "getContinuityCheckIndicator", "isEchoControlDeviceIndicator" };
		Object[] expectedValues = { NatureOfConnectionIndicatorsImpl._SI_ONE_SATELLITE, NatureOfConnectionIndicatorsImpl._CCI_REQUIRED_ON_THIS_CIRCUIT, NatureOfConnectionIndicatorsImpl._ECDI_INCLUDED };

		super.testValues(eci, methodNames, expectedValues);
	}

	private byte[] getBody(int siOneSatellite, int cciRequiredOnThisCircuit, boolean ecdiIncluded) {
		
		
		byte b=  (byte) (siOneSatellite | (cciRequiredOnThisCircuit<<2) | (ecdiIncluded? (0x01<<4):(0x00 << 4)));
		
		return new byte[]{b};
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
		return new NatureOfConnectionIndicatorsImpl(new byte[1]);
	}

}
