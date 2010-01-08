/**
 * Start time:15:44:56 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.ss7.isup.ParameterRangeInvalidException;
import org.mobicents.ss7.isup.message.parameter.CCNRPossibleIndicator;

/**
 * Start time:15:44:56 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 */
public class CCNRPossibleIndicatorImpl extends AbstractParameter implements CCNRPossibleIndicator{

	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;

	
	

	private boolean ccnrPossible = false;

	public CCNRPossibleIndicatorImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CCNRPossibleIndicatorImpl(boolean ccnrPossible) {
		super();
		this.ccnrPossible = ccnrPossible;
	}

	public CCNRPossibleIndicatorImpl(byte[] b) throws ParameterRangeInvalidException {
		super();
		decodeElement(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#decodeElement(byte[])
	 */
	public int decodeElement(byte[] b) throws ParameterRangeInvalidException {
		if (b == null || b.length == 0) {
			throw new ParameterRangeInvalidException("byte[] must not be null and length must be 1");
		}

		this.ccnrPossible = (b[0] & 0x01) == _TURN_ON;

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.isup.ISUPComponent#encodeElement()
	 */
	public byte[] encodeElement() throws IOException {
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
