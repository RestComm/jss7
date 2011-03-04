/**
 * Start time:09:09:20 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.PerformingPivotIndicator;

/**
 * Start time:09:09:20 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class PerformingPivotIndicatorImpl extends AbstractISUPParameter implements PerformingPivotIndicator {

	// FIXME: Oleg?
	// 3.94.3

	public int decode(byte[] b) throws ParameterException {
		// TODO Auto-generated method stub
		return 0;
	}

	public PerformingPivotIndicatorImpl() {
		super();
		
	}

	public byte[] encode() throws ParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
