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
import org.mobicents.protocols.ss7.isup.message.parameter.LocationNumber;

/**
 * Start time:14:11:03 2009-04-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski
 *         </a>
 */
public class LocationNumberTest extends ParameterHarness {

	/**
	 * @throws IOException
	 */
	public LocationNumberTest() throws IOException {
		super.badBodies.add(new byte[1]);

		super.goodBodies.add(getBody( false, LocationNumber._NAI_NATIONAL_SN, LocationNumberImpl._INN_ROUTING_ALLOWED, LocationNumberImpl._NPI_ISDN,
				LocationNumberImpl._APRI_NOT_AVAILABLE, LocationNumberImpl._SI_NETWORK_PROVIDED, getSixDigits()));

	}

	private byte[] getBody(boolean isODD, int _NAI, int _INN, int _NPI, int _APR, int _SI, byte[] digits) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// we will use odd number of digits, so we leave zero as MSB

		int nai = _NAI;
		if (isODD)
			nai |= 0x01 << 7;
		int bit3 = _SI;
		bit3 |= _APR << 2;
		bit3 |= _NPI << 4;
		bit3 |= _INN << 7;


		bos.write(nai);
		bos.write(bit3);
		bos.write(digits);
		return bos.toByteArray();
	}

	public void testBody1EncodedValues() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, ParameterRangeInvalidException {
		LocationNumberImpl bci = new LocationNumberImpl(getBody( false, LocationNumber._NAI_NATIONAL_SN, LocationNumberImpl._INN_ROUTING_ALLOWED, LocationNumberImpl._NPI_ISDN,
				LocationNumberImpl._APRI_NOT_AVAILABLE, LocationNumberImpl._SI_NETWORK_PROVIDED, getSixDigits()));

		String[] methodNames = { "isOddFlag", "getNatureOfAddressIndicator", "getInternalNetworkNumberIndicator", "getNumberingPlanIndicator", "getAddressRepresentationRestrictedIndicator",
				"getScreeningIndicator", "getAddress" };
		Object[] expectedValues = { false, LocationNumber._NAI_NATIONAL_SN, LocationNumberImpl._INN_ROUTING_ALLOWED, LocationNumberImpl._NPI_ISDN,
				LocationNumberImpl._APRI_NOT_AVAILABLE, LocationNumberImpl._SI_NETWORK_PROVIDED, getSixDigitsString() };
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
		return new LocationNumberImpl(1,"1",1,1,1,1);
	}

}
