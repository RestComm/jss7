/**
 * Start time:15:44:56 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.CCNRPossibleIndicator;

/**
 * Start time:15:44:56 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class CCNRPossibleIndicatorImpl extends AbstractISUPParameter implements CCNRPossibleIndicator{

	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;

	
	

	private boolean ccnrPossible = false;

	public CCNRPossibleIndicatorImpl() {
		super();
		
	}

	public CCNRPossibleIndicatorImpl(boolean ccnrPossible) {
		super();
		this.ccnrPossible = ccnrPossible;
	}

	public CCNRPossibleIndicatorImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length == 0) {
			throw new ParameterException("byte[] must not be null and length must be 1");
		}

		this.ccnrPossible = (b[0] & 0x01) == _TURN_ON;

		return 1;
	}

	public byte[] encode() throws ParameterException {
		return new byte[] { (byte) (this.ccnrPossible ? _TURN_ON : _TURN_OFF) };
	}

	public boolean isCcnrPossible() {
		return ccnrPossible;
	}

	public void setCcnrPossible(boolean ccnrPossible) {
		this.ccnrPossible = ccnrPossible;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
