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
import org.mobicents.protocols.ss7.isup.message.parameter.CalledPartyNumber;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class CalledPartyNumberTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public CalledPartyNumberTest() throws IOException {
		super.badBodies.add(new byte[1]);
		

		super.goodBodies.add(getBody1());
		super.goodBodies.add(getBody2());
	}

	private byte[] getBody1() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB

		bos.write(CalledPartyNumber._NAI_SUBSCRIBER_NUMBER);
		int v = CalledPartyNumberImpl._INN_ROUTING_ALLOWED << 7;
		v |= CalledPartyNumberImpl._NPI_ISDN << 4;
		bos.write(v);
		bos.write(super.getSixDigits());
		return bos.toByteArray();
	}

	private byte[] getBody2() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		bos.write(CalledPartyNumber._NAI_SUBSCRIBER_NUMBER | (0x01 << 7));
		int v = CalledPartyNumberImpl._INN_ROUTING_NOT_ALLOWED << 7;
		v |= CalledPartyNumberImpl._NPI_ISDN << 4;
		bos.write(v);
		bos.write(super.getFiveDigits());
		return bos.toByteArray();
	}

	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		CalledPartyNumberImpl bci = new CalledPartyNumberImpl(getBody1());
	
		String[] methodNames = { "getNumberingPlanIndicator", "getInternalNetworkNumberIndicator", "getNatureOfAddressIndicator", "isOddFlag", "getAddress" };
		Object[] expectedValues = { CalledPartyNumberImpl._NPI_ISDN, CalledPartyNumberImpl._INN_ROUTING_ALLOWED, CalledPartyNumber._NAI_SUBSCRIBER_NUMBER, false, super.getSixDigitsString() };
		super.testValues(bci, methodNames, expectedValues);
	}

	public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		CalledPartyNumberImpl bci = new CalledPartyNumberImpl(getBody2());
	
		String[] methodNames = { "getNumberingPlanIndicator", "getInternalNetworkNumberIndicator", "getNatureOfAddressIndicator", "isOddFlag", "getAddress" };
		Object[] expectedValues = { CalledPartyNumberImpl._NPI_ISDN, CalledPartyNumberImpl._INN_ROUTING_NOT_ALLOWED, CalledPartyNumber._NAI_SUBSCRIBER_NUMBER, true, super.getFiveDigitsString() };
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
	public ISUPComponent getTestedComponent() {
		return new CalledPartyNumberImpl(0, "1", 1, 1);
	}

}
