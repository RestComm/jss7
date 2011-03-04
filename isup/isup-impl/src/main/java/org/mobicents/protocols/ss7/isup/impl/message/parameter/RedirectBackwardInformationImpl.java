/**
 * Start time:22:05:48 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectBackwardInformation;

/**
 * Start time:22:05:48 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * 
 */
public class RedirectBackwardInformationImpl extends AbstractISUPParameter implements RedirectBackwardInformation {

	// FIXME: add impl

	public RedirectBackwardInformationImpl() {
		super();

	}

	public RedirectBackwardInformationImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public int decode(byte[] b) throws ParameterException {

		return 0;
	}

	public byte[] encode() throws ParameterException {
		return null;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
