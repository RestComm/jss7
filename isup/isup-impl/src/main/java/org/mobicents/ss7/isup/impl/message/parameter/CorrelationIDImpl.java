/**
 * Start time:12:48:19 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.CorrelationID;

/**
 * Start time:12:48:19 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * 
 */
public class CorrelationIDImpl extends GenericDigitsImpl implements CorrelationID {

	public CorrelationIDImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CorrelationIDImpl(byte[] b) throws ParameterRangeInvalidException {
		super(b);

		// TODO Auto-generated constructor stub
	}

	public CorrelationIDImpl(int encodignScheme, int typeOfDigits, int[] digits) {
		super(encodignScheme, typeOfDigits, digits);
		// TODO Auto-generated constructor stub
	}

	// FIXME: Q.1218 -- weird document.... Oleg is this correct? or should it be
	// mix of GenericNumber and Generic digits?
	public int getCode() {

		return CorrelationID._PARAMETER_CODE;
	}
}
