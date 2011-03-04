/**
 * Start time:08:57:49 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingForwardInformation;

/**
 * Start time:08:57:49 2009-04-06<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class PivotRoutingForwardInformationImpl extends AbstractISUPParameter implements PivotRoutingForwardInformation {

	// FIXME: Addd this after those polygon shapes. 3.94

	public int decode(byte[] b) throws ParameterException {
		// TODO Auto-generated method stub
		return 0;
	}

	public PivotRoutingForwardInformationImpl() {
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
