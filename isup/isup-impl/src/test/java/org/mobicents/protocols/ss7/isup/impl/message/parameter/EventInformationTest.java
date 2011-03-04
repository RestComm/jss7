/**
 * Start time:11:34:01 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import org.mobicents.protocols.ss7.isup.ParameterException;

/**
 * Start time:11:34:01 2009-04-24<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class EventInformationTest extends ParameterHarness {

	public EventInformationTest() {
		super();
		super.goodBodies.add(new byte[] { 67 });
		super.badBodies.add(new byte[2]);
	}

	private byte[] getBody(int _EI, boolean _RI) {
		byte[] b = new byte[1];
		int v = _EI;
		if (_RI)
			v |= 0x01 << 7;

		b[0] = (byte) v;

		return b;
	}

	public void testBody1EncodedValues() throws ParameterException {
		EventInformationImpl eci = new EventInformationImpl(getBody(EventInformationImpl._EVENT_INDICATOR_CFONNR, EventInformationImpl._EVENT_PRESENTATION_IPR));

		String[] methodNames = { "getEventIndicator", "isEventPresentationRestrictedIndicator" };
		Object[] expectedValues = { EventInformationImpl._EVENT_INDICATOR_CFONNR, EventInformationImpl._EVENT_PRESENTATION_IPR };
		super.testValues(eci, methodNames, expectedValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.isup.messages.parameters.ParameterHarness#getTestedComponent
	 * ()
	 */
	
	public AbstractISUPParameter getTestedComponent() throws ParameterException {
		return new EventInformationImpl(new byte[1]);
	}

}
