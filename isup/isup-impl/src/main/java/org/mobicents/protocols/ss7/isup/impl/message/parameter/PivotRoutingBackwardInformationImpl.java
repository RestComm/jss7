/**
 * Start time:16:16:18 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingBackwardInformation;

/**
 * Start time:16:16:18 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class PivotRoutingBackwardInformationImpl extends AbstractISUPParameter implements PivotRoutingBackwardInformation {
	// FIXME: impl

	public PivotRoutingBackwardInformationImpl(byte[] pivotRoutingIndicators) throws ParameterException {
		super();
		decode(pivotRoutingIndicators);
	}

	public PivotRoutingBackwardInformationImpl() {
		super();
		
	}

	public byte[] encode() throws ParameterException {

		return null;
	}

	public int decode(byte[] b) throws ParameterException {

		return 0;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}

}
