/**
 * Start time:16:59:42 2009-04-03<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.SuspendResumeIndicators;

/**
 * Start time:16:59:42 2009-04-03<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class SuspendResumeIndicatorsImpl extends AbstractISUPParameter implements SuspendResumeIndicators {

	private static final int _TURN_ON = 1;
	private static final int _TURN_OFF = 0;

	private boolean suspendResumeIndicator;

	public SuspendResumeIndicatorsImpl() {
		super();
		
	}

	public SuspendResumeIndicatorsImpl(byte[] b) throws ParameterException {
		super();
		decode(b);
	}

	public SuspendResumeIndicatorsImpl(boolean suspendResumeIndicator) {
		super();
		this.suspendResumeIndicator = suspendResumeIndicator;
	}

	public int decode(byte[] b) throws ParameterException {
		if (b == null || b.length != 1) {
			throw new ParameterException("byte[] must  not be null and length must  be 1");
		}

		this.suspendResumeIndicator = (b[0] & 0x01) == _TURN_ON;

		return 1;
	}

	public byte[] encode() throws ParameterException {
		return new byte[] { (byte) (this.suspendResumeIndicator ? _TURN_ON : _TURN_OFF) };
	}

	public boolean isSuspendResumeIndicator() {
		return suspendResumeIndicator;
	}

	public void setSuspendResumeIndicator(boolean suspendResumeIndicator) {
		this.suspendResumeIndicator = suspendResumeIndicator;
	}

	public int getCode() {

		return _PARAMETER_CODE;
	}
}
