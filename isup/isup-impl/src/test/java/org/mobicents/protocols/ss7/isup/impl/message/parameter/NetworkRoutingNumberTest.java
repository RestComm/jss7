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
import org.mobicents.protocols.ss7.isup.impl.message.parameter.NetworkRoutingNumberImpl;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class NetworkRoutingNumberTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public NetworkRoutingNumberTest() throws IOException {
		super.badBodies.add(new byte[1]);

		super.goodBodies.add(getBody(false,getSixDigits(), NetworkRoutingNumberImpl._NPI_ISDN_NP, NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF));
		super.goodBodies.add(getBody(true,getFiveDigits(), NetworkRoutingNumberImpl._NPI_ISDN_NP, NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF));
		// This will fail, cause this body has APRI allowed, so hardcoded body
		// does nto match encoded body :)
		// super.goodBodies.add(getBody2());
	}



	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		NetworkRoutingNumberImpl bci = new NetworkRoutingNumberImpl(getBody(false,getSixDigits(), NetworkRoutingNumberImpl._NPI_ISDN_NP, NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF));

		String[] methodNames = { "isOddFlag", "getNumberingPlanIndicator", "getNatureOfAddressIndicator",  "getAddress" };
		Object[] expectedValues = { false, NetworkRoutingNumberImpl._NPI_ISDN_NP, NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF,getSixDigitsString() };
		super.testValues(bci, methodNames, expectedValues);
	}
	
	public void testBody2EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		NetworkRoutingNumberImpl bci = new NetworkRoutingNumberImpl(getBody(true,getFiveDigits(), NetworkRoutingNumberImpl._NPI_ISDN_NP, NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF));

		String[] methodNames = { "isOddFlag", "getNumberingPlanIndicator", "getNatureOfAddressIndicator",  "getAddress" };
		Object[] expectedValues = { true, NetworkRoutingNumberImpl._NPI_ISDN_NP, NetworkRoutingNumberImpl._NAI_NRNI_NETWORK_SNF,getFiveDigitsString() };
		super.testValues(bci, methodNames, expectedValues);
	}

	private byte[] getBody(boolean isODD, byte[] digits, int npiIsdnNp, int naiNrniNetworkSnf) throws IOException {
		int b = 0;
		if(isODD)
		{
			b|=0x01<<7;
		}
		b|=npiIsdnNp<<4;
		b|=naiNrniNetworkSnf;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bos.write(b);
		bos.write(digits);
		return bos.toByteArray();
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
		return new NetworkRoutingNumberImpl("1", 1, 1);
	}

}
