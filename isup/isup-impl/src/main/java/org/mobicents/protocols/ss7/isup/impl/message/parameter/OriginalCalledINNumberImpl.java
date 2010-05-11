/**
 * Start time:18:36:26 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayInputStream;

import org.mobicents.protocols.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.protocols.ss7.isup.message.parameter.OriginalCalledINNumber;

/**
 * Start time:18:36:26 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class OriginalCalledINNumberImpl extends CalledNumberImpl implements OriginalCalledINNumber {

	public OriginalCalledINNumberImpl(byte[] representation) throws ParameterRangeInvalidException {
		super(representation);
		
	}

	public OriginalCalledINNumberImpl(ByteArrayInputStream bis) throws ParameterRangeInvalidException {
		super(bis);
		
	}

	public OriginalCalledINNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator, int addressRepresentationREstrictedIndicator) {
		super(natureOfAddresIndicator, address, numberingPlanIndicator, addressRepresentationREstrictedIndicator);
		
	}

	public OriginalCalledINNumberImpl() {
		super();
		
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
