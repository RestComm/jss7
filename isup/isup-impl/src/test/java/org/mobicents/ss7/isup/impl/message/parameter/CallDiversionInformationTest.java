/**
 * Start time:13:37:14 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import org.mobicents.ss7.isup.ISUPComponent;
import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.impl.message.parameter.CallDiversionInformationImpl;

/**
 * Start time:13:37:14 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>ca </a>
 */
public class CallDiversionInformationTest extends ParameterHarness {

	public CallDiversionInformationTest() {
		super();
		// TODO Auto-generated constructor stub
		super.badBodies.add(new byte[0]);
		super.badBodies.add(new byte[2]);

		super.goodBodies.add(getBody1());
		
	}
	private byte[] getBody1() {
		//Notif sub options : 010 - presentation allowed
		//redirect reason   : 0100 - deflection during alerting
		//whole : 00100010
		return new byte[]{0x22};
	}
	
	
	public void testBody1EncodedValues() throws ParameterRangeInvalidException
	{
		CallDiversionInformationImpl cdi = new CallDiversionInformationImpl(getBody1());
		String[] methodNames = { "getNotificationSubscriptionOptions", "getRedirectingReason"};
		Object[] expectedValues = { cdi._NSO_P_A_WITH_RN, cdi._REDIRECTING_REASON_DDA};
		super.testValues(cdi, methodNames, expectedValues);
	}
	/* (non-Javadoc)
	 * @see org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent()
	 */
	@Override
	public ISUPComponent getTestedComponent() throws ParameterRangeInvalidException {
		return new CallDiversionInformationImpl(new byte[1]);
	}

}
