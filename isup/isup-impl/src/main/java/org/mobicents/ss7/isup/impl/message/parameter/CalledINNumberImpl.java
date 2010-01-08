/**
 * Start time:13:06:26 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.CalledINNumber;

/**
 * Start time:13:06:26 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class CalledINNumberImpl extends CalledNumberImpl implements CalledINNumber{
	
	/**
	 * @param representation
	 */
	public CalledINNumberImpl(byte[] representation) throws ParameterRangeInvalidException{
		super(representation);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param bis
	 */
	public CalledINNumberImpl(ByteArrayInputStream bis) throws ParameterRangeInvalidException {
		super(bis);
		// TODO Auto-generated constructor stub
	}
	public CalledINNumberImpl() throws ParameterRangeInvalidException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param natureOfAddresIndicator
	 * @param address
	 * @param numberingPlanIndicator
	 * @param addressRepresentationREstrictedIndicator
	 */
	public CalledINNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator, int addressRepresentationREstrictedIndicator) {
		super(natureOfAddresIndicator, address, numberingPlanIndicator, addressRepresentationREstrictedIndicator);
		// TODO Auto-generated constructor stub
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
