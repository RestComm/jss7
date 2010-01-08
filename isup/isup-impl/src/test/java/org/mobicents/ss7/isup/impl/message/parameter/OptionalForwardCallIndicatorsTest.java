/**
 * Start time:16:20:47 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.mobicents.ss7.isup.ISUPComponent;
import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.impl.message.parameter.NetworkRoutingNumberImpl;
import org.mobicents.ss7.isup.impl.message.parameter.OptionalBackwardCallIndicatorsImpl;
import org.mobicents.ss7.isup.impl.message.parameter.OptionalForwardCallIndicatorsImpl;

/**
 * Start time:16:20:47 2009-04-26<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class OptionalForwardCallIndicatorsTest extends ParameterHarness {

	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;
	
	public OptionalForwardCallIndicatorsTest() {
		super();
		super.goodBodies.add(new byte[] { 7 });
		super.badBodies.add(new byte[] { 8, 8 });
	}

	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		OptionalForwardCallIndicatorsImpl bci = new OptionalForwardCallIndicatorsImpl(getBody(OptionalForwardCallIndicatorsImpl._CUGCI_CUG_CALL_OAL,
																					  OptionalForwardCallIndicatorsImpl._SSI_ADDITIONAL_INFO,
																					  OptionalForwardCallIndicatorsImpl._CLIRI_REQUESTED));

		String[] methodNames = {    "getClosedUserGroupCallIndicator", 
				                    "isSimpleSegmentationIndicator", 
				                    "isConnectedLineIdentityRequestIndicator"};
		Object[] expectedValues = { OptionalForwardCallIndicatorsImpl._CUGCI_CUG_CALL_OAL,
				  					OptionalForwardCallIndicatorsImpl._SSI_ADDITIONAL_INFO,
				  					OptionalForwardCallIndicatorsImpl._CLIRI_REQUESTED };
		super.testValues(bci, methodNames, expectedValues);
	}

	private byte[] getBody(int i, boolean _SSI, boolean _CLIRI){
		
		
		byte v = (byte) i;
		v |= ((_SSI ? _TURN_ON : _TURN_OFF) << 2);
		v |= ((_CLIRI ? _TURN_ON : _TURN_OFF) << 7);
		return new byte[] { (byte) v };
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
		return new OptionalForwardCallIndicatorsImpl(new byte[1]);
	}

}
